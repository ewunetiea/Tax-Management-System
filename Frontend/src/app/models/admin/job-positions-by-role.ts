import { JobPosition } from "./job-position";
import { Role } from "./role";

export class JobPositionsByRole {
    id?: number;
    role?: Role;
    job_positions?: JobPosition[]
}
