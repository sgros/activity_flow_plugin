package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.util.Util;

public class ProgramInformation {
   public final String copyright;
   public final String lang;
   public final String moreInformationURL;
   public final String source;
   public final String title;

   public ProgramInformation(String var1, String var2, String var3, String var4, String var5) {
      this.title = var1;
      this.source = var2;
      this.copyright = var3;
      this.moreInformationURL = var4;
      this.lang = var5;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && ProgramInformation.class == var1.getClass()) {
         ProgramInformation var3 = (ProgramInformation)var1;
         if (!Util.areEqual(this.title, var3.title) || !Util.areEqual(this.source, var3.source) || !Util.areEqual(this.copyright, var3.copyright) || !Util.areEqual(this.moreInformationURL, var3.moreInformationURL) || !Util.areEqual(this.lang, var3.lang)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      String var1 = this.title;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.source;
      int var4;
      if (var1 != null) {
         var4 = var1.hashCode();
      } else {
         var4 = 0;
      }

      var1 = this.copyright;
      int var5;
      if (var1 != null) {
         var5 = var1.hashCode();
      } else {
         var5 = 0;
      }

      var1 = this.moreInformationURL;
      int var6;
      if (var1 != null) {
         var6 = var1.hashCode();
      } else {
         var6 = 0;
      }

      var1 = this.lang;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return ((((527 + var3) * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var2;
   }
}
