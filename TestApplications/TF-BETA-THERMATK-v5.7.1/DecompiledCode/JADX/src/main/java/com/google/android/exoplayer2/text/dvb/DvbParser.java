package com.google.android.exoplayer2.text.dvb;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region.Op;
import android.util.SparseArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.telegram.p004ui.ActionBar.Theme;

final class DvbParser {
    private static final byte[] defaultMap2To4 = new byte[]{(byte) 0, (byte) 7, (byte) 8, (byte) 15};
    private static final byte[] defaultMap2To8 = new byte[]{(byte) 0, (byte) 119, (byte) -120, (byte) -1};
    private static final byte[] defaultMap4To8 = new byte[]{(byte) 0, (byte) 17, (byte) 34, (byte) 51, (byte) 68, (byte) 85, (byte) 102, (byte) 119, (byte) -120, (byte) -103, (byte) -86, (byte) -69, (byte) -52, (byte) -35, (byte) -18, (byte) -1};
    private Bitmap bitmap;
    private final Canvas canvas;
    private final ClutDefinition defaultClutDefinition;
    private final DisplayDefinition defaultDisplayDefinition;
    private final Paint defaultPaint = new Paint();
    private final Paint fillRegionPaint;
    private final SubtitleService subtitleService;

    private static final class ClutDefinition {
        public final int[] clutEntries2Bit;
        public final int[] clutEntries4Bit;
        public final int[] clutEntries8Bit;
        /* renamed from: id */
        public final int f26id;

        public ClutDefinition(int i, int[] iArr, int[] iArr2, int[] iArr3) {
            this.f26id = i;
            this.clutEntries2Bit = iArr;
            this.clutEntries4Bit = iArr2;
            this.clutEntries8Bit = iArr3;
        }
    }

    private static final class DisplayDefinition {
        public final int height;
        public final int horizontalPositionMaximum;
        public final int horizontalPositionMinimum;
        public final int verticalPositionMaximum;
        public final int verticalPositionMinimum;
        public final int width;

        public DisplayDefinition(int i, int i2, int i3, int i4, int i5, int i6) {
            this.width = i;
            this.height = i2;
            this.horizontalPositionMinimum = i3;
            this.horizontalPositionMaximum = i4;
            this.verticalPositionMinimum = i5;
            this.verticalPositionMaximum = i6;
        }
    }

    private static final class ObjectData {
        public final byte[] bottomFieldData;
        /* renamed from: id */
        public final int f27id;
        public final boolean nonModifyingColorFlag;
        public final byte[] topFieldData;

        public ObjectData(int i, boolean z, byte[] bArr, byte[] bArr2) {
            this.f27id = i;
            this.nonModifyingColorFlag = z;
            this.topFieldData = bArr;
            this.bottomFieldData = bArr2;
        }
    }

    private static final class PageComposition {
        public final SparseArray<PageRegion> regions;
        public final int state;
        public final int timeOutSecs;
        public final int version;

        public PageComposition(int i, int i2, int i3, SparseArray<PageRegion> sparseArray) {
            this.timeOutSecs = i;
            this.version = i2;
            this.state = i3;
            this.regions = sparseArray;
        }
    }

    private static final class PageRegion {
        public final int horizontalAddress;
        public final int verticalAddress;

        public PageRegion(int i, int i2) {
            this.horizontalAddress = i;
            this.verticalAddress = i2;
        }
    }

    private static final class RegionComposition {
        public final int clutId;
        public final int depth;
        public final boolean fillFlag;
        public final int height;
        /* renamed from: id */
        public final int f28id;
        public final int levelOfCompatibility;
        public final int pixelCode2Bit;
        public final int pixelCode4Bit;
        public final int pixelCode8Bit;
        public final SparseArray<RegionObject> regionObjects;
        public final int width;

        public RegionComposition(int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, SparseArray<RegionObject> sparseArray) {
            this.f28id = i;
            this.fillFlag = z;
            this.width = i2;
            this.height = i3;
            this.levelOfCompatibility = i4;
            this.depth = i5;
            this.clutId = i6;
            this.pixelCode8Bit = i7;
            this.pixelCode4Bit = i8;
            this.pixelCode2Bit = i9;
            this.regionObjects = sparseArray;
        }

        public void mergeFrom(RegionComposition regionComposition) {
            if (regionComposition != null) {
                SparseArray sparseArray = regionComposition.regionObjects;
                for (int i = 0; i < sparseArray.size(); i++) {
                    this.regionObjects.put(sparseArray.keyAt(i), sparseArray.valueAt(i));
                }
            }
        }
    }

    private static final class RegionObject {
        public final int backgroundPixelCode;
        public final int foregroundPixelCode;
        public final int horizontalPosition;
        public final int provider;
        public final int type;
        public final int verticalPosition;

        public RegionObject(int i, int i2, int i3, int i4, int i5, int i6) {
            this.type = i;
            this.provider = i2;
            this.horizontalPosition = i3;
            this.verticalPosition = i4;
            this.foregroundPixelCode = i5;
            this.backgroundPixelCode = i6;
        }
    }

    private static final class SubtitleService {
        public final SparseArray<ClutDefinition> ancillaryCluts = new SparseArray();
        public final SparseArray<ObjectData> ancillaryObjects = new SparseArray();
        public final int ancillaryPageId;
        public final SparseArray<ClutDefinition> cluts = new SparseArray();
        public DisplayDefinition displayDefinition;
        public final SparseArray<ObjectData> objects = new SparseArray();
        public PageComposition pageComposition;
        public final SparseArray<RegionComposition> regions = new SparseArray();
        public final int subtitlePageId;

        public SubtitleService(int i, int i2) {
            this.subtitlePageId = i;
            this.ancillaryPageId = i2;
        }

        public void reset() {
            this.regions.clear();
            this.cluts.clear();
            this.objects.clear();
            this.ancillaryCluts.clear();
            this.ancillaryObjects.clear();
            this.displayDefinition = null;
            this.pageComposition = null;
        }
    }

    private static int getColor(int i, int i2, int i3, int i4) {
        return (((i << 24) | (i2 << 16)) | (i3 << 8)) | i4;
    }

    public DvbParser(int i, int i2) {
        this.defaultPaint.setStyle(Style.FILL_AND_STROKE);
        this.defaultPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
        this.defaultPaint.setPathEffect(null);
        this.fillRegionPaint = new Paint();
        this.fillRegionPaint.setStyle(Style.FILL);
        this.fillRegionPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
        this.fillRegionPaint.setPathEffect(null);
        this.canvas = new Canvas();
        this.defaultDisplayDefinition = new DisplayDefinition(719, 575, 0, 719, 0, 575);
        this.defaultClutDefinition = new ClutDefinition(0, generateDefault2BitClutEntries(), generateDefault4BitClutEntries(), generateDefault8BitClutEntries());
        this.subtitleService = new SubtitleService(i, i2);
    }

    public void reset() {
        this.subtitleService.reset();
    }

    public List<Cue> decode(byte[] bArr, int i) {
        ParsableBitArray parsableBitArray = new ParsableBitArray(bArr, i);
        while (parsableBitArray.bitsLeft() >= 48 && parsableBitArray.readBits(8) == 15) {
            parseSubtitlingSegment(parsableBitArray, this.subtitleService);
        }
        SubtitleService subtitleService = this.subtitleService;
        if (subtitleService.pageComposition == null) {
            return Collections.emptyList();
        }
        DisplayDefinition displayDefinition = subtitleService.displayDefinition;
        if (displayDefinition == null) {
            displayDefinition = this.defaultDisplayDefinition;
        }
        Bitmap bitmap = this.bitmap;
        if (!(bitmap != null && displayDefinition.width + 1 == bitmap.getWidth() && displayDefinition.height + 1 == this.bitmap.getHeight())) {
            this.bitmap = Bitmap.createBitmap(displayDefinition.width + 1, displayDefinition.height + 1, Config.ARGB_8888);
            this.canvas.setBitmap(this.bitmap);
        }
        ArrayList arrayList = new ArrayList();
        SparseArray sparseArray = this.subtitleService.pageComposition.regions;
        for (int i2 = 0; i2 < sparseArray.size(); i2++) {
            PageRegion pageRegion = (PageRegion) sparseArray.valueAt(i2);
            RegionComposition regionComposition = (RegionComposition) this.subtitleService.regions.get(sparseArray.keyAt(i2));
            int i3 = pageRegion.horizontalAddress + displayDefinition.horizontalPositionMinimum;
            int i4 = pageRegion.verticalAddress + displayDefinition.verticalPositionMinimum;
            float f = (float) i3;
            float f2 = (float) i4;
            float f3 = f2;
            float f4 = f;
            this.canvas.clipRect(f, f2, (float) Math.min(regionComposition.width + i3, displayDefinition.horizontalPositionMaximum), (float) Math.min(regionComposition.height + i4, displayDefinition.verticalPositionMaximum), Op.REPLACE);
            ClutDefinition clutDefinition = (ClutDefinition) this.subtitleService.cluts.get(regionComposition.clutId);
            if (clutDefinition == null) {
                clutDefinition = (ClutDefinition) this.subtitleService.ancillaryCluts.get(regionComposition.clutId);
                if (clutDefinition == null) {
                    clutDefinition = this.defaultClutDefinition;
                }
            }
            SparseArray sparseArray2 = regionComposition.regionObjects;
            int i5 = 0;
            while (i5 < sparseArray2.size()) {
                int i6;
                SparseArray sparseArray3;
                int keyAt = sparseArray2.keyAt(i5);
                RegionObject regionObject = (RegionObject) sparseArray2.valueAt(i5);
                ObjectData objectData = (ObjectData) this.subtitleService.objects.get(keyAt);
                ObjectData objectData2 = objectData == null ? (ObjectData) this.subtitleService.ancillaryObjects.get(keyAt) : objectData;
                if (objectData2 != null) {
                    i6 = i5;
                    sparseArray3 = sparseArray2;
                    paintPixelDataSubBlocks(objectData2, clutDefinition, regionComposition.depth, regionObject.horizontalPosition + i3, i4 + regionObject.verticalPosition, objectData2.nonModifyingColorFlag ? null : this.defaultPaint, this.canvas);
                } else {
                    i6 = i5;
                    sparseArray3 = sparseArray2;
                }
                i5 = i6 + 1;
                sparseArray2 = sparseArray3;
            }
            if (regionComposition.fillFlag) {
                int i7 = regionComposition.depth;
                if (i7 == 3) {
                    i7 = clutDefinition.clutEntries8Bit[regionComposition.pixelCode8Bit];
                } else if (i7 == 2) {
                    i7 = clutDefinition.clutEntries4Bit[regionComposition.pixelCode4Bit];
                } else {
                    i7 = clutDefinition.clutEntries2Bit[regionComposition.pixelCode2Bit];
                }
                this.fillRegionPaint.setColor(i7);
                this.canvas.drawRect(f4, f3, (float) (regionComposition.width + i3), (float) (regionComposition.height + i4), this.fillRegionPaint);
            }
            Bitmap createBitmap = Bitmap.createBitmap(this.bitmap, i3, i4, regionComposition.width, regionComposition.height);
            i4 = displayDefinition.width;
            float f5 = f4 / ((float) i4);
            i3 = displayDefinition.height;
            arrayList.add(new Cue(createBitmap, f5, 0, f3 / ((float) i3), 0, ((float) regionComposition.width) / ((float) i4), ((float) regionComposition.height) / ((float) i3)));
            this.canvas.drawColor(0, Mode.CLEAR);
        }
        return arrayList;
    }

    private static void parseSubtitlingSegment(ParsableBitArray parsableBitArray, SubtitleService subtitleService) {
        int readBits = parsableBitArray.readBits(8);
        int readBits2 = parsableBitArray.readBits(16);
        int readBits3 = parsableBitArray.readBits(16);
        int bytePosition = parsableBitArray.getBytePosition() + readBits3;
        if (readBits3 * 8 > parsableBitArray.bitsLeft()) {
            Log.m18w("DvbParser", "Data field length exceeds limit");
            parsableBitArray.skipBits(parsableBitArray.bitsLeft());
            return;
        }
        PageComposition pageComposition;
        switch (readBits) {
            case 16:
                if (readBits2 == subtitleService.subtitlePageId) {
                    pageComposition = subtitleService.pageComposition;
                    PageComposition parsePageComposition = parsePageComposition(parsableBitArray, readBits3);
                    if (parsePageComposition.state == 0) {
                        if (!(pageComposition == null || pageComposition.version == parsePageComposition.version)) {
                            subtitleService.pageComposition = parsePageComposition;
                            break;
                        }
                    }
                    subtitleService.pageComposition = parsePageComposition;
                    subtitleService.regions.clear();
                    subtitleService.cluts.clear();
                    subtitleService.objects.clear();
                    break;
                }
                break;
            case 17:
                pageComposition = subtitleService.pageComposition;
                if (readBits2 == subtitleService.subtitlePageId && pageComposition != null) {
                    RegionComposition parseRegionComposition = parseRegionComposition(parsableBitArray, readBits3);
                    if (pageComposition.state == 0) {
                        parseRegionComposition.mergeFrom((RegionComposition) subtitleService.regions.get(parseRegionComposition.f28id));
                    }
                    subtitleService.regions.put(parseRegionComposition.f28id, parseRegionComposition);
                    break;
                }
            case 18:
                ClutDefinition parseClutDefinition;
                if (readBits2 != subtitleService.subtitlePageId) {
                    if (readBits2 == subtitleService.ancillaryPageId) {
                        parseClutDefinition = parseClutDefinition(parsableBitArray, readBits3);
                        subtitleService.ancillaryCluts.put(parseClutDefinition.f26id, parseClutDefinition);
                        break;
                    }
                }
                parseClutDefinition = parseClutDefinition(parsableBitArray, readBits3);
                subtitleService.cluts.put(parseClutDefinition.f26id, parseClutDefinition);
                break;
                break;
            case 19:
                ObjectData parseObjectData;
                if (readBits2 != subtitleService.subtitlePageId) {
                    if (readBits2 == subtitleService.ancillaryPageId) {
                        parseObjectData = parseObjectData(parsableBitArray);
                        subtitleService.ancillaryObjects.put(parseObjectData.f27id, parseObjectData);
                        break;
                    }
                }
                parseObjectData = parseObjectData(parsableBitArray);
                subtitleService.objects.put(parseObjectData.f27id, parseObjectData);
                break;
                break;
            case 20:
                if (readBits2 == subtitleService.subtitlePageId) {
                    subtitleService.displayDefinition = parseDisplayDefinition(parsableBitArray);
                    break;
                }
                break;
        }
        parsableBitArray.skipBytes(bytePosition - parsableBitArray.getBytePosition());
    }

    private static DisplayDefinition parseDisplayDefinition(ParsableBitArray parsableBitArray) {
        int readBits;
        int readBits2;
        int i;
        int i2;
        parsableBitArray.skipBits(4);
        boolean readBit = parsableBitArray.readBit();
        parsableBitArray.skipBits(3);
        int readBits3 = parsableBitArray.readBits(16);
        int readBits4 = parsableBitArray.readBits(16);
        if (readBit) {
            int readBits5 = parsableBitArray.readBits(16);
            int readBits6 = parsableBitArray.readBits(16);
            readBits = parsableBitArray.readBits(16);
            readBits2 = parsableBitArray.readBits(16);
            i = readBits6;
            i2 = readBits;
            readBits = readBits5;
        } else {
            i = readBits3;
            readBits2 = readBits4;
            readBits = 0;
            i2 = 0;
        }
        return new DisplayDefinition(readBits3, readBits4, readBits, i, i2, readBits2);
    }

    private static PageComposition parsePageComposition(ParsableBitArray parsableBitArray, int i) {
        int readBits = parsableBitArray.readBits(8);
        int readBits2 = parsableBitArray.readBits(4);
        int readBits3 = parsableBitArray.readBits(2);
        parsableBitArray.skipBits(2);
        i -= 2;
        SparseArray sparseArray = new SparseArray();
        while (i > 0) {
            int readBits4 = parsableBitArray.readBits(8);
            parsableBitArray.skipBits(8);
            i -= 6;
            sparseArray.put(readBits4, new PageRegion(parsableBitArray.readBits(16), parsableBitArray.readBits(16)));
        }
        return new PageComposition(readBits, readBits2, readBits3, sparseArray);
    }

    private static RegionComposition parseRegionComposition(ParsableBitArray parsableBitArray, int i) {
        ParsableBitArray parsableBitArray2 = parsableBitArray;
        int readBits = parsableBitArray2.readBits(8);
        parsableBitArray2.skipBits(4);
        boolean readBit = parsableBitArray.readBit();
        parsableBitArray2.skipBits(3);
        int i2 = 16;
        int readBits2 = parsableBitArray2.readBits(16);
        int readBits3 = parsableBitArray2.readBits(16);
        int readBits4 = parsableBitArray2.readBits(3);
        int readBits5 = parsableBitArray2.readBits(3);
        int i3 = 2;
        parsableBitArray2.skipBits(2);
        int readBits6 = parsableBitArray2.readBits(8);
        int readBits7 = parsableBitArray2.readBits(8);
        int readBits8 = parsableBitArray2.readBits(4);
        int readBits9 = parsableBitArray2.readBits(2);
        parsableBitArray2.skipBits(2);
        int i4 = i - 10;
        SparseArray sparseArray = new SparseArray();
        while (i4 > 0) {
            int readBits10;
            int readBits11;
            int readBits12 = parsableBitArray2.readBits(i2);
            i2 = parsableBitArray2.readBits(i3);
            int readBits13 = parsableBitArray2.readBits(i3);
            int readBits14 = parsableBitArray2.readBits(12);
            int i5 = readBits9;
            parsableBitArray2.skipBits(4);
            int readBits15 = parsableBitArray2.readBits(12);
            i4 -= 6;
            if (i2 == 1 || i2 == 2) {
                i4 -= 2;
                readBits10 = parsableBitArray2.readBits(8);
                readBits11 = parsableBitArray2.readBits(8);
            } else {
                readBits10 = 0;
                readBits11 = 0;
            }
            sparseArray.put(readBits12, new RegionObject(i2, readBits13, readBits14, readBits15, readBits10, readBits11));
            readBits9 = i5;
            i3 = 2;
            i2 = 16;
        }
        return new RegionComposition(readBits, readBit, readBits2, readBits3, readBits4, readBits5, readBits6, readBits7, readBits8, readBits9, sparseArray);
    }

    private static ClutDefinition parseClutDefinition(ParsableBitArray parsableBitArray, int i) {
        ParsableBitArray parsableBitArray2 = parsableBitArray;
        int i2 = 8;
        int readBits = parsableBitArray2.readBits(8);
        parsableBitArray2.skipBits(8);
        int i3 = 2;
        int i4 = i - 2;
        int[] generateDefault2BitClutEntries = generateDefault2BitClutEntries();
        int[] generateDefault4BitClutEntries = generateDefault4BitClutEntries();
        int[] generateDefault8BitClutEntries = generateDefault8BitClutEntries();
        while (i4 > 0) {
            int readBits2;
            int readBits3;
            int readBits4;
            int readBits5 = parsableBitArray2.readBits(i2);
            int readBits6 = parsableBitArray2.readBits(i2);
            i4 -= 2;
            int[] iArr = (readBits6 & 128) != 0 ? generateDefault2BitClutEntries : (readBits6 & 64) != 0 ? generateDefault4BitClutEntries : generateDefault8BitClutEntries;
            if ((readBits6 & 1) != 0) {
                readBits6 = parsableBitArray2.readBits(i2);
                readBits2 = parsableBitArray2.readBits(i2);
                readBits3 = parsableBitArray2.readBits(i2);
                readBits4 = parsableBitArray2.readBits(i2);
                i4 -= 4;
            } else {
                readBits3 = parsableBitArray2.readBits(4) << 4;
                i4 -= 2;
                readBits4 = parsableBitArray2.readBits(i3) << 6;
                readBits6 = parsableBitArray2.readBits(6) << i3;
                readBits2 = parsableBitArray2.readBits(4) << 4;
            }
            if (readBits6 == 0) {
                readBits2 = 0;
                readBits3 = 0;
                readBits4 = NalUnitUtil.EXTENDED_SAR;
            }
            byte b = (byte) (255 - (readBits4 & NalUnitUtil.EXTENDED_SAR));
            i = i4;
            double d = (double) readBits6;
            int i5 = readBits;
            double d2 = (double) (readBits2 - 128);
            Double.isNaN(d2);
            double d3 = 1.402d * d2;
            Double.isNaN(d);
            int[] iArr2 = iArr;
            readBits6 = (int) (d + d3);
            double d4 = (double) (readBits3 - 128);
            Double.isNaN(d4);
            d3 = 0.34414d * d4;
            Double.isNaN(d);
            d3 = d - d3;
            Double.isNaN(d2);
            i2 = (int) (d3 - (d2 * 0.71414d));
            Double.isNaN(d4);
            d4 *= 1.772d;
            Double.isNaN(d);
            iArr2[readBits5] = getColor(b, Util.constrainValue(readBits6, 0, (int) NalUnitUtil.EXTENDED_SAR), Util.constrainValue(i2, 0, (int) NalUnitUtil.EXTENDED_SAR), Util.constrainValue((int) (d + d4), 0, (int) NalUnitUtil.EXTENDED_SAR));
            i4 = i;
            readBits = i5;
            i2 = 8;
            i3 = 2;
        }
        return new ClutDefinition(readBits, generateDefault2BitClutEntries, generateDefault4BitClutEntries, generateDefault8BitClutEntries);
    }

    private static ObjectData parseObjectData(ParsableBitArray parsableBitArray) {
        byte[] bArr;
        int readBits = parsableBitArray.readBits(16);
        parsableBitArray.skipBits(4);
        int readBits2 = parsableBitArray.readBits(2);
        boolean readBit = parsableBitArray.readBit();
        parsableBitArray.skipBits(1);
        byte[] bArr2 = null;
        if (readBits2 == 1) {
            parsableBitArray.skipBits(parsableBitArray.readBits(8) * 16);
        } else if (readBits2 == 0) {
            readBits2 = parsableBitArray.readBits(16);
            int readBits3 = parsableBitArray.readBits(16);
            if (readBits2 > 0) {
                bArr2 = new byte[readBits2];
                parsableBitArray.readBytes(bArr2, 0, readBits2);
            }
            if (readBits3 > 0) {
                bArr = new byte[readBits3];
                parsableBitArray.readBytes(bArr, 0, readBits3);
                return new ObjectData(readBits, readBit, bArr2, bArr);
            }
        }
        bArr = bArr2;
        return new ObjectData(readBits, readBit, bArr2, bArr);
    }

    private static int[] generateDefault2BitClutEntries() {
        return new int[]{0, -1, Theme.ACTION_BAR_VIDEO_EDIT_COLOR, -8421505};
    }

    private static int[] generateDefault4BitClutEntries() {
        int[] iArr = new int[16];
        iArr[0] = 0;
        for (int i = 1; i < iArr.length; i++) {
            if (i < 8) {
                iArr[i] = getColor(NalUnitUtil.EXTENDED_SAR, (i & 1) != 0 ? NalUnitUtil.EXTENDED_SAR : 0, (i & 2) != 0 ? NalUnitUtil.EXTENDED_SAR : 0, (i & 4) != 0 ? NalUnitUtil.EXTENDED_SAR : 0);
            } else {
                int i2 = 127;
                int i3 = (i & 1) != 0 ? 127 : 0;
                int i4 = (i & 2) != 0 ? 127 : 0;
                if ((i & 4) == 0) {
                    i2 = 0;
                }
                iArr[i] = getColor(NalUnitUtil.EXTENDED_SAR, i3, i4, i2);
            }
        }
        return iArr;
    }

    private static int[] generateDefault8BitClutEntries() {
        int[] iArr = new int[256];
        iArr[0] = 0;
        for (int i = 0; i < iArr.length; i++) {
            int i2 = NalUnitUtil.EXTENDED_SAR;
            int i3;
            int i4;
            if (i < 8) {
                i3 = (i & 1) != 0 ? NalUnitUtil.EXTENDED_SAR : 0;
                i4 = (i & 2) != 0 ? NalUnitUtil.EXTENDED_SAR : 0;
                if ((i & 4) == 0) {
                    i2 = 0;
                }
                iArr[i] = getColor(63, i3, i4, i2);
            } else {
                i3 = i & 136;
                i4 = 170;
                int i5 = 85;
                int i6;
                if (i3 == 0) {
                    i6 = ((i & 1) != 0 ? 85 : 0) + ((i & 16) != 0 ? 170 : 0);
                    i3 = ((i & 2) != 0 ? 85 : 0) + ((i & 32) != 0 ? 170 : 0);
                    if ((i & 4) == 0) {
                        i5 = 0;
                    }
                    if ((i & 64) == 0) {
                        i4 = 0;
                    }
                    iArr[i] = getColor(NalUnitUtil.EXTENDED_SAR, i6, i3, i5 + i4);
                } else if (i3 != 8) {
                    i4 = 43;
                    if (i3 == 128) {
                        i6 = (((i & 1) != 0 ? 43 : 0) + 127) + ((i & 16) != 0 ? 85 : 0);
                        i3 = (((i & 2) != 0 ? 43 : 0) + 127) + ((i & 32) != 0 ? 85 : 0);
                        if ((i & 4) == 0) {
                            i4 = 0;
                        }
                        i4 += 127;
                        if ((i & 64) == 0) {
                            i5 = 0;
                        }
                        iArr[i] = getColor(NalUnitUtil.EXTENDED_SAR, i6, i3, i4 + i5);
                    } else if (i3 == 136) {
                        i6 = ((i & 1) != 0 ? 43 : 0) + ((i & 16) != 0 ? 85 : 0);
                        i3 = ((i & 2) != 0 ? 43 : 0) + ((i & 32) != 0 ? 85 : 0);
                        if ((i & 4) == 0) {
                            i4 = 0;
                        }
                        if ((i & 64) == 0) {
                            i5 = 0;
                        }
                        iArr[i] = getColor(NalUnitUtil.EXTENDED_SAR, i6, i3, i4 + i5);
                    }
                } else {
                    i6 = ((i & 1) != 0 ? 85 : 0) + ((i & 16) != 0 ? 170 : 0);
                    i2 = ((i & 2) != 0 ? 85 : 0) + ((i & 32) != 0 ? 170 : 0);
                    if ((i & 4) == 0) {
                        i5 = 0;
                    }
                    if ((i & 64) == 0) {
                        i4 = 0;
                    }
                    iArr[i] = getColor(127, i6, i2, i5 + i4);
                }
            }
        }
        return iArr;
    }

    private static void paintPixelDataSubBlocks(ObjectData objectData, ClutDefinition clutDefinition, int i, int i2, int i3, Paint paint, Canvas canvas) {
        int[] iArr;
        if (i == 3) {
            iArr = clutDefinition.clutEntries8Bit;
        } else if (i == 2) {
            iArr = clutDefinition.clutEntries4Bit;
        } else {
            iArr = clutDefinition.clutEntries2Bit;
        }
        int[] iArr2 = iArr;
        int i4 = i;
        int i5 = i2;
        Paint paint2 = paint;
        Canvas canvas2 = canvas;
        paintPixelDataSubBlock(objectData.topFieldData, iArr2, i4, i5, i3, paint2, canvas2);
        paintPixelDataSubBlock(objectData.bottomFieldData, iArr2, i4, i5, i3 + 1, paint2, canvas2);
    }

    private static void paintPixelDataSubBlock(byte[] bArr, int[] iArr, int i, int i2, int i3, Paint paint, Canvas canvas) {
        int i4 = i;
        byte[] bArr2 = bArr;
        ParsableBitArray parsableBitArray = new ParsableBitArray(bArr);
        int i5 = i2;
        int i6 = i3;
        byte[] bArr3 = null;
        byte[] bArr4 = bArr3;
        while (parsableBitArray.bitsLeft() != 0) {
            int readBits = parsableBitArray.readBits(8);
            if (readBits != 240) {
                int paint2BitPixelCodeString;
                switch (readBits) {
                    case 16:
                        byte[] bArr5;
                        if (i4 != 3) {
                            if (i4 != 2) {
                                bArr5 = null;
                                paint2BitPixelCodeString = paint2BitPixelCodeString(parsableBitArray, iArr, bArr5, i5, i6, paint, canvas);
                                parsableBitArray.byteAlign();
                                break;
                            }
                            bArr2 = bArr4 == null ? defaultMap2To4 : bArr4;
                        } else {
                            bArr2 = bArr3 == null ? defaultMap2To8 : bArr3;
                        }
                        bArr5 = bArr2;
                        paint2BitPixelCodeString = paint2BitPixelCodeString(parsableBitArray, iArr, bArr5, i5, i6, paint, canvas);
                        parsableBitArray.byteAlign();
                    case 17:
                        paint2BitPixelCodeString = paint4BitPixelCodeString(parsableBitArray, iArr, i4 == 3 ? defaultMap4To8 : null, i5, i6, paint, canvas);
                        parsableBitArray.byteAlign();
                        break;
                    case 18:
                        paint2BitPixelCodeString = paint8BitPixelCodeString(parsableBitArray, iArr, null, i5, i6, paint, canvas);
                        break;
                    default:
                        switch (readBits) {
                            case 32:
                                bArr4 = buildClutMapTable(4, 4, parsableBitArray);
                                continue;
                            case 33:
                                bArr2 = buildClutMapTable(4, 8, parsableBitArray);
                                break;
                            case 34:
                                bArr2 = buildClutMapTable(16, 8, parsableBitArray);
                                break;
                            default:
                                continue;
                                continue;
                        }
                        bArr3 = bArr2;
                        break;
                }
                i5 = paint2BitPixelCodeString;
            } else {
                i6 += 2;
                i5 = i2;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0083 A:{LOOP_END, LOOP:0: B:1:0x0009->B:30:0x0083} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0082 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0082 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0083 A:{LOOP_END, LOOP:0: B:1:0x0009->B:30:0x0083} */
    private static int paint2BitPixelCodeString(com.google.android.exoplayer2.util.ParsableBitArray r13, int[] r14, byte[] r15, int r16, int r17, android.graphics.Paint r18, android.graphics.Canvas r19) {
        /*
        r0 = r13;
        r1 = r17;
        r8 = r18;
        r9 = 0;
        r10 = r16;
        r2 = 0;
    L_0x0009:
        r3 = 2;
        r4 = r13.readBits(r3);
        r5 = 1;
        if (r4 == 0) goto L_0x0015;
    L_0x0011:
        r12 = r2;
        r3 = r4;
    L_0x0013:
        r11 = 1;
        goto L_0x0061;
    L_0x0015:
        r4 = r13.readBit();
        r6 = 3;
        if (r4 == 0) goto L_0x0028;
    L_0x001c:
        r4 = r13.readBits(r6);
        r4 = r4 + r6;
        r3 = r13.readBits(r3);
    L_0x0025:
        r12 = r2;
        r11 = r4;
        goto L_0x0061;
    L_0x0028:
        r4 = r13.readBit();
        if (r4 == 0) goto L_0x0031;
    L_0x002e:
        r12 = r2;
        r3 = 0;
        goto L_0x0013;
    L_0x0031:
        r4 = r13.readBits(r3);
        if (r4 == 0) goto L_0x005e;
    L_0x0037:
        if (r4 == r5) goto L_0x005a;
    L_0x0039:
        if (r4 == r3) goto L_0x004e;
    L_0x003b:
        if (r4 == r6) goto L_0x0041;
    L_0x003d:
        r12 = r2;
        r3 = 0;
        r11 = 0;
        goto L_0x0061;
    L_0x0041:
        r4 = 8;
        r4 = r13.readBits(r4);
        r4 = r4 + 29;
        r3 = r13.readBits(r3);
        goto L_0x0025;
    L_0x004e:
        r4 = 4;
        r4 = r13.readBits(r4);
        r4 = r4 + 12;
        r3 = r13.readBits(r3);
        goto L_0x0025;
    L_0x005a:
        r12 = r2;
        r3 = 0;
        r11 = 2;
        goto L_0x0061;
    L_0x005e:
        r3 = 0;
        r11 = 0;
        r12 = 1;
    L_0x0061:
        if (r11 == 0) goto L_0x007f;
    L_0x0063:
        if (r8 == 0) goto L_0x007f;
    L_0x0065:
        if (r15 == 0) goto L_0x0069;
    L_0x0067:
        r3 = r15[r3];
    L_0x0069:
        r2 = r14[r3];
        r8.setColor(r2);
        r3 = (float) r10;
        r4 = (float) r1;
        r2 = r10 + r11;
        r6 = (float) r2;
        r2 = r1 + 1;
        r7 = (float) r2;
        r2 = r19;
        r5 = r6;
        r6 = r7;
        r7 = r18;
        r2.drawRect(r3, r4, r5, r6, r7);
    L_0x007f:
        r10 = r10 + r11;
        if (r12 == 0) goto L_0x0083;
    L_0x0082:
        return r10;
    L_0x0083:
        r2 = r12;
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.dvb.DvbParser.paint2BitPixelCodeString(com.google.android.exoplayer2.util.ParsableBitArray, int[], byte[], int, int, android.graphics.Paint, android.graphics.Canvas):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008f A:{LOOP_END, LOOP:0: B:1:0x0009->B:33:0x008f} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x008e A:{SYNTHETIC} */
    private static int paint4BitPixelCodeString(com.google.android.exoplayer2.util.ParsableBitArray r13, int[] r14, byte[] r15, int r16, int r17, android.graphics.Paint r18, android.graphics.Canvas r19) {
        /*
        r0 = r13;
        r1 = r17;
        r8 = r18;
        r9 = 0;
        r10 = r16;
        r2 = 0;
    L_0x0009:
        r3 = 4;
        r4 = r13.readBits(r3);
        r5 = 2;
        r6 = 1;
        if (r4 == 0) goto L_0x0017;
    L_0x0012:
        r12 = r2;
        r3 = r4;
    L_0x0014:
        r11 = 1;
        goto L_0x006f;
    L_0x0017:
        r4 = r13.readBit();
        r7 = 3;
        if (r4 != 0) goto L_0x002e;
    L_0x001e:
        r3 = r13.readBits(r7);
        if (r3 == 0) goto L_0x002a;
    L_0x0024:
        r3 = r3 + 2;
        r12 = r2;
        r11 = r3;
        r3 = 0;
        goto L_0x006f;
    L_0x002a:
        r3 = 0;
        r11 = 0;
        r12 = 1;
        goto L_0x006f;
    L_0x002e:
        r4 = r13.readBit();
        if (r4 != 0) goto L_0x0040;
    L_0x0034:
        r4 = r13.readBits(r5);
        r4 = r4 + r3;
        r3 = r13.readBits(r3);
    L_0x003d:
        r12 = r2;
        r11 = r4;
        goto L_0x006f;
    L_0x0040:
        r4 = r13.readBits(r5);
        if (r4 == 0) goto L_0x006c;
    L_0x0046:
        if (r4 == r6) goto L_0x0068;
    L_0x0048:
        if (r4 == r5) goto L_0x005d;
    L_0x004a:
        if (r4 == r7) goto L_0x0050;
    L_0x004c:
        r12 = r2;
        r3 = 0;
        r11 = 0;
        goto L_0x006f;
    L_0x0050:
        r4 = 8;
        r4 = r13.readBits(r4);
        r4 = r4 + 25;
        r3 = r13.readBits(r3);
        goto L_0x003d;
    L_0x005d:
        r4 = r13.readBits(r3);
        r4 = r4 + 9;
        r3 = r13.readBits(r3);
        goto L_0x003d;
    L_0x0068:
        r12 = r2;
        r3 = 0;
        r11 = 2;
        goto L_0x006f;
    L_0x006c:
        r12 = r2;
        r3 = 0;
        goto L_0x0014;
    L_0x006f:
        if (r11 == 0) goto L_0x008b;
    L_0x0071:
        if (r8 == 0) goto L_0x008b;
    L_0x0073:
        if (r15 == 0) goto L_0x0077;
    L_0x0075:
        r3 = r15[r3];
    L_0x0077:
        r2 = r14[r3];
        r8.setColor(r2);
        r3 = (float) r10;
        r4 = (float) r1;
        r2 = r10 + r11;
        r5 = (float) r2;
        r2 = r1 + 1;
        r6 = (float) r2;
        r2 = r19;
        r7 = r18;
        r2.drawRect(r3, r4, r5, r6, r7);
    L_0x008b:
        r10 = r10 + r11;
        if (r12 == 0) goto L_0x008f;
    L_0x008e:
        return r10;
    L_0x008f:
        r2 = r12;
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.dvb.DvbParser.paint4BitPixelCodeString(com.google.android.exoplayer2.util.ParsableBitArray, int[], byte[], int, int, android.graphics.Paint, android.graphics.Canvas):int");
    }

    private static int paint8BitPixelCodeString(ParsableBitArray parsableBitArray, int[] iArr, byte[] bArr, int i, int i2, Paint paint, Canvas canvas) {
        ParsableBitArray parsableBitArray2 = parsableBitArray;
        int i3 = i2;
        Paint paint2 = paint;
        int i4 = i;
        Object obj = null;
        while (true) {
            Object obj2;
            int i5;
            int i6;
            int readBits = parsableBitArray.readBits(8);
            if (readBits != 0) {
                obj2 = obj;
                i5 = readBits;
                i6 = 1;
            } else if (parsableBitArray.readBit()) {
                readBits = parsableBitArray.readBits(7);
                i5 = parsableBitArray.readBits(8);
                obj2 = obj;
                i6 = readBits;
            } else {
                i5 = parsableBitArray.readBits(7);
                if (i5 != 0) {
                    obj2 = obj;
                    i6 = i5;
                    i5 = 0;
                } else {
                    i5 = 0;
                    i6 = 0;
                    obj2 = 1;
                }
            }
            if (!(i6 == 0 || paint2 == null)) {
                if (bArr != null) {
                    i5 = bArr[i5];
                }
                paint2.setColor(iArr[i5]);
                Canvas canvas2 = canvas;
                canvas2.drawRect((float) i4, (float) i3, (float) (i4 + i6), (float) (i3 + 1), paint);
            }
            i4 += i6;
            if (obj2 != null) {
                return i4;
            }
            obj = obj2;
        }
    }

    private static byte[] buildClutMapTable(int i, int i2, ParsableBitArray parsableBitArray) {
        byte[] bArr = new byte[i];
        for (int i3 = 0; i3 < i; i3++) {
            bArr[i3] = (byte) parsableBitArray.readBits(i2);
        }
        return bArr;
    }
}
