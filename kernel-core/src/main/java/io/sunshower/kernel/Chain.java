package io.sunshower.kernel;

import java.util.Optional;

public interface Chain<T, U> {
  void setNext(Chain<T, U> next);

  Optional<T> process(U value);
}
