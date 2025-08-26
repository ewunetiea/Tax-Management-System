import { RecentActivity } from "./recent-activity";

export class AdminDashboard {
    directorates?: String[];
  permission_region_branch?: Number[];
  polar_data?: Number[];
  card_data?: Number[];
  stacked_bar_chart_data?: Number[];
  bar_chart_data?: Number[];
  doughnut_data?: Number[];
  age_data?: Number[];
  line_chart_data?: Number[];
  horizontal_bar_chart_data?: Number[];
  recentActivity?: RecentActivity[] = [];
  roles_length_IS_MGT_INS?: Number[];
  roles_name_BFA?: String[];
}