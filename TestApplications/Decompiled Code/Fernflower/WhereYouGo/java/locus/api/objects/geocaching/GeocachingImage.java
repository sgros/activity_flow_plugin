package locus.api.objects.geocaching;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class GeocachingImage extends Storable {
   private static final String TAG = "GeocachingImage";
   private String mDescription;
   private String mName;
   private String mThumbUrl;
   private String mUrl;

   public String getDescription() {
      return this.mDescription;
   }

   public String getName() {
      return this.mName;
   }

   public String getThumbUrl() {
      return this.mThumbUrl;
   }

   public String getUrl() {
      return this.mUrl;
   }

   protected int getVersion() {
      return 0;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mName = var2.readString();
      this.mDescription = var2.readString();
      this.mThumbUrl = var2.readString();
      this.mUrl = var2.readString();
   }

   public void reset() {
      this.mName = "";
      this.mDescription = "";
      this.mThumbUrl = "";
      this.mUrl = "";
   }

   public void setDescription(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingImage", "setDescription(), empty parameter");
         var2 = "";
      }

      this.mDescription = var2;
   }

   public void setName(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingImage", "setName(), empty parameter");
         var2 = "";
      }

      this.mName = var2;
   }

   public void setThumbUrl(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingImage", "setThumbUrl(), empty parameter");
         var2 = "";
      }

      this.mThumbUrl = var2;
   }

   public void setUrl(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingImage", "setUrl(), empty parameter");
         var2 = "";
      }

      this.mUrl = var2;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeString(this.mName);
      var1.writeString(this.mDescription);
      var1.writeString(this.mThumbUrl);
      var1.writeString(this.mUrl);
   }
}
