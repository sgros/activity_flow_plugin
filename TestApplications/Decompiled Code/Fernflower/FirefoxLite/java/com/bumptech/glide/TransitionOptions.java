package com.bumptech.glide;

import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.TransitionFactory;

public abstract class TransitionOptions implements Cloneable {
   private TransitionFactory transitionFactory = NoTransition.getFactory();

   protected final TransitionOptions clone() {
      try {
         TransitionOptions var1 = (TransitionOptions)super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   final TransitionFactory getTransitionFactory() {
      return this.transitionFactory;
   }
}
