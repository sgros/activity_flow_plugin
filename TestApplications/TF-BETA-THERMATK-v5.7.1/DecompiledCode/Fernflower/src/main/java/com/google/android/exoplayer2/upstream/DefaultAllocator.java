package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class DefaultAllocator implements Allocator {
   private int allocatedCount;
   private Allocation[] availableAllocations;
   private int availableCount;
   private final int individualAllocationSize;
   private final byte[] initialAllocationBlock;
   private final Allocation[] singleAllocationReleaseHolder;
   private int targetBufferSize;
   private final boolean trimOnReset;

   public DefaultAllocator(boolean var1, int var2) {
      this(var1, var2, 0);
   }

   public DefaultAllocator(boolean var1, int var2, int var3) {
      int var4 = 0;
      boolean var5;
      if (var2 > 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Assertions.checkArgument(var5);
      if (var3 >= 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Assertions.checkArgument(var5);
      this.trimOnReset = var1;
      this.individualAllocationSize = var2;
      this.availableCount = var3;
      this.availableAllocations = new Allocation[var3 + 100];
      if (var3 > 0) {
         for(this.initialAllocationBlock = new byte[var3 * var2]; var4 < var3; ++var4) {
            this.availableAllocations[var4] = new Allocation(this.initialAllocationBlock, var4 * var2);
         }
      } else {
         this.initialAllocationBlock = null;
      }

      this.singleAllocationReleaseHolder = new Allocation[1];
   }

   public Allocation allocate() {
      synchronized(this){}

      Throwable var10000;
      label137: {
         boolean var10001;
         Allocation var1;
         int var2;
         Allocation[] var15;
         label131: {
            try {
               ++this.allocatedCount;
               if (this.availableCount > 0) {
                  var15 = this.availableAllocations;
                  var2 = this.availableCount - 1;
                  this.availableCount = var2;
                  break label131;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label137;
            }

            try {
               var1 = new Allocation(new byte[this.individualAllocationSize], 0);
               return var1;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label137;
            }
         }

         var1 = var15[var2];

         label121:
         try {
            this.availableAllocations[this.availableCount] = null;
            return var1;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label121;
         }
      }

      Throwable var16 = var10000;
      throw var16;
   }

   public int getIndividualAllocationLength() {
      return this.individualAllocationSize;
   }

   public int getTotalBytesAllocated() {
      synchronized(this){}

      int var1;
      int var2;
      try {
         var1 = this.allocatedCount;
         var2 = this.individualAllocationSize;
      } finally {
         ;
      }

      return var1 * var2;
   }

   public void release(Allocation var1) {
      synchronized(this){}

      try {
         this.singleAllocationReleaseHolder[0] = var1;
         this.release(this.singleAllocationReleaseHolder);
      } finally {
         ;
      }

   }

   public void release(Allocation[] var1) {
      synchronized(this){}

      Throwable var10000;
      label239: {
         boolean var10001;
         try {
            if (this.availableCount + var1.length >= this.availableAllocations.length) {
               this.availableAllocations = (Allocation[])Arrays.copyOf(this.availableAllocations, Math.max(this.availableAllocations.length * 2, this.availableCount + var1.length));
            }
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label239;
         }

         int var2;
         try {
            var2 = var1.length;
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label239;
         }

         for(int var3 = 0; var3 < var2; ++var3) {
            Allocation var4 = var1[var3];

            Allocation[] var5;
            int var6;
            try {
               var5 = this.availableAllocations;
               var6 = this.availableCount++;
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label239;
            }

            var5[var6] = var4;
         }

         label218:
         try {
            this.allocatedCount -= var1.length;
            this.notifyAll();
            return;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label218;
         }
      }

      Throwable var27 = var10000;
      throw var27;
   }

   public void reset() {
      synchronized(this){}

      try {
         if (this.trimOnReset) {
            this.setTargetBufferSize(0);
         }
      } finally {
         ;
      }

   }

   public void setTargetBufferSize(int var1) {
      synchronized(this){}

      Throwable var10000;
      label148: {
         boolean var10001;
         boolean var2;
         label142: {
            label141: {
               try {
                  if (var1 < this.targetBufferSize) {
                     break label141;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label148;
               }

               var2 = false;
               break label142;
            }

            var2 = true;
         }

         try {
            this.targetBufferSize = var1;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label148;
         }

         if (!var2) {
            return;
         }

         label131:
         try {
            this.trim();
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label131;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public void trim() {
      synchronized(this){}

      Throwable var10000;
      label726: {
         int var1;
         int var2;
         boolean var10001;
         try {
            var1 = Util.ceilDivide(this.targetBufferSize, this.individualAllocationSize);
            var2 = this.allocatedCount;
         } catch (Throwable var77) {
            var10000 = var77;
            var10001 = false;
            break label726;
         }

         int var3 = 0;

         try {
            var2 = Math.max(0, var1 - var2);
            var1 = this.availableCount;
         } catch (Throwable var76) {
            var10000 = var76;
            var10001 = false;
            break label726;
         }

         if (var2 >= var1) {
            return;
         }

         var1 = var2;

         label711: {
            try {
               if (this.initialAllocationBlock == null) {
                  break label711;
               }

               var1 = this.availableCount - 1;
            } catch (Throwable var73) {
               var10000 = var73;
               var10001 = false;
               break label726;
            }

            while(true) {
               if (var3 > var1) {
                  try {
                     var3 = Math.max(var2, var3);
                     var2 = this.availableCount;
                  } catch (Throwable var71) {
                     var10000 = var71;
                     var10001 = false;
                     break label726;
                  }

                  var1 = var3;
                  if (var3 >= var2) {
                     return;
                  }
                  break;
               }

               label728: {
                  Allocation var4;
                  try {
                     var4 = this.availableAllocations[var3];
                     if (var4.data == this.initialAllocationBlock) {
                        break label728;
                     }
                  } catch (Throwable var75) {
                     var10000 = var75;
                     var10001 = false;
                     break label726;
                  }

                  Allocation var5;
                  label702: {
                     try {
                        var5 = this.availableAllocations[var1];
                        if (var5.data == this.initialAllocationBlock) {
                           break label702;
                        }
                     } catch (Throwable var74) {
                        var10000 = var74;
                        var10001 = false;
                        break label726;
                     }

                     --var1;
                     continue;
                  }

                  try {
                     this.availableAllocations[var3] = var5;
                     this.availableAllocations[var1] = var4;
                  } catch (Throwable var72) {
                     var10000 = var72;
                     var10001 = false;
                     break label726;
                  }

                  --var1;
                  ++var3;
                  continue;
               }

               ++var3;
            }
         }

         try {
            Arrays.fill(this.availableAllocations, var1, this.availableCount, (Object)null);
            this.availableCount = var1;
         } catch (Throwable var70) {
            var10000 = var70;
            var10001 = false;
            break label726;
         }

         return;
      }

      Throwable var78 = var10000;
      throw var78;
   }
}
