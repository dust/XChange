package info.bitrich.xchangestream.dydx;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.dydx.service.v1.dydxStreamingMarketDataService;
import io.reactivex.rxjava3.core.Completable;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.dydx.DyDxExchangeV4;
import org.knowm.xchange.dydx.dydxExchange;
import org.knowm.xchange.instrument.Instrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Max Gao (gaamox@tutanota.com) Created: 20-02-2021
 *
 * <p>V3 Documentation: https://docs.dydx.exchange
 *
 * <p>Legacy Documentation: https://legacy-docs.dydx.exchange
 */
public class dydxStreamingExchange extends dydxExchange implements StreamingExchange {
  private static final Logger LOG = LoggerFactory.getLogger(dydxStreamingExchange.class);
  private static final String API_URI_V1 = "wss://api.dydx.exchange/v1/ws";
  private static final String API_URI_V3 = "wss://api.dydx.exchange/v3/ws";
  private static final String API_URI_ROPSTEN_V3 = "wss://api.stage.dydx.exchange/v3/ws";

  private dydxStreamingService streamingService;
  private StreamingMarketDataService streamingMarketDataService;
  private final DyDxExchangeV4 exchangeV4;

  public dydxStreamingExchange() {
    exchangeV4 = ExchangeFactory.INSTANCE.createExchange(DyDxExchangeV4.class);
  }

  @Override
  public List<Instrument> getExchangeInstruments() {
    return exchangeV4.getExchangeInstruments();
  }

  @Override
  public Completable connect(ProductSubscription... args) {
    if (args == null || args.length == 0)
      throw new UnsupportedOperationException("The ProductSubscription must be defined!");
    ExchangeSpecification exchangeSpec = getExchangeSpecification();

    switch ((String) exchangeSpec.getExchangeSpecificParametersItem("version")) {
      case V3:
        this.streamingService = new dydxStreamingService(API_URI_V3);
        this.streamingMarketDataService =
            new info.bitrich.xchangestream.dydx.service.v3.dydxStreamingMarketDataService(
                streamingService);
        break;
      case V3_ROPSTEN:
        this.streamingService = new dydxStreamingService(API_URI_ROPSTEN_V3);
        this.streamingMarketDataService =
            new info.bitrich.xchangestream.dydx.service.v3.dydxStreamingMarketDataService(
                streamingService);
        break;
      default:
        this.streamingService = new dydxStreamingService(API_URI_V1);
        this.streamingMarketDataService = new dydxStreamingMarketDataService(streamingService);
        break;
    }

    applyStreamingSpecification(getExchangeSpecification(), streamingService);
    streamingService.subscribeMultipleCurrencyPairs(args);
    return streamingService.connect();
  }

  @Override
  public Completable disconnect() {
    dydxStreamingService service = streamingService;
    streamingService = null;
    streamingMarketDataService = null;
    return service.disconnect();
  }

  @Override
  public StreamingMarketDataService getStreamingMarketDataService() {
    return streamingMarketDataService;
  }

  @Override
  public boolean isAlive() {
    return streamingService != null && streamingService.isSocketOpen();
  }

  @Override
  public void useCompressedMessages(boolean compressedMessages) {
    streamingService.useCompressedMessages(compressedMessages);
  }
}
