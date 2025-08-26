package com.afr.fms.Admin.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Mapper.BackupMapper;
import com.afr.fms.Admin.Mapper.RestoreMapper;
import com.afr.fms.Admin.Entity.Backup;

@Service
public class RestoreService {
    @Autowired
    private RestoreMapper restoreMapper;
    @Autowired
    private BackupMapper backupMapper;


    public String restoreBackup(Backup backup) {
        if (backup.isFlag()) {
            restoreMapper.restoreFullBackup(backup);
        } else {
            restoreMapper.restoreDIFFERENTIALBackup(backup);
        }
        return backupMapper.getFilepath(backup.getUser_id()).getFilepath();
    }

   
}
