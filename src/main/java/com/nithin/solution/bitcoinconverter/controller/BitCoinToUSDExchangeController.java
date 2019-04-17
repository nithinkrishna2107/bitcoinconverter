package com.nithin.solution.bitcoinconverter.controller;

import com.nithin.solution.bitcoinconverter.service.BitCoinToUSDExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bitcoinToUSD")
public class BitCoinToUSDExchangeController {

   private static final Logger logger = LoggerFactory.getLogger(BitCoinToUSDExchangeController.class);

   private BitCoinToUSDExchangeService bitCoinToUSDExchangeService;

   @Autowired
   public BitCoinToUSDExchangeController(BitCoinToUSDExchangeService bitCoinToUSDExchangeService){
      this.bitCoinToUSDExchangeService = bitCoinToUSDExchangeService;
   }

   @GetMapping
   @RequestMapping("/latestPrice")
   public ResponseEntity<String> getLatestBitcoingToUSDPrice() {
      logger.info("Fetching latest bitcoin to USD price ");
      try {
         return ResponseEntity.ok(bitCoinToUSDExchangeService.getLatestBitcoingToUSDPrice());
      } catch (Exception exception) {
         logger.info("Exception occured while fetching latest bitcoin to USD price ");
         return ResponseEntity.notFound().build();
      }
   }

   @GetMapping
   @RequestMapping("/historicalPrices")
   public ResponseEntity<List<String>> getHistoricalBitcoingToUSDPrices(
            @RequestParam(value = "startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
      logger.info("Fetching historical prices of bitcoin to USD exchange ");
      try {
         List<String> prices = bitCoinToUSDExchangeService.getHistoricalPrices(startDate, endDate);
         return ResponseEntity.ok(prices);

      } catch (Exception exception) {
         logger.info("Exception occured while Fetching historical prices of bitcoin to USD exchange ");
         return ResponseEntity.notFound().build();
      }
   }
}
