package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(9)
@RequiresApi(9)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class NotificationCompatBase {
   private static Method sSetLatestEventInfo;

   public static Notification add(Notification var0, Context var1, CharSequence var2, CharSequence var3, PendingIntent var4, PendingIntent var5) {
      if (sSetLatestEventInfo == null) {
         try {
            sSetLatestEventInfo = Notification.class.getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
         } catch (NoSuchMethodException var6) {
            throw new RuntimeException(var6);
         }
      }

      label19: {
         Object var9;
         try {
            sSetLatestEventInfo.invoke(var0, var1, var2, var3, var4);
            break label19;
         } catch (IllegalAccessException var7) {
            var9 = var7;
         } catch (InvocationTargetException var8) {
            var9 = var8;
         }

         throw new RuntimeException((Throwable)var9);
      }

      var0.fullScreenIntent = var5;
      return var0;
   }

   public abstract static class Action {
      public abstract PendingIntent getActionIntent();

      public abstract boolean getAllowGeneratedReplies();

      public abstract Bundle getExtras();

      public abstract int getIcon();

      public abstract RemoteInputCompatBase.RemoteInput[] getRemoteInputs();

      public abstract CharSequence getTitle();

      public interface Factory {
         NotificationCompatBase.Action build(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInputCompatBase.RemoteInput[] var5, boolean var6);

         NotificationCompatBase.Action[] newArray(int var1);
      }
   }

   public abstract static class UnreadConversation {
      abstract long getLatestTimestamp();

      abstract String[] getMessages();

      abstract String getParticipant();

      abstract String[] getParticipants();

      abstract PendingIntent getReadPendingIntent();

      abstract RemoteInputCompatBase.RemoteInput getRemoteInput();

      abstract PendingIntent getReplyPendingIntent();

      public interface Factory {
         NotificationCompatBase.UnreadConversation build(String[] var1, RemoteInputCompatBase.RemoteInput var2, PendingIntent var3, PendingIntent var4, String[] var5, long var6);
      }
   }
}
