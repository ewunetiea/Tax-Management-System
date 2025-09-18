package com.afr.fms.maker.controller;

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
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.maker.entity.Account;
import com.afr.fms.maker.service.AccountService;

@RestController
@RequestMapping("/api/account")
public class AccountController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private RecentActivityMapper recentActivityMapper;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@GetMapping("/fetch")
	public ResponseEntity<List<Account>> getAccounts(HttpServletRequest request) {
		try {
			List<Account> accounts = accountService.getAccounts();
			return new ResponseEntity<>(accounts, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching accounts", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/active")
	public ResponseEntity<List<Account>> getActiveAccounts() {
		try {
			List<Account> accounts = accountService.getActiveAccounts();
			return new ResponseEntity<>(accounts, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching active accounts", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable("id") Long id, HttpServletRequest request) {
		Account accounts = accountService.getAccountById(id);
		return new ResponseEntity<>(accounts, HttpStatus.OK);

	}

	@PostMapping("/create")
	public ResponseEntity<HttpStatus> saveAccount(@RequestBody Account account, HttpServletRequest request) {
		try {
			User user = new User();
			if (account.getId() == null) {
				accountService.createAccount(account);
				recentActivity.setMessage(account.getAccountName() + " account is created ");
				user.setId(account.getUser_id());
				recentActivity.setUser(user);
				recentActivityMapper.addRecentActivity(recentActivity);
			} else {
				accountService.updateAccount(account);
				recentActivity.setMessage(account.getAccountName() + " account info is updated ");
				user.setId(account.getUser_id());
				recentActivity.setUser(user);
				recentActivityMapper.addRecentActivity(recentActivity);
			}

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving account", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/search")
	public ResponseEntity<Account> searchAccounts(@RequestParam String searchKey) {
		accountService.searchAccount(searchKey);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/delete")
	public ResponseEntity<Account> deleteAccount(@RequestBody List<Account> accounts, HttpServletRequest request) {
		try {

			for (Account acc : accounts) {
				accountService.deleteAccount(acc);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while deleting account", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate")
	public ResponseEntity<Account> activateAccount(@RequestBody List<Account> Accounts, HttpServletRequest request) {
		try {
			for (Account acc : Accounts) {
				accountService.activateAccount(acc);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while activating account", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
