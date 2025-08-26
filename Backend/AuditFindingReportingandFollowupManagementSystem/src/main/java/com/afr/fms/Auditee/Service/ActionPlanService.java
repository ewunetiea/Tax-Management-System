package com.afr.fms.Auditee.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Auditee.Entity.ActionPlan;
import com.afr.fms.Auditee.Mapper.ActionPlanMapper;
@Service
public class ActionPlanService {
@Autowired
ActionPlanMapper actionPlanMapper;

    public void create_action_plan(ActionPlan actionPlan) {
        actionPlanMapper.create_action_plan(actionPlan);
    }

     public void update_action_plan(ActionPlan actionPlan) {
        actionPlanMapper.update_action_plan(actionPlan);
    }

     public List<ActionPlan> getActionPlans(Long auditee_id) {
        return actionPlanMapper.getActionPlans(auditee_id);
    }  

    public ActionPlan getActionPlan(Long auditee_id) {
      return actionPlanMapper.getActionPlan(auditee_id);
    }  

}
