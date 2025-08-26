package com.afr.fms.Admin.Entity;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuRole {
	private Long id;
	private Long header_id;
	private Long role_id;
	private List<Role> roles;
	private Long maker_id;
	private boolean status;
	private String created_date;
	private String updated_date;
}
