// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import java.util.Collection;
import java.util.Iterator;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.view.View$OnAttachStateChangeListener;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.support.v4.util.ArrayMap;
import java.lang.ref.WeakReference;
import android.view.ViewGroup;
import java.util.ArrayList;

public class TransitionManager
{
    private static Transition sDefaultTransition;
    static ArrayList<ViewGroup> sPendingTransitions;
    private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions;
    
    static {
        TransitionManager.sDefaultTransition = new AutoTransition();
        TransitionManager.sRunningTransitions = new ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>>();
        TransitionManager.sPendingTransitions = new ArrayList<ViewGroup>();
    }
    
    public static void beginDelayedTransition(final ViewGroup viewGroup, Transition clone) {
        if (!TransitionManager.sPendingTransitions.contains(viewGroup) && ViewCompat.isLaidOut((View)viewGroup)) {
            TransitionManager.sPendingTransitions.add(viewGroup);
            Transition sDefaultTransition;
            if ((sDefaultTransition = clone) == null) {
                sDefaultTransition = TransitionManager.sDefaultTransition;
            }
            clone = sDefaultTransition.clone();
            sceneChangeSetup(viewGroup, clone);
            Scene.setCurrentScene((View)viewGroup, null);
            sceneChangeRunTransition(viewGroup, clone);
        }
    }
    
    static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions() {
        final WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>> weakReference = TransitionManager.sRunningTransitions.get();
        if (weakReference != null) {
            final ArrayMap<ViewGroup, ArrayList<Transition>> arrayMap = weakReference.get();
            if (arrayMap != null) {
                return arrayMap;
            }
        }
        final ArrayMap<ViewGroup, ArrayList<Transition>> referent = new ArrayMap<ViewGroup, ArrayList<Transition>>();
        TransitionManager.sRunningTransitions.set(new WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>(referent));
        return referent;
    }
    
    private static void sceneChangeRunTransition(final ViewGroup viewGroup, final Transition transition) {
        if (transition != null && viewGroup != null) {
            final MultiListener multiListener = new MultiListener(transition, viewGroup);
            viewGroup.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)multiListener);
            viewGroup.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)multiListener);
        }
    }
    
    private static void sceneChangeSetup(final ViewGroup viewGroup, final Transition transition) {
        final ArrayList<Transition> list = getRunningTransitions().get(viewGroup);
        if (list != null && list.size() > 0) {
            final Iterator<Transition> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next().pause((View)viewGroup);
            }
        }
        if (transition != null) {
            transition.captureValues(viewGroup, true);
        }
        final Scene currentScene = Scene.getCurrentScene((View)viewGroup);
        if (currentScene != null) {
            currentScene.exit();
        }
    }
    
    private static class MultiListener implements View$OnAttachStateChangeListener, ViewTreeObserver$OnPreDrawListener
    {
        ViewGroup mSceneRoot;
        Transition mTransition;
        
        MultiListener(final Transition mTransition, final ViewGroup mSceneRoot) {
            this.mTransition = mTransition;
            this.mSceneRoot = mSceneRoot;
        }
        
        private void removeListeners() {
            this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
            this.mSceneRoot.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
        }
        
        public boolean onPreDraw() {
            this.removeListeners();
            if (!TransitionManager.sPendingTransitions.remove(this.mSceneRoot)) {
                return true;
            }
            final ArrayMap<ViewGroup, ArrayList<Transition>> runningTransitions = TransitionManager.getRunningTransitions();
            final ArrayList<Transition> c = runningTransitions.get(this.mSceneRoot);
            ArrayList<Transition> list = null;
            ArrayList<Transition> list2;
            if (c == null) {
                list2 = new ArrayList<Transition>();
                runningTransitions.put(this.mSceneRoot, list2);
            }
            else {
                list2 = c;
                if (c.size() > 0) {
                    list = new ArrayList<Transition>(c);
                    list2 = c;
                }
            }
            list2.add(this.mTransition);
            this.mTransition.addListener((Transition.TransitionListener)new TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(final Transition o) {
                    ((ArrayList)runningTransitions.get(MultiListener.this.mSceneRoot)).remove(o);
                }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (list != null) {
                final Iterator<Transition> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.playTransition(this.mSceneRoot);
            return true;
        }
        
        public void onViewAttachedToWindow(final View view) {
        }
        
        public void onViewDetachedFromWindow(final View view) {
            this.removeListeners();
            TransitionManager.sPendingTransitions.remove(this.mSceneRoot);
            final ArrayList<Transition> list = TransitionManager.getRunningTransitions().get(this.mSceneRoot);
            if (list != null && list.size() > 0) {
                final Iterator<Transition> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }
    }
}
