import { User } from './user';

export class Contact {
    id?: number;
    first_name?: String;
    last_name?: String;
    phone_number?: String;
    email?: String;
    registered_by?: number;
    user?: User;
    title?: String;
}
