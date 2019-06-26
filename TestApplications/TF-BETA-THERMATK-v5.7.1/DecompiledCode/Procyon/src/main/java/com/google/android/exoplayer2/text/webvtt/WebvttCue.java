// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.webvtt;

import com.google.android.exoplayer2.util.Log;
import android.text.SpannableStringBuilder;
import android.text.Layout$Alignment;
import com.google.android.exoplayer2.text.Cue;

public final class WebvttCue extends Cue
{
    public final long endTime;
    public final long startTime;
    
    public WebvttCue(final long n, final long n2, final CharSequence charSequence) {
        this(n, n2, charSequence, null, Float.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
    }
    
    public WebvttCue(final long startTime, final long endTime, final CharSequence charSequence, final Layout$Alignment layout$Alignment, final float n, final int n2, final int n3, final float n4, final int n5, final float n6) {
        super(charSequence, layout$Alignment, n, n2, n3, n4, n5, n6);
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public WebvttCue(final CharSequence charSequence) {
        this(0L, 0L, charSequence);
    }
    
    public boolean isNormalCue() {
        return super.line == Float.MIN_VALUE && super.position == Float.MIN_VALUE;
    }
    
    public static class Builder
    {
        private long endTime;
        private float line;
        private int lineAnchor;
        private int lineType;
        private float position;
        private int positionAnchor;
        private long startTime;
        private SpannableStringBuilder text;
        private Layout$Alignment textAlignment;
        private float width;
        
        public Builder() {
            this.reset();
        }
        
        private Builder derivePositionAnchorFromAlignment() {
            final Layout$Alignment textAlignment = this.textAlignment;
            if (textAlignment == null) {
                this.positionAnchor = Integer.MIN_VALUE;
            }
            else {
                final int n = WebvttCue$1.$SwitchMap$android$text$Layout$Alignment[textAlignment.ordinal()];
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unrecognized alignment: ");
                            sb.append(this.textAlignment);
                            Log.w("WebvttCueBuilder", sb.toString());
                            this.positionAnchor = 0;
                        }
                        else {
                            this.positionAnchor = 2;
                        }
                    }
                    else {
                        this.positionAnchor = 1;
                    }
                }
                else {
                    this.positionAnchor = 0;
                }
            }
            return this;
        }
        
        public WebvttCue build() {
            if (this.position != Float.MIN_VALUE && this.positionAnchor == Integer.MIN_VALUE) {
                this.derivePositionAnchorFromAlignment();
            }
            return new WebvttCue(this.startTime, this.endTime, (CharSequence)this.text, this.textAlignment, this.line, this.lineType, this.lineAnchor, this.position, this.positionAnchor, this.width);
        }
        
        public void reset() {
            this.startTime = 0L;
            this.endTime = 0L;
            this.text = null;
            this.textAlignment = null;
            this.line = Float.MIN_VALUE;
            this.lineType = Integer.MIN_VALUE;
            this.lineAnchor = Integer.MIN_VALUE;
            this.position = Float.MIN_VALUE;
            this.positionAnchor = Integer.MIN_VALUE;
            this.width = Float.MIN_VALUE;
        }
        
        public Builder setEndTime(final long endTime) {
            this.endTime = endTime;
            return this;
        }
        
        public Builder setLine(final float line) {
            this.line = line;
            return this;
        }
        
        public Builder setLineAnchor(final int lineAnchor) {
            this.lineAnchor = lineAnchor;
            return this;
        }
        
        public Builder setLineType(final int lineType) {
            this.lineType = lineType;
            return this;
        }
        
        public Builder setPosition(final float position) {
            this.position = position;
            return this;
        }
        
        public Builder setPositionAnchor(final int positionAnchor) {
            this.positionAnchor = positionAnchor;
            return this;
        }
        
        public Builder setStartTime(final long startTime) {
            this.startTime = startTime;
            return this;
        }
        
        public Builder setText(final SpannableStringBuilder text) {
            this.text = text;
            return this;
        }
        
        public Builder setTextAlignment(final Layout$Alignment textAlignment) {
            this.textAlignment = textAlignment;
            return this;
        }
        
        public Builder setWidth(final float width) {
            this.width = width;
            return this;
        }
    }
}
