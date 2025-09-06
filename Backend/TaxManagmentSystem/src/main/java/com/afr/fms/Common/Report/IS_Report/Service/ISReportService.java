package com.afr.fms.Common.Report.IS_Report.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Common.Report.IS_Report.Mapper.ISReportMapper;

@Service
public class ISReportService {

    @Autowired
    private ISReportMapper ISReportMapper;
    List<String> store = new ArrayList<>();

    // public List<Branch> getDivisionsByDirectorateId(Long directorate_id) {
    //     List<Branch> division_list = new ArrayList<>();
    //     for (IS_MGT_Auditee IS_MGTAuditee : ISReportMapper.getDivisions(directorate_id)) {
    //         for (AuditeeDivisionISM auditeeDivisionISM : IS_MGTAuditee.getAuditeeDivisionISM()) {
    //             if (!division_list.contains(auditeeDivisionISM.getDivision())) {
    //                 division_list.add(auditeeDivisionISM.getDivision());
    //             }
    //         }
    //     }
    //     return division_list;
    // }

}
