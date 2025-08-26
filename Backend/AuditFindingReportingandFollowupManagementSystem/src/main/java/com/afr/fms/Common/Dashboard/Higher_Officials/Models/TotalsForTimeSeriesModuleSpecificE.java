package com.afr.fms.Common.Dashboard.Higher_Officials.Models;


import java.sql.Date;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TotalsForTimeSeriesModuleSpecificE {

  private String reported_timestamp;
  private Double reported_value;
  private String approved_timestamp;
  private Double approved_value;
  private String rectified_timestamp;
  private Double rectified_value;
  private String unrectified_timestamp;
  private Double unrectified_value;

}
