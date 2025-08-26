package com.afr.fms.Admin.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Mapper.BackupMapper;


import com.afr.fms.Admin.Entity.Backup;

@Service
public class BackupService {
    @Autowired
    private BackupMapper backupMapper;

    @Value("${database.backup.path}")
    String backupPath;

    public Backup createBackup(Backup backup) {

        try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy - HHmmss");
            Date date = new Date();
            String now = formatter.format(date);
            backup.setFilepath(backupPath + "afrfms -" + now + ".BAK");
            backupMapper.insertIntoBackup(backup);
            backupMapper.createFullBackup(backup);
            return backup;

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }

    }
 
    public Backup getBackupByUserId(Long id) {
        if (!backupMapper.getFilepath(id).equals(null)) {
            return backupMapper.getFilepath(id);
        }
        return null;
    }
}
