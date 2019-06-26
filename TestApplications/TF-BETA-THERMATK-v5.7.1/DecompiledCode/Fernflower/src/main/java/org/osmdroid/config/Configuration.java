package org.osmdroid.config;

public class Configuration {
   private static IConfigurationProvider ref;

   public static IConfigurationProvider getInstance() {
      synchronized(Configuration.class){}

      IConfigurationProvider var3;
      try {
         if (ref == null) {
            DefaultConfigurationProvider var0 = new DefaultConfigurationProvider();
            ref = var0;
         }

         var3 = ref;
      } finally {
         ;
      }

      return var3;
   }
}
