package org.knowm.xchange.dydx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.dydx.marketdata.DyDxInstrumentDetails;
import org.knowm.xchange.dydx.marketdata.Instruments;

public class DydxMarketDataServiceRaw extends DyDxBaseService{

  public DydxMarketDataServiceRaw(DyDxExchangeV4 exchange,
      Dydx dydx, ResilienceRegistries resilienceRegistries) {
    super(exchange, dydx, resilienceRegistries);
  }


  public List<DyDxInstrumentDetails> getDyDxInstruments() throws IOException {
    Instruments instruments = gateio.getCurrencyPairDetails();
    if(instruments == null){
      return new ArrayList<>();
    }
    return new ArrayList<>(instruments.getMarkets().values());

  }


}
