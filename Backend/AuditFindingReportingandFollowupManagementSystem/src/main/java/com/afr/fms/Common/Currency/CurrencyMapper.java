package com.afr.fms.Common.Currency;
import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CurrencyMapper {

    @Select("select * from currency where currency_code <> 'ETB'")
    public List<Currency> getAllCurrency();

    @Select("select * from currency where currency_code=#{currency_code}")
    public Currency getCurrencyByCode(String currency_code);
}
