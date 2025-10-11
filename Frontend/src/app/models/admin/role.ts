import { Functionalities } from "./functionalities";
import { JobPosition } from "./job-position";

export class Role {
id?: any;
  code?: string;
  name?: string;
  description?: string;
  role_level?: number;
  role_position?: string;
  status?: boolean;
  dynamic_menu?: boolean;
  jobPositions?: JobPosition[];
  functionalities?: Functionalities[];
}