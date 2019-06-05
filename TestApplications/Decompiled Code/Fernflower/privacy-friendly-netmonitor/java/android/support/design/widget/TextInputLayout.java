package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.annotation.VisibleForTesting;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.Space;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintTypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class TextInputLayout extends LinearLayout {
   private static final int ANIMATION_DURATION = 200;
   private static final int INVALID_MAX_LENGTH = -1;
   private static final String LOG_TAG = "TextInputLayout";
   private ValueAnimator mAnimator;
   final CollapsingTextHelper mCollapsingTextHelper;
   boolean mCounterEnabled;
   private int mCounterMaxLength;
   private int mCounterOverflowTextAppearance;
   private boolean mCounterOverflowed;
   private int mCounterTextAppearance;
   private TextView mCounterView;
   private ColorStateList mDefaultTextColor;
   EditText mEditText;
   private CharSequence mError;
   private boolean mErrorEnabled;
   private boolean mErrorShown;
   private int mErrorTextAppearance;
   TextView mErrorView;
   private ColorStateList mFocusedTextColor;
   private boolean mHasPasswordToggleTintList;
   private boolean mHasPasswordToggleTintMode;
   private boolean mHasReconstructedEditTextBackground;
   private CharSequence mHint;
   private boolean mHintAnimationEnabled;
   private boolean mHintEnabled;
   private boolean mHintExpanded;
   private boolean mInDrawableStateChanged;
   private LinearLayout mIndicatorArea;
   private int mIndicatorsAdded;
   private final FrameLayout mInputFrame;
   private Drawable mOriginalEditTextEndDrawable;
   private CharSequence mOriginalHint;
   private CharSequence mPasswordToggleContentDesc;
   private Drawable mPasswordToggleDrawable;
   private Drawable mPasswordToggleDummyDrawable;
   private boolean mPasswordToggleEnabled;
   private ColorStateList mPasswordToggleTintList;
   private Mode mPasswordToggleTintMode;
   private CheckableImageButton mPasswordToggleView;
   private boolean mPasswordToggledVisible;
   private boolean mRestoringSavedState;
   private Paint mTmpPaint;
   private final Rect mTmpRect;
   private Typeface mTypeface;

   public TextInputLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TextInputLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public TextInputLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2);
      this.mTmpRect = new Rect();
      this.mCollapsingTextHelper = new CollapsingTextHelper(this);
      ThemeUtils.checkAppCompatTheme(var1);
      this.setOrientation(1);
      this.setWillNotDraw(false);
      this.setAddStatesFromChildren(true);
      this.mInputFrame = new FrameLayout(var1);
      this.mInputFrame.setAddStatesFromChildren(true);
      this.addView(this.mInputFrame);
      this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      this.mCollapsingTextHelper.setPositionInterpolator(new AccelerateInterpolator());
      this.mCollapsingTextHelper.setCollapsedTextGravity(8388659);
      TintTypedArray var6 = TintTypedArray.obtainStyledAttributes(var1, var2, R.styleable.TextInputLayout, var3, R.style.Widget_Design_TextInputLayout);
      this.mHintEnabled = var6.getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
      this.setHint(var6.getText(R.styleable.TextInputLayout_android_hint));
      this.mHintAnimationEnabled = var6.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
      if (var6.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
         ColorStateList var7 = var6.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
         this.mFocusedTextColor = var7;
         this.mDefaultTextColor = var7;
      }

      if (var6.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1) != -1) {
         this.setHintTextAppearance(var6.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0));
      }

      this.mErrorTextAppearance = var6.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
      boolean var4 = var6.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
      boolean var5 = var6.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
      this.setCounterMaxLength(var6.getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
      this.mCounterTextAppearance = var6.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
      this.mCounterOverflowTextAppearance = var6.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
      this.mPasswordToggleEnabled = var6.getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false);
      this.mPasswordToggleDrawable = var6.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable);
      this.mPasswordToggleContentDesc = var6.getText(R.styleable.TextInputLayout_passwordToggleContentDescription);
      if (var6.hasValue(R.styleable.TextInputLayout_passwordToggleTint)) {
         this.mHasPasswordToggleTintList = true;
         this.mPasswordToggleTintList = var6.getColorStateList(R.styleable.TextInputLayout_passwordToggleTint);
      }

      if (var6.hasValue(R.styleable.TextInputLayout_passwordToggleTintMode)) {
         this.mHasPasswordToggleTintMode = true;
         this.mPasswordToggleTintMode = ViewUtils.parseTintMode(var6.getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), (Mode)null);
      }

      var6.recycle();
      this.setErrorEnabled(var4);
      this.setCounterEnabled(var5);
      this.applyPasswordToggleTint();
      if (ViewCompat.getImportantForAccessibility(this) == 0) {
         ViewCompat.setImportantForAccessibility(this, 1);
      }

      ViewCompat.setAccessibilityDelegate(this, new TextInputLayout.TextInputAccessibilityDelegate());
   }

   private void addIndicator(TextView var1, int var2) {
      if (this.mIndicatorArea == null) {
         this.mIndicatorArea = new LinearLayout(this.getContext());
         this.mIndicatorArea.setOrientation(0);
         this.addView(this.mIndicatorArea, -1, -2);
         Space var3 = new Space(this.getContext());
         LayoutParams var4 = new LayoutParams(0, 0, 1.0F);
         this.mIndicatorArea.addView(var3, var4);
         if (this.mEditText != null) {
            this.adjustIndicatorPadding();
         }
      }

      this.mIndicatorArea.setVisibility(0);
      this.mIndicatorArea.addView(var1, var2);
      ++this.mIndicatorsAdded;
   }

   private void adjustIndicatorPadding() {
      ViewCompat.setPaddingRelative(this.mIndicatorArea, ViewCompat.getPaddingStart(this.mEditText), 0, ViewCompat.getPaddingEnd(this.mEditText), this.mEditText.getPaddingBottom());
   }

   private void applyPasswordToggleTint() {
      if (this.mPasswordToggleDrawable != null && (this.mHasPasswordToggleTintList || this.mHasPasswordToggleTintMode)) {
         this.mPasswordToggleDrawable = DrawableCompat.wrap(this.mPasswordToggleDrawable).mutate();
         if (this.mHasPasswordToggleTintList) {
            DrawableCompat.setTintList(this.mPasswordToggleDrawable, this.mPasswordToggleTintList);
         }

         if (this.mHasPasswordToggleTintMode) {
            DrawableCompat.setTintMode(this.mPasswordToggleDrawable, this.mPasswordToggleTintMode);
         }

         if (this.mPasswordToggleView != null && this.mPasswordToggleView.getDrawable() != this.mPasswordToggleDrawable) {
            this.mPasswordToggleView.setImageDrawable(this.mPasswordToggleDrawable);
         }
      }

   }

   private static boolean arrayContains(int[] var0, int var1) {
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var0[var3] == var1) {
            return true;
         }
      }

      return false;
   }

   private void collapseHint(boolean var1) {
      if (this.mAnimator != null && this.mAnimator.isRunning()) {
         this.mAnimator.cancel();
      }

      if (var1 && this.mHintAnimationEnabled) {
         this.animateToExpansionFraction(1.0F);
      } else {
         this.mCollapsingTextHelper.setExpansionFraction(1.0F);
      }

      this.mHintExpanded = false;
   }

   private void ensureBackgroundDrawableStateWorkaround() {
      int var1 = VERSION.SDK_INT;
      if (var1 == 21 || var1 == 22) {
         Drawable var2 = this.mEditText.getBackground();
         if (var2 != null) {
            if (!this.mHasReconstructedEditTextBackground) {
               Drawable var3 = var2.getConstantState().newDrawable();
               if (var2 instanceof DrawableContainer) {
                  this.mHasReconstructedEditTextBackground = DrawableUtils.setContainerConstantState((DrawableContainer)var2, var3.getConstantState());
               }

               if (!this.mHasReconstructedEditTextBackground) {
                  ViewCompat.setBackground(this.mEditText, var3);
                  this.mHasReconstructedEditTextBackground = true;
               }
            }

         }
      }
   }

   private void expandHint(boolean var1) {
      if (this.mAnimator != null && this.mAnimator.isRunning()) {
         this.mAnimator.cancel();
      }

      if (var1 && this.mHintAnimationEnabled) {
         this.animateToExpansionFraction(0.0F);
      } else {
         this.mCollapsingTextHelper.setExpansionFraction(0.0F);
      }

      this.mHintExpanded = true;
   }

   private boolean hasPasswordTransformation() {
      boolean var1;
      if (this.mEditText != null && this.mEditText.getTransformationMethod() instanceof PasswordTransformationMethod) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void passwordVisibilityToggleRequested(boolean var1) {
      if (this.mPasswordToggleEnabled) {
         int var2 = this.mEditText.getSelectionEnd();
         if (this.hasPasswordTransformation()) {
            this.mEditText.setTransformationMethod((TransformationMethod)null);
            this.mPasswordToggledVisible = true;
         } else {
            this.mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.mPasswordToggledVisible = false;
         }

         this.mPasswordToggleView.setChecked(this.mPasswordToggledVisible);
         if (var1) {
            this.mPasswordToggleView.jumpDrawablesToCurrentState();
         }

         this.mEditText.setSelection(var2);
      }

   }

   private static void recursiveSetEnabled(ViewGroup var0, boolean var1) {
      int var2 = var0.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = var0.getChildAt(var3);
         var4.setEnabled(var1);
         if (var4 instanceof ViewGroup) {
            recursiveSetEnabled((ViewGroup)var4, var1);
         }
      }

   }

   private void removeIndicator(TextView var1) {
      if (this.mIndicatorArea != null) {
         this.mIndicatorArea.removeView(var1);
         int var2 = this.mIndicatorsAdded - 1;
         this.mIndicatorsAdded = var2;
         if (var2 == 0) {
            this.mIndicatorArea.setVisibility(8);
         }
      }

   }

   private void setEditText(EditText var1) {
      if (this.mEditText != null) {
         throw new IllegalArgumentException("We already have an EditText, can only have one");
      } else {
         if (!(var1 instanceof TextInputEditText)) {
            Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead.");
         }

         this.mEditText = var1;
         if (!this.hasPasswordTransformation()) {
            this.mCollapsingTextHelper.setTypefaces(this.mEditText.getTypeface());
         }

         this.mCollapsingTextHelper.setExpandedTextSize(this.mEditText.getTextSize());
         int var2 = this.mEditText.getGravity();
         this.mCollapsingTextHelper.setCollapsedTextGravity(48 | var2 & -113);
         this.mCollapsingTextHelper.setExpandedTextGravity(var2);
         this.mEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               TextInputLayout.this.updateLabelState(TextInputLayout.this.mRestoringSavedState ^ true);
               if (TextInputLayout.this.mCounterEnabled) {
                  TextInputLayout.this.updateCounter(var1.length());
               }

            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         if (this.mDefaultTextColor == null) {
            this.mDefaultTextColor = this.mEditText.getHintTextColors();
         }

         if (this.mHintEnabled && TextUtils.isEmpty(this.mHint)) {
            this.mOriginalHint = this.mEditText.getHint();
            this.setHint(this.mOriginalHint);
            this.mEditText.setHint((CharSequence)null);
         }

         if (this.mCounterView != null) {
            this.updateCounter(this.mEditText.getText().length());
         }

         if (this.mIndicatorArea != null) {
            this.adjustIndicatorPadding();
         }

         this.updatePasswordToggleView();
         this.updateLabelState(false, true);
      }
   }

   private void setError(@Nullable final CharSequence var1, boolean var2) {
      this.mError = var1;
      if (!this.mErrorEnabled) {
         if (TextUtils.isEmpty(var1)) {
            return;
         }

         this.setErrorEnabled(true);
      }

      this.mErrorShown = TextUtils.isEmpty(var1) ^ true;
      this.mErrorView.animate().cancel();
      if (this.mErrorShown) {
         this.mErrorView.setText(var1);
         this.mErrorView.setVisibility(0);
         if (var2) {
            if (this.mErrorView.getAlpha() == 1.0F) {
               this.mErrorView.setAlpha(0.0F);
            }

            this.mErrorView.animate().alpha(1.0F).setDuration(200L).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
               public void onAnimationStart(Animator var1) {
                  TextInputLayout.this.mErrorView.setVisibility(0);
               }
            }).start();
         } else {
            this.mErrorView.setAlpha(1.0F);
         }
      } else if (this.mErrorView.getVisibility() == 0) {
         if (var2) {
            this.mErrorView.animate().alpha(0.0F).setDuration(200L).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  TextInputLayout.this.mErrorView.setText(var1);
                  TextInputLayout.this.mErrorView.setVisibility(4);
               }
            }).start();
         } else {
            this.mErrorView.setText(var1);
            this.mErrorView.setVisibility(4);
         }
      }

      this.updateEditTextBackground();
      this.updateLabelState(var2);
   }

   private void setHintInternal(CharSequence var1) {
      this.mHint = var1;
      this.mCollapsingTextHelper.setText(var1);
   }

   private boolean shouldShowPasswordIcon() {
      boolean var1;
      if (!this.mPasswordToggleEnabled || !this.hasPasswordTransformation() && !this.mPasswordToggledVisible) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private void updateEditTextBackground() {
      if (this.mEditText != null) {
         Drawable var1 = this.mEditText.getBackground();
         if (var1 != null) {
            this.ensureBackgroundDrawableStateWorkaround();
            Drawable var2 = var1;
            if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(var1)) {
               var2 = var1.mutate();
            }

            if (this.mErrorShown && this.mErrorView != null) {
               var2.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(this.mErrorView.getCurrentTextColor(), Mode.SRC_IN));
            } else if (this.mCounterOverflowed && this.mCounterView != null) {
               var2.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(this.mCounterView.getCurrentTextColor(), Mode.SRC_IN));
            } else {
               DrawableCompat.clearColorFilter(var2);
               this.mEditText.refreshDrawableState();
            }

         }
      }
   }

   private void updateInputLayoutMargins() {
      LayoutParams var1 = (LayoutParams)this.mInputFrame.getLayoutParams();
      int var2;
      if (this.mHintEnabled) {
         if (this.mTmpPaint == null) {
            this.mTmpPaint = new Paint();
         }

         this.mTmpPaint.setTypeface(this.mCollapsingTextHelper.getCollapsedTypeface());
         this.mTmpPaint.setTextSize(this.mCollapsingTextHelper.getCollapsedTextSize());
         var2 = (int)(-this.mTmpPaint.ascent());
      } else {
         var2 = 0;
      }

      if (var2 != var1.topMargin) {
         var1.topMargin = var2;
         this.mInputFrame.requestLayout();
      }

   }

   private void updatePasswordToggleView() {
      if (this.mEditText != null) {
         Drawable[] var1;
         if (this.shouldShowPasswordIcon()) {
            if (this.mPasswordToggleView == null) {
               this.mPasswordToggleView = (CheckableImageButton)LayoutInflater.from(this.getContext()).inflate(R.layout.design_text_input_password_icon, this.mInputFrame, false);
               this.mPasswordToggleView.setImageDrawable(this.mPasswordToggleDrawable);
               this.mPasswordToggleView.setContentDescription(this.mPasswordToggleContentDesc);
               this.mInputFrame.addView(this.mPasswordToggleView);
               this.mPasswordToggleView.setOnClickListener(new OnClickListener() {
                  public void onClick(View var1) {
                     TextInputLayout.this.passwordVisibilityToggleRequested(false);
                  }
               });
            }

            if (this.mEditText != null && ViewCompat.getMinimumHeight(this.mEditText) <= 0) {
               this.mEditText.setMinimumHeight(ViewCompat.getMinimumHeight(this.mPasswordToggleView));
            }

            this.mPasswordToggleView.setVisibility(0);
            this.mPasswordToggleView.setChecked(this.mPasswordToggledVisible);
            if (this.mPasswordToggleDummyDrawable == null) {
               this.mPasswordToggleDummyDrawable = new ColorDrawable();
            }

            this.mPasswordToggleDummyDrawable.setBounds(0, 0, this.mPasswordToggleView.getMeasuredWidth(), 1);
            var1 = TextViewCompat.getCompoundDrawablesRelative(this.mEditText);
            if (var1[2] != this.mPasswordToggleDummyDrawable) {
               this.mOriginalEditTextEndDrawable = var1[2];
            }

            TextViewCompat.setCompoundDrawablesRelative(this.mEditText, var1[0], var1[1], this.mPasswordToggleDummyDrawable, var1[3]);
            this.mPasswordToggleView.setPadding(this.mEditText.getPaddingLeft(), this.mEditText.getPaddingTop(), this.mEditText.getPaddingRight(), this.mEditText.getPaddingBottom());
         } else {
            if (this.mPasswordToggleView != null && this.mPasswordToggleView.getVisibility() == 0) {
               this.mPasswordToggleView.setVisibility(8);
            }

            if (this.mPasswordToggleDummyDrawable != null) {
               var1 = TextViewCompat.getCompoundDrawablesRelative(this.mEditText);
               if (var1[2] == this.mPasswordToggleDummyDrawable) {
                  TextViewCompat.setCompoundDrawablesRelative(this.mEditText, var1[0], var1[1], this.mOriginalEditTextEndDrawable, var1[3]);
                  this.mPasswordToggleDummyDrawable = null;
               }
            }
         }

      }
   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      if (var1 instanceof EditText) {
         android.widget.FrameLayout.LayoutParams var4 = new android.widget.FrameLayout.LayoutParams(var3);
         var4.gravity = 16 | var4.gravity & -113;
         this.mInputFrame.addView(var1, var4);
         this.mInputFrame.setLayoutParams(var3);
         this.updateInputLayoutMargins();
         this.setEditText((EditText)var1);
      } else {
         super.addView(var1, var2, var3);
      }

   }

   @VisibleForTesting
   void animateToExpansionFraction(float var1) {
      if (this.mCollapsingTextHelper.getExpansionFraction() != var1) {
         if (this.mAnimator == null) {
            this.mAnimator = new ValueAnimator();
            this.mAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
            this.mAnimator.setDuration(200L);
            this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
               public void onAnimationUpdate(ValueAnimator var1) {
                  TextInputLayout.this.mCollapsingTextHelper.setExpansionFraction((Float)var1.getAnimatedValue());
               }
            });
         }

         this.mAnimator.setFloatValues(new float[]{this.mCollapsingTextHelper.getExpansionFraction(), var1});
         this.mAnimator.start();
      }
   }

   public void dispatchProvideAutofillStructure(ViewStructure var1, int var2) {
      if (this.mOriginalHint != null && this.mEditText != null) {
         CharSequence var3 = this.mEditText.getHint();
         this.mEditText.setHint(this.mOriginalHint);

         try {
            super.dispatchProvideAutofillStructure(var1, var2);
         } finally {
            this.mEditText.setHint(var3);
         }

      } else {
         super.dispatchProvideAutofillStructure(var1, var2);
      }
   }

   protected void dispatchRestoreInstanceState(SparseArray var1) {
      this.mRestoringSavedState = true;
      super.dispatchRestoreInstanceState(var1);
      this.mRestoringSavedState = false;
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      if (this.mHintEnabled) {
         this.mCollapsingTextHelper.draw(var1);
      }

   }

   protected void drawableStateChanged() {
      if (!this.mInDrawableStateChanged) {
         boolean var1 = true;
         this.mInDrawableStateChanged = true;
         super.drawableStateChanged();
         int[] var2 = this.getDrawableState();
         if (!ViewCompat.isLaidOut(this) || !this.isEnabled()) {
            var1 = false;
         }

         this.updateLabelState(var1);
         this.updateEditTextBackground();
         boolean var3;
         if (this.mCollapsingTextHelper != null) {
            var3 = this.mCollapsingTextHelper.setState(var2) | false;
         } else {
            var3 = false;
         }

         if (var3) {
            this.invalidate();
         }

         this.mInDrawableStateChanged = false;
      }
   }

   public int getCounterMaxLength() {
      return this.mCounterMaxLength;
   }

   @Nullable
   public EditText getEditText() {
      return this.mEditText;
   }

   @Nullable
   public CharSequence getError() {
      CharSequence var1;
      if (this.mErrorEnabled) {
         var1 = this.mError;
      } else {
         var1 = null;
      }

      return var1;
   }

   @Nullable
   public CharSequence getHint() {
      CharSequence var1;
      if (this.mHintEnabled) {
         var1 = this.mHint;
      } else {
         var1 = null;
      }

      return var1;
   }

   @Nullable
   public CharSequence getPasswordVisibilityToggleContentDescription() {
      return this.mPasswordToggleContentDesc;
   }

   @Nullable
   public Drawable getPasswordVisibilityToggleDrawable() {
      return this.mPasswordToggleDrawable;
   }

   @NonNull
   public Typeface getTypeface() {
      return this.mTypeface;
   }

   public boolean isCounterEnabled() {
      return this.mCounterEnabled;
   }

   public boolean isErrorEnabled() {
      return this.mErrorEnabled;
   }

   public boolean isHintAnimationEnabled() {
      return this.mHintAnimationEnabled;
   }

   public boolean isHintEnabled() {
      return this.mHintEnabled;
   }

   @VisibleForTesting
   final boolean isHintExpanded() {
      return this.mHintExpanded;
   }

   public boolean isPasswordVisibilityToggleEnabled() {
      return this.mPasswordToggleEnabled;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (this.mHintEnabled && this.mEditText != null) {
         Rect var6 = this.mTmpRect;
         ViewGroupUtils.getDescendantRect(this, this.mEditText, var6);
         var2 = var6.left + this.mEditText.getCompoundPaddingLeft();
         var4 = var6.right - this.mEditText.getCompoundPaddingRight();
         this.mCollapsingTextHelper.setExpandedBounds(var2, var6.top + this.mEditText.getCompoundPaddingTop(), var4, var6.bottom - this.mEditText.getCompoundPaddingBottom());
         this.mCollapsingTextHelper.setCollapsedBounds(var2, this.getPaddingTop(), var4, var5 - var3 - this.getPaddingBottom());
         this.mCollapsingTextHelper.recalculate();
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.updatePasswordToggleView();
      super.onMeasure(var1, var2);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof TextInputLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         TextInputLayout.SavedState var2 = (TextInputLayout.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.setError(var2.error);
         if (var2.isPasswordToggledVisible) {
            this.passwordVisibilityToggleRequested(true);
         }

         this.requestLayout();
      }
   }

   public Parcelable onSaveInstanceState() {
      TextInputLayout.SavedState var1 = new TextInputLayout.SavedState(super.onSaveInstanceState());
      if (this.mErrorShown) {
         var1.error = this.getError();
      }

      var1.isPasswordToggledVisible = this.mPasswordToggledVisible;
      return var1;
   }

   public void setCounterEnabled(boolean var1) {
      if (this.mCounterEnabled != var1) {
         if (var1) {
            this.mCounterView = new AppCompatTextView(this.getContext());
            this.mCounterView.setId(R.id.textinput_counter);
            if (this.mTypeface != null) {
               this.mCounterView.setTypeface(this.mTypeface);
            }

            this.mCounterView.setMaxLines(1);

            try {
               TextViewCompat.setTextAppearance(this.mCounterView, this.mCounterTextAppearance);
            } catch (Exception var3) {
               TextViewCompat.setTextAppearance(this.mCounterView, android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Caption);
               this.mCounterView.setTextColor(ContextCompat.getColor(this.getContext(), android.support.v7.appcompat.R.color.error_color_material));
            }

            this.addIndicator(this.mCounterView, -1);
            if (this.mEditText == null) {
               this.updateCounter(0);
            } else {
               this.updateCounter(this.mEditText.getText().length());
            }
         } else {
            this.removeIndicator(this.mCounterView);
            this.mCounterView = null;
         }

         this.mCounterEnabled = var1;
      }

   }

   public void setCounterMaxLength(int var1) {
      if (this.mCounterMaxLength != var1) {
         if (var1 > 0) {
            this.mCounterMaxLength = var1;
         } else {
            this.mCounterMaxLength = -1;
         }

         if (this.mCounterEnabled) {
            if (this.mEditText == null) {
               var1 = 0;
            } else {
               var1 = this.mEditText.getText().length();
            }

            this.updateCounter(var1);
         }
      }

   }

   public void setEnabled(boolean var1) {
      recursiveSetEnabled(this, var1);
      super.setEnabled(var1);
   }

   public void setError(@Nullable CharSequence var1) {
      boolean var2;
      if (!ViewCompat.isLaidOut(this) || !this.isEnabled() || this.mErrorView != null && TextUtils.equals(this.mErrorView.getText(), var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      this.setError(var1, var2);
   }

   public void setErrorEnabled(boolean var1) {
      if (this.mErrorEnabled != var1) {
         if (this.mErrorView != null) {
            this.mErrorView.animate().cancel();
         }

         if (var1) {
            this.mErrorView = new AppCompatTextView(this.getContext());
            this.mErrorView.setId(R.id.textinput_error);
            if (this.mTypeface != null) {
               this.mErrorView.setTypeface(this.mTypeface);
            }

            boolean var5;
            label39: {
               label38: {
                  label37: {
                     int var2;
                     try {
                        TextViewCompat.setTextAppearance(this.mErrorView, this.mErrorTextAppearance);
                        if (VERSION.SDK_INT < 23) {
                           break label38;
                        }

                        var2 = this.mErrorView.getTextColors().getDefaultColor();
                     } catch (Exception var4) {
                        break label37;
                     }

                     if (var2 != -65281) {
                        break label38;
                     }
                  }

                  var5 = true;
                  break label39;
               }

               var5 = false;
            }

            if (var5) {
               TextViewCompat.setTextAppearance(this.mErrorView, android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Caption);
               this.mErrorView.setTextColor(ContextCompat.getColor(this.getContext(), android.support.v7.appcompat.R.color.error_color_material));
            }

            this.mErrorView.setVisibility(4);
            ViewCompat.setAccessibilityLiveRegion(this.mErrorView, 1);
            this.addIndicator(this.mErrorView, 0);
         } else {
            this.mErrorShown = false;
            this.updateEditTextBackground();
            this.removeIndicator(this.mErrorView);
            this.mErrorView = null;
         }

         this.mErrorEnabled = var1;
      }

   }

   public void setErrorTextAppearance(@StyleRes int var1) {
      this.mErrorTextAppearance = var1;
      if (this.mErrorView != null) {
         TextViewCompat.setTextAppearance(this.mErrorView, var1);
      }

   }

   public void setHint(@Nullable CharSequence var1) {
      if (this.mHintEnabled) {
         this.setHintInternal(var1);
         this.sendAccessibilityEvent(2048);
      }

   }

   public void setHintAnimationEnabled(boolean var1) {
      this.mHintAnimationEnabled = var1;
   }

   public void setHintEnabled(boolean var1) {
      if (var1 != this.mHintEnabled) {
         this.mHintEnabled = var1;
         CharSequence var2 = this.mEditText.getHint();
         if (!this.mHintEnabled) {
            if (!TextUtils.isEmpty(this.mHint) && TextUtils.isEmpty(var2)) {
               this.mEditText.setHint(this.mHint);
            }

            this.setHintInternal((CharSequence)null);
         } else if (!TextUtils.isEmpty(var2)) {
            if (TextUtils.isEmpty(this.mHint)) {
               this.setHint(var2);
            }

            this.mEditText.setHint((CharSequence)null);
         }

         if (this.mEditText != null) {
            this.updateInputLayoutMargins();
         }
      }

   }

   public void setHintTextAppearance(@StyleRes int var1) {
      this.mCollapsingTextHelper.setCollapsedTextAppearance(var1);
      this.mFocusedTextColor = this.mCollapsingTextHelper.getCollapsedTextColor();
      if (this.mEditText != null) {
         this.updateLabelState(false);
         this.updateInputLayoutMargins();
      }

   }

   public void setPasswordVisibilityToggleContentDescription(@StringRes int var1) {
      CharSequence var2;
      if (var1 != 0) {
         var2 = this.getResources().getText(var1);
      } else {
         var2 = null;
      }

      this.setPasswordVisibilityToggleContentDescription(var2);
   }

   public void setPasswordVisibilityToggleContentDescription(@Nullable CharSequence var1) {
      this.mPasswordToggleContentDesc = var1;
      if (this.mPasswordToggleView != null) {
         this.mPasswordToggleView.setContentDescription(var1);
      }

   }

   public void setPasswordVisibilityToggleDrawable(@DrawableRes int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setPasswordVisibilityToggleDrawable(var2);
   }

   public void setPasswordVisibilityToggleDrawable(@Nullable Drawable var1) {
      this.mPasswordToggleDrawable = var1;
      if (this.mPasswordToggleView != null) {
         this.mPasswordToggleView.setImageDrawable(var1);
      }

   }

   public void setPasswordVisibilityToggleEnabled(boolean var1) {
      if (this.mPasswordToggleEnabled != var1) {
         this.mPasswordToggleEnabled = var1;
         if (!var1 && this.mPasswordToggledVisible && this.mEditText != null) {
            this.mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
         }

         this.mPasswordToggledVisible = false;
         this.updatePasswordToggleView();
      }

   }

   public void setPasswordVisibilityToggleTintList(@Nullable ColorStateList var1) {
      this.mPasswordToggleTintList = var1;
      this.mHasPasswordToggleTintList = true;
      this.applyPasswordToggleTint();
   }

   public void setPasswordVisibilityToggleTintMode(@Nullable Mode var1) {
      this.mPasswordToggleTintMode = var1;
      this.mHasPasswordToggleTintMode = true;
      this.applyPasswordToggleTint();
   }

   public void setTypeface(@Nullable Typeface var1) {
      if (this.mTypeface != null && !this.mTypeface.equals(var1) || this.mTypeface == null && var1 != null) {
         this.mTypeface = var1;
         this.mCollapsingTextHelper.setTypefaces(var1);
         if (this.mCounterView != null) {
            this.mCounterView.setTypeface(var1);
         }

         if (this.mErrorView != null) {
            this.mErrorView.setTypeface(var1);
         }
      }

   }

   void updateCounter(int var1) {
      boolean var2 = this.mCounterOverflowed;
      if (this.mCounterMaxLength == -1) {
         this.mCounterView.setText(String.valueOf(var1));
         this.mCounterOverflowed = false;
      } else {
         boolean var3;
         if (var1 > this.mCounterMaxLength) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mCounterOverflowed = var3;
         if (var2 != this.mCounterOverflowed) {
            TextView var4 = this.mCounterView;
            int var5;
            if (this.mCounterOverflowed) {
               var5 = this.mCounterOverflowTextAppearance;
            } else {
               var5 = this.mCounterTextAppearance;
            }

            TextViewCompat.setTextAppearance(var4, var5);
         }

         this.mCounterView.setText(this.getContext().getString(R.string.character_counter_pattern, new Object[]{var1, this.mCounterMaxLength}));
      }

      if (this.mEditText != null && var2 != this.mCounterOverflowed) {
         this.updateLabelState(false);
         this.updateEditTextBackground();
      }

   }

   void updateLabelState(boolean var1) {
      this.updateLabelState(var1, false);
   }

   void updateLabelState(boolean var1, boolean var2) {
      boolean var3 = this.isEnabled();
      boolean var4;
      if (this.mEditText != null && !TextUtils.isEmpty(this.mEditText.getText())) {
         var4 = true;
      } else {
         var4 = false;
      }

      boolean var5 = arrayContains(this.getDrawableState(), 16842908);
      boolean var6 = TextUtils.isEmpty(this.getError());
      if (this.mDefaultTextColor != null) {
         this.mCollapsingTextHelper.setExpandedTextColor(this.mDefaultTextColor);
      }

      if (var3 && this.mCounterOverflowed && this.mCounterView != null) {
         this.mCollapsingTextHelper.setCollapsedTextColor(this.mCounterView.getTextColors());
      } else if (var3 && var5 && this.mFocusedTextColor != null) {
         this.mCollapsingTextHelper.setCollapsedTextColor(this.mFocusedTextColor);
      } else if (this.mDefaultTextColor != null) {
         this.mCollapsingTextHelper.setCollapsedTextColor(this.mDefaultTextColor);
      }

      if (!var4 && (!this.isEnabled() || !var5 && !(true ^ var6))) {
         if (var2 || !this.mHintExpanded) {
            this.expandHint(var1);
         }
      } else if (var2 || this.mHintExpanded) {
         this.collapseHint(var1);
      }

   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public TextInputLayout.SavedState createFromParcel(Parcel var1) {
            return new TextInputLayout.SavedState(var1, (ClassLoader)null);
         }

         public TextInputLayout.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new TextInputLayout.SavedState(var1, var2);
         }

         public TextInputLayout.SavedState[] newArray(int var1) {
            return new TextInputLayout.SavedState[var1];
         }
      };
      CharSequence error;
      boolean isPasswordToggledVisible;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
         int var3 = var1.readInt();
         boolean var4 = true;
         if (var3 != 1) {
            var4 = false;
         }

         this.isPasswordToggledVisible = var4;
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("TextInputLayout.SavedState{");
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append(" error=");
         var1.append(this.error);
         var1.append("}");
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         TextUtils.writeToParcel(this.error, var1, var2);
         var1.writeInt(this.isPasswordToggledVisible);
      }
   }

   private class TextInputAccessibilityDelegate extends AccessibilityDelegateCompat {
      TextInputAccessibilityDelegate() {
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(TextInputLayout.class.getSimpleName());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         var2.setClassName(TextInputLayout.class.getSimpleName());
         CharSequence var3 = TextInputLayout.this.mCollapsingTextHelper.getText();
         if (!TextUtils.isEmpty(var3)) {
            var2.setText(var3);
         }

         if (TextInputLayout.this.mEditText != null) {
            var2.setLabelFor(TextInputLayout.this.mEditText);
         }

         if (TextInputLayout.this.mErrorView != null) {
            var3 = TextInputLayout.this.mErrorView.getText();
         } else {
            var3 = null;
         }

         if (!TextUtils.isEmpty(var3)) {
            var2.setContentInvalid(true);
            var2.setError(var3);
         }

      }

      public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onPopulateAccessibilityEvent(var1, var2);
         CharSequence var3 = TextInputLayout.this.mCollapsingTextHelper.getText();
         if (!TextUtils.isEmpty(var3)) {
            var2.getText().add(var3);
         }

      }
   }
}
