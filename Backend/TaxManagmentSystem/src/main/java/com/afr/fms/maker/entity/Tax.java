
package com.afr.fms.maker.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tax {
    private String mainGuid;
    private int from_;
    private int sendTo_;
    private int taxCategory;
    private int noOfEmployee;
    private float taxableAmount;
    private float taxWithHold;
    private float incometaxPoolAmount;
    private float graduatetaxPool;
    private float graduatetaxPool1;
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
    private     String Category_List;
    private int FileDetail;
    private int status;

}