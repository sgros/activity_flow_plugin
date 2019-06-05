package mozilla.components.support.base.observer;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.LifecycleOwner;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ObserverRegistry.kt */
public final class ObserverRegistry<T> implements Observable<T> {
    private final WeakHashMap<T, LifecycleBoundObserver<T>> lifecycleObservers = new WeakHashMap();
    private final List<T> observers = new ArrayList();
    private final Set<T> pausedObservers = Collections.newSetFromMap(new WeakHashMap());
    private final WeakHashMap<T, ViewBoundObserver<T>> viewObservers = new WeakHashMap();

    /* compiled from: ObserverRegistry.kt */
    private static final class ViewBoundObserver<T> implements OnAttachStateChangeListener {
        private final T observer;
        private final ObserverRegistry<T> registry;
        private final View view;

        public void onViewAttachedToWindow(View view) {
            Intrinsics.checkParameterIsNotNull(view, "view");
        }

        public void onViewDetachedFromWindow(View view) {
            Intrinsics.checkParameterIsNotNull(view, "view");
            this.registry.unregister(this.observer);
        }

        public final void remove() {
            this.view.removeOnAttachStateChangeListener(this);
        }
    }

    /* compiled from: ObserverRegistry.kt */
    private static final class LifecycleBoundObserver<T> implements GenericLifecycleObserver {
        private final boolean autoPause;
        private final T observer;
        private final LifecycleOwner owner;
        private final ObserverRegistry<T> registry;

        public LifecycleBoundObserver(LifecycleOwner lifecycleOwner, ObserverRegistry<T> observerRegistry, T t, boolean z) {
            Intrinsics.checkParameterIsNotNull(lifecycleOwner, "owner");
            Intrinsics.checkParameterIsNotNull(observerRegistry, "registry");
            this.owner = lifecycleOwner;
            this.registry = observerRegistry;
            this.observer = t;
            this.autoPause = z;
        }

        public void onStateChanged(LifecycleOwner lifecycleOwner, Event event) {
            if (this.autoPause) {
                if (event == Event.ON_PAUSE) {
                    this.registry.pauseObserver(this.observer);
                } else if (event == Event.ON_RESUME) {
                    this.registry.resumeObserver(this.observer);
                }
            }
            Lifecycle lifecycle = this.owner.getLifecycle();
            Intrinsics.checkExpressionValueIsNotNull(lifecycle, "owner.lifecycle");
            if (lifecycle.getCurrentState() == State.DESTROYED) {
                this.registry.unregister(this.observer);
            }
        }

        public final void remove() {
            this.owner.getLifecycle().removeObserver(this);
        }
    }

    public void register(T t) {
        synchronized (this.observers) {
            this.observers.add(t);
        }
    }

    public void register(T t, LifecycleOwner lifecycleOwner, boolean z) {
        Intrinsics.checkParameterIsNotNull(lifecycleOwner, "owner");
        Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        Intrinsics.checkExpressionValueIsNotNull(lifecycle, "owner.lifecycle");
        if (lifecycle.getCurrentState() != State.DESTROYED) {
            register(t);
            LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(lifecycleOwner, this, t, z);
            this.lifecycleObservers.put(t, lifecycleBoundObserver);
            lifecycleOwner.getLifecycle().addObserver(lifecycleBoundObserver);
        }
    }

    public void unregister(T t) {
        synchronized (this.observers) {
            this.observers.remove(t);
            this.pausedObservers.remove(t);
        }
        LifecycleBoundObserver lifecycleBoundObserver = (LifecycleBoundObserver) this.lifecycleObservers.get(t);
        if (lifecycleBoundObserver != null) {
            lifecycleBoundObserver.remove();
        }
        ViewBoundObserver viewBoundObserver = (ViewBoundObserver) this.viewObservers.get(t);
        if (viewBoundObserver != null) {
            viewBoundObserver.remove();
        }
    }

    public void unregisterObservers() {
        synchronized (this.observers) {
            for (Object obj : this.observers) {
                LifecycleBoundObserver lifecycleBoundObserver = (LifecycleBoundObserver) this.lifecycleObservers.get(obj);
                if (lifecycleBoundObserver != null) {
                    lifecycleBoundObserver.remove();
                }
            }
            this.observers.clear();
            this.pausedObservers.clear();
            Unit unit = Unit.INSTANCE;
        }
    }

    public void pauseObserver(T t) {
        synchronized (this.observers) {
            this.pausedObservers.add(t);
        }
    }

    public void resumeObserver(T t) {
        synchronized (this.observers) {
            this.pausedObservers.remove(t);
        }
    }

    public void notifyObservers(Function1<? super T, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        synchronized (this.observers) {
            for (Object next : this.observers) {
                if (!this.pausedObservers.contains(next)) {
                    function1.invoke(next);
                }
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public <V> List<Function1<V, Boolean>> wrapConsumers(Function2<? super T, ? super V, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        List arrayList = new ArrayList();
        synchronized (this.observers) {
            for (Object observerRegistry$wrapConsumers$$inlined$synchronized$lambda$1 : this.observers) {
                arrayList.add(new ObserverRegistry$wrapConsumers$$inlined$synchronized$lambda$1(observerRegistry$wrapConsumers$$inlined$synchronized$lambda$1, this, arrayList, function2));
            }
            Unit unit = Unit.INSTANCE;
        }
        return arrayList;
    }
}
