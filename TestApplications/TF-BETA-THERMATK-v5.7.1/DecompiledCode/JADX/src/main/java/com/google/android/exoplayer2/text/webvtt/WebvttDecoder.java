package com.google.android.exoplayer2.text.webvtt;

import android.text.TextUtils;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.webvtt.WebvttCue.Builder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.List;

public final class WebvttDecoder extends SimpleSubtitleDecoder {
    private final CssParser cssParser = new CssParser();
    private final WebvttCueParser cueParser = new WebvttCueParser();
    private final List<WebvttCssStyle> definedStyles = new ArrayList();
    private final ParsableByteArray parsableWebvttData = new ParsableByteArray();
    private final Builder webvttCueBuilder = new Builder();

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:31:0x008a in {5, 11, 18, 20, 25, 27, 30} preds:[]
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
    protected com.google.android.exoplayer2.text.webvtt.WebvttSubtitle decode(byte[] r3, int r4, boolean r5) throws com.google.android.exoplayer2.text.SubtitleDecoderException {
        /*
        r2 = this;
        r5 = r2.parsableWebvttData;
        r5.reset(r3, r4);
        r3 = r2.webvttCueBuilder;
        r3.reset();
        r3 = r2.definedStyles;
        r3.clear();
        r3 = r2.parsableWebvttData;	 Catch:{ ParserException -> 0x0083 }
        com.google.android.exoplayer2.text.webvtt.WebvttParserUtil.validateWebvttHeaderLine(r3);	 Catch:{ ParserException -> 0x0083 }
        r3 = r2.parsableWebvttData;
        r3 = r3.readLine();
        r3 = android.text.TextUtils.isEmpty(r3);
        if (r3 != 0) goto L_0x0021;
        goto L_0x0014;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r4 = r2.parsableWebvttData;
        r4 = getNextEvent(r4);
        if (r4 == 0) goto L_0x007d;
        r5 = 1;
        if (r4 != r5) goto L_0x0037;
        r4 = r2.parsableWebvttData;
        skipComment(r4);
        goto L_0x0026;
        r5 = 2;
        if (r4 != r5) goto L_0x005d;
        r4 = r3.isEmpty();
        if (r4 == 0) goto L_0x0055;
        r4 = r2.parsableWebvttData;
        r4.readLine();
        r4 = r2.cssParser;
        r5 = r2.parsableWebvttData;
        r4 = r4.parseBlock(r5);
        if (r4 == 0) goto L_0x0026;
        r5 = r2.definedStyles;
        r5.add(r4);
        goto L_0x0026;
        r3 = new com.google.android.exoplayer2.text.SubtitleDecoderException;
        r4 = "A style block was found after the first cue.";
        r3.<init>(r4);
        throw r3;
        r5 = 3;
        if (r4 != r5) goto L_0x0026;
        r4 = r2.cueParser;
        r5 = r2.parsableWebvttData;
        r0 = r2.webvttCueBuilder;
        r1 = r2.definedStyles;
        r4 = r4.parseCue(r5, r0, r1);
        if (r4 == 0) goto L_0x0026;
        r4 = r2.webvttCueBuilder;
        r4 = r4.build();
        r3.add(r4);
        r4 = r2.webvttCueBuilder;
        r4.reset();
        goto L_0x0026;
        r4 = new com.google.android.exoplayer2.text.webvtt.WebvttSubtitle;
        r4.<init>(r3);
        return r4;
        r3 = move-exception;
        r4 = new com.google.android.exoplayer2.text.SubtitleDecoderException;
        r4.<init>(r3);
        throw r4;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttDecoder.decode(byte[], int, boolean):com.google.android.exoplayer2.text.webvtt.WebvttSubtitle");
    }

    public WebvttDecoder() {
        super("WebvttDecoder");
    }

    private static int getNextEvent(ParsableByteArray parsableByteArray) {
        int i = -1;
        int i2 = 0;
        while (i == -1) {
            i2 = parsableByteArray.getPosition();
            String readLine = parsableByteArray.readLine();
            i = readLine == null ? 0 : "STYLE".equals(readLine) ? 2 : readLine.startsWith("NOTE") ? 1 : 3;
        }
        parsableByteArray.setPosition(i2);
        return i;
    }

    private static void skipComment(ParsableByteArray parsableByteArray) {
        while (!TextUtils.isEmpty(parsableByteArray.readLine())) {
        }
    }
}
