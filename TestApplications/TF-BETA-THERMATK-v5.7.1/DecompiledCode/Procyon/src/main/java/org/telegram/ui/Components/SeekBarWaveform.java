// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.content.Context;
import android.view.View;
import org.telegram.messenger.MessageObject;
import android.graphics.Paint;

public class SeekBarWaveform
{
    private static Paint paintInner;
    private static Paint paintOuter;
    private SeekBar.SeekBarDelegate delegate;
    private int height;
    private int innerColor;
    private MessageObject messageObject;
    private int outerColor;
    private View parentView;
    private boolean pressed;
    private boolean selected;
    private int selectedColor;
    private boolean startDraging;
    private float startX;
    private int thumbDX;
    private int thumbX;
    private byte[] waveformBytes;
    private int width;
    
    public SeekBarWaveform(final Context context) {
        this.thumbX = 0;
        this.thumbDX = 0;
        this.startDraging = false;
        this.pressed = false;
        if (SeekBarWaveform.paintInner == null) {
            SeekBarWaveform.paintInner = new Paint();
            SeekBarWaveform.paintOuter = new Paint();
        }
    }
    
    public void draw(final Canvas canvas) {
        if (this.waveformBytes != null) {
            final int width = this.width;
            if (width != 0) {
                final float n = (float)(width / AndroidUtilities.dp(3.0f));
                if (n <= 0.1f) {
                    return;
                }
                final int n2 = this.waveformBytes.length * 8 / 5;
                final float n3 = n2 / n;
                final Paint paintInner = SeekBarWaveform.paintInner;
                final MessageObject messageObject = this.messageObject;
                int color;
                if (messageObject != null && !messageObject.isOutOwner() && this.messageObject.isContentUnread()) {
                    color = this.outerColor;
                }
                else if (this.selected) {
                    color = this.selectedColor;
                }
                else {
                    color = this.innerColor;
                }
                paintInner.setColor(color);
                SeekBarWaveform.paintOuter.setColor(this.outerColor);
                final int n4 = (this.height - AndroidUtilities.dp(14.0f)) / 2;
                int i = 0;
                int j = 0;
                float n5 = 0.0f;
                int n6 = 0;
                while (i < n2) {
                    if (i == j) {
                        int n7;
                        int n8;
                        for (n7 = 0, n8 = j; j == n8; n8 = (int)n5, ++n7) {
                            n5 += n3;
                        }
                        final int n9 = i * 5;
                        int n10 = n9 / 8;
                        final int n11 = n9 - n10 * 8;
                        final int b = 8 - n11;
                        final int n12 = 5 - b;
                        byte b3;
                        final byte b2 = b3 = (byte)(this.waveformBytes[n10] >> n11 & (2 << Math.min(5, b) - 1) - 1);
                        if (n12 > 0) {
                            ++n10;
                            final byte[] waveformBytes = this.waveformBytes;
                            b3 = b2;
                            if (n10 < waveformBytes.length) {
                                b3 = (byte)((byte)(b2 << n12) | (waveformBytes[n10] & (2 << n12 - 1) - 1));
                            }
                        }
                        for (int k = 0; k < n7; ++k) {
                            final int n13 = AndroidUtilities.dp(3.0f) * n6;
                            if (n13 < this.thumbX && AndroidUtilities.dp(2.0f) + n13 < this.thumbX) {
                                canvas.drawRect((float)n13, (float)(AndroidUtilities.dp(14.0f - Math.max(1.0f, b3 * 14.0f / 31.0f)) + n4), (float)(n13 + AndroidUtilities.dp(2.0f)), (float)(AndroidUtilities.dp(14.0f) + n4), SeekBarWaveform.paintOuter);
                            }
                            else {
                                final float n14 = (float)n13;
                                final float n15 = b3 * 14.0f / 31.0f;
                                canvas.drawRect(n14, (float)(n4 + AndroidUtilities.dp(14.0f - Math.max(1.0f, n15))), (float)(n13 + AndroidUtilities.dp(2.0f)), (float)(n4 + AndroidUtilities.dp(14.0f)), SeekBarWaveform.paintInner);
                                if (n13 < this.thumbX) {
                                    canvas.drawRect(n14, (float)(AndroidUtilities.dp(14.0f - Math.max(1.0f, n15)) + n4), (float)this.thumbX, (float)(AndroidUtilities.dp(14.0f) + n4), SeekBarWaveform.paintOuter);
                                }
                            }
                            ++n6;
                        }
                        j = n8;
                    }
                    ++i;
                }
            }
        }
    }
    
    public boolean isDragging() {
        return this.pressed;
    }
    
    public boolean isStartDraging() {
        return this.startDraging;
    }
    
    public boolean onTouch(int thumbX, final float startX, float startX2) {
        if (thumbX == 0) {
            if (0.0f <= startX && startX <= this.width && startX2 >= 0.0f && startX2 <= this.height) {
                this.startX = startX;
                this.pressed = true;
                this.thumbDX = (int)(startX - this.thumbX);
                this.startDraging = false;
                return true;
            }
        }
        else if (thumbX != 1 && thumbX != 3) {
            if (thumbX == 2 && this.pressed) {
                if (this.startDraging) {
                    this.thumbX = (int)(startX - this.thumbDX);
                    thumbX = this.thumbX;
                    if (thumbX < 0) {
                        this.thumbX = 0;
                    }
                    else {
                        final int width = this.width;
                        if (thumbX > width) {
                            this.thumbX = width;
                        }
                    }
                }
                startX2 = this.startX;
                if (startX2 != -1.0f && Math.abs(startX - startX2) > AndroidUtilities.getPixelsInCM(0.2f, true)) {
                    final View parentView = this.parentView;
                    if (parentView != null && parentView.getParent() != null) {
                        this.parentView.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    this.startDraging = true;
                    this.startX = -1.0f;
                }
                return true;
            }
        }
        else if (this.pressed) {
            if (thumbX == 1) {
                final SeekBar.SeekBarDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.onSeekBarDrag(this.thumbX / (float)this.width);
                }
            }
            this.pressed = false;
            return true;
        }
        return false;
    }
    
    public void setColors(final int innerColor, final int outerColor, final int selectedColor) {
        this.innerColor = innerColor;
        this.outerColor = outerColor;
        this.selectedColor = selectedColor;
    }
    
    public void setDelegate(final SeekBar.SeekBarDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setMessageObject(final MessageObject messageObject) {
        this.messageObject = messageObject;
    }
    
    public void setParentView(final View parentView) {
        this.parentView = parentView;
    }
    
    public void setProgress(final float n) {
        this.thumbX = (int)Math.ceil(this.width * n);
        final int thumbX = this.thumbX;
        if (thumbX < 0) {
            this.thumbX = 0;
        }
        else {
            final int width = this.width;
            if (thumbX > width) {
                this.thumbX = width;
            }
        }
    }
    
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
    
    public void setSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    public void setWaveform(final byte[] waveformBytes) {
        this.waveformBytes = waveformBytes;
    }
}
