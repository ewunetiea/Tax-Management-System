
export class PaginatorPayLoad {
    user_id?: number;
    totalRecords: number = 0;
    currentPage: number = 0; 
    pageSize: number = 5;
    event_length: number = 0;
    category?: String;
    audit_status?: String;
    banking?: String;
    audit_type?: string;
    user_role?: string;
    searchText?: string;
  }