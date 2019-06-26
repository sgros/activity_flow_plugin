package org.mapsforge.map.rendertheme;

import java.io.InputStream;

public enum InternalRenderTheme implements XmlRenderTheme {
   OSMARENDER("/osmarender/", "osmarender.xml");

   private final String absolutePath;
   private final String file;

   private InternalRenderTheme(String var3, String var4) {
      this.absolutePath = var3;
      this.file = var4;
   }

   public String getRelativePathPrefix() {
      return this.absolutePath;
   }

   public InputStream getRenderThemeAsStream() {
      return Thread.currentThread().getClass().getResourceAsStream(this.absolutePath + this.file);
   }
}
