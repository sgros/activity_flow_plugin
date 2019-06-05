package mozilla.components.browser.session;

import kotlin.jvm.internal.Intrinsics;

public final class Download {
   private final Long contentLength;
   private final String contentType;
   private final String destinationDirectory;
   private final String fileName;
   private final String url;
   private final String userAgent;

   public Download(String var1, String var2, String var3, Long var4, String var5, String var6) {
      Intrinsics.checkParameterIsNotNull(var1, "url");
      Intrinsics.checkParameterIsNotNull(var6, "destinationDirectory");
      super();
      this.url = var1;
      this.fileName = var2;
      this.contentType = var3;
      this.contentLength = var4;
      this.userAgent = var5;
      this.destinationDirectory = var6;
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof Download)) {
            return false;
         }

         Download var2 = (Download)var1;
         if (!Intrinsics.areEqual(this.url, var2.url) || !Intrinsics.areEqual(this.fileName, var2.fileName) || !Intrinsics.areEqual(this.contentType, var2.contentType) || !Intrinsics.areEqual(this.contentLength, var2.contentLength) || !Intrinsics.areEqual(this.userAgent, var2.userAgent) || !Intrinsics.areEqual(this.destinationDirectory, var2.destinationDirectory)) {
            return false;
         }
      }

      return true;
   }

   public final Long getContentLength() {
      return this.contentLength;
   }

   public final String getContentType() {
      return this.contentType;
   }

   public final String getFileName() {
      return this.fileName;
   }

   public final String getUrl() {
      return this.url;
   }

   public final String getUserAgent() {
      return this.userAgent;
   }

   public int hashCode() {
      String var1 = this.url;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.fileName;
      int var4;
      if (var1 != null) {
         var4 = var1.hashCode();
      } else {
         var4 = 0;
      }

      var1 = this.contentType;
      int var5;
      if (var1 != null) {
         var5 = var1.hashCode();
      } else {
         var5 = 0;
      }

      Long var8 = this.contentLength;
      int var6;
      if (var8 != null) {
         var6 = var8.hashCode();
      } else {
         var6 = 0;
      }

      var1 = this.userAgent;
      int var7;
      if (var1 != null) {
         var7 = var1.hashCode();
      } else {
         var7 = 0;
      }

      var1 = this.destinationDirectory;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return ((((var3 * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Download(url=");
      var1.append(this.url);
      var1.append(", fileName=");
      var1.append(this.fileName);
      var1.append(", contentType=");
      var1.append(this.contentType);
      var1.append(", contentLength=");
      var1.append(this.contentLength);
      var1.append(", userAgent=");
      var1.append(this.userAgent);
      var1.append(", destinationDirectory=");
      var1.append(this.destinationDirectory);
      var1.append(")");
      return var1.toString();
   }
}
