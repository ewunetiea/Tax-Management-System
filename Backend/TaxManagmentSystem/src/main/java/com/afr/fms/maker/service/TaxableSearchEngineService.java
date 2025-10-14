package com.afr.fms.Maker.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.entity.TaxableSearchEngine;
import com.afr.fms.Maker.mapper.TaxableSearchEngineMapper;

@Service
@Transactional(readOnly = true)
public class TaxableSearchEngineService {
    @Autowired
    private TaxableSearchEngineMapper taxableSearchEngineMapper;

    public List<Tax> getTaxableSearchEngineForMaker(TaxableSearchEngine tax) {
        return taxableSearchEngineMapper.getTaxableSearchEngineForMaker(tax);
    }

    public List<Tax> getTaxableSearchEngineForChecker(TaxableSearchEngine tax) {
        return taxableSearchEngineMapper.getTaxableSearchEngineForChecker(tax);
    }

    public List<Tax> getTaxableSearchEngineForHo(TaxableSearchEngine tax) {
        return taxableSearchEngineMapper.getTaxableSearchEngineForHo(tax);
    }

}
