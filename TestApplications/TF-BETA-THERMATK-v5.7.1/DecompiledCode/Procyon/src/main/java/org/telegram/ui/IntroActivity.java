// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.ViewGroup;
import android.os.Looper;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import org.telegram.messenger.FileLog;
import android.opengl.GLUtils;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGL10;
import org.telegram.messenger.DispatchQueue;
import android.graphics.drawable.Drawable;
import android.graphics.Shader$TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.view.View$OnLongClickListener;
import org.telegram.messenger.BuildVars;
import android.view.View$OnClickListener;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Build$VERSION;
import org.telegram.messenger.Intro;
import androidx.viewpager.widget.PagerAdapter;
import android.graphics.SurfaceTexture;
import android.view.TextureView$SurfaceTextureListener;
import android.view.TextureView;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import androidx.viewpager.widget.ViewPager;
import android.widget.TextView;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Components.BottomPagesView;
import org.telegram.messenger.NotificationCenter;
import android.app.Activity;

public class IntroActivity extends Activity implements NotificationCenterDelegate
{
    private BottomPagesView bottomPages;
    private int currentAccount;
    private long currentDate;
    private int currentViewPagerPage;
    private boolean destroyed;
    private boolean dragging;
    private EGLThread eglThread;
    private boolean justCreated;
    private boolean justEndDragging;
    private int lastPage;
    private LocaleController.LocaleInfo localeInfo;
    private String[] messages;
    private int startDragX;
    private boolean startPressed;
    private TextView textView;
    private String[] titles;
    private ViewPager viewPager;
    
    public IntroActivity() {
        this.currentAccount = UserConfig.selectedAccount;
        this.lastPage = 0;
        this.justCreated = false;
        this.startPressed = false;
    }
    
    private void checkContinueText() {
        final LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
        final String suggestedLangCode = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
        final boolean contains = suggestedLangCode.contains("-");
        int index = 0;
        String anObject;
        if (contains) {
            anObject = suggestedLangCode.split("-")[0];
        }
        else {
            anObject = suggestedLangCode;
        }
        final String localeAlias = LocaleController.getLocaleAlias(anObject);
        LocaleController.LocaleInfo localeInfo = null;
        LocaleController.LocaleInfo localeInfo2 = null;
        LocaleController.LocaleInfo localeInfo3;
        LocaleController.LocaleInfo localeInfo4;
        while (true) {
            localeInfo3 = localeInfo;
            localeInfo4 = localeInfo2;
            if (index >= LocaleController.getInstance().languages.size()) {
                break;
            }
            final LocaleController.LocaleInfo localeInfo5 = LocaleController.getInstance().languages.get(index);
            if (localeInfo5.shortName.equals("en")) {
                localeInfo = localeInfo5;
            }
            if (localeInfo5.shortName.replace("_", "-").equals(suggestedLangCode) || localeInfo5.shortName.equals(anObject) || localeInfo5.shortName.equals(localeAlias)) {
                localeInfo2 = localeInfo5;
            }
            if (localeInfo != null && localeInfo2 != null) {
                localeInfo3 = localeInfo;
                localeInfo4 = localeInfo2;
                break;
            }
            ++index;
        }
        if (localeInfo3 != null && localeInfo4 != null) {
            if (localeInfo3 != localeInfo4) {
                final TLRPC.TL_langpack_getStrings tl_langpack_getStrings = new TLRPC.TL_langpack_getStrings();
                if (localeInfo4 != currentLocaleInfo) {
                    tl_langpack_getStrings.lang_code = localeInfo4.getLangCode();
                    this.localeInfo = localeInfo4;
                }
                else {
                    tl_langpack_getStrings.lang_code = localeInfo3.getLangCode();
                    this.localeInfo = localeInfo3;
                }
                tl_langpack_getStrings.keys.add("ContinueOnThisLanguage");
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_langpack_getStrings, new _$$Lambda$IntroActivity$v08WUA33DIz9dwdLeC_Z0gtPf_4(this, suggestedLangCode), 8);
            }
        }
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.suggestedLangpack) {
            this.checkContinueText();
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        this.setTheme(2131624206);
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        MessagesController.getGlobalMainSettings().edit().putLong("intro_crashed_time", System.currentTimeMillis()).commit();
        this.titles = new String[] { LocaleController.getString("Page1Title", 2131560144), LocaleController.getString("Page2Title", 2131560146), LocaleController.getString("Page3Title", 2131560148), LocaleController.getString("Page5Title", 2131560152), LocaleController.getString("Page4Title", 2131560150), LocaleController.getString("Page6Title", 2131560154) };
        this.messages = new String[] { LocaleController.getString("Page1Message", 2131560143), LocaleController.getString("Page2Message", 2131560145), LocaleController.getString("Page3Message", 2131560147), LocaleController.getString("Page5Message", 2131560151), LocaleController.getString("Page4Message", 2131560149), LocaleController.getString("Page6Message", 2131560153) };
        final ScrollView contentView = new ScrollView((Context)this);
        contentView.setFillViewport(true);
        final FrameLayout frameLayout = new FrameLayout((Context)this);
        frameLayout.setBackgroundColor(-1);
        contentView.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createScroll(-1, -2, 51));
        final FrameLayout frameLayout2 = new FrameLayout((Context)this);
        frameLayout.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 78.0f, 0.0f, 0.0f));
        final TextureView textureView = new TextureView((Context)this);
        frameLayout2.addView((View)textureView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(200, 150, 17));
        textureView.setSurfaceTextureListener((TextureView$SurfaceTextureListener)new TextureView$SurfaceTextureListener() {
            public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
                if (IntroActivity.this.eglThread == null && surfaceTexture != null) {
                    final IntroActivity this$0 = IntroActivity.this;
                    this$0.eglThread = this$0.new EGLThread(surfaceTexture);
                    IntroActivity.this.eglThread.setSurfaceTextureSize(n, n2);
                    IntroActivity.this.eglThread.postRunnable(new _$$Lambda$IntroActivity$1$ziO_g9IlcqOuWLJOlD4I6EuEo50(this));
                }
            }
            
            public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
                if (IntroActivity.this.eglThread != null) {
                    IntroActivity.this.eglThread.shutdown();
                    IntroActivity.this.eglThread = null;
                }
                return true;
            }
            
            public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
                if (IntroActivity.this.eglThread != null) {
                    IntroActivity.this.eglThread.setSurfaceTextureSize(n, n2);
                }
            }
            
            public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
            }
        });
        (this.viewPager = new ViewPager((Context)this)).setAdapter(new IntroAdapter());
        this.viewPager.setPageMargin(0);
        this.viewPager.setOffscreenPageLimit(1);
        frameLayout.addView((View)this.viewPager, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
                if (n == 1) {
                    IntroActivity.this.dragging = true;
                    final IntroActivity this$0 = IntroActivity.this;
                    this$0.startDragX = this$0.viewPager.getCurrentItem() * IntroActivity.this.viewPager.getMeasuredWidth();
                }
                else if (n == 0 || n == 2) {
                    if (IntroActivity.this.dragging) {
                        IntroActivity.this.justEndDragging = true;
                        IntroActivity.this.dragging = false;
                    }
                    if (IntroActivity.this.lastPage != IntroActivity.this.viewPager.getCurrentItem()) {
                        final IntroActivity this$2 = IntroActivity.this;
                        this$2.lastPage = this$2.viewPager.getCurrentItem();
                    }
                }
            }
            
            @Override
            public void onPageScrolled(final int n, float n2, final int n3) {
                IntroActivity.this.bottomPages.setPageOffset(n, n2);
                n2 = (float)IntroActivity.this.viewPager.getMeasuredWidth();
                if (n2 == 0.0f) {
                    return;
                }
                Intro.setScrollOffset((n * n2 + n3 - IntroActivity.this.currentViewPagerPage * n2) / n2);
            }
            
            @Override
            public void onPageSelected(final int n) {
                IntroActivity.this.currentViewPagerPage = n;
            }
        });
        final TextView textView = new TextView((Context)this);
        textView.setText((CharSequence)LocaleController.getString("StartMessaging", 2131560804).toUpperCase());
        textView.setGravity(17);
        textView.setTextColor(-1);
        textView.setTextSize(1, 16.0f);
        textView.setBackgroundResource(2131165800);
        if (Build$VERSION.SDK_INT >= 21) {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)textView, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
            stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)textView, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
            textView.setStateListAnimator(stateListAnimator);
        }
        textView.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f));
        frameLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 81, 10.0f, 0.0f, 10.0f, 76.0f));
        textView.setOnClickListener((View$OnClickListener)new _$$Lambda$IntroActivity$Kg_leHKna32gZHR3CZT09b4whCI(this));
        if (BuildVars.DEBUG_VERSION) {
            textView.setOnLongClickListener((View$OnLongClickListener)new _$$Lambda$IntroActivity$iyZ2QRC4zfDIPF0e6T1a3cQJl5Y(this));
        }
        frameLayout.addView((View)(this.bottomPages = new BottomPagesView((Context)this, this.viewPager, 6)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(66, 5.0f, 49, 0.0f, 350.0f, 0.0f, 0.0f));
        (this.textView = new TextView((Context)this)).setTextColor(-15494190);
        this.textView.setGravity(17);
        this.textView.setTextSize(1, 16.0f);
        frameLayout.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 30.0f, 81, 0.0f, 0.0f, 0.0f, 20.0f));
        this.textView.setOnClickListener((View$OnClickListener)new _$$Lambda$IntroActivity$3V_J2Y1xEuzoe0ERCQjOPYz9JM8(this));
        if (AndroidUtilities.isTablet()) {
            final FrameLayout contentView2 = new FrameLayout((Context)this);
            this.setContentView((View)contentView2);
            final ImageView imageView = new ImageView((Context)this);
            final BitmapDrawable backgroundDrawable = (BitmapDrawable)this.getResources().getDrawable(2131165338);
            final Shader$TileMode repeat = Shader$TileMode.REPEAT;
            backgroundDrawable.setTileModeXY(repeat, repeat);
            ((View)imageView).setBackgroundDrawable((Drawable)backgroundDrawable);
            contentView2.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            final FrameLayout frameLayout3 = new FrameLayout((Context)this);
            frameLayout3.setBackgroundResource(2131165328);
            frameLayout3.addView((View)contentView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            contentView2.addView((View)frameLayout3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(498, 528, 17));
        }
        else {
            this.setRequestedOrientation(1);
            this.setContentView((View)contentView);
        }
        LocaleController.getInstance().loadRemoteLanguages(this.currentAccount);
        this.checkContinueText();
        this.justCreated = true;
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        AndroidUtilities.handleProxyIntent(this, this.getIntent());
    }
    
    protected void onDestroy() {
        super.onDestroy();
        this.destroyed = true;
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
        MessagesController.getGlobalMainSettings().edit().putLong("intro_crashed_time", 0L).commit();
    }
    
    protected void onPause() {
        super.onPause();
        AndroidUtilities.unregisterUpdates();
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
    }
    
    protected void onResume() {
        super.onResume();
        if (this.justCreated) {
            if (LocaleController.isRTL) {
                this.viewPager.setCurrentItem(6);
                this.lastPage = 6;
            }
            else {
                this.viewPager.setCurrentItem(0);
                this.lastPage = 0;
            }
            this.justCreated = false;
        }
        AndroidUtilities.checkForCrashes(this);
        AndroidUtilities.checkForUpdates(this);
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
    }
    
    public class EGLThread extends DispatchQueue
    {
        private final int EGL_CONTEXT_CLIENT_VERSION;
        private final int EGL_OPENGL_ES2_BIT;
        private Runnable drawRunnable;
        private EGL10 egl10;
        private EGLConfig eglConfig;
        private EGLContext eglContext;
        private EGLDisplay eglDisplay;
        private EGLSurface eglSurface;
        private GL gl;
        private boolean initied;
        private long lastRenderCallTime;
        private int surfaceHeight;
        private SurfaceTexture surfaceTexture;
        private int surfaceWidth;
        private int[] textures;
        
        public EGLThread(final SurfaceTexture surfaceTexture) {
            super("EGLThread");
            this.EGL_CONTEXT_CLIENT_VERSION = 12440;
            this.EGL_OPENGL_ES2_BIT = 4;
            this.textures = new int[23];
            this.drawRunnable = new Runnable() {
                @Override
                public void run() {
                    if (!EGLThread.this.initied) {
                        return;
                    }
                    if ((!EGLThread.this.eglContext.equals(EGLThread.this.egl10.eglGetCurrentContext()) || !EGLThread.this.eglSurface.equals(EGLThread.this.egl10.eglGetCurrentSurface(12377))) && !EGLThread.this.egl10.eglMakeCurrent(EGLThread.this.eglDisplay, EGLThread.this.eglSurface, EGLThread.this.eglSurface, EGLThread.this.eglContext)) {
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("eglMakeCurrent failed ");
                            sb.append(GLUtils.getEGLErrorString(EGLThread.this.egl10.eglGetError()));
                            FileLog.e(sb.toString());
                        }
                        return;
                    }
                    final float date = (System.currentTimeMillis() - IntroActivity.this.currentDate) / 1000.0f;
                    Intro.setPage(IntroActivity.this.currentViewPagerPage);
                    Intro.setDate(date);
                    Intro.onDrawFrame();
                    EGLThread.this.egl10.eglSwapBuffers(EGLThread.this.eglDisplay, EGLThread.this.eglSurface);
                    EGLThread.this.postRunnable(new _$$Lambda$IntroActivity$EGLThread$1$t5CDZ7wR8GgPufsAxg7RJ2ghGUs(this), 16L);
                }
            };
            this.surfaceTexture = surfaceTexture;
        }
        
        private boolean initGL() {
            this.egl10 = (EGL10)EGLContext.getEGL();
            this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            final EGLDisplay eglDisplay = this.eglDisplay;
            if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("eglGetDisplay failed ");
                    sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb.toString());
                }
                this.finish();
                return false;
            }
            if (!this.egl10.eglInitialize(eglDisplay, new int[2])) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("eglInitialize failed ");
                    sb2.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb2.toString());
                }
                this.finish();
                return false;
            }
            final int[] array = { 0 };
            final EGLConfig[] array2 = { null };
            if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[] { 12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 24, 12326, 0, 12338, 1, 12337, 2, 12344 }, array2, 1, array)) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("eglChooseConfig failed ");
                    sb3.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb3.toString());
                }
                this.finish();
                return false;
            }
            if (array[0] <= 0) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("eglConfig not initialized");
                }
                this.finish();
                return false;
            }
            this.eglConfig = array2[0];
            this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[] { 12440, 2, 12344 });
            if (this.eglContext == null) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("eglCreateContext failed ");
                    sb4.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb4.toString());
                }
                this.finish();
                return false;
            }
            final SurfaceTexture surfaceTexture = this.surfaceTexture;
            if (!(surfaceTexture instanceof SurfaceTexture)) {
                this.finish();
                return false;
            }
            this.eglSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, (Object)surfaceTexture, (int[])null);
            final EGLSurface eglSurface = this.eglSurface;
            if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("createWindowSurface failed ");
                    sb5.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb5.toString());
                }
                this.finish();
                return false;
            }
            if (!this.egl10.eglMakeCurrent(this.eglDisplay, eglSurface, eglSurface, this.eglContext)) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("eglMakeCurrent failed ");
                    sb6.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb6.toString());
                }
                this.finish();
                return false;
            }
            this.gl = this.eglContext.getGL();
            GLES20.glGenTextures(23, this.textures, 0);
            this.loadTexture(2131165498, 0);
            this.loadTexture(2131165497, 1);
            this.loadTexture(2131165499, 2);
            this.loadTexture(2131165500, 3);
            this.loadTexture(2131165502, 4);
            this.loadTexture(2131165501, 5);
            this.loadTexture(2131165504, 6);
            this.loadTexture(2131165503, 7);
            this.loadTexture(2131165505, 8);
            this.loadTexture(2131165506, 9);
            this.loadTexture(2131165508, 10);
            this.loadTexture(2131165507, 11);
            this.loadTexture(2131165509, 12);
            this.loadTexture(2131165510, 13);
            this.loadTexture(2131165511, 14);
            this.loadTexture(2131165513, 15);
            this.loadTexture(2131165512, 16);
            this.loadTexture(2131165514, 17);
            this.loadTexture(2131165515, 18);
            this.loadTexture(2131165516, 19);
            this.loadTexture(2131165517, 20);
            this.loadTexture(2131165518, 21);
            this.loadTexture(2131165519, 22);
            final int[] textures = this.textures;
            Intro.setTelegramTextures(textures[22], textures[21]);
            final int[] textures2 = this.textures;
            Intro.setPowerfulTextures(textures2[17], textures2[18], textures2[16], textures2[15]);
            final int[] textures3 = this.textures;
            Intro.setPrivateTextures(textures3[19], textures3[20]);
            final int[] textures4 = this.textures;
            Intro.setFreeTextures(textures4[14], textures4[13]);
            final int[] textures5 = this.textures;
            Intro.setFastTextures(textures5[2], textures5[3], textures5[1], textures5[0]);
            final int[] textures6 = this.textures;
            Intro.setIcTextures(textures6[4], textures6[5], textures6[6], textures6[7], textures6[8], textures6[9], textures6[10], textures6[11], textures6[12]);
            Intro.onSurfaceCreated();
            IntroActivity.this.currentDate = System.currentTimeMillis() - 1000L;
            return true;
        }
        
        private void loadTexture(final int n, final int n2) {
            final Drawable drawable = IntroActivity.this.getResources().getDrawable(n);
            if (drawable instanceof BitmapDrawable) {
                final Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                GLES20.glBindTexture(3553, this.textures[n2]);
                GLES20.glTexParameteri(3553, 10241, 9729);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10242, 33071);
                GLES20.glTexParameteri(3553, 10243, 33071);
                GLUtils.texImage2D(3553, 0, bitmap, 0);
            }
        }
        
        public void finish() {
            if (this.eglSurface != null) {
                final EGL10 egl10 = this.egl10;
                final EGLDisplay eglDisplay = this.eglDisplay;
                final EGLSurface egl_NO_SURFACE = EGL10.EGL_NO_SURFACE;
                egl10.eglMakeCurrent(eglDisplay, egl_NO_SURFACE, egl_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
                this.eglSurface = null;
            }
            final EGLContext eglContext = this.eglContext;
            if (eglContext != null) {
                this.egl10.eglDestroyContext(this.eglDisplay, eglContext);
                this.eglContext = null;
            }
            final EGLDisplay eglDisplay2 = this.eglDisplay;
            if (eglDisplay2 != null) {
                this.egl10.eglTerminate(eglDisplay2);
                this.eglDisplay = null;
            }
        }
        
        @Override
        public void run() {
            this.initied = this.initGL();
            super.run();
        }
        
        public void setSurfaceTextureSize(final int surfaceWidth, final int surfaceHeight) {
            Intro.onSurfaceChanged(this.surfaceWidth = surfaceWidth, this.surfaceHeight = surfaceHeight, Math.min(this.surfaceWidth / 150.0f, this.surfaceHeight / 150.0f), 0);
        }
        
        public void shutdown() {
            this.postRunnable(new _$$Lambda$IntroActivity$EGLThread$AriDXNPGmTpsZXAD4AD83qoD230(this));
        }
    }
    
    private class IntroAdapter extends PagerAdapter
    {
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            viewGroup.removeView((View)o);
        }
        
        @Override
        public int getCount() {
            return IntroActivity.this.titles.length;
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int n) {
            final FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            final TextView textView = new TextView(viewGroup.getContext());
            textView.setTextColor(-14606047);
            textView.setTextSize(1, 26.0f);
            textView.setGravity(17);
            frameLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 18.0f, 244.0f, 18.0f, 0.0f));
            final TextView textView2 = new TextView(viewGroup.getContext());
            textView2.setTextColor(-8355712);
            textView2.setTextSize(1, 15.0f);
            textView2.setGravity(17);
            frameLayout.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 16.0f, 286.0f, 16.0f, 0.0f));
            viewGroup.addView((View)frameLayout, 0);
            textView.setText((CharSequence)IntroActivity.this.titles[n]);
            textView2.setText((CharSequence)AndroidUtilities.replaceTags(IntroActivity.this.messages[n]));
            return frameLayout;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object obj) {
            return view.equals(obj);
        }
        
        @Override
        public void restoreState(final Parcelable parcelable, final ClassLoader classLoader) {
        }
        
        @Override
        public Parcelable saveState() {
            return null;
        }
        
        @Override
        public void setPrimaryItem(final ViewGroup viewGroup, final int currentPage, final Object o) {
            super.setPrimaryItem(viewGroup, currentPage, o);
            IntroActivity.this.bottomPages.setCurrentPage(currentPage);
            IntroActivity.this.currentViewPagerPage = currentPage;
        }
        
        @Override
        public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
            if (dataSetObserver != null) {
                super.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }
}
