package com.afr.fms.Admin.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Service.BranchService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@RestController
@RequestMapping("/api")
public class BranchController {

	@Autowired
	private BranchService branchService;
	
	@Autowired
	private RecentActivityMapper recentActivityMapper;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(BranchController.class);

	@GetMapping("/branch")
	public ResponseEntity<List<Branch>> getBranches(HttpServletRequest request) {
		try {
			List<Branch> branches = branchService.getBranches();
			return new ResponseEntity<>(branches, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching branches", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/branch/active")
	public ResponseEntity<List<Branch>> getActiveBranches() {
		try {
			List<Branch> branches = branchService.getActiveBranches();
			return new ResponseEntity<>(branches, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching active branches", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/branch/{id}")
	public ResponseEntity<Branch> getBranchById(@PathVariable("id") Long id, HttpServletRequest request) {
		Branch branches = branchService.getBranchById(id);
		return new ResponseEntity<>(branches, HttpStatus.OK);

	}

	@PostMapping("/branch")
	public ResponseEntity<HttpStatus> saveBranch(@RequestBody Branch branch, HttpServletRequest request) {
		try {
			User user = new User();
			if (branch.getId() == null) {
				branchService.createBranch(branch);
				recentActivity.setMessage(branch.getName() + " branch is created ");
				user.setId(branch.getUser_id());
				recentActivity.setUser(user);
				recentActivityMapper.addRecentActivity(recentActivity);
			} else {
				branchService.updateBranch(branch);
				recentActivity.setMessage(branch.getName() + " branch info is updated ");
				user.setId(branch.getUser_id());
				recentActivity.setUser(user);
				recentActivityMapper.addRecentActivity(recentActivity);
			}

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving branch", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/branch/search")
	public ResponseEntity<Branch> searchBranches(@RequestParam String searchKey) {
		branchService.searchBranch(searchKey);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/branch/delete")
	public ResponseEntity<Branch> deleteBranch(@RequestBody List<Branch> branches, HttpServletRequest request) {
		try {

			for (Branch branch2 : branches) {
				branchService.deleteBranch(branch2);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while deleting branch", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/branch/activate")
	public ResponseEntity<Branch> activateBranch(@RequestBody List<Branch> branches, HttpServletRequest request) {
		try {
			for (Branch branch2 : branches) {
				branchService.activateBranch(branch2);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while activating branch", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
