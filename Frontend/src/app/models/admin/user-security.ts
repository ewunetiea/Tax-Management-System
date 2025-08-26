export class UserSecurity {
    id?: number;
    number_of_attempts?: number;
    user_id?: number;
    password_created_date?: Date;
    password_modified_date?: Date;
    lock_time?: Date;
    accountNonLocked?:boolean;
    credentialsNonExpired?: boolean;
    accountNonExpired?: boolean;
}