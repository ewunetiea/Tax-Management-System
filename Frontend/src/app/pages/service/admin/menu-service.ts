import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.prod';
import { MenuItems } from '../../../models/admin/menu-items';
import { MenuHeader } from '../../../models/admin/menu-headers';
import { MenuRole } from '../../../models/admin/menu-roles';
import { MenuHeaderItems } from '../../../models/admin/menu-header-items';
import { MenuItemPermissions } from '../../../models/admin/menu-item-permissions';
const baseUrl = environment.backendUrl;

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  constructor(private httpClient: HttpClient) { }

  getMenuItems(): Observable<MenuItems[]> {
    return this.httpClient.get<MenuItems[]>(`${baseUrl}/menu/menu_item`);
  }

  getMenuHeaders(): Observable<MenuHeader[]> {
    return this.httpClient.get<MenuHeader[]>(`${baseUrl}/menu/menu_header`);
  }

  getCommonMenuHeaders(): Observable<MenuHeader[]> {
    return this.httpClient.get<MenuHeader[]>(`${baseUrl}/menu/menu_headers`);
  }

  getMenuByRole(role_names: MenuHeader): Observable<MenuHeader[]> {
    return this.httpClient.post<MenuHeader[]>(`${baseUrl}/menu/menu`, role_names);
  }

  addMenuItem(menuItem: MenuItems): Observable<any> {
    return this.httpClient.post(`${baseUrl}/menu/menu_item`, menuItem);
  }

  addMenuHeader(menuHeader: MenuHeader): Observable<any> {
    return this.httpClient.post(`${baseUrl}/menu/menu_header`, menuHeader);
  }

  manageMenuItemStatus(menuItems: MenuItems[]): Observable<any> {
    return this.httpClient.post(`${baseUrl}/menu/menu_item_status`, menuItems);
  }

  manageMenuRoles(menuRole: MenuRole): Observable<any> {
    return this.httpClient.post(`${baseUrl}/menu/menu_role`, menuRole);
  }

  manageMenuHeaderMenuItems(menuHeaderItems: MenuHeaderItems): Observable<any> {
    return this.httpClient.post(`${baseUrl}/menu/menu_header_items`, menuHeaderItems);
  }

  manageItemPermissions(menuItemPermissions: MenuItemPermissions): Observable<any> {
    return this.httpClient.post(`${baseUrl}/menu/menu_item_permission`, menuItemPermissions);
  }

  manageMenuHeaderStatus(menuHeader: MenuHeader[]): Observable<any> {
    return this.httpClient.post(`${baseUrl}/menu/menu_header_status`, menuHeader);
  }
  
}
