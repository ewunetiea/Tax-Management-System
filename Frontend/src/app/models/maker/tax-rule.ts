import { Transaction } from "./transaction";

export class TaxRule {

    id?: number;
    taxName?: string;
    taxRate?: number;
    taxType?: String
    effectiveDate?: Date;
    status?: string

    transaction?:Transaction

}
