package com.afr.fms.Common.Currency;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyMapper mapper;
    
    public List<Currency> getAllCurrency(){
        return mapper.getAllCurrency();
    }
}
