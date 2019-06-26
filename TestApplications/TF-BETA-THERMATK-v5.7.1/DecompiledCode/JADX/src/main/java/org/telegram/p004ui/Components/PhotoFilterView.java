package org.telegram.p004ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Build.VERSION;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.android.exoplayer2.util.NalUnitUtil;
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
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController.SavedFilterState;
import org.telegram.messenger.Utilities;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Cells.PhotoEditRadioCell;
import org.telegram.p004ui.Cells.PhotoEditToolCell;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;

@SuppressLint({"NewApi"})
/* renamed from: org.telegram.ui.Components.PhotoFilterView */
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
    private CurvesToolValue curvesToolValue;
    private TextView doneTextView;
    private EGLThread eglThread;
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
    private SavedFilterState lastState;
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

    /* renamed from: org.telegram.ui.Components.PhotoFilterView$1 */
    class C29121 implements SurfaceTextureListener {
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        C29121() {
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            if (PhotoFilterView.this.eglThread == null && surfaceTexture != null) {
                PhotoFilterView photoFilterView = PhotoFilterView.this;
                photoFilterView.eglThread = new EGLThread(surfaceTexture, photoFilterView.bitmapToEdit);
                PhotoFilterView.this.eglThread.setSurfaceTextureSize(i, i2);
                PhotoFilterView.this.eglThread.requestRender(true, true);
            }
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            if (PhotoFilterView.this.eglThread != null) {
                PhotoFilterView.this.eglThread.setSurfaceTextureSize(i, i2);
                PhotoFilterView.this.eglThread.requestRender(false, true);
                PhotoFilterView.this.eglThread.postRunnable(new C2615-$$Lambda$PhotoFilterView$1$ykukh_-An3a5DPXda05svw9B55U(this));
            }
        }

        public /* synthetic */ void lambda$onSurfaceTextureSizeChanged$0$PhotoFilterView$1() {
            if (PhotoFilterView.this.eglThread != null) {
                PhotoFilterView.this.eglThread.requestRender(false, true);
            }
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            if (PhotoFilterView.this.eglThread != null) {
                PhotoFilterView.this.eglThread.shutdown();
                PhotoFilterView.this.eglThread = null;
            }
            return true;
        }
    }

    /* renamed from: org.telegram.ui.Components.PhotoFilterView$CurvesToolValue */
    public static class CurvesToolValue {
        public static final int CurvesTypeBlue = 3;
        public static final int CurvesTypeGreen = 2;
        public static final int CurvesTypeLuminance = 0;
        public static final int CurvesTypeRed = 1;
        public int activeType;
        public CurvesValue blueCurve = new CurvesValue();
        public ByteBuffer curveBuffer = ByteBuffer.allocateDirect(800);
        public CurvesValue greenCurve = new CurvesValue();
        public CurvesValue luminanceCurve = new CurvesValue();
        public CurvesValue redCurve = new CurvesValue();

        public CurvesToolValue() {
            this.curveBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }

        public void fillBuffer() {
            this.curveBuffer.position(0);
            float[] dataPoints = this.luminanceCurve.getDataPoints();
            float[] dataPoints2 = this.redCurve.getDataPoints();
            float[] dataPoints3 = this.greenCurve.getDataPoints();
            float[] dataPoints4 = this.blueCurve.getDataPoints();
            for (int i = 0; i < Callback.DEFAULT_DRAG_ANIMATION_DURATION; i++) {
                this.curveBuffer.put((byte) ((int) (dataPoints2[i] * 255.0f)));
                this.curveBuffer.put((byte) ((int) (dataPoints3[i] * 255.0f)));
                this.curveBuffer.put((byte) ((int) (dataPoints4[i] * 255.0f)));
                this.curveBuffer.put((byte) ((int) (dataPoints[i] * 255.0f)));
            }
            this.curveBuffer.position(0);
        }

        public boolean shouldBeSkipped() {
            return this.luminanceCurve.isDefault() && this.redCurve.isDefault() && this.greenCurve.isDefault() && this.blueCurve.isDefault();
        }
    }

    /* renamed from: org.telegram.ui.Components.PhotoFilterView$CurvesValue */
    public static class CurvesValue {
        public float blacksLevel = 0.0f;
        public float[] cachedDataPoints;
        public float highlightsLevel = 75.0f;
        public float midtonesLevel = 50.0f;
        public float previousBlacksLevel = 0.0f;
        public float previousHighlightsLevel = 75.0f;
        public float previousMidtonesLevel = 50.0f;
        public float previousShadowsLevel = 25.0f;
        public float previousWhitesLevel = 100.0f;
        public float shadowsLevel = 25.0f;
        public float whitesLevel = 100.0f;

        public float[] getDataPoints() {
            if (this.cachedDataPoints == null) {
                interpolateCurve();
            }
            return this.cachedDataPoints;
        }

        public void saveValues() {
            this.previousBlacksLevel = this.blacksLevel;
            this.previousShadowsLevel = this.shadowsLevel;
            this.previousMidtonesLevel = this.midtonesLevel;
            this.previousHighlightsLevel = this.highlightsLevel;
            this.previousWhitesLevel = this.whitesLevel;
        }

        public void restoreValues() {
            this.blacksLevel = this.previousBlacksLevel;
            this.shadowsLevel = this.previousShadowsLevel;
            this.midtonesLevel = this.previousMidtonesLevel;
            this.highlightsLevel = this.previousHighlightsLevel;
            this.whitesLevel = this.previousWhitesLevel;
            interpolateCurve();
        }

        public float[] interpolateCurve() {
            int i;
            r1 = new float[14];
            float f = this.blacksLevel;
            int i2 = 1;
            r1[1] = f / 100.0f;
            r1[2] = 0.0f;
            r1[3] = f / 100.0f;
            r1[4] = 0.25f;
            r1[5] = this.shadowsLevel / 100.0f;
            f = 0.5f;
            r1[6] = 0.5f;
            r1[7] = this.midtonesLevel / 100.0f;
            r1[8] = 0.75f;
            r1[9] = this.highlightsLevel / 100.0f;
            r1[10] = 1.0f;
            float f2 = this.whitesLevel;
            r1[11] = f2 / 100.0f;
            r1[12] = 1.001f;
            r1[13] = f2 / 100.0f;
            ArrayList arrayList = new ArrayList(100);
            ArrayList arrayList2 = new ArrayList(100);
            arrayList2.add(Float.valueOf(r1[0]));
            arrayList2.add(Float.valueOf(r1[1]));
            int i3 = 1;
            while (i3 < (r1.length / 2) - 2) {
                int i4 = (i3 - 1) * 2;
                float f3 = r1[i4];
                float f4 = r1[i4 + i2];
                int i5 = i3 * 2;
                float f5 = r1[i5];
                float f6 = r1[i5 + 1];
                int i6 = i3 + 1;
                int i7 = i6 * 2;
                float f7 = r1[i7];
                float f8 = r1[i7 + 1];
                i3 = (i3 + 2) * 2;
                float f9 = r1[i3];
                float f10 = r1[i3 + i2];
                i = 1;
                while (i < 100) {
                    float f11 = ((float) i) * 0.01f;
                    float f12 = f11 * f11;
                    float f13 = f12 * f11;
                    float f14 = ((((f5 * 2.0f) + ((f7 - f3) * f11)) + (((((f3 * 2.0f) - (f5 * 5.0f)) + (f7 * 4.0f)) - f9) * f12)) + (((((f5 * 3.0f) - f3) - (f7 * 3.0f)) + f9) * f13)) * f;
                    f11 = Math.max(0.0f, Math.min(1.0f, ((((f6 * 2.0f) + ((f8 - f4) * f11)) + (((((2.0f * f4) - (5.0f * f6)) + (4.0f * f8)) - f10) * f12)) + (((((f6 * 3.0f) - f4) - (3.0f * f8)) + f10) * f13)) * f));
                    if (f14 > f3) {
                        arrayList2.add(Float.valueOf(f14));
                        arrayList2.add(Float.valueOf(f11));
                    }
                    if ((i - 1) % 2 == 0) {
                        arrayList.add(Float.valueOf(f11));
                    }
                    i++;
                    f = 0.5f;
                }
                arrayList2.add(Float.valueOf(f7));
                arrayList2.add(Float.valueOf(f8));
                i3 = i6;
                f = 0.5f;
                i2 = 1;
            }
            arrayList2.add(Float.valueOf(r1[12]));
            arrayList2.add(Float.valueOf(r1[13]));
            this.cachedDataPoints = new float[arrayList.size()];
            int i8 = 0;
            while (true) {
                float[] fArr = this.cachedDataPoints;
                if (i8 >= fArr.length) {
                    break;
                }
                fArr[i8] = ((Float) arrayList.get(i8)).floatValue();
                i8++;
            }
            r1 = new float[arrayList2.size()];
            for (i = 0; i < r1.length; i++) {
                r1[i] = ((Float) arrayList2.get(i)).floatValue();
            }
            return r1;
        }

        public boolean isDefault() {
            return ((double) Math.abs(this.blacksLevel - 0.0f)) < 1.0E-5d && ((double) Math.abs(this.shadowsLevel - 25.0f)) < 1.0E-5d && ((double) Math.abs(this.midtonesLevel - 50.0f)) < 1.0E-5d && ((double) Math.abs(this.highlightsLevel - 75.0f)) < 1.0E-5d && ((double) Math.abs(this.whitesLevel - 100.0f)) < 1.0E-5d;
        }
    }

    /* renamed from: org.telegram.ui.Components.PhotoFilterView$EGLThread */
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
        private Runnable drawRunnable = new C29131();
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
        /* renamed from: gl */
        private GL f599gl;
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

        /* renamed from: org.telegram.ui.Components.PhotoFilterView$EGLThread$1 */
        class C29131 implements Runnable {
            C29131() {
            }

            public void run() {
                if (!EGLThread.this.initied) {
                    return;
                }
                if ((EGLThread.this.eglContext.equals(EGLThread.this.egl10.eglGetCurrentContext()) && EGLThread.this.eglSurface.equals(EGLThread.this.egl10.eglGetCurrentSurface(12377))) || EGLThread.this.egl10.eglMakeCurrent(EGLThread.this.eglDisplay, EGLThread.this.eglSurface, EGLThread.this.eglSurface, EGLThread.this.eglContext)) {
                    GLES20.glViewport(0, 0, EGLThread.this.renderBufferWidth, EGLThread.this.renderBufferHeight);
                    EGLThread.this.drawEnhancePass();
                    EGLThread.this.drawSharpenPass();
                    EGLThread.this.drawCustomParamsPass();
                    EGLThread eGLThread = EGLThread.this;
                    eGLThread.blured = eGLThread.drawBlurPass();
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
                    return;
                }
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("eglMakeCurrent failed ");
                    stringBuilder.append(GLUtils.getEGLErrorString(EGLThread.this.egl10.eglGetError()));
                    FileLog.m28e(stringBuilder.toString());
                }
            }
        }

        public EGLThread(SurfaceTexture surfaceTexture, Bitmap bitmap) {
            super("EGLThread");
            this.surfaceTexture = surfaceTexture;
            this.currentBitmap = bitmap;
        }

        private int loadShader(int i, String str) {
            i = GLES20.glCreateShader(i);
            GLES20.glShaderSource(i, str);
            GLES20.glCompileShader(i);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(i, 35713, iArr, 0);
            if (iArr[0] != 0) {
                return i;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m28e(GLES20.glGetShaderInfoLog(i));
            }
            GLES20.glDeleteShader(i);
            return 0;
        }

        private boolean initGL() {
            this.egl10 = (EGL10) EGLContext.getEGL();
            this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            EGLDisplay eGLDisplay = this.eglDisplay;
            StringBuilder stringBuilder;
            if (eGLDisplay == EGL10.EGL_NO_DISPLAY) {
                if (BuildVars.LOGS_ENABLED) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("eglGetDisplay failed ");
                    stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.m28e(stringBuilder.toString());
                }
                finish();
                return false;
            }
            if (this.egl10.eglInitialize(eGLDisplay, new int[2])) {
                int[] iArr = new int[1];
                EGLConfig[] eGLConfigArr = new EGLConfig[1];
                if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12326, 0, 12344}, eGLConfigArr, 1, iArr)) {
                    if (BuildVars.LOGS_ENABLED) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("eglChooseConfig failed ");
                        stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        FileLog.m28e(stringBuilder.toString());
                    }
                    finish();
                    return false;
                } else if (iArr[0] > 0) {
                    this.eglConfig = eGLConfigArr[0];
                    this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
                    if (this.eglContext == null) {
                        if (BuildVars.LOGS_ENABLED) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("eglCreateContext failed ");
                            stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                            FileLog.m28e(stringBuilder.toString());
                        }
                        finish();
                        return false;
                    }
                    SurfaceTexture surfaceTexture = this.surfaceTexture;
                    if (surfaceTexture instanceof SurfaceTexture) {
                        this.eglSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, surfaceTexture, null);
                        EGLSurface eGLSurface = this.eglSurface;
                        if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("createWindowSurface failed ");
                                stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                FileLog.m28e(stringBuilder.toString());
                            }
                            finish();
                            return false;
                        } else if (this.egl10.eglMakeCurrent(this.eglDisplay, eGLSurface, eGLSurface, this.eglContext)) {
                            this.f599gl = this.eglContext.getGL();
                            float[] fArr = new float[]{-1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f};
                            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
                            allocateDirect.order(ByteOrder.nativeOrder());
                            this.vertexBuffer = allocateDirect.asFloatBuffer();
                            this.vertexBuffer.put(fArr);
                            this.vertexBuffer.position(0);
                            fArr = new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
                            allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
                            allocateDirect.order(ByteOrder.nativeOrder());
                            this.vertexInvertBuffer = allocateDirect.asFloatBuffer();
                            this.vertexInvertBuffer.put(fArr);
                            this.vertexInvertBuffer.position(0);
                            float[] fArr2 = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
                            ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(fArr2.length * 4);
                            allocateDirect2.order(ByteOrder.nativeOrder());
                            this.textureBuffer = allocateDirect2.asFloatBuffer();
                            this.textureBuffer.put(fArr2);
                            this.textureBuffer.position(0);
                            GLES20.glGenTextures(1, this.curveTextures, 0);
                            GLES20.glGenTextures(2, this.enhanceTextures, 0);
                            String str = simpleVertexShaderCode;
                            int loadShader = loadShader(35633, str);
                            int loadShader2 = loadShader(35632, toolsFragmentShaderCode);
                            if (loadShader == 0 || loadShader2 == 0) {
                                finish();
                                return false;
                            }
                            this.toolsShaderProgram = GLES20.glCreateProgram();
                            GLES20.glAttachShader(this.toolsShaderProgram, loadShader);
                            GLES20.glAttachShader(this.toolsShaderProgram, loadShader2);
                            String str2 = "position";
                            GLES20.glBindAttribLocation(this.toolsShaderProgram, 0, str2);
                            String str3 = "inputTexCoord";
                            GLES20.glBindAttribLocation(this.toolsShaderProgram, 1, str3);
                            GLES20.glLinkProgram(this.toolsShaderProgram);
                            int[] iArr2 = new int[1];
                            GLES20.glGetProgramiv(this.toolsShaderProgram, 35714, iArr2, 0);
                            String str4 = "sourceImage";
                            if (iArr2[0] == 0) {
                                GLES20.glDeleteProgram(this.toolsShaderProgram);
                                this.toolsShaderProgram = 0;
                            } else {
                                this.positionHandle = GLES20.glGetAttribLocation(this.toolsShaderProgram, str2);
                                this.inputTexCoordHandle = GLES20.glGetAttribLocation(this.toolsShaderProgram, str3);
                                this.sourceImageHandle = GLES20.glGetUniformLocation(this.toolsShaderProgram, str4);
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
                            loadShader = loadShader(35633, sharpenVertexShaderCode);
                            int loadShader3 = loadShader(35632, sharpenFragmentShaderCode);
                            if (loadShader == 0 || loadShader3 == 0) {
                                finish();
                                return false;
                            }
                            this.sharpenShaderProgram = GLES20.glCreateProgram();
                            GLES20.glAttachShader(this.sharpenShaderProgram, loadShader);
                            GLES20.glAttachShader(this.sharpenShaderProgram, loadShader3);
                            GLES20.glBindAttribLocation(this.sharpenShaderProgram, 0, str2);
                            GLES20.glBindAttribLocation(this.sharpenShaderProgram, 1, str3);
                            GLES20.glLinkProgram(this.sharpenShaderProgram);
                            iArr2 = new int[1];
                            GLES20.glGetProgramiv(this.sharpenShaderProgram, 35714, iArr2, 0);
                            if (iArr2[0] == 0) {
                                GLES20.glDeleteProgram(this.sharpenShaderProgram);
                                this.sharpenShaderProgram = 0;
                            } else {
                                this.sharpenPositionHandle = GLES20.glGetAttribLocation(this.sharpenShaderProgram, str2);
                                this.sharpenInputTexCoordHandle = GLES20.glGetAttribLocation(this.sharpenShaderProgram, str3);
                                this.sharpenSourceImageHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, str4);
                                this.sharpenWidthHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "inputWidth");
                                this.sharpenHeightHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "inputHeight");
                                this.sharpenHandle = GLES20.glGetUniformLocation(this.sharpenShaderProgram, "sharpen");
                            }
                            loadShader = loadShader(35633, blurVertexShaderCode);
                            loadShader3 = loadShader(35632, blurFragmentShaderCode);
                            if (loadShader == 0 || loadShader3 == 0) {
                                finish();
                                return false;
                            }
                            this.blurShaderProgram = GLES20.glCreateProgram();
                            GLES20.glAttachShader(this.blurShaderProgram, loadShader);
                            GLES20.glAttachShader(this.blurShaderProgram, loadShader3);
                            GLES20.glBindAttribLocation(this.blurShaderProgram, 0, str2);
                            GLES20.glBindAttribLocation(this.blurShaderProgram, 1, str3);
                            GLES20.glLinkProgram(this.blurShaderProgram);
                            iArr2 = new int[1];
                            GLES20.glGetProgramiv(this.blurShaderProgram, 35714, iArr2, 0);
                            if (iArr2[0] == 0) {
                                GLES20.glDeleteProgram(this.blurShaderProgram);
                                this.blurShaderProgram = 0;
                            } else {
                                this.blurPositionHandle = GLES20.glGetAttribLocation(this.blurShaderProgram, str2);
                                this.blurInputTexCoordHandle = GLES20.glGetAttribLocation(this.blurShaderProgram, str3);
                                this.blurSourceImageHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, str4);
                                this.blurWidthHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "texelWidthOffset");
                                this.blurHeightHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "texelHeightOffset");
                            }
                            loadShader = loadShader(35633, str);
                            loadShader3 = loadShader(35632, linearBlurFragmentShaderCode);
                            if (loadShader == 0 || loadShader3 == 0) {
                                finish();
                                return false;
                            }
                            this.linearBlurShaderProgram = GLES20.glCreateProgram();
                            GLES20.glAttachShader(this.linearBlurShaderProgram, loadShader);
                            GLES20.glAttachShader(this.linearBlurShaderProgram, loadShader3);
                            GLES20.glBindAttribLocation(this.linearBlurShaderProgram, 0, str2);
                            GLES20.glBindAttribLocation(this.linearBlurShaderProgram, 1, str3);
                            GLES20.glLinkProgram(this.linearBlurShaderProgram);
                            iArr2 = new int[1];
                            GLES20.glGetProgramiv(this.linearBlurShaderProgram, 35714, iArr2, 0);
                            String str5 = "excludePoint";
                            String str6 = "excludeSize";
                            String str7 = "inputImageTexture2";
                            if (iArr2[0] == 0) {
                                GLES20.glDeleteProgram(this.linearBlurShaderProgram);
                                this.linearBlurShaderProgram = 0;
                            } else {
                                this.linearBlurPositionHandle = GLES20.glGetAttribLocation(this.linearBlurShaderProgram, str2);
                                this.linearBlurInputTexCoordHandle = GLES20.glGetAttribLocation(this.linearBlurShaderProgram, str3);
                                this.linearBlurSourceImageHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, str4);
                                this.linearBlurSourceImage2Handle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, str7);
                                this.linearBlurExcludeSizeHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, str6);
                                this.linearBlurExcludePointHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, str5);
                                this.linearBlurExcludeBlurSizeHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "excludeBlurSize");
                                this.linearBlurAngleHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "angle");
                                this.linearBlurAspectRatioHandle = GLES20.glGetUniformLocation(this.linearBlurShaderProgram, "aspectRatio");
                            }
                            loadShader = loadShader(35633, str);
                            int loadShader4 = loadShader(35632, radialBlurFragmentShaderCode);
                            if (loadShader == 0 || loadShader4 == 0) {
                                finish();
                                return false;
                            }
                            this.radialBlurShaderProgram = GLES20.glCreateProgram();
                            GLES20.glAttachShader(this.radialBlurShaderProgram, loadShader);
                            GLES20.glAttachShader(this.radialBlurShaderProgram, loadShader4);
                            GLES20.glBindAttribLocation(this.radialBlurShaderProgram, 0, str2);
                            GLES20.glBindAttribLocation(this.radialBlurShaderProgram, 1, str3);
                            GLES20.glLinkProgram(this.radialBlurShaderProgram);
                            iArr2 = new int[1];
                            GLES20.glGetProgramiv(this.radialBlurShaderProgram, 35714, iArr2, 0);
                            if (iArr2[0] == 0) {
                                GLES20.glDeleteProgram(this.radialBlurShaderProgram);
                                this.radialBlurShaderProgram = 0;
                            } else {
                                this.radialBlurPositionHandle = GLES20.glGetAttribLocation(this.radialBlurShaderProgram, str2);
                                this.radialBlurInputTexCoordHandle = GLES20.glGetAttribLocation(this.radialBlurShaderProgram, str3);
                                this.radialBlurSourceImageHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, str4);
                                this.radialBlurSourceImage2Handle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, str7);
                                this.radialBlurExcludeSizeHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, str6);
                                this.radialBlurExcludePointHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, str5);
                                this.radialBlurExcludeBlurSizeHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "excludeBlurSize");
                                this.radialBlurAspectRatioHandle = GLES20.glGetUniformLocation(this.radialBlurShaderProgram, "aspectRatio");
                            }
                            loadShader = loadShader(35633, str);
                            loadShader3 = loadShader(35632, rgbToHsvFragmentShaderCode);
                            if (loadShader == 0 || loadShader3 == 0) {
                                finish();
                                return false;
                            }
                            this.rgbToHsvShaderProgram = GLES20.glCreateProgram();
                            GLES20.glAttachShader(this.rgbToHsvShaderProgram, loadShader);
                            GLES20.glAttachShader(this.rgbToHsvShaderProgram, loadShader3);
                            GLES20.glBindAttribLocation(this.rgbToHsvShaderProgram, 0, str2);
                            GLES20.glBindAttribLocation(this.rgbToHsvShaderProgram, 1, str3);
                            GLES20.glLinkProgram(this.rgbToHsvShaderProgram);
                            iArr2 = new int[1];
                            GLES20.glGetProgramiv(this.rgbToHsvShaderProgram, 35714, iArr2, 0);
                            if (iArr2[0] == 0) {
                                GLES20.glDeleteProgram(this.rgbToHsvShaderProgram);
                                this.rgbToHsvShaderProgram = 0;
                            } else {
                                this.rgbToHsvPositionHandle = GLES20.glGetAttribLocation(this.rgbToHsvShaderProgram, str2);
                                this.rgbToHsvInputTexCoordHandle = GLES20.glGetAttribLocation(this.rgbToHsvShaderProgram, str3);
                                this.rgbToHsvSourceImageHandle = GLES20.glGetUniformLocation(this.rgbToHsvShaderProgram, str4);
                            }
                            loadShader = loadShader(35633, str);
                            loadShader3 = loadShader(35632, enhanceFragmentShaderCode);
                            if (loadShader == 0 || loadShader3 == 0) {
                                finish();
                                return false;
                            }
                            this.enhanceShaderProgram = GLES20.glCreateProgram();
                            GLES20.glAttachShader(this.enhanceShaderProgram, loadShader);
                            GLES20.glAttachShader(this.enhanceShaderProgram, loadShader3);
                            GLES20.glBindAttribLocation(this.enhanceShaderProgram, 0, str2);
                            GLES20.glBindAttribLocation(this.enhanceShaderProgram, 1, str3);
                            GLES20.glLinkProgram(this.enhanceShaderProgram);
                            iArr2 = new int[1];
                            GLES20.glGetProgramiv(this.enhanceShaderProgram, 35714, iArr2, 0);
                            if (iArr2[0] == 0) {
                                GLES20.glDeleteProgram(this.enhanceShaderProgram);
                                this.enhanceShaderProgram = 0;
                            } else {
                                this.enhancePositionHandle = GLES20.glGetAttribLocation(this.enhanceShaderProgram, str2);
                                this.enhanceInputTexCoordHandle = GLES20.glGetAttribLocation(this.enhanceShaderProgram, str3);
                                this.enhanceSourceImageHandle = GLES20.glGetUniformLocation(this.enhanceShaderProgram, str4);
                                this.enhanceIntensityHandle = GLES20.glGetUniformLocation(this.enhanceShaderProgram, "intensity");
                                this.enhanceInputImageTexture2Handle = GLES20.glGetUniformLocation(this.enhanceShaderProgram, str7);
                            }
                            int loadShader5 = loadShader(35633, str);
                            int loadShader6 = loadShader(35632, simpleFragmentShaderCode);
                            if (loadShader5 == 0 || loadShader6 == 0) {
                                finish();
                                return false;
                            }
                            this.simpleShaderProgram = GLES20.glCreateProgram();
                            GLES20.glAttachShader(this.simpleShaderProgram, loadShader5);
                            GLES20.glAttachShader(this.simpleShaderProgram, loadShader6);
                            GLES20.glBindAttribLocation(this.simpleShaderProgram, 0, str2);
                            GLES20.glBindAttribLocation(this.simpleShaderProgram, 1, str3);
                            GLES20.glLinkProgram(this.simpleShaderProgram);
                            int[] iArr3 = new int[1];
                            GLES20.glGetProgramiv(this.simpleShaderProgram, 35714, iArr3, 0);
                            if (iArr3[0] == 0) {
                                GLES20.glDeleteProgram(this.simpleShaderProgram);
                                this.simpleShaderProgram = 0;
                            } else {
                                this.simplePositionHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, str2);
                                this.simpleInputTexCoordHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, str3);
                                this.simpleSourceImageHandle = GLES20.glGetUniformLocation(this.simpleShaderProgram, str4);
                            }
                            Bitmap bitmap = this.currentBitmap;
                            if (!(bitmap == null || bitmap.isRecycled())) {
                                loadTexture(this.currentBitmap);
                            }
                            return true;
                        } else {
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("eglMakeCurrent failed ");
                                stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                FileLog.m28e(stringBuilder.toString());
                            }
                            finish();
                            return false;
                        }
                    }
                    finish();
                    return false;
                } else {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.m28e("eglConfig not initialized");
                    }
                    finish();
                    return false;
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("eglInitialize failed ");
                stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                FileLog.m28e(stringBuilder.toString());
            }
            finish();
            return false;
        }

        public void finish() {
            if (this.eglSurface != null) {
                EGL10 egl10 = this.egl10;
                EGLDisplay eGLDisplay = this.eglDisplay;
                EGLSurface eGLSurface = EGL10.EGL_NO_SURFACE;
                egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL10.EGL_NO_CONTEXT);
                this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
                this.eglSurface = null;
            }
            EGLContext eGLContext = this.eglContext;
            if (eGLContext != null) {
                this.egl10.eglDestroyContext(this.eglDisplay, eGLContext);
                this.eglContext = null;
            }
            EGLDisplay eGLDisplay2 = this.eglDisplay;
            if (eGLDisplay2 != null) {
                this.egl10.eglTerminate(eGLDisplay2);
                this.eglDisplay = null;
            }
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
                Buffer allocateDirect = ByteBuffer.allocateDirect((this.renderBufferWidth * this.renderBufferHeight) * 4);
                GLES20.glReadPixels(0, 0, this.renderBufferWidth, this.renderBufferHeight, 6408, 5121, allocateDirect);
                GLES20.glBindTexture(3553, this.enhanceTextures[0]);
                GLES20.glTexParameteri(3553, 10241, 9729);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10242, 33071);
                GLES20.glTexParameteri(3553, 10243, 33071);
                GLES20.glTexImage2D(3553, 0, 6408, this.renderBufferWidth, this.renderBufferHeight, 0, 6408, 5121, allocateDirect);
                Buffer buffer = null;
                try {
                    buffer = ByteBuffer.allocateDirect(16384);
                    Utilities.calcCDT(allocateDirect, this.renderBufferWidth, this.renderBufferHeight, buffer);
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
                Buffer buffer2 = buffer;
                GLES20.glBindTexture(3553, this.enhanceTextures[1]);
                GLES20.glTexParameteri(3553, 10241, 9729);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10242, 33071);
                GLES20.glTexParameteri(3553, 10243, 33071);
                GLES20.glTexImage2D(3553, 0, 6408, 256, 16, 0, 6408, 5121, buffer2);
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
                GLES20.glUniform1f(this.sharpenHandle, 0.0f);
            } else {
                GLES20.glUniform1f(this.sharpenHandle, PhotoFilterView.this.getSharpenValue());
            }
            GLES20.glUniform1f(this.sharpenWidthHandle, (float) this.renderBufferWidth);
            GLES20.glUniform1f(this.sharpenHeightHandle, (float) this.renderBufferHeight);
            GLES20.glEnableVertexAttribArray(this.sharpenInputTexCoordHandle);
            GLES20.glVertexAttribPointer(this.sharpenInputTexCoordHandle, 2, 5126, false, 8, this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.sharpenPositionHandle);
            GLES20.glVertexAttribPointer(this.sharpenPositionHandle, 2, 5126, false, 8, this.vertexInvertBuffer);
            GLES20.glDrawArrays(5, 0, 4);
        }

        private void drawCustomParamsPass() {
            GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[1]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[1], 0);
            GLES20.glClear(0);
            GLES20.glUseProgram(this.toolsShaderProgram);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.renderTexture[0]);
            GLES20.glUniform1i(this.sourceImageHandle, 0);
            float f = 1.0f;
            if (PhotoFilterView.this.showOriginal) {
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
                GLES20.glUniform3f(this.highlightsTintColorHandle, ((float) ((PhotoFilterView.this.tintHighlightsColor >> 16) & NalUnitUtil.EXTENDED_SAR)) / 255.0f, ((float) ((PhotoFilterView.this.tintHighlightsColor >> 8) & NalUnitUtil.EXTENDED_SAR)) / 255.0f, ((float) (PhotoFilterView.this.tintHighlightsColor & NalUnitUtil.EXTENDED_SAR)) / 255.0f);
                GLES20.glUniform1f(this.highlightsTintIntensityHandle, PhotoFilterView.this.getTintHighlightsIntensityValue());
                GLES20.glUniform3f(this.shadowsTintColorHandle, ((float) ((PhotoFilterView.this.tintShadowsColor >> 16) & NalUnitUtil.EXTENDED_SAR)) / 255.0f, ((float) ((PhotoFilterView.this.tintShadowsColor >> 8) & NalUnitUtil.EXTENDED_SAR)) / 255.0f, ((float) (PhotoFilterView.this.tintShadowsColor & NalUnitUtil.EXTENDED_SAR)) / 255.0f);
                GLES20.glUniform1f(this.shadowsTintIntensityHandle, PhotoFilterView.this.getTintShadowsIntensityValue());
                boolean shouldBeSkipped = PhotoFilterView.this.curvesToolValue.shouldBeSkipped();
                int i = this.skipToneHandle;
                if (!shouldBeSkipped) {
                    f = 0.0f;
                }
                GLES20.glUniform1f(i, f);
                if (!shouldBeSkipped) {
                    PhotoFilterView.this.curvesToolValue.fillBuffer();
                    GLES20.glActiveTexture(33985);
                    GLES20.glBindTexture(3553, this.curveTextures[0]);
                    GLES20.glTexParameteri(3553, 10241, 9729);
                    GLES20.glTexParameteri(3553, 10240, 9729);
                    GLES20.glTexParameteri(3553, 10242, 33071);
                    GLES20.glTexParameteri(3553, 10243, 33071);
                    GLES20.glTexImage2D(3553, 0, 6408, Callback.DEFAULT_DRAG_ANIMATION_DURATION, 1, 0, 6408, 5121, PhotoFilterView.this.curvesToolValue.curveBuffer);
                    GLES20.glUniform1i(this.curvesImageHandle, 1);
                }
            }
            GLES20.glUniform1f(this.widthHandle, (float) this.renderBufferWidth);
            GLES20.glUniform1f(this.heightHandle, (float) this.renderBufferHeight);
            GLES20.glEnableVertexAttribArray(this.inputTexCoordHandle);
            GLES20.glVertexAttribPointer(this.inputTexCoordHandle, 2, 5126, false, 8, this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.positionHandle);
            GLES20.glVertexAttribPointer(this.positionHandle, 2, 5126, false, 8, this.vertexInvertBuffer);
            GLES20.glDrawArrays(5, 0, 4);
        }

        private boolean drawBlurPass() {
            if (PhotoFilterView.this.showOriginal || PhotoFilterView.this.blurType == 0) {
                return false;
            }
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
                GLES20.glUniform1f(this.blurWidthHandle, 0.0f);
                GLES20.glUniform1f(this.blurHeightHandle, 1.0f / ((float) this.renderBufferHeight));
                GLES20.glDrawArrays(5, 0, 4);
                GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[2]);
                GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[2], 0);
                GLES20.glClear(0);
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, this.renderTexture[0]);
                GLES20.glUniform1f(this.blurWidthHandle, 1.0f / ((float) this.renderBufferWidth));
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
                GLES20.glUniform2f(this.radialBlurExcludePointHandle, PhotoFilterView.this.blurExcludePoint.f600x, PhotoFilterView.this.blurExcludePoint.f601y);
                GLES20.glUniform1f(this.radialBlurAspectRatioHandle, ((float) this.renderBufferHeight) / ((float) this.renderBufferWidth));
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
                GLES20.glUniform2f(this.linearBlurExcludePointHandle, PhotoFilterView.this.blurExcludePoint.f600x, PhotoFilterView.this.blurExcludePoint.f601y);
                GLES20.glUniform1f(this.linearBlurAspectRatioHandle, ((float) this.renderBufferHeight) / ((float) this.renderBufferWidth));
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
        }

        private Bitmap getRenderBufferBitmap() {
            Buffer allocateDirect = ByteBuffer.allocateDirect((this.renderBufferWidth * this.renderBufferHeight) * 4);
            GLES20.glReadPixels(0, 0, this.renderBufferWidth, this.renderBufferHeight, 6408, 5121, allocateDirect);
            Bitmap createBitmap = Bitmap.createBitmap(this.renderBufferWidth, this.renderBufferHeight, Config.ARGB_8888);
            createBitmap.copyPixelsFromBuffer(allocateDirect);
            return createBitmap;
        }

        public Bitmap getTexture() {
            if (!this.initied) {
                return null;
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Bitmap[] bitmapArr = new Bitmap[1];
            try {
                postRunnable(new C2620-$$Lambda$PhotoFilterView$EGLThread$mV7yxJKIkka2xtURA3o1r9_1nD8(this, bitmapArr, countDownLatch));
                countDownLatch.await();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            return bitmapArr[0];
        }

        public /* synthetic */ void lambda$getTexture$0$PhotoFilterView$EGLThread(Bitmap[] bitmapArr, CountDownLatch countDownLatch) {
            GLES20.glBindFramebuffer(36160, this.renderFrameBuffer[1]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.renderTexture[1 ^ this.blured], 0);
            GLES20.glClear(0);
            bitmapArr[0] = getRenderBufferBitmap();
            countDownLatch.countDown();
            GLES20.glBindFramebuffer(36160, 0);
            GLES20.glClear(0);
        }

        private Bitmap createBitmap(Bitmap bitmap, int i, int i2, float f) {
            Matrix matrix = new Matrix();
            matrix.setScale(f, f);
            matrix.postRotate((float) PhotoFilterView.this.orientation);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        private void loadTexture(Bitmap bitmap) {
            this.renderBufferWidth = bitmap.getWidth();
            this.renderBufferHeight = bitmap.getHeight();
            float photoSize = (float) AndroidUtilities.getPhotoSize();
            if (((float) this.renderBufferWidth) > photoSize || ((float) this.renderBufferHeight) > photoSize || PhotoFilterView.this.orientation % 360 != 0) {
                float f = 1.0f;
                if (((float) this.renderBufferWidth) > photoSize || ((float) this.renderBufferHeight) > photoSize) {
                    f = photoSize / ((float) bitmap.getWidth());
                    float height = photoSize / ((float) bitmap.getHeight());
                    if (f < height) {
                        this.renderBufferWidth = (int) photoSize;
                        this.renderBufferHeight = (int) (((float) bitmap.getHeight()) * f);
                    } else {
                        this.renderBufferHeight = (int) photoSize;
                        this.renderBufferWidth = (int) (((float) bitmap.getWidth()) * height);
                        f = height;
                    }
                }
                if (PhotoFilterView.this.orientation % 360 == 90 || PhotoFilterView.this.orientation % 360 == 270) {
                    int i = this.renderBufferWidth;
                    this.renderBufferWidth = this.renderBufferHeight;
                    this.renderBufferHeight = i;
                }
                this.currentBitmap = createBitmap(bitmap, this.renderBufferWidth, this.renderBufferHeight, f);
            }
            GLES20.glGenFramebuffers(3, this.renderFrameBuffer, 0);
            GLES20.glGenTextures(3, this.renderTexture, 0);
            GLES20.glBindTexture(3553, this.renderTexture[0]);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLES20.glTexImage2D(3553, 0, 6408, this.renderBufferWidth, this.renderBufferHeight, 0, 6408, 5121, null);
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
            GLES20.glTexImage2D(3553, 0, 6408, this.renderBufferWidth, this.renderBufferHeight, 0, 6408, 5121, null);
        }

        public void shutdown() {
            postRunnable(new C2619-$$Lambda$PhotoFilterView$EGLThread$UPEak5EYsUntGACDTA79Biksn-k(this));
        }

        public /* synthetic */ void lambda$shutdown$1$PhotoFilterView$EGLThread() {
            finish();
            this.currentBitmap = null;
            Looper myLooper = Looper.myLooper();
            if (myLooper != null) {
                myLooper.quit();
            }
        }

        public void setSurfaceTextureSize(int i, int i2) {
            this.surfaceWidth = i;
            this.surfaceHeight = i2;
        }

        public void run() {
            this.initied = initGL();
            super.run();
        }

        public void requestRender(boolean z) {
            requestRender(z, false);
        }

        public void requestRender(boolean z, boolean z2) {
            postRunnable(new C2618-$$Lambda$PhotoFilterView$EGLThread$MrJUzcEqH3ADgrrNUt03oFMBD-4(this, z, z2));
        }

        public /* synthetic */ void lambda$requestRender$2$PhotoFilterView$EGLThread(boolean z, boolean z2) {
            if (!this.needUpdateBlurTexture) {
                this.needUpdateBlurTexture = z;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (z2 || Math.abs(this.lastRenderCallTime - currentTimeMillis) > 30) {
                this.lastRenderCallTime = currentTimeMillis;
                this.drawRunnable.run();
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.PhotoFilterView$ToolsAdapter */
    public class ToolsAdapter extends SelectionAdapter {
        private Context mContext;

        public int getItemCount() {
            return 13;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return false;
        }

        public ToolsAdapter(Context context) {
            this.mContext = context;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View photoEditToolCell;
            if (i == 0) {
                photoEditToolCell = new PhotoEditToolCell(this.mContext);
                photoEditToolCell.setSeekBarDelegate(new C4050x8fa51d5b(this));
            } else {
                photoEditToolCell = new PhotoEditRadioCell(this.mContext);
                photoEditToolCell.setOnClickListener(new C2626x8fdee53b(this));
            }
            return new Holder(photoEditToolCell);
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0$PhotoFilterView$ToolsAdapter(int i, int i2) {
            if (i == PhotoFilterView.this.enhanceTool) {
                PhotoFilterView.this.enhanceValue = (float) i2;
            } else if (i == PhotoFilterView.this.highlightsTool) {
                PhotoFilterView.this.highlightsValue = (float) i2;
            } else if (i == PhotoFilterView.this.contrastTool) {
                PhotoFilterView.this.contrastValue = (float) i2;
            } else if (i == PhotoFilterView.this.exposureTool) {
                PhotoFilterView.this.exposureValue = (float) i2;
            } else if (i == PhotoFilterView.this.warmthTool) {
                PhotoFilterView.this.warmthValue = (float) i2;
            } else if (i == PhotoFilterView.this.saturationTool) {
                PhotoFilterView.this.saturationValue = (float) i2;
            } else if (i == PhotoFilterView.this.vignetteTool) {
                PhotoFilterView.this.vignetteValue = (float) i2;
            } else if (i == PhotoFilterView.this.shadowsTool) {
                PhotoFilterView.this.shadowsValue = (float) i2;
            } else if (i == PhotoFilterView.this.grainTool) {
                PhotoFilterView.this.grainValue = (float) i2;
            } else if (i == PhotoFilterView.this.sharpenTool) {
                PhotoFilterView.this.sharpenValue = (float) i2;
            } else if (i == PhotoFilterView.this.fadeTool) {
                PhotoFilterView.this.fadeValue = (float) i2;
            }
            if (PhotoFilterView.this.eglThread != null) {
                PhotoFilterView.this.eglThread.requestRender(true);
            }
        }

        public /* synthetic */ void lambda$onCreateViewHolder$1$PhotoFilterView$ToolsAdapter(View view) {
            PhotoEditRadioCell photoEditRadioCell = (PhotoEditRadioCell) view;
            if (((Integer) photoEditRadioCell.getTag()).intValue() == PhotoFilterView.this.tintShadowsTool) {
                PhotoFilterView.this.tintShadowsColor = photoEditRadioCell.getCurrentColor();
            } else {
                PhotoFilterView.this.tintHighlightsColor = photoEditRadioCell.getCurrentColor();
            }
            if (PhotoFilterView.this.eglThread != null) {
                PhotoFilterView.this.eglThread.requestRender(false);
            }
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                PhotoEditToolCell photoEditToolCell = (PhotoEditToolCell) viewHolder.itemView;
                photoEditToolCell.setTag(Integer.valueOf(i));
                if (i == PhotoFilterView.this.enhanceTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Enhance", C1067R.string.Enhance), PhotoFilterView.this.enhanceValue, 0, 100);
                } else if (i == PhotoFilterView.this.highlightsTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Highlights", C1067R.string.Highlights), PhotoFilterView.this.highlightsValue, -100, 100);
                } else if (i == PhotoFilterView.this.contrastTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Contrast", C1067R.string.Contrast), PhotoFilterView.this.contrastValue, -100, 100);
                } else if (i == PhotoFilterView.this.exposureTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Exposure", C1067R.string.Exposure), PhotoFilterView.this.exposureValue, -100, 100);
                } else if (i == PhotoFilterView.this.warmthTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Warmth", C1067R.string.Warmth), PhotoFilterView.this.warmthValue, -100, 100);
                } else if (i == PhotoFilterView.this.saturationTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Saturation", C1067R.string.Saturation), PhotoFilterView.this.saturationValue, -100, 100);
                } else if (i == PhotoFilterView.this.vignetteTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Vignette", C1067R.string.Vignette), PhotoFilterView.this.vignetteValue, 0, 100);
                } else if (i == PhotoFilterView.this.shadowsTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Shadows", C1067R.string.Shadows), PhotoFilterView.this.shadowsValue, -100, 100);
                } else if (i == PhotoFilterView.this.grainTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Grain", C1067R.string.Grain), PhotoFilterView.this.grainValue, 0, 100);
                } else if (i == PhotoFilterView.this.sharpenTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Sharpen", C1067R.string.Sharpen), PhotoFilterView.this.sharpenValue, 0, 100);
                } else if (i == PhotoFilterView.this.fadeTool) {
                    photoEditToolCell.setIconAndTextAndValue(LocaleController.getString("Fade", C1067R.string.Fade), PhotoFilterView.this.fadeValue, 0, 100);
                }
            } else if (itemViewType == 1) {
                PhotoEditRadioCell photoEditRadioCell = (PhotoEditRadioCell) viewHolder.itemView;
                photoEditRadioCell.setTag(Integer.valueOf(i));
                if (i == PhotoFilterView.this.tintShadowsTool) {
                    photoEditRadioCell.setIconAndTextAndValue(LocaleController.getString("TintShadows", C1067R.string.TintShadows), 0, PhotoFilterView.this.tintShadowsColor);
                } else if (i == PhotoFilterView.this.tintHighlightsTool) {
                    photoEditRadioCell.setIconAndTextAndValue(LocaleController.getString("TintHighlights", C1067R.string.TintHighlights), 0, PhotoFilterView.this.tintHighlightsColor);
                }
            }
        }

        public int getItemViewType(int i) {
            return (i == PhotoFilterView.this.tintShadowsTool || i == PhotoFilterView.this.tintHighlightsTool) ? 1 : 0;
        }
    }

    public PhotoFilterView(Context context, Bitmap bitmap, int i, SavedFilterState savedFilterState) {
        Context context2 = context;
        SavedFilterState savedFilterState2 = savedFilterState;
        super(context);
        if (savedFilterState2 != null) {
            this.enhanceValue = savedFilterState2.enhanceValue;
            this.exposureValue = savedFilterState2.exposureValue;
            this.contrastValue = savedFilterState2.contrastValue;
            this.warmthValue = savedFilterState2.warmthValue;
            this.saturationValue = savedFilterState2.saturationValue;
            this.fadeValue = savedFilterState2.fadeValue;
            this.tintShadowsColor = savedFilterState2.tintShadowsColor;
            this.tintHighlightsColor = savedFilterState2.tintHighlightsColor;
            this.highlightsValue = savedFilterState2.highlightsValue;
            this.shadowsValue = savedFilterState2.shadowsValue;
            this.vignetteValue = savedFilterState2.vignetteValue;
            this.grainValue = savedFilterState2.grainValue;
            this.blurType = savedFilterState2.blurType;
            this.sharpenValue = savedFilterState2.sharpenValue;
            this.curvesToolValue = savedFilterState2.curvesToolValue;
            this.blurExcludeSize = savedFilterState2.blurExcludeSize;
            this.blurExcludePoint = savedFilterState2.blurExcludePoint;
            this.blurExcludeBlurSize = savedFilterState2.blurExcludeBlurSize;
            this.blurAngle = savedFilterState2.blurAngle;
            this.lastState = savedFilterState2;
        } else {
            this.curvesToolValue = new CurvesToolValue();
            this.blurExcludeSize = 0.35f;
            this.blurExcludePoint = new Point(0.5f, 0.5f);
            this.blurExcludeBlurSize = 0.15f;
            this.blurAngle = 1.5707964f;
        }
        this.bitmapToEdit = bitmap;
        this.orientation = i;
        this.textureView = new TextureView(context2);
        addView(this.textureView, LayoutHelper.createFrame(-1, -1, 51));
        this.textureView.setVisibility(4);
        this.textureView.setSurfaceTextureListener(new C29121());
        this.blurControl = new PhotoFilterBlurControl(context2);
        this.blurControl.setVisibility(4);
        addView(this.blurControl, LayoutHelper.createFrame(-1, -1, 51));
        this.blurControl.setDelegate(new C4051-$$Lambda$PhotoFilterView$wsvuAfZFAJpyt9V1ZYZMPETINLA(this));
        this.curvesControl = new PhotoFilterCurvesControl(context2, this.curvesToolValue);
        this.curvesControl.setDelegate(new C4049-$$Lambda$PhotoFilterView$Q8Q0QxhBgkn0x_QyOvZIPgaDQJM(this));
        this.curvesControl.setVisibility(4);
        addView(this.curvesControl, LayoutHelper.createFrame(-1, -1, 51));
        this.toolsView = new FrameLayout(context2);
        addView(this.toolsView, LayoutHelper.createFrame(-1, 186, 83));
        FrameLayout frameLayout = new FrameLayout(context2);
        frameLayout.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        this.toolsView.addView(frameLayout, LayoutHelper.createFrame(-1, 48, 83));
        this.cancelTextView = new TextView(context2);
        this.cancelTextView.setTextSize(1, 14.0f);
        this.cancelTextView.setTextColor(-1);
        this.cancelTextView.setGravity(17);
        this.cancelTextView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR, 0));
        this.cancelTextView.setPadding(AndroidUtilities.m26dp(20.0f), 0, AndroidUtilities.m26dp(20.0f), 0);
        this.cancelTextView.setText(LocaleController.getString("Cancel", C1067R.string.Cancel).toUpperCase());
        this.cancelTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView(this.cancelTextView, LayoutHelper.createFrame(-2, -1, 51));
        this.doneTextView = new TextView(context2);
        this.doneTextView.setTextSize(1, 14.0f);
        this.doneTextView.setTextColor(-11420173);
        this.doneTextView.setGravity(17);
        this.doneTextView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR, 0));
        this.doneTextView.setPadding(AndroidUtilities.m26dp(20.0f), 0, AndroidUtilities.m26dp(20.0f), 0);
        this.doneTextView.setText(LocaleController.getString("Done", C1067R.string.Done).toUpperCase());
        this.doneTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView(this.doneTextView, LayoutHelper.createFrame(-2, -1, 53));
        LinearLayout linearLayout = new LinearLayout(context2);
        frameLayout.addView(linearLayout, LayoutHelper.createFrame(-2, -1, 1));
        this.tuneItem = new ImageView(context2);
        this.tuneItem.setScaleType(ScaleType.CENTER);
        this.tuneItem.setImageResource(C1067R.C1065drawable.photo_tools);
        this.tuneItem.setColorFilter(new PorterDuffColorFilter(-9649153, Mode.MULTIPLY));
        this.tuneItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
        linearLayout.addView(this.tuneItem, LayoutHelper.createLinear(56, 48));
        this.tuneItem.setOnClickListener(new C2616-$$Lambda$PhotoFilterView$4hF97H88dpzJN2DxB_cny05r_QI(this));
        this.blurItem = new ImageView(context2);
        this.blurItem.setScaleType(ScaleType.CENTER);
        this.blurItem.setImageResource(C1067R.C1065drawable.tool_blur);
        this.blurItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
        linearLayout.addView(this.blurItem, LayoutHelper.createLinear(56, 48));
        this.blurItem.setOnClickListener(new C2617-$$Lambda$PhotoFilterView$DKhAjcvz5psQCw7kBhsHKlWIYQM(this));
        this.curveItem = new ImageView(context2);
        this.curveItem.setScaleType(ScaleType.CENTER);
        this.curveItem.setImageResource(C1067R.C1065drawable.tool_curve);
        this.curveItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
        linearLayout.addView(this.curveItem, LayoutHelper.createLinear(56, 48));
        this.curveItem.setOnClickListener(new C2622-$$Lambda$PhotoFilterView$F_KOwGrCZ7lau2u49edpZx3V5ug(this));
        this.recyclerListView = new RecyclerListView(context2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context2);
        linearLayoutManager.setOrientation(1);
        this.recyclerListView.setLayoutManager(linearLayoutManager);
        this.recyclerListView.setClipToPadding(false);
        this.recyclerListView.setOverScrollMode(2);
        this.recyclerListView.setAdapter(new ToolsAdapter(context2));
        this.toolsView.addView(this.recyclerListView, LayoutHelper.createFrame(-1, 120, 51));
        this.curveLayout = new FrameLayout(context2);
        this.curveLayout.setVisibility(4);
        this.toolsView.addView(this.curveLayout, LayoutHelper.createFrame(-1, 78.0f, 1, 0.0f, 40.0f, 0.0f, 0.0f));
        LinearLayout linearLayout2 = new LinearLayout(context2);
        linearLayout2.setOrientation(0);
        this.curveLayout.addView(linearLayout2, LayoutHelper.createFrame(-2, -2, 1));
        int i2 = 0;
        while (i2 < 4) {
            FrameLayout frameLayout2 = new FrameLayout(context2);
            frameLayout2.setTag(Integer.valueOf(i2));
            this.curveRadioButton[i2] = new RadioButton(context2);
            this.curveRadioButton[i2].setSize(AndroidUtilities.m26dp(20.0f));
            frameLayout2.addView(this.curveRadioButton[i2], LayoutHelper.createFrame(30, 30, 49));
            TextView textView = new TextView(context2);
            textView.setTextSize(1, 12.0f);
            textView.setGravity(16);
            String string;
            StringBuilder stringBuilder;
            if (i2 == 0) {
                string = LocaleController.getString("CurvesAll", C1067R.string.CurvesAll);
                stringBuilder = new StringBuilder();
                stringBuilder.append(string.substring(0, 1).toUpperCase());
                stringBuilder.append(string.substring(1).toLowerCase());
                textView.setText(stringBuilder.toString());
                textView.setTextColor(-1);
                this.curveRadioButton[i2].setColor(-1, -1);
            } else if (i2 == 1) {
                string = LocaleController.getString("CurvesRed", C1067R.string.CurvesRed);
                stringBuilder = new StringBuilder();
                stringBuilder.append(string.substring(0, 1).toUpperCase());
                stringBuilder.append(string.substring(1).toLowerCase());
                textView.setText(stringBuilder.toString());
                textView.setTextColor(-1684147);
                this.curveRadioButton[i2].setColor(-1684147, -1684147);
            } else if (i2 == 2) {
                string = LocaleController.getString("CurvesGreen", C1067R.string.CurvesGreen);
                stringBuilder = new StringBuilder();
                stringBuilder.append(string.substring(0, 1).toUpperCase());
                stringBuilder.append(string.substring(1).toLowerCase());
                textView.setText(stringBuilder.toString());
                textView.setTextColor(-10831009);
                this.curveRadioButton[i2].setColor(-10831009, -10831009);
            } else if (i2 == 3) {
                string = LocaleController.getString("CurvesBlue", C1067R.string.CurvesBlue);
                stringBuilder = new StringBuilder();
                stringBuilder.append(string.substring(0, 1).toUpperCase());
                stringBuilder.append(string.substring(1).toLowerCase());
                textView.setText(stringBuilder.toString());
                textView.setTextColor(-12734994);
                this.curveRadioButton[i2].setColor(-12734994, -12734994);
            }
            frameLayout2.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 38.0f, 0.0f, 0.0f));
            linearLayout2.addView(frameLayout2, LayoutHelper.createLinear(-2, -2, i2 == 0 ? 0.0f : 30.0f, 0.0f, 0.0f, 0.0f));
            frameLayout2.setOnClickListener(new C2623-$$Lambda$PhotoFilterView$L20A01o2AsZwohbyfeeuMPbGEQI(this));
            i2++;
        }
        this.blurLayout = new FrameLayout(context2);
        this.blurLayout.setVisibility(4);
        this.toolsView.addView(this.blurLayout, LayoutHelper.createFrame(280, 60.0f, 1, 0.0f, 40.0f, 0.0f, 0.0f));
        this.blurOffButton = new TextView(context2);
        this.blurOffButton.setCompoundDrawablePadding(AndroidUtilities.m26dp(2.0f));
        this.blurOffButton.setTextSize(1, 13.0f);
        this.blurOffButton.setGravity(1);
        this.blurOffButton.setText(LocaleController.getString("BlurOff", C1067R.string.BlurOff));
        this.blurLayout.addView(this.blurOffButton, LayoutHelper.createFrame(80, 60.0f));
        this.blurOffButton.setOnClickListener(new C2621-$$Lambda$PhotoFilterView$EHBGA4T-Wcgx03trAGzstG0gXqg(this));
        this.blurRadialButton = new TextView(context2);
        this.blurRadialButton.setCompoundDrawablePadding(AndroidUtilities.m26dp(2.0f));
        this.blurRadialButton.setTextSize(1, 13.0f);
        this.blurRadialButton.setGravity(1);
        this.blurRadialButton.setText(LocaleController.getString("BlurRadial", C1067R.string.BlurRadial));
        this.blurLayout.addView(this.blurRadialButton, LayoutHelper.createFrame(80, 80.0f, 51, 100.0f, 0.0f, 0.0f, 0.0f));
        this.blurRadialButton.setOnClickListener(new C2624-$$Lambda$PhotoFilterView$SHP9jNXWESNEp5KZC_qRcZYRL1Y(this));
        this.blurLinearButton = new TextView(context2);
        this.blurLinearButton.setCompoundDrawablePadding(AndroidUtilities.m26dp(2.0f));
        this.blurLinearButton.setTextSize(1, 13.0f);
        this.blurLinearButton.setGravity(1);
        this.blurLinearButton.setText(LocaleController.getString("BlurLinear", C1067R.string.BlurLinear));
        this.blurLayout.addView(this.blurLinearButton, LayoutHelper.createFrame(80, 80.0f, 51, 200.0f, 0.0f, 0.0f, 0.0f));
        this.blurLinearButton.setOnClickListener(new C2625-$$Lambda$PhotoFilterView$TdTpIDy0jwl-mIhu0bpchkPiKg8(this));
        updateSelectedBlurType();
        if (VERSION.SDK_INT >= 21) {
            ((LayoutParams) this.textureView.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
            ((LayoutParams) this.curvesControl.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
        }
    }

    public /* synthetic */ void lambda$new$0$PhotoFilterView(Point point, float f, float f2, float f3) {
        this.blurExcludeSize = f2;
        this.blurExcludePoint = point;
        this.blurExcludeBlurSize = f;
        this.blurAngle = f3;
        EGLThread eGLThread = this.eglThread;
        if (eGLThread != null) {
            eGLThread.requestRender(false);
        }
    }

    public /* synthetic */ void lambda$new$1$PhotoFilterView() {
        EGLThread eGLThread = this.eglThread;
        if (eGLThread != null) {
            eGLThread.requestRender(false);
        }
    }

    public /* synthetic */ void lambda$new$2$PhotoFilterView(View view) {
        this.selectedTool = 0;
        this.tuneItem.setColorFilter(new PorterDuffColorFilter(-9649153, Mode.MULTIPLY));
        this.blurItem.setColorFilter(null);
        this.curveItem.setColorFilter(null);
        switchMode();
    }

    public /* synthetic */ void lambda$new$3$PhotoFilterView(View view) {
        this.selectedTool = 1;
        this.tuneItem.setColorFilter(null);
        this.blurItem.setColorFilter(new PorterDuffColorFilter(-9649153, Mode.MULTIPLY));
        this.curveItem.setColorFilter(null);
        switchMode();
    }

    public /* synthetic */ void lambda$new$4$PhotoFilterView(View view) {
        this.selectedTool = 2;
        this.tuneItem.setColorFilter(null);
        this.blurItem.setColorFilter(null);
        this.curveItem.setColorFilter(new PorterDuffColorFilter(-9649153, Mode.MULTIPLY));
        switchMode();
    }

    public /* synthetic */ void lambda$new$5$PhotoFilterView(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        this.curvesToolValue.activeType = intValue;
        int i = 0;
        while (i < 4) {
            this.curveRadioButton[i].setChecked(i == intValue, true);
            i++;
        }
        this.curvesControl.invalidate();
    }

    public /* synthetic */ void lambda$new$6$PhotoFilterView(View view) {
        this.blurType = 0;
        updateSelectedBlurType();
        this.blurControl.setVisibility(4);
        EGLThread eGLThread = this.eglThread;
        if (eGLThread != null) {
            eGLThread.requestRender(false);
        }
    }

    public /* synthetic */ void lambda$new$7$PhotoFilterView(View view) {
        this.blurType = 1;
        updateSelectedBlurType();
        this.blurControl.setVisibility(0);
        this.blurControl.setType(1);
        EGLThread eGLThread = this.eglThread;
        if (eGLThread != null) {
            eGLThread.requestRender(false);
        }
    }

    public /* synthetic */ void lambda$new$8$PhotoFilterView(View view) {
        this.blurType = 2;
        updateSelectedBlurType();
        this.blurControl.setVisibility(0);
        this.blurControl.setType(0);
        EGLThread eGLThread = this.eglThread;
        if (eGLThread != null) {
            eGLThread.requestRender(false);
        }
    }

    private void updateSelectedBlurType() {
        int i = this.blurType;
        Drawable mutate;
        if (i == 0) {
            mutate = this.blurOffButton.getContext().getResources().getDrawable(C1067R.C1065drawable.blur_off).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-11420173, Mode.MULTIPLY));
            this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds(null, mutate, null, null);
            this.blurOffButton.setTextColor(-11420173);
            this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds(0, C1067R.C1065drawable.blur_radial, 0, 0);
            this.blurRadialButton.setTextColor(-1);
            this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds(0, C1067R.C1065drawable.blur_linear, 0, 0);
            this.blurLinearButton.setTextColor(-1);
        } else if (i == 1) {
            this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds(0, C1067R.C1065drawable.blur_off, 0, 0);
            this.blurOffButton.setTextColor(-1);
            mutate = this.blurOffButton.getContext().getResources().getDrawable(C1067R.C1065drawable.blur_radial).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-11420173, Mode.MULTIPLY));
            this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds(null, mutate, null, null);
            this.blurRadialButton.setTextColor(-11420173);
            this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds(0, C1067R.C1065drawable.blur_linear, 0, 0);
            this.blurLinearButton.setTextColor(-1);
        } else if (i == 2) {
            this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds(0, C1067R.C1065drawable.blur_off, 0, 0);
            this.blurOffButton.setTextColor(-1);
            this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds(0, C1067R.C1065drawable.blur_radial, 0, 0);
            this.blurRadialButton.setTextColor(-1);
            mutate = this.blurOffButton.getContext().getResources().getDrawable(C1067R.C1065drawable.blur_linear).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-11420173, Mode.MULTIPLY));
            this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds(null, mutate, null, null);
            this.blurLinearButton.setTextColor(-11420173);
        }
    }

    public SavedFilterState getSavedFilterState() {
        SavedFilterState savedFilterState = new SavedFilterState();
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

    public boolean hasChanges() {
        SavedFilterState savedFilterState = this.lastState;
        boolean z = false;
        if (savedFilterState != null) {
            if (!(this.enhanceValue == savedFilterState.enhanceValue && this.contrastValue == savedFilterState.contrastValue && this.highlightsValue == savedFilterState.highlightsValue && this.exposureValue == savedFilterState.exposureValue && this.warmthValue == savedFilterState.warmthValue && this.saturationValue == savedFilterState.saturationValue && this.vignetteValue == savedFilterState.vignetteValue && this.shadowsValue == savedFilterState.shadowsValue && this.grainValue == savedFilterState.grainValue && this.sharpenValue == savedFilterState.sharpenValue && this.fadeValue == savedFilterState.fadeValue && this.tintHighlightsColor == savedFilterState.tintHighlightsColor && this.tintShadowsColor == savedFilterState.tintShadowsColor && this.curvesToolValue.shouldBeSkipped())) {
                z = true;
            }
            return z;
        }
        if (!(this.enhanceValue == 0.0f && this.contrastValue == 0.0f && this.highlightsValue == 0.0f && this.exposureValue == 0.0f && this.warmthValue == 0.0f && this.saturationValue == 0.0f && this.vignetteValue == 0.0f && this.shadowsValue == 0.0f && this.grainValue == 0.0f && this.sharpenValue == 0.0f && this.fadeValue == 0.0f && this.tintHighlightsColor == 0 && this.tintShadowsColor == 0 && this.curvesToolValue.shouldBeSkipped())) {
            z = true;
        }
        return z;
    }

    public void onTouch(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
            LayoutParams layoutParams = (LayoutParams) this.textureView.getLayoutParams();
            if (layoutParams != null && motionEvent.getX() >= ((float) layoutParams.leftMargin) && motionEvent.getY() >= ((float) layoutParams.topMargin) && motionEvent.getX() <= ((float) (layoutParams.leftMargin + layoutParams.width)) && motionEvent.getY() <= ((float) (layoutParams.topMargin + layoutParams.height))) {
                setShowOriginal(true);
            }
        } else if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6) {
            setShowOriginal(false);
        }
    }

    private void setShowOriginal(boolean z) {
        if (this.showOriginal != z) {
            this.showOriginal = z;
            EGLThread eGLThread = this.eglThread;
            if (eGLThread != null) {
                eGLThread.requestRender(false);
            }
        }
    }

    public void switchMode() {
        int i = this.selectedTool;
        if (i == 0) {
            this.blurControl.setVisibility(4);
            this.blurLayout.setVisibility(4);
            this.curveLayout.setVisibility(4);
            this.curvesControl.setVisibility(4);
            this.recyclerListView.setVisibility(0);
        } else if (i == 1) {
            this.recyclerListView.setVisibility(4);
            this.curveLayout.setVisibility(4);
            this.curvesControl.setVisibility(4);
            this.blurLayout.setVisibility(0);
            if (this.blurType != 0) {
                this.blurControl.setVisibility(0);
            }
            updateSelectedBlurType();
        } else if (i == 2) {
            this.recyclerListView.setVisibility(4);
            this.blurLayout.setVisibility(4);
            this.blurControl.setVisibility(4);
            this.curveLayout.setVisibility(0);
            this.curvesControl.setVisibility(0);
            this.curvesToolValue.activeType = 0;
            i = 0;
            while (i < 4) {
                this.curveRadioButton[i].setChecked(i == 0, false);
                i++;
            }
        }
    }

    public void shutdown() {
        EGLThread eGLThread = this.eglThread;
        if (eGLThread != null) {
            eGLThread.shutdown();
            this.eglThread = null;
        }
        this.textureView.setVisibility(8);
    }

    public void init() {
        this.textureView.setVisibility(0);
    }

    public Bitmap getBitmap() {
        EGLThread eGLThread = this.eglThread;
        return eGLThread != null ? eGLThread.getTexture() : null;
    }

    private void fixLayout(int i, int i2) {
        if (this.bitmapToEdit != null) {
            float height;
            int width;
            i -= AndroidUtilities.m26dp(28.0f);
            i2 -= AndroidUtilities.m26dp(214.0f) + (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
            int i3 = this.orientation;
            if (i3 % 360 == 90 || i3 % 360 == 270) {
                height = (float) this.bitmapToEdit.getHeight();
                width = this.bitmapToEdit.getWidth();
            } else {
                height = (float) this.bitmapToEdit.getWidth();
                width = this.bitmapToEdit.getHeight();
            }
            float f = (float) width;
            float f2 = (float) i;
            float f3 = f2 / height;
            float f4 = (float) i2;
            float f5 = f4 / f;
            if (f3 > f5) {
                height = (float) ((int) Math.ceil((double) (height * f5)));
                f = f4;
            } else {
                f = (float) ((int) Math.ceil((double) (f * f3)));
                height = f2;
            }
            int ceil = (int) Math.ceil((double) (((f2 - height) / 2.0f) + ((float) AndroidUtilities.m26dp(14.0f))));
            int ceil2 = (int) Math.ceil((double) ((((f4 - f) / 2.0f) + ((float) AndroidUtilities.m26dp(14.0f))) + ((float) (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0))));
            LayoutParams layoutParams = (LayoutParams) this.textureView.getLayoutParams();
            layoutParams.leftMargin = ceil;
            layoutParams.topMargin = ceil2;
            layoutParams.width = (int) height;
            layoutParams.height = (int) f;
            this.curvesControl.setActualArea((float) ceil, (float) (ceil2 - (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)), (float) layoutParams.width, (float) layoutParams.height);
            this.blurControl.setActualAreaSize((float) layoutParams.width, (float) layoutParams.height);
            ((LayoutParams) this.blurControl.getLayoutParams()).height = AndroidUtilities.m26dp(38.0f) + i2;
            ((LayoutParams) this.curvesControl.getLayoutParams()).height = i2 + AndroidUtilities.m26dp(28.0f);
            if (AndroidUtilities.isTablet()) {
                i2 = AndroidUtilities.m26dp(86.0f) * 10;
                LayoutParams layoutParams2 = (LayoutParams) this.recyclerListView.getLayoutParams();
                if (i2 < i) {
                    layoutParams2.width = i2;
                    layoutParams2.leftMargin = (i - i2) / 2;
                } else {
                    layoutParams2.width = -1;
                    layoutParams2.leftMargin = 0;
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        fixLayout(MeasureSpec.getSize(i), MeasureSpec.getSize(i2));
        super.onMeasure(i, i2);
    }

    private float getShadowsValue() {
        return ((this.shadowsValue * 0.55f) + 100.0f) / 100.0f;
    }

    private float getHighlightsValue() {
        return ((this.highlightsValue * 0.75f) + 100.0f) / 100.0f;
    }

    private float getEnhanceValue() {
        return this.enhanceValue / 100.0f;
    }

    private float getExposureValue() {
        return this.exposureValue / 100.0f;
    }

    private float getContrastValue() {
        return ((this.contrastValue / 100.0f) * 0.3f) + 1.0f;
    }

    private float getWarmthValue() {
        return this.warmthValue / 100.0f;
    }

    private float getVignetteValue() {
        return this.vignetteValue / 100.0f;
    }

    private float getSharpenValue() {
        return ((this.sharpenValue / 100.0f) * 0.6f) + 0.11f;
    }

    private float getGrainValue() {
        return (this.grainValue / 100.0f) * 0.04f;
    }

    private float getFadeValue() {
        return this.fadeValue / 100.0f;
    }

    private float getTintHighlightsIntensityValue() {
        return this.tintHighlightsColor == 0 ? 0.0f : 0.5f;
    }

    private float getTintShadowsIntensityValue() {
        return this.tintShadowsColor == 0 ? 0.0f : 0.5f;
    }

    private float getSaturationValue() {
        float f = this.saturationValue / 100.0f;
        if (f > 0.0f) {
            f *= 1.05f;
        }
        return f + 1.0f;
    }

    public FrameLayout getToolsView() {
        return this.toolsView;
    }

    public TextView getDoneTextView() {
        return this.doneTextView;
    }

    public TextView getCancelTextView() {
        return this.cancelTextView;
    }
}