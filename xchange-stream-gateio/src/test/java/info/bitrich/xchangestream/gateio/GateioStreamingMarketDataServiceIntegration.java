package info.bitrich.xchangestream.gateio;

import static org.assertj.core.api.Assertions.assertThat;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;

public class GateioStreamingMarketDataServiceIntegration extends GateioStreamingExchangeIT {

  @Test
  void order_book() {
    Observable<OrderBook> observable = exchange
        .getStreamingMarketDataService()
        .getOrderBook(CurrencyPair.BTC_USDT);

    TestObserver<OrderBook> testObserver = observable.test();

    OrderBook orderBook = testObserver
        .assertSubscribed()
        .awaitCount(1)
        .assertNoTimeout()
        .values().get(0);

    testObserver.dispose();

    assertThat(orderBook).hasNoNullFieldsOrProperties();
    assertThat(orderBook.getBids()).isNotEmpty();
    assertThat(orderBook.getAsks()).isNotEmpty();
  }


  @Test
  void trades() {
    Observable<Trade> observable = exchange
        .getStreamingMarketDataService()
        .getTrades(CurrencyPair.BTC_USDT);

    TestObserver<Trade> testObserver = observable.test();

    Trade trade = testObserver
        .assertSubscribed()
        .awaitCount(1)
        .assertNoTimeout()
        .values().get(0);

    testObserver.dispose();

    assertThat(trade).hasNoNullFieldsOrPropertiesExcept("makerOrderId", "takerOrderId");
    assertThat(trade.getInstrument()).isEqualTo(CurrencyPair.BTC_USDT);

  }


  @Test
  void ticker() {
    Observable<Ticker> observable = exchange
        .getStreamingMarketDataService()
        .getTicker(CurrencyPair.BTC_USDT);

    TestObserver<Ticker> testObserver = observable.test();

    Ticker ticker = testObserver
        .assertSubscribed()
        .awaitCount(1)
        .assertNoTimeout()
        .values()
        .get(0);

    testObserver.dispose();

    assertThat(ticker).hasNoNullFieldsOrPropertiesExcept("open", "vwap", "bidSize", "askSize");
    assertThat(ticker.getInstrument()).isEqualTo(CurrencyPair.BTC_USDT);
  }


}