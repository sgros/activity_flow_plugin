package android.support.p001v4.app;

import android.support.p001v4.app.Fragment.OnStartEnterTransitionListener;
import android.support.p001v4.app.FragmentManager.BackStackEntry;
import android.support.p001v4.app.FragmentManagerImpl.OpGenerator;
import android.support.p001v4.util.LogWriter;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/* renamed from: android.support.v4.app.BackStackRecord */
final class BackStackRecord extends FragmentTransaction implements BackStackEntry, OpGenerator {
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack = true;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    ArrayList<Runnable> mCommitRunnables;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    int mIndex = -1;
    final FragmentManagerImpl mManager;
    String mName;
    ArrayList<C0106Op> mOps = new ArrayList();
    int mPopEnterAnim;
    int mPopExitAnim;
    boolean mReorderingAllowed = false;
    ArrayList<String> mSharedElementSourceNames;
    ArrayList<String> mSharedElementTargetNames;
    int mTransition;
    int mTransitionStyle;

    /* renamed from: android.support.v4.app.BackStackRecord$Op */
    static final class C0106Op {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        int popEnterAnim;
        int popExitAnim;

        C0106Op() {
        }

        C0106Op(int i, Fragment fragment) {
            this.cmd = i;
            this.fragment = fragment;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("BackStackEntry{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            stringBuilder.append(" #");
            stringBuilder.append(this.mIndex);
        }
        if (this.mName != null) {
            stringBuilder.append(" ");
            stringBuilder.append(this.mName);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        dump(str, printWriter, true);
    }

    public void dump(String str, PrintWriter printWriter, boolean z) {
        if (z) {
            printWriter.print(str);
            printWriter.print("mName=");
            printWriter.print(this.mName);
            printWriter.print(" mIndex=");
            printWriter.print(this.mIndex);
            printWriter.print(" mCommitted=");
            printWriter.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter.print(str);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.mTransition));
                printWriter.print(" mTransitionStyle=#");
                printWriter.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (!(this.mEnterAnim == 0 && this.mExitAnim == 0)) {
                printWriter.print(str);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mEnterAnim));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.mExitAnim));
            }
            if (!(this.mPopEnterAnim == 0 && this.mPopExitAnim == 0)) {
                printWriter.print(str);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (!(this.mBreadCrumbTitleRes == 0 && this.mBreadCrumbTitleText == null)) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.mBreadCrumbTitleText);
            }
            if (!(this.mBreadCrumbShortTitleRes == 0 && this.mBreadCrumbShortTitleText == null)) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (!this.mOps.isEmpty()) {
            printWriter.print(str);
            printWriter.println("Operations:");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append("    ");
            stringBuilder.toString();
            int size = this.mOps.size();
            for (int i = 0; i < size; i++) {
                String str2;
                C0106Op c0106Op = (C0106Op) this.mOps.get(i);
                switch (c0106Op.cmd) {
                    case 0:
                        str2 = "NULL";
                        break;
                    case 1:
                        str2 = "ADD";
                        break;
                    case 2:
                        str2 = "REPLACE";
                        break;
                    case 3:
                        str2 = "REMOVE";
                        break;
                    case 4:
                        str2 = "HIDE";
                        break;
                    case 5:
                        str2 = "SHOW";
                        break;
                    case 6:
                        str2 = "DETACH";
                        break;
                    case 7:
                        str2 = "ATTACH";
                        break;
                    case 8:
                        str2 = "SET_PRIMARY_NAV";
                        break;
                    case 9:
                        str2 = "UNSET_PRIMARY_NAV";
                        break;
                    default:
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("cmd=");
                        stringBuilder2.append(c0106Op.cmd);
                        str2 = stringBuilder2.toString();
                        break;
                }
                printWriter.print(str);
                printWriter.print("  Op #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.print(str2);
                printWriter.print(" ");
                printWriter.println(c0106Op.fragment);
                if (z) {
                    if (!(c0106Op.enterAnim == 0 && c0106Op.exitAnim == 0)) {
                        printWriter.print(str);
                        printWriter.print("enterAnim=#");
                        printWriter.print(Integer.toHexString(c0106Op.enterAnim));
                        printWriter.print(" exitAnim=#");
                        printWriter.println(Integer.toHexString(c0106Op.exitAnim));
                    }
                    if (c0106Op.popEnterAnim != 0 || c0106Op.popExitAnim != 0) {
                        printWriter.print(str);
                        printWriter.print("popEnterAnim=#");
                        printWriter.print(Integer.toHexString(c0106Op.popEnterAnim));
                        printWriter.print(" popExitAnim=#");
                        printWriter.println(Integer.toHexString(c0106Op.popExitAnim));
                    }
                }
            }
        }
    }

    public BackStackRecord(FragmentManagerImpl fragmentManagerImpl) {
        this.mManager = fragmentManagerImpl;
    }

    /* Access modifiers changed, original: 0000 */
    public void addOp(C0106Op c0106Op) {
        this.mOps.add(c0106Op);
        c0106Op.enterAnim = this.mEnterAnim;
        c0106Op.exitAnim = this.mExitAnim;
        c0106Op.popEnterAnim = this.mPopEnterAnim;
        c0106Op.popExitAnim = this.mPopExitAnim;
    }

    public FragmentTransaction add(Fragment fragment, String str) {
        doAddOp(0, fragment, str, 1);
        return this;
    }

    public FragmentTransaction add(int i, Fragment fragment, String str) {
        doAddOp(i, fragment, str, 1);
        return this;
    }

    private void doAddOp(int i, Fragment fragment, String str, int i2) {
        Class cls = fragment.getClass();
        int modifiers = cls.getModifiers();
        if (cls.isAnonymousClass() || !Modifier.isPublic(modifiers) || (cls.isMemberClass() && !Modifier.isStatic(modifiers))) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(cls.getCanonicalName());
            stringBuilder.append(" must be a public static class to be  properly recreated from");
            stringBuilder.append(" instance state.");
            throw new IllegalStateException(stringBuilder.toString());
        }
        StringBuilder stringBuilder2;
        fragment.mFragmentManager = this.mManager;
        if (str != null) {
            if (fragment.mTag == null || str.equals(fragment.mTag)) {
                fragment.mTag = str;
            } else {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Can't change tag of fragment ");
                stringBuilder2.append(fragment);
                stringBuilder2.append(": was ");
                stringBuilder2.append(fragment.mTag);
                stringBuilder2.append(" now ");
                stringBuilder2.append(str);
                throw new IllegalStateException(stringBuilder2.toString());
            }
        }
        if (i != 0) {
            if (i == -1) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Can't add fragment ");
                stringBuilder2.append(fragment);
                stringBuilder2.append(" with tag ");
                stringBuilder2.append(str);
                stringBuilder2.append(" to container view with no id");
                throw new IllegalArgumentException(stringBuilder2.toString());
            } else if (fragment.mFragmentId == 0 || fragment.mFragmentId == i) {
                fragment.mFragmentId = i;
                fragment.mContainerId = i;
            } else {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Can't change container ID of fragment ");
                stringBuilder2.append(fragment);
                stringBuilder2.append(": was ");
                stringBuilder2.append(fragment.mFragmentId);
                stringBuilder2.append(" now ");
                stringBuilder2.append(i);
                throw new IllegalStateException(stringBuilder2.toString());
            }
        }
        addOp(new C0106Op(i2, fragment));
    }

    public FragmentTransaction replace(int i, Fragment fragment) {
        return replace(i, fragment, null);
    }

    public FragmentTransaction replace(int i, Fragment fragment, String str) {
        if (i != 0) {
            doAddOp(i, fragment, str, 2);
            return this;
        }
        throw new IllegalArgumentException("Must use non-zero containerViewId");
    }

    public FragmentTransaction remove(Fragment fragment) {
        addOp(new C0106Op(3, fragment));
        return this;
    }

    public FragmentTransaction setCustomAnimations(int i, int i2, int i3, int i4) {
        this.mEnterAnim = i;
        this.mExitAnim = i2;
        this.mPopEnterAnim = i3;
        this.mPopExitAnim = i4;
        return this;
    }

    public FragmentTransaction addToBackStack(String str) {
        if (this.mAllowAddToBackStack) {
            this.mAddToBackStack = true;
            this.mName = str;
            return this;
        }
        throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
    }

    /* Access modifiers changed, original: 0000 */
    public void bumpBackStackNesting(int i) {
        if (this.mAddToBackStack) {
            if (FragmentManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Bump nesting in ");
                stringBuilder.append(this);
                stringBuilder.append(" by ");
                stringBuilder.append(i);
                Log.v("FragmentManager", stringBuilder.toString());
            }
            int size = this.mOps.size();
            for (int i2 = 0; i2 < size; i2++) {
                C0106Op c0106Op = (C0106Op) this.mOps.get(i2);
                if (c0106Op.fragment != null) {
                    Fragment fragment = c0106Op.fragment;
                    fragment.mBackStackNesting += i;
                    if (FragmentManagerImpl.DEBUG) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Bump nesting of ");
                        stringBuilder2.append(c0106Op.fragment);
                        stringBuilder2.append(" to ");
                        stringBuilder2.append(c0106Op.fragment.mBackStackNesting);
                        Log.v("FragmentManager", stringBuilder2.toString());
                    }
                }
            }
        }
    }

    public void runOnCommitRunnables() {
        if (this.mCommitRunnables != null) {
            int size = this.mCommitRunnables.size();
            for (int i = 0; i < size; i++) {
                ((Runnable) this.mCommitRunnables.get(i)).run();
            }
            this.mCommitRunnables = null;
        }
    }

    public int commit() {
        return commitInternal(false);
    }

    public int commitAllowingStateLoss() {
        return commitInternal(true);
    }

    /* Access modifiers changed, original: 0000 */
    public int commitInternal(boolean z) {
        if (this.mCommitted) {
            throw new IllegalStateException("commit already called");
        }
        if (FragmentManagerImpl.DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Commit: ");
            stringBuilder.append(this);
            Log.v("FragmentManager", stringBuilder.toString());
            PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
            dump("  ", null, printWriter, null);
            printWriter.close();
        }
        this.mCommitted = true;
        if (this.mAddToBackStack) {
            this.mIndex = this.mManager.allocBackStackIndex(this);
        } else {
            this.mIndex = -1;
        }
        this.mManager.enqueueAction(this, z);
        return this.mIndex;
    }

    public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (FragmentManagerImpl.DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Run: ");
            stringBuilder.append(this);
            Log.v("FragmentManager", stringBuilder.toString());
        }
        arrayList.add(this);
        arrayList2.add(Boolean.valueOf(false));
        if (this.mAddToBackStack) {
            this.mManager.addBackStackState(this);
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean interactsWith(int i) {
        int size = this.mOps.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0106Op c0106Op = (C0106Op) this.mOps.get(i2);
            int i3 = c0106Op.fragment != null ? c0106Op.fragment.mContainerId : 0;
            if (i3 != 0 && i3 == i) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean interactsWith(ArrayList<BackStackRecord> arrayList, int i, int i2) {
        if (i2 == i) {
            return false;
        }
        int size = this.mOps.size();
        int i3 = -1;
        for (int i4 = 0; i4 < size; i4++) {
            C0106Op c0106Op = (C0106Op) this.mOps.get(i4);
            int i5 = c0106Op.fragment != null ? c0106Op.fragment.mContainerId : 0;
            if (!(i5 == 0 || i5 == i3)) {
                for (i3 = i; i3 < i2; i3++) {
                    BackStackRecord backStackRecord = (BackStackRecord) arrayList.get(i3);
                    int size2 = backStackRecord.mOps.size();
                    for (int i6 = 0; i6 < size2; i6++) {
                        C0106Op c0106Op2 = (C0106Op) backStackRecord.mOps.get(i6);
                        if ((c0106Op2.fragment != null ? c0106Op2.fragment.mContainerId : 0) == i5) {
                            return true;
                        }
                    }
                }
                i3 = i5;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void executeOps() {
        int size = this.mOps.size();
        for (int i = 0; i < size; i++) {
            C0106Op c0106Op = (C0106Op) this.mOps.get(i);
            Fragment fragment = c0106Op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(this.mTransition, this.mTransitionStyle);
            }
            int i2 = c0106Op.cmd;
            if (i2 != 1) {
                switch (i2) {
                    case 3:
                        fragment.setNextAnim(c0106Op.exitAnim);
                        this.mManager.removeFragment(fragment);
                        break;
                    case 4:
                        fragment.setNextAnim(c0106Op.exitAnim);
                        this.mManager.hideFragment(fragment);
                        break;
                    case 5:
                        fragment.setNextAnim(c0106Op.enterAnim);
                        this.mManager.showFragment(fragment);
                        break;
                    case 6:
                        fragment.setNextAnim(c0106Op.exitAnim);
                        this.mManager.detachFragment(fragment);
                        break;
                    case 7:
                        fragment.setNextAnim(c0106Op.enterAnim);
                        this.mManager.attachFragment(fragment);
                        break;
                    case 8:
                        this.mManager.setPrimaryNavigationFragment(fragment);
                        break;
                    case 9:
                        this.mManager.setPrimaryNavigationFragment(null);
                        break;
                    default:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown cmd: ");
                        stringBuilder.append(c0106Op.cmd);
                        throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            fragment.setNextAnim(c0106Op.enterAnim);
            this.mManager.addFragment(fragment, false);
            if (!(this.mReorderingAllowed || c0106Op.cmd == 1 || fragment == null)) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mReorderingAllowed) {
            this.mManager.moveToState(this.mManager.mCurState, true);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void executePopOps(boolean z) {
        for (int size = this.mOps.size() - 1; size >= 0; size--) {
            C0106Op c0106Op = (C0106Op) this.mOps.get(size);
            Fragment fragment = c0106Op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
            }
            int i = c0106Op.cmd;
            if (i != 1) {
                switch (i) {
                    case 3:
                        fragment.setNextAnim(c0106Op.popEnterAnim);
                        this.mManager.addFragment(fragment, false);
                        break;
                    case 4:
                        fragment.setNextAnim(c0106Op.popEnterAnim);
                        this.mManager.showFragment(fragment);
                        break;
                    case 5:
                        fragment.setNextAnim(c0106Op.popExitAnim);
                        this.mManager.hideFragment(fragment);
                        break;
                    case 6:
                        fragment.setNextAnim(c0106Op.popEnterAnim);
                        this.mManager.attachFragment(fragment);
                        break;
                    case 7:
                        fragment.setNextAnim(c0106Op.popExitAnim);
                        this.mManager.detachFragment(fragment);
                        break;
                    case 8:
                        this.mManager.setPrimaryNavigationFragment(null);
                        break;
                    case 9:
                        this.mManager.setPrimaryNavigationFragment(fragment);
                        break;
                    default:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown cmd: ");
                        stringBuilder.append(c0106Op.cmd);
                        throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            fragment.setNextAnim(c0106Op.popExitAnim);
            this.mManager.removeFragment(fragment);
            if (!(this.mReorderingAllowed || c0106Op.cmd == 3 || fragment == null)) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mReorderingAllowed && z) {
            this.mManager.moveToState(this.mManager.mCurState, true);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Fragment expandOps(ArrayList<Fragment> arrayList, Fragment fragment) {
        Fragment fragment2 = fragment;
        int i = 0;
        while (i < this.mOps.size()) {
            C0106Op c0106Op = (C0106Op) this.mOps.get(i);
            switch (c0106Op.cmd) {
                case 1:
                case 7:
                    arrayList.add(c0106Op.fragment);
                    break;
                case 2:
                    Fragment fragment3 = c0106Op.fragment;
                    int i2 = fragment3.mContainerId;
                    Fragment fragment4 = fragment2;
                    int i3 = i;
                    Object obj = null;
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        Fragment fragment5 = (Fragment) arrayList.get(size);
                        if (fragment5.mContainerId == i2) {
                            if (fragment5 == fragment3) {
                                obj = 1;
                            } else {
                                if (fragment5 == fragment4) {
                                    this.mOps.add(i3, new C0106Op(9, fragment5));
                                    i3++;
                                    fragment4 = null;
                                }
                                C0106Op c0106Op2 = new C0106Op(3, fragment5);
                                c0106Op2.enterAnim = c0106Op.enterAnim;
                                c0106Op2.popEnterAnim = c0106Op.popEnterAnim;
                                c0106Op2.exitAnim = c0106Op.exitAnim;
                                c0106Op2.popExitAnim = c0106Op.popExitAnim;
                                this.mOps.add(i3, c0106Op2);
                                arrayList.remove(fragment5);
                                i3++;
                            }
                        }
                    }
                    if (obj != null) {
                        this.mOps.remove(i3);
                        i3--;
                    } else {
                        c0106Op.cmd = 1;
                        arrayList.add(fragment3);
                    }
                    i = i3;
                    fragment2 = fragment4;
                    break;
                case 3:
                case 6:
                    arrayList.remove(c0106Op.fragment);
                    if (c0106Op.fragment != fragment2) {
                        break;
                    }
                    this.mOps.add(i, new C0106Op(9, c0106Op.fragment));
                    i++;
                    fragment2 = null;
                    break;
                case 8:
                    this.mOps.add(i, new C0106Op(9, fragment2));
                    i++;
                    fragment2 = c0106Op.fragment;
                    break;
                default:
                    break;
            }
            i++;
        }
        return fragment2;
    }

    /* Access modifiers changed, original: 0000 */
    public Fragment trackAddedFragmentsInPop(ArrayList<Fragment> arrayList, Fragment fragment) {
        for (int i = 0; i < this.mOps.size(); i++) {
            C0106Op c0106Op = (C0106Op) this.mOps.get(i);
            int i2 = c0106Op.cmd;
            if (i2 != 1) {
                if (i2 != 3) {
                    switch (i2) {
                        case 6:
                            break;
                        case 7:
                            break;
                        case 8:
                            fragment = null;
                            break;
                        case 9:
                            fragment = c0106Op.fragment;
                            break;
                        default:
                            break;
                    }
                }
                arrayList.add(c0106Op.fragment);
            }
            arrayList.remove(c0106Op.fragment);
        }
        return fragment;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPostponed() {
        for (int i = 0; i < this.mOps.size(); i++) {
            if (BackStackRecord.isFragmentPostponed((C0106Op) this.mOps.get(i))) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void setOnStartPostponedListener(OnStartEnterTransitionListener onStartEnterTransitionListener) {
        for (int i = 0; i < this.mOps.size(); i++) {
            C0106Op c0106Op = (C0106Op) this.mOps.get(i);
            if (BackStackRecord.isFragmentPostponed(c0106Op)) {
                c0106Op.fragment.setOnStartEnterTransitionListener(onStartEnterTransitionListener);
            }
        }
    }

    private static boolean isFragmentPostponed(C0106Op c0106Op) {
        Fragment fragment = c0106Op.fragment;
        return (fragment == null || !fragment.mAdded || fragment.mView == null || fragment.mDetached || fragment.mHidden || !fragment.isPostponed()) ? false : true;
    }

    public String getName() {
        return this.mName;
    }
}
