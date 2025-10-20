package com.afr.fms.Approver.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.*;
import com.afr.fms.Approver.Entity.Announcement;

@Mapper
public interface AnnouncementMapper {

        @Select("insert into announcements(title, message, posted_by, audience, created_date,expiry_date , image) output inserted.id values (#{title}, #{message}, #{posted_by}, #{audience}, CURRENT_TIMESTAMP , #{expiry_date}, #{image})")
        public Long createCreateAnnouncement(Announcement announcement);

        @Select("SELECT * " +
                        "FROM announcements " +
                        "WHERE expiry_date >= GETDATE() " +
                        "ORDER BY id  DESC")
     public    List<Announcement> getOngoingAnnouncements();

@Select("SELECT TOP 1 * " +
        "FROM announcements " +
        "WHERE expiry_date >= GETDATE() " +
        "ORDER BY created_date DESC")
public Announcement getAnnouncementForDashBoard();


        // Fetch archived (expired) announcements
        @Select("SELECT * " +
                        "FROM announcements " +
                        "WHERE expiry_date < GETDATE() " + // already expired
                        "ORDER BY expiry_date DESC")
        List<Announcement> getArchivedAnnouncements();

        @Update("update announcements set  title = #{title}, message = #{message}, posted_by = #{posted_by}, audience = #{audience}, created_date = #{created_date}, expiry_date = #{expiry_date} where id = #{id}")
        public void updateAnnouncements(Announcement announcement);

        @Select("select * from announcements where id = #{id}")

        public Announcement getAnnouncementById(Long id);

        @Delete("delete from announcements   where id = #{id} ")
        public void deleteAnnouncement(Long id);

}
