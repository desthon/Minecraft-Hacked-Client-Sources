package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class ForwardingFuture extends ForwardingObject implements Future {
   protected ForwardingFuture() {
   }

   protected abstract Future delegate();

   public boolean cancel(boolean var1) {
      return this.delegate().cancel(var1);
   }

   public boolean isCancelled() {
      return this.delegate().isCancelled();
   }

   public boolean isDone() {
      return this.delegate().isDone();
   }

   public Object get() throws InterruptedException, ExecutionException {
      return this.delegate().get();
   }

   public Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
      return this.delegate().get(var1, var3);
   }

   protected Object delegate() {
      return this.delegate();
   }

   public abstract static class SimpleForwardingFuture extends ForwardingFuture {
      private final Future delegate;

      protected SimpleForwardingFuture(Future var1) {
         this.delegate = (Future)Preconditions.checkNotNull(var1);
      }

      protected final Future delegate() {
         return this.delegate;
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
