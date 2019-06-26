// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.graphics.Point;
import android.widget.FrameLayout$LayoutParams;
import android.view.VelocityTracker;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.NestedScrollingParent;
import org.telegram.messenger.LocaleController;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import android.view.View$MeasureSpec;
import android.view.Window;
import android.view.View$OnClickListener;
import android.view.View$OnTouchListener;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.Components.LayoutHelper;
import android.view.ViewGroup$LayoutParams;
import android.os.Bundle;
import android.content.res.Configuration;
import android.widget.FrameLayout;
import android.content.DialogInterface;
import org.telegram.messenger.AndroidUtilities;
import android.animation.Animator$AnimatorListener;
import android.view.WindowManager$LayoutParams;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View$OnApplyWindowInsetsListener;
import org.telegram.messenger.FileLog;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Rect;
import android.view.ViewConfiguration;
import android.os.Build$VERSION;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.content.DialogInterface$OnClickListener;
import android.view.WindowInsets;
import java.util.ArrayList;
import android.view.View;
import android.animation.AnimatorSet;
import android.view.ViewGroup;
import android.graphics.drawable.ColorDrawable;
import android.app.Dialog;

public class BottomSheet extends Dialog
{
    private boolean allowCustomAnimation;
    private boolean allowDrawContent;
    private boolean allowNestedScroll;
    private boolean applyBottomPadding;
    private boolean applyTopPadding;
    protected ColorDrawable backDrawable;
    protected int backgroundPaddingLeft;
    protected int backgroundPaddingTop;
    protected ContainerView container;
    protected ViewGroup containerView;
    protected int currentAccount;
    protected AnimatorSet currentSheetAnimation;
    private View customView;
    private BottomSheetDelegateInterface delegate;
    private boolean dimBehind;
    private Runnable dismissRunnable;
    private boolean dismissed;
    private boolean focusable;
    protected boolean fullWidth;
    protected boolean isFullscreen;
    private int[] itemIcons;
    private ArrayList<BottomSheetCell> itemViews;
    private CharSequence[] items;
    private WindowInsets lastInsets;
    private int layoutCount;
    protected View nestedScrollChild;
    private DialogInterface$OnClickListener onClickListener;
    private Drawable shadowDrawable;
    private boolean showWithoutAnimation;
    private Runnable startAnimationRunnable;
    private int tag;
    private CharSequence title;
    private TextView titleView;
    private int touchSlop;
    private boolean useFastDismiss;
    private boolean useHardwareLayer;
    
    public BottomSheet(final Context context, final boolean focusable, final int n) {
        super(context, 2131624225);
        this.currentAccount = UserConfig.selectedAccount;
        this.allowDrawContent = true;
        this.useHardwareLayer = true;
        this.backDrawable = new ColorDrawable(-16777216);
        this.allowCustomAnimation = true;
        this.dimBehind = true;
        this.allowNestedScroll = true;
        this.applyTopPadding = true;
        this.applyBottomPadding = true;
        this.itemViews = new ArrayList<BottomSheetCell>();
        this.dismissRunnable = new _$$Lambda$wKJSb77Iz9CSKJu9VMkyxGvOd_c(this);
        if (Build$VERSION.SDK_INT >= 21) {
            this.getWindow().addFlags(-2147417856);
        }
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        final Rect rect = new Rect();
        if (n == 0) {
            this.shadowDrawable = context.getResources().getDrawable(2131165823).mutate();
        }
        else if (n == 1) {
            this.shadowDrawable = context.getResources().getDrawable(2131165824).mutate();
        }
        this.shadowDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogBackground"), PorterDuff$Mode.MULTIPLY));
        this.shadowDrawable.getPadding(rect);
        this.backgroundPaddingLeft = rect.left;
        this.backgroundPaddingTop = rect.top;
        (this.container = (ContainerView)new ContainerView(this.getContext()) {
            public boolean drawChild(final Canvas canvas, final View view, final long n) {
                boolean b = true;
                try {
                    if (!BottomSheet.this.allowDrawContent || !super.drawChild(canvas, view, n)) {
                        b = false;
                    }
                    return b;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    return true;
                }
            }
        }).setBackgroundDrawable((Drawable)this.backDrawable);
        this.focusable = focusable;
        if (Build$VERSION.SDK_INT >= 21) {
            this.container.setFitsSystemWindows(true);
            this.container.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new _$$Lambda$BottomSheet$IjDyKTRWpdCwBFc4MNcspRHUp7w(this));
            this.container.setSystemUiVisibility(1280);
        }
        this.backDrawable.setAlpha(0);
    }
    
    private void cancelSheetAnimation() {
        final AnimatorSet currentSheetAnimation = this.currentSheetAnimation;
        if (currentSheetAnimation != null) {
            currentSheetAnimation.cancel();
            this.currentSheetAnimation = null;
        }
    }
    
    private void startOpenAnimation() {
        if (this.dismissed) {
            return;
        }
        this.containerView.setVisibility(0);
        if (!this.onCustomOpenAnimation()) {
            if (Build$VERSION.SDK_INT >= 20 && this.useHardwareLayer) {
                this.container.setLayerType(2, (Paint)null);
            }
            final ViewGroup containerView = this.containerView;
            containerView.setTranslationY((float)containerView.getMeasuredHeight());
            final AnimatorSet currentSheetAnimation = new AnimatorSet();
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.containerView, "translationY", new float[] { 0.0f });
            final ColorDrawable backDrawable = this.backDrawable;
            int n;
            if (this.dimBehind) {
                n = 51;
            }
            else {
                n = 0;
            }
            currentSheetAnimation.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofInt((Object)backDrawable, "alpha", new int[] { n }) });
            currentSheetAnimation.setDuration(400L);
            currentSheetAnimation.setStartDelay(20L);
            currentSheetAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
            currentSheetAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    final AnimatorSet currentSheetAnimation = BottomSheet.this.currentSheetAnimation;
                    if (currentSheetAnimation != null && currentSheetAnimation.equals(obj)) {
                        BottomSheet.this.currentSheetAnimation = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    final AnimatorSet currentSheetAnimation = BottomSheet.this.currentSheetAnimation;
                    if (currentSheetAnimation != null && currentSheetAnimation.equals(obj)) {
                        final BottomSheet this$0 = BottomSheet.this;
                        this$0.currentSheetAnimation = null;
                        if (this$0.delegate != null) {
                            BottomSheet.this.delegate.onOpenAnimationEnd();
                        }
                        if (BottomSheet.this.useHardwareLayer) {
                            BottomSheet.this.container.setLayerType(0, (Paint)null);
                        }
                        final BottomSheet this$2 = BottomSheet.this;
                        if (this$2.isFullscreen) {
                            final WindowManager$LayoutParams attributes = this$2.getWindow().getAttributes();
                            attributes.flags &= 0xFFFFFBFF;
                            BottomSheet.this.getWindow().setAttributes(attributes);
                        }
                    }
                }
            });
            currentSheetAnimation.start();
            this.currentSheetAnimation = currentSheetAnimation;
        }
    }
    
    protected boolean canDismissWithSwipe() {
        return true;
    }
    
    protected boolean canDismissWithTouchOutside() {
        return true;
    }
    
    public void dismiss() {
        final BottomSheetDelegateInterface delegate = this.delegate;
        if (delegate != null && !delegate.canDismiss()) {
            return;
        }
        if (this.dismissed) {
            return;
        }
        this.dismissed = true;
        this.cancelSheetAnimation();
        if (!this.allowCustomAnimation || !this.onCustomCloseAnimation()) {
            final AnimatorSet currentSheetAnimation = new AnimatorSet();
            final ViewGroup containerView = this.containerView;
            currentSheetAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)containerView, "translationY", new float[] { (float)(containerView.getMeasuredHeight() + AndroidUtilities.dp(10.0f)) }), (Animator)ObjectAnimator.ofInt((Object)this.backDrawable, "alpha", new int[] { 0 }) });
            if (this.useFastDismiss) {
                final float n = (float)this.containerView.getMeasuredHeight();
                currentSheetAnimation.setDuration((long)Math.max(60, (int)((n - this.containerView.getTranslationY()) * 180.0f / n)));
                this.useFastDismiss = false;
            }
            else {
                currentSheetAnimation.setDuration(180L);
            }
            currentSheetAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
            currentSheetAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    final AnimatorSet currentSheetAnimation = BottomSheet.this.currentSheetAnimation;
                    if (currentSheetAnimation != null && currentSheetAnimation.equals(obj)) {
                        BottomSheet.this.currentSheetAnimation = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    final AnimatorSet currentSheetAnimation = BottomSheet.this.currentSheetAnimation;
                    if (currentSheetAnimation != null && currentSheetAnimation.equals(obj)) {
                        BottomSheet.this.currentSheetAnimation = null;
                        AndroidUtilities.runOnUIThread(new _$$Lambda$BottomSheet$6$VTgE_oeIT2bQ5t_sdXAKcokhgP8(this));
                    }
                }
            });
            currentSheetAnimation.start();
            this.currentSheetAnimation = currentSheetAnimation;
        }
    }
    
    public void dismissInternal() {
        try {
            super.dismiss();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void dismissWithButtonClick(final int n) {
        if (this.dismissed) {
            return;
        }
        this.dismissed = true;
        this.cancelSheetAnimation();
        final AnimatorSet currentSheetAnimation = new AnimatorSet();
        final ViewGroup containerView = this.containerView;
        currentSheetAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)containerView, "translationY", new float[] { (float)(containerView.getMeasuredHeight() + AndroidUtilities.dp(10.0f)) }), (Animator)ObjectAnimator.ofInt((Object)this.backDrawable, "alpha", new int[] { 0 }) });
        currentSheetAnimation.setDuration(180L);
        currentSheetAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
        currentSheetAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator obj) {
                final AnimatorSet currentSheetAnimation = BottomSheet.this.currentSheetAnimation;
                if (currentSheetAnimation != null && currentSheetAnimation.equals(obj)) {
                    BottomSheet.this.currentSheetAnimation = null;
                }
            }
            
            public void onAnimationEnd(final Animator obj) {
                final AnimatorSet currentSheetAnimation = BottomSheet.this.currentSheetAnimation;
                if (currentSheetAnimation != null && currentSheetAnimation.equals(obj)) {
                    final BottomSheet this$0 = BottomSheet.this;
                    this$0.currentSheetAnimation = null;
                    if (this$0.onClickListener != null) {
                        BottomSheet.this.onClickListener.onClick((DialogInterface)BottomSheet.this, n);
                    }
                    AndroidUtilities.runOnUIThread(new _$$Lambda$BottomSheet$5$CikgvDyZEWn0favL4ZqbmH9PuGE(this));
                }
            }
        });
        currentSheetAnimation.start();
        this.currentSheetAnimation = currentSheetAnimation;
    }
    
    public FrameLayout getContainer() {
        return this.container;
    }
    
    protected int getLeftInset() {
        final WindowInsets lastInsets = this.lastInsets;
        if (lastInsets != null && Build$VERSION.SDK_INT >= 21) {
            return lastInsets.getSystemWindowInsetLeft();
        }
        return 0;
    }
    
    protected int getRightInset() {
        final WindowInsets lastInsets = this.lastInsets;
        if (lastInsets != null && Build$VERSION.SDK_INT >= 21) {
            return lastInsets.getSystemWindowInsetRight();
        }
        return 0;
    }
    
    public ViewGroup getSheetContainer() {
        return this.containerView;
    }
    
    public int getTag() {
        return this.tag;
    }
    
    public TextView getTitleView() {
        return this.titleView;
    }
    
    public boolean isDismissed() {
        return this.dismissed;
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
    }
    
    public void onContainerDraw(final Canvas canvas) {
    }
    
    protected boolean onContainerTouchEvent(final MotionEvent motionEvent) {
        return false;
    }
    
    protected void onContainerTranslationYChanged(final float n) {
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final Window window = this.getWindow();
        window.setWindowAnimations(2131624098);
        this.setContentView((View)this.container, new ViewGroup$LayoutParams(-1, -1));
        if (this.containerView == null) {
            (this.containerView = (ViewGroup)new FrameLayout(this.getContext()) {
                public boolean hasOverlappingRendering() {
                    return false;
                }
                
                public void setTranslationY(final float translationY) {
                    super.setTranslationY(translationY);
                    BottomSheet.this.onContainerTranslationYChanged(translationY);
                }
            }).setBackgroundDrawable(this.shadowDrawable);
            final ViewGroup containerView = this.containerView;
            final int backgroundPaddingLeft = this.backgroundPaddingLeft;
            int dp;
            if (this.applyTopPadding) {
                dp = AndroidUtilities.dp(8.0f);
            }
            else {
                dp = 0;
            }
            final int backgroundPaddingTop = this.backgroundPaddingTop;
            final int backgroundPaddingLeft2 = this.backgroundPaddingLeft;
            int dp2;
            if (this.applyBottomPadding) {
                dp2 = AndroidUtilities.dp(8.0f);
            }
            else {
                dp2 = 0;
            }
            containerView.setPadding(backgroundPaddingLeft, dp + backgroundPaddingTop - 1, backgroundPaddingLeft2, dp2);
        }
        this.containerView.setVisibility(4);
        this.container.addView((View)this.containerView, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 80));
        int n;
        if (this.title != null) {
            (this.titleView = new TextView(this.getContext())).setLines(1);
            this.titleView.setSingleLine(true);
            this.titleView.setText(this.title);
            this.titleView.setTextColor(Theme.getColor("dialogTextGray2"));
            this.titleView.setTextSize(1, 16.0f);
            this.titleView.setEllipsize(TextUtils$TruncateAt.MIDDLE);
            this.titleView.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
            this.titleView.setGravity(16);
            this.containerView.addView((View)this.titleView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48.0f));
            this.titleView.setOnTouchListener((View$OnTouchListener)_$$Lambda$BottomSheet$bysjO3P7kPXgYfq_9zd4_H2r0_8.INSTANCE);
            n = 48;
        }
        else {
            n = 0;
        }
        final View customView = this.customView;
        if (customView != null) {
            if (customView.getParent() != null) {
                ((ViewGroup)this.customView.getParent()).removeView(this.customView);
            }
            this.containerView.addView(this.customView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, (float)n, 0.0f, 0.0f));
        }
        else if (this.items != null) {
            final int n2 = 0;
            int n3 = n;
            int i = n2;
            while (true) {
                final CharSequence[] items = this.items;
                if (i >= items.length) {
                    break;
                }
                if (items[i] != null) {
                    final BottomSheetCell e = new BottomSheetCell(this.getContext(), 0);
                    final CharSequence charSequence = this.items[i];
                    final int[] itemIcons = this.itemIcons;
                    int n4;
                    if (itemIcons != null) {
                        n4 = itemIcons[i];
                    }
                    else {
                        n4 = 0;
                    }
                    e.setTextAndIcon(charSequence, n4);
                    this.containerView.addView((View)e, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, (float)n3, 0.0f, 0.0f));
                    n3 += 48;
                    e.setTag((Object)i);
                    e.setOnClickListener((View$OnClickListener)new _$$Lambda$BottomSheet$6IWrsZfWA7fvlM9_8brqhUJi_uM(this));
                    this.itemViews.add(e);
                }
                ++i;
            }
        }
        final WindowManager$LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.gravity = 51;
        attributes.dimAmount = 0.0f;
        attributes.flags &= 0xFFFFFFFD;
        if (this.focusable) {
            attributes.softInputMode = 16;
        }
        else {
            attributes.flags |= 0x20000;
        }
        if (this.isFullscreen) {
            if (Build$VERSION.SDK_INT >= 21) {
                attributes.flags |= 0x80010100;
            }
            attributes.flags |= 0x400;
            this.container.setSystemUiVisibility(1284);
        }
        attributes.height = -1;
        if (Build$VERSION.SDK_INT >= 28) {
            attributes.layoutInDisplayCutoutMode = 1;
        }
        window.setAttributes(attributes);
    }
    
    protected boolean onCustomCloseAnimation() {
        return false;
    }
    
    protected boolean onCustomLayout(final View view, final int n, final int n2, final int n3, final int n4) {
        return false;
    }
    
    protected boolean onCustomMeasure(final View view, final int n, final int n2) {
        return false;
    }
    
    protected boolean onCustomOpenAnimation() {
        return false;
    }
    
    public void setAllowDrawContent(final boolean allowDrawContent) {
        if (this.allowDrawContent != allowDrawContent) {
            this.allowDrawContent = allowDrawContent;
            final ContainerView container = this.container;
            Object backDrawable;
            if (this.allowDrawContent) {
                backDrawable = this.backDrawable;
            }
            else {
                backDrawable = null;
            }
            container.setBackgroundDrawable((Drawable)backDrawable);
            this.container.invalidate();
        }
    }
    
    public void setAllowNestedScroll(final boolean allowNestedScroll) {
        if (!(this.allowNestedScroll = allowNestedScroll)) {
            this.containerView.setTranslationY(0.0f);
        }
    }
    
    public void setApplyBottomPadding(final boolean applyBottomPadding) {
        this.applyBottomPadding = applyBottomPadding;
    }
    
    public void setApplyTopPadding(final boolean applyTopPadding) {
        this.applyTopPadding = applyTopPadding;
    }
    
    public void setBackgroundColor(final int n) {
        this.shadowDrawable.setColorFilter(n, PorterDuff$Mode.MULTIPLY);
    }
    
    public void setCustomView(final View customView) {
        this.customView = customView;
    }
    
    public void setDelegate(final BottomSheetDelegateInterface delegate) {
        this.delegate = delegate;
    }
    
    public void setDimBehind(final boolean dimBehind) {
        this.dimBehind = dimBehind;
    }
    
    public void setItemColor(final int index, final int textColor, final int n) {
        if (index >= 0) {
            if (index < this.itemViews.size()) {
                final BottomSheetCell bottomSheetCell = this.itemViews.get(index);
                bottomSheetCell.textView.setTextColor(textColor);
                bottomSheetCell.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
            }
        }
    }
    
    public void setItemText(final int index, final CharSequence text) {
        if (index >= 0) {
            if (index < this.itemViews.size()) {
                this.itemViews.get(index).textView.setText(text);
            }
        }
    }
    
    public void setItems(final CharSequence[] items, final int[] itemIcons, final DialogInterface$OnClickListener onClickListener) {
        this.items = items;
        this.itemIcons = itemIcons;
        this.onClickListener = onClickListener;
    }
    
    public void setShowWithoutAnimation(final boolean showWithoutAnimation) {
        this.showWithoutAnimation = showWithoutAnimation;
    }
    
    public void setTitle(final CharSequence title) {
        this.title = title;
    }
    
    public void setTitleColor(final int textColor) {
        final TextView titleView = this.titleView;
        if (titleView == null) {
            return;
        }
        titleView.setTextColor(textColor);
    }
    
    public void show() {
        super.show();
        if (this.focusable) {
            this.getWindow().setSoftInputMode(16);
        }
        int alpha = 0;
        this.dismissed = false;
        this.cancelSheetAnimation();
        this.containerView.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x + this.backgroundPaddingLeft * 2, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, Integer.MIN_VALUE));
        if (this.showWithoutAnimation) {
            final ColorDrawable backDrawable = this.backDrawable;
            if (this.dimBehind) {
                alpha = 51;
            }
            backDrawable.setAlpha(alpha);
            this.containerView.setTranslationY(0.0f);
            return;
        }
        this.backDrawable.setAlpha(0);
        if (Build$VERSION.SDK_INT >= 18) {
            this.layoutCount = 2;
            final ViewGroup containerView = this.containerView;
            containerView.setTranslationY((float)containerView.getMeasuredHeight());
            AndroidUtilities.runOnUIThread(this.startAnimationRunnable = new Runnable() {
                @Override
                public void run() {
                    if (BottomSheet.this.startAnimationRunnable == this) {
                        if (!BottomSheet.this.dismissed) {
                            BottomSheet.this.startAnimationRunnable = null;
                            BottomSheet.this.startOpenAnimation();
                        }
                    }
                }
            }, 150L);
        }
        else {
            this.startOpenAnimation();
        }
    }
    
    public static class BottomSheetCell extends FrameLayout
    {
        private ImageView imageView;
        private TextView textView;
        
        public BottomSheetCell(final Context context, int n) {
            super(context);
            this.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogIcon"), PorterDuff$Mode.MULTIPLY));
            final ImageView imageView = this.imageView;
            final boolean isRTL = LocaleController.isRTL;
            final int n2 = 5;
            int n3;
            if (isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 48, n3 | 0x10));
            (this.textView = new TextView(context)).setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils$TruncateAt.END);
            if (n == 0) {
                this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
                this.textView.setTextSize(1, 16.0f);
                final TextView textView = this.textView;
                if (LocaleController.isRTL) {
                    n = n2;
                }
                else {
                    n = 3;
                }
                this.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, n | 0x10));
            }
            else if (n == 1) {
                this.textView.setGravity(17);
                this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
                this.textView.setTextSize(1, 14.0f);
                this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }
        
        public void setGravity(final int gravity) {
            this.textView.setGravity(gravity);
        }
        
        public void setTextAndIcon(final CharSequence text, int dp) {
            this.textView.setText(text);
            final float n = 16.0f;
            if (dp != 0) {
                this.imageView.setImageResource(dp);
                this.imageView.setVisibility(0);
                final TextView textView = this.textView;
                float n2;
                if (LocaleController.isRTL) {
                    n2 = 16.0f;
                }
                else {
                    n2 = 72.0f;
                }
                dp = AndroidUtilities.dp(n2);
                float n3 = n;
                if (LocaleController.isRTL) {
                    n3 = 72.0f;
                }
                textView.setPadding(dp, 0, AndroidUtilities.dp(n3), 0);
            }
            else {
                this.imageView.setVisibility(4);
                this.textView.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
            }
        }
        
        public void setTextColor(final int textColor) {
            this.textView.setTextColor(textColor);
        }
    }
    
    public static class BottomSheetDelegate implements BottomSheetDelegateInterface
    {
        @Override
        public boolean canDismiss() {
            return true;
        }
        
        @Override
        public void onOpenAnimationEnd() {
        }
        
        @Override
        public void onOpenAnimationStart() {
        }
    }
    
    public interface BottomSheetDelegateInterface
    {
        boolean canDismiss();
        
        void onOpenAnimationEnd();
        
        void onOpenAnimationStart();
    }
    
    public static class Builder
    {
        private BottomSheet bottomSheet;
        
        public Builder(final Context context) {
            this.bottomSheet = new BottomSheet(context, false, 0);
        }
        
        public Builder(final Context context, final boolean b) {
            this.bottomSheet = new BottomSheet(context, b, 0);
        }
        
        public Builder(final Context context, final boolean b, final int n) {
            this.bottomSheet = new BottomSheet(context, b, n);
        }
        
        public BottomSheet create() {
            return this.bottomSheet;
        }
        
        public Runnable getDismissRunnable() {
            return this.bottomSheet.dismissRunnable;
        }
        
        public Builder setApplyBottomPadding(final boolean b) {
            this.bottomSheet.applyBottomPadding = b;
            return this;
        }
        
        public Builder setApplyTopPadding(final boolean b) {
            this.bottomSheet.applyTopPadding = b;
            return this;
        }
        
        public Builder setCustomView(final View view) {
            this.bottomSheet.customView = view;
            return this;
        }
        
        public Builder setDelegate(final BottomSheetDelegate delegate) {
            this.bottomSheet.setDelegate((BottomSheetDelegateInterface)delegate);
            return this;
        }
        
        public BottomSheet setDimBehind(final boolean b) {
            this.bottomSheet.dimBehind = b;
            return this.bottomSheet;
        }
        
        public Builder setItems(final CharSequence[] array, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
            this.bottomSheet.items = array;
            this.bottomSheet.onClickListener = dialogInterface$OnClickListener;
            return this;
        }
        
        public Builder setItems(final CharSequence[] array, final int[] array2, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
            this.bottomSheet.items = array;
            this.bottomSheet.itemIcons = array2;
            this.bottomSheet.onClickListener = dialogInterface$OnClickListener;
            return this;
        }
        
        public Builder setTag(final int n) {
            this.bottomSheet.tag = n;
            return this;
        }
        
        public Builder setTitle(final CharSequence charSequence) {
            this.bottomSheet.title = charSequence;
            return this;
        }
        
        public BottomSheet setUseFullWidth(final boolean fullWidth) {
            final BottomSheet bottomSheet = this.bottomSheet;
            bottomSheet.fullWidth = fullWidth;
            return bottomSheet;
        }
        
        public BottomSheet setUseFullscreen(final boolean isFullscreen) {
            final BottomSheet bottomSheet = this.bottomSheet;
            bottomSheet.isFullscreen = isFullscreen;
            return bottomSheet;
        }
        
        public Builder setUseHardwareLayer(final boolean b) {
            this.bottomSheet.useHardwareLayer = b;
            return this;
        }
        
        public BottomSheet show() {
            this.bottomSheet.show();
            return this.bottomSheet;
        }
    }
    
    protected class ContainerView extends FrameLayout implements NestedScrollingParent
    {
        private AnimatorSet currentAnimation;
        private boolean maybeStartTracking;
        private NestedScrollingParentHelper nestedScrollingParentHelper;
        private boolean startedTracking;
        private int startedTrackingPointerId;
        private int startedTrackingX;
        private int startedTrackingY;
        private VelocityTracker velocityTracker;
        
        public ContainerView(final Context context) {
            super(context);
            this.velocityTracker = null;
            this.startedTrackingPointerId = -1;
            this.maybeStartTracking = false;
            this.startedTracking = false;
            this.currentAnimation = null;
            this.nestedScrollingParentHelper = new NestedScrollingParentHelper((ViewGroup)this);
        }
        
        private void cancelCurrentAnimation() {
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.cancel();
                this.currentAnimation = null;
            }
        }
        
        private void checkDismiss(final float a, final float n) {
            final float translationY = BottomSheet.this.containerView.getTranslationY();
            if ((translationY >= AndroidUtilities.getPixelsInCM(0.8f, false) || (n >= 3500.0f && Math.abs(n) >= Math.abs(a))) && (n >= 0.0f || Math.abs(n) < 3500.0f)) {
                final boolean access$200 = BottomSheet.this.allowCustomAnimation;
                BottomSheet.this.allowCustomAnimation = false;
                BottomSheet.this.useFastDismiss = true;
                BottomSheet.this.dismiss();
                BottomSheet.this.allowCustomAnimation = access$200;
            }
            else {
                (this.currentAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)BottomSheet.this.containerView, "translationY", new float[] { 0.0f }) });
                this.currentAnimation.setDuration((long)(int)(translationY / AndroidUtilities.getPixelsInCM(0.8f, false) * 150.0f));
                this.currentAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
                this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator obj) {
                        if (ContainerView.this.currentAnimation != null && ContainerView.this.currentAnimation.equals(obj)) {
                            ContainerView.this.currentAnimation = null;
                        }
                    }
                });
                this.currentAnimation.start();
            }
        }
        
        public int getNestedScrollAxes() {
            return this.nestedScrollingParentHelper.getNestedScrollAxes();
        }
        
        public boolean hasOverlappingRendering() {
            return false;
        }
        
        protected void onDraw(final Canvas canvas) {
            super.onDraw(canvas);
            BottomSheet.this.onContainerDraw(canvas);
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            if (BottomSheet.this.canDismissWithSwipe()) {
                return this.processTouchEvent(motionEvent, true);
            }
            return super.onInterceptTouchEvent(motionEvent);
        }
        
        protected void onLayout(final boolean b, int n, final int n2, int n3, final int n4) {
            BottomSheet.this.layoutCount--;
            final BottomSheet this$0 = BottomSheet.this;
            int n10;
            int n11;
            if (this$0.containerView != null) {
                if (this$0.lastInsets != null && Build$VERSION.SDK_INT >= 21) {
                    n += BottomSheet.this.lastInsets.getSystemWindowInsetLeft();
                    final int n5 = n3 - BottomSheet.this.lastInsets.getSystemWindowInsetRight();
                    n3 = n;
                    n = n5;
                }
                else {
                    final int n6 = n;
                    n = n3;
                    n3 = n6;
                }
                final int n7 = n4 - n2 - BottomSheet.this.containerView.getMeasuredHeight();
                int n9;
                final int n8 = n9 = (n - n3 - BottomSheet.this.containerView.getMeasuredWidth()) / 2;
                if (BottomSheet.this.lastInsets != null) {
                    n9 = n8;
                    if (Build$VERSION.SDK_INT >= 21) {
                        n9 = n8 + BottomSheet.this.lastInsets.getSystemWindowInsetLeft();
                    }
                }
                final ViewGroup containerView = BottomSheet.this.containerView;
                containerView.layout(n9, n7, containerView.getMeasuredWidth() + n9, BottomSheet.this.containerView.getMeasuredHeight() + n7);
                n10 = n3;
                n11 = n;
            }
            else {
                n11 = n3;
                n10 = n;
            }
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != 8) {
                    final BottomSheet this$2 = BottomSheet.this;
                    if (child != this$2.containerView) {
                        if (!this$2.onCustomLayout(child, n10, n2, n11, n4)) {
                            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                            final int measuredWidth = child.getMeasuredWidth();
                            final int measuredHeight = child.getMeasuredHeight();
                            n3 = frameLayout$LayoutParams.gravity;
                            if ((n = n3) == -1) {
                                n = 51;
                            }
                            final int n12 = n & 0x70;
                            n = (n & 0x7 & 0x7);
                            Label_0409: {
                                if (n != 1) {
                                    if (n != 5) {
                                        n = frameLayout$LayoutParams.leftMargin;
                                        break Label_0409;
                                    }
                                    n3 = n11 - measuredWidth;
                                    n = frameLayout$LayoutParams.rightMargin;
                                }
                                else {
                                    n3 = (n11 - n10 - measuredWidth) / 2 + frameLayout$LayoutParams.leftMargin;
                                    n = frameLayout$LayoutParams.rightMargin;
                                }
                                n = n3 - n;
                            }
                            Label_0500: {
                                int n13;
                                if (n12 != 16) {
                                    if (n12 == 48) {
                                        n3 = frameLayout$LayoutParams.topMargin;
                                        break Label_0500;
                                    }
                                    if (n12 != 80) {
                                        n3 = frameLayout$LayoutParams.topMargin;
                                        break Label_0500;
                                    }
                                    n13 = n4 - n2 - measuredHeight;
                                    n3 = frameLayout$LayoutParams.bottomMargin;
                                }
                                else {
                                    n13 = (n4 - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                                    n3 = frameLayout$LayoutParams.bottomMargin;
                                }
                                n3 = n13 - n3;
                            }
                            int n14 = n;
                            if (BottomSheet.this.lastInsets != null) {
                                n14 = n;
                                if (Build$VERSION.SDK_INT >= 21) {
                                    n14 = n + BottomSheet.this.lastInsets.getSystemWindowInsetLeft();
                                }
                            }
                            child.layout(n14, n3, measuredWidth + n14, measuredHeight + n3);
                        }
                    }
                }
            }
            if (BottomSheet.this.layoutCount == 0 && BottomSheet.this.startAnimationRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(BottomSheet.this.startAnimationRunnable);
                BottomSheet.this.startAnimationRunnable.run();
                BottomSheet.this.startAnimationRunnable = null;
            }
        }
        
        protected void onMeasure(int size, int b) {
            final int size2 = View$MeasureSpec.getSize(size);
            b = (size = View$MeasureSpec.getSize(b));
            if (BottomSheet.this.lastInsets != null) {
                size = b;
                if (Build$VERSION.SDK_INT >= 21) {
                    size = b - BottomSheet.this.lastInsets.getSystemWindowInsetBottom();
                }
            }
            this.setMeasuredDimension(size2, size);
            b = size2;
            if (BottomSheet.this.lastInsets != null) {
                b = size2;
                if (Build$VERSION.SDK_INT >= 21) {
                    b = size2 - (BottomSheet.this.lastInsets.getSystemWindowInsetRight() + BottomSheet.this.lastInsets.getSystemWindowInsetLeft());
                }
            }
            final int n = 0;
            final boolean b2 = b < size;
            final BottomSheet this$0 = BottomSheet.this;
            final ViewGroup containerView = this$0.containerView;
            if (containerView != null) {
                if (!this$0.fullWidth) {
                    int n2;
                    if (AndroidUtilities.isTablet()) {
                        final Point displaySize = AndroidUtilities.displaySize;
                        n2 = View$MeasureSpec.makeMeasureSpec((int)(Math.min(displaySize.x, displaySize.y) * 0.8f) + BottomSheet.this.backgroundPaddingLeft * 2, 1073741824);
                    }
                    else {
                        int n3;
                        if (b2) {
                            n3 = BottomSheet.this.backgroundPaddingLeft * 2 + b;
                        }
                        else {
                            n3 = (int)Math.max(b * 0.8f, (float)Math.min(AndroidUtilities.dp(480.0f), b)) + BottomSheet.this.backgroundPaddingLeft * 2;
                        }
                        n2 = View$MeasureSpec.makeMeasureSpec(n3, 1073741824);
                    }
                    BottomSheet.this.containerView.measure(n2, View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE));
                }
                else {
                    containerView.measure(View$MeasureSpec.makeMeasureSpec(this$0.backgroundPaddingLeft * 2 + b, 1073741824), View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE));
                }
            }
            for (int childCount = this.getChildCount(), i = n; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != 8) {
                    final BottomSheet this$2 = BottomSheet.this;
                    if (child != this$2.containerView) {
                        if (!this$2.onCustomMeasure(child, b, size)) {
                            this.measureChildWithMargins(child, View$MeasureSpec.makeMeasureSpec(b, 1073741824), 0, View$MeasureSpec.makeMeasureSpec(size, 1073741824), 0);
                        }
                    }
                }
            }
        }
        
        public boolean onNestedFling(final View view, final float n, final float n2, final boolean b) {
            return false;
        }
        
        public boolean onNestedPreFling(final View view, final float n, final float n2) {
            return false;
        }
        
        public void onNestedPreScroll(final View view, final int n, final int n2, final int[] array) {
            if (!BottomSheet.this.dismissed) {
                if (BottomSheet.this.allowNestedScroll) {
                    this.cancelCurrentAnimation();
                    final float translationY = BottomSheet.this.containerView.getTranslationY();
                    if (translationY > 0.0f && n2 > 0) {
                        final float n3 = translationY - n2;
                        array[1] = n2;
                        float translationY2 = n3;
                        if (n3 < 0.0f) {
                            translationY2 = 0.0f;
                        }
                        BottomSheet.this.containerView.setTranslationY(translationY2);
                    }
                }
            }
        }
        
        public void onNestedScroll(final View view, final int n, final int n2, final int n3, final int n4) {
            if (!BottomSheet.this.dismissed) {
                if (BottomSheet.this.allowNestedScroll) {
                    this.cancelCurrentAnimation();
                    if (n4 != 0) {
                        float translationY;
                        if ((translationY = BottomSheet.this.containerView.getTranslationY() - n4) < 0.0f) {
                            translationY = 0.0f;
                        }
                        BottomSheet.this.containerView.setTranslationY(translationY);
                    }
                }
            }
        }
        
        public void onNestedScrollAccepted(final View view, final View view2, final int n) {
            this.nestedScrollingParentHelper.onNestedScrollAccepted(view, view2, n);
            if (!BottomSheet.this.dismissed) {
                if (BottomSheet.this.allowNestedScroll) {
                    this.cancelCurrentAnimation();
                }
            }
        }
        
        public boolean onStartNestedScroll(final View view, View nestedScrollChild, final int n) {
            nestedScrollChild = BottomSheet.this.nestedScrollChild;
            return (nestedScrollChild == null || view == nestedScrollChild) && !BottomSheet.this.dismissed && BottomSheet.this.allowNestedScroll && n == 2 && !BottomSheet.this.canDismissWithSwipe();
        }
        
        public void onStopNestedScroll(final View view) {
            this.nestedScrollingParentHelper.onStopNestedScroll(view);
            if (!BottomSheet.this.dismissed) {
                if (BottomSheet.this.allowNestedScroll) {
                    BottomSheet.this.containerView.getTranslationY();
                    this.checkDismiss(0.0f, 0.0f);
                }
            }
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return this.processTouchEvent(motionEvent, false);
        }
        
        boolean processTouchEvent(final MotionEvent motionEvent, final boolean b) {
            final boolean access$000 = BottomSheet.this.dismissed;
            final boolean b2 = false;
            if (access$000) {
                return false;
            }
            if (BottomSheet.this.onContainerTouchEvent(motionEvent)) {
                return true;
            }
            if (BottomSheet.this.canDismissWithTouchOutside() && motionEvent != null && (motionEvent.getAction() == 0 || motionEvent.getAction() == 2) && !this.startedTracking && !this.maybeStartTracking && motionEvent.getPointerCount() == 1) {
                this.startedTrackingX = (int)motionEvent.getX();
                this.startedTrackingY = (int)motionEvent.getY();
                if (this.startedTrackingY < BottomSheet.this.containerView.getTop() || this.startedTrackingX < BottomSheet.this.containerView.getLeft() || this.startedTrackingX > BottomSheet.this.containerView.getRight()) {
                    BottomSheet.this.dismiss();
                    return true;
                }
                this.startedTrackingPointerId = motionEvent.getPointerId(0);
                this.maybeStartTracking = true;
                this.cancelCurrentAnimation();
                final VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.clear();
                }
            }
            else {
                float translationY = 0.0f;
                if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    }
                    final float a = (float)Math.abs((int)(motionEvent.getX() - this.startedTrackingX));
                    final float a2 = (float)((int)motionEvent.getY() - this.startedTrackingY);
                    this.velocityTracker.addMovement(motionEvent);
                    if (this.maybeStartTracking && !this.startedTracking && a2 > 0.0f && a2 / 3.0f > Math.abs(a) && Math.abs(a2) >= BottomSheet.this.touchSlop) {
                        this.startedTrackingY = (int)motionEvent.getY();
                        this.maybeStartTracking = false;
                        this.requestDisallowInterceptTouchEvent(this.startedTracking = true);
                    }
                    else if (this.startedTracking) {
                        final float n = BottomSheet.this.containerView.getTranslationY() + a2;
                        if (n >= 0.0f) {
                            translationY = n;
                        }
                        BottomSheet.this.containerView.setTranslationY(translationY);
                        this.startedTrackingY = (int)motionEvent.getY();
                    }
                }
                else if (motionEvent == null || (motionEvent != null && motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    }
                    this.velocityTracker.computeCurrentVelocity(1000);
                    final float translationY2 = BottomSheet.this.containerView.getTranslationY();
                    if (!this.startedTracking && translationY2 == 0.0f) {
                        this.maybeStartTracking = false;
                        this.startedTracking = false;
                    }
                    else {
                        this.checkDismiss(this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity());
                        this.startedTracking = false;
                    }
                    final VelocityTracker velocityTracker2 = this.velocityTracker;
                    if (velocityTracker2 != null) {
                        velocityTracker2.recycle();
                        this.velocityTracker = null;
                    }
                    this.startedTrackingPointerId = -1;
                }
            }
            if ((b || !this.maybeStartTracking) && !this.startedTracking) {
                final boolean b3 = b2;
                if (BottomSheet.this.canDismissWithSwipe()) {
                    return b3;
                }
            }
            return true;
        }
        
        public void requestDisallowInterceptTouchEvent(final boolean b) {
            if (this.maybeStartTracking && !this.startedTracking) {
                this.onTouchEvent(null);
            }
            super.requestDisallowInterceptTouchEvent(b);
        }
    }
}
