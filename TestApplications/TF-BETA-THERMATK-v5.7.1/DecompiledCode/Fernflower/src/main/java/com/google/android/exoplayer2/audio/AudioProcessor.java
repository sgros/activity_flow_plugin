package com.google.android.exoplayer2.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public interface AudioProcessor {
   ByteBuffer EMPTY_BUFFER = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());

   boolean configure(int var1, int var2, int var3) throws AudioProcessor.UnhandledFormatException;

   void flush();

   ByteBuffer getOutput();

   int getOutputChannelCount();

   int getOutputEncoding();

   int getOutputSampleRateHz();

   boolean isActive();

   boolean isEnded();

   void queueEndOfStream();

   void queueInput(ByteBuffer var1);

   void reset();

   public static final class UnhandledFormatException extends Exception {
      public UnhandledFormatException(int var1, int var2, int var3) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unhandled format: ");
         var4.append(var1);
         var4.append(" Hz, ");
         var4.append(var2);
         var4.append(" channels in encoding ");
         var4.append(var3);
         super(var4.toString());
      }
   }
}
