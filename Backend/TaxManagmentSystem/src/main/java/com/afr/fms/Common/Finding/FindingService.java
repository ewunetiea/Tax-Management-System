package com.afr.fms.Common.Finding;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindingService {
    @Autowired
    private FindingMapper commonFindingMapper;

    public void createCommonFinding(Finding commonFinding) {

        commonFindingMapper.createCommonFinding(commonFinding);

    }

    public List<Finding> getCommonFinding(Long id) {
        return commonFindingMapper.getCommonFinding(id);
    }

    public List<Finding> getCommonFindings(String identifier) {
        return commonFindingMapper.getCommonFindings(identifier);
    }

    public void updateCommonFinding(Finding commonFinding) {
        commonFindingMapper.updateCommonFinding(commonFinding);
    }

    public void deleteCommonFinding(Long id) {
        commonFindingMapper.deleteCommonFinding(id);

    }

}
