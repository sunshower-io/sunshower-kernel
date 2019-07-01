package io.sunshower.api;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Event<T> implements Serializable {
  private T data;
  private Topic topic;
  private Plugin sender;

  public enum Mode {
    Synchronous,
    Asynchronous
  }
}
