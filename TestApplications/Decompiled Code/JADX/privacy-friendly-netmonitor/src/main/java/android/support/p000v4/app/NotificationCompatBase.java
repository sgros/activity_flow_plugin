package android.support.p000v4.app;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p000v4.app.RemoteInputCompatBase.RemoteInput;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.app.NotificationCompatBase */
public class NotificationCompatBase {

    /* renamed from: android.support.v4.app.NotificationCompatBase$Action */
    public static abstract class Action {

        /* renamed from: android.support.v4.app.NotificationCompatBase$Action$Factory */
        public interface Factory {
            Action build(int i, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInput[] remoteInputArr, RemoteInput[] remoteInputArr2, boolean z);

            Action[] newArray(int i);
        }

        public abstract PendingIntent getActionIntent();

        public abstract boolean getAllowGeneratedReplies();

        public abstract RemoteInput[] getDataOnlyRemoteInputs();

        public abstract Bundle getExtras();

        public abstract int getIcon();

        public abstract RemoteInput[] getRemoteInputs();

        public abstract CharSequence getTitle();
    }

    /* renamed from: android.support.v4.app.NotificationCompatBase$UnreadConversation */
    public static abstract class UnreadConversation {

        /* renamed from: android.support.v4.app.NotificationCompatBase$UnreadConversation$Factory */
        public interface Factory {
            UnreadConversation build(String[] strArr, RemoteInput remoteInput, PendingIntent pendingIntent, PendingIntent pendingIntent2, String[] strArr2, long j);
        }

        public abstract long getLatestTimestamp();

        public abstract String[] getMessages();

        public abstract String getParticipant();

        public abstract String[] getParticipants();

        public abstract PendingIntent getReadPendingIntent();

        public abstract RemoteInput getRemoteInput();

        public abstract PendingIntent getReplyPendingIntent();
    }
}
