package com.afr.fms.Maker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.afr.fms.Maker.entity.Account;

@Mapper
public interface AccountMapper {

	@Insert("insert into account(accountName, accountType, email, phone, status)values(#{accountName}, #{accountType}, #{email}, #{phone},#{status})")
	public void createAccount(Account account);

	@Select("select *  from account")
	// @Results(value = {
	// 		@Result(property = "id", column = "id"),

	// // 		@Result(property = "transaction", column = "region_id",

	// // 				one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById"))
	// // })
	public List<Account> getAccounts();

		@Select("select *  from account  where status = 'active'")

	// @Results(value = {
	// 		@Result(property = "id", column = "id"),

	// 		@Result(property = "region", column = "region_id",

	// 				one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById"))
	// })
	public List<Account> getActiveAccounts();

	@Select("select name from Account Order By accountName")
	public List<String> getAccountName();

	@Update("update Account set accountName=#{accountName},accountType=#{accountType},email=#{email},status=#{status} , phone =#{phone}  where id=#{id}")
	public void updateAccount(Account account);

	@Select("select * from account where accountName like concat('%',#{searchKey},'%') or accountType like concat('%',#{searchKey},'%') Order by accountName")
	public List<Account> searchAccount(String searchKey);

	@Update("update account set status= 'inactive' where id=#{id}")
	public void deleteAccount(Account account);

	@Update("update account set status='active' where id=#{id}")
	public void activateAccount(Account account);

	@Select("select * from account where id = #{id}")
	// @Results(value = {
	// 		@Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")) })
	// 
	public Account getAccountById(Long id);

	@Select("select top 1 * from account where LTRIM(RTRIM(accountName)) = LTRIM(RTRIM(#{accountName}))")
	// @Results(value = {
	// 		@Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")) })
	
	
			public Account getAccountByName(String name);

}
