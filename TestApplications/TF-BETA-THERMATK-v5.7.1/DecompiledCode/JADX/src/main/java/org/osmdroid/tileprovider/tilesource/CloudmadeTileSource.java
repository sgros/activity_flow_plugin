package org.osmdroid.tileprovider.tilesource;

import android.util.Log;
import org.osmdroid.tileprovider.util.CloudmadeUtil;
import org.osmdroid.util.MapTileIndex;

public class CloudmadeTileSource extends OnlineTileSourceBase implements IStyledTileSource<Integer> {
    private Integer mStyle = Integer.valueOf(1);

    public CloudmadeTileSource(String str, int i, int i2, int i3, String str2, String[] strArr) {
        super(str, i, i2, i3, str2, strArr);
    }

    public String pathBase() {
        Integer num = this.mStyle;
        if (num == null || num.intValue() <= 1) {
            return this.mName;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mName);
        stringBuilder.append(this.mStyle);
        return stringBuilder.toString();
    }

    public String getTileURLString(long j) {
        if (CloudmadeUtil.getCloudmadeKey().length() == 0) {
            Log.e("OsmDroid", "CloudMade key is not set. You should enter it in the manifest and call CloudmadeUtil.retrieveCloudmadeKey()");
        }
        String cloudmadeToken = CloudmadeUtil.getCloudmadeToken();
        return String.format(getBaseUrl(), new Object[]{r0, this.mStyle, Integer.valueOf(getTileSizePixels()), Integer.valueOf(MapTileIndex.getZoom(j)), Integer.valueOf(MapTileIndex.getX(j)), Integer.valueOf(MapTileIndex.getY(j)), this.mImageFilenameEnding, cloudmadeToken});
    }

    public void setStyle(String str) {
        try {
            this.mStyle = Integer.valueOf(Integer.parseInt(str));
        } catch (NumberFormatException unused) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error setting integer style: ");
            stringBuilder.append(str);
            Log.e("OsmDroid", stringBuilder.toString());
        }
    }
}
