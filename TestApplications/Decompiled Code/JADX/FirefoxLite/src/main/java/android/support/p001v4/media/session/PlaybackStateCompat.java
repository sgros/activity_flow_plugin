package android.support.p001v4.media.session;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/* renamed from: android.support.v4.media.session.PlaybackStateCompat */
public final class PlaybackStateCompat implements Parcelable {
    public static final Creator<PlaybackStateCompat> CREATOR = new C01421();
    final long mActions;
    final long mActiveItemId;
    final long mBufferedPosition;
    List<CustomAction> mCustomActions;
    final int mErrorCode;
    final CharSequence mErrorMessage;
    final Bundle mExtras;
    final long mPosition;
    final float mSpeed;
    final int mState;
    private Object mStateObj;
    final long mUpdateTime;

    /* renamed from: android.support.v4.media.session.PlaybackStateCompat$1 */
    static class C01421 implements Creator<PlaybackStateCompat> {
        C01421() {
        }

        public PlaybackStateCompat createFromParcel(Parcel parcel) {
            return new PlaybackStateCompat(parcel);
        }

        public PlaybackStateCompat[] newArray(int i) {
            return new PlaybackStateCompat[i];
        }
    }

    /* renamed from: android.support.v4.media.session.PlaybackStateCompat$CustomAction */
    public static final class CustomAction implements Parcelable {
        public static final Creator<CustomAction> CREATOR = new C01431();
        private final String mAction;
        private Object mCustomActionObj;
        private final Bundle mExtras;
        private final int mIcon;
        private final CharSequence mName;

        /* renamed from: android.support.v4.media.session.PlaybackStateCompat$CustomAction$1 */
        static class C01431 implements Creator<CustomAction> {
            C01431() {
            }

            public CustomAction createFromParcel(Parcel parcel) {
                return new CustomAction(parcel);
            }

            public CustomAction[] newArray(int i) {
                return new CustomAction[i];
            }
        }

        public int describeContents() {
            return 0;
        }

        CustomAction(String str, CharSequence charSequence, int i, Bundle bundle) {
            this.mAction = str;
            this.mName = charSequence;
            this.mIcon = i;
            this.mExtras = bundle;
        }

        CustomAction(Parcel parcel) {
            this.mAction = parcel.readString();
            this.mName = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.mIcon = parcel.readInt();
            this.mExtras = parcel.readBundle(MediaSessionCompat.class.getClassLoader());
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mAction);
            TextUtils.writeToParcel(this.mName, parcel, i);
            parcel.writeInt(this.mIcon);
            parcel.writeBundle(this.mExtras);
        }

        public static CustomAction fromCustomAction(Object obj) {
            if (obj == null || VERSION.SDK_INT < 21) {
                return null;
            }
            CustomAction customAction = new CustomAction(android.support.p001v4.media.session.PlaybackStateCompatApi21.CustomAction.getAction(obj), android.support.p001v4.media.session.PlaybackStateCompatApi21.CustomAction.getName(obj), android.support.p001v4.media.session.PlaybackStateCompatApi21.CustomAction.getIcon(obj), android.support.p001v4.media.session.PlaybackStateCompatApi21.CustomAction.getExtras(obj));
            customAction.mCustomActionObj = obj;
            return customAction;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Action:mName='");
            stringBuilder.append(this.mName);
            stringBuilder.append(", mIcon=");
            stringBuilder.append(this.mIcon);
            stringBuilder.append(", mExtras=");
            stringBuilder.append(this.mExtras);
            return stringBuilder.toString();
        }
    }

    public int describeContents() {
        return 0;
    }

    PlaybackStateCompat(int i, long j, long j2, float f, long j3, int i2, CharSequence charSequence, long j4, List<CustomAction> list, long j5, Bundle bundle) {
        this.mState = i;
        this.mPosition = j;
        this.mBufferedPosition = j2;
        this.mSpeed = f;
        this.mActions = j3;
        this.mErrorCode = i2;
        this.mErrorMessage = charSequence;
        this.mUpdateTime = j4;
        this.mCustomActions = new ArrayList(list);
        this.mActiveItemId = j5;
        this.mExtras = bundle;
    }

    PlaybackStateCompat(Parcel parcel) {
        this.mState = parcel.readInt();
        this.mPosition = parcel.readLong();
        this.mSpeed = parcel.readFloat();
        this.mUpdateTime = parcel.readLong();
        this.mBufferedPosition = parcel.readLong();
        this.mActions = parcel.readLong();
        this.mErrorMessage = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mCustomActions = parcel.createTypedArrayList(CustomAction.CREATOR);
        this.mActiveItemId = parcel.readLong();
        this.mExtras = parcel.readBundle(MediaSessionCompat.class.getClassLoader());
        this.mErrorCode = parcel.readInt();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("PlaybackState {");
        stringBuilder.append("state=");
        stringBuilder.append(this.mState);
        stringBuilder.append(", position=");
        stringBuilder.append(this.mPosition);
        stringBuilder.append(", buffered position=");
        stringBuilder.append(this.mBufferedPosition);
        stringBuilder.append(", speed=");
        stringBuilder.append(this.mSpeed);
        stringBuilder.append(", updated=");
        stringBuilder.append(this.mUpdateTime);
        stringBuilder.append(", actions=");
        stringBuilder.append(this.mActions);
        stringBuilder.append(", error code=");
        stringBuilder.append(this.mErrorCode);
        stringBuilder.append(", error message=");
        stringBuilder.append(this.mErrorMessage);
        stringBuilder.append(", custom actions=");
        stringBuilder.append(this.mCustomActions);
        stringBuilder.append(", active item id=");
        stringBuilder.append(this.mActiveItemId);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mState);
        parcel.writeLong(this.mPosition);
        parcel.writeFloat(this.mSpeed);
        parcel.writeLong(this.mUpdateTime);
        parcel.writeLong(this.mBufferedPosition);
        parcel.writeLong(this.mActions);
        TextUtils.writeToParcel(this.mErrorMessage, parcel, i);
        parcel.writeTypedList(this.mCustomActions);
        parcel.writeLong(this.mActiveItemId);
        parcel.writeBundle(this.mExtras);
        parcel.writeInt(this.mErrorCode);
    }

    public static PlaybackStateCompat fromPlaybackState(Object obj) {
        Object obj2 = obj;
        PlaybackStateCompat playbackStateCompat = null;
        if (obj2 == null || VERSION.SDK_INT < 21) {
            return null;
        }
        List list;
        List<Object> customActions = PlaybackStateCompatApi21.getCustomActions(obj);
        if (customActions != null) {
            ArrayList arrayList = new ArrayList(customActions.size());
            for (Object fromCustomAction : customActions) {
                arrayList.add(CustomAction.fromCustomAction(fromCustomAction));
            }
            list = arrayList;
        } else {
            list = null;
        }
        if (VERSION.SDK_INT >= 22) {
            playbackStateCompat = PlaybackStateCompatApi22.getExtras(obj);
        }
        PlaybackStateCompat playbackStateCompat2 = new PlaybackStateCompat(PlaybackStateCompatApi21.getState(obj), PlaybackStateCompatApi21.getPosition(obj), PlaybackStateCompatApi21.getBufferedPosition(obj), PlaybackStateCompatApi21.getPlaybackSpeed(obj), PlaybackStateCompatApi21.getActions(obj), 0, PlaybackStateCompatApi21.getErrorMessage(obj), PlaybackStateCompatApi21.getLastPositionUpdateTime(obj), list, PlaybackStateCompatApi21.getActiveQueueItemId(obj), playbackStateCompat);
        playbackStateCompat2.mStateObj = obj2;
        return playbackStateCompat2;
    }
}
