package com.afr.fms.Common.Audit_Remark;

import java.util.ArrayList;
// import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper;
import com.afr.fms.Auditor.Entity.AuditISM;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemarkService {

    @Autowired
    private RemarkMapper remarkMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuditeeDivisionISMMapper auditDivisionISMMapper;

    public List<Remark> getRemarks(Remark remark) {

        return remarkMapper.getRemarks(remark);
    }

    public List<User> getUserByCategory(AuditISM auditISM) {

        List<User> users = userMapper.getUsersRemark(auditISM);
        List<User> auditeeUsers = userMapper.getUserByBranchandRole(auditISM.getDirectorate_id(), "AUDITEE");
        users.addAll(auditeeUsers);

        List<AuditeeDivisionISM> assignedAuditeeDivisions = auditDivisionISMMapper
                .getAssignedAuditeeDivisions(auditISM.getIS_MGTAuditee().get(0).getId());

        List<Branch> divisions = assignedAuditeeDivisions.stream()
                .map(AuditeeDivisionISM::getDivision)
                .collect(Collectors.toList());

        List<User> usersDivision = new ArrayList<>();
        for (Branch branch : divisions) {
            if (branch != null) {

                usersDivision.addAll(userMapper
                        .getUserByBranchandRole(branch.getId(), "AUDITEE_DIVISION"));
            }
        }
        users.addAll(usersDivision);

        // add User objects to the list
        // distinctByKey - is a custom method that you can define to filter a list based
        // on a specific attribute of the objects in the list.
        List<User> uniqueUserList = users.stream()
                .filter(distinctByKey(User::getId))
                .collect(Collectors.toList());

        return uniqueUserList;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<Remark> getUnseenRemarks(Remark remark) {
        return remarkMapper.getUnseenRemarks(remark);
    }

    public void addRemark(Remark remark) {
        Long reciever_idM;
        if (remark.isRejected()) {
            // reciever_idM =
            // auditISMMapper.getAudit(remark.getAudit().getId()).getAuditor().getId();
            // User reciever = new User();
            // reciever.setId(reciever_idM);
            // remark.setReciever(reciever);
            remarkMapper.addRejectedRemark(remark);
        }

        else {
            remarkMapper.addRemark(remark);
        }
    }

    public void seenRemark(Remark remark) {
        remarkMapper.seenRemark(remark);
    }

}
