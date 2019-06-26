package org.telegram.messenger;

import java.util.ArrayList;
import java.util.Locale;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class WebFile extends TLObject {
   public ArrayList attributes;
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

   public static WebFile createWithGeoPoint(double var0, double var2, long var4, int var6, int var7, int var8, int var9) {
      WebFile var10 = new WebFile();
      TLRPC.TL_inputWebFileGeoPointLocation var11 = new TLRPC.TL_inputWebFileGeoPointLocation();
      var10.location = var11;
      TLRPC.TL_inputGeoPoint var12 = new TLRPC.TL_inputGeoPoint();
      var10.geo_point = var12;
      var11.geo_point = var12;
      var11.access_hash = var4;
      TLRPC.InputGeoPoint var13 = var10.geo_point;
      var13.lat = var0;
      var13._long = var2;
      var10.w = var6;
      var11.w = var6;
      var10.h = var7;
      var11.h = var7;
      var10.zoom = var8;
      var11.zoom = var8;
      var10.scale = var9;
      var11.scale = var9;
      var10.mime_type = "image/png";
      var10.url = String.format(Locale.US, "maps_%.6f_%.6f_%d_%d_%d_%d.png", var0, var2, var6, var7, var8, var9);
      var10.attributes = new ArrayList();
      return var10;
   }

   public static WebFile createWithGeoPoint(TLRPC.GeoPoint var0, int var1, int var2, int var3, int var4) {
      return createWithGeoPoint(var0.lat, var0._long, var0.access_hash, var1, var2, var3, var4);
   }

   public static WebFile createWithWebDocument(TLRPC.WebDocument var0) {
      if (!(var0 instanceof TLRPC.TL_webDocument)) {
         return null;
      } else {
         WebFile var1 = new WebFile();
         TLRPC.TL_webDocument var2 = (TLRPC.TL_webDocument)var0;
         TLRPC.TL_inputWebFileLocation var3 = new TLRPC.TL_inputWebFileLocation();
         var1.location = var3;
         String var4 = var0.url;
         var1.url = var4;
         var3.url = var4;
         var3.access_hash = var2.access_hash;
         var1.size = var2.size;
         var1.mime_type = var2.mime_type;
         var1.attributes = var2.attributes;
         return var1;
      }
   }
}
