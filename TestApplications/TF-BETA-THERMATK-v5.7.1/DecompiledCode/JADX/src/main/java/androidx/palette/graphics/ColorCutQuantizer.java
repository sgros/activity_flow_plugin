package androidx.palette.graphics;

import android.graphics.Color;
import android.util.TimingLogger;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette.Filter;
import androidx.palette.graphics.Palette.Swatch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

final class ColorCutQuantizer {
    private static final Comparator<Vbox> VBOX_COMPARATOR_VOLUME = new C00591();
    final int[] mColors;
    final Filter[] mFilters;
    final int[] mHistogram;
    final List<Swatch> mQuantizedColors;
    private final float[] mTempHsl = new float[3];
    final TimingLogger mTimingLogger = null;

    /* renamed from: androidx.palette.graphics.ColorCutQuantizer$1 */
    static class C00591 implements Comparator<Vbox> {
        C00591() {
        }

        public int compare(Vbox vbox, Vbox vbox2) {
            return vbox2.getVolume() - vbox.getVolume();
        }
    }

    private class Vbox {
        private int mLowerIndex;
        private int mMaxBlue;
        private int mMaxGreen;
        private int mMaxRed;
        private int mMinBlue;
        private int mMinGreen;
        private int mMinRed;
        private int mPopulation;
        private int mUpperIndex;

        Vbox(int i, int i2) {
            this.mLowerIndex = i;
            this.mUpperIndex = i2;
            fitBox();
        }

        /* Access modifiers changed, original: final */
        public final int getVolume() {
            return (((this.mMaxRed - this.mMinRed) + 1) * ((this.mMaxGreen - this.mMinGreen) + 1)) * ((this.mMaxBlue - this.mMinBlue) + 1);
        }

        /* Access modifiers changed, original: final */
        public final boolean canSplit() {
            return getColorCount() > 1;
        }

        /* Access modifiers changed, original: final */
        public final int getColorCount() {
            return (this.mUpperIndex + 1) - this.mLowerIndex;
        }

        /* Access modifiers changed, original: final */
        public final void fitBox() {
            ColorCutQuantizer colorCutQuantizer = ColorCutQuantizer.this;
            int[] iArr = colorCutQuantizer.mColors;
            int[] iArr2 = colorCutQuantizer.mHistogram;
            int i = Integer.MAX_VALUE;
            int i2 = Integer.MIN_VALUE;
            int i3 = Integer.MAX_VALUE;
            int i4 = Integer.MIN_VALUE;
            int i5 = Integer.MAX_VALUE;
            int i6 = Integer.MIN_VALUE;
            int i7 = 0;
            for (int i8 = this.mLowerIndex; i8 <= this.mUpperIndex; i8++) {
                int i9 = iArr[i8];
                i7 += iArr2[i9];
                int quantizedRed = ColorCutQuantizer.quantizedRed(i9);
                int quantizedGreen = ColorCutQuantizer.quantizedGreen(i9);
                i9 = ColorCutQuantizer.quantizedBlue(i9);
                if (quantizedRed > i2) {
                    i2 = quantizedRed;
                }
                if (quantizedRed < i) {
                    i = quantizedRed;
                }
                if (quantizedGreen > i4) {
                    i4 = quantizedGreen;
                }
                if (quantizedGreen < i3) {
                    i3 = quantizedGreen;
                }
                if (i9 > i6) {
                    i6 = i9;
                }
                if (i9 < i5) {
                    i5 = i9;
                }
            }
            this.mMinRed = i;
            this.mMaxRed = i2;
            this.mMinGreen = i3;
            this.mMaxGreen = i4;
            this.mMinBlue = i5;
            this.mMaxBlue = i6;
            this.mPopulation = i7;
        }

        /* Access modifiers changed, original: final */
        public final Vbox splitBox() {
            if (canSplit()) {
                int findSplitPoint = findSplitPoint();
                Vbox vbox = new Vbox(findSplitPoint + 1, this.mUpperIndex);
                this.mUpperIndex = findSplitPoint;
                fitBox();
                return vbox;
            }
            throw new IllegalStateException("Can not split a box with only 1 color");
        }

        /* Access modifiers changed, original: final */
        public final int getLongestColorDimension() {
            int i = this.mMaxRed - this.mMinRed;
            int i2 = this.mMaxGreen - this.mMinGreen;
            int i3 = this.mMaxBlue - this.mMinBlue;
            if (i < i2 || i < i3) {
                return (i2 < i || i2 < i3) ? -1 : -2;
            } else {
                return -3;
            }
        }

        /* Access modifiers changed, original: final */
        public final int findSplitPoint() {
            int longestColorDimension = getLongestColorDimension();
            ColorCutQuantizer colorCutQuantizer = ColorCutQuantizer.this;
            int[] iArr = colorCutQuantizer.mColors;
            int[] iArr2 = colorCutQuantizer.mHistogram;
            ColorCutQuantizer.modifySignificantOctet(iArr, longestColorDimension, this.mLowerIndex, this.mUpperIndex);
            Arrays.sort(iArr, this.mLowerIndex, this.mUpperIndex + 1);
            ColorCutQuantizer.modifySignificantOctet(iArr, longestColorDimension, this.mLowerIndex, this.mUpperIndex);
            longestColorDimension = this.mPopulation / 2;
            int i = this.mLowerIndex;
            int i2 = 0;
            while (true) {
                int i3 = this.mUpperIndex;
                if (i > i3) {
                    return this.mLowerIndex;
                }
                i2 += iArr2[iArr[i]];
                if (i2 >= longestColorDimension) {
                    return Math.min(i3 - 1, i);
                }
                i++;
            }
        }

        /* Access modifiers changed, original: final */
        public final Swatch getAverageColor() {
            ColorCutQuantizer colorCutQuantizer = ColorCutQuantizer.this;
            int[] iArr = colorCutQuantizer.mColors;
            int[] iArr2 = colorCutQuantizer.mHistogram;
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            for (int i5 = this.mLowerIndex; i5 <= this.mUpperIndex; i5++) {
                int i6 = iArr[i5];
                int i7 = iArr2[i6];
                i2 += i7;
                i += ColorCutQuantizer.quantizedRed(i6) * i7;
                i3 += ColorCutQuantizer.quantizedGreen(i6) * i7;
                i4 += i7 * ColorCutQuantizer.quantizedBlue(i6);
            }
            float f = (float) i2;
            return new Swatch(ColorCutQuantizer.approximateToRgb888(Math.round(((float) i) / f), Math.round(((float) i3) / f), Math.round(((float) i4) / f)), i2);
        }
    }

    private static int modifyWordWidth(int i, int i2, int i3) {
        return (i3 > i2 ? i << (i3 - i2) : i >> (i2 - i3)) & ((1 << i3) - 1);
    }

    static int quantizedBlue(int i) {
        return i & 31;
    }

    static int quantizedGreen(int i) {
        return (i >> 5) & 31;
    }

    static int quantizedRed(int i) {
        return (i >> 10) & 31;
    }

    ColorCutQuantizer(int[] iArr, int i, Filter[] filterArr) {
        int i2;
        int quantizeFromRgb888;
        this.mFilters = filterArr;
        int[] iArr2 = new int[32768];
        this.mHistogram = iArr2;
        int i3 = 0;
        for (i2 = 0; i2 < iArr.length; i2++) {
            quantizeFromRgb888 = quantizeFromRgb888(iArr[i2]);
            iArr[i2] = quantizeFromRgb888;
            iArr2[quantizeFromRgb888] = iArr2[quantizeFromRgb888] + 1;
        }
        int i4 = 0;
        i2 = 0;
        while (i4 < iArr2.length) {
            if (iArr2[i4] > 0 && shouldIgnoreColor(i4)) {
                iArr2[i4] = 0;
            }
            if (iArr2[i4] > 0) {
                i2++;
            }
            i4++;
        }
        iArr = new int[i2];
        this.mColors = iArr;
        int i5 = 0;
        for (quantizeFromRgb888 = 0; quantizeFromRgb888 < iArr2.length; quantizeFromRgb888++) {
            if (iArr2[quantizeFromRgb888] > 0) {
                int i6 = i5 + 1;
                iArr[i5] = quantizeFromRgb888;
                i5 = i6;
            }
        }
        if (i2 <= i) {
            this.mQuantizedColors = new ArrayList();
            i = iArr.length;
            while (i3 < i) {
                i2 = iArr[i3];
                this.mQuantizedColors.add(new Swatch(approximateToRgb888(i2), iArr2[i2]));
                i3++;
            }
            return;
        }
        this.mQuantizedColors = quantizePixels(i);
    }

    /* Access modifiers changed, original: 0000 */
    public List<Swatch> getQuantizedColors() {
        return this.mQuantizedColors;
    }

    private List<Swatch> quantizePixels(int i) {
        PriorityQueue priorityQueue = new PriorityQueue(i, VBOX_COMPARATOR_VOLUME);
        priorityQueue.offer(new Vbox(0, this.mColors.length - 1));
        splitBoxes(priorityQueue, i);
        return generateAverageColors(priorityQueue);
    }

    private void splitBoxes(PriorityQueue<Vbox> priorityQueue, int i) {
        while (priorityQueue.size() < i) {
            Vbox vbox = (Vbox) priorityQueue.poll();
            if (vbox != null && vbox.canSplit()) {
                priorityQueue.offer(vbox.splitBox());
                priorityQueue.offer(vbox);
            } else {
                return;
            }
        }
    }

    private List<Swatch> generateAverageColors(Collection<Vbox> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (Vbox averageColor : collection) {
            Swatch averageColor2 = averageColor.getAverageColor();
            if (!shouldIgnoreColor(averageColor2)) {
                arrayList.add(averageColor2);
            }
        }
        return arrayList;
    }

    static void modifySignificantOctet(int[] iArr, int i, int i2, int i3) {
        if (i == -3) {
            return;
        }
        if (i == -2) {
            while (i2 <= i3) {
                i = iArr[i2];
                iArr[i2] = quantizedBlue(i) | ((quantizedGreen(i) << 10) | (quantizedRed(i) << 5));
                i2++;
            }
        } else if (i == -1) {
            while (i2 <= i3) {
                i = iArr[i2];
                iArr[i2] = quantizedRed(i) | ((quantizedBlue(i) << 10) | (quantizedGreen(i) << 5));
                i2++;
            }
        }
    }

    private boolean shouldIgnoreColor(int i) {
        i = approximateToRgb888(i);
        ColorUtils.colorToHSL(i, this.mTempHsl);
        return shouldIgnoreColor(i, this.mTempHsl);
    }

    private boolean shouldIgnoreColor(Swatch swatch) {
        return shouldIgnoreColor(swatch.getRgb(), swatch.getHsl());
    }

    private boolean shouldIgnoreColor(int i, float[] fArr) {
        Filter[] filterArr = this.mFilters;
        if (filterArr != null && filterArr.length > 0) {
            int length = filterArr.length;
            for (int i2 = 0; i2 < length; i2++) {
                if (!this.mFilters[i2].isAllowed(i, fArr)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int quantizeFromRgb888(int i) {
        return modifyWordWidth(Color.blue(i), 8, 5) | ((modifyWordWidth(Color.red(i), 8, 5) << 10) | (modifyWordWidth(Color.green(i), 8, 5) << 5));
    }

    static int approximateToRgb888(int i, int i2, int i3) {
        return Color.rgb(modifyWordWidth(i, 5, 8), modifyWordWidth(i2, 5, 8), modifyWordWidth(i3, 5, 8));
    }

    private static int approximateToRgb888(int i) {
        return approximateToRgb888(quantizedRed(i), quantizedGreen(i), quantizedBlue(i));
    }
}
