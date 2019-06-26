// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.ViewPropertyAnimator;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import org.telegram.ui.Cells.TextColorThemeCell;
import java.util.HashMap;
import android.view.KeyEvent;
import android.graphics.Shader;
import android.graphics.ComposeShader;
import android.graphics.PorterDuff$Mode;
import android.graphics.RadialGradient;
import android.graphics.Shader$TileMode;
import android.graphics.SweepGradient;
import android.graphics.Bitmap$Config;
import android.widget.TextView$OnEditorActionListener;
import org.telegram.messenger.Utilities;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.graphics.Paint;
import android.graphics.LinearGradient;
import android.util.Property;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.View$OnClickListener;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.LocaleController;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View$MeasureSpec;
import android.graphics.Color;
import android.os.Build$VERSION;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.TextView;
import org.telegram.ui.ActionBar.BottomSheet;
import android.graphics.Bitmap;
import java.io.File;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.ui.ActionBar.ActionBarLayout;
import android.content.DialogInterface$OnDismissListener;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.LaunchActivity;
import android.view.MotionEvent;
import android.content.DialogInterface;
import android.content.Context;
import androidx.annotation.Keep;
import android.content.Intent;
import org.telegram.messenger.FileLog;
import android.view.ViewGroup$LayoutParams;
import android.content.SharedPreferences$Editor;
import java.util.Collection;
import android.animation.Animator$AnimatorListener;
import org.telegram.ui.ActionBar.Theme;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import android.animation.ObjectAnimator;
import org.telegram.messenger.AndroidUtilities;
import android.widget.FrameLayout;
import android.view.WindowManager;
import android.view.WindowManager$LayoutParams;
import android.content.SharedPreferences;
import android.app.Activity;
import android.view.animation.DecelerateInterpolator;
import org.telegram.ui.ActionBar.ThemeDescription;
import java.util.ArrayList;
import android.annotation.SuppressLint;

public class ThemeEditorView
{
    @SuppressLint({ "StaticFieldLeak" })
    private static volatile ThemeEditorView Instance;
    private ArrayList<ThemeDescription> currentThemeDesription;
    private int currentThemeDesriptionPosition;
    private String currentThemeName;
    private DecelerateInterpolator decelerateInterpolator;
    private EditorAlert editorAlert;
    private final int editorHeight;
    private final int editorWidth;
    private boolean hidden;
    private Activity parentActivity;
    private SharedPreferences preferences;
    private WallpaperUpdater wallpaperUpdater;
    private WindowManager$LayoutParams windowLayoutParams;
    private WindowManager windowManager;
    private FrameLayout windowView;
    
    public ThemeEditorView() {
        this.editorWidth = AndroidUtilities.dp(54.0f);
        this.editorHeight = AndroidUtilities.dp(54.0f);
    }
    
    private void animateToBoundsMaybe() {
        final int sideCoord = getSideCoord(true, 0, 0.0f, this.editorWidth);
        final int sideCoord2 = getSideCoord(true, 1, 0.0f, this.editorWidth);
        final int sideCoord3 = getSideCoord(false, 0, 0.0f, this.editorHeight);
        final int sideCoord4 = getSideCoord(false, 1, 0.0f, this.editorHeight);
        final SharedPreferences$Editor edit = this.preferences.edit();
        final int dp = AndroidUtilities.dp(20.0f);
        ArrayList<ObjectAnimator> list = null;
        boolean b = false;
        Label_0457: {
            Label_0455: {
                if (Math.abs(sideCoord - this.windowLayoutParams.x) > dp) {
                    final int x = this.windowLayoutParams.x;
                    if (x >= 0 || x <= -this.editorWidth / 4) {
                        Label_0378: {
                            if (Math.abs(sideCoord2 - this.windowLayoutParams.x) > dp) {
                                final int x2 = this.windowLayoutParams.x;
                                final int x3 = AndroidUtilities.displaySize.x;
                                final int editorWidth = this.editorWidth;
                                if (x2 <= x3 - editorWidth || x2 >= x3 - editorWidth / 4 * 3) {
                                    if (this.windowView.getAlpha() != 1.0f) {
                                        list = new ArrayList<ObjectAnimator>();
                                        if (this.windowLayoutParams.x < 0) {
                                            list.add(ObjectAnimator.ofInt((Object)this, "x", new int[] { -this.editorWidth }));
                                        }
                                        else {
                                            list.add(ObjectAnimator.ofInt((Object)this, "x", new int[] { AndroidUtilities.displaySize.x }));
                                        }
                                        b = true;
                                        break Label_0457;
                                    }
                                    edit.putFloat("px", (this.windowLayoutParams.x - sideCoord) / (float)(sideCoord2 - sideCoord));
                                    edit.putInt("sidex", 2);
                                    list = null;
                                    break Label_0378;
                                }
                            }
                            list = new ArrayList<ObjectAnimator>();
                            edit.putInt("sidex", 1);
                            if (this.windowView.getAlpha() != 1.0f) {
                                list.add(ObjectAnimator.ofFloat((Object)this.windowView, View.ALPHA, new float[] { 1.0f }));
                            }
                            list.add(ObjectAnimator.ofInt((Object)this, "x", new int[] { sideCoord2 }));
                        }
                        break Label_0455;
                    }
                }
                list = new ArrayList<ObjectAnimator>();
                edit.putInt("sidex", 0);
                if (this.windowView.getAlpha() != 1.0f) {
                    list.add(ObjectAnimator.ofFloat((Object)this.windowView, View.ALPHA, new float[] { 1.0f }));
                }
                list.add(ObjectAnimator.ofInt((Object)this, "x", new int[] { sideCoord }));
            }
            b = false;
        }
        ArrayList<ObjectAnimator> list2 = list;
        if (!b) {
            if (Math.abs(sideCoord3 - this.windowLayoutParams.y) > dp && this.windowLayoutParams.y > ActionBar.getCurrentActionBarHeight()) {
                if (Math.abs(sideCoord4 - this.windowLayoutParams.y) <= dp) {
                    ArrayList<ObjectAnimator> list3;
                    if ((list3 = list) == null) {
                        list3 = new ArrayList<ObjectAnimator>();
                    }
                    edit.putInt("sidey", 1);
                    list3.add(ObjectAnimator.ofInt((Object)this, "y", new int[] { sideCoord4 }));
                    list = list3;
                }
                else {
                    edit.putFloat("py", (this.windowLayoutParams.y - sideCoord3) / (float)(sideCoord4 - sideCoord3));
                    edit.putInt("sidey", 2);
                }
            }
            else {
                ArrayList<ObjectAnimator> list4;
                if ((list4 = list) == null) {
                    list4 = new ArrayList<ObjectAnimator>();
                }
                edit.putInt("sidey", 0);
                list4.add(ObjectAnimator.ofInt((Object)this, "y", new int[] { sideCoord3 }));
                list = list4;
            }
            edit.commit();
            list2 = list;
        }
        if (list2 != null) {
            if (this.decelerateInterpolator == null) {
                this.decelerateInterpolator = new DecelerateInterpolator();
            }
            final AnimatorSet set = new AnimatorSet();
            set.setInterpolator((TimeInterpolator)this.decelerateInterpolator);
            set.setDuration(150L);
            if (b) {
                list2.add(ObjectAnimator.ofFloat((Object)this.windowView, View.ALPHA, new float[] { 0.0f }));
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        Theme.saveCurrentTheme(ThemeEditorView.this.currentThemeName, true);
                        ThemeEditorView.this.destroy();
                    }
                });
            }
            set.playTogether((Collection)list2);
            set.start();
        }
    }
    
    public static ThemeEditorView getInstance() {
        return ThemeEditorView.Instance;
    }
    
    private static int getSideCoord(final boolean b, int dp, final float n, int currentActionBarHeight) {
        int x;
        if (b) {
            x = AndroidUtilities.displaySize.x;
        }
        else {
            x = AndroidUtilities.displaySize.y - currentActionBarHeight;
            currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
        }
        currentActionBarHeight = x - currentActionBarHeight;
        if (dp == 0) {
            dp = AndroidUtilities.dp(10.0f);
        }
        else if (dp == 1) {
            dp = currentActionBarHeight - AndroidUtilities.dp(10.0f);
        }
        else {
            dp = Math.round((currentActionBarHeight - AndroidUtilities.dp(20.0f)) * n) + AndroidUtilities.dp(10.0f);
        }
        currentActionBarHeight = dp;
        if (!b) {
            currentActionBarHeight = dp + ActionBar.getCurrentActionBarHeight();
        }
        return currentActionBarHeight;
    }
    
    private void hide() {
        if (this.parentActivity == null) {
            return;
        }
        try {
            final AnimatorSet set = new AnimatorSet();
            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.windowView, View.ALPHA, new float[] { 1.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.windowView, "scaleX", new float[] { 1.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.windowView, "scaleY", new float[] { 1.0f, 0.0f }) });
            set.setInterpolator((TimeInterpolator)this.decelerateInterpolator);
            set.setDuration(150L);
            set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (ThemeEditorView.this.windowView != null) {
                        ThemeEditorView.this.windowManager.removeView((View)ThemeEditorView.this.windowView);
                    }
                }
            });
            set.start();
            this.hidden = true;
        }
        catch (Exception ex) {}
    }
    
    private void show() {
        if (this.parentActivity == null) {
            return;
        }
        try {
            this.windowManager.addView((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
            this.hidden = false;
            this.showWithAnimation();
        }
        catch (Exception ex) {}
    }
    
    private void showWithAnimation() {
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.windowView, View.ALPHA, new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.windowView, "scaleX", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.windowView, "scaleY", new float[] { 0.0f, 1.0f }) });
        set.setInterpolator((TimeInterpolator)this.decelerateInterpolator);
        set.setDuration(150L);
        set.start();
    }
    
    public void close() {
        while (true) {
            try {
                this.windowManager.removeView((View)this.windowView);
                this.parentActivity = null;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public void destroy() {
        this.wallpaperUpdater.cleanup();
        if (this.parentActivity != null) {
            final FrameLayout windowView = this.windowView;
            if (windowView != null) {
                try {
                    this.windowManager.removeViewImmediate((View)windowView);
                    this.windowView = null;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                try {
                    if (this.editorAlert != null) {
                        this.editorAlert.dismiss();
                        this.editorAlert = null;
                    }
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
                this.parentActivity = null;
                ThemeEditorView.Instance = null;
            }
        }
    }
    
    public int getX() {
        return this.windowLayoutParams.x;
    }
    
    public int getY() {
        return this.windowLayoutParams.y;
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        final WallpaperUpdater wallpaperUpdater = this.wallpaperUpdater;
        if (wallpaperUpdater != null) {
            wallpaperUpdater.onActivityResult(n, n2, intent);
        }
    }
    
    public void onConfigurationChanged() {
        final int int1 = this.preferences.getInt("sidex", 1);
        final int int2 = this.preferences.getInt("sidey", 0);
        final float float1 = this.preferences.getFloat("px", 0.0f);
        final float float2 = this.preferences.getFloat("py", 0.0f);
        this.windowLayoutParams.x = getSideCoord(true, int1, float1, this.editorWidth);
        this.windowLayoutParams.y = getSideCoord(false, int2, float2, this.editorHeight);
        try {
            if (this.windowView.getParent() != null) {
                this.windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    @Keep
    public void setX(final int x) {
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.x = x;
        this.windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)windowLayoutParams);
    }
    
    @Keep
    public void setY(final int y) {
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.y = y;
        this.windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)windowLayoutParams);
    }
    
    public void show(final Activity parentActivity, final String currentThemeName) {
        if (ThemeEditorView.Instance != null) {
            ThemeEditorView.Instance.destroy();
        }
        this.hidden = false;
        this.currentThemeName = currentThemeName;
        (this.windowView = new FrameLayout(parentActivity) {
            private boolean dragging;
            private float startX;
            private float startY;
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                return true;
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                final float rawX = motionEvent.getRawX();
                final float rawY = motionEvent.getRawY();
                if (motionEvent.getAction() == 0) {
                    this.startX = rawX;
                    this.startY = rawY;
                }
                else if (motionEvent.getAction() == 2 && !this.dragging) {
                    if (Math.abs(this.startX - rawX) >= AndroidUtilities.getPixelsInCM(0.3f, true) || Math.abs(this.startY - rawY) >= AndroidUtilities.getPixelsInCM(0.3f, false)) {
                        this.dragging = true;
                        this.startX = rawX;
                        this.startY = rawY;
                    }
                }
                else if (motionEvent.getAction() == 1 && !this.dragging && ThemeEditorView.this.editorAlert == null) {
                    final LaunchActivity launchActivity = (LaunchActivity)ThemeEditorView.this.parentActivity;
                    final boolean tablet = AndroidUtilities.isTablet();
                    final BaseFragment baseFragment = null;
                    ActionBarLayout actionBarLayout2 = null;
                    Label_0228: {
                        if (tablet) {
                            final ActionBarLayout layersActionBarLayout = launchActivity.getLayersActionBarLayout();
                            ActionBarLayout actionBarLayout;
                            if ((actionBarLayout = layersActionBarLayout) != null) {
                                actionBarLayout = layersActionBarLayout;
                                if (layersActionBarLayout.fragmentsStack.isEmpty()) {
                                    actionBarLayout = null;
                                }
                            }
                            if ((actionBarLayout2 = actionBarLayout) != null) {
                                break Label_0228;
                            }
                            final ActionBarLayout rightActionBarLayout = launchActivity.getRightActionBarLayout();
                            if ((actionBarLayout2 = rightActionBarLayout) == null) {
                                break Label_0228;
                            }
                            actionBarLayout2 = rightActionBarLayout;
                            if (!rightActionBarLayout.fragmentsStack.isEmpty()) {
                                break Label_0228;
                            }
                        }
                        actionBarLayout2 = null;
                    }
                    ActionBarLayout actionBarLayout3;
                    if ((actionBarLayout3 = actionBarLayout2) == null) {
                        actionBarLayout3 = launchActivity.getActionBarLayout();
                    }
                    if (actionBarLayout3 != null) {
                        BaseFragment baseFragment2 = baseFragment;
                        if (!actionBarLayout3.fragmentsStack.isEmpty()) {
                            final ArrayList<BaseFragment> fragmentsStack = actionBarLayout3.fragmentsStack;
                            baseFragment2 = fragmentsStack.get(fragmentsStack.size() - 1);
                        }
                        if (baseFragment2 != null) {
                            final ThemeDescription[] themeDescriptions = baseFragment2.getThemeDescriptions();
                            if (themeDescriptions != null) {
                                final ThemeEditorView this$0 = ThemeEditorView.this;
                                this$0.editorAlert = this$0.new EditorAlert((Context)this$0.parentActivity, themeDescriptions);
                                ThemeEditorView.this.editorAlert.setOnDismissListener((DialogInterface$OnDismissListener)_$$Lambda$ThemeEditorView$1$wsYCYqNbqFfDt4B1cHgNjwuFWAI.INSTANCE);
                                ThemeEditorView.this.editorAlert.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$ThemeEditorView$1$E9dxEm5ftbqgpiMF889EOuTy_RY(this));
                                ThemeEditorView.this.editorAlert.show();
                                ThemeEditorView.this.hide();
                            }
                        }
                    }
                }
                if (this.dragging) {
                    if (motionEvent.getAction() == 2) {
                        final float startX = this.startX;
                        final float startY = this.startY;
                        final WindowManager$LayoutParams access$5300 = ThemeEditorView.this.windowLayoutParams;
                        access$5300.x += (int)(rawX - startX);
                        final WindowManager$LayoutParams access$5301 = ThemeEditorView.this.windowLayoutParams;
                        access$5301.y += (int)(rawY - startY);
                        final int n = ThemeEditorView.this.editorWidth / 2;
                        final int x = ThemeEditorView.this.windowLayoutParams.x;
                        final int x2 = -n;
                        if (x < x2) {
                            ThemeEditorView.this.windowLayoutParams.x = x2;
                        }
                        else if (ThemeEditorView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - ThemeEditorView.this.windowLayoutParams.width + n) {
                            ThemeEditorView.this.windowLayoutParams.x = AndroidUtilities.displaySize.x - ThemeEditorView.this.windowLayoutParams.width + n;
                        }
                        final int x3 = ThemeEditorView.this.windowLayoutParams.x;
                        float alpha = 1.0f;
                        if (x3 < 0) {
                            alpha = 1.0f + ThemeEditorView.this.windowLayoutParams.x / (float)n * 0.5f;
                        }
                        else if (ThemeEditorView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - ThemeEditorView.this.windowLayoutParams.width) {
                            alpha = 1.0f - (ThemeEditorView.this.windowLayoutParams.x - AndroidUtilities.displaySize.x + ThemeEditorView.this.windowLayoutParams.width) / (float)n * 0.5f;
                        }
                        if (ThemeEditorView.this.windowView.getAlpha() != alpha) {
                            ThemeEditorView.this.windowView.setAlpha(alpha);
                        }
                        if (ThemeEditorView.this.windowLayoutParams.y < 0) {
                            ThemeEditorView.this.windowLayoutParams.y = 0;
                        }
                        else if (ThemeEditorView.this.windowLayoutParams.y > AndroidUtilities.displaySize.y - ThemeEditorView.this.windowLayoutParams.height + 0) {
                            ThemeEditorView.this.windowLayoutParams.y = AndroidUtilities.displaySize.y - ThemeEditorView.this.windowLayoutParams.height + 0;
                        }
                        ThemeEditorView.this.windowManager.updateViewLayout((View)ThemeEditorView.this.windowView, (ViewGroup$LayoutParams)ThemeEditorView.this.windowLayoutParams);
                        this.startX = rawX;
                        this.startY = rawY;
                    }
                    else if (motionEvent.getAction() == 1) {
                        this.dragging = false;
                        ThemeEditorView.this.animateToBoundsMaybe();
                    }
                }
                return true;
            }
        }).setBackgroundResource(2131165873);
        this.windowManager = (WindowManager)parentActivity.getSystemService("window");
        this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
        final int int1 = this.preferences.getInt("sidex", 1);
        final int int2 = this.preferences.getInt("sidey", 0);
        final float float1 = this.preferences.getFloat("px", 0.0f);
        final float float2 = this.preferences.getFloat("py", 0.0f);
        try {
            this.windowLayoutParams = new WindowManager$LayoutParams();
            this.windowLayoutParams.width = this.editorWidth;
            this.windowLayoutParams.height = this.editorHeight;
            this.windowLayoutParams.x = getSideCoord(true, int1, float1, this.editorWidth);
            this.windowLayoutParams.y = getSideCoord(false, int2, float2, this.editorHeight);
            this.windowLayoutParams.format = -3;
            this.windowLayoutParams.gravity = 51;
            this.windowLayoutParams.type = 99;
            this.windowLayoutParams.flags = 16777736;
            this.windowManager.addView((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
            this.wallpaperUpdater = new WallpaperUpdater(parentActivity, null, (WallpaperUpdater.WallpaperUpdaterDelegate)new WallpaperUpdater.WallpaperUpdaterDelegate() {
                @Override
                public void didSelectWallpaper(final File file, final Bitmap bitmap, final boolean b) {
                    Theme.setThemeWallpaper(currentThemeName, bitmap, file);
                }
                
                @Override
                public void needOpenColorPicker() {
                    for (int i = 0; i < ThemeEditorView.this.currentThemeDesription.size(); ++i) {
                        final ThemeDescription themeDescription = ThemeEditorView.this.currentThemeDesription.get(i);
                        themeDescription.startEditing();
                        if (i == 0) {
                            ThemeEditorView.this.editorAlert.colorPicker.setColor(themeDescription.getCurrentColor());
                        }
                    }
                    ThemeEditorView.this.editorAlert.setColorPickerVisible(true);
                }
            });
            ThemeEditorView.Instance = this;
            this.parentActivity = parentActivity;
            this.showWithAnimation();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public class EditorAlert extends BottomSheet
    {
        private boolean animationInProgress;
        private FrameLayout bottomLayout;
        private FrameLayout bottomSaveLayout;
        private TextView cancelButton;
        private AnimatorSet colorChangeAnimation;
        private ColorPicker colorPicker;
        private TextView defaultButtom;
        private FrameLayout frameLayout;
        private boolean ignoreTextChange;
        private LinearLayoutManager layoutManager;
        private ListAdapter listAdapter;
        private RecyclerListView listView;
        private int previousScrollPosition;
        private TextView saveButton;
        private int scrollOffsetY;
        private SearchAdapter searchAdapter;
        private EmptyTextProgressView searchEmptyView;
        private SearchField searchField;
        private View[] shadow;
        private AnimatorSet[] shadowAnimation;
        private Drawable shadowDrawable;
        private boolean startedColorChange;
        private int topBeforeSwitch;
        
        public EditorAlert(final Context context, final ThemeDescription[] array) {
            super(context, true, 1);
            this.shadow = new View[2];
            this.shadowAnimation = new AnimatorSet[2];
            this.shadowDrawable = context.getResources().getDrawable(2131165824).mutate();
            (super.containerView = (ViewGroup)new FrameLayout(context) {
                private boolean ignoreLayout = false;
                private RectF rect1 = new RectF();
                
                protected void onDraw(final Canvas canvas) {
                    int n = EditorAlert.this.scrollOffsetY - EditorAlert.this.backgroundPaddingTop + AndroidUtilities.dp(6.0f);
                    int n2 = EditorAlert.this.scrollOffsetY - EditorAlert.this.backgroundPaddingTop - AndroidUtilities.dp(13.0f);
                    int n3 = this.getMeasuredHeight() + AndroidUtilities.dp(30.0f) + EditorAlert.this.backgroundPaddingTop;
                    int n4 = 0;
                    float n7 = 0.0f;
                    int n9 = 0;
                    Label_0271: {
                        float n8;
                        if (!EditorAlert.this.isFullscreen && Build$VERSION.SDK_INT >= 21) {
                            final int statusBarHeight = AndroidUtilities.statusBarHeight;
                            n4 = n2 + statusBarHeight;
                            final int n5 = n + statusBarHeight;
                            int n6 = n3 - statusBarHeight;
                            final int access$2700 = EditorAlert.this.backgroundPaddingTop;
                            final int statusBarHeight2 = AndroidUtilities.statusBarHeight;
                            if (access$2700 + n4 < statusBarHeight2 * 2) {
                                final int min = Math.min(statusBarHeight2, statusBarHeight2 * 2 - n4 - EditorAlert.this.backgroundPaddingTop);
                                n4 -= min;
                                n6 += min;
                                n7 = 1.0f - Math.min(1.0f, min * 2 / (float)AndroidUtilities.statusBarHeight);
                            }
                            else {
                                n7 = 1.0f;
                            }
                            final int access$2701 = EditorAlert.this.backgroundPaddingTop;
                            final int statusBarHeight3 = AndroidUtilities.statusBarHeight;
                            n = n5;
                            n2 = n4;
                            n3 = n6;
                            n8 = n7;
                            if (access$2701 + n4 < statusBarHeight3) {
                                final int min2 = Math.min(statusBarHeight3, statusBarHeight3 - n4 - EditorAlert.this.backgroundPaddingTop);
                                n = n5;
                                n3 = n6;
                                n9 = min2;
                                break Label_0271;
                            }
                        }
                        else {
                            n8 = 1.0f;
                        }
                        n9 = 0;
                        n7 = n8;
                        n4 = n2;
                    }
                    EditorAlert.this.shadowDrawable.setBounds(0, n4, this.getMeasuredWidth(), n3);
                    EditorAlert.this.shadowDrawable.draw(canvas);
                    if (n7 != 1.0f) {
                        Theme.dialogs_onlineCirclePaint.setColor(-1);
                        this.rect1.set((float)EditorAlert.this.backgroundPaddingLeft, (float)(EditorAlert.this.backgroundPaddingTop + n4), (float)(this.getMeasuredWidth() - EditorAlert.this.backgroundPaddingLeft), (float)(EditorAlert.this.backgroundPaddingTop + n4 + AndroidUtilities.dp(24.0f)));
                        canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(12.0f) * n7, AndroidUtilities.dp(12.0f) * n7, Theme.dialogs_onlineCirclePaint);
                    }
                    final int dp = AndroidUtilities.dp(36.0f);
                    this.rect1.set((float)((this.getMeasuredWidth() - dp) / 2), (float)n, (float)((this.getMeasuredWidth() + dp) / 2), (float)(n + AndroidUtilities.dp(4.0f)));
                    Theme.dialogs_onlineCirclePaint.setColor(-1973016);
                    Theme.dialogs_onlineCirclePaint.setAlpha((int)(EditorAlert.this.listView.getAlpha() * 255.0f));
                    canvas.drawRoundRect(this.rect1, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                    if (n9 > 0) {
                        Theme.dialogs_onlineCirclePaint.setColor(Color.argb(255, (int)(Color.red(-1) * 0.8f), (int)(Color.green(-1) * 0.8f), (int)(Color.blue(-1) * 0.8f)));
                        canvas.drawRect((float)EditorAlert.this.backgroundPaddingLeft, (float)(AndroidUtilities.statusBarHeight - n9), (float)(this.getMeasuredWidth() - EditorAlert.this.backgroundPaddingLeft), (float)AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
                    }
                }
                
                public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 0 && EditorAlert.this.scrollOffsetY != 0 && motionEvent.getY() < EditorAlert.this.scrollOffsetY) {
                        EditorAlert.this.dismiss();
                        return true;
                    }
                    return super.onInterceptTouchEvent(motionEvent);
                }
                
                protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                    super.onLayout(b, n, n2, n3, n4);
                    EditorAlert.this.updateLayout();
                }
                
                protected void onMeasure(final int n, int n2) {
                    final int size = View$MeasureSpec.getSize(n);
                    final int size2 = View$MeasureSpec.getSize(n2);
                    if (Build$VERSION.SDK_INT >= 21 && !EditorAlert.this.isFullscreen) {
                        this.ignoreLayout = true;
                        this.setPadding(EditorAlert.this.backgroundPaddingLeft, AndroidUtilities.statusBarHeight, EditorAlert.this.backgroundPaddingLeft, 0);
                        this.ignoreLayout = false;
                    }
                    if (Build$VERSION.SDK_INT >= 21) {
                        n2 = AndroidUtilities.statusBarHeight;
                    }
                    else {
                        n2 = 0;
                    }
                    final int min = Math.min(size, size2 - n2);
                    if (Build$VERSION.SDK_INT >= 21) {
                        n2 = AndroidUtilities.statusBarHeight;
                    }
                    else {
                        n2 = 0;
                    }
                    n2 = size2 - n2 + AndroidUtilities.dp(8.0f) - min;
                    if (EditorAlert.this.listView.getPaddingTop() != n2) {
                        this.ignoreLayout = true;
                        EditorAlert.this.listView.getPaddingTop();
                        EditorAlert.this.listView.setPadding(0, n2, 0, AndroidUtilities.dp(48.0f));
                        if (EditorAlert.this.colorPicker.getVisibility() == 0) {
                            final EditorAlert this$1 = EditorAlert.this;
                            this$1.setScrollOffsetY(this$1.listView.getPaddingTop());
                            EditorAlert.this.previousScrollPosition = 0;
                        }
                        this.ignoreLayout = false;
                    }
                    super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(size2, 1073741824));
                }
                
                public boolean onTouchEvent(final MotionEvent motionEvent) {
                    return !EditorAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
                }
                
                public void requestLayout() {
                    if (this.ignoreLayout) {
                        return;
                    }
                    super.requestLayout();
                }
            }).setWillNotDraw(false);
            final ViewGroup containerView = super.containerView;
            final int backgroundPaddingLeft = super.backgroundPaddingLeft;
            containerView.setPadding(backgroundPaddingLeft, 0, backgroundPaddingLeft, 0);
            (this.frameLayout = new FrameLayout(context)).setBackgroundColor(-1);
            this.searchField = new SearchField(context);
            this.frameLayout.addView((View)this.searchField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
            (this.listView = new RecyclerListView(context) {
                @Override
                protected boolean allowSelectChildAtPosition(final float n, final float n2) {
                    final int access$1600 = EditorAlert.this.scrollOffsetY;
                    final int dp = AndroidUtilities.dp(48.0f);
                    final int sdk_INT = Build$VERSION.SDK_INT;
                    boolean b = false;
                    int statusBarHeight;
                    if (sdk_INT >= 21) {
                        statusBarHeight = AndroidUtilities.statusBarHeight;
                    }
                    else {
                        statusBarHeight = 0;
                    }
                    if (n2 >= access$1600 + dp + statusBarHeight) {
                        b = true;
                    }
                    return b;
                }
            }).setSelectorDrawableColor(251658240);
            this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
            this.listView.setClipToPadding(false);
            this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(this.getContext())));
            this.listView.setHorizontalScrollBarEnabled(false);
            this.listView.setVerticalScrollBarEnabled(false);
            super.containerView.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
            this.listView.setAdapter(this.listAdapter = new ListAdapter(context, array));
            this.searchAdapter = new SearchAdapter(context);
            this.listView.setGlowColor(-657673);
            this.listView.setItemAnimator(null);
            this.listView.setLayoutAnimation((LayoutAnimationController)null);
            this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ThemeEditorView$EditorAlert$kK2jW5xMCGw4an44hq65kfySCdU(this));
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                    EditorAlert.this.updateLayout();
                }
            });
            (this.searchEmptyView = new EmptyTextProgressView(context)).setShowAtCenter(true);
            this.searchEmptyView.showTextView();
            this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131559943));
            this.listView.setEmptyView((View)this.searchEmptyView);
            super.containerView.addView((View)this.searchEmptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 52.0f, 0.0f, 0.0f));
            final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
            frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(58.0f);
            (this.shadow[0] = new View(context)).setBackgroundColor(301989888);
            this.shadow[0].setAlpha(0.0f);
            this.shadow[0].setTag((Object)1);
            super.containerView.addView(this.shadow[0], (ViewGroup$LayoutParams)frameLayout$LayoutParams);
            super.containerView.addView((View)this.frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 58, 51));
            (this.colorPicker = new ColorPicker(context)).setVisibility(8);
            super.containerView.addView((View)this.colorPicker, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 1));
            final FrameLayout$LayoutParams frameLayout$LayoutParams2 = new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
            frameLayout$LayoutParams2.bottomMargin = AndroidUtilities.dp(48.0f);
            (this.shadow[1] = new View(context)).setBackgroundColor(301989888);
            super.containerView.addView(this.shadow[1], (ViewGroup$LayoutParams)frameLayout$LayoutParams2);
            (this.bottomSaveLayout = new FrameLayout(context)).setBackgroundColor(-1);
            super.containerView.addView((View)this.bottomSaveLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
            final TextView textView = new TextView(context);
            textView.setTextSize(1, 14.0f);
            textView.setTextColor(-15095832);
            textView.setGravity(17);
            textView.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            textView.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            textView.setText((CharSequence)LocaleController.getString("CloseEditor", 2131559118).toUpperCase());
            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.bottomSaveLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
            textView.setOnClickListener((View$OnClickListener)new _$$Lambda$ThemeEditorView$EditorAlert$kRxzT12O1gEcsTUiGkZYFSscUt8(this));
            final TextView textView2 = new TextView(context);
            textView2.setTextSize(1, 14.0f);
            textView2.setTextColor(-15095832);
            textView2.setGravity(17);
            textView2.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            textView2.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            textView2.setText((CharSequence)LocaleController.getString("SaveTheme", 2131560627).toUpperCase());
            textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.bottomSaveLayout.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 53));
            textView2.setOnClickListener((View$OnClickListener)new _$$Lambda$ThemeEditorView$EditorAlert$_CO4an8qJdIEQQjX1P4ENdamV9E(this));
            (this.bottomLayout = new FrameLayout(context)).setVisibility(8);
            this.bottomLayout.setBackgroundColor(-1);
            super.containerView.addView((View)this.bottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
            (this.cancelButton = new TextView(context)).setTextSize(1, 14.0f);
            this.cancelButton.setTextColor(-15095832);
            this.cancelButton.setGravity(17);
            this.cancelButton.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            this.cancelButton.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            this.cancelButton.setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
            this.cancelButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.bottomLayout.addView((View)this.cancelButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
            this.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ThemeEditorView$EditorAlert$e8sB4SzqRAAe3BbXeRhAVLL0Fkg(this));
            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            this.bottomLayout.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 53));
            (this.defaultButtom = new TextView(context)).setTextSize(1, 14.0f);
            this.defaultButtom.setTextColor(-15095832);
            this.defaultButtom.setGravity(17);
            this.defaultButtom.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            this.defaultButtom.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            this.defaultButtom.setText((CharSequence)LocaleController.getString("Default", 2131559225).toUpperCase());
            this.defaultButtom.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            linearLayout.addView((View)this.defaultButtom, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
            this.defaultButtom.setOnClickListener((View$OnClickListener)new _$$Lambda$ThemeEditorView$EditorAlert$KOpMpGNwWrKZ5XW39TuNtWWXkWM(this));
            final TextView textView3 = new TextView(context);
            textView3.setTextSize(1, 14.0f);
            textView3.setTextColor(-15095832);
            textView3.setGravity(17);
            textView3.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            textView3.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            textView3.setText((CharSequence)LocaleController.getString("Save", 2131560626).toUpperCase());
            textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            linearLayout.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
            textView3.setOnClickListener((View$OnClickListener)new _$$Lambda$ThemeEditorView$EditorAlert$NlmVbEdgSNku_2tKRx9Agp6b_e4(this));
        }
        
        private int getCurrentTop() {
            if (this.listView.getChildCount() != 0) {
                final RecyclerListView listView = this.listView;
                final int n = 0;
                final View child = listView.getChildAt(0);
                final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findContainingViewHolder(child);
                if (holder != null) {
                    final int paddingTop = this.listView.getPaddingTop();
                    int top = n;
                    if (((RecyclerView.ViewHolder)holder).getAdapterPosition() == 0) {
                        top = n;
                        if (child.getTop() >= 0) {
                            top = child.getTop();
                        }
                    }
                    return paddingTop - top;
                }
            }
            return -1000;
        }
        
        private void runShadowAnimation(final int n, final boolean b) {
            if ((b && this.shadow[n].getTag() != null) || (!b && this.shadow[n].getTag() == null)) {
                final View view = this.shadow[n];
                Object value;
                if (b) {
                    value = null;
                }
                else {
                    value = 1;
                }
                view.setTag(value);
                if (b) {
                    this.shadow[n].setVisibility(0);
                }
                final AnimatorSet[] shadowAnimation = this.shadowAnimation;
                if (shadowAnimation[n] != null) {
                    shadowAnimation[n].cancel();
                }
                this.shadowAnimation[n] = new AnimatorSet();
                final AnimatorSet set = this.shadowAnimation[n];
                final View view2 = this.shadow[n];
                final Property alpha = View.ALPHA;
                float n2;
                if (b) {
                    n2 = 1.0f;
                }
                else {
                    n2 = 0.0f;
                }
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)view2, alpha, new float[] { n2 }) });
                this.shadowAnimation[n].setDuration(150L);
                this.shadowAnimation[n].addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator obj) {
                        if (EditorAlert.this.shadowAnimation[n] != null && EditorAlert.this.shadowAnimation[n].equals(obj)) {
                            EditorAlert.this.shadowAnimation[n] = null;
                        }
                    }
                    
                    public void onAnimationEnd(final Animator obj) {
                        if (EditorAlert.this.shadowAnimation[n] != null && EditorAlert.this.shadowAnimation[n].equals(obj)) {
                            if (!b) {
                                EditorAlert.this.shadow[n].setVisibility(4);
                            }
                            EditorAlert.this.shadowAnimation[n] = null;
                        }
                    }
                });
                this.shadowAnimation[n].start();
            }
        }
        
        private void setColorPickerVisible(final boolean b) {
            float n = 0.0f;
            if (b) {
                this.animationInProgress = true;
                this.colorPicker.setVisibility(0);
                this.bottomLayout.setVisibility(0);
                this.colorPicker.setAlpha(0.0f);
                this.bottomLayout.setAlpha(0.0f);
                this.previousScrollPosition = this.scrollOffsetY;
                final AnimatorSet set = new AnimatorSet();
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.colorPicker, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.bottomLayout, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.listView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.frameLayout, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shadow[0], View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.searchEmptyView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.bottomSaveLayout, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofInt((Object)this, "scrollOffsetY", new int[] { this.listView.getPaddingTop() }) });
                set.setDuration(150L);
                set.setInterpolator((TimeInterpolator)ThemeEditorView.this.decelerateInterpolator);
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        EditorAlert.this.listView.setVisibility(4);
                        EditorAlert.this.searchField.setVisibility(4);
                        EditorAlert.this.bottomSaveLayout.setVisibility(4);
                        EditorAlert.this.animationInProgress = false;
                    }
                });
                set.start();
            }
            else {
                if (ThemeEditorView.this.parentActivity != null) {
                    ((LaunchActivity)ThemeEditorView.this.parentActivity).rebuildAllFragments(false);
                }
                Theme.saveCurrentTheme(ThemeEditorView.this.currentThemeName, false);
                if (this.listView.getAdapter() == this.listAdapter) {
                    AndroidUtilities.hideKeyboard(this.getCurrentFocus());
                }
                this.animationInProgress = true;
                this.listView.setVisibility(0);
                this.bottomSaveLayout.setVisibility(0);
                this.searchField.setVisibility(0);
                this.listView.setAlpha(0.0f);
                final AnimatorSet set2 = new AnimatorSet();
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.colorPicker, View.ALPHA, new float[] { 0.0f });
                final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.bottomLayout, View.ALPHA, new float[] { 0.0f });
                final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.listView, View.ALPHA, new float[] { 1.0f });
                final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)this.frameLayout, View.ALPHA, new float[] { 1.0f });
                final View[] shadow = this.shadow;
                final View view = shadow[0];
                final Property alpha = View.ALPHA;
                if (shadow[0].getTag() == null) {
                    n = 1.0f;
                }
                set2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ofFloat3, (Animator)ofFloat4, (Animator)ObjectAnimator.ofFloat((Object)view, alpha, new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)this.searchEmptyView, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.bottomSaveLayout, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofInt((Object)this, "scrollOffsetY", new int[] { this.previousScrollPosition }) });
                set2.setDuration(150L);
                set2.setInterpolator((TimeInterpolator)ThemeEditorView.this.decelerateInterpolator);
                set2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (EditorAlert.this.listView.getAdapter() == EditorAlert.this.searchAdapter) {
                            EditorAlert.this.searchField.showKeyboard();
                        }
                        EditorAlert.this.colorPicker.setVisibility(8);
                        EditorAlert.this.bottomLayout.setVisibility(8);
                        EditorAlert.this.animationInProgress = false;
                    }
                });
                set2.start();
                this.listAdapter.notifyItemChanged(ThemeEditorView.this.currentThemeDesriptionPosition);
            }
        }
        
        @SuppressLint({ "NewApi" })
        private void updateLayout() {
            if (this.listView.getChildCount() > 0 && this.listView.getVisibility() == 0) {
                if (!this.animationInProgress) {
                    final View child = this.listView.getChildAt(0);
                    final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findContainingViewHolder(child);
                    int paddingTop;
                    if (this.listView.getVisibility() == 0 && !this.animationInProgress) {
                        paddingTop = child.getTop() - AndroidUtilities.dp(8.0f);
                    }
                    else {
                        paddingTop = this.listView.getPaddingTop();
                    }
                    if (paddingTop > -AndroidUtilities.dp(1.0f) && holder != null && ((RecyclerView.ViewHolder)holder).getAdapterPosition() == 0) {
                        this.runShadowAnimation(0, false);
                    }
                    else {
                        this.runShadowAnimation(0, true);
                        paddingTop = 0;
                    }
                    if (this.scrollOffsetY != paddingTop) {
                        this.setScrollOffsetY(paddingTop);
                    }
                }
            }
        }
        
        @Override
        protected boolean canDismissWithSwipe() {
            return false;
        }
        
        public int getScrollOffsetY() {
            return this.scrollOffsetY;
        }
        
        @Keep
        public void setScrollOffsetY(final int scrollOffsetY) {
            this.listView.setTopGlowOffset(this.scrollOffsetY = scrollOffsetY);
            this.frameLayout.setTranslationY((float)this.scrollOffsetY);
            this.colorPicker.setTranslationY((float)this.scrollOffsetY);
            this.searchEmptyView.setTranslationY((float)this.scrollOffsetY);
            super.containerView.invalidate();
        }
        
        private class ColorPicker extends FrameLayout
        {
            private float alpha;
            private LinearGradient alphaGradient;
            private boolean alphaPressed;
            private Drawable circleDrawable;
            private Paint circlePaint;
            private boolean circlePressed;
            private EditTextBoldCursor[] colorEditText;
            private LinearGradient colorGradient;
            private float[] colorHSV;
            private boolean colorPressed;
            private Bitmap colorWheelBitmap;
            private Paint colorWheelPaint;
            private int colorWheelRadius;
            private DecelerateInterpolator decelerateInterpolator;
            private float[] hsvTemp;
            private LinearLayout linearLayout;
            private final int paramValueSliderWidth;
            private Paint valueSliderPaint;
            
            public ColorPicker(final Context context) {
                super(context);
                this.paramValueSliderWidth = AndroidUtilities.dp(20.0f);
                this.colorEditText = new EditTextBoldCursor[4];
                this.colorHSV = new float[] { 0.0f, 0.0f, 1.0f };
                this.alpha = 1.0f;
                this.hsvTemp = new float[3];
                this.decelerateInterpolator = new DecelerateInterpolator();
                this.setWillNotDraw(false);
                this.circlePaint = new Paint(1);
                this.circleDrawable = context.getResources().getDrawable(2131165520).mutate();
                (this.colorWheelPaint = new Paint()).setAntiAlias(true);
                this.colorWheelPaint.setDither(true);
                (this.valueSliderPaint = new Paint()).setAntiAlias(true);
                this.valueSliderPaint.setDither(true);
                (this.linearLayout = new LinearLayout(context)).setOrientation(0);
                this.addView((View)this.linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 49));
                for (int i = 0; i < 4; ++i) {
                    (this.colorEditText[i] = new EditTextBoldCursor(context)).setInputType(2);
                    this.colorEditText[i].setTextColor(-14606047);
                    this.colorEditText[i].setCursorColor(-14606047);
                    this.colorEditText[i].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.colorEditText[i].setCursorWidth(1.5f);
                    this.colorEditText[i].setTextSize(1, 18.0f);
                    this.colorEditText[i].setBackgroundDrawable(Theme.createEditTextDrawable(context, true));
                    this.colorEditText[i].setMaxLines(1);
                    this.colorEditText[i].setTag((Object)i);
                    this.colorEditText[i].setGravity(17);
                    if (i == 0) {
                        this.colorEditText[i].setHint((CharSequence)"red");
                    }
                    else if (i == 1) {
                        this.colorEditText[i].setHint((CharSequence)"green");
                    }
                    else if (i == 2) {
                        this.colorEditText[i].setHint((CharSequence)"blue");
                    }
                    else if (i == 3) {
                        this.colorEditText[i].setHint((CharSequence)"alpha");
                    }
                    final EditTextBoldCursor editTextBoldCursor = this.colorEditText[i];
                    int n;
                    if (i == 3) {
                        n = 6;
                    }
                    else {
                        n = 5;
                    }
                    editTextBoldCursor.setImeOptions(n | 0x10000000);
                    this.colorEditText[i].setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(3) });
                    final LinearLayout linearLayout = this.linearLayout;
                    final EditTextBoldCursor editTextBoldCursor2 = this.colorEditText[i];
                    float n2;
                    if (i != 3) {
                        n2 = 16.0f;
                    }
                    else {
                        n2 = 0.0f;
                    }
                    linearLayout.addView((View)editTextBoldCursor2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(55, 36, 0.0f, 0.0f, n2, 0.0f));
                    this.colorEditText[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                        public void afterTextChanged(final Editable editable) {
                            if (EditorAlert.this.ignoreTextChange) {
                                return;
                            }
                            EditorAlert.this.ignoreTextChange = true;
                            final int intValue = Utilities.parseInt(editable.toString());
                            int n;
                            if (intValue < 0) {
                                final EditTextBoldCursor editTextBoldCursor = ColorPicker.this.colorEditText[i];
                                final StringBuilder sb = new StringBuilder();
                                sb.append("");
                                sb.append(0);
                                editTextBoldCursor.setText((CharSequence)sb.toString());
                                ColorPicker.this.colorEditText[i].setSelection(ColorPicker.this.colorEditText[i].length());
                                n = 0;
                            }
                            else if ((n = intValue) > 255) {
                                final EditTextBoldCursor editTextBoldCursor2 = ColorPicker.this.colorEditText[i];
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("");
                                sb2.append(255);
                                editTextBoldCursor2.setText((CharSequence)sb2.toString());
                                ColorPicker.this.colorEditText[i].setSelection(ColorPicker.this.colorEditText[i].length());
                                n = 255;
                            }
                            final int color = ColorPicker.this.getColor();
                            final int val$num = i;
                            int color2 = 0;
                            Label_0354: {
                                int n3;
                                int n4;
                                if (val$num == 2) {
                                    final int n2 = color & 0xFFFFFF00;
                                    n3 = (n & 0xFF);
                                    n4 = n2;
                                }
                                else if (val$num == 1) {
                                    final int n5 = color & 0xFFFF00FF;
                                    final int n6 = (n & 0xFF) << 8;
                                    n4 = n5;
                                    n3 = n6;
                                }
                                else if (val$num == 0) {
                                    final int n7 = color & 0xFF00FFFF;
                                    final int n8 = (n & 0xFF) << 16;
                                    n4 = n7;
                                    n3 = n8;
                                }
                                else {
                                    color2 = color;
                                    if (val$num != 3) {
                                        break Label_0354;
                                    }
                                    final int n9 = color & 0xFFFFFF;
                                    final int n10 = (n & 0xFF) << 24;
                                    n4 = n9;
                                    n3 = n10;
                                }
                                color2 = (n4 | n3);
                            }
                            ColorPicker.this.setColor(color2);
                            for (int i = 0; i < ThemeEditorView.this.currentThemeDesription.size(); ++i) {
                                ((ThemeDescription)ThemeEditorView.this.currentThemeDesription.get(i)).setColor(ColorPicker.this.getColor(), false);
                            }
                            EditorAlert.this.ignoreTextChange = false;
                        }
                        
                        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                        
                        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                    });
                    this.colorEditText[i].setOnEditorActionListener((TextView$OnEditorActionListener)_$$Lambda$ThemeEditorView$EditorAlert$ColorPicker$ajPoxH4sFpvaVlkZF92J0Sean_E.INSTANCE);
                }
            }
            
            private Bitmap createColorWheelBitmap(final int n, final int n2) {
                final Bitmap bitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
                final int[] array = new int[13];
                final float[] array3;
                final float[] array2 = array3 = new float[3];
                array3[0] = 0.0f;
                array3[2] = (array3[1] = 1.0f);
                for (int i = 0; i < array.length; ++i) {
                    array2[0] = (float)((i * 30 + 180) % 360);
                    array[i] = Color.HSVToColor(array2);
                }
                array[12] = array[0];
                final float n3 = (float)(n / 2);
                final float n4 = (float)(n2 / 2);
                this.colorWheelPaint.setShader((Shader)new ComposeShader((Shader)new SweepGradient(n3, n4, array, (float[])null), (Shader)new RadialGradient(n3, n4, (float)this.colorWheelRadius, -1, 16777215, Shader$TileMode.CLAMP), PorterDuff$Mode.SRC_OVER));
                new Canvas(bitmap).drawCircle(n3, n4, (float)this.colorWheelRadius, this.colorWheelPaint);
                return bitmap;
            }
            
            private void drawPointerArrow(final Canvas canvas, final int n, final int n2, final int color) {
                final int dp = AndroidUtilities.dp(13.0f);
                this.circleDrawable.setBounds(n - dp, n2 - dp, n + dp, dp + n2);
                this.circleDrawable.draw(canvas);
                this.circlePaint.setColor(-1);
                final float n3 = (float)n;
                final float n4 = (float)n2;
                canvas.drawCircle(n3, n4, (float)AndroidUtilities.dp(11.0f), this.circlePaint);
                this.circlePaint.setColor(color);
                canvas.drawCircle(n3, n4, (float)AndroidUtilities.dp(9.0f), this.circlePaint);
            }
            
            private void startColorChange(final boolean b) {
                if (EditorAlert.this.startedColorChange == b) {
                    return;
                }
                if (EditorAlert.this.colorChangeAnimation != null) {
                    EditorAlert.this.colorChangeAnimation.cancel();
                }
                EditorAlert.this.startedColorChange = b;
                EditorAlert.this.colorChangeAnimation = new AnimatorSet();
                final AnimatorSet access$1300 = EditorAlert.this.colorChangeAnimation;
                final ColorDrawable access$1301 = EditorAlert.this.backDrawable;
                final Property<ColorDrawable, Integer> color_DRAWABLE_ALPHA = AnimationProperties.COLOR_DRAWABLE_ALPHA;
                int n;
                if (b) {
                    n = 0;
                }
                else {
                    n = 51;
                }
                final ObjectAnimator ofInt = ObjectAnimator.ofInt((Object)access$1301, (Property)color_DRAWABLE_ALPHA, new int[] { n });
                final ViewGroup access$1302 = EditorAlert.this.containerView;
                final Property alpha = View.ALPHA;
                float n2;
                if (b) {
                    n2 = 0.2f;
                }
                else {
                    n2 = 1.0f;
                }
                access$1300.playTogether(new Animator[] { (Animator)ofInt, (Animator)ObjectAnimator.ofFloat((Object)access$1302, alpha, new float[] { n2 }) });
                EditorAlert.this.colorChangeAnimation.setDuration(150L);
                EditorAlert.this.colorChangeAnimation.setInterpolator((TimeInterpolator)this.decelerateInterpolator);
                EditorAlert.this.colorChangeAnimation.start();
            }
            
            public int getColor() {
                return (Color.HSVToColor(this.colorHSV) & 0xFFFFFF) | (int)(this.alpha * 255.0f) << 24;
            }
            
            protected void onDraw(final Canvas canvas) {
                final int n = this.getWidth() / 2 - this.paramValueSliderWidth * 2;
                final int n2 = this.getHeight() / 2 - AndroidUtilities.dp(8.0f);
                final Bitmap colorWheelBitmap = this.colorWheelBitmap;
                final int colorWheelRadius = this.colorWheelRadius;
                canvas.drawBitmap(colorWheelBitmap, (float)(n - colorWheelRadius), (float)(n2 - colorWheelRadius), (Paint)null);
                final double n3 = (float)Math.toRadians(this.colorHSV[0]);
                final double n4 = -Math.cos(n3);
                final double v = this.colorHSV[1];
                Double.isNaN(v);
                final double v2 = this.colorWheelRadius;
                Double.isNaN(v2);
                final int n5 = (int)(n4 * v * v2);
                final double n6 = -Math.sin(n3);
                final float[] colorHSV = this.colorHSV;
                final double v3 = colorHSV[1];
                Double.isNaN(v3);
                final double v4 = this.colorWheelRadius;
                Double.isNaN(v4);
                final int n7 = (int)(n6 * v3 * v4);
                final float[] hsvTemp = this.hsvTemp;
                hsvTemp[0] = colorHSV[0];
                hsvTemp[1] = colorHSV[1];
                hsvTemp[2] = 1.0f;
                this.drawPointerArrow(canvas, n5 + n, n7 + n2, Color.HSVToColor(hsvTemp));
                final int colorWheelRadius2 = this.colorWheelRadius;
                final int n8 = n + colorWheelRadius2 + this.paramValueSliderWidth;
                final int n9 = n2 - colorWheelRadius2;
                final int dp = AndroidUtilities.dp(9.0f);
                final int n10 = this.colorWheelRadius * 2;
                if (this.colorGradient == null) {
                    this.colorGradient = new LinearGradient((float)n8, (float)n9, (float)(n8 + dp), (float)(n9 + n10), new int[] { -16777216, Color.HSVToColor(this.hsvTemp) }, (float[])null, Shader$TileMode.CLAMP);
                }
                this.valueSliderPaint.setShader((Shader)this.colorGradient);
                final float n11 = (float)n8;
                final float n12 = (float)n9;
                final float n13 = (float)(n8 + dp);
                final float n14 = (float)(n9 + n10);
                canvas.drawRect(n11, n12, n13, n14, this.valueSliderPaint);
                final int n15 = dp / 2;
                final float[] colorHSV2 = this.colorHSV;
                final float n16 = colorHSV2[2];
                final float n17 = (float)n10;
                this.drawPointerArrow(canvas, n8 + n15, (int)(n16 * n17 + n12), Color.HSVToColor(colorHSV2));
                final int n18 = n8 + this.paramValueSliderWidth * 2;
                if (this.alphaGradient == null) {
                    final int hsvToColor = Color.HSVToColor(this.hsvTemp);
                    this.alphaGradient = new LinearGradient((float)n18, n12, (float)(n18 + dp), n14, new int[] { hsvToColor, hsvToColor & 0xFFFFFF }, (float[])null, Shader$TileMode.CLAMP);
                }
                this.valueSliderPaint.setShader((Shader)this.alphaGradient);
                canvas.drawRect((float)n18, n12, (float)(dp + n18), n14, this.valueSliderPaint);
                this.drawPointerArrow(canvas, n18 + n15, (int)(n12 + (1.0f - this.alpha) * n17), (Color.HSVToColor(this.colorHSV) & 0xFFFFFF) | (int)(this.alpha * 255.0f) << 24);
            }
            
            protected void onMeasure(final int n, final int n2) {
                final int min = Math.min(View$MeasureSpec.getSize(n), View$MeasureSpec.getSize(n2));
                this.measureChild((View)this.linearLayout, n, n2);
                this.setMeasuredDimension(min, min);
            }
            
            protected void onSizeChanged(int colorWheelRadius, final int n, final int n2, final int n3) {
                this.colorWheelRadius = Math.max(1, colorWheelRadius / 2 - this.paramValueSliderWidth * 2 - AndroidUtilities.dp(20.0f));
                colorWheelRadius = this.colorWheelRadius;
                this.colorWheelBitmap = this.createColorWheelBitmap(colorWheelRadius * 2, colorWheelRadius * 2);
                this.colorGradient = null;
                this.alphaGradient = null;
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                Label_0048: {
                    if (action != 0) {
                        if (action != 1) {
                            if (action == 2) {
                                break Label_0048;
                            }
                        }
                        else {
                            this.alphaPressed = false;
                            this.colorPressed = false;
                            this.startColorChange(this.circlePressed = false);
                        }
                        return super.onTouchEvent(motionEvent);
                    }
                }
                final int n = (int)motionEvent.getX();
                final int n2 = (int)motionEvent.getY();
                final int n3 = this.getWidth() / 2 - this.paramValueSliderWidth * 2;
                final int n4 = this.getHeight() / 2 - AndroidUtilities.dp(8.0f);
                final int n5 = n - n3;
                final int n6 = n2 - n4;
                final double sqrt = Math.sqrt(n5 * n5 + n6 * n6);
                if (this.circlePressed || (!this.alphaPressed && !this.colorPressed && sqrt <= this.colorWheelRadius)) {
                    final int colorWheelRadius = this.colorWheelRadius;
                    double n7 = sqrt;
                    if (sqrt > colorWheelRadius) {
                        n7 = colorWheelRadius;
                    }
                    this.circlePressed = true;
                    this.colorHSV[0] = (float)(Math.toDegrees(Math.atan2(n6, n5)) + 180.0);
                    final float[] colorHSV = this.colorHSV;
                    final double v = this.colorWheelRadius;
                    Double.isNaN(v);
                    colorHSV[1] = Math.max(0.0f, Math.min(1.0f, (float)(n7 / v)));
                    this.colorGradient = null;
                    this.alphaGradient = null;
                }
                Label_0387: {
                    if (!this.colorPressed) {
                        if (this.circlePressed || this.alphaPressed) {
                            break Label_0387;
                        }
                        final int colorWheelRadius2 = this.colorWheelRadius;
                        final int paramValueSliderWidth = this.paramValueSliderWidth;
                        if (n < n3 + colorWheelRadius2 + paramValueSliderWidth || n > n3 + colorWheelRadius2 + paramValueSliderWidth * 2 || n2 < n4 - colorWheelRadius2 || n2 > colorWheelRadius2 + n4) {
                            break Label_0387;
                        }
                    }
                    final int colorWheelRadius3 = this.colorWheelRadius;
                    final float n8 = (n2 - (n4 - colorWheelRadius3)) / (colorWheelRadius3 * 2.0f);
                    float n9;
                    if (n8 < 0.0f) {
                        n9 = 0.0f;
                    }
                    else {
                        n9 = n8;
                        if (n8 > 1.0f) {
                            n9 = 1.0f;
                        }
                    }
                    this.colorHSV[2] = n9;
                    this.colorPressed = true;
                }
                Label_0530: {
                    if (!this.alphaPressed) {
                        if (this.circlePressed || this.colorPressed) {
                            break Label_0530;
                        }
                        final int colorWheelRadius4 = this.colorWheelRadius;
                        final int paramValueSliderWidth2 = this.paramValueSliderWidth;
                        if (n < n3 + colorWheelRadius4 + paramValueSliderWidth2 * 3 || n > n3 + colorWheelRadius4 + paramValueSliderWidth2 * 4 || n2 < n4 - colorWheelRadius4 || n2 > colorWheelRadius4 + n4) {
                            break Label_0530;
                        }
                    }
                    final int colorWheelRadius5 = this.colorWheelRadius;
                    this.alpha = 1.0f - (n2 - (n4 - colorWheelRadius5)) / (colorWheelRadius5 * 2.0f);
                    final float alpha = this.alpha;
                    if (alpha < 0.0f) {
                        this.alpha = 0.0f;
                    }
                    else if (alpha > 1.0f) {
                        this.alpha = 1.0f;
                    }
                    this.alphaPressed = true;
                }
                if (this.alphaPressed || this.colorPressed || this.circlePressed) {
                    this.startColorChange(true);
                    final int color = this.getColor();
                    for (int i = 0; i < ThemeEditorView.this.currentThemeDesription.size(); ++i) {
                        ((ThemeDescription)ThemeEditorView.this.currentThemeDesription.get(i)).setColor(color, false);
                    }
                    final int red = Color.red(color);
                    final int green = Color.green(color);
                    final int blue = Color.blue(color);
                    final int alpha2 = Color.alpha(color);
                    if (!EditorAlert.this.ignoreTextChange) {
                        EditorAlert.this.ignoreTextChange = true;
                        final EditTextBoldCursor editTextBoldCursor = this.colorEditText[0];
                        final StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(red);
                        editTextBoldCursor.setText((CharSequence)sb.toString());
                        final EditTextBoldCursor editTextBoldCursor2 = this.colorEditText[1];
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        sb2.append(green);
                        editTextBoldCursor2.setText((CharSequence)sb2.toString());
                        final EditTextBoldCursor editTextBoldCursor3 = this.colorEditText[2];
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("");
                        sb3.append(blue);
                        editTextBoldCursor3.setText((CharSequence)sb3.toString());
                        final EditTextBoldCursor editTextBoldCursor4 = this.colorEditText[3];
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("");
                        sb4.append(alpha2);
                        editTextBoldCursor4.setText((CharSequence)sb4.toString());
                        for (int j = 0; j < 4; ++j) {
                            final EditTextBoldCursor[] colorEditText = this.colorEditText;
                            colorEditText[j].setSelection(colorEditText[j].length());
                        }
                        EditorAlert.this.ignoreTextChange = false;
                    }
                    this.invalidate();
                }
                return true;
            }
            
            public void setColor(final int n) {
                final int red = Color.red(n);
                final int green = Color.green(n);
                final int blue = Color.blue(n);
                final int alpha = Color.alpha(n);
                if (!EditorAlert.this.ignoreTextChange) {
                    EditorAlert.this.ignoreTextChange = true;
                    final EditTextBoldCursor editTextBoldCursor = this.colorEditText[0];
                    final StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(red);
                    editTextBoldCursor.setText((CharSequence)sb.toString());
                    final EditTextBoldCursor editTextBoldCursor2 = this.colorEditText[1];
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("");
                    sb2.append(green);
                    editTextBoldCursor2.setText((CharSequence)sb2.toString());
                    final EditTextBoldCursor editTextBoldCursor3 = this.colorEditText[2];
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("");
                    sb3.append(blue);
                    editTextBoldCursor3.setText((CharSequence)sb3.toString());
                    final EditTextBoldCursor editTextBoldCursor4 = this.colorEditText[3];
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("");
                    sb4.append(alpha);
                    editTextBoldCursor4.setText((CharSequence)sb4.toString());
                    for (int i = 0; i < 4; ++i) {
                        final EditTextBoldCursor[] colorEditText = this.colorEditText;
                        colorEditText[i].setSelection(colorEditText[i].length());
                    }
                    EditorAlert.this.ignoreTextChange = false;
                }
                this.alphaGradient = null;
                this.colorGradient = null;
                this.alpha = alpha / 255.0f;
                Color.colorToHSV(n, this.colorHSV);
                this.invalidate();
            }
        }
        
        private class ListAdapter extends SelectionAdapter
        {
            private Context context;
            private int currentCount;
            private ArrayList<ArrayList<ThemeDescription>> items;
            private HashMap<String, ArrayList<ThemeDescription>> itemsMap;
            
            public ListAdapter(final Context context, final ThemeDescription[] array) {
                this.items = new ArrayList<ArrayList<ThemeDescription>>();
                this.itemsMap = new HashMap<String, ArrayList<ThemeDescription>>();
                this.context = context;
                for (int i = 0; i < array.length; ++i) {
                    final ThemeDescription e = array[i];
                    final String currentKey = e.getCurrentKey();
                    ArrayList<ThemeDescription> list;
                    if ((list = this.itemsMap.get(currentKey)) == null) {
                        list = new ArrayList<ThemeDescription>();
                        this.itemsMap.put(currentKey, list);
                        this.items.add(list);
                    }
                    list.add(e);
                }
            }
            
            public ArrayList<ThemeDescription> getItem(final int index) {
                if (index >= 0 && index < this.items.size()) {
                    return this.items.get(index);
                }
                return null;
            }
            
            @Override
            public int getItemCount() {
                int n;
                if (this.items.isEmpty()) {
                    n = 0;
                }
                else {
                    n = this.items.size() + 1;
                }
                return n;
            }
            
            @Override
            public int getItemViewType(final int n) {
                if (n == 0) {
                    return 1;
                }
                return 0;
            }
            
            @Override
            public boolean isEnabled(final ViewHolder viewHolder) {
                return true;
            }
            
            @Override
            public void onBindViewHolder(final ViewHolder viewHolder, int setColor) {
                if (viewHolder.getItemViewType() == 0) {
                    final ArrayList<ThemeDescription> list = this.items.get(setColor - 1);
                    setColor = 0;
                    final ThemeDescription themeDescription = list.get(0);
                    if (!themeDescription.getCurrentKey().equals("chat_wallpaper")) {
                        setColor = themeDescription.getSetColor();
                    }
                    ((TextColorThemeCell)viewHolder.itemView).setTextAndColor(themeDescription.getTitle(), setColor);
                }
            }
            
            @Override
            public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
                Object o;
                if (n != 0) {
                    o = new View(this.context);
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
                }
                else {
                    o = new TextColorThemeCell(this.context);
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
                }
                return new RecyclerListView.Holder((View)o);
            }
        }
        
        public class SearchAdapter extends SelectionAdapter
        {
            private Context context;
            private int currentCount;
            private int lastSearchId;
            private String lastSearchText;
            private ArrayList<CharSequence> searchNames;
            private ArrayList<ArrayList<ThemeDescription>> searchResult;
            private Runnable searchRunnable;
            
            public SearchAdapter(final Context context) {
                this.searchResult = new ArrayList<ArrayList<ThemeDescription>>();
                this.searchNames = new ArrayList<CharSequence>();
                this.context = context;
            }
            
            private void searchDialogsInternal(String s, final int n) {
                try {
                    final String lowerCase = s.trim().toLowerCase();
                    if (lowerCase.length() == 0) {
                        this.lastSearchId = -1;
                        this.updateSearchResults(new ArrayList<ArrayList<ThemeDescription>>(), new ArrayList<CharSequence>(), this.lastSearchId);
                        return;
                    }
                    final String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
                    Label_0081: {
                        if (!lowerCase.equals(translitString)) {
                            s = translitString;
                            if (translitString.length() != 0) {
                                break Label_0081;
                            }
                        }
                        s = null;
                    }
                    int n2;
                    if (s != null) {
                        n2 = 1;
                    }
                    else {
                        n2 = 0;
                    }
                    final String[] array = new String[n2 + 1];
                    array[0] = lowerCase;
                    if (s != null) {
                        array[1] = s;
                    }
                    final ArrayList<ArrayList<ThemeDescription>> list = new ArrayList<ArrayList<ThemeDescription>>();
                    final ArrayList<CharSequence> list2 = new ArrayList<CharSequence>();
                    for (int size = EditorAlert.this.listAdapter.items.size(), i = 0; i < size; ++i) {
                        final ArrayList<ThemeDescription> e = EditorAlert.this.listAdapter.items.get(i);
                        final String lowerCase2 = e.get(0).getCurrentKey().toLowerCase();
                        for (final String s2 : array) {
                            if (lowerCase2.contains(s2)) {
                                list.add(e);
                                list2.add(this.generateSearchName(lowerCase2, s2));
                                break;
                            }
                        }
                    }
                    this.updateSearchResults(list, list2, n);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            
            private void updateSearchResults(final ArrayList<ArrayList<ThemeDescription>> list, final ArrayList<CharSequence> list2, final int n) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$ThemeEditorView$EditorAlert$SearchAdapter$p_mG9tNYo33F1pFcBkO4OWkissQ(this, n, list, list2));
            }
            
            public CharSequence generateSearchName(String lowerCase, final String str) {
                if (TextUtils.isEmpty((CharSequence)lowerCase)) {
                    return "";
                }
                final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                final String trim = lowerCase.trim();
                lowerCase = trim.toLowerCase();
                int beginIndex = 0;
                while (true) {
                    final int index = lowerCase.indexOf(str, beginIndex);
                    if (index == -1) {
                        break;
                    }
                    final int b = str.length() + index;
                    if (beginIndex != 0 && beginIndex != index + 1) {
                        spannableStringBuilder.append((CharSequence)trim.substring(beginIndex, index));
                    }
                    else if (beginIndex == 0 && index != 0) {
                        spannableStringBuilder.append((CharSequence)trim.substring(0, index));
                    }
                    final String substring = trim.substring(index, Math.min(trim.length(), b));
                    if (substring.startsWith(" ")) {
                        spannableStringBuilder.append((CharSequence)" ");
                    }
                    final String trim2 = substring.trim();
                    final int length = spannableStringBuilder.length();
                    spannableStringBuilder.append((CharSequence)trim2);
                    spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(-11697229), length, trim2.length() + length, 33);
                    beginIndex = b;
                }
                if (beginIndex != -1 && beginIndex < trim.length()) {
                    spannableStringBuilder.append((CharSequence)trim.substring(beginIndex));
                }
                return (CharSequence)spannableStringBuilder;
            }
            
            public ArrayList<ThemeDescription> getItem(final int index) {
                if (index >= 0 && index < this.searchResult.size()) {
                    return this.searchResult.get(index);
                }
                return null;
            }
            
            @Override
            public int getItemCount() {
                int n;
                if (this.searchResult.isEmpty()) {
                    n = 0;
                }
                else {
                    n = this.searchResult.size() + 1;
                }
                return n;
            }
            
            @Override
            public int getItemViewType(final int n) {
                if (n == 0) {
                    return 1;
                }
                return 0;
            }
            
            @Override
            public boolean isEnabled(final ViewHolder viewHolder) {
                return true;
            }
            
            @Override
            public void onBindViewHolder(final ViewHolder viewHolder, int setColor) {
                if (viewHolder.getItemViewType() == 0) {
                    final ArrayList<ArrayList<ThemeDescription>> searchResult = this.searchResult;
                    final int n = setColor - 1;
                    final ArrayList<ThemeDescription> list = searchResult.get(n);
                    setColor = 0;
                    final ThemeDescription themeDescription = list.get(0);
                    if (!themeDescription.getCurrentKey().equals("chat_wallpaper")) {
                        setColor = themeDescription.getSetColor();
                    }
                    ((TextColorThemeCell)viewHolder.itemView).setTextAndColor(this.searchNames.get(n), setColor);
                }
            }
            
            @Override
            public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
                Object o;
                if (n != 0) {
                    o = new View(this.context);
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
                }
                else {
                    o = new TextColorThemeCell(this.context);
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
                }
                return new RecyclerListView.Holder((View)o);
            }
            
            public void searchDialogs(final String lastSearchText) {
                if (lastSearchText != null && lastSearchText.equals(this.lastSearchText)) {
                    return;
                }
                this.lastSearchText = lastSearchText;
                if (this.searchRunnable != null) {
                    Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                    this.searchRunnable = null;
                }
                if (lastSearchText != null && lastSearchText.length() != 0) {
                    final int lastSearchId = this.lastSearchId + 1;
                    this.lastSearchId = lastSearchId;
                    this.searchRunnable = new _$$Lambda$ThemeEditorView$EditorAlert$SearchAdapter$QO0_7n9Zk7XEogLqnxXJbl4gmjs(this, lastSearchText, lastSearchId);
                    Utilities.searchQueue.postRunnable(this.searchRunnable, 300L);
                }
                else {
                    this.searchResult.clear();
                    final EditorAlert this$1 = EditorAlert.this;
                    this$1.topBeforeSwitch = this$1.getCurrentTop();
                    this.lastSearchId = -1;
                    this.notifyDataSetChanged();
                }
            }
        }
        
        private class SearchField extends FrameLayout
        {
            private View backgroundView;
            private ImageView clearSearchImageView;
            private CloseProgressDrawable2 progressDrawable;
            private View searchBackground;
            private EditTextBoldCursor searchEditText;
            private ImageView searchIconImageView;
            
            public SearchField(final Context context) {
                super(context);
                (this.searchBackground = new View(context)).setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), -854795));
                this.addView(this.searchBackground, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 11.0f, 14.0f, 0.0f));
                (this.searchIconImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
                this.searchIconImageView.setImageResource(2131165834);
                this.searchIconImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(-6182737, PorterDuff$Mode.MULTIPLY));
                this.addView((View)this.searchIconImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 51, 16.0f, 11.0f, 0.0f, 0.0f));
                (this.clearSearchImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
                this.clearSearchImageView.setImageDrawable((Drawable)(this.progressDrawable = new CloseProgressDrawable2()));
                this.progressDrawable.setSide(AndroidUtilities.dp(7.0f));
                this.clearSearchImageView.setScaleX(0.1f);
                this.clearSearchImageView.setScaleY(0.1f);
                this.clearSearchImageView.setAlpha(0.0f);
                this.clearSearchImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(-6182737, PorterDuff$Mode.MULTIPLY));
                this.addView((View)this.clearSearchImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 53, 14.0f, 11.0f, 14.0f, 0.0f));
                this.clearSearchImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$ThemeEditorView$EditorAlert$SearchField$oyMCfmJu6kX9C4_2783iB9VXPBE(this));
                (this.searchEditText = new EditTextBoldCursor(context) {
                    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
                        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
                        obtain.setLocation(obtain.getRawX(), obtain.getRawY() - EditorAlert.this.containerView.getTranslationY());
                        EditorAlert.this.listView.dispatchTouchEvent(obtain);
                        obtain.recycle();
                        return super.dispatchTouchEvent(motionEvent);
                    }
                }).setTextSize(1, 16.0f);
                this.searchEditText.setHintTextColor(-6774617);
                this.searchEditText.setTextColor(-14540254);
                this.searchEditText.setBackgroundDrawable((Drawable)null);
                this.searchEditText.setPadding(0, 0, 0, 0);
                this.searchEditText.setMaxLines(1);
                this.searchEditText.setLines(1);
                this.searchEditText.setSingleLine(true);
                this.searchEditText.setImeOptions(268435459);
                this.searchEditText.setHint((CharSequence)LocaleController.getString("Search", 2131560640));
                this.searchEditText.setCursorColor(-11491093);
                this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0f));
                this.searchEditText.setCursorWidth(1.5f);
                this.addView((View)this.searchEditText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 40.0f, 51, 54.0f, 9.0f, 46.0f, 0.0f));
                this.searchEditText.addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        final int length = SearchField.this.searchEditText.length();
                        boolean b = true;
                        final boolean b2 = length > 0;
                        final float alpha = SearchField.this.clearSearchImageView.getAlpha();
                        float n = 0.0f;
                        if (alpha == 0.0f) {
                            b = false;
                        }
                        if (b2 != b) {
                            final ViewPropertyAnimator animate = SearchField.this.clearSearchImageView.animate();
                            final float n2 = 1.0f;
                            if (b2) {
                                n = 1.0f;
                            }
                            final ViewPropertyAnimator setDuration = animate.alpha(n).setDuration(150L);
                            float n3;
                            if (b2) {
                                n3 = 1.0f;
                            }
                            else {
                                n3 = 0.1f;
                            }
                            final ViewPropertyAnimator scaleX = setDuration.scaleX(n3);
                            float n4;
                            if (b2) {
                                n4 = n2;
                            }
                            else {
                                n4 = 0.1f;
                            }
                            scaleX.scaleY(n4).start();
                        }
                        final String string = SearchField.this.searchEditText.getText().toString();
                        if (string.length() != 0) {
                            if (EditorAlert.this.searchEmptyView != null) {
                                EditorAlert.this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131559943));
                            }
                        }
                        else if (EditorAlert.this.listView.getAdapter() != EditorAlert.this.listAdapter) {
                            final int access$600 = EditorAlert.this.getCurrentTop();
                            EditorAlert.this.searchEmptyView.setText(LocaleController.getString("NoChats", 2131559918));
                            EditorAlert.this.searchEmptyView.showTextView();
                            EditorAlert.this.listView.setAdapter(EditorAlert.this.listAdapter);
                            EditorAlert.this.listAdapter.notifyDataSetChanged();
                            if (access$600 > 0) {
                                EditorAlert.this.layoutManager.scrollToPositionWithOffset(0, -access$600);
                            }
                        }
                        if (EditorAlert.this.searchAdapter != null) {
                            EditorAlert.this.searchAdapter.searchDialogs(string);
                        }
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
                this.searchEditText.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ThemeEditorView$EditorAlert$SearchField$7PNsSR7Iu_AOTIs_B2ibPMDGudM(this));
            }
            
            public void hideKeyboard() {
                AndroidUtilities.hideKeyboard((View)this.searchEditText);
            }
            
            public void requestDisallowInterceptTouchEvent(final boolean b) {
                super.requestDisallowInterceptTouchEvent(b);
            }
            
            public void showKeyboard() {
                this.searchEditText.requestFocus();
                AndroidUtilities.showKeyboard((View)this.searchEditText);
            }
        }
    }
}
