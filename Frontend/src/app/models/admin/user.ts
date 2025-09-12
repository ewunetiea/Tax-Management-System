import { Branch } from "./branch";
import { Functionalities } from "./functionalities";
import { JobPosition } from "./job-position";
import { Region } from "./region";
import { Role } from "./role";
import { UserSecurity } from "./user-security";

export class User {
  id?: number;
  first_name?: String;
  middle_name?: String;
  last_name?: String;
  phone_number?: String;
  email?: String;
  password?: String;
  status?: Boolean;
  roles?: Role[] = [];
  branch?: Branch;
  photoUrl?: string;
  gender?: String;
  user_security?: UserSecurity;
  region?: Region;
  admin_id?: Number;
  employee_id?: String;
  jobPosition?: JobPosition;
  userCopyFromHR?: any;
  category?: string;
  unseen_remark?: number;
  authenthication_media?: Boolean;
  banking?: string;
  special_user?: boolean;
  page_number?: number; // for paginator purpose only, it will not be saved in db
  page_size?: number; // for paginator purpose only, it will not be saved in db
  functionalities?: Functionalities[] = []; // This will hold the functionalities assigned to the user
  username?: string;
  editing ?: Boolean
  }
