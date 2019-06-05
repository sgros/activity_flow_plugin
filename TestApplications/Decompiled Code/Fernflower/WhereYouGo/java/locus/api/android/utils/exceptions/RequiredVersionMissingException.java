package locus.api.android.utils.exceptions;

import locus.api.android.utils.LocusUtils;

public class RequiredVersionMissingException extends Exception {
   private static final long serialVersionUID = 1L;
   private String mistake;

   public RequiredVersionMissingException(int var1) {
      this(var1, var1);
   }

   public RequiredVersionMissingException(int var1, int var2) {
      super("Required version: Free (" + var1 + "), " + "or Pro (" + var2 + "), not installed!");
   }

   public RequiredVersionMissingException(String var1, int var2) {
      super(String.format("Required application: '%s', version: '%s', not installed", var1, var2));
   }

   public RequiredVersionMissingException(LocusUtils.VersionCode var1) {
      super("Required version: Free (" + getVersionAsText(var1.vcFree) + "), or " + "Pro (" + getVersionAsText(var1.vcPro) + "), or " + "Gis (" + getVersionAsText(var1.vcGis) + "), not installed!");
   }

   private static String getVersionAsText(int var0) {
      String var1;
      if (var0 == 0) {
         var1 = "Not supported";
      } else {
         var1 = Integer.toString(var0);
      }

      return var1;
   }

   public String getError() {
      return this.mistake;
   }
}
