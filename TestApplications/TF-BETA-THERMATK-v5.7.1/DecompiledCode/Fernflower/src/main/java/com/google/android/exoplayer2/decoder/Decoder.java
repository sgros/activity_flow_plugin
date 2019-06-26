package com.google.android.exoplayer2.decoder;

public interface Decoder {
   Object dequeueInputBuffer() throws Exception;

   Object dequeueOutputBuffer() throws Exception;

   void flush();

   String getName();

   void queueInputBuffer(Object var1) throws Exception;

   void release();
}
