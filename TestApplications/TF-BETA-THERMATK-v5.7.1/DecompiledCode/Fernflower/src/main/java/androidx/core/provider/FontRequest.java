package androidx.core.provider;

import android.util.Base64;
import androidx.core.util.Preconditions;
import java.util.List;

public final class FontRequest {
   private final List mCertificates;
   private final int mCertificatesArray;
   private final String mIdentifier;
   private final String mProviderAuthority;
   private final String mProviderPackage;
   private final String mQuery;

   public FontRequest(String var1, String var2, String var3, List var4) {
      Preconditions.checkNotNull(var1);
      this.mProviderAuthority = (String)var1;
      Preconditions.checkNotNull(var2);
      this.mProviderPackage = (String)var2;
      Preconditions.checkNotNull(var3);
      this.mQuery = (String)var3;
      Preconditions.checkNotNull(var4);
      this.mCertificates = (List)var4;
      this.mCertificatesArray = 0;
      StringBuilder var5 = new StringBuilder(this.mProviderAuthority);
      var5.append("-");
      var5.append(this.mProviderPackage);
      var5.append("-");
      var5.append(this.mQuery);
      this.mIdentifier = var5.toString();
   }

   public List getCertificates() {
      return this.mCertificates;
   }

   public int getCertificatesArrayResId() {
      return this.mCertificatesArray;
   }

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
