import { RecentActivity } from "./recent-activity";

export class AdminDashboard {
  directorates?: String[];
  permission_region_branch?: number[];
  polar_data?: number[];
  card_data?: number[];
  stacked_bar_chart_data?: number[];
  bar_chart_data?: number[];
  doughnut_data?: number[];
  age_data?: number[];
  line_chart_data?: number[];
  horizontal_bar_chart_data?: number[];
  recentActivity?: RecentActivity[] = [];
  roles_length_IS_MGT_INS?: number[];
  roles_name_BFA?: String[];
}