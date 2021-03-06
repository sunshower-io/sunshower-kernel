package io.sunshower.kernel.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@SuppressWarnings("PMD.DoNotUseThreads")
public interface WorkerPool {

  <T> Future<T> submit(Callable<T> value);

  <K> void submitKernelAllocated(Runnable result);
}
