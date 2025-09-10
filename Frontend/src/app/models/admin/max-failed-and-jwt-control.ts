
export class MaxFailedAndJwtControl {
    id?: number;
    maximum_attempt?: number;
    lock_time?: number;
    credential_expiration? : number;
    jwt_expiration?:number;
    jwt_refresh_token_expiration?:number
    jwt_secret? : string;
}
