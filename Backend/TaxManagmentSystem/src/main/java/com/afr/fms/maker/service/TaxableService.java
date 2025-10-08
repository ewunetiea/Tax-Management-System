package com.afr.fms.Maker.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.mapper.TaxableMapper;

@Service

public class TaxableService {

    @Autowired
    private TaxableMapper taxableMapper;

    @Transactional
    public String createTax(Tax tax) {

      return  taxableMapper.createTax(tax);

    }

    @Transactional
    public void updateTax(Tax taxable) {

        taxableMapper.createTax(null);

    }

    public Tax fetchTaxById(int id) {

        return taxableMapper.fetchTaxBiID(id);

    }

    public List<Tax> fetchTax() {

        return taxableMapper.fetchTax();

    }

    public void deleteTax (String mainGuid)
    {
       
        taxableMapper.deleteTaxById(mainGuid);
    }

}
