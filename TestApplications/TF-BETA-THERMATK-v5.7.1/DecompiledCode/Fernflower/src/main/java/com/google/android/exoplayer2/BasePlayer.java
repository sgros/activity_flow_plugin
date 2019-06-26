package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Util;

public abstract class BasePlayer implements Player {
   protected final Timeline.Window window = new Timeline.Window();

   public final int getBufferedPercentage() {
      long var1 = this.getBufferedPosition();
      long var3 = this.getDuration();
      int var5 = 100;
      if (var1 != -9223372036854775807L && var3 != -9223372036854775807L) {
         if (var3 != 0L) {
            var5 = Util.constrainValue((int)(var1 * 100L / var3), 0, 100);
         }
      } else {
         var5 = 0;
      }

      return var5;
   }

   public final long getContentDuration() {
      Timeline var1 = this.getCurrentTimeline();
      long var2;
      if (var1.isEmpty()) {
         var2 = -9223372036854775807L;
      } else {
         var2 = var1.getWindow(this.getCurrentWindowIndex(), this.window).getDurationMs();
      }

      return var2;
   }

   public final void seekTo(long var1) {
      this.seekTo(this.getCurrentWindowIndex(), var1);
   }

   protected static final class ListenerHolder {
      public final Player.EventListener listener;
      private boolean released;

      public ListenerHolder(Player.EventListener var1) {
         this.listener = var1;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            return var1 != null && BasePlayer.ListenerHolder.class == var1.getClass() ? this.listener.equals(((BasePlayer.ListenerHolder)var1).listener) : false;
         }
      }

      public int hashCode() {
         return this.listener.hashCode();
      }

      public void invoke(BasePlayer.ListenerInvocation var1) {
         if (!this.released) {
            var1.invokeListener(this.listener);
         }

      }
   }

   protected interface ListenerInvocation {
      void invokeListener(Player.EventListener var1);
   }
}
