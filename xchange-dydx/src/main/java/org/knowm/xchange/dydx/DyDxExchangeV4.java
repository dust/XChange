package org.knowm.xchange.dydx;

import java.io.IOException;
import java.util.Map;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.instrument.Instrument;

public class DyDxExchangeV4 extends BaseExchange {

  protected static ResilienceRegistries RESILIENCE_REGISTRIES;

  public static final String V4 = "v4";
  protected Dydx dydx;
  @Override
  protected void initServices() {
    this.dydx =
        ExchangeRestProxyBuilder.forInterface(
                Dydx.class, getExchangeSpecification())
            .build();

    marketDataService = new DydxMarketDataService(this, dydx, getResilienceRegistries());
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification specification = new ExchangeSpecification(getClass());
    specification.setSslUri("https://indexer.dydx.trade");
    specification.setHost("indexer.dydx.trade");
    specification.setExchangeName("dydx");
    specification.setShouldLoadRemoteMetaData(true);
    specification.setExchangeSpecificParametersItem("version", V4);
    return specification;
  }

  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (RESILIENCE_REGISTRIES == null) {
      RESILIENCE_REGISTRIES = DydxResilience.createRegistries();
    }
    return RESILIENCE_REGISTRIES;
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    Map<Instrument, InstrumentMetaData> instruments =
        ((DydxMarketDataService) marketDataService).getMetaDataByInstrument();

    exchangeMetaData = new ExchangeMetaData(instruments, null, null, null, null);
  }


  public static void main(String[] args) {
    Exchange exchange = ExchangeFactory.INSTANCE.createExchange(DyDxExchangeV4.class);

    System.out.println(exchange.getExchangeInstruments());
  }
}
