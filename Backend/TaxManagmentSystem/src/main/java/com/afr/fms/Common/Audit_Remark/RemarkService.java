package com.afr.fms.Common.Audit_Remark;

import java.util.ArrayList;
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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemarkService {

    @Autowired
    private RemarkMapper remarkMapper;

    @Autowired
    private UserMapper userMapper;

    // @Autowired
    // private AuditeeDivisionISMMapper auditDivisionISMMapper;

    public List<Remark> getRemarks(Remark remark) {
        return remarkMapper.getRemarks(remark);
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
