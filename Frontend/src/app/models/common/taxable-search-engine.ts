export class TaxableSearchEngine {
    branch_id?:number;
    tax_category_id?:number;
    reference_number?:number;
    router_status?: string;
    maked_date?: Date[];
    checked_date?: Date[];
    approved_date?: Date[];
    rejected_date?: Date[];
    document_type?: string;
    user_id?: number;
    search_by?: string; 
    director_id?: number;

    // For Paginatoruser_id?: number;
    totalRecords: number = 0;
    currentPage: number = 0; 
    pageSize: number = 5;
    event_length: number = 0;
}