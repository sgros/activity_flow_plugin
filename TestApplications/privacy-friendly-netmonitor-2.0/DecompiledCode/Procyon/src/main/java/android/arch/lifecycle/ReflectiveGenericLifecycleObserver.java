// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class ReflectiveGenericLifecycleObserver implements GenericLifecycleObserver
{
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static final Map<Class, CallbackInfo> sInfoCache;
    private final CallbackInfo mInfo;
    private final Object mWrapped;
    
    static {
        sInfoCache = new HashMap<Class, CallbackInfo>();
    }
    
    ReflectiveGenericLifecycleObserver(final Object mWrapped) {
        this.mWrapped = mWrapped;
        this.mInfo = getInfo(this.mWrapped.getClass());
    }
    
    private static CallbackInfo createInfo(final Class clazz) {
        final Class superclass = clazz.getSuperclass();
        final HashMap<MethodReference, Lifecycle.Event> hashMap = new HashMap<MethodReference, Lifecycle.Event>();
        if (superclass != null) {
            final CallbackInfo info = getInfo(superclass);
            if (info != null) {
                hashMap.putAll((Map<?, ?>)info.mHandlerToEvent);
            }
        }
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        final Class[] interfaces = clazz.getInterfaces();
        for (int length = interfaces.length, i = 0; i < length; ++i) {
            for (final Map.Entry<MethodReference, Lifecycle.Event> entry : getInfo(interfaces[i]).mHandlerToEvent.entrySet()) {
                verifyAndPutHandler(hashMap, entry.getKey(), entry.getValue(), clazz);
            }
        }
        for (final Method method : declaredMethods) {
            final OnLifecycleEvent onLifecycleEvent = method.getAnnotation(OnLifecycleEvent.class);
            if (onLifecycleEvent != null) {
                final Class<?>[] parameterTypes = method.getParameterTypes();
                int n;
                if (parameterTypes.length > 0) {
                    if (!parameterTypes[0].isAssignableFrom(LifecycleOwner.class)) {
                        throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                    }
                    n = 1;
                }
                else {
                    n = 0;
                }
                final Lifecycle.Event value = onLifecycleEvent.value();
                if (parameterTypes.length > 1) {
                    if (!parameterTypes[1].isAssignableFrom(Lifecycle.Event.class)) {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    }
                    if (value != Lifecycle.Event.ON_ANY) {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                    n = 2;
                }
                if (parameterTypes.length > 2) {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
                verifyAndPutHandler(hashMap, new MethodReference(n, method), value, clazz);
            }
        }
        final CallbackInfo callbackInfo = new CallbackInfo(hashMap);
        ReflectiveGenericLifecycleObserver.sInfoCache.put(clazz, callbackInfo);
        return callbackInfo;
    }
    
    private static CallbackInfo getInfo(final Class clazz) {
        final CallbackInfo callbackInfo = ReflectiveGenericLifecycleObserver.sInfoCache.get(clazz);
        if (callbackInfo != null) {
            return callbackInfo;
        }
        return createInfo(clazz);
    }
    
    private void invokeCallback(final MethodReference methodReference, final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        try {
            switch (methodReference.mCallType) {
                case 2: {
                    methodReference.mMethod.invoke(this.mWrapped, lifecycleOwner, event);
                    break;
                }
                case 1: {
                    methodReference.mMethod.invoke(this.mWrapped, lifecycleOwner);
                    break;
                }
                case 0: {
                    methodReference.mMethod.invoke(this.mWrapped, new Object[0]);
                    break;
                }
            }
        }
        catch (IllegalAccessException cause) {
            throw new RuntimeException(cause);
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException("Failed to call observer method", ex.getCause());
        }
    }
    
    private void invokeCallbacks(final CallbackInfo callbackInfo, final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        this.invokeMethodsForEvent(callbackInfo.mEventToHandlers.get(event), lifecycleOwner, event);
        this.invokeMethodsForEvent(callbackInfo.mEventToHandlers.get(Lifecycle.Event.ON_ANY), lifecycleOwner, event);
    }
    
    private void invokeMethodsForEvent(final List<MethodReference> list, final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                this.invokeCallback(list.get(i), lifecycleOwner, event);
            }
        }
    }
    
    private static void verifyAndPutHandler(final Map<MethodReference, Lifecycle.Event> map, final MethodReference methodReference, final Lifecycle.Event obj, final Class clazz) {
        final Lifecycle.Event obj2 = map.get(methodReference);
        if (obj2 != null && obj != obj2) {
            final Method mMethod = methodReference.mMethod;
            final StringBuilder sb = new StringBuilder();
            sb.append("Method ");
            sb.append(mMethod.getName());
            sb.append(" in ");
            sb.append(clazz.getName());
            sb.append(" already declared with different @OnLifecycleEvent value: previous");
            sb.append(" value ");
            sb.append(obj2);
            sb.append(", new value ");
            sb.append(obj);
            throw new IllegalArgumentException(sb.toString());
        }
        if (obj2 == null) {
            map.put(methodReference, obj);
        }
    }
    
    @Override
    public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        this.invokeCallbacks(this.mInfo, lifecycleOwner, event);
    }
    
    static class CallbackInfo
    {
        final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers;
        final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;
        
        CallbackInfo(final Map<MethodReference, Lifecycle.Event> mHandlerToEvent) {
            this.mHandlerToEvent = mHandlerToEvent;
            this.mEventToHandlers = new HashMap<Lifecycle.Event, List<MethodReference>>();
            for (final Map.Entry<MethodReference, Lifecycle.Event> entry : mHandlerToEvent.entrySet()) {
                final Lifecycle.Event event = entry.getValue();
                List<MethodReference> list;
                if ((list = this.mEventToHandlers.get(event)) == null) {
                    list = new ArrayList<MethodReference>();
                    this.mEventToHandlers.put(event, list);
                }
                list.add(entry.getKey());
            }
        }
    }
    
    static class MethodReference
    {
        final int mCallType;
        final Method mMethod;
        
        MethodReference(final int mCallType, final Method mMethod) {
            this.mCallType = mCallType;
            (this.mMethod = mMethod).setAccessible(true);
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && this.getClass() == o.getClass()) {
                final MethodReference methodReference = (MethodReference)o;
                if (this.mCallType != methodReference.mCallType || !this.mMethod.getName().equals(methodReference.mMethod.getName())) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return 31 * this.mCallType + this.mMethod.getName().hashCode();
        }
    }
}
