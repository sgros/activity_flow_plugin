// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

import java.util.Iterator;
import com.bumptech.glide.request.target.Target;
import android.widget.ImageView;
import android.os.Looper;
import android.content.Context;
import android.os.Handler;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.load.engine.Engine;
import java.util.Map;
import com.bumptech.glide.request.RequestOptions;
import android.annotation.TargetApi;
import android.content.ContextWrapper;

@TargetApi(14)
public class GlideContext extends ContextWrapper
{
    static final TransitionOptions<?, ?> DEFAULT_TRANSITION_OPTIONS;
    private final RequestOptions defaultRequestOptions;
    private final Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions;
    private final Engine engine;
    private final ImageViewTargetFactory imageViewTargetFactory;
    private final int logLevel;
    private final Handler mainHandler;
    private final Registry registry;
    
    static {
        DEFAULT_TRANSITION_OPTIONS = new GenericTransitionOptions<Object>();
    }
    
    public GlideContext(final Context context, final Registry registry, final ImageViewTargetFactory imageViewTargetFactory, final RequestOptions defaultRequestOptions, final Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions, final Engine engine, final int logLevel) {
        super(context.getApplicationContext());
        this.registry = registry;
        this.imageViewTargetFactory = imageViewTargetFactory;
        this.defaultRequestOptions = defaultRequestOptions;
        this.defaultTransitionOptions = defaultTransitionOptions;
        this.engine = engine;
        this.logLevel = logLevel;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }
    
    public <X> Target<X> buildImageViewTarget(final ImageView imageView, final Class<X> clazz) {
        return this.imageViewTargetFactory.buildTarget(imageView, clazz);
    }
    
    public RequestOptions getDefaultRequestOptions() {
        return this.defaultRequestOptions;
    }
    
    public <T> TransitionOptions<?, T> getDefaultTransitionOptions(final Class<T> clazz) {
        TransitionOptions<?, T> transitionOptions2;
        TransitionOptions<?, ?> transitionOptions = transitionOptions2 = (TransitionOptions<?, T>)this.defaultTransitionOptions.get(clazz);
        if (transitionOptions == null) {
            final Iterator<Map.Entry<Class<?>, TransitionOptions<?, ?>>> iterator = this.defaultTransitionOptions.entrySet().iterator();
            while (true) {
                transitionOptions2 = (TransitionOptions<?, T>)transitionOptions;
                if (!iterator.hasNext()) {
                    break;
                }
                final Map.Entry<Class<?>, TransitionOptions<?, ?>> entry = iterator.next();
                if (!entry.getKey().isAssignableFrom(clazz)) {
                    continue;
                }
                transitionOptions = entry.getValue();
            }
        }
        TransitionOptions<?, ?> default_TRANSITION_OPTIONS;
        if ((default_TRANSITION_OPTIONS = transitionOptions2) == null) {
            default_TRANSITION_OPTIONS = GlideContext.DEFAULT_TRANSITION_OPTIONS;
        }
        return (TransitionOptions<?, T>)default_TRANSITION_OPTIONS;
    }
    
    public Engine getEngine() {
        return this.engine;
    }
    
    public int getLogLevel() {
        return this.logLevel;
    }
    
    public Registry getRegistry() {
        return this.registry;
    }
}
