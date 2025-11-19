import { TaxFile } from "./tax-file";

export class Tax {
  id?: number;
  mainGuid?: String
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
  taxCategoryList?: string;
  maker_name?: string;
  maker_date?: Date;
  checker_name?: string;
  checked_Date?: Date;
  updated_user_name?: string;
  updated_event_date?: Date;
  from_List?: string;
  sendTo_List?: string;
  Category_List?: String;
  FileDetail?: number;
  status?: number;
  taxFile?: TaxFile[] = []
  previouseTaxFile?: TaxFile[] = []

  remark?: string
  reference_number?: string
  initiator_branch?: string;   // The branch that initiated the tax
  destination_branch?: string; // The branch that receives/sends to
  taxType?: string; // tax category type
  user_id?: number;
  rejector_checker_id?: number;
  checker_rejected_date?: Date;
  checker_rejected_reason?: string;
  rejector_approver_id?: number;
  approver_rejected_reason?: string;
  approver_rejected_date?: Date;
  approver_name?: string;
  approved_date?: Date;
  isFileEdited?: boolean = false;
  drafted_date?: Date;
  maker_id ?: number

}