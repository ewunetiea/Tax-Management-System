package com.afr.fms.Admin.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Region;
import com.afr.fms.Admin.Mapper.BranchMapper;
import com.afr.fms.Admin.Mapper.RegionMapper;

@Service
public class RegionService {

    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private BranchMapper branchMapper;

    public void createRegion(Region region) {
        regionMapper.createRegion(region);
    }

    public List<Region> getRegions() {
        return regionMapper.getRegions();
    }

    public List<Region> getActiveRegions() {
        return regionMapper.getActiveRegions();
    }

    public Region getRegionById(Long id) {
        return regionMapper.getRegionById(id);
    }

    public void updateRegion(Region region) {
        regionMapper.updateRegion(region);
    }

    public void deleteRegion(Long id) {
        regionMapper.deleteRegion(id);
    }

    public void activateRegion(Long id) {
        regionMapper.activateRegion(id);
    }

    public boolean checkRegionNameExist(Region region) {
        boolean exist = false;
        for (Region reg : regionMapper.getRegions()) {
            if (region.getName().equalsIgnoreCase(reg.getName())) {
                exist = true;
            }
        }
        return exist;
    }

    public boolean checkRegionCodeExist(Region region) {
        boolean exist = false;
        for (Region reg : regionMapper.getRegions()) {
            if (region.getCode().equalsIgnoreCase(reg.getCode())) {
                exist = true;
            }
        }
        return exist;
    }

    public List<Object> drawBranchPerRegionLineChart() {
        List<Object> data = new ArrayList<>();
        for (Region region : regionMapper.getRegions()) {
            data.add(region.getName());
            data.add(branchMapper.getBranchesCountByRegionId(region.getId()));
        }
        return data;
    }
        public List<Object> drawRegionLineChart() {
        List<Object> data = new ArrayList<>();

        for (Region region : regionMapper.getRegions()) {
            data.add(region.getName());
            data.add(branchMapper.findNumberofBranchesByRegioId(region.getId()));

        }
        return data;
    }

}
