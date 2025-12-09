package com.tms.Maker.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Many;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tms.Maker.entity.MakerSearchPayload;
import com.tms.Maker.entity.Tax;

@Mapper
public interface TaxableMapper {

        @Select("INSERT INTO tblTaxable (" +
                        "mainGuid, " + "from_, " + "sendTo_, " + "taxCategory, " +
                        "noOfEmployee, " + "taxableAmount, " + "taxWithHold, " + "incometaxPoolAmount, " +
                        "graduatetaxPool, " + "graduaTotBasSalary, " + "graduateTotaEmployee, " +
                        "graduatetaxWithHold, " + "taxCategoryList, " + "Remark, " + "maker_name, " +
                        "drafted_date, " + "checker_name, "
                        + "updated_event_date, " +
                        "from_List, " + "sendTo_List, " + "Category_List, " + "status, "
                        + "reference_number, maker_id" +
                        ") OUTPUT inserted.Id VALUES (" +
                        "#{mainGuid}, " + "#{from_}, " + "#{sendTo_}, " + "#{taxCategory}, " + "#{noOfEmployee}, " +
                        "#{taxableAmount}, " + "#{taxWithHold}, " + "#{incometaxPoolAmount}, " + "#{graduatetaxPool}, "

                        + "#{graduaTotBasSalary}, " + "#{graduateTotaEmployee}, " +
                        "#{graduatetaxWithHold}, " + "#{taxCategoryList}, " + "#{remark}, " +
                        "#{maker_name}, " + "#{drafted_date}, " + "#{checker_name}, " +

                        "#{updated_event_date}, " + "#{from_List}, " + "#{sendTo_List}, " + "#{category_List}, "
                        + " 6 , " +
                        "#{reference_number} , #{maker_id}" +
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
                        "graduaTotBasSalary = #{graduaTotBasSalary}, " +
                        "graduateTotaEmployee = #{graduateTotaEmployee}, " +
                        "graduatetaxWithHold = #{graduatetaxWithHold}, " +
                        "taxCategoryList = #{taxCategoryList}, " +
                        "Remark = #{remark}, " +
                        "maker_name = #{maker_name}, " +
                        "drafted_date = #{drafted_date}, " +
                        "checked_Date = #{checked_Date}, " +
                        "updated_user_name = #{updated_user_name}, " +
                        "updated_event_date = #{updated_event_date}, " +
                        "from_List = #{from_List}, " +
                        "sendTo_List = #{sendTo_List}, " +
                        "Category_List = #{category_List}, " +
                        "status = #{status} " +
                        "WHERE id = #{id}")
        public void updateTaxable(Tax tax);

        @Select("select *  from tblTaxable   where mainGuid = #{id} ")
        public Tax fetchTaxBiID(int id);

        @Select({
                        "<script>",
                        "SELECT t.id as id, t.mainGuid, b.name as initiator_branch, b2.name as destination_branch,",
                        "t.taxCategory, t.noOfEmployee, t.taxableAmount, t.taxWithHold, t.incometaxPoolAmount,",
                        "t.graduatetaxPool, t.graduaTotBasSalary, t.graduateTotaEmployee, t.graduatetaxWithHold,",
                        "t.taxCategoryList, t.Remark, t.maker_name, t.maker_date, t.checker_name, t.checked_Date,",
                        "t.updated_user_name, t.updated_event_date, t.from_List, t.sendTo_List, t.Category_List,",
                        " t.status, t.reference_number, t.remark, t.drafted_date , t.maker_date, t.approved_date , t.checker_rejected_reason, t.approver_rejected_reason, t.checker_rejected_date, t.approver_rejected_date  ",
                        "FROM tblTaxable t",
                        "LEFT JOIN branch b ON t.from_ = b.id",
                        "LEFT JOIN branch b2 ON t.sendTo_ = b2.id",

                        "WHERE maker_id = #{user_id}",

                        "<choose>",
                        "  <when test='status == 2'>",
                        "     AND t.status IN (2, 3)",
                        "  </when>",
                        "  <otherwise>",
                        "     AND t.status = #{status}",
                        "  </otherwise>",
                        "</choose>",
                        "<if test='tax_category_id != null and tax_category_id != 0'>",
                        "   AND t.taxCategory = #{tax_category_id}",
                        "</if>",

                        "<if test= 'drafted_date != null and drafted_date.size() == 2 '> " +
                                        "  AND t.drafted_date BETWEEN #{drafted_date[0]} AND #{drafted_date[1]} " +
                                        "   </if> ",

                        "<if test= 'maker_date != null and maker_date.size() == 2 '> " +
                                        "  AND t.maker_date BETWEEN #{maker_date[0]} AND #{maker_date[1]} " +
                                        "   </if> ",

                        "<if test= 'checked_date != null and checked_date.size() == 2 '> " +
                                        "  AND t.checked_Date BETWEEN #{checked_date[0]} AND #{checked_date[1]} " +
                                        "   </if> ",

                        "<if test= 'rejected_date != null and rejected_date.size() == 2 '> ",
                        "  AND t.checker_rejected_date BETWEEN #{rejected_date[0]} AND #{rejected_date[1]} ",
                        "   </if> ",
                        "<if test= 'approved_date != null and approved_date.size() == 2 '> ",
                        "  AND t.approved_date BETWEEN #{approved_date[0]} AND #{approved_date[1]} ",
                        "   </if> ",

                        "<if test= 'reference_number != null '> ",
                        "   AND t.reference_number = #{reference_number} ",
                        "   </if> ",
                   "order by t.id desc ",

                        "</script>"

        })

        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "taxFile", column = "id", many = @Many(select = "com.tms.Maker.mapper.TaxFileMapper.getFileByFileById")),

        })
        public List<Tax> fetchTaxBasedonStatus(MakerSearchPayload payload);

      

        @Delete("delete from tblTaxable   where id  = #{id}")

        public void deleteTaxById(Long id);

        @Update("update  tblTaxable set status = 0 , maker_date = CURRENT_TIMESTAMP where id  = #{id}")
        public void submitToBrancManager(Long id);

        @Update("update  tblTaxable   set status = 6 , maker_date = null  where  status in (0,2,3) and  id  = #{id}")

        public void backToDraftedState(Long id);

        @Select("SELECT TOP 1 reference_number FROM tblTaxable ORDER BY Id DESC")
        String getLastReferenceNumber();

        @Select("select status  from tblTaxable   where id  = #{id}")
        public int fetchTaxByID(Long id);

        @Select({
                        "<script>",
                        "SELECT t.id as id, t.mainGuid, b.name as initiator_branch, b2.name as destination_branch,",
                        "t.taxCategory, t.noOfEmployee, t.taxableAmount, t.taxWithHold, t.incometaxPoolAmount,",
                        "t.graduatetaxPool, t.graduaTotBasSalary, t.graduateTotaEmployee, t.graduatetaxWithHold,",
                        "t.taxCategoryList, t.Remark, t.maker_name, t.maker_date, t.checker_name, t.checked_Date,",
                        "t.updated_user_name, t.updated_event_date, t.from_List, t.sendTo_List, t.Category_List,",
                        " t.status, t.reference_number, t.remark, t.drafted_date , t.maker_date, t.approved_date , t.checker_rejected_reason, t.approver_rejected_reason, t.checker_rejected_date, t.approver_rejected_date  ",
                        "FROM tblTaxable t",
                        "LEFT JOIN branch b ON t.from_ = b.id",
                        "LEFT JOIN branch b2 ON t.sendTo_ = b2.id",

                        "WHERE t.id = #{id}",

                        "</script>"

        })

        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "taxFile", column = "id", many = @Many(select = "com.tms.Maker.mapper.TaxFileMapper.getFileByFileById")),

        })
        public Tax fetchTaxById(Long id);

}
