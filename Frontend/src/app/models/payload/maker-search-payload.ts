
export class MakerSearchPayload {

   branch_id?: number;
   tax_category_id?: number;
   reference_number?: number;
   status_id?: number;
   approved_date?: Date;
   user_id?: number;
   maker_date?: Date[];
   checked_date?: Date[];
   rejected_date?: Date[];

   branch_name?: String

   routeControl?: string;

}
