// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.view.View$MeasureSpec;
import org.telegram.messenger.BuildVars;
import android.view.MotionEvent;
import android.text.TextUtils;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import java.util.Collection;
import android.animation.ObjectAnimator;
import java.util.ArrayList;
import android.text.TextPaint;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.widget.FrameLayout$LayoutParams;
import android.graphics.Point;
import org.telegram.messenger.LocaleController;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.view.View$OnClickListener;
import android.os.Build$VERSION;
import android.content.Context;
import org.telegram.ui.Components.SnowflakesEffect;
import android.graphics.Rect;
import android.graphics.Paint$FontMetricsInt;
import org.telegram.ui.Components.FireworksEffect;
import android.widget.ImageView;
import android.view.View;
import android.animation.AnimatorSet;
import android.widget.FrameLayout;

public class ActionBar extends FrameLayout
{
    public ActionBarMenuOnItemClick actionBarMenuOnItemClick;
    private ActionBarMenu actionMode;
    private AnimatorSet actionModeAnimation;
    private View actionModeExtraView;
    private View[] actionModeHidingViews;
    private View actionModeShowingView;
    private View actionModeTop;
    private View actionModeTranslationView;
    private boolean actionModeVisible;
    private boolean addToContainer;
    private boolean allowOverlayTitle;
    private ImageView backButtonImageView;
    private boolean castShadows;
    private boolean clipContent;
    private int extraHeight;
    private FireworksEffect fireworksEffect;
    private Paint$FontMetricsInt fontMetricsInt;
    private boolean ignoreLayoutRequest;
    private boolean interceptTouches;
    private boolean isBackOverlayVisible;
    protected boolean isSearchFieldVisible;
    protected int itemsActionModeBackgroundColor;
    protected int itemsActionModeColor;
    protected int itemsBackgroundColor;
    protected int itemsColor;
    private Runnable lastRunnable;
    private CharSequence lastTitle;
    private boolean manualStart;
    private ActionBarMenu menu;
    private boolean occupyStatusBar;
    protected BaseFragment parentFragment;
    private Rect rect;
    private SnowflakesEffect snowflakesEffect;
    private SimpleTextView subtitleTextView;
    private boolean supportsHolidayImage;
    private Runnable titleActionRunnable;
    private boolean titleOverlayShown;
    private int titleRightMargin;
    private SimpleTextView titleTextView;
    
    public ActionBar(final Context context) {
        super(context);
        this.occupyStatusBar = (Build$VERSION.SDK_INT >= 21);
        this.addToContainer = true;
        this.interceptTouches = true;
        this.castShadows = true;
        this.setOnClickListener((View$OnClickListener)new _$$Lambda$ActionBar$ipLNSE_u7HuyPKHoD94Vr_WlQ_U(this));
    }
    
    private void createBackButtonImage() {
        if (this.backButtonImageView != null) {
            return;
        }
        (this.backButtonImageView = new ImageView(this.getContext())).setScaleType(ImageView$ScaleType.CENTER);
        this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsBackgroundColor));
        final int itemsColor = this.itemsColor;
        if (itemsColor != 0) {
            this.backButtonImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(itemsColor, PorterDuff$Mode.MULTIPLY));
        }
        this.backButtonImageView.setPadding(AndroidUtilities.dp(1.0f), 0, 0, 0);
        this.addView((View)this.backButtonImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(54, 54, 51));
        this.backButtonImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$ActionBar$VS8eczH_GWXmmp1KP0J3EUbIWcg(this));
        this.backButtonImageView.setContentDescription((CharSequence)LocaleController.getString("AccDescrGoBack", 2131558435));
    }
    
    private void createSubtitleTextView() {
        if (this.subtitleTextView != null) {
            return;
        }
        (this.subtitleTextView = new SimpleTextView(this.getContext())).setGravity(3);
        this.subtitleTextView.setVisibility(8);
        this.subtitleTextView.setTextColor(Theme.getColor("actionBarDefaultSubtitle"));
        this.addView((View)this.subtitleTextView, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 51));
    }
    
    private void createTitleTextView() {
        if (this.titleTextView != null) {
            return;
        }
        (this.titleTextView = new SimpleTextView(this.getContext())).setGravity(3);
        this.titleTextView.setTextColor(Theme.getColor("actionBarDefaultTitle"));
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.addView((View)this.titleTextView, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 51));
    }
    
    public static int getCurrentActionBarHeight() {
        if (AndroidUtilities.isTablet()) {
            return AndroidUtilities.dp(64.0f);
        }
        final Point displaySize = AndroidUtilities.displaySize;
        if (displaySize.x > displaySize.y) {
            return AndroidUtilities.dp(48.0f);
        }
        return AndroidUtilities.dp(56.0f);
    }
    
    public void closeSearchField() {
        this.closeSearchField(true);
    }
    
    public void closeSearchField(final boolean b) {
        if (this.isSearchFieldVisible) {
            final ActionBarMenu menu = this.menu;
            if (menu != null) {
                menu.closeSearchField(b);
            }
        }
    }
    
    public ActionBarMenu createActionMode() {
        return this.createActionMode(true);
    }
    
    public ActionBarMenu createActionMode(final boolean b) {
        final ActionBarMenu actionMode = this.actionMode;
        if (actionMode != null) {
            return actionMode;
        }
        this.actionMode = new ActionBarMenu(this.getContext(), this);
        final ActionBarMenu actionMode2 = this.actionMode;
        actionMode2.isActionMode = true;
        actionMode2.setBackgroundColor(Theme.getColor("actionBarActionModeDefault"));
        this.addView((View)this.actionMode, this.indexOfChild((View)this.backButtonImageView));
        final ActionBarMenu actionMode3 = this.actionMode;
        int statusBarHeight;
        if (this.occupyStatusBar) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        actionMode3.setPadding(0, statusBarHeight, 0, 0);
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.actionMode.getLayoutParams();
        layoutParams.height = -1;
        layoutParams.width = -1;
        layoutParams.bottomMargin = this.extraHeight;
        layoutParams.gravity = 5;
        this.actionMode.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.actionMode.setVisibility(4);
        if (this.occupyStatusBar && b && this.actionModeTop == null) {
            (this.actionModeTop = new View(this.getContext())).setBackgroundColor(Theme.getColor("actionBarActionModeDefaultTop"));
            this.addView(this.actionModeTop);
            final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.actionModeTop.getLayoutParams();
            layoutParams2.height = AndroidUtilities.statusBarHeight;
            layoutParams2.width = -1;
            layoutParams2.gravity = 51;
            this.actionModeTop.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            this.actionModeTop.setVisibility(4);
        }
        return this.actionMode;
    }
    
    public ActionBarMenu createMenu() {
        final ActionBarMenu menu = this.menu;
        if (menu != null) {
            return menu;
        }
        this.addView((View)(this.menu = new ActionBarMenu(this.getContext(), this)), 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 5));
        return this.menu;
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        final boolean b = this.clipContent && (view == this.titleTextView || view == this.subtitleTextView || view == this.actionMode || view == this.menu || view == this.backButtonImageView);
        if (b) {
            canvas.save();
            final float n2 = -this.getTranslationY();
            int statusBarHeight;
            if (this.occupyStatusBar) {
                statusBarHeight = AndroidUtilities.statusBarHeight;
            }
            else {
                statusBarHeight = 0;
            }
            canvas.clipRect(0.0f, n2 + statusBarHeight, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
        }
        final boolean drawChild = super.drawChild(canvas, view, n);
        if (this.supportsHolidayImage && !this.titleOverlayShown && !LocaleController.isRTL && view == this.titleTextView) {
            final Drawable currentHolidayDrawable = Theme.getCurrentHolidayDrawable();
            if (currentHolidayDrawable != null) {
                final TextPaint textPaint = this.titleTextView.getTextPaint();
                textPaint.getFontMetricsInt(this.fontMetricsInt);
                textPaint.getTextBounds((String)this.titleTextView.getText(), 0, 1, this.rect);
                final int n3 = this.titleTextView.getTextStartX() + Theme.getCurrentHolidayDrawableXOffset() + (this.rect.width() - (currentHolidayDrawable.getIntrinsicWidth() + Theme.getCurrentHolidayDrawableXOffset())) / 2;
                final int n4 = this.titleTextView.getTextStartY() + Theme.getCurrentHolidayDrawableYOffset() + (int)Math.ceil((this.titleTextView.getTextHeight() - this.rect.height()) / 2.0f);
                currentHolidayDrawable.setBounds(n3, n4 - currentHolidayDrawable.getIntrinsicHeight(), currentHolidayDrawable.getIntrinsicWidth() + n3, n4);
                currentHolidayDrawable.draw(canvas);
                if (Theme.canStartHolidayAnimation()) {
                    if (this.snowflakesEffect == null) {
                        this.snowflakesEffect = new SnowflakesEffect();
                    }
                }
                else if (!this.manualStart && this.snowflakesEffect != null) {
                    this.snowflakesEffect = null;
                }
                final SnowflakesEffect snowflakesEffect = this.snowflakesEffect;
                if (snowflakesEffect != null) {
                    snowflakesEffect.onDraw((View)this, canvas);
                }
                else {
                    final FireworksEffect fireworksEffect = this.fireworksEffect;
                    if (fireworksEffect != null) {
                        fireworksEffect.onDraw((View)this, canvas);
                    }
                }
            }
        }
        if (b) {
            canvas.restore();
        }
        return drawChild;
    }
    
    public ActionBarMenuOnItemClick getActionBarMenuOnItemClick() {
        return this.actionBarMenuOnItemClick;
    }
    
    public boolean getAddToContainer() {
        return this.addToContainer;
    }
    
    public View getBackButton() {
        return (View)this.backButtonImageView;
    }
    
    public boolean getCastShadows() {
        return this.castShadows;
    }
    
    public boolean getOccupyStatusBar() {
        return this.occupyStatusBar;
    }
    
    public String getSubtitle() {
        final SimpleTextView subtitleTextView = this.subtitleTextView;
        if (subtitleTextView == null) {
            return null;
        }
        return subtitleTextView.getText().toString();
    }
    
    public SimpleTextView getSubtitleTextView() {
        return this.subtitleTextView;
    }
    
    public String getTitle() {
        final SimpleTextView titleTextView = this.titleTextView;
        if (titleTextView == null) {
            return null;
        }
        return titleTextView.getText().toString();
    }
    
    public SimpleTextView getTitleTextView() {
        return this.titleTextView;
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    public void hideActionMode() {
        final ActionBarMenu actionMode = this.actionMode;
        if (actionMode != null) {
            if (this.actionModeVisible) {
                actionMode.hideAllPopupMenus();
                this.actionModeVisible = false;
                final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                list.add(ObjectAnimator.ofFloat((Object)this.actionMode, View.ALPHA, new float[] { 0.0f }));
                if (this.actionModeHidingViews != null) {
                    int n = 0;
                    while (true) {
                        final View[] actionModeHidingViews = this.actionModeHidingViews;
                        if (n >= actionModeHidingViews.length) {
                            break;
                        }
                        if (actionModeHidingViews != null) {
                            actionModeHidingViews[n].setVisibility(0);
                            list.add(ObjectAnimator.ofFloat((Object)this.actionModeHidingViews[n], View.ALPHA, new float[] { 1.0f }));
                        }
                        ++n;
                    }
                }
                final View actionModeTranslationView = this.actionModeTranslationView;
                if (actionModeTranslationView != null) {
                    list.add(ObjectAnimator.ofFloat((Object)actionModeTranslationView, View.TRANSLATION_Y, new float[] { 0.0f }));
                    this.actionModeTranslationView = null;
                }
                final View actionModeShowingView = this.actionModeShowingView;
                if (actionModeShowingView != null) {
                    list.add(ObjectAnimator.ofFloat((Object)actionModeShowingView, View.ALPHA, new float[] { 0.0f }));
                }
                if (this.occupyStatusBar) {
                    final View actionModeTop = this.actionModeTop;
                    if (actionModeTop != null) {
                        list.add(ObjectAnimator.ofFloat((Object)actionModeTop, View.ALPHA, new float[] { 0.0f }));
                    }
                }
                final AnimatorSet actionModeAnimation = this.actionModeAnimation;
                if (actionModeAnimation != null) {
                    actionModeAnimation.cancel();
                }
                (this.actionModeAnimation = new AnimatorSet()).playTogether((Collection)list);
                this.actionModeAnimation.setDuration(200L);
                this.actionModeAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator obj) {
                        if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(obj)) {
                            ActionBar.this.actionModeAnimation = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator obj) {
                        if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(obj)) {
                            ActionBar.this.actionModeAnimation = null;
                            ActionBar.this.actionMode.setVisibility(4);
                            if (ActionBar.this.occupyStatusBar && ActionBar.this.actionModeTop != null) {
                                ActionBar.this.actionModeTop.setVisibility(4);
                            }
                            if (ActionBar.this.actionModeExtraView != null) {
                                ActionBar.this.actionModeExtraView.setVisibility(4);
                            }
                        }
                    }
                });
                this.actionModeAnimation.start();
                if (!this.isSearchFieldVisible) {
                    final SimpleTextView titleTextView = this.titleTextView;
                    if (titleTextView != null) {
                        titleTextView.setVisibility(0);
                    }
                    final SimpleTextView subtitleTextView = this.subtitleTextView;
                    if (subtitleTextView != null && !TextUtils.isEmpty(subtitleTextView.getText())) {
                        this.subtitleTextView.setVisibility(0);
                    }
                }
                final ActionBarMenu menu = this.menu;
                if (menu != null) {
                    menu.setVisibility(0);
                }
                final ImageView backButtonImageView = this.backButtonImageView;
                if (backButtonImageView != null) {
                    final Drawable drawable = backButtonImageView.getDrawable();
                    if (drawable instanceof BackDrawable) {
                        ((BackDrawable)drawable).setRotation(0.0f, true);
                    }
                    this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsBackgroundColor));
                }
            }
        }
    }
    
    public boolean isActionModeShowed() {
        return this.actionMode != null && this.actionModeVisible;
    }
    
    public boolean isSearchFieldVisible() {
        return this.isSearchFieldVisible;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        if (this.supportsHolidayImage && !this.titleOverlayShown && !LocaleController.isRTL && motionEvent.getAction() == 0) {
            final Drawable currentHolidayDrawable = Theme.getCurrentHolidayDrawable();
            if (currentHolidayDrawable != null && currentHolidayDrawable.getBounds().contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
                this.manualStart = true;
                if (this.snowflakesEffect == null) {
                    this.fireworksEffect = null;
                    this.snowflakesEffect = new SnowflakesEffect();
                    this.titleTextView.invalidate();
                    this.invalidate();
                }
                else if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    this.snowflakesEffect = null;
                    this.fireworksEffect = new FireworksEffect();
                    this.titleTextView.invalidate();
                    this.invalidate();
                }
            }
        }
        return super.onInterceptTouchEvent(motionEvent);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        final boolean occupyStatusBar = this.occupyStatusBar;
        final int n5 = 0;
        int statusBarHeight;
        if (occupyStatusBar) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        final ImageView backButtonImageView = this.backButtonImageView;
        int n7;
        if (backButtonImageView != null && backButtonImageView.getVisibility() != 8) {
            final ImageView backButtonImageView2 = this.backButtonImageView;
            backButtonImageView2.layout(0, statusBarHeight, backButtonImageView2.getMeasuredWidth(), this.backButtonImageView.getMeasuredHeight() + statusBarHeight);
            float n6;
            if (AndroidUtilities.isTablet()) {
                n6 = 80.0f;
            }
            else {
                n6 = 72.0f;
            }
            n7 = AndroidUtilities.dp(n6);
        }
        else {
            float n8;
            if (AndroidUtilities.isTablet()) {
                n8 = 26.0f;
            }
            else {
                n8 = 18.0f;
            }
            n7 = AndroidUtilities.dp(n8);
        }
        final ActionBarMenu menu = this.menu;
        if (menu != null && menu.getVisibility() != 8) {
            int dp;
            if (this.isSearchFieldVisible) {
                float n9;
                if (AndroidUtilities.isTablet()) {
                    n9 = 74.0f;
                }
                else {
                    n9 = 66.0f;
                }
                dp = AndroidUtilities.dp(n9);
            }
            else {
                dp = n3 - n - this.menu.getMeasuredWidth();
            }
            final ActionBarMenu menu2 = this.menu;
            menu2.layout(dp, statusBarHeight, menu2.getMeasuredWidth() + dp, this.menu.getMeasuredHeight() + statusBarHeight);
        }
        final SimpleTextView titleTextView = this.titleTextView;
        if (titleTextView != null && titleTextView.getVisibility() != 8) {
            final SimpleTextView subtitleTextView = this.subtitleTextView;
            int n12;
            if (subtitleTextView != null && subtitleTextView.getVisibility() != 8) {
                final int n10 = (getCurrentActionBarHeight() / 2 - this.titleTextView.getTextHeight()) / 2;
                float n11;
                if (!AndroidUtilities.isTablet() && this.getResources().getConfiguration().orientation == 2) {
                    n11 = 2.0f;
                }
                else {
                    n11 = 3.0f;
                }
                n12 = n10 + AndroidUtilities.dp(n11);
            }
            else {
                n12 = (getCurrentActionBarHeight() - this.titleTextView.getTextHeight()) / 2;
            }
            final SimpleTextView titleTextView2 = this.titleTextView;
            final int n13 = n12 + statusBarHeight;
            titleTextView2.layout(n7, n13, titleTextView2.getMeasuredWidth() + n7, this.titleTextView.getTextHeight() + n13);
        }
        final SimpleTextView subtitleTextView2 = this.subtitleTextView;
        if (subtitleTextView2 != null && subtitleTextView2.getVisibility() != 8) {
            final int n14 = getCurrentActionBarHeight() / 2;
            final int n15 = (getCurrentActionBarHeight() / 2 - this.subtitleTextView.getTextHeight()) / 2;
            if (!AndroidUtilities.isTablet()) {
                final int orientation = this.getResources().getConfiguration().orientation;
            }
            final int dp2 = AndroidUtilities.dp(1.0f);
            final SimpleTextView subtitleTextView3 = this.subtitleTextView;
            final int n16 = statusBarHeight + (n14 + n15 - dp2);
            subtitleTextView3.layout(n7, n16, subtitleTextView3.getMeasuredWidth() + n7, this.subtitleTextView.getTextHeight() + n16);
        }
        for (int childCount = this.getChildCount(), i = n5; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8 && child != this.titleTextView && child != this.subtitleTextView && child != this.menu) {
                if (child != this.backButtonImageView) {
                    final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                    final int measuredWidth = child.getMeasuredWidth();
                    final int measuredHeight = child.getMeasuredHeight();
                    int gravity;
                    if ((gravity = frameLayout$LayoutParams.gravity) == -1) {
                        gravity = 51;
                    }
                    final int n17 = gravity & 0x70;
                    final int n18 = gravity & 0x7 & 0x7;
                    int leftMargin = 0;
                    Label_0711: {
                        int n19;
                        int n20;
                        if (n18 != 1) {
                            if (n18 != 5) {
                                leftMargin = frameLayout$LayoutParams.leftMargin;
                                break Label_0711;
                            }
                            n19 = n3 - measuredWidth;
                            n20 = frameLayout$LayoutParams.rightMargin;
                        }
                        else {
                            n19 = (n3 - n - measuredWidth) / 2 + frameLayout$LayoutParams.leftMargin;
                            n20 = frameLayout$LayoutParams.rightMargin;
                        }
                        leftMargin = n19 - n20;
                    }
                    int n21 = 0;
                    Label_0802: {
                        int n22;
                        int n23;
                        if (n17 != 16) {
                            if (n17 == 48) {
                                n21 = frameLayout$LayoutParams.topMargin;
                                break Label_0802;
                            }
                            if (n17 != 80) {
                                n21 = frameLayout$LayoutParams.topMargin;
                                break Label_0802;
                            }
                            n22 = n4 - n2 - measuredHeight;
                            n23 = frameLayout$LayoutParams.bottomMargin;
                        }
                        else {
                            n22 = (n4 - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                            n23 = frameLayout$LayoutParams.bottomMargin;
                        }
                        n21 = n22 - n23;
                    }
                    child.layout(leftMargin, n21, measuredWidth + leftMargin, measuredHeight + n21);
                }
            }
        }
    }
    
    protected void onMeasure(final int n, int i) {
        final int size = View$MeasureSpec.getSize(n);
        View$MeasureSpec.getSize(i);
        final int currentActionBarHeight = getCurrentActionBarHeight();
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(currentActionBarHeight, 1073741824);
        this.ignoreLayoutRequest = true;
        final View actionModeTop = this.actionModeTop;
        if (actionModeTop != null) {
            ((FrameLayout$LayoutParams)actionModeTop.getLayoutParams()).height = AndroidUtilities.statusBarHeight;
        }
        final ActionBarMenu actionMode = this.actionMode;
        final int n2 = 0;
        if (actionMode != null) {
            if (this.occupyStatusBar) {
                i = AndroidUtilities.statusBarHeight;
            }
            else {
                i = 0;
            }
            actionMode.setPadding(0, i, 0, 0);
        }
        this.ignoreLayoutRequest = false;
        if (this.occupyStatusBar) {
            i = AndroidUtilities.statusBarHeight;
        }
        else {
            i = 0;
        }
        this.setMeasuredDimension(size, currentActionBarHeight + i + this.extraHeight);
        final ImageView backButtonImageView = this.backButtonImageView;
        if (backButtonImageView != null && backButtonImageView.getVisibility() != 8) {
            this.backButtonImageView.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(54.0f), 1073741824), measureSpec);
            float n3;
            if (AndroidUtilities.isTablet()) {
                n3 = 80.0f;
            }
            else {
                n3 = 72.0f;
            }
            i = AndroidUtilities.dp(n3);
        }
        else {
            float n4;
            if (AndroidUtilities.isTablet()) {
                n4 = 26.0f;
            }
            else {
                n4 = 18.0f;
            }
            i = AndroidUtilities.dp(n4);
        }
        final ActionBarMenu menu = this.menu;
        if (menu != null && menu.getVisibility() != 8) {
            int n6;
            if (this.isSearchFieldVisible) {
                float n5;
                if (AndroidUtilities.isTablet()) {
                    n5 = 74.0f;
                }
                else {
                    n5 = 66.0f;
                }
                n6 = View$MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(n5), 1073741824);
            }
            else {
                n6 = View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
            }
            this.menu.measure(n6, measureSpec);
        }
        final SimpleTextView titleTextView = this.titleTextView;
        Label_0704: {
            if (titleTextView == null || titleTextView.getVisibility() == 8) {
                final SimpleTextView subtitleTextView = this.subtitleTextView;
                if (subtitleTextView == null || subtitleTextView.getVisibility() == 8) {
                    break Label_0704;
                }
            }
            final ActionBarMenu menu2 = this.menu;
            int measuredWidth;
            if (menu2 != null) {
                measuredWidth = menu2.getMeasuredWidth();
            }
            else {
                measuredWidth = 0;
            }
            final int n7 = size - measuredWidth - AndroidUtilities.dp(16.0f) - i - this.titleRightMargin;
            final SimpleTextView titleTextView2 = this.titleTextView;
            i = 14;
            int n8 = 18;
            Label_0610: {
                if (titleTextView2 != null && titleTextView2.getVisibility() != 8) {
                    final SimpleTextView subtitleTextView2 = this.subtitleTextView;
                    if (subtitleTextView2 != null && subtitleTextView2.getVisibility() != 8) {
                        final SimpleTextView titleTextView3 = this.titleTextView;
                        if (AndroidUtilities.isTablet()) {
                            n8 = 20;
                        }
                        titleTextView3.setTextSize(n8);
                        final SimpleTextView subtitleTextView3 = this.subtitleTextView;
                        if (AndroidUtilities.isTablet()) {
                            i = 16;
                        }
                        subtitleTextView3.setTextSize(i);
                        break Label_0610;
                    }
                }
                final SimpleTextView titleTextView4 = this.titleTextView;
                if (titleTextView4 != null && titleTextView4.getVisibility() != 8) {
                    final SimpleTextView titleTextView5 = this.titleTextView;
                    if (AndroidUtilities.isTablet() || this.getResources().getConfiguration().orientation != 2) {
                        n8 = 20;
                    }
                    titleTextView5.setTextSize(n8);
                }
                final SimpleTextView subtitleTextView4 = this.subtitleTextView;
                if (subtitleTextView4 != null && subtitleTextView4.getVisibility() != 8) {
                    final SimpleTextView subtitleTextView5 = this.subtitleTextView;
                    if (AndroidUtilities.isTablet() || this.getResources().getConfiguration().orientation != 2) {
                        i = 16;
                    }
                    subtitleTextView5.setTextSize(i);
                }
            }
            final SimpleTextView titleTextView6 = this.titleTextView;
            if (titleTextView6 != null && titleTextView6.getVisibility() != 8) {
                this.titleTextView.measure(View$MeasureSpec.makeMeasureSpec(n7, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), Integer.MIN_VALUE));
            }
            final SimpleTextView subtitleTextView6 = this.subtitleTextView;
            if (subtitleTextView6 != null && subtitleTextView6.getVisibility() != 8) {
                this.subtitleTextView.measure(View$MeasureSpec.makeMeasureSpec(n7, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), Integer.MIN_VALUE));
            }
        }
        int childCount;
        View child;
        for (childCount = this.getChildCount(), i = n2; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8 && child != this.titleTextView && child != this.subtitleTextView && child != this.menu) {
                if (child != this.backButtonImageView) {
                    this.measureChildWithMargins(child, n, 0, View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824), 0);
                }
            }
        }
    }
    
    public void onMenuButtonPressed() {
        if (this.isActionModeShowed()) {
            return;
        }
        final ActionBarMenu menu = this.menu;
        if (menu != null) {
            menu.onMenuButtonPressed();
        }
    }
    
    protected void onPause() {
        final ActionBarMenu menu = this.menu;
        if (menu != null) {
            menu.hideAllPopupMenus();
        }
    }
    
    protected void onSearchFieldVisibilityChanged(final boolean isSearchFieldVisible) {
        this.isSearchFieldVisible = isSearchFieldVisible;
        final SimpleTextView titleTextView = this.titleTextView;
        final int n = 4;
        if (titleTextView != null) {
            int visibility;
            if (isSearchFieldVisible) {
                visibility = 4;
            }
            else {
                visibility = 0;
            }
            titleTextView.setVisibility(visibility);
        }
        final SimpleTextView subtitleTextView = this.subtitleTextView;
        if (subtitleTextView != null && !TextUtils.isEmpty(subtitleTextView.getText())) {
            final SimpleTextView subtitleTextView2 = this.subtitleTextView;
            int visibility2;
            if (isSearchFieldVisible) {
                visibility2 = n;
            }
            else {
                visibility2 = 0;
            }
            subtitleTextView2.setVisibility(visibility2);
        }
        final Drawable drawable = this.backButtonImageView.getDrawable();
        if (drawable instanceof MenuDrawable) {
            final MenuDrawable menuDrawable = (MenuDrawable)drawable;
            menuDrawable.setRotateToBack(true);
            float n2;
            if (isSearchFieldVisible) {
                n2 = 1.0f;
            }
            else {
                n2 = 0.0f;
            }
            menuDrawable.setRotation(n2, true);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent) || this.interceptTouches;
    }
    
    public void openSearchField(final String s, final boolean b) {
        final ActionBarMenu menu = this.menu;
        if (menu != null) {
            if (s != null) {
                menu.openSearchField(this.isSearchFieldVisible ^ true, s, b);
            }
        }
    }
    
    public void requestLayout() {
        if (this.ignoreLayoutRequest) {
            return;
        }
        super.requestLayout();
    }
    
    public void setActionBarMenuOnItemClick(final ActionBarMenuOnItemClick actionBarMenuOnItemClick) {
        this.actionBarMenuOnItemClick = actionBarMenuOnItemClick;
    }
    
    public void setActionModeColor(final int backgroundColor) {
        final ActionBarMenu actionMode = this.actionMode;
        if (actionMode != null) {
            actionMode.setBackgroundColor(backgroundColor);
        }
    }
    
    public void setActionModeTopColor(final int backgroundColor) {
        final View actionModeTop = this.actionModeTop;
        if (actionModeTop != null) {
            actionModeTop.setBackgroundColor(backgroundColor);
        }
    }
    
    public void setAddToContainer(final boolean addToContainer) {
        this.addToContainer = addToContainer;
    }
    
    public void setAllowOverlayTitle(final boolean allowOverlayTitle) {
        this.allowOverlayTitle = allowOverlayTitle;
    }
    
    public void setBackButtonContentDescription(final CharSequence contentDescription) {
        final ImageView backButtonImageView = this.backButtonImageView;
        if (backButtonImageView != null) {
            backButtonImageView.setContentDescription(contentDescription);
        }
    }
    
    public void setBackButtonDrawable(final Drawable imageDrawable) {
        if (this.backButtonImageView == null) {
            this.createBackButtonImage();
        }
        final ImageView backButtonImageView = this.backButtonImageView;
        int visibility;
        if (imageDrawable == null) {
            visibility = 8;
        }
        else {
            visibility = 0;
        }
        backButtonImageView.setVisibility(visibility);
        this.backButtonImageView.setImageDrawable(imageDrawable);
        if (imageDrawable instanceof BackDrawable) {
            final BackDrawable backDrawable = (BackDrawable)imageDrawable;
            float n;
            if (this.isActionModeShowed()) {
                n = 1.0f;
            }
            else {
                n = 0.0f;
            }
            backDrawable.setRotation(n, false);
            backDrawable.setRotatedColor(this.itemsActionModeColor);
            backDrawable.setColor(this.itemsColor);
        }
    }
    
    public void setBackButtonImage(final int imageResource) {
        if (this.backButtonImageView == null) {
            this.createBackButtonImage();
        }
        final ImageView backButtonImageView = this.backButtonImageView;
        int visibility;
        if (imageResource == 0) {
            visibility = 8;
        }
        else {
            visibility = 0;
        }
        backButtonImageView.setVisibility(visibility);
        this.backButtonImageView.setImageResource(imageResource);
    }
    
    public void setCastShadows(final boolean castShadows) {
        this.castShadows = castShadows;
    }
    
    public void setClipContent(final boolean clipContent) {
        this.clipContent = clipContent;
    }
    
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        final ImageView backButtonImageView = this.backButtonImageView;
        if (backButtonImageView != null) {
            backButtonImageView.setEnabled(b);
        }
        final ActionBarMenu menu = this.menu;
        if (menu != null) {
            menu.setEnabled(b);
        }
        final ActionBarMenu actionMode = this.actionMode;
        if (actionMode != null) {
            actionMode.setEnabled(b);
        }
    }
    
    public void setExtraHeight(final int extraHeight) {
        this.extraHeight = extraHeight;
        final ActionBarMenu actionMode = this.actionMode;
        if (actionMode != null) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)actionMode.getLayoutParams();
            layoutParams.bottomMargin = this.extraHeight;
            this.actionMode.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
    }
    
    public void setInterceptTouches(final boolean interceptTouches) {
        this.interceptTouches = interceptTouches;
    }
    
    public void setItemsBackgroundColor(final int n, final boolean b) {
        if (b) {
            this.itemsActionModeBackgroundColor = n;
            if (this.actionModeVisible) {
                final ImageView backButtonImageView = this.backButtonImageView;
                if (backButtonImageView != null) {
                    backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsActionModeBackgroundColor));
                }
            }
            final ActionBarMenu actionMode = this.actionMode;
            if (actionMode != null) {
                actionMode.updateItemsBackgroundColor();
            }
        }
        else {
            this.itemsBackgroundColor = n;
            final ImageView backButtonImageView2 = this.backButtonImageView;
            if (backButtonImageView2 != null) {
                backButtonImageView2.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsBackgroundColor));
            }
            final ActionBarMenu menu = this.menu;
            if (menu != null) {
                menu.updateItemsBackgroundColor();
            }
        }
    }
    
    public void setItemsColor(final int n, final boolean b) {
        if (b) {
            this.itemsActionModeColor = n;
            final ActionBarMenu actionMode = this.actionMode;
            if (actionMode != null) {
                actionMode.updateItemsColor();
            }
            final ImageView backButtonImageView = this.backButtonImageView;
            if (backButtonImageView != null) {
                final Drawable drawable = backButtonImageView.getDrawable();
                if (drawable instanceof BackDrawable) {
                    ((BackDrawable)drawable).setRotatedColor(n);
                }
            }
        }
        else {
            this.itemsColor = n;
            final ImageView backButtonImageView2 = this.backButtonImageView;
            if (backButtonImageView2 != null) {
                final int itemsColor = this.itemsColor;
                if (itemsColor != 0) {
                    backButtonImageView2.setColorFilter((ColorFilter)new PorterDuffColorFilter(itemsColor, PorterDuff$Mode.MULTIPLY));
                    final Drawable drawable2 = this.backButtonImageView.getDrawable();
                    if (drawable2 instanceof BackDrawable) {
                        ((BackDrawable)drawable2).setColor(n);
                    }
                }
            }
            final ActionBarMenu menu = this.menu;
            if (menu != null) {
                menu.updateItemsColor();
            }
        }
    }
    
    public void setOccupyStatusBar(final boolean occupyStatusBar) {
        this.occupyStatusBar = occupyStatusBar;
        final ActionBarMenu actionMode = this.actionMode;
        if (actionMode != null) {
            int statusBarHeight;
            if (this.occupyStatusBar) {
                statusBarHeight = AndroidUtilities.statusBarHeight;
            }
            else {
                statusBarHeight = 0;
            }
            actionMode.setPadding(0, statusBarHeight, 0, 0);
        }
    }
    
    public void setPopupBackgroundColor(final int n) {
        final ActionBarMenu menu = this.menu;
        if (menu != null) {
            menu.redrawPopup(n);
        }
    }
    
    public void setPopupItemsColor(final int n, final boolean b) {
        final ActionBarMenu menu = this.menu;
        if (menu != null) {
            menu.setPopupItemsColor(n, b);
        }
    }
    
    public void setSearchTextColor(final int n, final boolean b) {
        final ActionBarMenu menu = this.menu;
        if (menu != null) {
            menu.setSearchTextColor(n, b);
        }
    }
    
    public void setSubtitle(final CharSequence text) {
        if (text != null && this.subtitleTextView == null) {
            this.createSubtitleTextView();
        }
        final SimpleTextView subtitleTextView = this.subtitleTextView;
        if (subtitleTextView != null) {
            int visibility;
            if (!TextUtils.isEmpty(text) && !this.isSearchFieldVisible) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            subtitleTextView.setVisibility(visibility);
            this.subtitleTextView.setText(text);
        }
    }
    
    public void setSubtitleColor(final int textColor) {
        if (this.subtitleTextView == null) {
            this.createSubtitleTextView();
        }
        this.subtitleTextView.setTextColor(textColor);
    }
    
    public void setSupportsHolidayImage(final boolean supportsHolidayImage) {
        this.supportsHolidayImage = supportsHolidayImage;
        if (this.supportsHolidayImage) {
            this.fontMetricsInt = new Paint$FontMetricsInt();
            this.rect = new Rect();
        }
        this.invalidate();
    }
    
    public void setTitle(final CharSequence charSequence) {
        if (charSequence != null && this.titleTextView == null) {
            this.createTitleTextView();
        }
        final SimpleTextView titleTextView = this.titleTextView;
        if (titleTextView != null) {
            this.lastTitle = charSequence;
            int visibility;
            if (charSequence != null && !this.isSearchFieldVisible) {
                visibility = 0;
            }
            else {
                visibility = 4;
            }
            titleTextView.setVisibility(visibility);
            this.titleTextView.setText(charSequence);
        }
    }
    
    public void setTitleActionRunnable(final Runnable runnable) {
        this.titleActionRunnable = runnable;
        this.lastRunnable = runnable;
    }
    
    public void setTitleColor(final int textColor) {
        if (this.titleTextView == null) {
            this.createTitleTextView();
        }
        this.titleTextView.setTextColor(textColor);
    }
    
    public void setTitleOverlayText(final String s, int visibility, Runnable lastRunnable) {
        if (this.allowOverlayTitle) {
            if (this.parentFragment.parentLayout != null) {
                CharSequence text;
                if (s != null) {
                    text = LocaleController.getString(s, visibility);
                }
                else {
                    text = this.lastTitle;
                }
                if (text != null && this.titleTextView == null) {
                    this.createTitleTextView();
                }
                if (this.titleTextView != null) {
                    visibility = 0;
                    this.titleOverlayShown = (s != null);
                    if (this.supportsHolidayImage) {
                        this.titleTextView.invalidate();
                        this.invalidate();
                    }
                    final SimpleTextView titleTextView = this.titleTextView;
                    if (text == null || this.isSearchFieldVisible) {
                        visibility = 4;
                    }
                    titleTextView.setVisibility(visibility);
                    this.titleTextView.setText(text);
                }
                if (lastRunnable == null) {
                    lastRunnable = this.lastRunnable;
                }
                this.titleActionRunnable = lastRunnable;
            }
        }
    }
    
    public void setTitleRightMargin(final int titleRightMargin) {
        this.titleRightMargin = titleRightMargin;
    }
    
    public void setTranslationY(final float translationY) {
        super.setTranslationY(translationY);
        if (this.clipContent) {
            this.invalidate();
        }
    }
    
    public void showActionMode() {
        this.showActionMode(null, null, null, null, null, 0);
    }
    
    public void showActionMode(View actionModeTop, final View actionModeShowingView, final View[] actionModeHidingViews, final boolean[] array, final View actionModeTranslationView, final int n) {
        if (this.actionMode != null) {
            if (!this.actionModeVisible) {
                this.actionModeVisible = true;
                final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                list.add(ObjectAnimator.ofFloat((Object)this.actionMode, View.ALPHA, new float[] { 0.0f, 1.0f }));
                if (actionModeHidingViews != null) {
                    for (int i = 0; i < actionModeHidingViews.length; ++i) {
                        if (actionModeHidingViews[i] != null) {
                            list.add(ObjectAnimator.ofFloat((Object)actionModeHidingViews[i], View.ALPHA, new float[] { 1.0f, 0.0f }));
                        }
                    }
                }
                if (actionModeShowingView != null) {
                    list.add(ObjectAnimator.ofFloat((Object)actionModeShowingView, View.ALPHA, new float[] { 0.0f, 1.0f }));
                }
                if (actionModeTranslationView != null) {
                    list.add(ObjectAnimator.ofFloat((Object)actionModeTranslationView, View.TRANSLATION_Y, new float[] { (float)n }));
                    this.actionModeTranslationView = actionModeTranslationView;
                }
                this.actionModeExtraView = actionModeTop;
                this.actionModeShowingView = actionModeShowingView;
                this.actionModeHidingViews = actionModeHidingViews;
                if (this.occupyStatusBar) {
                    actionModeTop = this.actionModeTop;
                    if (actionModeTop != null) {
                        list.add(ObjectAnimator.ofFloat((Object)actionModeTop, View.ALPHA, new float[] { 0.0f, 1.0f }));
                    }
                }
                final AnimatorSet actionModeAnimation = this.actionModeAnimation;
                if (actionModeAnimation != null) {
                    actionModeAnimation.cancel();
                }
                (this.actionModeAnimation = new AnimatorSet()).playTogether((Collection)list);
                this.actionModeAnimation.setDuration(200L);
                this.actionModeAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator obj) {
                        if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(obj)) {
                            ActionBar.this.actionModeAnimation = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator obj) {
                        if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(obj)) {
                            ActionBar.this.actionModeAnimation = null;
                            if (ActionBar.this.titleTextView != null) {
                                ActionBar.this.titleTextView.setVisibility(4);
                            }
                            if (ActionBar.this.subtitleTextView != null && !TextUtils.isEmpty(ActionBar.this.subtitleTextView.getText())) {
                                ActionBar.this.subtitleTextView.setVisibility(4);
                            }
                            if (ActionBar.this.menu != null) {
                                ActionBar.this.menu.setVisibility(4);
                            }
                            if (ActionBar.this.actionModeHidingViews != null) {
                                for (int i = 0; i < ActionBar.this.actionModeHidingViews.length; ++i) {
                                    if (ActionBar.this.actionModeHidingViews[i] != null) {
                                        final boolean[] val$hideView = array;
                                        if (val$hideView == null || i >= val$hideView.length || val$hideView[i]) {
                                            ActionBar.this.actionModeHidingViews[i].setVisibility(4);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    public void onAnimationStart(final Animator animator) {
                        ActionBar.this.actionMode.setVisibility(0);
                        if (ActionBar.this.occupyStatusBar && ActionBar.this.actionModeTop != null) {
                            ActionBar.this.actionModeTop.setVisibility(0);
                        }
                    }
                });
                this.actionModeAnimation.start();
                final ImageView backButtonImageView = this.backButtonImageView;
                if (backButtonImageView != null) {
                    final Drawable drawable = backButtonImageView.getDrawable();
                    if (drawable instanceof BackDrawable) {
                        ((BackDrawable)drawable).setRotation(1.0f, true);
                    }
                    this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsActionModeBackgroundColor));
                }
            }
        }
    }
    
    public void showActionModeTop() {
        if (this.occupyStatusBar && this.actionModeTop == null) {
            (this.actionModeTop = new View(this.getContext())).setBackgroundColor(Theme.getColor("actionBarActionModeDefaultTop"));
            this.addView(this.actionModeTop);
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.actionModeTop.getLayoutParams();
            layoutParams.height = AndroidUtilities.statusBarHeight;
            layoutParams.width = -1;
            layoutParams.gravity = 51;
            this.actionModeTop.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
    }
    
    public static class ActionBarMenuOnItemClick
    {
        public boolean canOpenMenu() {
            return true;
        }
        
        public void onItemClick(final int n) {
        }
    }
}
