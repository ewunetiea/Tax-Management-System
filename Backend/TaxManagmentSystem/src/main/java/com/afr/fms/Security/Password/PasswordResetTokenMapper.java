package com.afr.fms.Security.Password;

import java.util.List;
// import java.util.Optional;

import org.apache.ibatis.annotations.*;
import com.afr.fms.Admin.Entity.User;

@Mapper
public interface PasswordResetTokenMapper {

    @Insert("INSERT INTO password_reset_token(user_id, token, expiry_date) VALUES (#{user.id}, #{token}, #{expiryDate})")
    public void saveToken(PasswordResetToken passwordResetToken);

    @Select("SELECT * FROM password_reset_token WHERE token=#{token}")
    @Results(value = {
        @Result(property = "id", column = "id"),
        @Result(property = "expire_at", column = "expiry_date"),
    })
    public PasswordResetToken findByToken(String token);

    @Select("SELECT u.* from password_reset_token prt INNER JOIN [user] u on prt.user_id=u.id WHERE token=#{token}")
    @Results(value = {
        @Result(property = "id", column = "id"),
        @Result(property = "photoUrl", column = "photo_url"),
        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.UserRoleMapper.getRolesByUserId"))
    })
    public User getUserByPasswordResetToken(String token);

    @Delete("DELETE FROM password_reset_token WHERE user_id=#{id}")
    public void deleteUserPasswordResetTokens(Long id);
    
}
