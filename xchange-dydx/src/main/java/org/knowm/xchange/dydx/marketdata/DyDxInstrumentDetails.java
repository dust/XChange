package org.knowm.xchange.dydx.marketdata;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class DyDxInstrumentDetails {

  /**
   * "BTC-USD": {
   *       "clobPairId": "0",
   *       "ticker": "BTC-USD",
   *       "status": "ACTIVE",
   *       "oraclePrice": "87405.74",
   *       "priceChange24H": "2222.25835",
   *       "volume24H": "38995948.5954",
   *       "trades24H": 10463,
   *       "nextFundingRate": "0.00001121875",
   *       "initialMarginFraction": "0.02",
   *       "maintenanceMarginFraction": "0.012",
   *       "openInterest": "753.7181",
   *       "atomicResolution": -10,
   *       "quantumConversionExponent": -9,
   *       "tickSize": "1",
   *       "stepSize": "0.0001",
   *       "stepBaseQuantums": 1000000,
   *       "subticksPerTick": 100000,
   *       "marketType": "CROSS",
   *       "openInterestLowerCap": "0",
   *       "openInterestUpperCap": "0",
   *       "baseOpenInterest": "782.0931",
   *       "defaultFundingRate1H": "0"
   *     },
   */

  @JsonProperty("clobPairId")
  private String clobPairId;

  @JsonProperty("ticker")
  private String ticker;

  @JsonProperty("status")
  private String status;

  @JsonProperty("oraclePrice")
  private BigDecimal oraclePrice;

  @JsonProperty("priceChange24H")
  private BigDecimal priceChange24H;

  @JsonProperty("volume24H")
  private BigDecimal volume24H;

  @JsonProperty("nextFundingRate")
  private BigDecimal nextFundingRate;

  @JsonProperty("initialMarginFraction")
  private BigDecimal initialMarginFraction;

  @JsonProperty("maintenanceMarginFraction")
  private BigDecimal maintenanceMarginFraction;

  @JsonProperty("openInterest")
  private BigDecimal openInterest;

  @JsonProperty("atomicResolution")
  private BigDecimal atomicResolution;

  @JsonProperty("quantumConversionExponent")
  private BigDecimal quantumConversionExponent;

  @JsonProperty("tickSize")
  private BigDecimal tickSize;

  @JsonProperty("stepSize")
  private BigDecimal stepSize;

  @JsonProperty("stepBaseQuantums")
  private BigDecimal stepBaseQuantums;

  @JsonProperty("subticksPerTick")
  private BigDecimal subticksPerTick;

  @JsonProperty("marketType")
  private String marketType;

  @JsonProperty("openInterestLowerCap")
  private BigDecimal openInterestLowerCap;

  @JsonProperty("openInterestUpperCap")
  private BigDecimal openInterestUpperCap;

  @JsonProperty("baseOpenInterest")
  private BigDecimal baseOpenInterest;

  @JsonProperty("defaultFundingRate1H")
  private BigDecimal defaultFundingRate1H;

}
