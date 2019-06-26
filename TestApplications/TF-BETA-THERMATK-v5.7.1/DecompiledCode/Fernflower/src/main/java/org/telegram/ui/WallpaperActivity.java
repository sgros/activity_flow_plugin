package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.style.CharacterStyle;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatActionCell$ChatActionCellDelegate$_CC;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.WallpaperParallaxEffect;

public class WallpaperActivity extends BaseFragment implements DownloadController.FileDownloadProgressListener, NotificationCenter.NotificationCenterDelegate {
   private static final int share_item = 1;
   private int TAG;
   private int backgroundColor;
   private BackupImageView backgroundImage;
   private Paint backgroundPaint;
   private Mode blendMode;
   private Bitmap blurredBitmap;
   private FrameLayout bottomOverlayChat;
   private TextView bottomOverlayChatText;
   private FrameLayout buttonsContainer;
   private WallpaperActivity.CheckBoxView[] checkBoxView;
   private Paint checkPaint;
   private WallpaperActivity.ColorPicker colorPicker;
   private float currentIntensity = 0.4F;
   private Object currentWallpaper;
   private Bitmap currentWallpaperBitmap;
   private WallpaperActivity.WallpaperActivityDelegate delegate;
   private Paint eraserPaint;
   private String imageFilter;
   private HeaderCell intensityCell;
   private SeekBarView intensitySeekBar;
   private boolean isBlurred;
   private boolean isMotion;
   private RecyclerListView listView;
   private String loadingFile;
   private File loadingFileObject;
   private TLRPC.PhotoSize loadingSize;
   private int maxWallpaperSize;
   private AnimatorSet motionAnimation;
   private WallpaperParallaxEffect parallaxEffect;
   private float parallaxScale;
   private int patternColor;
   private FrameLayout[] patternLayout = new FrameLayout[3];
   private ArrayList patterns;
   private WallpaperActivity.PatternsAdapter patternsAdapter;
   private FrameLayout[] patternsButtonsContainer = new FrameLayout[2];
   private TextView[] patternsCancelButton = new TextView[2];
   private LinearLayoutManager patternsLayoutManager;
   private RecyclerListView patternsListView;
   private TextView[] patternsSaveButton = new TextView[2];
   private int previousBackgroundColor;
   private float previousIntensity;
   private TLRPC.TL_wallPaper previousSelectedPattern;
   private boolean progressVisible;
   private RadialProgress2 radialProgress;
   private TLRPC.TL_wallPaper selectedPattern;
   private TextPaint textPaint;
   private Drawable themedWallpaper;
   private boolean viewCreated;

   public WallpaperActivity(Object var1, Bitmap var2) {
      this.blendMode = Mode.SRC_IN;
      this.parallaxScale = 1.0F;
      this.loadingFile = null;
      this.loadingFileObject = null;
      this.loadingSize = null;
      this.imageFilter = "640_360";
      this.maxWallpaperSize = 1920;
      this.currentWallpaper = var1;
      this.currentWallpaperBitmap = var2;
      var1 = this.currentWallpaper;
      if (var1 instanceof TLRPC.TL_wallPaper) {
         TLRPC.TL_wallPaper var3 = (TLRPC.TL_wallPaper)var1;
      } else if (var1 instanceof WallpapersListActivity.ColorWallpaper) {
         WallpapersListActivity.ColorWallpaper var4 = (WallpapersListActivity.ColorWallpaper)var1;
         this.isMotion = var4.motion;
         this.selectedPattern = var4.pattern;
         if (this.selectedPattern != null) {
            this.currentIntensity = var4.intensity;
         }
      }

   }

   // $FF: synthetic method
   static int access$1700(WallpaperActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1800(WallpaperActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2000(WallpaperActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3000(WallpaperActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3100(WallpaperActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3200(WallpaperActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3300(WallpaperActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3400(WallpaperActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$400(WallpaperActivity var0) {
      return var0.currentAccount;
   }

   private void animateMotionChange() {
      AnimatorSet var1 = this.motionAnimation;
      if (var1 != null) {
         var1.cancel();
      }

      this.motionAnimation = new AnimatorSet();
      if (this.isMotion) {
         this.motionAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.backgroundImage, View.SCALE_X, new float[]{this.parallaxScale}), ObjectAnimator.ofFloat(this.backgroundImage, View.SCALE_Y, new float[]{this.parallaxScale})});
      } else {
         this.motionAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.backgroundImage, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.backgroundImage, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.backgroundImage, View.TRANSLATION_X, new float[]{0.0F}), ObjectAnimator.ofFloat(this.backgroundImage, View.TRANSLATION_Y, new float[]{0.0F})});
      }

      this.motionAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
      this.motionAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            WallpaperActivity.this.motionAnimation = null;
         }
      });
      this.motionAnimation.start();
   }

   private void setBackgroundColor(int var1) {
      this.backgroundColor = var1;
      this.backgroundImage.setBackgroundColor(this.backgroundColor);
      WallpaperActivity.CheckBoxView[] var2 = this.checkBoxView;
      var1 = 0;
      if (var2[0] != null) {
         var2[0].invalidate();
      }

      this.patternColor = AndroidUtilities.getPatternColor(this.backgroundColor);
      int var3 = this.patternColor;
      Theme.applyChatServiceMessageColor(new int[]{var3, var3, var3, var3});
      BackupImageView var4 = this.backgroundImage;
      if (var4 != null) {
         var4.getImageReceiver().setColorFilter(new PorterDuffColorFilter(this.patternColor, this.blendMode));
         this.backgroundImage.getImageReceiver().setAlpha(this.currentIntensity);
         this.backgroundImage.invalidate();
      }

      RecyclerListView var5 = this.listView;
      if (var5 != null) {
         var5.invalidateViews();
      }

      FrameLayout var6 = this.buttonsContainer;
      if (var6 != null) {
         for(var3 = var6.getChildCount(); var1 < var3; ++var1) {
            this.buttonsContainer.getChildAt(var1).invalidate();
         }
      }

      RadialProgress2 var7 = this.radialProgress;
      if (var7 != null) {
         var7.setColors("chat_serviceBackground", "chat_serviceBackground", "chat_serviceText", "chat_serviceText");
      }

   }

   private void setCurrentImage(boolean var1) {
      Object var2 = this.currentWallpaper;
      boolean var3 = var2 instanceof TLRPC.TL_wallPaper;
      TLRPC.PhotoSize var4 = null;
      TLRPC.TL_wallPaper var8;
      if (var3) {
         var8 = (TLRPC.TL_wallPaper)var2;
         if (var1) {
            var4 = FileLoader.getClosestPhotoSizeWithSize(var8.document.thumbs, 100);
         }

         this.backgroundImage.setImage(ImageLocation.getForDocument(var8.document), this.imageFilter, ImageLocation.getForDocument(var4, var8.document), "100_100_b", "jpg", var8.document.size, 1, var8);
      } else if (var2 instanceof WallpapersListActivity.ColorWallpaper) {
         this.setBackgroundColor(((WallpapersListActivity.ColorWallpaper)var2).color);
         var8 = this.selectedPattern;
         if (var8 != null) {
            BackupImageView var11 = this.backgroundImage;
            ImageLocation var5 = ImageLocation.getForDocument(var8.document);
            String var6 = this.imageFilter;
            var8 = this.selectedPattern;
            var11.setImage(var5, var6, (ImageLocation)null, (String)null, "jpg", var8.document.size, 1, var8);
         }
      } else {
         int var7;
         if (var2 instanceof WallpapersListActivity.FileWallpaper) {
            Bitmap var12 = this.currentWallpaperBitmap;
            if (var12 != null) {
               this.backgroundImage.setImageBitmap(var12);
            } else {
               WallpapersListActivity.FileWallpaper var14 = (WallpapersListActivity.FileWallpaper)var2;
               File var9 = var14.originalPath;
               if (var9 != null) {
                  this.backgroundImage.setImage(var9.getAbsolutePath(), this.imageFilter, (Drawable)null);
               } else {
                  var9 = var14.path;
                  if (var9 != null) {
                     this.backgroundImage.setImage(var9.getAbsolutePath(), this.imageFilter, (Drawable)null);
                  } else {
                     var7 = var14.resId;
                     if ((long)var7 == -2L) {
                        this.backgroundImage.setImageDrawable(Theme.getThemedWallpaper(false));
                     } else if (var7 != 0) {
                        this.backgroundImage.setImageResource(var7);
                     }
                  }
               }
            }
         } else if (var2 instanceof MediaController.SearchImage) {
            MediaController.SearchImage var16 = (MediaController.SearchImage)var2;
            TLRPC.Photo var15 = var16.photo;
            if (var15 != null) {
               TLRPC.PhotoSize var13 = FileLoader.getClosestPhotoSizeWithSize(var15.sizes, 100);
               TLRPC.PhotoSize var10 = FileLoader.getClosestPhotoSizeWithSize(var16.photo.sizes, this.maxWallpaperSize, true);
               var4 = var10;
               if (var10 == var13) {
                  var4 = null;
               }

               if (var4 != null) {
                  var7 = var4.size;
               } else {
                  var7 = 0;
               }

               this.backgroundImage.setImage(ImageLocation.getForPhoto(var4, var16.photo), this.imageFilter, ImageLocation.getForPhoto(var13, var16.photo), "100_100_b", "jpg", var7, 1, var16);
            } else {
               this.backgroundImage.setImage(var16.imageUrl, this.imageFilter, var16.thumbUrl, "100_100_b");
            }
         }
      }

   }

   private void showPatternsView(final int var1, final boolean var2) {
      final boolean var3;
      if (var2 && var1 == 1 && this.selectedPattern != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      ArrayList var5;
      if (var2) {
         int var4;
         if (var1 == 0) {
            var4 = this.backgroundColor;
            this.previousBackgroundColor = var4;
            this.colorPicker.setColor(var4);
         } else {
            this.previousSelectedPattern = this.selectedPattern;
            this.previousIntensity = this.currentIntensity;
            this.patternsAdapter.notifyDataSetChanged();
            var5 = this.patterns;
            if (var5 != null) {
               TLRPC.TL_wallPaper var6 = this.selectedPattern;
               if (var6 == null) {
                  var4 = 0;
               } else {
                  var4 = var5.indexOf(var6) + 1;
               }

               this.patternsLayoutManager.scrollToPositionWithOffset(var4, (this.patternsListView.getMeasuredWidth() - AndroidUtilities.dp(100.0F) - AndroidUtilities.dp(12.0F)) / 2);
            }
         }
      }

      WallpaperActivity.CheckBoxView[] var11 = this.checkBoxView;
      final byte var12;
      if (var3) {
         var12 = 2;
      } else {
         var12 = 0;
      }

      var11[var12].setVisibility(0);
      AnimatorSet var13 = new AnimatorSet();
      var5 = new ArrayList();
      if (var1 == 0) {
         var12 = 1;
      } else {
         var12 = 0;
      }

      float var7 = 1.0F;
      FrameLayout[] var16;
      if (var2) {
         this.patternLayout[var1].setVisibility(0);
         var5.add(ObjectAnimator.ofFloat(this.listView, View.TRANSLATION_Y, new float[]{(float)(-this.patternLayout[var1].getMeasuredHeight() + AndroidUtilities.dp(48.0F))}));
         var5.add(ObjectAnimator.ofFloat(this.buttonsContainer, View.TRANSLATION_Y, new float[]{(float)(-this.patternLayout[var1].getMeasuredHeight() + AndroidUtilities.dp(48.0F))}));
         WallpaperActivity.CheckBoxView var8 = this.checkBoxView[2];
         Property var9 = View.ALPHA;
         float var10;
         if (var3) {
            var10 = 1.0F;
         } else {
            var10 = 0.0F;
         }

         var5.add(ObjectAnimator.ofFloat(var8, var9, new float[]{var10}));
         WallpaperActivity.CheckBoxView var15 = this.checkBoxView[0];
         Property var14 = View.ALPHA;
         var10 = var7;
         if (var3) {
            var10 = 0.0F;
         }

         var5.add(ObjectAnimator.ofFloat(var15, var14, new float[]{var10}));
         var5.add(ObjectAnimator.ofFloat(this.backgroundImage, View.ALPHA, new float[]{0.0F}));
         if (this.patternLayout[var12].getVisibility() == 0) {
            var5.add(ObjectAnimator.ofFloat(this.patternLayout[var12], View.ALPHA, new float[]{0.0F}));
            var5.add(ObjectAnimator.ofFloat(this.patternLayout[var1], View.ALPHA, new float[]{0.0F, 1.0F}));
            this.patternLayout[var1].setTranslationY(0.0F);
         } else {
            var16 = this.patternLayout;
            var5.add(ObjectAnimator.ofFloat(var16[var1], View.TRANSLATION_Y, new float[]{(float)var16[var1].getMeasuredHeight(), 0.0F}));
         }
      } else {
         var5.add(ObjectAnimator.ofFloat(this.listView, View.TRANSLATION_Y, new float[]{0.0F}));
         var5.add(ObjectAnimator.ofFloat(this.buttonsContainer, View.TRANSLATION_Y, new float[]{0.0F}));
         var16 = this.patternLayout;
         var5.add(ObjectAnimator.ofFloat(var16[var1], View.TRANSLATION_Y, new float[]{(float)var16[var1].getMeasuredHeight()}));
         var5.add(ObjectAnimator.ofFloat(this.checkBoxView[0], View.ALPHA, new float[]{1.0F}));
         var5.add(ObjectAnimator.ofFloat(this.checkBoxView[2], View.ALPHA, new float[]{0.0F}));
         var5.add(ObjectAnimator.ofFloat(this.backgroundImage, View.ALPHA, new float[]{1.0F}));
      }

      var13.playTogether(var5);
      var13.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            if (var2 && WallpaperActivity.this.patternLayout[var12].getVisibility() == 0) {
               WallpaperActivity.this.patternLayout[var12].setAlpha(1.0F);
               WallpaperActivity.this.patternLayout[var12].setVisibility(4);
            } else if (!var2) {
               WallpaperActivity.this.patternLayout[var1].setVisibility(4);
            }

            WallpaperActivity.CheckBoxView[] var3x = WallpaperActivity.this.checkBoxView;
            byte var2x;
            if (var3) {
               var2x = 0;
            } else {
               var2x = 2;
            }

            var3x[var2x].setVisibility(4);
         }
      });
      var13.setInterpolator(CubicBezierInterpolator.EASE_OUT);
      var13.setDuration(200L);
      var13.start();
   }

   private void updateBlurred() {
      Bitmap var1;
      if (this.isBlurred && this.blurredBitmap == null) {
         var1 = this.currentWallpaperBitmap;
         if (var1 != null) {
            this.blurredBitmap = Utilities.blurWallpaper(var1);
         } else {
            ImageReceiver var2 = this.backgroundImage.getImageReceiver();
            if (var2.hasNotThumb() || var2.hasStaticThumb()) {
               this.blurredBitmap = Utilities.blurWallpaper(var2.getBitmap());
            }
         }
      }

      if (this.isBlurred) {
         var1 = this.blurredBitmap;
         if (var1 != null) {
            this.backgroundImage.setImageBitmap(var1);
         }
      } else {
         this.setCurrentImage(false);
      }

   }

   private void updateButtonState(RadialProgress2 var1, Object var2, DownloadController.FileDownloadProgressListener var3, boolean var4, boolean var5) {
      Object var6;
      if (var3 == this) {
         var6 = this.selectedPattern;
         if (var6 == null) {
            var6 = this.currentWallpaper;
         }
      } else {
         var6 = var2;
      }

      boolean var7 = var6 instanceof TLRPC.TL_wallPaper;
      byte var8 = 4;
      if (!var7 && !(var6 instanceof MediaController.SearchImage)) {
         if (var3 != this) {
            var8 = 6;
         }

         var1.setIcon(var8, var4, var5);
      } else {
         boolean var9 = var5;
         if (var2 == null) {
            var9 = var5;
            if (var5) {
               var9 = var5;
               if (!this.progressVisible) {
                  var9 = false;
               }
            }
         }

         File var11;
         int var12;
         String var19;
         if (var7) {
            TLRPC.TL_wallPaper var10 = (TLRPC.TL_wallPaper)var6;
            var19 = FileLoader.getAttachFileName(var10.document);
            if (TextUtils.isEmpty(var19)) {
               return;
            }

            var11 = FileLoader.getPathToAttach(var10.document, true);
            var12 = var10.document.size;
         } else {
            MediaController.SearchImage var22 = (MediaController.SearchImage)var6;
            TLRPC.Photo var20 = var22.photo;
            File var21;
            if (var20 != null) {
               TLRPC.PhotoSize var24 = FileLoader.getClosestPhotoSizeWithSize(var20.sizes, this.maxWallpaperSize, true);
               var21 = FileLoader.getPathToAttach(var24, true);
               var19 = FileLoader.getAttachFileName(var24);
               var12 = var24.size;
            } else {
               var21 = ImageLoader.getHttpFilePath(var22.imageUrl, "jpg");
               var19 = var21.getName();
               var12 = var22.size;
            }

            var11 = var21;
            String var23 = var19;
            var19 = var19;
            if (TextUtils.isEmpty(var23)) {
               return;
            }
         }

         var5 = var11.exists();
         if (var5) {
            DownloadController.getInstance(super.currentAccount).removeLoadingFileObserver(var3);
            var1.setProgress(1.0F, var9);
            if (var2 != null) {
               var8 = 6;
            }

            var1.setIcon(var8, var4, var9);
            if (var2 == null) {
               this.backgroundImage.invalidate();
               if (var12 != 0) {
                  super.actionBar.setSubtitle(AndroidUtilities.formatFileSize((long)var12));
               } else {
                  super.actionBar.setSubtitle((CharSequence)null);
               }
            }
         } else {
            DownloadController.getInstance(super.currentAccount).addLoadingFileObserver(var19, (MessageObject)null, var3);
            FileLoader.getInstance(super.currentAccount).isLoadingFile(var19);
            Float var18 = ImageLoader.getInstance().getFileProgress(var19);
            if (var18 != null) {
               var1.setProgress(var18, var9);
            } else {
               var1.setProgress(0.0F, var9);
            }

            var1.setIcon(10, var4, var9);
            if (var2 == null) {
               super.actionBar.setSubtitle(LocaleController.getString("LoadingFullImage", 2131559769));
               this.backgroundImage.invalidate();
            }
         }

         if (var2 == null) {
            TLRPC.TL_wallPaper var15 = this.selectedPattern;
            float var13 = 0.5F;
            float var14;
            if (var15 == null) {
               FrameLayout var16 = this.buttonsContainer;
               if (var5) {
                  var14 = 1.0F;
               } else {
                  var14 = 0.5F;
               }

               var16.setAlpha(var14);
            }

            this.bottomOverlayChat.setEnabled(var5);
            TextView var17 = this.bottomOverlayChatText;
            var14 = var13;
            if (var5) {
               var14 = 1.0F;
            }

            var17.setAlpha(var14);
         }
      }

   }

   private void updateMotionButton() {
      WallpaperActivity.CheckBoxView[] var1 = this.checkBoxView;
      byte var2;
      if (this.selectedPattern != null) {
         var2 = 2;
      } else {
         var2 = 0;
      }

      var1[var2].setVisibility(0);
      AnimatorSet var8 = new AnimatorSet();
      WallpaperActivity.CheckBoxView var3 = this.checkBoxView[2];
      Property var4 = View.ALPHA;
      TLRPC.TL_wallPaper var5 = this.selectedPattern;
      float var6 = 1.0F;
      float var7;
      if (var5 != null) {
         var7 = 1.0F;
      } else {
         var7 = 0.0F;
      }

      ObjectAnimator var9 = ObjectAnimator.ofFloat(var3, var4, new float[]{var7});
      var3 = this.checkBoxView[0];
      Property var10 = View.ALPHA;
      var7 = var6;
      if (this.selectedPattern != null) {
         var7 = 0.0F;
      }

      var8.playTogether(new Animator[]{var9, ObjectAnimator.ofFloat(var3, var10, new float[]{var7})});
      var8.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            WallpaperActivity.CheckBoxView[] var3 = WallpaperActivity.this.checkBoxView;
            byte var2;
            if (WallpaperActivity.this.selectedPattern != null) {
               var2 = 0;
            } else {
               var2 = 2;
            }

            var3[var2].setVisibility(4);
         }
      });
      var8.setInterpolator(CubicBezierInterpolator.EASE_OUT);
      var8.setDuration(200L);
      var8.start();
   }

   private void updateSelectedPattern(boolean var1) {
      int var2 = this.patternsListView.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = this.patternsListView.getChildAt(var3);
         if (var4 instanceof WallpaperActivity.PatternCell) {
            ((WallpaperActivity.PatternCell)var4).updateSelected(var1);
         }
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("BackgroundPreview", 2131558821));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               WallpaperActivity.this.finishFragment();
            } else if (var1 == 1) {
               if (WallpaperActivity.this.getParentActivity() == null) {
                  return;
               }

               StringBuilder var2 = new StringBuilder();
               if (WallpaperActivity.this.isBlurred) {
                  var2.append("blur");
               }

               if (WallpaperActivity.this.isMotion) {
                  if (var2.length() > 0) {
                     var2.append("+");
                  }

                  var2.append("motion");
               }

               StringBuilder var4;
               String var5;
               String var7;
               if (WallpaperActivity.this.currentWallpaper instanceof TLRPC.TL_wallPaper) {
                  TLRPC.TL_wallPaper var3 = (TLRPC.TL_wallPaper)WallpaperActivity.this.currentWallpaper;
                  var4 = new StringBuilder();
                  var4.append("https://");
                  var4.append(MessagesController.getInstance(WallpaperActivity.access$1700(WallpaperActivity.this)).linkPrefix);
                  var4.append("/bg/");
                  var4.append(var3.slug);
                  var5 = var4.toString();
                  var7 = var5;
                  if (var2.length() > 0) {
                     var4 = new StringBuilder();
                     var4.append(var5);
                     var4.append("?mode=");
                     var4.append(var2.toString());
                     var7 = var4.toString();
                  }
               } else {
                  if (!(WallpaperActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                     return;
                  }

                  WallpapersListActivity.ColorWallpaper var9 = (WallpapersListActivity.ColorWallpaper)WallpaperActivity.this.currentWallpaper;
                  var7 = String.format("%02x%02x%02x", (byte)(WallpaperActivity.this.backgroundColor >> 16) & 255, (byte)(WallpaperActivity.this.backgroundColor >> 8) & 255, (byte)(WallpaperActivity.this.backgroundColor & 255)).toLowerCase();
                  StringBuilder var6;
                  if (WallpaperActivity.this.selectedPattern != null) {
                     var6 = new StringBuilder();
                     var6.append("https://");
                     var6.append(MessagesController.getInstance(WallpaperActivity.access$1800(WallpaperActivity.this)).linkPrefix);
                     var6.append("/bg/");
                     var6.append(WallpaperActivity.this.selectedPattern.slug);
                     var6.append("?intensity=");
                     var6.append((int)(WallpaperActivity.this.currentIntensity * 100.0F));
                     var6.append("&bg_color=");
                     var6.append(var7);
                     var5 = var6.toString();
                     var7 = var5;
                     if (var2.length() > 0) {
                        var4 = new StringBuilder();
                        var4.append(var5);
                        var4.append("&mode=");
                        var4.append(var2.toString());
                        var7 = var4.toString();
                     }
                  } else {
                     var6 = new StringBuilder();
                     var6.append("https://");
                     var6.append(MessagesController.getInstance(WallpaperActivity.access$2000(WallpaperActivity.this)).linkPrefix);
                     var6.append("/bg/");
                     var6.append(var7);
                     var7 = var6.toString();
                  }
               }

               WallpaperActivity var8 = WallpaperActivity.this;
               var8.showDialog(new ShareAlert(var8.getParentActivity(), (ArrayList)null, var7, false, var7, false));
            }

         }
      });
      Object var2 = this.currentWallpaper;
      if (var2 instanceof WallpapersListActivity.ColorWallpaper || var2 instanceof TLRPC.TL_wallPaper) {
         super.actionBar.createMenu().addItem(1, 2131165470);
      }

      byte var4;
      byte var5;
      FrameLayout var15;
      boolean var16;
      label125: {
         var15 = new FrameLayout(var1);
         super.fragmentView = var15;
         super.hasOwnBackground = true;
         this.backgroundImage = new BackupImageView(var1) {
            protected void onDraw(Canvas var1) {
               super.onDraw(var1);
               if (WallpaperActivity.this.progressVisible && WallpaperActivity.this.radialProgress != null) {
                  WallpaperActivity.this.radialProgress.draw(var1);
               }

            }

            protected void onMeasure(int var1, int var2) {
               super.onMeasure(var1, var2);
               WallpaperActivity var3 = WallpaperActivity.this;
               var3.parallaxScale = var3.parallaxEffect.getScale(this.getMeasuredWidth(), this.getMeasuredHeight());
               if (WallpaperActivity.this.isMotion) {
                  this.setScaleX(WallpaperActivity.this.parallaxScale);
                  this.setScaleY(WallpaperActivity.this.parallaxScale);
               }

               if (WallpaperActivity.this.radialProgress != null) {
                  int var4 = AndroidUtilities.dp(44.0F);
                  var1 = (this.getMeasuredWidth() - var4) / 2;
                  var2 = (this.getMeasuredHeight() - var4) / 2;
                  WallpaperActivity.this.radialProgress.setProgressRect(var1, var2, var1 + var4, var4 + var2);
               }

               var3 = WallpaperActivity.this;
               boolean var5;
               if (this.getMeasuredWidth() <= this.getMeasuredHeight()) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               var3.progressVisible = var5;
            }

            public void setAlpha(float var1) {
               WallpaperActivity.this.radialProgress.setOverrideAlpha(var1);
            }
         };
         byte var6;
         if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            byte var3 = 3;
            if (this.patterns != null) {
               var4 = 0;
            } else {
               var4 = 2;
            }

            var5 = var3;
            var6 = var4;
            if (this.patterns == null) {
               if (this.selectedPattern == null) {
                  boolean var18 = false;
                  var5 = var3;
                  var16 = var18;
                  break label125;
               }

               var5 = var3;
               var6 = var4;
            }
         } else {
            var5 = 2;
            var6 = 0;
         }

         var16 = true;
         var4 = var6;
      }

      var15.addView(this.backgroundImage, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
      this.backgroundImage.getImageReceiver().setDelegate(new _$$Lambda$WallpaperActivity$DLrQAr4_n4zrBNqOkPDVZ2ql9yg(this));
      this.radialProgress = new RadialProgress2(this.backgroundImage);
      this.radialProgress.setColors("chat_serviceBackground", "chat_serviceBackground", "chat_serviceText", "chat_serviceText");
      this.listView = new RecyclerListView(var1);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, true));
      this.listView.setOverScrollMode(2);
      this.listView.setAdapter(new WallpaperActivity.ListAdapter(var1));
      RecyclerListView var7 = this.listView;
      float var8;
      if (var16) {
         var8 = 64.0F;
      } else {
         var8 = 4.0F;
      }

      var7.setPadding(0, 0, 0, AndroidUtilities.dp(var8));
      var15.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
      this.bottomOverlayChat = new FrameLayout(var1) {
         public void onDraw(Canvas var1) {
            int var2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), var2);
            Theme.chat_composeShadowDrawable.draw(var1);
            var1.drawRect(0.0F, (float)var2, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
         }
      };
      this.bottomOverlayChat.setWillNotDraw(false);
      this.bottomOverlayChat.setPadding(0, AndroidUtilities.dp(3.0F), 0, 0);
      var15.addView(this.bottomOverlayChat, LayoutHelper.createFrame(-1, 51, 80));
      this.bottomOverlayChat.setOnClickListener(new _$$Lambda$WallpaperActivity$1kBuywjigfsWiHQkwiP2IyEMOss(this));
      this.bottomOverlayChatText = new TextView(var1);
      this.bottomOverlayChatText.setTextSize(1, 15.0F);
      this.bottomOverlayChatText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.bottomOverlayChatText.setTextColor(Theme.getColor("chat_fieldOverlayText"));
      this.bottomOverlayChatText.setText(LocaleController.getString("SetBackground", 2131560732));
      this.bottomOverlayChat.addView(this.bottomOverlayChatText, LayoutHelper.createFrame(-2, -2, 17));
      this.buttonsContainer = new FrameLayout(var1);
      var15.addView(this.buttonsContainer, LayoutHelper.createFrame(-2, 32.0F, 81, 0.0F, 0.0F, 0.0F, 66.0F));
      String[] var19 = new String[var5];
      int[] var9 = new int[var5];
      this.checkBoxView = new WallpaperActivity.CheckBoxView[var5];
      if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
         var19[0] = LocaleController.getString("BackgroundColor", 2131558812);
         var19[1] = LocaleController.getString("BackgroundPattern", 2131558820);
         var19[2] = LocaleController.getString("BackgroundMotion", 2131558819);
      } else {
         var19[0] = LocaleController.getString("BackgroundBlurred", 2131558810);
         var19[1] = LocaleController.getString("BackgroundMotion", 2131558819);
      }

      int var10 = 0;

      int var20;
      for(var20 = 0; var10 < var19.length; ++var10) {
         var9[var10] = (int)Math.ceil((double)this.textPaint.measureText(var19[var10]));
         var20 = Math.max(var20, var9[var10]);
      }

      for(var10 = var4; var10 < var5; ++var10) {
         WallpaperActivity.CheckBoxView[] var11 = this.checkBoxView;
         boolean var12;
         if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper && var10 == 0) {
            var12 = false;
         } else {
            var12 = true;
         }

         var11[var10] = new WallpaperActivity.CheckBoxView(var1, var12);
         this.checkBoxView[var10].setText(var19[var10], var9[var10], var20);
         WallpaperActivity.CheckBoxView var25;
         if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            if (var10 == 1) {
               var25 = this.checkBoxView[var10];
               if (this.selectedPattern != null) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               var25.setChecked(var12, false);
            } else if (var10 == 2) {
               this.checkBoxView[var10].setChecked(this.isMotion, false);
            }
         } else {
            var25 = this.checkBoxView[var10];
            if (var10 == 0) {
               var12 = this.isBlurred;
            } else {
               var12 = this.isMotion;
            }

            var25.setChecked(var12, false);
         }

         int var13 = AndroidUtilities.dp(56.0F) + var20;
         LayoutParams var26 = new LayoutParams(-2, var13);
         var26.gravity = 51;
         if (var10 == 1) {
            var13 += AndroidUtilities.dp(9.0F);
         } else {
            var13 = 0;
         }

         var26.leftMargin = var13;
         this.buttonsContainer.addView(this.checkBoxView[var10], var26);
         var11 = this.checkBoxView;
         WallpaperActivity.CheckBoxView var14 = var11[var10];
         var11[var10].setOnClickListener(new _$$Lambda$WallpaperActivity$1_TCmIiY1WoqCaRaze9MxJF8Hq4(this, var10, var14));
         if (var4 == 0 && var10 == 2) {
            this.checkBoxView[var10].setAlpha(0.0F);
            this.checkBoxView[var10].setVisibility(4);
         }
      }

      if (!var16) {
         this.buttonsContainer.setVisibility(8);
      }

      this.parallaxEffect = new WallpaperParallaxEffect(var1);
      this.parallaxEffect.setCallback(new _$$Lambda$WallpaperActivity$PNT8mBRP7gH3N8qVf5B9a9cTbAY(this));
      if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
         this.isBlurred = false;

         for(int var17 = 0; var17 < 2; ++var17) {
            this.patternLayout[var17] = new FrameLayout(var1) {
               public void onDraw(Canvas var1) {
                  int var2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                  Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), var2);
                  Theme.chat_composeShadowDrawable.draw(var1);
                  var1.drawRect(0.0F, (float)var2, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
               }
            };
            this.patternLayout[var17].setVisibility(4);
            this.patternLayout[var17].setWillNotDraw(false);
            FrameLayout var21 = this.patternLayout[var17];
            short var22;
            if (var17 == 0) {
               var22 = 390;
            } else {
               var22 = 242;
            }

            var15.addView(var21, LayoutHelper.createFrame(-1, var22, 83));
            this.patternsButtonsContainer[var17] = new FrameLayout(var1) {
               public void onDraw(Canvas var1) {
                  int var2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                  Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), var2);
                  Theme.chat_composeShadowDrawable.draw(var1);
                  var1.drawRect(0.0F, (float)var2, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
               }
            };
            this.patternsButtonsContainer[var17].setWillNotDraw(false);
            this.patternsButtonsContainer[var17].setPadding(0, AndroidUtilities.dp(3.0F), 0, 0);
            this.patternLayout[var17].addView(this.patternsButtonsContainer[var17], LayoutHelper.createFrame(-1, 51, 80));
            this.patternsCancelButton[var17] = new TextView(var1);
            this.patternsCancelButton[var17].setTextSize(1, 15.0F);
            this.patternsCancelButton[var17].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.patternsCancelButton[var17].setTextColor(Theme.getColor("chat_fieldOverlayText"));
            this.patternsCancelButton[var17].setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
            this.patternsCancelButton[var17].setGravity(17);
            this.patternsCancelButton[var17].setPadding(AndroidUtilities.dp(21.0F), 0, AndroidUtilities.dp(21.0F), 0);
            this.patternsCancelButton[var17].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 0));
            this.patternsButtonsContainer[var17].addView(this.patternsCancelButton[var17], LayoutHelper.createFrame(-2, -1, 51));
            this.patternsCancelButton[var17].setOnClickListener(new _$$Lambda$WallpaperActivity$25btyyl2ojPzi3umZx_yVj0JTsE(this, var17));
            this.patternsSaveButton[var17] = new TextView(var1);
            this.patternsSaveButton[var17].setTextSize(1, 15.0F);
            this.patternsSaveButton[var17].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.patternsSaveButton[var17].setTextColor(Theme.getColor("chat_fieldOverlayText"));
            this.patternsSaveButton[var17].setText(LocaleController.getString("Save", 2131560626).toUpperCase());
            this.patternsSaveButton[var17].setGravity(17);
            this.patternsSaveButton[var17].setPadding(AndroidUtilities.dp(21.0F), 0, AndroidUtilities.dp(21.0F), 0);
            this.patternsSaveButton[var17].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 0));
            this.patternsButtonsContainer[var17].addView(this.patternsSaveButton[var17], LayoutHelper.createFrame(-2, -1, 53));
            this.patternsSaveButton[var17].setOnClickListener(new _$$Lambda$WallpaperActivity$rqsIbe4XDaEpSOIXXh5TVdi7z9A(this, var17));
            if (var17 == 1) {
               this.patternsListView = new RecyclerListView(var1) {
                  public boolean onTouchEvent(MotionEvent var1) {
                     if (var1.getAction() == 0) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                     }

                     return super.onTouchEvent(var1);
                  }
               };
               var7 = this.patternsListView;
               LinearLayoutManager var23 = new LinearLayoutManager(var1, 0, false);
               this.patternsLayoutManager = var23;
               var7.setLayoutManager(var23);
               var7 = this.patternsListView;
               WallpaperActivity.PatternsAdapter var24 = new WallpaperActivity.PatternsAdapter(var1);
               this.patternsAdapter = var24;
               var7.setAdapter(var24);
               this.patternsListView.addItemDecoration(new RecyclerView.ItemDecoration() {
                  public void getItemOffsets(Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
                     int var5 = var3.getChildAdapterPosition(var2);
                     var1.left = AndroidUtilities.dp(12.0F);
                     var1.top = 0;
                     var1.bottom = 0;
                     if (var5 == var4.getItemCount() - 1) {
                        var1.right = AndroidUtilities.dp(12.0F);
                     }

                  }
               });
               this.patternLayout[var17].addView(this.patternsListView, LayoutHelper.createFrame(-1, 100.0F, 51, 0.0F, 14.0F, 0.0F, 0.0F));
               this.patternsListView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$WallpaperActivity$yS3bJtjNmGQ30kYj94XwI29Szdc(this)));
               this.intensityCell = new HeaderCell(var1);
               this.intensityCell.setText(LocaleController.getString("BackgroundIntensity", 2131558818));
               this.patternLayout[var17].addView(this.intensityCell, LayoutHelper.createFrame(-1, -2.0F, 51, 0.0F, 113.0F, 0.0F, 0.0F));
               this.intensitySeekBar = new SeekBarView(var1) {
                  public boolean onTouchEvent(MotionEvent var1) {
                     if (var1.getAction() == 0) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                     }

                     return super.onTouchEvent(var1);
                  }
               };
               this.intensitySeekBar.setProgress(this.currentIntensity);
               this.intensitySeekBar.setReportChanges(true);
               this.intensitySeekBar.setDelegate(new _$$Lambda$WallpaperActivity$JwZ2LKq5VeRI4R5G1vGS7VNqDIs(this));
               this.patternLayout[var17].addView(this.intensitySeekBar, LayoutHelper.createFrame(-1, 30.0F, 51, 9.0F, 153.0F, 9.0F, 0.0F));
            } else {
               this.colorPicker = new WallpaperActivity.ColorPicker(var1);
               this.patternLayout[var17].addView(this.colorPicker, LayoutHelper.createFrame(-1, -1.0F, 1, 0.0F, 0.0F, 0.0F, 48.0F));
            }
         }
      }

      this.setCurrentImage(true);
      this.updateButtonState(this.radialProgress, (Object)null, this, false, false);
      if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
         super.fragmentView.setBackgroundColor(-16777216);
      }

      if (!(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
         this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
         this.backgroundImage.getImageReceiver().setForceCrossfade(true);
      }

      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.wallpapersNeedReload) {
         Object var4 = this.currentWallpaper;
         if (var4 instanceof WallpapersListActivity.FileWallpaper) {
            WallpapersListActivity.FileWallpaper var5 = (WallpapersListActivity.FileWallpaper)var4;
            if (var5.id == -1L) {
               var5.id = (Long)var3[0];
            }
         }
      }

   }

   public int getObserverTag() {
      return this.TAG;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ArrayList var1 = new ArrayList();
      var1.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var1.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
      var1.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"));
      int var2 = 0;

      while(true) {
         FrameLayout[] var3 = this.patternLayout;
         if (var2 >= var3.length) {
            var2 = 0;

            while(true) {
               var3 = this.patternsButtonsContainer;
               if (var2 >= var3.length) {
                  var1.add(new ThemeDescription(this.bottomOverlayChat, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_composeShadowDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messagePanelShadow"));
                  var1.add(new ThemeDescription(this.bottomOverlayChat, 0, (Class[])null, Theme.chat_composeBackgroundPaint, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messagePanelBackground"));
                  var1.add(new ThemeDescription(this.bottomOverlayChatText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_fieldOverlayText"));
                  var2 = 0;

                  while(true) {
                     TextView[] var6 = this.patternsSaveButton;
                     if (var2 >= var6.length) {
                        var2 = 0;

                        while(true) {
                           var6 = this.patternsSaveButton;
                           if (var2 >= var6.length) {
                              if (this.colorPicker != null) {
                                 for(var2 = 0; var2 < this.colorPicker.colorEditText.length; ++var2) {
                                    var1.add(new ThemeDescription(this.colorPicker.colorEditText[var2], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                                    var1.add(new ThemeDescription(this.colorPicker.colorEditText[var2], ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
                                    var1.add(new ThemeDescription(this.colorPicker.colorEditText[var2], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"));
                                    var1.add(new ThemeDescription(this.colorPicker.colorEditText[var2], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
                                    var1.add(new ThemeDescription(this.colorPicker.colorEditText[var2], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
                                    var1.add(new ThemeDescription(this.colorPicker.colorEditText[var2], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText3"));
                                 }
                              }

                              var1.add(new ThemeDescription(this.intensitySeekBar, 0, new Class[]{SeekBarView.class}, new String[]{"innerPaint1"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_progressBackground"));
                              var1.add(new ThemeDescription(this.intensitySeekBar, 0, new Class[]{SeekBarView.class}, new String[]{"outerPaint1"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_progress"));
                              var1.add(new ThemeDescription(this.intensityCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"));
                              RecyclerListView var7 = this.listView;
                              Drawable var4 = Theme.chat_msgInDrawable;
                              Drawable var5 = Theme.chat_msgInMediaDrawable;
                              var1.add(new ThemeDescription(var7, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var4, var5}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubble"));
                              RecyclerListView var8 = this.listView;
                              var5 = Theme.chat_msgInSelectedDrawable;
                              Drawable var9 = Theme.chat_msgInMediaSelectedDrawable;
                              var1.add(new ThemeDescription(var8, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var5, var9}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubbleSelected"));
                              var7 = this.listView;
                              var5 = Theme.chat_msgInShadowDrawable;
                              var4 = Theme.chat_msgInMediaShadowDrawable;
                              var1.add(new ThemeDescription(var7, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var5, var4}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubbleShadow"));
                              var8 = this.listView;
                              var9 = Theme.chat_msgOutDrawable;
                              var5 = Theme.chat_msgOutMediaDrawable;
                              var1.add(new ThemeDescription(var8, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var9, var5}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubble"));
                              var8 = this.listView;
                              var9 = Theme.chat_msgOutSelectedDrawable;
                              var5 = Theme.chat_msgOutMediaSelectedDrawable;
                              var1.add(new ThemeDescription(var8, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var9, var5}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubbleSelected"));
                              RecyclerListView var10 = this.listView;
                              var9 = Theme.chat_msgOutShadowDrawable;
                              var4 = Theme.chat_msgOutMediaShadowDrawable;
                              var1.add(new ThemeDescription(var10, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var9, var4}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubbleShadow"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageTextIn"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageTextOut"));
                              var10 = this.listView;
                              var9 = Theme.chat_msgOutCheckDrawable;
                              var4 = Theme.chat_msgOutHalfCheckDrawable;
                              var1.add(new ThemeDescription(var10, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var9, var4}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentCheck"));
                              var10 = this.listView;
                              var9 = Theme.chat_msgOutCheckSelectedDrawable;
                              var4 = Theme.chat_msgOutHalfCheckSelectedDrawable;
                              var1.add(new ThemeDescription(var10, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var9, var4}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentCheckSelected"));
                              var10 = this.listView;
                              var4 = Theme.chat_msgMediaCheckDrawable;
                              var9 = Theme.chat_msgMediaHalfCheckDrawable;
                              var1.add(new ThemeDescription(var10, 0, new Class[]{ChatMessageCell.class}, (Paint)null, new Drawable[]{var4, var9}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaSentCheck"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyLine"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyLine"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyNameText"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyNameText"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyMessageText"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyMessageText"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyMediaMessageSelectedText"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyMediaMessageSelectedText"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inTimeText"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outTimeText"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inTimeSelectedText"));
                              var1.add(new ThemeDescription(this.listView, 0, new Class[]{ChatMessageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outTimeSelectedText"));
                              return (ThemeDescription[])var1.toArray(new ThemeDescription[0]);
                           }

                           var1.add(new ThemeDescription(var6[var2], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_fieldOverlayText"));
                           ++var2;
                        }
                     }

                     var1.add(new ThemeDescription(var6[var2], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_fieldOverlayText"));
                     ++var2;
                  }
               }

               var1.add(new ThemeDescription(var3[var2], 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_composeShadowDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messagePanelShadow"));
               var1.add(new ThemeDescription(this.patternsButtonsContainer[var2], 0, (Class[])null, Theme.chat_composeBackgroundPaint, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messagePanelBackground"));
               ++var2;
            }
         }

         var1.add(new ThemeDescription(var3[var2], 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_composeShadowDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messagePanelShadow"));
         var1.add(new ThemeDescription(this.patternLayout[var2], 0, (Class[])null, Theme.chat_composeBackgroundPaint, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messagePanelBackground"));
         ++var2;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$0$WallpaperActivity(ImageReceiver var1, boolean var2, boolean var3) {
      if (!(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
         Drawable var6 = var1.getDrawable();
         if (var2 && var6 != null) {
            Theme.applyChatServiceMessageColor(AndroidUtilities.calcDrawableColor(var6));
            this.listView.invalidateViews();
            int var4 = this.buttonsContainer.getChildCount();

            for(int var5 = 0; var5 < var4; ++var5) {
               this.buttonsContainer.getChildAt(var5).invalidate();
            }

            RadialProgress2 var7 = this.radialProgress;
            if (var7 != null) {
               var7.setColors("chat_serviceBackground", "chat_serviceBackground", "chat_serviceText", "chat_serviceText");
            }

            if (!var3 && this.isBlurred && this.blurredBitmap == null) {
               this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(false);
               this.updateBlurred();
               this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$1$WallpaperActivity(View var1) {
      File var2 = ApplicationLoader.getFilesDirFixed();
      String var32;
      if (this.isBlurred) {
         var32 = "wallpaper_original.jpg";
      } else {
         var32 = "wallpaper.jpg";
      }

      boolean var3;
      boolean var4;
      FileOutputStream var5;
      boolean var6;
      int var10;
      Object var33;
      File var35;
      WallpapersListActivity.ColorWallpaper var36;
      MediaController.SearchImage var37;
      WallpapersListActivity.FileWallpaper var38;
      TLRPC.Photo var40;
      label181: {
         label180: {
            label179: {
               var2 = new File(var2, var32);
               var33 = this.currentWallpaper;
               var3 = var33 instanceof TLRPC.TL_wallPaper;
               var4 = false;
               Bitmap var34;
               if (var3) {
                  label158: {
                     try {
                        var34 = this.backgroundImage.getImageReceiver().getBitmap();
                        var5 = new FileOutputStream(var2);
                        var34.compress(CompressFormat.JPEG, 87, var5);
                        var5.close();
                     } catch (Exception var28) {
                        FileLog.e((Throwable)var28);
                        var6 = false;
                        break label158;
                     }

                     var6 = true;
                  }

                  var3 = var6;
                  if (var6) {
                     break label180;
                  }

                  var35 = FileLoader.getPathToAttach(((TLRPC.TL_wallPaper)this.currentWallpaper).document, true);

                  try {
                     var3 = AndroidUtilities.copyFile(var35, var2);
                     break label180;
                  } catch (Exception var30) {
                     FileLog.e((Throwable)var30);
                  }
               } else if (var33 instanceof WallpapersListActivity.ColorWallpaper) {
                  if (this.selectedPattern == null) {
                     break label179;
                  }

                  try {
                     var36 = (WallpapersListActivity.ColorWallpaper)var33;
                     Bitmap var7 = this.backgroundImage.getImageReceiver().getBitmap();
                     var34 = Bitmap.createBitmap(var7.getWidth(), var7.getHeight(), Config.ARGB_8888);
                     Canvas var39 = new Canvas(var34);
                     var39.drawColor(this.backgroundColor);
                     Paint var8 = new Paint(2);
                     PorterDuffColorFilter var9 = new PorterDuffColorFilter(this.patternColor, this.blendMode);
                     var8.setColorFilter(var9);
                     var8.setAlpha((int)(this.currentIntensity * 255.0F));
                     var39.drawBitmap(var7, 0.0F, 0.0F, var8);
                     var5 = new FileOutputStream(var2);
                     var34.compress(CompressFormat.JPEG, 87, var5);
                     var5.close();
                     break label179;
                  } catch (Throwable var31) {
                     FileLog.e(var31);
                  }
               } else {
                  if (var33 instanceof WallpapersListActivity.FileWallpaper) {
                     var38 = (WallpapersListActivity.FileWallpaper)var33;
                     var10 = var38.resId;
                     if (var10 == 0 && (long)var10 != -2L) {
                        Exception var41;
                        label186: {
                           label150: {
                              Exception var10000;
                              label188: {
                                 boolean var10001;
                                 label147: {
                                    try {
                                       if (var38.originalPath != null) {
                                          var35 = var38.originalPath;
                                          break label147;
                                       }
                                    } catch (Exception var27) {
                                       var10000 = var27;
                                       var10001 = false;
                                       break label188;
                                    }

                                    try {
                                       var35 = var38.path;
                                    } catch (Exception var26) {
                                       var10000 = var26;
                                       var10001 = false;
                                       break label188;
                                    }
                                 }

                                 try {
                                    var6 = var35.equals(var2);
                                    break label150;
                                 } catch (Exception var25) {
                                    var10000 = var25;
                                    var10001 = false;
                                 }
                              }

                              var41 = var10000;
                              var6 = false;
                              break label186;
                           }

                           if (var6) {
                              var3 = true;
                              break label181;
                           }

                           try {
                              var3 = AndroidUtilities.copyFile(var35, var2);
                              break label181;
                           } catch (Exception var24) {
                              var41 = var24;
                           }
                        }

                        FileLog.e((Throwable)var41);
                        var3 = false;
                        break label181;
                     }
                     break label179;
                  }

                  if (var33 instanceof MediaController.SearchImage) {
                     var37 = (MediaController.SearchImage)var33;
                     var40 = var37.photo;
                     if (var40 != null) {
                        var35 = FileLoader.getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(var40.sizes, this.maxWallpaperSize, true), true);
                     } else {
                        var35 = ImageLoader.getHttpFilePath(var37.imageUrl, "jpg");
                     }

                     try {
                        var3 = AndroidUtilities.copyFile(var35, var2);
                        break label180;
                     } catch (Exception var29) {
                        FileLog.e((Throwable)var29);
                     }
                  }
               }

               var3 = false;
               break label180;
            }

            var3 = true;
         }

         var6 = false;
      }

      if (this.isBlurred) {
         label127: {
            try {
               var35 = new File(ApplicationLoader.getFilesDirFixed(), "wallpaper.jpg");
               var5 = new FileOutputStream(var35);
               this.blurredBitmap.compress(CompressFormat.JPEG, 87, var5);
               var5.close();
            } catch (Throwable var23) {
               FileLog.e(var23);
               var3 = false;
               break label127;
            }

            var3 = true;
         }
      }

      var33 = this.currentWallpaper;
      long var11;
      long var13;
      long var15;
      long var17;
      if (var33 instanceof TLRPC.TL_wallPaper) {
         TLRPC.TL_wallPaper var45 = (TLRPC.TL_wallPaper)var33;
         var11 = var45.id;
         var13 = var45.access_hash;
         var15 = var11;
         var10 = 0;
         var17 = 0L;
         var35 = null;
      } else if (var33 instanceof WallpapersListActivity.ColorWallpaper) {
         var36 = (WallpapersListActivity.ColorWallpaper)var33;
         TLRPC.TL_wallPaper var42 = this.selectedPattern;
         if (var42 == null) {
            var13 = 0L;
            var17 = 0L;
            var11 = -1L;
            var15 = 0L;
         } else {
            var15 = var42.id;
            var13 = var42.access_hash;
            if (var36.id == var36.patternId && this.backgroundColor == var36.color && var36.intensity - this.currentIntensity <= 0.001F) {
               var11 = var15;
            } else {
               var11 = -1L;
            }

            var17 = this.selectedPattern.id;
         }

         var10 = this.backgroundColor;
         var35 = null;
      } else {
         if (var33 instanceof WallpapersListActivity.FileWallpaper) {
            var38 = (WallpapersListActivity.FileWallpaper)var33;
            var11 = var38.id;
            var35 = var38.path;
         } else if (var33 instanceof MediaController.SearchImage) {
            var37 = (MediaController.SearchImage)var33;
            var40 = var37.photo;
            if (var40 != null) {
               var35 = FileLoader.getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(var40.sizes, this.maxWallpaperSize, true), true);
            } else {
               var35 = ImageLoader.getHttpFilePath(var37.imageUrl, "jpg");
            }

            var11 = -1L;
         } else {
            var11 = 0L;
            var35 = null;
         }

         var17 = 0L;
         var10 = 0;
         var15 = 0L;
         var13 = 0L;
      }

      MessagesController var43 = MessagesController.getInstance(super.currentAccount);
      boolean var19 = this.isBlurred;
      boolean var20 = this.isMotion;
      float var21 = this.currentIntensity;
      boolean var22;
      if (var13 != 0L) {
         var22 = true;
      } else {
         var22 = false;
      }

      var43.saveWallpaperToServer(var35, var15, var13, var19, var20, var10, var21, var22, 0L);
      if (var3) {
         Theme.serviceMessageColorBackup = Theme.getColor("chat_serviceBackground");
         Editor var46 = MessagesController.getGlobalMainSettings().edit();
         var46.putLong("selectedBackground2", var11);
         var46.putBoolean("selectedBackgroundBlurred", this.isBlurred);
         var46.putBoolean("selectedBackgroundMotion", this.isMotion);
         var46.putInt("selectedColor", var10);
         var46.putFloat("selectedIntensity", this.currentIntensity);
         var46.putLong("selectedPattern", var17);
         var3 = var4;
         if (var11 != -2L) {
            var3 = true;
         }

         var46.putBoolean("overrideThemeWallpaper", var3);
         var46.commit();
         Theme.reloadWallpaper();
         if (!var6) {
            ImageLoader var44 = ImageLoader.getInstance();
            StringBuilder var47 = new StringBuilder();
            var47.append(ImageLoader.getHttpFileName(var2.getAbsolutePath()));
            var47.append("@100_100");
            var44.removeImage(var47.toString());
         }
      }

      WallpaperActivity.WallpaperActivityDelegate var48 = this.delegate;
      if (var48 != null) {
         var48.didSetNewBackground();
      }

      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$2$WallpaperActivity(int var1, WallpaperActivity.CheckBoxView var2, View var3) {
      if (this.buttonsContainer.getAlpha() == 1.0F) {
         if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            if (var1 == 2) {
               var2.setChecked(var2.isChecked() ^ true, true);
               this.isMotion = var2.isChecked();
               this.parallaxEffect.setEnabled(this.isMotion);
               this.animateMotionChange();
            } else {
               boolean var4 = false;
               if (var1 == 1 && this.patternLayout[var1].getVisibility() == 0) {
                  this.backgroundImage.setImageDrawable((Drawable)null);
                  this.selectedPattern = null;
                  this.isMotion = false;
                  this.updateButtonState(this.radialProgress, (Object)null, this, false, true);
                  this.updateSelectedPattern(true);
                  this.checkBoxView[1].setChecked(false, true);
                  this.patternsListView.invalidateViews();
               }

               if (this.patternLayout[var1].getVisibility() != 0) {
                  var4 = true;
               }

               this.showPatternsView(var1, var4);
            }
         } else {
            var2.setChecked(var2.isChecked() ^ true, true);
            if (var1 == 0) {
               this.isBlurred = var2.isChecked();
               this.updateBlurred();
            } else {
               this.isMotion = var2.isChecked();
               this.parallaxEffect.setEnabled(this.isMotion);
               this.animateMotionChange();
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$createView$3$WallpaperActivity(int var1, int var2) {
      if (this.isMotion) {
         AnimatorSet var3 = this.motionAnimation;
         float var4 = 1.0F;
         if (var3 != null) {
            var4 = (this.backgroundImage.getScaleX() - 1.0F) / (this.parallaxScale - 1.0F);
         }

         this.backgroundImage.setTranslationX((float)var1 * var4);
         this.backgroundImage.setTranslationY((float)var2 * var4);
      }
   }

   // $FF: synthetic method
   public void lambda$createView$4$WallpaperActivity(int var1, View var2) {
      if (var1 == 0) {
         this.setBackgroundColor(this.previousBackgroundColor);
      } else {
         this.selectedPattern = this.previousSelectedPattern;
         TLRPC.TL_wallPaper var3 = this.selectedPattern;
         if (var3 == null) {
            this.backgroundImage.setImageDrawable((Drawable)null);
         } else {
            BackupImageView var7 = this.backgroundImage;
            ImageLocation var4 = ImageLocation.getForDocument(var3.document);
            String var5 = this.imageFilter;
            var3 = this.selectedPattern;
            var7.setImage(var4, var5, (ImageLocation)null, (String)null, "jpg", var3.document.size, 1, var3);
         }

         WallpaperActivity.CheckBoxView var8 = this.checkBoxView[1];
         boolean var6;
         if (this.selectedPattern != null) {
            var6 = true;
         } else {
            var6 = false;
         }

         var8.setChecked(var6, false);
         this.currentIntensity = this.previousIntensity;
         this.intensitySeekBar.setProgress(this.currentIntensity);
         this.backgroundImage.getImageReceiver().setAlpha(this.currentIntensity);
         this.updateButtonState(this.radialProgress, (Object)null, this, false, true);
         this.updateSelectedPattern(true);
      }

      this.showPatternsView(var1, false);
   }

   // $FF: synthetic method
   public void lambda$createView$5$WallpaperActivity(int var1, View var2) {
      this.showPatternsView(var1, false);
   }

   // $FF: synthetic method
   public void lambda$createView$6$WallpaperActivity(View var1, int var2) {
      TLRPC.TL_wallPaper var5 = this.selectedPattern;
      boolean var3 = false;
      boolean var4;
      if (var5 != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (var2 == 0) {
         this.backgroundImage.setImageDrawable((Drawable)null);
         this.selectedPattern = null;
         this.isMotion = false;
         this.updateButtonState(this.radialProgress, (Object)null, this, false, true);
      } else {
         var5 = (TLRPC.TL_wallPaper)this.patterns.get(var2 - 1);
         this.backgroundImage.setImage(ImageLocation.getForDocument(var5.document), this.imageFilter, (ImageLocation)null, (String)null, "jpg", var5.document.size, 1, var5);
         this.selectedPattern = var5;
         this.isMotion = this.checkBoxView[2].isChecked();
         this.updateButtonState(this.radialProgress, (Object)null, this, false, true);
      }

      boolean var7;
      if (this.selectedPattern == null) {
         var7 = true;
      } else {
         var7 = false;
      }

      if (var4 == var7) {
         this.animateMotionChange();
         this.updateMotionButton();
      }

      this.updateSelectedPattern(true);
      WallpaperActivity.CheckBoxView var6 = this.checkBoxView[1];
      if (this.selectedPattern != null) {
         var3 = true;
      }

      var6.setChecked(var3, true);
      this.patternsListView.invalidateViews();
   }

   // $FF: synthetic method
   public void lambda$createView$7$WallpaperActivity(float var1) {
      this.currentIntensity = var1;
      this.backgroundImage.getImageReceiver().setAlpha(this.currentIntensity);
      this.backgroundImage.invalidate();
      this.patternsListView.invalidateViews();
   }

   public void onFailedDownload(String var1, boolean var2) {
      this.updateButtonState(this.radialProgress, (Object)null, this, true, var2);
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      StringBuilder var1 = new StringBuilder();
      var1.append((int)(1080.0F / AndroidUtilities.density));
      var1.append("_");
      var1.append((int)(1920.0F / AndroidUtilities.density));
      var1.append("_f");
      this.imageFilter = var1.toString();
      Point var2 = AndroidUtilities.displaySize;
      this.maxWallpaperSize = Math.min(1920, Math.max(var2.x, var2.y));
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersNeedReload);
      this.TAG = DownloadController.getInstance(super.currentAccount).generateObserverTag();
      this.textPaint = new TextPaint(1);
      this.textPaint.setColor(-1);
      this.textPaint.setTextSize((float)AndroidUtilities.dp(14.0F));
      this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.checkPaint = new Paint(1);
      this.checkPaint.setStyle(Style.STROKE);
      this.checkPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.checkPaint.setColor(0);
      this.checkPaint.setStrokeCap(Cap.ROUND);
      this.checkPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
      this.eraserPaint = new Paint(1);
      this.eraserPaint.setColor(0);
      this.eraserPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
      this.backgroundPaint = new Paint(1);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      Bitmap var1 = this.blurredBitmap;
      if (var1 != null) {
         var1.recycle();
         this.blurredBitmap = null;
      }

      Theme.applyChatServiceMessageColor();
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersNeedReload);
   }

   public void onPause() {
      super.onPause();
      if (this.isMotion) {
         this.parallaxEffect.setEnabled(false);
      }

   }

   public void onProgressDownload(String var1, float var2) {
      this.radialProgress.setProgress(var2, this.progressVisible);
      if (this.radialProgress.getIcon() != 10) {
         this.updateButtonState(this.radialProgress, (Object)null, this, false, true);
      }

   }

   public void onProgressUpload(String var1, float var2, boolean var3) {
   }

   public void onResume() {
      super.onResume();
      if (this.isMotion) {
         this.parallaxEffect.setEnabled(true);
      }

   }

   public void onSuccessDownload(String var1) {
      this.radialProgress.setProgress(1.0F, this.progressVisible);
      this.updateButtonState(this.radialProgress, (Object)null, this, false, true);
   }

   public void setDelegate(WallpaperActivity.WallpaperActivityDelegate var1) {
      this.delegate = var1;
   }

   public void setInitialModes(boolean var1, boolean var2) {
      this.isBlurred = var1;
      this.isMotion = var2;
   }

   public void setPatterns(ArrayList var1) {
      this.patterns = var1;
      Object var5 = this.currentWallpaper;
      if (var5 instanceof WallpapersListActivity.ColorWallpaper) {
         WallpapersListActivity.ColorWallpaper var2 = (WallpapersListActivity.ColorWallpaper)var5;
         if (var2.patternId != 0L) {
            int var3 = 0;

            for(int var4 = this.patterns.size(); var3 < var4; ++var3) {
               TLRPC.TL_wallPaper var6 = (TLRPC.TL_wallPaper)this.patterns.get(var3);
               if (var6.id == var2.patternId) {
                  this.selectedPattern = var6;
                  break;
               }
            }

            this.currentIntensity = var2.intensity;
         }
      }

   }

   private class CheckBoxView extends View {
      private static final float progressBounceDiff = 0.2F;
      public final Property PROGRESS_PROPERTY = new AnimationProperties.FloatProperty("progress") {
         public Float get(WallpaperActivity.CheckBoxView var1) {
            return CheckBoxView.this.progress;
         }

         public void setValue(WallpaperActivity.CheckBoxView var1, float var2) {
            CheckBoxView.this.progress = var2;
            CheckBoxView.this.invalidate();
         }
      };
      private ObjectAnimator checkAnimator;
      private String currentText;
      private int currentTextSize;
      private Bitmap drawBitmap;
      private Canvas drawCanvas;
      private boolean isChecked;
      private int maxTextSize;
      private float progress;
      private RectF rect = new RectF();

      public CheckBoxView(Context var2, boolean var3) {
         super(var2);
         if (var3) {
            this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(18.0F), AndroidUtilities.dp(18.0F), Config.ARGB_4444);
            this.drawCanvas = new Canvas(this.drawBitmap);
         }

      }

      private void animateToCheckedState(boolean var1) {
         Property var2 = this.PROGRESS_PROPERTY;
         float var3;
         if (var1) {
            var3 = 1.0F;
         } else {
            var3 = 0.0F;
         }

         this.checkAnimator = ObjectAnimator.ofFloat(this, var2, new float[]{var3});
         this.checkAnimator.setDuration(300L);
         this.checkAnimator.start();
      }

      private void cancelCheckAnimator() {
         ObjectAnimator var1 = this.checkAnimator;
         if (var1 != null) {
            var1.cancel();
         }

      }

      private void setProgress(float var1) {
         if (this.progress != var1) {
            this.progress = var1;
            this.invalidate();
         }
      }

      public boolean isChecked() {
         return this.isChecked;
      }

      protected void onDraw(Canvas var1) {
         this.rect.set(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
         var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_actionBackgroundPaint);
         int var2 = (this.getMeasuredWidth() - this.currentTextSize - AndroidUtilities.dp(28.0F)) / 2;
         var1.drawText(this.currentText, (float)(AndroidUtilities.dp(28.0F) + var2), (float)AndroidUtilities.dp(21.0F), WallpaperActivity.this.textPaint);
         var1.save();
         var1.translate((float)var2, (float)AndroidUtilities.dp(7.0F));
         RectF var9;
         if (this.drawBitmap != null) {
            float var3 = this.progress;
            float var4;
            if (var3 <= 0.5F) {
               var3 /= 0.5F;
               var4 = var3;
            } else {
               var3 = 2.0F - var3 / 0.5F;
               var4 = 1.0F;
            }

            float var5 = (float)AndroidUtilities.dp(1.0F) * var3;
            this.rect.set(var5, var5, (float)AndroidUtilities.dp(18.0F) - var5, (float)AndroidUtilities.dp(18.0F) - var5);
            this.drawBitmap.eraseColor(0);
            WallpaperActivity.this.backgroundPaint.setColor(-1);
            Canvas var6 = this.drawCanvas;
            RectF var7 = this.rect;
            var6.drawRoundRect(var7, var7.width() / 2.0F, this.rect.height() / 2.0F, WallpaperActivity.this.backgroundPaint);
            if (var4 != 1.0F) {
               var4 = Math.min((float)AndroidUtilities.dp(7.0F), (float)AndroidUtilities.dp(7.0F) * var4 + var5);
               this.rect.set((float)AndroidUtilities.dp(2.0F) + var4, (float)AndroidUtilities.dp(2.0F) + var4, (float)AndroidUtilities.dp(16.0F) - var4, (float)AndroidUtilities.dp(16.0F) - var4);
               Canvas var10 = this.drawCanvas;
               var9 = this.rect;
               var10.drawRoundRect(var9, var9.width() / 2.0F, this.rect.height() / 2.0F, WallpaperActivity.this.eraserPaint);
            }

            if (this.progress > 0.5F) {
               var4 = (float)AndroidUtilities.dp(7.3F);
               var5 = (float)AndroidUtilities.dp(2.5F);
               var3 = 1.0F - var3;
               var2 = (int)(var4 - var5 * var3);
               int var8 = (int)((float)AndroidUtilities.dp(13.0F) - (float)AndroidUtilities.dp(2.5F) * var3);
               this.drawCanvas.drawLine((float)AndroidUtilities.dp(7.3F), (float)AndroidUtilities.dp(13.0F), (float)var2, (float)var8, WallpaperActivity.this.checkPaint);
               var8 = (int)((float)AndroidUtilities.dp(7.3F) + (float)AndroidUtilities.dp(6.0F) * var3);
               var2 = (int)((float)AndroidUtilities.dp(13.0F) - (float)AndroidUtilities.dp(6.0F) * var3);
               this.drawCanvas.drawLine((float)AndroidUtilities.dp(7.3F), (float)AndroidUtilities.dp(13.0F), (float)var8, (float)var2, WallpaperActivity.this.checkPaint);
            }

            var1.drawBitmap(this.drawBitmap, 0.0F, 0.0F, (Paint)null);
         } else {
            WallpaperActivity.this.backgroundPaint.setColor(WallpaperActivity.this.backgroundColor);
            this.rect.set(0.0F, 0.0F, (float)AndroidUtilities.dp(18.0F), (float)AndroidUtilities.dp(18.0F));
            var9 = this.rect;
            var1.drawRoundRect(var9, var9.width() / 2.0F, this.rect.height() / 2.0F, WallpaperActivity.this.backgroundPaint);
         }

         var1.restore();
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(this.maxTextSize + AndroidUtilities.dp(56.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0F), 1073741824));
      }

      public void setChecked(boolean var1, boolean var2) {
         if (var1 != this.isChecked) {
            this.isChecked = var1;
            if (var2) {
               this.animateToCheckedState(var1);
            } else {
               this.cancelCheckAnimator();
               float var3;
               if (var1) {
                  var3 = 1.0F;
               } else {
                  var3 = 0.0F;
               }

               this.progress = var3;
               this.invalidate();
            }

         }
      }

      public void setText(String var1, int var2, int var3) {
         this.currentText = var1;
         this.currentTextSize = var2;
         this.maxTextSize = var3;
      }
   }

   private class ColorPicker extends FrameLayout {
      private int centerX;
      private int centerY;
      private Drawable circleDrawable;
      private Paint circlePaint;
      private boolean circlePressed;
      private EditTextBoldCursor[] colorEditText = new EditTextBoldCursor[2];
      private LinearGradient colorGradient;
      private float[] colorHSV = new float[]{0.0F, 0.0F, 1.0F};
      private boolean colorPressed;
      private Bitmap colorWheelBitmap;
      private Paint colorWheelPaint;
      private int colorWheelRadius;
      private float[] hsvTemp = new float[3];
      boolean ignoreTextChange;
      private LinearLayout linearLayout;
      private int lx;
      private int ly;
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
         this.addView(this.linearLayout, LayoutHelper.createFrame(-1, 64.0F, 51, 12.0F, 14.0F, 21.0F, 0.0F));

         for(final int var3 = 0; var3 < 2; ++var3) {
            this.colorEditText[var3] = new EditTextBoldCursor(var2);
            this.colorEditText[var3].setTextSize(1, 18.0F);
            this.colorEditText[var3].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.colorEditText[var3].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.colorEditText[var3].setBackgroundDrawable((Drawable)null);
            this.colorEditText[var3].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.colorEditText[var3].setCursorSize(AndroidUtilities.dp(20.0F));
            this.colorEditText[var3].setCursorWidth(1.5F);
            this.colorEditText[var3].setSingleLine(true);
            this.colorEditText[var3].setGravity(19);
            this.colorEditText[var3].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            this.colorEditText[var3].setTransformHintToHeader(true);
            this.colorEditText[var3].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
            this.colorEditText[var3].setPadding(0, 0, 0, 0);
            if (var3 == 0) {
               this.colorEditText[var3].setInputType(1);
               this.colorEditText[var3].setHintText(LocaleController.getString("BackgroundHexColorCode", 2131558817));
            } else {
               this.colorEditText[var3].setInputType(2);
               this.colorEditText[var3].setHintText(LocaleController.getString("BackgroundBrightness", 2131558811));
            }

            this.colorEditText[var3].setImeOptions(268435462);
            byte var4;
            if (var3 == 0) {
               var4 = 7;
            } else {
               var4 = 3;
            }

            LengthFilter var5 = new LengthFilter(var4);
            this.colorEditText[var3].setFilters(new InputFilter[]{var5});
            LinearLayout var6 = this.linearLayout;
            EditTextBoldCursor var8 = this.colorEditText[var3];
            float var7;
            if (var3 == 0) {
               var7 = 0.67F;
            } else {
               var7 = 0.31F;
            }

            if (var3 != 1) {
               var4 = 23;
            } else {
               var4 = 0;
            }

            var6.addView(var8, LayoutHelper.createLinear(0, -1, var7, 0, 0, var4, 0));
            this.colorEditText[var3].addTextChangedListener(new TextWatcher() {
               public void afterTextChanged(Editable var1) {
                  WallpaperActivity.ColorPicker var2 = ColorPicker.this;
                  if (!var2.ignoreTextChange) {
                     var2.ignoreTextChange = true;
                     int var3x;
                     int var5;
                     WallpaperActivity.ColorPicker var7;
                     if (var3 == 0) {
                        for(var3x = 0; var3x < var1.length(); var3x = var5 + 1) {
                           char var4 = var1.charAt(var3x);
                           if (var4 >= '0') {
                              var5 = var3x;
                              if (var4 <= '9') {
                                 continue;
                              }
                           }

                           if (var4 >= 'a') {
                              var5 = var3x;
                              if (var4 <= 'f') {
                                 continue;
                              }
                           }

                           if (var4 >= 'A') {
                              var5 = var3x;
                              if (var4 <= 'F') {
                                 continue;
                              }
                           }

                           if (var4 == '#') {
                              var5 = var3x;
                              if (var3x == 0) {
                                 continue;
                              }
                           }

                           var1.replace(var3x, var3x + 1, "");
                           var5 = var3x - 1;
                        }

                        if (var1.length() == 0) {
                           var1.append("#");
                        } else if (var1.charAt(0) != '#') {
                           var1.insert(0, "#");
                        }

                        try {
                           ColorPicker.this.setColor(Integer.parseInt(var1.toString().substring(1), 16) | -16777216);
                        } catch (Exception var6) {
                           ColorPicker.this.setColor(-1);
                        }

                        var7 = ColorPicker.this;
                        WallpaperActivity.this.setBackgroundColor(var7.getColor());
                        EditTextBoldCursor var9 = ColorPicker.this.colorEditText[1];
                        StringBuilder var8 = new StringBuilder();
                        var8.append("");
                        var8.append((int)(ColorPicker.this.colorHSV[2] * 255.0F));
                        var9.setText(var8.toString());
                     } else {
                        label77: {
                           var5 = Utilities.parseInt(var1.toString());
                           if (var5 <= 255) {
                              var3x = var5;
                              if (var5 >= 0) {
                                 break label77;
                              }
                           }

                           if (var5 > 255) {
                              var3x = 255;
                           } else {
                              var3x = 0;
                           }

                           var5 = var1.length();
                           StringBuilder var10 = new StringBuilder();
                           var10.append("");
                           var10.append(var3x);
                           var1.replace(0, var5, var10.toString());
                        }

                        ColorPicker.this.colorHSV[2] = (float)var3x / 255.0F;
                        var7 = ColorPicker.this;
                        WallpaperActivity.this.setBackgroundColor(var7.getColor());
                        var3x = Color.red(WallpaperActivity.this.backgroundColor);
                        int var11 = Color.green(WallpaperActivity.this.backgroundColor);
                        var5 = Color.blue(WallpaperActivity.this.backgroundColor);
                        ColorPicker.this.colorEditText[0].setText(String.format("#%02x%02x%02x", (byte)var3x, (byte)var11, (byte)var5));
                     }

                     ColorPicker.this.invalidate();
                     ColorPicker.this.ignoreTextChange = false;
                  }
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3x, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3x, int var4) {
               }
            });
            this.colorEditText[var3].setOnEditorActionListener(_$$Lambda$WallpaperActivity$ColorPicker$DX_Z3G7ZN8lj8LNj53R64xuAIvg.INSTANCE);
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

      public int getColor() {
         return Color.HSVToColor(this.colorHSV) & 16777215 | -16777216;
      }

      protected void onDraw(Canvas var1) {
         this.centerX = this.getWidth() / 2 - this.paramValueSliderWidth * 2 + AndroidUtilities.dp(11.0F);
         this.centerY = this.getHeight() / 2 + AndroidUtilities.dp(34.0F);
         Bitmap var2 = this.colorWheelBitmap;
         int var3 = this.centerX;
         int var4 = this.colorWheelRadius;
         var1.drawBitmap(var2, (float)(var3 - var4), (float)(this.centerY - var4), (Paint)null);
         double var5 = (double)((float)Math.toRadians((double)this.colorHSV[0]));
         double var7 = -Math.cos(var5);
         double var9 = (double)this.colorHSV[1];
         Double.isNaN(var9);
         double var11 = (double)this.colorWheelRadius;
         Double.isNaN(var11);
         var3 = (int)(var7 * var9 * var11);
         int var13 = this.centerX;
         var5 = -Math.sin(var5);
         float[] var14 = this.colorHSV;
         var11 = (double)var14[1];
         Double.isNaN(var11);
         var7 = (double)this.colorWheelRadius;
         Double.isNaN(var7);
         var4 = (int)(var5 * var11 * var7);
         int var15 = this.centerY;
         float[] var20 = this.hsvTemp;
         var20[0] = var14[0];
         var20[1] = var14[1];
         var20[2] = 1.0F;
         this.drawPointerArrow(var1, var3 + var13, var4 + var15, Color.HSVToColor(var20));
         var4 = this.centerX;
         var3 = this.colorWheelRadius;
         this.lx = var4 + var3 + this.paramValueSliderWidth * 2;
         this.ly = this.centerY - var3;
         var3 = AndroidUtilities.dp(9.0F);
         var4 = this.colorWheelRadius * 2;
         float var19;
         if (this.colorGradient == null) {
            var15 = this.lx;
            float var16 = (float)var15;
            var13 = this.ly;
            float var17 = (float)var13;
            float var18 = (float)(var15 + var3);
            var19 = (float)(var13 + var4);
            var15 = Color.HSVToColor(this.hsvTemp);
            TileMode var21 = TileMode.CLAMP;
            this.colorGradient = new LinearGradient(var16, var17, var18, var19, new int[]{-16777216, var15}, (float[])null, var21);
         }

         this.valueSliderPaint.setShader(this.colorGradient);
         var15 = this.lx;
         var19 = (float)var15;
         var13 = this.ly;
         var1.drawRect(var19, (float)var13, (float)(var15 + var3), (float)(var13 + var4), this.valueSliderPaint);
         var15 = this.lx;
         var3 /= 2;
         var19 = (float)this.ly;
         var20 = this.colorHSV;
         this.drawPointerArrow(var1, var15 + var3, (int)(var19 + var20[2] * (float)var4), Color.HSVToColor(var20));
      }

      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         var1 = Math.min(var3, MeasureSpec.getSize(var2));
         this.measureChild(this.linearLayout, MeasureSpec.makeMeasureSpec(var3 - AndroidUtilities.dp(42.0F), 1073741824), var2);
         this.setMeasuredDimension(var1, var1);
      }

      protected void onSizeChanged(int var1, int var2, int var3, int var4) {
         if (this.colorWheelRadius != AndroidUtilities.dp(120.0F)) {
            this.colorWheelRadius = AndroidUtilities.dp(120.0F);
            var1 = this.colorWheelRadius;
            this.colorWheelBitmap = this.createColorWheelBitmap(var1 * 2, var1 * 2);
            this.colorGradient = null;
         }

      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var2 = var1.getAction();
         if (var2 != 0) {
            label98: {
               if (var2 != 1) {
                  if (var2 == 2) {
                     break label98;
                  }
               } else {
                  this.colorPressed = false;
                  this.circlePressed = false;
               }

               return super.onTouchEvent(var1);
            }
         }

         int var3 = (int)var1.getX();
         var2 = (int)var1.getY();
         int var4 = var3 - this.centerX;
         int var5 = var2 - this.centerY;
         double var6 = Math.sqrt((double)(var4 * var4 + var5 * var5));
         if (this.circlePressed || !this.colorPressed && var6 <= (double)this.colorWheelRadius) {
            int var8 = this.colorWheelRadius;
            double var9 = var6;
            if (var6 > (double)var8) {
               var9 = (double)var8;
            }

            if (!this.circlePressed) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
            }

            this.circlePressed = true;
            this.colorHSV[0] = (float)(Math.toDegrees(Math.atan2((double)var5, (double)var4)) + 180.0D);
            float[] var14 = this.colorHSV;
            var6 = (double)this.colorWheelRadius;
            Double.isNaN(var6);
            var14[1] = Math.max(0.0F, Math.min(1.0F, (float)(var9 / var6)));
            this.colorGradient = null;
         }

         label94: {
            if (!this.colorPressed) {
               if (this.circlePressed) {
                  break label94;
               }

               var5 = this.lx;
               if (var3 < var5 || var3 > var5 + this.paramValueSliderWidth) {
                  break label94;
               }

               var3 = this.ly;
               if (var2 < var3 || var2 > var3 + this.colorWheelRadius * 2) {
                  break label94;
               }
            }

            float var11 = (float)(var2 - this.ly) / ((float)this.colorWheelRadius * 2.0F);
            float var12;
            if (var11 < 0.0F) {
               var12 = 0.0F;
            } else {
               var12 = var11;
               if (var11 > 1.0F) {
                  var12 = 1.0F;
               }
            }

            this.colorHSV[2] = var12;
            if (!this.colorPressed) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
            }

            this.colorPressed = true;
         }

         if (this.colorPressed || this.circlePressed) {
            WallpaperActivity.this.setBackgroundColor(this.getColor());
            if (!this.ignoreTextChange) {
               var3 = Color.red(WallpaperActivity.this.backgroundColor);
               var2 = Color.green(WallpaperActivity.this.backgroundColor);
               var5 = Color.blue(WallpaperActivity.this.backgroundColor);
               this.ignoreTextChange = true;
               this.colorEditText[0].setText(String.format("#%02x%02x%02x", (byte)var3, (byte)var2, (byte)var5));
               EditTextBoldCursor var15 = this.colorEditText[1];
               StringBuilder var13 = new StringBuilder();
               var13.append("");
               var13.append((int)(this.colorHSV[2] * 255.0F));
               var15.setText(var13.toString());

               for(var2 = 0; var2 < 2; ++var2) {
                  EditTextBoldCursor[] var16 = this.colorEditText;
                  var16[var2].setSelection(var16[var2].length());
               }

               this.ignoreTextChange = false;
            }

            this.invalidate();
         }

         return true;
      }

      public void setColor(int var1) {
         if (!this.ignoreTextChange) {
            this.ignoreTextChange = true;
            int var2 = Color.red(var1);
            int var3 = Color.green(var1);
            int var4 = Color.blue(var1);
            Color.colorToHSV(var1, this.colorHSV);
            this.colorEditText[0].setText(String.format("#%02x%02x%02x", (byte)var2, (byte)var3, (byte)var4));
            EditTextBoldCursor var5 = this.colorEditText[1];
            StringBuilder var6 = new StringBuilder();
            var6.append("");
            var6.append((int)(this.colorHSV[2] * 255.0F));
            var5.setText(var6.toString());

            for(var1 = 0; var1 < 2; ++var1) {
               EditTextBoldCursor[] var7 = this.colorEditText;
               var7[var1].setSelection(var7[var1].length());
            }

            this.ignoreTextChange = false;
         } else {
            Color.colorToHSV(var1, this.colorHSV);
         }

         this.colorGradient = null;
         this.invalidate();
      }
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;
      private ArrayList messages;

      public ListAdapter(Context var2) {
         this.mContext = var2;
         this.messages = new ArrayList();
         int var3 = (int)(System.currentTimeMillis() / 1000L) - 3600;
         TLRPC.TL_message var6 = new TLRPC.TL_message();
         if (WallpaperActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            var6.message = LocaleController.getString("BackgroundColorSinglePreviewLine2", 2131558816);
         } else {
            var6.message = LocaleController.getString("BackgroundPreviewLine2", 2131558823);
         }

         int var4 = var3 + 60;
         var6.date = var4;
         var6.dialog_id = 1L;
         var6.flags = 259;
         var6.from_id = UserConfig.getInstance(WallpaperActivity.access$3000(WallpaperActivity.this)).getClientUserId();
         var6.id = 1;
         var6.media = new TLRPC.TL_messageMediaEmpty();
         var6.out = true;
         var6.to_id = new TLRPC.TL_peerUser();
         var6.to_id.user_id = 0;
         MessageObject var7 = new MessageObject(WallpaperActivity.access$3100(WallpaperActivity.this), var6, true);
         var7.eventId = 1L;
         var7.resetLayout();
         this.messages.add(var7);
         var6 = new TLRPC.TL_message();
         if (WallpaperActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            var6.message = LocaleController.getString("BackgroundColorSinglePreviewLine1", 2131558815);
         } else {
            var6.message = LocaleController.getString("BackgroundPreviewLine1", 2131558822);
         }

         var6.date = var4;
         var6.dialog_id = 1L;
         var6.flags = 265;
         var6.from_id = 0;
         var6.id = 1;
         var6.reply_to_msg_id = 5;
         var6.media = new TLRPC.TL_messageMediaEmpty();
         var6.out = false;
         var6.to_id = new TLRPC.TL_peerUser();
         var6.to_id.user_id = UserConfig.getInstance(WallpaperActivity.access$3200(WallpaperActivity.this)).getClientUserId();
         var7 = new MessageObject(WallpaperActivity.access$3300(WallpaperActivity.this), var6, true);
         var7.eventId = 1L;
         var7.resetLayout();
         this.messages.add(var7);
         var6 = new TLRPC.TL_message();
         var6.message = LocaleController.formatDateChat((long)var3);
         var6.id = 0;
         var6.date = var3;
         MessageObject var5 = new MessageObject(WallpaperActivity.access$3400(WallpaperActivity.this), var6, false);
         var5.type = 10;
         var5.contentType = 1;
         var5.isDateObject = true;
         this.messages.add(var5);
      }

      public int getItemCount() {
         return this.messages.size();
      }

      public int getItemViewType(int var1) {
         return var1 >= 0 && var1 < this.messages.size() ? ((MessageObject)this.messages.get(var1)).contentType : 4;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         MessageObject var3 = (MessageObject)this.messages.get(var2);
         View var4 = var1.itemView;
         if (var4 instanceof ChatMessageCell) {
            boolean var5;
            int var8;
            boolean var10;
            ChatMessageCell var14;
            label31: {
               var14 = (ChatMessageCell)var4;
               var5 = false;
               var14.isChat = false;
               int var6 = var2 - 1;
               int var7 = this.getItemViewType(var6);
               var8 = var2 + 1;
               var2 = this.getItemViewType(var8);
               if (!(var3.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && var7 == var1.getItemViewType()) {
                  MessageObject var9 = (MessageObject)this.messages.get(var6);
                  if (var9.isOutOwner() == var3.isOutOwner() && Math.abs(var9.messageOwner.date - var3.messageOwner.date) <= 300) {
                     var10 = true;
                     break label31;
                  }
               }

               var10 = false;
            }

            boolean var11 = var5;
            if (var2 == var1.getItemViewType()) {
               MessageObject var12 = (MessageObject)this.messages.get(var8);
               var11 = var5;
               if (!(var12.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup)) {
                  var11 = var5;
                  if (var12.isOutOwner() == var3.isOutOwner()) {
                     var11 = var5;
                     if (Math.abs(var12.messageOwner.date - var3.messageOwner.date) <= 300) {
                        var11 = true;
                     }
                  }
               }
            }

            var14.setFullyDraw(true);
            var14.setMessageObject(var3, (MessageObject.GroupedMessages)null, var10, var11);
         } else if (var4 instanceof ChatActionCell) {
            ChatActionCell var13 = (ChatActionCell)var4;
            var13.setMessageObject(var3);
            var13.setAlpha(1.0F);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 == 0) {
            var3 = new ChatMessageCell(this.mContext);
            ((ChatMessageCell)var3).setDelegate(new ChatMessageCell.ChatMessageCellDelegate() {
               // $FF: synthetic method
               public boolean canPerformActions() {
                  return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$canPerformActions(this);
               }

               // $FF: synthetic method
               public void didLongPress(ChatMessageCell var1, float var2, float var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didLongPress(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressBotButton(ChatMessageCell var1, TLRPC.KeyboardButton var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressBotButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressCancelSendButton(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressCancelSendButton(this, var1);
               }

               // $FF: synthetic method
               public void didPressChannelAvatar(ChatMessageCell var1, TLRPC.Chat var2, int var3, float var4, float var5) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressChannelAvatar(this, var1, var2, var3, var4, var5);
               }

               // $FF: synthetic method
               public void didPressHiddenForward(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressHiddenForward(this, var1);
               }

               // $FF: synthetic method
               public void didPressImage(ChatMessageCell var1, float var2, float var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressImage(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressInstantButton(ChatMessageCell var1, int var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressInstantButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressOther(ChatMessageCell var1, float var2, float var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressOther(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressReplyMessage(ChatMessageCell var1, int var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressReplyMessage(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressShare(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressShare(this, var1);
               }

               // $FF: synthetic method
               public void didPressUrl(MessageObject var1, CharacterStyle var2, boolean var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressUrl(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressUserAvatar(ChatMessageCell var1, TLRPC.User var2, float var3, float var4) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressUserAvatar(this, var1, var2, var3, var4);
               }

               // $FF: synthetic method
               public void didPressViaBot(ChatMessageCell var1, String var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressViaBot(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressVoteButton(ChatMessageCell var1, TLRPC.TL_pollAnswer var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressVoteButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didStartVideoStream(MessageObject var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didStartVideoStream(this, var1);
               }

               // $FF: synthetic method
               public boolean isChatAdminCell(int var1) {
                  return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$isChatAdminCell(this, var1);
               }

               // $FF: synthetic method
               public void needOpenWebView(String var1, String var2, String var3, String var4, int var5, int var6) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$needOpenWebView(this, var1, var2, var3, var4, var5, var6);
               }

               // $FF: synthetic method
               public boolean needPlayMessage(MessageObject var1) {
                  return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$needPlayMessage(this, var1);
               }

               // $FF: synthetic method
               public void videoTimerReached() {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$videoTimerReached(this);
               }
            });
         } else if (var2 == 1) {
            var3 = new ChatActionCell(this.mContext);
            ((ChatActionCell)var3).setDelegate(new ChatActionCell.ChatActionCellDelegate() {
               // $FF: synthetic method
               public void didClickImage(ChatActionCell var1) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$didClickImage(this, var1);
               }

               // $FF: synthetic method
               public void didLongPress(ChatActionCell var1, float var2, float var3) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$didLongPress(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressBotButton(MessageObject var1, TLRPC.KeyboardButton var2) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$didPressBotButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressReplyMessage(ChatActionCell var1, int var2) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$didPressReplyMessage(this, var1, var2);
               }

               // $FF: synthetic method
               public void needOpenUserProfile(int var1) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$needOpenUserProfile(this, var1);
               }
            });
         } else {
            var3 = null;
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }

   private class PatternCell extends BackupImageView implements DownloadController.FileDownloadProgressListener {
      private int TAG;
      private TLRPC.TL_wallPaper currentPattern;
      private RadialProgress2 radialProgress;
      private RectF rect = new RectF();
      private boolean wasSelected;

      public PatternCell(Context var2) {
         super(var2);
         this.setRoundRadius(AndroidUtilities.dp(6.0F));
         this.radialProgress = new RadialProgress2(this);
         this.radialProgress.setProgressRect(AndroidUtilities.dp(30.0F), AndroidUtilities.dp(30.0F), AndroidUtilities.dp(70.0F), AndroidUtilities.dp(70.0F));
         this.TAG = DownloadController.getInstance(WallpaperActivity.access$400(WallpaperActivity.this)).generateObserverTag();
      }

      private void setPattern(TLRPC.TL_wallPaper var1) {
         this.currentPattern = var1;
         if (var1 != null) {
            this.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(var1.document.thumbs, 100), var1.document), "100_100", (ImageLocation)null, (String)null, "jpg", 0, 1, var1);
         } else {
            this.setImageDrawable((Drawable)null);
         }

         this.updateSelected(false);
      }

      public int getObserverTag() {
         return this.TAG;
      }

      protected void onAttachedToWindow() {
         super.onAttachedToWindow();
         this.updateSelected(false);
      }

      protected void onDraw(Canvas var1) {
         this.getImageReceiver().setAlpha(0.8F);
         WallpaperActivity.this.backgroundPaint.setColor(WallpaperActivity.this.backgroundColor);
         this.rect.set(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
         var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp(6.0F), WallpaperActivity.this.backgroundPaint);
         super.onDraw(var1);
         this.radialProgress.setColors(WallpaperActivity.this.patternColor, WallpaperActivity.this.patternColor, -1, -1);
         this.radialProgress.draw(var1);
      }

      public void onFailedDownload(String var1, boolean var2) {
         WallpaperActivity.this.updateButtonState(this.radialProgress, this.currentPattern, this, true, var2);
      }

      protected void onMeasure(int var1, int var2) {
         this.setMeasuredDimension(AndroidUtilities.dp(100.0F), AndroidUtilities.dp(100.0F));
      }

      public void onProgressDownload(String var1, float var2) {
         this.radialProgress.setProgress(var2, WallpaperActivity.this.progressVisible);
         if (this.radialProgress.getIcon() != 10) {
            WallpaperActivity.this.updateButtonState(this.radialProgress, this.currentPattern, this, false, true);
         }

      }

      public void onProgressUpload(String var1, float var2, boolean var3) {
      }

      public void onSuccessDownload(String var1) {
         this.radialProgress.setProgress(1.0F, WallpaperActivity.this.progressVisible);
         WallpaperActivity.this.updateButtonState(this.radialProgress, this.currentPattern, this, false, true);
      }

      public void updateSelected(boolean var1) {
         boolean var3;
         label31: {
            if (this.currentPattern != null || WallpaperActivity.this.selectedPattern != null) {
               label30: {
                  if (WallpaperActivity.this.selectedPattern != null) {
                     TLRPC.TL_wallPaper var2 = this.currentPattern;
                     if (var2 != null && var2.id == WallpaperActivity.this.selectedPattern.id) {
                        break label30;
                     }
                  }

                  var3 = false;
                  break label31;
               }
            }

            var3 = true;
         }

         if (var3) {
            WallpaperActivity var4 = WallpaperActivity.this;
            var4.updateButtonState(this.radialProgress, var4.selectedPattern, this, false, var1);
         } else {
            this.radialProgress.setIcon(4, false, var1);
         }

         this.invalidate();
      }
   }

   private class PatternsAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public PatternsAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         int var1;
         if (WallpaperActivity.this.patterns != null) {
            var1 = WallpaperActivity.this.patterns.size();
         } else {
            var1 = 0;
         }

         return var1 + 1;
      }

      public int getItemViewType(int var1) {
         return super.getItemViewType(var1);
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         WallpaperActivity.PatternCell var3 = (WallpaperActivity.PatternCell)var1.itemView;
         if (var2 == 0) {
            var3.setPattern((TLRPC.TL_wallPaper)null);
         } else {
            var3.setPattern((TLRPC.TL_wallPaper)WallpaperActivity.this.patterns.get(var2 - 1));
         }

         var3.getImageReceiver().setColorFilter(new PorterDuffColorFilter(WallpaperActivity.this.patternColor, WallpaperActivity.this.blendMode));
      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         return new RecyclerListView.Holder(WallpaperActivity.this.new PatternCell(this.mContext));
      }
   }

   public interface WallpaperActivityDelegate {
      void didSetNewBackground();
   }
}
