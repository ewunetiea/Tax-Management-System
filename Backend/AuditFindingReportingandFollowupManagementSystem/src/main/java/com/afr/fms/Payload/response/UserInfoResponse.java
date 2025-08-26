package com.afr.fms.Payload.response;

import java.util.List;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.Region;

public class UserInfoResponse {
	private Long id;
	private Long id_login_tracker;
	private String username;
	private String email;
	private String photoUrl;
	private List<String> roles;
	private String title;
	private String category;
	private String banking;
	

	private Branch branch;
	private Region region;

	

	public UserInfoResponse(Long id, Long id_login_tracker, String username, String email, String photoUrl,
			String title, String category,Branch branch, Region region, String banking, List<String> roles) {
		this.id = id;
		this.id_login_tracker = id_login_tracker;
		this.username = username;
		this.email = email;
		this.photoUrl = photoUrl;
		this.title = title;
		this.category = category;
		this.branch = branch;
		this.region = region;
		this.roles = roles;
		this.banking = banking;
	}

	public String getBanking() {
		return banking;
	}

	public void setBanking(String banking) {
		this.banking = banking;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Branch getBranch() {
		return branch;
	  }
	
	  public void setBranch(Branch branch) {
		this.branch = branch;
	  }
	
	  public Region getRegion() {
		return region;
	  }
	
	  public void setRegion(Region region) {
		this.region = region;
	  }
	

	public List<String> getRoles() {
		return roles;
	}

	public Long getId_login_tracker() {
		return id_login_tracker;
	}

	public void setId_login_tracker(Long id_login_tracker) {
		this.id_login_tracker = id_login_tracker;
	}
}
