package com.afr.fms.Maker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxFile {
    private String Id;
    private String FileName;
    private String Extension;
    private String SupportId;
    private Long tax_id;
}
