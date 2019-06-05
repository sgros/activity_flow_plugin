// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.support.base.observer;

import android.view.View;
import android.view.View$OnAttachStateChangeListener;
import android.arch.lifecycle.GenericLifecycleObserver;
import kotlin.jvm.functions.Function2;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import java.util.Map;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.WeakHashMap;

public final class ObserverRegistry<T> implements Observable<T>
{
    private final WeakHashMap<T, LifecycleBoundObserver<T>> lifecycleObservers;
    private final List<T> observers;
    private final Set<T> pausedObservers;
    private final WeakHashMap<T, ViewBoundObserver<T>> viewObservers;
    
    public ObserverRegistry() {
        this.observers = new ArrayList<T>();
        this.lifecycleObservers = new WeakHashMap<T, LifecycleBoundObserver<T>>();
        this.viewObservers = new WeakHashMap<T, ViewBoundObserver<T>>();
        this.pausedObservers = Collections.newSetFromMap((Map<T, Boolean>)new WeakHashMap<Object, Boolean>());
    }
    
    @Override
    public void notifyObservers(final Function1<? super T, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        synchronized (this.observers) {
            for (final Object next : this.observers) {
                if (!this.pausedObservers.contains(next)) {
                    function1.invoke((Object)next);
                }
            }
            final Unit instance = Unit.INSTANCE;
        }
    }
    
    public void pauseObserver(final T t) {
        synchronized (this.observers) {
            this.pausedObservers.add(t);
        }
    }
    
    @Override
    public void register(final T t) {
        synchronized (this.observers) {
            this.observers.add(t);
        }
    }
    
    @Override
    public void register(final T t, final LifecycleOwner lifecycleOwner, final boolean b) {
        Intrinsics.checkParameterIsNotNull(lifecycleOwner, "owner");
        final Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        Intrinsics.checkExpressionValueIsNotNull(lifecycle, "owner.lifecycle");
        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        this.register(t);
        final LifecycleBoundObserver<T> lifecycleBoundObserver = new LifecycleBoundObserver<T>(lifecycleOwner, this, t, b);
        this.lifecycleObservers.put(t, lifecycleBoundObserver);
        lifecycleOwner.getLifecycle().addObserver(lifecycleBoundObserver);
    }
    
    public void resumeObserver(final T t) {
        synchronized (this.observers) {
            this.pausedObservers.remove(t);
        }
    }
    
    @Override
    public void unregister(final T t) {
        Object observers = this.observers;
        synchronized (observers) {
            this.observers.remove(t);
            this.pausedObservers.remove(t);
            // monitorexit(observers)
            observers = this.lifecycleObservers.get(t);
            if (observers != null) {
                ((LifecycleBoundObserver)observers).remove();
            }
            final ViewBoundObserver<T> viewBoundObserver = this.viewObservers.get(t);
            if (viewBoundObserver != null) {
                viewBoundObserver.remove();
            }
        }
    }
    
    @Override
    public void unregisterObservers() {
        synchronized (this.observers) {
            final Iterator<Object> iterator = this.observers.iterator();
            while (iterator.hasNext()) {
                final LifecycleBoundObserver<T> lifecycleBoundObserver = this.lifecycleObservers.get(iterator.next());
                if (lifecycleBoundObserver != null) {
                    lifecycleBoundObserver.remove();
                }
            }
            this.observers.clear();
            this.pausedObservers.clear();
            final Unit instance = Unit.INSTANCE;
        }
    }
    
    @Override
    public <V> List<Function1<V, Boolean>> wrapConsumers(final Function2<? super T, ? super V, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        final ArrayList<Object> list = new ArrayList<Object>();
        synchronized (this.observers) {
            final Iterator<Object> iterator = this.observers.iterator();
            while (iterator.hasNext()) {
                list.add(new ObserverRegistry$wrapConsumers$$inlined$synchronized$lambda.ObserverRegistry$wrapConsumers$$inlined$synchronized$lambda$1(iterator.next(), this, (List)list, (Function2)function2));
            }
            final Unit instance = Unit.INSTANCE;
            return (List<Function1<V, Boolean>>)list;
        }
    }
    
    private static final class LifecycleBoundObserver<T> implements GenericLifecycleObserver
    {
        private final boolean autoPause;
        private final T observer;
        private final LifecycleOwner owner;
        private final ObserverRegistry<T> registry;
        
        public LifecycleBoundObserver(final LifecycleOwner owner, final ObserverRegistry<T> registry, final T observer, final boolean autoPause) {
            Intrinsics.checkParameterIsNotNull(owner, "owner");
            Intrinsics.checkParameterIsNotNull(registry, "registry");
            this.owner = owner;
            this.registry = registry;
            this.observer = observer;
            this.autoPause = autoPause;
        }
        
        @Override
        public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
            if (this.autoPause) {
                if (event == Lifecycle.Event.ON_PAUSE) {
                    this.registry.pauseObserver(this.observer);
                }
                else if (event == Lifecycle.Event.ON_RESUME) {
                    this.registry.resumeObserver(this.observer);
                }
            }
            final Lifecycle lifecycle = this.owner.getLifecycle();
            Intrinsics.checkExpressionValueIsNotNull(lifecycle, "owner.lifecycle");
            if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
                this.registry.unregister(this.observer);
            }
        }
        
        public final void remove() {
            this.owner.getLifecycle().removeObserver(this);
        }
    }
    
    private static final class ViewBoundObserver<T> implements View$OnAttachStateChangeListener
    {
        private final T observer;
        private final ObserverRegistry<T> registry;
        private final View view;
        
        public void onViewAttachedToWindow(final View view) {
            Intrinsics.checkParameterIsNotNull(view, "view");
        }
        
        public void onViewDetachedFromWindow(final View view) {
            Intrinsics.checkParameterIsNotNull(view, "view");
            this.registry.unregister(this.observer);
        }
        
        public final void remove() {
            this.view.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
        }
    }
}
