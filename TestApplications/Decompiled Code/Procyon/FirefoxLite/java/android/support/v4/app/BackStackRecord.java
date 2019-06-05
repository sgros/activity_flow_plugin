// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import java.io.FileDescriptor;
import java.io.Writer;
import java.io.PrintWriter;
import android.support.v4.util.LogWriter;
import android.util.Log;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction implements BackStackEntry, OpGenerator
{
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    ArrayList<Runnable> mCommitRunnables;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    int mIndex;
    final FragmentManagerImpl mManager;
    String mName;
    ArrayList<Op> mOps;
    int mPopEnterAnim;
    int mPopExitAnim;
    boolean mReorderingAllowed;
    ArrayList<String> mSharedElementSourceNames;
    ArrayList<String> mSharedElementTargetNames;
    int mTransition;
    int mTransitionStyle;
    
    public BackStackRecord(final FragmentManagerImpl mManager) {
        this.mOps = new ArrayList<Op>();
        this.mAllowAddToBackStack = true;
        this.mIndex = -1;
        this.mReorderingAllowed = false;
        this.mManager = mManager;
    }
    
    private void doAddOp(final int mContainerId, final Fragment obj, final String str, final int n) {
        final Class<? extends Fragment> class1 = obj.getClass();
        final int modifiers = class1.getModifiers();
        if (!class1.isAnonymousClass() && Modifier.isPublic(modifiers) && (!class1.isMemberClass() || Modifier.isStatic(modifiers))) {
            obj.mFragmentManager = this.mManager;
            if (str != null) {
                if (obj.mTag != null && !str.equals(obj.mTag)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Can't change tag of fragment ");
                    sb.append(obj);
                    sb.append(": was ");
                    sb.append(obj.mTag);
                    sb.append(" now ");
                    sb.append(str);
                    throw new IllegalStateException(sb.toString());
                }
                obj.mTag = str;
            }
            if (mContainerId != 0) {
                if (mContainerId == -1) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Can't add fragment ");
                    sb2.append(obj);
                    sb2.append(" with tag ");
                    sb2.append(str);
                    sb2.append(" to container view with no id");
                    throw new IllegalArgumentException(sb2.toString());
                }
                if (obj.mFragmentId != 0 && obj.mFragmentId != mContainerId) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Can't change container ID of fragment ");
                    sb3.append(obj);
                    sb3.append(": was ");
                    sb3.append(obj.mFragmentId);
                    sb3.append(" now ");
                    sb3.append(mContainerId);
                    throw new IllegalStateException(sb3.toString());
                }
                obj.mFragmentId = mContainerId;
                obj.mContainerId = mContainerId;
            }
            this.addOp(new Op(n, obj));
            return;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Fragment ");
        sb4.append(class1.getCanonicalName());
        sb4.append(" must be a public static class to be  properly recreated from");
        sb4.append(" instance state.");
        throw new IllegalStateException(sb4.toString());
    }
    
    private static boolean isFragmentPostponed(final Op op) {
        final Fragment fragment = op.fragment;
        return fragment != null && fragment.mAdded && fragment.mView != null && !fragment.mDetached && !fragment.mHidden && fragment.isPostponed();
    }
    
    @Override
    public FragmentTransaction add(final int n, final Fragment fragment, final String s) {
        this.doAddOp(n, fragment, s, 1);
        return this;
    }
    
    @Override
    public FragmentTransaction add(final Fragment fragment, final String s) {
        this.doAddOp(0, fragment, s, 1);
        return this;
    }
    
    void addOp(final Op e) {
        this.mOps.add(e);
        e.enterAnim = this.mEnterAnim;
        e.exitAnim = this.mExitAnim;
        e.popEnterAnim = this.mPopEnterAnim;
        e.popExitAnim = this.mPopExitAnim;
    }
    
    @Override
    public FragmentTransaction addToBackStack(final String mName) {
        if (this.mAllowAddToBackStack) {
            this.mAddToBackStack = true;
            this.mName = mName;
            return this;
        }
        throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
    }
    
    void bumpBackStackNesting(final int i) {
        if (!this.mAddToBackStack) {
            return;
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bump nesting in ");
            sb.append(this);
            sb.append(" by ");
            sb.append(i);
            Log.v("FragmentManager", sb.toString());
        }
        for (int size = this.mOps.size(), j = 0; j < size; ++j) {
            final Op op = this.mOps.get(j);
            if (op.fragment != null) {
                final Fragment fragment = op.fragment;
                fragment.mBackStackNesting += i;
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Bump nesting of ");
                    sb2.append(op.fragment);
                    sb2.append(" to ");
                    sb2.append(op.fragment.mBackStackNesting);
                    Log.v("FragmentManager", sb2.toString());
                }
            }
        }
    }
    
    @Override
    public int commit() {
        return this.commitInternal(false);
    }
    
    @Override
    public int commitAllowingStateLoss() {
        return this.commitInternal(true);
    }
    
    int commitInternal(final boolean b) {
        if (!this.mCommitted) {
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Commit: ");
                sb.append(this);
                Log.v("FragmentManager", sb.toString());
                final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
                this.dump("  ", null, printWriter, null);
                printWriter.close();
            }
            this.mCommitted = true;
            if (this.mAddToBackStack) {
                this.mIndex = this.mManager.allocBackStackIndex(this);
            }
            else {
                this.mIndex = -1;
            }
            this.mManager.enqueueAction((FragmentManagerImpl.OpGenerator)this, b);
            return this.mIndex;
        }
        throw new IllegalStateException("commit already called");
    }
    
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        this.dump(s, printWriter, true);
    }
    
    public void dump(final String s, final PrintWriter printWriter, final boolean b) {
        if (b) {
            printWriter.print(s);
            printWriter.print("mName=");
            printWriter.print(this.mName);
            printWriter.print(" mIndex=");
            printWriter.print(this.mIndex);
            printWriter.print(" mCommitted=");
            printWriter.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter.print(s);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.mTransition));
                printWriter.print(" mTransitionStyle=#");
                printWriter.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
                printWriter.print(s);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mEnterAnim));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.mExitAnim));
            }
            if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
                printWriter.print(s);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
                printWriter.print(s);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.mBreadCrumbTitleText);
            }
            if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
                printWriter.print(s);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (!this.mOps.isEmpty()) {
            printWriter.print(s);
            printWriter.println("Operations:");
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("    ");
            sb.toString();
            for (int size = this.mOps.size(), i = 0; i < size; ++i) {
                final Op op = this.mOps.get(i);
                String string = null;
                switch (op.cmd) {
                    default: {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("cmd=");
                        sb2.append(op.cmd);
                        string = sb2.toString();
                        break;
                    }
                    case 9: {
                        string = "UNSET_PRIMARY_NAV";
                        break;
                    }
                    case 8: {
                        string = "SET_PRIMARY_NAV";
                        break;
                    }
                    case 7: {
                        string = "ATTACH";
                        break;
                    }
                    case 6: {
                        string = "DETACH";
                        break;
                    }
                    case 5: {
                        string = "SHOW";
                        break;
                    }
                    case 4: {
                        string = "HIDE";
                        break;
                    }
                    case 3: {
                        string = "REMOVE";
                        break;
                    }
                    case 2: {
                        string = "REPLACE";
                        break;
                    }
                    case 1: {
                        string = "ADD";
                        break;
                    }
                    case 0: {
                        string = "NULL";
                        break;
                    }
                }
                printWriter.print(s);
                printWriter.print("  Op #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.print(string);
                printWriter.print(" ");
                printWriter.println(op.fragment);
                if (b) {
                    if (op.enterAnim != 0 || op.exitAnim != 0) {
                        printWriter.print(s);
                        printWriter.print("enterAnim=#");
                        printWriter.print(Integer.toHexString(op.enterAnim));
                        printWriter.print(" exitAnim=#");
                        printWriter.println(Integer.toHexString(op.exitAnim));
                    }
                    if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
                        printWriter.print(s);
                        printWriter.print("popEnterAnim=#");
                        printWriter.print(Integer.toHexString(op.popEnterAnim));
                        printWriter.print(" popExitAnim=#");
                        printWriter.println(Integer.toHexString(op.popExitAnim));
                    }
                }
            }
        }
    }
    
    void executeOps() {
        for (int size = this.mOps.size(), i = 0; i < size; ++i) {
            final Op op = this.mOps.get(i);
            final Fragment fragment = op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(this.mTransition, this.mTransitionStyle);
            }
            final int cmd = op.cmd;
            if (cmd != 1) {
                switch (cmd) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown cmd: ");
                        sb.append(op.cmd);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    case 9: {
                        this.mManager.setPrimaryNavigationFragment(null);
                        break;
                    }
                    case 8: {
                        this.mManager.setPrimaryNavigationFragment(fragment);
                        break;
                    }
                    case 7: {
                        fragment.setNextAnim(op.enterAnim);
                        this.mManager.attachFragment(fragment);
                        break;
                    }
                    case 6: {
                        fragment.setNextAnim(op.exitAnim);
                        this.mManager.detachFragment(fragment);
                        break;
                    }
                    case 5: {
                        fragment.setNextAnim(op.enterAnim);
                        this.mManager.showFragment(fragment);
                        break;
                    }
                    case 4: {
                        fragment.setNextAnim(op.exitAnim);
                        this.mManager.hideFragment(fragment);
                        break;
                    }
                    case 3: {
                        fragment.setNextAnim(op.exitAnim);
                        this.mManager.removeFragment(fragment);
                        break;
                    }
                }
            }
            else {
                fragment.setNextAnim(op.enterAnim);
                this.mManager.addFragment(fragment, false);
            }
            if (!this.mReorderingAllowed && op.cmd != 1 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mReorderingAllowed) {
            this.mManager.moveToState(this.mManager.mCurState, true);
        }
    }
    
    void executePopOps(final boolean b) {
        for (int i = this.mOps.size() - 1; i >= 0; --i) {
            final Op op = this.mOps.get(i);
            final Fragment fragment = op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
            }
            final int cmd = op.cmd;
            if (cmd != 1) {
                switch (cmd) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown cmd: ");
                        sb.append(op.cmd);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    case 9: {
                        this.mManager.setPrimaryNavigationFragment(fragment);
                        break;
                    }
                    case 8: {
                        this.mManager.setPrimaryNavigationFragment(null);
                        break;
                    }
                    case 7: {
                        fragment.setNextAnim(op.popExitAnim);
                        this.mManager.detachFragment(fragment);
                        break;
                    }
                    case 6: {
                        fragment.setNextAnim(op.popEnterAnim);
                        this.mManager.attachFragment(fragment);
                        break;
                    }
                    case 5: {
                        fragment.setNextAnim(op.popExitAnim);
                        this.mManager.hideFragment(fragment);
                        break;
                    }
                    case 4: {
                        fragment.setNextAnim(op.popEnterAnim);
                        this.mManager.showFragment(fragment);
                        break;
                    }
                    case 3: {
                        fragment.setNextAnim(op.popEnterAnim);
                        this.mManager.addFragment(fragment, false);
                        break;
                    }
                }
            }
            else {
                fragment.setNextAnim(op.popExitAnim);
                this.mManager.removeFragment(fragment);
            }
            if (!this.mReorderingAllowed && op.cmd != 3 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mReorderingAllowed && b) {
            this.mManager.moveToState(this.mManager.mCurState, true);
        }
    }
    
    Fragment expandOps(final ArrayList<Fragment> list, Fragment fragment) {
        int i = 0;
        Fragment fragment2 = fragment;
        while (i < this.mOps.size()) {
            final Op op = this.mOps.get(i);
            int n = 0;
            switch (op.cmd) {
                default: {
                    fragment = fragment2;
                    n = i;
                    break;
                }
                case 8: {
                    this.mOps.add(i, new Op(9, fragment2));
                    n = i + 1;
                    fragment = op.fragment;
                    break;
                }
                case 3:
                case 6: {
                    list.remove(op.fragment);
                    fragment = fragment2;
                    n = i;
                    if (op.fragment == fragment2) {
                        this.mOps.add(i, new Op(9, op.fragment));
                        n = i + 1;
                        fragment = null;
                        break;
                    }
                    break;
                }
                case 2: {
                    final Fragment fragment3 = op.fragment;
                    final int mContainerId = fragment3.mContainerId;
                    int j = list.size() - 1;
                    fragment = fragment2;
                    int n2 = 0;
                    while (j >= 0) {
                        final Fragment o = list.get(j);
                        int index = i;
                        Fragment fragment4 = fragment;
                        int n3 = n2;
                        if (o.mContainerId == mContainerId) {
                            if (o == fragment3) {
                                n3 = 1;
                                index = i;
                                fragment4 = fragment;
                            }
                            else {
                                index = i;
                                if (o == (fragment4 = fragment)) {
                                    this.mOps.add(i, new Op(9, o));
                                    index = i + 1;
                                    fragment4 = null;
                                }
                                final Op element = new Op(3, o);
                                element.enterAnim = op.enterAnim;
                                element.popEnterAnim = op.popEnterAnim;
                                element.exitAnim = op.exitAnim;
                                element.popExitAnim = op.popExitAnim;
                                this.mOps.add(index, element);
                                list.remove(o);
                                ++index;
                                n3 = n2;
                            }
                        }
                        --j;
                        i = index;
                        fragment = fragment4;
                        n2 = n3;
                    }
                    if (n2 != 0) {
                        this.mOps.remove(i);
                        --i;
                    }
                    else {
                        op.cmd = 1;
                        list.add(fragment3);
                    }
                    n = i;
                    break;
                }
                case 1:
                case 7: {
                    list.add(op.fragment);
                    n = i;
                    fragment = fragment2;
                    break;
                }
            }
            i = n + 1;
            fragment2 = fragment;
        }
        return fragment2;
    }
    
    @Override
    public boolean generateOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Run: ");
            sb.append(this);
            Log.v("FragmentManager", sb.toString());
        }
        list.add(this);
        list2.add(false);
        if (this.mAddToBackStack) {
            this.mManager.addBackStackState(this);
        }
        return true;
    }
    
    @Override
    public String getName() {
        return this.mName;
    }
    
    boolean interactsWith(final int n) {
        for (int size = this.mOps.size(), i = 0; i < size; ++i) {
            final Op op = this.mOps.get(i);
            int mContainerId;
            if (op.fragment != null) {
                mContainerId = op.fragment.mContainerId;
            }
            else {
                mContainerId = 0;
            }
            if (mContainerId != 0 && mContainerId == n) {
                return true;
            }
        }
        return false;
    }
    
    boolean interactsWith(final ArrayList<BackStackRecord> list, final int n, final int n2) {
        if (n2 == n) {
            return false;
        }
        final int size = this.mOps.size();
        int i = 0;
        int n3 = -1;
        while (i < size) {
            final Op op = this.mOps.get(i);
            int mContainerId;
            if (op.fragment != null) {
                mContainerId = op.fragment.mContainerId;
            }
            else {
                mContainerId = 0;
            }
            int n4 = n3;
            if (mContainerId != 0 && mContainerId != (n4 = n3)) {
                for (int j = n; j < n2; ++j) {
                    final BackStackRecord backStackRecord = list.get(j);
                    for (int size2 = backStackRecord.mOps.size(), k = 0; k < size2; ++k) {
                        final Op op2 = backStackRecord.mOps.get(k);
                        int mContainerId2;
                        if (op2.fragment != null) {
                            mContainerId2 = op2.fragment.mContainerId;
                        }
                        else {
                            mContainerId2 = 0;
                        }
                        if (mContainerId2 == mContainerId) {
                            return true;
                        }
                    }
                }
                n4 = mContainerId;
            }
            ++i;
            n3 = n4;
        }
        return false;
    }
    
    boolean isPostponed() {
        for (int i = 0; i < this.mOps.size(); ++i) {
            if (isFragmentPostponed(this.mOps.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public FragmentTransaction remove(final Fragment fragment) {
        this.addOp(new Op(3, fragment));
        return this;
    }
    
    @Override
    public FragmentTransaction replace(final int n, final Fragment fragment) {
        return this.replace(n, fragment, null);
    }
    
    @Override
    public FragmentTransaction replace(final int n, final Fragment fragment, final String s) {
        if (n != 0) {
            this.doAddOp(n, fragment, s, 2);
            return this;
        }
        throw new IllegalArgumentException("Must use non-zero containerViewId");
    }
    
    public void runOnCommitRunnables() {
        if (this.mCommitRunnables != null) {
            for (int i = 0; i < this.mCommitRunnables.size(); ++i) {
                this.mCommitRunnables.get(i).run();
            }
            this.mCommitRunnables = null;
        }
    }
    
    @Override
    public FragmentTransaction setCustomAnimations(final int mEnterAnim, final int mExitAnim, final int mPopEnterAnim, final int mPopExitAnim) {
        this.mEnterAnim = mEnterAnim;
        this.mExitAnim = mExitAnim;
        this.mPopEnterAnim = mPopEnterAnim;
        this.mPopExitAnim = mPopExitAnim;
        return this;
    }
    
    void setOnStartPostponedListener(final Fragment.OnStartEnterTransitionListener onStartEnterTransitionListener) {
        for (int i = 0; i < this.mOps.size(); ++i) {
            final Op op = this.mOps.get(i);
            if (isFragmentPostponed(op)) {
                op.fragment.setOnStartEnterTransitionListener(onStartEnterTransitionListener);
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("BackStackEntry{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mName != null) {
            sb.append(" ");
            sb.append(this.mName);
        }
        sb.append("}");
        return sb.toString();
    }
    
    Fragment trackAddedFragmentsInPop(final ArrayList<Fragment> list, Fragment fragment) {
        for (int i = 0; i < this.mOps.size(); ++i) {
            final Op op = this.mOps.get(i);
            final int cmd = op.cmd;
            Label_0106: {
                if (cmd != 1) {
                    if (cmd != 3) {
                        switch (cmd) {
                            default: {
                                continue;
                            }
                            case 9: {
                                fragment = op.fragment;
                                continue;
                            }
                            case 8: {
                                fragment = null;
                                continue;
                            }
                            case 6: {
                                break;
                            }
                            case 7: {
                                break Label_0106;
                            }
                        }
                    }
                    list.add(op.fragment);
                    continue;
                }
            }
            list.remove(op.fragment);
        }
        return fragment;
    }
    
    static final class Op
    {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        int popEnterAnim;
        int popExitAnim;
        
        Op() {
        }
        
        Op(final int cmd, final Fragment fragment) {
            this.cmd = cmd;
            this.fragment = fragment;
        }
    }
}
