import { Region } from "./region";

export class Branch {
    id?:number;
    name?:string;
    code?:string;
    region?:Region;
    status?:boolean;
    user_id?:number;
    total_records_paginator?: number;
}