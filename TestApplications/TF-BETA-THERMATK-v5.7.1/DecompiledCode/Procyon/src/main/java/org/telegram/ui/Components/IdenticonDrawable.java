// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC;
import android.graphics.ColorFilter;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class IdenticonDrawable extends Drawable
{
    private int[] colors;
    private byte[] data;
    private Paint paint;
    
    public IdenticonDrawable() {
        this.paint = new Paint();
        this.colors = new int[] { -1, -2758925, -13805707, -13657655 };
    }
    
    private int getBits(final int n) {
        return this.data[n / 8] >> n % 8 & 0x3;
    }
    
    public void draw(final Canvas canvas) {
        final byte[] data = this.data;
        if (data == null) {
            return;
        }
        if (data.length == 16) {
            final float n = (float)Math.floor(Math.min(this.getBounds().width(), this.getBounds().height()) / 8.0f);
            final float n2 = (float)this.getBounds().width();
            final float n3 = 8.0f * n;
            final float max = Math.max(0.0f, (n2 - n3) / 2.0f);
            final float max2 = Math.max(0.0f, (this.getBounds().height() - n3) / 2.0f);
            int i = 0;
            int n4 = 0;
            while (i < 8) {
                for (int j = 0; j < 8; ++j) {
                    final int bits = this.getBits(n4);
                    n4 += 2;
                    this.paint.setColor(this.colors[Math.abs(bits) % 4]);
                    final float n5 = max + j * n;
                    final float n6 = i * n;
                    canvas.drawRect(n5, n6 + max2, n5 + n, n6 + n + max2, this.paint);
                }
                ++i;
            }
        }
        else {
            final float n7 = (float)Math.floor(Math.min(this.getBounds().width(), this.getBounds().height()) / 12.0f);
            final float n8 = (float)this.getBounds().width();
            final float n9 = 12.0f * n7;
            final float max3 = Math.max(0.0f, (n8 - n9) / 2.0f);
            final float max4 = Math.max(0.0f, (this.getBounds().height() - n9) / 2.0f);
            int k = 0;
            int n10 = 0;
            while (k < 12) {
                for (int l = 0; l < 12; ++l) {
                    this.paint.setColor(this.colors[Math.abs(this.getBits(n10)) % 4]);
                    final float n11 = max3 + l * n7;
                    final float n12 = k * n7;
                    canvas.drawRect(n11, n12 + max4, n11 + n7, n12 + n7 + max4, this.paint);
                    n10 += 2;
                }
                ++k;
            }
        }
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(32.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(32.0f);
    }
    
    public int getOpacity() {
        return 0;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
    
    public void setColors(final int[] colors) {
        if (this.colors.length == 4) {
            this.colors = colors;
            this.invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("colors must have length of 4");
    }
    
    public void setEncryptedChat(final TLRPC.EncryptedChat encryptedChat) {
        this.data = encryptedChat.key_hash;
        if (this.data == null) {
            final byte[] calcAuthKeyHash = AndroidUtilities.calcAuthKeyHash(encryptedChat.auth_key);
            this.data = calcAuthKeyHash;
            encryptedChat.key_hash = calcAuthKeyHash;
        }
        this.invalidateSelf();
    }
}
