package androidx.palette.graphics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.SparseBooleanArray;
import androidx.collection.ArrayMap;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.telegram.p004ui.ActionBar.Theme;

public final class Palette {
    static final Filter DEFAULT_FILTER = new C00601();
    private final Swatch mDominantSwatch = findDominantSwatch();
    private final Map<Target, Swatch> mSelectedSwatches = new ArrayMap();
    private final List<Swatch> mSwatches;
    private final List<Target> mTargets;
    private final SparseBooleanArray mUsedColors = new SparseBooleanArray();

    public static final class Builder {
        private final Bitmap mBitmap;
        private final List<Filter> mFilters = new ArrayList();
        private int mMaxColors = 16;
        private Rect mRegion;
        private int mResizeArea = 12544;
        private int mResizeMaxDimension = -1;
        private final List<Swatch> mSwatches;
        private final List<Target> mTargets = new ArrayList();

        public Builder(Bitmap bitmap) {
            if (bitmap == null || bitmap.isRecycled()) {
                throw new IllegalArgumentException("Bitmap is not valid");
            }
            this.mFilters.add(Palette.DEFAULT_FILTER);
            this.mBitmap = bitmap;
            this.mSwatches = null;
            this.mTargets.add(Target.LIGHT_VIBRANT);
            this.mTargets.add(Target.VIBRANT);
            this.mTargets.add(Target.DARK_VIBRANT);
            this.mTargets.add(Target.LIGHT_MUTED);
            this.mTargets.add(Target.MUTED);
            this.mTargets.add(Target.DARK_MUTED);
        }

        public Palette generate() {
            List quantizedColors;
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null) {
                Filter[] filterArr;
                bitmap = scaleBitmapDown(bitmap);
                Rect rect = this.mRegion;
                if (!(bitmap == this.mBitmap || rect == null)) {
                    double width = (double) bitmap.getWidth();
                    double width2 = (double) this.mBitmap.getWidth();
                    Double.isNaN(width);
                    Double.isNaN(width2);
                    width /= width2;
                    width2 = (double) rect.left;
                    Double.isNaN(width2);
                    rect.left = (int) Math.floor(width2 * width);
                    width2 = (double) rect.top;
                    Double.isNaN(width2);
                    rect.top = (int) Math.floor(width2 * width);
                    width2 = (double) rect.right;
                    Double.isNaN(width2);
                    rect.right = Math.min((int) Math.ceil(width2 * width), bitmap.getWidth());
                    width2 = (double) rect.bottom;
                    Double.isNaN(width2);
                    rect.bottom = Math.min((int) Math.ceil(width2 * width), bitmap.getHeight());
                }
                int[] pixelsFromBitmap = getPixelsFromBitmap(bitmap);
                int i = this.mMaxColors;
                if (this.mFilters.isEmpty()) {
                    filterArr = null;
                } else {
                    List list = this.mFilters;
                    filterArr = (Filter[]) list.toArray(new Filter[list.size()]);
                }
                ColorCutQuantizer colorCutQuantizer = new ColorCutQuantizer(pixelsFromBitmap, i, filterArr);
                if (bitmap != this.mBitmap) {
                    bitmap.recycle();
                }
                quantizedColors = colorCutQuantizer.getQuantizedColors();
            } else {
                quantizedColors = this.mSwatches;
                if (quantizedColors == null) {
                    throw new AssertionError();
                }
            }
            Palette palette = new Palette(quantizedColors, this.mTargets);
            palette.generate();
            return palette;
        }

        private int[] getPixelsFromBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] iArr = new int[(width * height)];
            bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
            Rect rect = this.mRegion;
            if (rect == null) {
                return iArr;
            }
            int width2 = rect.width();
            int height2 = this.mRegion.height();
            int[] iArr2 = new int[(width2 * height2)];
            for (int i = 0; i < height2; i++) {
                Rect rect2 = this.mRegion;
                System.arraycopy(iArr, ((rect2.top + i) * width) + rect2.left, iArr2, i * width2, width2);
            }
            return iArr2;
        }

        private Bitmap scaleBitmapDown(Bitmap bitmap) {
            int width;
            double d;
            double d2 = -1.0d;
            int i;
            if (this.mResizeArea > 0) {
                width = bitmap.getWidth() * bitmap.getHeight();
                i = this.mResizeArea;
                if (width > i) {
                    d2 = (double) i;
                    d = (double) width;
                    Double.isNaN(d2);
                    Double.isNaN(d);
                    d2 = Math.sqrt(d2 / d);
                }
            } else if (this.mResizeMaxDimension > 0) {
                width = Math.max(bitmap.getWidth(), bitmap.getHeight());
                i = this.mResizeMaxDimension;
                if (width > i) {
                    d2 = (double) i;
                    d = (double) width;
                    Double.isNaN(d2);
                    Double.isNaN(d);
                    d2 /= d;
                }
            }
            if (d2 <= 0.0d) {
                return bitmap;
            }
            d = (double) bitmap.getWidth();
            Double.isNaN(d);
            width = (int) Math.ceil(d * d2);
            d = (double) bitmap.getHeight();
            Double.isNaN(d);
            return Bitmap.createScaledBitmap(bitmap, width, (int) Math.ceil(d * d2), false);
        }
    }

    public interface Filter {
        boolean isAllowed(int i, float[] fArr);
    }

    public static final class Swatch {
        private final int mBlue;
        private int mBodyTextColor;
        private boolean mGeneratedTextColors;
        private final int mGreen;
        private float[] mHsl;
        private final int mPopulation;
        private final int mRed;
        private final int mRgb;
        private int mTitleTextColor;

        public Swatch(int i, int i2) {
            this.mRed = Color.red(i);
            this.mGreen = Color.green(i);
            this.mBlue = Color.blue(i);
            this.mRgb = i;
            this.mPopulation = i2;
        }

        public int getRgb() {
            return this.mRgb;
        }

        public float[] getHsl() {
            if (this.mHsl == null) {
                this.mHsl = new float[3];
            }
            ColorUtils.RGBToHSL(this.mRed, this.mGreen, this.mBlue, this.mHsl);
            return this.mHsl;
        }

        public int getPopulation() {
            return this.mPopulation;
        }

        public int getTitleTextColor() {
            ensureTextColorsGenerated();
            return this.mTitleTextColor;
        }

        public int getBodyTextColor() {
            ensureTextColorsGenerated();
            return this.mBodyTextColor;
        }

        private void ensureTextColorsGenerated() {
            if (!this.mGeneratedTextColors) {
                int calculateMinimumAlpha = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 4.5f);
                int calculateMinimumAlpha2 = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 3.0f);
                if (calculateMinimumAlpha == -1 || calculateMinimumAlpha2 == -1) {
                    int calculateMinimumAlpha3 = ColorUtils.calculateMinimumAlpha(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, this.mRgb, 4.5f);
                    int calculateMinimumAlpha4 = ColorUtils.calculateMinimumAlpha(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, this.mRgb, 3.0f);
                    if (calculateMinimumAlpha3 == -1 || calculateMinimumAlpha4 == -1) {
                        if (calculateMinimumAlpha != -1) {
                            calculateMinimumAlpha = ColorUtils.setAlphaComponent(-1, calculateMinimumAlpha);
                        } else {
                            calculateMinimumAlpha = ColorUtils.setAlphaComponent(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, calculateMinimumAlpha3);
                        }
                        this.mBodyTextColor = calculateMinimumAlpha;
                        if (calculateMinimumAlpha2 != -1) {
                            calculateMinimumAlpha = ColorUtils.setAlphaComponent(-1, calculateMinimumAlpha2);
                        } else {
                            calculateMinimumAlpha = ColorUtils.setAlphaComponent(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, calculateMinimumAlpha4);
                        }
                        this.mTitleTextColor = calculateMinimumAlpha;
                        this.mGeneratedTextColors = true;
                    } else {
                        this.mBodyTextColor = ColorUtils.setAlphaComponent(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, calculateMinimumAlpha3);
                        this.mTitleTextColor = ColorUtils.setAlphaComponent(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, calculateMinimumAlpha4);
                        this.mGeneratedTextColors = true;
                        return;
                    }
                }
                this.mBodyTextColor = ColorUtils.setAlphaComponent(-1, calculateMinimumAlpha);
                this.mTitleTextColor = ColorUtils.setAlphaComponent(-1, calculateMinimumAlpha2);
                this.mGeneratedTextColors = true;
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(Swatch.class.getSimpleName());
            stringBuilder.append(" [RGB: #");
            stringBuilder.append(Integer.toHexString(getRgb()));
            stringBuilder.append(']');
            stringBuilder.append(" [HSL: ");
            stringBuilder.append(Arrays.toString(getHsl()));
            stringBuilder.append(']');
            stringBuilder.append(" [Population: ");
            stringBuilder.append(this.mPopulation);
            stringBuilder.append(']');
            stringBuilder.append(" [Title Text: #");
            stringBuilder.append(Integer.toHexString(getTitleTextColor()));
            stringBuilder.append(']');
            stringBuilder.append(" [Body Text: #");
            stringBuilder.append(Integer.toHexString(getBodyTextColor()));
            stringBuilder.append(']');
            return stringBuilder.toString();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || Swatch.class != obj.getClass()) {
                return false;
            }
            Swatch swatch = (Swatch) obj;
            if (!(this.mPopulation == swatch.mPopulation && this.mRgb == swatch.mRgb)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (this.mRgb * 31) + this.mPopulation;
        }
    }

    /* renamed from: androidx.palette.graphics.Palette$1 */
    static class C00601 implements Filter {
        C00601() {
        }

        public boolean isAllowed(int i, float[] fArr) {
            return (isWhite(fArr) || isBlack(fArr) || isNearRedILine(fArr)) ? false : true;
        }

        private boolean isBlack(float[] fArr) {
            return fArr[2] <= 0.05f;
        }

        private boolean isWhite(float[] fArr) {
            return fArr[2] >= 0.95f;
        }

        private boolean isNearRedILine(float[] fArr) {
            return fArr[0] >= 10.0f && fArr[0] <= 37.0f && fArr[1] <= 0.82f;
        }
    }

    public static Builder from(Bitmap bitmap) {
        return new Builder(bitmap);
    }

    Palette(List<Swatch> list, List<Target> list2) {
        this.mSwatches = list;
        this.mTargets = list2;
    }

    public int getDarkMutedColor(int i) {
        return getColorForTarget(Target.DARK_MUTED, i);
    }

    public Swatch getSwatchForTarget(Target target) {
        return (Swatch) this.mSelectedSwatches.get(target);
    }

    public int getColorForTarget(Target target, int i) {
        Swatch swatchForTarget = getSwatchForTarget(target);
        return swatchForTarget != null ? swatchForTarget.getRgb() : i;
    }

    /* Access modifiers changed, original: 0000 */
    public void generate() {
        int size = this.mTargets.size();
        for (int i = 0; i < size; i++) {
            Target target = (Target) this.mTargets.get(i);
            target.normalizeWeights();
            this.mSelectedSwatches.put(target, generateScoredTarget(target));
        }
        this.mUsedColors.clear();
    }

    private Swatch generateScoredTarget(Target target) {
        Swatch maxScoredSwatchForTarget = getMaxScoredSwatchForTarget(target);
        if (maxScoredSwatchForTarget != null && target.isExclusive()) {
            this.mUsedColors.append(maxScoredSwatchForTarget.getRgb(), true);
        }
        return maxScoredSwatchForTarget;
    }

    private Swatch getMaxScoredSwatchForTarget(Target target) {
        int size = this.mSwatches.size();
        float f = 0.0f;
        Swatch swatch = null;
        for (int i = 0; i < size; i++) {
            Swatch swatch2 = (Swatch) this.mSwatches.get(i);
            if (shouldBeScoredForTarget(swatch2, target)) {
                float generateScore = generateScore(swatch2, target);
                if (swatch == null || generateScore > f) {
                    swatch = swatch2;
                    f = generateScore;
                }
            }
        }
        return swatch;
    }

    private boolean shouldBeScoredForTarget(Swatch swatch, Target target) {
        float[] hsl = swatch.getHsl();
        if (hsl[1] < target.getMinimumSaturation() || hsl[1] > target.getMaximumSaturation() || hsl[2] < target.getMinimumLightness() || hsl[2] > target.getMaximumLightness() || this.mUsedColors.get(swatch.getRgb())) {
            return false;
        }
        return true;
    }

    private float generateScore(Swatch swatch, Target target) {
        float[] hsl = swatch.getHsl();
        Swatch swatch2 = this.mDominantSwatch;
        int population = swatch2 != null ? swatch2.getPopulation() : 1;
        float f = 0.0f;
        float abs = target.getSaturationWeight() > 0.0f ? (1.0f - Math.abs(hsl[1] - target.getTargetSaturation())) * target.getSaturationWeight() : 0.0f;
        float lightnessWeight = target.getLightnessWeight() > 0.0f ? target.getLightnessWeight() * (1.0f - Math.abs(hsl[2] - target.getTargetLightness())) : 0.0f;
        if (target.getPopulationWeight() > 0.0f) {
            f = target.getPopulationWeight() * (((float) swatch.getPopulation()) / ((float) population));
        }
        return (abs + lightnessWeight) + f;
    }

    private Swatch findDominantSwatch() {
        int size = this.mSwatches.size();
        int i = Integer.MIN_VALUE;
        Swatch swatch = null;
        for (int i2 = 0; i2 < size; i2++) {
            Swatch swatch2 = (Swatch) this.mSwatches.get(i2);
            if (swatch2.getPopulation() > i) {
                i = swatch2.getPopulation();
                swatch = swatch2;
            }
        }
        return swatch;
    }
}
