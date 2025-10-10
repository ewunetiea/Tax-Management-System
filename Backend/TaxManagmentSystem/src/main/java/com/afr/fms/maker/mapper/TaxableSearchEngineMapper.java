package com.afr.fms.Maker.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.entity.TaxableSearchEngine;

@Mapper
public interface TaxableSearchEngineMapper {

    @Select({
        "<script>",
        "SELECT * FROM tblTaxable",
        "WHERE 1 = 1",

        // ✅ Router Status -> mapped numeric codes
        "<if test='router_status != null'>",
        "   <choose>",
        "       <when test='router_status == \"pending\"'>AND status = 1</when>",
        "       <when test='router_status == \"rejected\"'>AND status = 2</when>",
        "       <when test='router_status == \"approved\"'>AND status = 5</when>",
        "   </choose>",
        "</if>",

        // ✅ Filter by Branch
        "<if test='branch_id != null'>AND from_ = #{branch_id}</if>",

        // ✅ Filter by Category
        "<if test='tax_category_id != null'>AND taxCategory = #{tax_category_id}</if>",

        // ✅ Reference Number
        "<if test='reference_number != null and reference_number.trim() != \"\"'>",
        "   AND reference_number = #{reference_number}",
        "</if>",

        // ✅ Maker Date (between or single)
        "<if test='maked_date != null and maked_date.size > 0'>",
        "   <choose>",
        "       <when test='maked_date.size == 2'>",
        "           AND maker_date BETWEEN #{maked_date[0]} AND #{maked_date[1]}",
        "       </when>",
        "       <otherwise>",
        "           AND maker_date = #{maked_date[0]}",
        "       </otherwise>",
        "   </choose>",
        "</if>",

        // ✅ Checked Date
        "<if test='checked_date != null and checked_date.size > 0'>",
        "   <choose>",
        "       <when test='checked_date.size == 2'>",
        "           AND checked_date BETWEEN #{checked_date[0]} AND #{checked_date[1]}",
        "       </when>",
        "       <otherwise>",
        "           AND checked_date = #{checked_date[0]}",
        "       </otherwise>",
        "   </choose>",
        "</if>",

        // ✅ Approved Date
        "<if test='approved_date != null and approved_date.size > 0'>",
        "   <choose>",
        "       <when test='approved_date.size == 2'>",
        "           AND approved_date BETWEEN #{approved_date[0]} AND #{approved_date[1]}",
        "       </when>",
        "       <otherwise>",
        "           AND approved_date = #{approved_date[0]}",
        "       </otherwise>",
        "   </choose>",
        "</if>",

        // ✅ Rejected Date
        "<if test='rejected_date != null and rejected_date.size > 0'>",
        "   <choose>",
        "       <when test='rejected_date.size == 2'>",
        "           AND checker_rejected_date BETWEEN #{rejected_date[0]} AND #{rejected_date[1]}",
        "       </when>",
        "       <otherwise>",
        "           AND checker_rejected_date = #{rejected_date[0]}",
        "       </otherwise>",
        "   </choose>",
        "</if>",

        // ✅ Document Type
        "<if test='document_type != null and document_type.trim() != \"\"'>",
        "   AND mainGuid = #{document_type}",
        "</if>",

        // ✅ Search by Maker
        "<if test='search_by != null and search_by.trim() != \"\"'>",
        "   AND maker_name = #{search_by}",
        "</if>",

        "ORDER BY id DESC",
        "</script>"
    })
    public List<Tax> getTaxableSearchEngine(TaxableSearchEngine tax);
}
