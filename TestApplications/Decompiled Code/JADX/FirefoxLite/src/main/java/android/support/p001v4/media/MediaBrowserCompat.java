package android.support.p001v4.media;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.p001v4.media.session.MediaSessionCompat;
import android.support.p001v4.p003os.ResultReceiver;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* renamed from: android.support.v4.media.MediaBrowserCompat */
public final class MediaBrowserCompat {
    static final boolean DEBUG = Log.isLoggable("MediaBrowserCompat", 3);

    /* renamed from: android.support.v4.media.MediaBrowserCompat$CustomActionCallback */
    public static abstract class CustomActionCallback {
        public void onError(String str, Bundle bundle, Bundle bundle2) {
        }

        public void onProgressUpdate(String str, Bundle bundle, Bundle bundle2) {
        }

        public void onResult(String str, Bundle bundle, Bundle bundle2) {
        }
    }

    /* renamed from: android.support.v4.media.MediaBrowserCompat$ItemCallback */
    public static abstract class ItemCallback {
        public void onError(String str) {
        }

        public void onItemLoaded(MediaItem mediaItem) {
        }
    }

    /* renamed from: android.support.v4.media.MediaBrowserCompat$MediaItem */
    public static class MediaItem implements Parcelable {
        public static final Creator<MediaItem> CREATOR = new C01341();
        private final MediaDescriptionCompat mDescription;
        private final int mFlags;

        /* renamed from: android.support.v4.media.MediaBrowserCompat$MediaItem$1 */
        static class C01341 implements Creator<MediaItem> {
            C01341() {
            }

            public MediaItem createFromParcel(Parcel parcel) {
                return new MediaItem(parcel);
            }

            public MediaItem[] newArray(int i) {
                return new MediaItem[i];
            }
        }

        public int describeContents() {
            return 0;
        }

        MediaItem(Parcel parcel) {
            this.mFlags = parcel.readInt();
            this.mDescription = (MediaDescriptionCompat) MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.mFlags);
            this.mDescription.writeToParcel(parcel, i);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("MediaItem{");
            stringBuilder.append("mFlags=");
            stringBuilder.append(this.mFlags);
            stringBuilder.append(", mDescription=");
            stringBuilder.append(this.mDescription);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    /* renamed from: android.support.v4.media.MediaBrowserCompat$SearchCallback */
    public static abstract class SearchCallback {
        public void onError(String str, Bundle bundle) {
        }

        public void onSearchResult(String str, Bundle bundle, List<MediaItem> list) {
        }
    }

    /* renamed from: android.support.v4.media.MediaBrowserCompat$CustomActionResultReceiver */
    private static class CustomActionResultReceiver extends ResultReceiver {
        private final String mAction;
        private final CustomActionCallback mCallback;
        private final Bundle mExtras;

        /* Access modifiers changed, original: protected */
        public void onReceiveResult(int i, Bundle bundle) {
            if (this.mCallback != null) {
                MediaSessionCompat.ensureClassLoader(bundle);
                switch (i) {
                    case -1:
                        this.mCallback.onError(this.mAction, this.mExtras, bundle);
                        break;
                    case 0:
                        this.mCallback.onResult(this.mAction, this.mExtras, bundle);
                        break;
                    case 1:
                        this.mCallback.onProgressUpdate(this.mAction, this.mExtras, bundle);
                        break;
                    default:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown result code: ");
                        stringBuilder.append(i);
                        stringBuilder.append(" (extras=");
                        stringBuilder.append(this.mExtras);
                        stringBuilder.append(", resultData=");
                        stringBuilder.append(bundle);
                        stringBuilder.append(")");
                        Log.w("MediaBrowserCompat", stringBuilder.toString());
                        break;
                }
            }
        }
    }

    /* renamed from: android.support.v4.media.MediaBrowserCompat$ItemReceiver */
    private static class ItemReceiver extends ResultReceiver {
        private final ItemCallback mCallback;
        private final String mMediaId;

        /* Access modifiers changed, original: protected */
        public void onReceiveResult(int i, Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            if (i == 0 && bundle != null && bundle.containsKey("media_item")) {
                Parcelable parcelable = bundle.getParcelable("media_item");
                if (parcelable == null || (parcelable instanceof MediaItem)) {
                    this.mCallback.onItemLoaded((MediaItem) parcelable);
                } else {
                    this.mCallback.onError(this.mMediaId);
                }
                return;
            }
            this.mCallback.onError(this.mMediaId);
        }
    }

    /* renamed from: android.support.v4.media.MediaBrowserCompat$SearchResultReceiver */
    private static class SearchResultReceiver extends ResultReceiver {
        private final SearchCallback mCallback;
        private final Bundle mExtras;
        private final String mQuery;

        /* Access modifiers changed, original: protected */
        public void onReceiveResult(int i, Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            if (i == 0 && bundle != null && bundle.containsKey("search_results")) {
                Parcelable[] parcelableArray = bundle.getParcelableArray("search_results");
                List list = null;
                if (parcelableArray != null) {
                    list = new ArrayList();
                    for (Parcelable parcelable : parcelableArray) {
                        list.add((MediaItem) parcelable);
                    }
                }
                this.mCallback.onSearchResult(this.mQuery, this.mExtras, list);
                return;
            }
            this.mCallback.onError(this.mQuery, this.mExtras);
        }
    }
}
