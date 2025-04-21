package org.knowm.xchange.dydx.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Instruments {

  @JsonProperty("markets")
  private Map<String, DyDxInstrumentDetails> markets;

}
