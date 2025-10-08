import { Branch } from "./branch";

export class PaginatorPayLoad {
    user_id?: number;
    totalRecords: number = 0;
    currentPage: number = 0; 
    pageSize: number = 5;
    event_length: number = 0;
    branch_id?: number;
  }