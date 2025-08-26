package com.afr.fms.Common.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Currency {
    private Long id;
    private boolean status;
    private String currency_code;
    private double rate;
}
