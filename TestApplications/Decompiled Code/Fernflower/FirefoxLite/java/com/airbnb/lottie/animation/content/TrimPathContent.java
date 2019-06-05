package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.ArrayList;
import java.util.List;

public class TrimPathContent implements Content, BaseKeyframeAnimation.AnimationListener {
   private final BaseKeyframeAnimation endAnimation;
   private final List listeners = new ArrayList();
   private final String name;
   private final BaseKeyframeAnimation offsetAnimation;
   private final BaseKeyframeAnimation startAnimation;
   private final ShapeTrimPath.Type type;

   public TrimPathContent(BaseLayer var1, ShapeTrimPath var2) {
      this.name = var2.getName();
      this.type = var2.getType();
      this.startAnimation = var2.getStart().createAnimation();
      this.endAnimation = var2.getEnd().createAnimation();
      this.offsetAnimation = var2.getOffset().createAnimation();
      var1.addAnimation(this.startAnimation);
      var1.addAnimation(this.endAnimation);
      var1.addAnimation(this.offsetAnimation);
      this.startAnimation.addUpdateListener(this);
      this.endAnimation.addUpdateListener(this);
      this.offsetAnimation.addUpdateListener(this);
   }

   void addListener(BaseKeyframeAnimation.AnimationListener var1) {
      this.listeners.add(var1);
   }

   public BaseKeyframeAnimation getEnd() {
      return this.endAnimation;
   }

   public String getName() {
      return this.name;
   }

   public BaseKeyframeAnimation getOffset() {
      return this.offsetAnimation;
   }

   public BaseKeyframeAnimation getStart() {
      return this.startAnimation;
   }

   ShapeTrimPath.Type getType() {
      return this.type;
   }

   public void onValueChanged() {
      for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
         ((BaseKeyframeAnimation.AnimationListener)this.listeners.get(var1)).onValueChanged();
      }

   }

   public void setContents(List var1, List var2) {
   }
}
