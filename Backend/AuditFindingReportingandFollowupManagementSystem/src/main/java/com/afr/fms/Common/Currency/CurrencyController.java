package com.afr.fms.Common.Currency;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;
   

    @GetMapping("/getAllCurrency")
    public ResponseEntity<List<Currency>> getAllCurrency(HttpServletRequest request) {
            try {
                List<Currency> currency = currencyService.getAllCurrency();
                return new ResponseEntity<>(currency, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
       
    }

}
