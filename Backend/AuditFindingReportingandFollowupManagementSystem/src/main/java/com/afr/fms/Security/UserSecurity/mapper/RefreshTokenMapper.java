package com.afr.fms.Security.UserSecurity.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.*;
import com.afr.fms.Security.UserSecurity.entity.RefreshToken;

@Mapper
public interface RefreshTokenMapper {

	@Select("select * from refresh_token where token = #{token}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "expiryDate", column = "expiry_date"),
			@Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
	})
	Optional<RefreshToken> findByToken(String token);

	@Select("select * from refresh_token where id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "expiryDate", column = "expiry_date"),
			@Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
	})
	public RefreshToken findById(Long id);

	@Select("insert into refresh_token (user_id, token, expiry_date, user_tracker_id) OUTPUT inserted.id values (#{user.id}, #{token}, #{expiryDate}, #{user_tracker_id})")
	public Long save(RefreshToken refreshToken);

	@Delete("delete from refresh_token where id = #{id}")
	public void delete(RefreshToken token);

	@Select("delete from refresh_token where user_id = #{user_id}")
	public void deleteByUser(Long user_id);

	@Select("select * from refresh_token where user_id = #{user_id} and user_tracker_id = #{user_tracker_id} and expiry_date < GETDATE()")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "expiryDate", column = "expiry_date"),
	})
	public List<RefreshToken> retrieveRefreshToken(Long user_id, Long user_tracker_id);
	
	@Select("SELECT CAST(CASE WHEN EXISTS(SELECT 1 FROM refresh_token WHERE user_id = #{user_id}) THEN 1 ELSE 0 END AS BIT);")
	public boolean isRefreshTokenExist(Long user_id);



}
