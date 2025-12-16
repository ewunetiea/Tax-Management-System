package com.tms.Maker.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxableSearchEngine {

    private Long branch_id;
    private Long tax_category_id;
    private String reference_number;
    private String router_status;

    private List<LocalDate> maked_date;
    private List<LocalDate> checked_date;
    private List<LocalDate> approved_date;
    private List<LocalDate> rejected_date;

    private String document_type;
    private Long user_id;
    private String search_by;
    private Long director_id;

    // pagination
    private int totalRecords;
    private int currentPage;
    private int pageSize;
    private int event_length;
}
