package com.afr.fms.Common.Report.MGT_Report.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Common.Report.IS_Report.Entity.ISManagementAuditDTO;
import com.afr.fms.Common.Report.IS_Report.Entity.ISReport;
import com.afr.fms.Common.Report.IS_Report.Mapper.ISReportMapper;
import com.afr.fms.Common.Report.IS_Report.Service.ISMGTService;
import com.afr.fms.Common.Report.MGT_Report.Mapper.ISManagementAuditMapper2;
import com.afr.fms.Common.Report.MGT_Report.Mapper.NewReport.NewManagemetReport;

@Service
public class Mgt_Report_Service {
    @Autowired
    private ISReportMapper isReportMapper;
    @Autowired
    private ISMGTService ismgtService;

    @Autowired
    private ISManagementAuditMapper2 MGTReportMapper;

    @Autowired

   private  NewManagemetReport  newManagemetReport;
    List<String> store = new ArrayList<>();

    public List<Branch> getDivisionsByDirectorateId(Long directorate_id) {
        List<Branch> division_list = new ArrayList<>();
        for (IS_MGT_Auditee IS_MGTAuditee : isReportMapper.getDivisions(directorate_id)) {
            for (AuditeeDivisionISM auditeeDivisionISM : IS_MGTAuditee.getAuditeeDivisionISM()) {
                if (!division_list.contains(auditeeDivisionISM.getDivision())) {
                    division_list.add(auditeeDivisionISM.getDivision());
                }
            }
        }
        return division_list;
    }

    public List<ISManagementAuditDTO> getManagementReport(ISReport isReport) {
        List<ISManagementAuditDTO> audits = MGTReportMapper.searchISManagementAudit(isReport.getRole(),
                isReport.getUserId(),
                isReport.getDirectorateId(), 
                isReport.getBranchId(), 
                isReport.getCategory(),
                isReport.getFinding(),
                ismgtService.checkAmounts(isReport, 0) ? isReport.getAmount().get(0) : null,
                ismgtService.checkAmounts(isReport, 1)   ? isReport.getAmount().get(1) : null,
                isReport.getRiskLevel(),
                isReport.getFindingStatus(),
                ismgtService.checkAuditPeriodDates(isReport, 0) ? isReport.getAuditPeriod().get(0) : null,
                ismgtService.checkAuditPeriodDates(isReport, 1)
                        ? isReport.getAuditPeriod().get(1)
                        : null,
                isReport.getSingleSelection(),
                isReport.getRectificationStatus(), ismgtService.checkRectificationDates(isReport, 0)
                        ? isReport.getRectificationDate().get(0)
                        : null,
                ismgtService.checkRectificationDates(isReport, 1)
                        ? isReport.getRectificationDate().get(1)
                        : null, isReport.getAudit_type());
        // return ismgtService.filterAudits(audits);


        System.out.println(audits);
        return audits;

    }

}
