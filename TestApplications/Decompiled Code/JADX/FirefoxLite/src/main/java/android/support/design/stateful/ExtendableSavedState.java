package android.support.design.stateful;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.p001v4.util.SimpleArrayMap;
import android.support.p001v4.view.AbsSavedState;

public class ExtendableSavedState extends AbsSavedState {
    public static final Creator<ExtendableSavedState> CREATOR = new C00401();
    public final SimpleArrayMap<String, Bundle> extendableStates;

    /* renamed from: android.support.design.stateful.ExtendableSavedState$1 */
    static class C00401 implements ClassLoaderCreator<ExtendableSavedState> {
        C00401() {
        }

        public ExtendableSavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new ExtendableSavedState(parcel, classLoader, null);
        }

        public ExtendableSavedState createFromParcel(Parcel parcel) {
            return new ExtendableSavedState(parcel, null, null);
        }

        public ExtendableSavedState[] newArray(int i) {
            return new ExtendableSavedState[i];
        }
    }

    /* synthetic */ ExtendableSavedState(Parcel parcel, ClassLoader classLoader, C00401 c00401) {
        this(parcel, classLoader);
    }

    public ExtendableSavedState(Parcelable parcelable) {
        super(parcelable);
        this.extendableStates = new SimpleArrayMap();
    }

    private ExtendableSavedState(Parcel parcel, ClassLoader classLoader) {
        super(parcel, classLoader);
        int readInt = parcel.readInt();
        String[] strArr = new String[readInt];
        parcel.readStringArray(strArr);
        Bundle[] bundleArr = new Bundle[readInt];
        parcel.readTypedArray(bundleArr, Bundle.CREATOR);
        this.extendableStates = new SimpleArrayMap(readInt);
        for (int i = 0; i < readInt; i++) {
            this.extendableStates.put(strArr[i], bundleArr[i]);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        i = this.extendableStates.size();
        parcel.writeInt(i);
        String[] strArr = new String[i];
        Bundle[] bundleArr = new Bundle[i];
        for (int i2 = 0; i2 < i; i2++) {
            strArr[i2] = (String) this.extendableStates.keyAt(i2);
            bundleArr[i2] = (Bundle) this.extendableStates.valueAt(i2);
        }
        parcel.writeStringArray(strArr);
        parcel.writeTypedArray(bundleArr, 0);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ExtendableSavedState{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" states=");
        stringBuilder.append(this.extendableStates);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
