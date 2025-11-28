package com.tms.Maker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxFile {
    private String Id;
    private String fileName;
    private String extension;
    private String supportId;
    private Long tax_id;
}
