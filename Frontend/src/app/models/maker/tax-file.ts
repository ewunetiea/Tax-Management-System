import { SafeResourceUrl } from "@angular/platform-browser";
import { Transaction } from "./transaction";

export class TaxFile {
    Id?: String;
    fileName?: String;
    extension?: String;
    supportId?: String;
    file?: Blob;
    fileType?: string;         // type of file (image/pdf/word)
    pdfUrl?: string;           // blob URL for PDF download
    safePdfUrl?: SafeResourceUrl;
    tax_id?: number;
}
