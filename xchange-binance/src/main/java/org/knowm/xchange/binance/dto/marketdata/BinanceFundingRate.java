package org.knowm.xchange.binance.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;
import org.knowm.xchange.binance.BinanceAdapters;
import org.knowm.xchange.dto.marketdata.FundingRate;
import org.knowm.xchange.instrument.Instrument;

@Getter
@ToString
public class BinanceFundingRate extends FundingRate {

  //in fundingRate
//  private final Instrument instrument;
//  private final BigDecimal fundingRate1h;
//  private final BigDecimal fundingRate8h;
//  private final Date fundingRateDate;
//  private final long fundingRateEffectiveInMinutes;

  private final Instrument instrument;
  private final BigDecimal markPrice;
  private final BigDecimal indexPrice;
  private final BigDecimal estimatedSettlePrice;
  private final BigDecimal lastFundingRate;
  private final Date nextFundingTime;
  private final BigDecimal interestRate;
  private final Date time;

  public BinanceFundingRate(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("markPrice") BigDecimal markPrice,
      @JsonProperty("indexPrice") BigDecimal indexPrice,
      @JsonProperty("estimatedSettlePrice") BigDecimal estimatedSettlePrice,
      @JsonProperty("lastFundingRate") BigDecimal lastFundingRate,
      @JsonProperty("nextFundingTime") Date nextFundingTime,
      @JsonProperty("interestRate") BigDecimal interestRate,
      @JsonProperty("time") Date time) {
    //        .fundingRate8h(fundingRate)
//        .fundingRate1h(
//            fundingRate.divide(BigDecimal.valueOf(8), fundingRate.scale(), RoundingMode.HALF_EVEN))
//        .fundingRateDate(nextFundingTime)
//        .instrument(BinanceAdapters.adaptSymbol(symbol, true))

    super(
        BinanceAdapters.adaptSymbol(symbol, true),
        lastFundingRate.divide(BigDecimal.valueOf(8), lastFundingRate.scale(), RoundingMode.HALF_EVEN),
        lastFundingRate, nextFundingTime, 1);
    this.instrument = BinanceAdapters.adaptSymbol(symbol, true);
    this.markPrice = markPrice;
    this.indexPrice = indexPrice;
    this.estimatedSettlePrice = estimatedSettlePrice;
    this.lastFundingRate = lastFundingRate;
    this.nextFundingTime = nextFundingTime;
    this.interestRate = interestRate;
    this.time = time;
  }
}
