package com.afr.fms.Common.Dashboard.Higher_Officials;

import com.afr.fms.Common.Dashboard.Higher_Officials.Models.TotalsForPieChart_byModule;
import com.afr.fms.Common.Dashboard.Higher_Officials.Models.TotalsForStatChartE;
import com.afr.fms.Common.Dashboard.Higher_Officials.Models.TotalsForTimeSeriesModuleSpecificE;
import com.afr.fms.Common.Dashboard.Higher_Officials.Models.UnrectifiedForAllModulesE;

// package com.afr.fms.Branch_Audit.Dashboard.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/branch/dashboard/")
public class DashboardController_HigherOfficials {

  @Autowired
  DashboardService_HigherOfficials reportService;

  private List<TotalsForStatChartE> statData = new ArrayList<>();
  // private List<TotalsForTimeSeriesModuleSpecificE>
  // totalsForTimeSeriesModuleSpecific = new ArrayList<>();
  // private List<UnrectifiedForAllModulesE> unrectifiedForAllModules = new
  // ArrayList<>();
  private Random random = new Random();

  @GetMapping("getTotalsForStatChartE")
  public List<TotalsForStatChartE> getTotalsForStatChartE() {
    return reportService.getTotalsForStatChartE();
  }

  @GetMapping("getUnrectifiedForAllModulesE")
  public List<UnrectifiedForAllModulesE> getUnrectifiedForAllModulesE() {
    return reportService.getUnrectifiedForAllModulesE();
  }

  // @GetMapping("getTotalsForTimeSeriesIs_reportedE")
  // public String getTotalsForTimeSeriesIs_reportedE() {
  // return (
  // "[\r\n" + //
  // " {\"reported_timestamp\": \"2024-01-17\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-18\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-19\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-20\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-21\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-22\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-23\", \"reported_value\": 3.0}\r\n" + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesIs_reportedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_reportedE() {
    return reportService.getTotalsForTimeSeriesIs_reportedE();
  }

  // @GetMapping("getTotalsForTimeSeriesIs_approvedE")
  // public String getTotalsForTimeSeriesIs_approvedE() {
  // return (
  // "[\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-17\", \"approved_value\": 5},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-18\", \"approved_value\": 9},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-19\", \"approved_value\": 12},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-20\", \"approved_value\": 14},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-21\", \"approved_value\": 18},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-22\", \"approved_value\": 22},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-23\", \"approved_value\": 27}\r\n" + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesIs_approvedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_approvedE() {
    return reportService.getTotalsForTimeSeriesIs_approvedE();
  }

  // @GetMapping("getTotalsForTimeSeriesIs_rectifiedE")
  // public String getTotalsForTimeSeriesIs_rectifiedE() {
  // return (
  // "[\r\n" + //
  // " {\"rectified_timestamp\": \"2024-01-17\", \"rectified_value\": 3},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-18\", \"rectified_value\": 5},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-19\", \"rectified_value\": 9},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-20\", \"rectified_value\": 14},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-21\", \"rectified_value\": 22},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-22\", \"rectified_value\": 30},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-23\", \"rectified_value\": 38}\r\n" +
  // //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesIs_rectifiedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_rectifiedE() {
    return reportService.getTotalsForTimeSeriesIs_rectifiedE();
  }

  // @GetMapping("getTotalsForTimeSeriesIs_unrectifiedE")
  // public String getTotalsForTimeSeriesIs_unrectifiedE() {
  // return (
  // "[\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-17\", \"unrectified_value\":
  // 13},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-18\", \"unrectified_value\":
  // 20},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-19\", \"unrectified_value\":
  // 35},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-20\", \"unrectified_value\":
  // 45},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-21\", \"unrectified_value\":
  // 58},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-22\", \"unrectified_value\":
  // 70},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-23\", \"unrectified_value\": 85}\r\n"
  // + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesIs_unrectifiedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_unrectifiedE() {
    return reportService.getTotalsForTimeSeriesIs_unrectifiedE();
  }

  // @GetMapping("getTotalsForTimeSeriesMgt_reportedE")
  // public String getTotalsForTimeSeriesMgt_reportedE() {
  // return (
  // "[\r\n" + //
  // " {\"reported_timestamp\": \"2024-01-17\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-18\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-19\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-20\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-21\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-22\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-23\", \"reported_value\": 3.0}\r\n" + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesMgt_reportedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_reportedE() {
    return reportService.getTotalsForTimeSeriesMgt_reportedE();
  }

  // @GetMapping("getTotalsForTimeSeriesMgt_approvedE")
  // public String getTotalsForTimeSeriesMgt_approvedE() {
  // return (
  // "[\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-17\", \"approved_value\": 5},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-18\", \"approved_value\": 9},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-19\", \"approved_value\": 12},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-20\", \"approved_value\": 14},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-21\", \"approved_value\": 18},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-22\", \"approved_value\": 22},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-23\", \"approved_value\": 27}\r\n" + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesMgt_approvedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_approvedE() {
    return reportService.getTotalsForTimeSeriesMgt_approvedE();
  }

  // @GetMapping("getTotalsForTimeSeriesMgt_rectifiedE")
  // public String getTotalsForTimeSeriesMgt_rectifiedE() {
  // return (
  // "[\r\n" + //
  // " {\"rectified_timestamp\": \"2024-01-17\", \"rectified_value\": 3},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-18\", \"rectified_value\": 5},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-19\", \"rectified_value\": 9},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-20\", \"rectified_value\": 14},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-21\", \"rectified_value\": 22},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-22\", \"rectified_value\": 30},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-23\", \"rectified_value\": 38}\r\n" +
  // //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesMgt_rectifiedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_rectifiedE() {
    return reportService.getTotalsForTimeSeriesMgt_rectifiedE();
  }

  // @GetMapping("getTotalsForTimeSeriesMgt_unrectifiedE")
  // public String getTotalsForTimeSeriesMgt_unrectifiedE() {
  // return (
  // "[\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-17\", \"unrectified_value\":
  // 13},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-18\", \"unrectified_value\":
  // 20},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-19\", \"unrectified_value\":
  // 35},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-20\", \"unrectified_value\":
  // 45},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-21\", \"unrectified_value\":
  // 58},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-22\", \"unrectified_value\":
  // 70},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-23\", \"unrectified_value\": 85}\r\n"
  // + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesMgt_unrectifiedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_unrectifiedE() {
    return reportService.getTotalsForTimeSeriesMgt_unrectifiedE();
  }

  // @GetMapping("getTotalsForTimeSeriesInspection_reportedE")
  // public String getTotalsForTimeSeriesInspection_reportedE() {
  // return (
  // "[\r\n" + //
  // " {\"reported_timestamp\": \"2024-01-17\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-18\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-19\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-20\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-21\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-22\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-23\", \"reported_value\": 3.0}\r\n" + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesInspection_reportedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_reportedE() {
    return reportService.getTotalsForTimeSeriesInspection_reportedE();
  }

  // @GetMapping("getTotalsForTimeSeriesInspection_approvedE")
  // public String getTotalsForTimeSeriesInspection_approvedE() {
  // return (
  // "[\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-17\", \"approved_value\": 5},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-18\", \"approved_value\": 9},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-19\", \"approved_value\": 12},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-20\", \"approved_value\": 14},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-21\", \"approved_value\": 18},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-22\", \"approved_value\": 22},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-23\", \"approved_value\": 27}\r\n" + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesInspection_approvedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_approvedE() {
    return reportService.getTotalsForTimeSeriesInspection_approvedE();
  }

  // @GetMapping("getTotalsForTimeSeriesInspection_rectifiedE")
  // public String getTotalsForTimeSeriesInspection_rectifiedE() {
  // return (
  // "[\r\n" + //
  // " {\"rectified_timestamp\": \"2024-01-17\", \"rectified_value\": 3},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-18\", \"rectified_value\": 5},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-19\", \"rectified_value\": 9},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-20\", \"rectified_value\": 14},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-21\", \"rectified_value\": 22},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-22\", \"rectified_value\": 30},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-23\", \"rectified_value\": 38}\r\n" +
  // //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesInspection_rectifiedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_rectifiedE() {
    return reportService.getTotalsForTimeSeriesInspection_rectifiedE();
  }

  // @GetMapping("getTotalsForTimeSeriesInspection_unrectifiedE")
  // public String getTotalsForTimeSeriesInspection_unrectifiedE() {
  // return (
  // "[\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-17\", \"unrectified_value\":
  // 13},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-18\", \"unrectified_value\":
  // 20},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-19\", \"unrectified_value\":
  // 35},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-20\", \"unrectified_value\":
  // 45},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-21\", \"unrectified_value\":
  // 58},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-22\", \"unrectified_value\":
  // 70},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-23\", \"unrectified_value\": 85}\r\n"
  // + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesInspection_unrectifiedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_unrectifiedE() {
    return reportService.getTotalsForTimeSeriesInspection_unrectifiedE();
  }

  ////////////////////////////////////////////////////////

  // @GetMapping("getTotalsForTimeSeriesBranch_reportedE")
  // public String getTotalsForTimeSeriesBranch_reportedE() {
  // return (
  // "[\r\n" + //
  // " {\"reported_timestamp\": \"2024-01-17\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-18\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-19\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-20\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-21\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-22\", \"reported_value\": 3.0},\r\n" +
  // //
  // " {\"reported_timestamp\": \"2024-01-23\", \"reported_value\": 3.0}\r\n" + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesBranch_reportedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_reportedE() {
    return reportService.getTotalsForTimeSeriesBranch_reportedE();
  }

  // @GetMapping("getTotalsForTimeSeriesBranch_approvedE")
  // public String getTotalsForTimeSeriesBranch_approvedE() {
  // return (
  // "[\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-17\", \"approved_value\": 5},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-18\", \"approved_value\": 9},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-19\", \"approved_value\": 12},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-20\", \"approved_value\": 14},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-21\", \"approved_value\": 18},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-22\", \"approved_value\": 22},\r\n" + //
  // " {\"approved_timestamp\": \"2024-01-23\", \"approved_value\": 27}\r\n" + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesBranch_approvedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_approvedE() {
    return reportService.getTotalsForTimeSeriesBranch_approvedE();
  }

  // @GetMapping("getTotalsForTimeSeriesBranch_rectifiedE")
  // public String getTotalsForTimeSeriesBranch_rectifiedE() {
  // return (
  // "[\r\n" + //
  // " {\"rectified_timestamp\": \"2024-01-17\", \"rectified_value\": 3},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-18\", \"rectified_value\": 5},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-19\", \"rectified_value\": 9},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-20\", \"rectified_value\": 14},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-21\", \"rectified_value\": 22},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-22\", \"rectified_value\": 30},\r\n" +
  // //
  // " {\"rectified_timestamp\": \"2024-01-23\", \"rectified_value\": 38}\r\n" +
  // //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesBranch_rectifiedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_rectifiedE() {
    return reportService.getTotalsForTimeSeriesBranch_rectifiedE();
  }

  // @GetMapping("getTotalsForTimeSeriesBranch_unrectifiedE")
  // public String getTotalsForTimeSeriesBranch_unrectifiedE() {
  // return (
  // "[\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-17\", \"unrectified_value\":
  // 13},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-18\", \"unrectified_value\":
  // 20},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-19\", \"unrectified_value\":
  // 35},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-20\", \"unrectified_value\":
  // 45},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-21\", \"unrectified_value\":
  // 58},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-22\", \"unrectified_value\":
  // 70},\r\n" + //
  // " {\"unrectified_timestamp\": \"2024-01-23\", \"unrectified_value\": 85}\r\n"
  // + //
  // "]\r\n" + //
  // ""
  // );
  // }

  @GetMapping("getTotalsForTimeSeriesBranch_unrectifiedE")
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_unrectifiedE() {
    return reportService.getTotalsForTimeSeriesBranch_unrectifiedE();
  }

  @GetMapping("getTotalsForPieChart_byModule")
  public List<TotalsForPieChart_byModule> getTotalsForPieChart_byModule() {
    return reportService.getTotalsForPieChart_byModule();
  }

  @GetMapping("getTotalsForPieChart_byModule2")
  public String getTotalsForPieChart_byModule2() {
    return "aka";
  }
}
