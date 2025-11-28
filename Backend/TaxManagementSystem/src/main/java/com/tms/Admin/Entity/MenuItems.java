package com.tms.Admin.Entity;

import java.util.List;

import com.tms.Common.Entity.Functionalities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItems {
	private Long id;
	private Long header_id;
	private String item_label;
	private String item_icon;
	private String item_to;
	private String item_badge;
	private Long maker_id;
	private String maker_email;
	private MenuHeader menuHeader;
	private List<MenuHeader> menuHeaders;
	private String created_date;
	private String updated_date;
	private boolean status;
	private int item_order;
	private boolean delete;
	private List<Functionalities> permissions;

}
