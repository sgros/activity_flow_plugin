package com.google.android.exoplayer2.upstream;

public interface Allocator {
   Allocation allocate();

   int getIndividualAllocationLength();

   void release(Allocation var1);

   void release(Allocation[] var1);

   void trim();
}
