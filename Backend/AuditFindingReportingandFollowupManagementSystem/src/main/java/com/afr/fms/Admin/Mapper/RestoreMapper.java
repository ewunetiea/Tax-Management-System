package com.afr.fms.Admin.Mapper;
import org.apache.ibatis.annotations.*;
import com.afr.fms.Admin.Entity.Backup;
@Mapper
public interface RestoreMapper {  
	
    @Insert("RESTORE DATABASE LoanFollowupManagementSystem FROM DISK = #{filepath};")
    public void restoreFullBackup(Backup backup);

	@Insert("RESTORE DATABASE LoanFollowupManagementSystem FROM DISK = #{filepath} WITH NORECOVERY;")
    public void restoreDIFFERENTIALBackup(Backup backup);
	}

