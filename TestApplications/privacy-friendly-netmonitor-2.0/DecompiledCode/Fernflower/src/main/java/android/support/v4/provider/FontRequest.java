package android.support.v4.provider;

import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.Preconditions;
import android.util.Base64;
import java.util.List;

public final class FontRequest {
   private final List mCertificates;
   private final int mCertificatesArray;
   private final String mIdentifier;
   private final String mProviderAuthority;
   private final String mProviderPackage;
   private final String mQuery;

   public FontRequest(@NonNull String var1, @NonNull String var2, @NonNull String var3, @ArrayRes int var4) {
      this.mProviderAuthority = (String)Preconditions.checkNotNull(var1);
      this.mProviderPackage = (String)Preconditions.checkNotNull(var2);
      this.mQuery = (String)Preconditions.checkNotNull(var3);
      this.mCertificates = null;
      boolean var5;
      if (var4 != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Preconditions.checkArgument(var5);
      this.mCertificatesArray = var4;
      StringBuilder var6 = new StringBuilder(this.mProviderAuthority);
      var6.append("-");
      var6.append(this.mProviderPackage);
      var6.append("-");
      var6.append(this.mQuery);
      this.mIdentifier = var6.toString();
   }

   public FontRequest(@NonNull String var1, @NonNull String var2, @NonNull String var3, @NonNull List var4) {
      this.mProviderAuthority = (String)Preconditions.checkNotNull(var1);
      this.mProviderPackage = (String)Preconditions.checkNotNull(var2);
      this.mQuery = (String)Preconditions.checkNotNull(var3);
      this.mCertificates = (List)Preconditions.checkNotNull(var4);
      this.mCertificatesArray = 0;
      StringBuilder var5 = new StringBuilder(this.mProviderAuthority);
      var5.append("-");
      var5.append(this.mProviderPackage);
      var5.append("-");
      var5.append(this.mQuery);
      this.mIdentifier = var5.toString();
   }

   @Nullable
   public List getCertificates() {
      return this.mCertificates;
   }

   @ArrayRes
   public int getCertificatesArrayResId() {
      return this.mCertificatesArray;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public String getIdentifier() {
      return this.mIdentifier;
   }

   public String getProviderAuthority() {
      return this.mProviderAuthority;
   }

   public String getProviderPackage() {
      return this.mProviderPackage;
   }

   public String getQuery() {
      return this.mQuery;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      StringBuilder var2 = new StringBuilder();
      var2.append("FontRequest {mProviderAuthority: ");
      var2.append(this.mProviderAuthority);
      var2.append(", mProviderPackage: ");
      var2.append(this.mProviderPackage);
      var2.append(", mQuery: ");
      var2.append(this.mQuery);
      var2.append(", mCertificates:");
      var1.append(var2.toString());

      for(int var3 = 0; var3 < this.mCertificates.size(); ++var3) {
         var1.append(" [");
         List var5 = (List)this.mCertificates.get(var3);

         for(int var4 = 0; var4 < var5.size(); ++var4) {
            var1.append(" \"");
            var1.append(Base64.encodeToString((byte[])var5.get(var4), 0));
            var1.append("\"");
         }

         var1.append(" ]");
      }

      var1.append("}");
      var2 = new StringBuilder();
      var2.append("mCertificatesArray: ");
      var2.append(this.mCertificatesArray);
      var1.append(var2.toString());
      return var1.toString();
   }
}
