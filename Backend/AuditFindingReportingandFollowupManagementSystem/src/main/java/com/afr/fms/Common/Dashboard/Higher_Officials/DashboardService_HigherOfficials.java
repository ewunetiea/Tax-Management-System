package com.afr.fms.Common.Dashboard.Higher_Officials;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.afr.fms.Common.Dashboard.Higher_Officials.Models.TotalsForPieChart_byModule;
import com.afr.fms.Common.Dashboard.Higher_Officials.Models.TotalsForStatChartE;
import com.afr.fms.Common.Dashboard.Higher_Officials.Models.TotalsForTimeSeriesModuleSpecificE;
import com.afr.fms.Common.Dashboard.Higher_Officials.Models.UnrectifiedForAllModulesE;

@Service
public class DashboardService_HigherOfficials {

  @Autowired
  DashboardMapper_HigherOfficials dashboardMapper;

  List<TotalsForTimeSeriesModuleSpecificE> total_is = new ArrayList<>();

  public List<TotalsForStatChartE> getTotalsForStatChartE() {
    return dashboardMapper.getTotalsForStatChartE();
  }

  public List<UnrectifiedForAllModulesE> getUnrectifiedForAllModulesE() {
    List<UnrectifiedForAllModulesE> all = new ArrayList<>();
    all.add(dashboardMapper.getUnrectifiedForAllModules_is());
    all.add(dashboardMapper.getUnrectifiedForAllModules_inspection());
    all.add(dashboardMapper.getUnrectifiedForAllModules_branch());
    all.add(dashboardMapper.getUnrectifiedForAllModules_mgt());
    return all;
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_reportedE() {
    return dashboardMapper.getTotalsForTimeSeriesIs_reportedE();
  }

	public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_approvedE() {
	System.out.println("service code e42");
    return dashboardMapper.getTotalsForTimeSeriesIs_approvedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_rectifiedE() {
    return dashboardMapper.getTotalsForTimeSeriesIs_rectifiedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_unrectifiedE() {
    return dashboardMapper.getTotalsForTimeSeriesIs_unrectifiedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_reportedE() {
    return dashboardMapper.getTotalsForTimeSeriesMgt_reportedE();
  }

	public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_approvedE() {
	System.out.println("service code e42");
    return dashboardMapper.getTotalsForTimeSeriesMgt_approvedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_rectifiedE() {
    return dashboardMapper.getTotalsForTimeSeriesMgt_rectifiedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_unrectifiedE() {
    return dashboardMapper.getTotalsForTimeSeriesMgt_unrectifiedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_reportedE() {
    return dashboardMapper.getTotalsForTimeSeriesInspection_reportedE();
  }

	public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_approvedE() {
	System.out.println("service code e42");
    return dashboardMapper.getTotalsForTimeSeriesInspection_approvedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_rectifiedE() {
    return dashboardMapper.getTotalsForTimeSeriesInspection_rectifiedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_unrectifiedE() {
    return dashboardMapper.getTotalsForTimeSeriesInspection_unrectifiedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_reportedE() {
    return dashboardMapper.getTotalsForTimeSeriesBranch_reportedE();
  }

	public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_approvedE() {
	System.out.println("service code e42");
    return dashboardMapper.getTotalsForTimeSeriesBranch_approvedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_rectifiedE() {
    return dashboardMapper.getTotalsForTimeSeriesBranch_rectifiedE();
  }

  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_unrectifiedE() {
    return dashboardMapper.getTotalsForTimeSeriesBranch_unrectifiedE();
  }

  public List<TotalsForPieChart_byModule> getTotalsForPieChart_byModule() {
    return dashboardMapper.getTotalsForPieChart_byModule();
  }

}
