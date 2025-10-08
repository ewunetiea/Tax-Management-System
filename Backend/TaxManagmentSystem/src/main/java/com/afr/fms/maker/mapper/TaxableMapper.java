package com.afr.fms.maker.mapper;

import java.util.List;

import javax.ws.rs.DELETE;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.maker.entity.Tax;

@Mapper
public interface TaxableMapper {

    @Insert("INSERT INTO tblTaxable (" + "mainGuid, " + "from_, " + "sendTo_, " + "taxCategory, " + "noOfEmployee, " +
            "taxableAmount, " + "taxWithHold, " + "incometaxPoolAmount, " + "graduatetaxPool, " + "graduatetaxPool1, " +
            "graduaTotBasSalary, " + "graduateTotaEmployee, " + "graduatetaxWithHold, " + "taxCategoryList, "
            + "Remark, " + "maker_name, " +
            "maker_date, " + "checker_name, " + "checked_Date, " + "updated_user_name, " + "updated_event_date, " +
            "from_List, " + "sendTo_List, " + "Category_List, " + "FileDetail, " + "status" +
            ") output inserted.mainGuid VALUES (" + "#{mainGuid}, " + "#{from_}, " + "#{sendTo_}, " + "#{taxCategory}, " + "#{noOfEmployee}, " +
            "#{taxableAmount}, " + "#{taxWithHold}, " + "#{incometaxPoolAmount}, " + "#{graduatetaxPool}, " +
            "#{graduatetaxPool1}, " + "#{graduaTotBasSalary}, " + "#{graduateTotaEmployee}, "
            + "#{graduatetaxWithHold}, " +
            "#{taxCategoryList}, " + "#{remark}, " + "#{maker_name}, " + "#{maker_date}, " +
            "#{checker_name}, " + "#{checked_Date}, " + "#{updated_user_name}, " + "#{updated_event_date}, " +
            "#{from_List}, " + "#{sendTo_List}, " + "#{category_List}, " + "#{fileDetail}, " + "#{status}" +
            ")")
    public String createTax(Tax tax);

    @Update("UPDATE tblTaxable  SET " +
            "from_ = #{from_}, " +
            "sendTo_ = #{sendTo_}, " +
            "taxCategory = #{taxCategory}, " +
            "noOfEmployee = #{noOfEmployee}, " +
            "taxableAmount = #{taxableAmount}, " +
            "taxWithHold = #{taxWithHold}, " +
            "incometaxPoolAmount = #{incometaxPoolAmount}, " +
            "graduatetaxPool = #{graduatetaxPool}, " +
            "graduatetaxPool1 = #{graduatetaxPool1}, " +
            "graduaTotBasSalary = #{graduaTotBasSalary}, " +
            "graduateTotaEmployee = #{graduateTotaEmployee}, " +
            "graduatetaxWithHold = #{graduatetaxWithHold}, " +
            "taxCategoryList = #{taxCategoryList}, " +
            "Remark = #{remark}, " +
            "maker_name = #{maker_name}, " +
            "maker_date = #{maker_date}, " +
            "checker_name = #{checker_name}, " +
            "checked_Date = #{checked_Date}, " +
            "updated_user_name = #{updated_user_name}, " +
            "updated_event_date = #{updated_event_date}, " +
            "from_List = #{from_List}, " +
            "sendTo_List = #{sendTo_List}, " +
            "Category_List = #{category_List}, " +
            "FileDetail = #{fileDetail}, " +
            "status = #{status} " +
            "WHERE mainGuid = #{mainGuid}")
    public void updateTaxable(Tax tax);

    @Select("select *  from tblTaxable   where mainGuid = #{id} ")
    public Tax fetchTaxBiID(int id);

    @Select("select *  from tblTaxable  ")
    public List<Tax> fetchTax();


    @Delete("delete from tblTaxable   where mainGuid  = #{mainGuid}")

    public void deleteTaxById(String mainGuid);

}
