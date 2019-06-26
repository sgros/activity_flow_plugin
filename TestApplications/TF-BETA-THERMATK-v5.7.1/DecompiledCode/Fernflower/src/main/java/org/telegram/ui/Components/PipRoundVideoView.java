package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Property;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.Keep;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;

public class PipRoundVideoView implements NotificationCenter.NotificationCenterDelegate {
   @SuppressLint({"StaticFieldLeak"})
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
   private RectF rect = new RectF();
   private TextureView textureView;
   private int videoHeight;
   private int videoWidth;
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
            int var1 = getSideCoord(true, 0, 0.0F, this.videoWidth);
            int var2 = getSideCoord(true, 1, 0.0F, this.videoWidth);
            var3 = getSideCoord(false, 0, 0.0F, this.videoHeight);
            var4 = getSideCoord(false, 1, 0.0F, this.videoHeight);
            var5 = this.preferences.edit();
            var6 = AndroidUtilities.dp(20.0F);
            if (Math.abs(var1 - this.windowLayoutParams.x) > var6) {
               int var7 = this.windowLayoutParams.x;
               if (var7 >= 0 || var7 <= -this.videoWidth / 4) {
                  if (Math.abs(var2 - this.windowLayoutParams.x) > var6) {
                     var7 = this.windowLayoutParams.x;
                     int var8 = AndroidUtilities.displaySize.x;
                     int var9 = this.videoWidth;
                     if (var7 <= var8 - var9 || var7 >= var8 - var9 / 4 * 3) {
                        if (this.windowView.getAlpha() != 1.0F) {
                           var10 = new ArrayList();
                           if (this.windowLayoutParams.x < 0) {
                              var10.add(ObjectAnimator.ofInt(this, "x", new int[]{-this.videoWidth}));
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
                  PipRoundVideoView.this.close(false);
                  if (PipRoundVideoView.this.onCloseRunnable != null) {
                     PipRoundVideoView.this.onCloseRunnable.run();
                  }

               }
            });
         }

         var13.playTogether(var11);
         var13.start();
      }

   }

   public static PipRoundVideoView getInstance() {
      return instance;
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

   private void runShowHideAnimation(final boolean var1) {
      AnimatorSet var2 = this.hideShowAnimation;
      if (var2 != null) {
         var2.cancel();
      }

      this.hideShowAnimation = new AnimatorSet();
      var2 = this.hideShowAnimation;
      FrameLayout var3 = this.windowView;
      Property var4 = View.ALPHA;
      float var5 = 1.0F;
      float var6;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.0F;
      }

      ObjectAnimator var9 = ObjectAnimator.ofFloat(var3, var4, new float[]{var6});
      var3 = this.windowView;
      Property var7 = View.SCALE_X;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.8F;
      }

      ObjectAnimator var8 = ObjectAnimator.ofFloat(var3, var7, new float[]{var6});
      var3 = this.windowView;
      var7 = View.SCALE_Y;
      if (var1) {
         var6 = var5;
      } else {
         var6 = 0.8F;
      }

      var2.playTogether(new Animator[]{var9, var8, ObjectAnimator.ofFloat(var3, var7, new float[]{var6})});
      this.hideShowAnimation.setDuration(150L);
      if (this.decelerateInterpolator == null) {
         this.decelerateInterpolator = new DecelerateInterpolator();
      }

      this.hideShowAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (var1x.equals(PipRoundVideoView.this.hideShowAnimation)) {
               PipRoundVideoView.this.hideShowAnimation = null;
            }

         }

         public void onAnimationEnd(Animator var1x) {
            if (var1x.equals(PipRoundVideoView.this.hideShowAnimation)) {
               if (!var1) {
                  PipRoundVideoView.this.close(false);
               }

               PipRoundVideoView.this.hideShowAnimation = null;
            }

         }
      });
      this.hideShowAnimation.setInterpolator(this.decelerateInterpolator);
      this.hideShowAnimation.start();
   }

   public void close(boolean var1) {
      if (var1) {
         TextureView var2 = this.textureView;
         if (var2 != null && var2.getParent() != null) {
            if (this.textureView.getWidth() > 0 && this.textureView.getHeight() > 0) {
               this.bitmap = Bitmaps.createBitmap(this.textureView.getWidth(), this.textureView.getHeight(), Config.ARGB_8888);
            }

            try {
               this.textureView.getBitmap(this.bitmap);
            } catch (Throwable var5) {
               this.bitmap = null;
            }

            this.imageView.setImageBitmap(this.bitmap);

            try {
               this.aspectRatioFrameLayout.removeView(this.textureView);
            } catch (Exception var4) {
            }

            this.imageView.setVisibility(0);
            this.runShowHideAnimation(false);
         }
      } else {
         if (this.bitmap != null) {
            this.imageView.setImageDrawable((Drawable)null);
            this.bitmap.recycle();
            this.bitmap = null;
         }

         try {
            this.windowManager.removeView(this.windowView);
         } catch (Exception var3) {
         }

         if (instance == this) {
            instance = null;
         }

         this.parentActivity = null;
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
      }

   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.messagePlayingProgressDidChanged) {
         AspectRatioFrameLayout var4 = this.aspectRatioFrameLayout;
         if (var4 != null) {
            var4.invalidate();
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
      int var1 = this.preferences.getInt("sidex", 1);
      int var2 = this.preferences.getInt("sidey", 0);
      float var3 = this.preferences.getFloat("px", 0.0F);
      float var4 = this.preferences.getFloat("py", 0.0F);
      this.windowLayoutParams.x = getSideCoord(true, var1, var3, this.videoWidth);
      this.windowLayoutParams.y = getSideCoord(false, var2, var4, this.videoHeight);
      this.windowManager.updateViewLayout(this.windowView, this.windowLayoutParams);
   }

   @Keep
   public void setX(int var1) {
      LayoutParams var2 = this.windowLayoutParams;
      var2.x = var1;

      try {
         this.windowManager.updateViewLayout(this.windowView, var2);
      } catch (Exception var3) {
      }

   }

   @Keep
   public void setY(int var1) {
      LayoutParams var2 = this.windowLayoutParams;
      var2.y = var1;

      try {
         this.windowManager.updateViewLayout(this.windowView, var2);
      } catch (Exception var3) {
      }

   }

   public void show(Activity var1, Runnable var2) {
      if (var1 != null) {
         instance = this;
         this.onCloseRunnable = var2;
         this.windowView = new FrameLayout(var1) {
            private boolean dragging;
            private boolean startDragging;
            private float startX;
            private float startY;

            protected void onDraw(Canvas var1) {
               Drawable var2 = Theme.chat_roundVideoShadow;
               if (var2 != null) {
                  var2.setAlpha((int)(this.getAlpha() * 255.0F));
                  Theme.chat_roundVideoShadow.setBounds(AndroidUtilities.dp(1.0F), AndroidUtilities.dp(2.0F), AndroidUtilities.dp(125.0F), AndroidUtilities.dp(125.0F));
                  Theme.chat_roundVideoShadow.draw(var1);
                  Theme.chat_docBackPaint.setColor(Theme.getColor("chat_inBubble"));
                  Theme.chat_docBackPaint.setAlpha((int)(this.getAlpha() * 255.0F));
                  var1.drawCircle((float)AndroidUtilities.dp(63.0F), (float)AndroidUtilities.dp(63.0F), (float)AndroidUtilities.dp(59.5F), Theme.chat_docBackPaint);
               }

            }

            public boolean onInterceptTouchEvent(MotionEvent var1) {
               if (var1.getAction() == 0) {
                  this.startX = var1.getRawX();
                  this.startY = var1.getRawY();
                  this.startDragging = true;
               }

               return true;
            }

            public boolean onTouchEvent(MotionEvent var1) {
               if (!this.startDragging && !this.dragging) {
                  return false;
               } else {
                  float var2 = var1.getRawX();
                  float var3 = var1.getRawY();
                  if (var1.getAction() == 2) {
                     float var4 = var2 - this.startX;
                     float var5 = var3 - this.startY;
                     if (this.startDragging) {
                        if (Math.abs(var4) >= AndroidUtilities.getPixelsInCM(0.3F, true) || Math.abs(var5) >= AndroidUtilities.getPixelsInCM(0.3F, false)) {
                           this.dragging = true;
                           this.startDragging = false;
                        }
                     } else if (this.dragging) {
                        LayoutParams var9 = PipRoundVideoView.this.windowLayoutParams;
                        var9.x = (int)((float)var9.x + var4);
                        var9 = PipRoundVideoView.this.windowLayoutParams;
                        var9.y = (int)((float)var9.y + var5);
                        int var6 = PipRoundVideoView.this.videoWidth / 2;
                        int var7 = PipRoundVideoView.this.windowLayoutParams.x;
                        int var8 = -var6;
                        if (var7 < var8) {
                           PipRoundVideoView.this.windowLayoutParams.x = var8;
                        } else if (PipRoundVideoView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - PipRoundVideoView.this.windowLayoutParams.width + var6) {
                           PipRoundVideoView.this.windowLayoutParams.x = AndroidUtilities.displaySize.x - PipRoundVideoView.this.windowLayoutParams.width + var6;
                        }

                        var8 = PipRoundVideoView.this.windowLayoutParams.x;
                        var5 = 1.0F;
                        if (var8 < 0) {
                           var5 = 1.0F + (float)PipRoundVideoView.this.windowLayoutParams.x / (float)var6 * 0.5F;
                        } else if (PipRoundVideoView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - PipRoundVideoView.this.windowLayoutParams.width) {
                           var5 = 1.0F - (float)(PipRoundVideoView.this.windowLayoutParams.x - AndroidUtilities.displaySize.x + PipRoundVideoView.this.windowLayoutParams.width) / (float)var6 * 0.5F;
                        }

                        if (PipRoundVideoView.this.windowView.getAlpha() != var5) {
                           PipRoundVideoView.this.windowView.setAlpha(var5);
                        }

                        if (PipRoundVideoView.this.windowLayoutParams.y < 0) {
                           PipRoundVideoView.this.windowLayoutParams.y = 0;
                        } else if (PipRoundVideoView.this.windowLayoutParams.y > AndroidUtilities.displaySize.y - PipRoundVideoView.this.windowLayoutParams.height + 0) {
                           PipRoundVideoView.this.windowLayoutParams.y = AndroidUtilities.displaySize.y - PipRoundVideoView.this.windowLayoutParams.height + 0;
                        }

                        PipRoundVideoView.this.windowManager.updateViewLayout(PipRoundVideoView.this.windowView, PipRoundVideoView.this.windowLayoutParams);
                        this.startX = var2;
                        this.startY = var3;
                     }
                  } else if (var1.getAction() == 1) {
                     if (this.startDragging && !this.dragging) {
                        MessageObject var10 = MediaController.getInstance().getPlayingMessageObject();
                        if (var10 != null) {
                           if (MediaController.getInstance().isMessagePaused()) {
                              MediaController.getInstance().playMessage(var10);
                           } else {
                              MediaController.getInstance().pauseMessage(var10);
                           }
                        }
                     }

                     this.dragging = false;
                     this.startDragging = false;
                     PipRoundVideoView.this.animateToBoundsMaybe();
                  }

                  return true;
               }
            }

            public void requestDisallowInterceptTouchEvent(boolean var1) {
               super.requestDisallowInterceptTouchEvent(var1);
            }
         };
         this.windowView.setWillNotDraw(false);
         this.videoWidth = AndroidUtilities.dp(126.0F);
         this.videoHeight = AndroidUtilities.dp(126.0F);
         if (VERSION.SDK_INT >= 21) {
            this.aspectRatioFrameLayout = new AspectRatioFrameLayout(var1) {
               protected boolean drawChild(Canvas var1, View var2, long var3) {
                  boolean var5 = super.drawChild(var1, var2, var3);
                  if (var2 == PipRoundVideoView.this.textureView) {
                     MessageObject var6 = MediaController.getInstance().getPlayingMessageObject();
                     if (var6 != null) {
                        PipRoundVideoView.this.rect.set(AndroidUtilities.dpf2(1.5F), AndroidUtilities.dpf2(1.5F), (float)this.getMeasuredWidth() - AndroidUtilities.dpf2(1.5F), (float)this.getMeasuredHeight() - AndroidUtilities.dpf2(1.5F));
                        var1.drawArc(PipRoundVideoView.this.rect, -90.0F, var6.audioProgress * 360.0F, false, Theme.chat_radialProgressPaint);
                     }
                  }

                  return var5;
               }
            };
            this.aspectRatioFrameLayout.setOutlineProvider(new ViewOutlineProvider() {
               @TargetApi(21)
               public void getOutline(View var1, Outline var2) {
                  var2.setOval(0, 0, AndroidUtilities.dp(120.0F), AndroidUtilities.dp(120.0F));
               }
            });
            this.aspectRatioFrameLayout.setClipToOutline(true);
         } else {
            final Paint var8 = new Paint(1);
            var8.setColor(-16777216);
            var8.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            this.aspectRatioFrameLayout = new AspectRatioFrameLayout(var1) {
               private Path aspectPath = new Path();

               protected void dispatchDraw(Canvas var1) {
                  super.dispatchDraw(var1);
                  var1.drawPath(this.aspectPath, var8);
               }

               protected boolean drawChild(Canvas var1, View var2, long var3) {
                  boolean var5;
                  try {
                     var5 = super.drawChild(var1, var2, var3);
                  } catch (Throwable var7) {
                     var5 = false;
                  }

                  if (var2 == PipRoundVideoView.this.textureView) {
                     MessageObject var8x = MediaController.getInstance().getPlayingMessageObject();
                     if (var8x != null) {
                        PipRoundVideoView.this.rect.set(AndroidUtilities.dpf2(1.5F), AndroidUtilities.dpf2(1.5F), (float)this.getMeasuredWidth() - AndroidUtilities.dpf2(1.5F), (float)this.getMeasuredHeight() - AndroidUtilities.dpf2(1.5F));
                        var1.drawArc(PipRoundVideoView.this.rect, -90.0F, var8x.audioProgress * 360.0F, false, Theme.chat_radialProgressPaint);
                     }
                  }

                  return var5;
               }

               protected void onSizeChanged(int var1, int var2, int var3, int var4) {
                  super.onSizeChanged(var1, var2, var3, var4);
                  this.aspectPath.reset();
                  Path var5 = this.aspectPath;
                  float var6 = (float)(var1 / 2);
                  var5.addCircle(var6, (float)(var2 / 2), var6, Direction.CW);
                  this.aspectPath.toggleInverseFillType();
               }
            };
            this.aspectRatioFrameLayout.setLayerType(2, (Paint)null);
         }

         this.aspectRatioFrameLayout.setAspectRatio(1.0F, 0);
         this.windowView.addView(this.aspectRatioFrameLayout, LayoutHelper.createFrame(120, 120.0F, 51, 3.0F, 3.0F, 0.0F, 0.0F));
         this.windowView.setAlpha(1.0F);
         this.windowView.setScaleX(0.8F);
         this.windowView.setScaleY(0.8F);
         this.textureView = new TextureView(var1);
         this.aspectRatioFrameLayout.addView(this.textureView, LayoutHelper.createFrame(-1, -1.0F));
         this.imageView = new ImageView(var1);
         this.aspectRatioFrameLayout.addView(this.imageView, LayoutHelper.createFrame(-1, -1.0F));
         this.imageView.setVisibility(4);
         this.windowManager = (WindowManager)var1.getSystemService("window");
         this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("pipconfig", 0);
         int var3 = this.preferences.getInt("sidex", 1);
         int var4 = this.preferences.getInt("sidey", 0);
         float var5 = this.preferences.getFloat("px", 0.0F);
         float var6 = this.preferences.getFloat("py", 0.0F);

         try {
            LayoutParams var9 = new LayoutParams();
            this.windowLayoutParams = var9;
            this.windowLayoutParams.width = this.videoWidth;
            this.windowLayoutParams.height = this.videoHeight;
            this.windowLayoutParams.x = getSideCoord(true, var3, var5, this.videoWidth);
            this.windowLayoutParams.y = getSideCoord(false, var4, var6, this.videoHeight);
            this.windowLayoutParams.format = -3;
            this.windowLayoutParams.gravity = 51;
            this.windowLayoutParams.type = 99;
            this.windowLayoutParams.flags = 16777736;
            this.windowManager.addView(this.windowView, this.windowLayoutParams);
         } catch (Exception var7) {
            FileLog.e((Throwable)var7);
            return;
         }

         this.parentActivity = var1;
         this.currentAccount = UserConfig.selectedAccount;
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
         this.runShowHideAnimation(true);
      }
   }

   public void showTemporary(boolean var1) {
      AnimatorSet var2 = this.hideShowAnimation;
      if (var2 != null) {
         var2.cancel();
      }

      this.hideShowAnimation = new AnimatorSet();
      var2 = this.hideShowAnimation;
      FrameLayout var3 = this.windowView;
      Property var4 = View.ALPHA;
      float var5 = 1.0F;
      float var6;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.0F;
      }

      ObjectAnimator var9 = ObjectAnimator.ofFloat(var3, var4, new float[]{var6});
      FrameLayout var10 = this.windowView;
      Property var7 = View.SCALE_X;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.8F;
      }

      ObjectAnimator var8 = ObjectAnimator.ofFloat(var10, var7, new float[]{var6});
      var10 = this.windowView;
      var7 = View.SCALE_Y;
      if (var1) {
         var6 = var5;
      } else {
         var6 = 0.8F;
      }

      var2.playTogether(new Animator[]{var9, var8, ObjectAnimator.ofFloat(var10, var7, new float[]{var6})});
      this.hideShowAnimation.setDuration(150L);
      if (this.decelerateInterpolator == null) {
         this.decelerateInterpolator = new DecelerateInterpolator();
      }

      this.hideShowAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            if (var1.equals(PipRoundVideoView.this.hideShowAnimation)) {
               PipRoundVideoView.this.hideShowAnimation = null;
            }

         }
      });
      this.hideShowAnimation.setInterpolator(this.decelerateInterpolator);
      this.hideShowAnimation.start();
   }
}
