// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Paint$FontMetricsInt;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import java.util.Locale;
import org.telegram.tgnet.TLRPC;
import android.view.View;
import org.telegram.messenger.ImageReceiver;
import android.text.style.ReplacementSpan;

public class TextPaintImageReceiverSpan extends ReplacementSpan
{
    private boolean alignTop;
    private int height;
    private ImageReceiver imageReceiver;
    private int width;
    
    public TextPaintImageReceiverSpan(final View view, final TLRPC.Document document, final Object o, final int n, final int n2, final boolean alignTop, final boolean b) {
        final String format = String.format(Locale.US, "%d_%d_i", n, n2);
        this.width = n;
        this.height = n2;
        (this.imageReceiver = new ImageReceiver(view)).setInvalidateAll(true);
        if (b) {
            this.imageReceiver.setDelegate((ImageReceiver.ImageReceiverDelegate)_$$Lambda$TextPaintImageReceiverSpan$Cb0mzcqNIfBx1iovVDp8PZkH5ug.INSTANCE);
        }
        this.imageReceiver.setImage(ImageLocation.getForDocument(document), format, ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90), document), format, -1, null, o, 1);
        this.alignTop = alignTop;
    }
    
    public void draw(final Canvas canvas, final CharSequence charSequence, int height, int dp, final float n, final int n2, int n3, final int n4, final Paint paint) {
        canvas.save();
        if (this.alignTop) {
            this.imageReceiver.setImageCoords((int)n, n2 - 1, this.width, this.height);
        }
        else {
            dp = AndroidUtilities.dp(4.0f);
            final ImageReceiver imageReceiver = this.imageReceiver;
            n3 = (int)n;
            height = this.height;
            imageReceiver.setImageCoords(n3, n2 + (n4 - dp - n2 - height) / 2, this.width, height);
        }
        this.imageReceiver.draw(canvas);
        canvas.restore();
    }
    
    public int getSize(final Paint paint, final CharSequence charSequence, int height, int n, final Paint$FontMetricsInt paint$FontMetricsInt) {
        if (paint$FontMetricsInt != null) {
            if (this.alignTop) {
                height = paint$FontMetricsInt.descent - paint$FontMetricsInt.ascent - AndroidUtilities.dp(4.0f);
                n = this.height - height;
                paint$FontMetricsInt.descent = n;
                paint$FontMetricsInt.bottom = n;
                height = 0 - height;
                paint$FontMetricsInt.ascent = height;
                paint$FontMetricsInt.top = height;
            }
            else {
                height = -this.height / 2 - AndroidUtilities.dp(4.0f);
                paint$FontMetricsInt.ascent = height;
                paint$FontMetricsInt.top = height;
                height = this.height;
                height = height - height / 2 - AndroidUtilities.dp(4.0f);
                paint$FontMetricsInt.descent = height;
                paint$FontMetricsInt.bottom = height;
            }
        }
        return this.width;
    }
}
