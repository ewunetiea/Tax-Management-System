package com.afr.fms.Auditee.Entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditeeDivisionFileISM {
    private Long id;
    private List<String> file_urls;
    private String created_date;
    private String updated_date;
    private String file_url;
    private String previous_file_url;
}
