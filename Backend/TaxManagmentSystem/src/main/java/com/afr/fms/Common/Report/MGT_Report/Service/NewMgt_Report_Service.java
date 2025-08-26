package com.afr.fms.Common.Report.MGT_Report.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Common.Report.IS_Report.Entity.CreditDocumentationReport;
import com.afr.fms.Common.Report.IS_Report.Entity.ISManagementAuditDTO;
import com.afr.fms.Common.Report.IS_Report.Entity.ISReport;
import com.afr.fms.Common.Report.IS_Report.Service.CreditDocumentationNewService;
import com.afr.fms.Common.Report.IS_Report.Service.ISMGTService;
import com.afr.fms.Common.Report.MGT_Report.Mapper.NewReport.NewCreditDocumentationReport;
import com.afr.fms.Common.Report.MGT_Report.Mapper.NewReport.NewManagemetReport;

@Service
public class NewMgt_Report_Service {

    @Autowired
    private ISMGTService ismgtService;

    @Autowired
    private CreditDocumentationNewService creditDocumentationNewService;

    @Autowired
    private NewManagemetReport newManagemetReport;

    @Autowired
    private NewCreditDocumentationReport newCreditDocumentationReport;

    List<String> store = new ArrayList<>();

    public List<ISManagementAuditDTO> getManagementReport(ISReport isReport) {
        List<ISManagementAuditDTO> audits = newManagemetReport.searchISManagementAudit(isReport.getRole(),
                isReport.getUserId(),
                isReport.getDirectorateId(),
                isReport.getBranchId(),
                isReport.getCategory(),
                isReport.getFinding(),
                ismgtService.checkAmounts(isReport, 0) ? isReport.getAmount().get(0) : null,
                ismgtService.checkAmounts(isReport, 1) ? isReport.getAmount().get(1) : null,
                isReport.getRiskLevel(),
                isReport.getFindingStatus(),
                ismgtService.checkAuditPeriodDates(isReport, 0) ? isReport.getAuditPeriod().get(0) : null,
                ismgtService.checkAuditPeriodDates(isReport, 1) ? isReport.getAuditPeriod().get(1) : null,
                isReport.getSingleSelection(),
                isReport.getRectificationStatus(),
                ismgtService.checkRectificationDates(isReport, 0) ? isReport.getRectificationDate().get(0) : null,
                ismgtService.checkRectificationDates(isReport, 1) ? isReport.getRectificationDate().get(1) : null,
                ismgtService.checkDraftedDates(isReport, 0) ? isReport.getDraftedDate().get(0) : null,
                ismgtService.checkDraftedDates(isReport, 1) ? isReport.getDraftedDate().get(1) : null,
                ismgtService.checkReviewedDates(isReport, 0) ? isReport.getReviewedDate().get(0) : null,
                ismgtService.checkReviewedDates(isReport, 1) ? isReport.getReviewedDate().get(1) : null,
                ismgtService.checkApprovedDates(isReport, 0) ? isReport.getApprovedDate().get(0) : null,
                ismgtService.checkApprovedDates(isReport, 1) ? isReport.getApprovedDate().get(1) : null,
                ismgtService.checkRespondedDates(isReport, 0) ? isReport.getRespondedDate().get(0) : null,
                ismgtService.checkRespondedDates(isReport, 1) ? isReport.getRespondedDate().get(1) : null,
                isReport.getAudit_type());
        return audits;
    }

    public List<ISManagementAuditDTO> fetchCreditDocumentationNewReport(CreditDocumentationReport isReport) {
        List<ISManagementAuditDTO> audits = newCreditDocumentationReport.searchISManagementAudit(
                isReport.getRole(),
                isReport.getUserId(),
                isReport.getDirectorateId(),
                isReport.getBranchId(),
                isReport.getCategory(),
                isReport.getFinding(),
                creditDocumentationNewService.checkAmounts(isReport, 0) ? isReport.getAmount().get(0) : null,
                creditDocumentationNewService.checkAmounts(isReport, 1) ? isReport.getAmount().get(1) : null,
                isReport.getRiskLevel(),
                isReport.getFindingStatus(),
                creditDocumentationNewService.checkAuditPeriodDates(isReport, 0) ? isReport.getAuditPeriod().get(0) : null,
                creditDocumentationNewService.checkAuditPeriodDates(isReport, 1) ? isReport.getAuditPeriod().get(1) : null,
                isReport.getSingleSelection(),
                isReport.getRectificationStatus(),
                creditDocumentationNewService.checkRectificationDates(isReport, 0) ? isReport.getRectificationDate().get(0) : null,
                creditDocumentationNewService.checkRectificationDates(isReport, 1) ? isReport.getRectificationDate().get(1) : null,
                creditDocumentationNewService.checkDraftedDates(isReport, 0) ? isReport.getDraftedDate().get(0) : null,
                creditDocumentationNewService.checkDraftedDates(isReport, 1) ? isReport.getDraftedDate().get(1) : null,
                creditDocumentationNewService.checkReviewedDates(isReport, 0) ? isReport.getReviewedDate().get(0) : null,
                creditDocumentationNewService.checkReviewedDates(isReport, 1) ? isReport.getReviewedDate().get(1) : null,
                creditDocumentationNewService.checkApprovedDates(isReport, 0) ? isReport.getApprovedDate().get(0) : null,
                creditDocumentationNewService.checkApprovedDates(isReport, 1) ? isReport.getApprovedDate().get(1) : null,
                creditDocumentationNewService.checkRespondedDates(isReport, 0) ? isReport.getRespondedDate().get(0) : null,
                creditDocumentationNewService.checkRespondedDates(isReport, 1) ? isReport.getRespondedDate().get(1) : null,
                isReport.getAudit_type(), 
                isReport.getBorrowerName(),
                creditDocumentationNewService.checkLoanGrantedDates(isReport, 0) ? isReport.getLoanGrantedDate().get(0) : null,
                creditDocumentationNewService.checkLoanGrantedDates(isReport, 1) ? isReport.getLoanGrantedDate().get(1) : null,
                creditDocumentationNewService.checkLoanSetlmentDates(isReport, 0) ? isReport.getLoanSetlmentDate().get(0) : null,
                creditDocumentationNewService.checkLoanSetlmentDates(isReport, 1) ? isReport.getLoanSetlmentDate().get(1) : null,
                isReport.getLoan_type() , isReport.getAccount_number(), isReport.getCollateral_type() , isReport.getCase_number());
        return audits;
    }

}
