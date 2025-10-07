package com.afr.fms.Maker.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Maker.entity.TaxCategory;

@Mapper
public interface TaxCategoryMapper {
    
    @Insert("INSERT INTO tblTaxCategory(type, description, created_by, created_date) VALUES(#{type}, #{description}, #{created_by}, CURRENT_TIMESTAMP)")
    public void createTaxCategory(TaxCategory tax);
    
    @Update("UPDATE tblTaxCategory SET type = #{type}, description = #{description}, updated_by = #{created_by}, updated_date = CURRENT_TIMESTAMP WHERE id = #{id}")
    public void updateTaxCategory(TaxCategory tax);

    @Select("select * from tblTaxCategory")
	public List<TaxCategory> getTaxCategories();

    @Delete("DELETE FROM tblTaxCategory WHERE id = #{id}")
    public void deleteTaxCategory(TaxCategory tax);


}
