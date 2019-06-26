package androidx.appcompat.graphics.drawable;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.StateSet;
import androidx.appcompat.R$styleable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedStateListDrawableCompat extends StateListDrawable {
   private boolean mMutated;
   private AnimatedStateListDrawableCompat.AnimatedStateListState mState;
   private AnimatedStateListDrawableCompat.Transition mTransition;
   private int mTransitionFromIndex;
   private int mTransitionToIndex;

   public AnimatedStateListDrawableCompat() {
      this((AnimatedStateListDrawableCompat.AnimatedStateListState)null, (Resources)null);
   }

   AnimatedStateListDrawableCompat(AnimatedStateListDrawableCompat.AnimatedStateListState var1, Resources var2) {
      super((StateListDrawable.StateListState)null);
      this.mTransitionToIndex = -1;
      this.mTransitionFromIndex = -1;
      this.setConstantState(new AnimatedStateListDrawableCompat.AnimatedStateListState(var1, this, var2));
      this.onStateChange(this.getState());
      this.jumpToCurrentState();
   }

   public static AnimatedStateListDrawableCompat createFromXmlInner(Context var0, Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws IOException, XmlPullParserException {
      String var5 = var2.getName();
      if (var5.equals("animated-selector")) {
         AnimatedStateListDrawableCompat var6 = new AnimatedStateListDrawableCompat();
         var6.inflate(var0, var1, var2, var3, var4);
         return var6;
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append(var2.getPositionDescription());
         var7.append(": invalid animated-selector tag ");
         var7.append(var5);
         throw new XmlPullParserException(var7.toString());
      }
   }

   private void inflateChildElements(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      int var6 = var3.getDepth() + 1;

      while(true) {
         int var7 = var3.next();
         if (var7 == 1) {
            break;
         }

         int var8 = var3.getDepth();
         if (var8 < var6 && var7 == 3) {
            break;
         }

         if (var7 == 2 && var8 <= var6) {
            if (var3.getName().equals("item")) {
               this.parseItem(var1, var2, var3, var4, var5);
            } else if (var3.getName().equals("transition")) {
               this.parseTransition(var1, var2, var3, var4, var5);
            }
         }
      }

   }

   private void init() {
      this.onStateChange(this.getState());
   }

   private int parseItem(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      TypedArray var6 = TypedArrayUtils.obtainAttributes(var2, var5, var4, R$styleable.AnimatedStateListDrawableItem);
      int var7 = var6.getResourceId(R$styleable.AnimatedStateListDrawableItem_android_id, 0);
      int var8 = var6.getResourceId(R$styleable.AnimatedStateListDrawableItem_android_drawable, -1);
      Drawable var9;
      if (var8 > 0) {
         var9 = AppCompatResources.getDrawable(var1, var8);
      } else {
         var9 = null;
      }

      var6.recycle();
      int[] var12 = this.extractStateSet(var4);
      Object var10 = var9;
      StringBuilder var11;
      if (var9 == null) {
         while(true) {
            var8 = var3.next();
            if (var8 != 4) {
               if (var8 != 2) {
                  var11 = new StringBuilder();
                  var11.append(var3.getPositionDescription());
                  var11.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                  throw new XmlPullParserException(var11.toString());
               }

               if (var3.getName().equals("vector")) {
                  var10 = VectorDrawableCompat.createFromXmlInner(var2, var3, var4, var5);
               } else if (VERSION.SDK_INT >= 21) {
                  var10 = Drawable.createFromXmlInner(var2, var3, var4, var5);
               } else {
                  var10 = Drawable.createFromXmlInner(var2, var3, var4);
               }
               break;
            }
         }
      }

      if (var10 != null) {
         return this.mState.addStateSet(var12, (Drawable)var10, var7);
      } else {
         var11 = new StringBuilder();
         var11.append(var3.getPositionDescription());
         var11.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
         throw new XmlPullParserException(var11.toString());
      }
   }

   private int parseTransition(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      TypedArray var6 = TypedArrayUtils.obtainAttributes(var2, var5, var4, R$styleable.AnimatedStateListDrawableTransition);
      int var7 = var6.getResourceId(R$styleable.AnimatedStateListDrawableTransition_android_fromId, -1);
      int var8 = var6.getResourceId(R$styleable.AnimatedStateListDrawableTransition_android_toId, -1);
      int var9 = var6.getResourceId(R$styleable.AnimatedStateListDrawableTransition_android_drawable, -1);
      Drawable var10;
      if (var9 > 0) {
         var10 = AppCompatResources.getDrawable(var1, var9);
      } else {
         var10 = null;
      }

      boolean var11 = var6.getBoolean(R$styleable.AnimatedStateListDrawableTransition_android_reversible, false);
      var6.recycle();
      Object var13 = var10;
      StringBuilder var12;
      if (var10 == null) {
         while(true) {
            var9 = var3.next();
            if (var9 != 4) {
               if (var9 != 2) {
                  var12 = new StringBuilder();
                  var12.append(var3.getPositionDescription());
                  var12.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
                  throw new XmlPullParserException(var12.toString());
               }

               if (var3.getName().equals("animated-vector")) {
                  var13 = AnimatedVectorDrawableCompat.createFromXmlInner(var1, var2, var3, var4, var5);
               } else if (VERSION.SDK_INT >= 21) {
                  var13 = Drawable.createFromXmlInner(var2, var3, var4, var5);
               } else {
                  var13 = Drawable.createFromXmlInner(var2, var3, var4);
               }
               break;
            }
         }
      }

      if (var13 != null) {
         if (var7 != -1 && var8 != -1) {
            return this.mState.addTransition(var7, var8, (Drawable)var13, var11);
         } else {
            var12 = new StringBuilder();
            var12.append(var3.getPositionDescription());
            var12.append(": <transition> tag requires 'fromId' & 'toId' attributes");
            throw new XmlPullParserException(var12.toString());
         }
      } else {
         var12 = new StringBuilder();
         var12.append(var3.getPositionDescription());
         var12.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
         throw new XmlPullParserException(var12.toString());
      }
   }

   private boolean selectTransition(int var1) {
      AnimatedStateListDrawableCompat.Transition var2 = this.mTransition;
      int var3;
      if (var2 != null) {
         if (var1 == this.mTransitionToIndex) {
            return true;
         }

         if (var1 == this.mTransitionFromIndex && var2.canReverse()) {
            var2.reverse();
            this.mTransitionToIndex = this.mTransitionFromIndex;
            this.mTransitionFromIndex = var1;
            return true;
         }

         var3 = this.mTransitionToIndex;
         var2.stop();
      } else {
         var3 = this.getCurrentIndex();
      }

      Object var11;
      label42: {
         this.mTransition = null;
         this.mTransitionFromIndex = -1;
         this.mTransitionToIndex = -1;
         AnimatedStateListDrawableCompat.AnimatedStateListState var4 = this.mState;
         int var5 = var4.getKeyframeIdAt(var3);
         int var6 = var4.getKeyframeIdAt(var1);
         if (var6 != 0 && var5 != 0) {
            int var7 = var4.indexOfTransition(var5, var6);
            if (var7 < 0) {
               return false;
            }

            boolean var8 = var4.transitionHasReversibleFlag(var5, var6);
            this.selectDrawable(var7);
            Drawable var10 = this.getCurrent();
            if (var10 instanceof AnimationDrawable) {
               boolean var9 = var4.isTransitionReversed(var5, var6);
               var11 = new AnimatedStateListDrawableCompat.AnimationDrawableTransition((AnimationDrawable)var10, var9, var8);
               break label42;
            }

            if (var10 instanceof AnimatedVectorDrawableCompat) {
               var11 = new AnimatedStateListDrawableCompat.AnimatedVectorDrawableTransition((AnimatedVectorDrawableCompat)var10);
               break label42;
            }

            if (var10 instanceof Animatable) {
               var11 = new AnimatedStateListDrawableCompat.AnimatableTransition((Animatable)var10);
               break label42;
            }
         }

         return false;
      }

      ((AnimatedStateListDrawableCompat.Transition)var11).start();
      this.mTransition = (AnimatedStateListDrawableCompat.Transition)var11;
      this.mTransitionFromIndex = var3;
      this.mTransitionToIndex = var1;
      return true;
   }

   private void updateStateFromTypedArray(TypedArray var1) {
      AnimatedStateListDrawableCompat.AnimatedStateListState var2 = this.mState;
      if (VERSION.SDK_INT >= 21) {
         var2.mChangingConfigurations |= var1.getChangingConfigurations();
      }

      var2.setVariablePadding(var1.getBoolean(R$styleable.AnimatedStateListDrawableCompat_android_variablePadding, var2.mVariablePadding));
      var2.setConstantSize(var1.getBoolean(R$styleable.AnimatedStateListDrawableCompat_android_constantSize, var2.mConstantSize));
      var2.setEnterFadeDuration(var1.getInt(R$styleable.AnimatedStateListDrawableCompat_android_enterFadeDuration, var2.mEnterFadeDuration));
      var2.setExitFadeDuration(var1.getInt(R$styleable.AnimatedStateListDrawableCompat_android_exitFadeDuration, var2.mExitFadeDuration));
      this.setDither(var1.getBoolean(R$styleable.AnimatedStateListDrawableCompat_android_dither, var2.mDither));
   }

   AnimatedStateListDrawableCompat.AnimatedStateListState cloneConstantState() {
      return new AnimatedStateListDrawableCompat.AnimatedStateListState(this.mState, this, (Resources)null);
   }

   public void inflate(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      TypedArray var6 = TypedArrayUtils.obtainAttributes(var2, var5, var4, R$styleable.AnimatedStateListDrawableCompat);
      this.setVisible(var6.getBoolean(R$styleable.AnimatedStateListDrawableCompat_android_visible, true), true);
      this.updateStateFromTypedArray(var6);
      this.updateDensity(var2);
      var6.recycle();
      this.inflateChildElements(var1, var2, var3, var4, var5);
      this.init();
   }

   public boolean isStateful() {
      return true;
   }

   public void jumpToCurrentState() {
      super.jumpToCurrentState();
      AnimatedStateListDrawableCompat.Transition var1 = this.mTransition;
      if (var1 != null) {
         var1.stop();
         this.mTransition = null;
         this.selectDrawable(this.mTransitionToIndex);
         this.mTransitionToIndex = -1;
         this.mTransitionFromIndex = -1;
      }

   }

   public Drawable mutate() {
      if (!this.mMutated) {
         super.mutate();
         this.mState.mutate();
         this.mMutated = true;
      }

      return this;
   }

   protected boolean onStateChange(int[] var1) {
      int var2 = this.mState.indexOfKeyframe(var1);
      boolean var3;
      if (var2 == this.getCurrentIndex() || !this.selectTransition(var2) && !this.selectDrawable(var2)) {
         var3 = false;
      } else {
         var3 = true;
      }

      Drawable var4 = this.getCurrent();
      boolean var5 = var3;
      if (var4 != null) {
         var5 = var3 | var4.setState(var1);
      }

      return var5;
   }

   protected void setConstantState(DrawableContainer.DrawableContainerState var1) {
      super.setConstantState(var1);
      if (var1 instanceof AnimatedStateListDrawableCompat.AnimatedStateListState) {
         this.mState = (AnimatedStateListDrawableCompat.AnimatedStateListState)var1;
      }

   }

   public boolean setVisible(boolean var1, boolean var2) {
      boolean var3 = super.setVisible(var1, var2);
      if (this.mTransition != null && (var3 || var2)) {
         if (var1) {
            this.mTransition.start();
         } else {
            this.jumpToCurrentState();
         }
      }

      return var3;
   }

   private static class AnimatableTransition extends AnimatedStateListDrawableCompat.Transition {
      private final Animatable mA;

      AnimatableTransition(Animatable var1) {
         super(null);
         this.mA = var1;
      }

      public void start() {
         this.mA.start();
      }

      public void stop() {
         this.mA.stop();
      }
   }

   static class AnimatedStateListState extends StateListDrawable.StateListState {
      SparseArrayCompat mStateIds;
      LongSparseArray mTransitions;

      AnimatedStateListState(AnimatedStateListDrawableCompat.AnimatedStateListState var1, AnimatedStateListDrawableCompat var2, Resources var3) {
         super(var1, var2, var3);
         if (var1 != null) {
            this.mTransitions = var1.mTransitions;
            this.mStateIds = var1.mStateIds;
         } else {
            this.mTransitions = new LongSparseArray();
            this.mStateIds = new SparseArrayCompat();
         }

      }

      private static long generateTransitionKey(int var0, int var1) {
         long var2 = (long)var0;
         return (long)var1 | var2 << 32;
      }

      int addStateSet(int[] var1, Drawable var2, int var3) {
         int var4 = super.addStateSet(var1, var2);
         this.mStateIds.put(var4, var3);
         return var4;
      }

      int addTransition(int var1, int var2, Drawable var3, boolean var4) {
         int var5 = super.addChild(var3);
         long var6 = generateTransitionKey(var1, var2);
         long var8;
         if (var4) {
            var8 = 8589934592L;
         } else {
            var8 = 0L;
         }

         LongSparseArray var12 = this.mTransitions;
         long var10 = (long)var5;
         var12.append(var6, var10 | var8);
         if (var4) {
            var6 = generateTransitionKey(var2, var1);
            this.mTransitions.append(var6, 4294967296L | var10 | var8);
         }

         return var5;
      }

      int getKeyframeIdAt(int var1) {
         byte var2 = 0;
         if (var1 < 0) {
            var1 = var2;
         } else {
            var1 = (Integer)this.mStateIds.get(var1, 0);
         }

         return var1;
      }

      int indexOfKeyframe(int[] var1) {
         int var2 = super.indexOfStateSet(var1);
         return var2 >= 0 ? var2 : super.indexOfStateSet(StateSet.WILD_CARD);
      }

      int indexOfTransition(int var1, int var2) {
         long var3 = generateTransitionKey(var1, var2);
         return (int)(Long)this.mTransitions.get(var3, -1L);
      }

      boolean isTransitionReversed(int var1, int var2) {
         long var3 = generateTransitionKey(var1, var2);
         boolean var5;
         if (((Long)this.mTransitions.get(var3, -1L) & 4294967296L) != 0L) {
            var5 = true;
         } else {
            var5 = false;
         }

         return var5;
      }

      void mutate() {
         this.mTransitions = this.mTransitions.clone();
         this.mStateIds = this.mStateIds.clone();
      }

      public Drawable newDrawable() {
         return new AnimatedStateListDrawableCompat(this, (Resources)null);
      }

      public Drawable newDrawable(Resources var1) {
         return new AnimatedStateListDrawableCompat(this, var1);
      }

      boolean transitionHasReversibleFlag(int var1, int var2) {
         long var3 = generateTransitionKey(var1, var2);
         boolean var5;
         if (((Long)this.mTransitions.get(var3, -1L) & 8589934592L) != 0L) {
            var5 = true;
         } else {
            var5 = false;
         }

         return var5;
      }
   }

   private static class AnimatedVectorDrawableTransition extends AnimatedStateListDrawableCompat.Transition {
      private final AnimatedVectorDrawableCompat mAvd;

      AnimatedVectorDrawableTransition(AnimatedVectorDrawableCompat var1) {
         super(null);
         this.mAvd = var1;
      }

      public void start() {
         this.mAvd.start();
      }

      public void stop() {
         this.mAvd.stop();
      }
   }

   private static class AnimationDrawableTransition extends AnimatedStateListDrawableCompat.Transition {
      private final ObjectAnimator mAnim;
      private final boolean mHasReversibleFlag;

      AnimationDrawableTransition(AnimationDrawable var1, boolean var2, boolean var3) {
         super(null);
         int var4 = var1.getNumberOfFrames();
         int var5;
         if (var2) {
            var5 = var4 - 1;
         } else {
            var5 = 0;
         }

         if (var2) {
            var4 = 0;
         } else {
            --var4;
         }

         AnimatedStateListDrawableCompat.FrameInterpolator var6 = new AnimatedStateListDrawableCompat.FrameInterpolator(var1, var2);
         ObjectAnimator var7 = ObjectAnimator.ofInt(var1, "currentIndex", new int[]{var5, var4});
         if (VERSION.SDK_INT >= 18) {
            var7.setAutoCancel(true);
         }

         var7.setDuration((long)var6.getTotalDuration());
         var7.setInterpolator(var6);
         this.mHasReversibleFlag = var3;
         this.mAnim = var7;
      }

      public boolean canReverse() {
         return this.mHasReversibleFlag;
      }

      public void reverse() {
         this.mAnim.reverse();
      }

      public void start() {
         this.mAnim.start();
      }

      public void stop() {
         this.mAnim.cancel();
      }
   }

   private static class FrameInterpolator implements TimeInterpolator {
      private int[] mFrameTimes;
      private int mFrames;
      private int mTotalDuration;

      FrameInterpolator(AnimationDrawable var1, boolean var2) {
         this.updateFrames(var1, var2);
      }

      public float getInterpolation(float var1) {
         int var2 = (int)(var1 * (float)this.mTotalDuration + 0.5F);
         int var3 = this.mFrames;
         int[] var4 = this.mFrameTimes;

         int var5;
         for(var5 = 0; var5 < var3 && var2 >= var4[var5]; ++var5) {
            var2 -= var4[var5];
         }

         if (var5 < var3) {
            var1 = (float)var2 / (float)this.mTotalDuration;
         } else {
            var1 = 0.0F;
         }

         return (float)var5 / (float)var3 + var1;
      }

      int getTotalDuration() {
         return this.mTotalDuration;
      }

      int updateFrames(AnimationDrawable var1, boolean var2) {
         int var3 = var1.getNumberOfFrames();
         this.mFrames = var3;
         int[] var4 = this.mFrameTimes;
         if (var4 == null || var4.length < var3) {
            this.mFrameTimes = new int[var3];
         }

         var4 = this.mFrameTimes;
         int var5 = 0;

         int var6;
         for(var6 = 0; var5 < var3; ++var5) {
            int var7;
            if (var2) {
               var7 = var3 - var5 - 1;
            } else {
               var7 = var5;
            }

            var7 = var1.getDuration(var7);
            var4[var5] = var7;
            var6 += var7;
         }

         this.mTotalDuration = var6;
         return var6;
      }
   }

   private abstract static class Transition {
      private Transition() {
      }

      // $FF: synthetic method
      Transition(Object var1) {
         this();
      }

      public boolean canReverse() {
         return false;
      }

      public void reverse() {
      }

      public abstract void start();

      public abstract void stop();
   }
}
