package com.afr.fms.Common.Report.IS_Report.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Common.Report.IS_Report.Entity.ISManagementAuditDTO;
import com.afr.fms.Common.Report.IS_Report.Entity.ISReport;
import com.afr.fms.Common.Report.IS_Report.Mapper.ISManagementAuditMapper;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ISMGTService {

        @Autowired
        private ISManagementAuditMapper ISReportMapper;

        List<String> store = new ArrayList<>();

        public List<ISManagementAuditDTO> getISReport(ISReport isReport) {

                List<ISManagementAuditDTO> audits = ISReportMapper.searchISManagementAudit(isReport.getRole(),
                                isReport.getUserId(),
                                isReport.getDirectorateId(), 
                                isReport.getBranchId(), 
                                isReport.getCategory(),
                                isReport.getFinding(),
                                isReport.getRiskLevel(), 
                                isReport.getFindingStatus(),
                                checkAuditPeriodDates(isReport, 0) ? isReport.getAuditPeriod().get(0)  : null,
                                checkAuditPeriodDates(isReport, 1) ? isReport.getAuditPeriod().get(1)  : null,
                                isReport.getSingleSelection(),
                                isReport.getRectificationStatus(),
                                checkRectificationDates(isReport, 0) ? isReport.getRectificationDate().get(0) : null,
                                checkRectificationDates(isReport, 1) ? isReport.getRectificationDate().get(1) : null,
                                checkDraftedDates(isReport, 0) ? isReport.getDraftedDate().get(0) : null,
                                checkDraftedDates(isReport, 1) ? isReport.getDraftedDate().get(1) : null,
                                checkReviewedDates(isReport, 0) ? isReport.getReviewedDate().get(0) : null,
                                checkReviewedDates(isReport, 1) ? isReport.getReviewedDate().get(1) : null,
                                checkApprovedDates(isReport, 0) ? isReport.getApprovedDate().get(0) : null,
                                checkApprovedDates(isReport, 1) ? isReport.getApprovedDate().get(1) : null,
                                checkRespondedDates(isReport, 0) ? isReport.getRespondedDate().get(0) : null,
                                checkRespondedDates(isReport, 1) ? isReport.getRespondedDate().get(1) : null
                                , isReport.getCase_number());
                return filterAudits(audits);
        }

        public List<ISManagementAuditDTO> filterAudits(List<ISManagementAuditDTO> audits) {
                // Group audits by case_number
                Map<String, List<ISManagementAuditDTO>> auditsByCaseNumber = audits.stream()
                                .collect(Collectors.groupingBy(ISManagementAuditDTO::getCase_number));

                // Filter audits to keep only one auditee_id per case_number
                return auditsByCaseNumber.values().stream()
                                .flatMap(auditList -> auditList.stream()
                                                .map(audit -> {
                                                        audit.setDirectorate_name(audit.getDirectorate_name().trim());
                                                        return audit;
                                                })
                                                .collect(Collectors.toMap(ISManagementAuditDTO::getDirectorate_name,
                                                                audit -> audit,
                                                                (audit1, audit2) -> audit1))
                                                .values().stream())
                                .collect(Collectors.toList());
        }

      
      
        public boolean checkAuditPeriodDates(ISReport isReport, int index) {
                if (isReport.getAuditPeriod() != null) {
                        if (index == 0)
                                return isReport.getAuditPeriod().size() > 0;
                        return isReport.getAuditPeriod().size() > 1;
                }
                return false;
        }

        public boolean checkAmounts(ISReport isReport, int index) {
                if (isReport.getAmount() != null) {
                        if (isReport.getAmount().size() != 0) {
                                if (index == 0)
                                        return isReport.getAmount().size() > 0;
                                return isReport.getAmount().size() > 1;
                        }
                }
                return false;
        }

        public boolean checkRectificationDates(ISReport isReport, int index) {
                if (isReport.getRectificationDate() != null) {
                        if (index == 0)
                                return isReport.getRectificationDate().size() > 0;
                        return isReport.getRectificationDate().size() > 1;
                }
                return false;
        }

        public boolean checkDraftedDates(ISReport isReport, int index) {
                if (isReport.getDraftedDate() != null) {
                        if (index == 0)
                                return isReport.getDraftedDate().size() > 0;
                        return isReport.getDraftedDate().size() > 1;
                }
                return false;
        }

        public boolean checkReviewedDates(ISReport isReport, int index) {
                if (isReport.getReviewedDate() != null) {
                        if (index == 0)
                                return isReport.getReviewedDate().size() > 0;
                        return isReport.getReviewedDate().size() > 1;
                }
                return false;
        }


        public boolean checkApprovedDates(ISReport isReport, int index) {
                if (isReport.getApprovedDate() != null) {
                        if (index == 0)
                                return isReport.getApprovedDate().size() > 0;
                        return isReport.getApprovedDate().size() > 1;
                }
                return false;
        }

        public boolean checkRespondedDates(ISReport isReport, int index) {
                if (isReport.getRespondedDate() != null) {
                        if (index == 0)
                                return isReport.getRespondedDate().size() > 0;
                        return isReport.getRespondedDate().size() > 1;
                }
                return false;
        }

}
