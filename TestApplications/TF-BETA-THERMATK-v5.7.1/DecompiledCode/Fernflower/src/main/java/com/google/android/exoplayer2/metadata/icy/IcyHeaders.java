package com.google.android.exoplayer2.metadata.icy;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.util.List;
import java.util.Map;

public final class IcyHeaders implements Metadata.Entry {
   public static final Creator CREATOR = new Creator() {
      public IcyHeaders createFromParcel(Parcel var1) {
         return new IcyHeaders(var1);
      }

      public IcyHeaders[] newArray(int var1) {
         return new IcyHeaders[var1];
      }
   };
   public final int bitrate;
   public final String genre;
   public final boolean isPublic;
   public final int metadataInterval;
   public final String name;
   public final String url;

   public IcyHeaders(int var1, String var2, String var3, String var4, boolean var5, int var6) {
      boolean var7;
      if (var6 != -1 && var6 <= 0) {
         var7 = false;
      } else {
         var7 = true;
      }

      Assertions.checkArgument(var7);
      this.bitrate = var1;
      this.genre = var2;
      this.name = var3;
      this.url = var4;
      this.isPublic = var5;
      this.metadataInterval = var6;
   }

   IcyHeaders(Parcel var1) {
      this.bitrate = var1.readInt();
      this.genre = var1.readString();
      this.name = var1.readString();
      this.url = var1.readString();
      this.isPublic = Util.readBoolean(var1);
      this.metadataInterval = var1.readInt();
   }

   public static IcyHeaders parse(Map var0) {
      List var1 = (List)var0.get("icy-br");
      byte var2 = -1;
      int var4;
      int var6;
      String var17;
      boolean var18;
      if (var1 != null) {
         label88: {
            var17 = (String)var1.get(0);

            label82: {
               int var3;
               StringBuilder var5;
               label89: {
                  try {
                     var3 = Integer.parseInt(var17);
                  } catch (NumberFormatException var13) {
                     var3 = -1;
                     break label89;
                  }

                  var4 = var3 * 1000;
                  if (var4 > 0) {
                     var18 = true;
                     break label82;
                  }

                  try {
                     var5 = new StringBuilder();
                     var5.append("Invalid bitrate: ");
                     var5.append(var17);
                     Log.w("IcyHeaders", var5.toString());
                  } catch (NumberFormatException var12) {
                     var3 = var4;
                     break label89;
                  }

                  var18 = false;
                  var4 = -1;
                  break label82;
               }

               var5 = new StringBuilder();
               var5.append("Invalid bitrate header: ");
               var5.append(var17);
               Log.w("IcyHeaders", var5.toString());
               var6 = var3;
               var18 = false;
               break label88;
            }

            var6 = var4;
         }
      } else {
         var18 = false;
         var6 = -1;
      }

      var1 = (List)var0.get("icy-genre");
      if (var1 != null) {
         var17 = (String)var1.get(0);
         var18 = true;
      } else {
         var17 = null;
      }

      List var19 = (List)var0.get("icy-name");
      String var20;
      if (var19 != null) {
         var20 = (String)var19.get(0);
         var18 = true;
      } else {
         var20 = null;
      }

      List var7 = (List)var0.get("icy-url");
      String var21;
      if (var7 != null) {
         var21 = (String)var7.get(0);
         var18 = true;
      } else {
         var21 = null;
      }

      List var8 = (List)var0.get("icy-pub");
      boolean var9;
      if (var8 != null) {
         var9 = ((String)var8.get(0)).equals("1");
         var18 = true;
      } else {
         var9 = false;
      }

      List var14 = (List)var0.get("icy-metaint");
      if (var14 != null) {
         label90: {
            String var15 = (String)var14.get(0);

            StringBuilder var22;
            label91: {
               try {
                  var4 = Integer.parseInt(var15);
               } catch (NumberFormatException var11) {
                  var4 = var2;
                  break label91;
               }

               if (var4 > 0) {
                  var18 = true;
                  break label90;
               }

               try {
                  var22 = new StringBuilder();
                  var22.append("Invalid metadata interval: ");
                  var22.append(var15);
                  Log.w("IcyHeaders", var22.toString());
               } catch (NumberFormatException var10) {
                  break label91;
               }

               var4 = var2;
               break label90;
            }

            var22 = new StringBuilder();
            var22.append("Invalid metadata interval: ");
            var22.append(var15);
            Log.w("IcyHeaders", var22.toString());
         }
      } else {
         var4 = -1;
      }

      IcyHeaders var16;
      if (var18) {
         var16 = new IcyHeaders(var6, var17, var20, var21, var9, var4);
      } else {
         var16 = null;
      }

      return var16;
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && IcyHeaders.class == var1.getClass()) {
         IcyHeaders var3 = (IcyHeaders)var1;
         if (this.bitrate != var3.bitrate || !Util.areEqual(this.genre, var3.genre) || !Util.areEqual(this.name, var3.name) || !Util.areEqual(this.url, var3.url) || this.isPublic != var3.isPublic || this.metadataInterval != var3.metadataInterval) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.bitrate;
      String var2 = this.genre;
      int var3 = 0;
      int var4;
      if (var2 != null) {
         var4 = var2.hashCode();
      } else {
         var4 = 0;
      }

      var2 = this.name;
      int var5;
      if (var2 != null) {
         var5 = var2.hashCode();
      } else {
         var5 = 0;
      }

      var2 = this.url;
      if (var2 != null) {
         var3 = var2.hashCode();
      }

      return (((((527 + var1) * 31 + var4) * 31 + var5) * 31 + var3) * 31 + this.isPublic) * 31 + this.metadataInterval;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("IcyHeaders: name=\"");
      var1.append(this.name);
      var1.append("\", genre=\"");
      var1.append(this.genre);
      var1.append("\", bitrate=");
      var1.append(this.bitrate);
      var1.append(", metadataInterval=");
      var1.append(this.metadataInterval);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.bitrate);
      var1.writeString(this.genre);
      var1.writeString(this.name);
      var1.writeString(this.url);
      Util.writeBoolean(var1, this.isPublic);
      var1.writeInt(this.metadataInterval);
   }
}
