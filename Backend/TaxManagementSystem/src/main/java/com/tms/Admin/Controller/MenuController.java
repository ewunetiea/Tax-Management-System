package com.tms.Admin.Controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tms.Admin.Entity.MenuHeader;
import com.tms.Admin.Entity.MenuHeaderItems;
import com.tms.Admin.Entity.MenuItemPermissions;
import com.tms.Admin.Entity.MenuItems;
import com.tms.Admin.Entity.MenuRole;
import com.tms.Admin.Service.MenuService;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

	@PostMapping("/menu_item")
	public ResponseEntity<?> createMenuItem(@RequestBody @Validated MenuItems menuItems, HttpServletRequest request) {
		try {
			menuService.createMenuItem(menuItems);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error while creating/editing menu items: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/menu_role")
	public ResponseEntity<?> manageMenuRole(@RequestBody @Validated MenuRole menuRole, HttpServletRequest request) {

		try {
			menuService.manageMenuRole(menuRole);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error while mapping menu header with roles: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/menu_header_items")
	public ResponseEntity<?> manageMenuHeaderItems(@RequestBody @Validated MenuHeaderItems menuHeaderItems,
			HttpServletRequest request) {

		try {
			menuService.menuHeaderItems(menuHeaderItems);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error while mapping menu header with menu items: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/menu_item_permission")
	public ResponseEntity<?> manageItemPermissions(@RequestBody @Validated MenuItemPermissions menuItemPermissions,
			HttpServletRequest request) {

		try {
			menuService.manageItemPermissions(menuItemPermissions);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error while mapping menu item with permissions: ", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/menu_header")
	public ResponseEntity<?> createMenuHeader(@RequestBody @Validated MenuHeader menuHeader,
			HttpServletRequest request) {

		try {
			menuService.createMenuHeader(menuHeader);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error while creating/editing menu header: ", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/menu_item")
	public ResponseEntity<List<MenuItems>> getMenuItems(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(menuService.getMenuItems(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while fetching menu items: ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/menu_header")
	public ResponseEntity<List<MenuHeader>> getMenuHeaders(HttpServletRequest request) {

		try {
			return new ResponseEntity<>(menuService.getMenuHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while fetching menu headers: ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/menu")
	public ResponseEntity<List<MenuHeader>> getMenuByRole(@RequestBody @Validated MenuHeader menuHeader,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(menuService.getMenuByRole(menuHeader), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while fetching menu header mapping with roles: ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/menu_headers")
	public ResponseEntity<List<MenuHeader>> getCommonMenuHeaders(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(menuService.getCommonMenuHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while fetching common menu headers: ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("menu_item_status")
	public ResponseEntity<HttpStatus> manageMenuItemsStatus(@RequestBody List<MenuItems> menuItems,
			HttpServletRequest request) {

		try {
			menuService.manageMenuItemsStatus(menuItems);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while managing menu item status: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("menu_header_status")
	public ResponseEntity<HttpStatus> manageMenuHeaderStatus(@RequestBody List<MenuHeader> menuHeaders,
			HttpServletRequest request) {

		try {
			menuService.manageMenuHeaderStatus(menuHeaders);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while managing menu header status: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
