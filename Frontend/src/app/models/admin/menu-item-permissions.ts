import { Functionalities } from "./functionalities";

export class MenuItemPermissions {
    id?: number;
    item_id?: number;
    permission_id?: number;
    permissions?: Functionalities[];
    maker_id?: number;
    created_date?: string;
    updated_date?: string;
    status?: boolean;
}
