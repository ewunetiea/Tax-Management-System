import { MenuItems } from "./menu-items";
import { Role } from "./role";

export class MenuHeader {
    id?: number;
    header?: string;
    target?: string;
    target_roles?: string;
    url?: string;
    icon?: string;
    badge?: string;
    command?: boolean;
    template?: boolean;
    menuItems?: MenuItems[];
    roles?: Role[];
    maker_id?: number;
    maker_email?: string;
    created_date?: string;
    updated_date?: string;
    status?: boolean;
    header_order?: number;
    role_names?: string[];
    delete?: boolean;
    super_menu?: boolean;
}
