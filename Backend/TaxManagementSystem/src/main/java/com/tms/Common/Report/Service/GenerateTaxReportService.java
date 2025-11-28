package com.tms.Common.Report.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tms.Common.Report.Mapper.GenerateTaxReportMapper;
import com.tms.Maker.entity.Tax;
import com.tms.Maker.entity.TaxableSearchEngine;

@Service
public class GenerateTaxReportService {
    @Autowired
    private GenerateTaxReportMapper generateTaxReportMapper;

    public List<Tax> generateReportForMaker(TaxableSearchEngine tax) {
        return generateTaxReportMapper.generateReportForMaker(tax);
    }

    public List<Tax> generateReportForReviewer(TaxableSearchEngine tax) {
        return generateTaxReportMapper.generateReportForReviewer(tax);
    }

    public List<Tax> generateReportForApprover(TaxableSearchEngine tax) {
        return generateTaxReportMapper.generateReportForApprover(tax);
    }

}
