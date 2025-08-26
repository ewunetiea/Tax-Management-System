package com.afr.fms.Admin.Entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuHeader {

	private Long id;
	private String header;
	private List<MenuItems> menuItems;
	private List<Role> roles;
	private Long maker_id;
	private String maker_email;
	private String target;
	private String created_date;
	private String updated_date;
	private int header_order;
	private String url;
	private String icon;
	private String badge;
	private boolean command;
	private boolean template;
	private boolean status;
	private boolean super_menu;
	private List<String> role_names;
	private String target_roles;
	private boolean delete;
}
