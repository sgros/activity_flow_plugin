// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.graphics.Rect;
import android.view.ViewGroup;
import java.util.Map;
import java.util.List;
import android.util.SparseArray;
import android.support.v4.view.ViewCompat;
import java.util.Collection;
import android.support.v4.util.ArrayMap;
import android.view.View;
import java.util.ArrayList;
import android.os.Build$VERSION;

class FragmentTransition
{
    private static final int[] INVERSE_OPS;
    private static final FragmentTransitionImpl PLATFORM_IMPL;
    private static final FragmentTransitionImpl SUPPORT_IMPL;
    
    static {
        INVERSE_OPS = new int[] { 0, 3, 0, 1, 5, 4, 7, 6, 9, 8 };
        FragmentTransitionCompat21 platform_IMPL;
        if (Build$VERSION.SDK_INT >= 21) {
            platform_IMPL = new FragmentTransitionCompat21();
        }
        else {
            platform_IMPL = null;
        }
        PLATFORM_IMPL = platform_IMPL;
        SUPPORT_IMPL = resolveSupportImpl();
    }
    
    private static void addSharedElementsWithMatchingNames(final ArrayList<View> list, final ArrayMap<String, View> arrayMap, final Collection<String> collection) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            final View e = arrayMap.valueAt(i);
            if (collection.contains(ViewCompat.getTransitionName(e))) {
                list.add(e);
            }
        }
    }
    
    private static void addToFirstInLastOut(final BackStackRecord backStackRecord, final BackStackRecord.Op op, final SparseArray<FragmentContainerTransition> sparseArray, final boolean b, final boolean b2) {
        final Fragment fragment = op.fragment;
        if (fragment == null) {
            return;
        }
        final int mContainerId = fragment.mContainerId;
        if (mContainerId == 0) {
            return;
        }
        int cmd;
        if (b) {
            cmd = FragmentTransition.INVERSE_OPS[op.cmd];
        }
        else {
            cmd = op.cmd;
        }
        final int n = 0;
        int n2 = 0;
        while (true) {
            boolean b3 = false;
            int n4 = 0;
            boolean b4 = false;
        Label_0337:
            while (true) {
                Label_0331: {
                    Label_0328: {
                        Label_0322: {
                            Label_0291: {
                                if (cmd != 1) {
                                    int n3 = 0;
                                    Label_0274: {
                                        Label_0249: {
                                            switch (cmd) {
                                                default: {
                                                    b3 = false;
                                                    break Label_0099;
                                                }
                                                case 5: {
                                                    if (!b2) {
                                                        n2 = (fragment.mHidden ? 1 : 0);
                                                        break Label_0331;
                                                    }
                                                    if (fragment.mHiddenChanged && !fragment.mHidden && fragment.mAdded) {
                                                        break Label_0322;
                                                    }
                                                    break Label_0328;
                                                }
                                                case 4: {
                                                    if (b2 ? (fragment.mHiddenChanged && fragment.mAdded && fragment.mHidden) : (fragment.mAdded && !fragment.mHidden)) {
                                                        break;
                                                    }
                                                    break Label_0249;
                                                }
                                                case 3:
                                                case 6: {
                                                    if (b2 ? (!fragment.mAdded && fragment.mView != null && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f) : (fragment.mAdded && !fragment.mHidden)) {
                                                        break;
                                                    }
                                                    break Label_0249;
                                                }
                                                case 7: {
                                                    break Label_0291;
                                                }
                                            }
                                            n3 = 1;
                                            break Label_0274;
                                        }
                                        n3 = 0;
                                    }
                                    n4 = n3;
                                    b3 = false;
                                    b4 = true;
                                    n2 = n;
                                    break Label_0337;
                                }
                                break Label_0291;
                                b4 = false;
                                n4 = 0;
                                break Label_0337;
                            }
                            if (b2) {
                                n2 = (fragment.mIsNewlyAdded ? 1 : 0);
                                break Label_0331;
                            }
                            if (fragment.mAdded || fragment.mHidden) {
                                break Label_0328;
                            }
                        }
                        n2 = 1;
                        break Label_0331;
                    }
                    n2 = 0;
                }
                b3 = true;
                continue;
            }
            FragmentContainerTransition ensureContainer;
            final FragmentContainerTransition fragmentContainerTransition = ensureContainer = (FragmentContainerTransition)sparseArray.get(mContainerId);
            if (n2 != 0) {
                ensureContainer = ensureContainer(fragmentContainerTransition, sparseArray, mContainerId);
                ensureContainer.lastIn = fragment;
                ensureContainer.lastInIsPop = b;
                ensureContainer.lastInTransaction = backStackRecord;
            }
            if (!b2 && b3) {
                if (ensureContainer != null && ensureContainer.firstOut == fragment) {
                    ensureContainer.firstOut = null;
                }
                final FragmentManagerImpl mManager = backStackRecord.mManager;
                if (fragment.mState < 1 && mManager.mCurState >= 1 && !backStackRecord.mReorderingAllowed) {
                    mManager.makeActive(fragment);
                    mManager.moveToState(fragment, 1, 0, 0, false);
                }
            }
            FragmentContainerTransition ensureContainer2 = ensureContainer;
            Label_0508: {
                if (n4 != 0) {
                    if (ensureContainer != null) {
                        ensureContainer2 = ensureContainer;
                        if (ensureContainer.firstOut != null) {
                            break Label_0508;
                        }
                    }
                    ensureContainer2 = ensureContainer(ensureContainer, sparseArray, mContainerId);
                    ensureContainer2.firstOut = fragment;
                    ensureContainer2.firstOutIsPop = b;
                    ensureContainer2.firstOutTransaction = backStackRecord;
                }
            }
            if (!b2 && b4 && ensureContainer2 != null && ensureContainer2.lastIn == fragment) {
                ensureContainer2.lastIn = null;
            }
            return;
            continue;
        }
    }
    
    public static void calculateFragments(final BackStackRecord backStackRecord, final SparseArray<FragmentContainerTransition> sparseArray, final boolean b) {
        for (int size = backStackRecord.mOps.size(), i = 0; i < size; ++i) {
            addToFirstInLastOut(backStackRecord, backStackRecord.mOps.get(i), sparseArray, false, b);
        }
    }
    
    private static ArrayMap<String, String> calculateNameOverrides(final int n, final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final int n2, int i) {
        final ArrayMap<Object, String> arrayMap = new ArrayMap<Object, String>();
        --i;
        while (i >= n2) {
            final BackStackRecord backStackRecord = list.get(i);
            if (backStackRecord.interactsWith(n)) {
                final boolean booleanValue = list2.get(i);
                if (backStackRecord.mSharedElementSourceNames != null) {
                    final int size = backStackRecord.mSharedElementSourceNames.size();
                    ArrayList<String> list3;
                    ArrayList<String> list4;
                    if (booleanValue) {
                        list3 = backStackRecord.mSharedElementSourceNames;
                        list4 = backStackRecord.mSharedElementTargetNames;
                    }
                    else {
                        list4 = backStackRecord.mSharedElementSourceNames;
                        list3 = backStackRecord.mSharedElementTargetNames;
                    }
                    for (int j = 0; j < size; ++j) {
                        final String s = list4.get(j);
                        final String s2 = list3.get(j);
                        final String s3 = arrayMap.remove(s2);
                        if (s3 != null) {
                            arrayMap.put(s, s3);
                        }
                        else {
                            arrayMap.put(s, s2);
                        }
                    }
                }
            }
            --i;
        }
        return (ArrayMap<String, String>)arrayMap;
    }
    
    public static void calculatePopFragments(final BackStackRecord backStackRecord, final SparseArray<FragmentContainerTransition> sparseArray, final boolean b) {
        if (!backStackRecord.mManager.mContainer.onHasView()) {
            return;
        }
        for (int i = backStackRecord.mOps.size() - 1; i >= 0; --i) {
            addToFirstInLastOut(backStackRecord, backStackRecord.mOps.get(i), sparseArray, true, b);
        }
    }
    
    static void callSharedElementStartEnd(final Fragment fragment, final Fragment fragment2, final boolean b, final ArrayMap<String, View> arrayMap, final boolean b2) {
        SharedElementCallback sharedElementCallback;
        if (b) {
            sharedElementCallback = fragment2.getEnterTransitionCallback();
        }
        else {
            sharedElementCallback = fragment.getEnterTransitionCallback();
        }
        if (sharedElementCallback != null) {
            final ArrayList<View> list = new ArrayList<View>();
            final ArrayList<String> list2 = new ArrayList<String>();
            int i = 0;
            int size;
            if (arrayMap == null) {
                size = 0;
            }
            else {
                size = arrayMap.size();
            }
            while (i < size) {
                list2.add(arrayMap.keyAt(i));
                list.add(arrayMap.valueAt(i));
                ++i;
            }
            if (b2) {
                sharedElementCallback.onSharedElementStart(list2, list, null);
            }
            else {
                sharedElementCallback.onSharedElementEnd(list2, list, null);
            }
        }
    }
    
    private static boolean canHandleAll(final FragmentTransitionImpl fragmentTransitionImpl, final List<Object> list) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            if (!fragmentTransitionImpl.canHandle(list.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    static ArrayMap<String, View> captureInSharedElements(final FragmentTransitionImpl fragmentTransitionImpl, final ArrayMap<String, String> arrayMap, final Object o, final FragmentContainerTransition fragmentContainerTransition) {
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final View view = lastIn.getView();
        if (!arrayMap.isEmpty() && o != null && view != null) {
            final ArrayMap<Object, View> arrayMap2 = new ArrayMap<Object, View>();
            fragmentTransitionImpl.findNamedViews((Map<String, View>)arrayMap2, view);
            final BackStackRecord lastInTransaction = fragmentContainerTransition.lastInTransaction;
            SharedElementCallback sharedElementCallback;
            ArrayList<String> list;
            if (fragmentContainerTransition.lastInIsPop) {
                sharedElementCallback = lastIn.getExitTransitionCallback();
                list = lastInTransaction.mSharedElementSourceNames;
            }
            else {
                sharedElementCallback = lastIn.getEnterTransitionCallback();
                list = lastInTransaction.mSharedElementTargetNames;
            }
            if (list != null) {
                arrayMap2.retainAll(list);
                arrayMap2.retainAll(arrayMap.values());
            }
            if (sharedElementCallback != null) {
                sharedElementCallback.onMapSharedElements(list, (Map<String, View>)arrayMap2);
                for (int i = list.size() - 1; i >= 0; --i) {
                    final String s = list.get(i);
                    final View view2 = arrayMap2.get(s);
                    if (view2 == null) {
                        final String keyForValue = findKeyForValue(arrayMap, s);
                        if (keyForValue != null) {
                            arrayMap.remove(keyForValue);
                        }
                    }
                    else if (!s.equals(ViewCompat.getTransitionName(view2))) {
                        final String keyForValue2 = findKeyForValue(arrayMap, s);
                        if (keyForValue2 != null) {
                            arrayMap.put(keyForValue2, ViewCompat.getTransitionName(view2));
                        }
                    }
                }
            }
            else {
                retainValues(arrayMap, (ArrayMap<String, View>)arrayMap2);
            }
            return (ArrayMap<String, View>)arrayMap2;
        }
        arrayMap.clear();
        return null;
    }
    
    private static ArrayMap<String, View> captureOutSharedElements(final FragmentTransitionImpl fragmentTransitionImpl, final ArrayMap<String, String> arrayMap, final Object o, final FragmentContainerTransition fragmentContainerTransition) {
        if (!arrayMap.isEmpty() && o != null) {
            final Fragment firstOut = fragmentContainerTransition.firstOut;
            final ArrayMap<Object, Object> arrayMap2 = new ArrayMap<Object, Object>();
            fragmentTransitionImpl.findNamedViews((Map<String, View>)arrayMap2, firstOut.getView());
            final BackStackRecord firstOutTransaction = fragmentContainerTransition.firstOutTransaction;
            SharedElementCallback sharedElementCallback;
            ArrayList<String> list;
            if (fragmentContainerTransition.firstOutIsPop) {
                sharedElementCallback = firstOut.getEnterTransitionCallback();
                list = firstOutTransaction.mSharedElementTargetNames;
            }
            else {
                sharedElementCallback = firstOut.getExitTransitionCallback();
                list = firstOutTransaction.mSharedElementSourceNames;
            }
            arrayMap2.retainAll(list);
            if (sharedElementCallback != null) {
                sharedElementCallback.onMapSharedElements(list, (Map<String, View>)arrayMap2);
                for (int i = list.size() - 1; i >= 0; --i) {
                    final String s = list.get(i);
                    final View view = arrayMap2.get(s);
                    if (view == null) {
                        arrayMap.remove(s);
                    }
                    else if (!s.equals(ViewCompat.getTransitionName(view))) {
                        arrayMap.put(ViewCompat.getTransitionName(view), arrayMap.remove(s));
                    }
                }
            }
            else {
                arrayMap.retainAll(arrayMap2.keySet());
            }
            return (ArrayMap<String, View>)arrayMap2;
        }
        arrayMap.clear();
        return null;
    }
    
    private static FragmentTransitionImpl chooseImpl(final Fragment fragment, final Fragment fragment2) {
        final ArrayList<Object> list = new ArrayList<Object>();
        if (fragment != null) {
            final Object exitTransition = fragment.getExitTransition();
            if (exitTransition != null) {
                list.add(exitTransition);
            }
            final Object returnTransition = fragment.getReturnTransition();
            if (returnTransition != null) {
                list.add(returnTransition);
            }
            final Object sharedElementReturnTransition = fragment.getSharedElementReturnTransition();
            if (sharedElementReturnTransition != null) {
                list.add(sharedElementReturnTransition);
            }
        }
        if (fragment2 != null) {
            final Object enterTransition = fragment2.getEnterTransition();
            if (enterTransition != null) {
                list.add(enterTransition);
            }
            final Object reenterTransition = fragment2.getReenterTransition();
            if (reenterTransition != null) {
                list.add(reenterTransition);
            }
            final Object sharedElementEnterTransition = fragment2.getSharedElementEnterTransition();
            if (sharedElementEnterTransition != null) {
                list.add(sharedElementEnterTransition);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        if (FragmentTransition.PLATFORM_IMPL != null && canHandleAll(FragmentTransition.PLATFORM_IMPL, list)) {
            return FragmentTransition.PLATFORM_IMPL;
        }
        if (FragmentTransition.SUPPORT_IMPL != null && canHandleAll(FragmentTransition.SUPPORT_IMPL, list)) {
            return FragmentTransition.SUPPORT_IMPL;
        }
        if (FragmentTransition.PLATFORM_IMPL == null && FragmentTransition.SUPPORT_IMPL == null) {
            return null;
        }
        throw new IllegalArgumentException("Invalid Transition types");
    }
    
    static ArrayList<View> configureEnteringExitingViews(final FragmentTransitionImpl fragmentTransitionImpl, final Object o, final Fragment fragment, final ArrayList<View> c, final View e) {
        ArrayList<View> list2;
        if (o != null) {
            final ArrayList<View> list = new ArrayList<View>();
            final View view = fragment.getView();
            if (view != null) {
                fragmentTransitionImpl.captureTransitioningViews(list, view);
            }
            if (c != null) {
                list.removeAll(c);
            }
            list2 = list;
            if (!list.isEmpty()) {
                list.add(e);
                fragmentTransitionImpl.addTargets(o, list);
                list2 = list;
            }
        }
        else {
            list2 = null;
        }
        return list2;
    }
    
    private static Object configureSharedElementsOrdered(final FragmentTransitionImpl fragmentTransitionImpl, final ViewGroup viewGroup, final View view, final ArrayMap<String, String> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final ArrayList<View> list, final ArrayList<View> list2, final Object o, Object o2) {
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        if (lastIn == null || firstOut == null) {
            return null;
        }
        final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
        Object sharedElementTransition;
        if (arrayMap.isEmpty()) {
            sharedElementTransition = null;
        }
        else {
            sharedElementTransition = getSharedElementTransition(fragmentTransitionImpl, lastIn, firstOut, lastInIsPop);
        }
        final ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(fragmentTransitionImpl, arrayMap, sharedElementTransition, fragmentContainerTransition);
        if (arrayMap.isEmpty()) {
            sharedElementTransition = null;
        }
        else {
            list.addAll(captureOutSharedElements.values());
        }
        if (o == null && o2 == null && sharedElementTransition == null) {
            return null;
        }
        callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureOutSharedElements, true);
        if (sharedElementTransition != null) {
            final Rect rect = new Rect();
            fragmentTransitionImpl.setSharedElementTargets(sharedElementTransition, view, list);
            setOutEpicenter(fragmentTransitionImpl, sharedElementTransition, o2, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
            o2 = rect;
            if (o != null) {
                fragmentTransitionImpl.setEpicenter(o, rect);
                o2 = rect;
            }
        }
        else {
            o2 = null;
        }
        OneShotPreDrawListener.add((View)viewGroup, new Runnable() {
            @Override
            public void run() {
                final ArrayMap<String, View> captureInSharedElements = FragmentTransition.captureInSharedElements(fragmentTransitionImpl, arrayMap, sharedElementTransition, fragmentContainerTransition);
                if (captureInSharedElements != null) {
                    list2.addAll(captureInSharedElements.values());
                    list2.add(view);
                }
                FragmentTransition.callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureInSharedElements, false);
                if (sharedElementTransition != null) {
                    fragmentTransitionImpl.swapSharedElementTargets(sharedElementTransition, list, list2);
                    final View inEpicenterView = FragmentTransition.getInEpicenterView(captureInSharedElements, fragmentContainerTransition, o, lastInIsPop);
                    if (inEpicenterView != null) {
                        fragmentTransitionImpl.getBoundsOnScreen(inEpicenterView, o2);
                    }
                }
            }
        });
        return sharedElementTransition;
    }
    
    private static Object configureSharedElementsReordered(final FragmentTransitionImpl fragmentTransitionImpl, final ViewGroup viewGroup, final View e, final ArrayMap<String, String> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final ArrayList<View> list, final ArrayList<View> list2, final Object o, final Object o2) {
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        if (lastIn != null) {
            lastIn.getView().setVisibility(0);
        }
        if (lastIn == null || firstOut == null) {
            return null;
        }
        final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
        Object sharedElementTransition;
        if (arrayMap.isEmpty()) {
            sharedElementTransition = null;
        }
        else {
            sharedElementTransition = getSharedElementTransition(fragmentTransitionImpl, lastIn, firstOut, lastInIsPop);
        }
        final ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(fragmentTransitionImpl, arrayMap, sharedElementTransition, fragmentContainerTransition);
        final ArrayMap<String, View> captureInSharedElements = captureInSharedElements(fragmentTransitionImpl, arrayMap, sharedElementTransition, fragmentContainerTransition);
        Object o3;
        if (arrayMap.isEmpty()) {
            if (captureOutSharedElements != null) {
                captureOutSharedElements.clear();
            }
            if (captureInSharedElements != null) {
                captureInSharedElements.clear();
            }
            o3 = null;
        }
        else {
            addSharedElementsWithMatchingNames(list, captureOutSharedElements, arrayMap.keySet());
            addSharedElementsWithMatchingNames(list2, captureInSharedElements, arrayMap.values());
            o3 = sharedElementTransition;
        }
        if (o == null && o2 == null && o3 == null) {
            return null;
        }
        callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureOutSharedElements, true);
        Rect rect;
        Object inEpicenterView;
        if (o3 != null) {
            list2.add(e);
            fragmentTransitionImpl.setSharedElementTargets(o3, e, list);
            setOutEpicenter(fragmentTransitionImpl, o3, o2, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
            rect = new Rect();
            inEpicenterView = getInEpicenterView(captureInSharedElements, fragmentContainerTransition, o, lastInIsPop);
            if (inEpicenterView != null) {
                fragmentTransitionImpl.setEpicenter(o, rect);
            }
        }
        else {
            inEpicenterView = (rect = null);
        }
        OneShotPreDrawListener.add((View)viewGroup, new Runnable() {
            @Override
            public void run() {
                FragmentTransition.callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureInSharedElements, false);
                if (inEpicenterView != null) {
                    fragmentTransitionImpl.getBoundsOnScreen(inEpicenterView, rect);
                }
            }
        });
        return o3;
    }
    
    private static void configureTransitionsOrdered(final FragmentManagerImpl fragmentManagerImpl, final int n, final FragmentContainerTransition fragmentContainerTransition, final View view, final ArrayMap<String, String> arrayMap) {
        ViewGroup viewGroup;
        if (fragmentManagerImpl.mContainer.onHasView()) {
            viewGroup = (ViewGroup)fragmentManagerImpl.mContainer.onFindViewById(n);
        }
        else {
            viewGroup = null;
        }
        if (viewGroup == null) {
            return;
        }
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        final FragmentTransitionImpl chooseImpl = chooseImpl(firstOut, lastIn);
        if (chooseImpl == null) {
            return;
        }
        final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
        final boolean firstOutIsPop = fragmentContainerTransition.firstOutIsPop;
        final Object enterTransition = getEnterTransition(chooseImpl, lastIn, lastInIsPop);
        Object exitTransition = getExitTransition(chooseImpl, firstOut, firstOutIsPop);
        final ArrayList<View> list = new ArrayList<View>();
        final ArrayList<View> list2 = new ArrayList<View>();
        final Object configureSharedElementsOrdered = configureSharedElementsOrdered(chooseImpl, viewGroup, view, arrayMap, fragmentContainerTransition, list, list2, enterTransition, exitTransition);
        if (enterTransition == null && configureSharedElementsOrdered == null && exitTransition == null) {
            return;
        }
        final ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(chooseImpl, exitTransition, firstOut, list, view);
        if (configureEnteringExitingViews == null || configureEnteringExitingViews.isEmpty()) {
            exitTransition = null;
        }
        chooseImpl.addTarget(enterTransition, view);
        final Object mergeTransitions = mergeTransitions(chooseImpl, enterTransition, exitTransition, configureSharedElementsOrdered, lastIn, fragmentContainerTransition.lastInIsPop);
        if (mergeTransitions != null) {
            final ArrayList<View> list3 = new ArrayList<View>();
            chooseImpl.scheduleRemoveTargets(mergeTransitions, enterTransition, list3, exitTransition, configureEnteringExitingViews, configureSharedElementsOrdered, list2);
            scheduleTargetChange(chooseImpl, viewGroup, lastIn, view, list2, enterTransition, list3, exitTransition, configureEnteringExitingViews);
            chooseImpl.setNameOverridesOrdered((View)viewGroup, list2, arrayMap);
            chooseImpl.beginDelayedTransition(viewGroup, mergeTransitions);
            chooseImpl.scheduleNameReset(viewGroup, list2, arrayMap);
        }
    }
    
    private static void configureTransitionsReordered(final FragmentManagerImpl fragmentManagerImpl, final int n, final FragmentContainerTransition fragmentContainerTransition, final View view, final ArrayMap<String, String> arrayMap) {
        ViewGroup viewGroup;
        if (fragmentManagerImpl.mContainer.onHasView()) {
            viewGroup = (ViewGroup)fragmentManagerImpl.mContainer.onFindViewById(n);
        }
        else {
            viewGroup = null;
        }
        if (viewGroup == null) {
            return;
        }
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        final FragmentTransitionImpl chooseImpl = chooseImpl(firstOut, lastIn);
        if (chooseImpl == null) {
            return;
        }
        final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
        final boolean firstOutIsPop = fragmentContainerTransition.firstOutIsPop;
        final ArrayList<View> list = new ArrayList<View>();
        final ArrayList<View> list2 = new ArrayList<View>();
        final Object enterTransition = getEnterTransition(chooseImpl, lastIn, lastInIsPop);
        final Object exitTransition = getExitTransition(chooseImpl, firstOut, firstOutIsPop);
        final Object configureSharedElementsReordered = configureSharedElementsReordered(chooseImpl, viewGroup, view, arrayMap, fragmentContainerTransition, list2, list, enterTransition, exitTransition);
        if (enterTransition == null && configureSharedElementsReordered == null && exitTransition == null) {
            return;
        }
        final Object o = exitTransition;
        final ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(chooseImpl, o, firstOut, list2, view);
        final ArrayList<View> configureEnteringExitingViews2 = configureEnteringExitingViews(chooseImpl, enterTransition, lastIn, list, view);
        setViewVisibility(configureEnteringExitingViews2, 4);
        final Object mergeTransitions = mergeTransitions(chooseImpl, enterTransition, o, configureSharedElementsReordered, lastIn, lastInIsPop);
        if (mergeTransitions != null) {
            replaceHide(chooseImpl, o, firstOut, configureEnteringExitingViews);
            final ArrayList<String> prepareSetNameOverridesReordered = chooseImpl.prepareSetNameOverridesReordered(list);
            chooseImpl.scheduleRemoveTargets(mergeTransitions, enterTransition, configureEnteringExitingViews2, o, configureEnteringExitingViews, configureSharedElementsReordered, list);
            chooseImpl.beginDelayedTransition(viewGroup, mergeTransitions);
            chooseImpl.setNameOverridesReordered((View)viewGroup, list2, list, prepareSetNameOverridesReordered, arrayMap);
            setViewVisibility(configureEnteringExitingViews2, 0);
            chooseImpl.swapSharedElementTargets(configureSharedElementsReordered, list2, list);
        }
    }
    
    private static FragmentContainerTransition ensureContainer(final FragmentContainerTransition fragmentContainerTransition, final SparseArray<FragmentContainerTransition> sparseArray, final int n) {
        FragmentContainerTransition fragmentContainerTransition2 = fragmentContainerTransition;
        if (fragmentContainerTransition == null) {
            fragmentContainerTransition2 = new FragmentContainerTransition();
            sparseArray.put(n, (Object)fragmentContainerTransition2);
        }
        return fragmentContainerTransition2;
    }
    
    private static String findKeyForValue(final ArrayMap<String, String> arrayMap, final String s) {
        for (int size = arrayMap.size(), i = 0; i < size; ++i) {
            if (s.equals(arrayMap.valueAt(i))) {
                return arrayMap.keyAt(i);
            }
        }
        return null;
    }
    
    private static Object getEnterTransition(final FragmentTransitionImpl fragmentTransitionImpl, final Fragment fragment, final boolean b) {
        if (fragment == null) {
            return null;
        }
        Object o;
        if (b) {
            o = fragment.getReenterTransition();
        }
        else {
            o = fragment.getEnterTransition();
        }
        return fragmentTransitionImpl.cloneTransition(o);
    }
    
    private static Object getExitTransition(final FragmentTransitionImpl fragmentTransitionImpl, final Fragment fragment, final boolean b) {
        if (fragment == null) {
            return null;
        }
        Object o;
        if (b) {
            o = fragment.getReturnTransition();
        }
        else {
            o = fragment.getExitTransition();
        }
        return fragmentTransitionImpl.cloneTransition(o);
    }
    
    static View getInEpicenterView(final ArrayMap<String, View> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final Object o, final boolean b) {
        final BackStackRecord lastInTransaction = fragmentContainerTransition.lastInTransaction;
        if (o != null && arrayMap != null && lastInTransaction.mSharedElementSourceNames != null && !lastInTransaction.mSharedElementSourceNames.isEmpty()) {
            String s;
            if (b) {
                s = lastInTransaction.mSharedElementSourceNames.get(0);
            }
            else {
                s = lastInTransaction.mSharedElementTargetNames.get(0);
            }
            return arrayMap.get(s);
        }
        return null;
    }
    
    private static Object getSharedElementTransition(final FragmentTransitionImpl fragmentTransitionImpl, final Fragment fragment, final Fragment fragment2, final boolean b) {
        if (fragment != null && fragment2 != null) {
            Object o;
            if (b) {
                o = fragment2.getSharedElementReturnTransition();
            }
            else {
                o = fragment.getSharedElementEnterTransition();
            }
            return fragmentTransitionImpl.wrapTransitionInSet(fragmentTransitionImpl.cloneTransition(o));
        }
        return null;
    }
    
    private static Object mergeTransitions(final FragmentTransitionImpl fragmentTransitionImpl, final Object o, final Object o2, final Object o3, final Fragment fragment, final boolean b) {
        boolean b2;
        if (o != null && o2 != null && fragment != null) {
            if (b) {
                b2 = fragment.getAllowReturnTransitionOverlap();
            }
            else {
                b2 = fragment.getAllowEnterTransitionOverlap();
            }
        }
        else {
            b2 = true;
        }
        Object o4;
        if (b2) {
            o4 = fragmentTransitionImpl.mergeTransitionsTogether(o2, o, o3);
        }
        else {
            o4 = fragmentTransitionImpl.mergeTransitionsInSequence(o2, o, o3);
        }
        return o4;
    }
    
    private static void replaceHide(final FragmentTransitionImpl fragmentTransitionImpl, final Object o, final Fragment fragment, final ArrayList<View> list) {
        if (fragment != null && o != null && fragment.mAdded && fragment.mHidden && fragment.mHiddenChanged) {
            fragment.setHideReplaced(true);
            fragmentTransitionImpl.scheduleHideFragmentView(o, fragment.getView(), list);
            OneShotPreDrawListener.add((View)fragment.mContainer, new Runnable() {
                @Override
                public void run() {
                    FragmentTransition.setViewVisibility(list, 4);
                }
            });
        }
    }
    
    private static FragmentTransitionImpl resolveSupportImpl() {
        try {
            return (FragmentTransitionImpl)Class.forName("android.support.transition.FragmentTransitionSupport").getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private static void retainValues(final ArrayMap<String, String> arrayMap, final ArrayMap<String, View> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            if (!arrayMap2.containsKey(arrayMap.valueAt(i))) {
                arrayMap.removeAt(i);
            }
        }
    }
    
    private static void scheduleTargetChange(final FragmentTransitionImpl fragmentTransitionImpl, final ViewGroup viewGroup, final Fragment fragment, final View view, final ArrayList<View> list, final Object o, final ArrayList<View> list2, final Object o2, final ArrayList<View> list3) {
        OneShotPreDrawListener.add((View)viewGroup, new Runnable() {
            @Override
            public void run() {
                if (o != null) {
                    fragmentTransitionImpl.removeTarget(o, view);
                    list2.addAll(FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, o, fragment, list, view));
                }
                if (list3 != null) {
                    if (o2 != null) {
                        final ArrayList<View> list = new ArrayList<View>();
                        list.add(view);
                        fragmentTransitionImpl.replaceTargets(o2, list3, list);
                    }
                    list3.clear();
                    list3.add(view);
                }
            }
        });
    }
    
    private static void setOutEpicenter(final FragmentTransitionImpl fragmentTransitionImpl, final Object o, final Object o2, final ArrayMap<String, View> arrayMap, final boolean b, final BackStackRecord backStackRecord) {
        if (backStackRecord.mSharedElementSourceNames != null && !backStackRecord.mSharedElementSourceNames.isEmpty()) {
            String s;
            if (b) {
                s = backStackRecord.mSharedElementTargetNames.get(0);
            }
            else {
                s = backStackRecord.mSharedElementSourceNames.get(0);
            }
            final View view = arrayMap.get(s);
            fragmentTransitionImpl.setEpicenter(o, view);
            if (o2 != null) {
                fragmentTransitionImpl.setEpicenter(o2, view);
            }
        }
    }
    
    static void setViewVisibility(final ArrayList<View> list, final int visibility) {
        if (list == null) {
            return;
        }
        for (int i = list.size() - 1; i >= 0; --i) {
            list.get(i).setVisibility(visibility);
        }
    }
    
    static void startTransitions(final FragmentManagerImpl fragmentManagerImpl, final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final int n, final int n2, final boolean b) {
        if (fragmentManagerImpl.mCurState < 1) {
            return;
        }
        final SparseArray sparseArray = new SparseArray();
        for (int i = n; i < n2; ++i) {
            final BackStackRecord backStackRecord = list.get(i);
            if (list2.get(i)) {
                calculatePopFragments(backStackRecord, (SparseArray<FragmentContainerTransition>)sparseArray, b);
            }
            else {
                calculateFragments(backStackRecord, (SparseArray<FragmentContainerTransition>)sparseArray, b);
            }
        }
        if (sparseArray.size() != 0) {
            final View view = new View(fragmentManagerImpl.mHost.getContext());
            for (int size = sparseArray.size(), j = 0; j < size; ++j) {
                final int key = sparseArray.keyAt(j);
                final ArrayMap<String, String> calculateNameOverrides = calculateNameOverrides(key, list, list2, n, n2);
                final FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition)sparseArray.valueAt(j);
                if (b) {
                    configureTransitionsReordered(fragmentManagerImpl, key, fragmentContainerTransition, view, calculateNameOverrides);
                }
                else {
                    configureTransitionsOrdered(fragmentManagerImpl, key, fragmentContainerTransition, view, calculateNameOverrides);
                }
            }
        }
    }
    
    static class FragmentContainerTransition
    {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;
    }
}
