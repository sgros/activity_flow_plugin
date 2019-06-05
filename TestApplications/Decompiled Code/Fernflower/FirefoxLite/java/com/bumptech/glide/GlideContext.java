package com.bumptech.glide;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@TargetApi(14)
public class GlideContext extends ContextWrapper {
   static final TransitionOptions DEFAULT_TRANSITION_OPTIONS = new GenericTransitionOptions();
   private final RequestOptions defaultRequestOptions;
   private final Map defaultTransitionOptions;
   private final Engine engine;
   private final ImageViewTargetFactory imageViewTargetFactory;
   private final int logLevel;
   private final Handler mainHandler;
   private final Registry registry;

   public GlideContext(Context var1, Registry var2, ImageViewTargetFactory var3, RequestOptions var4, Map var5, Engine var6, int var7) {
      super(var1.getApplicationContext());
      this.registry = var2;
      this.imageViewTargetFactory = var3;
      this.defaultRequestOptions = var4;
      this.defaultTransitionOptions = var5;
      this.engine = var6;
      this.logLevel = var7;
      this.mainHandler = new Handler(Looper.getMainLooper());
   }

   public Target buildImageViewTarget(ImageView var1, Class var2) {
      return this.imageViewTargetFactory.buildTarget(var1, var2);
   }

   public RequestOptions getDefaultRequestOptions() {
      return this.defaultRequestOptions;
   }

   public TransitionOptions getDefaultTransitionOptions(Class var1) {
      TransitionOptions var2 = (TransitionOptions)this.defaultTransitionOptions.get(var1);
      TransitionOptions var3 = var2;
      if (var2 == null) {
         Iterator var4 = this.defaultTransitionOptions.entrySet().iterator();

         while(true) {
            var3 = var2;
            if (!var4.hasNext()) {
               break;
            }

            Entry var6 = (Entry)var4.next();
            if (((Class)var6.getKey()).isAssignableFrom(var1)) {
               var2 = (TransitionOptions)var6.getValue();
            }
         }
      }

      TransitionOptions var5 = var3;
      if (var3 == null) {
         var5 = DEFAULT_TRANSITION_OPTIONS;
      }

      return var5;
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
