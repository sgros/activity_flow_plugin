// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import java.util.Iterator;
import java.util.List;
import android.util.SparseIntArray;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;
import android.widget.ListView;
import android.support.v4.view.ViewCompat;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;
import android.animation.TimeInterpolator;
import java.util.ArrayList;
import android.animation.Animator;
import android.support.v4.util.ArrayMap;

public abstract class Transition implements Cloneable
{
    private static final int[] DEFAULT_MATCH_ORDER;
    private static final PathMotion STRAIGHT_PATH_MOTION;
    private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators;
    private ArrayList<Animator> mAnimators;
    boolean mCanRemoveViews;
    ArrayList<Animator> mCurrentAnimators;
    long mDuration;
    private TransitionValuesMaps mEndValues;
    private ArrayList<TransitionValues> mEndValuesList;
    private boolean mEnded;
    private EpicenterCallback mEpicenterCallback;
    private TimeInterpolator mInterpolator;
    private ArrayList<TransitionListener> mListeners;
    private int[] mMatchOrder;
    private String mName;
    private ArrayMap<String, String> mNameOverrides;
    private int mNumInstances;
    TransitionSet mParent;
    private PathMotion mPathMotion;
    private boolean mPaused;
    TransitionPropagation mPropagation;
    private ViewGroup mSceneRoot;
    private long mStartDelay;
    private TransitionValuesMaps mStartValues;
    private ArrayList<TransitionValues> mStartValuesList;
    private ArrayList<View> mTargetChildExcludes;
    private ArrayList<View> mTargetExcludes;
    private ArrayList<Integer> mTargetIdChildExcludes;
    private ArrayList<Integer> mTargetIdExcludes;
    ArrayList<Integer> mTargetIds;
    private ArrayList<String> mTargetNameExcludes;
    private ArrayList<String> mTargetNames;
    private ArrayList<Class> mTargetTypeChildExcludes;
    private ArrayList<Class> mTargetTypeExcludes;
    private ArrayList<Class> mTargetTypes;
    ArrayList<View> mTargets;
    
    static {
        DEFAULT_MATCH_ORDER = new int[] { 2, 1, 3, 4 };
        STRAIGHT_PATH_MOTION = new PathMotion() {
            @Override
            public Path getPath(final float n, final float n2, final float n3, final float n4) {
                final Path path = new Path();
                path.moveTo(n, n2);
                path.lineTo(n3, n4);
                return path;
            }
        };
        Transition.sRunningAnimators = new ThreadLocal<ArrayMap<Animator, AnimationInfo>>();
    }
    
    public Transition() {
        this.mName = this.getClass().getName();
        this.mStartDelay = -1L;
        this.mDuration = -1L;
        this.mInterpolator = null;
        this.mTargetIds = new ArrayList<Integer>();
        this.mTargets = new ArrayList<View>();
        this.mTargetNames = null;
        this.mTargetTypes = null;
        this.mTargetIdExcludes = null;
        this.mTargetExcludes = null;
        this.mTargetTypeExcludes = null;
        this.mTargetNameExcludes = null;
        this.mTargetIdChildExcludes = null;
        this.mTargetChildExcludes = null;
        this.mTargetTypeChildExcludes = null;
        this.mStartValues = new TransitionValuesMaps();
        this.mEndValues = new TransitionValuesMaps();
        this.mParent = null;
        this.mMatchOrder = Transition.DEFAULT_MATCH_ORDER;
        this.mSceneRoot = null;
        this.mCanRemoveViews = false;
        this.mCurrentAnimators = new ArrayList<Animator>();
        this.mNumInstances = 0;
        this.mPaused = false;
        this.mEnded = false;
        this.mListeners = null;
        this.mAnimators = new ArrayList<Animator>();
        this.mPathMotion = Transition.STRAIGHT_PATH_MOTION;
    }
    
    private void addUnmatched(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2) {
        final int n = 0;
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= arrayMap.size()) {
                break;
            }
            final TransitionValues e = arrayMap.valueAt(n2);
            if (this.isValidTarget(e.view)) {
                this.mStartValuesList.add(e);
                this.mEndValuesList.add(null);
            }
            ++n2;
        }
        while (i < arrayMap2.size()) {
            final TransitionValues e2 = arrayMap2.valueAt(i);
            if (this.isValidTarget(e2.view)) {
                this.mEndValuesList.add(e2);
                this.mStartValuesList.add(null);
            }
            ++i;
        }
    }
    
    private static void addViewValues(final TransitionValuesMaps transitionValuesMaps, View view, final TransitionValues transitionValues) {
        transitionValuesMaps.mViewValues.put(view, transitionValues);
        final int id = view.getId();
        if (id >= 0) {
            if (transitionValuesMaps.mIdValues.indexOfKey(id) >= 0) {
                transitionValuesMaps.mIdValues.put(id, (Object)null);
            }
            else {
                transitionValuesMaps.mIdValues.put(id, (Object)view);
            }
        }
        final String transitionName = ViewCompat.getTransitionName(view);
        if (transitionName != null) {
            if (transitionValuesMaps.mNameValues.containsKey(transitionName)) {
                transitionValuesMaps.mNameValues.put(transitionName, null);
            }
            else {
                transitionValuesMaps.mNameValues.put(transitionName, view);
            }
        }
        if (view.getParent() instanceof ListView) {
            final ListView listView = (ListView)view.getParent();
            if (listView.getAdapter().hasStableIds()) {
                final long itemIdAtPosition = listView.getItemIdAtPosition(listView.getPositionForView(view));
                if (transitionValuesMaps.mItemIdValues.indexOfKey(itemIdAtPosition) >= 0) {
                    view = transitionValuesMaps.mItemIdValues.get(itemIdAtPosition);
                    if (view != null) {
                        ViewCompat.setHasTransientState(view, false);
                        transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, null);
                    }
                }
                else {
                    ViewCompat.setHasTransientState(view, true);
                    transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, view);
                }
            }
        }
    }
    
    private void captureHierarchy(final View o, final boolean b) {
        if (o == null) {
            return;
        }
        final int id = o.getId();
        if (this.mTargetIdExcludes != null && this.mTargetIdExcludes.contains(id)) {
            return;
        }
        if (this.mTargetExcludes != null && this.mTargetExcludes.contains(o)) {
            return;
        }
        final ArrayList<Class> mTargetTypeExcludes = this.mTargetTypeExcludes;
        final int n = 0;
        if (mTargetTypeExcludes != null) {
            for (int size = this.mTargetTypeExcludes.size(), i = 0; i < size; ++i) {
                if (this.mTargetTypeExcludes.get(i).isInstance(o)) {
                    return;
                }
            }
        }
        if (o.getParent() instanceof ViewGroup) {
            final TransitionValues transitionValues = new TransitionValues();
            transitionValues.view = o;
            if (b) {
                this.captureStartValues(transitionValues);
            }
            else {
                this.captureEndValues(transitionValues);
            }
            transitionValues.mTargetedTransitions.add(this);
            this.capturePropagationValues(transitionValues);
            if (b) {
                addViewValues(this.mStartValues, o, transitionValues);
            }
            else {
                addViewValues(this.mEndValues, o, transitionValues);
            }
        }
        if (o instanceof ViewGroup) {
            if (this.mTargetIdChildExcludes != null && this.mTargetIdChildExcludes.contains(id)) {
                return;
            }
            if (this.mTargetChildExcludes != null && this.mTargetChildExcludes.contains(o)) {
                return;
            }
            if (this.mTargetTypeChildExcludes != null) {
                for (int size2 = this.mTargetTypeChildExcludes.size(), j = 0; j < size2; ++j) {
                    if (this.mTargetTypeChildExcludes.get(j).isInstance(o)) {
                        return;
                    }
                }
            }
            final ViewGroup viewGroup = (ViewGroup)o;
            for (int k = n; k < viewGroup.getChildCount(); ++k) {
                this.captureHierarchy(viewGroup.getChildAt(k), b);
            }
        }
    }
    
    private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        ArrayMap<Animator, AnimationInfo> value;
        if ((value = Transition.sRunningAnimators.get()) == null) {
            value = new ArrayMap<Animator, AnimationInfo>();
            Transition.sRunningAnimators.set(value);
        }
        return value;
    }
    
    private static boolean isValueChanged(final TransitionValues transitionValues, final TransitionValues transitionValues2, final String s) {
        final Object value = transitionValues.values.get(s);
        final Object value2 = transitionValues2.values.get(s);
        final boolean b = true;
        boolean b2;
        if (value == null && value2 == null) {
            b2 = false;
        }
        else {
            b2 = b;
            if (value != null) {
                if (value2 == null) {
                    b2 = b;
                }
                else {
                    b2 = (true ^ value.equals(value2));
                }
            }
        }
        return b2;
    }
    
    private void matchIds(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2, final SparseArray<View> sparseArray, final SparseArray<View> sparseArray2) {
        for (int size = sparseArray.size(), i = 0; i < size; ++i) {
            final View view = (View)sparseArray.valueAt(i);
            if (view != null && this.isValidTarget(view)) {
                final View view2 = (View)sparseArray2.get(sparseArray.keyAt(i));
                if (view2 != null && this.isValidTarget(view2)) {
                    final TransitionValues e = arrayMap.get(view);
                    final TransitionValues e2 = arrayMap2.get(view2);
                    if (e != null && e2 != null) {
                        this.mStartValuesList.add(e);
                        this.mEndValuesList.add(e2);
                        arrayMap.remove(view);
                        arrayMap2.remove(view2);
                    }
                }
            }
        }
    }
    
    private void matchInstances(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            final View view = arrayMap.keyAt(i);
            if (view != null && this.isValidTarget(view)) {
                final TransitionValues e = arrayMap2.remove(view);
                if (e != null && e.view != null && this.isValidTarget(e.view)) {
                    this.mStartValuesList.add(arrayMap.removeAt(i));
                    this.mEndValuesList.add(e);
                }
            }
        }
    }
    
    private void matchItemIds(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2, final LongSparseArray<View> longSparseArray, final LongSparseArray<View> longSparseArray2) {
        for (int size = longSparseArray.size(), i = 0; i < size; ++i) {
            final View view = longSparseArray.valueAt(i);
            if (view != null && this.isValidTarget(view)) {
                final View view2 = longSparseArray2.get(longSparseArray.keyAt(i));
                if (view2 != null && this.isValidTarget(view2)) {
                    final TransitionValues e = arrayMap.get(view);
                    final TransitionValues e2 = arrayMap2.get(view2);
                    if (e != null && e2 != null) {
                        this.mStartValuesList.add(e);
                        this.mEndValuesList.add(e2);
                        arrayMap.remove(view);
                        arrayMap2.remove(view2);
                    }
                }
            }
        }
    }
    
    private void matchNames(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2, final ArrayMap<String, View> arrayMap3, final ArrayMap<String, View> arrayMap4) {
        for (int size = arrayMap3.size(), i = 0; i < size; ++i) {
            final View view = arrayMap3.valueAt(i);
            if (view != null && this.isValidTarget(view)) {
                final View view2 = arrayMap4.get(arrayMap3.keyAt(i));
                if (view2 != null && this.isValidTarget(view2)) {
                    final TransitionValues e = arrayMap.get(view);
                    final TransitionValues e2 = arrayMap2.get(view2);
                    if (e != null && e2 != null) {
                        this.mStartValuesList.add(e);
                        this.mEndValuesList.add(e2);
                        arrayMap.remove(view);
                        arrayMap2.remove(view2);
                    }
                }
            }
        }
    }
    
    private void matchStartAndEnd(final TransitionValuesMaps transitionValuesMaps, final TransitionValuesMaps transitionValuesMaps2) {
        final ArrayMap<View, TransitionValues> arrayMap = new ArrayMap<View, TransitionValues>(transitionValuesMaps.mViewValues);
        final ArrayMap<View, TransitionValues> arrayMap2 = new ArrayMap<View, TransitionValues>(transitionValuesMaps2.mViewValues);
        for (int i = 0; i < this.mMatchOrder.length; ++i) {
            switch (this.mMatchOrder[i]) {
                case 4: {
                    this.matchItemIds(arrayMap, arrayMap2, transitionValuesMaps.mItemIdValues, transitionValuesMaps2.mItemIdValues);
                    break;
                }
                case 3: {
                    this.matchIds(arrayMap, arrayMap2, transitionValuesMaps.mIdValues, transitionValuesMaps2.mIdValues);
                    break;
                }
                case 2: {
                    this.matchNames(arrayMap, arrayMap2, transitionValuesMaps.mNameValues, transitionValuesMaps2.mNameValues);
                    break;
                }
                case 1: {
                    this.matchInstances(arrayMap, arrayMap2);
                    break;
                }
            }
        }
        this.addUnmatched(arrayMap, arrayMap2);
    }
    
    private void runAnimator(final Animator animator, final ArrayMap<Animator, AnimationInfo> arrayMap) {
        if (animator != null) {
            animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator o) {
                    arrayMap.remove(o);
                    Transition.this.mCurrentAnimators.remove(o);
                }
                
                public void onAnimationStart(final Animator e) {
                    Transition.this.mCurrentAnimators.add(e);
                }
            });
            this.animate(animator);
        }
    }
    
    public Transition addListener(final TransitionListener e) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<TransitionListener>();
        }
        this.mListeners.add(e);
        return this;
    }
    
    public Transition addTarget(final View e) {
        this.mTargets.add(e);
        return this;
    }
    
    protected void animate(final Animator animator) {
        if (animator == null) {
            this.end();
        }
        else {
            if (this.getDuration() >= 0L) {
                animator.setDuration(this.getDuration());
            }
            if (this.getStartDelay() >= 0L) {
                animator.setStartDelay(this.getStartDelay());
            }
            if (this.getInterpolator() != null) {
                animator.setInterpolator(this.getInterpolator());
            }
            animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    Transition.this.end();
                    animator.removeListener((Animator$AnimatorListener)this);
                }
            });
            animator.start();
        }
    }
    
    public abstract void captureEndValues(final TransitionValues p0);
    
    void capturePropagationValues(final TransitionValues transitionValues) {
        if (this.mPropagation != null && !transitionValues.values.isEmpty()) {
            final String[] propagationProperties = this.mPropagation.getPropagationProperties();
            if (propagationProperties == null) {
                return;
            }
            final int n = 0;
            int i = 0;
            while (true) {
                while (i < propagationProperties.length) {
                    if (!transitionValues.values.containsKey(propagationProperties[i])) {
                        final int n2 = n;
                        if (n2 == 0) {
                            this.mPropagation.captureValues(transitionValues);
                        }
                        return;
                    }
                    else {
                        ++i;
                    }
                }
                final int n2 = 1;
                continue;
            }
        }
    }
    
    public abstract void captureStartValues(final TransitionValues p0);
    
    void captureValues(final ViewGroup viewGroup, final boolean b) {
        this.clearValues(b);
        final int size = this.mTargetIds.size();
        final int n = 0;
        if ((size > 0 || this.mTargets.size() > 0) && (this.mTargetNames == null || this.mTargetNames.isEmpty()) && (this.mTargetTypes == null || this.mTargetTypes.isEmpty())) {
            for (int i = 0; i < this.mTargetIds.size(); ++i) {
                final View viewById = viewGroup.findViewById((int)this.mTargetIds.get(i));
                if (viewById != null) {
                    final TransitionValues transitionValues = new TransitionValues();
                    transitionValues.view = viewById;
                    if (b) {
                        this.captureStartValues(transitionValues);
                    }
                    else {
                        this.captureEndValues(transitionValues);
                    }
                    transitionValues.mTargetedTransitions.add(this);
                    this.capturePropagationValues(transitionValues);
                    if (b) {
                        addViewValues(this.mStartValues, viewById, transitionValues);
                    }
                    else {
                        addViewValues(this.mEndValues, viewById, transitionValues);
                    }
                }
            }
            for (int j = 0; j < this.mTargets.size(); ++j) {
                final View view = this.mTargets.get(j);
                final TransitionValues transitionValues2 = new TransitionValues();
                transitionValues2.view = view;
                if (b) {
                    this.captureStartValues(transitionValues2);
                }
                else {
                    this.captureEndValues(transitionValues2);
                }
                transitionValues2.mTargetedTransitions.add(this);
                this.capturePropagationValues(transitionValues2);
                if (b) {
                    addViewValues(this.mStartValues, view, transitionValues2);
                }
                else {
                    addViewValues(this.mEndValues, view, transitionValues2);
                }
            }
        }
        else {
            this.captureHierarchy((View)viewGroup, b);
        }
        if (!b && this.mNameOverrides != null) {
            final int size2 = this.mNameOverrides.size();
            final ArrayList list = new ArrayList<View>(size2);
            int n2 = 0;
            int k;
            while (true) {
                k = n;
                if (n2 >= size2) {
                    break;
                }
                list.add(this.mStartValues.mNameValues.remove(this.mNameOverrides.keyAt(n2)));
                ++n2;
            }
            while (k < size2) {
                final View view2 = list.get(k);
                if (view2 != null) {
                    this.mStartValues.mNameValues.put(this.mNameOverrides.valueAt(k), view2);
                }
                ++k;
            }
        }
    }
    
    void clearValues(final boolean b) {
        if (b) {
            this.mStartValues.mViewValues.clear();
            this.mStartValues.mIdValues.clear();
            this.mStartValues.mItemIdValues.clear();
        }
        else {
            this.mEndValues.mViewValues.clear();
            this.mEndValues.mIdValues.clear();
            this.mEndValues.mItemIdValues.clear();
        }
    }
    
    public Transition clone() {
        try {
            final Transition transition = (Transition)super.clone();
            transition.mAnimators = new ArrayList<Animator>();
            transition.mStartValues = new TransitionValuesMaps();
            transition.mEndValues = new TransitionValuesMaps();
            transition.mStartValuesList = null;
            transition.mEndValuesList = null;
            return transition;
        }
        catch (CloneNotSupportedException ex) {
            return null;
        }
    }
    
    public Animator createAnimator(final ViewGroup viewGroup, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        return null;
    }
    
    protected void createAnimators(final ViewGroup viewGroup, final TransitionValuesMaps transitionValuesMaps, final TransitionValuesMaps transitionValuesMaps2, final ArrayList<TransitionValues> list, final ArrayList<TransitionValues> list2) {
        final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        final SparseIntArray sparseIntArray = new SparseIntArray();
        final int size = list.size();
        long b = Long.MAX_VALUE;
        long min;
        int n3;
        for (int i = 0; i < size; i = n3 + 1, b = min) {
            final TransitionValues transitionValues = list.get(i);
            final TransitionValues transitionValues2 = list2.get(i);
            TransitionValues transitionValues3;
            if ((transitionValues3 = transitionValues) != null) {
                transitionValues3 = transitionValues;
                if (!transitionValues.mTargetedTransitions.contains(this)) {
                    transitionValues3 = null;
                }
            }
            TransitionValues transitionValues4;
            if ((transitionValues4 = transitionValues2) != null) {
                transitionValues4 = transitionValues2;
                if (!transitionValues2.mTargetedTransitions.contains(this)) {
                    transitionValues4 = null;
                }
            }
            if ((transitionValues3 != null || transitionValues4 != null) && (transitionValues3 == null || transitionValues4 == null || this.isTransitionRequired(transitionValues3, transitionValues4))) {
                Animator animator = this.createAnimator(viewGroup, transitionValues3, transitionValues4);
                if (animator != null) {
                    TransitionValues transitionValues8;
                    View view2;
                    if (transitionValues4 != null) {
                        final View view = transitionValues4.view;
                        final String[] transitionProperties = this.getTransitionProperties();
                        TransitionValues transitionValues7 = null;
                        Label_0421: {
                            if (view != null && transitionProperties != null && transitionProperties.length > 0) {
                                final TransitionValues transitionValues5 = new TransitionValues();
                                transitionValues5.view = view;
                                final TransitionValues transitionValues6 = transitionValuesMaps2.mViewValues.get(view);
                                int n = i;
                                if (transitionValues6 != null) {
                                    int n2 = 0;
                                    while (true) {
                                        n = i;
                                        if (n2 >= transitionProperties.length) {
                                            break;
                                        }
                                        transitionValues5.values.put(transitionProperties[n2], transitionValues6.values.get(transitionProperties[n2]));
                                        ++n2;
                                    }
                                }
                                i = n;
                                for (int size2 = runningAnimators.size(), j = 0; j < size2; ++j) {
                                    final AnimationInfo animationInfo = runningAnimators.get(runningAnimators.keyAt(j));
                                    if (animationInfo.mValues != null && animationInfo.mView == view && animationInfo.mName.equals(this.getName()) && animationInfo.mValues.equals(transitionValues5)) {
                                        animator = null;
                                        transitionValues7 = transitionValues5;
                                        break Label_0421;
                                    }
                                }
                                transitionValues7 = transitionValues5;
                            }
                            else {
                                transitionValues7 = null;
                            }
                        }
                        transitionValues8 = transitionValues7;
                        view2 = view;
                    }
                    else {
                        view2 = transitionValues3.view;
                        transitionValues8 = null;
                    }
                    min = b;
                    n3 = i;
                    if (animator != null) {
                        min = b;
                        if (this.mPropagation != null) {
                            final long startDelay = this.mPropagation.getStartDelay(viewGroup, this, transitionValues3, transitionValues4);
                            sparseIntArray.put(this.mAnimators.size(), (int)startDelay);
                            min = Math.min(startDelay, b);
                        }
                        runningAnimators.put(animator, new AnimationInfo(view2, this.getName(), this, ViewUtils.getWindowId((View)viewGroup), transitionValues8));
                        this.mAnimators.add(animator);
                        n3 = i;
                    }
                    continue;
                }
            }
            min = b;
            n3 = i;
        }
        if (b != 0L) {
            for (int k = 0; k < sparseIntArray.size(); ++k) {
                final Animator animator2 = this.mAnimators.get(sparseIntArray.keyAt(k));
                animator2.setStartDelay(sparseIntArray.valueAt(k) - b + animator2.getStartDelay());
            }
        }
    }
    
    protected void end() {
        --this.mNumInstances;
        if (this.mNumInstances == 0) {
            if (this.mListeners != null && this.mListeners.size() > 0) {
                final ArrayList list = (ArrayList)this.mListeners.clone();
                for (int size = list.size(), i = 0; i < size; ++i) {
                    list.get(i).onTransitionEnd(this);
                }
            }
            for (int j = 0; j < this.mStartValues.mItemIdValues.size(); ++j) {
                final View view = this.mStartValues.mItemIdValues.valueAt(j);
                if (view != null) {
                    ViewCompat.setHasTransientState(view, false);
                }
            }
            for (int k = 0; k < this.mEndValues.mItemIdValues.size(); ++k) {
                final View view2 = this.mEndValues.mItemIdValues.valueAt(k);
                if (view2 != null) {
                    ViewCompat.setHasTransientState(view2, false);
                }
            }
            this.mEnded = true;
        }
    }
    
    public long getDuration() {
        return this.mDuration;
    }
    
    public EpicenterCallback getEpicenterCallback() {
        return this.mEpicenterCallback;
    }
    
    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }
    
    TransitionValues getMatchedTransitionValues(final View view, final boolean b) {
        if (this.mParent != null) {
            return this.mParent.getMatchedTransitionValues(view, b);
        }
        ArrayList<TransitionValues> list;
        if (b) {
            list = this.mStartValuesList;
        }
        else {
            list = this.mEndValuesList;
        }
        final TransitionValues transitionValues = null;
        if (list == null) {
            return null;
        }
        final int size = list.size();
        final int n = -1;
        int index = 0;
        int index2;
        while (true) {
            index2 = n;
            if (index >= size) {
                break;
            }
            final TransitionValues transitionValues2 = list.get(index);
            if (transitionValues2 == null) {
                return null;
            }
            if (transitionValues2.view == view) {
                index2 = index;
                break;
            }
            ++index;
        }
        TransitionValues transitionValues3 = transitionValues;
        if (index2 >= 0) {
            ArrayList<TransitionValues> list2;
            if (b) {
                list2 = this.mEndValuesList;
            }
            else {
                list2 = this.mStartValuesList;
            }
            transitionValues3 = list2.get(index2);
        }
        return transitionValues3;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public PathMotion getPathMotion() {
        return this.mPathMotion;
    }
    
    public TransitionPropagation getPropagation() {
        return this.mPropagation;
    }
    
    public long getStartDelay() {
        return this.mStartDelay;
    }
    
    public List<Integer> getTargetIds() {
        return this.mTargetIds;
    }
    
    public List<String> getTargetNames() {
        return this.mTargetNames;
    }
    
    public List<Class> getTargetTypes() {
        return this.mTargetTypes;
    }
    
    public List<View> getTargets() {
        return this.mTargets;
    }
    
    public String[] getTransitionProperties() {
        return null;
    }
    
    public TransitionValues getTransitionValues(final View view, final boolean b) {
        if (this.mParent != null) {
            return this.mParent.getTransitionValues(view, b);
        }
        TransitionValuesMaps transitionValuesMaps;
        if (b) {
            transitionValuesMaps = this.mStartValues;
        }
        else {
            transitionValuesMaps = this.mEndValues;
        }
        return (TransitionValues)transitionValuesMaps.mViewValues.get(view);
    }
    
    public boolean isTransitionRequired(final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        boolean b2;
        final boolean b = b2 = false;
        if (transitionValues != null) {
            b2 = b;
            if (transitionValues2 != null) {
                final String[] transitionProperties = this.getTransitionProperties();
                if (transitionProperties != null) {
                    final int length = transitionProperties.length;
                    int n = 0;
                    while (true) {
                        b2 = b;
                        if (n >= length) {
                            return b2;
                        }
                        if (isValueChanged(transitionValues, transitionValues2, transitionProperties[n])) {
                            break;
                        }
                        ++n;
                    }
                }
                else {
                    final Iterator<String> iterator = transitionValues.values.keySet().iterator();
                    do {
                        b2 = b;
                        if (iterator.hasNext()) {
                            continue;
                        }
                        return b2;
                    } while (!isValueChanged(transitionValues, transitionValues2, iterator.next()));
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    boolean isValidTarget(final View view) {
        final int id = view.getId();
        if (this.mTargetIdExcludes != null && this.mTargetIdExcludes.contains(id)) {
            return false;
        }
        if (this.mTargetExcludes != null && this.mTargetExcludes.contains(view)) {
            return false;
        }
        if (this.mTargetTypeExcludes != null) {
            for (int size = this.mTargetTypeExcludes.size(), i = 0; i < size; ++i) {
                if (this.mTargetTypeExcludes.get(i).isInstance(view)) {
                    return false;
                }
            }
        }
        if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(view) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(view))) {
            return false;
        }
        if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0 && (this.mTargetTypes == null || this.mTargetTypes.isEmpty()) && (this.mTargetNames == null || this.mTargetNames.isEmpty())) {
            return true;
        }
        if (this.mTargetIds.contains(id) || this.mTargets.contains(view)) {
            return true;
        }
        if (this.mTargetNames != null && this.mTargetNames.contains(ViewCompat.getTransitionName(view))) {
            return true;
        }
        if (this.mTargetTypes != null) {
            for (int j = 0; j < this.mTargetTypes.size(); ++j) {
                if (this.mTargetTypes.get(j).isInstance(view)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void pause(final View view) {
        if (!this.mEnded) {
            final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
            int i = runningAnimators.size();
            final WindowIdImpl windowId = ViewUtils.getWindowId(view);
            --i;
            while (i >= 0) {
                final AnimationInfo animationInfo = runningAnimators.valueAt(i);
                if (animationInfo.mView != null && windowId.equals(animationInfo.mWindowId)) {
                    AnimatorUtils.pause(runningAnimators.keyAt(i));
                }
                --i;
            }
            if (this.mListeners != null && this.mListeners.size() > 0) {
                final ArrayList list = (ArrayList)this.mListeners.clone();
                for (int size = list.size(), j = 0; j < size; ++j) {
                    list.get(j).onTransitionPause(this);
                }
            }
            this.mPaused = true;
        }
    }
    
    void playTransition(final ViewGroup viewGroup) {
        this.mStartValuesList = new ArrayList<TransitionValues>();
        this.mEndValuesList = new ArrayList<TransitionValues>();
        this.matchStartAndEnd(this.mStartValues, this.mEndValues);
        final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        int i = runningAnimators.size();
        final WindowIdImpl windowId = ViewUtils.getWindowId((View)viewGroup);
        --i;
        while (i >= 0) {
            final Animator animator = runningAnimators.keyAt(i);
            if (animator != null) {
                final AnimationInfo animationInfo = runningAnimators.get(animator);
                if (animationInfo != null && animationInfo.mView != null && windowId.equals(animationInfo.mWindowId)) {
                    final TransitionValues mValues = animationInfo.mValues;
                    final View mView = animationInfo.mView;
                    final TransitionValues transitionValues = this.getTransitionValues(mView, true);
                    final TransitionValues matchedTransitionValues = this.getMatchedTransitionValues(mView, true);
                    if ((transitionValues != null || matchedTransitionValues != null) && animationInfo.mTransition.isTransitionRequired(mValues, matchedTransitionValues)) {
                        if (!animator.isRunning() && !animator.isStarted()) {
                            runningAnimators.remove(animator);
                        }
                        else {
                            animator.cancel();
                        }
                    }
                }
            }
            --i;
        }
        this.createAnimators(viewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
        this.runAnimators();
    }
    
    public Transition removeListener(final TransitionListener o) {
        if (this.mListeners == null) {
            return this;
        }
        this.mListeners.remove(o);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
        }
        return this;
    }
    
    public Transition removeTarget(final View o) {
        this.mTargets.remove(o);
        return this;
    }
    
    public void resume(final View view) {
        if (this.mPaused) {
            if (!this.mEnded) {
                final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
                int i = runningAnimators.size();
                final WindowIdImpl windowId = ViewUtils.getWindowId(view);
                --i;
                while (i >= 0) {
                    final AnimationInfo animationInfo = runningAnimators.valueAt(i);
                    if (animationInfo.mView != null && windowId.equals(animationInfo.mWindowId)) {
                        AnimatorUtils.resume(runningAnimators.keyAt(i));
                    }
                    --i;
                }
                if (this.mListeners != null && this.mListeners.size() > 0) {
                    final ArrayList list = (ArrayList)this.mListeners.clone();
                    for (int size = list.size(), j = 0; j < size; ++j) {
                        list.get(j).onTransitionResume(this);
                    }
                }
            }
            this.mPaused = false;
        }
    }
    
    protected void runAnimators() {
        this.start();
        final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        for (final Animator animator : this.mAnimators) {
            if (runningAnimators.containsKey(animator)) {
                this.start();
                this.runAnimator(animator, runningAnimators);
            }
        }
        this.mAnimators.clear();
        this.end();
    }
    
    public Transition setDuration(final long mDuration) {
        this.mDuration = mDuration;
        return this;
    }
    
    public void setEpicenterCallback(final EpicenterCallback mEpicenterCallback) {
        this.mEpicenterCallback = mEpicenterCallback;
    }
    
    public Transition setInterpolator(final TimeInterpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
        return this;
    }
    
    public void setPathMotion(final PathMotion mPathMotion) {
        if (mPathMotion == null) {
            this.mPathMotion = Transition.STRAIGHT_PATH_MOTION;
        }
        else {
            this.mPathMotion = mPathMotion;
        }
    }
    
    public void setPropagation(final TransitionPropagation mPropagation) {
        this.mPropagation = mPropagation;
    }
    
    public Transition setStartDelay(final long mStartDelay) {
        this.mStartDelay = mStartDelay;
        return this;
    }
    
    protected void start() {
        if (this.mNumInstances == 0) {
            if (this.mListeners != null && this.mListeners.size() > 0) {
                final ArrayList list = (ArrayList)this.mListeners.clone();
                for (int size = list.size(), i = 0; i < size; ++i) {
                    list.get(i).onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        ++this.mNumInstances;
    }
    
    @Override
    public String toString() {
        return this.toString("");
    }
    
    String toString(String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(this.getClass().getSimpleName());
        sb.append("@");
        sb.append(Integer.toHexString(this.hashCode()));
        sb.append(": ");
        String str2;
        str = (str2 = sb.toString());
        if (this.mDuration != -1L) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("dur(");
            sb2.append(this.mDuration);
            sb2.append(") ");
            str2 = sb2.toString();
        }
        str = str2;
        if (this.mStartDelay != -1L) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append("dly(");
            sb3.append(this.mStartDelay);
            sb3.append(") ");
            str = sb3.toString();
        }
        String string = str;
        if (this.mInterpolator != null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append("interp(");
            sb4.append(this.mInterpolator);
            sb4.append(") ");
            string = sb4.toString();
        }
        if (this.mTargetIds.size() <= 0) {
            str = string;
            if (this.mTargets.size() <= 0) {
                return str;
            }
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(string);
        sb5.append("tgts(");
        final String string2 = sb5.toString();
        final int size = this.mTargetIds.size();
        final int n = 0;
        str = string2;
        if (size > 0) {
            str = string2;
            for (int i = 0; i < this.mTargetIds.size(); ++i) {
                String string3 = str;
                if (i > 0) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(str);
                    sb6.append(", ");
                    string3 = sb6.toString();
                }
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(string3);
                sb7.append(this.mTargetIds.get(i));
                str = sb7.toString();
            }
        }
        String str3 = str;
        if (this.mTargets.size() > 0) {
            int index = n;
            while (true) {
                str3 = str;
                if (index >= this.mTargets.size()) {
                    break;
                }
                String string4 = str;
                if (index > 0) {
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append(str);
                    sb8.append(", ");
                    string4 = sb8.toString();
                }
                final StringBuilder sb9 = new StringBuilder();
                sb9.append(string4);
                sb9.append(this.mTargets.get(index));
                str = sb9.toString();
                ++index;
            }
        }
        final StringBuilder sb10 = new StringBuilder();
        sb10.append(str3);
        sb10.append(")");
        str = sb10.toString();
        return str;
    }
    
    private static class AnimationInfo
    {
        String mName;
        Transition mTransition;
        TransitionValues mValues;
        View mView;
        WindowIdImpl mWindowId;
        
        AnimationInfo(final View mView, final String mName, final Transition mTransition, final WindowIdImpl mWindowId, final TransitionValues mValues) {
            this.mView = mView;
            this.mName = mName;
            this.mValues = mValues;
            this.mWindowId = mWindowId;
            this.mTransition = mTransition;
        }
    }
    
    public abstract static class EpicenterCallback
    {
    }
    
    public interface TransitionListener
    {
        void onTransitionEnd(final Transition p0);
        
        void onTransitionPause(final Transition p0);
        
        void onTransitionResume(final Transition p0);
        
        void onTransitionStart(final Transition p0);
    }
}
