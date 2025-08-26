import { User } from "../admin/user";

export class Report {
  user_id?: number;
  branch_code?: String;
  startDateTime?: String;
  endDateTime?: String;
  action_date?: string[];
  content?:String;
  age?:Number;
  age_range?:Number[];
  users?:User[]
  user_ids?:Number[]
}
