package com.afr.fms.Admin.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.afr.fms.Admin.Entity.Branch;

@Mapper
public interface BranchMapper {
	@Insert("insert into branch(name, code, region_id, status)values(#{name}, #{code}, #{region.id}, 1)")
	public void createBranch(Branch branch);

	@Select("WITH CTE AS (  " +
			"    SELECT *, ROW_NUMBER() OVER (PARTITION BY Name ORDER BY Name) AS RowNum  " +
			"    FROM branch  " +
			")  " +
			"SELECT *  " +
			"FROM CTE  " +
			"WHERE RowNum = 1 Order By name")
	@Results(value = {
			@Result(property = "id", column = "id"),

			@Result(property = "region", column = "region_id",

					one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById"))
	})
	public List<Branch> getBranches();

	@Select("WITH CTE AS (  " +
			"    SELECT *, ROW_NUMBER() OVER (PARTITION BY Name ORDER BY Name) AS RowNum  " +
			"    FROM branch  " +
			")  " +
			"SELECT *  " +
			"FROM CTE  " +
			"WHERE RowNum = 1 and status = 1 Order By name")
	@Results(value = {
			@Result(property = "id", column = "id"),

			@Result(property = "region", column = "region_id",

					one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById"))
	})
	public List<Branch> getActiveBranches();

	@Select("select name from branch Order By name")
	public List<String> getBranchName();

	@Update("update branch set name=#{name},code=#{code},region_id=#{region.id},status=#{status}  where id=#{id}")
	public void updateBranch(Branch branch);

	@Select("select * from branch where name like concat('%',#{searchKey},'%') or code like concat('%',#{searchKey},'%') Order by name")
	public List<Branch> searchBranch(String searchKey);

	@Update("update branch set status=0 where id=#{id}")
	public void deleteBranch(Branch branch);

	@Update("update branch set status=1 where id=#{id}")
	public void activateBranch(Branch branch);

	@Select("select * from branch where id = #{id}")
	@Results(value = {
			@Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")) })
	public Branch getBranchById(Long id);

	@Select("select top 1 * from branch where LTRIM(RTRIM(name)) = LTRIM(RTRIM(#{name}))")
	@Results(value = {
			@Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")) })
	public Branch getBranchByName(String name);

	@Select("select count(id) from branch where region_id=#{id}")
	public long getBranchesCountByRegionId(Long region_id);

	@Select("select count(id) from branch where region_id=#{id}")
	public long findNumberofBranchesByRegioId(long id);




	
@Select("SELECT id, name FROM branch ORDER BY name ASC")
List<Branch> getBranchForDropDown();

}
