package org.knowm.xchange.dydx;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DydxException extends RuntimeException {
  @JsonProperty("errorCode")
  int errorCode;

  @JsonProperty("error")
  String error;
}
