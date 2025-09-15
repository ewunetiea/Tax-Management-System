import { Functionalities } from "./functionalities";
import { JobPosition } from "./job-position";

export class Role {
id?: any;
  code?: String;
  name?: String;
  description?: String;
  role_level?: Number;
  role_position?: String;
  status?: boolean;
  dynamic_menu?: boolean;
  jobPositions?: JobPosition[];
  functionalities?: Functionalities[];
}