package com.tms.Maker.entity;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor

@NoArgsConstructor

public class MakerSearchPayload {

    private Long id;
    private String routeControl;
    private String user_name;
    private int status;

    private Long branch_id;
    private Long tax_category_id;
    private String reference_number;
    private Long status_id;
    private Date maked_date;
    private String document_type;
    private Long user_id;
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private List<Date> drafted_date; 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private List<Date> maker_date; 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private List<Date> checked_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

     private List<Date> rejected_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

     private List<Date> approved_date;



}
