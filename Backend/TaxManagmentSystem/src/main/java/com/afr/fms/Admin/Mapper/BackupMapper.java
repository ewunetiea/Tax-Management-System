package com.afr.fms.Admin.Mapper;
import org.apache.ibatis.annotations.*;
import com.afr.fms.Admin.Entity.Backup;
@Mapper
public interface BackupMapper {  
	@Insert("insert into backupDB (filepath, user_id) values(#{filepath},#{user_id})")
	public void insertIntoBackup(Backup backup);

	@Select("SELECT * FROM backupDB WHERE user_id = #{user_id}")
	public Backup getFilepath(@Param(value = "user_id") Long user_id);

	@Update("update backupDB set filepath = #{filepath} where user_id = #{user_id}")
	public void updateFilepath(Backup backup);

    @Insert("BACKUP DATABASE afrfms TO DISK = #{filepath}")
    public void createFullBackup(Backup backup);

	@Insert("BACKUP DATABASE afrfms TO DISK = #{filepath} WITH DIFFERENTIAL")
    public void createDIFFERENTIALBackup(Backup backup);
	}

