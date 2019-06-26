// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.metadata.id3.InternalFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.Metadata;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GaplessInfoHolder
{
    private static final Pattern GAPLESS_COMMENT_PATTERN;
    public int encoderDelay;
    public int encoderPadding;
    
    static {
        GAPLESS_COMMENT_PATTERN = Pattern.compile("^ [0-9a-fA-F]{8} ([0-9a-fA-F]{8}) ([0-9a-fA-F]{8})");
    }
    
    public GaplessInfoHolder() {
        this.encoderDelay = -1;
        this.encoderPadding = -1;
    }
    
    private boolean setFromComment(final String input) {
        final Matcher matcher = GaplessInfoHolder.GAPLESS_COMMENT_PATTERN.matcher(input);
        if (!matcher.find()) {
            return false;
        }
        try {
            final int int1 = Integer.parseInt(matcher.group(1), 16);
            final int int2 = Integer.parseInt(matcher.group(2), 16);
            if (int1 > 0 || int2 > 0) {
                this.encoderDelay = int1;
                this.encoderPadding = int2;
                return true;
            }
            return false;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
    
    public boolean hasGaplessInfo() {
        return this.encoderDelay != -1 && this.encoderPadding != -1;
    }
    
    public boolean setFromMetadata(final Metadata metadata) {
        for (int i = 0; i < metadata.length(); ++i) {
            final Metadata.Entry value = metadata.get(i);
            if (value instanceof CommentFrame) {
                final CommentFrame commentFrame = (CommentFrame)value;
                if ("iTunSMPB".equals(commentFrame.description) && this.setFromComment(commentFrame.text)) {
                    return true;
                }
            }
            else if (value instanceof InternalFrame) {
                final InternalFrame internalFrame = (InternalFrame)value;
                if ("com.apple.iTunes".equals(internalFrame.domain) && "iTunSMPB".equals(internalFrame.description) && this.setFromComment(internalFrame.text)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean setFromXingHeaderValue(int encoderPadding) {
        final int encoderDelay = encoderPadding >> 12;
        encoderPadding &= 0xFFF;
        if (encoderDelay <= 0 && encoderPadding <= 0) {
            return false;
        }
        this.encoderDelay = encoderDelay;
        this.encoderPadding = encoderPadding;
        return true;
    }
}
