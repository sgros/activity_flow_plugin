package org.mapsforge.core.model;

import java.io.Serializable;

public class Tag implements Serializable {
   private static final char KEY_VALUE_SEPARATOR = '=';
   private static final long serialVersionUID = 1L;
   public final String key;
   public final String value;

   public Tag(String var1) {
      this(var1, var1.indexOf(61));
   }

   private Tag(String var1, int var2) {
      String var3 = var1.substring(0, var2);
      if (var2 == var1.length() - 1) {
         var1 = "";
      } else {
         var1 = var1.substring(var2 + 1);
      }

      this(var3, var1);
   }

   public Tag(String var1, String var2) {
      this.key = var1;
      this.value = var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof Tag)) {
            var2 = false;
         } else {
            Tag var3 = (Tag)var1;
            if (this.key == null) {
               if (var3.key != null) {
                  var2 = false;
               }
            } else if (!this.key.equals(var3.key)) {
               var2 = false;
            } else if (this.value == null) {
               if (var3.value != null) {
                  var2 = false;
               }
            } else if (!this.value.equals(var3.value)) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      int var1 = 0;
      int var2;
      if (this.key == null) {
         var2 = 0;
      } else {
         var2 = this.key.hashCode();
      }

      if (this.value != null) {
         var1 = this.value.hashCode();
      }

      return (var2 + 31) * 31 + var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("key=");
      var1.append(this.key);
      var1.append(", value=");
      var1.append(this.value);
      return var1.toString();
   }
}
