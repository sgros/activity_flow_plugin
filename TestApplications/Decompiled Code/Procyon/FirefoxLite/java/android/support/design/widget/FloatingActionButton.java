// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import java.util.List;
import android.view.ViewGroup;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.os.Bundle;
import android.support.design.stateful.ExtendableSavedState;
import android.os.Parcelable;
import android.support.design.animation.MotionSpec;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.animation.Animator$AnimatorListener;
import android.view.View$MeasureSpec;
import android.graphics.ColorFilter;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.content.res.Resources;
import android.support.design.R;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageHelper;
import android.support.design.expandable.ExpandableWidgetHelper;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v4.view.TintableBackgroundView;
import android.support.design.expandable.ExpandableTransformationWidget;

@CoordinatorLayout.DefaultBehavior(Behavior.class)
public class FloatingActionButton extends VisibilityAwareImageButton implements ExpandableTransformationWidget, TintableBackgroundView, TintableImageSourceView
{
    private ColorStateList backgroundTint;
    private PorterDuff$Mode backgroundTintMode;
    boolean compatPadding;
    private int customSize;
    private final ExpandableWidgetHelper expandableWidgetHelper;
    private final AppCompatImageHelper imageHelper;
    private PorterDuff$Mode imageMode;
    private int imagePadding;
    private ColorStateList imageTint;
    private FloatingActionButtonImpl impl;
    private int maxImageSize;
    private ColorStateList rippleColor;
    final Rect shadowPadding;
    private int size;
    private final Rect touchArea;
    
    static /* synthetic */ void access$101(final FloatingActionButton floatingActionButton, final Drawable backgroundDrawable) {
        floatingActionButton.setBackgroundDrawable(backgroundDrawable);
    }
    
    private FloatingActionButtonImpl createImpl() {
        if (Build$VERSION.SDK_INT >= 21) {
            return new FloatingActionButtonImplLollipop(this, new ShadowDelegateImpl());
        }
        return new FloatingActionButtonImpl(this, new ShadowDelegateImpl());
    }
    
    private FloatingActionButtonImpl getImpl() {
        if (this.impl == null) {
            this.impl = this.createImpl();
        }
        return this.impl;
    }
    
    private int getSizeDimension(int n) {
        if (this.customSize != 0) {
            return this.customSize;
        }
        final Resources resources = this.getResources();
        if (n == -1) {
            if (Math.max(resources.getConfiguration().screenWidthDp, resources.getConfiguration().screenHeightDp) < 470) {
                n = this.getSizeDimension(1);
            }
            else {
                n = this.getSizeDimension(0);
            }
            return n;
        }
        if (n != 1) {
            return resources.getDimensionPixelSize(R.dimen.design_fab_size_normal);
        }
        return resources.getDimensionPixelSize(R.dimen.design_fab_size_mini);
    }
    
    private void offsetRectWithShadow(final Rect rect) {
        rect.left += this.shadowPadding.left;
        rect.top += this.shadowPadding.top;
        rect.right -= this.shadowPadding.right;
        rect.bottom -= this.shadowPadding.bottom;
    }
    
    private void onApplySupportImageTint() {
        final Drawable drawable = this.getDrawable();
        if (drawable == null) {
            return;
        }
        if (this.imageTint == null) {
            DrawableCompat.clearColorFilter(drawable);
            return;
        }
        final int colorForState = this.imageTint.getColorForState(this.getDrawableState(), 0);
        PorterDuff$Mode porterDuff$Mode;
        if ((porterDuff$Mode = this.imageMode) == null) {
            porterDuff$Mode = PorterDuff$Mode.SRC_IN;
        }
        drawable.mutate().setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(colorForState, porterDuff$Mode));
    }
    
    private static int resolveAdjustedSize(int min, int size) {
        final int mode = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        if (mode != Integer.MIN_VALUE) {
            if (mode != 0) {
                if (mode != 1073741824) {
                    throw new IllegalArgumentException();
                }
                min = size;
            }
        }
        else {
            min = Math.min(min, size);
        }
        return min;
    }
    
    private FloatingActionButtonImpl.InternalVisibilityChangedListener wrapOnVisibilityChangedListener(final OnVisibilityChangedListener onVisibilityChangedListener) {
        if (onVisibilityChangedListener == null) {
            return null;
        }
        return new FloatingActionButtonImpl.InternalVisibilityChangedListener() {
            @Override
            public void onHidden() {
                onVisibilityChangedListener.onHidden(FloatingActionButton.this);
            }
            
            @Override
            public void onShown() {
                onVisibilityChangedListener.onShown(FloatingActionButton.this);
            }
        };
    }
    
    public void addOnHideAnimationListener(final Animator$AnimatorListener animator$AnimatorListener) {
        this.getImpl().addOnHideAnimationListener(animator$AnimatorListener);
    }
    
    public void addOnShowAnimationListener(final Animator$AnimatorListener animator$AnimatorListener) {
        this.getImpl().addOnShowAnimationListener(animator$AnimatorListener);
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.getImpl().onDrawableStateChanged(this.getDrawableState());
    }
    
    public ColorStateList getBackgroundTintList() {
        return this.backgroundTint;
    }
    
    public PorterDuff$Mode getBackgroundTintMode() {
        return this.backgroundTintMode;
    }
    
    public float getCompatElevation() {
        return this.getImpl().getElevation();
    }
    
    public float getCompatHoveredFocusedTranslationZ() {
        return this.getImpl().getHoveredFocusedTranslationZ();
    }
    
    public float getCompatPressedTranslationZ() {
        return this.getImpl().getPressedTranslationZ();
    }
    
    public Drawable getContentBackground() {
        return this.getImpl().getContentBackground();
    }
    
    @Deprecated
    public boolean getContentRect(final Rect rect) {
        if (ViewCompat.isLaidOut((View)this)) {
            rect.set(0, 0, this.getWidth(), this.getHeight());
            this.offsetRectWithShadow(rect);
            return true;
        }
        return false;
    }
    
    public int getCustomSize() {
        return this.customSize;
    }
    
    public int getExpandedComponentIdHint() {
        return this.expandableWidgetHelper.getExpandedComponentIdHint();
    }
    
    public MotionSpec getHideMotionSpec() {
        return this.getImpl().getHideMotionSpec();
    }
    
    public void getMeasuredContentRect(final Rect rect) {
        rect.set(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
        this.offsetRectWithShadow(rect);
    }
    
    @Deprecated
    public int getRippleColor() {
        int defaultColor;
        if (this.rippleColor != null) {
            defaultColor = this.rippleColor.getDefaultColor();
        }
        else {
            defaultColor = 0;
        }
        return defaultColor;
    }
    
    public ColorStateList getRippleColorStateList() {
        return this.rippleColor;
    }
    
    public MotionSpec getShowMotionSpec() {
        return this.getImpl().getShowMotionSpec();
    }
    
    public int getSize() {
        return this.size;
    }
    
    int getSizeDimension() {
        return this.getSizeDimension(this.size);
    }
    
    @Override
    public ColorStateList getSupportBackgroundTintList() {
        return this.getBackgroundTintList();
    }
    
    @Override
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        return this.getBackgroundTintMode();
    }
    
    @Override
    public ColorStateList getSupportImageTintList() {
        return this.imageTint;
    }
    
    @Override
    public PorterDuff$Mode getSupportImageTintMode() {
        return this.imageMode;
    }
    
    public boolean getUseCompatPadding() {
        return this.compatPadding;
    }
    
    void hide(final OnVisibilityChangedListener onVisibilityChangedListener, final boolean b) {
        this.getImpl().hide(this.wrapOnVisibilityChangedListener(onVisibilityChangedListener), b);
    }
    
    public boolean isExpanded() {
        return this.expandableWidgetHelper.isExpanded();
    }
    
    public boolean isOrWillBeShown() {
        return this.getImpl().isOrWillBeShown();
    }
    
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.getImpl().jumpDrawableToCurrentState();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getImpl().onAttachedToWindow();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.getImpl().onDetachedFromWindow();
    }
    
    protected void onMeasure(int min, final int n) {
        final int sizeDimension = this.getSizeDimension();
        this.imagePadding = (sizeDimension - this.maxImageSize) / 2;
        this.getImpl().updatePadding();
        min = Math.min(resolveAdjustedSize(sizeDimension, min), resolveAdjustedSize(sizeDimension, n));
        this.setMeasuredDimension(this.shadowPadding.left + min + this.shadowPadding.right, min + this.shadowPadding.top + this.shadowPadding.bottom);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof ExtendableSavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final ExtendableSavedState extendableSavedState = (ExtendableSavedState)parcelable;
        super.onRestoreInstanceState(extendableSavedState.getSuperState());
        this.expandableWidgetHelper.onRestoreInstanceState(extendableSavedState.extendableStates.get("expandableWidgetHelper"));
    }
    
    protected Parcelable onSaveInstanceState() {
        final ExtendableSavedState extendableSavedState = new ExtendableSavedState(super.onSaveInstanceState());
        extendableSavedState.extendableStates.put("expandableWidgetHelper", this.expandableWidgetHelper.onSaveInstanceState());
        return (Parcelable)extendableSavedState;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return (motionEvent.getAction() != 0 || !this.getContentRect(this.touchArea) || this.touchArea.contains((int)motionEvent.getX(), (int)motionEvent.getY())) && super.onTouchEvent(motionEvent);
    }
    
    public void removeOnHideAnimationListener(final Animator$AnimatorListener animator$AnimatorListener) {
        this.getImpl().removeOnHideAnimationListener(animator$AnimatorListener);
    }
    
    public void removeOnShowAnimationListener(final Animator$AnimatorListener animator$AnimatorListener) {
        this.getImpl().removeOnShowAnimationListener(animator$AnimatorListener);
    }
    
    public void setBackgroundColor(final int n) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }
    
    public void setBackgroundDrawable(final Drawable drawable) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }
    
    public void setBackgroundResource(final int n) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }
    
    public void setBackgroundTintList(final ColorStateList list) {
        if (this.backgroundTint != list) {
            this.backgroundTint = list;
            this.getImpl().setBackgroundTintList(list);
        }
    }
    
    public void setBackgroundTintMode(final PorterDuff$Mode porterDuff$Mode) {
        if (this.backgroundTintMode != porterDuff$Mode) {
            this.backgroundTintMode = porterDuff$Mode;
            this.getImpl().setBackgroundTintMode(porterDuff$Mode);
        }
    }
    
    public void setCompatElevation(final float elevation) {
        this.getImpl().setElevation(elevation);
    }
    
    public void setCompatElevationResource(final int n) {
        this.setCompatElevation(this.getResources().getDimension(n));
    }
    
    public void setCompatHoveredFocusedTranslationZ(final float hoveredFocusedTranslationZ) {
        this.getImpl().setHoveredFocusedTranslationZ(hoveredFocusedTranslationZ);
    }
    
    public void setCompatHoveredFocusedTranslationZResource(final int n) {
        this.setCompatHoveredFocusedTranslationZ(this.getResources().getDimension(n));
    }
    
    public void setCompatPressedTranslationZ(final float pressedTranslationZ) {
        this.getImpl().setPressedTranslationZ(pressedTranslationZ);
    }
    
    public void setCompatPressedTranslationZResource(final int n) {
        this.setCompatPressedTranslationZ(this.getResources().getDimension(n));
    }
    
    public void setCustomSize(final int customSize) {
        if (customSize >= 0) {
            this.customSize = customSize;
            return;
        }
        throw new IllegalArgumentException("Custom size must be non-negative");
    }
    
    public void setExpandedComponentIdHint(final int expandedComponentIdHint) {
        this.expandableWidgetHelper.setExpandedComponentIdHint(expandedComponentIdHint);
    }
    
    public void setHideMotionSpec(final MotionSpec hideMotionSpec) {
        this.getImpl().setHideMotionSpec(hideMotionSpec);
    }
    
    public void setHideMotionSpecResource(final int n) {
        this.setHideMotionSpec(MotionSpec.createFromResource(this.getContext(), n));
    }
    
    public void setImageDrawable(final Drawable imageDrawable) {
        super.setImageDrawable(imageDrawable);
        this.getImpl().updateImageMatrixScale();
    }
    
    public void setImageResource(final int imageResource) {
        this.imageHelper.setImageResource(imageResource);
    }
    
    public void setRippleColor(final int n) {
        this.setRippleColor(ColorStateList.valueOf(n));
    }
    
    public void setRippleColor(final ColorStateList rippleColor) {
        if (this.rippleColor != rippleColor) {
            this.rippleColor = rippleColor;
            this.getImpl().setRippleColor(this.rippleColor);
        }
    }
    
    public void setShowMotionSpec(final MotionSpec showMotionSpec) {
        this.getImpl().setShowMotionSpec(showMotionSpec);
    }
    
    public void setShowMotionSpecResource(final int n) {
        this.setShowMotionSpec(MotionSpec.createFromResource(this.getContext(), n));
    }
    
    public void setSize(final int size) {
        this.customSize = 0;
        if (size != this.size) {
            this.size = size;
            this.requestLayout();
        }
    }
    
    @Override
    public void setSupportBackgroundTintList(final ColorStateList backgroundTintList) {
        this.setBackgroundTintList(backgroundTintList);
    }
    
    @Override
    public void setSupportBackgroundTintMode(final PorterDuff$Mode backgroundTintMode) {
        this.setBackgroundTintMode(backgroundTintMode);
    }
    
    @Override
    public void setSupportImageTintList(final ColorStateList imageTint) {
        if (this.imageTint != imageTint) {
            this.imageTint = imageTint;
            this.onApplySupportImageTint();
        }
    }
    
    @Override
    public void setSupportImageTintMode(final PorterDuff$Mode imageMode) {
        if (this.imageMode != imageMode) {
            this.imageMode = imageMode;
            this.onApplySupportImageTint();
        }
    }
    
    public void setUseCompatPadding(final boolean compatPadding) {
        if (this.compatPadding != compatPadding) {
            this.compatPadding = compatPadding;
            this.getImpl().onCompatShadowChanged();
        }
    }
    
    void show(final OnVisibilityChangedListener onVisibilityChangedListener, final boolean b) {
        this.getImpl().show(this.wrapOnVisibilityChangedListener(onVisibilityChangedListener), b);
    }
    
    protected static class BaseBehavior<T extends FloatingActionButton> extends CoordinatorLayout.Behavior<T>
    {
        private boolean autoHideEnabled;
        private OnVisibilityChangedListener internalAutoHideListener;
        private Rect tmpRect;
        
        public BaseBehavior() {
            this.autoHideEnabled = true;
        }
        
        public BaseBehavior(final Context context, final AttributeSet set) {
            super(context, set);
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.FloatingActionButton_Behavior_Layout);
            this.autoHideEnabled = obtainStyledAttributes.getBoolean(R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, true);
            obtainStyledAttributes.recycle();
        }
        
        private static boolean isBottomSheet(final View view) {
            final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
            return layoutParams instanceof LayoutParams && ((LayoutParams)layoutParams).getBehavior() instanceof BottomSheetBehavior;
        }
        
        private void offsetIfNeeded(final CoordinatorLayout coordinatorLayout, final FloatingActionButton floatingActionButton) {
            final Rect shadowPadding = floatingActionButton.shadowPadding;
            if (shadowPadding != null && shadowPadding.centerX() > 0 && shadowPadding.centerY() > 0) {
                final LayoutParams layoutParams = (LayoutParams)floatingActionButton.getLayoutParams();
                final int right = floatingActionButton.getRight();
                final int width = coordinatorLayout.getWidth();
                final int rightMargin = layoutParams.rightMargin;
                int bottom = 0;
                int right2;
                if (right >= width - rightMargin) {
                    right2 = shadowPadding.right;
                }
                else if (floatingActionButton.getLeft() <= layoutParams.leftMargin) {
                    right2 = -shadowPadding.left;
                }
                else {
                    right2 = 0;
                }
                if (floatingActionButton.getBottom() >= coordinatorLayout.getHeight() - layoutParams.bottomMargin) {
                    bottom = shadowPadding.bottom;
                }
                else if (floatingActionButton.getTop() <= layoutParams.topMargin) {
                    bottom = -shadowPadding.top;
                }
                if (bottom != 0) {
                    ViewCompat.offsetTopAndBottom((View)floatingActionButton, bottom);
                }
                if (right2 != 0) {
                    ViewCompat.offsetLeftAndRight((View)floatingActionButton, right2);
                }
            }
        }
        
        private boolean shouldUpdateVisibility(final View view, final FloatingActionButton floatingActionButton) {
            final LayoutParams layoutParams = (LayoutParams)floatingActionButton.getLayoutParams();
            return this.autoHideEnabled && layoutParams.getAnchorId() == view.getId() && floatingActionButton.getUserSetVisibility() == 0;
        }
        
        private boolean updateFabVisibilityForAppBarLayout(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, final FloatingActionButton floatingActionButton) {
            if (!this.shouldUpdateVisibility((View)appBarLayout, floatingActionButton)) {
                return false;
            }
            if (this.tmpRect == null) {
                this.tmpRect = new Rect();
            }
            final Rect tmpRect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(coordinatorLayout, (View)appBarLayout, tmpRect);
            if (tmpRect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                floatingActionButton.hide(this.internalAutoHideListener, false);
            }
            else {
                floatingActionButton.show(this.internalAutoHideListener, false);
            }
            return true;
        }
        
        private boolean updateFabVisibilityForBottomSheet(final View view, final FloatingActionButton floatingActionButton) {
            if (!this.shouldUpdateVisibility(view, floatingActionButton)) {
                return false;
            }
            if (view.getTop() < floatingActionButton.getHeight() / 2 + ((LayoutParams)floatingActionButton.getLayoutParams()).topMargin) {
                floatingActionButton.hide(this.internalAutoHideListener, false);
            }
            else {
                floatingActionButton.show(this.internalAutoHideListener, false);
            }
            return true;
        }
        
        public boolean getInsetDodgeRect(final CoordinatorLayout coordinatorLayout, final FloatingActionButton floatingActionButton, final Rect rect) {
            final Rect shadowPadding = floatingActionButton.shadowPadding;
            rect.set(floatingActionButton.getLeft() + shadowPadding.left, floatingActionButton.getTop() + shadowPadding.top, floatingActionButton.getRight() - shadowPadding.right, floatingActionButton.getBottom() - shadowPadding.bottom);
            return true;
        }
        
        @Override
        public void onAttachedToLayoutParams(final LayoutParams layoutParams) {
            if (layoutParams.dodgeInsetEdges == 0) {
                layoutParams.dodgeInsetEdges = 80;
            }
        }
        
        public boolean onDependentViewChanged(final CoordinatorLayout coordinatorLayout, final FloatingActionButton floatingActionButton, final View view) {
            if (view instanceof AppBarLayout) {
                this.updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout)view, floatingActionButton);
            }
            else if (isBottomSheet(view)) {
                this.updateFabVisibilityForBottomSheet(view, floatingActionButton);
            }
            return false;
        }
        
        public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final FloatingActionButton floatingActionButton, final int n) {
            final List<View> dependencies = coordinatorLayout.getDependencies((View)floatingActionButton);
            for (int size = dependencies.size(), i = 0; i < size; ++i) {
                final View view = dependencies.get(i);
                if (view instanceof AppBarLayout) {
                    if (this.updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout)view, floatingActionButton)) {
                        break;
                    }
                }
                else if (isBottomSheet(view) && this.updateFabVisibilityForBottomSheet(view, floatingActionButton)) {
                    break;
                }
            }
            coordinatorLayout.onLayoutChild((View)floatingActionButton, n);
            this.offsetIfNeeded(coordinatorLayout, floatingActionButton);
            return true;
        }
    }
    
    public static class Behavior extends BaseBehavior<FloatingActionButton>
    {
        public Behavior() {
        }
        
        public Behavior(final Context context, final AttributeSet set) {
            super(context, set);
        }
    }
    
    public abstract static class OnVisibilityChangedListener
    {
        public void onHidden(final FloatingActionButton floatingActionButton) {
        }
        
        public void onShown(final FloatingActionButton floatingActionButton) {
        }
    }
    
    private class ShadowDelegateImpl implements ShadowViewDelegate
    {
        ShadowDelegateImpl() {
        }
        
        @Override
        public float getRadius() {
            return FloatingActionButton.this.getSizeDimension() / 2.0f;
        }
        
        @Override
        public boolean isCompatPaddingEnabled() {
            return FloatingActionButton.this.compatPadding;
        }
        
        @Override
        public void setBackgroundDrawable(final Drawable drawable) {
            FloatingActionButton.access$101(FloatingActionButton.this, drawable);
        }
        
        @Override
        public void setShadowPadding(final int n, final int n2, final int n3, final int n4) {
            FloatingActionButton.this.shadowPadding.set(n, n2, n3, n4);
            FloatingActionButton.this.setPadding(n + FloatingActionButton.this.imagePadding, n2 + FloatingActionButton.this.imagePadding, n3 + FloatingActionButton.this.imagePadding, n4 + FloatingActionButton.this.imagePadding);
        }
    }
}
