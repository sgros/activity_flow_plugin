// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.util.MapTileIndex;
import android.util.Log;
import org.osmdroid.tileprovider.util.CloudmadeUtil;

public class CloudmadeTileSource extends OnlineTileSourceBase implements IStyledTileSource<Integer>
{
    private Integer mStyle;
    
    public CloudmadeTileSource(final String s, final int n, final int n2, final int n3, final String s2, final String[] array) {
        super(s, n, n2, n3, s2, array);
        this.mStyle = 1;
    }
    
    @Override
    public String getTileURLString(final long n) {
        final String cloudmadeKey = CloudmadeUtil.getCloudmadeKey();
        if (cloudmadeKey.length() == 0) {
            Log.e("OsmDroid", "CloudMade key is not set. You should enter it in the manifest and call CloudmadeUtil.retrieveCloudmadeKey()");
        }
        return String.format(this.getBaseUrl(), cloudmadeKey, this.mStyle, this.getTileSizePixels(), MapTileIndex.getZoom(n), MapTileIndex.getX(n), MapTileIndex.getY(n), super.mImageFilenameEnding, CloudmadeUtil.getCloudmadeToken());
    }
    
    @Override
    public String pathBase() {
        final Integer mStyle = this.mStyle;
        if (mStyle != null && mStyle > 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append(super.mName);
            sb.append(this.mStyle);
            return sb.toString();
        }
        return super.mName;
    }
    
    @Override
    public void setStyle(final String s) {
        try {
            this.mStyle = Integer.parseInt(s);
        }
        catch (NumberFormatException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error setting integer style: ");
            sb.append(s);
            Log.e("OsmDroid", sb.toString());
        }
    }
}
