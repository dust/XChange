package org.knowm.xchange.dydx;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dydx.marketdata.DyDxInstrumentDetails;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;

public class DydxMarketDataService extends DydxMarketDataServiceRaw  implements MarketDataService {

  public DydxMarketDataService(DyDxExchangeV4 dyDxExchangeV4,
      Dydx dydx, ResilienceRegistries resilienceRegistries) {
    super(dyDxExchangeV4, dydx, resilienceRegistries);
  }


  public Map<Instrument, InstrumentMetaData> getMetaDataByInstrument() throws IOException {
    try {
      List<DyDxInstrumentDetails> metadata = getDyDxInstruments();

      return metadata.stream()
          .collect(
              Collectors.toMap(
                  gateioCurrencyPairDetails -> {
                    String symbol = gateioCurrencyPairDetails.getTicker();
                    String[] baseAndQuote = symbol.split("\\-");
                    return new CurrencyPair(
                        baseAndQuote[0],
                        baseAndQuote[1]);
                  },
                  DydxMarketDataService::toInstrumentMetaData)
          );
    } catch (DydxException e) {
      throw new ExchangeException(e.getMessage(), e);
    }

  }


  public static InstrumentMetaData toInstrumentMetaData(
      DyDxInstrumentDetails gateioCurrencyPairDetails) {
    return new InstrumentMetaData.Builder()
        .minimumAmount(gateioCurrencyPairDetails.getTickSize())
        .counterMinimumAmount(gateioCurrencyPairDetails.getStepSize())
        .build();
  }


  public static void main(String[] args) {
  String symbol = "BTC-USDT";
  System.out.println(Arrays.toString(symbol.split("\\-")));
  }
}
