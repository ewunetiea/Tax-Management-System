export class TaxableSearchEngine {
    branch_id?:number;
    tax_category_id?:number;
    reference_number?:number;
    status_id?: number;
    maked_date?:Date;
    checked_date?:Date;
    approved_date?:Date;
    rejected_date?: Date;
    document_type?: string;
    user_id?: number;
}