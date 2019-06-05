package android.support.p000v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.p000v4.app.BackStackRecord.C0124Op;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

/* compiled from: BackStackRecord */
/* renamed from: android.support.v4.app.BackStackState */
final class BackStackState implements Parcelable {
    public static final Creator<BackStackState> CREATOR = new C01251();
    final int mBreadCrumbShortTitleRes;
    final CharSequence mBreadCrumbShortTitleText;
    final int mBreadCrumbTitleRes;
    final CharSequence mBreadCrumbTitleText;
    final int mIndex;
    final String mName;
    final int[] mOps;
    final boolean mReorderingAllowed;
    final ArrayList<String> mSharedElementSourceNames;
    final ArrayList<String> mSharedElementTargetNames;
    final int mTransition;
    final int mTransitionStyle;

    /* compiled from: BackStackRecord */
    /* renamed from: android.support.v4.app.BackStackState$1 */
    static class C01251 implements Creator<BackStackState> {
        C01251() {
        }

        public BackStackState createFromParcel(Parcel parcel) {
            return new BackStackState(parcel);
        }

        public BackStackState[] newArray(int i) {
            return new BackStackState[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public BackStackState(BackStackRecord backStackRecord) {
        int size = backStackRecord.mOps.size();
        this.mOps = new int[(size * 6)];
        if (backStackRecord.mAddToBackStack) {
            int i = 0;
            int i2 = 0;
            while (i < size) {
                C0124Op c0124Op = (C0124Op) backStackRecord.mOps.get(i);
                int i3 = i2 + 1;
                this.mOps[i2] = c0124Op.cmd;
                int i4 = i3 + 1;
                this.mOps[i3] = c0124Op.fragment != null ? c0124Op.fragment.mIndex : -1;
                i3 = i4 + 1;
                this.mOps[i4] = c0124Op.enterAnim;
                i4 = i3 + 1;
                this.mOps[i3] = c0124Op.exitAnim;
                i3 = i4 + 1;
                this.mOps[i4] = c0124Op.popEnterAnim;
                i4 = i3 + 1;
                this.mOps[i3] = c0124Op.popExitAnim;
                i++;
                i2 = i4;
            }
            this.mTransition = backStackRecord.mTransition;
            this.mTransitionStyle = backStackRecord.mTransitionStyle;
            this.mName = backStackRecord.mName;
            this.mIndex = backStackRecord.mIndex;
            this.mBreadCrumbTitleRes = backStackRecord.mBreadCrumbTitleRes;
            this.mBreadCrumbTitleText = backStackRecord.mBreadCrumbTitleText;
            this.mBreadCrumbShortTitleRes = backStackRecord.mBreadCrumbShortTitleRes;
            this.mBreadCrumbShortTitleText = backStackRecord.mBreadCrumbShortTitleText;
            this.mSharedElementSourceNames = backStackRecord.mSharedElementSourceNames;
            this.mSharedElementTargetNames = backStackRecord.mSharedElementTargetNames;
            this.mReorderingAllowed = backStackRecord.mReorderingAllowed;
            return;
        }
        throw new IllegalStateException("Not on back stack");
    }

    public BackStackState(Parcel parcel) {
        this.mOps = parcel.createIntArray();
        this.mTransition = parcel.readInt();
        this.mTransitionStyle = parcel.readInt();
        this.mName = parcel.readString();
        this.mIndex = parcel.readInt();
        this.mBreadCrumbTitleRes = parcel.readInt();
        this.mBreadCrumbTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mBreadCrumbShortTitleRes = parcel.readInt();
        this.mBreadCrumbShortTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mSharedElementSourceNames = parcel.createStringArrayList();
        this.mSharedElementTargetNames = parcel.createStringArrayList();
        this.mReorderingAllowed = parcel.readInt() != 0;
    }

    public BackStackRecord instantiate(FragmentManagerImpl fragmentManagerImpl) {
        BackStackRecord backStackRecord = new BackStackRecord(fragmentManagerImpl);
        int i = 0;
        int i2 = 0;
        while (i < this.mOps.length) {
            C0124Op c0124Op = new C0124Op();
            int i3 = i + 1;
            c0124Op.cmd = this.mOps[i];
            if (FragmentManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Instantiate ");
                stringBuilder.append(backStackRecord);
                stringBuilder.append(" op #");
                stringBuilder.append(i2);
                stringBuilder.append(" base fragment #");
                stringBuilder.append(this.mOps[i3]);
                Log.v("FragmentManager", stringBuilder.toString());
            }
            int i4 = i3 + 1;
            i = this.mOps[i3];
            if (i >= 0) {
                c0124Op.fragment = (Fragment) fragmentManagerImpl.mActive.get(i);
            } else {
                c0124Op.fragment = null;
            }
            i3 = i4 + 1;
            c0124Op.enterAnim = this.mOps[i4];
            i4 = i3 + 1;
            c0124Op.exitAnim = this.mOps[i3];
            i3 = i4 + 1;
            c0124Op.popEnterAnim = this.mOps[i4];
            i4 = i3 + 1;
            c0124Op.popExitAnim = this.mOps[i3];
            backStackRecord.mEnterAnim = c0124Op.enterAnim;
            backStackRecord.mExitAnim = c0124Op.exitAnim;
            backStackRecord.mPopEnterAnim = c0124Op.popEnterAnim;
            backStackRecord.mPopExitAnim = c0124Op.popExitAnim;
            backStackRecord.addOp(c0124Op);
            i2++;
            i = i4;
        }
        backStackRecord.mTransition = this.mTransition;
        backStackRecord.mTransitionStyle = this.mTransitionStyle;
        backStackRecord.mName = this.mName;
        backStackRecord.mIndex = this.mIndex;
        backStackRecord.mAddToBackStack = true;
        backStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
        backStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
        backStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
        backStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
        backStackRecord.mSharedElementSourceNames = this.mSharedElementSourceNames;
        backStackRecord.mSharedElementTargetNames = this.mSharedElementTargetNames;
        backStackRecord.mReorderingAllowed = this.mReorderingAllowed;
        backStackRecord.bumpBackStackNesting(1);
        return backStackRecord;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeIntArray(this.mOps);
        parcel.writeInt(this.mTransition);
        parcel.writeInt(this.mTransitionStyle);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mIndex);
        parcel.writeInt(this.mBreadCrumbTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbTitleText, parcel, 0);
        parcel.writeInt(this.mBreadCrumbShortTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, parcel, 0);
        parcel.writeStringList(this.mSharedElementSourceNames);
        parcel.writeStringList(this.mSharedElementTargetNames);
        parcel.writeInt(this.mReorderingAllowed);
    }
}
