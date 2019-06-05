package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResourceDecoderRegistry {
   private final List bucketPriorityList = new ArrayList();
   private final Map decoders = new HashMap();

   private List getOrAddEntryList(String var1) {
      synchronized(this){}

      Throwable var10000;
      label136: {
         boolean var10001;
         try {
            if (!this.bucketPriorityList.contains(var1)) {
               this.bucketPriorityList.add(var1);
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label136;
         }

         List var2;
         try {
            var2 = (List)this.decoders.get(var1);
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label136;
         }

         Object var3 = var2;
         if (var2 != null) {
            return (List)var3;
         }

         label124:
         try {
            var3 = new ArrayList();
            this.decoders.put(var1, var3);
            return (List)var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label124;
         }
      }

      Throwable var16 = var10000;
      throw var16;
   }

   public void append(String var1, ResourceDecoder var2, Class var3, Class var4) {
      synchronized(this){}

      try {
         List var8 = this.getOrAddEntryList(var1);
         ResourceDecoderRegistry.Entry var5 = new ResourceDecoderRegistry.Entry(var3, var4, var2);
         var8.add(var5);
      } finally {
         ;
      }

   }

   public List getDecoders(Class var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label236: {
         ArrayList var3;
         Iterator var4;
         boolean var10001;
         try {
            var3 = new ArrayList();
            var4 = this.bucketPriorityList.iterator();
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label236;
         }

         label233:
         while(true) {
            List var28;
            try {
               if (!var4.hasNext()) {
                  return var3;
               }

               String var5 = (String)var4.next();
               var28 = (List)this.decoders.get(var5);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            if (var28 != null) {
               Iterator var6;
               try {
                  var6 = var28.iterator();
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               while(true) {
                  try {
                     ResourceDecoderRegistry.Entry var29;
                     do {
                        if (!var6.hasNext()) {
                           continue label233;
                        }

                        var29 = (ResourceDecoderRegistry.Entry)var6.next();
                     } while(!var29.handles(var1, var2));

                     var3.add(var29.decoder);
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label233;
                  }
               }
            }
         }
      }

      Throwable var27 = var10000;
      throw var27;
   }

   public List getResourceClasses(Class var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label236: {
         ArrayList var3;
         Iterator var4;
         boolean var10001;
         try {
            var3 = new ArrayList();
            var4 = this.bucketPriorityList.iterator();
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label236;
         }

         label233:
         while(true) {
            List var28;
            try {
               if (!var4.hasNext()) {
                  return var3;
               }

               String var5 = (String)var4.next();
               var28 = (List)this.decoders.get(var5);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            if (var28 != null) {
               Iterator var29;
               try {
                  var29 = var28.iterator();
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               while(true) {
                  try {
                     ResourceDecoderRegistry.Entry var6;
                     do {
                        if (!var29.hasNext()) {
                           continue label233;
                        }

                        var6 = (ResourceDecoderRegistry.Entry)var29.next();
                     } while(!var6.handles(var1, var2));

                     var3.add(var6.resourceClass);
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label233;
                  }
               }
            }
         }
      }

      Throwable var27 = var10000;
      throw var27;
   }

   public void prepend(String var1, ResourceDecoder var2, Class var3, Class var4) {
      synchronized(this){}

      try {
         List var8 = this.getOrAddEntryList(var1);
         ResourceDecoderRegistry.Entry var5 = new ResourceDecoderRegistry.Entry(var3, var4, var2);
         var8.add(0, var5);
      } finally {
         ;
      }

   }

   public void setBucketPriorityList(List var1) {
      synchronized(this){}

      Throwable var10000;
      label92: {
         boolean var10001;
         Iterator var11;
         try {
            ArrayList var2 = new ArrayList(this.bucketPriorityList);
            this.bucketPriorityList.clear();
            this.bucketPriorityList.addAll(var1);
            var11 = var2.iterator();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label92;
         }

         while(true) {
            try {
               String var3;
               do {
                  if (!var11.hasNext()) {
                     return;
                  }

                  var3 = (String)var11.next();
               } while(var1.contains(var3));

               this.bucketPriorityList.add(var3);
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   private static class Entry {
      private final Class dataClass;
      final ResourceDecoder decoder;
      final Class resourceClass;

      public Entry(Class var1, Class var2, ResourceDecoder var3) {
         this.dataClass = var1;
         this.resourceClass = var2;
         this.decoder = var3;
      }

      public boolean handles(Class var1, Class var2) {
         boolean var3;
         if (this.dataClass.isAssignableFrom(var1) && var2.isAssignableFrom(this.resourceClass)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }
}
