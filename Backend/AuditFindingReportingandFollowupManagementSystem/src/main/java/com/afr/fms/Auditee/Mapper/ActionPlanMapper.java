package com.afr.fms.Auditee.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.afr.fms.Auditee.Entity.ActionPlan;
@Mapper
public interface ActionPlanMapper {
@Insert("insert into action_plan(auditee_id,content,created_date) values(#{auditee_id}, #{content}, CURRENT_TIMESTAMP)")
public void create_action_plan(ActionPlan actionPlan);   

@Update("update action_plan set content = #{content}, updated_date = CURRENT_TIMESTAMP where id = #{id}")
public void update_action_plan(ActionPlan actionPlan);

@Select("select * from action_plan where auditee_id = #{auditee_id}")
public List<ActionPlan> getActionPlans(Long auditee_id);

@Select("select top 1 * from action_plan where auditee_id = #{auditee_id} order by id DESC")
public ActionPlan getActionPlan(Long auditee_id);

}
