package org.knowm.xchange.dydx;

import static org.knowm.xchange.dydx.DydxResilience.REQUEST_WEIGHT_RATE_LIMITER;

import java.io.IOException;
import lombok.SneakyThrows;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.dydx.marketdata.Instruments;
import org.knowm.xchange.service.BaseResilientExchangeService;

public class DyDxBaseService extends BaseResilientExchangeService<DyDxExchangeV4>  {


  protected final Dydx gateio;


  @SneakyThrows
  public DyDxBaseService(DyDxExchangeV4 exchange, Dydx dydx, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
    gateio = dydx;
  }

  public Instruments getInstruments() throws IOException {
    return decorateApiCall(gateio::getCurrencyPairDetails)
        .withRetry(retry("getCurrencyPairDetails"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }
}
