package org.knowm.xchange.bitrue.dto.meta;


import org.knowm.xchange.dto.meta.FeeTier;

import java.math.BigDecimal;

/** @author ujjwal on 26/02/18. */
public class BinanceCurrencyPairMetaData  {
  private final BigDecimal minNotional;
  private final BigDecimal tradingFee;
  private final BigDecimal minimumAmount;
  private final BigDecimal maximumAmount;
  private final Integer priceScale;
  private final FeeTier[] feeTiers;

  /**
   * Constructor
   *
   * @param tradingFee Trading fee (fraction)
   * @param minimumAmount Minimum trade amount
   * @param maximumAmount Maximum trade amount
   * @param priceScale Price scale
   */
  public BinanceCurrencyPairMetaData(
      BigDecimal tradingFee,
      BigDecimal minimumAmount,
      BigDecimal maximumAmount,
      Integer priceScale,
      BigDecimal minNotional,
      FeeTier[] feeTiers) {
    this.tradingFee = tradingFee;
    this.minimumAmount = minimumAmount;
    this.maximumAmount = maximumAmount;
    this.priceScale = priceScale;
    this.feeTiers = feeTiers;
    this.minNotional = minNotional;
  }

  public BigDecimal getMinNotional() {
    return minNotional;
  }

  @Override
  public String toString() {
    return "BinanceCurrencyPairMetaData{" + "minNotional=" + minNotional + "} " ;
  }
}
