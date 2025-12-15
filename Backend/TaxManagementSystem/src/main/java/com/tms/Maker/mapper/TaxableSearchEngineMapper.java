package com.tms.Maker.mapper;

import java.util.List;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.tms.Maker.entity.Tax;
import com.tms.Maker.entity.TaxableSearchEngine;

@Mapper
public interface TaxableSearchEngineMapper {

    @Select({
            "<script>",
            "SELECT",
            "   br_from.name AS initiator_branch,",
            "   br_to.name AS destination_branch,",
            "   taxc.type AS taxType,",
            "   tax.*",
            "FROM tblTaxable AS tax",
            "LEFT JOIN branch AS br_from ON br_from.id = tax.from_",
            "LEFT JOIN branch AS br_to ON br_to.id = tax.sendTo_",
            "LEFT JOIN tblTaxCategory AS taxc ON taxc.id = tax.taxCategory",
            "<where>",

            // ✅ Router status logic
            "<if test='router_status != null'>",
            "   <choose>",
            "       <when test='router_status == \"pending\"'>AND tax.status = 1</when>",
            "       <when test='router_status == \"rejected\"'>AND tax.status = 2</when>",
            "       <when test='router_status == \"approved\"'>AND tax.status = 5</when>",
            "   </choose>",
            "</if>",

            "<if test='branch_id != null'>AND tax.from_ = #{branch_id}</if>",
            "<if test='tax_category_id != null'>AND tax.taxCategory = #{tax_category_id}</if>",
            "<if test='reference_number != null and reference_number.trim() != \"\"'>",
            "   AND tax.reference_number = #{reference_number}",
            "</if>",

            // ✅ Maker date range
            "<if test='maked_date != null and maked_date.size > 0'>",
            "   <choose>",
            "       <when test='maked_date.size == 2'>AND tax.maker_date BETWEEN #{maked_date[0]} AND #{maked_date[1]}</when>",
            "       <otherwise>AND tax.maker_date = #{maked_date[0]}</otherwise>",
            "   </choose>",
            "</if>",

            // ✅ Checked date range
            "<if test='checked_date != null and checked_date.size > 0'>",
            "   <choose>",
            "       <when test='checked_date.size == 2'>AND tax.checked_date BETWEEN #{checked_date[0]} AND #{checked_date[1]}</when>",
            "       <otherwise>AND tax.checked_date = #{checked_date[0]}</otherwise>",
            "   </choose>",
            "</if>",

            // ✅ Approved date range
            "<if test='approved_date != null and approved_date.size > 0'>",
            "   <choose>",
            "       <when test='approved_date.size == 2'>AND tax.approved_date BETWEEN #{approved_date[0]} AND #{approved_date[1]}</when>",
            "       <otherwise>AND tax.approved_date = #{approved_date[0]}</otherwise>",
            "   </choose>",
            "</if>",

            // ✅ Rejected date range
            "<if test='rejected_date != null and rejected_date.size > 0'>",
            "   <choose>",
            "       <when test='rejected_date.size == 2'>AND tax.checker_rejected_date BETWEEN #{rejected_date[0]} AND #{rejected_date[1]}</when>",
            "       <otherwise>AND tax.checker_rejected_date = #{rejected_date[0]}</otherwise>",
            "   </choose>",
            "</if>",

            "<if test='document_type != null and document_type.trim() != \"\"'>",
            "   AND tax.mainGuid = #{document_type}",
            "</if>",

            // ✅ Optional search_by
            "<if test='search_by != null and search_by.trim() != \"\"'>",
            "   AND tax.maker_name = #{search_by}",
            "</if>",

            "</where>",
            "ORDER BY tax.id DESC",
            "</script>"
    })
    @Results(value = {
        @Result(property = "id", column = "id"),
        @Result(property = "taxFile", column = "id", many = @Many(select = "com.tms.Maker.mapper.TaxFileMapper.getFileByFileById")),
        })
    public List<Tax> getTaxableSearchEngineForMaker(TaxableSearchEngine tax);

    @Select({
            "<script>",
            "SELECT",
            "   br_from.name AS initiator_branch,",
            "   br_to.name AS destination_branch,",
            "   taxc.type AS taxType,",
            "   tax.*",
            "FROM tblTaxable AS tax",
            "LEFT JOIN branch AS br_from ON br_from.id = tax.from_",
            "LEFT JOIN branch AS br_to ON br_to.id = tax.sendTo_",
            "LEFT JOIN tblTaxCategory AS taxc ON taxc.id = tax.taxCategory",
            "<where>",

            // Router status
            "<if test='router_status != null'>",
            "  <choose>",
            "    <when test='router_status == \"pending\"'>AND tax.status = 0</when>",
            "    <when test='router_status == \"sent\"'>AND tax.status = 1</when>",
            "    <when test='router_status == \"rejected\"'>AND (tax.status = 2 or tax.status = 3) </when>",
            "    <when test='router_status == \"settled\"'>AND tax.status = 5</when>",
            "    <otherwise>AND tax.status IN (0, 1, 2, 3, 5)</otherwise>",
            "  </choose>",
            "</if>",

            // Branch
            "<if test='branch_id != null'> AND tax.from_ = #{branch_id}</if>",

            // Tax category
            "<if test='tax_category_id != null'> AND tax.taxCategory = #{tax_category_id}</if>",

            // Reference number
            "<if test='reference_number != null and reference_number.trim() != \"\"'> AND tax.reference_number = #{reference_number}</if>",

            // ✅ Maker date range
            "<if test='maked_date != null and maked_date.size() > 0'>",
            "  <choose>",
            "    <when test='maked_date.size() == 2 and maked_date[1] != null'>",
            "      AND CAST(tax.maker_date AS DATE) BETWEEN CAST(#{maked_date[0]} AS DATE) AND CAST(#{maked_date[1]} AS DATE)",
            "    </when>",
            "    <otherwise>",
            "      AND CAST(tax.maker_date AS DATE) = CAST(#{maked_date[0]} AS DATE)",
            "    </otherwise>",
            "  </choose>",
            "</if>",

            // ✅ Checked date range
            "<if test='checked_date != null and checked_date.size() > 0'>",
            "  <choose>",
            "    <when test='checked_date.size() == 2 and checked_date[1] != null'>",
            "      AND CAST(tax.checked_date AS DATE) BETWEEN CAST(#{checked_date[0]} AS DATE) AND CAST(#{checked_date[1]} AS DATE)",
            "    </when>",
            "    <otherwise>",
            "      AND CAST(tax.checked_date AS DATE) = CAST(#{checked_date[0]} AS DATE)",
            "    </otherwise>",
            "  </choose>",
            "</if>",

            // ✅ Approved date range
            "<if test='approved_date != null and approved_date.size() > 0'>",
            "  <choose>",
            "    <when test='approved_date.size() == 2 and approved_date[1] != null'>",
            "      AND CAST(tax.approved_date AS DATE) BETWEEN CAST(#{approved_date[0]} AS DATE) AND CAST(#{approved_date[1]} AS DATE)",
            "    </when>",
            "    <otherwise>",
            "      AND CAST(tax.approved_date AS DATE) = CAST(#{approved_date[0]} AS DATE)",
            "    </otherwise>",
            "  </choose>",
            "</if>",

            // ✅ Rejected date range
            "<if test='rejected_date != null and rejected_date.size() > 0'>",
            "  <choose>",
            "    <when test='rejected_date.size() == 2 and rejected_date[1] != null'>",
            "      AND CAST(tax.checker_rejected_date AS DATE) BETWEEN CAST(#{rejected_date[0]} AS DATE) AND CAST(#{rejected_date[1]} AS DATE)",
            "    </when>",
            "    <otherwise>",
            "      AND CAST(tax.checker_rejected_date AS DATE) = CAST(#{rejected_date[0]} AS DATE)",
            "    </otherwise>",
            "  </choose>",
            "</if>",

            // Document type
            "<if test='document_type != null and document_type.trim() != \"\"'> AND tax.mainGuid = #{document_type}</if>",

            "</where>",
            "ORDER BY tax.id DESC",
            "</script>"
    })
     @Results(value = {
        @Result(property = "id", column = "id"),
        @Result(property = "taxFile", column = "id", many = @Many(select = "com.tms.Maker.mapper.TaxFileMapper.getFileByFileById")),
        })
    public List<Tax> getTaxableSearchEngineForReviewer(TaxableSearchEngine tax);

    @Select({
            "<script>",
            "SELECT",
            "   br_from.name AS initiator_branch,",
            "   br_to.name AS destination_branch,",
            "   taxc.type AS taxType,",
            "   tax.*",
            "FROM tblTaxable AS tax",
            "LEFT JOIN branch AS br_from ON br_from.id = tax.from_",
            "LEFT JOIN branch AS br_to ON br_to.id = tax.sendTo_",
            "LEFT JOIN tblTaxCategory AS taxc ON taxc.id = tax.taxCategory",
            "<where>",

            // ✅ Router status
            "<if test='router_status != null'>",
            "   <choose>",
            "       <when test='router_status == \"pending\"'>AND tax.status = 1</when>",
            "       <when test='router_status == \"rejected\"'>AND tax.status = 3</when>",
            "       <when test='router_status == \"approved\"'>AND tax.status = 5</when>",
            "   </choose>",
             " <otherwise>AND tax.status IN (0, 1, 3, 5)</otherwise> ",
            "</if>",

            "<if test='branch_id != null'>AND tax.from_ = #{branch_id}</if>",
            "<if test='director_id != null'>AND tax.sendTo_ = #{director_id}</if>",
            "<if test='tax_category_id != null'>AND tax.taxCategory = #{tax_category_id}</if>",
            "<if test='reference_number != null and reference_number.trim() != \"\"'>AND tax.reference_number = #{reference_number}</if>",

            // ✅ Maker date range
            "<if test='maked_date != null and maked_date.size() > 0'>",
            "   <choose>",
            "       <when test='maked_date.size() == 2 and maked_date[1] != null'>",
            "           AND CAST(tax.maker_date AS DATE) BETWEEN CAST(#{maked_date[0]} AS DATE) AND CAST(#{maked_date[1]} AS DATE)",
            "       </when>",
            "       <otherwise>",
            "           AND CAST(tax.maker_date AS DATE) = CAST(#{maked_date[0]} AS DATE)",
            "       </otherwise>",
            "   </choose>",
            "</if>",

            // ✅ Checked date range
            "<if test='checked_date != null and checked_date.size() > 0'>",
            "   <choose>",
            "       <when test='checked_date.size() == 2 and checked_date[1] != null'>",
            "           AND CAST(tax.checked_date AS DATE) BETWEEN CAST(#{checked_date[0]} AS DATE) AND CAST(#{checked_date[1]} AS DATE)",
            "       </when>",
            "       <otherwise>",
            "           AND CAST(tax.checked_date AS DATE) = CAST(#{checked_date[0]} AS DATE)",
            "       </otherwise>",
            "   </choose>",
            "</if>",

            // ✅ Approved date range
            "<if test='approved_date != null and approved_date.size() > 0'>",
            "   <choose>",
            "       <when test='approved_date.size() == 2 and approved_date[1] != null'>",
            "           AND CAST(tax.approved_date AS DATE) BETWEEN CAST(#{approved_date[0]} AS DATE) AND CAST(#{approved_date[1]} AS DATE)",
            "       </when>",
            "       <otherwise>",
            "           AND CAST(tax.approved_date AS DATE) = CAST(#{approved_date[0]} AS DATE)",
            "       </otherwise>",
            "   </choose>",
            "</if>",

            // ✅ Rejected date range
            "<if test='rejected_date != null and rejected_date.size() > 0'>",
            "   <choose>",
            "       <when test='rejected_date.size() == 2 and rejected_date[1] != null'>",
            "           AND CAST(tax.checker_rejected_date AS DATE) BETWEEN CAST(#{rejected_date[0]} AS DATE) AND CAST(#{rejected_date[1]} AS DATE)",
            "       </when>",
            "       <otherwise>",
            "           AND CAST(tax.checker_rejected_date AS DATE) = CAST(#{rejected_date[0]} AS DATE)",
            "       </otherwise>",
            "   </choose>",
            "</if>",

            "<if test='document_type != null and document_type.trim() != \"\"'>AND tax.mainGuid = #{document_type}</if>",

            "</where>",
            "ORDER BY tax.id DESC",
            "</script>"
    })
    @Results(value = {
        @Result(property = "id", column = "id"),
        @Result(property = "taxFile", column = "id", many = @Many(select = "com.tms.Maker.mapper.TaxFileMapper.getFileByFileById")),
        })
    public List<Tax> getTaxableSearchEngineForApprover(TaxableSearchEngine tax);
    
    @Select({
            "<script>",
            "SELECT",
            "   br_from.name AS initiator_branch,",
            "   br_to.name AS destination_branch,",
            "   taxc.type AS taxType,",
            "   tax.*",
            "FROM tblTaxable AS tax",
            "LEFT JOIN branch AS br_from ON br_from.id = tax.from_",
            "LEFT JOIN branch AS br_to ON br_to.id = tax.sendTo_",
            "LEFT JOIN tblTaxCategory AS taxc ON taxc.id = tax.taxCategory",
            "<where>",

            // ✅ Router status
            "<if test='router_status != null'>",
            "   <choose>",
            "       <when test='router_status == \"Drafted\"'>AND tax.status = 6</when>",
            "       <when test='router_status == \"Submitted\"'>AND tax.status = 0</when>",
            "       <when test='router_status == \"Sent\"'>AND tax.status = 1</when>",
            "       <when test='router_status == \"Settled\"'>AND tax.status = 5</when>",
            "    <when test='router_status == \"Rejected\"'>AND (tax.status = 2 or tax.status = 3) </when>",
            "   </choose>",
            "</if>",

            "<if test='branch_id != null'>AND tax.from_ = #{branch_id}</if>",
            "<if test='director_id != null'>AND tax.sendTo_ = #{director_id}</if>",
            "<if test='tax_category_id != null'>AND tax.taxCategory = #{tax_category_id}</if>",
            "<if test='reference_number != null and reference_number.trim() != \"\"'>AND tax.reference_number = #{reference_number}</if>",

            // ✅ Maker date range
            "<if test='maked_date != null and maked_date.size() > 0'>",
            "   <choose>",
            "       <when test='maked_date.size() == 2 and maked_date[1] != null'>",
            "           AND CAST(tax.maker_date AS DATE) BETWEEN CAST(#{maked_date[0]} AS DATE) AND CAST(#{maked_date[1]} AS DATE)",
            "       </when>",
            "       <otherwise>",
            "           AND CAST(tax.maker_date AS DATE) = CAST(#{maked_date[0]} AS DATE)",
            "       </otherwise>",
            "   </choose>",
            "</if>",

            // ✅ Checked date range
            "<if test='checked_date != null and checked_date.size() > 0'>",
            "   <choose>",
            "       <when test='checked_date.size() == 2 and checked_date[1] != null'>",
            "           AND CAST(tax.checked_date AS DATE) BETWEEN CAST(#{checked_date[0]} AS DATE) AND CAST(#{checked_date[1]} AS DATE)",
            "       </when>",
            "       <otherwise>",
            "           AND CAST(tax.checked_date AS DATE) = CAST(#{checked_date[0]} AS DATE)",
            "       </otherwise>",
            "   </choose>",
            "</if>",

            // ✅ Approved date range
            "<if test='approved_date != null and approved_date.size() > 0'>",
            "   <choose>",
            "       <when test='approved_date.size() == 2 and approved_date[1] != null'>",
            "           AND CAST(tax.approved_date AS DATE) BETWEEN CAST(#{approved_date[0]} AS DATE) AND CAST(#{approved_date[1]} AS DATE)",
            "       </when>",
            "       <otherwise>",
            "           AND CAST(tax.approved_date AS DATE) = CAST(#{approved_date[0]} AS DATE)",
            "       </otherwise>",
            "   </choose>",
            "</if>",

            // ✅ Rejected date range
            "<if test='rejected_date != null and rejected_date.size() > 0'>",
            "   <choose>",
            "       <when test='rejected_date.size() == 2 and rejected_date[1] != null'>",
            "           AND CAST(tax.checker_rejected_date AS DATE) BETWEEN CAST(#{rejected_date[0]} AS DATE) AND CAST(#{rejected_date[1]} AS DATE)",
            "       </when>",
            "       <otherwise>",
            "           AND CAST(tax.checker_rejected_date AS DATE) = CAST(#{rejected_date[0]} AS DATE)",
            "       </otherwise>",
            "   </choose>",
            "</if>",

            "<if test='document_type != null and document_type.trim() != \"\"'>AND tax.mainGuid = #{document_type}</if>",

            "</where>",
            "ORDER BY tax.id DESC",
            "</script>"
    })
    @Results(value = {
        @Result(property = "id", column = "id"),
        @Result(property = "taxFile", column = "id", many = @Many(select = "com.tms.Maker.mapper.TaxFileMapper.getFileByFileById")),
        })
    public List<Tax> getTaxableSearchEngineForAdmin(TaxableSearchEngine tax);

}
