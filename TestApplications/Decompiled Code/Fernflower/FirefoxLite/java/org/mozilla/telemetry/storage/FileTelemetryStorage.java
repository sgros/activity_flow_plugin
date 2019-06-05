package org.mozilla.telemetry.storage;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;
import mozilla.components.support.base.log.logger.Logger;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.ping.TelemetryPing;
import org.mozilla.telemetry.serialize.TelemetryPingSerializer;
import org.mozilla.telemetry.util.FileUtils;

public class FileTelemetryStorage implements TelemetryStorage {
   private final TelemetryConfiguration configuration;
   private final Logger logger = new Logger("telemetry/storage");
   private final TelemetryPingSerializer serializer;
   private final File storageDirectory;

   public FileTelemetryStorage(TelemetryConfiguration var1, TelemetryPingSerializer var2) {
      this.configuration = var1;
      this.serializer = var2;
      this.storageDirectory = new File(var1.getDataDirectory(), "storage");
      FileUtils.assertDirectory(this.storageDirectory);
   }

   private void maybePrunePings(String var1) {
      File[] var8 = this.listPingFiles(var1);
      int var2 = var8.length - this.configuration.getMaximumNumberOfPingsPerType();
      if (var2 > 0) {
         ArrayList var9 = new ArrayList(Arrays.asList(var8));
         Collections.sort(var9, new FileUtils.FileLastModifiedComparator());
         Iterator var3 = var9.iterator();

         StringBuilder var6;
         while(var3.hasNext()) {
            File var4 = (File)var3.next();
            PrintStream var5 = System.out;
            var6 = new StringBuilder();
            var6.append(var4.lastModified());
            var6.append(" ");
            var6.append(var4.getAbsolutePath());
            var5.println(var6.toString());
         }

         for(int var7 = 0; var7 < var2; ++var7) {
            File var11 = (File)var9.get(var7);
            if (!var11.delete()) {
               Logger var10 = this.logger;
               var6 = new StringBuilder();
               var6.append("Can't prune ping file: ");
               var6.append(var11.getAbsolutePath());
               var10.warn(var6.toString(), new IOException());
            }
         }

      }
   }

   private void storePing(TelemetryPing param1) {
      // $FF: Couldn't be decompiled
   }

   public int countStoredPings(String var1) {
      return this.listPingFiles(var1).length;
   }

   File[] listPingFiles(String var1) {
      File[] var2 = (new File(this.storageDirectory, var1)).listFiles(new FileUtils.FilenameRegexFilter(Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")));
      return var2 == null ? new File[0] : var2;
   }

   public boolean process(String param1, TelemetryStorage.TelemetryStorageCallback param2) {
      // $FF: Couldn't be decompiled
   }

   public void store(TelemetryPing var1) {
      synchronized(this){}

      try {
         this.storePing(var1);
         this.maybePrunePings(var1.getType());
      } finally {
         ;
      }

   }
}
