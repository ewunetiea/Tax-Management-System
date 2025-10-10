package com.afr.fms.Maker.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.entity.TaxFile;

@Mapper
public interface TaxableMapper {

        @Select("INSERT INTO tblTaxable (" +
                        "mainGuid, " + "from_, " + "sendTo_, " + "taxCategory, " +
                        "noOfEmployee, " + "taxableAmount, " + "taxWithHold, " + "incometaxPoolAmount, " +
                        "graduatetaxPool, " + "graduatetaxPool1, " + "graduaTotBasSalary, " + "graduateTotaEmployee, " +
                        "graduatetaxWithHold, " + "taxCategoryList, " + "Remark, " + "maker_name, " +
                        "maker_date, " + "checker_name, " + "checked_Date, " + "updated_user_name, "
                        + "updated_event_date, " +
                        "from_List, " + "sendTo_List, " + "Category_List, " + "FileDetail, " + "status, "
                        + "reference_number" +
                        ") OUTPUT inserted.Id VALUES (" +
                        "#{mainGuid}, " + "#{from_}, " + "#{sendTo_}, " + "#{taxCategory}, " + "#{noOfEmployee}, " +
                        "#{taxableAmount}, " + "#{taxWithHold}, " + "#{incometaxPoolAmount}, " + "#{graduatetaxPool}, "
                        +
                        "#{graduatetaxPool1}, " + "#{graduaTotBasSalary}, " + "#{graduateTotaEmployee}, " +
                        "#{graduatetaxWithHold}, " + "#{taxCategoryList}, " + "#{remark}, " +
                        "#{maker_name}, " + "#{maker_date}, " + "#{checker_name}, " + "#{checked_Date}, "
                        + "#{updated_user_name}, " +
                        "#{updated_event_date}, " + "#{from_List}, " + "#{sendTo_List}, " + "#{category_List}, "
                        + "#{fileDetail}, " + " 0 , " +
                        "#{reference_number}" +
                        ")")

        public Long createTax(Tax tax);

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

        // @Select("select * from tblTaxable ")
        // public List<Tax> fetchTax(String maker_name);

        @Select("SELECT  t.id as id , t.mainGuid, b.UnitCode as initiator_branch , b2.UnitCode as destination_branch, t.taxCategory, t.noOfEmployee, t.taxableAmount, "
                        +
                        "t.taxWithHold, t.incometaxPoolAmount, t.graduatetaxPool, t.graduatetaxPool1, t.graduaTotBasSalary, "
                        +
                        "t.graduateTotaEmployee, t.graduatetaxWithHold, t.taxCategoryList, t.Remark, t.maker_name, t.maker_date, "
                        +
                        "t.checker_name, t.checked_Date, t.updated_user_name, t.updated_event_date, t.from_List, t.sendTo_List, "
                        +
                        "t.Category_List, t.FileDetail, t.status, t.reference_number " +
                        "FROM tblTaxable t " +
                        "LEFT JOIN unit b ON t.from_ = b.id " +
                        "LEFT JOIN unit b2 ON t.sendTo_ = b2.id " +
                        "WHERE t.Id  > 39350"

        )

        @Results(value = {
                        @Result(property = "id", column = "id"), // crucial

                        @Result(property = "taxFile", column = "id", many = @Many(select = "com.afr.fms.Maker.mapper.TaxFileMapper.getFileByFileById"))
        })
        public List<Tax> fetchTax(String maker_name);

        @Delete("delete from tblTaxable   where mainGuid  = #{mainGuid}")

        public void deleteTaxById(String mainGuid);

        @Select("SELECT TOP 1 reference_number FROM tblTaxable ORDER BY Id DESC")
        String getLastReferenceNumber();

}
