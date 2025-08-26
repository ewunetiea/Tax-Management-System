import { User } from "./user";

export class Feedback {
    id?: number;
    user_id?: number;
    feedback?:String
    user?: User;
    response?: String;
}
