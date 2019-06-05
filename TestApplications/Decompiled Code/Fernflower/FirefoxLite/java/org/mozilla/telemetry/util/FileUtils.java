package org.mozilla.telemetry.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {
   public static void assertDirectory(File var0) {
      StringBuilder var1;
      if (!var0.exists() && !var0.mkdirs()) {
         var1 = new StringBuilder();
         var1.append("Directory doesn't exist and can't be created: ");
         var1.append(var0.getAbsolutePath());
         throw new IllegalStateException(var1.toString());
      } else if (!var0.isDirectory() || !var0.canWrite()) {
         var1 = new StringBuilder();
         var1.append("Directory is not writable directory: ");
         var1.append(var0.getAbsolutePath());
         throw new IllegalStateException(var1.toString());
      }
   }

   public static class FileLastModifiedComparator implements Comparator {
      public int compare(File var1, File var2) {
         long var4;
         int var3 = (var4 = var1.lastModified() - var2.lastModified()) == 0L ? 0 : (var4 < 0L ? -1 : 1);
         if (var3 < 0) {
            return -1;
         } else {
            return var3 == 0 ? 0 : 1;
         }
      }
   }

   public static class FilenameRegexFilter implements FilenameFilter {
      private Matcher mCachedMatcher;
      private final Pattern mPattern;

      public FilenameRegexFilter(Pattern var1) {
         this.mPattern = var1;
      }

      public boolean accept(File var1, String var2) {
         if (this.mCachedMatcher == null) {
            this.mCachedMatcher = this.mPattern.matcher(var2);
         } else {
            this.mCachedMatcher.reset(var2);
         }

         return this.mCachedMatcher.matches();
      }
   }
}
