package com.afr.fms.Admin.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import com.afr.fms.Admin.Entity.Contact;
import com.afr.fms.Admin.Entity.Feedback;

@Mapper
public interface ContactMaper {
    @Insert("insert into contact(first_name, last_name, phone_number, email, title, registered_by) values(#{first_name}, #{last_name}, #{phone_number}, #{email},#{title}, #{registered_by})")
    public void createContact(Contact contact);

    @Delete("delete from contact where id = #{id}")
    public void deleteContact(Contact contact);

    @Select("select * from contact")
    @Result(property = "id", column = "id")
    public List<Contact> getContacts();

    @Select("select * from contact where id=#{id}")
    public Contact getContactById(Long id);

    @Update("update contact set first_name= #{first_name}, last_name=#{last_name}, email=#{email}, phone_number=#{phone_number}, title = #{title}, registered_by=#{registered_by} where id=#{id} ")
    public void updateContact(Contact contact);

    @Insert("insert into feedback(user_id, feedback) values(#{user_id}, #{feedback})")
    public void createFeedback(Feedback feedback);

    @Update("update feedback set status = 1 where id = #{id}")
    public void closeFeedback(Feedback feedback);

    @Delete("delete from feedback where id = #{id}")
    public void deleteFeedback(Feedback feedback);

    @Update("update feedback set response = #{response} where id = #{id}")
    public void respondFeedback(Feedback feedback);

    @Select("select * from feedback where status = 0")
    @Result(property = "id", column = "id")
    @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById"))
    public List<Feedback> getFeedbacks();

    @Select("select * from feedback where user_id = #{id}")
    @Result(property = "id", column = "id")
    @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById"))
    public List<Feedback> getFeedbacksByUserID(Long id);

}
