package com.airbnb.lottie.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyPath {
   private final List keys;
   private KeyPathElement resolvedElement;

   private KeyPath(KeyPath var1) {
      this.keys = new ArrayList(var1.keys);
      this.resolvedElement = var1.resolvedElement;
   }

   public KeyPath(String... var1) {
      this.keys = Arrays.asList(var1);
   }

   private boolean endsWithGlobstar() {
      return ((String)this.keys.get(this.keys.size() - 1)).equals("**");
   }

   private boolean isContainer(String var1) {
      return var1.equals("__container");
   }

   public KeyPath addKey(String var1) {
      KeyPath var2 = new KeyPath(this);
      var2.keys.add(var1);
      return var2;
   }

   public boolean fullyResolvesTo(String var1, int var2) {
      int var3 = this.keys.size();
      boolean var4 = false;
      boolean var5 = false;
      if (var2 >= var3) {
         return false;
      } else {
         boolean var9;
         if (var2 == this.keys.size() - 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         String var6 = (String)this.keys.get(var2);
         boolean var7;
         boolean var8;
         if (!var6.equals("**")) {
            if (!var6.equals(var1) && !var6.equals("*")) {
               var7 = false;
            } else {
               var7 = true;
            }

            if (!var9) {
               var8 = var5;
               if (var2 != this.keys.size() - 2) {
                  return var8;
               }

               var8 = var5;
               if (!this.endsWithGlobstar()) {
                  return var8;
               }
            }

            var8 = var5;
            if (var7) {
               var8 = true;
            }

            return var8;
         } else {
            if (!var9 && ((String)this.keys.get(var2 + 1)).equals(var1)) {
               var7 = true;
            } else {
               var7 = false;
            }

            if (!var7) {
               if (var9) {
                  return true;
               } else {
                  ++var2;
                  return var2 < this.keys.size() - 1 ? false : ((String)this.keys.get(var2)).equals(var1);
               }
            } else {
               if (var2 != this.keys.size() - 2) {
                  var8 = var4;
                  if (var2 != this.keys.size() - 3) {
                     return var8;
                  }

                  var8 = var4;
                  if (!this.endsWithGlobstar()) {
                     return var8;
                  }
               }

               var8 = true;
               return var8;
            }
         }
      }
   }

   public KeyPathElement getResolvedElement() {
      return this.resolvedElement;
   }

   public int incrementDepthBy(String var1, int var2) {
      if (this.isContainer(var1)) {
         return 0;
      } else if (!((String)this.keys.get(var2)).equals("**")) {
         return 1;
      } else if (var2 == this.keys.size() - 1) {
         return 0;
      } else {
         return ((String)this.keys.get(var2 + 1)).equals(var1) ? 2 : 0;
      }
   }

   public boolean matches(String var1, int var2) {
      if (this.isContainer(var1)) {
         return true;
      } else if (var2 >= this.keys.size()) {
         return false;
      } else {
         return ((String)this.keys.get(var2)).equals(var1) || ((String)this.keys.get(var2)).equals("**") || ((String)this.keys.get(var2)).equals("*");
      }
   }

   public boolean propagateToChildren(String var1, int var2) {
      boolean var3 = var1.equals("__container");
      boolean var4 = true;
      if (var3) {
         return true;
      } else {
         var3 = var4;
         if (var2 >= this.keys.size() - 1) {
            if (((String)this.keys.get(var2)).equals("**")) {
               var3 = var4;
            } else {
               var3 = false;
            }
         }

         return var3;
      }
   }

   public KeyPath resolve(KeyPathElement var1) {
      KeyPath var2 = new KeyPath(this);
      var2.resolvedElement = var1;
      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("KeyPath{keys=");
      var1.append(this.keys);
      var1.append(",resolved=");
      boolean var2;
      if (this.resolvedElement != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      var1.append(var2);
      var1.append('}');
      return var1.toString();
   }
}
