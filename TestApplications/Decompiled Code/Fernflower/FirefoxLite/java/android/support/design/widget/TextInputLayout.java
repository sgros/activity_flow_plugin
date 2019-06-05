package android.support.design.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.design.R;
import android.support.design.animation.AnimationUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.internal.ViewUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class TextInputLayout extends LinearLayout {
   private ValueAnimator animator;
   private GradientDrawable boxBackground;
   private int boxBackgroundColor;
   private int boxBackgroundMode;
   private final int boxBottomOffsetPx;
   private final int boxCollapsedPaddingTopPx;
   private float boxCornerRadiusBottomEnd;
   private float boxCornerRadiusBottomStart;
   private float boxCornerRadiusTopEnd;
   private float boxCornerRadiusTopStart;
   private final int boxLabelCutoutPaddingPx;
   private int boxStrokeColor;
   private final int boxStrokeWidthDefaultPx;
   private final int boxStrokeWidthFocusedPx;
   private int boxStrokeWidthPx;
   final CollapsingTextHelper collapsingTextHelper;
   boolean counterEnabled;
   private int counterMaxLength;
   private final int counterOverflowTextAppearance;
   private boolean counterOverflowed;
   private final int counterTextAppearance;
   private TextView counterView;
   private ColorStateList defaultHintTextColor;
   private final int defaultStrokeColor;
   private final int disabledColor;
   EditText editText;
   private Drawable editTextOriginalDrawable;
   private int focusedStrokeColor;
   private ColorStateList focusedTextColor;
   private boolean hasPasswordToggleTintList;
   private boolean hasPasswordToggleTintMode;
   private boolean hasReconstructedEditTextBackground;
   private CharSequence hint;
   private boolean hintAnimationEnabled;
   private boolean hintEnabled;
   private boolean hintExpanded;
   private final int hoveredStrokeColor;
   private boolean inDrawableStateChanged;
   private final IndicatorViewController indicatorViewController;
   private final FrameLayout inputFrame;
   private boolean isProvidingHint;
   private Drawable originalEditTextEndDrawable;
   private CharSequence originalHint;
   private CharSequence passwordToggleContentDesc;
   private Drawable passwordToggleDrawable;
   private Drawable passwordToggleDummyDrawable;
   private boolean passwordToggleEnabled;
   private ColorStateList passwordToggleTintList;
   private Mode passwordToggleTintMode;
   private CheckableImageButton passwordToggleView;
   private boolean passwordToggledVisible;
   private boolean restoringSavedState;
   private final Rect tmpRect;
   private final RectF tmpRectF;
   private Typeface typeface;

   public TextInputLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TextInputLayout(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.textInputStyle);
   }

   public TextInputLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.indicatorViewController = new IndicatorViewController(this);
      this.tmpRect = new Rect();
      this.tmpRectF = new RectF();
      this.collapsingTextHelper = new CollapsingTextHelper(this);
      this.setOrientation(1);
      this.setWillNotDraw(false);
      this.setAddStatesFromChildren(true);
      this.inputFrame = new FrameLayout(var1);
      this.inputFrame.setAddStatesFromChildren(true);
      this.addView(this.inputFrame);
      this.collapsingTextHelper.setTextSizeInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
      this.collapsingTextHelper.setPositionInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
      this.collapsingTextHelper.setCollapsedTextGravity(8388659);
      TintTypedArray var10 = ThemeEnforcement.obtainTintedStyledAttributes(var1, var2, R.styleable.TextInputLayout, var3, R.style.Widget_Design_TextInputLayout);
      this.hintEnabled = var10.getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
      this.setHint(var10.getText(R.styleable.TextInputLayout_android_hint));
      this.hintAnimationEnabled = var10.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
      this.boxBottomOffsetPx = var1.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_bottom_offset);
      this.boxLabelCutoutPaddingPx = var1.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_label_cutout_padding);
      this.boxCollapsedPaddingTopPx = var10.getDimensionPixelOffset(R.styleable.TextInputLayout_boxCollapsedPaddingTop, 0);
      this.boxCornerRadiusTopStart = var10.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopStart, 0.0F);
      this.boxCornerRadiusTopEnd = var10.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopEnd, 0.0F);
      this.boxCornerRadiusBottomEnd = var10.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomEnd, 0.0F);
      this.boxCornerRadiusBottomStart = var10.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomStart, 0.0F);
      this.boxBackgroundColor = var10.getColor(R.styleable.TextInputLayout_boxBackgroundColor, 0);
      this.focusedStrokeColor = var10.getColor(R.styleable.TextInputLayout_boxStrokeColor, 0);
      this.boxStrokeWidthDefaultPx = var1.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_default);
      this.boxStrokeWidthFocusedPx = var1.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_focused);
      this.boxStrokeWidthPx = this.boxStrokeWidthDefaultPx;
      this.setBoxBackgroundMode(var10.getInt(R.styleable.TextInputLayout_boxBackgroundMode, 0));
      if (var10.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
         ColorStateList var4 = var10.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
         this.focusedTextColor = var4;
         this.defaultHintTextColor = var4;
      }

      this.defaultStrokeColor = ContextCompat.getColor(var1, R.color.mtrl_textinput_default_box_stroke_color);
      this.disabledColor = ContextCompat.getColor(var1, R.color.mtrl_textinput_disabled_color);
      this.hoveredStrokeColor = ContextCompat.getColor(var1, R.color.mtrl_textinput_hovered_box_stroke_color);
      if (var10.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1) != -1) {
         this.setHintTextAppearance(var10.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0));
      }

      var3 = var10.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
      boolean var5 = var10.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
      int var6 = var10.getResourceId(R.styleable.TextInputLayout_helperTextTextAppearance, 0);
      boolean var7 = var10.getBoolean(R.styleable.TextInputLayout_helperTextEnabled, false);
      CharSequence var9 = var10.getText(R.styleable.TextInputLayout_helperText);
      boolean var8 = var10.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
      this.setCounterMaxLength(var10.getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
      this.counterTextAppearance = var10.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
      this.counterOverflowTextAppearance = var10.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
      this.passwordToggleEnabled = var10.getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false);
      this.passwordToggleDrawable = var10.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable);
      this.passwordToggleContentDesc = var10.getText(R.styleable.TextInputLayout_passwordToggleContentDescription);
      if (var10.hasValue(R.styleable.TextInputLayout_passwordToggleTint)) {
         this.hasPasswordToggleTintList = true;
         this.passwordToggleTintList = var10.getColorStateList(R.styleable.TextInputLayout_passwordToggleTint);
      }

      if (var10.hasValue(R.styleable.TextInputLayout_passwordToggleTintMode)) {
         this.hasPasswordToggleTintMode = true;
         this.passwordToggleTintMode = ViewUtils.parseTintMode(var10.getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), (Mode)null);
      }

      var10.recycle();
      this.setHelperTextEnabled(var7);
      this.setHelperText(var9);
      this.setHelperTextTextAppearance(var6);
      this.setErrorEnabled(var5);
      this.setErrorTextAppearance(var3);
      this.setCounterEnabled(var8);
      this.applyPasswordToggleTint();
      ViewCompat.setImportantForAccessibility(this, 2);
   }

   private void applyBoxAttributes() {
      if (this.boxBackground != null) {
         this.setBoxAttributes();
         if (this.editText != null && this.boxBackgroundMode == 2) {
            if (this.editText.getBackground() != null) {
               this.editTextOriginalDrawable = this.editText.getBackground();
            }

            ViewCompat.setBackground(this.editText, (Drawable)null);
         }

         if (this.editText != null && this.boxBackgroundMode == 1 && this.editTextOriginalDrawable != null) {
            ViewCompat.setBackground(this.editText, this.editTextOriginalDrawable);
         }

         if (this.boxStrokeWidthPx > -1 && this.boxStrokeColor != 0) {
            this.boxBackground.setStroke(this.boxStrokeWidthPx, this.boxStrokeColor);
         }

         this.boxBackground.setCornerRadii(this.getCornerRadiiAsArray());
         this.boxBackground.setColor(this.boxBackgroundColor);
         this.invalidate();
      }
   }

   private void applyCutoutPadding(RectF var1) {
      var1.left -= (float)this.boxLabelCutoutPaddingPx;
      var1.top -= (float)this.boxLabelCutoutPaddingPx;
      var1.right += (float)this.boxLabelCutoutPaddingPx;
      var1.bottom += (float)this.boxLabelCutoutPaddingPx;
   }

   private void applyPasswordToggleTint() {
      if (this.passwordToggleDrawable != null && (this.hasPasswordToggleTintList || this.hasPasswordToggleTintMode)) {
         this.passwordToggleDrawable = DrawableCompat.wrap(this.passwordToggleDrawable).mutate();
         if (this.hasPasswordToggleTintList) {
            DrawableCompat.setTintList(this.passwordToggleDrawable, this.passwordToggleTintList);
         }

         if (this.hasPasswordToggleTintMode) {
            DrawableCompat.setTintMode(this.passwordToggleDrawable, this.passwordToggleTintMode);
         }

         if (this.passwordToggleView != null && this.passwordToggleView.getDrawable() != this.passwordToggleDrawable) {
            this.passwordToggleView.setImageDrawable(this.passwordToggleDrawable);
         }
      }

   }

   private void assignBoxBackgroundByMode() {
      if (this.boxBackgroundMode == 0) {
         this.boxBackground = null;
      } else if (this.boxBackgroundMode == 2 && this.hintEnabled && !(this.boxBackground instanceof CutoutDrawable)) {
         this.boxBackground = new CutoutDrawable();
      } else if (!(this.boxBackground instanceof GradientDrawable)) {
         this.boxBackground = new GradientDrawable();
      }

   }

   private int calculateBoxBackgroundTop() {
      if (this.editText == null) {
         return 0;
      } else {
         switch(this.boxBackgroundMode) {
         case 1:
            return this.editText.getTop();
         case 2:
            return this.editText.getTop() + this.calculateLabelMarginTop();
         default:
            return 0;
         }
      }
   }

   private int calculateCollapsedTextTopBounds() {
      switch(this.boxBackgroundMode) {
      case 1:
         return this.getBoxBackground().getBounds().top + this.boxCollapsedPaddingTopPx;
      case 2:
         return this.getBoxBackground().getBounds().top - this.calculateLabelMarginTop();
      default:
         return this.getPaddingTop();
      }
   }

   private int calculateLabelMarginTop() {
      if (!this.hintEnabled) {
         return 0;
      } else {
         switch(this.boxBackgroundMode) {
         case 0:
         case 1:
            return (int)this.collapsingTextHelper.getCollapsedTextHeight();
         case 2:
            return (int)(this.collapsingTextHelper.getCollapsedTextHeight() / 2.0F);
         default:
            return 0;
         }
      }
   }

   private void closeCutout() {
      if (this.cutoutEnabled()) {
         ((CutoutDrawable)this.boxBackground).removeCutout();
      }

   }

   private void collapseHint(boolean var1) {
      if (this.animator != null && this.animator.isRunning()) {
         this.animator.cancel();
      }

      if (var1 && this.hintAnimationEnabled) {
         this.animateToExpansionFraction(1.0F);
      } else {
         this.collapsingTextHelper.setExpansionFraction(1.0F);
      }

      this.hintExpanded = false;
      if (this.cutoutEnabled()) {
         this.openCutout();
      }

   }

   private boolean cutoutEnabled() {
      boolean var1;
      if (this.hintEnabled && !TextUtils.isEmpty(this.hint) && this.boxBackground instanceof CutoutDrawable) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void ensureBackgroundDrawableStateWorkaround() {
      int var1 = VERSION.SDK_INT;
      if (var1 == 21 || var1 == 22) {
         Drawable var2 = this.editText.getBackground();
         if (var2 != null) {
            if (!this.hasReconstructedEditTextBackground) {
               Drawable var3 = var2.getConstantState().newDrawable();
               if (var2 instanceof DrawableContainer) {
                  this.hasReconstructedEditTextBackground = DrawableUtils.setContainerConstantState((DrawableContainer)var2, var3.getConstantState());
               }

               if (!this.hasReconstructedEditTextBackground) {
                  ViewCompat.setBackground(this.editText, var3);
                  this.hasReconstructedEditTextBackground = true;
                  this.onApplyBoxBackgroundMode();
               }
            }

         }
      }
   }

   private void expandHint(boolean var1) {
      if (this.animator != null && this.animator.isRunning()) {
         this.animator.cancel();
      }

      if (var1 && this.hintAnimationEnabled) {
         this.animateToExpansionFraction(0.0F);
      } else {
         this.collapsingTextHelper.setExpansionFraction(0.0F);
      }

      if (this.cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout()) {
         this.closeCutout();
      }

      this.hintExpanded = true;
   }

   private Drawable getBoxBackground() {
      if (this.boxBackgroundMode != 1 && this.boxBackgroundMode != 2) {
         throw new IllegalStateException();
      } else {
         return this.boxBackground;
      }
   }

   private float[] getCornerRadiiAsArray() {
      return !ViewUtils.isLayoutRtl(this) ? new float[]{this.boxCornerRadiusTopStart, this.boxCornerRadiusTopStart, this.boxCornerRadiusTopEnd, this.boxCornerRadiusTopEnd, this.boxCornerRadiusBottomEnd, this.boxCornerRadiusBottomEnd, this.boxCornerRadiusBottomStart, this.boxCornerRadiusBottomStart} : new float[]{this.boxCornerRadiusTopEnd, this.boxCornerRadiusTopEnd, this.boxCornerRadiusTopStart, this.boxCornerRadiusTopStart, this.boxCornerRadiusBottomStart, this.boxCornerRadiusBottomStart, this.boxCornerRadiusBottomEnd, this.boxCornerRadiusBottomEnd};
   }

   private boolean hasPasswordTransformation() {
      boolean var1;
      if (this.editText != null && this.editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void onApplyBoxBackgroundMode() {
      this.assignBoxBackgroundByMode();
      if (this.boxBackgroundMode != 0) {
         this.updateInputLayoutMargins();
      }

      this.updateTextInputBoxBounds();
   }

   private void openCutout() {
      if (this.cutoutEnabled()) {
         RectF var1 = this.tmpRectF;
         this.collapsingTextHelper.getCollapsedTextActualBounds(var1);
         this.applyCutoutPadding(var1);
         ((CutoutDrawable)this.boxBackground).setCutout(var1);
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

   private void setBoxAttributes() {
      switch(this.boxBackgroundMode) {
      case 1:
         this.boxStrokeWidthPx = 0;
         break;
      case 2:
         if (this.focusedStrokeColor == 0) {
            this.focusedStrokeColor = this.focusedTextColor.getColorForState(this.getDrawableState(), this.focusedTextColor.getDefaultColor());
         }
      }

   }

   private void setEditText(EditText var1) {
      if (this.editText == null) {
         if (!(var1 instanceof TextInputEditText)) {
            Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead.");
         }

         this.editText = var1;
         this.onApplyBoxBackgroundMode();
         this.setTextInputAccessibilityDelegate(new TextInputLayout.AccessibilityDelegate(this));
         if (!this.hasPasswordTransformation()) {
            this.collapsingTextHelper.setTypefaces(this.editText.getTypeface());
         }

         this.collapsingTextHelper.setExpandedTextSize(this.editText.getTextSize());
         int var2 = this.editText.getGravity();
         this.collapsingTextHelper.setCollapsedTextGravity(var2 & -113 | 48);
         this.collapsingTextHelper.setExpandedTextGravity(var2);
         this.editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               TextInputLayout.this.updateLabelState(TextInputLayout.this.restoringSavedState ^ true);
               if (TextInputLayout.this.counterEnabled) {
                  TextInputLayout.this.updateCounter(var1.length());
               }

            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         if (this.defaultHintTextColor == null) {
            this.defaultHintTextColor = this.editText.getHintTextColors();
         }

         if (this.hintEnabled) {
            if (TextUtils.isEmpty(this.hint)) {
               this.originalHint = this.editText.getHint();
               this.setHint(this.originalHint);
               this.editText.setHint((CharSequence)null);
            }

            this.isProvidingHint = true;
         }

         if (this.counterView != null) {
            this.updateCounter(this.editText.getText().length());
         }

         this.indicatorViewController.adjustIndicatorPadding();
         this.updatePasswordToggleView();
         this.updateLabelState(false, true);
      } else {
         throw new IllegalArgumentException("We already have an EditText, can only have one");
      }
   }

   private void setHintInternal(CharSequence var1) {
      if (!TextUtils.equals(var1, this.hint)) {
         this.hint = var1;
         this.collapsingTextHelper.setText(var1);
         if (!this.hintExpanded) {
            this.openCutout();
         }
      }

   }

   private boolean shouldShowPasswordIcon() {
      boolean var1;
      if (!this.passwordToggleEnabled || !this.hasPasswordTransformation() && !this.passwordToggledVisible) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private void updateEditTextBackgroundBounds() {
      if (this.editText != null) {
         Drawable var1 = this.editText.getBackground();
         if (var1 != null) {
            Drawable var2 = var1;
            if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(var1)) {
               var2 = var1.mutate();
            }

            Rect var8 = new Rect();
            DescendantOffsetUtils.getDescendantRect(this, this.editText, var8);
            Rect var3 = var2.getBounds();
            if (var3.left != var3.right) {
               var8 = new Rect();
               var2.getPadding(var8);
               int var4 = var3.left;
               int var5 = var8.left;
               int var6 = var3.right;
               int var7 = var8.right;
               var2.setBounds(var4 - var5, var3.top, var6 + var7 * 2, this.editText.getBottom());
            }

         }
      }
   }

   private void updateInputLayoutMargins() {
      LayoutParams var1 = (LayoutParams)this.inputFrame.getLayoutParams();
      int var2 = this.calculateLabelMarginTop();
      if (var2 != var1.topMargin) {
         var1.topMargin = var2;
         this.inputFrame.requestLayout();
      }

   }

   private void updateLabelState(boolean var1, boolean var2) {
      boolean var3 = this.isEnabled();
      EditText var4 = this.editText;
      boolean var5 = true;
      boolean var6;
      if (var4 != null && !TextUtils.isEmpty(this.editText.getText())) {
         var6 = true;
      } else {
         var6 = false;
      }

      if (this.editText == null || !this.editText.hasFocus()) {
         var5 = false;
      }

      boolean var7 = this.indicatorViewController.errorShouldBeShown();
      if (this.defaultHintTextColor != null) {
         this.collapsingTextHelper.setCollapsedTextColor(this.defaultHintTextColor);
         this.collapsingTextHelper.setExpandedTextColor(this.defaultHintTextColor);
      }

      if (!var3) {
         this.collapsingTextHelper.setCollapsedTextColor(ColorStateList.valueOf(this.disabledColor));
         this.collapsingTextHelper.setExpandedTextColor(ColorStateList.valueOf(this.disabledColor));
      } else if (var7) {
         this.collapsingTextHelper.setCollapsedTextColor(this.indicatorViewController.getErrorViewTextColors());
      } else if (this.counterOverflowed && this.counterView != null) {
         this.collapsingTextHelper.setCollapsedTextColor(this.counterView.getTextColors());
      } else if (var5 && this.focusedTextColor != null) {
         this.collapsingTextHelper.setCollapsedTextColor(this.focusedTextColor);
      }

      if (!var6 && (!this.isEnabled() || !var5 && !var7)) {
         if (var2 || !this.hintExpanded) {
            this.expandHint(var1);
         }
      } else if (var2 || this.hintExpanded) {
         this.collapseHint(var1);
      }

   }

   private void updatePasswordToggleView() {
      if (this.editText != null) {
         Drawable[] var1;
         if (this.shouldShowPasswordIcon()) {
            if (this.passwordToggleView == null) {
               this.passwordToggleView = (CheckableImageButton)LayoutInflater.from(this.getContext()).inflate(R.layout.design_text_input_password_icon, this.inputFrame, false);
               this.passwordToggleView.setImageDrawable(this.passwordToggleDrawable);
               this.passwordToggleView.setContentDescription(this.passwordToggleContentDesc);
               this.inputFrame.addView(this.passwordToggleView);
               this.passwordToggleView.setOnClickListener(new OnClickListener() {
                  public void onClick(View var1) {
                     TextInputLayout.this.passwordVisibilityToggleRequested(false);
                  }
               });
            }

            if (this.editText != null && ViewCompat.getMinimumHeight(this.editText) <= 0) {
               this.editText.setMinimumHeight(ViewCompat.getMinimumHeight(this.passwordToggleView));
            }

            this.passwordToggleView.setVisibility(0);
            this.passwordToggleView.setChecked(this.passwordToggledVisible);
            if (this.passwordToggleDummyDrawable == null) {
               this.passwordToggleDummyDrawable = new ColorDrawable();
            }

            this.passwordToggleDummyDrawable.setBounds(0, 0, this.passwordToggleView.getMeasuredWidth(), 1);
            var1 = TextViewCompat.getCompoundDrawablesRelative(this.editText);
            if (var1[2] != this.passwordToggleDummyDrawable) {
               this.originalEditTextEndDrawable = var1[2];
            }

            TextViewCompat.setCompoundDrawablesRelative(this.editText, var1[0], var1[1], this.passwordToggleDummyDrawable, var1[3]);
            this.passwordToggleView.setPadding(this.editText.getPaddingLeft(), this.editText.getPaddingTop(), this.editText.getPaddingRight(), this.editText.getPaddingBottom());
         } else {
            if (this.passwordToggleView != null && this.passwordToggleView.getVisibility() == 0) {
               this.passwordToggleView.setVisibility(8);
            }

            if (this.passwordToggleDummyDrawable != null) {
               var1 = TextViewCompat.getCompoundDrawablesRelative(this.editText);
               if (var1[2] == this.passwordToggleDummyDrawable) {
                  TextViewCompat.setCompoundDrawablesRelative(this.editText, var1[0], var1[1], this.originalEditTextEndDrawable, var1[3]);
                  this.passwordToggleDummyDrawable = null;
               }
            }
         }

      }
   }

   private void updateTextInputBoxBounds() {
      if (this.boxBackgroundMode != 0 && this.boxBackground != null && this.editText != null && this.getRight() != 0) {
         int var1 = this.editText.getLeft();
         int var2 = this.calculateBoxBackgroundTop();
         int var3 = this.editText.getRight();
         int var4 = this.editText.getBottom() + this.boxBottomOffsetPx;
         int var5 = var1;
         int var6 = var2;
         int var7 = var3;
         int var8 = var4;
         if (this.boxBackgroundMode == 2) {
            var5 = var1 + this.boxStrokeWidthFocusedPx / 2;
            var6 = var2 - this.boxStrokeWidthFocusedPx / 2;
            var7 = var3 - this.boxStrokeWidthFocusedPx / 2;
            var8 = var4 + this.boxStrokeWidthFocusedPx / 2;
         }

         this.boxBackground.setBounds(var5, var6, var7, var8);
         this.applyBoxAttributes();
         this.updateEditTextBackgroundBounds();
      }
   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      if (var1 instanceof EditText) {
         android.widget.FrameLayout.LayoutParams var4 = new android.widget.FrameLayout.LayoutParams(var3);
         var4.gravity = var4.gravity & -113 | 16;
         this.inputFrame.addView(var1, var4);
         this.inputFrame.setLayoutParams(var3);
         this.updateInputLayoutMargins();
         this.setEditText((EditText)var1);
      } else {
         super.addView(var1, var2, var3);
      }

   }

   void animateToExpansionFraction(float var1) {
      if (this.collapsingTextHelper.getExpansionFraction() != var1) {
         if (this.animator == null) {
            this.animator = new ValueAnimator();
            this.animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.animator.setDuration(167L);
            this.animator.addUpdateListener(new AnimatorUpdateListener() {
               public void onAnimationUpdate(ValueAnimator var1) {
                  TextInputLayout.this.collapsingTextHelper.setExpansionFraction((Float)var1.getAnimatedValue());
               }
            });
         }

         this.animator.setFloatValues(new float[]{this.collapsingTextHelper.getExpansionFraction(), var1});
         this.animator.start();
      }
   }

   public void dispatchProvideAutofillStructure(ViewStructure var1, int var2) {
      if (this.originalHint != null && this.editText != null) {
         boolean var3 = this.isProvidingHint;
         this.isProvidingHint = false;
         CharSequence var4 = this.editText.getHint();
         this.editText.setHint(this.originalHint);

         try {
            super.dispatchProvideAutofillStructure(var1, var2);
         } finally {
            this.editText.setHint(var4);
            this.isProvidingHint = var3;
         }

      } else {
         super.dispatchProvideAutofillStructure(var1, var2);
      }
   }

   protected void dispatchRestoreInstanceState(SparseArray var1) {
      this.restoringSavedState = true;
      super.dispatchRestoreInstanceState(var1);
      this.restoringSavedState = false;
   }

   public void draw(Canvas var1) {
      if (this.boxBackground != null) {
         this.boxBackground.draw(var1);
      }

      super.draw(var1);
      if (this.hintEnabled) {
         this.collapsingTextHelper.draw(var1);
      }

   }

   protected void drawableStateChanged() {
      if (!this.inDrawableStateChanged) {
         boolean var1 = true;
         this.inDrawableStateChanged = true;
         super.drawableStateChanged();
         int[] var2 = this.getDrawableState();
         if (!ViewCompat.isLaidOut(this) || !this.isEnabled()) {
            var1 = false;
         }

         this.updateLabelState(var1);
         this.updateEditTextBackground();
         this.updateTextInputBoxBounds();
         this.updateTextInputBoxState();
         boolean var3;
         if (this.collapsingTextHelper != null) {
            var3 = this.collapsingTextHelper.setState(var2) | false;
         } else {
            var3 = false;
         }

         if (var3) {
            this.invalidate();
         }

         this.inDrawableStateChanged = false;
      }
   }

   public int getBoxBackgroundColor() {
      return this.boxBackgroundColor;
   }

   public float getBoxCornerRadiusBottomEnd() {
      return this.boxCornerRadiusBottomEnd;
   }

   public float getBoxCornerRadiusBottomStart() {
      return this.boxCornerRadiusBottomStart;
   }

   public float getBoxCornerRadiusTopEnd() {
      return this.boxCornerRadiusTopEnd;
   }

   public float getBoxCornerRadiusTopStart() {
      return this.boxCornerRadiusTopStart;
   }

   public int getBoxStrokeColor() {
      return this.focusedStrokeColor;
   }

   public int getCounterMaxLength() {
      return this.counterMaxLength;
   }

   CharSequence getCounterOverflowDescription() {
      return this.counterEnabled && this.counterOverflowed && this.counterView != null ? this.counterView.getContentDescription() : null;
   }

   public ColorStateList getDefaultHintTextColor() {
      return this.defaultHintTextColor;
   }

   public EditText getEditText() {
      return this.editText;
   }

   public CharSequence getError() {
      CharSequence var1;
      if (this.indicatorViewController.isErrorEnabled()) {
         var1 = this.indicatorViewController.getErrorText();
      } else {
         var1 = null;
      }

      return var1;
   }

   public int getErrorCurrentTextColors() {
      return this.indicatorViewController.getErrorViewCurrentTextColor();
   }

   final int getErrorTextCurrentColor() {
      return this.indicatorViewController.getErrorViewCurrentTextColor();
   }

   public CharSequence getHelperText() {
      CharSequence var1;
      if (this.indicatorViewController.isHelperTextEnabled()) {
         var1 = this.indicatorViewController.getHelperText();
      } else {
         var1 = null;
      }

      return var1;
   }

   public int getHelperTextCurrentTextColor() {
      return this.indicatorViewController.getHelperTextViewCurrentTextColor();
   }

   public CharSequence getHint() {
      CharSequence var1;
      if (this.hintEnabled) {
         var1 = this.hint;
      } else {
         var1 = null;
      }

      return var1;
   }

   final float getHintCollapsedTextHeight() {
      return this.collapsingTextHelper.getCollapsedTextHeight();
   }

   final int getHintCurrentCollapsedTextColor() {
      return this.collapsingTextHelper.getCurrentCollapsedTextColor();
   }

   public CharSequence getPasswordVisibilityToggleContentDescription() {
      return this.passwordToggleContentDesc;
   }

   public Drawable getPasswordVisibilityToggleDrawable() {
      return this.passwordToggleDrawable;
   }

   public Typeface getTypeface() {
      return this.typeface;
   }

   public boolean isHelperTextEnabled() {
      return this.indicatorViewController.isHelperTextEnabled();
   }

   boolean isProvidingHint() {
      return this.isProvidingHint;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (this.boxBackground != null) {
         this.updateTextInputBoxBounds();
      }

      if (this.hintEnabled && this.editText != null) {
         Rect var6 = this.tmpRect;
         DescendantOffsetUtils.getDescendantRect(this, this.editText, var6);
         var2 = var6.left + this.editText.getCompoundPaddingLeft();
         int var7 = var6.right - this.editText.getCompoundPaddingRight();
         var4 = this.calculateCollapsedTextTopBounds();
         this.collapsingTextHelper.setExpandedBounds(var2, var6.top + this.editText.getCompoundPaddingTop(), var7, var6.bottom - this.editText.getCompoundPaddingBottom());
         this.collapsingTextHelper.setCollapsedBounds(var2, var4, var7, var5 - var3 - this.getPaddingBottom());
         this.collapsingTextHelper.recalculate();
         if (this.cutoutEnabled() && !this.hintExpanded) {
            this.openCutout();
         }
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
      if (this.indicatorViewController.errorShouldBeShown()) {
         var1.error = this.getError();
      }

      var1.isPasswordToggledVisible = this.passwordToggledVisible;
      return var1;
   }

   public void passwordVisibilityToggleRequested(boolean var1) {
      if (this.passwordToggleEnabled) {
         int var2 = this.editText.getSelectionEnd();
         if (this.hasPasswordTransformation()) {
            this.editText.setTransformationMethod((TransformationMethod)null);
            this.passwordToggledVisible = true;
         } else {
            this.editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.passwordToggledVisible = false;
         }

         this.passwordToggleView.setChecked(this.passwordToggledVisible);
         if (var1) {
            this.passwordToggleView.jumpDrawablesToCurrentState();
         }

         this.editText.setSelection(var2);
      }

   }

   public void setBoxBackgroundColor(int var1) {
      if (this.boxBackgroundColor != var1) {
         this.boxBackgroundColor = var1;
         this.applyBoxAttributes();
      }

   }

   public void setBoxBackgroundColorResource(int var1) {
      this.setBoxBackgroundColor(ContextCompat.getColor(this.getContext(), var1));
   }

   public void setBoxBackgroundMode(int var1) {
      if (var1 != this.boxBackgroundMode) {
         this.boxBackgroundMode = var1;
         this.onApplyBoxBackgroundMode();
      }
   }

   public void setBoxStrokeColor(int var1) {
      if (this.focusedStrokeColor != var1) {
         this.focusedStrokeColor = var1;
         this.updateTextInputBoxState();
      }

   }

   public void setCounterEnabled(boolean var1) {
      if (this.counterEnabled != var1) {
         if (var1) {
            this.counterView = new AppCompatTextView(this.getContext());
            this.counterView.setId(R.id.textinput_counter);
            if (this.typeface != null) {
               this.counterView.setTypeface(this.typeface);
            }

            this.counterView.setMaxLines(1);
            this.setTextAppearanceCompatWithErrorFallback(this.counterView, this.counterTextAppearance);
            this.indicatorViewController.addIndicator(this.counterView, 2);
            if (this.editText == null) {
               this.updateCounter(0);
            } else {
               this.updateCounter(this.editText.getText().length());
            }
         } else {
            this.indicatorViewController.removeIndicator(this.counterView, 2);
            this.counterView = null;
         }

         this.counterEnabled = var1;
      }

   }

   public void setCounterMaxLength(int var1) {
      if (this.counterMaxLength != var1) {
         if (var1 > 0) {
            this.counterMaxLength = var1;
         } else {
            this.counterMaxLength = -1;
         }

         if (this.counterEnabled) {
            if (this.editText == null) {
               var1 = 0;
            } else {
               var1 = this.editText.getText().length();
            }

            this.updateCounter(var1);
         }
      }

   }

   public void setDefaultHintTextColor(ColorStateList var1) {
      this.defaultHintTextColor = var1;
      this.focusedTextColor = var1;
      if (this.editText != null) {
         this.updateLabelState(false);
      }

   }

   public void setEnabled(boolean var1) {
      recursiveSetEnabled(this, var1);
      super.setEnabled(var1);
   }

   public void setError(CharSequence var1) {
      if (!this.indicatorViewController.isErrorEnabled()) {
         if (TextUtils.isEmpty(var1)) {
            return;
         }

         this.setErrorEnabled(true);
      }

      if (!TextUtils.isEmpty(var1)) {
         this.indicatorViewController.showError(var1);
      } else {
         this.indicatorViewController.hideError();
      }

   }

   public void setErrorEnabled(boolean var1) {
      this.indicatorViewController.setErrorEnabled(var1);
   }

   public void setErrorTextAppearance(int var1) {
      this.indicatorViewController.setErrorTextAppearance(var1);
   }

   public void setErrorTextColor(ColorStateList var1) {
      this.indicatorViewController.setErrorViewTextColor(var1);
   }

   public void setHelperText(CharSequence var1) {
      if (TextUtils.isEmpty(var1)) {
         if (this.isHelperTextEnabled()) {
            this.setHelperTextEnabled(false);
         }
      } else {
         if (!this.isHelperTextEnabled()) {
            this.setHelperTextEnabled(true);
         }

         this.indicatorViewController.showHelper(var1);
      }

   }

   public void setHelperTextColor(ColorStateList var1) {
      this.indicatorViewController.setHelperTextViewTextColor(var1);
   }

   public void setHelperTextEnabled(boolean var1) {
      this.indicatorViewController.setHelperTextEnabled(var1);
   }

   public void setHelperTextTextAppearance(int var1) {
      this.indicatorViewController.setHelperTextAppearance(var1);
   }

   public void setHint(CharSequence var1) {
      if (this.hintEnabled) {
         this.setHintInternal(var1);
         this.sendAccessibilityEvent(2048);
      }

   }

   public void setHintAnimationEnabled(boolean var1) {
      this.hintAnimationEnabled = var1;
   }

   public void setHintEnabled(boolean var1) {
      if (var1 != this.hintEnabled) {
         this.hintEnabled = var1;
         if (!this.hintEnabled) {
            this.isProvidingHint = false;
            if (!TextUtils.isEmpty(this.hint) && TextUtils.isEmpty(this.editText.getHint())) {
               this.editText.setHint(this.hint);
            }

            this.setHintInternal((CharSequence)null);
         } else {
            CharSequence var2 = this.editText.getHint();
            if (!TextUtils.isEmpty(var2)) {
               if (TextUtils.isEmpty(this.hint)) {
                  this.setHint(var2);
               }

               this.editText.setHint((CharSequence)null);
            }

            this.isProvidingHint = true;
         }

         if (this.editText != null) {
            this.updateInputLayoutMargins();
         }
      }

   }

   public void setHintTextAppearance(int var1) {
      this.collapsingTextHelper.setCollapsedTextAppearance(var1);
      this.focusedTextColor = this.collapsingTextHelper.getCollapsedTextColor();
      if (this.editText != null) {
         this.updateLabelState(false);
         this.updateInputLayoutMargins();
      }

   }

   public void setPasswordVisibilityToggleContentDescription(int var1) {
      CharSequence var2;
      if (var1 != 0) {
         var2 = this.getResources().getText(var1);
      } else {
         var2 = null;
      }

      this.setPasswordVisibilityToggleContentDescription(var2);
   }

   public void setPasswordVisibilityToggleContentDescription(CharSequence var1) {
      this.passwordToggleContentDesc = var1;
      if (this.passwordToggleView != null) {
         this.passwordToggleView.setContentDescription(var1);
      }

   }

   public void setPasswordVisibilityToggleDrawable(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setPasswordVisibilityToggleDrawable(var2);
   }

   public void setPasswordVisibilityToggleDrawable(Drawable var1) {
      this.passwordToggleDrawable = var1;
      if (this.passwordToggleView != null) {
         this.passwordToggleView.setImageDrawable(var1);
      }

   }

   public void setPasswordVisibilityToggleEnabled(boolean var1) {
      if (this.passwordToggleEnabled != var1) {
         this.passwordToggleEnabled = var1;
         if (!var1 && this.passwordToggledVisible && this.editText != null) {
            this.editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
         }

         this.passwordToggledVisible = false;
         this.updatePasswordToggleView();
      }

   }

   public void setPasswordVisibilityToggleTintList(ColorStateList var1) {
      this.passwordToggleTintList = var1;
      this.hasPasswordToggleTintList = true;
      this.applyPasswordToggleTint();
   }

   public void setPasswordVisibilityToggleTintMode(Mode var1) {
      this.passwordToggleTintMode = var1;
      this.hasPasswordToggleTintMode = true;
      this.applyPasswordToggleTint();
   }

   void setTextAppearanceCompatWithErrorFallback(TextView var1, int var2) {
      boolean var3 = true;

      boolean var6;
      label25: {
         label24: {
            try {
               TextViewCompat.setTextAppearance(var1, var2);
               if (VERSION.SDK_INT < 23) {
                  break label24;
               }

               var2 = var1.getTextColors().getDefaultColor();
            } catch (Exception var5) {
               var6 = var3;
               break label25;
            }

            if (var2 == -65281) {
               var6 = var3;
               break label25;
            }
         }

         var6 = false;
      }

      if (var6) {
         TextViewCompat.setTextAppearance(var1, R.style.TextAppearance_AppCompat_Caption);
         var1.setTextColor(ContextCompat.getColor(this.getContext(), R.color.design_error));
      }

   }

   public void setTextInputAccessibilityDelegate(TextInputLayout.AccessibilityDelegate var1) {
      if (this.editText != null) {
         ViewCompat.setAccessibilityDelegate(this.editText, var1);
      }

   }

   public void setTypeface(Typeface var1) {
      if (var1 != this.typeface) {
         this.typeface = var1;
         this.collapsingTextHelper.setTypefaces(var1);
         this.indicatorViewController.setTypefaces(var1);
         if (this.counterView != null) {
            this.counterView.setTypeface(var1);
         }
      }

   }

   void updateCounter(int var1) {
      boolean var2 = this.counterOverflowed;
      if (this.counterMaxLength == -1) {
         this.counterView.setText(String.valueOf(var1));
         this.counterView.setContentDescription((CharSequence)null);
         this.counterOverflowed = false;
      } else {
         if (ViewCompat.getAccessibilityLiveRegion(this.counterView) == 1) {
            ViewCompat.setAccessibilityLiveRegion(this.counterView, 0);
         }

         boolean var3;
         if (var1 > this.counterMaxLength) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.counterOverflowed = var3;
         if (var2 != this.counterOverflowed) {
            TextView var4 = this.counterView;
            int var5;
            if (this.counterOverflowed) {
               var5 = this.counterOverflowTextAppearance;
            } else {
               var5 = this.counterTextAppearance;
            }

            this.setTextAppearanceCompatWithErrorFallback(var4, var5);
            if (this.counterOverflowed) {
               ViewCompat.setAccessibilityLiveRegion(this.counterView, 1);
            }
         }

         this.counterView.setText(this.getContext().getString(R.string.character_counter_pattern, new Object[]{var1, this.counterMaxLength}));
         this.counterView.setContentDescription(this.getContext().getString(R.string.character_counter_content_description, new Object[]{var1, this.counterMaxLength}));
      }

      if (this.editText != null && var2 != this.counterOverflowed) {
         this.updateLabelState(false);
         this.updateTextInputBoxState();
         this.updateEditTextBackground();
      }

   }

   void updateEditTextBackground() {
      if (this.editText != null) {
         Drawable var1 = this.editText.getBackground();
         if (var1 != null) {
            this.ensureBackgroundDrawableStateWorkaround();
            Drawable var2 = var1;
            if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(var1)) {
               var2 = var1.mutate();
            }

            if (this.indicatorViewController.errorShouldBeShown()) {
               var2.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(this.indicatorViewController.getErrorViewCurrentTextColor(), Mode.SRC_IN));
            } else if (this.counterOverflowed && this.counterView != null) {
               var2.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(this.counterView.getCurrentTextColor(), Mode.SRC_IN));
            } else {
               DrawableCompat.clearColorFilter(var2);
               this.editText.refreshDrawableState();
            }

         }
      }
   }

   void updateLabelState(boolean var1) {
      this.updateLabelState(var1, false);
   }

   void updateTextInputBoxState() {
      if (this.boxBackground != null && this.boxBackgroundMode != 0) {
         EditText var1 = this.editText;
         boolean var2 = true;
         boolean var3;
         if (var1 != null && this.editText.hasFocus()) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (this.editText == null || !this.editText.isHovered()) {
            var2 = false;
         }

         if (this.boxBackgroundMode == 2) {
            if (!this.isEnabled()) {
               this.boxStrokeColor = this.disabledColor;
            } else if (this.indicatorViewController.errorShouldBeShown()) {
               this.boxStrokeColor = this.indicatorViewController.getErrorViewCurrentTextColor();
            } else if (this.counterOverflowed && this.counterView != null) {
               this.boxStrokeColor = this.counterView.getCurrentTextColor();
            } else if (var3) {
               this.boxStrokeColor = this.focusedStrokeColor;
            } else if (var2) {
               this.boxStrokeColor = this.hoveredStrokeColor;
            } else {
               this.boxStrokeColor = this.defaultStrokeColor;
            }

            if ((var2 || var3) && this.isEnabled()) {
               this.boxStrokeWidthPx = this.boxStrokeWidthFocusedPx;
            } else {
               this.boxStrokeWidthPx = this.boxStrokeWidthDefaultPx;
            }

            this.applyBoxAttributes();
         }

      }
   }

   public static class AccessibilityDelegate extends AccessibilityDelegateCompat {
      private final TextInputLayout layout;

      public AccessibilityDelegate(TextInputLayout var1) {
         this.layout = var1;
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         EditText var12 = this.layout.getEditText();
         Editable var13;
         if (var12 != null) {
            var13 = var12.getText();
         } else {
            var13 = null;
         }

         CharSequence var3 = this.layout.getHint();
         CharSequence var4 = this.layout.getError();
         CharSequence var5 = this.layout.getCounterOverflowDescription();
         boolean var6 = TextUtils.isEmpty(var13) ^ true;
         boolean var7 = TextUtils.isEmpty(var3) ^ true;
         boolean var8 = TextUtils.isEmpty(var4) ^ true;
         boolean var9 = false;
         boolean var10;
         if (!var8 && TextUtils.isEmpty(var5)) {
            var10 = false;
         } else {
            var10 = true;
         }

         if (var6) {
            var2.setText(var13);
         } else if (var7) {
            var2.setText(var3);
         }

         if (var7) {
            var2.setHintText(var3);
            boolean var11 = var9;
            if (!var6) {
               var11 = var9;
               if (var7) {
                  var11 = true;
               }
            }

            var2.setShowingHintText(var11);
         }

         if (var10) {
            CharSequence var14;
            if (var8) {
               var14 = var4;
            } else {
               var14 = var5;
            }

            var2.setError(var14);
            var2.setContentInvalid(true);
         }

      }

      public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onPopulateAccessibilityEvent(var1, var2);
         EditText var4 = this.layout.getEditText();
         Editable var5;
         if (var4 != null) {
            var5 = var4.getText();
         } else {
            var5 = null;
         }

         Object var3 = var5;
         if (TextUtils.isEmpty(var5)) {
            var3 = this.layout.getHint();
         }

         if (!TextUtils.isEmpty((CharSequence)var3)) {
            var2.getText().add(var3);
         }

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
}
