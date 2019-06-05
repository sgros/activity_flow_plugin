// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media.session;

import android.os.IBinder;
import android.os.ResultReceiver;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import android.os.Build$VERSION;
import android.os.Parcel;
import android.support.v4.media.MediaDescriptionCompat;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import android.os.Bundle;

public class MediaSessionCompat
{
    public static void ensureClassLoader(final Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
        }
    }
    
    public static final class QueueItem implements Parcelable
    {
        public static final Parcelable$Creator<QueueItem> CREATOR;
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private Object mItem;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<QueueItem>() {
                public QueueItem createFromParcel(final Parcel parcel) {
                    return new QueueItem(parcel);
                }
                
                public QueueItem[] newArray(final int n) {
                    return new QueueItem[n];
                }
            };
        }
        
        QueueItem(final Parcel parcel) {
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            this.mId = parcel.readLong();
        }
        
        private QueueItem(final Object mItem, final MediaDescriptionCompat mDescription, final long mId) {
            if (mDescription == null) {
                throw new IllegalArgumentException("Description cannot be null.");
            }
            if (mId != -1L) {
                this.mDescription = mDescription;
                this.mId = mId;
                this.mItem = mItem;
                return;
            }
            throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
        }
        
        public static QueueItem fromQueueItem(final Object o) {
            if (o != null && Build$VERSION.SDK_INT >= 21) {
                return new QueueItem(o, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(o)), MediaSessionCompatApi21.QueueItem.getQueueId(o));
            }
            return null;
        }
        
        public static List<QueueItem> fromQueueItemList(final List<?> list) {
            if (list != null && Build$VERSION.SDK_INT >= 21) {
                final ArrayList<QueueItem> list2 = new ArrayList<QueueItem>();
                final Iterator<?> iterator = list.iterator();
                while (iterator.hasNext()) {
                    list2.add(fromQueueItem(iterator.next()));
                }
                return list2;
            }
            return null;
        }
        
        public int describeContents() {
            return 0;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("MediaSession.QueueItem {Description=");
            sb.append(this.mDescription);
            sb.append(", Id=");
            sb.append(this.mId);
            sb.append(" }");
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            this.mDescription.writeToParcel(parcel, n);
            parcel.writeLong(this.mId);
        }
    }
    
    public static final class ResultReceiverWrapper implements Parcelable
    {
        public static final Parcelable$Creator<ResultReceiverWrapper> CREATOR;
        ResultReceiver mResultReceiver;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<ResultReceiverWrapper>() {
                public ResultReceiverWrapper createFromParcel(final Parcel parcel) {
                    return new ResultReceiverWrapper(parcel);
                }
                
                public ResultReceiverWrapper[] newArray(final int n) {
                    return new ResultReceiverWrapper[n];
                }
            };
        }
        
        ResultReceiverWrapper(final Parcel parcel) {
            this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            this.mResultReceiver.writeToParcel(parcel, n);
        }
    }
    
    public static final class Token implements Parcelable
    {
        public static final Parcelable$Creator<Token> CREATOR;
        private IMediaSession mExtraBinder;
        private final Object mInner;
        private Bundle mSessionToken2Bundle;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<Token>() {
                public Token createFromParcel(final Parcel parcel) {
                    Object o;
                    if (Build$VERSION.SDK_INT >= 21) {
                        o = parcel.readParcelable((ClassLoader)null);
                    }
                    else {
                        o = parcel.readStrongBinder();
                    }
                    return new Token(o);
                }
                
                public Token[] newArray(final int n) {
                    return new Token[n];
                }
            };
        }
        
        Token(final Object o) {
            this(o, null, null);
        }
        
        Token(final Object mInner, final IMediaSession mExtraBinder, final Bundle mSessionToken2Bundle) {
            this.mInner = mInner;
            this.mExtraBinder = mExtraBinder;
            this.mSessionToken2Bundle = mSessionToken2Bundle;
        }
        
        public int describeContents() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (!(o instanceof Token)) {
                return false;
            }
            final Token token = (Token)o;
            if (this.mInner == null) {
                if (token.mInner != null) {
                    b = false;
                }
                return b;
            }
            return token.mInner != null && this.mInner.equals(token.mInner);
        }
        
        public IMediaSession getExtraBinder() {
            return this.mExtraBinder;
        }
        
        @Override
        public int hashCode() {
            if (this.mInner == null) {
                return 0;
            }
            return this.mInner.hashCode();
        }
        
        public void setExtraBinder(final IMediaSession mExtraBinder) {
            this.mExtraBinder = mExtraBinder;
        }
        
        public void setSessionToken2Bundle(final Bundle mSessionToken2Bundle) {
            this.mSessionToken2Bundle = mSessionToken2Bundle;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            if (Build$VERSION.SDK_INT >= 21) {
                parcel.writeParcelable((Parcelable)this.mInner, n);
            }
            else {
                parcel.writeStrongBinder((IBinder)this.mInner);
            }
        }
    }
}
