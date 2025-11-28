package com.tms.Admin.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.Admin.Entity.MenuHeader;
import com.tms.Admin.Entity.MenuHeaderItems;
import com.tms.Admin.Entity.MenuItemPermissions;
import com.tms.Admin.Entity.MenuItems;
import com.tms.Admin.Entity.MenuRole;
import com.tms.Admin.Entity.Role;
import com.tms.Admin.Mapper.MenuMapper;
import com.tms.Admin.Mapper.RoleMapper;
import com.tms.Common.Entity.Functionalities;
import com.tms.Common.Permission.Mapper.FunctionalitiesMapper;

@Service
public class MenuService {

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private FunctionalitiesMapper functionalitiesMapper;
	@Autowired
	private RoleMapper roleMapper;

	private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

	public List<MenuItems> getMenuItems() {
		return menuMapper.getMenuItems();
	}

	public List<MenuHeader> getMenuHeaders() {
		return menuMapper.getMenuHeaders();
	}

	public List<MenuHeader> getCommonMenuHeaders() {
		return menuMapper.getCommonMenuHeaders();
	}

	public List<MenuHeader> getMenuByRole(MenuHeader menuHeader) {
		List<MenuHeader> menu_headers = menuMapper.getMenuByRole(menuHeader.getRole_names(), menuHeader.getTarget(),
				menuHeader.isStatus(), menuHeader.isSuper_menu());
		return menu_headers;
	}

	public void manageMenuItemsStatus(List<MenuItems> menuItems) {
		if (menuItems.size() == 1 && menuItems.get(0).isDelete()) {
			menuMapper.deleteMenuItem(menuItems.get(0));
		} else {
			boolean status = menuItems.get(0).isStatus();
			Long maker_id = menuItems.get(0).getMaker_id();
			for (MenuItems menuItem : menuItems) {
				menuItem.setStatus(status);
				menuItem.setMaker_id(maker_id);
				menuMapper.manageMenuItemsStatus(menuItem);
			}
		}
	}

	public void manageMenuHeaderStatus(List<MenuHeader> menuHeaders) {
		if (menuHeaders.size() == 1 && menuHeaders.get(0).isDelete()) {
			menuMapper.deleteMenuHeader(menuHeaders.get(0));
		} else {
			boolean status = menuHeaders.get(0).isStatus();
			Long maker_id = menuHeaders.get(0).getMaker_id();

			for (MenuHeader menuHeader : menuHeaders) {
				menuHeader.setStatus(status);
				menuHeader.setMaker_id(maker_id);
				menuMapper.manageMenuHeaderStatus(menuHeader);
			}
		}
	}

	public void createMenuItem(MenuItems menuItems) {
		MenuHeaderItems menuHeaderItem = new MenuHeaderItems();
		if (menuItems.getId() == null) {
			Long id = menuMapper.createMenuItem(menuItems);
			menuHeaderItem.setItem_id(id);
		} else {
			menuMapper.updateMenuItem(menuItems);
			menuHeaderItem.setItem_id(menuItems.getId());
		}

		menuMapper.deleteMenuHeaderItemsByItem(menuHeaderItem.getItem_id());

		for (MenuHeader menuHeader : menuItems.getMenuHeaders()) {
			menuHeaderItem.setHeader_id(menuHeader.getId());
			menuHeaderItem.setCreator_id(menuItems.getMaker_id());
			menuMapper.createMenuHeaderItems(menuHeaderItem);
		}
	}

	public void createMenuHeader(MenuHeader menuHeader) {
		MenuRole menuRole = new MenuRole();
		if (menuHeader.getId() == null) {
			Long id = menuMapper.createMenuheader(menuHeader);
			menuRole.setHeader_id(id);
		} else {
			menuMapper.updateMenuHeader(menuHeader);
			menuRole.setHeader_id(menuHeader.getId());
		}
		menuRole.setStatus(menuHeader.isStatus());
		menuRole.setMaker_id(menuHeader.getMaker_id());
		menuRole.setRoles(
				menuHeader.getRoles());
		manageMenuRole(menuRole);
	}

	// advance this by implementing the menu item permissions configuration
	public void menuHeaderItems(MenuHeaderItems menuHeaderItems) {
		menuMapper.deleteMenuHeaderItems(menuHeaderItems.getHeader_id());
		MenuHeaderItems menuHeaderItem = new MenuHeaderItems();
		menuHeaderItem.setHeader_id(menuHeaderItems.getHeader_id());
		menuHeaderItem.setCreator_id(menuHeaderItems.getCreator_id());
		for (MenuItems menuItems : menuHeaderItems.getMenuItems()) {
			menuHeaderItem.setItem_id(menuItems.getId());
			menuMapper.createMenuHeaderItems(menuHeaderItem);
		}
	}

	@Transactional
	public void manageMenuRole(MenuRole menuRole) {
		String roleNames = menuRole.getRoles().stream()
				.map(Role::getCode)
				.collect(Collectors.joining(", "));
		try {
			configureAPIPermissionRole(menuRole);

		} catch (Exception e) {
			logger.error(
					"Error while configuring role permission mapping starting from menu item - menu header mapping: ",
					e);
		}
		menuMapper.deleteMenuRoleByHeaderID(menuRole.getHeader_id());
		MenuRole menuRole2 = menuRole;
		for (Role role : menuRole.getRoles()) {
			menuRole2.setRole_id(role.getId());
			menuMapper.createMenuRole(menuRole2);
		}

		MenuHeader menuHeader = new MenuHeader();
		menuHeader.setId(menuRole.getHeader_id());
		menuHeader.setTarget_roles(roleNames);
		menuMapper.updateMenuHeaderTargetRoles(menuHeader);
	}

	public void manageItemPermissions(MenuItemPermissions menuItemPermissions) {
		menuMapper.deleteMenuPermissionByItemID(menuItemPermissions.getItem_id());
		MenuItemPermissions menuItemPermissions2 = menuItemPermissions;
		for (Functionalities permission : menuItemPermissions.getPermissions()) {
			menuItemPermissions2.setPermission_id(permission.getId());
			menuMapper.createMenuPermission(menuItemPermissions2);
		}
	}

	public void configureAPIPermissionRole(MenuRole menuRole) {
		List<MenuItems> menuItems = menuMapper.getMenuItemsByHeaderIDNew(menuRole.getHeader_id());

		List<Long> menuItemIDs = menuItems.stream()
				.map(MenuItems::getId)
				.collect(Collectors.toList());

		String menuItemID = menuItemIDs.stream()
				.map(String::valueOf) // Convert Long to String, without quotes
				.collect(Collectors.joining(","));

		List<Long> permissionIDs = functionalitiesMapper.getPermissionsByMenuItemID(menuItemID);

		String permissionID = permissionIDs.stream()
				.map(String::valueOf) // Convert Long to String, without quotes
				.collect(Collectors.joining(","));

		configureRoleHeaderMapping(permissionID, menuRole);

	}

	public void configureRoleHeaderMapping(String permissionID, MenuRole menuRole) {
		List<Role> roles = roleMapper.getRoleByHeaderID(menuRole.getHeader_id());
		List<Long> assigendRoleIDs = roles.stream()
				.map(Role::getId)
				.collect(Collectors.toList());

		List<Long> newlyAssigendRoleIDs = menuRole.getRoles().stream()
				.map(Role::getId)
				.collect(Collectors.toList());

		List<Long> idsNotInNewlyAssigned = new ArrayList<>(assigendRoleIDs);
		idsNotInNewlyAssigned.removeAll(newlyAssigendRoleIDs);

		addPermissionsRole(permissionID, newlyAssigendRoleIDs);
		removePermissionsRole(permissionID, idsNotInNewlyAssigned, menuRole);

	}

	public void addPermissionsRole(String permissionID, List<Long> newlyAssigendRoleIDs) {
		for (Long role : newlyAssigendRoleIDs) {
			List<Long> unassignedPermissionIDs = functionalitiesMapper.getUnassignedPermissionsFromRole(role,
					permissionID);
			for (Long id : unassignedPermissionIDs) {
				// if (id != 0)
				functionalitiesMapper.assignPermission(role, id, true);
			}
		}
	}

	public void removePermissionsRole(String permissionID, List<Long> idsNotInNewlyAssigned, MenuRole menuRole) {
		for (Long role : idsNotInNewlyAssigned) {
			functionalitiesMapper.deleteUnassignedPermissionsRoleID(role, menuRole.getHeader_id(),
					permissionID);
		}
	}
}
