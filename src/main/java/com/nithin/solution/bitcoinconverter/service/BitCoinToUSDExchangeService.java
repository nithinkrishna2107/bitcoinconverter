package com.nithin.solution.bitcoinconverter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.nithin.solution.bitcoinconverter.util.BitCoinToUSDExchangerConstants.BITCOIN_TO_USD_URL;
import static com.nithin.solution.bitcoinconverter.util.BitCoinToUSDExchangerConstants.BITCOIN_TO_USD_WITH_TIMESTAMP_URL;

@Service
public class BitCoinToUSDExchangeService {

   @Autowired
   RestTemplate restTemplate;

   @Value("${app.checkPeriod}")
   private int checkPeriod;

   public List<String> getHistoricalPrices(LocalDate startDate,LocalDate endDate){

      List<String> prices = new ArrayList<>();
      long noOfHistoricalDays = ChronoUnit.DAYS.between(startDate, endDate);
      long noOfPricePointsRequiredPerDay = 24/checkPeriod;
      long totalPricePointsRequired= noOfHistoricalDays*noOfPricePointsRequiredPerDay;

      ZonedDateTime nextCheckPointDateTime =startDate.atStartOfDay(ZoneId.systemDefault());
      long nextCheckPointDateTimeEpochSeconds = nextCheckPointDateTime.toEpochSecond();
      String nextTimeStamp = String.valueOf(nextCheckPointDateTimeEpochSeconds);

      for(int pricePoint=1;pricePoint<=totalPricePointsRequired;pricePoint++){
         LinkedHashMap priceMap = restTemplate
                  .getForObject(BITCOIN_TO_USD_WITH_TIMESTAMP_URL.concat(nextTimeStamp), LinkedHashMap.class);
         prices.add(String.valueOf(priceMap.get("USD")));
         nextCheckPointDateTime = nextCheckPointDateTime.plusHours(noOfPricePointsRequiredPerDay);
         nextTimeStamp = String.valueOf(nextCheckPointDateTime.toEpochSecond());
      }
      return prices;
   }

   public String getLatestBitcoingToUSDPrice(){
      LinkedHashMap latestPriceMap = restTemplate.getForObject(BITCOIN_TO_USD_URL, LinkedHashMap.class);
      return String.valueOf(latestPriceMap.get("USD"));
   }
}
