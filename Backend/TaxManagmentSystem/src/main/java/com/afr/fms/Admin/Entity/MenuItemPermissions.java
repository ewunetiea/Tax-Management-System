package com.afr.fms.Admin.Entity;

import java.util.List;

import com.afr.fms.Common.Entity.Functionalities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemPermissions {
	private Long id;
	private Long item_id;
	private Long permission_id;
	private List<Functionalities> permissions;
	private Long maker_id;
	private boolean status;
	private String created_date;
	private String updated_date;
}
