// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.util.Locale;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.tgnet.TLObject;

public class WebFile extends TLObject
{
    public ArrayList<TLRPC.DocumentAttribute> attributes;
    public TLRPC.InputGeoPoint geo_point;
    public int h;
    public TLRPC.InputWebFileLocation location;
    public String mime_type;
    public int msg_id;
    public TLRPC.InputPeer peer;
    public int scale;
    public int size;
    public String url;
    public int w;
    public int zoom;
    
    public static WebFile createWithGeoPoint(final double n, final double n2, final long access_hash, final int i, final int j, final int k, final int l) {
        final WebFile webFile = new WebFile();
        final TLRPC.TL_inputWebFileGeoPointLocation location = new TLRPC.TL_inputWebFileGeoPointLocation();
        webFile.location = location;
        final TLRPC.TL_inputGeoPoint tl_inputGeoPoint = new TLRPC.TL_inputGeoPoint();
        webFile.geo_point = tl_inputGeoPoint;
        location.geo_point = tl_inputGeoPoint;
        location.access_hash = access_hash;
        final TLRPC.InputGeoPoint geo_point = webFile.geo_point;
        geo_point.lat = n;
        geo_point._long = n2;
        webFile.w = i;
        location.w = i;
        webFile.h = j;
        location.h = j;
        webFile.zoom = k;
        location.zoom = k;
        webFile.scale = l;
        location.scale = l;
        webFile.mime_type = "image/png";
        webFile.url = String.format(Locale.US, "maps_%.6f_%.6f_%d_%d_%d_%d.png", n, n2, i, j, k, l);
        webFile.attributes = new ArrayList<TLRPC.DocumentAttribute>();
        return webFile;
    }
    
    public static WebFile createWithGeoPoint(final TLRPC.GeoPoint geoPoint, final int n, final int n2, final int n3, final int n4) {
        return createWithGeoPoint(geoPoint.lat, geoPoint._long, geoPoint.access_hash, n, n2, n3, n4);
    }
    
    public static WebFile createWithWebDocument(final TLRPC.WebDocument webDocument) {
        if (!(webDocument instanceof TLRPC.TL_webDocument)) {
            return null;
        }
        final WebFile webFile = new WebFile();
        final TLRPC.TL_webDocument tl_webDocument = (TLRPC.TL_webDocument)webDocument;
        final TLRPC.TL_inputWebFileLocation location = new TLRPC.TL_inputWebFileLocation();
        webFile.location = location;
        final String url = webDocument.url;
        webFile.url = url;
        location.url = url;
        location.access_hash = tl_webDocument.access_hash;
        webFile.size = tl_webDocument.size;
        webFile.mime_type = tl_webDocument.mime_type;
        webFile.attributes = tl_webDocument.attributes;
        return webFile;
    }
}
