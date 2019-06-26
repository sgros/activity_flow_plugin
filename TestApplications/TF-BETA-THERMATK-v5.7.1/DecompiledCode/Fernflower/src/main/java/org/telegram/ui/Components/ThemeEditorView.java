package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.style.ForegroundColorSpan;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.WindowManager.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.TextColorThemeCell;

public class ThemeEditorView {
   @SuppressLint({"StaticFieldLeak"})
   private static volatile ThemeEditorView Instance;
   private ArrayList currentThemeDesription;
   private int currentThemeDesriptionPosition;
   private String currentThemeName;
   private DecelerateInterpolator decelerateInterpolator;
   private ThemeEditorView.EditorAlert editorAlert;
   private final int editorHeight = AndroidUtilities.dp(54.0F);
   private final int editorWidth = AndroidUtilities.dp(54.0F);
   private boolean hidden;
   private Activity parentActivity;
   private SharedPreferences preferences;
   private WallpaperUpdater wallpaperUpdater;
   private LayoutParams windowLayoutParams;
   private WindowManager windowManager;
   private FrameLayout windowView;

   private void animateToBoundsMaybe() {
      int var3;
      int var4;
      Editor var5;
      int var6;
      ArrayList var10;
      boolean var12;
      label100: {
         label93: {
            int var1 = getSideCoord(true, 0, 0.0F, this.editorWidth);
            int var2 = getSideCoord(true, 1, 0.0F, this.editorWidth);
            var3 = getSideCoord(false, 0, 0.0F, this.editorHeight);
            var4 = getSideCoord(false, 1, 0.0F, this.editorHeight);
            var5 = this.preferences.edit();
            var6 = AndroidUtilities.dp(20.0F);
            if (Math.abs(var1 - this.windowLayoutParams.x) > var6) {
               int var7 = this.windowLayoutParams.x;
               if (var7 >= 0 || var7 <= -this.editorWidth / 4) {
                  if (Math.abs(var2 - this.windowLayoutParams.x) > var6) {
                     int var8 = this.windowLayoutParams.x;
                     int var9 = AndroidUtilities.displaySize.x;
                     var7 = this.editorWidth;
                     if (var8 <= var9 - var7 || var8 >= var9 - var7 / 4 * 3) {
                        if (this.windowView.getAlpha() != 1.0F) {
                           var10 = new ArrayList();
                           if (this.windowLayoutParams.x < 0) {
                              var10.add(ObjectAnimator.ofInt(this, "x", new int[]{-this.editorWidth}));
                           } else {
                              var10.add(ObjectAnimator.ofInt(this, "x", new int[]{AndroidUtilities.displaySize.x}));
                           }

                           var12 = true;
                           break label100;
                        }

                        var5.putFloat("px", (float)(this.windowLayoutParams.x - var1) / (float)(var2 - var1));
                        var5.putInt("sidex", 2);
                        var10 = null;
                        break label93;
                     }
                  }

                  var10 = new ArrayList();
                  var5.putInt("sidex", 1);
                  if (this.windowView.getAlpha() != 1.0F) {
                     var10.add(ObjectAnimator.ofFloat(this.windowView, View.ALPHA, new float[]{1.0F}));
                  }

                  var10.add(ObjectAnimator.ofInt(this, "x", new int[]{var2}));
                  break label93;
               }
            }

            var10 = new ArrayList();
            var5.putInt("sidex", 0);
            if (this.windowView.getAlpha() != 1.0F) {
               var10.add(ObjectAnimator.ofFloat(this.windowView, View.ALPHA, new float[]{1.0F}));
            }

            var10.add(ObjectAnimator.ofInt(this, "x", new int[]{var1}));
         }

         var12 = false;
      }

      ArrayList var11 = var10;
      if (!var12) {
         if (Math.abs(var3 - this.windowLayoutParams.y) > var6 && this.windowLayoutParams.y > ActionBar.getCurrentActionBarHeight()) {
            if (Math.abs(var4 - this.windowLayoutParams.y) <= var6) {
               var11 = var10;
               if (var10 == null) {
                  var11 = new ArrayList();
               }

               var5.putInt("sidey", 1);
               var11.add(ObjectAnimator.ofInt(this, "y", new int[]{var4}));
               var10 = var11;
            } else {
               var5.putFloat("py", (float)(this.windowLayoutParams.y - var3) / (float)(var4 - var3));
               var5.putInt("sidey", 2);
            }
         } else {
            var11 = var10;
            if (var10 == null) {
               var11 = new ArrayList();
            }

            var5.putInt("sidey", 0);
            var11.add(ObjectAnimator.ofInt(this, "y", new int[]{var3}));
            var10 = var11;
         }

         var5.commit();
         var11 = var10;
      }

      if (var11 != null) {
         if (this.decelerateInterpolator == null) {
            this.decelerateInterpolator = new DecelerateInterpolator();
         }

         AnimatorSet var13 = new AnimatorSet();
         var13.setInterpolator(this.decelerateInterpolator);
         var13.setDuration(150L);
         if (var12) {
            var11.add(ObjectAnimator.ofFloat(this.windowView, View.ALPHA, new float[]{0.0F}));
            var13.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  Theme.saveCurrentTheme(ThemeEditorView.this.currentThemeName, true);
                  ThemeEditorView.this.destroy();
               }
            });
         }

         var13.playTogether(var11);
         var13.start();
      }

   }

   public static ThemeEditorView getInstance() {
      return Instance;
   }

   private static int getSideCoord(boolean var0, int var1, float var2, int var3) {
      int var4;
      if (var0) {
         var4 = AndroidUtilities.displaySize.x;
      } else {
         var4 = AndroidUtilities.displaySize.y - var3;
         var3 = ActionBar.getCurrentActionBarHeight();
      }

      var3 = var4 - var3;
      if (var1 == 0) {
         var1 = AndroidUtilities.dp(10.0F);
      } else if (var1 == 1) {
         var1 = var3 - AndroidUtilities.dp(10.0F);
      } else {
         var1 = Math.round((float)(var3 - AndroidUtilities.dp(20.0F)) * var2) + AndroidUtilities.dp(10.0F);
      }

      var3 = var1;
      if (!var0) {
         var3 = var1 + ActionBar.getCurrentActionBarHeight();
      }

      return var3;
   }

   private void hide() {
      if (this.parentActivity != null) {
         try {
            AnimatorSet var1 = new AnimatorSet();
            var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.windowView, View.ALPHA, new float[]{1.0F, 0.0F}), ObjectAnimator.ofFloat(this.windowView, "scaleX", new float[]{1.0F, 0.0F}), ObjectAnimator.ofFloat(this.windowView, "scaleY", new float[]{1.0F, 0.0F})});
            var1.setInterpolator(this.decelerateInterpolator);
            var1.setDuration(150L);
            AnimatorListenerAdapter var2 = new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (ThemeEditorView.this.windowView != null) {
                     ThemeEditorView.this.windowManager.removeView(ThemeEditorView.this.windowView);
                  }

               }
            };
            var1.addListener(var2);
            var1.start();
            this.hidden = true;
         } catch (Exception var3) {
         }

      }
   }

   private void show() {
      if (this.parentActivity != null) {
         try {
            this.windowManager.addView(this.windowView, this.windowLayoutParams);
            this.hidden = false;
            this.showWithAnimation();
         } catch (Exception var2) {
         }

      }
   }

   private void showWithAnimation() {
      AnimatorSet var1 = new AnimatorSet();
      var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.windowView, View.ALPHA, new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.windowView, "scaleX", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.windowView, "scaleY", new float[]{0.0F, 1.0F})});
      var1.setInterpolator(this.decelerateInterpolator);
      var1.setDuration(150L);
      var1.start();
   }

   public void close() {
      try {
         this.windowManager.removeView(this.windowView);
      } catch (Exception var2) {
      }

      this.parentActivity = null;
   }

   public void destroy() {
      this.wallpaperUpdater.cleanup();
      if (this.parentActivity != null) {
         FrameLayout var1 = this.windowView;
         if (var1 != null) {
            try {
               this.windowManager.removeViewImmediate(var1);
               this.windowView = null;
            } catch (Exception var3) {
               FileLog.e((Throwable)var3);
            }

            try {
               if (this.editorAlert != null) {
                  this.editorAlert.dismiss();
                  this.editorAlert = null;
               }
            } catch (Exception var2) {
               FileLog.e((Throwable)var2);
            }

            this.parentActivity = null;
            Instance = null;
         }
      }

   }

   public int getX() {
      return this.windowLayoutParams.x;
   }

   public int getY() {
      return this.windowLayoutParams.y;
   }

   public void onActivityResult(int var1, int var2, Intent var3) {
      WallpaperUpdater var4 = this.wallpaperUpdater;
      if (var4 != null) {
         var4.onActivityResult(var1, var2, var3);
      }

   }

   public void onConfigurationChanged() {
      int var1 = this.preferences.getInt("sidex", 1);
      int var2 = this.preferences.getInt("sidey", 0);
      float var3 = this.preferences.getFloat("px", 0.0F);
      float var4 = this.preferences.getFloat("py", 0.0F);
      this.windowLayoutParams.x = getSideCoord(true, var1, var3, this.editorWidth);
      this.windowLayoutParams.y = getSideCoord(false, var2, var4, this.editorHeight);

      try {
         if (this.windowView.getParent() != null) {
            this.windowManager.updateViewLayout(this.windowView, this.windowLayoutParams);
         }
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

   }

   @Keep
   public void setX(int var1) {
      LayoutParams var2 = this.windowLayoutParams;
      var2.x = var1;
      this.windowManager.updateViewLayout(this.windowView, var2);
   }

   @Keep
   public void setY(int var1) {
      LayoutParams var2 = this.windowLayoutParams;
      var2.y = var1;
      this.windowManager.updateViewLayout(this.windowView, var2);
   }

   public void show(Activity var1, final String var2) {
      if (Instance != null) {
         Instance.destroy();
      }

      this.hidden = false;
      this.currentThemeName = var2;
      this.windowView = new FrameLayout(var1) {
         private boolean dragging;
         private float startX;
         private float startY;

         // $FF: synthetic method
         static void lambda$onTouchEvent$0(DialogInterface var0) {
         }

         // $FF: synthetic method
         public void lambda$onTouchEvent$1$ThemeEditorView$1(DialogInterface var1) {
            ThemeEditorView.this.editorAlert = null;
            ThemeEditorView.this.show();
         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            return true;
         }

         public boolean onTouchEvent(MotionEvent var1) {
            float var2 = var1.getRawX();
            float var3 = var1.getRawY();
            if (var1.getAction() == 0) {
               this.startX = var2;
               this.startY = var3;
            } else if (var1.getAction() == 2 && !this.dragging) {
               if (Math.abs(this.startX - var2) >= AndroidUtilities.getPixelsInCM(0.3F, true) || Math.abs(this.startY - var3) >= AndroidUtilities.getPixelsInCM(0.3F, false)) {
                  this.dragging = true;
                  this.startX = var2;
                  this.startY = var3;
               }
            } else if (var1.getAction() == 1 && !this.dragging && ThemeEditorView.this.editorAlert == null) {
               LaunchActivity var4;
               Object var6;
               ActionBarLayout var7;
               ActionBarLayout var8;
               label82: {
                  var4 = (LaunchActivity)ThemeEditorView.this.parentActivity;
                  boolean var5 = AndroidUtilities.isTablet();
                  var6 = null;
                  if (var5) {
                     var7 = var4.getLayersActionBarLayout();
                     var8 = var7;
                     if (var7 != null) {
                        var8 = var7;
                        if (var7.fragmentsStack.isEmpty()) {
                           var8 = null;
                        }
                     }

                     var7 = var8;
                     if (var8 != null) {
                        break label82;
                     }

                     var8 = var4.getRightActionBarLayout();
                     var7 = var8;
                     if (var8 == null) {
                        break label82;
                     }

                     var7 = var8;
                     if (!var8.fragmentsStack.isEmpty()) {
                        break label82;
                     }
                  }

                  var7 = null;
               }

               var8 = var7;
               if (var7 == null) {
                  var8 = var4.getActionBarLayout();
               }

               if (var8 != null) {
                  BaseFragment var15 = (BaseFragment)var6;
                  if (!var8.fragmentsStack.isEmpty()) {
                     ArrayList var16 = var8.fragmentsStack;
                     var15 = (BaseFragment)var16.get(var16.size() - 1);
                  }

                  if (var15 != null) {
                     ThemeDescription[] var17 = var15.getThemeDescriptions();
                     if (var17 != null) {
                        ThemeEditorView var18 = ThemeEditorView.this;
                        var18.editorAlert = var18.new EditorAlert(var18.parentActivity, var17);
                        ThemeEditorView.this.editorAlert.setOnDismissListener(_$$Lambda$ThemeEditorView$1$wsYCYqNbqFfDt4B1cHgNjwuFWAI.INSTANCE);
                        ThemeEditorView.this.editorAlert.setOnDismissListener(new _$$Lambda$ThemeEditorView$1$E9dxEm5ftbqgpiMF889EOuTy_RY(this));
                        ThemeEditorView.this.editorAlert.show();
                        ThemeEditorView.this.hide();
                     }
                  }
               }
            }

            if (this.dragging) {
               if (var1.getAction() == 2) {
                  float var9 = this.startX;
                  float var10 = this.startY;
                  LayoutParams var14 = ThemeEditorView.this.windowLayoutParams;
                  var14.x = (int)((float)var14.x + (var2 - var9));
                  var14 = ThemeEditorView.this.windowLayoutParams;
                  var14.y = (int)((float)var14.y + (var3 - var10));
                  int var11 = ThemeEditorView.this.editorWidth / 2;
                  int var12 = ThemeEditorView.this.windowLayoutParams.x;
                  int var13 = -var11;
                  if (var12 < var13) {
                     ThemeEditorView.this.windowLayoutParams.x = var13;
                  } else if (ThemeEditorView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - ThemeEditorView.this.windowLayoutParams.width + var11) {
                     ThemeEditorView.this.windowLayoutParams.x = AndroidUtilities.displaySize.x - ThemeEditorView.this.windowLayoutParams.width + var11;
                  }

                  var13 = ThemeEditorView.this.windowLayoutParams.x;
                  var9 = 1.0F;
                  if (var13 < 0) {
                     var9 = 1.0F + (float)ThemeEditorView.this.windowLayoutParams.x / (float)var11 * 0.5F;
                  } else if (ThemeEditorView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - ThemeEditorView.this.windowLayoutParams.width) {
                     var9 = 1.0F - (float)(ThemeEditorView.this.windowLayoutParams.x - AndroidUtilities.displaySize.x + ThemeEditorView.this.windowLayoutParams.width) / (float)var11 * 0.5F;
                  }

                  if (ThemeEditorView.this.windowView.getAlpha() != var9) {
                     ThemeEditorView.this.windowView.setAlpha(var9);
                  }

                  if (ThemeEditorView.this.windowLayoutParams.y < 0) {
                     ThemeEditorView.this.windowLayoutParams.y = 0;
                  } else if (ThemeEditorView.this.windowLayoutParams.y > AndroidUtilities.displaySize.y - ThemeEditorView.this.windowLayoutParams.height + 0) {
                     ThemeEditorView.this.windowLayoutParams.y = AndroidUtilities.displaySize.y - ThemeEditorView.this.windowLayoutParams.height + 0;
                  }

                  ThemeEditorView.this.windowManager.updateViewLayout(ThemeEditorView.this.windowView, ThemeEditorView.this.windowLayoutParams);
                  this.startX = var2;
                  this.startY = var3;
               } else if (var1.getAction() == 1) {
                  this.dragging = false;
                  ThemeEditorView.this.animateToBoundsMaybe();
               }
            }

            return true;
         }
      };
      this.windowView.setBackgroundResource(2131165873);
      this.windowManager = (WindowManager)var1.getSystemService("window");
      this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
      int var3 = this.preferences.getInt("sidex", 1);
      int var4 = this.preferences.getInt("sidey", 0);
      float var5 = this.preferences.getFloat("px", 0.0F);
      float var6 = this.preferences.getFloat("py", 0.0F);

      try {
         LayoutParams var7 = new LayoutParams();
         this.windowLayoutParams = var7;
         this.windowLayoutParams.width = this.editorWidth;
         this.windowLayoutParams.height = this.editorHeight;
         this.windowLayoutParams.x = getSideCoord(true, var3, var5, this.editorWidth);
         this.windowLayoutParams.y = getSideCoord(false, var4, var6, this.editorHeight);
         this.windowLayoutParams.format = -3;
         this.windowLayoutParams.gravity = 51;
         this.windowLayoutParams.type = 99;
         this.windowLayoutParams.flags = 16777736;
         this.windowManager.addView(this.windowView, this.windowLayoutParams);
      } catch (Exception var8) {
         FileLog.e((Throwable)var8);
         return;
      }

      this.wallpaperUpdater = new WallpaperUpdater(var1, (BaseFragment)null, new WallpaperUpdater.WallpaperUpdaterDelegate() {
         public void didSelectWallpaper(File var1, Bitmap var2x, boolean var3) {
            Theme.setThemeWallpaper(var2, var2x, var1);
         }

         public void needOpenColorPicker() {
            for(int var1 = 0; var1 < ThemeEditorView.this.currentThemeDesription.size(); ++var1) {
               ThemeDescription var2x = (ThemeDescription)ThemeEditorView.this.currentThemeDesription.get(var1);
               var2x.startEditing();
               if (var1 == 0) {
                  ThemeEditorView.this.editorAlert.colorPicker.setColor(var2x.getCurrentColor());
               }
            }

            ThemeEditorView.this.editorAlert.setColorPickerVisible(true);
         }
      });
      Instance = this;
      this.parentActivity = var1;
      this.showWithAnimation();
   }

   public class EditorAlert extends BottomSheet {
      private boolean animationInProgress;
      private FrameLayout bottomLayout;
      private FrameLayout bottomSaveLayout;
      private TextView cancelButton;
      private AnimatorSet colorChangeAnimation;
      private ThemeEditorView.EditorAlert.ColorPicker colorPicker;
      private TextView defaultButtom;
      private FrameLayout frameLayout;
      private boolean ignoreTextChange;
      private LinearLayoutManager layoutManager;
      private ThemeEditorView.EditorAlert.ListAdapter listAdapter;
      private RecyclerListView listView;
      private int previousScrollPosition;
      private TextView saveButton;
      private int scrollOffsetY;
      private ThemeEditorView.EditorAlert.SearchAdapter searchAdapter;
      private EmptyTextProgressView searchEmptyView;
      private ThemeEditorView.EditorAlert.SearchField searchField;
      private View[] shadow = new View[2];
      private AnimatorSet[] shadowAnimation = new AnimatorSet[2];
      private Drawable shadowDrawable;
      private boolean startedColorChange;
      private int topBeforeSwitch;

      public EditorAlert(Context var2, ThemeDescription[] var3) {
         super(var2, true, 1);
         this.shadowDrawable = var2.getResources().getDrawable(2131165824).mutate();
         super.containerView = new FrameLayout(var2) {
            private boolean ignoreLayout = false;
            private RectF rect1 = new RectF();

            protected void onDraw(Canvas var1) {
               int var2;
               int var4;
               int var5;
               int var6;
               float var8;
               label33: {
                  var2 = EditorAlert.this.scrollOffsetY - ThemeEditorView.EditorAlert.access$2300(EditorAlert.this) + AndroidUtilities.dp(6.0F);
                  int var3 = EditorAlert.this.scrollOffsetY - ThemeEditorView.EditorAlert.access$2400(EditorAlert.this) - AndroidUtilities.dp(13.0F);
                  var4 = this.getMeasuredHeight() + AndroidUtilities.dp(30.0F) + ThemeEditorView.EditorAlert.access$2500(EditorAlert.this);
                  float var11;
                  if (!ThemeEditorView.EditorAlert.access$2600(EditorAlert.this) && VERSION.SDK_INT >= 21) {
                     var5 = AndroidUtilities.statusBarHeight;
                     var6 = var3 + var5;
                     int var7 = var2 + var5;
                     var5 = var4 - var5;
                     var4 = ThemeEditorView.EditorAlert.access$2700(EditorAlert.this);
                     var3 = AndroidUtilities.statusBarHeight;
                     if (var4 + var6 < var3 * 2) {
                        var4 = Math.min(var3, var3 * 2 - var6 - ThemeEditorView.EditorAlert.access$2800(EditorAlert.this));
                        var6 -= var4;
                        var5 += var4;
                        var8 = 1.0F - Math.min(1.0F, (float)(var4 * 2) / (float)AndroidUtilities.statusBarHeight);
                     } else {
                        var8 = 1.0F;
                     }

                     int var9 = ThemeEditorView.EditorAlert.access$2900(EditorAlert.this);
                     int var10 = AndroidUtilities.statusBarHeight;
                     var2 = var7;
                     var3 = var6;
                     var4 = var5;
                     var11 = var8;
                     if (var9 + var6 < var10) {
                        var3 = Math.min(var10, var10 - var6 - ThemeEditorView.EditorAlert.access$3000(EditorAlert.this));
                        var2 = var7;
                        var4 = var5;
                        var5 = var3;
                        break label33;
                     }
                  } else {
                     var11 = 1.0F;
                  }

                  var5 = 0;
                  var8 = var11;
                  var6 = var3;
               }

               EditorAlert.this.shadowDrawable.setBounds(0, var6, this.getMeasuredWidth(), var4);
               EditorAlert.this.shadowDrawable.draw(var1);
               if (var8 != 1.0F) {
                  Theme.dialogs_onlineCirclePaint.setColor(-1);
                  this.rect1.set((float)ThemeEditorView.EditorAlert.access$3200(EditorAlert.this), (float)(ThemeEditorView.EditorAlert.access$3300(EditorAlert.this) + var6), (float)(this.getMeasuredWidth() - ThemeEditorView.EditorAlert.access$3400(EditorAlert.this)), (float)(ThemeEditorView.EditorAlert.access$3500(EditorAlert.this) + var6 + AndroidUtilities.dp(24.0F)));
                  var1.drawRoundRect(this.rect1, (float)AndroidUtilities.dp(12.0F) * var8, (float)AndroidUtilities.dp(12.0F) * var8, Theme.dialogs_onlineCirclePaint);
               }

               var6 = AndroidUtilities.dp(36.0F);
               this.rect1.set((float)((this.getMeasuredWidth() - var6) / 2), (float)var2, (float)((this.getMeasuredWidth() + var6) / 2), (float)(var2 + AndroidUtilities.dp(4.0F)));
               Theme.dialogs_onlineCirclePaint.setColor(-1973016);
               Theme.dialogs_onlineCirclePaint.setAlpha((int)(EditorAlert.this.listView.getAlpha() * 255.0F));
               var1.drawRoundRect(this.rect1, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), Theme.dialogs_onlineCirclePaint);
               if (var5 > 0) {
                  var6 = Color.argb(255, (int)((float)Color.red(-1) * 0.8F), (int)((float)Color.green(-1) * 0.8F), (int)((float)Color.blue(-1) * 0.8F));
                  Theme.dialogs_onlineCirclePaint.setColor(var6);
                  var1.drawRect((float)ThemeEditorView.EditorAlert.access$3600(EditorAlert.this), (float)(AndroidUtilities.statusBarHeight - var5), (float)(this.getMeasuredWidth() - ThemeEditorView.EditorAlert.access$3700(EditorAlert.this)), (float)AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
               }

            }

            public boolean onInterceptTouchEvent(MotionEvent var1) {
               if (var1.getAction() == 0 && EditorAlert.this.scrollOffsetY != 0 && var1.getY() < (float)EditorAlert.this.scrollOffsetY) {
                  EditorAlert.this.dismiss();
                  return true;
               } else {
                  return super.onInterceptTouchEvent(var1);
               }
            }

            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               super.onLayout(var1, var2, var3, var4, var5);
               EditorAlert.this.updateLayout();
            }

            protected void onMeasure(int var1, int var2) {
               int var3 = MeasureSpec.getSize(var1);
               int var4 = MeasureSpec.getSize(var2);
               if (VERSION.SDK_INT >= 21 && !ThemeEditorView.EditorAlert.access$1700(EditorAlert.this)) {
                  this.ignoreLayout = true;
                  this.setPadding(ThemeEditorView.EditorAlert.access$1800(EditorAlert.this), AndroidUtilities.statusBarHeight, ThemeEditorView.EditorAlert.access$1900(EditorAlert.this), 0);
                  this.ignoreLayout = false;
               }

               if (VERSION.SDK_INT >= 21) {
                  var2 = AndroidUtilities.statusBarHeight;
               } else {
                  var2 = 0;
               }

               var3 = Math.min(var3, var4 - var2);
               if (VERSION.SDK_INT >= 21) {
                  var2 = AndroidUtilities.statusBarHeight;
               } else {
                  var2 = 0;
               }

               var2 = var4 - var2 + AndroidUtilities.dp(8.0F) - var3;
               if (EditorAlert.this.listView.getPaddingTop() != var2) {
                  this.ignoreLayout = true;
                  EditorAlert.this.listView.getPaddingTop();
                  EditorAlert.this.listView.setPadding(0, var2, 0, AndroidUtilities.dp(48.0F));
                  if (EditorAlert.this.colorPicker.getVisibility() == 0) {
                     ThemeEditorView.EditorAlert var5 = EditorAlert.this;
                     var5.setScrollOffsetY(var5.listView.getPaddingTop());
                     EditorAlert.this.previousScrollPosition = 0;
                  }

                  this.ignoreLayout = false;
               }

               super.onMeasure(var1, MeasureSpec.makeMeasureSpec(var4, 1073741824));
            }

            public boolean onTouchEvent(MotionEvent var1) {
               boolean var2;
               if (!EditorAlert.this.isDismissed() && super.onTouchEvent(var1)) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               return var2;
            }

            public void requestLayout() {
               if (!this.ignoreLayout) {
                  super.requestLayout();
               }
            }
         };
         super.containerView.setWillNotDraw(false);
         ViewGroup var4 = super.containerView;
         int var5 = super.backgroundPaddingLeft;
         var4.setPadding(var5, 0, var5, 0);
         this.frameLayout = new FrameLayout(var2);
         this.frameLayout.setBackgroundColor(-1);
         this.searchField = new ThemeEditorView.EditorAlert.SearchField(var2);
         this.frameLayout.addView(this.searchField, LayoutHelper.createFrame(-1, -1, 51));
         this.listView = new RecyclerListView(var2) {
            protected boolean allowSelectChildAtPosition(float var1, float var2) {
               int var3 = EditorAlert.this.scrollOffsetY;
               int var4 = AndroidUtilities.dp(48.0F);
               int var5 = VERSION.SDK_INT;
               boolean var6 = false;
               if (var5 >= 21) {
                  var5 = AndroidUtilities.statusBarHeight;
               } else {
                  var5 = 0;
               }

               if (var2 >= (float)(var3 + var4 + var5)) {
                  var6 = true;
               }

               return var6;
            }
         };
         this.listView.setSelectorDrawableColor(251658240);
         this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0F));
         this.listView.setClipToPadding(false);
         RecyclerListView var6 = this.listView;
         LinearLayoutManager var12 = new LinearLayoutManager(this.getContext());
         this.layoutManager = var12;
         var6.setLayoutManager(var12);
         this.listView.setHorizontalScrollBarEnabled(false);
         this.listView.setVerticalScrollBarEnabled(false);
         super.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
         RecyclerListView var13 = this.listView;
         ThemeEditorView.EditorAlert.ListAdapter var11 = new ThemeEditorView.EditorAlert.ListAdapter(var2, var3);
         this.listAdapter = var11;
         var13.setAdapter(var11);
         this.searchAdapter = new ThemeEditorView.EditorAlert.SearchAdapter(var2);
         this.listView.setGlowColor(-657673);
         this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
         this.listView.setLayoutAnimation((LayoutAnimationController)null);
         this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ThemeEditorView$EditorAlert$kK2jW5xMCGw4an44hq65kfySCdU(this)));
         this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView var1, int var2, int var3) {
               EditorAlert.this.updateLayout();
            }
         });
         this.searchEmptyView = new EmptyTextProgressView(var2);
         this.searchEmptyView.setShowAtCenter(true);
         this.searchEmptyView.showTextView();
         this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131559943));
         this.listView.setEmptyView(this.searchEmptyView);
         super.containerView.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 52.0F, 0.0F, 0.0F));
         android.widget.FrameLayout.LayoutParams var7 = new android.widget.FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
         var7.topMargin = AndroidUtilities.dp(58.0F);
         this.shadow[0] = new View(var2);
         this.shadow[0].setBackgroundColor(301989888);
         this.shadow[0].setAlpha(0.0F);
         this.shadow[0].setTag(1);
         super.containerView.addView(this.shadow[0], var7);
         super.containerView.addView(this.frameLayout, LayoutHelper.createFrame(-1, 58, 51));
         this.colorPicker = new ThemeEditorView.EditorAlert.ColorPicker(var2);
         this.colorPicker.setVisibility(8);
         super.containerView.addView(this.colorPicker, LayoutHelper.createFrame(-1, -1, 1));
         var7 = new android.widget.FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
         var7.bottomMargin = AndroidUtilities.dp(48.0F);
         this.shadow[1] = new View(var2);
         this.shadow[1].setBackgroundColor(301989888);
         super.containerView.addView(this.shadow[1], var7);
         this.bottomSaveLayout = new FrameLayout(var2);
         this.bottomSaveLayout.setBackgroundColor(-1);
         super.containerView.addView(this.bottomSaveLayout, LayoutHelper.createFrame(-1, 48, 83));
         TextView var8 = new TextView(var2);
         var8.setTextSize(1, 14.0F);
         var8.setTextColor(-15095832);
         var8.setGravity(17);
         var8.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
         var8.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
         var8.setText(LocaleController.getString("CloseEditor", 2131559118).toUpperCase());
         var8.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.bottomSaveLayout.addView(var8, LayoutHelper.createFrame(-2, -1, 51));
         var8.setOnClickListener(new _$$Lambda$ThemeEditorView$EditorAlert$kRxzT12O1gEcsTUiGkZYFSscUt8(this));
         var8 = new TextView(var2);
         var8.setTextSize(1, 14.0F);
         var8.setTextColor(-15095832);
         var8.setGravity(17);
         var8.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
         var8.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
         var8.setText(LocaleController.getString("SaveTheme", 2131560627).toUpperCase());
         var8.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.bottomSaveLayout.addView(var8, LayoutHelper.createFrame(-2, -1, 53));
         var8.setOnClickListener(new _$$Lambda$ThemeEditorView$EditorAlert$_CO4an8qJdIEQQjX1P4ENdamV9E(this));
         this.bottomLayout = new FrameLayout(var2);
         this.bottomLayout.setVisibility(8);
         this.bottomLayout.setBackgroundColor(-1);
         super.containerView.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 83));
         this.cancelButton = new TextView(var2);
         this.cancelButton.setTextSize(1, 14.0F);
         this.cancelButton.setTextColor(-15095832);
         this.cancelButton.setGravity(17);
         this.cancelButton.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
         this.cancelButton.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
         this.cancelButton.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
         this.cancelButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.bottomLayout.addView(this.cancelButton, LayoutHelper.createFrame(-2, -1, 51));
         this.cancelButton.setOnClickListener(new _$$Lambda$ThemeEditorView$EditorAlert$e8sB4SzqRAAe3BbXeRhAVLL0Fkg(this));
         LinearLayout var10 = new LinearLayout(var2);
         var10.setOrientation(0);
         this.bottomLayout.addView(var10, LayoutHelper.createFrame(-2, -1, 53));
         this.defaultButtom = new TextView(var2);
         this.defaultButtom.setTextSize(1, 14.0F);
         this.defaultButtom.setTextColor(-15095832);
         this.defaultButtom.setGravity(17);
         this.defaultButtom.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
         this.defaultButtom.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
         this.defaultButtom.setText(LocaleController.getString("Default", 2131559225).toUpperCase());
         this.defaultButtom.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var10.addView(this.defaultButtom, LayoutHelper.createFrame(-2, -1, 51));
         this.defaultButtom.setOnClickListener(new _$$Lambda$ThemeEditorView$EditorAlert$KOpMpGNwWrKZ5XW39TuNtWWXkWM(this));
         TextView var9 = new TextView(var2);
         var9.setTextSize(1, 14.0F);
         var9.setTextColor(-15095832);
         var9.setGravity(17);
         var9.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
         var9.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
         var9.setText(LocaleController.getString("Save", 2131560626).toUpperCase());
         var9.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var10.addView(var9, LayoutHelper.createFrame(-2, -1, 51));
         var9.setOnClickListener(new _$$Lambda$ThemeEditorView$EditorAlert$NlmVbEdgSNku_2tKRx9Agp6b_e4(this));
      }

      // $FF: synthetic method
      static ViewGroup access$000(ThemeEditorView.EditorAlert var0) {
         return var0.containerView;
      }

      // $FF: synthetic method
      static ColorDrawable access$1400(ThemeEditorView.EditorAlert var0) {
         return var0.backDrawable;
      }

      // $FF: synthetic method
      static ViewGroup access$1500(ThemeEditorView.EditorAlert var0) {
         return var0.containerView;
      }

      // $FF: synthetic method
      static boolean access$1700(ThemeEditorView.EditorAlert var0) {
         return var0.isFullscreen;
      }

      // $FF: synthetic method
      static int access$1800(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingLeft;
      }

      // $FF: synthetic method
      static int access$1900(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingLeft;
      }

      // $FF: synthetic method
      static int access$2300(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingTop;
      }

      // $FF: synthetic method
      static int access$2400(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingTop;
      }

      // $FF: synthetic method
      static int access$2500(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingTop;
      }

      // $FF: synthetic method
      static boolean access$2600(ThemeEditorView.EditorAlert var0) {
         return var0.isFullscreen;
      }

      // $FF: synthetic method
      static int access$2700(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingTop;
      }

      // $FF: synthetic method
      static int access$2800(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingTop;
      }

      // $FF: synthetic method
      static int access$2900(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingTop;
      }

      // $FF: synthetic method
      static int access$3000(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingTop;
      }

      // $FF: synthetic method
      static int access$3200(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingLeft;
      }

      // $FF: synthetic method
      static int access$3300(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingTop;
      }

      // $FF: synthetic method
      static int access$3400(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingLeft;
      }

      // $FF: synthetic method
      static int access$3500(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingTop;
      }

      // $FF: synthetic method
      static int access$3600(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingLeft;
      }

      // $FF: synthetic method
      static int access$3700(ThemeEditorView.EditorAlert var0) {
         return var0.backgroundPaddingLeft;
      }

      private int getCurrentTop() {
         if (this.listView.getChildCount() != 0) {
            RecyclerListView var1 = this.listView;
            byte var2 = 0;
            View var6 = var1.getChildAt(0);
            RecyclerListView.Holder var3 = (RecyclerListView.Holder)this.listView.findContainingViewHolder(var6);
            if (var3 != null) {
               int var4 = this.listView.getPaddingTop();
               int var5 = var2;
               if (var3.getAdapterPosition() == 0) {
                  var5 = var2;
                  if (var6.getTop() >= 0) {
                     var5 = var6.getTop();
                  }
               }

               return var4 - var5;
            }
         }

         return -1000;
      }

      private void runShadowAnimation(final int var1, final boolean var2) {
         if (var2 && this.shadow[var1].getTag() != null || !var2 && this.shadow[var1].getTag() == null) {
            View var3 = this.shadow[var1];
            Integer var4;
            if (var2) {
               var4 = null;
            } else {
               var4 = 1;
            }

            var3.setTag(var4);
            if (var2) {
               this.shadow[var1].setVisibility(0);
            }

            AnimatorSet[] var7 = this.shadowAnimation;
            if (var7[var1] != null) {
               var7[var1].cancel();
            }

            this.shadowAnimation[var1] = new AnimatorSet();
            AnimatorSet var8 = this.shadowAnimation[var1];
            var3 = this.shadow[var1];
            Property var5 = View.ALPHA;
            float var6;
            if (var2) {
               var6 = 1.0F;
            } else {
               var6 = 0.0F;
            }

            var8.playTogether(new Animator[]{ObjectAnimator.ofFloat(var3, var5, new float[]{var6})});
            this.shadowAnimation[var1].setDuration(150L);
            this.shadowAnimation[var1].addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1x) {
                  if (EditorAlert.this.shadowAnimation[var1] != null && EditorAlert.this.shadowAnimation[var1].equals(var1x)) {
                     EditorAlert.this.shadowAnimation[var1] = null;
                  }

               }

               public void onAnimationEnd(Animator var1x) {
                  if (EditorAlert.this.shadowAnimation[var1] != null && EditorAlert.this.shadowAnimation[var1].equals(var1x)) {
                     if (!var2) {
                        EditorAlert.this.shadow[var1].setVisibility(4);
                     }

                     EditorAlert.this.shadowAnimation[var1] = null;
                  }

               }
            });
            this.shadowAnimation[var1].start();
         }

      }

      private void setColorPickerVisible(boolean var1) {
         float var2 = 0.0F;
         if (var1) {
            this.animationInProgress = true;
            this.colorPicker.setVisibility(0);
            this.bottomLayout.setVisibility(0);
            this.colorPicker.setAlpha(0.0F);
            this.bottomLayout.setAlpha(0.0F);
            this.previousScrollPosition = this.scrollOffsetY;
            AnimatorSet var3 = new AnimatorSet();
            var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.colorPicker, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.bottomLayout, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.listView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.frameLayout, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.shadow[0], View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.searchEmptyView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.bottomSaveLayout, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofInt(this, "scrollOffsetY", new int[]{this.listView.getPaddingTop()})});
            var3.setDuration(150L);
            var3.setInterpolator(ThemeEditorView.this.decelerateInterpolator);
            var3.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  EditorAlert.this.listView.setVisibility(4);
                  EditorAlert.this.searchField.setVisibility(4);
                  EditorAlert.this.bottomSaveLayout.setVisibility(4);
                  EditorAlert.this.animationInProgress = false;
               }
            });
            var3.start();
         } else {
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
            this.listView.setAlpha(0.0F);
            AnimatorSet var4 = new AnimatorSet();
            ObjectAnimator var5 = ObjectAnimator.ofFloat(this.colorPicker, View.ALPHA, new float[]{0.0F});
            ObjectAnimator var6 = ObjectAnimator.ofFloat(this.bottomLayout, View.ALPHA, new float[]{0.0F});
            ObjectAnimator var7 = ObjectAnimator.ofFloat(this.listView, View.ALPHA, new float[]{1.0F});
            ObjectAnimator var8 = ObjectAnimator.ofFloat(this.frameLayout, View.ALPHA, new float[]{1.0F});
            View[] var9 = this.shadow;
            View var10 = var9[0];
            Property var11 = View.ALPHA;
            if (var9[0].getTag() == null) {
               var2 = 1.0F;
            }

            var4.playTogether(new Animator[]{var5, var6, var7, var8, ObjectAnimator.ofFloat(var10, var11, new float[]{var2}), ObjectAnimator.ofFloat(this.searchEmptyView, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.bottomSaveLayout, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofInt(this, "scrollOffsetY", new int[]{this.previousScrollPosition})});
            var4.setDuration(150L);
            var4.setInterpolator(ThemeEditorView.this.decelerateInterpolator);
            var4.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (EditorAlert.this.listView.getAdapter() == EditorAlert.this.searchAdapter) {
                     EditorAlert.this.searchField.showKeyboard();
                  }

                  EditorAlert.this.colorPicker.setVisibility(8);
                  EditorAlert.this.bottomLayout.setVisibility(8);
                  EditorAlert.this.animationInProgress = false;
               }
            });
            var4.start();
            this.listAdapter.notifyItemChanged(ThemeEditorView.this.currentThemeDesriptionPosition);
         }

      }

      @SuppressLint({"NewApi"})
      private void updateLayout() {
         if (this.listView.getChildCount() > 0 && this.listView.getVisibility() == 0 && !this.animationInProgress) {
            View var1 = this.listView.getChildAt(0);
            RecyclerListView.Holder var2 = (RecyclerListView.Holder)this.listView.findContainingViewHolder(var1);
            int var3;
            if (this.listView.getVisibility() == 0 && !this.animationInProgress) {
               var3 = var1.getTop() - AndroidUtilities.dp(8.0F);
            } else {
               var3 = this.listView.getPaddingTop();
            }

            if (var3 > -AndroidUtilities.dp(1.0F) && var2 != null && var2.getAdapterPosition() == 0) {
               this.runShadowAnimation(0, false);
            } else {
               this.runShadowAnimation(0, true);
               var3 = 0;
            }

            if (this.scrollOffsetY != var3) {
               this.setScrollOffsetY(var3);
            }
         }

      }

      protected boolean canDismissWithSwipe() {
         return false;
      }

      public int getScrollOffsetY() {
         return this.scrollOffsetY;
      }

      // $FF: synthetic method
      public void lambda$new$0$ThemeEditorView$EditorAlert(View var1, int var2) {
         if (var2 != 0) {
            RecyclerView.Adapter var3 = this.listView.getAdapter();
            ThemeEditorView.EditorAlert.ListAdapter var4 = this.listAdapter;
            if (var3 == var4) {
               ThemeEditorView.this.currentThemeDesription = var4.getItem(var2 - 1);
            } else {
               ThemeEditorView.this.currentThemeDesription = this.searchAdapter.getItem(var2 - 1);
            }

            ThemeEditorView.this.currentThemeDesriptionPosition = var2;

            for(var2 = 0; var2 < ThemeEditorView.this.currentThemeDesription.size(); ++var2) {
               ThemeDescription var5 = (ThemeDescription)ThemeEditorView.this.currentThemeDesription.get(var2);
               if (var5.getCurrentKey().equals("chat_wallpaper")) {
                  ThemeEditorView.this.wallpaperUpdater.showAlert(true);
                  return;
               }

               var5.startEditing();
               if (var2 == 0) {
                  this.colorPicker.setColor(var5.getCurrentColor());
               }
            }

            this.setColorPickerVisible(true);
         }
      }

      // $FF: synthetic method
      public void lambda$new$1$ThemeEditorView$EditorAlert(View var1) {
         this.dismiss();
      }

      // $FF: synthetic method
      public void lambda$new$2$ThemeEditorView$EditorAlert(View var1) {
         Theme.saveCurrentTheme(ThemeEditorView.this.currentThemeName, true);
         this.setOnDismissListener((OnDismissListener)null);
         this.dismiss();
         ThemeEditorView.this.close();
      }

      // $FF: synthetic method
      public void lambda$new$3$ThemeEditorView$EditorAlert(View var1) {
         for(int var2 = 0; var2 < ThemeEditorView.this.currentThemeDesription.size(); ++var2) {
            ((ThemeDescription)ThemeEditorView.this.currentThemeDesription.get(var2)).setPreviousColor();
         }

         this.setColorPickerVisible(false);
      }

      // $FF: synthetic method
      public void lambda$new$4$ThemeEditorView$EditorAlert(View var1) {
         for(int var2 = 0; var2 < ThemeEditorView.this.currentThemeDesription.size(); ++var2) {
            ((ThemeDescription)ThemeEditorView.this.currentThemeDesription.get(var2)).setDefaultColor();
         }

         this.setColorPickerVisible(false);
      }

      // $FF: synthetic method
      public void lambda$new$5$ThemeEditorView$EditorAlert(View var1) {
         this.setColorPickerVisible(false);
      }

      @Keep
      public void setScrollOffsetY(int var1) {
         RecyclerListView var2 = this.listView;
         this.scrollOffsetY = var1;
         var2.setTopGlowOffset(var1);
         this.frameLayout.setTranslationY((float)this.scrollOffsetY);
         this.colorPicker.setTranslationY((float)this.scrollOffsetY);
         this.searchEmptyView.setTranslationY((float)this.scrollOffsetY);
         super.containerView.invalidate();
      }

      private class ColorPicker extends FrameLayout {
         private float alpha = 1.0F;
         private LinearGradient alphaGradient;
         private boolean alphaPressed;
         private Drawable circleDrawable;
         private Paint circlePaint;
         private boolean circlePressed;
         private EditTextBoldCursor[] colorEditText = new EditTextBoldCursor[4];
         private LinearGradient colorGradient;
         private float[] colorHSV = new float[]{0.0F, 0.0F, 1.0F};
         private boolean colorPressed;
         private Bitmap colorWheelBitmap;
         private Paint colorWheelPaint;
         private int colorWheelRadius;
         private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
         private float[] hsvTemp = new float[3];
         private LinearLayout linearLayout;
         private final int paramValueSliderWidth = AndroidUtilities.dp(20.0F);
         private Paint valueSliderPaint;

         public ColorPicker(Context var2) {
            super(var2);
            this.setWillNotDraw(false);
            this.circlePaint = new Paint(1);
            this.circleDrawable = var2.getResources().getDrawable(2131165520).mutate();
            this.colorWheelPaint = new Paint();
            this.colorWheelPaint.setAntiAlias(true);
            this.colorWheelPaint.setDither(true);
            this.valueSliderPaint = new Paint();
            this.valueSliderPaint.setAntiAlias(true);
            this.valueSliderPaint.setDither(true);
            this.linearLayout = new LinearLayout(var2);
            this.linearLayout.setOrientation(0);
            this.addView(this.linearLayout, LayoutHelper.createFrame(-2, -2, 49));

            for(final int var3 = 0; var3 < 4; ++var3) {
               this.colorEditText[var3] = new EditTextBoldCursor(var2);
               this.colorEditText[var3].setInputType(2);
               this.colorEditText[var3].setTextColor(-14606047);
               this.colorEditText[var3].setCursorColor(-14606047);
               this.colorEditText[var3].setCursorSize(AndroidUtilities.dp(20.0F));
               this.colorEditText[var3].setCursorWidth(1.5F);
               this.colorEditText[var3].setTextSize(1, 18.0F);
               this.colorEditText[var3].setBackgroundDrawable(Theme.createEditTextDrawable(var2, true));
               this.colorEditText[var3].setMaxLines(1);
               this.colorEditText[var3].setTag(var3);
               this.colorEditText[var3].setGravity(17);
               if (var3 == 0) {
                  this.colorEditText[var3].setHint("red");
               } else if (var3 == 1) {
                  this.colorEditText[var3].setHint("green");
               } else if (var3 == 2) {
                  this.colorEditText[var3].setHint("blue");
               } else if (var3 == 3) {
                  this.colorEditText[var3].setHint("alpha");
               }

               EditTextBoldCursor var4 = this.colorEditText[var3];
               byte var5;
               if (var3 == 3) {
                  var5 = 6;
               } else {
                  var5 = 5;
               }

               var4.setImeOptions(var5 | 268435456);
               LengthFilter var8 = new LengthFilter(3);
               this.colorEditText[var3].setFilters(new InputFilter[]{var8});
               LinearLayout var9 = this.linearLayout;
               EditTextBoldCursor var6 = this.colorEditText[var3];
               float var7;
               if (var3 != 3) {
                  var7 = 16.0F;
               } else {
                  var7 = 0.0F;
               }

               var9.addView(var6, LayoutHelper.createLinear(55, 36, 0.0F, 0.0F, var7, 0.0F));
               this.colorEditText[var3].addTextChangedListener(new TextWatcher() {
                  public void afterTextChanged(Editable var1) {
                     if (!EditorAlert.this.ignoreTextChange) {
                        EditorAlert.this.ignoreTextChange = true;
                        int var2 = Utilities.parseInt(var1.toString());
                        EditTextBoldCursor var3x;
                        int var4;
                        StringBuilder var7;
                        if (var2 < 0) {
                           var3x = ColorPicker.this.colorEditText[var3];
                           var7 = new StringBuilder();
                           var7.append("");
                           var7.append(0);
                           var3x.setText(var7.toString());
                           ColorPicker.this.colorEditText[var3].setSelection(ColorPicker.this.colorEditText[var3].length());
                           var4 = 0;
                        } else {
                           var4 = var2;
                           if (var2 > 255) {
                              var3x = ColorPicker.this.colorEditText[var3];
                              var7 = new StringBuilder();
                              var7.append("");
                              var7.append(255);
                              var3x.setText(var7.toString());
                              ColorPicker.this.colorEditText[var3].setSelection(ColorPicker.this.colorEditText[var3].length());
                              var4 = 255;
                           }
                        }

                        label37: {
                           int var5 = ColorPicker.this.getColor();
                           int var6 = var3;
                           if (var6 == 2) {
                              var5 &= -256;
                              var2 = var4 & 255;
                              var4 = var5;
                           } else if (var6 == 1) {
                              var2 = var5 & -65281;
                              var5 = (var4 & 255) << 8;
                              var4 = var2;
                              var2 = var5;
                           } else if (var6 == 0) {
                              var2 = var5 & -16711681;
                              var5 = (var4 & 255) << 16;
                              var4 = var2;
                              var2 = var5;
                           } else {
                              var2 = var5;
                              if (var6 != 3) {
                                 break label37;
                              }

                              var2 = var5 & 16777215;
                              var5 = (var4 & 255) << 24;
                              var4 = var2;
                              var2 = var5;
                           }

                           var2 |= var4;
                        }

                        ColorPicker.this.setColor(var2);

                        for(var4 = 0; var4 < ThemeEditorView.this.currentThemeDesription.size(); ++var4) {
                           ((ThemeDescription)ThemeEditorView.this.currentThemeDesription.get(var4)).setColor(ColorPicker.this.getColor(), false);
                        }

                        EditorAlert.this.ignoreTextChange = false;
                     }
                  }

                  public void beforeTextChanged(CharSequence var1, int var2, int var3x, int var4) {
                  }

                  public void onTextChanged(CharSequence var1, int var2, int var3x, int var4) {
                  }
               });
               this.colorEditText[var3].setOnEditorActionListener(_$$Lambda$ThemeEditorView$EditorAlert$ColorPicker$ajPoxH4sFpvaVlkZF92J0Sean_E.INSTANCE);
            }

         }

         private Bitmap createColorWheelBitmap(int var1, int var2) {
            Bitmap var3 = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
            int[] var4 = new int[13];
            float[] var5 = new float[]{0.0F, 1.0F, 1.0F};

            for(int var6 = 0; var6 < var4.length; ++var6) {
               var5[0] = (float)((var6 * 30 + 180) % 360);
               var4[var6] = Color.HSVToColor(var5);
            }

            var4[12] = var4[0];
            float var7 = (float)(var1 / 2);
            float var8 = (float)(var2 / 2);
            ComposeShader var9 = new ComposeShader(new SweepGradient(var7, var8, var4, (float[])null), new RadialGradient(var7, var8, (float)this.colorWheelRadius, -1, 16777215, TileMode.CLAMP), Mode.SRC_OVER);
            this.colorWheelPaint.setShader(var9);
            (new Canvas(var3)).drawCircle(var7, var8, (float)this.colorWheelRadius, this.colorWheelPaint);
            return var3;
         }

         private void drawPointerArrow(Canvas var1, int var2, int var3, int var4) {
            int var5 = AndroidUtilities.dp(13.0F);
            this.circleDrawable.setBounds(var2 - var5, var3 - var5, var2 + var5, var5 + var3);
            this.circleDrawable.draw(var1);
            this.circlePaint.setColor(-1);
            float var6 = (float)var2;
            float var7 = (float)var3;
            var1.drawCircle(var6, var7, (float)AndroidUtilities.dp(11.0F), this.circlePaint);
            this.circlePaint.setColor(var4);
            var1.drawCircle(var6, var7, (float)AndroidUtilities.dp(9.0F), this.circlePaint);
         }

         // $FF: synthetic method
         static boolean lambda$new$0(TextView var0, int var1, KeyEvent var2) {
            if (var1 == 6) {
               AndroidUtilities.hideKeyboard(var0);
               return true;
            } else {
               return false;
            }
         }

         private void startColorChange(boolean var1) {
            if (EditorAlert.this.startedColorChange != var1) {
               if (EditorAlert.this.colorChangeAnimation != null) {
                  EditorAlert.this.colorChangeAnimation.cancel();
               }

               EditorAlert.this.startedColorChange = var1;
               EditorAlert.this.colorChangeAnimation = new AnimatorSet();
               AnimatorSet var2 = EditorAlert.this.colorChangeAnimation;
               ColorDrawable var3 = ThemeEditorView.EditorAlert.access$1400(EditorAlert.this);
               Property var4 = AnimationProperties.COLOR_DRAWABLE_ALPHA;
               byte var5;
               if (var1) {
                  var5 = 0;
               } else {
                  var5 = 51;
               }

               ObjectAnimator var6 = ObjectAnimator.ofInt(var3, var4, new int[]{var5});
               ViewGroup var9 = ThemeEditorView.EditorAlert.access$1500(EditorAlert.this);
               Property var8 = View.ALPHA;
               float var7;
               if (var1) {
                  var7 = 0.2F;
               } else {
                  var7 = 1.0F;
               }

               var2.playTogether(new Animator[]{var6, ObjectAnimator.ofFloat(var9, var8, new float[]{var7})});
               EditorAlert.this.colorChangeAnimation.setDuration(150L);
               EditorAlert.this.colorChangeAnimation.setInterpolator(this.decelerateInterpolator);
               EditorAlert.this.colorChangeAnimation.start();
            }
         }

         public int getColor() {
            return Color.HSVToColor(this.colorHSV) & 16777215 | (int)(this.alpha * 255.0F) << 24;
         }

         protected void onDraw(Canvas var1) {
            int var2 = this.getWidth() / 2 - this.paramValueSliderWidth * 2;
            int var3 = this.getHeight() / 2 - AndroidUtilities.dp(8.0F);
            Bitmap var4 = this.colorWheelBitmap;
            int var5 = this.colorWheelRadius;
            var1.drawBitmap(var4, (float)(var2 - var5), (float)(var3 - var5), (Paint)null);
            double var6 = (double)((float)Math.toRadians((double)this.colorHSV[0]));
            double var8 = -Math.cos(var6);
            double var10 = (double)this.colorHSV[1];
            Double.isNaN(var10);
            double var12 = (double)this.colorWheelRadius;
            Double.isNaN(var12);
            int var14 = (int)(var8 * var10 * var12);
            var6 = -Math.sin(var6);
            float[] var15 = this.colorHSV;
            var12 = (double)var15[1];
            Double.isNaN(var12);
            var10 = (double)this.colorWheelRadius;
            Double.isNaN(var10);
            var5 = (int)(var6 * var12 * var10);
            float[] var22 = this.hsvTemp;
            var22[0] = var15[0];
            var22[1] = var15[1];
            var22[2] = 1.0F;
            this.drawPointerArrow(var1, var14 + var2, var5 + var3, Color.HSVToColor(var22));
            var5 = this.colorWheelRadius;
            var2 = var2 + var5 + this.paramValueSliderWidth;
            int var16 = var3 - var5;
            var3 = AndroidUtilities.dp(9.0F);
            var14 = this.colorWheelRadius * 2;
            float var17;
            float var18;
            float var19;
            float var20;
            TileMode var23;
            if (this.colorGradient == null) {
               var17 = (float)var2;
               var18 = (float)var16;
               var19 = (float)(var2 + var3);
               var20 = (float)(var16 + var14);
               var5 = Color.HSVToColor(this.hsvTemp);
               var23 = TileMode.CLAMP;
               this.colorGradient = new LinearGradient(var17, var18, var19, var20, new int[]{-16777216, var5}, (float[])null, var23);
            }

            this.valueSliderPaint.setShader(this.colorGradient);
            var17 = (float)var2;
            var18 = (float)var16;
            var20 = (float)(var2 + var3);
            var19 = (float)(var16 + var14);
            var1.drawRect(var17, var18, var20, var19, this.valueSliderPaint);
            var5 = var3 / 2;
            var22 = this.colorHSV;
            var20 = var22[2];
            var17 = (float)var14;
            this.drawPointerArrow(var1, var2 + var5, (int)(var20 * var17 + var18), Color.HSVToColor(var22));
            var14 = var2 + this.paramValueSliderWidth * 2;
            if (this.alphaGradient == null) {
               var2 = Color.HSVToColor(this.hsvTemp);
               float var21 = (float)var14;
               var20 = (float)(var14 + var3);
               var23 = TileMode.CLAMP;
               this.alphaGradient = new LinearGradient(var21, var18, var20, var19, new int[]{var2, var2 & 16777215}, (float[])null, var23);
            }

            this.valueSliderPaint.setShader(this.alphaGradient);
            var1.drawRect((float)var14, var18, (float)(var3 + var14), var19, this.valueSliderPaint);
            this.drawPointerArrow(var1, var14 + var5, (int)(var18 + (1.0F - this.alpha) * var17), Color.HSVToColor(this.colorHSV) & 16777215 | (int)(this.alpha * 255.0F) << 24);
         }

         protected void onMeasure(int var1, int var2) {
            int var3 = Math.min(MeasureSpec.getSize(var1), MeasureSpec.getSize(var2));
            this.measureChild(this.linearLayout, var1, var2);
            this.setMeasuredDimension(var3, var3);
         }

         protected void onSizeChanged(int var1, int var2, int var3, int var4) {
            this.colorWheelRadius = Math.max(1, var1 / 2 - this.paramValueSliderWidth * 2 - AndroidUtilities.dp(20.0F));
            var1 = this.colorWheelRadius;
            this.colorWheelBitmap = this.createColorWheelBitmap(var1 * 2, var1 * 2);
            this.colorGradient = null;
            this.alphaGradient = null;
         }

         public boolean onTouchEvent(MotionEvent var1) {
            int var2 = var1.getAction();
            if (var2 != 0) {
               if (var2 == 1) {
                  this.alphaPressed = false;
                  this.colorPressed = false;
                  this.circlePressed = false;
                  this.startColorChange(false);
                  return super.onTouchEvent(var1);
               }

               if (var2 != 2) {
                  return super.onTouchEvent(var1);
               }
            }

            int var3 = (int)var1.getX();
            var2 = (int)var1.getY();
            int var4 = this.getWidth() / 2 - this.paramValueSliderWidth * 2;
            int var5 = this.getHeight() / 2 - AndroidUtilities.dp(8.0F);
            int var6 = var3 - var4;
            int var7 = var2 - var5;
            double var8 = Math.sqrt((double)(var6 * var6 + var7 * var7));
            int var10;
            if (this.circlePressed || !this.alphaPressed && !this.colorPressed && var8 <= (double)this.colorWheelRadius) {
               var10 = this.colorWheelRadius;
               double var11 = var8;
               if (var8 > (double)var10) {
                  var11 = (double)var10;
               }

               this.circlePressed = true;
               this.colorHSV[0] = (float)(Math.toDegrees(Math.atan2((double)var7, (double)var6)) + 180.0D);
               float[] var16 = this.colorHSV;
               var8 = (double)this.colorWheelRadius;
               Double.isNaN(var8);
               var16[1] = Math.max(0.0F, Math.min(1.0F, (float)(var11 / var8)));
               this.colorGradient = null;
               this.alphaGradient = null;
            }

            float var14;
            label133: {
               if (!this.colorPressed) {
                  if (this.circlePressed || this.alphaPressed) {
                     break label133;
                  }

                  var7 = this.colorWheelRadius;
                  var10 = this.paramValueSliderWidth;
                  if (var3 < var4 + var7 + var10 || var3 > var4 + var7 + var10 * 2 || var2 < var5 - var7 || var2 > var7 + var5) {
                     break label133;
                  }
               }

               var7 = this.colorWheelRadius;
               float var13 = (float)(var2 - (var5 - var7)) / ((float)var7 * 2.0F);
               if (var13 < 0.0F) {
                  var14 = 0.0F;
               } else {
                  var14 = var13;
                  if (var13 > 1.0F) {
                     var14 = 1.0F;
                  }
               }

               this.colorHSV[2] = var14;
               this.colorPressed = true;
            }

            label135: {
               if (!this.alphaPressed) {
                  if (this.circlePressed || this.colorPressed) {
                     break label135;
                  }

                  var10 = this.colorWheelRadius;
                  var7 = this.paramValueSliderWidth;
                  if (var3 < var4 + var10 + var7 * 3 || var3 > var4 + var10 + var7 * 4 || var2 < var5 - var10 || var2 > var10 + var5) {
                     break label135;
                  }
               }

               var4 = this.colorWheelRadius;
               this.alpha = 1.0F - (float)(var2 - (var5 - var4)) / ((float)var4 * 2.0F);
               var14 = this.alpha;
               if (var14 < 0.0F) {
                  this.alpha = 0.0F;
               } else if (var14 > 1.0F) {
                  this.alpha = 1.0F;
               }

               this.alphaPressed = true;
            }

            if (this.alphaPressed || this.colorPressed || this.circlePressed) {
               this.startColorChange(true);
               var5 = this.getColor();

               for(var2 = 0; var2 < ThemeEditorView.this.currentThemeDesription.size(); ++var2) {
                  ((ThemeDescription)ThemeEditorView.this.currentThemeDesription.get(var2)).setColor(var5, false);
               }

               var3 = Color.red(var5);
               var2 = Color.green(var5);
               var4 = Color.blue(var5);
               var5 = Color.alpha(var5);
               if (!EditorAlert.this.ignoreTextChange) {
                  EditorAlert.this.ignoreTextChange = true;
                  EditTextBoldCursor var15 = this.colorEditText[0];
                  StringBuilder var17 = new StringBuilder();
                  var17.append("");
                  var17.append(var3);
                  var15.setText(var17.toString());
                  EditTextBoldCursor var18 = this.colorEditText[1];
                  StringBuilder var20 = new StringBuilder();
                  var20.append("");
                  var20.append(var2);
                  var18.setText(var20.toString());
                  var18 = this.colorEditText[2];
                  var20 = new StringBuilder();
                  var20.append("");
                  var20.append(var4);
                  var18.setText(var20.toString());
                  var15 = this.colorEditText[3];
                  var17 = new StringBuilder();
                  var17.append("");
                  var17.append(var5);
                  var15.setText(var17.toString());

                  for(var2 = 0; var2 < 4; ++var2) {
                     EditTextBoldCursor[] var19 = this.colorEditText;
                     var19[var2].setSelection(var19[var2].length());
                  }

                  EditorAlert.this.ignoreTextChange = false;
               }

               this.invalidate();
            }

            return true;
         }

         public void setColor(int var1) {
            int var2 = Color.red(var1);
            int var3 = Color.green(var1);
            int var4 = Color.blue(var1);
            int var5 = Color.alpha(var1);
            if (!EditorAlert.this.ignoreTextChange) {
               EditorAlert.this.ignoreTextChange = true;
               EditTextBoldCursor var6 = this.colorEditText[0];
               StringBuilder var7 = new StringBuilder();
               var7.append("");
               var7.append(var2);
               var6.setText(var7.toString());
               EditTextBoldCursor var9 = this.colorEditText[1];
               StringBuilder var8 = new StringBuilder();
               var8.append("");
               var8.append(var3);
               var9.setText(var8.toString());
               var9 = this.colorEditText[2];
               var8 = new StringBuilder();
               var8.append("");
               var8.append(var4);
               var9.setText(var8.toString());
               var9 = this.colorEditText[3];
               var8 = new StringBuilder();
               var8.append("");
               var8.append(var5);
               var9.setText(var8.toString());

               for(var3 = 0; var3 < 4; ++var3) {
                  EditTextBoldCursor[] var10 = this.colorEditText;
                  var10[var3].setSelection(var10[var3].length());
               }

               EditorAlert.this.ignoreTextChange = false;
            }

            this.alphaGradient = null;
            this.colorGradient = null;
            this.alpha = (float)var5 / 255.0F;
            Color.colorToHSV(var1, this.colorHSV);
            this.invalidate();
         }
      }

      private class ListAdapter extends RecyclerListView.SelectionAdapter {
         private Context context;
         private int currentCount;
         private ArrayList items = new ArrayList();
         private HashMap itemsMap = new HashMap();

         public ListAdapter(Context var2, ThemeDescription[] var3) {
            this.context = var2;

            for(int var4 = 0; var4 < var3.length; ++var4) {
               ThemeDescription var5 = var3[var4];
               String var6 = var5.getCurrentKey();
               ArrayList var8 = (ArrayList)this.itemsMap.get(var6);
               ArrayList var7 = var8;
               if (var8 == null) {
                  var7 = new ArrayList();
                  this.itemsMap.put(var6, var7);
                  this.items.add(var7);
               }

               var7.add(var5);
            }

         }

         public ArrayList getItem(int var1) {
            return var1 >= 0 && var1 < this.items.size() ? (ArrayList)this.items.get(var1) : null;
         }

         public int getItemCount() {
            int var1;
            if (this.items.isEmpty()) {
               var1 = 0;
            } else {
               var1 = this.items.size() + 1;
            }

            return var1;
         }

         public int getItemViewType(int var1) {
            return var1 == 0 ? 1 : 0;
         }

         public boolean isEnabled(RecyclerView.ViewHolder var1) {
            return true;
         }

         public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
            if (var1.getItemViewType() == 0) {
               ArrayList var3 = (ArrayList)this.items.get(var2 - 1);
               var2 = 0;
               ThemeDescription var4 = (ThemeDescription)var3.get(0);
               if (!var4.getCurrentKey().equals("chat_wallpaper")) {
                  var2 = var4.getSetColor();
               }

               ((TextColorThemeCell)var1.itemView).setTextAndColor(var4.getTitle(), var2);
            }

         }

         public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
            Object var3;
            if (var2 != 0) {
               var3 = new View(this.context);
               ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0F)));
            } else {
               var3 = new TextColorThemeCell(this.context);
               ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            }

            return new RecyclerListView.Holder((View)var3);
         }
      }

      public class SearchAdapter extends RecyclerListView.SelectionAdapter {
         private Context context;
         private int currentCount;
         private int lastSearchId;
         private String lastSearchText;
         private ArrayList searchNames = new ArrayList();
         private ArrayList searchResult = new ArrayList();
         private Runnable searchRunnable;

         public SearchAdapter(Context var2) {
            this.context = var2;
         }

         private void searchDialogsInternal(String var1, int var2) {
            Exception var10000;
            label107: {
               String var3;
               boolean var10001;
               ArrayList var20;
               try {
                  var3 = var1.trim().toLowerCase();
                  if (var3.length() == 0) {
                     this.lastSearchId = -1;
                     var20 = new ArrayList();
                     ArrayList var24 = new ArrayList();
                     this.updateSearchResults(var20, var24, this.lastSearchId);
                     return;
                  }
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label107;
               }

               label96: {
                  label104: {
                     String var4;
                     try {
                        var4 = LocaleController.getInstance().getTranslitString(var3);
                        if (var3.equals(var4)) {
                           break label104;
                        }
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label107;
                     }

                     var1 = var4;

                     try {
                        if (var4.length() != 0) {
                           break label96;
                        }
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label107;
                     }
                  }

                  var1 = null;
               }

               byte var5;
               if (var1 != null) {
                  var5 = 1;
               } else {
                  var5 = 0;
               }

               String[] var23;
               try {
                  var23 = new String[var5 + 1];
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label107;
               }

               var23[0] = var3;
               if (var1 != null) {
                  var23[1] = var1;
               }

               ArrayList var6;
               int var7;
               try {
                  var6 = new ArrayList();
                  var20 = new ArrayList();
                  var7 = EditorAlert.this.listAdapter.items.size();
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label107;
               }

               int var25 = 0;

               label77:
               while(true) {
                  if (var25 >= var7) {
                     try {
                        this.updateSearchResults(var6, var20, var2);
                        return;
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break;
                     }
                  }

                  String var8;
                  int var9;
                  ArrayList var22;
                  try {
                     var22 = (ArrayList)EditorAlert.this.listAdapter.items.get(var25);
                     var8 = ((ThemeDescription)var22.get(0)).getCurrentKey().toLowerCase();
                     var9 = var23.length;
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break;
                  }

                  for(int var10 = 0; var10 < var9; ++var10) {
                     String var11 = var23[var10];

                     try {
                        if (var8.contains(var11)) {
                           var6.add(var22);
                           var20.add(this.generateSearchName(var8, var11));
                           break;
                        }
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label77;
                     }
                  }

                  ++var25;
               }
            }

            Exception var21 = var10000;
            FileLog.e((Throwable)var21);
         }

         private void updateSearchResults(ArrayList var1, ArrayList var2, int var3) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ThemeEditorView$EditorAlert$SearchAdapter$p_mG9tNYo33F1pFcBkO4OWkissQ(this, var3, var1, var2));
         }

         public CharSequence generateSearchName(String var1, String var2) {
            if (TextUtils.isEmpty(var1)) {
               return "";
            } else {
               SpannableStringBuilder var3 = new SpannableStringBuilder();
               String var4 = var1.trim();
               var1 = var4.toLowerCase();
               int var5 = 0;

               while(true) {
                  int var6 = var1.indexOf(var2, var5);
                  if (var6 == -1) {
                     if (var5 != -1 && var5 < var4.length()) {
                        var3.append(var4.substring(var5));
                     }

                     return var3;
                  }

                  int var7 = var2.length() + var6;
                  if (var5 != 0 && var5 != var6 + 1) {
                     var3.append(var4.substring(var5, var6));
                  } else if (var5 == 0 && var6 != 0) {
                     var3.append(var4.substring(0, var6));
                  }

                  String var8 = var4.substring(var6, Math.min(var4.length(), var7));
                  if (var8.startsWith(" ")) {
                     var3.append(" ");
                  }

                  var8 = var8.trim();
                  var5 = var3.length();
                  var3.append(var8);
                  var3.setSpan(new ForegroundColorSpan(-11697229), var5, var8.length() + var5, 33);
                  var5 = var7;
               }
            }
         }

         public ArrayList getItem(int var1) {
            return var1 >= 0 && var1 < this.searchResult.size() ? (ArrayList)this.searchResult.get(var1) : null;
         }

         public int getItemCount() {
            int var1;
            if (this.searchResult.isEmpty()) {
               var1 = 0;
            } else {
               var1 = this.searchResult.size() + 1;
            }

            return var1;
         }

         public int getItemViewType(int var1) {
            return var1 == 0 ? 1 : 0;
         }

         public boolean isEnabled(RecyclerView.ViewHolder var1) {
            return true;
         }

         // $FF: synthetic method
         public void lambda$searchDialogs$1$ThemeEditorView$EditorAlert$SearchAdapter(String var1, int var2) {
            this.searchDialogsInternal(var1, var2);
         }

         // $FF: synthetic method
         public void lambda$updateSearchResults$0$ThemeEditorView$EditorAlert$SearchAdapter(int var1, ArrayList var2, ArrayList var3) {
            if (var1 == this.lastSearchId) {
               ThemeEditorView.EditorAlert var4;
               if (EditorAlert.this.listView.getAdapter() != EditorAlert.this.searchAdapter) {
                  var4 = EditorAlert.this;
                  var4.topBeforeSwitch = var4.getCurrentTop();
                  EditorAlert.this.listView.setAdapter(EditorAlert.this.searchAdapter);
                  EditorAlert.this.searchAdapter.notifyDataSetChanged();
               }

               boolean var5 = this.searchResult.isEmpty();
               boolean var6 = true;
               boolean var7;
               if (!var5 && var2.isEmpty()) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               if (!this.searchResult.isEmpty() || !var2.isEmpty()) {
                  var6 = false;
               }

               if (var7) {
                  var4 = EditorAlert.this;
                  var4.topBeforeSwitch = var4.getCurrentTop();
               }

               this.searchResult = var2;
               this.searchNames = var3;
               this.notifyDataSetChanged();
               if (!var6 && !var7 && EditorAlert.this.topBeforeSwitch > 0) {
                  EditorAlert.this.layoutManager.scrollToPositionWithOffset(0, -EditorAlert.this.topBeforeSwitch);
                  EditorAlert.this.topBeforeSwitch = -1000;
               }

               EditorAlert.this.searchEmptyView.showTextView();
            }
         }

         public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
            if (var1.getItemViewType() == 0) {
               ArrayList var3 = this.searchResult;
               int var4 = var2 - 1;
               var3 = (ArrayList)var3.get(var4);
               var2 = 0;
               ThemeDescription var5 = (ThemeDescription)var3.get(0);
               if (!var5.getCurrentKey().equals("chat_wallpaper")) {
                  var2 = var5.getSetColor();
               }

               ((TextColorThemeCell)var1.itemView).setTextAndColor((CharSequence)this.searchNames.get(var4), var2);
            }

         }

         public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
            Object var3;
            if (var2 != 0) {
               var3 = new View(this.context);
               ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0F)));
            } else {
               var3 = new TextColorThemeCell(this.context);
               ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            }

            return new RecyclerListView.Holder((View)var3);
         }

         public void searchDialogs(String var1) {
            if (var1 == null || !var1.equals(this.lastSearchText)) {
               this.lastSearchText = var1;
               if (this.searchRunnable != null) {
                  Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                  this.searchRunnable = null;
               }

               if (var1 != null && var1.length() != 0) {
                  int var2 = this.lastSearchId + 1;
                  this.lastSearchId = var2;
                  this.searchRunnable = new _$$Lambda$ThemeEditorView$EditorAlert$SearchAdapter$QO0_7n9Zk7XEogLqnxXJbl4gmjs(this, var1, var2);
                  Utilities.searchQueue.postRunnable(this.searchRunnable, 300L);
               } else {
                  this.searchResult.clear();
                  ThemeEditorView.EditorAlert var3 = EditorAlert.this;
                  var3.topBeforeSwitch = var3.getCurrentTop();
                  this.lastSearchId = -1;
                  this.notifyDataSetChanged();
               }

            }
         }
      }

      private class SearchField extends FrameLayout {
         private View backgroundView;
         private ImageView clearSearchImageView;
         private CloseProgressDrawable2 progressDrawable;
         private View searchBackground;
         private EditTextBoldCursor searchEditText;
         private ImageView searchIconImageView;

         public SearchField(Context var2) {
            super(var2);
            this.searchBackground = new View(var2);
            this.searchBackground.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0F), -854795));
            this.addView(this.searchBackground, LayoutHelper.createFrame(-1, 36.0F, 51, 14.0F, 11.0F, 14.0F, 0.0F));
            this.searchIconImageView = new ImageView(var2);
            this.searchIconImageView.setScaleType(ScaleType.CENTER);
            this.searchIconImageView.setImageResource(2131165834);
            this.searchIconImageView.setColorFilter(new PorterDuffColorFilter(-6182737, Mode.MULTIPLY));
            this.addView(this.searchIconImageView, LayoutHelper.createFrame(36, 36.0F, 51, 16.0F, 11.0F, 0.0F, 0.0F));
            this.clearSearchImageView = new ImageView(var2);
            this.clearSearchImageView.setScaleType(ScaleType.CENTER);
            ImageView var3 = this.clearSearchImageView;
            CloseProgressDrawable2 var4 = new CloseProgressDrawable2();
            this.progressDrawable = var4;
            var3.setImageDrawable(var4);
            this.progressDrawable.setSide(AndroidUtilities.dp(7.0F));
            this.clearSearchImageView.setScaleX(0.1F);
            this.clearSearchImageView.setScaleY(0.1F);
            this.clearSearchImageView.setAlpha(0.0F);
            this.clearSearchImageView.setColorFilter(new PorterDuffColorFilter(-6182737, Mode.MULTIPLY));
            this.addView(this.clearSearchImageView, LayoutHelper.createFrame(36, 36.0F, 53, 14.0F, 11.0F, 14.0F, 0.0F));
            this.clearSearchImageView.setOnClickListener(new _$$Lambda$ThemeEditorView$EditorAlert$SearchField$oyMCfmJu6kX9C4_2783iB9VXPBE(this));
            this.searchEditText = new EditTextBoldCursor(var2) {
               public boolean dispatchTouchEvent(MotionEvent var1) {
                  MotionEvent var2 = MotionEvent.obtain(var1);
                  var2.setLocation(var2.getRawX(), var2.getRawY() - ThemeEditorView.EditorAlert.access$000(EditorAlert.this).getTranslationY());
                  EditorAlert.this.listView.dispatchTouchEvent(var2);
                  var2.recycle();
                  return super.dispatchTouchEvent(var1);
               }
            };
            this.searchEditText.setTextSize(1, 16.0F);
            this.searchEditText.setHintTextColor(-6774617);
            this.searchEditText.setTextColor(-14540254);
            this.searchEditText.setBackgroundDrawable((Drawable)null);
            this.searchEditText.setPadding(0, 0, 0, 0);
            this.searchEditText.setMaxLines(1);
            this.searchEditText.setLines(1);
            this.searchEditText.setSingleLine(true);
            this.searchEditText.setImeOptions(268435459);
            this.searchEditText.setHint(LocaleController.getString("Search", 2131560640));
            this.searchEditText.setCursorColor(-11491093);
            this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0F));
            this.searchEditText.setCursorWidth(1.5F);
            this.addView(this.searchEditText, LayoutHelper.createFrame(-1, 40.0F, 51, 54.0F, 9.0F, 46.0F, 0.0F));
            this.searchEditText.addTextChangedListener(new TextWatcher() {
               public void afterTextChanged(Editable var1) {
                  int var2 = SearchField.this.searchEditText.length();
                  boolean var3 = true;
                  boolean var7;
                  if (var2 > 0) {
                     var7 = true;
                  } else {
                     var7 = false;
                  }

                  float var4 = SearchField.this.clearSearchImageView.getAlpha();
                  float var5 = 0.0F;
                  if (var4 == 0.0F) {
                     var3 = false;
                  }

                  if (var7 != var3) {
                     ViewPropertyAnimator var6 = SearchField.this.clearSearchImageView.animate();
                     var4 = 1.0F;
                     if (var7) {
                        var5 = 1.0F;
                     }

                     var6 = var6.alpha(var5).setDuration(150L);
                     if (var7) {
                        var5 = 1.0F;
                     } else {
                        var5 = 0.1F;
                     }

                     var6 = var6.scaleX(var5);
                     if (var7) {
                        var5 = var4;
                     } else {
                        var5 = 0.1F;
                     }

                     var6.scaleY(var5).start();
                  }

                  String var8 = SearchField.this.searchEditText.getText().toString();
                  if (var8.length() != 0) {
                     if (EditorAlert.this.searchEmptyView != null) {
                        EditorAlert.this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131559943));
                     }
                  } else if (EditorAlert.this.listView.getAdapter() != EditorAlert.this.listAdapter) {
                     var2 = EditorAlert.this.getCurrentTop();
                     EditorAlert.this.searchEmptyView.setText(LocaleController.getString("NoChats", 2131559918));
                     EditorAlert.this.searchEmptyView.showTextView();
                     EditorAlert.this.listView.setAdapter(EditorAlert.this.listAdapter);
                     EditorAlert.this.listAdapter.notifyDataSetChanged();
                     if (var2 > 0) {
                        EditorAlert.this.layoutManager.scrollToPositionWithOffset(0, -var2);
                     }
                  }

                  if (EditorAlert.this.searchAdapter != null) {
                     EditorAlert.this.searchAdapter.searchDialogs(var8);
                  }

               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
            this.searchEditText.setOnEditorActionListener(new _$$Lambda$ThemeEditorView$EditorAlert$SearchField$7PNsSR7Iu_AOTIs_B2ibPMDGudM(this));
         }

         public void hideKeyboard() {
            AndroidUtilities.hideKeyboard(this.searchEditText);
         }

         // $FF: synthetic method
         public void lambda$new$0$ThemeEditorView$EditorAlert$SearchField(View var1) {
            this.searchEditText.setText("");
            AndroidUtilities.showKeyboard(this.searchEditText);
         }

         // $FF: synthetic method
         public boolean lambda$new$1$ThemeEditorView$EditorAlert$SearchField(TextView var1, int var2, KeyEvent var3) {
            if (var3 != null && (var3.getAction() == 1 && var3.getKeyCode() == 84 || var3.getAction() == 0 && var3.getKeyCode() == 66)) {
               AndroidUtilities.hideKeyboard(this.searchEditText);
            }

            return false;
         }

         public void requestDisallowInterceptTouchEvent(boolean var1) {
            super.requestDisallowInterceptTouchEvent(var1);
         }

         public void showKeyboard() {
            this.searchEditText.requestFocus();
            AndroidUtilities.showKeyboard(this.searchEditText);
         }
      }
   }
}
