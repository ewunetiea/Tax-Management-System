import { SafeResourceUrl } from "@angular/platform-browser";
import { User } from "../admin/user";

export class Announcement {

    id?: number;
    title?: string;
    message?: string
    posted_by?: number
    user?: User
    audience?: string;
    created_date?: Date | null;
    expiry_date?: Date | null;
        image?: string;

  fileType?: string;         // type of file (image/pdf/word)
  pdfUrl?: string;           // blob URL for PDF download
  safePdfUrl?: SafeResourceUrl; 
  postedBy?: String


}