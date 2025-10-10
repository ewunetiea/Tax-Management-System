package com.afr.fms.Maker.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.entity.TaxableSearchEngine;
import com.afr.fms.Maker.mapper.TaxableSearchEngineMapper;

@Service
public class TaxableSearchEngineService {
    @Autowired
    private TaxableSearchEngineMapper taxableSearchEngineMapper;

     public List<Tax> getTaxableSearchEngine(TaxableSearchEngine tax) {
        return taxableSearchEngineMapper.getTaxableSearchEngine(tax);
    }

}
