package com.google.android.exoplayer2.extractor;

import java.io.IOException;

public interface ExtractorInput {
   void advancePeekPosition(int var1) throws IOException, InterruptedException;

   boolean advancePeekPosition(int var1, boolean var2) throws IOException, InterruptedException;

   long getLength();

   long getPeekPosition();

   long getPosition();

   void peekFully(byte[] var1, int var2, int var3) throws IOException, InterruptedException;

   boolean peekFully(byte[] var1, int var2, int var3, boolean var4) throws IOException, InterruptedException;

   int read(byte[] var1, int var2, int var3) throws IOException, InterruptedException;

   void readFully(byte[] var1, int var2, int var3) throws IOException, InterruptedException;

   boolean readFully(byte[] var1, int var2, int var3, boolean var4) throws IOException, InterruptedException;

   void resetPeekPosition();

   void setRetryPosition(long var1, Throwable var3) throws Throwable;

   int skip(int var1) throws IOException, InterruptedException;

   void skipFully(int var1) throws IOException, InterruptedException;
}
