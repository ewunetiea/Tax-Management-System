package com.afr.fms.Dashboard.maker;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor

public class MakerDatashboardPayload {

  private List <Integer>  card_data;
    private List <Integer> barchart_data;
  private List <Integer>  linechart_data;
  private List <Integer>  piechart_data;
   private List <Integer>  polar_data;
    
}







