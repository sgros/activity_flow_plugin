package android.support.v4.util;

import java.util.LinkedHashMap;
import java.util.Map;
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
      if (var1 <= 0) {
         throw new IllegalArgumentException("maxSize <= 0");
      } else {
         this.maxSize = var1;
         this.map = new LinkedHashMap(0, 0.75F, true);
      }
   }

   private int safeSizeOf(Object var1, Object var2) {
      int var3 = this.sizeOf(var1, var2);
      if (var3 < 0) {
         throw new IllegalStateException("Negative size: " + var1 + "=" + var2);
      } else {
         return var3;
      }
   }

   protected Object create(Object var1) {
      return null;
   }

   public final int createCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.createCount;
      } finally {
         ;
      }

      return var1;
   }

   protected void entryRemoved(boolean var1, Object var2, Object var3, Object var4) {
   }

   public final void evictAll() {
      this.trimToSize(-1);
   }

   public final int evictionCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.evictionCount;
      } finally {
         ;
      }

      return var1;
   }

   public final Object get(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("key == null");
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         Throwable var94;
         label904: {
            Object var2;
            try {
               var2 = this.map.get(var1);
            } catch (Throwable var93) {
               var10000 = var93;
               var10001 = false;
               break label904;
            }

            if (var2 != null) {
               label906: {
                  try {
                     ++this.hitCount;
                  } catch (Throwable var91) {
                     var10000 = var91;
                     var10001 = false;
                     break label906;
                  }

                  var1 = var2;
                  return var1;
               }
            } else {
               label911: {
                  try {
                     ++this.missCount;
                  } catch (Throwable var92) {
                     var10000 = var92;
                     var10001 = false;
                     break label911;
                  }

                  var2 = this.create(var1);
                  if (var2 == null) {
                     var1 = null;
                     return var1;
                  }

                  synchronized(this){}

                  Object var3;
                  label889: {
                     label890: {
                        try {
                           ++this.createCount;
                           var3 = this.map.put(var1, var2);
                        } catch (Throwable var90) {
                           var10000 = var90;
                           var10001 = false;
                           break label890;
                        }

                        if (var3 != null) {
                           try {
                              this.map.put(var1, var3);
                           } catch (Throwable var89) {
                              var10000 = var89;
                              var10001 = false;
                              break label890;
                           }
                        } else {
                           try {
                              this.size += this.safeSizeOf(var1, var2);
                           } catch (Throwable var88) {
                              var10000 = var88;
                              var10001 = false;
                              break label890;
                           }
                        }

                        label856:
                        try {
                           break label889;
                        } catch (Throwable var87) {
                           var10000 = var87;
                           var10001 = false;
                           break label856;
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

                  if (var3 != null) {
                     this.entryRemoved(false, var1, var2, var3);
                     var1 = var3;
                  } else {
                     this.trimToSize(this.maxSize);
                     var1 = var2;
                  }

                  return var1;
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
      }
   }

   public final int hitCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.hitCount;
      } finally {
         ;
      }

      return var1;
   }

   public final int maxSize() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.maxSize;
      } finally {
         ;
      }

      return var1;
   }

   public final int missCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.missCount;
      } finally {
         ;
      }

      return var1;
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

   public final int putCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.putCount;
      } finally {
         ;
      }

      return var1;
   }

   public final Object remove(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("key == null");
      } else {
         synchronized(this){}

         Object var2;
         label229: {
            Throwable var10000;
            boolean var10001;
            label230: {
               try {
                  var2 = this.map.remove(var1);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label230;
               }

               if (var2 != null) {
                  try {
                     this.size -= this.safeSizeOf(var1, var2);
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label230;
                  }
               }

               label215:
               try {
                  break label229;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label215;
               }
            }

            while(true) {
               Throwable var23 = var10000;

               try {
                  throw var23;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (var2 != null) {
            this.entryRemoved(false, var1, var2, (Object)null);
         }

         return var2;
      }
   }

   public void resize(int param1) {
      // $FF: Couldn't be decompiled
   }

   public final int size() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.size;
      } finally {
         ;
      }

      return var1;
   }

   protected int sizeOf(Object var1, Object var2) {
      return 1;
   }

   public final Map snapshot() {
      synchronized(this){}

      LinkedHashMap var1;
      try {
         var1 = new LinkedHashMap(this.map);
      } finally {
         ;
      }

      return var1;
   }

   public final String toString() {
      int var1 = 0;
      synchronized(this){}

      Throwable var10000;
      label116: {
         boolean var10001;
         int var2;
         try {
            var2 = this.hitCount + this.missCount;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label116;
         }

         if (var2 != 0) {
            try {
               var1 = this.hitCount * 100 / var2;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label116;
            }
         }

         label104:
         try {
            String var16 = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.maxSize, this.hitCount, this.missCount, var1);
            return var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label104;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public void trimToSize(int var1) {
      while(true) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label434: {
            label428: {
               try {
                  if (this.size >= 0 && (!this.map.isEmpty() || this.size == 0)) {
                     break label428;
                  }
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label434;
               }

               try {
                  StringBuilder var3 = new StringBuilder();
                  IllegalStateException var2 = new IllegalStateException(var3.append(this.getClass().getName()).append(".sizeOf() is reporting inconsistent results!").toString());
                  throw var2;
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label434;
               }
            }

            label415: {
               try {
                  if (this.size > var1 && !this.map.isEmpty()) {
                     break label415;
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label434;
               }

               try {
                  return;
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label434;
               }
            }

            Object var47;
            Object var49;
            try {
               Entry var48 = (Entry)this.map.entrySet().iterator().next();
               var47 = var48.getKey();
               var49 = var48.getValue();
               this.map.remove(var47);
               this.size -= this.safeSizeOf(var47, var49);
               ++this.evictionCount;
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break label434;
            }

            this.entryRemoved(true, var47, var49, (Object)null);
            continue;
         }

         while(true) {
            Throwable var46 = var10000;

            try {
               throw var46;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
