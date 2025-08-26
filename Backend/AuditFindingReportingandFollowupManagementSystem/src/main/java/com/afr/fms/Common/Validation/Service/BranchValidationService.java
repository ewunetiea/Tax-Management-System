package com.afr.fms.Common.Validation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Mapper.BranchMapper;

@Service
public class BranchValidationService {

	@Autowired
	private BranchMapper branchMapper;

	public Branch checkBranchName(String name) {

		for (Branch branch : branchMapper.getBranches()) {
			try {
				if (branch.getName().equalsIgnoreCase(name)) {
					return branch;
				}
			} catch (Exception e) {
				System.out.println(e);

			}
		}
		return null;
	}

	public Branch checkBranchCode(String code) {

		for (Branch branch : branchMapper.getBranches()) {

			try {
				if (branch.getCode().equalsIgnoreCase(code)) {
					return branch;
				}
			} catch (Exception e) {
				System.out.println(e);

			}
		}
		return null;
	}
}
