package android.support.p001v4.media.session;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ResultReceiver;
import android.support.p001v4.media.MediaDescriptionCompat;
import java.util.ArrayList;
import java.util.List;

/* renamed from: android.support.v4.media.session.MediaSessionCompat */
public class MediaSessionCompat {

    /* renamed from: android.support.v4.media.session.MediaSessionCompat$QueueItem */
    public static final class QueueItem implements Parcelable {
        public static final Creator<QueueItem> CREATOR = new C01381();
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private Object mItem;

        /* renamed from: android.support.v4.media.session.MediaSessionCompat$QueueItem$1 */
        static class C01381 implements Creator<QueueItem> {
            C01381() {
            }

            public QueueItem createFromParcel(Parcel parcel) {
                return new QueueItem(parcel);
            }

            public QueueItem[] newArray(int i) {
                return new QueueItem[i];
            }
        }

        public int describeContents() {
            return 0;
        }

        private QueueItem(Object obj, MediaDescriptionCompat mediaDescriptionCompat, long j) {
            if (mediaDescriptionCompat == null) {
                throw new IllegalArgumentException("Description cannot be null.");
            } else if (j != -1) {
                this.mDescription = mediaDescriptionCompat;
                this.mId = j;
                this.mItem = obj;
            } else {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
        }

        QueueItem(Parcel parcel) {
            this.mDescription = (MediaDescriptionCompat) MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            this.mId = parcel.readLong();
        }

        public void writeToParcel(Parcel parcel, int i) {
            this.mDescription.writeToParcel(parcel, i);
            parcel.writeLong(this.mId);
        }

        public static QueueItem fromQueueItem(Object obj) {
            return (obj == null || VERSION.SDK_INT < 21) ? null : new QueueItem(obj, MediaDescriptionCompat.fromMediaDescription(android.support.p001v4.media.session.MediaSessionCompatApi21.QueueItem.getDescription(obj)), android.support.p001v4.media.session.MediaSessionCompatApi21.QueueItem.getQueueId(obj));
        }

        public static List<QueueItem> fromQueueItemList(List<?> list) {
            if (list == null || VERSION.SDK_INT < 21) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (Object fromQueueItem : list) {
                arrayList.add(QueueItem.fromQueueItem(fromQueueItem));
            }
            return arrayList;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MediaSession.QueueItem {Description=");
            stringBuilder.append(this.mDescription);
            stringBuilder.append(", Id=");
            stringBuilder.append(this.mId);
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    /* renamed from: android.support.v4.media.session.MediaSessionCompat$ResultReceiverWrapper */
    public static final class ResultReceiverWrapper implements Parcelable {
        public static final Creator<ResultReceiverWrapper> CREATOR = new C01391();
        ResultReceiver mResultReceiver;

        /* renamed from: android.support.v4.media.session.MediaSessionCompat$ResultReceiverWrapper$1 */
        static class C01391 implements Creator<ResultReceiverWrapper> {
            C01391() {
            }

            public ResultReceiverWrapper createFromParcel(Parcel parcel) {
                return new ResultReceiverWrapper(parcel);
            }

            public ResultReceiverWrapper[] newArray(int i) {
                return new ResultReceiverWrapper[i];
            }
        }

        public int describeContents() {
            return 0;
        }

        ResultReceiverWrapper(Parcel parcel) {
            this.mResultReceiver = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
        }

        public void writeToParcel(Parcel parcel, int i) {
            this.mResultReceiver.writeToParcel(parcel, i);
        }
    }

    /* renamed from: android.support.v4.media.session.MediaSessionCompat$Token */
    public static final class Token implements Parcelable {
        public static final Creator<Token> CREATOR = new C01401();
        private IMediaSession mExtraBinder;
        private final Object mInner;
        private Bundle mSessionToken2Bundle;

        /* renamed from: android.support.v4.media.session.MediaSessionCompat$Token$1 */
        static class C01401 implements Creator<Token> {
            C01401() {
            }

            public Token createFromParcel(Parcel parcel) {
                Object readParcelable;
                if (VERSION.SDK_INT >= 21) {
                    readParcelable = parcel.readParcelable(null);
                } else {
                    readParcelable = parcel.readStrongBinder();
                }
                return new Token(readParcelable);
            }

            public Token[] newArray(int i) {
                return new Token[i];
            }
        }

        public int describeContents() {
            return 0;
        }

        Token(Object obj) {
            this(obj, null, null);
        }

        Token(Object obj, IMediaSession iMediaSession, Bundle bundle) {
            this.mInner = obj;
            this.mExtraBinder = iMediaSession;
            this.mSessionToken2Bundle = bundle;
        }

        public void writeToParcel(Parcel parcel, int i) {
            if (VERSION.SDK_INT >= 21) {
                parcel.writeParcelable((Parcelable) this.mInner, i);
            } else {
                parcel.writeStrongBinder((IBinder) this.mInner);
            }
        }

        public int hashCode() {
            if (this.mInner == null) {
                return 0;
            }
            return this.mInner.hashCode();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Token)) {
                return false;
            }
            Token token = (Token) obj;
            if (this.mInner == null) {
                if (token.mInner != null) {
                    z = false;
                }
                return z;
            } else if (token.mInner == null) {
                return false;
            } else {
                return this.mInner.equals(token.mInner);
            }
        }

        public IMediaSession getExtraBinder() {
            return this.mExtraBinder;
        }

        public void setExtraBinder(IMediaSession iMediaSession) {
            this.mExtraBinder = iMediaSession;
        }

        public void setSessionToken2Bundle(Bundle bundle) {
            this.mSessionToken2Bundle = bundle;
        }
    }

    public static void ensureClassLoader(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
        }
    }
}
