package org.osmdroid.tileprovider.tilesource;

import android.util.Log;
import org.osmdroid.tileprovider.util.CloudmadeUtil;
import org.osmdroid.util.MapTileIndex;

public class CloudmadeTileSource extends OnlineTileSourceBase implements IStyledTileSource {
   private Integer mStyle = 1;

   public CloudmadeTileSource(String var1, int var2, int var3, int var4, String var5, String[] var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public String getTileURLString(long var1) {
      String var3 = CloudmadeUtil.getCloudmadeKey();
      if (var3.length() == 0) {
         Log.e("OsmDroid", "CloudMade key is not set. You should enter it in the manifest and call CloudmadeUtil.retrieveCloudmadeKey()");
      }

      String var4 = CloudmadeUtil.getCloudmadeToken();
      return String.format(this.getBaseUrl(), var3, this.mStyle, this.getTileSizePixels(), MapTileIndex.getZoom(var1), MapTileIndex.getX(var1), MapTileIndex.getY(var1), super.mImageFilenameEnding, var4);
   }

   public String pathBase() {
      Integer var1 = this.mStyle;
      if (var1 != null && var1 > 1) {
         StringBuilder var2 = new StringBuilder();
         var2.append(super.mName);
         var2.append(this.mStyle);
         return var2.toString();
      } else {
         return super.mName;
      }
   }

   public void setStyle(String var1) {
      try {
         this.mStyle = Integer.parseInt(var1);
      } catch (NumberFormatException var3) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Error setting integer style: ");
         var2.append(var1);
         Log.e("OsmDroid", var2.toString());
      }

   }
}
