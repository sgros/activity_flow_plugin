package com.google.android.exoplayer2;

public final class RendererConfiguration {
   public static final RendererConfiguration DEFAULT = new RendererConfiguration(0);
   public final int tunnelingAudioSessionId;

   public RendererConfiguration(int var1) {
      this.tunnelingAudioSessionId = var1;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && RendererConfiguration.class == var1.getClass()) {
         RendererConfiguration var3 = (RendererConfiguration)var1;
         if (this.tunnelingAudioSessionId != var3.tunnelingAudioSessionId) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.tunnelingAudioSessionId;
   }
}
