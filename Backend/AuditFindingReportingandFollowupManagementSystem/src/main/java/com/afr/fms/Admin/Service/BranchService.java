package com.afr.fms.Admin.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Mapper.BranchMapper;
import com.afr.fms.Admin.Entity.Branch;

@Service
public class BranchService {
    @Autowired
    private BranchMapper branchMapper;

    public List<Branch> getBranches() {
        return branchMapper.getBranches();
    }

    public List<Branch> getActiveBranches() {
        return branchMapper.getActiveBranches();
    }

    public void createBranch(Branch branch) {
        branchMapper.createBranch(branch);

    }

    public void updateBranch(Branch branch) {
        branchMapper.updateBranch(branch);
    }

    public void deleteBranch(Branch branch) {
        branchMapper.deleteBranch(branch);
    }

      public void activateBranch(Branch branch) {
        branchMapper.activateBranch(branch);
    }

    public List<Branch> searchBranch(String searchKey) {
        return branchMapper.searchBranch(searchKey);
    }

    public Branch getBranchById(Long id) {
        return branchMapper.getBranchById(id);
    }
}
