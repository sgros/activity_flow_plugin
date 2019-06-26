package com.google.android.exoplayer2.video;

// $FF: synthetic class
public final class _$$Lambda$VideoRendererEventListener$EventDispatcher$TaBV3X3b5lKElsQ7tczViKAyQ3w implements Runnable {
   // $FF: synthetic field
   private final VideoRendererEventListener.EventDispatcher f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final float f$4;

   // $FF: synthetic method
   public _$$Lambda$VideoRendererEventListener$EventDispatcher$TaBV3X3b5lKElsQ7tczViKAyQ3w(VideoRendererEventListener.EventDispatcher var1, int var2, int var3, int var4, float var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$videoSizeChanged$4$VideoRendererEventListener$EventDispatcher(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
