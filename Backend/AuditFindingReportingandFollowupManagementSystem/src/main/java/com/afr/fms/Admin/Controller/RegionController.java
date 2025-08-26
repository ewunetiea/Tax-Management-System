package com.afr.fms.Admin.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Admin.Entity.Region;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Service.RegionService;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

import com.afr.fms.Payload.response.AGPResponse;
import com.nimbusds.oauth2.sdk.ParseException;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;

@RestController
// @PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    @Autowired
    private FunctionalitiesService functionalitiesService;

    RecentActivity recentActivity = new RecentActivity();

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @PostMapping("/region")
    public ResponseEntity<?> createRegion(HttpServletRequest request, @RequestBody Region region)
            throws ParseException {
        try {
            User user = new User();
            regionService.createRegion(region);
            recentActivity.setMessage(region.getName() + " region is created ");
            user.setId(region.getUser_id());
            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);
            return AGPResponse.success("region sucessfully saved");
        } catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/region")
    public ResponseEntity<?> updateRegion(HttpServletRequest request, @RequestBody Region region)
            throws ParseException {
        try {
            User user = new User();
            regionService.updateRegion(region);
            recentActivity.setMessage(region.getName() + " region info is updated ");
            user.setId(region.getUser_id());
            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);

            return AGPResponse.success("region sucessfully saved");
        } catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/regions")
    public ResponseEntity<List<Region>> getRegions(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        List<Region> regions = regionService.getRegions(paginatorPayLoad);
        logger.info("Successfully retrieved {} regions for auditor ID: {}", regions.size(),
                paginatorPayLoad.getUser_id());
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/regions")
    public ResponseEntity<List<Region>> getAllRegions(HttpServletRequest request) {
        List<Region> regions = regionService.getAllRegions();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/region/active")
    public ResponseEntity<List<Region>> getActiveRegions(HttpServletRequest request) {
        // if (functionalitiesService.verifyPermission(request,
        // "fetching_active_regions")) {

        try {
            return new ResponseEntity<>(regionService.getActiveRegions(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // } else {
        // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // }
    }

    @GetMapping("/region/{id}")
    public ResponseEntity<Region> getRegionById(HttpServletRequest request, @PathVariable Long id) {
        try {
            return new ResponseEntity<>(regionService.getRegionById(id), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/region/delete")
    public ResponseEntity<Void> deleteRegion(HttpServletRequest request, @RequestBody List<Region> regions) {
        for (Region region : regions) {
            regionService.deleteRegion(region.getId());
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    @PostMapping("/region/activate")
    public ResponseEntity<Void> activateRegion(HttpServletRequest request, @RequestBody List<Region> regions) {
        for (Region region : regions) {
            regionService.activateRegion(region.getId());
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    @PostMapping("/region/name")
    public ResponseEntity<Boolean> checkRegionNameExist(@RequestBody Region region) {
        try {
            return new ResponseEntity<>(regionService.checkRegionNameExist(region), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/region/code")
    public ResponseEntity<Boolean> checkRegionCodeExist(@RequestBody Region region) {
        try {
            return new ResponseEntity<>(regionService.checkRegionCodeExist(region), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/region/drawRegionLineChart")
    public ResponseEntity<List<Object>> drawRegionLineChart() {
        try {
            return new ResponseEntity<>(regionService.drawRegionLineChart(), HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
