// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.view.accessibility.AccessibilityEvent;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.graphics.ColorFilter;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.TransformationMethod;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewStructure;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.support.v4.widget.TextViewCompat;
import android.graphics.drawable.ColorDrawable;
import android.view.View$OnClickListener;
import android.view.LayoutInflater;
import android.widget.LinearLayout$LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.text.method.PasswordTransformationMethod;
import android.graphics.drawable.DrawableContainer;
import android.os.Build$VERSION;
import android.text.TextUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.TintTypedArray;
import android.support.v4.view.ViewCompat;
import android.support.design.internal.ViewUtils;
import android.support.v4.content.ContextCompat;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.animation.AnimationUtils;
import android.view.View;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.PorterDuff$Mode;
import android.widget.FrameLayout;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.content.res.ColorStateList;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import android.animation.ValueAnimator;
import android.widget.LinearLayout;

public class TextInputLayout extends LinearLayout
{
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
    private PorterDuff$Mode passwordToggleTintMode;
    private CheckableImageButton passwordToggleView;
    private boolean passwordToggledVisible;
    private boolean restoringSavedState;
    private final Rect tmpRect;
    private final RectF tmpRectF;
    private Typeface typeface;
    
    public TextInputLayout(final Context context) {
        this(context, null);
    }
    
    public TextInputLayout(final Context context, final AttributeSet set) {
        this(context, set, R.attr.textInputStyle);
    }
    
    public TextInputLayout(final Context context, final AttributeSet set, int resourceId) {
        super(context, set, resourceId);
        this.indicatorViewController = new IndicatorViewController(this);
        this.tmpRect = new Rect();
        this.tmpRectF = new RectF();
        this.collapsingTextHelper = new CollapsingTextHelper((View)this);
        this.setOrientation(1);
        this.setWillNotDraw(false);
        this.setAddStatesFromChildren(true);
        (this.inputFrame = new FrameLayout(context)).setAddStatesFromChildren(true);
        this.addView((View)this.inputFrame);
        this.collapsingTextHelper.setTextSizeInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        this.collapsingTextHelper.setPositionInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        this.collapsingTextHelper.setCollapsedTextGravity(8388659);
        final TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context, set, R.styleable.TextInputLayout, resourceId, R.style.Widget_Design_TextInputLayout, new int[0]);
        this.hintEnabled = obtainTintedStyledAttributes.getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
        this.setHint(obtainTintedStyledAttributes.getText(R.styleable.TextInputLayout_android_hint));
        this.hintAnimationEnabled = obtainTintedStyledAttributes.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
        this.boxBottomOffsetPx = context.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_bottom_offset);
        this.boxLabelCutoutPaddingPx = context.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_label_cutout_padding);
        this.boxCollapsedPaddingTopPx = obtainTintedStyledAttributes.getDimensionPixelOffset(R.styleable.TextInputLayout_boxCollapsedPaddingTop, 0);
        this.boxCornerRadiusTopStart = obtainTintedStyledAttributes.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopStart, 0.0f);
        this.boxCornerRadiusTopEnd = obtainTintedStyledAttributes.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopEnd, 0.0f);
        this.boxCornerRadiusBottomEnd = obtainTintedStyledAttributes.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomEnd, 0.0f);
        this.boxCornerRadiusBottomStart = obtainTintedStyledAttributes.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomStart, 0.0f);
        this.boxBackgroundColor = obtainTintedStyledAttributes.getColor(R.styleable.TextInputLayout_boxBackgroundColor, 0);
        this.focusedStrokeColor = obtainTintedStyledAttributes.getColor(R.styleable.TextInputLayout_boxStrokeColor, 0);
        this.boxStrokeWidthDefaultPx = context.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_default);
        this.boxStrokeWidthFocusedPx = context.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_focused);
        this.boxStrokeWidthPx = this.boxStrokeWidthDefaultPx;
        this.setBoxBackgroundMode(obtainTintedStyledAttributes.getInt(R.styleable.TextInputLayout_boxBackgroundMode, 0));
        if (obtainTintedStyledAttributes.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
            final ColorStateList colorStateList = obtainTintedStyledAttributes.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
            this.focusedTextColor = colorStateList;
            this.defaultHintTextColor = colorStateList;
        }
        this.defaultStrokeColor = ContextCompat.getColor(context, R.color.mtrl_textinput_default_box_stroke_color);
        this.disabledColor = ContextCompat.getColor(context, R.color.mtrl_textinput_disabled_color);
        this.hoveredStrokeColor = ContextCompat.getColor(context, R.color.mtrl_textinput_hovered_box_stroke_color);
        if (obtainTintedStyledAttributes.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1) != -1) {
            this.setHintTextAppearance(obtainTintedStyledAttributes.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0));
        }
        resourceId = obtainTintedStyledAttributes.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
        final boolean boolean1 = obtainTintedStyledAttributes.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
        final int resourceId2 = obtainTintedStyledAttributes.getResourceId(R.styleable.TextInputLayout_helperTextTextAppearance, 0);
        final boolean boolean2 = obtainTintedStyledAttributes.getBoolean(R.styleable.TextInputLayout_helperTextEnabled, false);
        final CharSequence text = obtainTintedStyledAttributes.getText(R.styleable.TextInputLayout_helperText);
        final boolean boolean3 = obtainTintedStyledAttributes.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
        this.setCounterMaxLength(obtainTintedStyledAttributes.getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
        this.counterTextAppearance = obtainTintedStyledAttributes.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
        this.counterOverflowTextAppearance = obtainTintedStyledAttributes.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
        this.passwordToggleEnabled = obtainTintedStyledAttributes.getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false);
        this.passwordToggleDrawable = obtainTintedStyledAttributes.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable);
        this.passwordToggleContentDesc = obtainTintedStyledAttributes.getText(R.styleable.TextInputLayout_passwordToggleContentDescription);
        if (obtainTintedStyledAttributes.hasValue(R.styleable.TextInputLayout_passwordToggleTint)) {
            this.hasPasswordToggleTintList = true;
            this.passwordToggleTintList = obtainTintedStyledAttributes.getColorStateList(R.styleable.TextInputLayout_passwordToggleTint);
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.TextInputLayout_passwordToggleTintMode)) {
            this.hasPasswordToggleTintMode = true;
            this.passwordToggleTintMode = ViewUtils.parseTintMode(obtainTintedStyledAttributes.getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), null);
        }
        obtainTintedStyledAttributes.recycle();
        this.setHelperTextEnabled(boolean2);
        this.setHelperText(text);
        this.setHelperTextTextAppearance(resourceId2);
        this.setErrorEnabled(boolean1);
        this.setErrorTextAppearance(resourceId);
        this.setCounterEnabled(boolean3);
        this.applyPasswordToggleTint();
        ViewCompat.setImportantForAccessibility((View)this, 2);
    }
    
    private void applyBoxAttributes() {
        if (this.boxBackground == null) {
            return;
        }
        this.setBoxAttributes();
        if (this.editText != null && this.boxBackgroundMode == 2) {
            if (this.editText.getBackground() != null) {
                this.editTextOriginalDrawable = this.editText.getBackground();
            }
            ViewCompat.setBackground((View)this.editText, null);
        }
        if (this.editText != null && this.boxBackgroundMode == 1 && this.editTextOriginalDrawable != null) {
            ViewCompat.setBackground((View)this.editText, this.editTextOriginalDrawable);
        }
        if (this.boxStrokeWidthPx > -1 && this.boxStrokeColor != 0) {
            this.boxBackground.setStroke(this.boxStrokeWidthPx, this.boxStrokeColor);
        }
        this.boxBackground.setCornerRadii(this.getCornerRadiiAsArray());
        this.boxBackground.setColor(this.boxBackgroundColor);
        this.invalidate();
    }
    
    private void applyCutoutPadding(final RectF rectF) {
        rectF.left -= this.boxLabelCutoutPaddingPx;
        rectF.top -= this.boxLabelCutoutPaddingPx;
        rectF.right += this.boxLabelCutoutPaddingPx;
        rectF.bottom += this.boxLabelCutoutPaddingPx;
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
        }
        else if (this.boxBackgroundMode == 2 && this.hintEnabled && !(this.boxBackground instanceof CutoutDrawable)) {
            this.boxBackground = new CutoutDrawable();
        }
        else if (!(this.boxBackground instanceof GradientDrawable)) {
            this.boxBackground = new GradientDrawable();
        }
    }
    
    private int calculateBoxBackgroundTop() {
        if (this.editText == null) {
            return 0;
        }
        switch (this.boxBackgroundMode) {
            default: {
                return 0;
            }
            case 2: {
                return this.editText.getTop() + this.calculateLabelMarginTop();
            }
            case 1: {
                return this.editText.getTop();
            }
        }
    }
    
    private int calculateCollapsedTextTopBounds() {
        switch (this.boxBackgroundMode) {
            default: {
                return this.getPaddingTop();
            }
            case 2: {
                return this.getBoxBackground().getBounds().top - this.calculateLabelMarginTop();
            }
            case 1: {
                return this.getBoxBackground().getBounds().top + this.boxCollapsedPaddingTopPx;
            }
        }
    }
    
    private int calculateLabelMarginTop() {
        if (!this.hintEnabled) {
            return 0;
        }
        switch (this.boxBackgroundMode) {
            default: {
                return 0;
            }
            case 2: {
                return (int)(this.collapsingTextHelper.getCollapsedTextHeight() / 2.0f);
            }
            case 0:
            case 1: {
                return (int)this.collapsingTextHelper.getCollapsedTextHeight();
            }
        }
    }
    
    private void closeCutout() {
        if (this.cutoutEnabled()) {
            ((CutoutDrawable)this.boxBackground).removeCutout();
        }
    }
    
    private void collapseHint(final boolean b) {
        if (this.animator != null && this.animator.isRunning()) {
            this.animator.cancel();
        }
        if (b && this.hintAnimationEnabled) {
            this.animateToExpansionFraction(1.0f);
        }
        else {
            this.collapsingTextHelper.setExpansionFraction(1.0f);
        }
        this.hintExpanded = false;
        if (this.cutoutEnabled()) {
            this.openCutout();
        }
    }
    
    private boolean cutoutEnabled() {
        return this.hintEnabled && !TextUtils.isEmpty(this.hint) && this.boxBackground instanceof CutoutDrawable;
    }
    
    private void ensureBackgroundDrawableStateWorkaround() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT != 21 && sdk_INT != 22) {
            return;
        }
        final Drawable background = this.editText.getBackground();
        if (background == null) {
            return;
        }
        if (!this.hasReconstructedEditTextBackground) {
            final Drawable drawable = background.getConstantState().newDrawable();
            if (background instanceof DrawableContainer) {
                this.hasReconstructedEditTextBackground = DrawableUtils.setContainerConstantState((DrawableContainer)background, drawable.getConstantState());
            }
            if (!this.hasReconstructedEditTextBackground) {
                ViewCompat.setBackground((View)this.editText, drawable);
                this.hasReconstructedEditTextBackground = true;
                this.onApplyBoxBackgroundMode();
            }
        }
    }
    
    private void expandHint(final boolean b) {
        if (this.animator != null && this.animator.isRunning()) {
            this.animator.cancel();
        }
        if (b && this.hintAnimationEnabled) {
            this.animateToExpansionFraction(0.0f);
        }
        else {
            this.collapsingTextHelper.setExpansionFraction(0.0f);
        }
        if (this.cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout()) {
            this.closeCutout();
        }
        this.hintExpanded = true;
    }
    
    private Drawable getBoxBackground() {
        if (this.boxBackgroundMode != 1 && this.boxBackgroundMode != 2) {
            throw new IllegalStateException();
        }
        return (Drawable)this.boxBackground;
    }
    
    private float[] getCornerRadiiAsArray() {
        if (!ViewUtils.isLayoutRtl((View)this)) {
            return new float[] { this.boxCornerRadiusTopStart, this.boxCornerRadiusTopStart, this.boxCornerRadiusTopEnd, this.boxCornerRadiusTopEnd, this.boxCornerRadiusBottomEnd, this.boxCornerRadiusBottomEnd, this.boxCornerRadiusBottomStart, this.boxCornerRadiusBottomStart };
        }
        return new float[] { this.boxCornerRadiusTopEnd, this.boxCornerRadiusTopEnd, this.boxCornerRadiusTopStart, this.boxCornerRadiusTopStart, this.boxCornerRadiusBottomStart, this.boxCornerRadiusBottomStart, this.boxCornerRadiusBottomEnd, this.boxCornerRadiusBottomEnd };
    }
    
    private boolean hasPasswordTransformation() {
        return this.editText != null && this.editText.getTransformationMethod() instanceof PasswordTransformationMethod;
    }
    
    private void onApplyBoxBackgroundMode() {
        this.assignBoxBackgroundByMode();
        if (this.boxBackgroundMode != 0) {
            this.updateInputLayoutMargins();
        }
        this.updateTextInputBoxBounds();
    }
    
    private void openCutout() {
        if (!this.cutoutEnabled()) {
            return;
        }
        final RectF tmpRectF = this.tmpRectF;
        this.collapsingTextHelper.getCollapsedTextActualBounds(tmpRectF);
        this.applyCutoutPadding(tmpRectF);
        ((CutoutDrawable)this.boxBackground).setCutout(tmpRectF);
    }
    
    private static void recursiveSetEnabled(final ViewGroup viewGroup, final boolean enabled) {
        for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = viewGroup.getChildAt(i);
            child.setEnabled(enabled);
            if (child instanceof ViewGroup) {
                recursiveSetEnabled((ViewGroup)child, enabled);
            }
        }
    }
    
    private void setBoxAttributes() {
        switch (this.boxBackgroundMode) {
            case 2: {
                if (this.focusedStrokeColor == 0) {
                    this.focusedStrokeColor = this.focusedTextColor.getColorForState(this.getDrawableState(), this.focusedTextColor.getDefaultColor());
                    break;
                }
                break;
            }
            case 1: {
                this.boxStrokeWidthPx = 0;
                break;
            }
        }
    }
    
    private void setEditText(final EditText editText) {
        if (this.editText == null) {
            if (!(editText instanceof TextInputEditText)) {
                Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead.");
            }
            this.editText = editText;
            this.onApplyBoxBackgroundMode();
            this.setTextInputAccessibilityDelegate(new AccessibilityDelegate(this));
            if (!this.hasPasswordTransformation()) {
                this.collapsingTextHelper.setTypefaces(this.editText.getTypeface());
            }
            this.collapsingTextHelper.setExpandedTextSize(this.editText.getTextSize());
            final int gravity = this.editText.getGravity();
            this.collapsingTextHelper.setCollapsedTextGravity((gravity & 0xFFFFFF8F) | 0x30);
            this.collapsingTextHelper.setExpandedTextGravity(gravity);
            this.editText.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                    TextInputLayout.this.updateLabelState(TextInputLayout.this.restoringSavedState ^ true);
                    if (TextInputLayout.this.counterEnabled) {
                        TextInputLayout.this.updateCounter(editable.length());
                    }
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
            });
            if (this.defaultHintTextColor == null) {
                this.defaultHintTextColor = this.editText.getHintTextColors();
            }
            if (this.hintEnabled) {
                if (TextUtils.isEmpty(this.hint)) {
                    this.setHint(this.originalHint = this.editText.getHint());
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
            return;
        }
        throw new IllegalArgumentException("We already have an EditText, can only have one");
    }
    
    private void setHintInternal(final CharSequence charSequence) {
        if (!TextUtils.equals(charSequence, this.hint)) {
            this.hint = charSequence;
            this.collapsingTextHelper.setText(charSequence);
            if (!this.hintExpanded) {
                this.openCutout();
            }
        }
    }
    
    private boolean shouldShowPasswordIcon() {
        return this.passwordToggleEnabled && (this.hasPasswordTransformation() || this.passwordToggledVisible);
    }
    
    private void updateEditTextBackgroundBounds() {
        if (this.editText == null) {
            return;
        }
        final Drawable background = this.editText.getBackground();
        if (background == null) {
            return;
        }
        Drawable mutate = background;
        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(background)) {
            mutate = background.mutate();
        }
        DescendantOffsetUtils.getDescendantRect((ViewGroup)this, (View)this.editText, new Rect());
        final Rect bounds = mutate.getBounds();
        if (bounds.left != bounds.right) {
            final Rect rect = new Rect();
            mutate.getPadding(rect);
            mutate.setBounds(bounds.left - rect.left, bounds.top, bounds.right + rect.right * 2, this.editText.getBottom());
        }
    }
    
    private void updateInputLayoutMargins() {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.inputFrame.getLayoutParams();
        final int calculateLabelMarginTop = this.calculateLabelMarginTop();
        if (calculateLabelMarginTop != linearLayout$LayoutParams.topMargin) {
            linearLayout$LayoutParams.topMargin = calculateLabelMarginTop;
            this.inputFrame.requestLayout();
        }
    }
    
    private void updateLabelState(final boolean b, final boolean b2) {
        final boolean enabled = this.isEnabled();
        final EditText editText = this.editText;
        boolean b3 = true;
        final boolean b4 = editText != null && !TextUtils.isEmpty((CharSequence)this.editText.getText());
        if (this.editText == null || !this.editText.hasFocus()) {
            b3 = false;
        }
        final boolean errorShouldBeShown = this.indicatorViewController.errorShouldBeShown();
        if (this.defaultHintTextColor != null) {
            this.collapsingTextHelper.setCollapsedTextColor(this.defaultHintTextColor);
            this.collapsingTextHelper.setExpandedTextColor(this.defaultHintTextColor);
        }
        if (!enabled) {
            this.collapsingTextHelper.setCollapsedTextColor(ColorStateList.valueOf(this.disabledColor));
            this.collapsingTextHelper.setExpandedTextColor(ColorStateList.valueOf(this.disabledColor));
        }
        else if (errorShouldBeShown) {
            this.collapsingTextHelper.setCollapsedTextColor(this.indicatorViewController.getErrorViewTextColors());
        }
        else if (this.counterOverflowed && this.counterView != null) {
            this.collapsingTextHelper.setCollapsedTextColor(this.counterView.getTextColors());
        }
        else if (b3 && this.focusedTextColor != null) {
            this.collapsingTextHelper.setCollapsedTextColor(this.focusedTextColor);
        }
        Label_0257: {
            if (!b4) {
                if (this.isEnabled()) {
                    if (b3) {
                        break Label_0257;
                    }
                    if (errorShouldBeShown) {
                        break Label_0257;
                    }
                }
                if (b2 || !this.hintExpanded) {
                    this.expandHint(b);
                }
                return;
            }
        }
        if (b2 || this.hintExpanded) {
            this.collapseHint(b);
        }
    }
    
    private void updatePasswordToggleView() {
        if (this.editText == null) {
            return;
        }
        if (this.shouldShowPasswordIcon()) {
            if (this.passwordToggleView == null) {
                (this.passwordToggleView = (CheckableImageButton)LayoutInflater.from(this.getContext()).inflate(R.layout.design_text_input_password_icon, (ViewGroup)this.inputFrame, false)).setImageDrawable(this.passwordToggleDrawable);
                this.passwordToggleView.setContentDescription(this.passwordToggleContentDesc);
                this.inputFrame.addView((View)this.passwordToggleView);
                this.passwordToggleView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        TextInputLayout.this.passwordVisibilityToggleRequested(false);
                    }
                });
            }
            if (this.editText != null && ViewCompat.getMinimumHeight((View)this.editText) <= 0) {
                this.editText.setMinimumHeight(ViewCompat.getMinimumHeight((View)this.passwordToggleView));
            }
            this.passwordToggleView.setVisibility(0);
            this.passwordToggleView.setChecked(this.passwordToggledVisible);
            if (this.passwordToggleDummyDrawable == null) {
                this.passwordToggleDummyDrawable = (Drawable)new ColorDrawable();
            }
            this.passwordToggleDummyDrawable.setBounds(0, 0, this.passwordToggleView.getMeasuredWidth(), 1);
            final Drawable[] compoundDrawablesRelative = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
            if (compoundDrawablesRelative[2] != this.passwordToggleDummyDrawable) {
                this.originalEditTextEndDrawable = compoundDrawablesRelative[2];
            }
            TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, compoundDrawablesRelative[0], compoundDrawablesRelative[1], this.passwordToggleDummyDrawable, compoundDrawablesRelative[3]);
            this.passwordToggleView.setPadding(this.editText.getPaddingLeft(), this.editText.getPaddingTop(), this.editText.getPaddingRight(), this.editText.getPaddingBottom());
        }
        else {
            if (this.passwordToggleView != null && this.passwordToggleView.getVisibility() == 0) {
                this.passwordToggleView.setVisibility(8);
            }
            if (this.passwordToggleDummyDrawable != null) {
                final Drawable[] compoundDrawablesRelative2 = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
                if (compoundDrawablesRelative2[2] == this.passwordToggleDummyDrawable) {
                    TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, compoundDrawablesRelative2[0], compoundDrawablesRelative2[1], this.originalEditTextEndDrawable, compoundDrawablesRelative2[3]);
                    this.passwordToggleDummyDrawable = null;
                }
            }
        }
    }
    
    private void updateTextInputBoxBounds() {
        if (this.boxBackgroundMode != 0 && this.boxBackground != null && this.editText != null && this.getRight() != 0) {
            final int left = this.editText.getLeft();
            final int calculateBoxBackgroundTop = this.calculateBoxBackgroundTop();
            final int right = this.editText.getRight();
            final int n = this.editText.getBottom() + this.boxBottomOffsetPx;
            int n2 = left;
            int n3 = calculateBoxBackgroundTop;
            int n4 = right;
            int n5 = n;
            if (this.boxBackgroundMode == 2) {
                n2 = left + this.boxStrokeWidthFocusedPx / 2;
                n3 = calculateBoxBackgroundTop - this.boxStrokeWidthFocusedPx / 2;
                n4 = right - this.boxStrokeWidthFocusedPx / 2;
                n5 = n + this.boxStrokeWidthFocusedPx / 2;
            }
            this.boxBackground.setBounds(n2, n3, n4, n5);
            this.applyBoxAttributes();
            this.updateEditTextBackgroundBounds();
        }
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams layoutParams) {
        if (view instanceof EditText) {
            final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(layoutParams);
            frameLayout$LayoutParams.gravity = ((frameLayout$LayoutParams.gravity & 0xFFFFFF8F) | 0x10);
            this.inputFrame.addView(view, (ViewGroup$LayoutParams)frameLayout$LayoutParams);
            this.inputFrame.setLayoutParams(layoutParams);
            this.updateInputLayoutMargins();
            this.setEditText((EditText)view);
        }
        else {
            super.addView(view, n, layoutParams);
        }
    }
    
    void animateToExpansionFraction(final float n) {
        if (this.collapsingTextHelper.getExpansionFraction() == n) {
            return;
        }
        if (this.animator == null) {
            (this.animator = new ValueAnimator()).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.animator.setDuration(167L);
            this.animator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    TextInputLayout.this.collapsingTextHelper.setExpansionFraction((float)valueAnimator.getAnimatedValue());
                }
            });
        }
        this.animator.setFloatValues(new float[] { this.collapsingTextHelper.getExpansionFraction(), n });
        this.animator.start();
    }
    
    public void dispatchProvideAutofillStructure(final ViewStructure viewStructure, final int n) {
        if (this.originalHint != null) {
            if (this.editText != null) {
                final boolean isProvidingHint = this.isProvidingHint;
                this.isProvidingHint = false;
                final CharSequence hint = this.editText.getHint();
                this.editText.setHint(this.originalHint);
                try {
                    super.dispatchProvideAutofillStructure(viewStructure, n);
                    return;
                }
                finally {
                    this.editText.setHint(hint);
                    this.isProvidingHint = isProvidingHint;
                }
            }
        }
        super.dispatchProvideAutofillStructure(viewStructure, n);
    }
    
    protected void dispatchRestoreInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.restoringSavedState = true;
        super.dispatchRestoreInstanceState((SparseArray)sparseArray);
        this.restoringSavedState = false;
    }
    
    public void draw(final Canvas canvas) {
        if (this.boxBackground != null) {
            this.boxBackground.draw(canvas);
        }
        super.draw(canvas);
        if (this.hintEnabled) {
            this.collapsingTextHelper.draw(canvas);
        }
    }
    
    protected void drawableStateChanged() {
        if (this.inDrawableStateChanged) {
            return;
        }
        boolean b = true;
        this.inDrawableStateChanged = true;
        super.drawableStateChanged();
        final int[] drawableState = this.getDrawableState();
        if (!ViewCompat.isLaidOut((View)this) || !this.isEnabled()) {
            b = false;
        }
        this.updateLabelState(b);
        this.updateEditTextBackground();
        this.updateTextInputBoxBounds();
        this.updateTextInputBoxState();
        if (this.collapsingTextHelper != null && (this.collapsingTextHelper.setState(drawableState) | false)) {
            this.invalidate();
        }
        this.inDrawableStateChanged = false;
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
        if (this.counterEnabled && this.counterOverflowed && this.counterView != null) {
            return this.counterView.getContentDescription();
        }
        return null;
    }
    
    public ColorStateList getDefaultHintTextColor() {
        return this.defaultHintTextColor;
    }
    
    public EditText getEditText() {
        return this.editText;
    }
    
    public CharSequence getError() {
        CharSequence errorText;
        if (this.indicatorViewController.isErrorEnabled()) {
            errorText = this.indicatorViewController.getErrorText();
        }
        else {
            errorText = null;
        }
        return errorText;
    }
    
    public int getErrorCurrentTextColors() {
        return this.indicatorViewController.getErrorViewCurrentTextColor();
    }
    
    final int getErrorTextCurrentColor() {
        return this.indicatorViewController.getErrorViewCurrentTextColor();
    }
    
    public CharSequence getHelperText() {
        CharSequence helperText;
        if (this.indicatorViewController.isHelperTextEnabled()) {
            helperText = this.indicatorViewController.getHelperText();
        }
        else {
            helperText = null;
        }
        return helperText;
    }
    
    public int getHelperTextCurrentTextColor() {
        return this.indicatorViewController.getHelperTextViewCurrentTextColor();
    }
    
    public CharSequence getHint() {
        CharSequence hint;
        if (this.hintEnabled) {
            hint = this.hint;
        }
        else {
            hint = null;
        }
        return hint;
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
    
    protected void onLayout(final boolean b, int n, final int n2, int calculateCollapsedTextTopBounds, final int n3) {
        super.onLayout(b, n, n2, calculateCollapsedTextTopBounds, n3);
        if (this.boxBackground != null) {
            this.updateTextInputBoxBounds();
        }
        if (this.hintEnabled && this.editText != null) {
            final Rect tmpRect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect((ViewGroup)this, (View)this.editText, tmpRect);
            n = tmpRect.left + this.editText.getCompoundPaddingLeft();
            final int n4 = tmpRect.right - this.editText.getCompoundPaddingRight();
            calculateCollapsedTextTopBounds = this.calculateCollapsedTextTopBounds();
            this.collapsingTextHelper.setExpandedBounds(n, tmpRect.top + this.editText.getCompoundPaddingTop(), n4, tmpRect.bottom - this.editText.getCompoundPaddingBottom());
            this.collapsingTextHelper.setCollapsedBounds(n, calculateCollapsedTextTopBounds, n4, n3 - n2 - this.getPaddingBottom());
            this.collapsingTextHelper.recalculate();
            if (this.cutoutEnabled() && !this.hintExpanded) {
                this.openCutout();
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.updatePasswordToggleView();
        super.onMeasure(n, n2);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.setError(savedState.error);
        if (savedState.isPasswordToggledVisible) {
            this.passwordVisibilityToggleRequested(true);
        }
        this.requestLayout();
    }
    
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.indicatorViewController.errorShouldBeShown()) {
            savedState.error = this.getError();
        }
        savedState.isPasswordToggledVisible = this.passwordToggledVisible;
        return (Parcelable)savedState;
    }
    
    public void passwordVisibilityToggleRequested(final boolean b) {
        if (this.passwordToggleEnabled) {
            final int selectionEnd = this.editText.getSelectionEnd();
            if (this.hasPasswordTransformation()) {
                this.editText.setTransformationMethod((TransformationMethod)null);
                this.passwordToggledVisible = true;
            }
            else {
                this.editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
                this.passwordToggledVisible = false;
            }
            this.passwordToggleView.setChecked(this.passwordToggledVisible);
            if (b) {
                this.passwordToggleView.jumpDrawablesToCurrentState();
            }
            this.editText.setSelection(selectionEnd);
        }
    }
    
    public void setBoxBackgroundColor(final int boxBackgroundColor) {
        if (this.boxBackgroundColor != boxBackgroundColor) {
            this.boxBackgroundColor = boxBackgroundColor;
            this.applyBoxAttributes();
        }
    }
    
    public void setBoxBackgroundColorResource(final int n) {
        this.setBoxBackgroundColor(ContextCompat.getColor(this.getContext(), n));
    }
    
    public void setBoxBackgroundMode(final int boxBackgroundMode) {
        if (boxBackgroundMode == this.boxBackgroundMode) {
            return;
        }
        this.boxBackgroundMode = boxBackgroundMode;
        this.onApplyBoxBackgroundMode();
    }
    
    public void setBoxStrokeColor(final int focusedStrokeColor) {
        if (this.focusedStrokeColor != focusedStrokeColor) {
            this.focusedStrokeColor = focusedStrokeColor;
            this.updateTextInputBoxState();
        }
    }
    
    public void setCounterEnabled(final boolean counterEnabled) {
        if (this.counterEnabled != counterEnabled) {
            if (counterEnabled) {
                (this.counterView = new AppCompatTextView(this.getContext())).setId(R.id.textinput_counter);
                if (this.typeface != null) {
                    this.counterView.setTypeface(this.typeface);
                }
                this.counterView.setMaxLines(1);
                this.setTextAppearanceCompatWithErrorFallback(this.counterView, this.counterTextAppearance);
                this.indicatorViewController.addIndicator(this.counterView, 2);
                if (this.editText == null) {
                    this.updateCounter(0);
                }
                else {
                    this.updateCounter(this.editText.getText().length());
                }
            }
            else {
                this.indicatorViewController.removeIndicator(this.counterView, 2);
                this.counterView = null;
            }
            this.counterEnabled = counterEnabled;
        }
    }
    
    public void setCounterMaxLength(int length) {
        if (this.counterMaxLength != length) {
            if (length > 0) {
                this.counterMaxLength = length;
            }
            else {
                this.counterMaxLength = -1;
            }
            if (this.counterEnabled) {
                if (this.editText == null) {
                    length = 0;
                }
                else {
                    length = this.editText.getText().length();
                }
                this.updateCounter(length);
            }
        }
    }
    
    public void setDefaultHintTextColor(final ColorStateList list) {
        this.defaultHintTextColor = list;
        this.focusedTextColor = list;
        if (this.editText != null) {
            this.updateLabelState(false);
        }
    }
    
    public void setEnabled(final boolean enabled) {
        recursiveSetEnabled((ViewGroup)this, enabled);
        super.setEnabled(enabled);
    }
    
    public void setError(final CharSequence charSequence) {
        if (!this.indicatorViewController.isErrorEnabled()) {
            if (TextUtils.isEmpty(charSequence)) {
                return;
            }
            this.setErrorEnabled(true);
        }
        if (!TextUtils.isEmpty(charSequence)) {
            this.indicatorViewController.showError(charSequence);
        }
        else {
            this.indicatorViewController.hideError();
        }
    }
    
    public void setErrorEnabled(final boolean errorEnabled) {
        this.indicatorViewController.setErrorEnabled(errorEnabled);
    }
    
    public void setErrorTextAppearance(final int errorTextAppearance) {
        this.indicatorViewController.setErrorTextAppearance(errorTextAppearance);
    }
    
    public void setErrorTextColor(final ColorStateList errorViewTextColor) {
        this.indicatorViewController.setErrorViewTextColor(errorViewTextColor);
    }
    
    public void setHelperText(final CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            if (this.isHelperTextEnabled()) {
                this.setHelperTextEnabled(false);
            }
        }
        else {
            if (!this.isHelperTextEnabled()) {
                this.setHelperTextEnabled(true);
            }
            this.indicatorViewController.showHelper(charSequence);
        }
    }
    
    public void setHelperTextColor(final ColorStateList helperTextViewTextColor) {
        this.indicatorViewController.setHelperTextViewTextColor(helperTextViewTextColor);
    }
    
    public void setHelperTextEnabled(final boolean helperTextEnabled) {
        this.indicatorViewController.setHelperTextEnabled(helperTextEnabled);
    }
    
    public void setHelperTextTextAppearance(final int helperTextAppearance) {
        this.indicatorViewController.setHelperTextAppearance(helperTextAppearance);
    }
    
    public void setHint(final CharSequence hintInternal) {
        if (this.hintEnabled) {
            this.setHintInternal(hintInternal);
            this.sendAccessibilityEvent(2048);
        }
    }
    
    public void setHintAnimationEnabled(final boolean hintAnimationEnabled) {
        this.hintAnimationEnabled = hintAnimationEnabled;
    }
    
    public void setHintEnabled(final boolean hintEnabled) {
        if (hintEnabled != this.hintEnabled) {
            if (!(this.hintEnabled = hintEnabled)) {
                this.isProvidingHint = false;
                if (!TextUtils.isEmpty(this.hint) && TextUtils.isEmpty(this.editText.getHint())) {
                    this.editText.setHint(this.hint);
                }
                this.setHintInternal(null);
            }
            else {
                final CharSequence hint = this.editText.getHint();
                if (!TextUtils.isEmpty(hint)) {
                    if (TextUtils.isEmpty(this.hint)) {
                        this.setHint(hint);
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
    
    public void setHintTextAppearance(final int collapsedTextAppearance) {
        this.collapsingTextHelper.setCollapsedTextAppearance(collapsedTextAppearance);
        this.focusedTextColor = this.collapsingTextHelper.getCollapsedTextColor();
        if (this.editText != null) {
            this.updateLabelState(false);
            this.updateInputLayoutMargins();
        }
    }
    
    public void setPasswordVisibilityToggleContentDescription(final int n) {
        CharSequence text;
        if (n != 0) {
            text = this.getResources().getText(n);
        }
        else {
            text = null;
        }
        this.setPasswordVisibilityToggleContentDescription(text);
    }
    
    public void setPasswordVisibilityToggleContentDescription(final CharSequence charSequence) {
        this.passwordToggleContentDesc = charSequence;
        if (this.passwordToggleView != null) {
            this.passwordToggleView.setContentDescription(charSequence);
        }
    }
    
    public void setPasswordVisibilityToggleDrawable(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setPasswordVisibilityToggleDrawable(drawable);
    }
    
    public void setPasswordVisibilityToggleDrawable(final Drawable drawable) {
        this.passwordToggleDrawable = drawable;
        if (this.passwordToggleView != null) {
            this.passwordToggleView.setImageDrawable(drawable);
        }
    }
    
    public void setPasswordVisibilityToggleEnabled(final boolean passwordToggleEnabled) {
        if (this.passwordToggleEnabled != passwordToggleEnabled) {
            this.passwordToggleEnabled = passwordToggleEnabled;
            if (!passwordToggleEnabled && this.passwordToggledVisible && this.editText != null) {
                this.editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            }
            this.passwordToggledVisible = false;
            this.updatePasswordToggleView();
        }
    }
    
    public void setPasswordVisibilityToggleTintList(final ColorStateList passwordToggleTintList) {
        this.passwordToggleTintList = passwordToggleTintList;
        this.hasPasswordToggleTintList = true;
        this.applyPasswordToggleTint();
    }
    
    public void setPasswordVisibilityToggleTintMode(final PorterDuff$Mode passwordToggleTintMode) {
        this.passwordToggleTintMode = passwordToggleTintMode;
        this.hasPasswordToggleTintMode = true;
        this.applyPasswordToggleTint();
    }
    
    void setTextAppearanceCompatWithErrorFallback(final TextView textView, int defaultColor) {
        final int n = 1;
        while (true) {
            try {
                TextViewCompat.setTextAppearance(textView, defaultColor);
                if (Build$VERSION.SDK_INT >= 23) {
                    defaultColor = textView.getTextColors().getDefaultColor();
                    if (defaultColor == -65281) {
                        defaultColor = n;
                        break Label_0037;
                    }
                }
                defaultColor = 0;
                if (defaultColor != 0) {
                    TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_AppCompat_Caption);
                    textView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.design_error));
                }
            }
            catch (Exception ex) {
                defaultColor = n;
                continue;
            }
            break;
        }
    }
    
    public void setTextInputAccessibilityDelegate(final AccessibilityDelegate accessibilityDelegate) {
        if (this.editText != null) {
            ViewCompat.setAccessibilityDelegate((View)this.editText, accessibilityDelegate);
        }
    }
    
    public void setTypeface(final Typeface typeface) {
        if (typeface != this.typeface) {
            this.typeface = typeface;
            this.collapsingTextHelper.setTypefaces(typeface);
            this.indicatorViewController.setTypefaces(typeface);
            if (this.counterView != null) {
                this.counterView.setTypeface(typeface);
            }
        }
    }
    
    void updateCounter(final int i) {
        final boolean counterOverflowed = this.counterOverflowed;
        if (this.counterMaxLength == -1) {
            this.counterView.setText((CharSequence)String.valueOf(i));
            this.counterView.setContentDescription((CharSequence)null);
            this.counterOverflowed = false;
        }
        else {
            if (ViewCompat.getAccessibilityLiveRegion((View)this.counterView) == 1) {
                ViewCompat.setAccessibilityLiveRegion((View)this.counterView, 0);
            }
            this.counterOverflowed = (i > this.counterMaxLength);
            if (counterOverflowed != this.counterOverflowed) {
                final TextView counterView = this.counterView;
                int n;
                if (this.counterOverflowed) {
                    n = this.counterOverflowTextAppearance;
                }
                else {
                    n = this.counterTextAppearance;
                }
                this.setTextAppearanceCompatWithErrorFallback(counterView, n);
                if (this.counterOverflowed) {
                    ViewCompat.setAccessibilityLiveRegion((View)this.counterView, 1);
                }
            }
            this.counterView.setText((CharSequence)this.getContext().getString(R.string.character_counter_pattern, new Object[] { i, this.counterMaxLength }));
            this.counterView.setContentDescription((CharSequence)this.getContext().getString(R.string.character_counter_content_description, new Object[] { i, this.counterMaxLength }));
        }
        if (this.editText != null && counterOverflowed != this.counterOverflowed) {
            this.updateLabelState(false);
            this.updateTextInputBoxState();
            this.updateEditTextBackground();
        }
    }
    
    void updateEditTextBackground() {
        if (this.editText == null) {
            return;
        }
        final Drawable background = this.editText.getBackground();
        if (background == null) {
            return;
        }
        this.ensureBackgroundDrawableStateWorkaround();
        Drawable mutate = background;
        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(background)) {
            mutate = background.mutate();
        }
        if (this.indicatorViewController.errorShouldBeShown()) {
            mutate.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(this.indicatorViewController.getErrorViewCurrentTextColor(), PorterDuff$Mode.SRC_IN));
        }
        else if (this.counterOverflowed && this.counterView != null) {
            mutate.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(this.counterView.getCurrentTextColor(), PorterDuff$Mode.SRC_IN));
        }
        else {
            DrawableCompat.clearColorFilter(mutate);
            this.editText.refreshDrawableState();
        }
    }
    
    void updateLabelState(final boolean b) {
        this.updateLabelState(b, false);
    }
    
    void updateTextInputBoxState() {
        if (this.boxBackground != null && this.boxBackgroundMode != 0) {
            final EditText editText = this.editText;
            boolean b = true;
            final boolean b2 = editText != null && this.editText.hasFocus();
            if (this.editText == null || !this.editText.isHovered()) {
                b = false;
            }
            if (this.boxBackgroundMode == 2) {
                if (!this.isEnabled()) {
                    this.boxStrokeColor = this.disabledColor;
                }
                else if (this.indicatorViewController.errorShouldBeShown()) {
                    this.boxStrokeColor = this.indicatorViewController.getErrorViewCurrentTextColor();
                }
                else if (this.counterOverflowed && this.counterView != null) {
                    this.boxStrokeColor = this.counterView.getCurrentTextColor();
                }
                else if (b2) {
                    this.boxStrokeColor = this.focusedStrokeColor;
                }
                else if (b) {
                    this.boxStrokeColor = this.hoveredStrokeColor;
                }
                else {
                    this.boxStrokeColor = this.defaultStrokeColor;
                }
                if ((b || b2) && this.isEnabled()) {
                    this.boxStrokeWidthPx = this.boxStrokeWidthFocusedPx;
                }
                else {
                    this.boxStrokeWidthPx = this.boxStrokeWidthDefaultPx;
                }
                this.applyBoxAttributes();
            }
        }
    }
    
    public static class AccessibilityDelegate extends AccessibilityDelegateCompat
    {
        private final TextInputLayout layout;
        
        public AccessibilityDelegate(final TextInputLayout layout) {
            this.layout = layout;
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            final EditText editText = this.layout.getEditText();
            Object text;
            if (editText != null) {
                text = editText.getText();
            }
            else {
                text = null;
            }
            final CharSequence hint = this.layout.getHint();
            final CharSequence error = this.layout.getError();
            final CharSequence counterOverflowDescription = this.layout.getCounterOverflowDescription();
            final boolean b = TextUtils.isEmpty((CharSequence)text) ^ true;
            final boolean b2 = TextUtils.isEmpty(hint) ^ true;
            final boolean b3 = TextUtils.isEmpty(error) ^ true;
            final boolean b4 = false;
            final boolean b5 = b3 || !TextUtils.isEmpty(counterOverflowDescription);
            if (b) {
                accessibilityNodeInfoCompat.setText((CharSequence)text);
            }
            else if (b2) {
                accessibilityNodeInfoCompat.setText(hint);
            }
            if (b2) {
                accessibilityNodeInfoCompat.setHintText(hint);
                boolean showingHintText = b4;
                if (!b) {
                    showingHintText = b4;
                    if (b2) {
                        showingHintText = true;
                    }
                }
                accessibilityNodeInfoCompat.setShowingHintText(showingHintText);
            }
            if (b5) {
                CharSequence error2;
                if (b3) {
                    error2 = error;
                }
                else {
                    error2 = counterOverflowDescription;
                }
                accessibilityNodeInfoCompat.setError(error2);
                accessibilityNodeInfoCompat.setContentInvalid(true);
            }
        }
        
        @Override
        public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            final EditText editText = this.layout.getEditText();
            Editable text;
            if (editText != null) {
                text = editText.getText();
            }
            else {
                text = null;
            }
            Object hint = text;
            if (TextUtils.isEmpty((CharSequence)text)) {
                hint = this.layout.getHint();
            }
            if (!TextUtils.isEmpty((CharSequence)hint)) {
                accessibilityEvent.getText().add(hint);
            }
        }
    }
    
    static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        CharSequence error;
        boolean isPasswordToggledVisible;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            final int int1 = parcel.readInt();
            boolean isPasswordToggledVisible = true;
            if (int1 != 1) {
                isPasswordToggledVisible = false;
            }
            this.isPasswordToggledVisible = isPasswordToggledVisible;
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("TextInputLayout.SavedState{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" error=");
            sb.append((Object)this.error);
            sb.append("}");
            return sb.toString();
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            TextUtils.writeToParcel(this.error, parcel, n);
            parcel.writeInt((int)(this.isPasswordToggledVisible ? 1 : 0));
        }
    }
}
