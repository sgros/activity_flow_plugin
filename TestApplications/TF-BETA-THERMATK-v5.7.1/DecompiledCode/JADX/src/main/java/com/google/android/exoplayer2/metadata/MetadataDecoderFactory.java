package com.google.android.exoplayer2.metadata;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.MimeTypes;

public interface MetadataDecoderFactory {
    public static final MetadataDecoderFactory DEFAULT = new C01721();

    /* renamed from: com.google.android.exoplayer2.metadata.MetadataDecoderFactory$1 */
    static class C01721 implements MetadataDecoderFactory {
        C01721() {
        }

        public boolean supportsFormat(Format format) {
            String str = format.sampleMimeType;
            return MimeTypes.APPLICATION_ID3.equals(str) || MimeTypes.APPLICATION_EMSG.equals(str) || MimeTypes.APPLICATION_SCTE35.equals(str) || MimeTypes.APPLICATION_ICY.equals(str);
        }

        public com.google.android.exoplayer2.metadata.MetadataDecoder createDecoder(com.google.android.exoplayer2.Format r5) {
            /*
            r4 = this;
            r5 = r5.sampleMimeType;
            r0 = r5.hashCode();
            r1 = 3;
            r2 = 2;
            r3 = 1;
            switch(r0) {
                case -1348231605: goto L_0x002b;
                case -1248341703: goto L_0x0021;
                case 1154383568: goto L_0x0017;
                case 1652648887: goto L_0x000d;
                default: goto L_0x000c;
            };
        L_0x000c:
            goto L_0x0035;
        L_0x000d:
            r0 = "application/x-scte35";
            r5 = r5.equals(r0);
            if (r5 == 0) goto L_0x0035;
        L_0x0015:
            r5 = 2;
            goto L_0x0036;
        L_0x0017:
            r0 = "application/x-emsg";
            r5 = r5.equals(r0);
            if (r5 == 0) goto L_0x0035;
        L_0x001f:
            r5 = 1;
            goto L_0x0036;
        L_0x0021:
            r0 = "application/id3";
            r5 = r5.equals(r0);
            if (r5 == 0) goto L_0x0035;
        L_0x0029:
            r5 = 0;
            goto L_0x0036;
        L_0x002b:
            r0 = "application/x-icy";
            r5 = r5.equals(r0);
            if (r5 == 0) goto L_0x0035;
        L_0x0033:
            r5 = 3;
            goto L_0x0036;
        L_0x0035:
            r5 = -1;
        L_0x0036:
            if (r5 == 0) goto L_0x0058;
        L_0x0038:
            if (r5 == r3) goto L_0x0052;
        L_0x003a:
            if (r5 == r2) goto L_0x004c;
        L_0x003c:
            if (r5 != r1) goto L_0x0044;
        L_0x003e:
            r5 = new com.google.android.exoplayer2.metadata.icy.IcyDecoder;
            r5.<init>();
            return r5;
        L_0x0044:
            r5 = new java.lang.IllegalArgumentException;
            r0 = "Attempted to create decoder for unsupported format";
            r5.<init>(r0);
            throw r5;
        L_0x004c:
            r5 = new com.google.android.exoplayer2.metadata.scte35.SpliceInfoDecoder;
            r5.<init>();
            return r5;
        L_0x0052:
            r5 = new com.google.android.exoplayer2.metadata.emsg.EventMessageDecoder;
            r5.<init>();
            return r5;
        L_0x0058:
            r5 = new com.google.android.exoplayer2.metadata.id3.Id3Decoder;
            r5.<init>();
            return r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.metadata.MetadataDecoderFactory$C01721.createDecoder(com.google.android.exoplayer2.Format):com.google.android.exoplayer2.metadata.MetadataDecoder");
        }
    }

    MetadataDecoder createDecoder(Format format);

    boolean supportsFormat(Format format);
}
