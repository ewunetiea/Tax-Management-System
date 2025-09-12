package com.afr.fms.Admin.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Payload.response.AGPResponse;
import com.nimbusds.oauth2.sdk.ParseException;

@RestController
@RequestMapping("/api")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();

    @PostMapping("/region")
    public ResponseEntity<?> saveRegion(HttpServletRequest request, @RequestBody Region region) throws ParseException {
        try {
            User user = new User();
            user.setId(region.getUser_id());

            if (region.getId() != null) {
                // Update region
                regionService.updateRegion(region);
                recentActivity.setMessage(region.getName() + " region is updated ");
            } else {
                // Create new region
                regionService.createRegion(region);
                recentActivity.setMessage(region.getName() + " region is created ");
            }

            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);

            return AGPResponse.success("Region successfully " + (region.getId() != null ? "updated" : "created"));
        } catch (Exception ex) {
            System.out.println(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping("/region")
    // public ResponseEntity<?> createRegion(HttpServletRequest request,
    // @RequestBody Region region) throws ParseException {
    // try {
    // User user = new User();
    // regionService.createRegion(region);
    // recentActivity.setMessage(region.getName() + " region is created ");
    // user.setId(region.getUser_id());
    // recentActivity.setUser(user);
    // recentActivityMapper.addRecentActivity(recentActivity);
    // return AGPResponse.success("region sucessfully saved");
    // } catch (Exception ex) {

    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

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
            System.out.println(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/region")
    public ResponseEntity<List<Region>> getRegions(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(regionService.getRegions(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/region/active")
    public ResponseEntity<List<Region>> getActiveRegions(HttpServletRequest request) {

        try {
            return new ResponseEntity<>(regionService.getActiveRegions(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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
            System.out.println(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
