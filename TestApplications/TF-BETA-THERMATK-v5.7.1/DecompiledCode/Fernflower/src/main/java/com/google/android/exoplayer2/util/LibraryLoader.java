package com.google.android.exoplayer2.util;

public final class LibraryLoader {
   private boolean isAvailable;
   private boolean loadAttempted;
   private String[] nativeLibraries;

   public LibraryLoader(String... var1) {
      this.nativeLibraries = var1;
   }

   public boolean isAvailable() {
      // $FF: Couldn't be decompiled
   }

   public void setLibraries(String... var1) {
      synchronized(this){}

      Throwable var10000;
      label80: {
         boolean var10001;
         boolean var2;
         label79: {
            label78: {
               try {
                  if (!this.loadAttempted) {
                     break label78;
                  }
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label80;
               }

               var2 = false;
               break label79;
            }

            var2 = true;
         }

         label72:
         try {
            Assertions.checkState(var2, "Cannot set libraries after loading");
            this.nativeLibraries = var1;
            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label72;
         }
      }

      Throwable var9 = var10000;
      throw var9;
   }
}
