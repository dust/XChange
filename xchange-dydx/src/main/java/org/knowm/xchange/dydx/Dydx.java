package org.knowm.xchange.dydx;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import org.knowm.xchange.dydx.marketdata.DyDxInstrumentDetails;
import org.knowm.xchange.dydx.marketdata.Instruments;

@Path("/v4")
@Produces(MediaType.APPLICATION_JSON)
public interface Dydx {

//  @GET
//  @Path("spot/order_book")
//  GateioOrderBook getOrderBook(
//      @QueryParam("currency_pair") String currencyPair, @QueryParam("with_id") Boolean withId)
//      throws IOException, GateioException;
//

  @GET
  @Path("perpetualMarkets")
  Instruments getCurrencyPairDetails() throws IOException, DydxException;

//  @GET
//  @Path("spot/currency_pairs/{currency_pair}")
//  GateioCurrencyPairDetails getCurrencyPairDetails(@PathParam("currency_pair") String currencyPair)
//      throws IOException, GateioException;

//  @GET
//  @Path("spot/tickers")
//  List<GateioTicker> getTickers(@QueryParam("currency_pair") String currencyPair)
//      throws IOException, GateioException;
}
