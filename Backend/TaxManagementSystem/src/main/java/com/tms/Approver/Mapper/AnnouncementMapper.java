package com.tms.Approver.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.*;
import com.tms.Approver.Entity.Announcement;
import com.tms.Approver.Entity.AnnouncementFile;
import com.tms.Maker.entity.TaxFile;

@Mapper
public interface AnnouncementMapper {

        @Select("insert into announcements(title, message, posted_by, audience, created_date,expiry_date , image) output inserted.id values (#{title}, #{message}, #{posted_by}, #{audience}, CURRENT_TIMESTAMP , #{expiry_date}, #{image})")
        public Long createCreateAnnouncement(Announcement announcement);

        // @Select(" SELECT a.*, ur.email AS postedBy FROM announcements a " +
        // "INNER JOIN [user] ur ON ur.id = a.posted_by WHERE a.expiry_date >= GETDATE()
        // " +
        // " AND (a.audience = #{role_type} OR a.audience = 'ALL') ORDER BY a.id DESC")
        // public List<Announcement> getOngoingAnnouncements(String role_type);

        // @Select(" SELECT TOP 1 a.*, ur.email AS postedBy FROM announcements a " +
        // " INNER JOIN [user] ur ON ur.id = a.posted_by WHERE a.expiry_date >=
        // GETDATE() " +
        // "AND (a.audience = #{role_type} OR a.audience = 'ALL') ORDER BY
        // a.created_date DESC ")
        // public Announcement getAnnouncementForDashBoard(String role_type);

        // // Fetch archived (expired) announcements
        // @Select(" SELECT a.*, ur.email AS postedBy FROM announcements a " +
        // " INNER JOIN [user] ur ON ur.id = a.posted_by WHERE a.expiry_date < GETDATE()
        // " +
        // " AND (a.audience = #{role_type} OR a.audience = 'ALL') ORDER BY
        // a.expiry_date DESC ")
        // public List<Announcement> getArchivedAnnouncements(String role_type);

        // 1. Ongoing announcements (including future ones) - Approver sees everything

        @Select(" SELECT a.*, ur.email AS postedBy   FROM announcements a " +
                        "   INNER JOIN [user] ur ON ur.id = a.posted_by    WHERE a.expiry_date >= GETDATE() " +
                        "    AND (    #{role_type} = 'ROLE_APPROVER'   OR a.audience = #{role_type}   OR a.audience = 'ALL'   )  ORDER BY a.id DESC ")
        List<Announcement> getOngoingAnnouncements(String role_type);

        // Latest one for dashboard
        @Select("   SELECT TOP 1 a.*, ur.email AS postedBy  FROM announcements a  " +
                        "  INNER JOIN [user] ur ON ur.id = a.posted_by   WHERE a.expiry_date >= GETDATE() " +
                        "   AND (  #{role_type} = 'ROLE_APPROVER' OR a.audience = #{role_type}  OR a.audience = 'ALL'  )  ORDER BY a.created_date DESC ")
        Announcement getAnnouncementForDashBoard(String role_type);

        // Archived announcements
        @Select("  SELECT a.*, ur.email AS postedBy   FROM announcements a    INNER JOIN [user] ur ON ur.id = a.posted_by "
                        +
                        "  WHERE a.expiry_date < GETDATE()   AND ( #{role_type} = 'ROLE_APPROVER'   OR a.audience = #{role_type} "
                        +
                        " OR a.audience = 'ALL' )  ORDER BY a.expiry_date DESC ")
        List<Announcement> getArchivedAnnouncements(String role_type);

        @Update("update announcements set  title = #{title}, message = #{message}, posted_by = #{posted_by}, audience = #{audience}, created_date = #{created_date}, expiry_date = #{expiry_date} where id = #{id}")
        public void updateAnnouncements(Announcement announcement);

        @Select("select * from announcements where id = #{id}")
        public Announcement getAnnouncementById(Long id);

        @Delete("delete from announcements   where id = #{id} ")
        public void deleteAnnouncement(Long id);

        @Delete("delete from FileDetailOfClaim  where FileName = #{fileName} ")
        public void deleteAnnouncementFile(String fileName);

        // Insert new file detail
        @Insert("INSERT INTO FileDetailOfClaim (Id, FileName, Extension, SupportId, tax_id) " +
                        "VALUES (#{id}, #{fileName}, #{extension}, #{supportId}, #{taxId})")
        public void insertFile(AnnouncementFile file);

        @Select("SELECT CASE WHEN EXISTS (SELECT 1 FROM FileDetailOfClaim WHERE FileName LIKE CONCAT(#{fileName}, '%')) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
        public boolean checkFilnameExistance(String fileName);

         @Select("SELECT TOP 1 reference_number FROM announcements ORDER BY Id DESC")
       public String getLastReferenceNumber();

}
