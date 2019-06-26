package org.telegram.messenger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LruCache {
   private final LinkedHashMap map;
   private final LinkedHashMap mapFilters;
   private int maxSize;
   private int size;

   public LruCache(int var1) {
      if (var1 > 0) {
         this.maxSize = var1;
         this.map = new LinkedHashMap(0, 0.75F, true);
         this.mapFilters = new LinkedHashMap();
      } else {
         throw new IllegalArgumentException("maxSize <= 0");
      }
   }

   private int safeSizeOf(String var1, Object var2) {
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

   private void trimToSize(int var1, String var2) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label846: {
         Iterator var3;
         try {
            var3 = this.map.entrySet().iterator();
         } catch (Throwable var94) {
            var10000 = var94;
            var10001 = false;
            break label846;
         }

         while(true) {
            label853: {
               try {
                  if (var3.hasNext() && this.size > var1 && !this.map.isEmpty()) {
                     break label853;
                  }
               } catch (Throwable var97) {
                  var10000 = var97;
                  var10001 = false;
                  break;
               }

               try {
                  return;
               } catch (Throwable var90) {
                  var10000 = var90;
                  var10001 = false;
                  break;
               }
            }

            Entry var4;
            String var5;
            try {
               var4 = (Entry)var3.next();
               var5 = (String)var4.getKey();
            } catch (Throwable var93) {
               var10000 = var93;
               var10001 = false;
               break;
            }

            if (var2 != null) {
               label828:
               try {
                  if (!var2.equals(var5)) {
                     break label828;
                  }
                  continue;
               } catch (Throwable var96) {
                  var10000 = var96;
                  var10001 = false;
                  break;
               }
            }

            Object var6;
            label823: {
               ArrayList var99;
               String[] var7;
               try {
                  var6 = var4.getValue();
                  this.size -= this.safeSizeOf(var5, var6);
                  var3.remove();
                  var7 = var5.split("@");
                  if (var7.length <= 1) {
                     break label823;
                  }

                  var99 = (ArrayList)this.mapFilters.get(var7[0]);
               } catch (Throwable var95) {
                  var10000 = var95;
                  var10001 = false;
                  break;
               }

               if (var99 != null) {
                  try {
                     var99.remove(var7[1]);
                     if (var99.isEmpty()) {
                        this.mapFilters.remove(var7[0]);
                     }
                  } catch (Throwable var92) {
                     var10000 = var92;
                     var10001 = false;
                     break;
                  }
               }
            }

            try {
               this.entryRemoved(true, var5, var6, (Object)null);
            } catch (Throwable var91) {
               var10000 = var91;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var98 = var10000;

         try {
            throw var98;
         } catch (Throwable var89) {
            var10000 = var89;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean contains(String var1) {
      return this.map.containsKey(var1);
   }

   protected void entryRemoved(boolean var1, String var2, Object var3, Object var4) {
   }

   public final void evictAll() {
      this.trimToSize(-1, (String)null);
   }

   public final Object get(String var1) {
      if (var1 != null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label182: {
            Object var22;
            try {
               var22 = this.map.get(var1);
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label182;
            }

            if (var22 != null) {
               label176:
               try {
                  return var22;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label176;
               }
            } else {
               label178:
               try {
                  return null;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label178;
               }
            }
         }

         while(true) {
            Throwable var23 = var10000;

            try {
               throw var23;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         throw new NullPointerException("key == null");
      }
   }

   public ArrayList getFilterKeys(String var1) {
      ArrayList var2 = (ArrayList)this.mapFilters.get(var1);
      return var2 != null ? new ArrayList(var2) : null;
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

   public Object put(String var1, Object var2) {
      if (var1 != null && var2 != null) {
         synchronized(this){}

         Object var3;
         label289: {
            Throwable var10000;
            boolean var10001;
            label290: {
               try {
                  this.size += this.safeSizeOf(var1, var2);
                  var3 = this.map.put(var1, var2);
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label290;
               }

               if (var3 != null) {
                  try {
                     this.size -= this.safeSizeOf(var1, var3);
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label290;
                  }
               }

               label274:
               try {
                  break label289;
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label274;
               }
            }

            while(true) {
               Throwable var27 = var10000;

               try {
                  throw var27;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  continue;
               }
            }
         }

         String[] var4 = var1.split("@");
         if (var4.length > 1) {
            ArrayList var5 = (ArrayList)this.mapFilters.get(var4[0]);
            ArrayList var6 = var5;
            if (var5 == null) {
               var6 = new ArrayList();
               this.mapFilters.put(var4[0], var6);
            }

            if (!var6.contains(var4[1])) {
               var6.add(var4[1]);
            }
         }

         if (var3 != null) {
            this.entryRemoved(false, var1, var3, var2);
         }

         this.trimToSize(this.maxSize, var1);
         return var3;
      } else {
         throw new NullPointerException("key == null || value == null");
      }
   }

   public final Object remove(String var1) {
      if (var1 != null) {
         synchronized(this){}

         Object var2;
         label269: {
            Throwable var10000;
            boolean var10001;
            label270: {
               try {
                  var2 = this.map.remove(var1);
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label270;
               }

               if (var2 != null) {
                  try {
                     this.size -= this.safeSizeOf(var1, var2);
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label270;
                  }
               }

               label255:
               try {
                  break label269;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label255;
               }
            }

            while(true) {
               Throwable var25 = var10000;

               try {
                  throw var25;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (var2 != null) {
            String[] var3 = var1.split("@");
            if (var3.length > 1) {
               ArrayList var4 = (ArrayList)this.mapFilters.get(var3[0]);
               if (var4 != null) {
                  var4.remove(var3[1]);
                  if (var4.isEmpty()) {
                     this.mapFilters.remove(var3[0]);
                  }
               }
            }

            this.entryRemoved(false, var1, var2, (Object)null);
         }

         return var2;
      } else {
         throw new NullPointerException("key == null");
      }
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

   protected int sizeOf(String var1, Object var2) {
      return 1;
   }
}
