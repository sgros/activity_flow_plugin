package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class Id3Decoder implements MetadataDecoder {
    public static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    public static final FramePredicate NO_FRAMES_PREDICATE = C3344-$$Lambda$Id3Decoder$7M0gB-IGKaTbyTVX-WCb62bIHyc.INSTANCE;
    private final FramePredicate framePredicate;

    public interface FramePredicate {
        boolean evaluate(int i, int i2, int i3, int i4, int i5);
    }

    private static final class Id3Header {
        private final int framesSize;
        private final boolean isUnsynchronized;
        private final int majorVersion;

        public Id3Header(int i, boolean z, int i2) {
            this.majorVersion = i;
            this.isUnsynchronized = z;
            this.framesSize = i2;
        }
    }

    private static int delimiterLength(int i) {
        return (i == 0 || i == 3) ? 1 : 2;
    }

    private static String getCharsetName(int i) {
        return i != 1 ? i != 2 ? i != 3 ? "ISO-8859-1" : "UTF-8" : "UTF-16BE" : "UTF-16";
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:66:0x00b7 in {6, 8, 15, 22, 23, 27, 28, 30, 31, 35, 36, 39, 40, 41, 43, 44, 46, 50, 56, 60, 62, 65} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private static boolean validateFrames(com.google.android.exoplayer2.util.ParsableByteArray r18, int r19, int r20, boolean r21) {
        /*
        r1 = r18;
        r0 = r19;
        r2 = r18.getPosition();
        r3 = r18.bytesLeft();	 Catch:{ all -> 0x00b2 }
        r4 = 1;	 Catch:{ all -> 0x00b2 }
        r5 = r20;	 Catch:{ all -> 0x00b2 }
        if (r3 < r5) goto L_0x00ae;	 Catch:{ all -> 0x00b2 }
        r3 = 3;	 Catch:{ all -> 0x00b2 }
        r6 = 0;	 Catch:{ all -> 0x00b2 }
        if (r0 < r3) goto L_0x0022;	 Catch:{ all -> 0x00b2 }
        r7 = r18.readInt();	 Catch:{ all -> 0x00b2 }
        r8 = r18.readUnsignedInt();	 Catch:{ all -> 0x00b2 }
        r10 = r18.readUnsignedShort();	 Catch:{ all -> 0x00b2 }
        goto L_0x002c;	 Catch:{ all -> 0x00b2 }
        r7 = r18.readUnsignedInt24();	 Catch:{ all -> 0x00b2 }
        r8 = r18.readUnsignedInt24();	 Catch:{ all -> 0x00b2 }
        r8 = (long) r8;
        r10 = 0;
        r11 = 0;
        if (r7 != 0) goto L_0x003a;
        r7 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1));
        if (r7 != 0) goto L_0x003a;
        if (r10 != 0) goto L_0x003a;
        r1.setPosition(r2);
        return r4;
        r7 = 4;
        if (r0 != r7) goto L_0x006b;
        if (r21 != 0) goto L_0x006b;
        r13 = 8421504; // 0x808080 float:1.180104E-38 double:4.160776E-317;
        r13 = r13 & r8;
        r15 = (r13 > r11 ? 1 : (r13 == r11 ? 0 : -1));
        if (r15 == 0) goto L_0x004b;
        r1.setPosition(r2);
        return r6;
        r11 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r13 = r8 & r11;
        r15 = 8;
        r15 = r8 >> r15;
        r15 = r15 & r11;
        r17 = 7;
        r15 = r15 << r17;
        r13 = r13 | r15;
        r15 = 16;
        r15 = r8 >> r15;
        r15 = r15 & r11;
        r17 = 14;
        r15 = r15 << r17;
        r13 = r13 | r15;
        r15 = 24;
        r8 = r8 >> r15;
        r8 = r8 & r11;
        r11 = 21;
        r8 = r8 << r11;
        r8 = r8 | r13;
        if (r0 != r7) goto L_0x007a;
        r3 = r10 & 64;
        if (r3 == 0) goto L_0x0073;
        r3 = 1;
        goto L_0x0074;
        r3 = 0;
        r7 = r10 & 1;
        if (r7 == 0) goto L_0x0089;
        r7 = 1;
        goto L_0x008a;
        if (r0 != r3) goto L_0x0088;
        r3 = r10 & 32;
        if (r3 == 0) goto L_0x0082;
        r3 = 1;
        goto L_0x0083;
        r3 = 0;
        r7 = r10 & 128;
        if (r7 == 0) goto L_0x0089;
        goto L_0x0078;
        r3 = 0;
        r7 = 0;
        if (r3 == 0) goto L_0x008d;
        goto L_0x008e;
        r4 = 0;
        if (r7 == 0) goto L_0x0092;
        r4 = r4 + 4;
        r3 = (long) r4;
        r7 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1));
        if (r7 >= 0) goto L_0x009b;
        r1.setPosition(r2);
        return r6;
        r3 = r18.bytesLeft();	 Catch:{ all -> 0x00b2 }
        r3 = (long) r3;
        r7 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));
        if (r7 >= 0) goto L_0x00a8;
        r1.setPosition(r2);
        return r6;
        r3 = (int) r8;
        r1.skipBytes(r3);	 Catch:{ all -> 0x00b2 }
        goto L_0x0008;
        r1.setPosition(r2);
        return r4;
        r0 = move-exception;
        r1.setPosition(r2);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.metadata.id3.Id3Decoder.validateFrames(com.google.android.exoplayer2.util.ParsableByteArray, int, int, boolean):boolean");
    }

    public Id3Decoder() {
        this(null);
    }

    public Id3Decoder(FramePredicate framePredicate) {
        this.framePredicate = framePredicate;
    }

    public Metadata decode(MetadataInputBuffer metadataInputBuffer) {
        ByteBuffer byteBuffer = metadataInputBuffer.data;
        return decode(byteBuffer.array(), byteBuffer.limit());
    }

    public Metadata decode(byte[] bArr, int i) {
        List arrayList = new ArrayList();
        ParsableByteArray parsableByteArray = new ParsableByteArray(bArr, i);
        Id3Header decodeHeader = decodeHeader(parsableByteArray);
        if (decodeHeader == null) {
            return null;
        }
        int position = parsableByteArray.getPosition();
        int i2 = decodeHeader.majorVersion == 2 ? 6 : 10;
        int access$100 = decodeHeader.framesSize;
        if (decodeHeader.isUnsynchronized) {
            access$100 = removeUnsynchronization(parsableByteArray, decodeHeader.framesSize);
        }
        parsableByteArray.setLimit(position + access$100);
        boolean z = false;
        if (!validateFrames(parsableByteArray, decodeHeader.majorVersion, i2, false)) {
            if (decodeHeader.majorVersion == 4 && validateFrames(parsableByteArray, 4, i2, true)) {
                z = true;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to validate ID3 tag with majorVersion=");
                stringBuilder.append(decodeHeader.majorVersion);
                Log.m18w("Id3Decoder", stringBuilder.toString());
                return null;
            }
        }
        while (parsableByteArray.bytesLeft() >= i2) {
            Id3Frame decodeFrame = decodeFrame(decodeHeader.majorVersion, parsableByteArray, z, i2, this.framePredicate);
            if (decodeFrame != null) {
                arrayList.add(decodeFrame);
            }
        }
        return new Metadata(arrayList);
    }

    private static Id3Header decodeHeader(ParsableByteArray parsableByteArray) {
        String str = "Id3Decoder";
        if (parsableByteArray.bytesLeft() < 10) {
            Log.m18w(str, "Data too short to be an ID3 tag");
            return null;
        }
        int readUnsignedInt24 = parsableByteArray.readUnsignedInt24();
        StringBuilder stringBuilder;
        if (readUnsignedInt24 != ID3_TAG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected first three bytes of ID3 tag header: ");
            stringBuilder.append(readUnsignedInt24);
            Log.m18w(str, stringBuilder.toString());
            return null;
        }
        readUnsignedInt24 = parsableByteArray.readUnsignedByte();
        boolean z = true;
        parsableByteArray.skipBytes(1);
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int readSynchSafeInt = parsableByteArray.readSynchSafeInt();
        int readInt;
        if (readUnsignedInt24 == 2) {
            if (((readUnsignedByte & 64) != 0 ? 1 : null) != null) {
                Log.m18w(str, "Skipped ID3 tag with majorVersion=2 and undefined compression scheme");
                return null;
            }
        } else if (readUnsignedInt24 == 3) {
            if (((readUnsignedByte & 64) != 0 ? 1 : null) != null) {
                readInt = parsableByteArray.readInt();
                parsableByteArray.skipBytes(readInt);
                readSynchSafeInt -= readInt + 4;
            }
        } else if (readUnsignedInt24 == 4) {
            if (((readUnsignedByte & 64) != 0 ? 1 : null) != null) {
                readInt = parsableByteArray.readSynchSafeInt();
                parsableByteArray.skipBytes(readInt - 4);
                readSynchSafeInt -= readInt;
            }
            if (((readUnsignedByte & 16) != 0 ? 1 : null) != null) {
                readSynchSafeInt -= 10;
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Skipped ID3 tag with unsupported majorVersion=");
            stringBuilder.append(readUnsignedInt24);
            Log.m18w(str, stringBuilder.toString());
            return null;
        }
        if (readUnsignedInt24 >= 4 || (readUnsignedByte & 128) == 0) {
            z = false;
        }
        return new Id3Header(readUnsignedInt24, z, readSynchSafeInt);
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:164:0x021f */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:131:0x0193, code skipped:
            if (r13 == 67) goto L_0x0195;
     */
    /* JADX WARNING: Missing block: B:165:?, code skipped:
            com.google.android.exoplayer2.util.Log.m18w(r12, "Unsupported character encoding");
     */
    /* JADX WARNING: Missing block: B:166:0x0224, code skipped:
            r7.setPosition(r14);
     */
    /* JADX WARNING: Missing block: B:167:0x0227, code skipped:
            return null;
     */
    /* JADX WARNING: Missing block: B:168:0x0228, code skipped:
            r7.setPosition(r14);
     */
    private static com.google.android.exoplayer2.metadata.id3.Id3Frame decodeFrame(int r19, com.google.android.exoplayer2.util.ParsableByteArray r20, boolean r21, int r22, com.google.android.exoplayer2.metadata.id3.Id3Decoder.FramePredicate r23) {
        /*
        r0 = r19;
        r7 = r20;
        r8 = r20.readUnsignedByte();
        r9 = r20.readUnsignedByte();
        r10 = r20.readUnsignedByte();
        r11 = 3;
        if (r0 < r11) goto L_0x0019;
    L_0x0013:
        r1 = r20.readUnsignedByte();
        r13 = r1;
        goto L_0x001a;
    L_0x0019:
        r13 = 0;
    L_0x001a:
        r14 = 4;
        if (r0 != r14) goto L_0x003c;
    L_0x001d:
        r1 = r20.readUnsignedIntToInt();
        if (r21 != 0) goto L_0x003a;
    L_0x0023:
        r2 = r1 & 255;
        r3 = r1 >> 8;
        r3 = r3 & 255;
        r3 = r3 << 7;
        r2 = r2 | r3;
        r3 = r1 >> 16;
        r3 = r3 & 255;
        r3 = r3 << 14;
        r2 = r2 | r3;
        r1 = r1 >> 24;
        r1 = r1 & 255;
        r1 = r1 << 21;
        r1 = r1 | r2;
    L_0x003a:
        r15 = r1;
        goto L_0x0048;
    L_0x003c:
        if (r0 != r11) goto L_0x0043;
    L_0x003e:
        r1 = r20.readUnsignedIntToInt();
        goto L_0x003a;
    L_0x0043:
        r1 = r20.readUnsignedInt24();
        goto L_0x003a;
    L_0x0048:
        if (r0 < r11) goto L_0x0050;
    L_0x004a:
        r1 = r20.readUnsignedShort();
        r6 = r1;
        goto L_0x0051;
    L_0x0050:
        r6 = 0;
    L_0x0051:
        r16 = 0;
        if (r8 != 0) goto L_0x0067;
    L_0x0055:
        if (r9 != 0) goto L_0x0067;
    L_0x0057:
        if (r10 != 0) goto L_0x0067;
    L_0x0059:
        if (r13 != 0) goto L_0x0067;
    L_0x005b:
        if (r15 != 0) goto L_0x0067;
    L_0x005d:
        if (r6 != 0) goto L_0x0067;
    L_0x005f:
        r0 = r20.limit();
        r7.setPosition(r0);
        return r16;
    L_0x0067:
        r1 = r20.getPosition();
        r5 = r1 + r15;
        r1 = r20.limit();
        r4 = "Id3Decoder";
        if (r5 <= r1) goto L_0x0082;
    L_0x0075:
        r0 = "Frame size exceeds remaining tag data";
        com.google.android.exoplayer2.util.Log.m18w(r4, r0);
        r0 = r20.limit();
        r7.setPosition(r0);
        return r16;
    L_0x0082:
        if (r23 == 0) goto L_0x009a;
    L_0x0084:
        r1 = r23;
        r2 = r19;
        r3 = r8;
        r12 = r4;
        r4 = r9;
        r14 = r5;
        r5 = r10;
        r18 = r6;
        r6 = r13;
        r1 = r1.evaluate(r2, r3, r4, r5, r6);
        if (r1 != 0) goto L_0x009e;
    L_0x0096:
        r7.setPosition(r14);
        return r16;
    L_0x009a:
        r12 = r4;
        r14 = r5;
        r18 = r6;
    L_0x009e:
        r1 = 1;
        if (r0 != r11) goto L_0x00bc;
    L_0x00a1:
        r2 = r18;
        r3 = r2 & 128;
        if (r3 == 0) goto L_0x00a9;
    L_0x00a7:
        r3 = 1;
        goto L_0x00aa;
    L_0x00a9:
        r3 = 0;
    L_0x00aa:
        r4 = r2 & 64;
        if (r4 == 0) goto L_0x00b0;
    L_0x00ae:
        r4 = 1;
        goto L_0x00b1;
    L_0x00b0:
        r4 = 0;
    L_0x00b1:
        r2 = r2 & 32;
        if (r2 == 0) goto L_0x00b7;
    L_0x00b5:
        r2 = 1;
        goto L_0x00b8;
    L_0x00b7:
        r2 = 0;
    L_0x00b8:
        r17 = r3;
        r6 = 0;
        goto L_0x00f2;
    L_0x00bc:
        r2 = r18;
        r3 = 4;
        if (r0 != r3) goto L_0x00ec;
    L_0x00c1:
        r3 = r2 & 64;
        if (r3 == 0) goto L_0x00c7;
    L_0x00c5:
        r3 = 1;
        goto L_0x00c8;
    L_0x00c7:
        r3 = 0;
    L_0x00c8:
        r4 = r2 & 8;
        if (r4 == 0) goto L_0x00ce;
    L_0x00cc:
        r4 = 1;
        goto L_0x00cf;
    L_0x00ce:
        r4 = 0;
    L_0x00cf:
        r5 = r2 & 4;
        if (r5 == 0) goto L_0x00d5;
    L_0x00d3:
        r5 = 1;
        goto L_0x00d6;
    L_0x00d5:
        r5 = 0;
    L_0x00d6:
        r6 = r2 & 2;
        if (r6 == 0) goto L_0x00dc;
    L_0x00da:
        r6 = 1;
        goto L_0x00dd;
    L_0x00dc:
        r6 = 0;
    L_0x00dd:
        r2 = r2 & r1;
        if (r2 == 0) goto L_0x00e3;
    L_0x00e0:
        r17 = 1;
        goto L_0x00e5;
    L_0x00e3:
        r17 = 0;
    L_0x00e5:
        r2 = r3;
        r3 = r17;
        r17 = r4;
        r4 = r5;
        goto L_0x00f2;
    L_0x00ec:
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r6 = 0;
        r17 = 0;
    L_0x00f2:
        if (r17 != 0) goto L_0x022c;
    L_0x00f4:
        if (r4 == 0) goto L_0x00f8;
    L_0x00f6:
        goto L_0x022c;
    L_0x00f8:
        if (r2 == 0) goto L_0x00ff;
    L_0x00fa:
        r15 = r15 + -1;
        r7.skipBytes(r1);
    L_0x00ff:
        if (r3 == 0) goto L_0x0107;
    L_0x0101:
        r15 = r15 + -4;
        r1 = 4;
        r7.skipBytes(r1);
    L_0x0107:
        if (r6 == 0) goto L_0x010e;
    L_0x0109:
        r1 = removeUnsynchronization(r7, r15);
        r15 = r1;
    L_0x010e:
        r1 = 84;
        r2 = 2;
        r3 = 88;
        if (r8 != r1) goto L_0x0123;
    L_0x0115:
        if (r9 != r3) goto L_0x0123;
    L_0x0117:
        if (r10 != r3) goto L_0x0123;
    L_0x0119:
        if (r0 == r2) goto L_0x011d;
    L_0x011b:
        if (r13 != r3) goto L_0x0123;
    L_0x011d:
        r1 = decodeTxxxFrame(r7, r15);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x0123:
        if (r8 != r1) goto L_0x0132;
    L_0x0125:
        r1 = getFrameId(r0, r8, r9, r10, r13);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r1 = decodeTextInformationFrame(r7, r15, r1);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x012f:
        r0 = move-exception;
        goto L_0x0228;
    L_0x0132:
        r4 = 87;
        if (r8 != r4) goto L_0x0144;
    L_0x0136:
        if (r9 != r3) goto L_0x0144;
    L_0x0138:
        if (r10 != r3) goto L_0x0144;
    L_0x013a:
        if (r0 == r2) goto L_0x013e;
    L_0x013c:
        if (r13 != r3) goto L_0x0144;
    L_0x013e:
        r1 = decodeWxxxFrame(r7, r15);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x0144:
        r3 = 87;
        if (r8 != r3) goto L_0x0152;
    L_0x0148:
        r1 = getFrameId(r0, r8, r9, r10, r13);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r1 = decodeUrlLinkFrame(r7, r15, r1);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x0152:
        r3 = 73;
        r4 = 80;
        if (r8 != r4) goto L_0x0168;
    L_0x0158:
        r5 = 82;
        if (r9 != r5) goto L_0x0168;
    L_0x015c:
        if (r10 != r3) goto L_0x0168;
    L_0x015e:
        r5 = 86;
        if (r13 != r5) goto L_0x0168;
    L_0x0162:
        r1 = decodePrivFrame(r7, r15);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x0168:
        r5 = 71;
        r6 = 79;
        if (r8 != r5) goto L_0x0180;
    L_0x016e:
        r5 = 69;
        if (r9 != r5) goto L_0x0180;
    L_0x0172:
        if (r10 != r6) goto L_0x0180;
    L_0x0174:
        r5 = 66;
        if (r13 == r5) goto L_0x017a;
    L_0x0178:
        if (r0 != r2) goto L_0x0180;
    L_0x017a:
        r1 = decodeGeobFrame(r7, r15);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x0180:
        r5 = 67;
        if (r0 != r2) goto L_0x018b;
    L_0x0184:
        if (r8 != r4) goto L_0x019b;
    L_0x0186:
        if (r9 != r3) goto L_0x019b;
    L_0x0188:
        if (r10 != r5) goto L_0x019b;
    L_0x018a:
        goto L_0x0195;
    L_0x018b:
        r11 = 65;
        if (r8 != r11) goto L_0x019b;
    L_0x018f:
        if (r9 != r4) goto L_0x019b;
    L_0x0191:
        if (r10 != r3) goto L_0x019b;
    L_0x0193:
        if (r13 != r5) goto L_0x019b;
    L_0x0195:
        r1 = decodeApicFrame(r7, r15, r0);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x019b:
        r3 = 77;
        if (r8 != r5) goto L_0x01ac;
    L_0x019f:
        if (r9 != r6) goto L_0x01ac;
    L_0x01a1:
        if (r10 != r3) goto L_0x01ac;
    L_0x01a3:
        if (r13 == r3) goto L_0x01a7;
    L_0x01a5:
        if (r0 != r2) goto L_0x01ac;
    L_0x01a7:
        r1 = decodeCommentFrame(r7, r15);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x01ac:
        if (r8 != r5) goto L_0x01c8;
    L_0x01ae:
        r2 = 72;
        if (r9 != r2) goto L_0x01c8;
    L_0x01b2:
        r2 = 65;
        if (r10 != r2) goto L_0x01c8;
    L_0x01b6:
        if (r13 != r4) goto L_0x01c8;
    L_0x01b8:
        r1 = r20;
        r2 = r15;
        r3 = r19;
        r4 = r21;
        r5 = r22;
        r6 = r23;
        r1 = decodeChapterFrame(r1, r2, r3, r4, r5, r6);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x01c8:
        if (r8 != r5) goto L_0x01e0;
    L_0x01ca:
        if (r9 != r1) goto L_0x01e0;
    L_0x01cc:
        if (r10 != r6) goto L_0x01e0;
    L_0x01ce:
        if (r13 != r5) goto L_0x01e0;
    L_0x01d0:
        r1 = r20;
        r2 = r15;
        r3 = r19;
        r4 = r21;
        r5 = r22;
        r6 = r23;
        r1 = decodeChapterTOCFrame(r1, r2, r3, r4, r5, r6);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x01e0:
        if (r8 != r3) goto L_0x01f1;
    L_0x01e2:
        r2 = 76;
        if (r9 != r2) goto L_0x01f1;
    L_0x01e6:
        r2 = 76;
        if (r10 != r2) goto L_0x01f1;
    L_0x01ea:
        if (r13 != r1) goto L_0x01f1;
    L_0x01ec:
        r1 = decodeMlltFrame(r7, r15);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        goto L_0x01f9;
    L_0x01f1:
        r1 = getFrameId(r0, r8, r9, r10, r13);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r1 = decodeBinaryFrame(r7, r15, r1);	 Catch:{ UnsupportedEncodingException -> 0x021f }
    L_0x01f9:
        if (r1 != 0) goto L_0x021b;
    L_0x01fb:
        r2 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r2.<init>();	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r3 = "Failed to decode frame: id=";
        r2.append(r3);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r0 = getFrameId(r0, r8, r9, r10, r13);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r2.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r0 = ", frameSize=";
        r2.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r2.append(r15);	 Catch:{ UnsupportedEncodingException -> 0x021f }
        r0 = r2.toString();	 Catch:{ UnsupportedEncodingException -> 0x021f }
        com.google.android.exoplayer2.util.Log.m18w(r12, r0);	 Catch:{ UnsupportedEncodingException -> 0x021f }
    L_0x021b:
        r7.setPosition(r14);
        return r1;
    L_0x021f:
        r0 = "Unsupported character encoding";
        com.google.android.exoplayer2.util.Log.m18w(r12, r0);	 Catch:{ all -> 0x012f }
        r7.setPosition(r14);
        return r16;
    L_0x0228:
        r7.setPosition(r14);
        throw r0;
    L_0x022c:
        r0 = "Skipping unsupported compressed or encrypted frame";
        com.google.android.exoplayer2.util.Log.m18w(r12, r0);
        r7.setPosition(r14);
        return r16;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.metadata.id3.Id3Decoder.decodeFrame(int, com.google.android.exoplayer2.util.ParsableByteArray, boolean, int, com.google.android.exoplayer2.metadata.id3.Id3Decoder$FramePredicate):com.google.android.exoplayer2.metadata.id3.Id3Frame");
    }

    private static TextInformationFrame decodeTxxxFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        if (i < 1) {
            return null;
        }
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        int indexOfEos = indexOfEos(bArr, 0, readUnsignedByte);
        String str = new String(bArr, 0, indexOfEos, charsetName);
        indexOfEos += delimiterLength(readUnsignedByte);
        return new TextInformationFrame("TXXX", str, decodeStringIfValid(bArr, indexOfEos, indexOfEos(bArr, indexOfEos, readUnsignedByte), charsetName));
    }

    private static TextInformationFrame decodeTextInformationFrame(ParsableByteArray parsableByteArray, int i, String str) throws UnsupportedEncodingException {
        if (i < 1) {
            return null;
        }
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        return new TextInformationFrame(str, null, new String(bArr, 0, indexOfEos(bArr, 0, readUnsignedByte), charsetName));
    }

    private static UrlLinkFrame decodeWxxxFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        if (i < 1) {
            return null;
        }
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        int indexOfEos = indexOfEos(bArr, 0, readUnsignedByte);
        String str = new String(bArr, 0, indexOfEos, charsetName);
        indexOfEos += delimiterLength(readUnsignedByte);
        return new UrlLinkFrame("WXXX", str, decodeStringIfValid(bArr, indexOfEos, indexOfZeroByte(bArr, indexOfEos), "ISO-8859-1"));
    }

    private static UrlLinkFrame decodeUrlLinkFrame(ParsableByteArray parsableByteArray, int i, String str) throws UnsupportedEncodingException {
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        return new UrlLinkFrame(str, null, new String(bArr, 0, indexOfZeroByte(bArr, 0), "ISO-8859-1"));
    }

    private static PrivFrame decodePrivFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        int indexOfZeroByte = indexOfZeroByte(bArr, 0);
        return new PrivFrame(new String(bArr, 0, indexOfZeroByte, "ISO-8859-1"), copyOfRangeIfValid(bArr, indexOfZeroByte + 1, bArr.length));
    }

    private static GeobFrame decodeGeobFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        int indexOfZeroByte = indexOfZeroByte(bArr, 0);
        String str = new String(bArr, 0, indexOfZeroByte, "ISO-8859-1");
        indexOfZeroByte++;
        int indexOfEos = indexOfEos(bArr, indexOfZeroByte, readUnsignedByte);
        String decodeStringIfValid = decodeStringIfValid(bArr, indexOfZeroByte, indexOfEos, charsetName);
        indexOfEos += delimiterLength(readUnsignedByte);
        int indexOfEos2 = indexOfEos(bArr, indexOfEos, readUnsignedByte);
        return new GeobFrame(str, decodeStringIfValid, decodeStringIfValid(bArr, indexOfEos, indexOfEos2, charsetName), copyOfRangeIfValid(bArr, indexOfEos2 + delimiterLength(readUnsignedByte), bArr.length));
    }

    private static ApicFrame decodeApicFrame(ParsableByteArray parsableByteArray, int i, int i2) throws UnsupportedEncodingException {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        String str = "image/";
        String str2 = "ISO-8859-1";
        if (i2 == 2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(Util.toLowerInvariant(new String(bArr, 0, 3, str2)));
            str = stringBuilder.toString();
            if ("image/jpg".equals(str)) {
                str = "image/jpeg";
            }
            i2 = 2;
        } else {
            i2 = indexOfZeroByte(bArr, 0);
            str2 = Util.toLowerInvariant(new String(bArr, 0, i2, str2));
            if (str2.indexOf(47) == -1) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(str2);
                str = stringBuilder2.toString();
            } else {
                str = str2;
            }
        }
        i = bArr[i2 + 1] & NalUnitUtil.EXTENDED_SAR;
        i2 += 2;
        int indexOfEos = indexOfEos(bArr, i2, readUnsignedByte);
        return new ApicFrame(str, new String(bArr, i2, indexOfEos - i2, charsetName), i, copyOfRangeIfValid(bArr, indexOfEos + delimiterLength(readUnsignedByte), bArr.length));
    }

    private static CommentFrame decodeCommentFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        if (i < 4) {
            return null;
        }
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        byte[] bArr = new byte[3];
        parsableByteArray.readBytes(bArr, 0, 3);
        String str = new String(bArr, 0, 3);
        i -= 4;
        byte[] bArr2 = new byte[i];
        parsableByteArray.readBytes(bArr2, 0, i);
        int indexOfEos = indexOfEos(bArr2, 0, readUnsignedByte);
        String str2 = new String(bArr2, 0, indexOfEos, charsetName);
        indexOfEos += delimiterLength(readUnsignedByte);
        return new CommentFrame(str, str2, decodeStringIfValid(bArr2, indexOfEos, indexOfEos(bArr2, indexOfEos, readUnsignedByte), charsetName));
    }

    private static ChapterFrame decodeChapterFrame(ParsableByteArray parsableByteArray, int i, int i2, boolean z, int i3, FramePredicate framePredicate) throws UnsupportedEncodingException {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int position = parsableByteArray.getPosition();
        int indexOfZeroByte = indexOfZeroByte(parsableByteArray2.data, position);
        String str = new String(parsableByteArray2.data, position, indexOfZeroByte - position, "ISO-8859-1");
        parsableByteArray.setPosition(indexOfZeroByte + 1);
        int readInt = parsableByteArray.readInt();
        int readInt2 = parsableByteArray.readInt();
        long readUnsignedInt = parsableByteArray.readUnsignedInt();
        long j = readUnsignedInt == 4294967295L ? -1 : readUnsignedInt;
        readUnsignedInt = parsableByteArray.readUnsignedInt();
        long j2 = readUnsignedInt == 4294967295L ? -1 : readUnsignedInt;
        ArrayList arrayList = new ArrayList();
        position += i;
        while (parsableByteArray.getPosition() < position) {
            Id3Frame decodeFrame = decodeFrame(i2, parsableByteArray, z, i3, framePredicate);
            if (decodeFrame != null) {
                arrayList.add(decodeFrame);
            }
        }
        Id3Frame[] id3FrameArr = new Id3Frame[arrayList.size()];
        arrayList.toArray(id3FrameArr);
        return new ChapterFrame(str, readInt, readInt2, j, j2, id3FrameArr);
    }

    private static ChapterTocFrame decodeChapterTOCFrame(ParsableByteArray parsableByteArray, int i, int i2, boolean z, int i3, FramePredicate framePredicate) throws UnsupportedEncodingException {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int position = parsableByteArray.getPosition();
        int indexOfZeroByte = indexOfZeroByte(parsableByteArray2.data, position);
        String str = "ISO-8859-1";
        String str2 = new String(parsableByteArray2.data, position, indexOfZeroByte - position, str);
        parsableByteArray.setPosition(indexOfZeroByte + 1);
        indexOfZeroByte = parsableByteArray.readUnsignedByte();
        boolean z2 = (indexOfZeroByte & 2) != 0;
        boolean z3 = (indexOfZeroByte & 1) != 0;
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String[] strArr = new String[readUnsignedByte];
        for (int i4 = 0; i4 < readUnsignedByte; i4++) {
            int position2 = parsableByteArray.getPosition();
            int indexOfZeroByte2 = indexOfZeroByte(parsableByteArray2.data, position2);
            strArr[i4] = new String(parsableByteArray2.data, position2, indexOfZeroByte2 - position2, str);
            parsableByteArray.setPosition(indexOfZeroByte2 + 1);
        }
        ArrayList arrayList = new ArrayList();
        position += i;
        while (parsableByteArray.getPosition() < position) {
            Id3Frame decodeFrame = decodeFrame(i2, parsableByteArray, z, i3, framePredicate);
            if (decodeFrame != null) {
                arrayList.add(decodeFrame);
            }
        }
        Id3Frame[] id3FrameArr = new Id3Frame[arrayList.size()];
        arrayList.toArray(id3FrameArr);
        return new ChapterTocFrame(str2, z2, z3, strArr, id3FrameArr);
    }

    private static MlltFrame decodeMlltFrame(ParsableByteArray parsableByteArray, int i) {
        int readUnsignedShort = parsableByteArray.readUnsignedShort();
        int readUnsignedInt24 = parsableByteArray.readUnsignedInt24();
        int readUnsignedInt242 = parsableByteArray.readUnsignedInt24();
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int readUnsignedByte2 = parsableByteArray.readUnsignedByte();
        ParsableBitArray parsableBitArray = new ParsableBitArray();
        parsableBitArray.reset(parsableByteArray);
        i = ((i - 10) * 8) / (readUnsignedByte + readUnsignedByte2);
        int[] iArr = new int[i];
        int[] iArr2 = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            int readBits = parsableBitArray.readBits(readUnsignedByte);
            int readBits2 = parsableBitArray.readBits(readUnsignedByte2);
            iArr[i2] = readBits;
            iArr2[i2] = readBits2;
        }
        return new MlltFrame(readUnsignedShort, readUnsignedInt24, readUnsignedInt242, iArr, iArr2);
    }

    private static BinaryFrame decodeBinaryFrame(ParsableByteArray parsableByteArray, int i, String str) {
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        return new BinaryFrame(str, bArr);
    }

    private static int removeUnsynchronization(ParsableByteArray parsableByteArray, int i) {
        byte[] bArr = parsableByteArray.data;
        int position = parsableByteArray.getPosition();
        while (true) {
            int i2 = position + 1;
            if (i2 >= i) {
                return i;
            }
            if ((bArr[position] & NalUnitUtil.EXTENDED_SAR) == NalUnitUtil.EXTENDED_SAR && bArr[i2] == (byte) 0) {
                System.arraycopy(bArr, position + 2, bArr, i2, (i - position) - 2);
                i--;
            }
            position = i2;
        }
    }

    private static String getFrameId(int i, int i2, int i3, int i4, int i5) {
        if (i == 2) {
            return String.format(Locale.US, "%c%c%c", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)});
        }
        return String.format(Locale.US, "%c%c%c%c", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5)});
    }

    private static int indexOfEos(byte[] bArr, int i, int i2) {
        i = indexOfZeroByte(bArr, i);
        if (i2 == 0 || i2 == 3) {
            return i;
        }
        while (i < bArr.length - 1) {
            if (i % 2 == 0 && bArr[i + 1] == (byte) 0) {
                return i;
            }
            i = indexOfZeroByte(bArr, i + 1);
        }
        return bArr.length;
    }

    private static int indexOfZeroByte(byte[] bArr, int i) {
        while (i < bArr.length) {
            if (bArr[i] == (byte) 0) {
                return i;
            }
            i++;
        }
        return bArr.length;
    }

    private static byte[] copyOfRangeIfValid(byte[] bArr, int i, int i2) {
        if (i2 <= i) {
            return Util.EMPTY_BYTE_ARRAY;
        }
        return Arrays.copyOfRange(bArr, i, i2);
    }

    private static String decodeStringIfValid(byte[] bArr, int i, int i2, String str) throws UnsupportedEncodingException {
        return (i2 <= i || i2 > bArr.length) ? "" : new String(bArr, i, i2 - i, str);
    }
}
