package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuffColorFilter;
import android.graphics.SurfaceTexture;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Looper;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
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
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.PhotoEditRadioCell;
import org.telegram.ui.Cells.PhotoEditToolCell;

@SuppressLint({"NewApi"})
public class PhotoFilterView extends FrameLayout {
   private static final int curveDataStep = 2;
   private static final int curveGranularity = 100;
   private Bitmap bitmapToEdit;
   private float blurAngle;
   private PhotoFilterBlurControl blurControl;
   private float blurExcludeBlurSize;
   private Point blurExcludePoint;
   private float blurExcludeSize;
   private ImageView blurItem;
   private FrameLayout blurLayout;
   private TextView blurLinearButton;
   private TextView blurOffButton;
   private TextView blurRadialButton;
   private int blurType;
   private TextView cancelTextView;
   private int contrastTool = 2;
   private float contrastValue;
   private ImageView curveItem;
   private FrameLayout curveLayout;
   private RadioButton[] curveRadioButton = new RadioButton[4];
   private PhotoFilterCurvesControl curvesControl;
   private PhotoFilterView.CurvesToolValue curvesToolValue;
   private TextView doneTextView;
   private PhotoFilterView.EGLThread eglThread;
   private int enhanceTool = 0;
   private float enhanceValue;
   private int exposureTool = 1;
   private float exposureValue;
   private int fadeTool = 5;
   private float fadeValue;
   private int grainTool = 9;
   private float grainValue;
   private int highlightsTool = 6;
   private float highlightsValue;
   private MediaController.SavedFilterState lastState;
   private int orientation;
   private RecyclerListView recyclerListView;
   private int saturationTool = 3;
   private float saturationValue;
   private int selectedTool;
   private int shadowsTool = 7;
   private float shadowsValue;
   private int sharpenTool = 10;
   private float sharpenValue;
   private boolean showOriginal;
   private TextureView textureView;
   private int tintHighlightsColor;
   private int tintHighlightsTool = 12;
   private int tintShadowsColor;
   private int tintShadowsTool = 11;
   private FrameLayout toolsView;
   private ImageView tuneItem;
   private int vignetteTool = 8;
   private float vignetteValue;
   private int warmthTool = 4;
   private float warmthValue;

   public PhotoFilterView(Context var1, Bitmap var2, int var3, MediaController.SavedFilterState var4) {
      super(var1);
      if (var4 != null) {
         this.enhanceValue = var4.enhanceValue;
         this.exposureValue = var4.exposureValue;
         this.contrastValue = var4.contrastValue;
         this.warmthValue = var4.warmthValue;
         this.saturationValue = var4.saturationValue;
         this.fadeValue = var4.fadeValue;
         this.tintShadowsColor = var4.tintShadowsColor;
         this.tintHighlightsColor = var4.tintHighlightsColor;
         this.highlightsValue = var4.highlightsValue;
         this.shadowsValue = var4.shadowsValue;
         this.vignetteValue = var4.vignetteValue;
         this.grainValue = var4.grainValue;
         this.blurType = var4.blurType;
         this.sharpenValue = var4.sharpenValue;
         this.curvesToolValue = var4.curvesToolValue;
         this.blurExcludeSize = var4.blurExcludeSize;
         this.blurExcludePoint = var4.blurExcludePoint;
         this.blurExcludeBlurSize = var4.blurExcludeBlurSize;
         this.blurAngle = var4.blurAngle;
         this.lastState = var4;
      } else {
         this.curvesToolValue = new PhotoFilterView.CurvesToolValue();
         this.blurExcludeSize = 0.35F;
         this.blurExcludePoint = new Point(0.5F, 0.5F);
         this.blurExcludeBlurSize = 0.15F;
         this.blurAngle = 1.5707964F;
      }

      this.bitmapToEdit = var2;
      this.orientation = var3;
      this.textureView = new TextureView(var1);
      this.addView(this.textureView, LayoutHelper.createFrame(-1, -1, 51));
      this.textureView.setVisibility(4);
      this.textureView.setSurfaceTextureListener(new SurfaceTextureListener() {
         // $FF: synthetic method
         public void lambda$onSurfaceTextureSizeChanged$0$PhotoFilterView$1() {
            if (PhotoFilterView.this.eglThread != null) {
               PhotoFilterView.this.eglThread.requestRender(false, true);
            }

         }

         public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
            if (PhotoFilterView.this.eglThread == null && var1 != null) {
               PhotoFilterView var4 = PhotoFilterView.this;
               var4.eglThread = var4.new EGLThread(var1, var4.bitmapToEdit);
               PhotoFilterView.this.eglThread.setSurfaceTextureSize(var2, var3);
               PhotoFilterView.this.eglThread.requestRender(true, true);
            }

         }

         public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
            if (PhotoFilterView.this.eglThread != null) {
               PhotoFilterView.this.eglThread.shutdown();
               PhotoFilterView.this.eglThread = null;
            }

            return true;
         }

         public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
            if (PhotoFilterView.this.eglThread != null) {
               PhotoFilterView.this.eglThread.setSurfaceTextureSize(var2, var3);
               PhotoFilterView.this.eglThread.requestRender(false, true);
               PhotoFilterView.this.eglThread.postRunnable(new _$$Lambda$PhotoFilterView$1$ykukh__An3a5DPXda05svw9B55U(this));
            }

         }

         public void onSurfaceTextureUpdated(SurfaceTexture var1) {
         }
      });
      this.blurControl = new PhotoFilterBlurControl(var1);
      this.blurControl.setVisibility(4);
      this.addView(this.blurControl, LayoutHelper.createFrame(-1, -1, 51));
      this.blurControl.setDelegate(new _$$Lambda$PhotoFilterView$wsvuAfZFAJpyt9V1ZYZMPETINLA(this));
      this.curvesControl = new PhotoFilterCurvesControl(var1, this.curvesToolValue);
      this.curvesControl.setDelegate(new _$$Lambda$PhotoFilterView$Q8Q0QxhBgkn0x_QyOvZIPgaDQJM(this));
      this.curvesControl.setVisibility(4);
      this.addView(this.curvesControl, LayoutHelper.createFrame(-1, -1, 51));
      this.toolsView = new FrameLayout(var1);
      this.addView(this.toolsView, LayoutHelper.createFrame(-1, 186, 83));
      FrameLayout var9 = new FrameLayout(var1);
      var9.setBackgroundColor(-16777216);
      this.toolsView.addView(var9, LayoutHelper.createFrame(-1, 48, 83));
      this.cancelTextView = new TextView(var1);
      this.cancelTextView.setTextSize(1, 14.0F);
      this.cancelTextView.setTextColor(-1);
      this.cancelTextView.setGravity(17);
      this.cancelTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
      this.cancelTextView.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      this.cancelTextView.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
      this.cancelTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var9.addView(this.cancelTextView, LayoutHelper.createFrame(-2, -1, 51));
      this.doneTextView = new TextView(var1);
      this.doneTextView.setTextSize(1, 14.0F);
      this.doneTextView.setTextColor(-11420173);
      this.doneTextView.setGravity(17);
      this.doneTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
      this.doneTextView.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      this.doneTextView.setText(LocaleController.getString("Done", 2131559299).toUpperCase());
      this.doneTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var9.addView(this.doneTextView, LayoutHelper.createFrame(-2, -1, 53));
      LinearLayout var12 = new LinearLayout(var1);
      var9.addView(var12, LayoutHelper.createFrame(-2, -1, 1));
      this.tuneItem = new ImageView(var1);
      this.tuneItem.setScaleType(ScaleType.CENTER);
      this.tuneItem.setImageResource(2131165751);
      this.tuneItem.setColorFilter(new PorterDuffColorFilter(-9649153, Mode.MULTIPLY));
      this.tuneItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
      var12.addView(this.tuneItem, LayoutHelper.createLinear(56, 48));
      this.tuneItem.setOnClickListener(new _$$Lambda$PhotoFilterView$4hF97H88dpzJN2DxB_cny05r_QI(this));
      this.blurItem = new ImageView(var1);
      this.blurItem.setScaleType(ScaleType.CENTER);
      this.blurItem.setImageResource(2131165882);
      this.blurItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
      var12.addView(this.blurItem, LayoutHelper.createLinear(56, 48));
      this.blurItem.setOnClickListener(new _$$Lambda$PhotoFilterView$DKhAjcvz5psQCw7kBhsHKlWIYQM(this));
      this.curveItem = new ImageView(var1);
      this.curveItem.setScaleType(ScaleType.CENTER);
      this.curveItem.setImageResource(2131165884);
      this.curveItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
      var12.addView(this.curveItem, LayoutHelper.createLinear(56, 48));
      this.curveItem.setOnClickListener(new _$$Lambda$PhotoFilterView$F_KOwGrCZ7lau2u49edpZx3V5ug(this));
      this.recyclerListView = new RecyclerListView(var1);
      LinearLayoutManager var10 = new LinearLayoutManager(var1);
      var10.setOrientation(1);
      this.recyclerListView.setLayoutManager(var10);
      this.recyclerListView.setClipToPadding(false);
      this.recyclerListView.setOverScrollMode(2);
      this.recyclerListView.setAdapter(new PhotoFilterView.ToolsAdapter(var1));
      this.toolsView.addView(this.recyclerListView, LayoutHelper.createFrame(-1, 120, 51));
      this.curveLayout = new FrameLayout(var1);
      this.curveLayout.setVisibility(4);
      this.toolsView.addView(this.curveLayout, LayoutHelper.createFrame(-1, 78.0F, 1, 0.0F, 40.0F, 0.0F, 0.0F));
      LinearLayout var5 = new LinearLayout(var1);
      var5.setOrientation(0);
      this.curveLayout.addView(var5, LayoutHelper.createFrame(-2, -2, 1));

      for(var3 = 0; var3 < 4; ++var3) {
         FrameLayout var13 = new FrameLayout(var1);
         var13.setTag(var3);
         this.curveRadioButton[var3] = new RadioButton(var1);
         this.curveRadioButton[var3].setSize(AndroidUtilities.dp(20.0F));
         var13.addView(this.curveRadioButton[var3], LayoutHelper.createFrame(30, 30, 49));
         TextView var11 = new TextView(var1);
         var11.setTextSize(1, 12.0F);
         var11.setGravity(16);
         String var6;
         StringBuilder var7;
         if (var3 == 0) {
            var6 = LocaleController.getString("CurvesAll", 2131559182);
            var7 = new StringBuilder();
            var7.append(var6.substring(0, 1).toUpperCase());
            var7.append(var6.substring(1).toLowerCase());
            var11.setText(var7.toString());
            var11.setTextColor(-1);
            this.curveRadioButton[var3].setColor(-1, -1);
         } else if (var3 == 1) {
            var6 = LocaleController.getString("CurvesRed", 2131559185);
            var7 = new StringBuilder();
            var7.append(var6.substring(0, 1).toUpperCase());
            var7.append(var6.substring(1).toLowerCase());
            var11.setText(var7.toString());
            var11.setTextColor(-1684147);
            this.curveRadioButton[var3].setColor(-1684147, -1684147);
         } else if (var3 == 2) {
            var6 = LocaleController.getString("CurvesGreen", 2131559184);
            var7 = new StringBuilder();
            var7.append(var6.substring(0, 1).toUpperCase());
            var7.append(var6.substring(1).toLowerCase());
            var11.setText(var7.toString());
            var11.setTextColor(-10831009);
            this.curveRadioButton[var3].setColor(-10831009, -10831009);
         } else if (var3 == 3) {
            var6 = LocaleController.getString("CurvesBlue", 2131559183);
            var7 = new StringBuilder();
            var7.append(var6.substring(0, 1).toUpperCase());
            var7.append(var6.substring(1).toLowerCase());
            var11.setText(var7.toString());
            var11.setTextColor(-12734994);
            this.curveRadioButton[var3].setColor(-12734994, -12734994);
         }

         var13.addView(var11, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, 38.0F, 0.0F, 0.0F));
         float var8;
         if (var3 == 0) {
            var8 = 0.0F;
         } else {
            var8 = 30.0F;
         }

         var5.addView(var13, LayoutHelper.createLinear(-2, -2, var8, 0.0F, 0.0F, 0.0F));
         var13.setOnClickListener(new _$$Lambda$PhotoFilterView$L20A01o2AsZwohbyfeeuMPbGEQI(this));
      }

      this.blurLayout = new FrameLayout(var1);
      this.blurLayout.setVisibility(4);
      this.toolsView.addView(this.blurLayout, LayoutHelper.createFrame(280, 60.0F, 1, 0.0F, 40.0F, 0.0F, 0.0F));
      this.blurOffButton = new TextView(var1);
      this.blurOffButton.setCompoundDrawablePadding(AndroidUtilities.dp(2.0F));
      this.blurOffButton.setTextSize(1, 13.0F);
      this.blurOffButton.setGravity(1);
      this.blurOffButton.setText(LocaleController.getString("BlurOff", 2131558845));
      this.blurLayout.addView(this.blurOffButton, LayoutHelper.createFrame(80, 60.0F));
      this.blurOffButton.setOnClickListener(new _$$Lambda$PhotoFilterView$EHBGA4T_Wcgx03trAGzstG0gXqg(this));
      this.blurRadialButton = new TextView(var1);
      this.blurRadialButton.setCompoundDrawablePadding(AndroidUtilities.dp(2.0F));
      this.blurRadialButton.setTextSize(1, 13.0F);
      this.blurRadialButton.setGravity(1);
      this.blurRadialButton.setText(LocaleController.getString("BlurRadial", 2131558846));
      this.blurLayout.addView(this.blurRadialButton, LayoutHelper.createFrame(80, 80.0F, 51, 100.0F, 0.0F, 0.0F, 0.0F));
      this.blurRadialButton.setOnClickListener(new _$$Lambda$PhotoFilterView$SHP9jNXWESNEp5KZC_qRcZYRL1Y(this));
      this.blurLinearButton = new TextView(var1);
      this.blurLinearButton.setCompoundDrawablePadding(AndroidUtilities.dp(2.0F));
      this.blurLinearButton.setTextSize(1, 13.0F);
      this.blurLinearButton.setGravity(1);
      this.blurLinearButton.setText(LocaleController.getString("BlurLinear", 2131558844));
      this.blurLayout.addView(this.blurLinearButton, LayoutHelper.createFrame(80, 80.0F, 51, 200.0F, 0.0F, 0.0F, 0.0F));
      this.blurLinearButton.setOnClickListener(new _$$Lambda$PhotoFilterView$TdTpIDy0jwl_mIhu0bpchkPiKg8(this));
      this.updateSelectedBlurType();
      if (VERSION.SDK_INT >= 21) {
         ((LayoutParams)this.textureView.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
         ((LayoutParams)this.curvesControl.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
      }

   }

   private void fixLayout(int var1, int var2) {
      if (this.bitmapToEdit != null) {
         int var3 = var1 - AndroidUtilities.dp(28.0F);
         int var4 = AndroidUtilities.dp(214.0F);
         if (VERSION.SDK_INT >= 21) {
            var1 = AndroidUtilities.statusBarHeight;
         } else {
            var1 = 0;
         }

         var2 -= var4 + var1;
         var1 = this.orientation;
         float var5;
         if (var1 % 360 != 90 && var1 % 360 != 270) {
            var5 = (float)this.bitmapToEdit.getWidth();
            var1 = this.bitmapToEdit.getHeight();
         } else {
            var5 = (float)this.bitmapToEdit.getHeight();
            var1 = this.bitmapToEdit.getWidth();
         }

         float var6 = (float)var1;
         float var7 = (float)var3;
         float var8 = var7 / var5;
         float var9 = (float)var2;
         float var10 = var9 / var6;
         if (var8 > var10) {
            var5 = (float)((int)Math.ceil((double)(var5 * var10)));
            var6 = var9;
         } else {
            var6 = (float)((int)Math.ceil((double)(var6 * var8)));
            var5 = var7;
         }

         int var11 = (int)Math.ceil((double)((var7 - var5) / 2.0F + (float)AndroidUtilities.dp(14.0F)));
         var7 = (var9 - var6) / 2.0F;
         var9 = (float)AndroidUtilities.dp(14.0F);
         if (VERSION.SDK_INT >= 21) {
            var1 = AndroidUtilities.statusBarHeight;
         } else {
            var1 = 0;
         }

         var4 = (int)Math.ceil((double)(var7 + var9 + (float)var1));
         LayoutParams var12 = (LayoutParams)this.textureView.getLayoutParams();
         var12.leftMargin = var11;
         var12.topMargin = var4;
         var12.width = (int)var5;
         var12.height = (int)var6;
         PhotoFilterCurvesControl var13 = this.curvesControl;
         var5 = (float)var11;
         if (VERSION.SDK_INT >= 21) {
            var1 = AndroidUtilities.statusBarHeight;
         } else {
            var1 = 0;
         }

         var13.setActualArea(var5, (float)(var4 - var1), (float)var12.width, (float)var12.height);
         this.blurControl.setActualAreaSize((float)var12.width, (float)var12.height);
         ((LayoutParams)this.blurControl.getLayoutParams()).height = AndroidUtilities.dp(38.0F) + var2;
         ((LayoutParams)this.curvesControl.getLayoutParams()).height = var2 + AndroidUtilities.dp(28.0F);
         if (AndroidUtilities.isTablet()) {
            var1 = AndroidUtilities.dp(86.0F) * 10;
            var12 = (LayoutParams)this.recyclerListView.getLayoutParams();
            if (var1 < var3) {
               var12.width = var1;
               var12.leftMargin = (var3 - var1) / 2;
            } else {
               var12.width = -1;
               var12.leftMargin = 0;
            }
         }

      }
   }

   private float getContrastValue() {
      return this.contrastValue / 100.0F * 0.3F + 1.0F;
   }

   private float getEnhanceValue() {
      return this.enhanceValue / 100.0F;
   }

   private float getExposureValue() {
      return this.exposureValue / 100.0F;
   }

   private float getFadeValue() {
      return this.fadeValue / 100.0F;
   }

   private float getGrainValue() {
      return this.grainValue / 100.0F * 0.04F;
   }

   private float getHighlightsValue() {
      return (this.highlightsValue * 0.75F + 100.0F) / 100.0F;
   }

   private float getSaturationValue() {
      float var1 = this.saturationValue / 100.0F;
      float var2 = var1;
      if (var1 > 0.0F) {
         var2 = var1 * 1.05F;
      }

      return var2 + 1.0F;
   }

   private float getShadowsValue() {
      return (this.shadowsValue * 0.55F + 100.0F) / 100.0F;
   }

   private float getSharpenValue() {
      return this.sharpenValue / 100.0F * 0.6F + 0.11F;
   }

   private float getTintHighlightsIntensityValue() {
      float var1;
      if (this.tintHighlightsColor == 0) {
         var1 = 0.0F;
      } else {
         var1 = 0.5F;
      }

      return var1;
   }

   private float getTintShadowsIntensityValue() {
      float var1;
      if (this.tintShadowsColor == 0) {
         var1 = 0.0F;
      } else {
         var1 = 0.5F;
      }

      return var1;
   }

   private float getVignetteValue() {
      return this.vignetteValue / 100.0F;
   }

   private float getWarmthValue() {
      return this.warmthValue / 100.0F;
   }

   private void setShowOriginal(boolean var1) {
      if (this.showOriginal != var1) {
         this.showOriginal = var1;
         PhotoFilterView.EGLThread var2 = this.eglThread;
         if (var2 != null) {
            var2.requestRender(false);
         }

      }
   }

   private void updateSelectedBlurType() {
      int var1 = this.blurType;
      Drawable var2;
      if (var1 == 0) {
         var2 = this.blurOffButton.getContext().getResources().getDrawable(2131165308).mutate();
         var2.setColorFilter(new PorterDuffColorFilter(-11420173, Mode.MULTIPLY));
         this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, var2, (Drawable)null, (Drawable)null);
         this.blurOffButton.setTextColor(-11420173);
         this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165309, 0, 0);
         this.blurRadialButton.setTextColor(-1);
         this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165307, 0, 0);
         this.blurLinearButton.setTextColor(-1);
      } else if (var1 == 1) {
         this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165308, 0, 0);
         this.blurOffButton.setTextColor(-1);
         var2 = this.blurOffButton.getContext().getResources().getDrawable(2131165309).mutate();
         var2.setColorFilter(new PorterDuffColorFilter(-11420173, Mode.MULTIPLY));
         this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, var2, (Drawable)null, (Drawable)null);
         this.blurRadialButton.setTextColor(-11420173);
         this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165307, 0, 0);
         this.blurLinearButton.setTextColor(-1);
      } else if (var1 == 2) {
         this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165308, 0, 0);
         this.blurOffButton.setTextColor(-1);
         this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165309, 0, 0);
         this.blurRadialButton.setTextColor(-1);
         var2 = this.blurOffButton.getContext().getResources().getDrawable(2131165307).mutate();
         var2.setColorFilter(new PorterDuffColorFilter(-11420173, Mode.MULTIPLY));
         this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, var2, (Drawable)null, (Drawable)null);
         this.blurLinearButton.setTextColor(-11420173);
      }

   }

   public Bitmap getBitmap() {
      PhotoFilterView.EGLThread var1 = this.eglThread;
      Bitmap var2;
      if (var1 != null) {
         var2 = var1.getTexture();
      } else {
         var2 = null;
      }

      return var2;
   }

   public TextView getCancelTextView() {
      return this.cancelTextView;
   }

   public TextView getDoneTextView() {
      return this.doneTextView;
   }

   public MediaController.SavedFilterState getSavedFilterState() {
      MediaController.SavedFilterState var1 = new MediaController.SavedFilterState();
      var1.enhanceValue = this.enhanceValue;
      var1.exposureValue = this.exposureValue;
      var1.contrastValue = this.contrastValue;
      var1.warmthValue = this.warmthValue;
      var1.saturationValue = this.saturationValue;
      var1.fadeValue = this.fadeValue;
      var1.tintShadowsColor = this.tintShadowsColor;
      var1.tintHighlightsColor = this.tintHighlightsColor;
      var1.highlightsValue = this.highlightsValue;
      var1.shadowsValue = this.shadowsValue;
      var1.vignetteValue = this.vignetteValue;
      var1.grainValue = this.grainValue;
      var1.blurType = this.blurType;
      var1.sharpenValue = this.sharpenValue;
      var1.curvesToolValue = this.curvesToolValue;
      var1.blurExcludeSize = this.blurExcludeSize;
      var1.blurExcludePoint = this.blurExcludePoint;
      var1.blurExcludeBlurSize = this.blurExcludeBlurSize;
      var1.blurAngle = this.blurAngle;
      return var1;
   }

   public FrameLayout getToolsView() {
      return this.toolsView;
   }

   public boolean hasChanges() {
      MediaController.SavedFilterState var1 = this.lastState;
      boolean var2 = false;
      boolean var3 = false;
      if (var1 != null) {
         if (this.enhanceValue != var1.enhanceValue || this.contrastValue != var1.contrastValue || this.highlightsValue != var1.highlightsValue || this.exposureValue != var1.exposureValue || this.warmthValue != var1.warmthValue || this.saturationValue != var1.saturationValue || this.vignetteValue != var1.vignetteValue || this.shadowsValue != var1.shadowsValue || this.grainValue != var1.grainValue || this.sharpenValue != var1.sharpenValue || this.fadeValue != var1.fadeValue || this.tintHighlightsColor != var1.tintHighlightsColor || this.tintShadowsColor != var1.tintShadowsColor || !this.curvesToolValue.shouldBeSkipped()) {
            var3 = true;
         }

         return var3;
      } else {
         if (this.enhanceValue == 0.0F && this.contrastValue == 0.0F && this.highlightsValue == 0.0F && this.exposureValue == 0.0F && this.warmthValue == 0.0F && this.saturationValue == 0.0F && this.vignetteValue == 0.0F && this.shadowsValue == 0.0F && this.grainValue == 0.0F && this.sharpenValue == 0.0F && this.fadeValue == 0.0F && this.tintHighlightsColor == 0 && this.tintShadowsColor == 0) {
            var3 = var2;
            if (this.curvesToolValue.shouldBeSkipped()) {
               return var3;
            }
         }

         var3 = true;
         return var3;
      }
   }

   public void init() {
      this.textureView.setVisibility(0);
   }

   // $FF: synthetic method
   public void lambda$new$0$PhotoFilterView(Point var1, float var2, float var3, float var4) {
      this.blurExcludeSize = var3;
      this.blurExcludePoint = var1;
      this.blurExcludeBlurSize = var2;
      this.blurAngle = var4;
      PhotoFilterView.EGLThread var5 = this.eglThread;
      if (var5 != null) {
         var5.requestRender(false);
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$PhotoFilterView() {
      PhotoFilterView.EGLThread var1 = this.eglThread;
      if (var1 != null) {
         var1.requestRender(false);
      }

   }

   // $FF: synthetic method
   public void lambda$new$2$PhotoFilterView(View var1) {
      this.selectedTool = 0;
      this.tuneItem.setColorFilter(new PorterDuffColorFilter(-9649153, Mode.MULTIPLY));
      this.blurItem.setColorFilter((ColorFilter)null);
      this.curveItem.setColorFilter((ColorFilter)null);
      this.switchMode();
   }

   // $FF: synthetic method
   public void lambda$new$3$PhotoFilterView(View var1) {
      this.selectedTool = 1;
      this.tuneItem.setColorFilter((ColorFilter)null);
      this.blurItem.setColorFilter(new PorterDuffColorFilter(-9649153, Mode.MULTIPLY));
      this.curveItem.setColorFilter((ColorFilter)null);
      this.switchMode();
   }

   // $FF: synthetic method
   public void lambda$new$4$PhotoFilterView(View var1) {
      this.selectedTool = 2;
      this.tuneItem.setColorFilter((ColorFilter)null);
      this.blurItem.setColorFilter((ColorFilter)null);
      this.curveItem.setColorFilter(new PorterDuffColorFilter(-9649153, Mode.MULTIPLY));
      this.switchMode();
   }

   // $FF: synthetic method
   public void lambda$new$5$PhotoFilterView(View var1) {
      int var2 = (Integer)var1.getTag();
      this.curvesToolValue.activeType = var2;

      for(int var3 = 0; var3 < 4; ++var3) {
         RadioButton var5 = this.curveRadioButton[var3];
         boolean var4;
         if (var3 == var2) {
            var4 = true;
         } else {
            var4 = false;
         }

         var5.setChecked(var4, true);
      }

      this.curvesControl.invalidate();
   }

   // $FF: synthetic method
   public void lambda$new$6$PhotoFilterView(View var1) {
      this.blurType = 0;
      this.updateSelectedBlurType();
      this.blurControl.setVisibility(4);
      PhotoFilterView.EGLThread var2 = this.eglThread;
      if (var2 != null) {
         var2.requestRender(false);
      }

   }

   // $FF: synthetic method
   public void lambda$new$7$PhotoFilterView(View var1) {
      this.blurType = 1;
      this.updateSelectedBlurType();
      this.blurControl.setVisibility(0);
      this.blurControl.setType(1);
      PhotoFilterView.EGLThread var2 = this.eglThread;
      if (var2 != null) {
         var2.requestRender(false);
      }

   }

   // $FF: synthetic method
   public void lambda$new$8$PhotoFilterView(View var1) {
      this.blurType = 2;
      this.updateSelectedBlurType();
      this.blurControl.setVisibility(0);
      this.blurControl.setType(0);
      PhotoFilterView.EGLThread var2 = this.eglThread;
      if (var2 != null) {
         var2.requestRender(false);
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.fixLayout(MeasureSpec.getSize(var1), MeasureSpec.getSize(var2));
      super.onMeasure(var1, var2);
   }

   public void onTouch(MotionEvent var1) {
      if (var1.getActionMasked() != 0 && var1.getActionMasked() != 5) {
         if (var1.getActionMasked() == 1 || var1.getActionMasked() == 6) {
            this.setShowOriginal(false);
         }
      } else {
         LayoutParams var2 = (LayoutParams)this.textureView.getLayoutParams();
         if (var2 != null && var1.getX() >= (float)var2.leftMargin && var1.getY() >= (float)var2.topMargin && var1.getX() <= (float)(var2.leftMargin + var2.width) && var1.getY() <= (float)(var2.topMargin + var2.height)) {
            this.setShowOriginal(true);
         }
      }

   }

   public void shutdown() {
      PhotoFilterView.EGLThread var1 = this.eglThread;
      if (var1 != null) {
         var1.shutdown();
         this.eglThread = null;
      }

      this.textureView.setVisibility(8);
   }

   public void switchMode() {
      int var1 = this.selectedTool;
      if (var1 == 0) {
         this.blurControl.setVisibility(4);
         this.blurLayout.setVisibility(4);
         this.curveLayout.setVisibility(4);
         this.curvesControl.setVisibility(4);
         this.recyclerListView.setVisibility(0);
      } else if (var1 == 1) {
         this.recyclerListView.setVisibility(4);
         this.curveLayout.setVisibility(4);
         this.curvesControl.setVisibility(4);
         this.blurLayout.setVisibility(0);
         if (this.blurType != 0) {
            this.blurControl.setVisibility(0);
         }

         this.updateSelectedBlurType();
      } else if (var1 == 2) {
         this.recyclerListView.setVisibility(4);
         this.blurLayout.setVisibility(4);
         this.blurControl.setVisibility(4);
         this.curveLayout.setVisibility(0);
         this.curvesControl.setVisibility(0);
         this.curvesToolValue.activeType = 0;

         for(var1 = 0; var1 < 4; ++var1) {
            RadioButton var2 = this.curveRadioButton[var1];
            boolean var3;
            if (var1 == 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            var2.setChecked(var3, false);
         }
      }

   }

   public static class CurvesToolValue {
      public static final int CurvesTypeBlue = 3;
      public static final int CurvesTypeGreen = 2;
      public static final int CurvesTypeLuminance = 0;
      public static final int CurvesTypeRed = 1;
      public int activeType;
      public PhotoFilterView.CurvesValue blueCurve = new PhotoFilterView.CurvesValue();
      public ByteBuffer curveBuffer = ByteBuffer.allocateDirect(800);
      public PhotoFilterView.CurvesValue greenCurve = new PhotoFilterView.CurvesValue();
      public PhotoFilterView.CurvesValue luminanceCurve = new PhotoFilterView.CurvesValue();
      public PhotoFilterView.CurvesValue redCurve = new PhotoFilterView.CurvesValue();

      public CurvesToolValue() {
         this.curveBuffer.order(ByteOrder.LITTLE_ENDIAN);
      }

      public void fillBuffer() {
         this.curveBuffer.position(0);
         float[] var1 = this.luminanceCurve.getDataPoints();
         float[] var2 = this.redCurve.getDataPoints();
         float[] var3 = this.greenCurve.getDataPoints();
         float[] var4 = this.blueCurve.getDataPoints();

         for(int var5 = 0; var5 < 200; ++var5) {
            this.curveBuffer.put((byte)((int)(var2[var5] * 255.0F)));
            this.curveBuffer.put((byte)((int)(var3[var5] * 255.0F)));
            this.curveBuffer.put((byte)((int)(var4[var5] * 255.0F)));
            this.curveBuffer.put((byte)((int)(var1[var5] * 255.0F)));
         }

         this.curveBuffer.position(0);
      }

      public boolean shouldBeSkipped() {
         boolean var1;
         if (this.luminanceCurve.isDefault() && this.redCurve.isDefault() && this.greenCurve.isDefault() && this.blueCurve.isDefault()) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static class CurvesValue {
      public float blacksLevel = 0.0F;
      public float[] cachedDataPoints;
      public float highlightsLevel = 75.0F;
      public float midtonesLevel = 50.0F;
      public float previousBlacksLevel = 0.0F;
      public float previousHighlightsLevel = 75.0F;
      public float previousMidtonesLevel = 50.0F;
      public float previousShadowsLevel = 25.0F;
      public float previousWhitesLevel = 100.0F;
      public float shadowsLevel = 25.0F;
      public float whitesLevel = 100.0F;

      public float[] getDataPoints() {
         if (this.cachedDataPoints == null) {
            this.interpolateCurve();
         }

         return this.cachedDataPoints;
      }

      public float[] interpolateCurve() {
         float[] var1 = new float[14];
         var1[0] = -0.001F;
         float var2 = this.blacksLevel;
         var1[1] = var2 / 100.0F;
         var1[2] = 0.0F;
         var1[3] = var2 / 100.0F;
         var1[4] = 0.25F;
         var1[5] = this.shadowsLevel / 100.0F;
         var1[6] = 0.5F;
         var1[7] = this.midtonesLevel / 100.0F;
         var1[8] = 0.75F;
         var1[9] = this.highlightsLevel / 100.0F;
         var1[10] = 1.0F;
         var2 = this.whitesLevel;
         var1[11] = var2 / 100.0F;
         var1[12] = 1.001F;
         var1[13] = var2 / 100.0F;
         ArrayList var3 = new ArrayList(100);
         ArrayList var4 = new ArrayList(100);
         var4.add(var1[0]);
         var4.add(var1[1]);

         int var5;
         int var6;
         for(var5 = 1; var5 < var1.length / 2 - 2; var5 = var6) {
            var6 = (var5 - 1) * 2;
            float var7 = var1[var6];
            float var8 = var1[var6 + 1];
            var6 = var5 * 2;
            float var9 = var1[var6];
            var2 = var1[var6 + 1];
            var6 = var5 + 1;
            int var10 = var6 * 2;
            float var11 = var1[var10];
            float var12 = var1[var10 + 1];
            var5 = (var5 + 2) * 2;
            float var13 = var1[var5];
            float var14 = var1[var5 + 1];

            for(var5 = 1; var5 < 100; ++var5) {
               float var15 = (float)var5 * 0.01F;
               float var16 = var15 * var15;
               float var17 = var16 * var15;
               float var18 = (var9 * 2.0F + (var11 - var7) * var15 + (var7 * 2.0F - var9 * 5.0F + var11 * 4.0F - var13) * var16 + (var9 * 3.0F - var7 - var11 * 3.0F + var13) * var17) * 0.5F;
               var15 = Math.max(0.0F, Math.min(1.0F, (var2 * 2.0F + (var12 - var8) * var15 + (2.0F * var8 - 5.0F * var2 + 4.0F * var12 - var14) * var16 + (var2 * 3.0F - var8 - 3.0F * var12 + var14) * var17) * 0.5F));
               if (var18 > var7) {
                  var4.add(var18);
                  var4.add(var15);
               }

               if ((var5 - 1) % 2 == 0) {
                  var3.add(var15);
               }
            }

            var4.add(var11);
            var4.add(var12);
         }

         var4.add(var1[12]);
         var4.add(var1[13]);
         this.cachedDataPoints = new float[var3.size()];
         var5 = 0;

         while(true) {
            var1 = this.cachedDataPoints;
            if (var5 >= var1.length) {
               float[] var19 = new float[var4.size()];

               for(var5 = 0; var5 < var19.length; ++var5) {
                  var19[var5] = (Float)var4.get(var5);
               }

               return var19;
            }

            var1[var5] = (Float)var3.get(var5);
            ++var5;
         }
      }

      public boolean isDefault() {
         boolean var1;
         if ((double)Math.abs(this.blacksLevel - 0.0F) < 1.0E-5D && (double)Math.abs(this.shadowsLevel - 25.0F) < 1.0E-5D && (double)Math.abs(this.midtonesLevel - 50.0F) < 1.0E-5D && (double)Math.abs(this.highlightsLevel - 75.0F) < 1.0E-5D && (double)Math.abs(this.whitesLevel - 100.0F) < 1.0E-5D) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void restoreValues() {
         this.blacksLevel = this.previousBlacksLevel;
         this.shadowsLevel = this.previousShadowsLevel;
         this.midtonesLevel = this.previousMidtonesLevel;
         this.highlightsLevel = this.previousHighlightsLevel;
         this.whitesLevel = this.previousWhitesLevel;
         this.interpolateCurve();
      }

      public void saveValues() {
         this.previousBlacksLevel = this.blacksLevel;
         this.previousShadowsLevel = this.shadowsLevel;
         this.previousMidtonesLevel = this.midtonesLevel;
         this.previousHighlightsLevel = this.highlightsLevel;
         this.previousWhitesLevel = this.whitesLevel;
      }
   }

   public class EGLThread extends DispatchQueue {
      private static final int PGPhotoEnhanceHistogramBins = 256;
      private static final int PGPhotoEnhanceSegments = 4;
      private static final String blurFragmentShaderCode = "uniform sampler2D sourceImage;varying highp vec2 blurCoordinates[9];void main() {lowp vec4 sum = vec4(0.0);sum += texture2D(sourceImage, blurCoordinates[0]) * 0.133571;sum += texture2D(sourceImage, blurCoordinates[1]) * 0.233308;sum += texture2D(sourceImage, blurCoordinates[2]) * 0.233308;sum += texture2D(sourceImage, blurCoordinates[3]) * 0.135928;sum += texture2D(sourceImage, blurCoordinates[4]) * 0.135928;sum += texture2D(sourceImage, blurCoordinates[5]) * 0.051383;sum += texture2D(sourceImage, blurCoordinates[6]) * 0.051383;sum += texture2D(sourceImage, blurCoordinates[7]) * 0.012595;sum += texture2D(sourceImage, blurCoordinates[8]) * 0.012595;gl_FragColor = sum;}";
      private static final String blurVertexShaderCode = "attribute vec4 position;attribute vec4 inputTexCoord;uniform highp float texelWidthOffset;uniform highp float texelHeightOffset;varying vec2 blurCoordinates[9];void main() {gl_Position = position;vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);blurCoordinates[0] = inputTexCoord.xy;blurCoordinates[1] = inputTexCoord.xy + singleStepOffset * 1.458430;blurCoordinates[2] = inputTexCoord.xy - singleStepOffset * 1.458430;blurCoordinates[3] = inputTexCoord.xy + singleStepOffset * 3.403985;blurCoordinates[4] = inputTexCoord.xy - singleStepOffset * 3.403985;blurCoordinates[5] = inputTexCoord.xy + singleStepOffset * 5.351806;blurCoordinates[6] = inputTexCoord.xy - singleStepOffset * 5.351806;blurCoordinates[7] = inputTexCoord.xy + singleStepOffset * 7.302940;blurCoordinates[8] = inputTexCoord.xy - singleStepOffset * 7.302940;}";
      private static final String enhanceFragmentShaderCode = "precision highp float;varying vec2 texCoord;uniform sampler2D sourceImage;uniform sampler2D inputImageTexture2;uniform float intensity;float enhance(float value) {const vec2 offset = vec2(0.001953125, 0.03125);value = value + offset.x;vec2 coord = (clamp(texCoord, 0.125, 1.0 - 0.125001) - 0.125) * 4.0;vec2 frac = fract(coord);coord = floor(coord);float p00 = float(coord.y * 4.0 + coord.x) * 0.0625 + offset.y;float p01 = float(coord.y * 4.0 + coord.x + 1.0) * 0.0625 + offset.y;float p10 = float((coord.y + 1.0) * 4.0 + coord.x) * 0.0625 + offset.y;float p11 = float((coord.y + 1.0) * 4.0 + coord.x + 1.0) * 0.0625 + offset.y;vec3 c00 = texture2D(inputImageTexture2, vec2(value, p00)).rgb;vec3 c01 = texture2D(inputImageTexture2, vec2(value, p01)).rgb;vec3 c10 = texture2D(inputImageTexture2, vec2(value, p10)).rgb;vec3 c11 = texture2D(inputImageTexture2, vec2(value, p11)).rgb;float c1 = ((c00.r - c00.g) / (c00.b - c00.g));float c2 = ((c01.r - c01.g) / (c01.b - c01.g));float c3 = ((c10.r - c10.g) / (c10.b - c10.g));float c4 = ((c11.r - c11.g) / (c11.b - c11.g));float c1_2 = mix(c1, c2, frac.x);float c3_4 = mix(c3, c4, frac.x);return mix(c1_2, c3_4, frac.y);}vec3 hsv_to_rgb(vec3 c) {vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);}void main() {vec4 texel = texture2D(sourceImage, texCoord);vec4 hsv = texel;hsv.y = min(1.0, hsv.y * 1.2);hsv.z = min(1.0, enhance(hsv.z) * 1.1);gl_FragColor = vec4(hsv_to_rgb(mix(texel.xyz, hsv.xyz, intensity)), texel.w);}";
      private static final String linearBlurFragmentShaderCode = "varying highp vec2 texCoord;uniform sampler2D sourceImage;uniform sampler2D inputImageTexture2;uniform lowp float excludeSize;uniform lowp vec2 excludePoint;uniform lowp float excludeBlurSize;uniform highp float angle;uniform highp float aspectRatio;void main() {lowp vec4 sharpImageColor = texture2D(sourceImage, texCoord);lowp vec4 blurredImageColor = texture2D(inputImageTexture2, texCoord);highp vec2 texCoordToUse = vec2(texCoord.x, (texCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));highp float distanceFromCenter = abs((texCoordToUse.x - excludePoint.x) * aspectRatio * cos(angle) + (texCoordToUse.y - excludePoint.y) * sin(angle));gl_FragColor = mix(sharpImageColor, blurredImageColor, smoothstep(excludeSize - excludeBlurSize, excludeSize, distanceFromCenter));}";
      private static final String radialBlurFragmentShaderCode = "varying highp vec2 texCoord;uniform sampler2D sourceImage;uniform sampler2D inputImageTexture2;uniform lowp float excludeSize;uniform lowp vec2 excludePoint;uniform lowp float excludeBlurSize;uniform highp float aspectRatio;void main() {lowp vec4 sharpImageColor = texture2D(sourceImage, texCoord);lowp vec4 blurredImageColor = texture2D(inputImageTexture2, texCoord);highp vec2 texCoordToUse = vec2(texCoord.x, (texCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));highp float distanceFromCenter = distance(excludePoint, texCoordToUse);gl_FragColor = mix(sharpImageColor, blurredImageColor, smoothstep(excludeSize - excludeBlurSize, excludeSize, distanceFromCenter));}";
      private static final String rgbToHsvFragmentShaderCode = "precision highp float;varying vec2 texCoord;uniform sampler2D sourceImage;vec3 rgb_to_hsv(vec3 c) {vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);vec4 p = c.g < c.b ? vec4(c.bg, K.wz) : vec4(c.gb, K.xy);vec4 q = c.r < p.x ? vec4(p.xyw, c.r) : vec4(c.r, p.yzx);float d = q.x - min(q.w, q.y);float e = 1.0e-10;return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);}void main() {vec4 texel = texture2D(sourceImage, texCoord);gl_FragColor = vec4(rgb_to_hsv(texel.rgb), texel.a);}";
      private static final String sharpenFragmentShaderCode = "precision highp float;varying vec2 texCoord;varying vec2 leftTexCoord;varying vec2 rightTexCoord;varying vec2 topTexCoord;varying vec2 bottomTexCoord;uniform sampler2D sourceImage;uniform float sharpen;void main() {vec4 result = texture2D(sourceImage, texCoord);vec3 leftTextureColor = texture2D(sourceImage, leftTexCoord).rgb;vec3 rightTextureColor = texture2D(sourceImage, rightTexCoord).rgb;vec3 topTextureColor = texture2D(sourceImage, topTexCoord).rgb;vec3 bottomTextureColor = texture2D(sourceImage, bottomTexCoord).rgb;result.rgb = result.rgb * (1.0 + 4.0 * sharpen) - (leftTextureColor + rightTextureColor + topTextureColor + bottomTextureColor) * sharpen;gl_FragColor = result;}";
      private static final String sharpenVertexShaderCode = "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;uniform highp float inputWidth;uniform highp float inputHeight;varying vec2 leftTexCoord;varying vec2 rightTexCoord;varying vec2 topTexCoord;varying vec2 bottomTexCoord;void main() {gl_Position = position;texCoord = inputTexCoord;highp vec2 widthStep = vec2(1.0 / inputWidth, 0.0);highp vec2 heightStep = vec2(0.0, 1.0 / inputHeight);leftTexCoord = inputTexCoord - widthStep;rightTexCoord = inputTexCoord + widthStep;topTexCoord = inputTexCoord + heightStep;bottomTexCoord = inputTexCoord - heightStep;}";
      private static final String simpleFragmentShaderCode = "varying highp vec2 texCoord;uniform sampler2D sourceImage;void main() {gl_FragColor = texture2D(sourceImage, texCoord);}";
      private static final String simpleVertexShaderCode = "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}";
      private static final String toolsFragmentShaderCode = "varying highp vec2 texCoord;uniform sampler2D sourceImage;uniform highp float width;uniform highp float height;uniform sampler2D curvesImage;uniform lowp float skipTone;uniform lowp float shadows;const mediump vec3 hsLuminanceWeighting = vec3(0.3, 0.3, 0.3);uniform lowp float highlights;uniform lowp float contrast;uniform lowp float fadeAmount;const mediump vec3 satLuminanceWeighting = vec3(0.2126, 0.7152, 0.0722);uniform lowp float saturation;uniform lowp float shadowsTintIntensity;uniform lowp float highlightsTintIntensity;uniform lowp vec3 shadowsTintColor;uniform lowp vec3 highlightsTintColor;uniform lowp float exposure;uniform lowp float warmth;uniform lowp float grain;const lowp float permTexUnit = 1.0 / 256.0;const lowp float permTexUnitHalf = 0.5 / 256.0;const lowp float grainsize = 2.3;uniform lowp float vignette;highp float getLuma(highp vec3 rgbP) {return (0.299 * rgbP.r) + (0.587 * rgbP.g) + (0.114 * rgbP.b);}lowp vec3 rgbToHsv(lowp vec3 c) {highp vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);highp vec4 p = c.g < c.b ? vec4(c.bg, K.wz) : vec4(c.gb, K.xy);highp vec4 q = c.r < p.x ? vec4(p.xyw, c.r) : vec4(c.r, p.yzx);highp float d = q.x - min(q.w, q.y);highp float e = 1.0e-10;return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);}lowp vec3 hsvToRgb(lowp vec3 c) {highp vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);highp vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);}highp vec3 rgbToHsl(highp vec3 color) {highp vec3 hsl;highp float fmin = min(min(color.r, color.g), color.b);highp float fmax = max(max(color.r, color.g), color.b);highp float delta = fmax - fmin;hsl.z = (fmax + fmin) / 2.0;if (delta == 0.0) {hsl.x = 0.0;hsl.y = 0.0;} else {if (hsl.z < 0.5) {hsl.y = delta / (fmax + fmin);} else {hsl.y = delta / (2.0 - fmax - fmin);}highp float deltaR = (((fmax - color.r) / 6.0) + (delta / 2.0)) / delta;highp float deltaG = (((fmax - color.g) / 6.0) + (delta / 2.0)) / delta;highp float deltaB = (((fmax - color.b) / 6.0) + (delta / 2.0)) / delta;if (color.r == fmax) {hsl.x = deltaB - deltaG;} else if (color.g == fmax) {hsl.x = (1.0 / 3.0) + deltaR - deltaB;} else if (color.b == fmax) {hsl.x = (2.0 / 3.0) + deltaG - deltaR;}if (hsl.x < 0.0) {hsl.x += 1.0;} else if (hsl.x > 1.0) {hsl.x -= 1.0;}}return hsl;}highp float hueToRgb(highp float f1, highp float f2, highp float hue) {if (hue < 0.0) {hue += 1.0;} else if (hue > 1.0) {hue -= 1.0;}highp float res;if ((6.0 * hue) < 1.0) {res = f1 + (f2 - f1) * 6.0 * hue;} else if ((2.0 * hue) < 1.0) {res = f2;} else if ((3.0 * hue) < 2.0) {res = f1 + (f2 - f1) * ((2.0 / 3.0) - hue) * 6.0;} else {res = f1;} return res;}highp vec3 hslToRgb(highp vec3 hsl) {if (hsl.y == 0.0) {return vec3(hsl.z);} else {highp float f2;if (hsl.z < 0.5) {f2 = hsl.z * (1.0 + hsl.y);} else {f2 = (hsl.z + hsl.y) - (hsl.y * hsl.z);}highp float f1 = 2.0 * hsl.z - f2;return vec3(hueToRgb(f1, f2, hsl.x + (1.0/3.0)), hueToRgb(f1, f2, hsl.x), hueToRgb(f1, f2, hsl.x - (1.0/3.0)));}}highp vec3 rgbToYuv(highp vec3 inP) {highp float luma = getLuma(inP);return vec3(luma, (1.0 / 1.772) * (inP.b - luma), (1.0 / 1.402) * (inP.r - luma));}lowp vec3 yuvToRgb(highp vec3 inP) {return vec3(1.402 * inP.b + inP.r, (inP.r - (0.299 * 1.402 / 0.587) * inP.b - (0.114 * 1.772 / 0.587) * inP.g), 1.772 * inP.g + inP.r);}lowp float easeInOutSigmoid(lowp float value, lowp float strength) {if (value > 0.5) {return 1.0 - pow(2.0 - 2.0 * value, 1.0 / (1.0 - strength)) * 0.5;} else {return pow(2.0 * value, 1.0 / (1.0 - strength)) * 0.5;}}lowp vec3 applyLuminanceCurve(lowp vec3 pixel) {highp float index = floor(clamp(pixel.z / (1.0 / 200.0), 0.0, 199.0));pixel.y = mix(0.0, pixel.y, smoothstep(0.0, 0.1, pixel.z) * (1.0 - smoothstep(0.8, 1.0, pixel.z)));pixel.z = texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).a;return pixel;}lowp vec3 applyRGBCurve(lowp vec3 pixel) {highp float index = floor(clamp(pixel.r / (1.0 / 200.0), 0.0, 199.0));pixel.r = texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).r;index = floor(clamp(pixel.g / (1.0 / 200.0), 0.0, 199.0));pixel.g = clamp(texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).g, 0.0, 1.0);index = floor(clamp(pixel.b / (1.0 / 200.0), 0.0, 199.0));pixel.b = clamp(texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).b, 0.0, 1.0);return pixel;}highp vec3 fadeAdjust(highp vec3 color, highp float fadeVal) {return (color * (1.0 - fadeVal)) + ((color + (vec3(-0.9772) * pow(vec3(color), vec3(3.0)) + vec3(1.708) * pow(vec3(color), vec3(2.0)) + vec3(-0.1603) * vec3(color) + vec3(0.2878) - color * vec3(0.9))) * fadeVal);}lowp vec3 tintRaiseShadowsCurve(lowp vec3 color) {return vec3(-0.003671) * pow(color, vec3(3.0)) + vec3(0.3842) * pow(color, vec3(2.0)) + vec3(0.3764) * color + vec3(0.2515);}lowp vec3 tintShadows(lowp vec3 texel, lowp vec3 tintColor, lowp float tintAmount) {return clamp(mix(texel, mix(texel, tintRaiseShadowsCurve(texel), tintColor), tintAmount), 0.0, 1.0);} lowp vec3 tintHighlights(lowp vec3 texel, lowp vec3 tintColor, lowp float tintAmount) {return clamp(mix(texel, mix(texel, vec3(1.0) - tintRaiseShadowsCurve(vec3(1.0) - texel), (vec3(1.0) - tintColor)), tintAmount), 0.0, 1.0);}highp vec4 rnm(in highp vec2 tc) {highp float noise = sin(dot(tc, vec2(12.9898, 78.233))) * 43758.5453;return vec4(fract(noise), fract(noise * 1.2154), fract(noise * 1.3453), fract(noise * 1.3647)) * 2.0 - 1.0;}highp float fade(in highp float t) {return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);}highp float pnoise3D(in highp vec3 p) {highp vec3 pi = permTexUnit * floor(p) + permTexUnitHalf;highp vec3 pf = fract(p);highp float perm = rnm(pi.xy).a;highp float n000 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf);highp float n001 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(0.0, 0.0, 1.0));perm = rnm(pi.xy + vec2(0.0, permTexUnit)).a;highp float n010 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf - vec3(0.0, 1.0, 0.0));highp float n011 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(0.0, 1.0, 1.0));perm = rnm(pi.xy + vec2(permTexUnit, 0.0)).a;highp float n100 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf - vec3(1.0, 0.0, 0.0));highp float n101 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(1.0, 0.0, 1.0));perm = rnm(pi.xy + vec2(permTexUnit, permTexUnit)).a;highp float n110 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf - vec3(1.0, 1.0, 0.0));highp float n111 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(1.0, 1.0, 1.0));highp vec4 n_x = mix(vec4(n000, n001, n010, n011), vec4(n100, n101, n110, n111), fade(pf.x));highp vec2 n_xy = mix(n_x.xy, n_x.zw, fade(pf.y));return mix(n_xy.x, n_xy.y, fade(pf.z));}lowp vec2 coordRot(in lowp vec2 tc, in lowp float angle) {return vec2(((tc.x * 2.0 - 1.0) * cos(angle) - (tc.y * 2.0 - 1.0) * sin(angle)) * 0.5 + 0.5, ((tc.y * 2.0 - 1.0) * cos(angle) + (tc.x * 2.0 - 1.0) * sin(angle)) * 0.5 + 0.5);}void main() {lowp vec4 source = texture2D(sourceImage, texCoord);lowp vec4 result = source;const lowp float toolEpsilon = 0.005;if (skipTone < toolEpsilon) {result = vec4(applyRGBCurve(hslToRgb(applyLuminanceCurve(rgbToHsl(result.rgb)))), result.a);}mediump float hsLuminance = dot(result.rgb, hsLuminanceWeighting);mediump float shadow = clamp((pow(hsLuminance, 1.0 / shadows) + (-0.76) * pow(hsLuminance, 2.0 / shadows)) - hsLuminance, 0.0, 1.0);mediump float highlight = clamp((1.0 - (pow(1.0 - hsLuminance, 1.0 / (2.0 - highlights)) + (-0.8) * pow(1.0 - hsLuminance, 2.0 / (2.0 - highlights)))) - hsLuminance, -1.0, 0.0);lowp vec3 hsresult = vec3(0.0, 0.0, 0.0) + ((hsLuminance + shadow + highlight) - 0.0) * ((result.rgb - vec3(0.0, 0.0, 0.0)) / (hsLuminance - 0.0));mediump float contrastedLuminance = ((hsLuminance - 0.5) * 1.5) + 0.5;mediump float whiteInterp = contrastedLuminance * contrastedLuminance * contrastedLuminance;mediump float whiteTarget = clamp(highlights, 1.0, 2.0) - 1.0;hsresult = mix(hsresult, vec3(1.0), whiteInterp * whiteTarget);mediump float invContrastedLuminance = 1.0 - contrastedLuminance;mediump float blackInterp = invContrastedLuminance * invContrastedLuminance * invContrastedLuminance;mediump float blackTarget = 1.0 - clamp(shadows, 0.0, 1.0);hsresult = mix(hsresult, vec3(0.0), blackInterp * blackTarget);result = vec4(hsresult.rgb, result.a);result = vec4(clamp(((result.rgb - vec3(0.5)) * contrast + vec3(0.5)), 0.0, 1.0), result.a);if (abs(fadeAmount) > toolEpsilon) {result.rgb = fadeAdjust(result.rgb, fadeAmount);}lowp float satLuminance = dot(result.rgb, satLuminanceWeighting);lowp vec3 greyScaleColor = vec3(satLuminance);result = vec4(clamp(mix(greyScaleColor, result.rgb, saturation), 0.0, 1.0), result.a);if (abs(shadowsTintIntensity) > toolEpsilon) {result.rgb = tintShadows(result.rgb, shadowsTintColor, shadowsTintIntensity * 2.0);}if (abs(highlightsTintIntensity) > toolEpsilon) {result.rgb = tintHighlights(result.rgb, highlightsTintColor, highlightsTintIntensity * 2.0);}if (abs(exposure) > toolEpsilon) {mediump float mag = exposure * 1.045;mediump float exppower = 1.0 + abs(mag);if (mag < 0.0) {exppower = 1.0 / exppower;}result.r = 1.0 - pow((1.0 - result.r), exppower);result.g = 1.0 - pow((1.0 - result.g), exppower);result.b = 1.0 - pow((1.0 - result.b), exppower);}if (abs(warmth) > toolEpsilon) {highp vec3 yuvVec;if (warmth > 0.0 ) {yuvVec = vec3(0.1765, -0.1255, 0.0902);} else {yuvVec = -vec3(0.0588, 0.1569, -0.1255);}highp vec3 yuvColor = rgbToYuv(result.rgb);highp float luma = yuvColor.r;highp float curveScale = sin(luma * 3.14159);yuvColor += 0.375 * warmth * curveScale * yuvVec;result.rgb = yuvToRgb(yuvColor);}if (abs(grain) > toolEpsilon) {highp vec3 rotOffset = vec3(1.425, 3.892, 5.835);highp vec2 rotCoordsR = coordRot(texCoord, rotOffset.x);highp vec3 noise = vec3(pnoise3D(vec3(rotCoordsR * vec2(width / grainsize, height / grainsize),0.0)));lowp vec3 lumcoeff = vec3(0.299,0.587,0.114);lowp float luminance = dot(result.rgb, lumcoeff);lowp float lum = smoothstep(0.2, 0.0, luminance);lum += luminance;noise = mix(noise,vec3(0.0),pow(lum,4.0));result.rgb = result.rgb + noise * grain;}if (abs(vignette) > toolEpsilon) {const lowp float midpoint = 0.7;const lowp float fuzziness = 0.62;lowp float radDist = length(texCoord - 0.5) / sqrt(0.5);lowp float mag = easeInOutSigmoid(radDist * midpoint, fuzziness) * vignette * 0.645;result.rgb = mix(pow(result.rgb, vec3(1.0 / (1.0 - mag))), vec3(0.0), mag * mag);}gl_FragColor = result;}";
      private final int EGL_CONTEXT_CLIENT_VERSION = 12440;
      private final int EGL_OPENGL_ES2_BIT = 4;
      private int blurHeightHandle;
      private int blurInputTexCoordHandle;
      private int blurPositionHandle;
      private int blurShaderProgram;
      private int blurSourceImageHandle;
      private int blurWidthHandle;
      private boolean blured;
      private int contrastHandle;
      private Bitmap currentBitmap;
      private int[] curveTextures = new int[1];
      private int curvesImageHandle;
      private Runnable drawRunnable = new Runnable() {
         public void run() {
            if (EGLThread.this.initied) {
               if ((!EGLThread.this.eglContext.equals(EGLThread.this.egl10.eglGetCurrentContext()) || !EGLThread.this.eglSurface.equals(EGLThread.this.egl10.eglGetCurrentSurface(12377))) && !EGLThread.this.egl10.eglMakeCurrent(EGLThread.this.eglDisplay, EGLThread.this.eglSurface, EGLThread.this.eglSurface, EGLThread.this.eglContext)) {
                  if (BuildVars.LOGS_ENABLED) {
                     StringBuilder var2 = new StringBuilder();
                     var2.append("eglMakeCurrent failed ");
                     var2.append(GLUtils.getEGLErrorString(EGLThread.this.egl10.eglGetError()));
                     FileLog.e(var2.toString());
                  }

               } else {
                  GLES20.glViewport(0, 0, EGLThread.this.renderBufferWidth, EGLThread.this.renderBufferHeight);
                  EGLThread.this.drawEnhancePass();
                  EGLThread.this.drawSharpenPass();
                  EGLThread.this.drawCustomParamsPass();
                  PhotoFilterView.EGLThread var1 = EGLThread.this;
                  var1.blured = var1.drawBlurPass();
                  GLES20.glViewport(0, 0, EGLThread.this.surfaceWidth, EGLThread.this.surfaceHeight);
                  GLES20.glBindFramebuffer(36160, 0);
                  GLES20.glClear(0);
                  GLES20.glUseProgram(EGLThread.this.simpleShaderProgram);
                  GLES20.glActiveTexture(33984);
                  GLES20.glBindTexture(3553, EGLThread.this.renderTexture[EGLThread.this.blured ^ 1]);
                  GLES20.glUniform1i(EGLThread.this.simpleSourceImageHandle, 0);
                  GLES20.glEnableVertexAttribArray(EGLThread.this.simpleInputTexCoordHandle);
                  GLES20.glVertexAttribPointer(EGLThread.this.simpleInputTexCoordHandle, 2, 5126, false, 8, EGLThread.this.textureBuffer);
                  GLES20.glEnableVertexAttribArray(EGLThread.this.simplePositionHandle);
                  GLES20.glVertexAttribPointer(EGLThread.this.simplePositionHandle, 2, 5126, false, 8, EGLThread.this.vertexBuffer);
                  GLES20.glDrawArrays(5, 0, 4);
                  EGLThread.this.egl10.eglSwapBuffers(EGLThread.this.eglDisplay, EGLThread.this.eglSurface);
               }
            }
         }
      };
      private EGL10 egl10;
      private EGLConfig eglConfig;
      private EGLContext eglContext;
      private EGLDisplay eglDisplay;
      private EGLSurface eglSurface;
      private int enhanceInputImageTexture2Handle;
      private int enhanceInputTexCoordHandle;
      private int enhanceIntensityHandle;
      private int enhancePositionHandle;
      private int enhanceShaderProgram;
      private int enhanceSourceImageHandle;
      private int[] enhanceTextures = new int[2];
      private int exposureHandle;
      private int fadeAmountHandle;
      private GL gl;
      private int grainHandle;
      private int heightHandle;
      private int highlightsHandle;
      private int highlightsTintColorHandle;
      private int highlightsTintIntensityHandle;
      private boolean hsvGenerated;
      private boolean initied;
      private int inputTexCoordHandle;
      private long lastRenderCallTime;
      private int linearBlurAngleHandle;
      private int linearBlurAspectRatioHandle;
      private int linearBlurExcludeBlurSizeHandle;
      private int linearBlurExcludePointHandle;
      private int linearBlurExcludeSizeHandle;
      private int linearBlurInputTexCoordHandle;
      private int linearBlurPositionHandle;
      private int linearBlurShaderProgram;
      private int linearBlurSourceImage2Handle;
      private int linearBlurSourceImageHandle;
      private boolean needUpdateBlurTexture = true;
      private int positionHandle;
      private int radialBlurAspectRatioHandle;
      private int radialBlurExcludeBlurSizeHandle;
      private int radialBlurExcludePointHandle;
      private int radialBlurExcludeSizeHandle;
      private int radialBlurInputTexCoordHandle;
      private int radialBlurPositionHandle;
      private int radialBlurShaderProgram;
      private int radialBlurSourceImage2Handle;
      private int radialBlurSourceImageHandle;
      private int renderBufferHeight;
      private int renderBufferWidth;
      private int[] renderFrameBuffer = new int[3];
      private int[] renderTexture = new int[3];
      private int rgbToHsvInputTexCoordHandle;
      private int rgbToHsvPositionHandle;
      private int rgbToHsvShaderProgram;
      private int rgbToHsvSourceImageHandle;
      private int saturationHandle;
      private int shadowsHandle;
      private int shadowsTintColorHandle;
      private int shadowsTintIntensityHandle;
      private int sharpenHandle;
      private int sharpenHeightHandle;
      private int sharpenInputTexCoordHandle;
      private int sharpenPositionHandle;
      private int sharpenShaderProgram;
      private int sharpenSourceImageHandle;
      private int sharpenWidthHandle;
      private int simpleInputTexCoordHandle;
      private int simplePositionHandle;
      private int simpleShaderProgram;
      private int simpleSourceImageHandle;
      private int skipToneHandle;
      private int sourceImageHandle;
      private volatile int surfaceHeight;
      private SurfaceTexture surfaceTexture;
      private volatile int surfaceWidth;
      private FloatBuffer textureBuffer;
      private int toolsShaderProgram;
      private FloatBuffer vertexBuffer;
      private FloatBuffer vertexInvertBuffer;
      private int vignetteHandle;
      private int warmthHandle;
      private int widthHandle;

      public EGLThread(SurfaceTexture var2, Bitmap var3) {
         super("EGLThread");
         this.surfaceTexture = var2;
         this.currentBitmap = var3;
      }

      private Bitmap createBitmap(Bitmap var1, int var2, int var3, float var4) {
         Matrix var5 = new Matrix();
         var5.setScale(var4, var4);
         var5.postRotate((float)PhotoFilterView.this.orientation);
         return Bitmap.createBitmap(var1, 0, 0, var1.getWidth(), var1.getHeight(), var5, true);
      }

      private boolean drawBlurPass() {
         if (!PhotoFilterView.this.showOriginal && PhotoFilterView.this.blurType != 0) {
            if (this.needUpdateBlurTexture) {
               GLES20.glUseProgram(this.blurShaderProgram);
               GLES20.glUniform1i(this.blurSourceImageHandle, 0);
               GLES20.glEnableVertexAttribArray(this.blurInputTexCoordHandle);
               GLES20.glVertexAttribPointer(this.blurInputTexCoordHandle, 2, 5126, false, 8, this.textureBuffer);
               GLES20.glEnableVertexAttribArray(this.blurPositionHandle);
               GLES20.glVertexAttribPointer(this.blurPositionHandle, 2, 5126, false, 8, this.vertexInvertBuffer);
               GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[0]);
               GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[0], 0);
               GLES20.glClear(0);
               GLES20.glActiveTexture(33984);
               GLES20.glBindTexture(3553, this.renderTexture[1]);
               GLES20.glUniform1f(this.blurWidthHandle, 0.0F);
               GLES20.glUniform1f(this.blurHeightHandle, 1.0F / (float)this.renderBufferHeight);
               GLES20.glDrawArrays(5, 0, 4);
               GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[2]);
               GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[2], 0);
               GLES20.glClear(0);
               GLES20.glActiveTexture(33984);
               GLES20.glBindTexture(3553, this.renderTexture[0]);
               GLES20.glUniform1f(this.blurWidthHandle, 1.0F / (float)this.renderBufferWidth);
               GLES20.glUniform1f(this.blurHeightHandle, 0.0F);
               GLES20.glDrawArrays(5, 0, 4);
               this.needUpdateBlurTexture = false;
            }

            GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[0]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[0], 0);
            GLES20.glClear(0);
            if (PhotoFilterView.this.blurType == 1) {
               GLES20.glUseProgram(this.radialBlurShaderProgram);
               GLES20.glUniform1i(this.radialBlurSourceImageHandle, 0);
               GLES20.glUniform1i(this.radialBlurSourceImage2Handle, 1);
               GLES20.glUniform1f(this.radialBlurExcludeSizeHandle, PhotoFilterView.this.blurExcludeSize);
               GLES20.glUniform1f(this.radialBlurExcludeBlurSizeHandle, PhotoFilterView.this.blurExcludeBlurSize);
               GLES20.glUniform2f(this.radialBlurExcludePointHandle, PhotoFilterView.this.blurExcludePoint.x, PhotoFilterView.this.blurExcludePoint.y);
               GLES20.glUniform1f(this.radialBlurAspectRatioHandle, (float)this.renderBufferHeight / (float)this.renderBufferWidth);
               GLES20.glEnableVertexAttribArray(this.radialBlurInputTexCoordHandle);
               GLES20.glVertexAttribPointer(this.radialBlurInputTexCoordHandle, 2, 5126, false, 8, this.textureBuffer);
               GLES20.glEnableVertexAttribArray(this.radialBlurPositionHandle);
               GLES20.glVertexAttribPointer(this.radialBlurPositionHandle, 2, 5126, false, 8, this.vertexInvertBuffer);
            } else if (PhotoFilterView.this.blurType == 2) {
               GLES20.glUseProgram(this.linearBlurShaderProgram);
               GLES20.glUniform1i(this.linearBlurSourceImageHandle, 0);
               GLES20.glUniform1i(this.linearBlurSourceImage2Handle, 1);
               GLES20.glUniform1f(this.linearBlurExcludeSizeHandle, PhotoFilterView.this.blurExcludeSize);
               GLES20.glUniform1f(this.linearBlurExcludeBlurSizeHandle, PhotoFilterView.this.blurExcludeBlurSize);
               GLES20.glUniform1f(this.linearBlurAngleHandle, PhotoFilterView.this.blurAngle);
               GLES20.glUniform2f(this.linearBlurExcludePointHandle, PhotoFilterView.this.blurExcludePoint.x, PhotoFilterView.this.blurExcludePoint.y);
               GLES20.glUniform1f(this.linearBlurAspectRatioHandle, (float)this.renderBufferHeight / (float)this.renderBufferWidth);
               GLES20.glEnableVertexAttribArray(this.linearBlurInputTexCoordHandle);
               GLES20.glVertexAttribPointer(this.linearBlurInputTexCoordHandle, 2, 5126, false, 8, this.textureBuffer);
               GLES20.glEnableVertexAttribArray(this.linearBlurPositionHandle);
               GLES20.glVertexAttribPointer(this.linearBlurPositionHandle, 2, 5126, false, 8, this.vertexInvertBuffer);
            }

            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.renderTexture[1]);
            GLES20.glActiveTexture(33985);
            GLES20.glBindTexture(3553, this.renderTexture[2]);
            GLES20.glDrawArrays(5, 0, 4);
            return true;
         } else {
            return false;
         }
      }

      private void drawCustomParamsPass() {
         GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[1]);
         GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[1], 0);
         GLES20.glClear(0);
         GLES20.glUseProgram(this.toolsShaderProgram);
         GLES20.glActiveTexture(33984);
         GLES20.glBindTexture(3553, this.renderTexture[0]);
         GLES20.glUniform1i(this.sourceImageHandle, 0);
         boolean var1 = PhotoFilterView.this.showOriginal;
         float var2 = 1.0F;
         if (var1) {
            GLES20.glUniform1f(this.shadowsHandle, 1.0F);
            GLES20.glUniform1f(this.highlightsHandle, 1.0F);
            GLES20.glUniform1f(this.exposureHandle, 0.0F);
            GLES20.glUniform1f(this.contrastHandle, 1.0F);
            GLES20.glUniform1f(this.saturationHandle, 1.0F);
            GLES20.glUniform1f(this.warmthHandle, 0.0F);
            GLES20.glUniform1f(this.vignetteHandle, 0.0F);
            GLES20.glUniform1f(this.grainHandle, 0.0F);
            GLES20.glUniform1f(this.fadeAmountHandle, 0.0F);
            GLES20.glUniform3f(this.highlightsTintColorHandle, 0.0F, 0.0F, 0.0F);
            GLES20.glUniform1f(this.highlightsTintIntensityHandle, 0.0F);
            GLES20.glUniform3f(this.shadowsTintColorHandle, 0.0F, 0.0F, 0.0F);
            GLES20.glUniform1f(this.shadowsTintIntensityHandle, 0.0F);
            GLES20.glUniform1f(this.skipToneHandle, 1.0F);
         } else {
            GLES20.glUniform1f(this.shadowsHandle, PhotoFilterView.this.getShadowsValue());
            GLES20.glUniform1f(this.highlightsHandle, PhotoFilterView.this.getHighlightsValue());
            GLES20.glUniform1f(this.exposureHandle, PhotoFilterView.this.getExposureValue());
            GLES20.glUniform1f(this.contrastHandle, PhotoFilterView.this.getContrastValue());
            GLES20.glUniform1f(this.saturationHandle, PhotoFilterView.this.getSaturationValue());
            GLES20.glUniform1f(this.warmthHandle, PhotoFilterView.this.getWarmthValue());
            GLES20.glUniform1f(this.vignetteHandle, PhotoFilterView.this.getVignetteValue());
            GLES20.glUniform1f(this.grainHandle, PhotoFilterView.this.getGrainValue());
            GLES20.glUniform1f(this.fadeAmountHandle, PhotoFilterView.this.getFadeValue());
            GLES20.glUniform3f(this.highlightsTintColorHandle, (float)(PhotoFilterView.this.tintHighlightsColor >> 16 & 255) / 255.0F, (float)(PhotoFilterView.this.tintHighlightsColor >> 8 & 255) / 255.0F, (float)(PhotoFilterView.this.tintHighlightsColor & 255) / 255.0F);
            GLES20.glUniform1f(this.highlightsTintIntensityHandle, PhotoFilterView.this.getTintHighlightsIntensityValue());
            GLES20.glUniform3f(this.shadowsTintColorHandle, (float)(PhotoFilterView.this.tintShadowsColor >> 16 & 255) / 255.0F, (float)(PhotoFilterView.this.tintShadowsColor >> 8 & 255) / 255.0F, (float)(PhotoFilterView.this.tintShadowsColor & 255) / 255.0F);
            GLES20.glUniform1f(this.shadowsTintIntensityHandle, PhotoFilterView.this.getTintShadowsIntensityValue());
            var1 = PhotoFilterView.this.curvesToolValue.shouldBeSkipped();
            int var3 = this.skipToneHandle;
            if (!var1) {
               var2 = 0.0F;
            }

            GLES20.glUniform1f(var3, var2);
            if (!var1) {
               PhotoFilterView.this.curvesToolValue.fillBuffer();
               GLES20.glActiveTexture(33985);
               GLES20.glBindTexture(3553, this.curveTextures[0]);
               GLES20.glTexParameteri(3553, 10241, 9729);
               GLES20.glTexParameteri(3553, 10240, 9729);
               GLES20.glTexParameteri(3553, 10242, 33071);
               GLES20.glTexParameteri(3553, 10243, 33071);
               GLES20.glTexImage2D(3553, 0, 6408, 200, 1, 0, 6408, 5121, PhotoFilterView.this.curvesToolValue.curveBuffer);
               GLES20.glUniform1i(this.curvesImageHandle, 1);
            }
         }

         GLES20.glUniform1f(this.widthHandle, (float)this.renderBufferWidth);
         GLES20.glUniform1f(this.heightHandle, (float)this.renderBufferHeight);
         GLES20.glEnableVertexAttribArray(this.inputTexCoordHandle);
         GLES20.glVertexAttribPointer(this.inputTexCoordHandle, 2, 5126, false, 8, this.textureBuffer);
         GLES20.glEnableVertexAttribArray(this.positionHandle);
         GLES20.glVertexAttribPointer(this.positionHandle, 2, 5126, false, 8, this.vertexInvertBuffer);
         GLES20.glDrawArrays(5, 0, 4);
      }

      private void drawEnhancePass() {
         if (!this.hsvGenerated) {
            GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[0]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[0], 0);
            GLES20.glClear(0);
            GLES20.glUseProgram(this.rgbToHsvShaderProgram);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.renderTexture[1]);
            GLES20.glUniform1i(this.rgbToHsvSourceImageHandle, 0);
            GLES20.glEnableVertexAttribArray(this.rgbToHsvInputTexCoordHandle);
            GLES20.glVertexAttribPointer(this.rgbToHsvInputTexCoordHandle, 2, 5126, false, 8, this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.rgbToHsvPositionHandle);
            GLES20.glVertexAttribPointer(this.rgbToHsvPositionHandle, 2, 5126, false, 8, this.vertexBuffer);
            GLES20.glDrawArrays(5, 0, 4);
            ByteBuffer var1 = ByteBuffer.allocateDirect(this.renderBufferWidth * this.renderBufferHeight * 4);
            GLES20.glReadPixels(0, 0, this.renderBufferWidth, this.renderBufferHeight, 6408, 5121, var1);
            GLES20.glBindTexture(3553, this.enhanceTextures[0]);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLES20.glTexImage2D(3553, 0, 6408, this.renderBufferWidth, this.renderBufferHeight, 0, 6408, 5121, var1);
            ByteBuffer var2 = null;

            label32: {
               ByteBuffer var3;
               label31: {
                  Exception var10000;
                  label39: {
                     boolean var10001;
                     try {
                        var3 = ByteBuffer.allocateDirect(16384);
                     } catch (Exception var5) {
                        var10000 = var5;
                        var10001 = false;
                        break label39;
                     }

                     var2 = var3;

                     try {
                        Utilities.calcCDT(var1, this.renderBufferWidth, this.renderBufferHeight, var3);
                        break label31;
                     } catch (Exception var4) {
                        var10000 = var4;
                        var10001 = false;
                     }
                  }

                  Exception var6 = var10000;
                  FileLog.e((Throwable)var6);
                  break label32;
               }

               var2 = var3;
            }

            GLES20.glBindTexture(3553, this.enhanceTextures[1]);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLES20.glTexImage2D(3553, 0, 6408, 256, 16, 0, 6408, 5121, var2);
            this.hsvGenerated = true;
         }

         GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[1]);
         GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[1], 0);
         GLES20.glClear(0);
         GLES20.glUseProgram(this.enhanceShaderProgram);
         GLES20.glActiveTexture(33984);
         GLES20.glBindTexture(3553, this.enhanceTextures[0]);
         GLES20.glUniform1i(this.enhanceSourceImageHandle, 0);
         GLES20.glActiveTexture(33985);
         GLES20.glBindTexture(3553, this.enhanceTextures[1]);
         GLES20.glUniform1i(this.enhanceInputImageTexture2Handle, 1);
         if (PhotoFilterView.this.showOriginal) {
            GLES20.glUniform1f(this.enhanceIntensityHandle, 0.0F);
         } else {
            GLES20.glUniform1f(this.enhanceIntensityHandle, PhotoFilterView.this.getEnhanceValue());
         }

         GLES20.glEnableVertexAttribArray(this.enhanceInputTexCoordHandle);
         GLES20.glVertexAttribPointer(this.enhanceInputTexCoordHandle, 2, 5126, false, 8, this.textureBuffer);
         GLES20.glEnableVertexAttribArray(this.enhancePositionHandle);
         GLES20.glVertexAttribPointer(this.enhancePositionHandle, 2, 5126, false, 8, this.vertexBuffer);
         GLES20.glDrawArrays(5, 0, 4);
      }

      private void drawSharpenPass() {
         GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[0]);
         GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[0], 0);
         GLES20.glClear(0);
         GLES20.glUseProgram(this.sharpenShaderProgram);
         GLES20.glActiveTexture(33984);
         GLES20.glBindTexture(3553, this.renderTexture[1]);
         GLES20.glUniform1i(this.sharpenSourceImageHandle, 0);
         if (PhotoFilterView.this.showOriginal) {
            GLES20.glUniform1f(this.sharpenHandle, 0.0F);
         } else {
            GLES20.glUniform1f(this.sharpenHandle, PhotoFilterView.this.getSharpenValue());
         }

         GLES20.glUniform1f(this.sharpenWidthHandle, (float)this.renderBufferWidth);
         GLES20.glUniform1f(this.sharpenHeightHandle, (float)this.renderBufferHeight);
         GLES20.glEnableVertexAttribArray(this.sharpenInputTexCoordHandle);
         GLES20.glVertexAttribPointer(this.sharpenInputTexCoordHandle, 2, 5126, false, 8, this.textureBuffer);
         GLES20.glEnableVertexAttribArray(this.sharpenPositionHandle);
         GLES20.glVertexAttribPointer(this.sharpenPositionHandle, 2, 5126, false, 8, this.vertexInvertBuffer);
         GLES20.glDrawArrays(5, 0, 4);
      }

      private Bitmap getRenderBufferBitmap() {
         ByteBuffer var1 = ByteBuffer.allocateDirect(this.renderBufferWidth * this.renderBufferHeight * 4);
         GLES20.glReadPixels(0, 0, this.renderBufferWidth, this.renderBufferHeight, 6408, 5121, var1);
         Bitmap var2 = Bitmap.createBitmap(this.renderBufferWidth, this.renderBufferHeight, Config.ARGB_8888);
         var2.copyPixelsFromBuffer(var1);
         return var2;
      }

      private boolean initGL() {
         this.egl10 = (EGL10)EGLContext.getEGL();
         this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
         EGLDisplay var1 = this.eglDisplay;
         StringBuilder var8;
         if (var1 == EGL10.EGL_NO_DISPLAY) {
            if (BuildVars.LOGS_ENABLED) {
               var8 = new StringBuilder();
               var8.append("eglGetDisplay failed ");
               var8.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
               FileLog.e(var8.toString());
            }

            this.finish();
            return false;
         } else {
            int[] var2 = new int[2];
            if (!this.egl10.eglInitialize(var1, var2)) {
               if (BuildVars.LOGS_ENABLED) {
                  var8 = new StringBuilder();
                  var8.append("eglInitialize failed ");
                  var8.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                  FileLog.e(var8.toString());
               }

               this.finish();
               return false;
            } else {
               var2 = new int[1];
               EGLConfig[] var5 = new EGLConfig[1];
               if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12326, 0, 12344}, var5, 1, var2)) {
                  if (BuildVars.LOGS_ENABLED) {
                     var8 = new StringBuilder();
                     var8.append("eglChooseConfig failed ");
                     var8.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                     FileLog.e(var8.toString());
                  }

                  this.finish();
                  return false;
               } else if (var2[0] > 0) {
                  this.eglConfig = var5[0];
                  this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
                  if (this.eglContext == null) {
                     if (BuildVars.LOGS_ENABLED) {
                        var8 = new StringBuilder();
                        var8.append("eglCreateContext failed ");
                        var8.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        FileLog.e(var8.toString());
                     }

                     this.finish();
                     return false;
                  } else {
                     SurfaceTexture var6 = this.surfaceTexture;
                     if (var6 instanceof SurfaceTexture) {
                        this.eglSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, var6, (int[])null);
                        EGLSurface var7 = this.eglSurface;
                        if (var7 != null && var7 != EGL10.EGL_NO_SURFACE) {
                           if (!this.egl10.eglMakeCurrent(this.eglDisplay, var7, var7, this.eglContext)) {
                              if (BuildVars.LOGS_ENABLED) {
                                 var8 = new StringBuilder();
                                 var8.append("eglMakeCurrent failed ");
                                 var8.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                 FileLog.e(var8.toString());
                              }

                              this.finish();
                              return false;
                           } else {
                              this.gl = this.eglContext.getGL();
                              float[] var9 = new float[]{-1.0F, 1.0F, 1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F};
                              ByteBuffer var10 = ByteBuffer.allocateDirect(var9.length * 4);
                              var10.order(ByteOrder.nativeOrder());
                              this.vertexBuffer = var10.asFloatBuffer();
                              this.vertexBuffer.put(var9);
                              this.vertexBuffer.position(0);
                              float[] var11 = new float[]{-1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F};
                              ByteBuffer var12 = ByteBuffer.allocateDirect(var11.length * 4);
                              var12.order(ByteOrder.nativeOrder());
                              this.vertexInvertBuffer = var12.asFloatBuffer();
                              this.vertexInvertBuffer.put(var11);
                              this.vertexInvertBuffer.position(0);
                              var11 = new float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F};
                              var12 = ByteBuffer.allocateDirect(var11.length * 4);
                              var12.order(ByteOrder.nativeOrder());
                              this.textureBuffer = var12.asFloatBuffer();
                              this.textureBuffer.put(var11);
                              this.textureBuffer.position(0);
                              GLES20.glGenTextures(1, this.curveTextures, 0);
                              GLES20.glGenTextures(2, this.enhanceTextures, 0);
                              int var3 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
                              int var4 = this.loadShader(35632, "varying highp vec2 texCoord;uniform sampler2D sourceImage;uniform highp float width;uniform highp float height;uniform sampler2D curvesImage;uniform lowp float skipTone;uniform lowp float shadows;const mediump vec3 hsLuminanceWeighting = vec3(0.3, 0.3, 0.3);uniform lowp float highlights;uniform lowp float contrast;uniform lowp float fadeAmount;const mediump vec3 satLuminanceWeighting = vec3(0.2126, 0.7152, 0.0722);uniform lowp float saturation;uniform lowp float shadowsTintIntensity;uniform lowp float highlightsTintIntensity;uniform lowp vec3 shadowsTintColor;uniform lowp vec3 highlightsTintColor;uniform lowp float exposure;uniform lowp float warmth;uniform lowp float grain;const lowp float permTexUnit = 1.0 / 256.0;const lowp float permTexUnitHalf = 0.5 / 256.0;const lowp float grainsize = 2.3;uniform lowp float vignette;highp float getLuma(highp vec3 rgbP) {return (0.299 * rgbP.r) + (0.587 * rgbP.g) + (0.114 * rgbP.b);}lowp vec3 rgbToHsv(lowp vec3 c) {highp vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);highp vec4 p = c.g < c.b ? vec4(c.bg, K.wz) : vec4(c.gb, K.xy);highp vec4 q = c.r < p.x ? vec4(p.xyw, c.r) : vec4(c.r, p.yzx);highp float d = q.x - min(q.w, q.y);highp float e = 1.0e-10;return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);}lowp vec3 hsvToRgb(lowp vec3 c) {highp vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);highp vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);}highp vec3 rgbToHsl(highp vec3 color) {highp vec3 hsl;highp float fmin = min(min(color.r, color.g), color.b);highp float fmax = max(max(color.r, color.g), color.b);highp float delta = fmax - fmin;hsl.z = (fmax + fmin) / 2.0;if (delta == 0.0) {hsl.x = 0.0;hsl.y = 0.0;} else {if (hsl.z < 0.5) {hsl.y = delta / (fmax + fmin);} else {hsl.y = delta / (2.0 - fmax - fmin);}highp float deltaR = (((fmax - color.r) / 6.0) + (delta / 2.0)) / delta;highp float deltaG = (((fmax - color.g) / 6.0) + (delta / 2.0)) / delta;highp float deltaB = (((fmax - color.b) / 6.0) + (delta / 2.0)) / delta;if (color.r == fmax) {hsl.x = deltaB - deltaG;} else if (color.g == fmax) {hsl.x = (1.0 / 3.0) + deltaR - deltaB;} else if (color.b == fmax) {hsl.x = (2.0 / 3.0) + deltaG - deltaR;}if (hsl.x < 0.0) {hsl.x += 1.0;} else if (hsl.x > 1.0) {hsl.x -= 1.0;}}return hsl;}highp float hueToRgb(highp float f1, highp float f2, highp float hue) {if (hue < 0.0) {hue += 1.0;} else if (hue > 1.0) {hue -= 1.0;}highp float res;if ((6.0 * hue) < 1.0) {res = f1 + (f2 - f1) * 6.0 * hue;} else if ((2.0 * hue) < 1.0) {res = f2;} else if ((3.0 * hue) < 2.0) {res = f1 + (f2 - f1) * ((2.0 / 3.0) - hue) * 6.0;} else {res = f1;} return res;}highp vec3 hslToRgb(highp vec3 hsl) {if (hsl.y == 0.0) {return vec3(hsl.z);} else {highp float f2;if (hsl.z < 0.5) {f2 = hsl.z * (1.0 + hsl.y);} else {f2 = (hsl.z + hsl.y) - (hsl.y * hsl.z);}highp float f1 = 2.0 * hsl.z - f2;return vec3(hueToRgb(f1, f2, hsl.x + (1.0/3.0)), hueToRgb(f1, f2, hsl.x), hueToRgb(f1, f2, hsl.x - (1.0/3.0)));}}highp vec3 rgbToYuv(highp vec3 inP) {highp float luma = getLuma(inP);return vec3(luma, (1.0 / 1.772) * (inP.b - luma), (1.0 / 1.402) * (inP.r - luma));}lowp vec3 yuvToRgb(highp vec3 inP) {return vec3(1.402 * inP.b + inP.r, (inP.r - (0.299 * 1.402 / 0.587) * inP.b - (0.114 * 1.772 / 0.587) * inP.g), 1.772 * inP.g + inP.r);}lowp float easeInOutSigmoid(lowp float value, lowp float strength) {if (value > 0.5) {return 1.0 - pow(2.0 - 2.0 * value, 1.0 / (1.0 - strength)) * 0.5;} else {return pow(2.0 * value, 1.0 / (1.0 - strength)) * 0.5;}}lowp vec3 applyLuminanceCurve(lowp vec3 pixel) {highp float index = floor(clamp(pixel.z / (1.0 / 200.0), 0.0, 199.0));pixel.y = mix(0.0, pixel.y, smoothstep(0.0, 0.1, pixel.z) * (1.0 - smoothstep(0.8, 1.0, pixel.z)));pixel.z = texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).a;return pixel;}lowp vec3 applyRGBCurve(lowp vec3 pixel) {highp float index = floor(clamp(pixel.r / (1.0 / 200.0), 0.0, 199.0));pixel.r = texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).r;index = floor(clamp(pixel.g / (1.0 / 200.0), 0.0, 199.0));pixel.g = clamp(texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).g, 0.0, 1.0);index = floor(clamp(pixel.b / (1.0 / 200.0), 0.0, 199.0));pixel.b = clamp(texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).b, 0.0, 1.0);return pixel;}highp vec3 fadeAdjust(highp vec3 color, highp float fadeVal) {return (color * (1.0 - fadeVal)) + ((color + (vec3(-0.9772) * pow(vec3(color), vec3(3.0)) + vec3(1.708) * pow(vec3(color), vec3(2.0)) + vec3(-0.1603) * vec3(color) + vec3(0.2878) - color * vec3(0.9))) * fadeVal);}lowp vec3 tintRaiseShadowsCurve(lowp vec3 color) {return vec3(-0.003671) * pow(color, vec3(3.0)) + vec3(0.3842) * pow(color, vec3(2.0)) + vec3(0.3764) * color + vec3(0.2515);}lowp vec3 tintShadows(lowp vec3 texel, lowp vec3 tintColor, lowp float tintAmount) {return clamp(mix(texel, mix(texel, tintRaiseShadowsCurve(texel), tintColor), tintAmount), 0.0, 1.0);} lowp vec3 tintHighlights(lowp vec3 texel, lowp vec3 tintColor, lowp float tintAmount) {return clamp(mix(texel, mix(texel, vec3(1.0) - tintRaiseShadowsCurve(vec3(1.0) - texel), (vec3(1.0) - tintColor)), tintAmount), 0.0, 1.0);}highp vec4 rnm(in highp vec2 tc) {highp float noise = sin(dot(tc, vec2(12.9898, 78.233))) * 43758.5453;return vec4(fract(noise), fract(noise * 1.2154), fract(noise * 1.3453), fract(noise * 1.3647)) * 2.0 - 1.0;}highp float fade(in highp float t) {return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);}highp float pnoise3D(in highp vec3 p) {highp vec3 pi = permTexUnit * floor(p) + permTexUnitHalf;highp vec3 pf = fract(p);highp float perm = rnm(pi.xy).a;highp float n000 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf);highp float n001 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(0.0, 0.0, 1.0));perm = rnm(pi.xy + vec2(0.0, permTexUnit)).a;highp float n010 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf - vec3(0.0, 1.0, 0.0));highp float n011 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(0.0, 1.0, 1.0));perm = rnm(pi.xy + vec2(permTexUnit, 0.0)).a;highp float n100 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf - vec3(1.0, 0.0, 0.0));highp float n101 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(1.0, 0.0, 1.0));perm = rnm(pi.xy + vec2(permTexUnit, permTexUnit)).a;highp float n110 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf - vec3(1.0, 1.0, 0.0));highp float n111 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(1.0, 1.0, 1.0));highp vec4 n_x = mix(vec4(n000, n001, n010, n011), vec4(n100, n101, n110, n111), fade(pf.x));highp vec2 n_xy = mix(n_x.xy, n_x.zw, fade(pf.y));return mix(n_xy.x, n_xy.y, fade(pf.z));}lowp vec2 coordRot(in lowp vec2 tc, in lowp float angle) {return vec2(((tc.x * 2.0 - 1.0) * cos(angle) - (tc.y * 2.0 - 1.0) * sin(angle)) * 0.5 + 0.5, ((tc.y * 2.0 - 1.0) * cos(angle) + (tc.x * 2.0 - 1.0) * sin(angle)) * 0.5 + 0.5);}void main() {lowp vec4 source = texture2D(sourceImage, texCoord);lowp vec4 result = source;const lowp float toolEpsilon = 0.005;if (skipTone < toolEpsilon) {result = vec4(applyRGBCurve(hslToRgb(applyLuminanceCurve(rgbToHsl(result.rgb)))), result.a);}mediump float hsLuminance = dot(result.rgb, hsLuminanceWeighting);mediump float shadow = clamp((pow(hsLuminance, 1.0 / shadows) + (-0.76) * pow(hsLuminance, 2.0 / shadows)) - hsLuminance, 0.0, 1.0);mediump float highlight = clamp((1.0 - (pow(1.0 - hsLuminance, 1.0 / (2.0 - highlights)) + (-0.8) * pow(1.0 - hsLuminance, 2.0 / (2.0 - highlights)))) - hsLuminance, -1.0, 0.0);lowp vec3 hsresult = vec3(0.0, 0.0, 0.0) + ((hsLuminance + shadow + highlight) - 0.0) * ((result.rgb - vec3(0.0, 0.0, 0.0)) / (hsLuminance - 0.0));mediump float contrastedLuminance = ((hsLuminance - 0.5) * 1.5) + 0.5;mediump float whiteInterp = contrastedLuminance * contrastedLuminance * contrastedLuminance;mediump float whiteTarget = clamp(highlights, 1.0, 2.0) - 1.0;hsresult = mix(hsresult, vec3(1.0), whiteInterp * whiteTarget);mediump float invContrastedLuminance = 1.0 - contrastedLuminance;mediump float blackInterp = invContrastedLuminance * invContrastedLuminance * invContrastedLuminance;mediump float blackTarget = 1.0 - clamp(shadows, 0.0, 1.0);hsresult = mix(hsresult, vec3(0.0), blackInterp * blackTarget);result = vec4(hsresult.rgb, result.a);result = vec4(clamp(((result.rgb - vec3(0.5)) * contrast + vec3(0.5)), 0.0, 1.0), result.a);if (abs(fadeAmount) > toolEpsilon) {result.rgb = fadeAdjust(result.rgb, fadeAmount);}lowp float satLuminance = dot(result.rgb, satLuminanceWeighting);lowp vec3 greyScaleColor = vec3(satLuminance);result = vec4(clamp(mix(greyScaleColor, result.rgb, saturation), 0.0, 1.0), result.a);if (abs(shadowsTintIntensity) > toolEpsilon) {result.rgb = tintShadows(result.rgb, shadowsTintColor, shadowsTintIntensity * 2.0);}if (abs(highlightsTintIntensity) > toolEpsilon) {result.rgb = tintHighlights(result.rgb, highlightsTintColor, highlightsTintIntensity * 2.0);}if (abs(exposure) > toolEpsilon) {mediump float mag = exposure * 1.045;mediump float exppower = 1.0 + abs(mag);if (mag < 0.0) {exppower = 1.0 / exppower;}result.r = 1.0 - pow((1.0 - result.r), exppower);result.g = 1.0 - pow((1.0 - result.g), exppower);result.b = 1.0 - pow((1.0 - result.b), exppower);}if (abs(warmth) > toolEpsilon) {highp vec3 yuvVec;if (warmth > 0.0 ) {yuvVec = vec3(0.1765, -0.1255, 0.0902);} else {yuvVec = -vec3(0.0588, 0.1569, -0.1255);}highp vec3 yuvColor = rgbToYuv(result.rgb);highp float luma = yuvColor.r;highp float curveScale = sin(luma * 3.14159);yuvColor += 0.375 * warmth * curveScale * yuvVec;result.rgb = yuvToRgb(yuvColor);}if (abs(grain) > toolEpsilon) {highp vec3 rotOffset = vec3(1.425, 3.892, 5.835);highp vec2 rotCoordsR = coordRot(texCoord, rotOffset.x);highp vec3 noise = vec3(pnoise3D(vec3(rotCoordsR * vec2(width / grainsize, height / grainsize),0.0)));lowp vec3 lumcoeff = vec3(0.299,0.587,0.114);lowp float luminance = dot(result.rgb, lumcoeff);lowp float lum = smoothstep(0.2, 0.0, luminance);lum += luminance;noise = mix(noise,vec3(0.0),pow(lum,4.0));result.rgb = result.rgb + noise * grain;}if (abs(vignette) > toolEpsilon) {const lowp float midpoint = 0.7;const lowp float fuzziness = 0.62;lowp float radDist = length(texCoord - 0.5) / sqrt(0.5);lowp float mag = easeInOutSigmoid(radDist * midpoint, fuzziness) * vignette * 0.645;result.rgb = mix(pow(result.rgb, vec3(1.0 / (1.0 - mag))), vec3(0.0), mag * mag);}gl_FragColor = result;}");
                              if (var3 != 0 && var4 != 0) {
                                 this.toolsShaderProgram = GLES20.glCreateProgram();
                                 GLES20.glAttachShader(this.toolsShaderProgram, var3);
                                 GLES20.glAttachShader(this.toolsShaderProgram, var4);
                                 GLES20.glBindAttribLocation(this.toolsShaderProgram, 0, "position");
                                 GLES20.glBindAttribLocation(this.toolsShaderProgram, 1, "inputTexCoord");
                                 GLES20.glLinkProgram(this.toolsShaderProgram);
                                 int[] var13 = new int[1];
                                 GLES20.glGetProgramiv(this.toolsShaderProgram, 35714, var13, 0);
                                 if (var13[0] == 0) {
                                    GLES20.glDeleteProgram(this.toolsShaderProgram);
                                    this.toolsShaderProgram = 0;
                                 } else {
                                    this.positionHandle = GLES20.glGetAttribLocation(this.toolsShaderProgram, "position");
                                    this.inputTexCoordHandle = GLES20.glGetAttribLocation(this.toolsShaderProgram, "inputTexCoord");
                                    this.sourceImageHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "sourceImage");
                                    this.shadowsHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "shadows");
                                    this.highlightsHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "highlights");
                                    this.exposureHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "exposure");
                                    this.contrastHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "contrast");
                                    this.saturationHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "saturation");
                                    this.warmthHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "warmth");
                                    this.vignetteHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "vignette");
                                    this.grainHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "grain");
                                    this.widthHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "width");
                                    this.heightHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "height");
                                    this.curvesImageHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "curvesImage");
                                    this.skipToneHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "skipTone");
                                    this.fadeAmountHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "fadeAmount");
                                    this.shadowsTintIntensityHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "shadowsTintIntensity");
                                    this.highlightsTintIntensityHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "highlightsTintIntensity");
                                    this.shadowsTintColorHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "shadowsTintColor");
                                    this.highlightsTintColorHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, "highlightsTintColor");
                                 }

                                 var3 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;uniform highp float inputWidth;uniform highp float inputHeight;varying vec2 leftTexCoord;varying vec2 rightTexCoord;varying vec2 topTexCoord;varying vec2 bottomTexCoord;void main() {gl_Position = position;texCoord = inputTexCoord;highp vec2 widthStep = vec2(1.0 / inputWidth, 0.0);highp vec2 heightStep = vec2(0.0, 1.0 / inputHeight);leftTexCoord = inputTexCoord - widthStep;rightTexCoord = inputTexCoord + widthStep;topTexCoord = inputTexCoord + heightStep;bottomTexCoord = inputTexCoord - heightStep;}");
                                 var4 = this.loadShader(35632, "precision highp float;varying vec2 texCoord;varying vec2 leftTexCoord;varying vec2 rightTexCoord;varying vec2 topTexCoord;varying vec2 bottomTexCoord;uniform sampler2D sourceImage;uniform float sharpen;void main() {vec4 result = texture2D(sourceImage, texCoord);vec3 leftTextureColor = texture2D(sourceImage, leftTexCoord).rgb;vec3 rightTextureColor = texture2D(sourceImage, rightTexCoord).rgb;vec3 topTextureColor = texture2D(sourceImage, topTexCoord).rgb;vec3 bottomTextureColor = texture2D(sourceImage, bottomTexCoord).rgb;result.rgb = result.rgb * (1.0 + 4.0 * sharpen) - (leftTextureColor + rightTextureColor + topTextureColor + bottomTextureColor) * sharpen;gl_FragColor = result;}");
                                 if (var3 != 0 && var4 != 0) {
                                    this.sharpenShaderProgram = GLES20.glCreateProgram();
                                    GLES20.glAttachShader(this.sharpenShaderProgram, var3);
                                    GLES20.glAttachShader(this.sharpenShaderProgram, var4);
                                    GLES20.glBindAttribLocation(this.sharpenShaderProgram, 0, "position");
                                    GLES20.glBindAttribLocation(this.sharpenShaderProgram, 1, "inputTexCoord");
                                    GLES20.glLinkProgram(this.sharpenShaderProgram);
                                    var13 = new int[1];
                                    GLES20.glGetProgramiv(this.sharpenShaderProgram, 35714, var13, 0);
                                    if (var13[0] == 0) {
                                       GLES20.glDeleteProgram(this.sharpenShaderProgram);
                                       this.sharpenShaderProgram = 0;
                                    } else {
                                       this.sharpenPositionHandle = GLES20.glGetAttribLocation(this.sharpenShaderProgram, "position");
                                       this.sharpenInputTexCoordHandle = GLES20.glGetAttribLocation(this.sharpenShaderProgram, "inputTexCoord");
                                       this.sharpenSourceImageHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "sourceImage");
                                       this.sharpenWidthHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "inputWidth");
                                       this.sharpenHeightHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "inputHeight");
                                       this.sharpenHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "sharpen");
                                    }

                                    var3 = this.loadShader(35633, "attribute vec4 position;attribute vec4 inputTexCoord;uniform highp float texelWidthOffset;uniform highp float texelHeightOffset;varying vec2 blurCoordinates[9];void main() {gl_Position = position;vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);blurCoordinates[0] = inputTexCoord.xy;blurCoordinates[1] = inputTexCoord.xy + singleStepOffset * 1.458430;blurCoordinates[2] = inputTexCoord.xy - singleStepOffset * 1.458430;blurCoordinates[3] = inputTexCoord.xy + singleStepOffset * 3.403985;blurCoordinates[4] = inputTexCoord.xy - singleStepOffset * 3.403985;blurCoordinates[5] = inputTexCoord.xy + singleStepOffset * 5.351806;blurCoordinates[6] = inputTexCoord.xy - singleStepOffset * 5.351806;blurCoordinates[7] = inputTexCoord.xy + singleStepOffset * 7.302940;blurCoordinates[8] = inputTexCoord.xy - singleStepOffset * 7.302940;}");
                                    var4 = this.loadShader(35632, "uniform sampler2D sourceImage;varying highp vec2 blurCoordinates[9];void main() {lowp vec4 sum = vec4(0.0);sum += texture2D(sourceImage, blurCoordinates[0]) * 0.133571;sum += texture2D(sourceImage, blurCoordinates[1]) * 0.233308;sum += texture2D(sourceImage, blurCoordinates[2]) * 0.233308;sum += texture2D(sourceImage, blurCoordinates[3]) * 0.135928;sum += texture2D(sourceImage, blurCoordinates[4]) * 0.135928;sum += texture2D(sourceImage, blurCoordinates[5]) * 0.051383;sum += texture2D(sourceImage, blurCoordinates[6]) * 0.051383;sum += texture2D(sourceImage, blurCoordinates[7]) * 0.012595;sum += texture2D(sourceImage, blurCoordinates[8]) * 0.012595;gl_FragColor = sum;}");
                                    if (var3 != 0 && var4 != 0) {
                                       this.blurShaderProgram = GLES20.glCreateProgram();
                                       GLES20.glAttachShader(this.blurShaderProgram, var3);
                                       GLES20.glAttachShader(this.blurShaderProgram, var4);
                                       GLES20.glBindAttribLocation(this.blurShaderProgram, 0, "position");
                                       GLES20.glBindAttribLocation(this.blurShaderProgram, 1, "inputTexCoord");
                                       GLES20.glLinkProgram(this.blurShaderProgram);
                                       var13 = new int[1];
                                       GLES20.glGetProgramiv(this.blurShaderProgram, 35714, var13, 0);
                                       if (var13[0] == 0) {
                                          GLES20.glDeleteProgram(this.blurShaderProgram);
                                          this.blurShaderProgram = 0;
                                       } else {
                                          this.blurPositionHandle = GLES20.glGetAttribLocation(this.blurShaderProgram, "position");
                                          this.blurInputTexCoordHandle = GLES20.glGetAttribLocation(this.blurShaderProgram, "inputTexCoord");
                                          this.blurSourceImageHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "sourceImage");
                                          this.blurWidthHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "texelWidthOffset");
                                          this.blurHeightHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "texelHeightOffset");
                                       }

                                       var3 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
                                       var4 = this.loadShader(35632, "varying highp vec2 texCoord;uniform sampler2D sourceImage;uniform sampler2D inputImageTexture2;uniform lowp float excludeSize;uniform lowp vec2 excludePoint;uniform lowp float excludeBlurSize;uniform highp float angle;uniform highp float aspectRatio;void main() {lowp vec4 sharpImageColor = texture2D(sourceImage, texCoord);lowp vec4 blurredImageColor = texture2D(inputImageTexture2, texCoord);highp vec2 texCoordToUse = vec2(texCoord.x, (texCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));highp float distanceFromCenter = abs((texCoordToUse.x - excludePoint.x) * aspectRatio * cos(angle) + (texCoordToUse.y - excludePoint.y) * sin(angle));gl_FragColor = mix(sharpImageColor, blurredImageColor, smoothstep(excludeSize - excludeBlurSize, excludeSize, distanceFromCenter));}");
                                       if (var3 != 0 && var4 != 0) {
                                          this.linearBlurShaderProgram = GLES20.glCreateProgram();
                                          GLES20.glAttachShader(this.linearBlurShaderProgram, var3);
                                          GLES20.glAttachShader(this.linearBlurShaderProgram, var4);
                                          GLES20.glBindAttribLocation(this.linearBlurShaderProgram, 0, "position");
                                          GLES20.glBindAttribLocation(this.linearBlurShaderProgram, 1, "inputTexCoord");
                                          GLES20.glLinkProgram(this.linearBlurShaderProgram);
                                          var13 = new int[1];
                                          GLES20.glGetProgramiv(this.linearBlurShaderProgram, 35714, var13, 0);
                                          if (var13[0] == 0) {
                                             GLES20.glDeleteProgram(this.linearBlurShaderProgram);
                                             this.linearBlurShaderProgram = 0;
                                          } else {
                                             this.linearBlurPositionHandle = GLES20.glGetAttribLocation(this.linearBlurShaderProgram, "position");
                                             this.linearBlurInputTexCoordHandle = GLES20.glGetAttribLocation(this.linearBlurShaderProgram, "inputTexCoord");
                                             this.linearBlurSourceImageHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "sourceImage");
                                             this.linearBlurSourceImage2Handle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "inputImageTexture2");
                                             this.linearBlurExcludeSizeHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "excludeSize");
                                             this.linearBlurExcludePointHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "excludePoint");
                                             this.linearBlurExcludeBlurSizeHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "excludeBlurSize");
                                             this.linearBlurAngleHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "angle");
                                             this.linearBlurAspectRatioHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "aspectRatio");
                                          }

                                          var4 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
                                          var3 = this.loadShader(35632, "varying highp vec2 texCoord;uniform sampler2D sourceImage;uniform sampler2D inputImageTexture2;uniform lowp float excludeSize;uniform lowp vec2 excludePoint;uniform lowp float excludeBlurSize;uniform highp float aspectRatio;void main() {lowp vec4 sharpImageColor = texture2D(sourceImage, texCoord);lowp vec4 blurredImageColor = texture2D(inputImageTexture2, texCoord);highp vec2 texCoordToUse = vec2(texCoord.x, (texCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));highp float distanceFromCenter = distance(excludePoint, texCoordToUse);gl_FragColor = mix(sharpImageColor, blurredImageColor, smoothstep(excludeSize - excludeBlurSize, excludeSize, distanceFromCenter));}");
                                          if (var4 != 0 && var3 != 0) {
                                             this.radialBlurShaderProgram = GLES20.glCreateProgram();
                                             GLES20.glAttachShader(this.radialBlurShaderProgram, var4);
                                             GLES20.glAttachShader(this.radialBlurShaderProgram, var3);
                                             GLES20.glBindAttribLocation(this.radialBlurShaderProgram, 0, "position");
                                             GLES20.glBindAttribLocation(this.radialBlurShaderProgram, 1, "inputTexCoord");
                                             GLES20.glLinkProgram(this.radialBlurShaderProgram);
                                             var13 = new int[1];
                                             GLES20.glGetProgramiv(this.radialBlurShaderProgram, 35714, var13, 0);
                                             if (var13[0] == 0) {
                                                GLES20.glDeleteProgram(this.radialBlurShaderProgram);
                                                this.radialBlurShaderProgram = 0;
                                             } else {
                                                this.radialBlurPositionHandle = GLES20.glGetAttribLocation(this.radialBlurShaderProgram, "position");
                                                this.radialBlurInputTexCoordHandle = GLES20.glGetAttribLocation(this.radialBlurShaderProgram, "inputTexCoord");
                                                this.radialBlurSourceImageHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "sourceImage");
                                                this.radialBlurSourceImage2Handle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "inputImageTexture2");
                                                this.radialBlurExcludeSizeHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "excludeSize");
                                                this.radialBlurExcludePointHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "excludePoint");
                                                this.radialBlurExcludeBlurSizeHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "excludeBlurSize");
                                                this.radialBlurAspectRatioHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "aspectRatio");
                                             }

                                             var3 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
                                             var4 = this.loadShader(35632, "precision highp float;varying vec2 texCoord;uniform sampler2D sourceImage;vec3 rgb_to_hsv(vec3 c) {vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);vec4 p = c.g < c.b ? vec4(c.bg, K.wz) : vec4(c.gb, K.xy);vec4 q = c.r < p.x ? vec4(p.xyw, c.r) : vec4(c.r, p.yzx);float d = q.x - min(q.w, q.y);float e = 1.0e-10;return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);}void main() {vec4 texel = texture2D(sourceImage, texCoord);gl_FragColor = vec4(rgb_to_hsv(texel.rgb), texel.a);}");
                                             if (var3 != 0 && var4 != 0) {
                                                this.rgbToHsvShaderProgram = GLES20.glCreateProgram();
                                                GLES20.glAttachShader(this.rgbToHsvShaderProgram, var3);
                                                GLES20.glAttachShader(this.rgbToHsvShaderProgram, var4);
                                                GLES20.glBindAttribLocation(this.rgbToHsvShaderProgram, 0, "position");
                                                GLES20.glBindAttribLocation(this.rgbToHsvShaderProgram, 1, "inputTexCoord");
                                                GLES20.glLinkProgram(this.rgbToHsvShaderProgram);
                                                var13 = new int[1];
                                                GLES20.glGetProgramiv(this.rgbToHsvShaderProgram, 35714, var13, 0);
                                                if (var13[0] == 0) {
                                                   GLES20.glDeleteProgram(this.rgbToHsvShaderProgram);
                                                   this.rgbToHsvShaderProgram = 0;
                                                } else {
                                                   this.rgbToHsvPositionHandle = GLES20.glGetAttribLocation(this.rgbToHsvShaderProgram, "position");
                                                   this.rgbToHsvInputTexCoordHandle = GLES20.glGetAttribLocation(this.rgbToHsvShaderProgram, "inputTexCoord");
                                                   this.rgbToHsvSourceImageHandle = GLES20.glGetUniformLocation(this.rgbToHsvShaderProgram, "sourceImage");
                                                }

                                                var3 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
                                                var4 = this.loadShader(35632, "precision highp float;varying vec2 texCoord;uniform sampler2D sourceImage;uniform sampler2D inputImageTexture2;uniform float intensity;float enhance(float value) {const vec2 offset = vec2(0.001953125, 0.03125);value = value + offset.x;vec2 coord = (clamp(texCoord, 0.125, 1.0 - 0.125001) - 0.125) * 4.0;vec2 frac = fract(coord);coord = floor(coord);float p00 = float(coord.y * 4.0 + coord.x) * 0.0625 + offset.y;float p01 = float(coord.y * 4.0 + coord.x + 1.0) * 0.0625 + offset.y;float p10 = float((coord.y + 1.0) * 4.0 + coord.x) * 0.0625 + offset.y;float p11 = float((coord.y + 1.0) * 4.0 + coord.x + 1.0) * 0.0625 + offset.y;vec3 c00 = texture2D(inputImageTexture2, vec2(value, p00)).rgb;vec3 c01 = texture2D(inputImageTexture2, vec2(value, p01)).rgb;vec3 c10 = texture2D(inputImageTexture2, vec2(value, p10)).rgb;vec3 c11 = texture2D(inputImageTexture2, vec2(value, p11)).rgb;float c1 = ((c00.r - c00.g) / (c00.b - c00.g));float c2 = ((c01.r - c01.g) / (c01.b - c01.g));float c3 = ((c10.r - c10.g) / (c10.b - c10.g));float c4 = ((c11.r - c11.g) / (c11.b - c11.g));float c1_2 = mix(c1, c2, frac.x);float c3_4 = mix(c3, c4, frac.x);return mix(c1_2, c3_4, frac.y);}vec3 hsv_to_rgb(vec3 c) {vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);}void main() {vec4 texel = texture2D(sourceImage, texCoord);vec4 hsv = texel;hsv.y = min(1.0, hsv.y * 1.2);hsv.z = min(1.0, enhance(hsv.z) * 1.1);gl_FragColor = vec4(hsv_to_rgb(mix(texel.xyz, hsv.xyz, intensity)), texel.w);}");
                                                if (var3 != 0 && var4 != 0) {
                                                   this.enhanceShaderProgram = GLES20.glCreateProgram();
                                                   GLES20.glAttachShader(this.enhanceShaderProgram, var3);
                                                   GLES20.glAttachShader(this.enhanceShaderProgram, var4);
                                                   GLES20.glBindAttribLocation(this.enhanceShaderProgram, 0, "position");
                                                   GLES20.glBindAttribLocation(this.enhanceShaderProgram, 1, "inputTexCoord");
                                                   GLES20.glLinkProgram(this.enhanceShaderProgram);
                                                   var13 = new int[1];
                                                   GLES20.glGetProgramiv(this.enhanceShaderProgram, 35714, var13, 0);
                                                   if (var13[0] == 0) {
                                                      GLES20.glDeleteProgram(this.enhanceShaderProgram);
                                                      this.enhanceShaderProgram = 0;
                                                   } else {
                                                      this.enhancePositionHandle = GLES20.glGetAttribLocation(this.enhanceShaderProgram, "position");
                                                      this.enhanceInputTexCoordHandle = GLES20.glGetAttribLocation(this.enhanceShaderProgram, "inputTexCoord");
                                                      this.enhanceSourceImageHandle = GLES20.glGetUniformLocation(this.enhanceShaderProgram, "sourceImage");
                                                      this.enhanceIntensityHandle = GLES20.glGetUniformLocation(this.enhanceShaderProgram, "intensity");
                                                      this.enhanceInputImageTexture2Handle = GLES20.glGetUniformLocation(this.enhanceShaderProgram, "inputImageTexture2");
                                                   }

                                                   var3 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
                                                   var4 = this.loadShader(35632, "varying highp vec2 texCoord;uniform sampler2D sourceImage;void main() {gl_FragColor = texture2D(sourceImage, texCoord);}");
                                                   if (var3 != 0 && var4 != 0) {
                                                      this.simpleShaderProgram = GLES20.glCreateProgram();
                                                      GLES20.glAttachShader(this.simpleShaderProgram, var3);
                                                      GLES20.glAttachShader(this.simpleShaderProgram, var4);
                                                      GLES20.glBindAttribLocation(this.simpleShaderProgram, 0, "position");
                                                      GLES20.glBindAttribLocation(this.simpleShaderProgram, 1, "inputTexCoord");
                                                      GLES20.glLinkProgram(this.simpleShaderProgram);
                                                      var13 = new int[1];
                                                      GLES20.glGetProgramiv(this.simpleShaderProgram, 35714, var13, 0);
                                                      if (var13[0] == 0) {
                                                         GLES20.glDeleteProgram(this.simpleShaderProgram);
                                                         this.simpleShaderProgram = 0;
                                                      } else {
                                                         this.simplePositionHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, "position");
                                                         this.simpleInputTexCoordHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, "inputTexCoord");
                                                         this.simpleSourceImageHandle = GLES20.glGetUniformLocation(this.simpleShaderProgram, "sourceImage");
                                                      }

                                                      Bitmap var14 = this.currentBitmap;
                                                      if (var14 != null && !var14.isRecycled()) {
                                                         this.loadTexture(this.currentBitmap);
                                                      }

                                                      return true;
                                                   } else {
                                                      this.finish();
                                                      return false;
                                                   }
                                                } else {
                                                   this.finish();
                                                   return false;
                                                }
                                             } else {
                                                this.finish();
                                                return false;
                                             }
                                          } else {
                                             this.finish();
                                             return false;
                                          }
                                       } else {
                                          this.finish();
                                          return false;
                                       }
                                    } else {
                                       this.finish();
                                       return false;
                                    }
                                 } else {
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
                              var8 = new StringBuilder();
                              var8.append("createWindowSurface failed ");
                              var8.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                              FileLog.e(var8.toString());
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

      private int loadShader(int var1, String var2) {
         int var3 = GLES20.glCreateShader(var1);
         GLES20.glShaderSource(var3, var2);
         GLES20.glCompileShader(var3);
         int[] var4 = new int[1];
         GLES20.glGetShaderiv(var3, 35713, var4, 0);
         var1 = var3;
         if (var4[0] == 0) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e(GLES20.glGetShaderInfoLog(var3));
            }

            GLES20.glDeleteShader(var3);
            var1 = 0;
         }

         return var1;
      }

      private void loadTexture(Bitmap var1) {
         this.renderBufferWidth = var1.getWidth();
         this.renderBufferHeight = var1.getHeight();
         float var2 = (float)AndroidUtilities.getPhotoSize();
         if ((float)this.renderBufferWidth > var2 || (float)this.renderBufferHeight > var2 || PhotoFilterView.this.orientation % 360 != 0) {
            float var3 = 1.0F;
            if ((float)this.renderBufferWidth > var2 || (float)this.renderBufferHeight > var2) {
               var3 = var2 / (float)var1.getWidth();
               float var4 = var2 / (float)var1.getHeight();
               if (var3 < var4) {
                  this.renderBufferWidth = (int)var2;
                  this.renderBufferHeight = (int)((float)var1.getHeight() * var3);
               } else {
                  this.renderBufferHeight = (int)var2;
                  this.renderBufferWidth = (int)((float)var1.getWidth() * var4);
                  var3 = var4;
               }
            }

            if (PhotoFilterView.this.orientation % 360 == 90 || PhotoFilterView.this.orientation % 360 == 270) {
               int var5 = this.renderBufferWidth;
               this.renderBufferWidth = this.renderBufferHeight;
               this.renderBufferHeight = var5;
            }

            this.currentBitmap = this.createBitmap(var1, this.renderBufferWidth, this.renderBufferHeight, var3);
         }

         GLES20.glGenFramebuffers(3, this.renderFrameBuffer, 0);
         GLES20.glGenTextures(3, this.renderTexture, 0);
         GLES20.glBindTexture(3553, this.renderTexture[0]);
         GLES20.glTexParameteri(3553, 10241, 9729);
         GLES20.glTexParameteri(3553, 10240, 9729);
         GLES20.glTexParameteri(3553, 10242, 33071);
         GLES20.glTexParameteri(3553, 10243, 33071);
         GLES20.glTexImage2D(3553, 0, 6408, this.renderBufferWidth, this.renderBufferHeight, 0, 6408, 5121, (Buffer)null);
         GLES20.glBindTexture(3553, this.renderTexture[1]);
         GLES20.glTexParameteri(3553, 10241, 9729);
         GLES20.glTexParameteri(3553, 10240, 9729);
         GLES20.glTexParameteri(3553, 10242, 33071);
         GLES20.glTexParameteri(3553, 10243, 33071);
         GLUtils.texImage2D(3553, 0, this.currentBitmap, 0);
         GLES20.glBindTexture(3553, this.renderTexture[2]);
         GLES20.glTexParameteri(3553, 10241, 9729);
         GLES20.glTexParameteri(3553, 10240, 9729);
         GLES20.glTexParameteri(3553, 10242, 33071);
         GLES20.glTexParameteri(3553, 10243, 33071);
         GLES20.glTexImage2D(3553, 0, 6408, this.renderBufferWidth, this.renderBufferHeight, 0, 6408, 5121, (Buffer)null);
      }

      public void finish() {
         EGLDisplay var2;
         if (this.eglSurface != null) {
            EGL10 var1 = this.egl10;
            var2 = this.eglDisplay;
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

         var2 = this.eglDisplay;
         if (var2 != null) {
            this.egl10.eglTerminate(var2);
            this.eglDisplay = null;
         }

      }

      public Bitmap getTexture() {
         if (!this.initied) {
            return null;
         } else {
            CountDownLatch var1 = new CountDownLatch(1);
            Bitmap[] var2 = new Bitmap[1];

            try {
               _$$Lambda$PhotoFilterView$EGLThread$mV7yxJKIkka2xtURA3o1r9_1nD8 var3 = new _$$Lambda$PhotoFilterView$EGLThread$mV7yxJKIkka2xtURA3o1r9_1nD8(this, var2, var1);
               this.postRunnable(var3);
               var1.await();
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }

            return var2[0];
         }
      }

      // $FF: synthetic method
      public void lambda$getTexture$0$PhotoFilterView$EGLThread(Bitmap[] var1, CountDownLatch var2) {
         GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[1]);
         GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[1 ^ this.blured], 0);
         GLES20.glClear(0);
         var1[0] = this.getRenderBufferBitmap();
         var2.countDown();
         GLES20.glBindFramebuffer(36160, 0);
         GLES20.glClear(0);
      }

      // $FF: synthetic method
      public void lambda$requestRender$2$PhotoFilterView$EGLThread(boolean var1, boolean var2) {
         if (!this.needUpdateBlurTexture) {
            this.needUpdateBlurTexture = var1;
         }

         long var3 = System.currentTimeMillis();
         if (var2 || Math.abs(this.lastRenderCallTime - var3) > 30L) {
            this.lastRenderCallTime = var3;
            this.drawRunnable.run();
         }

      }

      // $FF: synthetic method
      public void lambda$shutdown$1$PhotoFilterView$EGLThread() {
         this.finish();
         this.currentBitmap = null;
         Looper var1 = Looper.myLooper();
         if (var1 != null) {
            var1.quit();
         }

      }

      public void requestRender(boolean var1) {
         this.requestRender(var1, false);
      }

      public void requestRender(boolean var1, boolean var2) {
         this.postRunnable(new _$$Lambda$PhotoFilterView$EGLThread$MrJUzcEqH3ADgrrNUt03oFMBD_4(this, var1, var2));
      }

      public void run() {
         this.initied = this.initGL();
         super.run();
      }

      public void setSurfaceTextureSize(int var1, int var2) {
         this.surfaceWidth = var1;
         this.surfaceHeight = var2;
      }

      public void shutdown() {
         this.postRunnable(new _$$Lambda$PhotoFilterView$EGLThread$UPEak5EYsUntGACDTA79Biksn_k(this));
      }
   }

   public class ToolsAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ToolsAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return 13;
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         return var1 != PhotoFilterView.this.tintShadowsTool && var1 != PhotoFilterView.this.tintHighlightsTool ? 0 : 1;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$PhotoFilterView$ToolsAdapter(int var1, int var2) {
         if (var1 == PhotoFilterView.this.enhanceTool) {
            PhotoFilterView.this.enhanceValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.highlightsTool) {
            PhotoFilterView.this.highlightsValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.contrastTool) {
            PhotoFilterView.this.contrastValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.exposureTool) {
            PhotoFilterView.this.exposureValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.warmthTool) {
            PhotoFilterView.this.warmthValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.saturationTool) {
            PhotoFilterView.this.saturationValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.vignetteTool) {
            PhotoFilterView.this.vignetteValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.shadowsTool) {
            PhotoFilterView.this.shadowsValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.grainTool) {
            PhotoFilterView.this.grainValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.sharpenTool) {
            PhotoFilterView.this.sharpenValue = (float)var2;
         } else if (var1 == PhotoFilterView.this.fadeTool) {
            PhotoFilterView.this.fadeValue = (float)var2;
         }

         if (PhotoFilterView.this.eglThread != null) {
            PhotoFilterView.this.eglThread.requestRender(true);
         }

      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$1$PhotoFilterView$ToolsAdapter(View var1) {
         PhotoEditRadioCell var2 = (PhotoEditRadioCell)var1;
         if ((Integer)var2.getTag() == PhotoFilterView.this.tintShadowsTool) {
            PhotoFilterView.this.tintShadowsColor = var2.getCurrentColor();
         } else {
            PhotoFilterView.this.tintHighlightsColor = var2.getCurrentColor();
         }

         if (PhotoFilterView.this.eglThread != null) {
            PhotoFilterView.this.eglThread.requestRender(false);
         }

      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 == 1) {
               PhotoEditRadioCell var4 = (PhotoEditRadioCell)var1.itemView;
               var4.setTag(var2);
               if (var2 == PhotoFilterView.this.tintShadowsTool) {
                  var4.setIconAndTextAndValue(LocaleController.getString("TintShadows", 2131560906), 0, PhotoFilterView.this.tintShadowsColor);
               } else if (var2 == PhotoFilterView.this.tintHighlightsTool) {
                  var4.setIconAndTextAndValue(LocaleController.getString("TintHighlights", 2131560905), 0, PhotoFilterView.this.tintHighlightsColor);
               }
            }
         } else {
            PhotoEditToolCell var5 = (PhotoEditToolCell)var1.itemView;
            var5.setTag(var2);
            if (var2 == PhotoFilterView.this.enhanceTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Enhance", 2131559366), PhotoFilterView.this.enhanceValue, 0, 100);
            } else if (var2 == PhotoFilterView.this.highlightsTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Highlights", 2131559638), PhotoFilterView.this.highlightsValue, -100, 100);
            } else if (var2 == PhotoFilterView.this.contrastTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Contrast", 2131559155), PhotoFilterView.this.contrastValue, -100, 100);
            } else if (var2 == PhotoFilterView.this.exposureTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Exposure", 2131559475), PhotoFilterView.this.exposureValue, -100, 100);
            } else if (var2 == PhotoFilterView.this.warmthTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Warmth", 2131561103), PhotoFilterView.this.warmthValue, -100, 100);
            } else if (var2 == PhotoFilterView.this.saturationTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Saturation", 2131560625), PhotoFilterView.this.saturationValue, -100, 100);
            } else if (var2 == PhotoFilterView.this.vignetteTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Vignette", 2131561055), PhotoFilterView.this.vignetteValue, 0, 100);
            } else if (var2 == PhotoFilterView.this.shadowsTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Shadows", 2131560745), PhotoFilterView.this.shadowsValue, -100, 100);
            } else if (var2 == PhotoFilterView.this.grainTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Grain", 2131559598), PhotoFilterView.this.grainValue, 0, 100);
            } else if (var2 == PhotoFilterView.this.sharpenTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Sharpen", 2131560774), PhotoFilterView.this.sharpenValue, 0, 100);
            } else if (var2 == PhotoFilterView.this.fadeTool) {
               var5.setIconAndTextAndValue(LocaleController.getString("Fade", 2131559477), PhotoFilterView.this.fadeValue, 0, 100);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 == 0) {
            var3 = new PhotoEditToolCell(this.mContext);
            ((PhotoEditToolCell)var3).setSeekBarDelegate(new _$$Lambda$PhotoFilterView$ToolsAdapter$y8L3fL76Xdh9BIlSQihYImp1GRU(this));
         } else {
            var3 = new PhotoEditRadioCell(this.mContext);
            ((PhotoEditRadioCell)var3).setOnClickListener(new _$$Lambda$PhotoFilterView$ToolsAdapter$l6jhbYVDCcFuEfXfCcHJenobMmo(this));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }
}
