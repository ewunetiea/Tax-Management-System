package com.tms.Approver.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.*;
import com.tms.Approver.Entity.Announcement;
import com.tms.Approver.Entity.AnnouncementFile;

@Mapper
public interface AnnouncementMapper {

        @Select("insert into announcements(title, message, posted_by, audience, created_date,expiry_date , image, mainGuid) output inserted.id values (#{title}, #{message}, #{posted_by}, #{audience}, CURRENT_TIMESTAMP , #{expiry_date}, #{image}, #{mainGuid})")
        public Long createCreateAnnouncement(Announcement announcement);
        
        @Select("SELECT a.*, ur.email AS postedBy " +
        "FROM announcements a " +
        "INNER JOIN [user] ur ON ur.id = a.posted_by " +
        "WHERE a.expiry_date >= GETDATE() " +
        "AND (#{role_type} = 'ROLE_APPROVER' OR a.audience = #{role_type} OR a.audience = 'ALL') " +
        "ORDER BY a.id DESC")
        @Results({
          @Result(property = "id", column = "id"),
          @Result(property = "announcementFile", column = "id", many = @Many(select = "com.tms.Approver.Mapper.AnnouncementMapper.getAnnouncementFileByAnnouncementId"))
        })
        public List<Announcement> getOngoingAnnouncements(String role_type);
        
        // Latest one for dashboard
        @Select(" SELECT TOP 1 a.*, ur.email AS postedBy  FROM announcements a  " +
        "  INNER JOIN [user] ur ON ur.id = a.posted_by   WHERE a.expiry_date >= GETDATE() " +
        "  AND (  #{role_type} = 'ROLE_APPROVER' OR a.audience = #{role_type}  OR a.audience = 'ALL') ORDER BY a.created_date DESC ")
        @Results({
          @Result(property = "id", column = "id"),
          @Result(property = "announcementFile", column = "id", many = @Many(select = "com.tms.Approver.Mapper.AnnouncementMapper.getAnnouncementFileByAnnouncementId"))
        })
        public Announcement getAnnouncementForDashBoard(String role_type);

        // Archived announcements
        @Select("  SELECT a.*, ur.email AS postedBy FROM announcements a INNER JOIN [user] ur ON ur.id = a.posted_by "
        +"  WHERE a.expiry_date < GETDATE()   AND ( #{role_type} = 'ROLE_APPROVER'   OR a.audience = #{role_type} "
        +" OR a.audience = 'ALL' )  ORDER BY a.expiry_date DESC ")
        @Results({
          @Result(property = "id", column = "id"),
          @Result(property = "announcementFile", column = "id", many = @Many(select = "com.tms.Approver.Mapper.AnnouncementMapper.getAnnouncementFileByAnnouncementId"))
        })
        public List<Announcement> getArchivedAnnouncements(String role_type);

        @Update("update announcements set  title = #{title}, message = #{message}, posted_by = #{posted_by}, audience = #{audience}, created_date = #{created_date}, expiry_date = #{expiry_date} where id = #{id}")
        public void updateAnnouncements(Announcement announcement);

        @Select("select * from announcements where id = #{id}")
        public Announcement getAnnouncementById(Long id);

        @Delete("delete from announcements   where id = #{id} ")
        public void deleteAnnouncement(Long id);

        @Delete("delete from announcement_file  where announcement_id = #{announcement_id} ")
        public void deleteAnnouncementFile(Long announcement_id);

        // Insert new file detail
        @Insert("INSERT INTO announcement_file (fileName, extension, supportId, announcement_id) VALUES (#{fileName}, #{extension}, #{supportId}, #{announcement_id})")
        public void insertFile(AnnouncementFile file);

        @Select("SELECT CASE WHEN EXISTS (SELECT 1 FROM announcement_file WHERE FileName LIKE CONCAT(#{fileName}, '%')) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
        public boolean checkFileNameExistance(String fileName);
        
        @Select("SELECT fileName, extension FROM announcement_file WHERE announcement_id = #{Id}")
        public List<AnnouncementFile> getAnnouncementFileByAnnouncementId(Long announcement_id);
}
