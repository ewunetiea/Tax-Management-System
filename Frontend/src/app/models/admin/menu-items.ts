import { Functionalities } from "./functionalities";
import { MenuHeader } from "./menu-headers";

export class MenuItems {
    id?: number;
    header_id?: number;
    menuHeader?: MenuHeader;
    menuHeaders?: MenuHeader[];
    item_label?: string;
    item_icon?: string;
    item_to?: string;
    item_order?: number;
    item_badge?: string;
    maker_id?: number;
    maker_email?: string;
    status?: boolean;
    created_date?: string;
    updated_date?: string;
    delete?: boolean;
    permissions?: Functionalities[]
}
