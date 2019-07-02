package io.sunshower.kernel;

import java.util.Objects;
import java.util.Optional;
import lombok.val;

public abstract class LinkedChain<T, U> implements Chain<T, U> {
  private Chain<T, U> next;

  @Override
  public void setNext(Chain<T, U> next) {
    Objects.requireNonNull(next, "Next link must not be null!");
  }

  @Override
  public Optional<T> process(U value) {
    val result = doProcess(value);
    if (result.isEmpty()) {
      if (next != null) {
        return next.process(value);
      }
    }
    return result;
  }

  protected abstract Optional<T> doProcess(U value);
}
