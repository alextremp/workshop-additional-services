package com.github.alextremp.loadrules.application.savesite.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;

public enum CalcOperatorDTO {
  ADD, SUB, MUL, DIV;

  private static final String ADD_VALUE = "add";
  private static final String SUB_VALUE = "sub";
  private static final String MUL_VALUE = "mul";
  private static final String DIV_VALUE = "div";

  @JsonValue
  public String toValue() {
    switch (this) {
      case ADD:
        return ADD_VALUE;
      case SUB:
        return SUB_VALUE;
      case MUL:
        return MUL_VALUE;
      case DIV:
        return DIV_VALUE;
    }
    return null;
  }

  @JsonCreator
  public static CalcOperatorDTO forValue(String value) throws IOException {
    switch (value) {
      case ADD_VALUE:
        return ADD;
      case SUB_VALUE:
        return SUB;
      case MUL_VALUE:
        return MUL;
      case DIV_VALUE:
        return DIV;
      default:
        throw new IOException("Cannot deserialize CalcOperator: " + value);
    }
  }
}
