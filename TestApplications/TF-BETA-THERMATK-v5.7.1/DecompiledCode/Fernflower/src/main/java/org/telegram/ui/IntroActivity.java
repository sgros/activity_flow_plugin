package org.telegram.ui;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Intro;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BottomPagesView;
import org.telegram.ui.Components.LayoutHelper;

public class IntroActivity extends Activity implements NotificationCenter.NotificationCenterDelegate {
   private BottomPagesView bottomPages;
   private int currentAccount;
   private long currentDate;
   private int currentViewPagerPage;
   private boolean destroyed;
   private boolean dragging;
   private IntroActivity.EGLThread eglThread;
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
      LocaleController.LocaleInfo var1 = LocaleController.getInstance().getCurrentLocaleInfo();
      String var2 = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
      boolean var3 = var2.contains("-");
      int var4 = 0;
      String var5;
      if (var3) {
         var5 = var2.split("-")[0];
      } else {
         var5 = var2;
      }

      String var6 = LocaleController.getLocaleAlias(var5);
      LocaleController.LocaleInfo var7 = null;
      LocaleController.LocaleInfo var8 = null;

      LocaleController.LocaleInfo var9;
      LocaleController.LocaleInfo var10;
      while(true) {
         var9 = var7;
         var10 = var8;
         if (var4 >= LocaleController.getInstance().languages.size()) {
            break;
         }

         var9 = (LocaleController.LocaleInfo)LocaleController.getInstance().languages.get(var4);
         if (var9.shortName.equals("en")) {
            var7 = var9;
         }

         if (var9.shortName.replace("_", "-").equals(var2) || var9.shortName.equals(var5) || var9.shortName.equals(var6)) {
            var8 = var9;
         }

         if (var7 != null && var8 != null) {
            var9 = var7;
            var10 = var8;
            break;
         }

         ++var4;
      }

      if (var9 != null && var10 != null && var9 != var10) {
         TLRPC.TL_langpack_getStrings var11 = new TLRPC.TL_langpack_getStrings();
         if (var10 != var1) {
            var11.lang_code = var10.getLangCode();
            this.localeInfo = var10;
         } else {
            var11.lang_code = var9.getLangCode();
            this.localeInfo = var9;
         }

         var11.keys.add("ContinueOnThisLanguage");
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var11, new _$$Lambda$IntroActivity$v08WUA33DIz9dwdLeC_Z0gtPf_4(this, var2), 8);
      }

   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.suggestedLangpack) {
         this.checkContinueText();
      }

   }

   // $FF: synthetic method
   public void lambda$checkContinueText$4$IntroActivity(String var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 != null) {
         TLRPC.Vector var4 = (TLRPC.Vector)var2;
         if (var4.objects.isEmpty()) {
            return;
         }

         TLRPC.LangPackString var5 = (TLRPC.LangPackString)var4.objects.get(0);
         if (var5 instanceof TLRPC.TL_langPackString) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$IntroActivity$xVDraVC71XqfCcnZOtPiiDOx4JQ(this, var5, var1));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$IntroActivity(TLRPC.LangPackString var1, String var2) {
      if (!this.destroyed) {
         this.textView.setText(var1.value);
         MessagesController.getGlobalMainSettings().edit().putString("language_showed2", var2.toLowerCase()).commit();
      }

   }

   // $FF: synthetic method
   public void lambda$onCreate$0$IntroActivity(View var1) {
      if (!this.startPressed) {
         this.startPressed = true;
         Intent var2 = new Intent(this, LaunchActivity.class);
         var2.putExtra("fromIntro", true);
         this.startActivity(var2);
         this.destroyed = true;
         this.finish();
      }
   }

   // $FF: synthetic method
   public boolean lambda$onCreate$1$IntroActivity(View var1) {
      ConnectionsManager.getInstance(this.currentAccount).switchBackend();
      return true;
   }

   // $FF: synthetic method
   public void lambda$onCreate$2$IntroActivity(View var1) {
      if (!this.startPressed && this.localeInfo != null) {
         LocaleController.getInstance().applyLanguage(this.localeInfo, true, false, this.currentAccount);
         this.startPressed = true;
         Intent var2 = new Intent(this, LaunchActivity.class);
         var2.putExtra("fromIntro", true);
         this.startActivity(var2);
         this.destroyed = true;
         this.finish();
      }

   }

   protected void onCreate(Bundle var1) {
      this.setTheme(2131624206);
      super.onCreate(var1);
      this.requestWindowFeature(1);
      MessagesController.getGlobalMainSettings().edit().putLong("intro_crashed_time", System.currentTimeMillis()).commit();
      this.titles = new String[]{LocaleController.getString("Page1Title", 2131560144), LocaleController.getString("Page2Title", 2131560146), LocaleController.getString("Page3Title", 2131560148), LocaleController.getString("Page5Title", 2131560152), LocaleController.getString("Page4Title", 2131560150), LocaleController.getString("Page6Title", 2131560154)};
      this.messages = new String[]{LocaleController.getString("Page1Message", 2131560143), LocaleController.getString("Page2Message", 2131560145), LocaleController.getString("Page3Message", 2131560147), LocaleController.getString("Page5Message", 2131560151), LocaleController.getString("Page4Message", 2131560149), LocaleController.getString("Page6Message", 2131560153)};
      ScrollView var6 = new ScrollView(this);
      var6.setFillViewport(true);
      FrameLayout var2 = new FrameLayout(this);
      var2.setBackgroundColor(-1);
      var6.addView(var2, LayoutHelper.createScroll(-1, -2, 51));
      FrameLayout var3 = new FrameLayout(this);
      var2.addView(var3, LayoutHelper.createFrame(-1, -2.0F, 51, 0.0F, 78.0F, 0.0F, 0.0F));
      TextureView var4 = new TextureView(this);
      var3.addView(var4, LayoutHelper.createFrame(200, 150, 17));
      var4.setSurfaceTextureListener(new SurfaceTextureListener() {
         // $FF: synthetic method
         public void lambda$onSurfaceTextureAvailable$0$IntroActivity$1() {
            IntroActivity.this.eglThread.drawRunnable.run();
         }

         public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
            if (IntroActivity.this.eglThread == null && var1 != null) {
               IntroActivity var4 = IntroActivity.this;
               var4.eglThread = var4.new EGLThread(var1);
               IntroActivity.this.eglThread.setSurfaceTextureSize(var2, var3);
               IntroActivity.this.eglThread.postRunnable(new _$$Lambda$IntroActivity$1$ziO_g9IlcqOuWLJOlD4I6EuEo50(this));
            }

         }

         public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
            if (IntroActivity.this.eglThread != null) {
               IntroActivity.this.eglThread.shutdown();
               IntroActivity.this.eglThread = null;
            }

            return true;
         }

         public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
            if (IntroActivity.this.eglThread != null) {
               IntroActivity.this.eglThread.setSurfaceTextureSize(var2, var3);
            }

         }

         public void onSurfaceTextureUpdated(SurfaceTexture var1) {
         }
      });
      this.viewPager = new ViewPager(this);
      this.viewPager.setAdapter(new IntroActivity.IntroAdapter());
      this.viewPager.setPageMargin(0);
      this.viewPager.setOffscreenPageLimit(1);
      var2.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0F));
      this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         public void onPageScrollStateChanged(int var1) {
            IntroActivity var2;
            if (var1 == 1) {
               IntroActivity.this.dragging = true;
               var2 = IntroActivity.this;
               var2.startDragX = var2.viewPager.getCurrentItem() * IntroActivity.this.viewPager.getMeasuredWidth();
            } else if (var1 == 0 || var1 == 2) {
               if (IntroActivity.this.dragging) {
                  IntroActivity.this.justEndDragging = true;
                  IntroActivity.this.dragging = false;
               }

               if (IntroActivity.this.lastPage != IntroActivity.this.viewPager.getCurrentItem()) {
                  var2 = IntroActivity.this;
                  var2.lastPage = var2.viewPager.getCurrentItem();
               }
            }

         }

         public void onPageScrolled(int var1, float var2, int var3) {
            IntroActivity.this.bottomPages.setPageOffset(var1, var2);
            var2 = (float)IntroActivity.this.viewPager.getMeasuredWidth();
            if (var2 != 0.0F) {
               Intro.setScrollOffset(((float)var1 * var2 + (float)var3 - (float)IntroActivity.this.currentViewPagerPage * var2) / var2);
            }
         }

         public void onPageSelected(int var1) {
            IntroActivity.this.currentViewPagerPage = var1;
         }
      });
      TextView var7 = new TextView(this);
      var7.setText(LocaleController.getString("StartMessaging", 2131560804).toUpperCase());
      var7.setGravity(17);
      var7.setTextColor(-1);
      var7.setTextSize(1, 16.0F);
      var7.setBackgroundResource(2131165800);
      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var9 = new StateListAnimator();
         ObjectAnimator var5 = ObjectAnimator.ofFloat(var7, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var9.addState(new int[]{16842919}, var5);
         var5 = ObjectAnimator.ofFloat(var7, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var9.addState(new int[0], var5);
         var7.setStateListAnimator(var9);
      }

      var7.setPadding(AndroidUtilities.dp(20.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(20.0F), AndroidUtilities.dp(10.0F));
      var2.addView(var7, LayoutHelper.createFrame(-2, -2.0F, 81, 10.0F, 0.0F, 10.0F, 76.0F));
      var7.setOnClickListener(new _$$Lambda$IntroActivity$Kg_leHKna32gZHR3CZT09b4whCI(this));
      if (BuildVars.DEBUG_VERSION) {
         var7.setOnLongClickListener(new _$$Lambda$IntroActivity$iyZ2QRC4zfDIPF0e6T1a3cQJl5Y(this));
      }

      this.bottomPages = new BottomPagesView(this, this.viewPager, 6);
      var2.addView(this.bottomPages, LayoutHelper.createFrame(66, 5.0F, 49, 0.0F, 350.0F, 0.0F, 0.0F));
      this.textView = new TextView(this);
      this.textView.setTextColor(-15494190);
      this.textView.setGravity(17);
      this.textView.setTextSize(1, 16.0F);
      var2.addView(this.textView, LayoutHelper.createFrame(-2, 30.0F, 81, 0.0F, 0.0F, 0.0F, 20.0F));
      this.textView.setOnClickListener(new _$$Lambda$IntroActivity$3V_J2Y1xEuzoe0ERCQjOPYz9JM8(this));
      if (AndroidUtilities.isTablet()) {
         var2 = new FrameLayout(this);
         this.setContentView(var2);
         ImageView var10 = new ImageView(this);
         BitmapDrawable var8 = (BitmapDrawable)this.getResources().getDrawable(2131165338);
         TileMode var11 = TileMode.REPEAT;
         var8.setTileModeXY(var11, var11);
         var10.setBackgroundDrawable(var8);
         var2.addView(var10, LayoutHelper.createFrame(-1, -1.0F));
         var3 = new FrameLayout(this);
         var3.setBackgroundResource(2131165328);
         var3.addView(var6, LayoutHelper.createFrame(-1, -1.0F));
         var2.addView(var3, LayoutHelper.createFrame(498, 528, 17));
      } else {
         this.setRequestedOrientation(1);
         this.setContentView(var6);
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
         } else {
            this.viewPager.setCurrentItem(0);
            this.lastPage = 0;
         }

         this.justCreated = false;
      }

      AndroidUtilities.checkForCrashes(this);
      AndroidUtilities.checkForUpdates(this);
      ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
   }

   public class EGLThread extends DispatchQueue {
      private final int EGL_CONTEXT_CLIENT_VERSION = 12440;
      private final int EGL_OPENGL_ES2_BIT = 4;
      private Runnable drawRunnable = new Runnable() {
         // $FF: synthetic method
         public void lambda$run$0$IntroActivity$EGLThread$1() {
            EGLThread.this.drawRunnable.run();
         }

         public void run() {
            if (EGLThread.this.initied) {
               if ((!EGLThread.this.eglContext.equals(EGLThread.this.egl10.eglGetCurrentContext()) || !EGLThread.this.eglSurface.equals(EGLThread.this.egl10.eglGetCurrentSurface(12377))) && !EGLThread.this.egl10.eglMakeCurrent(EGLThread.this.eglDisplay, EGLThread.this.eglSurface, EGLThread.this.eglSurface, EGLThread.this.eglContext)) {
                  if (BuildVars.LOGS_ENABLED) {
                     StringBuilder var1 = new StringBuilder();
                     var1.append("eglMakeCurrent failed ");
                     var1.append(GLUtils.getEGLErrorString(EGLThread.this.egl10.eglGetError()));
                     FileLog.e(var1.toString());
                  }

               } else {
                  float var2 = (float)(System.currentTimeMillis() - IntroActivity.this.currentDate) / 1000.0F;
                  Intro.setPage(IntroActivity.this.currentViewPagerPage);
                  Intro.setDate(var2);
                  Intro.onDrawFrame();
                  EGLThread.this.egl10.eglSwapBuffers(EGLThread.this.eglDisplay, EGLThread.this.eglSurface);
                  EGLThread.this.postRunnable(new _$$Lambda$IntroActivity$EGLThread$1$t5CDZ7wR8GgPufsAxg7RJ2ghGUs(this), 16L);
               }
            }
         }
      };
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
      private int[] textures = new int[23];

      public EGLThread(SurfaceTexture var2) {
         super("EGLThread");
         this.surfaceTexture = var2;
      }

      private boolean initGL() {
         this.egl10 = (EGL10)EGLContext.getEGL();
         this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
         EGLDisplay var1 = this.eglDisplay;
         StringBuilder var6;
         if (var1 == EGL10.EGL_NO_DISPLAY) {
            if (BuildVars.LOGS_ENABLED) {
               var6 = new StringBuilder();
               var6.append("eglGetDisplay failed ");
               var6.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
               FileLog.e(var6.toString());
            }

            this.finish();
            return false;
         } else {
            int[] var2 = new int[2];
            if (!this.egl10.eglInitialize(var1, var2)) {
               if (BuildVars.LOGS_ENABLED) {
                  var6 = new StringBuilder();
                  var6.append("eglInitialize failed ");
                  var6.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                  FileLog.e(var6.toString());
               }

               this.finish();
               return false;
            } else {
               var2 = new int[1];
               EGLConfig[] var3 = new EGLConfig[1];
               if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 24, 12326, 0, 12338, 1, 12337, 2, 12344}, var3, 1, var2)) {
                  if (BuildVars.LOGS_ENABLED) {
                     var6 = new StringBuilder();
                     var6.append("eglChooseConfig failed ");
                     var6.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                     FileLog.e(var6.toString());
                  }

                  this.finish();
                  return false;
               } else if (var2[0] > 0) {
                  this.eglConfig = var3[0];
                  this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
                  if (this.eglContext == null) {
                     if (BuildVars.LOGS_ENABLED) {
                        var6 = new StringBuilder();
                        var6.append("eglCreateContext failed ");
                        var6.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        FileLog.e(var6.toString());
                     }

                     this.finish();
                     return false;
                  } else {
                     SurfaceTexture var4 = this.surfaceTexture;
                     if (var4 instanceof SurfaceTexture) {
                        this.eglSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, var4, (int[])null);
                        EGLSurface var5 = this.eglSurface;
                        if (var5 != null && var5 != EGL10.EGL_NO_SURFACE) {
                           if (!this.egl10.eglMakeCurrent(this.eglDisplay, var5, var5, this.eglContext)) {
                              if (BuildVars.LOGS_ENABLED) {
                                 var6 = new StringBuilder();
                                 var6.append("eglMakeCurrent failed ");
                                 var6.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                 FileLog.e(var6.toString());
                              }

                              this.finish();
                              return false;
                           } else {
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
                              var2 = this.textures;
                              Intro.setTelegramTextures(var2[22], var2[21]);
                              var2 = this.textures;
                              Intro.setPowerfulTextures(var2[17], var2[18], var2[16], var2[15]);
                              var2 = this.textures;
                              Intro.setPrivateTextures(var2[19], var2[20]);
                              var2 = this.textures;
                              Intro.setFreeTextures(var2[14], var2[13]);
                              var2 = this.textures;
                              Intro.setFastTextures(var2[2], var2[3], var2[1], var2[0]);
                              var2 = this.textures;
                              Intro.setIcTextures(var2[4], var2[5], var2[6], var2[7], var2[8], var2[9], var2[10], var2[11], var2[12]);
                              Intro.onSurfaceCreated();
                              IntroActivity.this.currentDate = System.currentTimeMillis() - 1000L;
                              return true;
                           }
                        } else {
                           if (BuildVars.LOGS_ENABLED) {
                              var6 = new StringBuilder();
                              var6.append("createWindowSurface failed ");
                              var6.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                              FileLog.e(var6.toString());
                           }

                           this.finish();
                           return false;
                        }
                     } else {
                        this.finish();
                        return false;
                     }
                  }
               } else {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.e("eglConfig not initialized");
                  }

                  this.finish();
                  return false;
               }
            }
         }
      }

      private void loadTexture(int var1, int var2) {
         Drawable var3 = IntroActivity.this.getResources().getDrawable(var1);
         if (var3 instanceof BitmapDrawable) {
            Bitmap var4 = ((BitmapDrawable)var3).getBitmap();
            GLES20.glBindTexture(3553, this.textures[var2]);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLUtils.texImage2D(3553, 0, var4, 0);
         }

      }

      public void finish() {
         if (this.eglSurface != null) {
            EGL10 var1 = this.egl10;
            EGLDisplay var2 = this.eglDisplay;
            EGLSurface var3 = EGL10.EGL_NO_SURFACE;
            var1.eglMakeCurrent(var2, var3, var3, EGL10.EGL_NO_CONTEXT);
            this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
            this.eglSurface = null;
         }

         EGLContext var4 = this.eglContext;
         if (var4 != null) {
            this.egl10.eglDestroyContext(this.eglDisplay, var4);
            this.eglContext = null;
         }

         EGLDisplay var5 = this.eglDisplay;
         if (var5 != null) {
            this.egl10.eglTerminate(var5);
            this.eglDisplay = null;
         }

      }

      // $FF: synthetic method
      public void lambda$shutdown$0$IntroActivity$EGLThread() {
         this.finish();
         Looper var1 = Looper.myLooper();
         if (var1 != null) {
            var1.quit();
         }

      }

      public void run() {
         this.initied = this.initGL();
         super.run();
      }

      public void setSurfaceTextureSize(int var1, int var2) {
         this.surfaceWidth = var1;
         this.surfaceHeight = var2;
         Intro.onSurfaceChanged(var1, var2, Math.min((float)this.surfaceWidth / 150.0F, (float)this.surfaceHeight / 150.0F), 0);
      }

      public void shutdown() {
         this.postRunnable(new _$$Lambda$IntroActivity$EGLThread$AriDXNPGmTpsZXAD4AD83qoD230(this));
      }
   }

   private class IntroAdapter extends PagerAdapter {
      private IntroAdapter() {
      }

      // $FF: synthetic method
      IntroAdapter(Object var2) {
         this();
      }

      public void destroyItem(ViewGroup var1, int var2, Object var3) {
         var1.removeView((View)var3);
      }

      public int getCount() {
         return IntroActivity.this.titles.length;
      }

      public Object instantiateItem(ViewGroup var1, int var2) {
         FrameLayout var3 = new FrameLayout(var1.getContext());
         TextView var4 = new TextView(var1.getContext());
         var4.setTextColor(-14606047);
         var4.setTextSize(1, 26.0F);
         var4.setGravity(17);
         var3.addView(var4, LayoutHelper.createFrame(-1, -2.0F, 51, 18.0F, 244.0F, 18.0F, 0.0F));
         TextView var5 = new TextView(var1.getContext());
         var5.setTextColor(-8355712);
         var5.setTextSize(1, 15.0F);
         var5.setGravity(17);
         var3.addView(var5, LayoutHelper.createFrame(-1, -2.0F, 51, 16.0F, 286.0F, 16.0F, 0.0F));
         var1.addView(var3, 0);
         var4.setText(IntroActivity.this.titles[var2]);
         var5.setText(AndroidUtilities.replaceTags(IntroActivity.this.messages[var2]));
         return var3;
      }

      public boolean isViewFromObject(View var1, Object var2) {
         return var1.equals(var2);
      }

      public void restoreState(Parcelable var1, ClassLoader var2) {
      }

      public Parcelable saveState() {
         return null;
      }

      public void setPrimaryItem(ViewGroup var1, int var2, Object var3) {
         super.setPrimaryItem(var1, var2, var3);
         IntroActivity.this.bottomPages.setCurrentPage(var2);
         IntroActivity.this.currentViewPagerPage = var2;
      }

      public void unregisterDataSetObserver(DataSetObserver var1) {
         if (var1 != null) {
            super.unregisterDataSetObserver(var1);
         }

      }
   }
}
