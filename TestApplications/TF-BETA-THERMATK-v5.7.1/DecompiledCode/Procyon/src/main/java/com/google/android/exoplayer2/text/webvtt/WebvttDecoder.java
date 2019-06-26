// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.webvtt;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.Subtitle;
import android.text.TextUtils;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

public final class WebvttDecoder extends SimpleSubtitleDecoder
{
    private final CssParser cssParser;
    private final WebvttCueParser cueParser;
    private final List<WebvttCssStyle> definedStyles;
    private final ParsableByteArray parsableWebvttData;
    private final WebvttCue.Builder webvttCueBuilder;
    
    public WebvttDecoder() {
        super("WebvttDecoder");
        this.cueParser = new WebvttCueParser();
        this.parsableWebvttData = new ParsableByteArray();
        this.webvttCueBuilder = new WebvttCue.Builder();
        this.cssParser = new CssParser();
        this.definedStyles = new ArrayList<WebvttCssStyle>();
    }
    
    private static int getNextEvent(final ParsableByteArray parsableByteArray) {
        int i = -1;
        int position = 0;
        while (i == -1) {
            position = parsableByteArray.getPosition();
            final String line = parsableByteArray.readLine();
            if (line == null) {
                i = 0;
            }
            else if ("STYLE".equals(line)) {
                i = 2;
            }
            else if (line.startsWith("NOTE")) {
                i = 1;
            }
            else {
                i = 3;
            }
        }
        parsableByteArray.setPosition(position);
        return i;
    }
    
    private static void skipComment(final ParsableByteArray parsableByteArray) {
        while (!TextUtils.isEmpty((CharSequence)parsableByteArray.readLine())) {}
    }
    
    @Override
    protected WebvttSubtitle decode(final byte[] array, int nextEvent, final boolean b) throws SubtitleDecoderException {
        this.parsableWebvttData.reset(array, nextEvent);
        this.webvttCueBuilder.reset();
        this.definedStyles.clear();
        try {
            WebvttParserUtil.validateWebvttHeaderLine(this.parsableWebvttData);
            while (!TextUtils.isEmpty((CharSequence)this.parsableWebvttData.readLine())) {}
            final ArrayList<WebvttCue> list = new ArrayList<WebvttCue>();
            while (true) {
                nextEvent = getNextEvent(this.parsableWebvttData);
                if (nextEvent == 0) {
                    return new WebvttSubtitle(list);
                }
                if (nextEvent == 1) {
                    skipComment(this.parsableWebvttData);
                }
                else if (nextEvent == 2) {
                    if (!list.isEmpty()) {
                        throw new SubtitleDecoderException("A style block was found after the first cue.");
                    }
                    this.parsableWebvttData.readLine();
                    final WebvttCssStyle block = this.cssParser.parseBlock(this.parsableWebvttData);
                    if (block == null) {
                        continue;
                    }
                    this.definedStyles.add(block);
                }
                else {
                    if (nextEvent != 3 || !this.cueParser.parseCue(this.parsableWebvttData, this.webvttCueBuilder, this.definedStyles)) {
                        continue;
                    }
                    list.add(this.webvttCueBuilder.build());
                    this.webvttCueBuilder.reset();
                }
            }
        }
        catch (ParserException ex) {
            throw new SubtitleDecoderException(ex);
        }
    }
}
