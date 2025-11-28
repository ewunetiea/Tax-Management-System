package com.tms.Maker.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxCategory {
    private Long id;
    private Long user_id;
    private String type;
    private String description;
    private String created_by;
    private Date created_date;
    private Date updated_date;
}
