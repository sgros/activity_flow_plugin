// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.view.View$OnClickListener;
import android.text.TextUtils;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.Components.RadialProgressView;
import android.text.method.MovementMethod;
import android.widget.FrameLayout$LayoutParams;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.ImageView$ScaleType;
import android.os.Build$VERSION;
import android.widget.LinearLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.os.Bundle;
import android.content.DialogInterface;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.FileLog;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.view.ViewGroup;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.content.Context;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.animation.AnimatorSet;
import android.graphics.drawable.BitmapDrawable;
import android.widget.LinearLayout;
import android.view.ViewTreeObserver$OnScrollChangedListener;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnClickListener;
import android.widget.TextView;
import org.telegram.ui.Components.LineProgressView;
import java.util.ArrayList;
import android.view.View;
import android.widget.ScrollView;
import android.widget.FrameLayout;
import android.graphics.Rect;
import android.graphics.drawable.Drawable$Callback;
import android.app.Dialog;

public class AlertDialog extends Dialog implements Drawable$Callback
{
    private Rect backgroundPaddings;
    protected FrameLayout buttonsLayout;
    private boolean canCacnel;
    private AlertDialog cancelDialog;
    private ScrollView contentScrollView;
    private int currentProgress;
    private View customView;
    private int customViewOffset;
    private boolean dismissDialogByButtons;
    private Runnable dismissRunnable;
    private int[] itemIcons;
    private ArrayList<AlertDialogCell> itemViews;
    private CharSequence[] items;
    private int lastScreenWidth;
    private LineProgressView lineProgressView;
    private TextView lineProgressViewPercent;
    private CharSequence message;
    private TextView messageTextView;
    private boolean messageTextViewClickable;
    private DialogInterface$OnClickListener negativeButtonListener;
    private CharSequence negativeButtonText;
    private DialogInterface$OnClickListener neutralButtonListener;
    private CharSequence neutralButtonText;
    private DialogInterface$OnClickListener onBackButtonListener;
    private DialogInterface$OnCancelListener onCancelListener;
    private DialogInterface$OnClickListener onClickListener;
    private DialogInterface$OnDismissListener onDismissListener;
    private ViewTreeObserver$OnScrollChangedListener onScrollChangedListener;
    private DialogInterface$OnClickListener positiveButtonListener;
    private CharSequence positiveButtonText;
    private FrameLayout progressViewContainer;
    private int progressViewStyle;
    private TextView progressViewTextView;
    private LinearLayout scrollContainer;
    private CharSequence secondTitle;
    private TextView secondTitleTextView;
    private BitmapDrawable[] shadow;
    private AnimatorSet[] shadowAnimation;
    private Drawable shadowDrawable;
    private boolean[] shadowVisibility;
    private CharSequence subtitle;
    private TextView subtitleTextView;
    private CharSequence title;
    private FrameLayout titleContainer;
    private TextView titleTextView;
    private int topBackgroundColor;
    private Drawable topDrawable;
    private int topHeight;
    private ImageView topImageView;
    private int topResId;
    
    public AlertDialog(final Context context, final int progressViewStyle) {
        super(context, 2131624225);
        this.shadow = new BitmapDrawable[2];
        this.shadowVisibility = new boolean[2];
        this.shadowAnimation = new AnimatorSet[2];
        this.customViewOffset = 20;
        this.topHeight = 132;
        this.messageTextViewClickable = true;
        this.canCacnel = true;
        this.dismissDialogByButtons = true;
        this.dismissRunnable = new _$$Lambda$H9iyBEO4Zihg11d8XSg_qvJnAGk(this);
        this.itemViews = new ArrayList<AlertDialogCell>();
        this.backgroundPaddings = new Rect();
        if (progressViewStyle != 3) {
            (this.shadowDrawable = context.getResources().getDrawable(2131165776).mutate()).setColorFilter((ColorFilter)new PorterDuffColorFilter(this.getThemeColor("dialogBackground"), PorterDuff$Mode.MULTIPLY));
            this.shadowDrawable.getPadding(this.backgroundPaddings);
        }
        this.progressViewStyle = progressViewStyle;
    }
    
    private boolean canTextInput(final View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        final ViewGroup viewGroup = (ViewGroup)view;
        int i = viewGroup.getChildCount();
        while (i > 0) {
            if (this.canTextInput(viewGroup.getChildAt(--i))) {
                return true;
            }
        }
        return false;
    }
    
    private void runShadowAnimation(final int n, final boolean b) {
        if ((b && !this.shadowVisibility[n]) || (!b && this.shadowVisibility[n])) {
            this.shadowVisibility[n] = b;
            final AnimatorSet[] shadowAnimation = this.shadowAnimation;
            if (shadowAnimation[n] != null) {
                shadowAnimation[n].cancel();
            }
            this.shadowAnimation[n] = new AnimatorSet();
            final BitmapDrawable[] shadow = this.shadow;
            if (shadow[n] != null) {
                final AnimatorSet set = this.shadowAnimation[n];
                final BitmapDrawable bitmapDrawable = shadow[n];
                int n2;
                if (b) {
                    n2 = 255;
                }
                else {
                    n2 = 0;
                }
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofInt((Object)bitmapDrawable, "alpha", new int[] { n2 }) });
            }
            this.shadowAnimation[n].setDuration(150L);
            this.shadowAnimation[n].addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    if (AlertDialog.this.shadowAnimation[n] != null && AlertDialog.this.shadowAnimation[n].equals(obj)) {
                        AlertDialog.this.shadowAnimation[n] = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (AlertDialog.this.shadowAnimation[n] != null && AlertDialog.this.shadowAnimation[n].equals(obj)) {
                        AlertDialog.this.shadowAnimation[n] = null;
                    }
                }
            });
            try {
                this.shadowAnimation[n].start();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    private void showCancelAlert() {
        if (this.canCacnel) {
            if (this.cancelDialog == null) {
                final Builder builder = new Builder(this.getContext());
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setMessage(LocaleController.getString("StopLoading", 2131560827));
                builder.setPositiveButton(LocaleController.getString("WaitMore", 2131561101), null);
                builder.setNegativeButton(LocaleController.getString("Stop", 2131560820), (DialogInterface$OnClickListener)new _$$Lambda$AlertDialog$1zFp_sikyCYaQ1aMdMAPeCc_86g(this));
                builder.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$AlertDialog$_jIiJ2tOQUco_X_BniQD16XJ3QE(this));
                this.cancelDialog = builder.show();
            }
        }
    }
    
    private void updateLineProgressTextView() {
        this.lineProgressViewPercent.setText((CharSequence)String.format("%d%%", this.currentProgress));
    }
    
    public void dismiss() {
        final AlertDialog cancelDialog = this.cancelDialog;
        if (cancelDialog != null) {
            cancelDialog.dismiss();
        }
        try {
            super.dismiss();
        }
        catch (Throwable t) {}
    }
    
    public View getButton(final int i) {
        final FrameLayout buttonsLayout = this.buttonsLayout;
        if (buttonsLayout != null) {
            return buttonsLayout.findViewWithTag((Object)i);
        }
        return null;
    }
    
    protected int getThemeColor(final String s) {
        return Theme.getColor(s);
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        this.contentScrollView.invalidate();
        this.scrollContainer.invalidate();
    }
    
    public void onBackPressed() {
        super.onBackPressed();
        final DialogInterface$OnClickListener onBackButtonListener = this.onBackButtonListener;
        if (onBackButtonListener != null) {
            onBackButtonListener.onClick((DialogInterface)this, -2);
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final LinearLayout contentView = new LinearLayout(this.getContext()) {
            private boolean inLayout;
            
            public boolean hasOverlappingRendering() {
                return false;
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (AlertDialog.this.progressViewStyle == 3) {
                    AlertDialog.this.showCancelAlert();
                    return false;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
            
            protected void onLayout(final boolean b, int n, int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                if (AlertDialog.this.progressViewStyle == 3) {
                    n = (n3 - n - AlertDialog.this.progressViewContainer.getMeasuredWidth()) / 2;
                    n2 = (n4 - n2 - AlertDialog.this.progressViewContainer.getMeasuredHeight()) / 2;
                    AlertDialog.this.progressViewContainer.layout(n, n2, AlertDialog.this.progressViewContainer.getMeasuredWidth() + n, AlertDialog.this.progressViewContainer.getMeasuredHeight() + n2);
                }
                else if (AlertDialog.this.contentScrollView != null) {
                    if (AlertDialog.this.onScrollChangedListener == null) {
                        AlertDialog.this.onScrollChangedListener = (ViewTreeObserver$OnScrollChangedListener)new _$$Lambda$AlertDialog$1$vvKcenyzvRwmFgV39QOFVkx4krI(this);
                        AlertDialog.this.contentScrollView.getViewTreeObserver().addOnScrollChangedListener(AlertDialog.this.onScrollChangedListener);
                    }
                    AlertDialog.this.onScrollChangedListener.onScrollChanged();
                }
            }
            
            protected void onMeasure(int i, int n) {
                if (AlertDialog.this.progressViewStyle == 3) {
                    AlertDialog.this.progressViewContainer.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0f), 1073741824));
                    this.setMeasuredDimension(View$MeasureSpec.getSize(i), View$MeasureSpec.getSize(n));
                }
                else {
                    this.inLayout = true;
                    final int size = View$MeasureSpec.getSize(i);
                    final int n2 = View$MeasureSpec.getSize(n) - this.getPaddingTop() - this.getPaddingBottom();
                    final int n3 = size - this.getPaddingLeft() - this.getPaddingRight();
                    final int measureSpec = View$MeasureSpec.makeMeasureSpec(n3 - AndroidUtilities.dp(48.0f), 1073741824);
                    final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(n3, 1073741824);
                    final FrameLayout buttonsLayout = AlertDialog.this.buttonsLayout;
                    int n4;
                    if (buttonsLayout != null) {
                        int childCount;
                        View child;
                        for (childCount = buttonsLayout.getChildCount(), i = 0; i < childCount; ++i) {
                            child = AlertDialog.this.buttonsLayout.getChildAt(i);
                            if (child instanceof TextView) {
                                ((TextView)child).setMaxWidth(AndroidUtilities.dp((float)((n3 - AndroidUtilities.dp(24.0f)) / 2)));
                            }
                        }
                        AlertDialog.this.buttonsLayout.measure(measureSpec2, n);
                        final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)AlertDialog.this.buttonsLayout.getLayoutParams();
                        n4 = n2 - (AlertDialog.this.buttonsLayout.getMeasuredHeight() + linearLayout$LayoutParams.bottomMargin + linearLayout$LayoutParams.topMargin);
                    }
                    else {
                        n4 = n2;
                    }
                    if (AlertDialog.this.secondTitleTextView != null) {
                        AlertDialog.this.secondTitleTextView.measure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), Integer.MIN_VALUE), n);
                    }
                    if (AlertDialog.this.titleTextView != null) {
                        if (AlertDialog.this.secondTitleTextView != null) {
                            AlertDialog.this.titleTextView.measure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec) - AlertDialog.this.secondTitleTextView.getMeasuredWidth() - AndroidUtilities.dp(8.0f), 1073741824), n);
                        }
                        else {
                            AlertDialog.this.titleTextView.measure(measureSpec, n);
                        }
                    }
                    i = n4;
                    if (AlertDialog.this.titleContainer != null) {
                        AlertDialog.this.titleContainer.measure(measureSpec, n);
                        final LinearLayout$LayoutParams linearLayout$LayoutParams2 = (LinearLayout$LayoutParams)AlertDialog.this.titleContainer.getLayoutParams();
                        i = n4 - (AlertDialog.this.titleContainer.getMeasuredHeight() + linearLayout$LayoutParams2.bottomMargin + linearLayout$LayoutParams2.topMargin);
                    }
                    int n5 = i;
                    if (AlertDialog.this.subtitleTextView != null) {
                        AlertDialog.this.subtitleTextView.measure(measureSpec, n);
                        final LinearLayout$LayoutParams linearLayout$LayoutParams3 = (LinearLayout$LayoutParams)AlertDialog.this.subtitleTextView.getLayoutParams();
                        n5 = i - (AlertDialog.this.subtitleTextView.getMeasuredHeight() + linearLayout$LayoutParams3.bottomMargin + linearLayout$LayoutParams3.topMargin);
                    }
                    i = n5;
                    if (AlertDialog.this.topImageView != null) {
                        AlertDialog.this.topImageView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)AlertDialog.this.topHeight), 1073741824));
                        i = n5 - (AlertDialog.this.topImageView.getMeasuredHeight() - AndroidUtilities.dp(8.0f));
                    }
                    if (AlertDialog.this.progressViewStyle == 0) {
                        final LinearLayout$LayoutParams linearLayout$LayoutParams4 = (LinearLayout$LayoutParams)AlertDialog.this.contentScrollView.getLayoutParams();
                        if (AlertDialog.this.customView != null) {
                            if (AlertDialog.this.titleTextView == null && AlertDialog.this.messageTextView.getVisibility() == 8 && AlertDialog.this.items == null) {
                                n = AndroidUtilities.dp(16.0f);
                            }
                            else {
                                n = 0;
                            }
                            linearLayout$LayoutParams4.topMargin = n;
                            if (AlertDialog.this.buttonsLayout == null) {
                                n = AndroidUtilities.dp(8.0f);
                            }
                            else {
                                n = 0;
                            }
                            linearLayout$LayoutParams4.bottomMargin = n;
                        }
                        else if (AlertDialog.this.items != null) {
                            if (AlertDialog.this.titleTextView == null && AlertDialog.this.messageTextView.getVisibility() == 8) {
                                n = AndroidUtilities.dp(8.0f);
                            }
                            else {
                                n = 0;
                            }
                            linearLayout$LayoutParams4.topMargin = n;
                            linearLayout$LayoutParams4.bottomMargin = AndroidUtilities.dp(8.0f);
                        }
                        else if (AlertDialog.this.messageTextView.getVisibility() == 0) {
                            if (AlertDialog.this.titleTextView == null) {
                                n = AndroidUtilities.dp(19.0f);
                            }
                            else {
                                n = 0;
                            }
                            linearLayout$LayoutParams4.topMargin = n;
                            linearLayout$LayoutParams4.bottomMargin = AndroidUtilities.dp(20.0f);
                        }
                        i -= linearLayout$LayoutParams4.bottomMargin + linearLayout$LayoutParams4.topMargin;
                        AlertDialog.this.contentScrollView.measure(measureSpec2, View$MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE));
                        i -= AlertDialog.this.contentScrollView.getMeasuredHeight();
                    }
                    else {
                        Label_1021: {
                            int n6;
                            if (AlertDialog.this.progressViewContainer != null) {
                                AlertDialog.this.progressViewContainer.measure(measureSpec, View$MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE));
                                final LinearLayout$LayoutParams linearLayout$LayoutParams5 = (LinearLayout$LayoutParams)AlertDialog.this.progressViewContainer.getLayoutParams();
                                n6 = AlertDialog.this.progressViewContainer.getMeasuredHeight() + linearLayout$LayoutParams5.bottomMargin;
                                n = linearLayout$LayoutParams5.topMargin;
                            }
                            else {
                                n = i;
                                if (AlertDialog.this.messageTextView == null) {
                                    break Label_1021;
                                }
                                AlertDialog.this.messageTextView.measure(measureSpec, View$MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE));
                                n = i;
                                if (AlertDialog.this.messageTextView.getVisibility() == 8) {
                                    break Label_1021;
                                }
                                final LinearLayout$LayoutParams linearLayout$LayoutParams6 = (LinearLayout$LayoutParams)AlertDialog.this.messageTextView.getLayoutParams();
                                n6 = AlertDialog.this.messageTextView.getMeasuredHeight() + linearLayout$LayoutParams6.bottomMargin;
                                n = linearLayout$LayoutParams6.topMargin;
                            }
                            n = i - (n6 + n);
                        }
                        i = n;
                        if (AlertDialog.this.lineProgressView != null) {
                            AlertDialog.this.lineProgressView.measure(measureSpec, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(4.0f), 1073741824));
                            final LinearLayout$LayoutParams linearLayout$LayoutParams7 = (LinearLayout$LayoutParams)AlertDialog.this.lineProgressView.getLayoutParams();
                            i = n - (AlertDialog.this.lineProgressView.getMeasuredHeight() + linearLayout$LayoutParams7.bottomMargin + linearLayout$LayoutParams7.topMargin);
                            AlertDialog.this.lineProgressViewPercent.measure(measureSpec, View$MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE));
                            final LinearLayout$LayoutParams linearLayout$LayoutParams8 = (LinearLayout$LayoutParams)AlertDialog.this.lineProgressViewPercent.getLayoutParams();
                            i -= AlertDialog.this.lineProgressViewPercent.getMeasuredHeight() + linearLayout$LayoutParams8.bottomMargin + linearLayout$LayoutParams8.topMargin;
                        }
                    }
                    this.setMeasuredDimension(size, n2 - i + this.getPaddingTop() + this.getPaddingBottom());
                    this.inLayout = false;
                    if (AlertDialog.this.lastScreenWidth != AndroidUtilities.displaySize.x) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$AlertDialog$1$2il1lPevBw8X_3FhfSjXOpGmbaM(this));
                    }
                }
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (AlertDialog.this.progressViewStyle == 3) {
                    AlertDialog.this.showCancelAlert();
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }
            
            public void requestLayout() {
                if (this.inLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        contentView.setOrientation(1);
        if (this.progressViewStyle == 3) {
            contentView.setBackgroundDrawable((Drawable)null);
        }
        else {
            contentView.setBackgroundDrawable(this.shadowDrawable);
        }
        contentView.setFitsSystemWindows(Build$VERSION.SDK_INT >= 21);
        this.setContentView((View)contentView);
        final boolean b = this.positiveButtonText != null || this.negativeButtonText != null || this.neutralButtonText != null;
        if (this.topResId != 0 || this.topDrawable != null) {
            this.topImageView = new ImageView(this.getContext());
            final Drawable topDrawable = this.topDrawable;
            if (topDrawable != null) {
                this.topImageView.setImageDrawable(topDrawable);
            }
            else {
                this.topImageView.setImageResource(this.topResId);
            }
            this.topImageView.setScaleType(ImageView$ScaleType.CENTER);
            this.topImageView.setBackgroundDrawable(this.getContext().getResources().getDrawable(2131165778));
            this.topImageView.getBackground().setColorFilter((ColorFilter)new PorterDuffColorFilter(this.topBackgroundColor, PorterDuff$Mode.MULTIPLY));
            this.topImageView.setPadding(0, 0, 0, 0);
            contentView.addView((View)this.topImageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, this.topHeight, 51, -8, -8, 0, 0));
        }
        if (this.title != null) {
            contentView.addView((View)(this.titleContainer = new FrameLayout(this.getContext())), (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 24.0f, 0.0f, 24.0f, 0.0f));
            (this.titleTextView = new TextView(this.getContext())).setText(this.title);
            this.titleTextView.setTextColor(this.getThemeColor("dialogTextBlack"));
            this.titleTextView.setTextSize(1, 20.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            final TextView titleTextView = this.titleTextView;
            int n;
            if (LocaleController.isRTL) {
                n = 5;
            }
            else {
                n = 3;
            }
            titleTextView.setGravity(n | 0x30);
            final FrameLayout titleContainer = this.titleContainer;
            final TextView titleTextView2 = this.titleTextView;
            int n2;
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            int n3;
            if (this.subtitle != null) {
                n3 = 2;
            }
            else if (this.items != null) {
                n3 = 14;
            }
            else {
                n3 = 10;
            }
            titleContainer.addView((View)titleTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n2 | 0x30, 0.0f, 19.0f, 0.0f, (float)n3));
        }
        if (this.secondTitle != null && this.title != null) {
            (this.secondTitleTextView = new TextView(this.getContext())).setText(this.secondTitle);
            this.secondTitleTextView.setTextColor(this.getThemeColor("dialogTextGray3"));
            this.secondTitleTextView.setTextSize(1, 18.0f);
            final TextView secondTitleTextView = this.secondTitleTextView;
            int n4;
            if (LocaleController.isRTL) {
                n4 = 3;
            }
            else {
                n4 = 5;
            }
            secondTitleTextView.setGravity(n4 | 0x30);
            final FrameLayout titleContainer2 = this.titleContainer;
            final TextView secondTitleTextView2 = this.secondTitleTextView;
            int n5;
            if (LocaleController.isRTL) {
                n5 = 3;
            }
            else {
                n5 = 5;
            }
            titleContainer2.addView((View)secondTitleTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n5 | 0x30, 0.0f, 21.0f, 0.0f, 0.0f));
        }
        if (this.subtitle != null) {
            (this.subtitleTextView = new TextView(this.getContext())).setText(this.subtitle);
            this.subtitleTextView.setTextColor(this.getThemeColor("dialogIcon"));
            this.subtitleTextView.setTextSize(1, 14.0f);
            final TextView subtitleTextView = this.subtitleTextView;
            int n6;
            if (LocaleController.isRTL) {
                n6 = 5;
            }
            else {
                n6 = 3;
            }
            subtitleTextView.setGravity(n6 | 0x30);
            final TextView subtitleTextView2 = this.subtitleTextView;
            int n7;
            if (LocaleController.isRTL) {
                n7 = 5;
            }
            else {
                n7 = 3;
            }
            int n8;
            if (this.items != null) {
                n8 = 14;
            }
            else {
                n8 = 10;
            }
            contentView.addView((View)subtitleTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n7 | 0x30, 24, 0, 24, n8));
        }
        if (this.progressViewStyle == 0) {
            this.shadow[0] = (BitmapDrawable)this.getContext().getResources().getDrawable(2131165407).mutate();
            this.shadow[1] = (BitmapDrawable)this.getContext().getResources().getDrawable(2131165408).mutate();
            this.shadow[0].setAlpha(0);
            this.shadow[1].setAlpha(0);
            this.shadow[0].setCallback((Drawable$Callback)this);
            this.shadow[1].setCallback((Drawable$Callback)this);
            (this.contentScrollView = new ScrollView(this.getContext()) {
                protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                    final boolean drawChild = super.drawChild(canvas, view, n);
                    if (AlertDialog.this.shadow[0].getPaint().getAlpha() != 0) {
                        AlertDialog.this.shadow[0].setBounds(0, this.getScrollY(), this.getMeasuredWidth(), this.getScrollY() + AndroidUtilities.dp(3.0f));
                        AlertDialog.this.shadow[0].draw(canvas);
                    }
                    if (AlertDialog.this.shadow[1].getPaint().getAlpha() != 0) {
                        AlertDialog.this.shadow[1].setBounds(0, this.getScrollY() + this.getMeasuredHeight() - AndroidUtilities.dp(3.0f), this.getMeasuredWidth(), this.getScrollY() + this.getMeasuredHeight());
                        AlertDialog.this.shadow[1].draw(canvas);
                    }
                    return drawChild;
                }
            }).setVerticalScrollBarEnabled(false);
            AndroidUtilities.setScrollViewEdgeEffectColor(this.contentScrollView, this.getThemeColor("dialogScrollGlow"));
            contentView.addView((View)this.contentScrollView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 0.0f));
            (this.scrollContainer = new LinearLayout(this.getContext())).setOrientation(1);
            this.contentScrollView.addView((View)this.scrollContainer, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -2));
        }
        (this.messageTextView = new TextView(this.getContext())).setTextColor(this.getThemeColor("dialogTextBlack"));
        this.messageTextView.setTextSize(1, 16.0f);
        this.messageTextView.setMovementMethod((MovementMethod)new AndroidUtilities.LinkMovementMethodMy());
        this.messageTextView.setLinkTextColor(this.getThemeColor("dialogTextLink"));
        if (!this.messageTextViewClickable) {
            this.messageTextView.setClickable(false);
            this.messageTextView.setEnabled(false);
        }
        final TextView messageTextView = this.messageTextView;
        int n9;
        if (LocaleController.isRTL) {
            n9 = 5;
        }
        else {
            n9 = 3;
        }
        messageTextView.setGravity(n9 | 0x30);
        final int progressViewStyle = this.progressViewStyle;
        if (progressViewStyle == 1) {
            this.progressViewContainer = new FrameLayout(this.getContext());
            final FrameLayout progressViewContainer = this.progressViewContainer;
            int n10;
            if (this.title == null) {
                n10 = 24;
            }
            else {
                n10 = 0;
            }
            contentView.addView((View)progressViewContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 44, 51, 23, n10, 23, 24));
            final RadialProgressView radialProgressView = new RadialProgressView(this.getContext());
            radialProgressView.setProgressColor(this.getThemeColor("dialogProgressCircle"));
            final FrameLayout progressViewContainer2 = this.progressViewContainer;
            int n11;
            if (LocaleController.isRTL) {
                n11 = 5;
            }
            else {
                n11 = 3;
            }
            progressViewContainer2.addView((View)radialProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(44, 44, n11 | 0x30));
            this.messageTextView.setLines(1);
            this.messageTextView.setEllipsize(TextUtils$TruncateAt.END);
            final FrameLayout progressViewContainer3 = this.progressViewContainer;
            final TextView messageTextView2 = this.messageTextView;
            int n12;
            if (LocaleController.isRTL) {
                n12 = 5;
            }
            else {
                n12 = 3;
            }
            int n13;
            if (LocaleController.isRTL) {
                n13 = 0;
            }
            else {
                n13 = 62;
            }
            final float n14 = (float)n13;
            int n15;
            if (LocaleController.isRTL) {
                n15 = 62;
            }
            else {
                n15 = 0;
            }
            progressViewContainer3.addView((View)messageTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n12 | 0x10, n14, 0.0f, (float)n15, 0.0f));
        }
        else if (progressViewStyle == 2) {
            final TextView messageTextView3 = this.messageTextView;
            int n16;
            if (LocaleController.isRTL) {
                n16 = 5;
            }
            else {
                n16 = 3;
            }
            int n17;
            if (this.title == null) {
                n17 = 19;
            }
            else {
                n17 = 0;
            }
            contentView.addView((View)messageTextView3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n16 | 0x30, 24, n17, 24, 20));
            (this.lineProgressView = new LineProgressView(this.getContext())).setProgress(this.currentProgress / 100.0f, false);
            this.lineProgressView.setProgressColor(this.getThemeColor("dialogLineProgress"));
            this.lineProgressView.setBackColor(this.getThemeColor("dialogLineProgressBackground"));
            contentView.addView((View)this.lineProgressView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 4, 19, 24, 0, 24, 0));
            (this.lineProgressViewPercent = new TextView(this.getContext())).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            final TextView lineProgressViewPercent = this.lineProgressViewPercent;
            int n18;
            if (LocaleController.isRTL) {
                n18 = 5;
            }
            else {
                n18 = 3;
            }
            lineProgressViewPercent.setGravity(n18 | 0x30);
            this.lineProgressViewPercent.setTextColor(this.getThemeColor("dialogTextGray2"));
            this.lineProgressViewPercent.setTextSize(1, 14.0f);
            final TextView lineProgressViewPercent2 = this.lineProgressViewPercent;
            int n19;
            if (LocaleController.isRTL) {
                n19 = 5;
            }
            else {
                n19 = 3;
            }
            contentView.addView((View)lineProgressViewPercent2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n19 | 0x30, 23, 4, 23, 24));
            this.updateLineProgressTextView();
        }
        else if (progressViewStyle == 3) {
            this.setCanceledOnTouchOutside(false);
            this.setCancelable(false);
            (this.progressViewContainer = new FrameLayout(this.getContext())).setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), Theme.getColor("dialog_inlineProgressBackground")));
            contentView.addView((View)this.progressViewContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(86, 86, 17));
            final RadialProgressView radialProgressView2 = new RadialProgressView(this.getContext());
            radialProgressView2.setProgressColor(this.getThemeColor("dialog_inlineProgress"));
            this.progressViewContainer.addView((View)radialProgressView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(86, 86));
        }
        else {
            final LinearLayout scrollContainer = this.scrollContainer;
            final TextView messageTextView4 = this.messageTextView;
            int n20;
            if (LocaleController.isRTL) {
                n20 = 5;
            }
            else {
                n20 = 3;
            }
            int customViewOffset;
            if (this.customView == null && this.items == null) {
                customViewOffset = 0;
            }
            else {
                customViewOffset = this.customViewOffset;
            }
            scrollContainer.addView((View)messageTextView4, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n20 | 0x30, 24, 0, 24, customViewOffset));
        }
        if (!TextUtils.isEmpty(this.message)) {
            this.messageTextView.setText(this.message);
            this.messageTextView.setVisibility(0);
        }
        else {
            this.messageTextView.setVisibility(8);
        }
        if (this.items != null) {
            int i = 0;
            while (true) {
                final CharSequence[] items = this.items;
                if (i >= items.length) {
                    break;
                }
                if (items[i] != null) {
                    final AlertDialogCell e = new AlertDialogCell(this.getContext());
                    final CharSequence charSequence = this.items[i];
                    final int[] itemIcons = this.itemIcons;
                    int n21;
                    if (itemIcons != null) {
                        n21 = itemIcons[i];
                    }
                    else {
                        n21 = 0;
                    }
                    e.setTextAndIcon(charSequence, n21);
                    e.setTag((Object)i);
                    this.itemViews.add(e);
                    this.scrollContainer.addView((View)e, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                    e.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertDialog$iC26x2guh9hO2NrF8CCJRy6v4_w(this));
                }
                ++i;
            }
        }
        final View customView = this.customView;
        if (customView != null) {
            if (customView.getParent() != null) {
                ((ViewGroup)this.customView.getParent()).removeView(this.customView);
            }
            this.scrollContainer.addView(this.customView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        if (b) {
            (this.buttonsLayout = new FrameLayout(this.getContext()) {
                protected void onLayout(final boolean b, int i, int paddingLeft, int n, int childCount) {
                    childCount = this.getChildCount();
                    final int n2 = n - i;
                    View view = null;
                    View child;
                    Integer n3;
                    View view2;
                    int measuredWidth;
                    int measuredHeight;
                    for (i = 0; i < childCount; ++i, view = view2) {
                        child = this.getChildAt(i);
                        n3 = (Integer)child.getTag();
                        if (n3 != null) {
                            if (n3 == -1) {
                                if (LocaleController.isRTL) {
                                    child.layout(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingLeft() + child.getMeasuredWidth(), this.getPaddingTop() + child.getMeasuredHeight());
                                }
                                else {
                                    child.layout(n2 - this.getPaddingRight() - child.getMeasuredWidth(), this.getPaddingTop(), n2 - this.getPaddingRight(), this.getPaddingTop() + child.getMeasuredHeight());
                                }
                                view2 = child;
                            }
                            else if (n3 == -2) {
                                if (LocaleController.isRTL) {
                                    n = (paddingLeft = this.getPaddingLeft());
                                    if (view != null) {
                                        paddingLeft = n + (view.getMeasuredWidth() + AndroidUtilities.dp(8.0f));
                                    }
                                    child.layout(paddingLeft, this.getPaddingTop(), child.getMeasuredWidth() + paddingLeft, this.getPaddingTop() + child.getMeasuredHeight());
                                    view2 = view;
                                }
                                else {
                                    n = (paddingLeft = n2 - this.getPaddingRight() - child.getMeasuredWidth());
                                    if (view != null) {
                                        paddingLeft = n - (view.getMeasuredWidth() + AndroidUtilities.dp(8.0f));
                                    }
                                    child.layout(paddingLeft, this.getPaddingTop(), child.getMeasuredWidth() + paddingLeft, this.getPaddingTop() + child.getMeasuredHeight());
                                    view2 = view;
                                }
                            }
                            else {
                                view2 = view;
                                if (n3 == -3) {
                                    if (LocaleController.isRTL) {
                                        child.layout(n2 - this.getPaddingRight() - child.getMeasuredWidth(), this.getPaddingTop(), n2 - this.getPaddingRight(), this.getPaddingTop() + child.getMeasuredHeight());
                                        view2 = view;
                                    }
                                    else {
                                        child.layout(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingLeft() + child.getMeasuredWidth(), this.getPaddingTop() + child.getMeasuredHeight());
                                        view2 = view;
                                    }
                                }
                            }
                        }
                        else {
                            measuredWidth = child.getMeasuredWidth();
                            measuredHeight = child.getMeasuredHeight();
                            if (view != null) {
                                paddingLeft = view.getLeft() + (view.getMeasuredWidth() - measuredWidth) / 2;
                                n = view.getTop() + (view.getMeasuredHeight() - measuredHeight) / 2;
                            }
                            else {
                                paddingLeft = 0;
                                n = 0;
                            }
                            child.layout(paddingLeft, n, measuredWidth + paddingLeft, measuredHeight + n);
                            view2 = view;
                        }
                    }
                }
                
                protected void onMeasure(int i, int n) {
                    super.onMeasure(i, n);
                    final int n2 = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
                    final int childCount = this.getChildCount();
                    i = 0;
                    int n3 = 0;
                    while (i < childCount) {
                        final View child = this.getChildAt(i);
                        n = n3;
                        if (child instanceof TextView) {
                            n = n3;
                            if (child.getTag() != null) {
                                n = n3 + child.getMeasuredWidth();
                            }
                        }
                        ++i;
                        n3 = n;
                    }
                    if (n3 > n2) {
                        final View viewWithTag = this.findViewWithTag((Object)(-2));
                        final View viewWithTag2 = this.findViewWithTag((Object)(-3));
                        if (viewWithTag != null && viewWithTag2 != null) {
                            if (viewWithTag.getMeasuredWidth() < viewWithTag2.getMeasuredWidth()) {
                                viewWithTag2.measure(View$MeasureSpec.makeMeasureSpec(viewWithTag2.getMeasuredWidth() - (n3 - n2), 1073741824), View$MeasureSpec.makeMeasureSpec(viewWithTag2.getMeasuredHeight(), 1073741824));
                            }
                            else {
                                viewWithTag.measure(View$MeasureSpec.makeMeasureSpec(viewWithTag.getMeasuredWidth() - (n3 - n2), 1073741824), View$MeasureSpec.makeMeasureSpec(viewWithTag.getMeasuredHeight(), 1073741824));
                            }
                        }
                    }
                }
            }).setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            contentView.addView((View)this.buttonsLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 52));
            if (this.positiveButtonText != null) {
                final TextView textView = new TextView(this.getContext()) {
                    public void setEnabled(final boolean enabled) {
                        super.setEnabled(enabled);
                        float alpha;
                        if (enabled) {
                            alpha = 1.0f;
                        }
                        else {
                            alpha = 0.5f;
                        }
                        this.setAlpha(alpha);
                    }
                    
                    public void setTextColor(final int textColor) {
                        super.setTextColor(textColor);
                        this.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(textColor));
                    }
                };
                textView.setMinWidth(AndroidUtilities.dp(64.0f));
                textView.setTag((Object)(-1));
                textView.setTextSize(1, 14.0f);
                textView.setTextColor(this.getThemeColor("dialogButton"));
                textView.setGravity(17);
                textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                textView.setText((CharSequence)this.positiveButtonText.toString().toUpperCase());
                textView.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(this.getThemeColor("dialogButton")));
                textView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                this.buttonsLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 36, 53));
                textView.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertDialog$rp49coWDdM6PFZnr9_LTptCU2Ag(this));
            }
            if (this.negativeButtonText != null) {
                final TextView textView2 = new TextView(this.getContext()) {
                    public void setEnabled(final boolean enabled) {
                        super.setEnabled(enabled);
                        float alpha;
                        if (enabled) {
                            alpha = 1.0f;
                        }
                        else {
                            alpha = 0.5f;
                        }
                        this.setAlpha(alpha);
                    }
                    
                    public void setTextColor(final int textColor) {
                        super.setTextColor(textColor);
                        this.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(textColor));
                    }
                };
                textView2.setMinWidth(AndroidUtilities.dp(64.0f));
                textView2.setTag((Object)(-2));
                textView2.setTextSize(1, 14.0f);
                textView2.setTextColor(this.getThemeColor("dialogButton"));
                textView2.setGravity(17);
                textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                textView2.setEllipsize(TextUtils$TruncateAt.END);
                textView2.setSingleLine(true);
                textView2.setText((CharSequence)this.negativeButtonText.toString().toUpperCase());
                textView2.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(this.getThemeColor("dialogButton")));
                textView2.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                this.buttonsLayout.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 36, 53));
                textView2.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertDialog$35svlhUpH_M074FLkhJN8iyIwmw(this));
            }
            if (this.neutralButtonText != null) {
                final TextView textView3 = new TextView(this.getContext()) {
                    public void setEnabled(final boolean enabled) {
                        super.setEnabled(enabled);
                        float alpha;
                        if (enabled) {
                            alpha = 1.0f;
                        }
                        else {
                            alpha = 0.5f;
                        }
                        this.setAlpha(alpha);
                    }
                    
                    public void setTextColor(final int textColor) {
                        super.setTextColor(textColor);
                        this.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(textColor));
                    }
                };
                textView3.setMinWidth(AndroidUtilities.dp(64.0f));
                textView3.setTag((Object)(-3));
                textView3.setTextSize(1, 14.0f);
                textView3.setTextColor(this.getThemeColor("dialogButton"));
                textView3.setGravity(17);
                textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                textView3.setEllipsize(TextUtils$TruncateAt.END);
                textView3.setSingleLine(true);
                textView3.setText((CharSequence)this.neutralButtonText.toString().toUpperCase());
                textView3.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(this.getThemeColor("dialogButton")));
                textView3.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                this.buttonsLayout.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 36, 51));
                textView3.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertDialog$hCRmQxFHC_EIDauULvRdmfnSEuE(this));
            }
        }
        final Window window = this.getWindow();
        final WindowManager$LayoutParams attributes = new WindowManager$LayoutParams();
        attributes.copyFrom(window.getAttributes());
        if (this.progressViewStyle == 3) {
            attributes.width = -1;
        }
        else {
            attributes.dimAmount = 0.6f;
            attributes.flags |= 0x2;
            final int x = AndroidUtilities.displaySize.x;
            this.lastScreenWidth = x;
            final int dp = AndroidUtilities.dp(48.0f);
            int a;
            if (AndroidUtilities.isTablet()) {
                if (AndroidUtilities.isSmallTablet()) {
                    a = AndroidUtilities.dp(446.0f);
                }
                else {
                    a = AndroidUtilities.dp(496.0f);
                }
            }
            else {
                a = AndroidUtilities.dp(356.0f);
            }
            final int min = Math.min(a, x - dp);
            final Rect backgroundPaddings = this.backgroundPaddings;
            attributes.width = min + backgroundPaddings.left + backgroundPaddings.right;
        }
        final View customView2 = this.customView;
        if (customView2 != null && this.canTextInput(customView2)) {
            attributes.softInputMode = 4;
        }
        else {
            attributes.flags |= 0x20000;
        }
        if (Build$VERSION.SDK_INT >= 28) {
            attributes.layoutInDisplayCutoutMode = 0;
        }
        window.setAttributes(attributes);
    }
    
    public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
        final ScrollView contentScrollView = this.contentScrollView;
        if (contentScrollView != null) {
            contentScrollView.postDelayed(runnable, n);
        }
    }
    
    public void setButton(final int n, final CharSequence neutralButtonText, final DialogInterface$OnClickListener neutralButtonListener) {
        if (n != -3) {
            if (n != -2) {
                if (n == -1) {
                    this.positiveButtonText = neutralButtonText;
                    this.positiveButtonListener = neutralButtonListener;
                }
            }
            else {
                this.negativeButtonText = neutralButtonText;
                this.negativeButtonListener = neutralButtonListener;
            }
        }
        else {
            this.neutralButtonText = neutralButtonText;
            this.neutralButtonListener = neutralButtonListener;
        }
    }
    
    public void setCanCacnel(final boolean canCacnel) {
        this.canCacnel = canCacnel;
    }
    
    public void setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
        super.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }
    
    public void setDismissDialogByButtons(final boolean dismissDialogByButtons) {
        this.dismissDialogByButtons = dismissDialogByButtons;
    }
    
    public void setItemColor(final int index, final int textColor, final int n) {
        if (index >= 0) {
            if (index < this.itemViews.size()) {
                final AlertDialogCell alertDialogCell = this.itemViews.get(index);
                alertDialogCell.textView.setTextColor(textColor);
                alertDialogCell.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
            }
        }
    }
    
    public void setMessage(final CharSequence message) {
        this.message = message;
        if (this.messageTextView != null) {
            if (!TextUtils.isEmpty(this.message)) {
                this.messageTextView.setText(this.message);
                this.messageTextView.setVisibility(0);
            }
            else {
                this.messageTextView.setVisibility(8);
            }
        }
    }
    
    public void setMessageTextViewClickable(final boolean messageTextViewClickable) {
        this.messageTextViewClickable = messageTextViewClickable;
    }
    
    public void setNegativeButton(final CharSequence negativeButtonText, final DialogInterface$OnClickListener negativeButtonListener) {
        this.negativeButtonText = negativeButtonText;
        this.negativeButtonListener = negativeButtonListener;
    }
    
    public void setNeutralButton(final CharSequence neutralButtonText, final DialogInterface$OnClickListener neutralButtonListener) {
        this.neutralButtonText = neutralButtonText;
        this.neutralButtonListener = neutralButtonListener;
    }
    
    public void setOnCancelListener(final DialogInterface$OnCancelListener onCancelListener) {
        super.setOnCancelListener(this.onCancelListener = onCancelListener);
    }
    
    public void setPositiveButton(final CharSequence positiveButtonText, final DialogInterface$OnClickListener positiveButtonListener) {
        this.positiveButtonText = positiveButtonText;
        this.positiveButtonListener = positiveButtonListener;
    }
    
    public void setPositiveButtonListener(final DialogInterface$OnClickListener positiveButtonListener) {
        this.positiveButtonListener = positiveButtonListener;
    }
    
    public void setProgress(final int currentProgress) {
        this.currentProgress = currentProgress;
        final LineProgressView lineProgressView = this.lineProgressView;
        if (lineProgressView != null) {
            lineProgressView.setProgress(currentProgress / 100.0f, true);
            this.updateLineProgressTextView();
        }
    }
    
    public void setProgressStyle(final int progressViewStyle) {
        this.progressViewStyle = progressViewStyle;
    }
    
    public void setSecondTitle(final CharSequence secondTitle) {
        this.secondTitle = secondTitle;
    }
    
    public void setTitle(final CharSequence charSequence) {
        this.title = charSequence;
        final TextView titleTextView = this.titleTextView;
        if (titleTextView != null) {
            titleTextView.setText(charSequence);
        }
    }
    
    public void setTopHeight(final int topHeight) {
        this.topHeight = topHeight;
    }
    
    public void setTopImage(final int topResId, final int topBackgroundColor) {
        this.topResId = topResId;
        this.topBackgroundColor = topBackgroundColor;
    }
    
    public void setTopImage(final Drawable topDrawable, final int topBackgroundColor) {
        this.topDrawable = topDrawable;
        this.topBackgroundColor = topBackgroundColor;
    }
    
    public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
        final ScrollView contentScrollView = this.contentScrollView;
        if (contentScrollView != null) {
            contentScrollView.removeCallbacks(runnable);
        }
    }
    
    public static class AlertDialogCell extends FrameLayout
    {
        private ImageView imageView;
        private TextView textView;
        
        public AlertDialogCell(final Context context) {
            super(context);
            this.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
            this.setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
            (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogIcon"), PorterDuff$Mode.MULTIPLY));
            final ImageView imageView = this.imageView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int n2;
            if (isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 40, n2 | 0x10));
            (this.textView = new TextView(context)).setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils$TruncateAt.END);
            this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
            this.textView.setTextSize(1, 16.0f);
            final TextView textView = this.textView;
            int n3;
            if (LocaleController.isRTL) {
                n3 = n;
            }
            else {
                n3 = 3;
            }
            this.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, n3 | 0x10));
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }
        
        public void setGravity(final int gravity) {
            this.textView.setGravity(gravity);
        }
        
        public void setTextAndIcon(final CharSequence text, int dp) {
            this.textView.setText(text);
            if (dp != 0) {
                this.imageView.setImageResource(dp);
                this.imageView.setVisibility(0);
                final TextView textView = this.textView;
                if (LocaleController.isRTL) {
                    dp = 0;
                }
                else {
                    dp = AndroidUtilities.dp(56.0f);
                }
                int dp2;
                if (LocaleController.isRTL) {
                    dp2 = AndroidUtilities.dp(56.0f);
                }
                else {
                    dp2 = 0;
                }
                textView.setPadding(dp, 0, dp2, 0);
            }
            else {
                this.imageView.setVisibility(4);
                this.textView.setPadding(0, 0, 0, 0);
            }
        }
        
        public void setTextColor(final int textColor) {
            this.textView.setTextColor(textColor);
        }
    }
    
    public static class Builder
    {
        private AlertDialog alertDialog;
        
        public Builder(final Context context) {
            this.alertDialog = new AlertDialog(context, 0);
        }
        
        public Builder(final Context context, final int n) {
            this.alertDialog = new AlertDialog(context, n);
        }
        
        protected Builder(final AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
        }
        
        public AlertDialog create() {
            return this.alertDialog;
        }
        
        public Context getContext() {
            return this.alertDialog.getContext();
        }
        
        public Runnable getDismissRunnable() {
            return this.alertDialog.dismissRunnable;
        }
        
        public Builder setCustomViewOffset(final int n) {
            this.alertDialog.customViewOffset = n;
            return this;
        }
        
        public Builder setItems(final CharSequence[] array, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
            this.alertDialog.items = array;
            this.alertDialog.onClickListener = dialogInterface$OnClickListener;
            return this;
        }
        
        public Builder setItems(final CharSequence[] array, final int[] array2, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
            this.alertDialog.items = array;
            this.alertDialog.itemIcons = array2;
            this.alertDialog.onClickListener = dialogInterface$OnClickListener;
            return this;
        }
        
        public Builder setMessage(final CharSequence charSequence) {
            this.alertDialog.message = charSequence;
            return this;
        }
        
        public Builder setMessageTextViewClickable(final boolean b) {
            this.alertDialog.messageTextViewClickable = b;
            return this;
        }
        
        public Builder setNegativeButton(final CharSequence charSequence, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
            this.alertDialog.negativeButtonText = charSequence;
            this.alertDialog.negativeButtonListener = dialogInterface$OnClickListener;
            return this;
        }
        
        public Builder setNeutralButton(final CharSequence charSequence, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
            this.alertDialog.neutralButtonText = charSequence;
            this.alertDialog.neutralButtonListener = dialogInterface$OnClickListener;
            return this;
        }
        
        public Builder setOnBackButtonListener(final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
            this.alertDialog.onBackButtonListener = dialogInterface$OnClickListener;
            return this;
        }
        
        public Builder setOnCancelListener(final DialogInterface$OnCancelListener onCancelListener) {
            this.alertDialog.setOnCancelListener(onCancelListener);
            return this;
        }
        
        public Builder setOnDismissListener(final DialogInterface$OnDismissListener onDismissListener) {
            this.alertDialog.setOnDismissListener(onDismissListener);
            return this;
        }
        
        public Builder setPositiveButton(final CharSequence charSequence, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
            this.alertDialog.positiveButtonText = charSequence;
            this.alertDialog.positiveButtonListener = dialogInterface$OnClickListener;
            return this;
        }
        
        public Builder setSubtitle(final CharSequence charSequence) {
            this.alertDialog.subtitle = charSequence;
            return this;
        }
        
        public Builder setTitle(final CharSequence charSequence) {
            this.alertDialog.title = charSequence;
            return this;
        }
        
        public Builder setTopImage(final int n, final int n2) {
            this.alertDialog.topResId = n;
            this.alertDialog.topBackgroundColor = n2;
            return this;
        }
        
        public Builder setTopImage(final Drawable drawable, final int n) {
            this.alertDialog.topDrawable = drawable;
            this.alertDialog.topBackgroundColor = n;
            return this;
        }
        
        public Builder setView(final View view) {
            this.alertDialog.customView = view;
            return this;
        }
        
        public AlertDialog show() {
            this.alertDialog.show();
            return this.alertDialog;
        }
    }
}
