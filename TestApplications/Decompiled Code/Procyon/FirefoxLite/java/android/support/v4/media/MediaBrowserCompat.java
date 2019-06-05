// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.os.Bundle;
import android.util.Log;

public final class MediaBrowserCompat
{
    static final boolean DEBUG;
    
    static {
        DEBUG = Log.isLoggable("MediaBrowserCompat", 3);
    }
    
    public abstract static class CustomActionCallback
    {
        public void onError(final String s, final Bundle bundle, final Bundle bundle2) {
        }
        
        public void onProgressUpdate(final String s, final Bundle bundle, final Bundle bundle2) {
        }
        
        public void onResult(final String s, final Bundle bundle, final Bundle bundle2) {
        }
    }
    
    private static class CustomActionResultReceiver extends ResultReceiver
    {
        private final String mAction;
        private final CustomActionCallback mCallback;
        private final Bundle mExtras;
        
        @Override
        protected void onReceiveResult(final int i, final Bundle obj) {
            if (this.mCallback == null) {
                return;
            }
            MediaSessionCompat.ensureClassLoader(obj);
            switch (i) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown result code: ");
                    sb.append(i);
                    sb.append(" (extras=");
                    sb.append(this.mExtras);
                    sb.append(", resultData=");
                    sb.append(obj);
                    sb.append(")");
                    Log.w("MediaBrowserCompat", sb.toString());
                    break;
                }
                case 1: {
                    this.mCallback.onProgressUpdate(this.mAction, this.mExtras, obj);
                    break;
                }
                case 0: {
                    this.mCallback.onResult(this.mAction, this.mExtras, obj);
                    break;
                }
                case -1: {
                    this.mCallback.onError(this.mAction, this.mExtras, obj);
                    break;
                }
            }
        }
    }
    
    public abstract static class ItemCallback
    {
        public void onError(final String s) {
        }
        
        public void onItemLoaded(final MediaItem mediaItem) {
        }
    }
    
    private static class ItemReceiver extends ResultReceiver
    {
        private final ItemCallback mCallback;
        private final String mMediaId;
        
        @Override
        protected void onReceiveResult(final int n, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            if (n == 0 && bundle != null && bundle.containsKey("media_item")) {
                final Parcelable parcelable = bundle.getParcelable("media_item");
                if (parcelable != null && !(parcelable instanceof MediaItem)) {
                    this.mCallback.onError(this.mMediaId);
                }
                else {
                    this.mCallback.onItemLoaded((MediaItem)parcelable);
                }
                return;
            }
            this.mCallback.onError(this.mMediaId);
        }
    }
    
    public static class MediaItem implements Parcelable
    {
        public static final Parcelable$Creator<MediaItem> CREATOR;
        private final MediaDescriptionCompat mDescription;
        private final int mFlags;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<MediaItem>() {
                public MediaItem createFromParcel(final Parcel parcel) {
                    return new MediaItem(parcel);
                }
                
                public MediaItem[] newArray(final int n) {
                    return new MediaItem[n];
                }
            };
        }
        
        MediaItem(final Parcel parcel) {
            this.mFlags = parcel.readInt();
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
        }
        
        public int describeContents() {
            return 0;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MediaItem{");
            sb.append("mFlags=");
            sb.append(this.mFlags);
            sb.append(", mDescription=");
            sb.append(this.mDescription);
            sb.append('}');
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.mFlags);
            this.mDescription.writeToParcel(parcel, n);
        }
    }
    
    public abstract static class SearchCallback
    {
        public void onError(final String s, final Bundle bundle) {
        }
        
        public void onSearchResult(final String s, final Bundle bundle, final List<MediaItem> list) {
        }
    }
    
    private static class SearchResultReceiver extends ResultReceiver
    {
        private final SearchCallback mCallback;
        private final Bundle mExtras;
        private final String mQuery;
        
        @Override
        protected void onReceiveResult(int n, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            if (n == 0 && bundle != null && bundle.containsKey("search_results")) {
                final Parcelable[] parcelableArray = bundle.getParcelableArray("search_results");
                List<MediaItem> list = null;
                if (parcelableArray != null) {
                    final ArrayList<MediaItem> list2 = new ArrayList<MediaItem>();
                    final int length = parcelableArray.length;
                    n = 0;
                    while (true) {
                        list = list2;
                        if (n >= length) {
                            break;
                        }
                        list2.add((MediaItem)parcelableArray[n]);
                        ++n;
                    }
                }
                this.mCallback.onSearchResult(this.mQuery, this.mExtras, list);
                return;
            }
            this.mCallback.onError(this.mQuery, this.mExtras);
        }
    }
}
