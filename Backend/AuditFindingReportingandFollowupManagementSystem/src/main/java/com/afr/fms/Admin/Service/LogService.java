package com.afr.fms.Admin.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Log;
import com.afr.fms.Admin.Mapper.LogMapper;

@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;

    public List<Log> getLogRecords() {
        return logMapper.getLogRecords();
    }
    
    public void deleteLogRecordById(Long id){
        logMapper.deleteLogRecordById(id);

    }
}