package org.mapsforge.android.maps.mapgenerator.tiledownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Tile;

public abstract class TileDownloader implements MapGenerator {
    private static final Logger LOG = Logger.getLogger(TileDownloader.class.getName());
    private static final GeoPoint START_POINT = new GeoPoint(51.33d, 10.45d);
    private static final Byte START_ZOOM_LEVEL = Byte.valueOf((byte) 5);
    private final int[] pixels = new int[65536];

    public abstract String getAttribution();

    public abstract String getHostName();

    public abstract String getProtocol();

    public abstract String getTilePath(Tile tile);

    protected TileDownloader() {
    }

    public final boolean executeJob(MapGeneratorJob mapGeneratorJob, Bitmap bitmap) {
        boolean z;
        InputStream inputStream = null;
        try {
            URLConnection conn = new URL(getProtocol(), getHostName(), getTilePath(mapGeneratorJob.tile)).openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            inputStream = conn.getInputStream();
            Bitmap decodedBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            inputStream = null;
            if (decodedBitmap == null) {
                z = false;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            } else {
                decodedBitmap.getPixels(this.pixels, 0, 256, 0, 0, 256, 256);
                decodedBitmap.recycle();
                bitmap.setPixels(this.pixels, 0, 256, 0, 0, 256, 256);
                z = true;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                    }
                }
            }
        } catch (UnknownHostException e3) {
            LOG.log(Level.SEVERE, null, e3);
            z = false;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                }
            }
        } catch (IOException e5) {
            LOG.log(Level.SEVERE, null, e5);
            z = false;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                }
            }
        } catch (NullPointerException e7) {
            LOG.log(Level.SEVERE, null, e7);
            z = false;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e8) {
                }
            }
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e9) {
                }
            }
        }
        return z;
    }

    public final GeoPoint getStartPoint() {
        return START_POINT;
    }

    public final Byte getStartZoomLevel() {
        return START_ZOOM_LEVEL;
    }

    public final boolean requiresInternetConnection() {
        return true;
    }
}
