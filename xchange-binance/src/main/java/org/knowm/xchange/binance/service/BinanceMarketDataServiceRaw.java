package org.knowm.xchange.binance.service;

import static org.knowm.xchange.binance.BinanceResilience.REQUEST_WEIGHT_RATE_LIMITER;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.knowm.xchange.binance.BinanceAdapters;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.marketdata.BinanceAggTrades;
import org.knowm.xchange.binance.dto.marketdata.BinanceFundingRate;
import org.knowm.xchange.binance.dto.marketdata.BinanceKline;
import org.knowm.xchange.binance.dto.marketdata.BinanceOrderbook;
import org.knowm.xchange.binance.dto.marketdata.BinancePrice;
import org.knowm.xchange.binance.dto.marketdata.BinancePriceQuantity;
import org.knowm.xchange.binance.dto.marketdata.BinanceTicker24h;
import org.knowm.xchange.binance.dto.marketdata.KlineInterval;
import org.knowm.xchange.binance.dto.meta.BinanceTime;
import org.knowm.xchange.binance.dto.meta.exchangeinfo.BinanceExchangeInfo;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.utils.StreamUtils;

public class BinanceMarketDataServiceRaw extends BinanceBaseService {

  protected BinanceMarketDataServiceRaw(
      BinanceExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public void ping() throws IOException {
    decorateApiCall(binance::ping).withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER)).call();
  }

  public BinanceTime binanceTime() throws IOException {
    return decorateApiCall(binance::time)
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  public BinanceExchangeInfo getExchangeInfo() throws IOException {
    return decorateApiCall(binance::exchangeInfo)
        .withRetry(retry("exchangeInfo"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  public BinanceExchangeInfo getFutureExchangeInfo() throws IOException {
    return decorateApiCall(binanceFutures::exchangeInfo)
        .withRetry(retry("exchangeInfo"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  public BinanceExchangeInfo getInverseFutureExchangeInfo() throws IOException {
    return decorateApiCall(inverseBinanceFutures::inverseExchangeInfo)
        .withRetry(retry("exchangeInfo"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  public BinanceOrderbook getBinanceOrderbookAllProducts(Instrument pair, Integer limit)
      throws IOException {
    return decorateApiCall(
            () ->
                (pair instanceof FuturesContract)
                    ? getFutureDepth(pair, limit)
                    : binance.depth(BinanceAdapters.toSymbol(pair), limit))
        .withRetry(retry("depth"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER), depthPermits(limit))
        .call();
  }

  private BinanceOrderbook getFutureDepth(Instrument pair, Integer limit) throws IOException {
    return BinanceAdapters.isInverse(pair) ?
        inverseBinanceFutures.inverseDepth(BinanceAdapters.toInverseSymbol(pair), limit):
        binanceFutures.depth(BinanceAdapters.toSymbol(pair), limit);
  }

  public List<BinanceAggTrades> aggTradesAllProducts(
      Instrument pair, Long fromId, Long startTime, Long endTime, Integer limit)
      throws IOException {
    return decorateApiCall(
            () ->
                (pair instanceof FuturesContract)
                    ? getFutureAggTrades(pair, fromId, startTime, endTime, limit)
                    : binance.aggTrades(
                        BinanceAdapters.toSymbol(pair), fromId, startTime, endTime, limit))
        .withRetry(retry("aggTrades"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER), aggTradesPermits(limit))
        .call();
  }

  private List<BinanceAggTrades> getFutureAggTrades(Instrument pair, Long fromId, Long startTime,
      Long endTime, Integer limit) throws IOException {
    return BinanceAdapters.isInverse(pair)?
        inverseBinanceFutures.inverseAggTrades(BinanceAdapters.toInverseSymbol(pair), fromId, startTime, endTime, limit):
        binanceFutures.aggTrades(BinanceAdapters.toSymbol(pair), fromId, startTime, endTime, limit);
  }

  public BinanceKline lastKline(CurrencyPair pair, KlineInterval interval) throws IOException {
    return klines(pair, interval, 1, null, null).stream().collect(StreamUtils.singletonCollector());
  }

  public List<BinanceKline> klines(CurrencyPair pair, KlineInterval interval) throws IOException {
    return klines(pair, interval, null, null, null);
  }

  public List<BinanceKline> klines(
      CurrencyPair pair, KlineInterval interval, Integer limit, Long startTime, Long endTime)
      throws IOException {
    List<Object[]> raw =
        decorateApiCall(
                () ->
                    binance.klines(
                        BinanceAdapters.toSymbol(pair), interval.code(), limit, startTime, endTime))
            .withRetry(retry("klines"))
            .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
            .call();
    return raw.stream()
        .map(obj -> new BinanceKline(pair, interval, obj))
        .collect(Collectors.toList());
  }

  public List<BinanceTicker24h> ticker24hAllProducts(boolean isFutures) throws IOException {
    if (isFutures)
      return decorateApiCall(binanceFutures::ticker24h)
          .withRetry(retry("ticker24h"))
          .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER), 40)
          .call();
    else
      return decorateApiCall(binance::ticker24h)
          .withRetry(retry("ticker24h"))
          .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER), 80)
          .call();
  }

  public BinanceTicker24h ticker24hAllProducts(Instrument pair) throws IOException {
    BinanceTicker24h ticker24h =
        decorateApiCall(
                () ->
                    (pair instanceof FuturesContract)
                        ? getFuturesTicker24h(pair)
                        : binance.ticker24h(BinanceAdapters.toSymbol(pair)))
            .withRetry(retry("ticker24h"))
            .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
            .call();
    return ticker24h;
  }

  private BinanceTicker24h getFuturesTicker24h(Instrument pair) throws IOException {
    if(BinanceAdapters.isInverse(pair)) {
      List<BinanceTicker24h> tickerList = inverseBinanceFutures.inverseTicker24h(BinanceAdapters.toInverseSymbol(pair));
      if(tickerList!= null && tickerList.size() > 0){
        return tickerList.get(0);
      }
      return null;
    }
    else {
      return binanceFutures.ticker24h(BinanceAdapters.toSymbol(pair));
    }
  }

  public List<BinanceFundingRate> getBinanceFundingRates() throws IOException {
    return decorateApiCall(binanceFutures::fundingRates)
        .withRetry(retry("fundingRate"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  public List<BinanceFundingRate> getInverseFundingRates() throws IOException {
    return decorateApiCall(inverseBinanceFutures::inverseFundingRates)
        .withRetry(retry("fundingRate"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  public BinanceFundingRate getBinanceFundingRate(Instrument instrument) throws IOException {
    return decorateApiCall(
        () ->
            BinanceAdapters.isInverse(instrument)?
                inverseBinanceFutures.fundingRate(BinanceAdapters.toInverseSymbol(instrument)):
                binanceFutures.fundingRate(BinanceAdapters.toSymbol(instrument))
        )
        .withRetry(retry("fundingRate"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  public BinancePrice tickerPrice(CurrencyPair pair) throws IOException {
    return tickerAllPrices().stream()
        .filter(p -> p.getCurrencyPair().equals(pair))
        .collect(StreamUtils.singletonCollector());
  }

  public List<BinancePrice> tickerAllPrices() throws IOException {
    return decorateApiCall(binance::tickerAllPrices)
        .withRetry(retry("tickerAllPrices"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  public List<BinancePriceQuantity> tickerAllBookTickers() throws IOException {
    return decorateApiCall(binance::tickerAllBookTickers)
        .withRetry(retry("tickerAllBookTickers"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  protected int depthPermits(Integer limit) {
    if (limit == null || limit <= 100) {
      return 1;
    } else if (limit <= 500) {
      return 5;
    } else if (limit <= 1000) {
      return 10;
    }
    return 50;
  }

  protected int aggTradesPermits(Integer limit) {
    if (limit != null && limit > 500) {
      return 2;
    }
    return 1;
  }
}
