package com.afr.fms.Admin.Entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuHeaderItems {
	private Long id;
	private Long header_id;
	private Long item_id;
	private List<MenuItems> menuItems;
	private Long creator_id;
	private String created_date;
	private String updated_date;
}
