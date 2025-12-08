import { User } from "../admin/user";

export class Report {
  user_id?: number;
  branch_code?: string;

  action_date?: Date[];
  content?:string;
  age?:number;
  age_range?:number[];
  users?:User[]
  user_ids?:number[]
}
