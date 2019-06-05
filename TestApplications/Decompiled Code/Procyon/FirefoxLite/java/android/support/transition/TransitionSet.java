// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.util.AndroidRuntimeException;
import android.animation.TimeInterpolator;
import android.view.ViewGroup;
import android.view.View;
import java.util.Iterator;
import java.util.ArrayList;

public class TransitionSet extends Transition
{
    private int mChangeFlags;
    int mCurrentListeners;
    private boolean mPlayTogether;
    boolean mStarted;
    private ArrayList<Transition> mTransitions;
    
    public TransitionSet() {
        this.mTransitions = new ArrayList<Transition>();
        this.mPlayTogether = true;
        this.mStarted = false;
        this.mChangeFlags = 0;
    }
    
    private void setupStartEndListeners() {
        final TransitionSetListener transitionSetListener = new TransitionSetListener(this);
        final Iterator<Transition> iterator = this.mTransitions.iterator();
        while (iterator.hasNext()) {
            iterator.next().addListener((TransitionListener)transitionSetListener);
        }
        this.mCurrentListeners = this.mTransitions.size();
    }
    
    @Override
    public TransitionSet addListener(final TransitionListener transitionListener) {
        return (TransitionSet)super.addListener(transitionListener);
    }
    
    @Override
    public TransitionSet addTarget(final View view) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(view);
        }
        return (TransitionSet)super.addTarget(view);
    }
    
    public TransitionSet addTransition(final Transition e) {
        this.mTransitions.add(e);
        e.mParent = this;
        if (this.mDuration >= 0L) {
            e.setDuration(this.mDuration);
        }
        if ((this.mChangeFlags & 0x1) != 0x0) {
            e.setInterpolator(this.getInterpolator());
        }
        if ((this.mChangeFlags & 0x2) != 0x0) {
            e.setPropagation(this.getPropagation());
        }
        if ((this.mChangeFlags & 0x4) != 0x0) {
            e.setPathMotion(this.getPathMotion());
        }
        if ((this.mChangeFlags & 0x8) != 0x0) {
            e.setEpicenterCallback(this.getEpicenterCallback());
        }
        return this;
    }
    
    @Override
    public void captureEndValues(final TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (final Transition e : this.mTransitions) {
                if (e.isValidTarget(transitionValues.view)) {
                    e.captureEndValues(transitionValues);
                    transitionValues.mTargetedTransitions.add(e);
                }
            }
        }
    }
    
    @Override
    void capturePropagationValues(final TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).capturePropagationValues(transitionValues);
        }
    }
    
    @Override
    public void captureStartValues(final TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (final Transition e : this.mTransitions) {
                if (e.isValidTarget(transitionValues.view)) {
                    e.captureStartValues(transitionValues);
                    transitionValues.mTargetedTransitions.add(e);
                }
            }
        }
    }
    
    @Override
    public Transition clone() {
        final TransitionSet set = (TransitionSet)super.clone();
        set.mTransitions = new ArrayList<Transition>();
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            set.addTransition(this.mTransitions.get(i).clone());
        }
        return set;
    }
    
    @Override
    protected void createAnimators(final ViewGroup viewGroup, final TransitionValuesMaps transitionValuesMaps, final TransitionValuesMaps transitionValuesMaps2, final ArrayList<TransitionValues> list, final ArrayList<TransitionValues> list2) {
        final long startDelay = this.getStartDelay();
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            final Transition transition = this.mTransitions.get(i);
            if (startDelay > 0L && (this.mPlayTogether || i == 0)) {
                final long startDelay2 = transition.getStartDelay();
                if (startDelay2 > 0L) {
                    transition.setStartDelay(startDelay2 + startDelay);
                }
                else {
                    transition.setStartDelay(startDelay);
                }
            }
            transition.createAnimators(viewGroup, transitionValuesMaps, transitionValuesMaps2, list, list2);
        }
    }
    
    public Transition getTransitionAt(final int index) {
        if (index >= 0 && index < this.mTransitions.size()) {
            return this.mTransitions.get(index);
        }
        return null;
    }
    
    public int getTransitionCount() {
        return this.mTransitions.size();
    }
    
    @Override
    public void pause(final View view) {
        super.pause(view);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).pause(view);
        }
    }
    
    @Override
    public TransitionSet removeListener(final TransitionListener transitionListener) {
        return (TransitionSet)super.removeListener(transitionListener);
    }
    
    @Override
    public TransitionSet removeTarget(final View view) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(view);
        }
        return (TransitionSet)super.removeTarget(view);
    }
    
    @Override
    public void resume(final View view) {
        super.resume(view);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).resume(view);
        }
    }
    
    @Override
    protected void runAnimators() {
        if (this.mTransitions.isEmpty()) {
            this.start();
            this.end();
            return;
        }
        this.setupStartEndListeners();
        if (!this.mPlayTogether) {
            for (int i = 1; i < this.mTransitions.size(); ++i) {
                this.mTransitions.get(i - 1).addListener((TransitionListener)new TransitionListenerAdapter() {
                    final /* synthetic */ Transition val$nextTransition = TransitionSet.this.mTransitions.get(i);
                    
                    @Override
                    public void onTransitionEnd(final Transition transition) {
                        this.val$nextTransition.runAnimators();
                        transition.removeListener((TransitionListener)this);
                    }
                });
            }
            final Transition transition = this.mTransitions.get(0);
            if (transition != null) {
                transition.runAnimators();
            }
        }
        else {
            final Iterator<Transition> iterator = this.mTransitions.iterator();
            while (iterator.hasNext()) {
                iterator.next().runAnimators();
            }
        }
    }
    
    @Override
    public TransitionSet setDuration(final long n) {
        super.setDuration(n);
        if (this.mDuration >= 0L) {
            for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
                this.mTransitions.get(i).setDuration(n);
            }
        }
        return this;
    }
    
    @Override
    public void setEpicenterCallback(final EpicenterCallback epicenterCallback) {
        super.setEpicenterCallback(epicenterCallback);
        this.mChangeFlags |= 0x8;
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).setEpicenterCallback(epicenterCallback);
        }
    }
    
    @Override
    public TransitionSet setInterpolator(final TimeInterpolator timeInterpolator) {
        this.mChangeFlags |= 0x1;
        if (this.mTransitions != null) {
            for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
                this.mTransitions.get(i).setInterpolator(timeInterpolator);
            }
        }
        return (TransitionSet)super.setInterpolator(timeInterpolator);
    }
    
    public TransitionSet setOrdering(final int i) {
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid parameter for TransitionSet ordering: ");
                sb.append(i);
                throw new AndroidRuntimeException(sb.toString());
            }
            case 1: {
                this.mPlayTogether = false;
                break;
            }
            case 0: {
                this.mPlayTogether = true;
                break;
            }
        }
        return this;
    }
    
    @Override
    public void setPathMotion(final PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        this.mChangeFlags |= 0x4;
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).setPathMotion(pathMotion);
        }
    }
    
    @Override
    public void setPropagation(final TransitionPropagation transitionPropagation) {
        super.setPropagation(transitionPropagation);
        this.mChangeFlags |= 0x2;
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).setPropagation(transitionPropagation);
        }
    }
    
    @Override
    public TransitionSet setStartDelay(final long startDelay) {
        return (TransitionSet)super.setStartDelay(startDelay);
    }
    
    @Override
    String toString(final String str) {
        String str2 = super.toString(str);
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("\n");
            final Transition transition = this.mTransitions.get(i);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("  ");
            sb.append(transition.toString(sb2.toString()));
            str2 = sb.toString();
        }
        return str2;
    }
    
    static class TransitionSetListener extends TransitionListenerAdapter
    {
        TransitionSet mTransitionSet;
        
        TransitionSetListener(final TransitionSet mTransitionSet) {
            this.mTransitionSet = mTransitionSet;
        }
        
        @Override
        public void onTransitionEnd(final Transition transition) {
            final TransitionSet mTransitionSet = this.mTransitionSet;
            --mTransitionSet.mCurrentListeners;
            if (this.mTransitionSet.mCurrentListeners == 0) {
                this.mTransitionSet.mStarted = false;
                this.mTransitionSet.end();
            }
            transition.removeListener((TransitionListener)this);
        }
        
        @Override
        public void onTransitionStart(final Transition transition) {
            if (!this.mTransitionSet.mStarted) {
                this.mTransitionSet.start();
                this.mTransitionSet.mStarted = true;
            }
        }
    }
}
