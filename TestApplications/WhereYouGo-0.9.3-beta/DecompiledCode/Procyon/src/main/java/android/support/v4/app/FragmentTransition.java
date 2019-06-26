// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.os.Build$VERSION;
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

class FragmentTransition
{
    private static final int[] INVERSE_OPS;
    
    static {
        INVERSE_OPS = new int[] { 0, 3, 0, 1, 5, 4, 7, 6 };
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
        final int mContainerId = fragment.mContainerId;
        if (mContainerId != 0) {
            int cmd;
            if (b) {
                cmd = FragmentTransition.INVERSE_OPS[op.cmd];
            }
            else {
                cmd = op.cmd;
            }
            final boolean b3 = false;
            final int n = 0;
            final boolean b4 = false;
            final int n2 = 0;
            int n3 = b4 ? 1 : 0;
            int n4 = b3 ? 1 : 0;
            int n5 = n2;
            int n6 = n;
            Label_0120: {
                switch (cmd) {
                    default: {
                        n6 = n;
                        n5 = n2;
                        n4 = (b3 ? 1 : 0);
                        n3 = (b4 ? 1 : 0);
                        break Label_0120;
                    }
                    case 3:
                    case 6: {
                        if (b2) {
                            if (!fragment.mAdded && fragment.mView != null && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f) {
                                n3 = 1;
                            }
                            else {
                                n3 = 0;
                            }
                        }
                        else if (fragment.mAdded && !fragment.mHidden) {
                            n3 = 1;
                        }
                        else {
                            n3 = 0;
                        }
                        n6 = 1;
                        n4 = (b3 ? 1 : 0);
                        n5 = n2;
                        break Label_0120;
                    }
                    case 4: {
                        if (b2) {
                            if (fragment.mHiddenChanged && fragment.mAdded && fragment.mHidden) {
                                n3 = 1;
                            }
                            else {
                                n3 = 0;
                            }
                        }
                        else if (fragment.mAdded && !fragment.mHidden) {
                            n3 = 1;
                        }
                        else {
                            n3 = 0;
                        }
                        n6 = 1;
                        n4 = (b3 ? 1 : 0);
                        n5 = n2;
                        break Label_0120;
                    }
                    case 1:
                    case 7: {
                        if (b2) {
                            n4 = (fragment.mIsNewlyAdded ? 1 : 0);
                        }
                        else if (!fragment.mAdded && !fragment.mHidden) {
                            n4 = 1;
                        }
                        else {
                            n4 = 0;
                        }
                        n5 = 1;
                        n3 = (b4 ? 1 : 0);
                        n6 = n;
                        break Label_0120;
                    }
                    case 5: {
                        if (b2) {
                            if (fragment.mHiddenChanged && !fragment.mHidden && fragment.mAdded) {
                                n4 = 1;
                            }
                            else {
                                n4 = 0;
                            }
                        }
                        else {
                            n4 = (fragment.mHidden ? 1 : 0);
                        }
                        n5 = 1;
                        n3 = (b4 ? 1 : 0);
                        n6 = n;
                    }
                    case 2: {
                        FragmentContainerTransition ensureContainer;
                        final FragmentContainerTransition fragmentContainerTransition = ensureContainer = (FragmentContainerTransition)sparseArray.get(mContainerId);
                        if (n4 != 0) {
                            ensureContainer = ensureContainer(fragmentContainerTransition, sparseArray, mContainerId);
                            ensureContainer.lastIn = fragment;
                            ensureContainer.lastInIsPop = b;
                            ensureContainer.lastInTransaction = backStackRecord;
                        }
                        if (!b2 && n5 != 0) {
                            if (ensureContainer != null && ensureContainer.firstOut == fragment) {
                                ensureContainer.firstOut = null;
                            }
                            final FragmentManagerImpl mManager = backStackRecord.mManager;
                            if (fragment.mState < 1 && mManager.mCurState >= 1 && !backStackRecord.mAllowOptimization) {
                                mManager.makeActive(fragment);
                                mManager.moveToState(fragment, 1, 0, 0, false);
                            }
                        }
                        FragmentContainerTransition ensureContainer2 = ensureContainer;
                        Label_0291: {
                            if (n3 != 0) {
                                if (ensureContainer != null) {
                                    ensureContainer2 = ensureContainer;
                                    if (ensureContainer.firstOut != null) {
                                        break Label_0291;
                                    }
                                }
                                ensureContainer2 = ensureContainer(ensureContainer, sparseArray, mContainerId);
                                ensureContainer2.firstOut = fragment;
                                ensureContainer2.firstOutIsPop = b;
                                ensureContainer2.firstOutTransaction = backStackRecord;
                            }
                        }
                        if (!b2 && n6 != 0 && ensureContainer2 != null && ensureContainer2.lastIn == fragment) {
                            ensureContainer2.lastIn = null;
                            break;
                        }
                        break;
                    }
                }
            }
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
        if (backStackRecord.mManager.mContainer.onHasView()) {
            for (int i = backStackRecord.mOps.size() - 1; i >= 0; --i) {
                addToFirstInLastOut(backStackRecord, backStackRecord.mOps.get(i), sparseArray, true, b);
            }
        }
    }
    
    private static void callSharedElementStartEnd(final Fragment fragment, final Fragment fragment2, final boolean b, final ArrayMap<String, View> arrayMap, final boolean b2) {
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
            int size;
            if (arrayMap == null) {
                size = 0;
            }
            else {
                size = arrayMap.size();
            }
            for (int i = 0; i < size; ++i) {
                list2.add(arrayMap.keyAt(i));
                list.add(arrayMap.valueAt(i));
            }
            if (b2) {
                sharedElementCallback.onSharedElementStart(list2, list, null);
            }
            else {
                sharedElementCallback.onSharedElementEnd(list2, list, null);
            }
        }
    }
    
    private static ArrayMap<String, View> captureInSharedElements(final ArrayMap<String, String> arrayMap, final Object o, final FragmentContainerTransition fragmentContainerTransition) {
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final View view = lastIn.getView();
        ArrayMap<String, View> arrayMap2;
        if (arrayMap.isEmpty() || o == null || view == null) {
            arrayMap.clear();
            arrayMap2 = null;
        }
        else {
            final ArrayMap arrayMap3 = new ArrayMap<Object, Object>();
            FragmentTransitionCompat21.findNamedViews(arrayMap3, view);
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
                arrayMap3.retainAll(list);
            }
            if (sharedElementCallback != null) {
                sharedElementCallback.onMapSharedElements(list, arrayMap3);
                int index = list.size() - 1;
                while (true) {
                    arrayMap2 = (ArrayMap<String, View>)arrayMap3;
                    if (index < 0) {
                        break;
                    }
                    final String s = list.get(index);
                    final View view2 = arrayMap3.get(s);
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
                    --index;
                }
            }
            else {
                retainValues(arrayMap, arrayMap3);
                arrayMap2 = (ArrayMap<String, View>)arrayMap3;
            }
        }
        return arrayMap2;
    }
    
    private static ArrayMap<String, View> captureOutSharedElements(final ArrayMap<String, String> arrayMap, final Object o, final FragmentContainerTransition fragmentContainerTransition) {
        ArrayMap<Object, Object> arrayMap2;
        if (arrayMap.isEmpty() || o == null) {
            arrayMap.clear();
            arrayMap2 = null;
        }
        else {
            final Fragment firstOut = fragmentContainerTransition.firstOut;
            final ArrayMap<Object, Object> arrayMap3 = new ArrayMap<Object, Object>();
            FragmentTransitionCompat21.findNamedViews((Map<String, View>)arrayMap3, firstOut.getView());
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
            arrayMap3.retainAll(list);
            if (sharedElementCallback != null) {
                sharedElementCallback.onMapSharedElements(list, (Map<String, View>)arrayMap3);
                int index = list.size() - 1;
                while (true) {
                    arrayMap2 = arrayMap3;
                    if (index < 0) {
                        break;
                    }
                    final String s = list.get(index);
                    final View view = arrayMap3.get(s);
                    if (view == null) {
                        arrayMap.remove(s);
                    }
                    else if (!s.equals(ViewCompat.getTransitionName(view))) {
                        arrayMap.put(ViewCompat.getTransitionName(view), arrayMap.remove(s));
                    }
                    --index;
                }
            }
            else {
                arrayMap.retainAll(arrayMap3.keySet());
                arrayMap2 = arrayMap3;
            }
        }
        return (ArrayMap<String, View>)arrayMap2;
    }
    
    private static ArrayList<View> configureEnteringExitingViews(final Object o, final Fragment fragment, final ArrayList<View> c, final View e) {
        ArrayList<View> list = null;
        if (o != null) {
            final ArrayList<View> list2 = new ArrayList<View>();
            final View view = fragment.getView();
            if (view != null) {
                FragmentTransitionCompat21.captureTransitioningViews(list2, view);
            }
            if (c != null) {
                list2.removeAll(c);
            }
            list = list2;
            if (!list2.isEmpty()) {
                list2.add(e);
                FragmentTransitionCompat21.addTargets(o, list2);
                list = list2;
            }
        }
        return list;
    }
    
    private static Object configureSharedElementsOptimized(final ViewGroup viewGroup, View inEpicenterView, final ArrayMap<String, String> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final ArrayList<View> list, final ArrayList<View> list2, final Object o, final Object o2) {
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        if (lastIn != null) {
            lastIn.getView().setVisibility(0);
        }
        Object o3;
        if (lastIn == null || firstOut == null) {
            o3 = null;
        }
        else {
            final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
            Object sharedElementTransition;
            if (arrayMap.isEmpty()) {
                sharedElementTransition = null;
            }
            else {
                sharedElementTransition = getSharedElementTransition(lastIn, firstOut, lastInIsPop);
            }
            final ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(arrayMap, sharedElementTransition, fragmentContainerTransition);
            final ArrayMap<String, View> captureInSharedElements = captureInSharedElements(arrayMap, sharedElementTransition, fragmentContainerTransition);
            if (arrayMap.isEmpty()) {
                final Object o4 = null;
                if (captureOutSharedElements != null) {
                    captureOutSharedElements.clear();
                }
                o3 = o4;
                if (captureInSharedElements != null) {
                    captureInSharedElements.clear();
                    o3 = o4;
                }
            }
            else {
                addSharedElementsWithMatchingNames(list, captureOutSharedElements, arrayMap.keySet());
                addSharedElementsWithMatchingNames(list2, captureInSharedElements, arrayMap.values());
                o3 = sharedElementTransition;
            }
            if (o == null && o2 == null && o3 == null) {
                o3 = null;
            }
            else {
                callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureOutSharedElements, true);
                Rect rect2;
                if (o3 != null) {
                    list2.add(inEpicenterView);
                    FragmentTransitionCompat21.setSharedElementTargets(o3, inEpicenterView, list);
                    setOutEpicenter(o3, o2, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
                    final Rect rect = new Rect();
                    final View view = inEpicenterView = getInEpicenterView(captureInSharedElements, fragmentContainerTransition, o, lastInIsPop);
                    rect2 = rect;
                    if (view != null) {
                        FragmentTransitionCompat21.setEpicenter(o, rect);
                        rect2 = rect;
                        inEpicenterView = view;
                    }
                }
                else {
                    rect2 = null;
                    inEpicenterView = null;
                }
                OneShotPreDrawListener.add((View)viewGroup, new Runnable() {
                    @Override
                    public void run() {
                        callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureInSharedElements, false);
                        if (inEpicenterView != null) {
                            FragmentTransitionCompat21.getBoundsOnScreen(inEpicenterView, rect2);
                        }
                    }
                });
            }
        }
        return o3;
    }
    
    private static Object configureSharedElementsUnoptimized(final ViewGroup viewGroup, final View view, final ArrayMap<String, String> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final ArrayList<View> list, final ArrayList<View> list2, final Object o, Object o2) {
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        Object sharedElementTransition;
        if (lastIn == null || firstOut == null) {
            sharedElementTransition = null;
        }
        else {
            final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
            if (arrayMap.isEmpty()) {
                sharedElementTransition = null;
            }
            else {
                sharedElementTransition = getSharedElementTransition(lastIn, firstOut, lastInIsPop);
            }
            final ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(arrayMap, sharedElementTransition, fragmentContainerTransition);
            if (arrayMap.isEmpty()) {
                sharedElementTransition = null;
            }
            else {
                list.addAll(captureOutSharedElements.values());
            }
            if (o == null && o2 == null && sharedElementTransition == null) {
                sharedElementTransition = null;
            }
            else {
                callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureOutSharedElements, true);
                if (sharedElementTransition != null) {
                    final Rect rect = new Rect();
                    FragmentTransitionCompat21.setSharedElementTargets(sharedElementTransition, view, list);
                    setOutEpicenter(sharedElementTransition, o2, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
                    o2 = rect;
                    if (o != null) {
                        FragmentTransitionCompat21.setEpicenter(o, rect);
                        o2 = rect;
                    }
                }
                else {
                    o2 = null;
                }
                OneShotPreDrawListener.add((View)viewGroup, new Runnable() {
                    @Override
                    public void run() {
                        final ArrayMap access$300 = captureInSharedElements(arrayMap, sharedElementTransition, fragmentContainerTransition);
                        if (access$300 != null) {
                            list2.addAll(access$300.values());
                            list2.add(view);
                        }
                        callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, access$300, false);
                        if (sharedElementTransition != null) {
                            FragmentTransitionCompat21.swapSharedElementTargets(sharedElementTransition, list, list2);
                            final View access$301 = getInEpicenterView(access$300, fragmentContainerTransition, o, lastInIsPop);
                            if (access$301 != null) {
                                FragmentTransitionCompat21.getBoundsOnScreen(access$301, o2);
                            }
                        }
                    }
                });
            }
        }
        return sharedElementTransition;
    }
    
    private static void configureTransitionsOptimized(final FragmentManagerImpl fragmentManagerImpl, final int n, final FragmentContainerTransition fragmentContainerTransition, final View view, final ArrayMap<String, String> arrayMap) {
        Object o = null;
        if (fragmentManagerImpl.mContainer.onHasView()) {
            o = fragmentManagerImpl.mContainer.onFindViewById(n);
        }
        if (o != null) {
            final Fragment lastIn = fragmentContainerTransition.lastIn;
            final Fragment firstOut = fragmentContainerTransition.firstOut;
            final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
            final boolean firstOutIsPop = fragmentContainerTransition.firstOutIsPop;
            final ArrayList<View> list = new ArrayList<View>();
            final ArrayList<View> list2 = new ArrayList<View>();
            final Object enterTransition = getEnterTransition(lastIn, lastInIsPop);
            final Object exitTransition = getExitTransition(firstOut, firstOutIsPop);
            final Object configureSharedElementsOptimized = configureSharedElementsOptimized((ViewGroup)o, view, arrayMap, fragmentContainerTransition, list2, list, enterTransition, exitTransition);
            if (enterTransition != null || configureSharedElementsOptimized != null || exitTransition != null) {
                final ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(exitTransition, firstOut, list2, view);
                final ArrayList<View> configureEnteringExitingViews2 = configureEnteringExitingViews(enterTransition, lastIn, list, view);
                setViewVisibility(configureEnteringExitingViews2, 4);
                final Object mergeTransitions = mergeTransitions(enterTransition, exitTransition, configureSharedElementsOptimized, lastIn, lastInIsPop);
                if (mergeTransitions != null) {
                    replaceHide(exitTransition, firstOut, configureEnteringExitingViews);
                    final ArrayList<String> prepareSetNameOverridesOptimized = FragmentTransitionCompat21.prepareSetNameOverridesOptimized(list);
                    FragmentTransitionCompat21.scheduleRemoveTargets(mergeTransitions, enterTransition, configureEnteringExitingViews2, exitTransition, configureEnteringExitingViews, configureSharedElementsOptimized, list);
                    FragmentTransitionCompat21.beginDelayedTransition((ViewGroup)o, mergeTransitions);
                    FragmentTransitionCompat21.setNameOverridesOptimized((View)o, list2, list, prepareSetNameOverridesOptimized, arrayMap);
                    setViewVisibility(configureEnteringExitingViews2, 0);
                    FragmentTransitionCompat21.swapSharedElementTargets(configureSharedElementsOptimized, list2, list);
                }
            }
        }
    }
    
    private static void configureTransitionsUnoptimized(final FragmentManagerImpl fragmentManagerImpl, final int n, final FragmentContainerTransition fragmentContainerTransition, final View view, final ArrayMap<String, String> arrayMap) {
        Object o = null;
        if (fragmentManagerImpl.mContainer.onHasView()) {
            o = fragmentManagerImpl.mContainer.onFindViewById(n);
        }
        if (o != null) {
            final Fragment lastIn = fragmentContainerTransition.lastIn;
            final Fragment firstOut = fragmentContainerTransition.firstOut;
            final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
            final boolean firstOutIsPop = fragmentContainerTransition.firstOutIsPop;
            final Object enterTransition = getEnterTransition(lastIn, lastInIsPop);
            Object exitTransition = getExitTransition(firstOut, firstOutIsPop);
            final ArrayList<View> list = new ArrayList<View>();
            final ArrayList<View> list2 = new ArrayList<View>();
            final Object configureSharedElementsUnoptimized = configureSharedElementsUnoptimized((ViewGroup)o, view, arrayMap, fragmentContainerTransition, list, list2, enterTransition, exitTransition);
            if (enterTransition != null || configureSharedElementsUnoptimized != null || exitTransition != null) {
                final ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(exitTransition, firstOut, list, view);
                if (configureEnteringExitingViews == null || configureEnteringExitingViews.isEmpty()) {
                    exitTransition = null;
                }
                FragmentTransitionCompat21.addTarget(enterTransition, view);
                final Object mergeTransitions = mergeTransitions(enterTransition, exitTransition, configureSharedElementsUnoptimized, lastIn, fragmentContainerTransition.lastInIsPop);
                if (mergeTransitions != null) {
                    final ArrayList<View> list3 = new ArrayList<View>();
                    FragmentTransitionCompat21.scheduleRemoveTargets(mergeTransitions, enterTransition, list3, exitTransition, configureEnteringExitingViews, configureSharedElementsUnoptimized, list2);
                    scheduleTargetChange((ViewGroup)o, lastIn, view, list2, enterTransition, list3, exitTransition, configureEnteringExitingViews);
                    FragmentTransitionCompat21.setNameOverridesUnoptimized((View)o, list2, arrayMap);
                    FragmentTransitionCompat21.beginDelayedTransition((ViewGroup)o, mergeTransitions);
                    FragmentTransitionCompat21.scheduleNameReset((ViewGroup)o, list2, arrayMap);
                }
            }
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
    
    private static Object getEnterTransition(final Fragment fragment, final boolean b) {
        Object cloneTransition;
        if (fragment == null) {
            cloneTransition = null;
        }
        else {
            Object o;
            if (b) {
                o = fragment.getReenterTransition();
            }
            else {
                o = fragment.getEnterTransition();
            }
            cloneTransition = FragmentTransitionCompat21.cloneTransition(o);
        }
        return cloneTransition;
    }
    
    private static Object getExitTransition(final Fragment fragment, final boolean b) {
        Object cloneTransition;
        if (fragment == null) {
            cloneTransition = null;
        }
        else {
            Object o;
            if (b) {
                o = fragment.getReturnTransition();
            }
            else {
                o = fragment.getExitTransition();
            }
            cloneTransition = FragmentTransitionCompat21.cloneTransition(o);
        }
        return cloneTransition;
    }
    
    private static View getInEpicenterView(final ArrayMap<String, View> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final Object o, final boolean b) {
        final BackStackRecord lastInTransaction = fragmentContainerTransition.lastInTransaction;
        View view;
        if (o != null && arrayMap != null && lastInTransaction.mSharedElementSourceNames != null && !lastInTransaction.mSharedElementSourceNames.isEmpty()) {
            String s;
            if (b) {
                s = lastInTransaction.mSharedElementSourceNames.get(0);
            }
            else {
                s = lastInTransaction.mSharedElementTargetNames.get(0);
            }
            view = arrayMap.get(s);
        }
        else {
            view = null;
        }
        return view;
    }
    
    private static Object getSharedElementTransition(final Fragment fragment, final Fragment fragment2, final boolean b) {
        Object wrapTransitionInSet;
        if (fragment == null || fragment2 == null) {
            wrapTransitionInSet = null;
        }
        else {
            Object o;
            if (b) {
                o = fragment2.getSharedElementReturnTransition();
            }
            else {
                o = fragment.getSharedElementEnterTransition();
            }
            wrapTransitionInSet = FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(o));
        }
        return wrapTransitionInSet;
    }
    
    private static Object mergeTransitions(Object o, final Object o2, final Object o3, final Fragment fragment, final boolean b) {
        int n2;
        final int n = n2 = 1;
        if (o != null) {
            n2 = n;
            if (o2 != null) {
                n2 = n;
                if (fragment != null) {
                    if (b) {
                        n2 = (fragment.getAllowReturnTransitionOverlap() ? 1 : 0);
                    }
                    else {
                        n2 = (fragment.getAllowEnterTransitionOverlap() ? 1 : 0);
                    }
                }
            }
        }
        if (n2 != 0) {
            o = FragmentTransitionCompat21.mergeTransitionsTogether(o2, o, o3);
        }
        else {
            o = FragmentTransitionCompat21.mergeTransitionsInSequence(o2, o, o3);
        }
        return o;
    }
    
    private static void replaceHide(final Object o, final Fragment fragment, final ArrayList<View> list) {
        if (fragment != null && o != null && fragment.mAdded && fragment.mHidden && fragment.mHiddenChanged) {
            fragment.setHideReplaced(true);
            FragmentTransitionCompat21.scheduleHideFragmentView(o, fragment.getView(), list);
            OneShotPreDrawListener.add((View)fragment.mContainer, new Runnable() {
                @Override
                public void run() {
                    setViewVisibility(list, 4);
                }
            });
        }
    }
    
    private static void retainValues(final ArrayMap<String, String> arrayMap, final ArrayMap<String, View> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            if (!arrayMap2.containsKey(arrayMap.valueAt(i))) {
                arrayMap.removeAt(i);
            }
        }
    }
    
    private static void scheduleTargetChange(final ViewGroup viewGroup, final Fragment fragment, final View view, final ArrayList<View> list, final Object o, final ArrayList<View> list2, final Object o2, final ArrayList<View> list3) {
        OneShotPreDrawListener.add((View)viewGroup, new Runnable() {
            @Override
            public void run() {
                if (o != null) {
                    FragmentTransitionCompat21.removeTarget(o, view);
                    list2.addAll(configureEnteringExitingViews(o, fragment, list, view));
                }
                if (list3 != null) {
                    if (o2 != null) {
                        final ArrayList<View> list = new ArrayList<View>();
                        list.add(view);
                        FragmentTransitionCompat21.replaceTargets(o2, list3, list);
                    }
                    list3.clear();
                    list3.add(view);
                }
            }
        });
    }
    
    private static void setOutEpicenter(final Object o, final Object o2, final ArrayMap<String, View> arrayMap, final boolean b, final BackStackRecord backStackRecord) {
        if (backStackRecord.mSharedElementSourceNames != null && !backStackRecord.mSharedElementSourceNames.isEmpty()) {
            String s;
            if (b) {
                s = backStackRecord.mSharedElementTargetNames.get(0);
            }
            else {
                s = backStackRecord.mSharedElementSourceNames.get(0);
            }
            final View view = arrayMap.get(s);
            FragmentTransitionCompat21.setEpicenter(o, view);
            if (o2 != null) {
                FragmentTransitionCompat21.setEpicenter(o2, view);
            }
        }
    }
    
    private static void setViewVisibility(final ArrayList<View> list, final int visibility) {
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                list.get(i).setVisibility(visibility);
            }
        }
    }
    
    static void startTransitions(final FragmentManagerImpl fragmentManagerImpl, final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final int n, final int n2, final boolean b) {
        if (fragmentManagerImpl.mCurState >= 1 && Build$VERSION.SDK_INT >= 21) {
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
                        configureTransitionsOptimized(fragmentManagerImpl, key, fragmentContainerTransition, view, calculateNameOverrides);
                    }
                    else {
                        configureTransitionsUnoptimized(fragmentManagerImpl, key, fragmentContainerTransition, view, calculateNameOverrides);
                    }
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
