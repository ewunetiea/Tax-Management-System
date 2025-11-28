package com.tms.Admin.Mapper;
import org.apache.ibatis.annotations.*;
import com.tms.Admin.Entity.Backup;
@Mapper
public interface RestoreMapper {  
	
    @Insert("RESTORE DATABASE LoanFollowupManagementSystem FROM DISK = #{filepath};")
    public void restoreFullBackup(Backup backup);

	@Insert("RESTORE DATABASE LoanFollowupManagementSystem FROM DISK = #{filepath} WITH NORECOVERY;")
    public void restoreDIFFERENTIALBackup(Backup backup);
	}

