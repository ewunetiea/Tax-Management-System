package com.afr.fms.Auditee.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Auditee.Entity.ActionPlan;
import com.afr.fms.Auditee.Service.ActionPlanService;
import com.afr.fms.Payload.response.AGPResponse;

@RestController
@RequestMapping("api/auditee")
public class ActionPlanController {
    @Autowired
    private ActionPlanService actionPlanService;

    @PostMapping(path = "/ism/actionplan")
    public ResponseEntity<AGPResponse> create_action_plan(HttpServletRequest request,
            @RequestBody ActionPlan actionPlan) {
        try {
            if (actionPlan.getId() != null) {
                actionPlanService.update_action_plan(actionPlan);
                return AGPResponse.success("SUCCESS");
            } else {
                actionPlanService.create_action_plan(actionPlan);
                return AGPResponse.success("SUCCESS");
            }

        } catch (Exception ex) {

            return AGPResponse.error("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(path = "/ism/actionplans/{auditee_id}")
    public ResponseEntity<List<ActionPlan>> getActionPlans(HttpServletRequest request,
            @PathVariable Long auditee_id) {
        try {
            return new ResponseEntity(actionPlanService.getActionPlans(auditee_id), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/ism/actionplan/{auditee_id}")
    public ResponseEntity<ActionPlan> getActionPlan(HttpServletRequest request,
            @PathVariable Long auditee_id) {
        try {
            return new ResponseEntity<>(actionPlanService.getActionPlan(auditee_id), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
