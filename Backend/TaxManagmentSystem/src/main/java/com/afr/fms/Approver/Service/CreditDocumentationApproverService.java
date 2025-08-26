package com.afr.fms.Approver.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@Service
public class CreditDocumentationApproverService {

   @Autowired

   private CreditDocumentationNewMapper creditDocumentationNewMapper;

   public List<AuditISM> getFindingBasedOnStatus(PaginatorPayLoad paginatorPayload) {
      List<AuditISM> auditISM = new ArrayList<>();
      auditISM = creditDocumentationNewMapper
            .getFindingBasedOnStatus(paginatorPayload.getUser_id(), paginatorPayload.getAudit_status(),
                  paginatorPayload.getCategory(), paginatorPayload.getBanking());

      return auditISM;
   }

}
