package com.tms.Maker.entity;

import java.sql.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tax {
    private Long id;
    private String mainGuid;
    private int from_;
    private int sendTo_;
    private int taxCategory;
    private int noOfEmployee;
    private float taxableAmount;
    private float taxWithHold;
    private float incometaxPoolAmount;
    private float graduatetaxPool;
    private float graduaTotBasSalary;
    private int graduateTotaEmployee;
    private float graduatetaxWithHold;
    private String taxCategoryList;
    private String Remark;
    private String maker_name;
    private Date maker_date;
    private String checker_name;
    private Date checked_Date;
    private String updated_user_name;
    private Date updated_event_date;
    private String from_List;
    private String sendTo_List;
    private String Category_List;
    private int status;
    private List<TaxFile> taxFile;
    private String remark;
    private String initiator_branch;
    private String destination_branch;
    private String taxType;
    private Long user_id;
    private Date approver_rejected_date;
    private String approver_rejected_reason;
    private Long rejector_approver_id;
    private String checker_rejected_reason;
    private Date checker_rejected_date;
    private Long rejector_checker_id;
    private String approver_name;
    private Date approved_date;
    private String reference_number;

    private Boolean isFileEdited;
    private List<TaxFile> previouseTaxFile;

    private Long maker_id;
    private Date drafted_date;
    private String fileExsistance;

}