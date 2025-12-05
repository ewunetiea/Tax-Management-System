import { SafeResourceUrl } from "@angular/platform-browser";
import { User } from "../admin/user";
import { AnnouncementFile } from "./announcementFile";

export class Announcement {
  id?: number;
  title?: string;
  message?: string;
  posted_by?: number;
  user?: User;
  audience?: string;
  created_date?: Date | null;
  expiry_date?: Date | null;
  image?: string;
  fileType?: string;         
  pdfUrl?: string;           
  safePdfUrl?: SafeResourceUrl;
  postedBy?: string;
  isFileEdited?: boolean;
  fileExsistance?: string;
  mainGuid?: string;
  previouseAnnouncementFile?: AnnouncementFile[];  
  announcementFile?: AnnouncementFile[];           
}
