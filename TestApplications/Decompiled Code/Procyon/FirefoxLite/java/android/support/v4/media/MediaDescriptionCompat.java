// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media;

import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.os.Build$VERSION;
import android.os.Parcel;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class MediaDescriptionCompat implements Parcelable
{
    public static final Parcelable$Creator<MediaDescriptionCompat> CREATOR;
    private final CharSequence mDescription;
    private Object mDescriptionObj;
    private final Bundle mExtras;
    private final Bitmap mIcon;
    private final Uri mIconUri;
    private final String mMediaId;
    private final Uri mMediaUri;
    private final CharSequence mSubtitle;
    private final CharSequence mTitle;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MediaDescriptionCompat>() {
            public MediaDescriptionCompat createFromParcel(final Parcel parcel) {
                if (Build$VERSION.SDK_INT < 21) {
                    return new MediaDescriptionCompat(parcel);
                }
                return MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(parcel));
            }
            
            public MediaDescriptionCompat[] newArray(final int n) {
                return new MediaDescriptionCompat[n];
            }
        };
    }
    
    MediaDescriptionCompat(final Parcel parcel) {
        this.mMediaId = parcel.readString();
        this.mTitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mSubtitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mDescription = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        final ClassLoader classLoader = this.getClass().getClassLoader();
        this.mIcon = (Bitmap)parcel.readParcelable(classLoader);
        this.mIconUri = (Uri)parcel.readParcelable(classLoader);
        this.mExtras = parcel.readBundle(classLoader);
        this.mMediaUri = (Uri)parcel.readParcelable(classLoader);
    }
    
    MediaDescriptionCompat(final String mMediaId, final CharSequence mTitle, final CharSequence mSubtitle, final CharSequence mDescription, final Bitmap mIcon, final Uri mIconUri, final Bundle mExtras, final Uri mMediaUri) {
        this.mMediaId = mMediaId;
        this.mTitle = mTitle;
        this.mSubtitle = mSubtitle;
        this.mDescription = mDescription;
        this.mIcon = mIcon;
        this.mIconUri = mIconUri;
        this.mExtras = mExtras;
        this.mMediaUri = mMediaUri;
    }
    
    public static MediaDescriptionCompat fromMediaDescription(final Object mDescriptionObj) {
        Bundle extras = null;
        if (mDescriptionObj != null && Build$VERSION.SDK_INT >= 21) {
            final Builder builder = new Builder();
            builder.setMediaId(MediaDescriptionCompatApi21.getMediaId(mDescriptionObj));
            builder.setTitle(MediaDescriptionCompatApi21.getTitle(mDescriptionObj));
            builder.setSubtitle(MediaDescriptionCompatApi21.getSubtitle(mDescriptionObj));
            builder.setDescription(MediaDescriptionCompatApi21.getDescription(mDescriptionObj));
            builder.setIconBitmap(MediaDescriptionCompatApi21.getIconBitmap(mDescriptionObj));
            builder.setIconUri(MediaDescriptionCompatApi21.getIconUri(mDescriptionObj));
            final Bundle extras2 = MediaDescriptionCompatApi21.getExtras(mDescriptionObj);
            Uri mediaUri;
            if (extras2 != null) {
                MediaSessionCompat.ensureClassLoader(extras2);
                mediaUri = (Uri)extras2.getParcelable("android.support.v4.media.description.MEDIA_URI");
            }
            else {
                mediaUri = null;
            }
            Label_0145: {
                if (mediaUri != null) {
                    if (extras2.containsKey("android.support.v4.media.description.NULL_BUNDLE_FLAG") && extras2.size() == 2) {
                        break Label_0145;
                    }
                    extras2.remove("android.support.v4.media.description.MEDIA_URI");
                    extras2.remove("android.support.v4.media.description.NULL_BUNDLE_FLAG");
                }
                extras = extras2;
            }
            builder.setExtras(extras);
            if (mediaUri != null) {
                builder.setMediaUri(mediaUri);
            }
            else if (Build$VERSION.SDK_INT >= 23) {
                builder.setMediaUri(MediaDescriptionCompatApi23.getMediaUri(mDescriptionObj));
            }
            final MediaDescriptionCompat build = builder.build();
            build.mDescriptionObj = mDescriptionObj;
            return build;
        }
        return null;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Object getMediaDescription() {
        if (this.mDescriptionObj == null && Build$VERSION.SDK_INT >= 21) {
            final Object instance = MediaDescriptionCompatApi21.Builder.newInstance();
            MediaDescriptionCompatApi21.Builder.setMediaId(instance, this.mMediaId);
            MediaDescriptionCompatApi21.Builder.setTitle(instance, this.mTitle);
            MediaDescriptionCompatApi21.Builder.setSubtitle(instance, this.mSubtitle);
            MediaDescriptionCompatApi21.Builder.setDescription(instance, this.mDescription);
            MediaDescriptionCompatApi21.Builder.setIconBitmap(instance, this.mIcon);
            MediaDescriptionCompatApi21.Builder.setIconUri(instance, this.mIconUri);
            Bundle mExtras;
            final Bundle bundle = mExtras = this.mExtras;
            if (Build$VERSION.SDK_INT < 23) {
                mExtras = bundle;
                if (this.mMediaUri != null) {
                    if ((mExtras = bundle) == null) {
                        mExtras = new Bundle();
                        mExtras.putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
                    }
                    mExtras.putParcelable("android.support.v4.media.description.MEDIA_URI", (Parcelable)this.mMediaUri);
                }
            }
            MediaDescriptionCompatApi21.Builder.setExtras(instance, mExtras);
            if (Build$VERSION.SDK_INT >= 23) {
                MediaDescriptionCompatApi23.Builder.setMediaUri(instance, this.mMediaUri);
            }
            return this.mDescriptionObj = MediaDescriptionCompatApi21.Builder.build(instance);
        }
        return this.mDescriptionObj;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append((Object)this.mTitle);
        sb.append(", ");
        sb.append((Object)this.mSubtitle);
        sb.append(", ");
        sb.append((Object)this.mDescription);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        if (Build$VERSION.SDK_INT < 21) {
            parcel.writeString(this.mMediaId);
            TextUtils.writeToParcel(this.mTitle, parcel, n);
            TextUtils.writeToParcel(this.mSubtitle, parcel, n);
            TextUtils.writeToParcel(this.mDescription, parcel, n);
            parcel.writeParcelable((Parcelable)this.mIcon, n);
            parcel.writeParcelable((Parcelable)this.mIconUri, n);
            parcel.writeBundle(this.mExtras);
            parcel.writeParcelable((Parcelable)this.mMediaUri, n);
        }
        else {
            MediaDescriptionCompatApi21.writeToParcel(this.getMediaDescription(), parcel, n);
        }
    }
    
    public static final class Builder
    {
        private CharSequence mDescription;
        private Bundle mExtras;
        private Bitmap mIcon;
        private Uri mIconUri;
        private String mMediaId;
        private Uri mMediaUri;
        private CharSequence mSubtitle;
        private CharSequence mTitle;
        
        public MediaDescriptionCompat build() {
            return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, this.mMediaUri);
        }
        
        public Builder setDescription(final CharSequence mDescription) {
            this.mDescription = mDescription;
            return this;
        }
        
        public Builder setExtras(final Bundle mExtras) {
            this.mExtras = mExtras;
            return this;
        }
        
        public Builder setIconBitmap(final Bitmap mIcon) {
            this.mIcon = mIcon;
            return this;
        }
        
        public Builder setIconUri(final Uri mIconUri) {
            this.mIconUri = mIconUri;
            return this;
        }
        
        public Builder setMediaId(final String mMediaId) {
            this.mMediaId = mMediaId;
            return this;
        }
        
        public Builder setMediaUri(final Uri mMediaUri) {
            this.mMediaUri = mMediaUri;
            return this;
        }
        
        public Builder setSubtitle(final CharSequence mSubtitle) {
            this.mSubtitle = mSubtitle;
            return this;
        }
        
        public Builder setTitle(final CharSequence mTitle) {
            this.mTitle = mTitle;
            return this;
        }
    }
}
