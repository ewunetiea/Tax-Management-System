import { TaxFile } from "./tax-file";



export class Tax {
    id?: number;
    mainGuid?: String;
    from_?: number;
    sendTo_?: number;
    taxCategory?: number;
    noOfEmployee?: number;
    taxableAmount?: number;
    taxWithHold?: number;
    incometaxPoolAmount?: number;
    graduatetaxPool?: number;
    graduatetaxPool1?: number;
    graduaTotBasSalary?: number;
    graduateTotaEmployee?: number;
    graduatetaxWithHold?: number;
    taxCategoryList?: String;
    maker_name?: String;
    maker_date?: Date;
    checker_name?: String;
    checked_Date?: Date;
    updated_user_name?: String;
    updated_event_date?: Date;
    from_List?: String;
    sendTo_List?: String;
    Category_List?: String;
    FileDetail?: number;
    status?: number;
    taxFile?: TaxFile[] = []
    remark?: String
    reference_number?: String
   

    initiator_branch?:String;   // The branch that initiated the tax
  destination_branch?: String; // The branch that receives/sends to


}