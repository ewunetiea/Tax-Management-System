
export class MakerSearchPayload {

   branch_id?: number;
   tax_category_id?: number;
   reference_number?: string;
   status_id?: Number;
   user_id?: number;
   drafted_date?: Date[];
   maker_date?: Date[];
   checked_date?: Date[];
   rejected_date?: Date[];
   approved_date?: Date[];
   branch_name?: String
   routeControl?: string;



//  paginator payload   
    totalRecords: number = 0;
  currentPage?: number = 0; 
  pageSize?: number = 10;
  event_length?: number = 0;

}
