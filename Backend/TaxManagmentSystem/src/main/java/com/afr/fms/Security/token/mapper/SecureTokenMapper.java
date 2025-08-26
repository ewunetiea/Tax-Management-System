package com.afr.fms.Security.token.mapper;

import org.apache.ibatis.annotations.*;

import com.afr.fms.Security.token.entity.SecureToken;

@Mapper
public interface SecureTokenMapper {

	// Long removeByToken(String token);
	// @Select("select * from refreshToken where token = #{token}")
	// Optional<RefreshToken> findByToken(String token);

	@Select("select * from secure_token where token = #{token}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "expire_at", column = "expire_at"),
			@Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.findById"))
	})
	public SecureToken findByToken(String token);

	@Select("insert into secure_token (user_id, token, timeStamp, expire_at) values (#{user.id}, #{token}, CURRENT_TIMESTAMP, #{expireAt})")
	public void save(SecureToken secureToken);

	@Delete("delete from secure_token where id = #{id}")
	public void delete(SecureToken token);

	@Delete("delete from secure_token where user_id = #{user_id}")
	public void deleteByUserId(Long user_id);

	@Select("delete from secure_token where token = #{token}")
	public void removeByToken(String token);

}
