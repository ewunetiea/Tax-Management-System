package com.afr.fms.Admin.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Admin.Entity.MenuHeader;
import com.afr.fms.Admin.Entity.MenuHeaderItems;
import com.afr.fms.Admin.Entity.MenuItemPermissions;
import com.afr.fms.Admin.Entity.MenuItems;
import com.afr.fms.Admin.Entity.MenuRole;

@Mapper
public interface MenuMapper {

	// menu items

	@Select("select CAST(mi.created_date as Date) as created_date, CAST(mi.updated_date as Date) as updated_date, u.email as maker_email, mi.* from menu_items mi "

			+
			"inner join [user] u on u.id = mi.maker_id ")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "menuHeaders", javaType = List.class, column = "id", one = @One(select = "com.afr.fms.Admin.Mapper.MenuMapper.getMenuHeadersByItemID")),
			@Result(property = "permissions", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Common.Permission.Mapper.FunctionalitiesMapper.getAllFunctionalitiesByMenuItem"))

	})
	public List<MenuItems> getMenuItems();

	@Select("select mi.* from menu_header_items mhi inner join menu_items mi on mi.id = mhi.item_id where mhi.header_id = #{header_id} and mi.status = 1 order by mi.item_order")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public List<MenuItems> getMenuItemsByHeaderIDNew(Long header_id);

	@Select("select mh.* from menu_header_items mhi inner join menu_header mh on mh.id = mhi.header_id where mhi.item_id = #{item_id} and mh.status = 1 order by mh.header_order")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public List<MenuHeader> getMenuHeadersByItemID(Long item_id);

	@Select("select item_label, item_badge, item_to, item_icon from menu_items where header_id = #{header_id} and status = 1 order by item_order")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public List<MenuItems> getMenuItemsByHeaderIDDynamic(Long header_id);

	@Select("select mi.item_label, mi.item_badge, mi.item_to, mi.item_icon from menu_header_items mhi inner join menu_items mi on mi.id = mhi.item_id where mhi.header_id = #{header_id} and mi.status = 1 order by mi.item_order")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public List<MenuItems> getMenuItemsByHeaderIDDynamicNew(Long header_id);

	@Update("update menu_items set item_order = #{item_order},  item_label = #{item_label}, item_to= #{item_to} , item_icon = #{item_icon}, status=#{status}, item_badge = #{item_badge}, updated_date = CURRENT_TIMESTAMP where id=#{id}")
	public void updateMenuItem(MenuItems menuItems);

	@Update("update menu_items set status = #{status}, maker_id = #{maker_id} where id=#{id}")
	public void manageMenuItemsStatus(MenuItems menuItems);

	@Delete("delete from menu_items where id=#{id}")
	public void deleteMenuItem(MenuItems menuItems);

	@Select("insert into menu_items(item_label, item_to, item_order, item_icon, item_badge, status, created_date, maker_id) OUTPUT inserted.id values (#{item_label}, #{item_to}, #{item_order}, #{item_icon}, #{item_badge}, #{status}, CURRENT_TIMESTAMP, #{maker_id})")
	public Long createMenuItem(MenuItems menuItems);

	// menu header
	@Select("select * from menu_header where id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public MenuHeader getMenuHeaderByID(Long id);

	@Select("select * from menu_role where role_id = #{role_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public MenuHeader getMenuHeaderByRoleID(Long role_id);

	@Select("select * from menu_header")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public List<MenuHeader> getCommonMenuHeaders();

	@Select("select CAST(mh.created_date as Date) as created_date, CAST(mh.updated_date as Date) as updated_date, u.email as maker_email, mh.* from menu_header mh "
			+
			"inner join [user] u on u.id = mh.maker_id")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "menuItems", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.MenuMapper.getMenuItemsByHeaderIDNew")),
			@Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRoleByHeaderID")),
	})
	public List<MenuHeader> getMenuHeaders();

	@Update("update menu_header set super_menu = #{super_menu}, header = #{header}, target_roles = #{target_roles}, icon = #{icon}, url = #{url}, badge = #{badge}, header_order= #{header_order}, command= #{command}, template= #{template}, target = #{target}, status=#{status}, updated_date = CURRENT_TIMESTAMP where id=#{id}")
	public void updateMenuHeader(MenuHeader menuHeader);

	@Update("update menu_header set  target_roles = #{target_roles}, updated_date = CURRENT_TIMESTAMP where id=#{id}")
	public void updateMenuHeaderTargetRoles(MenuHeader menuHeader);

	@Update("update menu_header set status = #{status}, maker_id = #{maker_id} where id=#{id}")
	public void manageMenuHeaderStatus(MenuHeader menuHeader);

	@Delete("delete from menu_header where id=#{id}")
	public void deleteMenuHeader(MenuHeader menuHeader);

	@Delete("delete from menu_header_items where header_id=#{header_id}")
	public void deleteMenuHeaderItems(Long header_id);

	@Delete("delete from menu_header_items where item_id=#{item_id}")
	public void deleteMenuHeaderItemsByItem(Long item_id);

	@Select("insert into menu_header(super_menu, header, target_roles, status, created_date, maker_id, target, header_order, icon, url, badge, command, template) OUTPUT inserted.id values (#{super_menu}, #{header}, #{target_roles}, #{status}, CURRENT_TIMESTAMP, #{maker_id}, #{target}, #{header_order}, #{icon}, #{url}, #{badge}, #{command}, #{template})")
	public Long createMenuheader(MenuHeader menuHeader);

	@Delete("delete from menu_role where header_id = #{header_id}")
	public void deleteMenuRoleByHeaderID(Long header_id);

	@Delete("delete from menu_permission where item_id = #{item_id}")
	public void deleteMenuPermissionByItemID(Long item_id);

	@Insert("insert into menu_role(header_id, role_id, status, created_date, maker_id) values (#{header_id}, #{role_id}, #{status}, CURRENT_TIMESTAMP, #{maker_id})")
	public void createMenuRole(MenuRole menuRole);

	@Insert("insert into menu_header_items(header_id, item_id, created_date, creator_id) values (#{header_id}, #{item_id}, CURRENT_TIMESTAMP, #{creator_id})")
	public void createMenuHeaderItems(MenuHeaderItems menuHeaderItems);

	@Insert("insert into menu_permission(item_id, permission_id, status, created_date, maker_id) values (#{item_id}, #{permission_id}, #{status}, CURRENT_TIMESTAMP, #{maker_id})")
	public void createMenuPermission(MenuItemPermissions menuItemPermissions);

	// headers and menu items by role
	@SelectProvider(type = SqlProvider.class, method = "getMenuByRoleSql")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "menuItems", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.MenuMapper.getMenuItemsByHeaderIDDynamicNew"))
	})
	List<MenuHeader> getMenuByRole(@Param("role_names") List<String> role_names,
			@Param("target") String target,
			@Param("status") boolean status,
			@Param("super_menu") boolean super_menu);

	class SqlProvider {
		public static String getMenuByRoleSql(@Param("role_names") List<String> role_names,
				@Param("target") String target,
				@Param("status") boolean status,
				@Param("super_menu") boolean super_menu) {
			StringBuilder sql = new StringBuilder();
			sql.append(
					"SELECT mh.id, mh.header, mh.target, mh.header_order, mh.icon, mh.url, mh.badge, mh.command, mh.template ");
			sql.append("FROM menu_header mh ");
			sql.append("INNER JOIN menu_role mr ON mr.header_id = mh.id ");
			sql.append("INNER JOIN role r ON r.id = mr.role_id ");
			sql.append("WHERE r.name IN (");

			for (int i = 0; i < role_names.size(); i++) {
				sql.append("#{role_names[").append(i).append("]}");
				if (i < role_names.size() - 1) {
					sql.append(", ");
				}
			}
			sql.append(") ");
			sql.append("AND mh.status = #{status} ");

			// Only filter out super_menu when the parameter is false
			if (!super_menu) {
				sql.append("AND mh.super_menu = 0 ");
			}

			sql.append("ORDER BY mh.header_order");

			return sql.toString();
		}

	}

}
