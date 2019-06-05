package android.support.p001v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.p001v4.media.MediaDescriptionCompatApi21.Builder;
import android.text.TextUtils;

/* renamed from: android.support.v4.media.MediaDescriptionCompat */
public final class MediaDescriptionCompat implements Parcelable {
    public static final Creator<MediaDescriptionCompat> CREATOR = new C01351();
    private final CharSequence mDescription;
    private Object mDescriptionObj;
    private final Bundle mExtras;
    private final Bitmap mIcon;
    private final Uri mIconUri;
    private final String mMediaId;
    private final Uri mMediaUri;
    private final CharSequence mSubtitle;
    private final CharSequence mTitle;

    /* renamed from: android.support.v4.media.MediaDescriptionCompat$1 */
    static class C01351 implements Creator<MediaDescriptionCompat> {
        C01351() {
        }

        public MediaDescriptionCompat createFromParcel(Parcel parcel) {
            if (VERSION.SDK_INT < 21) {
                return new MediaDescriptionCompat(parcel);
            }
            return MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(parcel));
        }

        public MediaDescriptionCompat[] newArray(int i) {
            return new MediaDescriptionCompat[i];
        }
    }

    /* renamed from: android.support.v4.media.MediaDescriptionCompat$Builder */
    public static final class Builder {
        private CharSequence mDescription;
        private Bundle mExtras;
        private Bitmap mIcon;
        private Uri mIconUri;
        private String mMediaId;
        private Uri mMediaUri;
        private CharSequence mSubtitle;
        private CharSequence mTitle;

        public Builder setMediaId(String str) {
            this.mMediaId = str;
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = charSequence;
            return this;
        }

        public Builder setSubtitle(CharSequence charSequence) {
            this.mSubtitle = charSequence;
            return this;
        }

        public Builder setDescription(CharSequence charSequence) {
            this.mDescription = charSequence;
            return this;
        }

        public Builder setIconBitmap(Bitmap bitmap) {
            this.mIcon = bitmap;
            return this;
        }

        public Builder setIconUri(Uri uri) {
            this.mIconUri = uri;
            return this;
        }

        public Builder setExtras(Bundle bundle) {
            this.mExtras = bundle;
            return this;
        }

        public Builder setMediaUri(Uri uri) {
            this.mMediaUri = uri;
            return this;
        }

        public MediaDescriptionCompat build() {
            return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, this.mMediaUri);
        }
    }

    public int describeContents() {
        return 0;
    }

    MediaDescriptionCompat(String str, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, Bitmap bitmap, Uri uri, Bundle bundle, Uri uri2) {
        this.mMediaId = str;
        this.mTitle = charSequence;
        this.mSubtitle = charSequence2;
        this.mDescription = charSequence3;
        this.mIcon = bitmap;
        this.mIconUri = uri;
        this.mExtras = bundle;
        this.mMediaUri = uri2;
    }

    MediaDescriptionCompat(Parcel parcel) {
        this.mMediaId = parcel.readString();
        this.mTitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mSubtitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mDescription = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        ClassLoader classLoader = getClass().getClassLoader();
        this.mIcon = (Bitmap) parcel.readParcelable(classLoader);
        this.mIconUri = (Uri) parcel.readParcelable(classLoader);
        this.mExtras = parcel.readBundle(classLoader);
        this.mMediaUri = (Uri) parcel.readParcelable(classLoader);
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (VERSION.SDK_INT < 21) {
            parcel.writeString(this.mMediaId);
            TextUtils.writeToParcel(this.mTitle, parcel, i);
            TextUtils.writeToParcel(this.mSubtitle, parcel, i);
            TextUtils.writeToParcel(this.mDescription, parcel, i);
            parcel.writeParcelable(this.mIcon, i);
            parcel.writeParcelable(this.mIconUri, i);
            parcel.writeBundle(this.mExtras);
            parcel.writeParcelable(this.mMediaUri, i);
            return;
        }
        MediaDescriptionCompatApi21.writeToParcel(getMediaDescription(), parcel, i);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mTitle);
        stringBuilder.append(", ");
        stringBuilder.append(this.mSubtitle);
        stringBuilder.append(", ");
        stringBuilder.append(this.mDescription);
        return stringBuilder.toString();
    }

    public Object getMediaDescription() {
        if (this.mDescriptionObj != null || VERSION.SDK_INT < 21) {
            return this.mDescriptionObj;
        }
        Object newInstance = Builder.newInstance();
        Builder.setMediaId(newInstance, this.mMediaId);
        Builder.setTitle(newInstance, this.mTitle);
        Builder.setSubtitle(newInstance, this.mSubtitle);
        Builder.setDescription(newInstance, this.mDescription);
        Builder.setIconBitmap(newInstance, this.mIcon);
        Builder.setIconUri(newInstance, this.mIconUri);
        Bundle bundle = this.mExtras;
        if (VERSION.SDK_INT < 23 && this.mMediaUri != null) {
            if (bundle == null) {
                bundle = new Bundle();
                bundle.putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
            }
            bundle.putParcelable("android.support.v4.media.description.MEDIA_URI", this.mMediaUri);
        }
        Builder.setExtras(newInstance, bundle);
        if (VERSION.SDK_INT >= 23) {
            MediaDescriptionCompatApi23.Builder.setMediaUri(newInstance, this.mMediaUri);
        }
        this.mDescriptionObj = Builder.build(newInstance);
        return this.mDescriptionObj;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x006d  */
    public static android.support.p001v4.media.MediaDescriptionCompat fromMediaDescription(java.lang.Object r6) {
        /*
        r0 = 0;
        if (r6 == 0) goto L_0x0085;
    L_0x0003:
        r1 = android.os.Build.VERSION.SDK_INT;
        r2 = 21;
        if (r1 < r2) goto L_0x0085;
    L_0x0009:
        r1 = new android.support.v4.media.MediaDescriptionCompat$Builder;
        r1.<init>();
        r2 = android.support.p001v4.media.MediaDescriptionCompatApi21.getMediaId(r6);
        r1.setMediaId(r2);
        r2 = android.support.p001v4.media.MediaDescriptionCompatApi21.getTitle(r6);
        r1.setTitle(r2);
        r2 = android.support.p001v4.media.MediaDescriptionCompatApi21.getSubtitle(r6);
        r1.setSubtitle(r2);
        r2 = android.support.p001v4.media.MediaDescriptionCompatApi21.getDescription(r6);
        r1.setDescription(r2);
        r2 = android.support.p001v4.media.MediaDescriptionCompatApi21.getIconBitmap(r6);
        r1.setIconBitmap(r2);
        r2 = android.support.p001v4.media.MediaDescriptionCompatApi21.getIconUri(r6);
        r1.setIconUri(r2);
        r2 = android.support.p001v4.media.MediaDescriptionCompatApi21.getExtras(r6);
        if (r2 == 0) goto L_0x004a;
    L_0x003e:
        android.support.p001v4.media.session.MediaSessionCompat.ensureClassLoader(r2);
        r3 = "android.support.v4.media.description.MEDIA_URI";
        r3 = r2.getParcelable(r3);
        r3 = (android.net.Uri) r3;
        goto L_0x004b;
    L_0x004a:
        r3 = r0;
    L_0x004b:
        if (r3 == 0) goto L_0x0067;
    L_0x004d:
        r4 = "android.support.v4.media.description.NULL_BUNDLE_FLAG";
        r4 = r2.containsKey(r4);
        if (r4 == 0) goto L_0x005d;
    L_0x0055:
        r4 = r2.size();
        r5 = 2;
        if (r4 != r5) goto L_0x005d;
    L_0x005c:
        goto L_0x0068;
    L_0x005d:
        r0 = "android.support.v4.media.description.MEDIA_URI";
        r2.remove(r0);
        r0 = "android.support.v4.media.description.NULL_BUNDLE_FLAG";
        r2.remove(r0);
    L_0x0067:
        r0 = r2;
    L_0x0068:
        r1.setExtras(r0);
        if (r3 == 0) goto L_0x0071;
    L_0x006d:
        r1.setMediaUri(r3);
        goto L_0x007e;
    L_0x0071:
        r0 = android.os.Build.VERSION.SDK_INT;
        r2 = 23;
        if (r0 < r2) goto L_0x007e;
    L_0x0077:
        r0 = android.support.p001v4.media.MediaDescriptionCompatApi23.getMediaUri(r6);
        r1.setMediaUri(r0);
    L_0x007e:
        r0 = r1.build();
        r0.mDescriptionObj = r6;
        return r0;
    L_0x0085:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.media.MediaDescriptionCompat.fromMediaDescription(java.lang.Object):android.support.v4.media.MediaDescriptionCompat");
    }
}
