// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.ViewGroup;
import org.telegram.ui.Cells.PhotoEditToolCell;
import org.telegram.ui.Cells.PhotoEditRadioCell;
import android.os.Looper;
import java.util.concurrent.CountDownLatch;
import android.graphics.Bitmap$Config;
import org.telegram.messenger.Utilities;
import android.graphics.Matrix;
import java.nio.Buffer;
import android.opengl.GLES20;
import org.telegram.messenger.FileLog;
import android.opengl.GLUtils;
import org.telegram.messenger.BuildVars;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGL10;
import org.telegram.messenger.DispatchQueue;
import java.util.ArrayList;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout$LayoutParams;
import android.os.Build$VERSION;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View$OnClickListener;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.widget.LinearLayout;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.SurfaceTexture;
import android.view.TextureView$SurfaceTextureListener;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.content.Context;
import android.view.TextureView;
import org.telegram.messenger.MediaController;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.annotation.SuppressLint;
import android.widget.FrameLayout;

@SuppressLint({ "NewApi" })
public class PhotoFilterView extends FrameLayout
{
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
    private int contrastTool;
    private float contrastValue;
    private ImageView curveItem;
    private FrameLayout curveLayout;
    private RadioButton[] curveRadioButton;
    private PhotoFilterCurvesControl curvesControl;
    private CurvesToolValue curvesToolValue;
    private TextView doneTextView;
    private EGLThread eglThread;
    private int enhanceTool;
    private float enhanceValue;
    private int exposureTool;
    private float exposureValue;
    private int fadeTool;
    private float fadeValue;
    private int grainTool;
    private float grainValue;
    private int highlightsTool;
    private float highlightsValue;
    private MediaController.SavedFilterState lastState;
    private int orientation;
    private RecyclerListView recyclerListView;
    private int saturationTool;
    private float saturationValue;
    private int selectedTool;
    private int shadowsTool;
    private float shadowsValue;
    private int sharpenTool;
    private float sharpenValue;
    private boolean showOriginal;
    private TextureView textureView;
    private int tintHighlightsColor;
    private int tintHighlightsTool;
    private int tintShadowsColor;
    private int tintShadowsTool;
    private FrameLayout toolsView;
    private ImageView tuneItem;
    private int vignetteTool;
    private float vignetteValue;
    private int warmthTool;
    private float warmthValue;
    
    public PhotoFilterView(final Context context, final Bitmap bitmapToEdit, int i, final MediaController.SavedFilterState lastState) {
        super(context);
        this.enhanceTool = 0;
        this.exposureTool = 1;
        this.contrastTool = 2;
        this.saturationTool = 3;
        this.warmthTool = 4;
        this.fadeTool = 5;
        this.highlightsTool = 6;
        this.shadowsTool = 7;
        this.vignetteTool = 8;
        this.grainTool = 9;
        this.sharpenTool = 10;
        this.tintShadowsTool = 11;
        this.tintHighlightsTool = 12;
        this.curveRadioButton = new RadioButton[4];
        if (lastState != null) {
            this.enhanceValue = lastState.enhanceValue;
            this.exposureValue = lastState.exposureValue;
            this.contrastValue = lastState.contrastValue;
            this.warmthValue = lastState.warmthValue;
            this.saturationValue = lastState.saturationValue;
            this.fadeValue = lastState.fadeValue;
            this.tintShadowsColor = lastState.tintShadowsColor;
            this.tintHighlightsColor = lastState.tintHighlightsColor;
            this.highlightsValue = lastState.highlightsValue;
            this.shadowsValue = lastState.shadowsValue;
            this.vignetteValue = lastState.vignetteValue;
            this.grainValue = lastState.grainValue;
            this.blurType = lastState.blurType;
            this.sharpenValue = lastState.sharpenValue;
            this.curvesToolValue = lastState.curvesToolValue;
            this.blurExcludeSize = lastState.blurExcludeSize;
            this.blurExcludePoint = lastState.blurExcludePoint;
            this.blurExcludeBlurSize = lastState.blurExcludeBlurSize;
            this.blurAngle = lastState.blurAngle;
            this.lastState = lastState;
        }
        else {
            this.curvesToolValue = new CurvesToolValue();
            this.blurExcludeSize = 0.35f;
            this.blurExcludePoint = new Point(0.5f, 0.5f);
            this.blurExcludeBlurSize = 0.15f;
            this.blurAngle = 1.5707964f;
        }
        this.bitmapToEdit = bitmapToEdit;
        this.orientation = i;
        this.addView((View)(this.textureView = new TextureView(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.textureView.setVisibility(4);
        this.textureView.setSurfaceTextureListener((TextureView$SurfaceTextureListener)new TextureView$SurfaceTextureListener() {
            public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
                if (PhotoFilterView.this.eglThread == null && surfaceTexture != null) {
                    final PhotoFilterView this$0 = PhotoFilterView.this;
                    this$0.eglThread = this$0.new EGLThread(surfaceTexture, this$0.bitmapToEdit);
                    PhotoFilterView.this.eglThread.setSurfaceTextureSize(n, n2);
                    PhotoFilterView.this.eglThread.requestRender(true, true);
                }
            }
            
            public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
                if (PhotoFilterView.this.eglThread != null) {
                    PhotoFilterView.this.eglThread.shutdown();
                    PhotoFilterView.this.eglThread = null;
                }
                return true;
            }
            
            public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
                if (PhotoFilterView.this.eglThread != null) {
                    PhotoFilterView.this.eglThread.setSurfaceTextureSize(n, n2);
                    PhotoFilterView.this.eglThread.requestRender(false, true);
                    PhotoFilterView.this.eglThread.postRunnable(new _$$Lambda$PhotoFilterView$1$ykukh__An3a5DPXda05svw9B55U(this));
                }
            }
            
            public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
            }
        });
        (this.blurControl = new PhotoFilterBlurControl(context)).setVisibility(4);
        this.addView((View)this.blurControl, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.blurControl.setDelegate((PhotoFilterBlurControl.PhotoFilterLinearBlurControlDelegate)new _$$Lambda$PhotoFilterView$wsvuAfZFAJpyt9V1ZYZMPETINLA(this));
        (this.curvesControl = new PhotoFilterCurvesControl(context, this.curvesToolValue)).setDelegate((PhotoFilterCurvesControl.PhotoFilterCurvesControlDelegate)new _$$Lambda$PhotoFilterView$Q8Q0QxhBgkn0x_QyOvZIPgaDQJM(this));
        this.curvesControl.setVisibility(4);
        this.addView((View)this.curvesControl, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.addView((View)(this.toolsView = new FrameLayout(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 186, 83));
        final FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(-16777216);
        this.toolsView.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        (this.cancelTextView = new TextView(context)).setTextSize(1, 14.0f);
        this.cancelTextView.setTextColor(-1);
        this.cancelTextView.setGravity(17);
        this.cancelTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
        this.cancelTextView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.cancelTextView.setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
        this.cancelTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView((View)this.cancelTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
        (this.doneTextView = new TextView(context)).setTextSize(1, 14.0f);
        this.doneTextView.setTextColor(-11420173);
        this.doneTextView.setGravity(17);
        this.doneTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
        this.doneTextView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.doneTextView.setText((CharSequence)LocaleController.getString("Done", 2131559299).toUpperCase());
        this.doneTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView((View)this.doneTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 53));
        final LinearLayout linearLayout = new LinearLayout(context);
        frameLayout.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 1));
        (this.tuneItem = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.tuneItem.setImageResource(2131165751);
        this.tuneItem.setColorFilter((ColorFilter)new PorterDuffColorFilter(-9649153, PorterDuff$Mode.MULTIPLY));
        this.tuneItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView((View)this.tuneItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(56, 48));
        this.tuneItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoFilterView$4hF97H88dpzJN2DxB_cny05r_QI(this));
        (this.blurItem = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.blurItem.setImageResource(2131165882);
        this.blurItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView((View)this.blurItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(56, 48));
        this.blurItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoFilterView$DKhAjcvz5psQCw7kBhsHKlWIYQM(this));
        (this.curveItem = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.curveItem.setImageResource(2131165884);
        this.curveItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView((View)this.curveItem, (ViewGroup$LayoutParams)LayoutHelper.createLinear(56, 48));
        this.curveItem.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoFilterView$F_KOwGrCZ7lau2u49edpZx3V5ug(this));
        this.recyclerListView = new RecyclerListView(context);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(1);
        this.recyclerListView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
        this.recyclerListView.setClipToPadding(false);
        this.recyclerListView.setOverScrollMode(2);
        this.recyclerListView.setAdapter(new ToolsAdapter(context));
        this.toolsView.addView((View)this.recyclerListView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 120, 51));
        (this.curveLayout = new FrameLayout(context)).setVisibility(4);
        this.toolsView.addView((View)this.curveLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 78.0f, 1, 0.0f, 40.0f, 0.0f, 0.0f));
        final LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        this.curveLayout.addView((View)linearLayout2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 1));
        FrameLayout frameLayout2;
        TextView textView;
        String string;
        StringBuilder sb;
        String string2;
        StringBuilder sb2;
        String string3;
        StringBuilder sb3;
        String string4;
        StringBuilder sb4;
        float n;
        for (i = 0; i < 4; ++i) {
            frameLayout2 = new FrameLayout(context);
            frameLayout2.setTag((Object)i);
            (this.curveRadioButton[i] = new RadioButton(context)).setSize(AndroidUtilities.dp(20.0f));
            frameLayout2.addView((View)this.curveRadioButton[i], (ViewGroup$LayoutParams)LayoutHelper.createFrame(30, 30, 49));
            textView = new TextView(context);
            textView.setTextSize(1, 12.0f);
            textView.setGravity(16);
            if (i == 0) {
                string = LocaleController.getString("CurvesAll", 2131559182);
                sb = new StringBuilder();
                sb.append(string.substring(0, 1).toUpperCase());
                sb.append(string.substring(1).toLowerCase());
                textView.setText((CharSequence)sb.toString());
                textView.setTextColor(-1);
                this.curveRadioButton[i].setColor(-1, -1);
            }
            else if (i == 1) {
                string2 = LocaleController.getString("CurvesRed", 2131559185);
                sb2 = new StringBuilder();
                sb2.append(string2.substring(0, 1).toUpperCase());
                sb2.append(string2.substring(1).toLowerCase());
                textView.setText((CharSequence)sb2.toString());
                textView.setTextColor(-1684147);
                this.curveRadioButton[i].setColor(-1684147, -1684147);
            }
            else if (i == 2) {
                string3 = LocaleController.getString("CurvesGreen", 2131559184);
                sb3 = new StringBuilder();
                sb3.append(string3.substring(0, 1).toUpperCase());
                sb3.append(string3.substring(1).toLowerCase());
                textView.setText((CharSequence)sb3.toString());
                textView.setTextColor(-10831009);
                this.curveRadioButton[i].setColor(-10831009, -10831009);
            }
            else if (i == 3) {
                string4 = LocaleController.getString("CurvesBlue", 2131559183);
                sb4 = new StringBuilder();
                sb4.append(string4.substring(0, 1).toUpperCase());
                sb4.append(string4.substring(1).toLowerCase());
                textView.setText((CharSequence)sb4.toString());
                textView.setTextColor(-12734994);
                this.curveRadioButton[i].setColor(-12734994, -12734994);
            }
            frameLayout2.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 38.0f, 0.0f, 0.0f));
            if (i == 0) {
                n = 0.0f;
            }
            else {
                n = 30.0f;
            }
            linearLayout2.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n, 0.0f, 0.0f, 0.0f));
            frameLayout2.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoFilterView$L20A01o2AsZwohbyfeeuMPbGEQI(this));
        }
        (this.blurLayout = new FrameLayout(context)).setVisibility(4);
        this.toolsView.addView((View)this.blurLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(280, 60.0f, 1, 0.0f, 40.0f, 0.0f, 0.0f));
        (this.blurOffButton = new TextView(context)).setCompoundDrawablePadding(AndroidUtilities.dp(2.0f));
        this.blurOffButton.setTextSize(1, 13.0f);
        this.blurOffButton.setGravity(1);
        this.blurOffButton.setText((CharSequence)LocaleController.getString("BlurOff", 2131558845));
        this.blurLayout.addView((View)this.blurOffButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(80, 60.0f));
        this.blurOffButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoFilterView$EHBGA4T_Wcgx03trAGzstG0gXqg(this));
        (this.blurRadialButton = new TextView(context)).setCompoundDrawablePadding(AndroidUtilities.dp(2.0f));
        this.blurRadialButton.setTextSize(1, 13.0f);
        this.blurRadialButton.setGravity(1);
        this.blurRadialButton.setText((CharSequence)LocaleController.getString("BlurRadial", 2131558846));
        this.blurLayout.addView((View)this.blurRadialButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(80, 80.0f, 51, 100.0f, 0.0f, 0.0f, 0.0f));
        this.blurRadialButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoFilterView$SHP9jNXWESNEp5KZC_qRcZYRL1Y(this));
        (this.blurLinearButton = new TextView(context)).setCompoundDrawablePadding(AndroidUtilities.dp(2.0f));
        this.blurLinearButton.setTextSize(1, 13.0f);
        this.blurLinearButton.setGravity(1);
        this.blurLinearButton.setText((CharSequence)LocaleController.getString("BlurLinear", 2131558844));
        this.blurLayout.addView((View)this.blurLinearButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(80, 80.0f, 51, 200.0f, 0.0f, 0.0f, 0.0f));
        this.blurLinearButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoFilterView$TdTpIDy0jwl_mIhu0bpchkPiKg8(this));
        this.updateSelectedBlurType();
        if (Build$VERSION.SDK_INT >= 21) {
            ((FrameLayout$LayoutParams)this.textureView.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
            ((FrameLayout$LayoutParams)this.curvesControl.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
        }
    }
    
    private void fixLayout(int width, int n) {
        if (this.bitmapToEdit == null) {
            return;
        }
        final int n2 = width - AndroidUtilities.dp(28.0f);
        final int dp = AndroidUtilities.dp(214.0f);
        if (Build$VERSION.SDK_INT >= 21) {
            width = AndroidUtilities.statusBarHeight;
        }
        else {
            width = 0;
        }
        n -= dp + width;
        width = this.orientation;
        float n3;
        if (width % 360 != 90 && width % 360 != 270) {
            n3 = (float)this.bitmapToEdit.getWidth();
            width = this.bitmapToEdit.getHeight();
        }
        else {
            n3 = (float)this.bitmapToEdit.getHeight();
            width = this.bitmapToEdit.getWidth();
        }
        final float n4 = (float)width;
        final float n5 = (float)n2;
        final float n6 = n5 / n3;
        final float n7 = (float)n;
        final float n8 = n7 / n4;
        float n9;
        float n10;
        if (n6 > n8) {
            n9 = (float)(int)Math.ceil(n3 * n8);
            n10 = n7;
        }
        else {
            n10 = (float)(int)Math.ceil(n4 * n6);
            n9 = n5;
        }
        final int leftMargin = (int)Math.ceil((n5 - n9) / 2.0f + AndroidUtilities.dp(14.0f));
        final float n11 = (n7 - n10) / 2.0f;
        final float n12 = (float)AndroidUtilities.dp(14.0f);
        if (Build$VERSION.SDK_INT >= 21) {
            width = AndroidUtilities.statusBarHeight;
        }
        else {
            width = 0;
        }
        final int topMargin = (int)Math.ceil(n11 + n12 + width);
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.textureView.getLayoutParams();
        frameLayout$LayoutParams.leftMargin = leftMargin;
        frameLayout$LayoutParams.topMargin = topMargin;
        frameLayout$LayoutParams.width = (int)n9;
        frameLayout$LayoutParams.height = (int)n10;
        final PhotoFilterCurvesControl curvesControl = this.curvesControl;
        final float n13 = (float)leftMargin;
        if (Build$VERSION.SDK_INT >= 21) {
            width = AndroidUtilities.statusBarHeight;
        }
        else {
            width = 0;
        }
        curvesControl.setActualArea(n13, (float)(topMargin - width), (float)frameLayout$LayoutParams.width, (float)frameLayout$LayoutParams.height);
        this.blurControl.setActualAreaSize((float)frameLayout$LayoutParams.width, (float)frameLayout$LayoutParams.height);
        ((FrameLayout$LayoutParams)this.blurControl.getLayoutParams()).height = AndroidUtilities.dp(38.0f) + n;
        ((FrameLayout$LayoutParams)this.curvesControl.getLayoutParams()).height = n + AndroidUtilities.dp(28.0f);
        if (AndroidUtilities.isTablet()) {
            width = AndroidUtilities.dp(86.0f) * 10;
            final FrameLayout$LayoutParams frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.recyclerListView.getLayoutParams();
            if (width < n2) {
                frameLayout$LayoutParams2.width = width;
                frameLayout$LayoutParams2.leftMargin = (n2 - width) / 2;
            }
            else {
                frameLayout$LayoutParams2.width = -1;
                frameLayout$LayoutParams2.leftMargin = 0;
            }
        }
    }
    
    private float getContrastValue() {
        return this.contrastValue / 100.0f * 0.3f + 1.0f;
    }
    
    private float getEnhanceValue() {
        return this.enhanceValue / 100.0f;
    }
    
    private float getExposureValue() {
        return this.exposureValue / 100.0f;
    }
    
    private float getFadeValue() {
        return this.fadeValue / 100.0f;
    }
    
    private float getGrainValue() {
        return this.grainValue / 100.0f * 0.04f;
    }
    
    private float getHighlightsValue() {
        return (this.highlightsValue * 0.75f + 100.0f) / 100.0f;
    }
    
    private float getSaturationValue() {
        float n2;
        final float n = n2 = this.saturationValue / 100.0f;
        if (n > 0.0f) {
            n2 = n * 1.05f;
        }
        return n2 + 1.0f;
    }
    
    private float getShadowsValue() {
        return (this.shadowsValue * 0.55f + 100.0f) / 100.0f;
    }
    
    private float getSharpenValue() {
        return this.sharpenValue / 100.0f * 0.6f + 0.11f;
    }
    
    private float getTintHighlightsIntensityValue() {
        float n;
        if (this.tintHighlightsColor == 0) {
            n = 0.0f;
        }
        else {
            n = 0.5f;
        }
        return n;
    }
    
    private float getTintShadowsIntensityValue() {
        float n;
        if (this.tintShadowsColor == 0) {
            n = 0.0f;
        }
        else {
            n = 0.5f;
        }
        return n;
    }
    
    private float getVignetteValue() {
        return this.vignetteValue / 100.0f;
    }
    
    private float getWarmthValue() {
        return this.warmthValue / 100.0f;
    }
    
    private void setShowOriginal(final boolean showOriginal) {
        if (this.showOriginal == showOriginal) {
            return;
        }
        this.showOriginal = showOriginal;
        final EGLThread eglThread = this.eglThread;
        if (eglThread != null) {
            eglThread.requestRender(false);
        }
    }
    
    private void updateSelectedBlurType() {
        final int blurType = this.blurType;
        if (blurType == 0) {
            final Drawable mutate = this.blurOffButton.getContext().getResources().getDrawable(2131165308).mutate();
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-11420173, PorterDuff$Mode.MULTIPLY));
            this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, mutate, (Drawable)null, (Drawable)null);
            this.blurOffButton.setTextColor(-11420173);
            this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165309, 0, 0);
            this.blurRadialButton.setTextColor(-1);
            this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165307, 0, 0);
            this.blurLinearButton.setTextColor(-1);
        }
        else if (blurType == 1) {
            this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165308, 0, 0);
            this.blurOffButton.setTextColor(-1);
            final Drawable mutate2 = this.blurOffButton.getContext().getResources().getDrawable(2131165309).mutate();
            mutate2.setColorFilter((ColorFilter)new PorterDuffColorFilter(-11420173, PorterDuff$Mode.MULTIPLY));
            this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, mutate2, (Drawable)null, (Drawable)null);
            this.blurRadialButton.setTextColor(-11420173);
            this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165307, 0, 0);
            this.blurLinearButton.setTextColor(-1);
        }
        else if (blurType == 2) {
            this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165308, 0, 0);
            this.blurOffButton.setTextColor(-1);
            this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds(0, 2131165309, 0, 0);
            this.blurRadialButton.setTextColor(-1);
            final Drawable mutate3 = this.blurOffButton.getContext().getResources().getDrawable(2131165307).mutate();
            mutate3.setColorFilter((ColorFilter)new PorterDuffColorFilter(-11420173, PorterDuff$Mode.MULTIPLY));
            this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, mutate3, (Drawable)null, (Drawable)null);
            this.blurLinearButton.setTextColor(-11420173);
        }
    }
    
    public Bitmap getBitmap() {
        final EGLThread eglThread = this.eglThread;
        Bitmap texture;
        if (eglThread != null) {
            texture = eglThread.getTexture();
        }
        else {
            texture = null;
        }
        return texture;
    }
    
    public TextView getCancelTextView() {
        return this.cancelTextView;
    }
    
    public TextView getDoneTextView() {
        return this.doneTextView;
    }
    
    public MediaController.SavedFilterState getSavedFilterState() {
        final MediaController.SavedFilterState savedFilterState = new MediaController.SavedFilterState();
        savedFilterState.enhanceValue = this.enhanceValue;
        savedFilterState.exposureValue = this.exposureValue;
        savedFilterState.contrastValue = this.contrastValue;
        savedFilterState.warmthValue = this.warmthValue;
        savedFilterState.saturationValue = this.saturationValue;
        savedFilterState.fadeValue = this.fadeValue;
        savedFilterState.tintShadowsColor = this.tintShadowsColor;
        savedFilterState.tintHighlightsColor = this.tintHighlightsColor;
        savedFilterState.highlightsValue = this.highlightsValue;
        savedFilterState.shadowsValue = this.shadowsValue;
        savedFilterState.vignetteValue = this.vignetteValue;
        savedFilterState.grainValue = this.grainValue;
        savedFilterState.blurType = this.blurType;
        savedFilterState.sharpenValue = this.sharpenValue;
        savedFilterState.curvesToolValue = this.curvesToolValue;
        savedFilterState.blurExcludeSize = this.blurExcludeSize;
        savedFilterState.blurExcludePoint = this.blurExcludePoint;
        savedFilterState.blurExcludeBlurSize = this.blurExcludeBlurSize;
        savedFilterState.blurAngle = this.blurAngle;
        return savedFilterState;
    }
    
    public FrameLayout getToolsView() {
        return this.toolsView;
    }
    
    public boolean hasChanges() {
        final MediaController.SavedFilterState lastState = this.lastState;
        final boolean b = false;
        boolean b2 = false;
        if (lastState != null) {
            if (this.enhanceValue != lastState.enhanceValue || this.contrastValue != lastState.contrastValue || this.highlightsValue != lastState.highlightsValue || this.exposureValue != lastState.exposureValue || this.warmthValue != lastState.warmthValue || this.saturationValue != lastState.saturationValue || this.vignetteValue != lastState.vignetteValue || this.shadowsValue != lastState.shadowsValue || this.grainValue != lastState.grainValue || this.sharpenValue != lastState.sharpenValue || this.fadeValue != lastState.fadeValue || this.tintHighlightsColor != lastState.tintHighlightsColor || this.tintShadowsColor != lastState.tintShadowsColor || !this.curvesToolValue.shouldBeSkipped()) {
                b2 = true;
            }
            return b2;
        }
        if (this.enhanceValue == 0.0f && this.contrastValue == 0.0f && this.highlightsValue == 0.0f && this.exposureValue == 0.0f && this.warmthValue == 0.0f && this.saturationValue == 0.0f && this.vignetteValue == 0.0f && this.shadowsValue == 0.0f && this.grainValue == 0.0f && this.sharpenValue == 0.0f && this.fadeValue == 0.0f && this.tintHighlightsColor == 0 && this.tintShadowsColor == 0) {
            final boolean b3 = b;
            if (this.curvesToolValue.shouldBeSkipped()) {
                return b3;
            }
        }
        return true;
    }
    
    public void init() {
        this.textureView.setVisibility(0);
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.fixLayout(View$MeasureSpec.getSize(n), View$MeasureSpec.getSize(n2));
        super.onMeasure(n, n2);
    }
    
    public void onTouch(final MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0 && motionEvent.getActionMasked() != 5) {
            if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6) {
                this.setShowOriginal(false);
            }
        }
        else {
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.textureView.getLayoutParams();
            if (frameLayout$LayoutParams != null && motionEvent.getX() >= frameLayout$LayoutParams.leftMargin && motionEvent.getY() >= frameLayout$LayoutParams.topMargin && motionEvent.getX() <= frameLayout$LayoutParams.leftMargin + frameLayout$LayoutParams.width && motionEvent.getY() <= frameLayout$LayoutParams.topMargin + frameLayout$LayoutParams.height) {
                this.setShowOriginal(true);
            }
        }
    }
    
    public void shutdown() {
        final EGLThread eglThread = this.eglThread;
        if (eglThread != null) {
            eglThread.shutdown();
            this.eglThread = null;
        }
        this.textureView.setVisibility(8);
    }
    
    public void switchMode() {
        final int selectedTool = this.selectedTool;
        if (selectedTool == 0) {
            this.blurControl.setVisibility(4);
            this.blurLayout.setVisibility(4);
            this.curveLayout.setVisibility(4);
            this.curvesControl.setVisibility(4);
            this.recyclerListView.setVisibility(0);
        }
        else if (selectedTool == 1) {
            this.recyclerListView.setVisibility(4);
            this.curveLayout.setVisibility(4);
            this.curvesControl.setVisibility(4);
            this.blurLayout.setVisibility(0);
            if (this.blurType != 0) {
                this.blurControl.setVisibility(0);
            }
            this.updateSelectedBlurType();
        }
        else if (selectedTool == 2) {
            this.recyclerListView.setVisibility(4);
            this.blurLayout.setVisibility(4);
            this.blurControl.setVisibility(4);
            this.curveLayout.setVisibility(0);
            this.curvesControl.setVisibility(0);
            this.curvesToolValue.activeType = 0;
            for (int i = 0; i < 4; ++i) {
                this.curveRadioButton[i].setChecked(i == 0, false);
            }
        }
    }
    
    public static class CurvesToolValue
    {
        public static final int CurvesTypeBlue = 3;
        public static final int CurvesTypeGreen = 2;
        public static final int CurvesTypeLuminance = 0;
        public static final int CurvesTypeRed = 1;
        public int activeType;
        public CurvesValue blueCurve;
        public ByteBuffer curveBuffer;
        public CurvesValue greenCurve;
        public CurvesValue luminanceCurve;
        public CurvesValue redCurve;
        
        public CurvesToolValue() {
            this.luminanceCurve = new CurvesValue();
            this.redCurve = new CurvesValue();
            this.greenCurve = new CurvesValue();
            this.blueCurve = new CurvesValue();
            (this.curveBuffer = ByteBuffer.allocateDirect(800)).order(ByteOrder.LITTLE_ENDIAN);
        }
        
        public void fillBuffer() {
            this.curveBuffer.position(0);
            final float[] dataPoints = this.luminanceCurve.getDataPoints();
            final float[] dataPoints2 = this.redCurve.getDataPoints();
            final float[] dataPoints3 = this.greenCurve.getDataPoints();
            final float[] dataPoints4 = this.blueCurve.getDataPoints();
            for (int i = 0; i < 200; ++i) {
                this.curveBuffer.put((byte)(dataPoints2[i] * 255.0f));
                this.curveBuffer.put((byte)(dataPoints3[i] * 255.0f));
                this.curveBuffer.put((byte)(dataPoints4[i] * 255.0f));
                this.curveBuffer.put((byte)(dataPoints[i] * 255.0f));
            }
            this.curveBuffer.position(0);
        }
        
        public boolean shouldBeSkipped() {
            return this.luminanceCurve.isDefault() && this.redCurve.isDefault() && this.greenCurve.isDefault() && this.blueCurve.isDefault();
        }
    }
    
    public static class CurvesValue
    {
        public float blacksLevel;
        public float[] cachedDataPoints;
        public float highlightsLevel;
        public float midtonesLevel;
        public float previousBlacksLevel;
        public float previousHighlightsLevel;
        public float previousMidtonesLevel;
        public float previousShadowsLevel;
        public float previousWhitesLevel;
        public float shadowsLevel;
        public float whitesLevel;
        
        public CurvesValue() {
            this.blacksLevel = 0.0f;
            this.shadowsLevel = 25.0f;
            this.midtonesLevel = 50.0f;
            this.highlightsLevel = 75.0f;
            this.whitesLevel = 100.0f;
            this.previousBlacksLevel = 0.0f;
            this.previousShadowsLevel = 25.0f;
            this.previousMidtonesLevel = 50.0f;
            this.previousHighlightsLevel = 75.0f;
            this.previousWhitesLevel = 100.0f;
        }
        
        public float[] getDataPoints() {
            if (this.cachedDataPoints == null) {
                this.interpolateCurve();
            }
            return this.cachedDataPoints;
        }
        
        public float[] interpolateCurve() {
            final float[] array = new float[14];
            array[0] = -0.001f;
            final float blacksLevel = this.blacksLevel;
            array[1] = blacksLevel / 100.0f;
            array[2] = 0.0f;
            array[3] = blacksLevel / 100.0f;
            array[4] = 0.25f;
            array[5] = this.shadowsLevel / 100.0f;
            array[6] = 0.5f;
            array[7] = this.midtonesLevel / 100.0f;
            array[8] = 0.75f;
            array[9] = this.highlightsLevel / 100.0f;
            array[10] = 1.0f;
            final float whitesLevel = this.whitesLevel;
            array[11] = whitesLevel / 100.0f;
            array[12] = 1.001f;
            array[13] = whitesLevel / 100.0f;
            final ArrayList<Float> list = new ArrayList<Float>(100);
            final ArrayList<Float> list2 = new ArrayList<Float>(100);
            list2.add(array[0]);
            list2.add(array[1]);
            int n7;
            for (int i = 1; i < array.length / 2 - 2; i = n7) {
                final int n = (i - 1) * 2;
                final float n2 = array[n];
                final float n3 = array[n + 1];
                final int n4 = i * 2;
                final float n5 = array[n4];
                final float n6 = array[n4 + 1];
                n7 = i + 1;
                final int n8 = n7 * 2;
                final float f = array[n8];
                final float f2 = array[n8 + 1];
                final int n9 = (i + 2) * 2;
                final float n10 = array[n9];
                final float n11 = array[n9 + 1];
                for (int j = 1; j < 100; ++j) {
                    final float n12 = j * 0.01f;
                    final float n13 = n12 * n12;
                    final float n14 = n13 * n12;
                    final float f3 = (n5 * 2.0f + (f - n2) * n12 + (n2 * 2.0f - n5 * 5.0f + f * 4.0f - n10) * n13 + (n5 * 3.0f - n2 - f * 3.0f + n10) * n14) * 0.5f;
                    final float max = Math.max(0.0f, Math.min(1.0f, (n6 * 2.0f + (f2 - n3) * n12 + (2.0f * n3 - 5.0f * n6 + 4.0f * f2 - n11) * n13 + (n6 * 3.0f - n3 - 3.0f * f2 + n11) * n14) * 0.5f));
                    if (f3 > n2) {
                        list2.add(f3);
                        list2.add(max);
                    }
                    if ((j - 1) % 2 == 0) {
                        list.add(max);
                    }
                }
                list2.add(f);
                list2.add(f2);
            }
            list2.add(array[12]);
            list2.add(array[13]);
            this.cachedDataPoints = new float[list.size()];
            int index = 0;
            while (true) {
                final float[] cachedDataPoints = this.cachedDataPoints;
                if (index >= cachedDataPoints.length) {
                    break;
                }
                cachedDataPoints[index] = list.get(index);
                ++index;
            }
            final float[] array2 = new float[list2.size()];
            for (int k = 0; k < array2.length; ++k) {
                array2[k] = list2.get(k);
            }
            return array2;
        }
        
        public boolean isDefault() {
            return Math.abs(this.blacksLevel - 0.0f) < 1.0E-5 && Math.abs(this.shadowsLevel - 25.0f) < 1.0E-5 && Math.abs(this.midtonesLevel - 50.0f) < 1.0E-5 && Math.abs(this.highlightsLevel - 75.0f) < 1.0E-5 && Math.abs(this.whitesLevel - 100.0f) < 1.0E-5;
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
    
    public class EGLThread extends DispatchQueue
    {
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
        private final int EGL_CONTEXT_CLIENT_VERSION;
        private final int EGL_OPENGL_ES2_BIT;
        private int blurHeightHandle;
        private int blurInputTexCoordHandle;
        private int blurPositionHandle;
        private int blurShaderProgram;
        private int blurSourceImageHandle;
        private int blurWidthHandle;
        private boolean blured;
        private int contrastHandle;
        private Bitmap currentBitmap;
        private int[] curveTextures;
        private int curvesImageHandle;
        private Runnable drawRunnable;
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
        private int[] enhanceTextures;
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
        private boolean needUpdateBlurTexture;
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
        private int[] renderFrameBuffer;
        private int[] renderTexture;
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
        
        public EGLThread(final SurfaceTexture surfaceTexture, final Bitmap currentBitmap) {
            super("EGLThread");
            this.EGL_CONTEXT_CLIENT_VERSION = 12440;
            this.EGL_OPENGL_ES2_BIT = 4;
            this.needUpdateBlurTexture = true;
            this.enhanceTextures = new int[2];
            this.renderTexture = new int[3];
            this.renderFrameBuffer = new int[3];
            this.curveTextures = new int[1];
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
                    GLES20.glViewport(0, 0, EGLThread.this.renderBufferWidth, EGLThread.this.renderBufferHeight);
                    EGLThread.this.drawEnhancePass();
                    EGLThread.this.drawSharpenPass();
                    EGLThread.this.drawCustomParamsPass();
                    final EGLThread this$1 = EGLThread.this;
                    this$1.blured = this$1.drawBlurPass();
                    GLES20.glViewport(0, 0, EGLThread.this.surfaceWidth, EGLThread.this.surfaceHeight);
                    GLES20.glBindFramebuffer(36160, 0);
                    GLES20.glClear(0);
                    GLES20.glUseProgram(EGLThread.this.simpleShaderProgram);
                    GLES20.glActiveTexture(33984);
                    GLES20.glBindTexture(3553, EGLThread.this.renderTexture[EGLThread.this.blured ^ true]);
                    GLES20.glUniform1i(EGLThread.this.simpleSourceImageHandle, 0);
                    GLES20.glEnableVertexAttribArray(EGLThread.this.simpleInputTexCoordHandle);
                    GLES20.glVertexAttribPointer(EGLThread.this.simpleInputTexCoordHandle, 2, 5126, false, 8, (Buffer)EGLThread.this.textureBuffer);
                    GLES20.glEnableVertexAttribArray(EGLThread.this.simplePositionHandle);
                    GLES20.glVertexAttribPointer(EGLThread.this.simplePositionHandle, 2, 5126, false, 8, (Buffer)EGLThread.this.vertexBuffer);
                    GLES20.glDrawArrays(5, 0, 4);
                    EGLThread.this.egl10.eglSwapBuffers(EGLThread.this.eglDisplay, EGLThread.this.eglSurface);
                }
            };
            this.surfaceTexture = surfaceTexture;
            this.currentBitmap = currentBitmap;
        }
        
        private Bitmap createBitmap(final Bitmap bitmap, final int n, final int n2, final float n3) {
            final Matrix matrix = new Matrix();
            matrix.setScale(n3, n3);
            matrix.postRotate((float)PhotoFilterView.this.orientation);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        
        private boolean drawBlurPass() {
            if (!PhotoFilterView.this.showOriginal && PhotoFilterView.this.blurType != 0) {
                if (this.needUpdateBlurTexture) {
                    GLES20.glUseProgram(this.blurShaderProgram);
                    GLES20.glUniform1i(this.blurSourceImageHandle, 0);
                    GLES20.glEnableVertexAttribArray(this.blurInputTexCoordHandle);
                    GLES20.glVertexAttribPointer(this.blurInputTexCoordHandle, 2, 5126, false, 8, (Buffer)this.textureBuffer);
                    GLES20.glEnableVertexAttribArray(this.blurPositionHandle);
                    GLES20.glVertexAttribPointer(this.blurPositionHandle, 2, 5126, false, 8, (Buffer)this.vertexInvertBuffer);
                    GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[0]);
                    GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[0], 0);
                    GLES20.glClear(0);
                    GLES20.glActiveTexture(33984);
                    GLES20.glBindTexture(3553, this.renderTexture[1]);
                    GLES20.glUniform1f(this.blurWidthHandle, 0.0f);
                    GLES20.glUniform1f(this.blurHeightHandle, 1.0f / this.renderBufferHeight);
                    GLES20.glDrawArrays(5, 0, 4);
                    GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[2]);
                    GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[2], 0);
                    GLES20.glClear(0);
                    GLES20.glActiveTexture(33984);
                    GLES20.glBindTexture(3553, this.renderTexture[0]);
                    GLES20.glUniform1f(this.blurWidthHandle, 1.0f / this.renderBufferWidth);
                    GLES20.glUniform1f(this.blurHeightHandle, 0.0f);
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
                    GLES20.glUniform1f(this.radialBlurAspectRatioHandle, this.renderBufferHeight / (float)this.renderBufferWidth);
                    GLES20.glEnableVertexAttribArray(this.radialBlurInputTexCoordHandle);
                    GLES20.glVertexAttribPointer(this.radialBlurInputTexCoordHandle, 2, 5126, false, 8, (Buffer)this.textureBuffer);
                    GLES20.glEnableVertexAttribArray(this.radialBlurPositionHandle);
                    GLES20.glVertexAttribPointer(this.radialBlurPositionHandle, 2, 5126, false, 8, (Buffer)this.vertexInvertBuffer);
                }
                else if (PhotoFilterView.this.blurType == 2) {
                    GLES20.glUseProgram(this.linearBlurShaderProgram);
                    GLES20.glUniform1i(this.linearBlurSourceImageHandle, 0);
                    GLES20.glUniform1i(this.linearBlurSourceImage2Handle, 1);
                    GLES20.glUniform1f(this.linearBlurExcludeSizeHandle, PhotoFilterView.this.blurExcludeSize);
                    GLES20.glUniform1f(this.linearBlurExcludeBlurSizeHandle, PhotoFilterView.this.blurExcludeBlurSize);
                    GLES20.glUniform1f(this.linearBlurAngleHandle, PhotoFilterView.this.blurAngle);
                    GLES20.glUniform2f(this.linearBlurExcludePointHandle, PhotoFilterView.this.blurExcludePoint.x, PhotoFilterView.this.blurExcludePoint.y);
                    GLES20.glUniform1f(this.linearBlurAspectRatioHandle, this.renderBufferHeight / (float)this.renderBufferWidth);
                    GLES20.glEnableVertexAttribArray(this.linearBlurInputTexCoordHandle);
                    GLES20.glVertexAttribPointer(this.linearBlurInputTexCoordHandle, 2, 5126, false, 8, (Buffer)this.textureBuffer);
                    GLES20.glEnableVertexAttribArray(this.linearBlurPositionHandle);
                    GLES20.glVertexAttribPointer(this.linearBlurPositionHandle, 2, 5126, false, 8, (Buffer)this.vertexInvertBuffer);
                }
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, this.renderTexture[1]);
                GLES20.glActiveTexture(33985);
                GLES20.glBindTexture(3553, this.renderTexture[2]);
                GLES20.glDrawArrays(5, 0, 4);
                return true;
            }
            return false;
        }
        
        private void drawCustomParamsPass() {
            GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[1]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[1], 0);
            GLES20.glClear(0);
            GLES20.glUseProgram(this.toolsShaderProgram);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.renderTexture[0]);
            GLES20.glUniform1i(this.sourceImageHandle, 0);
            final boolean access$000 = PhotoFilterView.this.showOriginal;
            float n = 1.0f;
            if (access$000) {
                GLES20.glUniform1f(this.shadowsHandle, 1.0f);
                GLES20.glUniform1f(this.highlightsHandle, 1.0f);
                GLES20.glUniform1f(this.exposureHandle, 0.0f);
                GLES20.glUniform1f(this.contrastHandle, 1.0f);
                GLES20.glUniform1f(this.saturationHandle, 1.0f);
                GLES20.glUniform1f(this.warmthHandle, 0.0f);
                GLES20.glUniform1f(this.vignetteHandle, 0.0f);
                GLES20.glUniform1f(this.grainHandle, 0.0f);
                GLES20.glUniform1f(this.fadeAmountHandle, 0.0f);
                GLES20.glUniform3f(this.highlightsTintColorHandle, 0.0f, 0.0f, 0.0f);
                GLES20.glUniform1f(this.highlightsTintIntensityHandle, 0.0f);
                GLES20.glUniform3f(this.shadowsTintColorHandle, 0.0f, 0.0f, 0.0f);
                GLES20.glUniform1f(this.shadowsTintIntensityHandle, 0.0f);
                GLES20.glUniform1f(this.skipToneHandle, 1.0f);
            }
            else {
                GLES20.glUniform1f(this.shadowsHandle, PhotoFilterView.this.getShadowsValue());
                GLES20.glUniform1f(this.highlightsHandle, PhotoFilterView.this.getHighlightsValue());
                GLES20.glUniform1f(this.exposureHandle, PhotoFilterView.this.getExposureValue());
                GLES20.glUniform1f(this.contrastHandle, PhotoFilterView.this.getContrastValue());
                GLES20.glUniform1f(this.saturationHandle, PhotoFilterView.this.getSaturationValue());
                GLES20.glUniform1f(this.warmthHandle, PhotoFilterView.this.getWarmthValue());
                GLES20.glUniform1f(this.vignetteHandle, PhotoFilterView.this.getVignetteValue());
                GLES20.glUniform1f(this.grainHandle, PhotoFilterView.this.getGrainValue());
                GLES20.glUniform1f(this.fadeAmountHandle, PhotoFilterView.this.getFadeValue());
                GLES20.glUniform3f(this.highlightsTintColorHandle, (PhotoFilterView.this.tintHighlightsColor >> 16 & 0xFF) / 255.0f, (PhotoFilterView.this.tintHighlightsColor >> 8 & 0xFF) / 255.0f, (PhotoFilterView.this.tintHighlightsColor & 0xFF) / 255.0f);
                GLES20.glUniform1f(this.highlightsTintIntensityHandle, PhotoFilterView.this.getTintHighlightsIntensityValue());
                GLES20.glUniform3f(this.shadowsTintColorHandle, (PhotoFilterView.this.tintShadowsColor >> 16 & 0xFF) / 255.0f, (PhotoFilterView.this.tintShadowsColor >> 8 & 0xFF) / 255.0f, (PhotoFilterView.this.tintShadowsColor & 0xFF) / 255.0f);
                GLES20.glUniform1f(this.shadowsTintIntensityHandle, PhotoFilterView.this.getTintShadowsIntensityValue());
                final boolean shouldBeSkipped = PhotoFilterView.this.curvesToolValue.shouldBeSkipped();
                final int skipToneHandle = this.skipToneHandle;
                if (!shouldBeSkipped) {
                    n = 0.0f;
                }
                GLES20.glUniform1f(skipToneHandle, n);
                if (!shouldBeSkipped) {
                    PhotoFilterView.this.curvesToolValue.fillBuffer();
                    GLES20.glActiveTexture(33985);
                    GLES20.glBindTexture(3553, this.curveTextures[0]);
                    GLES20.glTexParameteri(3553, 10241, 9729);
                    GLES20.glTexParameteri(3553, 10240, 9729);
                    GLES20.glTexParameteri(3553, 10242, 33071);
                    GLES20.glTexParameteri(3553, 10243, 33071);
                    GLES20.glTexImage2D(3553, 0, 6408, 200, 1, 0, 6408, 5121, (Buffer)PhotoFilterView.this.curvesToolValue.curveBuffer);
                    GLES20.glUniform1i(this.curvesImageHandle, 1);
                }
            }
            GLES20.glUniform1f(this.widthHandle, (float)this.renderBufferWidth);
            GLES20.glUniform1f(this.heightHandle, (float)this.renderBufferHeight);
            GLES20.glEnableVertexAttribArray(this.inputTexCoordHandle);
            GLES20.glVertexAttribPointer(this.inputTexCoordHandle, 2, 5126, false, 8, (Buffer)this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.positionHandle);
            GLES20.glVertexAttribPointer(this.positionHandle, 2, 5126, false, 8, (Buffer)this.vertexInvertBuffer);
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
                GLES20.glVertexAttribPointer(this.rgbToHsvInputTexCoordHandle, 2, 5126, false, 8, (Buffer)this.textureBuffer);
                GLES20.glEnableVertexAttribArray(this.rgbToHsvPositionHandle);
                GLES20.glVertexAttribPointer(this.rgbToHsvPositionHandle, 2, 5126, false, 8, (Buffer)this.vertexBuffer);
                GLES20.glDrawArrays(5, 0, 4);
                final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.renderBufferWidth * this.renderBufferHeight * 4);
                GLES20.glReadPixels(0, 0, this.renderBufferWidth, this.renderBufferHeight, 6408, 5121, (Buffer)allocateDirect);
                GLES20.glBindTexture(3553, this.enhanceTextures[0]);
                GLES20.glTexParameteri(3553, 10241, 9729);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10242, 33071);
                GLES20.glTexParameteri(3553, 10243, 33071);
                GLES20.glTexImage2D(3553, 0, 6408, this.renderBufferWidth, this.renderBufferHeight, 0, 6408, 5121, (Buffer)allocateDirect);
                Buffer allocateDirect2 = null;
                try {
                    final ByteBuffer byteBuffer = (ByteBuffer)(allocateDirect2 = ByteBuffer.allocateDirect(16384));
                    Utilities.calcCDT(allocateDirect, this.renderBufferWidth, this.renderBufferHeight, byteBuffer);
                    allocateDirect2 = byteBuffer;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                GLES20.glBindTexture(3553, this.enhanceTextures[1]);
                GLES20.glTexParameteri(3553, 10241, 9729);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10242, 33071);
                GLES20.glTexParameteri(3553, 10243, 33071);
                GLES20.glTexImage2D(3553, 0, 6408, 256, 16, 0, 6408, 5121, allocateDirect2);
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
                GLES20.glUniform1f(this.enhanceIntensityHandle, 0.0f);
            }
            else {
                GLES20.glUniform1f(this.enhanceIntensityHandle, PhotoFilterView.this.getEnhanceValue());
            }
            GLES20.glEnableVertexAttribArray(this.enhanceInputTexCoordHandle);
            GLES20.glVertexAttribPointer(this.enhanceInputTexCoordHandle, 2, 5126, false, 8, (Buffer)this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.enhancePositionHandle);
            GLES20.glVertexAttribPointer(this.enhancePositionHandle, 2, 5126, false, 8, (Buffer)this.vertexBuffer);
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
                GLES20.glUniform1f(this.sharpenHandle, 0.0f);
            }
            else {
                GLES20.glUniform1f(this.sharpenHandle, PhotoFilterView.this.getSharpenValue());
            }
            GLES20.glUniform1f(this.sharpenWidthHandle, (float)this.renderBufferWidth);
            GLES20.glUniform1f(this.sharpenHeightHandle, (float)this.renderBufferHeight);
            GLES20.glEnableVertexAttribArray(this.sharpenInputTexCoordHandle);
            GLES20.glVertexAttribPointer(this.sharpenInputTexCoordHandle, 2, 5126, false, 8, (Buffer)this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.sharpenPositionHandle);
            GLES20.glVertexAttribPointer(this.sharpenPositionHandle, 2, 5126, false, 8, (Buffer)this.vertexInvertBuffer);
            GLES20.glDrawArrays(5, 0, 4);
        }
        
        private Bitmap getRenderBufferBitmap() {
            final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.renderBufferWidth * this.renderBufferHeight * 4);
            GLES20.glReadPixels(0, 0, this.renderBufferWidth, this.renderBufferHeight, 6408, 5121, (Buffer)allocateDirect);
            final Bitmap bitmap = Bitmap.createBitmap(this.renderBufferWidth, this.renderBufferHeight, Bitmap$Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer((Buffer)allocateDirect);
            return bitmap;
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
            if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[] { 12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12326, 0, 12344 }, array2, 1, array)) {
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
            final float[] array3;
            final float[] src = array3 = new float[8];
            array3[0] = -1.0f;
            array3[1] = 1.0f;
            array3[3] = (array3[2] = 1.0f);
            array3[5] = (array3[4] = -1.0f);
            array3[6] = 1.0f;
            array3[7] = -1.0f;
            final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(src.length * 4);
            allocateDirect.order(ByteOrder.nativeOrder());
            (this.vertexBuffer = allocateDirect.asFloatBuffer()).put(src);
            this.vertexBuffer.position(0);
            final float[] array4;
            final float[] src2 = array4 = new float[8];
            array4[1] = (array4[0] = -1.0f);
            array4[2] = 1.0f;
            array4[4] = (array4[3] = -1.0f);
            array4[5] = 1.0f;
            array4[7] = (array4[6] = 1.0f);
            final ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(src2.length * 4);
            allocateDirect2.order(ByteOrder.nativeOrder());
            (this.vertexInvertBuffer = allocateDirect2.asFloatBuffer()).put(src2);
            this.vertexInvertBuffer.position(0);
            final float[] array5;
            final float[] src3 = array5 = new float[8];
            array5[1] = (array5[0] = 0.0f);
            array5[2] = 1.0f;
            array5[4] = (array5[3] = 0.0f);
            array5[5] = 1.0f;
            array5[7] = (array5[6] = 1.0f);
            final ByteBuffer allocateDirect3 = ByteBuffer.allocateDirect(src3.length * 4);
            allocateDirect3.order(ByteOrder.nativeOrder());
            (this.textureBuffer = allocateDirect3.asFloatBuffer()).put(src3);
            this.textureBuffer.position(0);
            GLES20.glGenTextures(1, this.curveTextures, 0);
            GLES20.glGenTextures(2, this.enhanceTextures, 0);
            final int loadShader = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
            final int loadShader2 = this.loadShader(35632, "varying highp vec2 texCoord;uniform sampler2D sourceImage;uniform highp float width;uniform highp float height;uniform sampler2D curvesImage;uniform lowp float skipTone;uniform lowp float shadows;const mediump vec3 hsLuminanceWeighting = vec3(0.3, 0.3, 0.3);uniform lowp float highlights;uniform lowp float contrast;uniform lowp float fadeAmount;const mediump vec3 satLuminanceWeighting = vec3(0.2126, 0.7152, 0.0722);uniform lowp float saturation;uniform lowp float shadowsTintIntensity;uniform lowp float highlightsTintIntensity;uniform lowp vec3 shadowsTintColor;uniform lowp vec3 highlightsTintColor;uniform lowp float exposure;uniform lowp float warmth;uniform lowp float grain;const lowp float permTexUnit = 1.0 / 256.0;const lowp float permTexUnitHalf = 0.5 / 256.0;const lowp float grainsize = 2.3;uniform lowp float vignette;highp float getLuma(highp vec3 rgbP) {return (0.299 * rgbP.r) + (0.587 * rgbP.g) + (0.114 * rgbP.b);}lowp vec3 rgbToHsv(lowp vec3 c) {highp vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);highp vec4 p = c.g < c.b ? vec4(c.bg, K.wz) : vec4(c.gb, K.xy);highp vec4 q = c.r < p.x ? vec4(p.xyw, c.r) : vec4(c.r, p.yzx);highp float d = q.x - min(q.w, q.y);highp float e = 1.0e-10;return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);}lowp vec3 hsvToRgb(lowp vec3 c) {highp vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);highp vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);}highp vec3 rgbToHsl(highp vec3 color) {highp vec3 hsl;highp float fmin = min(min(color.r, color.g), color.b);highp float fmax = max(max(color.r, color.g), color.b);highp float delta = fmax - fmin;hsl.z = (fmax + fmin) / 2.0;if (delta == 0.0) {hsl.x = 0.0;hsl.y = 0.0;} else {if (hsl.z < 0.5) {hsl.y = delta / (fmax + fmin);} else {hsl.y = delta / (2.0 - fmax - fmin);}highp float deltaR = (((fmax - color.r) / 6.0) + (delta / 2.0)) / delta;highp float deltaG = (((fmax - color.g) / 6.0) + (delta / 2.0)) / delta;highp float deltaB = (((fmax - color.b) / 6.0) + (delta / 2.0)) / delta;if (color.r == fmax) {hsl.x = deltaB - deltaG;} else if (color.g == fmax) {hsl.x = (1.0 / 3.0) + deltaR - deltaB;} else if (color.b == fmax) {hsl.x = (2.0 / 3.0) + deltaG - deltaR;}if (hsl.x < 0.0) {hsl.x += 1.0;} else if (hsl.x > 1.0) {hsl.x -= 1.0;}}return hsl;}highp float hueToRgb(highp float f1, highp float f2, highp float hue) {if (hue < 0.0) {hue += 1.0;} else if (hue > 1.0) {hue -= 1.0;}highp float res;if ((6.0 * hue) < 1.0) {res = f1 + (f2 - f1) * 6.0 * hue;} else if ((2.0 * hue) < 1.0) {res = f2;} else if ((3.0 * hue) < 2.0) {res = f1 + (f2 - f1) * ((2.0 / 3.0) - hue) * 6.0;} else {res = f1;} return res;}highp vec3 hslToRgb(highp vec3 hsl) {if (hsl.y == 0.0) {return vec3(hsl.z);} else {highp float f2;if (hsl.z < 0.5) {f2 = hsl.z * (1.0 + hsl.y);} else {f2 = (hsl.z + hsl.y) - (hsl.y * hsl.z);}highp float f1 = 2.0 * hsl.z - f2;return vec3(hueToRgb(f1, f2, hsl.x + (1.0/3.0)), hueToRgb(f1, f2, hsl.x), hueToRgb(f1, f2, hsl.x - (1.0/3.0)));}}highp vec3 rgbToYuv(highp vec3 inP) {highp float luma = getLuma(inP);return vec3(luma, (1.0 / 1.772) * (inP.b - luma), (1.0 / 1.402) * (inP.r - luma));}lowp vec3 yuvToRgb(highp vec3 inP) {return vec3(1.402 * inP.b + inP.r, (inP.r - (0.299 * 1.402 / 0.587) * inP.b - (0.114 * 1.772 / 0.587) * inP.g), 1.772 * inP.g + inP.r);}lowp float easeInOutSigmoid(lowp float value, lowp float strength) {if (value > 0.5) {return 1.0 - pow(2.0 - 2.0 * value, 1.0 / (1.0 - strength)) * 0.5;} else {return pow(2.0 * value, 1.0 / (1.0 - strength)) * 0.5;}}lowp vec3 applyLuminanceCurve(lowp vec3 pixel) {highp float index = floor(clamp(pixel.z / (1.0 / 200.0), 0.0, 199.0));pixel.y = mix(0.0, pixel.y, smoothstep(0.0, 0.1, pixel.z) * (1.0 - smoothstep(0.8, 1.0, pixel.z)));pixel.z = texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).a;return pixel;}lowp vec3 applyRGBCurve(lowp vec3 pixel) {highp float index = floor(clamp(pixel.r / (1.0 / 200.0), 0.0, 199.0));pixel.r = texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).r;index = floor(clamp(pixel.g / (1.0 / 200.0), 0.0, 199.0));pixel.g = clamp(texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).g, 0.0, 1.0);index = floor(clamp(pixel.b / (1.0 / 200.0), 0.0, 199.0));pixel.b = clamp(texture2D(curvesImage, vec2(1.0 / 200.0 * index, 0)).b, 0.0, 1.0);return pixel;}highp vec3 fadeAdjust(highp vec3 color, highp float fadeVal) {return (color * (1.0 - fadeVal)) + ((color + (vec3(-0.9772) * pow(vec3(color), vec3(3.0)) + vec3(1.708) * pow(vec3(color), vec3(2.0)) + vec3(-0.1603) * vec3(color) + vec3(0.2878) - color * vec3(0.9))) * fadeVal);}lowp vec3 tintRaiseShadowsCurve(lowp vec3 color) {return vec3(-0.003671) * pow(color, vec3(3.0)) + vec3(0.3842) * pow(color, vec3(2.0)) + vec3(0.3764) * color + vec3(0.2515);}lowp vec3 tintShadows(lowp vec3 texel, lowp vec3 tintColor, lowp float tintAmount) {return clamp(mix(texel, mix(texel, tintRaiseShadowsCurve(texel), tintColor), tintAmount), 0.0, 1.0);} lowp vec3 tintHighlights(lowp vec3 texel, lowp vec3 tintColor, lowp float tintAmount) {return clamp(mix(texel, mix(texel, vec3(1.0) - tintRaiseShadowsCurve(vec3(1.0) - texel), (vec3(1.0) - tintColor)), tintAmount), 0.0, 1.0);}highp vec4 rnm(in highp vec2 tc) {highp float noise = sin(dot(tc, vec2(12.9898, 78.233))) * 43758.5453;return vec4(fract(noise), fract(noise * 1.2154), fract(noise * 1.3453), fract(noise * 1.3647)) * 2.0 - 1.0;}highp float fade(in highp float t) {return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);}highp float pnoise3D(in highp vec3 p) {highp vec3 pi = permTexUnit * floor(p) + permTexUnitHalf;highp vec3 pf = fract(p);highp float perm = rnm(pi.xy).a;highp float n000 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf);highp float n001 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(0.0, 0.0, 1.0));perm = rnm(pi.xy + vec2(0.0, permTexUnit)).a;highp float n010 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf - vec3(0.0, 1.0, 0.0));highp float n011 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(0.0, 1.0, 1.0));perm = rnm(pi.xy + vec2(permTexUnit, 0.0)).a;highp float n100 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf - vec3(1.0, 0.0, 0.0));highp float n101 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(1.0, 0.0, 1.0));perm = rnm(pi.xy + vec2(permTexUnit, permTexUnit)).a;highp float n110 = dot(rnm(vec2(perm, pi.z)).rgb * 4.0 - 1.0, pf - vec3(1.0, 1.0, 0.0));highp float n111 = dot(rnm(vec2(perm, pi.z + permTexUnit)).rgb * 4.0 - 1.0, pf - vec3(1.0, 1.0, 1.0));highp vec4 n_x = mix(vec4(n000, n001, n010, n011), vec4(n100, n101, n110, n111), fade(pf.x));highp vec2 n_xy = mix(n_x.xy, n_x.zw, fade(pf.y));return mix(n_xy.x, n_xy.y, fade(pf.z));}lowp vec2 coordRot(in lowp vec2 tc, in lowp float angle) {return vec2(((tc.x * 2.0 - 1.0) * cos(angle) - (tc.y * 2.0 - 1.0) * sin(angle)) * 0.5 + 0.5, ((tc.y * 2.0 - 1.0) * cos(angle) + (tc.x * 2.0 - 1.0) * sin(angle)) * 0.5 + 0.5);}void main() {lowp vec4 source = texture2D(sourceImage, texCoord);lowp vec4 result = source;const lowp float toolEpsilon = 0.005;if (skipTone < toolEpsilon) {result = vec4(applyRGBCurve(hslToRgb(applyLuminanceCurve(rgbToHsl(result.rgb)))), result.a);}mediump float hsLuminance = dot(result.rgb, hsLuminanceWeighting);mediump float shadow = clamp((pow(hsLuminance, 1.0 / shadows) + (-0.76) * pow(hsLuminance, 2.0 / shadows)) - hsLuminance, 0.0, 1.0);mediump float highlight = clamp((1.0 - (pow(1.0 - hsLuminance, 1.0 / (2.0 - highlights)) + (-0.8) * pow(1.0 - hsLuminance, 2.0 / (2.0 - highlights)))) - hsLuminance, -1.0, 0.0);lowp vec3 hsresult = vec3(0.0, 0.0, 0.0) + ((hsLuminance + shadow + highlight) - 0.0) * ((result.rgb - vec3(0.0, 0.0, 0.0)) / (hsLuminance - 0.0));mediump float contrastedLuminance = ((hsLuminance - 0.5) * 1.5) + 0.5;mediump float whiteInterp = contrastedLuminance * contrastedLuminance * contrastedLuminance;mediump float whiteTarget = clamp(highlights, 1.0, 2.0) - 1.0;hsresult = mix(hsresult, vec3(1.0), whiteInterp * whiteTarget);mediump float invContrastedLuminance = 1.0 - contrastedLuminance;mediump float blackInterp = invContrastedLuminance * invContrastedLuminance * invContrastedLuminance;mediump float blackTarget = 1.0 - clamp(shadows, 0.0, 1.0);hsresult = mix(hsresult, vec3(0.0), blackInterp * blackTarget);result = vec4(hsresult.rgb, result.a);result = vec4(clamp(((result.rgb - vec3(0.5)) * contrast + vec3(0.5)), 0.0, 1.0), result.a);if (abs(fadeAmount) > toolEpsilon) {result.rgb = fadeAdjust(result.rgb, fadeAmount);}lowp float satLuminance = dot(result.rgb, satLuminanceWeighting);lowp vec3 greyScaleColor = vec3(satLuminance);result = vec4(clamp(mix(greyScaleColor, result.rgb, saturation), 0.0, 1.0), result.a);if (abs(shadowsTintIntensity) > toolEpsilon) {result.rgb = tintShadows(result.rgb, shadowsTintColor, shadowsTintIntensity * 2.0);}if (abs(highlightsTintIntensity) > toolEpsilon) {result.rgb = tintHighlights(result.rgb, highlightsTintColor, highlightsTintIntensity * 2.0);}if (abs(exposure) > toolEpsilon) {mediump float mag = exposure * 1.045;mediump float exppower = 1.0 + abs(mag);if (mag < 0.0) {exppower = 1.0 / exppower;}result.r = 1.0 - pow((1.0 - result.r), exppower);result.g = 1.0 - pow((1.0 - result.g), exppower);result.b = 1.0 - pow((1.0 - result.b), exppower);}if (abs(warmth) > toolEpsilon) {highp vec3 yuvVec;if (warmth > 0.0 ) {yuvVec = vec3(0.1765, -0.1255, 0.0902);} else {yuvVec = -vec3(0.0588, 0.1569, -0.1255);}highp vec3 yuvColor = rgbToYuv(result.rgb);highp float luma = yuvColor.r;highp float curveScale = sin(luma * 3.14159);yuvColor += 0.375 * warmth * curveScale * yuvVec;result.rgb = yuvToRgb(yuvColor);}if (abs(grain) > toolEpsilon) {highp vec3 rotOffset = vec3(1.425, 3.892, 5.835);highp vec2 rotCoordsR = coordRot(texCoord, rotOffset.x);highp vec3 noise = vec3(pnoise3D(vec3(rotCoordsR * vec2(width / grainsize, height / grainsize),0.0)));lowp vec3 lumcoeff = vec3(0.299,0.587,0.114);lowp float luminance = dot(result.rgb, lumcoeff);lowp float lum = smoothstep(0.2, 0.0, luminance);lum += luminance;noise = mix(noise,vec3(0.0),pow(lum,4.0));result.rgb = result.rgb + noise * grain;}if (abs(vignette) > toolEpsilon) {const lowp float midpoint = 0.7;const lowp float fuzziness = 0.62;lowp float radDist = length(texCoord - 0.5) / sqrt(0.5);lowp float mag = easeInOutSigmoid(radDist * midpoint, fuzziness) * vignette * 0.645;result.rgb = mix(pow(result.rgb, vec3(1.0 / (1.0 - mag))), vec3(0.0), mag * mag);}gl_FragColor = result;}");
            if (loadShader == 0 || loadShader2 == 0) {
                this.finish();
                return false;
            }
            GLES20.glAttachShader(this.toolsShaderProgram = GLES20.glCreateProgram(), loadShader);
            GLES20.glAttachShader(this.toolsShaderProgram, loadShader2);
            GLES20.glBindAttribLocation(this.toolsShaderProgram, 0, "position");
            GLES20.glBindAttribLocation(this.toolsShaderProgram, 1, "inputTexCoord");
            GLES20.glLinkProgram(this.toolsShaderProgram);
            final int[] array6 = { 0 };
            GLES20.glGetProgramiv(this.toolsShaderProgram, 35714, array6, 0);
            if (array6[0] == 0) {
                GLES20.glDeleteProgram(this.toolsShaderProgram);
                this.toolsShaderProgram = 0;
            }
            else {
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
            final int loadShader3 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;uniform highp float inputWidth;uniform highp float inputHeight;varying vec2 leftTexCoord;varying vec2 rightTexCoord;varying vec2 topTexCoord;varying vec2 bottomTexCoord;void main() {gl_Position = position;texCoord = inputTexCoord;highp vec2 widthStep = vec2(1.0 / inputWidth, 0.0);highp vec2 heightStep = vec2(0.0, 1.0 / inputHeight);leftTexCoord = inputTexCoord - widthStep;rightTexCoord = inputTexCoord + widthStep;topTexCoord = inputTexCoord + heightStep;bottomTexCoord = inputTexCoord - heightStep;}");
            final int loadShader4 = this.loadShader(35632, "precision highp float;varying vec2 texCoord;varying vec2 leftTexCoord;varying vec2 rightTexCoord;varying vec2 topTexCoord;varying vec2 bottomTexCoord;uniform sampler2D sourceImage;uniform float sharpen;void main() {vec4 result = texture2D(sourceImage, texCoord);vec3 leftTextureColor = texture2D(sourceImage, leftTexCoord).rgb;vec3 rightTextureColor = texture2D(sourceImage, rightTexCoord).rgb;vec3 topTextureColor = texture2D(sourceImage, topTexCoord).rgb;vec3 bottomTextureColor = texture2D(sourceImage, bottomTexCoord).rgb;result.rgb = result.rgb * (1.0 + 4.0 * sharpen) - (leftTextureColor + rightTextureColor + topTextureColor + bottomTextureColor) * sharpen;gl_FragColor = result;}");
            if (loadShader3 == 0 || loadShader4 == 0) {
                this.finish();
                return false;
            }
            GLES20.glAttachShader(this.sharpenShaderProgram = GLES20.glCreateProgram(), loadShader3);
            GLES20.glAttachShader(this.sharpenShaderProgram, loadShader4);
            GLES20.glBindAttribLocation(this.sharpenShaderProgram, 0, "position");
            GLES20.glBindAttribLocation(this.sharpenShaderProgram, 1, "inputTexCoord");
            GLES20.glLinkProgram(this.sharpenShaderProgram);
            final int[] array7 = { 0 };
            GLES20.glGetProgramiv(this.sharpenShaderProgram, 35714, array7, 0);
            if (array7[0] == 0) {
                GLES20.glDeleteProgram(this.sharpenShaderProgram);
                this.sharpenShaderProgram = 0;
            }
            else {
                this.sharpenPositionHandle = GLES20.glGetAttribLocation(this.sharpenShaderProgram, "position");
                this.sharpenInputTexCoordHandle = GLES20.glGetAttribLocation(this.sharpenShaderProgram, "inputTexCoord");
                this.sharpenSourceImageHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "sourceImage");
                this.sharpenWidthHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "inputWidth");
                this.sharpenHeightHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "inputHeight");
                this.sharpenHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "sharpen");
            }
            final int loadShader5 = this.loadShader(35633, "attribute vec4 position;attribute vec4 inputTexCoord;uniform highp float texelWidthOffset;uniform highp float texelHeightOffset;varying vec2 blurCoordinates[9];void main() {gl_Position = position;vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);blurCoordinates[0] = inputTexCoord.xy;blurCoordinates[1] = inputTexCoord.xy + singleStepOffset * 1.458430;blurCoordinates[2] = inputTexCoord.xy - singleStepOffset * 1.458430;blurCoordinates[3] = inputTexCoord.xy + singleStepOffset * 3.403985;blurCoordinates[4] = inputTexCoord.xy - singleStepOffset * 3.403985;blurCoordinates[5] = inputTexCoord.xy + singleStepOffset * 5.351806;blurCoordinates[6] = inputTexCoord.xy - singleStepOffset * 5.351806;blurCoordinates[7] = inputTexCoord.xy + singleStepOffset * 7.302940;blurCoordinates[8] = inputTexCoord.xy - singleStepOffset * 7.302940;}");
            final int loadShader6 = this.loadShader(35632, "uniform sampler2D sourceImage;varying highp vec2 blurCoordinates[9];void main() {lowp vec4 sum = vec4(0.0);sum += texture2D(sourceImage, blurCoordinates[0]) * 0.133571;sum += texture2D(sourceImage, blurCoordinates[1]) * 0.233308;sum += texture2D(sourceImage, blurCoordinates[2]) * 0.233308;sum += texture2D(sourceImage, blurCoordinates[3]) * 0.135928;sum += texture2D(sourceImage, blurCoordinates[4]) * 0.135928;sum += texture2D(sourceImage, blurCoordinates[5]) * 0.051383;sum += texture2D(sourceImage, blurCoordinates[6]) * 0.051383;sum += texture2D(sourceImage, blurCoordinates[7]) * 0.012595;sum += texture2D(sourceImage, blurCoordinates[8]) * 0.012595;gl_FragColor = sum;}");
            if (loadShader5 == 0 || loadShader6 == 0) {
                this.finish();
                return false;
            }
            GLES20.glAttachShader(this.blurShaderProgram = GLES20.glCreateProgram(), loadShader5);
            GLES20.glAttachShader(this.blurShaderProgram, loadShader6);
            GLES20.glBindAttribLocation(this.blurShaderProgram, 0, "position");
            GLES20.glBindAttribLocation(this.blurShaderProgram, 1, "inputTexCoord");
            GLES20.glLinkProgram(this.blurShaderProgram);
            final int[] array8 = { 0 };
            GLES20.glGetProgramiv(this.blurShaderProgram, 35714, array8, 0);
            if (array8[0] == 0) {
                GLES20.glDeleteProgram(this.blurShaderProgram);
                this.blurShaderProgram = 0;
            }
            else {
                this.blurPositionHandle = GLES20.glGetAttribLocation(this.blurShaderProgram, "position");
                this.blurInputTexCoordHandle = GLES20.glGetAttribLocation(this.blurShaderProgram, "inputTexCoord");
                this.blurSourceImageHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "sourceImage");
                this.blurWidthHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "texelWidthOffset");
                this.blurHeightHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "texelHeightOffset");
            }
            final int loadShader7 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
            final int loadShader8 = this.loadShader(35632, "varying highp vec2 texCoord;uniform sampler2D sourceImage;uniform sampler2D inputImageTexture2;uniform lowp float excludeSize;uniform lowp vec2 excludePoint;uniform lowp float excludeBlurSize;uniform highp float angle;uniform highp float aspectRatio;void main() {lowp vec4 sharpImageColor = texture2D(sourceImage, texCoord);lowp vec4 blurredImageColor = texture2D(inputImageTexture2, texCoord);highp vec2 texCoordToUse = vec2(texCoord.x, (texCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));highp float distanceFromCenter = abs((texCoordToUse.x - excludePoint.x) * aspectRatio * cos(angle) + (texCoordToUse.y - excludePoint.y) * sin(angle));gl_FragColor = mix(sharpImageColor, blurredImageColor, smoothstep(excludeSize - excludeBlurSize, excludeSize, distanceFromCenter));}");
            if (loadShader7 == 0 || loadShader8 == 0) {
                this.finish();
                return false;
            }
            GLES20.glAttachShader(this.linearBlurShaderProgram = GLES20.glCreateProgram(), loadShader7);
            GLES20.glAttachShader(this.linearBlurShaderProgram, loadShader8);
            GLES20.glBindAttribLocation(this.linearBlurShaderProgram, 0, "position");
            GLES20.glBindAttribLocation(this.linearBlurShaderProgram, 1, "inputTexCoord");
            GLES20.glLinkProgram(this.linearBlurShaderProgram);
            final int[] array9 = { 0 };
            GLES20.glGetProgramiv(this.linearBlurShaderProgram, 35714, array9, 0);
            if (array9[0] == 0) {
                GLES20.glDeleteProgram(this.linearBlurShaderProgram);
                this.linearBlurShaderProgram = 0;
            }
            else {
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
            final int loadShader9 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
            final int loadShader10 = this.loadShader(35632, "varying highp vec2 texCoord;uniform sampler2D sourceImage;uniform sampler2D inputImageTexture2;uniform lowp float excludeSize;uniform lowp vec2 excludePoint;uniform lowp float excludeBlurSize;uniform highp float aspectRatio;void main() {lowp vec4 sharpImageColor = texture2D(sourceImage, texCoord);lowp vec4 blurredImageColor = texture2D(inputImageTexture2, texCoord);highp vec2 texCoordToUse = vec2(texCoord.x, (texCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));highp float distanceFromCenter = distance(excludePoint, texCoordToUse);gl_FragColor = mix(sharpImageColor, blurredImageColor, smoothstep(excludeSize - excludeBlurSize, excludeSize, distanceFromCenter));}");
            if (loadShader9 == 0 || loadShader10 == 0) {
                this.finish();
                return false;
            }
            GLES20.glAttachShader(this.radialBlurShaderProgram = GLES20.glCreateProgram(), loadShader9);
            GLES20.glAttachShader(this.radialBlurShaderProgram, loadShader10);
            GLES20.glBindAttribLocation(this.radialBlurShaderProgram, 0, "position");
            GLES20.glBindAttribLocation(this.radialBlurShaderProgram, 1, "inputTexCoord");
            GLES20.glLinkProgram(this.radialBlurShaderProgram);
            final int[] array10 = { 0 };
            GLES20.glGetProgramiv(this.radialBlurShaderProgram, 35714, array10, 0);
            if (array10[0] == 0) {
                GLES20.glDeleteProgram(this.radialBlurShaderProgram);
                this.radialBlurShaderProgram = 0;
            }
            else {
                this.radialBlurPositionHandle = GLES20.glGetAttribLocation(this.radialBlurShaderProgram, "position");
                this.radialBlurInputTexCoordHandle = GLES20.glGetAttribLocation(this.radialBlurShaderProgram, "inputTexCoord");
                this.radialBlurSourceImageHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "sourceImage");
                this.radialBlurSourceImage2Handle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "inputImageTexture2");
                this.radialBlurExcludeSizeHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "excludeSize");
                this.radialBlurExcludePointHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "excludePoint");
                this.radialBlurExcludeBlurSizeHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "excludeBlurSize");
                this.radialBlurAspectRatioHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "aspectRatio");
            }
            final int loadShader11 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
            final int loadShader12 = this.loadShader(35632, "precision highp float;varying vec2 texCoord;uniform sampler2D sourceImage;vec3 rgb_to_hsv(vec3 c) {vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);vec4 p = c.g < c.b ? vec4(c.bg, K.wz) : vec4(c.gb, K.xy);vec4 q = c.r < p.x ? vec4(p.xyw, c.r) : vec4(c.r, p.yzx);float d = q.x - min(q.w, q.y);float e = 1.0e-10;return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);}void main() {vec4 texel = texture2D(sourceImage, texCoord);gl_FragColor = vec4(rgb_to_hsv(texel.rgb), texel.a);}");
            if (loadShader11 == 0 || loadShader12 == 0) {
                this.finish();
                return false;
            }
            GLES20.glAttachShader(this.rgbToHsvShaderProgram = GLES20.glCreateProgram(), loadShader11);
            GLES20.glAttachShader(this.rgbToHsvShaderProgram, loadShader12);
            GLES20.glBindAttribLocation(this.rgbToHsvShaderProgram, 0, "position");
            GLES20.glBindAttribLocation(this.rgbToHsvShaderProgram, 1, "inputTexCoord");
            GLES20.glLinkProgram(this.rgbToHsvShaderProgram);
            final int[] array11 = { 0 };
            GLES20.glGetProgramiv(this.rgbToHsvShaderProgram, 35714, array11, 0);
            if (array11[0] == 0) {
                GLES20.glDeleteProgram(this.rgbToHsvShaderProgram);
                this.rgbToHsvShaderProgram = 0;
            }
            else {
                this.rgbToHsvPositionHandle = GLES20.glGetAttribLocation(this.rgbToHsvShaderProgram, "position");
                this.rgbToHsvInputTexCoordHandle = GLES20.glGetAttribLocation(this.rgbToHsvShaderProgram, "inputTexCoord");
                this.rgbToHsvSourceImageHandle = GLES20.glGetUniformLocation(this.rgbToHsvShaderProgram, "sourceImage");
            }
            final int loadShader13 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
            final int loadShader14 = this.loadShader(35632, "precision highp float;varying vec2 texCoord;uniform sampler2D sourceImage;uniform sampler2D inputImageTexture2;uniform float intensity;float enhance(float value) {const vec2 offset = vec2(0.001953125, 0.03125);value = value + offset.x;vec2 coord = (clamp(texCoord, 0.125, 1.0 - 0.125001) - 0.125) * 4.0;vec2 frac = fract(coord);coord = floor(coord);float p00 = float(coord.y * 4.0 + coord.x) * 0.0625 + offset.y;float p01 = float(coord.y * 4.0 + coord.x + 1.0) * 0.0625 + offset.y;float p10 = float((coord.y + 1.0) * 4.0 + coord.x) * 0.0625 + offset.y;float p11 = float((coord.y + 1.0) * 4.0 + coord.x + 1.0) * 0.0625 + offset.y;vec3 c00 = texture2D(inputImageTexture2, vec2(value, p00)).rgb;vec3 c01 = texture2D(inputImageTexture2, vec2(value, p01)).rgb;vec3 c10 = texture2D(inputImageTexture2, vec2(value, p10)).rgb;vec3 c11 = texture2D(inputImageTexture2, vec2(value, p11)).rgb;float c1 = ((c00.r - c00.g) / (c00.b - c00.g));float c2 = ((c01.r - c01.g) / (c01.b - c01.g));float c3 = ((c10.r - c10.g) / (c10.b - c10.g));float c4 = ((c11.r - c11.g) / (c11.b - c11.g));float c1_2 = mix(c1, c2, frac.x);float c3_4 = mix(c3, c4, frac.x);return mix(c1_2, c3_4, frac.y);}vec3 hsv_to_rgb(vec3 c) {vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);}void main() {vec4 texel = texture2D(sourceImage, texCoord);vec4 hsv = texel;hsv.y = min(1.0, hsv.y * 1.2);hsv.z = min(1.0, enhance(hsv.z) * 1.1);gl_FragColor = vec4(hsv_to_rgb(mix(texel.xyz, hsv.xyz, intensity)), texel.w);}");
            if (loadShader13 == 0 || loadShader14 == 0) {
                this.finish();
                return false;
            }
            GLES20.glAttachShader(this.enhanceShaderProgram = GLES20.glCreateProgram(), loadShader13);
            GLES20.glAttachShader(this.enhanceShaderProgram, loadShader14);
            GLES20.glBindAttribLocation(this.enhanceShaderProgram, 0, "position");
            GLES20.glBindAttribLocation(this.enhanceShaderProgram, 1, "inputTexCoord");
            GLES20.glLinkProgram(this.enhanceShaderProgram);
            final int[] array12 = { 0 };
            GLES20.glGetProgramiv(this.enhanceShaderProgram, 35714, array12, 0);
            if (array12[0] == 0) {
                GLES20.glDeleteProgram(this.enhanceShaderProgram);
                this.enhanceShaderProgram = 0;
            }
            else {
                this.enhancePositionHandle = GLES20.glGetAttribLocation(this.enhanceShaderProgram, "position");
                this.enhanceInputTexCoordHandle = GLES20.glGetAttribLocation(this.enhanceShaderProgram, "inputTexCoord");
                this.enhanceSourceImageHandle = GLES20.glGetUniformLocation(this.enhanceShaderProgram, "sourceImage");
                this.enhanceIntensityHandle = GLES20.glGetUniformLocation(this.enhanceShaderProgram, "intensity");
                this.enhanceInputImageTexture2Handle = GLES20.glGetUniformLocation(this.enhanceShaderProgram, "inputImageTexture2");
            }
            final int loadShader15 = this.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
            final int loadShader16 = this.loadShader(35632, "varying highp vec2 texCoord;uniform sampler2D sourceImage;void main() {gl_FragColor = texture2D(sourceImage, texCoord);}");
            if (loadShader15 != 0 && loadShader16 != 0) {
                GLES20.glAttachShader(this.simpleShaderProgram = GLES20.glCreateProgram(), loadShader15);
                GLES20.glAttachShader(this.simpleShaderProgram, loadShader16);
                GLES20.glBindAttribLocation(this.simpleShaderProgram, 0, "position");
                GLES20.glBindAttribLocation(this.simpleShaderProgram, 1, "inputTexCoord");
                GLES20.glLinkProgram(this.simpleShaderProgram);
                final int[] array13 = { 0 };
                GLES20.glGetProgramiv(this.simpleShaderProgram, 35714, array13, 0);
                if (array13[0] == 0) {
                    GLES20.glDeleteProgram(this.simpleShaderProgram);
                    this.simpleShaderProgram = 0;
                }
                else {
                    this.simplePositionHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, "position");
                    this.simpleInputTexCoordHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, "inputTexCoord");
                    this.simpleSourceImageHandle = GLES20.glGetUniformLocation(this.simpleShaderProgram, "sourceImage");
                }
                final Bitmap currentBitmap = this.currentBitmap;
                if (currentBitmap != null && !currentBitmap.isRecycled()) {
                    this.loadTexture(this.currentBitmap);
                }
                return true;
            }
            this.finish();
            return false;
        }
        
        private int loadShader(int n, final String s) {
            final int glCreateShader = GLES20.glCreateShader(n);
            GLES20.glShaderSource(glCreateShader, s);
            GLES20.glCompileShader(glCreateShader);
            final int[] array = { 0 };
            GLES20.glGetShaderiv(glCreateShader, 35713, array, 0);
            n = glCreateShader;
            if (array[0] == 0) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e(GLES20.glGetShaderInfoLog(glCreateShader));
                }
                GLES20.glDeleteShader(glCreateShader);
                n = 0;
            }
            return n;
        }
        
        private void loadTexture(final Bitmap bitmap) {
            this.renderBufferWidth = bitmap.getWidth();
            this.renderBufferHeight = bitmap.getHeight();
            final float n = (float)AndroidUtilities.getPhotoSize();
            if (this.renderBufferWidth > n || this.renderBufferHeight > n || PhotoFilterView.this.orientation % 360 != 0) {
                float n2 = 1.0f;
                if (this.renderBufferWidth > n || this.renderBufferHeight > n) {
                    n2 = n / bitmap.getWidth();
                    final float n3 = n / bitmap.getHeight();
                    if (n2 < n3) {
                        this.renderBufferWidth = (int)n;
                        this.renderBufferHeight = (int)(bitmap.getHeight() * n2);
                    }
                    else {
                        this.renderBufferHeight = (int)n;
                        this.renderBufferWidth = (int)(bitmap.getWidth() * n3);
                        n2 = n3;
                    }
                }
                if (PhotoFilterView.this.orientation % 360 == 90 || PhotoFilterView.this.orientation % 360 == 270) {
                    final int renderBufferWidth = this.renderBufferWidth;
                    this.renderBufferWidth = this.renderBufferHeight;
                    this.renderBufferHeight = renderBufferWidth;
                }
                this.currentBitmap = this.createBitmap(bitmap, this.renderBufferWidth, this.renderBufferHeight, n2);
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
        
        public Bitmap getTexture() {
            if (!this.initied) {
                return null;
            }
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final Bitmap[] array = { null };
            try {
                this.postRunnable(new _$$Lambda$PhotoFilterView$EGLThread$mV7yxJKIkka2xtURA3o1r9_1nD8(this, array, countDownLatch));
                countDownLatch.await();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            return array[0];
        }
        
        public void requestRender(final boolean b) {
            this.requestRender(b, false);
        }
        
        public void requestRender(final boolean b, final boolean b2) {
            this.postRunnable(new _$$Lambda$PhotoFilterView$EGLThread$MrJUzcEqH3ADgrrNUt03oFMBD_4(this, b, b2));
        }
        
        @Override
        public void run() {
            this.initied = this.initGL();
            super.run();
        }
        
        public void setSurfaceTextureSize(final int surfaceWidth, final int surfaceHeight) {
            this.surfaceWidth = surfaceWidth;
            this.surfaceHeight = surfaceHeight;
        }
        
        public void shutdown() {
            this.postRunnable(new _$$Lambda$PhotoFilterView$EGLThread$UPEak5EYsUntGACDTA79Biksn_k(this));
        }
    }
    
    public class ToolsAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ToolsAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return 13;
        }
        
        @Override
        public long getItemId(final int n) {
            return n;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n != PhotoFilterView.this.tintShadowsTool && n != PhotoFilterView.this.tintHighlightsTool) {
                return 0;
            }
            return 1;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    final PhotoEditRadioCell photoEditRadioCell = (PhotoEditRadioCell)viewHolder.itemView;
                    photoEditRadioCell.setTag((Object)n);
                    if (n == PhotoFilterView.this.tintShadowsTool) {
                        photoEditRadioCell.setIconAndTextAndValue(LocaleController.getString("TintShadows", 2131560906), 0, PhotoFilterView.this.tintShadowsColor);
                    }
                    else if (n == PhotoFilterView.this.tintHighlightsTool) {
                        photoEditRadioCell.setIconAndTextAndValue(LocaleController.getString("TintHighlights", 2131560905), 0, PhotoFilterView.this.tintHighlightsColor);
                    }
                }
            }
            else {
                final PhotoEditToolCell photoEditToolCell = (PhotoEditToolCell)viewHolder.itemView;
                photoEditToolCell.setTag(n);
                if (n == PhotoFilterView.this.enhanceTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Enhance", 2131559366), PhotoFilterView.this.enhanceValue, 0, 100);
                }
                else if (n == PhotoFilterView.this.highlightsTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Highlights", 2131559638), PhotoFilterView.this.highlightsValue, -100, 100);
                }
                else if (n == PhotoFilterView.this.contrastTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Contrast", 2131559155), PhotoFilterView.this.contrastValue, -100, 100);
                }
                else if (n == PhotoFilterView.this.exposureTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Exposure", 2131559475), PhotoFilterView.this.exposureValue, -100, 100);
                }
                else if (n == PhotoFilterView.this.warmthTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Warmth", 2131561103), PhotoFilterView.this.warmthValue, -100, 100);
                }
                else if (n == PhotoFilterView.this.saturationTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Saturation", 2131560625), PhotoFilterView.this.saturationValue, -100, 100);
                }
                else if (n == PhotoFilterView.this.vignetteTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Vignette", 2131561055), PhotoFilterView.this.vignetteValue, 0, 100);
                }
                else if (n == PhotoFilterView.this.shadowsTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Shadows", 2131560745), PhotoFilterView.this.shadowsValue, -100, 100);
                }
                else if (n == PhotoFilterView.this.grainTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Grain", 2131559598), PhotoFilterView.this.grainValue, 0, 100);
                }
                else if (n == PhotoFilterView.this.sharpenTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Sharpen", 2131560774), PhotoFilterView.this.sharpenValue, 0, 100);
                }
                else if (n == PhotoFilterView.this.fadeTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Fade", 2131559477), PhotoFilterView.this.fadeValue, 0, 100);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n == 0) {
                frameLayout = new PhotoEditToolCell(this.mContext);
                ((PhotoEditToolCell)frameLayout).setSeekBarDelegate(new _$$Lambda$PhotoFilterView$ToolsAdapter$y8L3fL76Xdh9BIlSQihYImp1GRU(this));
            }
            else {
                frameLayout = new PhotoEditRadioCell(this.mContext);
                ((PhotoEditRadioCell)frameLayout).setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoFilterView$ToolsAdapter$l6jhbYVDCcFuEfXfCcHJenobMmo(this));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
}
