// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.FileLog;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.ApplicationLoader;
import android.graphics.Path$Direction;
import android.graphics.Path;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint;
import android.annotation.TargetApi;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.os.Build$VERSION;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MediaController;
import android.view.MotionEvent;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import android.content.Context;
import androidx.annotation.Keep;
import android.view.ViewGroup$LayoutParams;
import android.util.Property;
import android.content.SharedPreferences$Editor;
import java.util.Collection;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import android.animation.ObjectAnimator;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import android.widget.FrameLayout;
import android.view.WindowManager;
import android.view.WindowManager$LayoutParams;
import android.view.TextureView;
import android.graphics.RectF;
import android.content.SharedPreferences;
import android.app.Activity;
import android.widget.ImageView;
import android.animation.AnimatorSet;
import android.view.animation.DecelerateInterpolator;
import android.graphics.Bitmap;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import android.annotation.SuppressLint;
import org.telegram.messenger.NotificationCenter;

public class PipRoundVideoView implements NotificationCenterDelegate
{
    @SuppressLint({ "StaticFieldLeak" })
    private static PipRoundVideoView instance;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private Bitmap bitmap;
    private int currentAccount;
    private DecelerateInterpolator decelerateInterpolator;
    private AnimatorSet hideShowAnimation;
    private ImageView imageView;
    private Runnable onCloseRunnable;
    private Activity parentActivity;
    private SharedPreferences preferences;
    private RectF rect;
    private TextureView textureView;
    private int videoHeight;
    private int videoWidth;
    private WindowManager$LayoutParams windowLayoutParams;
    private WindowManager windowManager;
    private FrameLayout windowView;
    
    public PipRoundVideoView() {
        this.rect = new RectF();
    }
    
    private void animateToBoundsMaybe() {
        final int sideCoord = getSideCoord(true, 0, 0.0f, this.videoWidth);
        final int sideCoord2 = getSideCoord(true, 1, 0.0f, this.videoWidth);
        final int sideCoord3 = getSideCoord(false, 0, 0.0f, this.videoHeight);
        final int sideCoord4 = getSideCoord(false, 1, 0.0f, this.videoHeight);
        final SharedPreferences$Editor edit = this.preferences.edit();
        final int dp = AndroidUtilities.dp(20.0f);
        ArrayList<ObjectAnimator> list = null;
        boolean b = false;
        Label_0457: {
            Label_0455: {
                if (Math.abs(sideCoord - this.windowLayoutParams.x) > dp) {
                    final int x = this.windowLayoutParams.x;
                    if (x >= 0 || x <= -this.videoWidth / 4) {
                        Label_0378: {
                            if (Math.abs(sideCoord2 - this.windowLayoutParams.x) > dp) {
                                final int x2 = this.windowLayoutParams.x;
                                final int x3 = AndroidUtilities.displaySize.x;
                                final int videoWidth = this.videoWidth;
                                if (x2 <= x3 - videoWidth || x2 >= x3 - videoWidth / 4 * 3) {
                                    if (this.windowView.getAlpha() != 1.0f) {
                                        list = new ArrayList<ObjectAnimator>();
                                        if (this.windowLayoutParams.x < 0) {
                                            list.add(ObjectAnimator.ofInt((Object)this, "x", new int[] { -this.videoWidth }));
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
                        PipRoundVideoView.this.close(false);
                        if (PipRoundVideoView.this.onCloseRunnable != null) {
                            PipRoundVideoView.this.onCloseRunnable.run();
                        }
                    }
                });
            }
            set.playTogether((Collection)list2);
            set.start();
        }
    }
    
    public static PipRoundVideoView getInstance() {
        return PipRoundVideoView.instance;
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
    
    private void runShowHideAnimation(final boolean b) {
        final AnimatorSet hideShowAnimation = this.hideShowAnimation;
        if (hideShowAnimation != null) {
            hideShowAnimation.cancel();
        }
        this.hideShowAnimation = new AnimatorSet();
        final AnimatorSet hideShowAnimation2 = this.hideShowAnimation;
        final FrameLayout windowView = this.windowView;
        final Property alpha = View.ALPHA;
        final float n = 1.0f;
        float n2;
        if (b) {
            n2 = 1.0f;
        }
        else {
            n2 = 0.0f;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)windowView, alpha, new float[] { n2 });
        final FrameLayout windowView2 = this.windowView;
        final Property scale_X = View.SCALE_X;
        float n3;
        if (b) {
            n3 = 1.0f;
        }
        else {
            n3 = 0.8f;
        }
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)windowView2, scale_X, new float[] { n3 });
        final FrameLayout windowView3 = this.windowView;
        final Property scale_Y = View.SCALE_Y;
        float n4;
        if (b) {
            n4 = n;
        }
        else {
            n4 = 0.8f;
        }
        hideShowAnimation2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ObjectAnimator.ofFloat((Object)windowView3, scale_Y, new float[] { n4 }) });
        this.hideShowAnimation.setDuration(150L);
        if (this.decelerateInterpolator == null) {
            this.decelerateInterpolator = new DecelerateInterpolator();
        }
        this.hideShowAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                if (animator.equals(PipRoundVideoView.this.hideShowAnimation)) {
                    PipRoundVideoView.this.hideShowAnimation = null;
                }
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (animator.equals(PipRoundVideoView.this.hideShowAnimation)) {
                    if (!b) {
                        PipRoundVideoView.this.close(false);
                    }
                    PipRoundVideoView.this.hideShowAnimation = null;
                }
            }
        });
        this.hideShowAnimation.setInterpolator((TimeInterpolator)this.decelerateInterpolator);
        this.hideShowAnimation.start();
    }
    
    public void close(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifeq            123
        //     4: aload_0        
        //     5: getfield        org/telegram/ui/Components/PipRoundVideoView.textureView:Landroid/view/TextureView;
        //     8: astore_2       
        //     9: aload_2        
        //    10: ifnull          193
        //    13: aload_2        
        //    14: invokevirtual   android/view/TextureView.getParent:()Landroid/view/ViewParent;
        //    17: ifnull          193
        //    20: aload_0        
        //    21: getfield        org/telegram/ui/Components/PipRoundVideoView.textureView:Landroid/view/TextureView;
        //    24: invokevirtual   android/view/TextureView.getWidth:()I
        //    27: ifle            64
        //    30: aload_0        
        //    31: getfield        org/telegram/ui/Components/PipRoundVideoView.textureView:Landroid/view/TextureView;
        //    34: invokevirtual   android/view/TextureView.getHeight:()I
        //    37: ifle            64
        //    40: aload_0        
        //    41: aload_0        
        //    42: getfield        org/telegram/ui/Components/PipRoundVideoView.textureView:Landroid/view/TextureView;
        //    45: invokevirtual   android/view/TextureView.getWidth:()I
        //    48: aload_0        
        //    49: getfield        org/telegram/ui/Components/PipRoundVideoView.textureView:Landroid/view/TextureView;
        //    52: invokevirtual   android/view/TextureView.getHeight:()I
        //    55: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
        //    58: invokestatic    org/telegram/messenger/Bitmaps.createBitmap:(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
        //    61: putfield        org/telegram/ui/Components/PipRoundVideoView.bitmap:Landroid/graphics/Bitmap;
        //    64: aload_0        
        //    65: getfield        org/telegram/ui/Components/PipRoundVideoView.textureView:Landroid/view/TextureView;
        //    68: aload_0        
        //    69: getfield        org/telegram/ui/Components/PipRoundVideoView.bitmap:Landroid/graphics/Bitmap;
        //    72: invokevirtual   android/view/TextureView.getBitmap:(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;
        //    75: pop            
        //    76: goto            85
        //    79: astore_2       
        //    80: aload_0        
        //    81: aconst_null    
        //    82: putfield        org/telegram/ui/Components/PipRoundVideoView.bitmap:Landroid/graphics/Bitmap;
        //    85: aload_0        
        //    86: getfield        org/telegram/ui/Components/PipRoundVideoView.imageView:Landroid/widget/ImageView;
        //    89: aload_0        
        //    90: getfield        org/telegram/ui/Components/PipRoundVideoView.bitmap:Landroid/graphics/Bitmap;
        //    93: invokevirtual   android/widget/ImageView.setImageBitmap:(Landroid/graphics/Bitmap;)V
        //    96: aload_0        
        //    97: getfield        org/telegram/ui/Components/PipRoundVideoView.aspectRatioFrameLayout:Lcom/google/android/exoplayer2/ui/AspectRatioFrameLayout;
        //   100: aload_0        
        //   101: getfield        org/telegram/ui/Components/PipRoundVideoView.textureView:Landroid/view/TextureView;
        //   104: invokevirtual   android/widget/FrameLayout.removeView:(Landroid/view/View;)V
        //   107: aload_0        
        //   108: getfield        org/telegram/ui/Components/PipRoundVideoView.imageView:Landroid/widget/ImageView;
        //   111: iconst_0       
        //   112: invokevirtual   android/widget/ImageView.setVisibility:(I)V
        //   115: aload_0        
        //   116: iconst_0       
        //   117: invokespecial   org/telegram/ui/Components/PipRoundVideoView.runShowHideAnimation:(Z)V
        //   120: goto            193
        //   123: aload_0        
        //   124: getfield        org/telegram/ui/Components/PipRoundVideoView.bitmap:Landroid/graphics/Bitmap;
        //   127: ifnull          150
        //   130: aload_0        
        //   131: getfield        org/telegram/ui/Components/PipRoundVideoView.imageView:Landroid/widget/ImageView;
        //   134: aconst_null    
        //   135: invokevirtual   android/widget/ImageView.setImageDrawable:(Landroid/graphics/drawable/Drawable;)V
        //   138: aload_0        
        //   139: getfield        org/telegram/ui/Components/PipRoundVideoView.bitmap:Landroid/graphics/Bitmap;
        //   142: invokevirtual   android/graphics/Bitmap.recycle:()V
        //   145: aload_0        
        //   146: aconst_null    
        //   147: putfield        org/telegram/ui/Components/PipRoundVideoView.bitmap:Landroid/graphics/Bitmap;
        //   150: aload_0        
        //   151: getfield        org/telegram/ui/Components/PipRoundVideoView.windowManager:Landroid/view/WindowManager;
        //   154: aload_0        
        //   155: getfield        org/telegram/ui/Components/PipRoundVideoView.windowView:Landroid/widget/FrameLayout;
        //   158: invokeinterface android/view/WindowManager.removeView:(Landroid/view/View;)V
        //   163: getstatic       org/telegram/ui/Components/PipRoundVideoView.instance:Lorg/telegram/ui/Components/PipRoundVideoView;
        //   166: aload_0        
        //   167: if_acmpne       174
        //   170: aconst_null    
        //   171: putstatic       org/telegram/ui/Components/PipRoundVideoView.instance:Lorg/telegram/ui/Components/PipRoundVideoView;
        //   174: aload_0        
        //   175: aconst_null    
        //   176: putfield        org/telegram/ui/Components/PipRoundVideoView.parentActivity:Landroid/app/Activity;
        //   179: aload_0        
        //   180: getfield        org/telegram/ui/Components/PipRoundVideoView.currentAccount:I
        //   183: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        //   186: aload_0        
        //   187: getstatic       org/telegram/messenger/NotificationCenter.messagePlayingProgressDidChanged:I
        //   190: invokevirtual   org/telegram/messenger/NotificationCenter.removeObserver:(Ljava/lang/Object;I)V
        //   193: return         
        //   194: astore_2       
        //   195: goto            107
        //   198: astore_2       
        //   199: goto            163
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  64     76     79     85     Ljava/lang/Throwable;
        //  96     107    194    198    Ljava/lang/Exception;
        //  150    163    198    202    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0150:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.messagePlayingProgressDidChanged) {
            final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
            if (aspectRatioFrameLayout != null) {
                aspectRatioFrameLayout.invalidate();
            }
        }
    }
    
    public TextureView getTextureView() {
        return this.textureView;
    }
    
    public int getX() {
        return this.windowLayoutParams.x;
    }
    
    public int getY() {
        return this.windowLayoutParams.y;
    }
    
    public void onConfigurationChanged() {
        final int int1 = this.preferences.getInt("sidex", 1);
        final int int2 = this.preferences.getInt("sidey", 0);
        final float float1 = this.preferences.getFloat("px", 0.0f);
        final float float2 = this.preferences.getFloat("py", 0.0f);
        this.windowLayoutParams.x = getSideCoord(true, int1, float1, this.videoWidth);
        this.windowLayoutParams.y = getSideCoord(false, int2, float2, this.videoHeight);
        this.windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
    }
    
    @Keep
    public void setX(final int x) {
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.x = x;
        try {
            this.windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)windowLayoutParams);
        }
        catch (Exception ex) {}
    }
    
    @Keep
    public void setY(final int y) {
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.y = y;
        try {
            this.windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)windowLayoutParams);
        }
        catch (Exception ex) {}
    }
    
    public void show(final Activity parentActivity, final Runnable onCloseRunnable) {
        if (parentActivity == null) {
            return;
        }
        PipRoundVideoView.instance = this;
        this.onCloseRunnable = onCloseRunnable;
        (this.windowView = new FrameLayout(parentActivity) {
            private boolean dragging;
            private boolean startDragging;
            private float startX;
            private float startY;
            
            protected void onDraw(final Canvas canvas) {
                final Drawable chat_roundVideoShadow = Theme.chat_roundVideoShadow;
                if (chat_roundVideoShadow != null) {
                    chat_roundVideoShadow.setAlpha((int)(this.getAlpha() * 255.0f));
                    Theme.chat_roundVideoShadow.setBounds(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(125.0f), AndroidUtilities.dp(125.0f));
                    Theme.chat_roundVideoShadow.draw(canvas);
                    Theme.chat_docBackPaint.setColor(Theme.getColor("chat_inBubble"));
                    Theme.chat_docBackPaint.setAlpha((int)(this.getAlpha() * 255.0f));
                    canvas.drawCircle((float)AndroidUtilities.dp(63.0f), (float)AndroidUtilities.dp(63.0f), (float)AndroidUtilities.dp(59.5f), Theme.chat_docBackPaint);
                }
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    this.startX = motionEvent.getRawX();
                    this.startY = motionEvent.getRawY();
                    this.startDragging = true;
                }
                return true;
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (!this.startDragging && !this.dragging) {
                    return false;
                }
                final float rawX = motionEvent.getRawX();
                final float rawY = motionEvent.getRawY();
                if (motionEvent.getAction() == 2) {
                    final float a = rawX - this.startX;
                    final float a2 = rawY - this.startY;
                    if (this.startDragging) {
                        if (Math.abs(a) >= AndroidUtilities.getPixelsInCM(0.3f, true) || Math.abs(a2) >= AndroidUtilities.getPixelsInCM(0.3f, false)) {
                            this.dragging = true;
                            this.startDragging = false;
                        }
                    }
                    else if (this.dragging) {
                        final WindowManager$LayoutParams access$000 = PipRoundVideoView.this.windowLayoutParams;
                        access$000.x += (int)a;
                        final WindowManager$LayoutParams access$2 = PipRoundVideoView.this.windowLayoutParams;
                        access$2.y += (int)a2;
                        final int n = PipRoundVideoView.this.videoWidth / 2;
                        final int x = PipRoundVideoView.this.windowLayoutParams.x;
                        final int x2 = -n;
                        if (x < x2) {
                            PipRoundVideoView.this.windowLayoutParams.x = x2;
                        }
                        else if (PipRoundVideoView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - PipRoundVideoView.this.windowLayoutParams.width + n) {
                            PipRoundVideoView.this.windowLayoutParams.x = AndroidUtilities.displaySize.x - PipRoundVideoView.this.windowLayoutParams.width + n;
                        }
                        final int x3 = PipRoundVideoView.this.windowLayoutParams.x;
                        float alpha = 1.0f;
                        if (x3 < 0) {
                            alpha = 1.0f + PipRoundVideoView.this.windowLayoutParams.x / (float)n * 0.5f;
                        }
                        else if (PipRoundVideoView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - PipRoundVideoView.this.windowLayoutParams.width) {
                            alpha = 1.0f - (PipRoundVideoView.this.windowLayoutParams.x - AndroidUtilities.displaySize.x + PipRoundVideoView.this.windowLayoutParams.width) / (float)n * 0.5f;
                        }
                        if (PipRoundVideoView.this.windowView.getAlpha() != alpha) {
                            PipRoundVideoView.this.windowView.setAlpha(alpha);
                        }
                        if (PipRoundVideoView.this.windowLayoutParams.y < 0) {
                            PipRoundVideoView.this.windowLayoutParams.y = 0;
                        }
                        else if (PipRoundVideoView.this.windowLayoutParams.y > AndroidUtilities.displaySize.y - PipRoundVideoView.this.windowLayoutParams.height + 0) {
                            PipRoundVideoView.this.windowLayoutParams.y = AndroidUtilities.displaySize.y - PipRoundVideoView.this.windowLayoutParams.height + 0;
                        }
                        PipRoundVideoView.this.windowManager.updateViewLayout((View)PipRoundVideoView.this.windowView, (ViewGroup$LayoutParams)PipRoundVideoView.this.windowLayoutParams);
                        this.startX = rawX;
                        this.startY = rawY;
                    }
                }
                else if (motionEvent.getAction() == 1) {
                    if (this.startDragging && !this.dragging) {
                        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                        if (playingMessageObject != null) {
                            if (MediaController.getInstance().isMessagePaused()) {
                                MediaController.getInstance().playMessage(playingMessageObject);
                            }
                            else {
                                MediaController.getInstance().pauseMessage(playingMessageObject);
                            }
                        }
                    }
                    this.dragging = false;
                    this.startDragging = false;
                    PipRoundVideoView.this.animateToBoundsMaybe();
                }
                return true;
            }
            
            public void requestDisallowInterceptTouchEvent(final boolean b) {
                super.requestDisallowInterceptTouchEvent(b);
            }
        }).setWillNotDraw(false);
        this.videoWidth = AndroidUtilities.dp(126.0f);
        this.videoHeight = AndroidUtilities.dp(126.0f);
        if (Build$VERSION.SDK_INT >= 21) {
            (this.aspectRatioFrameLayout = new AspectRatioFrameLayout(parentActivity) {
                protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                    final boolean drawChild = super.drawChild(canvas, view, n);
                    if (view == PipRoundVideoView.this.textureView) {
                        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                        if (playingMessageObject != null) {
                            PipRoundVideoView.this.rect.set(AndroidUtilities.dpf2(1.5f), AndroidUtilities.dpf2(1.5f), this.getMeasuredWidth() - AndroidUtilities.dpf2(1.5f), this.getMeasuredHeight() - AndroidUtilities.dpf2(1.5f));
                            canvas.drawArc(PipRoundVideoView.this.rect, -90.0f, playingMessageObject.audioProgress * 360.0f, false, Theme.chat_radialProgressPaint);
                        }
                    }
                    return drawChild;
                }
            }).setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                @TargetApi(21)
                public void getOutline(final View view, final Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(120.0f), AndroidUtilities.dp(120.0f));
                }
            });
            this.aspectRatioFrameLayout.setClipToOutline(true);
        }
        else {
            final Paint paint = new Paint(1);
            paint.setColor(-16777216);
            paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
            (this.aspectRatioFrameLayout = new AspectRatioFrameLayout(parentActivity) {
                private Path aspectPath = new Path();
                
                protected void dispatchDraw(final Canvas canvas) {
                    super.dispatchDraw(canvas);
                    canvas.drawPath(this.aspectPath, paint);
                }
                
                protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                    boolean drawChild;
                    try {
                        drawChild = super.drawChild(canvas, view, n);
                    }
                    catch (Throwable t) {
                        drawChild = false;
                    }
                    if (view == PipRoundVideoView.this.textureView) {
                        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                        if (playingMessageObject != null) {
                            PipRoundVideoView.this.rect.set(AndroidUtilities.dpf2(1.5f), AndroidUtilities.dpf2(1.5f), this.getMeasuredWidth() - AndroidUtilities.dpf2(1.5f), this.getMeasuredHeight() - AndroidUtilities.dpf2(1.5f));
                            canvas.drawArc(PipRoundVideoView.this.rect, -90.0f, playingMessageObject.audioProgress * 360.0f, false, Theme.chat_radialProgressPaint);
                        }
                    }
                    return drawChild;
                }
                
                protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
                    super.onSizeChanged(n, n2, n3, n4);
                    this.aspectPath.reset();
                    final Path aspectPath = this.aspectPath;
                    final float n5 = (float)(n / 2);
                    aspectPath.addCircle(n5, (float)(n2 / 2), n5, Path$Direction.CW);
                    this.aspectPath.toggleInverseFillType();
                }
            }).setLayerType(2, (Paint)null);
        }
        this.aspectRatioFrameLayout.setAspectRatio(1.0f, 0);
        this.windowView.addView((View)this.aspectRatioFrameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(120, 120.0f, 51, 3.0f, 3.0f, 0.0f, 0.0f));
        this.windowView.setAlpha(1.0f);
        this.windowView.setScaleX(0.8f);
        this.windowView.setScaleY(0.8f);
        this.textureView = new TextureView((Context)parentActivity);
        this.aspectRatioFrameLayout.addView((View)this.textureView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.imageView = new ImageView((Context)parentActivity);
        this.aspectRatioFrameLayout.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.imageView.setVisibility(4);
        this.windowManager = (WindowManager)parentActivity.getSystemService("window");
        this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("pipconfig", 0);
        final int int1 = this.preferences.getInt("sidex", 1);
        final int int2 = this.preferences.getInt("sidey", 0);
        final float float1 = this.preferences.getFloat("px", 0.0f);
        final float float2 = this.preferences.getFloat("py", 0.0f);
        try {
            this.windowLayoutParams = new WindowManager$LayoutParams();
            this.windowLayoutParams.width = this.videoWidth;
            this.windowLayoutParams.height = this.videoHeight;
            this.windowLayoutParams.x = getSideCoord(true, int1, float1, this.videoWidth);
            this.windowLayoutParams.y = getSideCoord(false, int2, float2, this.videoHeight);
            this.windowLayoutParams.format = -3;
            this.windowLayoutParams.gravity = 51;
            this.windowLayoutParams.type = 99;
            this.windowLayoutParams.flags = 16777736;
            this.windowManager.addView((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
            this.parentActivity = parentActivity;
            this.currentAccount = UserConfig.selectedAccount;
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
            this.runShowHideAnimation(true);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void showTemporary(final boolean b) {
        final AnimatorSet hideShowAnimation = this.hideShowAnimation;
        if (hideShowAnimation != null) {
            hideShowAnimation.cancel();
        }
        this.hideShowAnimation = new AnimatorSet();
        final AnimatorSet hideShowAnimation2 = this.hideShowAnimation;
        final FrameLayout windowView = this.windowView;
        final Property alpha = View.ALPHA;
        final float n = 1.0f;
        float n2;
        if (b) {
            n2 = 1.0f;
        }
        else {
            n2 = 0.0f;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)windowView, alpha, new float[] { n2 });
        final FrameLayout windowView2 = this.windowView;
        final Property scale_X = View.SCALE_X;
        float n3;
        if (b) {
            n3 = 1.0f;
        }
        else {
            n3 = 0.8f;
        }
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)windowView2, scale_X, new float[] { n3 });
        final FrameLayout windowView3 = this.windowView;
        final Property scale_Y = View.SCALE_Y;
        float n4;
        if (b) {
            n4 = n;
        }
        else {
            n4 = 0.8f;
        }
        hideShowAnimation2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ObjectAnimator.ofFloat((Object)windowView3, scale_Y, new float[] { n4 }) });
        this.hideShowAnimation.setDuration(150L);
        if (this.decelerateInterpolator == null) {
            this.decelerateInterpolator = new DecelerateInterpolator();
        }
        this.hideShowAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (animator.equals(PipRoundVideoView.this.hideShowAnimation)) {
                    PipRoundVideoView.this.hideShowAnimation = null;
                }
            }
        });
        this.hideShowAnimation.setInterpolator((TimeInterpolator)this.decelerateInterpolator);
        this.hideShowAnimation.start();
    }
}
