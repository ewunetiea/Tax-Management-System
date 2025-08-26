package com.afr.fms.Admin.Mapper;

import com.afr.fms.Admin.Entity.Region;

import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
public interface RegionMapper {

        @Select("select * from region ORDER BY name")
        @Results(value = {
                        @Result(property = "id", column = "id")
        })
        public List<Region> getRegions();

        @Select("select top 1 * from region where LTRIM(RTRIM(name)) = LTRIM(RTRIM(#{name}))")
        @Results(value = {
                        @Result(property = "id", column = "id")
        })
        public Region getRegionByName(String name);

        @Select("select name from region Order By name")
        public List<String> getRegionNames();

        @Select("select * from region where status = 1 ORDER BY name")
        public List<Region> getActiveRegions();

        @Select("select * from region where id=#{id}")
        public Region getRegionById(Long id);

        @Insert("insert into region(name,code,status) values(#{name},#{code},1)")
        public void createRegion(Region region);

        @Update("update region set name=#{name}, code=#{code} where id=#{id}")
        public void updateRegion(Region region);

        @Update("update region set status=0 where id=#{id}")
        public void deleteRegion(Long id);

        @Update("update region set status=1 where id=#{id}")
        public void activateRegion(Long id);
}
