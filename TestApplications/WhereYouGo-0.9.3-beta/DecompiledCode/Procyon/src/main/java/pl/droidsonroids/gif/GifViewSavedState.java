// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;

class GifViewSavedState extends View$BaseSavedState
{
    public static final Parcelable$Creator<GifViewSavedState> CREATOR;
    final long[][] mStates;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<GifViewSavedState>() {
            public GifViewSavedState createFromParcel(final Parcel parcel) {
                return new GifViewSavedState(parcel, null);
            }
            
            public GifViewSavedState[] newArray(final int n) {
                return new GifViewSavedState[n];
            }
        };
    }
    
    private GifViewSavedState(final Parcel parcel) {
        super(parcel);
        this.mStates = new long[parcel.readInt()][];
        for (int i = 0; i < this.mStates.length; ++i) {
            this.mStates[i] = parcel.createLongArray();
        }
    }
    
    GifViewSavedState(final Parcelable parcelable, final long[] array) {
        super(parcelable);
        (this.mStates = new long[1][])[0] = array;
    }
    
    GifViewSavedState(final Parcelable parcelable, final Drawable... array) {
        super(parcelable);
        this.mStates = new long[array.length][];
        for (int i = 0; i < array.length; ++i) {
            final Drawable drawable = array[i];
            if (drawable instanceof GifDrawable) {
                this.mStates[i] = ((GifDrawable)drawable).mNativeInfoHandle.getSavedState();
            }
            else {
                this.mStates[i] = null;
            }
        }
    }
    
    void restoreState(final Drawable drawable, final int n) {
        if (this.mStates[n] != null && drawable instanceof GifDrawable) {
            final GifDrawable gifDrawable = (GifDrawable)drawable;
            gifDrawable.startAnimation(gifDrawable.mNativeInfoHandle.restoreSavedState(this.mStates[n], gifDrawable.mBuffer));
        }
    }
    
    public void writeToParcel(@NonNull final Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mStates.length);
        final long[][] mStates = this.mStates;
        int length;
        for (length = mStates.length, i = 0; i < length; ++i) {
            parcel.writeLongArray(mStates[i]);
        }
    }
}
