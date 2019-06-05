// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.stateful;

import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;

public class ExtendableSavedState extends AbsSavedState
{
    public static final Parcelable$Creator<ExtendableSavedState> CREATOR;
    public final SimpleArrayMap<String, Bundle> extendableStates;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<ExtendableSavedState>() {
            public ExtendableSavedState createFromParcel(final Parcel parcel) {
                return new ExtendableSavedState(parcel, null, null);
            }
            
            public ExtendableSavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                return new ExtendableSavedState(parcel, classLoader, null);
            }
            
            public ExtendableSavedState[] newArray(final int n) {
                return new ExtendableSavedState[n];
            }
        };
    }
    
    private ExtendableSavedState(final Parcel parcel, final ClassLoader classLoader) {
        super(parcel, classLoader);
        final int int1 = parcel.readInt();
        final String[] array = new String[int1];
        parcel.readStringArray(array);
        final Bundle[] array2 = new Bundle[int1];
        parcel.readTypedArray((Object[])array2, Bundle.CREATOR);
        this.extendableStates = new SimpleArrayMap<String, Bundle>(int1);
        for (int i = 0; i < int1; ++i) {
            this.extendableStates.put(array[i], array2[i]);
        }
    }
    
    public ExtendableSavedState(final Parcelable parcelable) {
        super(parcelable);
        this.extendableStates = new SimpleArrayMap<String, Bundle>();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ExtendableSavedState{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" states=");
        sb.append(this.extendableStates);
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public void writeToParcel(final Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        final int size = this.extendableStates.size();
        parcel.writeInt(size);
        final String[] array = new String[size];
        final Bundle[] array2 = new Bundle[size];
        for (i = 0; i < size; ++i) {
            array[i] = this.extendableStates.keyAt(i);
            array2[i] = this.extendableStates.valueAt(i);
        }
        parcel.writeStringArray(array);
        parcel.writeTypedArray((Parcelable[])array2, 0);
    }
}
