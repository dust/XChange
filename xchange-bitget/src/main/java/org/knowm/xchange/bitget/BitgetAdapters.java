package org.knowm.xchange.bitget;

import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.knowm.xchange.bitget.dto.marketdata.BitgetSymbolDto;
import org.knowm.xchange.bitget.dto.marketdata.BitgetSymbolDto.Status;
import org.knowm.xchange.bitget.dto.marketdata.BitgetTickerDto;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.instrument.Instrument;

@UtilityClass
public class BitgetAdapters {

  private final Map<String, CurrencyPair> SYMBOL_TO_CURRENCY_PAIR = new HashMap<>();


  public CurrencyPair toCurrencyPair(String symbol) {
    return SYMBOL_TO_CURRENCY_PAIR.get(symbol);
  }


  public String toString(Instrument instrument) {
    return instrument == null ? null : instrument.getBase().toString() + instrument.getCounter().toString();
  }


  public void putSymbolMapping(String symbol, CurrencyPair currencyPair) {
    SYMBOL_TO_CURRENCY_PAIR.put(symbol, currencyPair);
  }


  public InstrumentMetaData toInstrumentMetaData(BitgetSymbolDto bitgetSymbolDto) {
    InstrumentMetaData.Builder builder = new InstrumentMetaData.Builder()
        .tradingFee(bitgetSymbolDto.getTakerFeeRate())
        .minimumAmount(bitgetSymbolDto.getMinTradeAmount())
        .maximumAmount(bitgetSymbolDto.getMaxTradeAmount())
        .volumeScale(bitgetSymbolDto.getQuantityPrecision())
        .priceScale(bitgetSymbolDto.getPricePrecision())
        .marketOrderEnabled(bitgetSymbolDto.getStatus() == Status.ONLINE);

    // set min quote amount for USDT
    if (bitgetSymbolDto.getCurrencyPair().getCounter().equals(Currency.USDT)) {
      builder.counterMinimumAmount(bitgetSymbolDto.getMinTradeUSDT());
    }

    return builder.build();
  }


  public Ticker toTicker(BitgetTickerDto bitgetTickerDto) {
    CurrencyPair currencyPair = toCurrencyPair(bitgetTickerDto.getSymbol());
    if (currencyPair == null) {
      return null;
    }
    return new Ticker.Builder()
        .instrument(currencyPair)
        .open(bitgetTickerDto.getOpen24h())
        .last(bitgetTickerDto.getLastPrice())
        .bid(bitgetTickerDto.getBestBidPrice())
        .ask(bitgetTickerDto.getBestAskPrice())
        .high(bitgetTickerDto.getHigh24h())
        .low(bitgetTickerDto.getLow24h())
        .volume(bitgetTickerDto.getAssetVolume24h())
        .quoteVolume(bitgetTickerDto.getQuoteVolume24h())
        .timestamp(bitgetTickerDto.getTimestampAsDate())
        .bidSize(bitgetTickerDto.getBestBidSize())
        .askSize(bitgetTickerDto.getBestAskSize())
        .percentageChange(bitgetTickerDto.getChange24h())
        .build();
  }


}
