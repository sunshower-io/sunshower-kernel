package io.sunshower.kernel.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

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
