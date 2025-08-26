package com.afr.fms.Common.Recommendation;

import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RecommendationMapper {

	@Select("select * from recommendation where user_id=#{user_id} order by created_date DESC")
	public List<Recommendation> getRecommendation(Long user_id);

	@Select("select * from recommendation where identifier = #{identifier} order by created_date DESC")
	public List<Recommendation> getRecommendations(String identifier);

	@Insert("insert into recommendation(content, created_date, modified_date, user_id, identifier) values(#{content}  , CURRENT_TIMESTAMP , #{modified_date}, #{user.id}, #{identifier})")
	public void createRecommendation(Recommendation recommendation);

	@Update("update recommendation set content = #{content}, identifier = #{identifier}, user_id = #{user.id}, modified_date = CURRENT_TIMESTAMP where id = #{id}")
	public void updateRecommendation(Recommendation recommendation);

	@Delete("delete from recommendation where id=#{id}")
	public void deleteRecommendation(Long id);

}
