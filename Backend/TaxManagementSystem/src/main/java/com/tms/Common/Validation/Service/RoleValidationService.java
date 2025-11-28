package com.tms.Common.Validation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.Admin.Entity.Role;
import com.tms.Admin.Mapper.RoleMapper;

@Service
public class RoleValidationService {

	@Autowired
	private RoleMapper roleMapper;

	public Role checkRoleCode(String code) {
		for (Role role : roleMapper.getRoles()) {
			try {
				if (role.getCode().equalsIgnoreCase(code)) {
					return role;
				}
			} catch (Exception e) {
				
			}
		}
		return null;
	}

	public Role checkRoleName(String name) {
		for (Role role : roleMapper.getRoles()) {
			try {
				if (role.getName().equalsIgnoreCase(name)) {
					return role;
				}
			} catch (Exception e) {
				

			}
		}
		return null;
	}
}
