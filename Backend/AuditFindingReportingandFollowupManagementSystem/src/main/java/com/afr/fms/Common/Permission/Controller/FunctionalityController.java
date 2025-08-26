package com.afr.fms.Common.Permission.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Common.Entity.Functionalities;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;

import com.afr.fms.Payload.response.AGPResponse;
import com.nimbusds.oauth2.sdk.ParseException;

@RestController
@RequestMapping("/api/permission")
public class FunctionalityController {

    @Autowired
    private FunctionalitiesService functionalitiesService;

    private static final Logger logger = LoggerFactory.getLogger(FunctionalityController.class);

    @PostMapping("/save")
    public ResponseEntity<?> createFunctionality(HttpServletRequest request,
            @RequestBody Functionalities functionalities) throws ParseException {
        try {
            if (functionalities.getId() != null) {
                functionalitiesService.updateFunctionality(functionalities);
            } else {
                functionalitiesService.createFunctionality(functionalities);
            }
            return AGPResponse.success("Functionalities sucessfully saved");
        } catch (Exception ex) {
            logger.error("Error while saving functionality", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllRoleFunctionalities")
    public ResponseEntity<List<Functionalities>> getAllRoleFunctionalities(HttpServletRequest request) {
        System.out.println(
                "Fffffffffffffffffffffffffffffffffffffffffffffffffffff Permissions:" + request.getRequestURI());
        try {
            return new ResponseEntity<>(functionalitiesService.getAllRoleFunctionalities(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching all role functionalities", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/deactivate")
    public ResponseEntity<Void> deactivatePermissions(HttpServletRequest request,
            @RequestBody List<Functionalities> functionalities) {
        try {
            for (Functionalities permission : functionalities) {
                functionalitiesService.deactivatePermissions(permission.getId());
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (Exception e) {
            logger.error("Error while deactivating permissions", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deletePermissions(HttpServletRequest request,
            @RequestBody List<Functionalities> functionalities) {
        try {
            for (Functionalities permission : functionalities) {
                functionalitiesService.deletePermission(permission.getId());
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (Exception e) {
            logger.error("Error while deleting permissions", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activatePermissions(HttpServletRequest request,
            @RequestBody List<Functionalities> functionalities) {
        try {

            for (Functionalities function : functionalities) {
                functionalitiesService.activatePermissions(function.getId());
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error("Error while activating permissions", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/functionalities")
    public ResponseEntity<List<Functionalities>> getFunctionalityByCategory(@RequestBody Role role,
            HttpServletRequest request) {

        try {
            return new ResponseEntity<>(functionalitiesService.getFunctionalityByCategory(role), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching functionalities by category", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/assignFunctionalities")
    public ResponseEntity<HttpStatus> assignFunctionalitiesForGivenRole(@RequestBody Role role,
            HttpServletRequest request) {
        try {
            functionalitiesService.assignPermission(role);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while assigning permission", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/updateFunctionalitiesByRole")
    public ResponseEntity<HttpStatus> updateFunctionalitiesById(@RequestBody Role role, HttpServletRequest request) {
        try {

            functionalitiesService.changeRolePermisssion(role);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while updating functionalities by id", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/functionalities/{roleId}")
    public ResponseEntity<List<Functionalities>> getFunctionalitiesByRoleID(HttpServletRequest request,
            @PathVariable("roleId") Long roleId) {
        try {
            List<Functionalities> functionalities = functionalitiesService.getAllFunctionalitiesByRole(roleId);
            return new ResponseEntity<>(functionalities, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error while fetching functionalities by role ID", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
