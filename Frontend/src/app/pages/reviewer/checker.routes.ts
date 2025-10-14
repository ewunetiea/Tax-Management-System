import { AuthGuard } from "../../service/admin/auth.guard";
import { ManagetaxComponent } from "./managetax/managetax.component";

export default [
  {
  path: 'manage_tax/:status',
  component: ManagetaxComponent,
  canActivate: [AuthGuard],
  data: {
    roles: ['ROLE_REVIEWER'],
  }
}

];
