// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.manager;

import java.util.Iterator;
import com.bumptech.glide.util.Util;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.WeakHashMap;
import com.bumptech.glide.request.target.Target;
import java.util.Set;

public final class TargetTracker implements LifecycleListener
{
    private final Set<Target<?>> targets;
    
    public TargetTracker() {
        this.targets = Collections.newSetFromMap(new WeakHashMap<Target<?>, Boolean>());
    }
    
    public void clear() {
        this.targets.clear();
    }
    
    public List<Target<?>> getAll() {
        return new ArrayList<Target<?>>(this.targets);
    }
    
    @Override
    public void onDestroy() {
        final Iterator<Target<?>> iterator = Util.getSnapshot(this.targets).iterator();
        while (iterator.hasNext()) {
            iterator.next().onDestroy();
        }
    }
    
    @Override
    public void onStart() {
        final Iterator<Target<?>> iterator = Util.getSnapshot(this.targets).iterator();
        while (iterator.hasNext()) {
            iterator.next().onStart();
        }
    }
    
    @Override
    public void onStop() {
        final Iterator<Target<?>> iterator = Util.getSnapshot(this.targets).iterator();
        while (iterator.hasNext()) {
            iterator.next().onStop();
        }
    }
    
    public void track(final Target<?> target) {
        this.targets.add(target);
    }
    
    public void untrack(final Target<?> target) {
        this.targets.remove(target);
    }
}
