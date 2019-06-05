// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.controllers;

import java.util.Iterator;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import androidx.work.impl.constraints.trackers.ConstraintTracker;
import java.util.List;
import androidx.work.impl.constraints.ConstraintListener;

public abstract class ConstraintController<T> implements ConstraintListener<T>
{
    private OnConstraintUpdatedCallback mCallback;
    private T mCurrentValue;
    private final List<String> mMatchingWorkSpecIds;
    private ConstraintTracker<T> mTracker;
    
    ConstraintController(final ConstraintTracker<T> mTracker) {
        this.mMatchingWorkSpecIds = new ArrayList<String>();
        this.mTracker = mTracker;
    }
    
    private void updateCallback() {
        if (!this.mMatchingWorkSpecIds.isEmpty() && this.mCallback != null) {
            if (this.mCurrentValue != null && !this.isConstrained(this.mCurrentValue)) {
                this.mCallback.onConstraintMet(this.mMatchingWorkSpecIds);
            }
            else {
                this.mCallback.onConstraintNotMet(this.mMatchingWorkSpecIds);
            }
        }
    }
    
    abstract boolean hasConstraint(final WorkSpec p0);
    
    abstract boolean isConstrained(final T p0);
    
    public boolean isWorkSpecConstrained(final String s) {
        return this.mCurrentValue != null && this.isConstrained(this.mCurrentValue) && this.mMatchingWorkSpecIds.contains(s);
    }
    
    @Override
    public void onConstraintChanged(final T mCurrentValue) {
        this.mCurrentValue = mCurrentValue;
        this.updateCallback();
    }
    
    public void replace(final List<WorkSpec> list) {
        this.mMatchingWorkSpecIds.clear();
        for (final WorkSpec workSpec : list) {
            if (this.hasConstraint(workSpec)) {
                this.mMatchingWorkSpecIds.add(workSpec.id);
            }
        }
        if (this.mMatchingWorkSpecIds.isEmpty()) {
            this.mTracker.removeListener(this);
        }
        else {
            this.mTracker.addListener(this);
        }
        this.updateCallback();
    }
    
    public void reset() {
        if (!this.mMatchingWorkSpecIds.isEmpty()) {
            this.mMatchingWorkSpecIds.clear();
            this.mTracker.removeListener(this);
        }
    }
    
    public void setCallback(final OnConstraintUpdatedCallback mCallback) {
        if (this.mCallback != mCallback) {
            this.mCallback = mCallback;
            this.updateCallback();
        }
    }
    
    public interface OnConstraintUpdatedCallback
    {
        void onConstraintMet(final List<String> p0);
        
        void onConstraintNotMet(final List<String> p0);
    }
}
