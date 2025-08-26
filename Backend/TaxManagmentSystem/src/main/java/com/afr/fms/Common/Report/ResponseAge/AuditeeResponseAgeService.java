package com.afr.fms.Common.Report.ResponseAge;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.Report;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditeeResponseAgeService {

    @Autowired
    private AuditeeResponseAgeMapper auditeeResponseAgeMapper;

    @Autowired
    private UserMapper userMapper;

    public List<AuditISM> getReport(Report report) {
        // System.out.println(report);
        User user = userMapper.getAuditorById(report.getUser_id());
        report.setUser(user);
        if (report.getAge_range() != null && report.getAction_date() != null) {
            return auditeeResponseAgeMapper.getAuditsByAgeRangeandApprovedDate(report);
        } else if (report.getAge_range() != null
                && report.getAction_date() == null) {
            return auditeeResponseAgeMapper.getAuditsByAgeRange(report);
        } else if (report.getAge_range() == null && report.getAge() != null
                && report.getAction_date() != null) {
            return auditeeResponseAgeMapper.getAuditsByAgeandApprovedDate(report);

        }

        else if (report.getAge() != null
                && report.getAction_date() == null) {
            return auditeeResponseAgeMapper.getAuditsByAge(report);
        } else if (report.getAge_range() == null && report.getAge() == null
                && report.getAction_date() != null) {
            return auditeeResponseAgeMapper.getAuditsByApprovedDate(report);
        }

        return null;
    }
}
