package p008pl.droidsonroids.gif;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.view.View.BaseSavedState;

/* renamed from: pl.droidsonroids.gif.GifViewSavedState */
class GifViewSavedState extends BaseSavedState {
    public static final Creator<GifViewSavedState> CREATOR = new C03841();
    final long[][] mStates;

    /* renamed from: pl.droidsonroids.gif.GifViewSavedState$1 */
    static class C03841 implements Creator<GifViewSavedState> {
        C03841() {
        }

        public GifViewSavedState createFromParcel(Parcel in) {
            return new GifViewSavedState(in, null);
        }

        public GifViewSavedState[] newArray(int size) {
            return new GifViewSavedState[size];
        }
    }

    GifViewSavedState(Parcelable superState, Drawable... drawables) {
        super(superState);
        this.mStates = new long[drawables.length][];
        for (int i = 0; i < drawables.length; i++) {
            Drawable drawable = drawables[i];
            if (drawable instanceof GifDrawable) {
                this.mStates[i] = ((GifDrawable) drawable).mNativeInfoHandle.getSavedState();
            } else {
                this.mStates[i] = null;
            }
        }
    }

    private GifViewSavedState(Parcel in) {
        super(in);
        this.mStates = new long[in.readInt()][];
        for (int i = 0; i < this.mStates.length; i++) {
            this.mStates[i] = in.createLongArray();
        }
    }

    GifViewSavedState(Parcelable superState, long[] savedState) {
        super(superState);
        this.mStates = new long[1][];
        this.mStates[0] = savedState;
    }

    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mStates.length);
        for (long[] mState : this.mStates) {
            dest.writeLongArray(mState);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void restoreState(Drawable drawable, int i) {
        if (this.mStates[i] != null && (drawable instanceof GifDrawable)) {
            GifDrawable gifDrawable = (GifDrawable) drawable;
            gifDrawable.startAnimation((long) gifDrawable.mNativeInfoHandle.restoreSavedState(this.mStates[i], gifDrawable.mBuffer));
        }
    }
}
