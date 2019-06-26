package androidx.collection;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;

public class LruCache {
   private int createCount;
   private int evictionCount;
   private int hitCount;
   private final LinkedHashMap map;
   private int maxSize;
   private int missCount;
   private int putCount;
   private int size;

   public LruCache(int var1) {
      if (var1 > 0) {
         this.maxSize = var1;
         this.map = new LinkedHashMap(0, 0.75F, true);
      } else {
         throw new IllegalArgumentException("maxSize <= 0");
      }
   }

   private int safeSizeOf(Object var1, Object var2) {
      int var3 = this.sizeOf(var1, var2);
      if (var3 >= 0) {
         return var3;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Negative size: ");
         var4.append(var1);
         var4.append("=");
         var4.append(var2);
         throw new IllegalStateException(var4.toString());
      }
   }

   protected Object create(Object var1) {
      return null;
   }

   protected void entryRemoved(boolean var1, Object var2, Object var3, Object var4) {
   }

   public final Object get(Object var1) {
      if (var1 != null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         Throwable var94;
         label843: {
            Object var2;
            try {
               var2 = this.map.get(var1);
            } catch (Throwable var93) {
               var10000 = var93;
               var10001 = false;
               break label843;
            }

            if (var2 != null) {
               label836:
               try {
                  ++this.hitCount;
                  return var2;
               } catch (Throwable var91) {
                  var10000 = var91;
                  var10001 = false;
                  break label836;
               }
            } else {
               label849: {
                  try {
                     ++this.missCount;
                  } catch (Throwable var92) {
                     var10000 = var92;
                     var10001 = false;
                     break label849;
                  }

                  Object var3 = this.create(var1);
                  if (var3 == null) {
                     return null;
                  }

                  synchronized(this){}

                  label850: {
                     label851: {
                        try {
                           ++this.createCount;
                           var2 = this.map.put(var1, var3);
                        } catch (Throwable var90) {
                           var10000 = var90;
                           var10001 = false;
                           break label851;
                        }

                        if (var2 != null) {
                           try {
                              this.map.put(var1, var2);
                           } catch (Throwable var89) {
                              var10000 = var89;
                              var10001 = false;
                              break label851;
                           }
                        } else {
                           try {
                              this.size += this.safeSizeOf(var1, var3);
                           } catch (Throwable var88) {
                              var10000 = var88;
                              var10001 = false;
                              break label851;
                           }
                        }

                        label819:
                        try {
                           break label850;
                        } catch (Throwable var87) {
                           var10000 = var87;
                           var10001 = false;
                           break label819;
                        }
                     }

                     while(true) {
                        var94 = var10000;

                        try {
                           throw var94;
                        } catch (Throwable var85) {
                           var10000 = var85;
                           var10001 = false;
                           continue;
                        }
                     }
                  }

                  if (var2 != null) {
                     this.entryRemoved(false, var1, var3, var2);
                     return var2;
                  }

                  this.trimToSize(this.maxSize);
                  return var3;
               }
            }
         }

         while(true) {
            var94 = var10000;

            try {
               throw var94;
            } catch (Throwable var86) {
               var10000 = var86;
               var10001 = false;
               continue;
            }
         }
      } else {
         throw new NullPointerException("key == null");
      }
   }

   public final Object put(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         synchronized(this){}

         Object var3;
         label239: {
            Throwable var10000;
            boolean var10001;
            label240: {
               try {
                  ++this.putCount;
                  this.size += this.safeSizeOf(var1, var2);
                  var3 = this.map.put(var1, var2);
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label240;
               }

               if (var3 != null) {
                  try {
                     this.size -= this.safeSizeOf(var1, var3);
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label240;
                  }
               }

               label224:
               try {
                  break label239;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label224;
               }
            }

            while(true) {
               Throwable var24 = var10000;

               try {
                  throw var24;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (var3 != null) {
            this.entryRemoved(false, var1, var3, var2);
         }

         this.trimToSize(this.maxSize);
         return var3;
      } else {
         throw new NullPointerException("key == null || value == null");
      }
   }

   protected int sizeOf(Object var1, Object var2) {
      return 1;
   }

   public final String toString() {
      synchronized(this){}

      Throwable var10000;
      label128: {
         int var1;
         boolean var10001;
         try {
            var1 = this.hitCount + this.missCount;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label128;
         }

         if (var1 != 0) {
            try {
               var1 = this.hitCount * 100 / var1;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label128;
            }
         } else {
            var1 = 0;
         }

         label115:
         try {
            String var15 = String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.maxSize, this.hitCount, this.missCount, var1);
            return var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label115;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public void trimToSize(int var1) {
      while(true) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label443: {
            label448: {
               try {
                  if (this.size < 0 || this.map.isEmpty() && this.size != 0) {
                     break label448;
                  }
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label443;
               }

               label450: {
                  try {
                     if (this.size > var1 && !this.map.isEmpty()) {
                        break label450;
                     }
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label443;
                  }

                  try {
                     return;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label443;
                  }
               }

               Object var3;
               Object var46;
               try {
                  Entry var2 = (Entry)this.map.entrySet().iterator().next();
                  var3 = var2.getKey();
                  var46 = var2.getValue();
                  this.map.remove(var3);
                  this.size -= this.safeSizeOf(var3, var46);
                  ++this.evictionCount;
               } catch (Throwable var41) {
                  var10000 = var41;
                  var10001 = false;
                  break label443;
               }

               this.entryRemoved(true, var3, var46, (Object)null);
               continue;
            }

            label432:
            try {
               StringBuilder var49 = new StringBuilder();
               var49.append(this.getClass().getName());
               var49.append(".sizeOf() is reporting inconsistent results!");
               IllegalStateException var47 = new IllegalStateException(var49.toString());
               throw var47;
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label432;
            }
         }

         while(true) {
            Throwable var48 = var10000;

            try {
               throw var48;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
