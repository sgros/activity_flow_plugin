package com.airbnb.lottie.network;

public enum FileExtension {
   JSON(".json"),
   ZIP(".zip");

   public final String extension;

   private FileExtension(String var3) {
      this.extension = var3;
   }

   public String tempExtension() {
      StringBuilder var1 = new StringBuilder();
      var1.append(".temp");
      var1.append(this.extension);
      return var1.toString();
   }

   public String toString() {
      return this.extension;
   }
}
