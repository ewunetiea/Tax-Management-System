import { Role } from "./role";

export class MenuRole {
  id?: number;
  header_id?: number;
  role_id?: number;
  roles?: Role[];
  maker_id?: number;
  created_date?: string;
  updated_date?: string;
  status?: boolean;
}
