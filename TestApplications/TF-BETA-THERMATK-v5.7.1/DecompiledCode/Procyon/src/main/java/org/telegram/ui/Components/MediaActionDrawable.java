// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Rect;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import android.graphics.PathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.Paint$Style;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Cap;
import android.text.TextPaint;
import android.graphics.RectF;
import android.graphics.Path;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

public class MediaActionDrawable extends Drawable
{
    private static final float CANCEL_TO_CHECK_STAGE1 = 0.5f;
    private static final float CANCEL_TO_CHECK_STAGE2 = 0.5f;
    private static final float DOWNLOAD_TO_CANCEL_STAGE1 = 0.5f;
    private static final float DOWNLOAD_TO_CANCEL_STAGE2 = 0.2f;
    private static final float DOWNLOAD_TO_CANCEL_STAGE3 = 0.3f;
    private static final float EPS = 0.001f;
    public static final int ICON_CANCEL = 3;
    public static final int ICON_CANCEL_FILL = 14;
    public static final int ICON_CANCEL_NOPROFRESS = 12;
    public static final int ICON_CANCEL_PERCENT = 13;
    public static final int ICON_CHECK = 6;
    public static final int ICON_DOWNLOAD = 2;
    public static final int ICON_EMPTY = 10;
    public static final int ICON_EMPTY_NOPROGRESS = 11;
    public static final int ICON_FILE = 5;
    public static final int ICON_FIRE = 7;
    public static final int ICON_GIF = 8;
    public static final int ICON_NONE = 4;
    public static final int ICON_PAUSE = 1;
    public static final int ICON_PLAY = 0;
    public static final int ICON_SECRETCHECK = 9;
    private static final float[] pausePath1;
    private static final float[] pausePath2;
    private static final int pauseRotation = 90;
    private static final float[] playFinalPath;
    private static final float[] playPath1;
    private static final float[] playPath2;
    private static final int playRotation = 0;
    private float animatedDownloadProgress;
    private boolean animatingTransition;
    private ColorFilter colorFilter;
    private int currentIcon;
    private MediaActionDrawableDelegate delegate;
    private float downloadProgress;
    private float downloadProgressAnimationStart;
    private float downloadProgressTime;
    private float downloadRadOffset;
    private DecelerateInterpolator interpolator;
    private boolean isMini;
    private long lastAnimationTime;
    private int lastPercent;
    private int nextIcon;
    private float overrideAlpha;
    private Paint paint;
    private Paint paint2;
    private Paint paint3;
    private Path path1;
    private Path path2;
    private String percentString;
    private int percentStringWidth;
    private RectF rect;
    private float savedTransitionProgress;
    private float scale;
    private TextPaint textPaint;
    private float transitionAnimationTime;
    private float transitionProgress;
    
    static {
        playPath1 = new float[] { 18.0f, 15.0f, 34.0f, 24.0f, 34.0f, 24.0f, 18.0f, 24.0f, 18.0f, 24.0f };
        playPath2 = new float[] { 18.0f, 33.0f, 34.0f, 24.0f, 34.0f, 24.0f, 18.0f, 24.0f, 18.0f, 24.0f };
        playFinalPath = new float[] { 18.0f, 15.0f, 34.0f, 24.0f, 18.0f, 33.0f };
        pausePath1 = new float[] { 16.0f, 17.0f, 32.0f, 17.0f, 32.0f, 22.0f, 16.0f, 22.0f, 16.0f, 19.5f };
        pausePath2 = new float[] { 16.0f, 31.0f, 32.0f, 31.0f, 32.0f, 26.0f, 16.0f, 26.0f, 16.0f, 28.5f };
    }
    
    public MediaActionDrawable() {
        this.textPaint = new TextPaint(1);
        this.paint = new Paint(1);
        this.paint2 = new Paint(1);
        this.paint3 = new Paint(1);
        this.path1 = new Path();
        this.path2 = new Path();
        this.rect = new RectF();
        this.scale = 1.0f;
        this.interpolator = new DecelerateInterpolator();
        this.transitionAnimationTime = 400.0f;
        this.lastPercent = -1;
        this.overrideAlpha = 1.0f;
        this.transitionProgress = 1.0f;
        this.paint.setColor(-1);
        this.paint.setStrokeCap(Paint$Cap.ROUND);
        this.paint.setStrokeWidth((float)AndroidUtilities.dp(3.0f));
        this.paint.setStyle(Paint$Style.STROKE);
        this.paint3.setColor(-1);
        this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textPaint.setTextSize((float)AndroidUtilities.dp(13.0f));
        this.textPaint.setColor(-1);
        this.paint2.setColor(-1);
        this.paint2.setPathEffect((PathEffect)new CornerPathEffect((float)AndroidUtilities.dp(2.0f)));
    }
    
    private float getCircleValue(float n) {
        while (n > 360.0f) {
            n -= 360.0f;
        }
        return n;
    }
    
    public void draw(final Canvas canvas) {
        final Rect bounds = this.getBounds();
        final int centerX;
        final int n = centerX = bounds.centerX();
        final int centerY = bounds.centerY();
        final int nextIcon = this.nextIcon;
        if (nextIcon == 4) {
            final float n2 = 1.0f - this.transitionProgress;
            canvas.save();
            canvas.scale(n2, n2, (float)centerX, (float)centerY);
        }
        else if ((nextIcon == 6 || nextIcon == 10) && this.currentIcon == 4) {
            canvas.save();
            final float transitionProgress = this.transitionProgress;
            canvas.scale(transitionProgress, transitionProgress, (float)centerX, (float)centerY);
        }
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon == 2 || this.nextIcon == 2) {
            final float n3 = (float)centerY;
            final float n4 = n3 - AndroidUtilities.dp(9.0f) * this.scale;
            final float n5 = AndroidUtilities.dp(9.0f) * this.scale + n3;
            float n6 = AndroidUtilities.dp(12.0f) * this.scale + n3;
            final int currentIcon = this.currentIcon;
            float n7;
            float n8;
            if ((currentIcon == 3 || currentIcon == 14) && this.nextIcon == 2) {
                this.paint.setAlpha((int)(Math.min(1.0f, this.transitionProgress / 0.5f) * 255.0f));
                n7 = this.transitionProgress;
                n8 = AndroidUtilities.dp(12.0f) * this.scale + n3;
            }
            else {
                final int nextIcon2 = this.nextIcon;
                if (nextIcon2 != 3 && nextIcon2 != 14 && nextIcon2 != 2) {
                    this.paint.setAlpha((int)(Math.min(1.0f, this.savedTransitionProgress / 0.5f) * 255.0f * (1.0f - this.transitionProgress)));
                    n7 = this.savedTransitionProgress;
                }
                else {
                    this.paint.setAlpha(255);
                    n7 = this.transitionProgress;
                }
                n8 = AndroidUtilities.dp(1.0f) * this.scale + n3;
            }
            float n27;
            float n36;
            float n37;
            float n38;
            float n39;
            if (this.animatingTransition) {
                float n26;
                float n28;
                float n29;
                if (this.nextIcon != 2 && n7 > 0.5f) {
                    final float n9 = AndroidUtilities.dp(13.0f) * this.scale;
                    final float n10 = n7 - 0.5f;
                    final float n11 = n10 / 0.5f;
                    float n12;
                    float n13;
                    if (n10 > 0.2f) {
                        n12 = (n10 - 0.2f) / 0.3f;
                        n13 = 1.0f;
                    }
                    else {
                        n13 = n10 / 0.2f;
                        n12 = 0.0f;
                    }
                    final RectF rect = this.rect;
                    final float n14 = (float)centerX;
                    final float n15 = n9 / 2.0f;
                    rect.set(n14 - n9, n6 - n15, n14, n15 + n6);
                    final float n16 = n12 * 100.0f;
                    canvas.drawArc(this.rect, n16, n11 * 104.0f - n16, false, this.paint);
                    final float n17 = n8 + (n6 - n8) * n13;
                    if (n12 > 0.0f) {
                        float n18;
                        if (this.nextIcon == 14) {
                            n18 = 0.0f;
                        }
                        else {
                            n18 = (1.0f - n12) * -45.0f;
                        }
                        final float n19 = AndroidUtilities.dp(7.0f) * n12 * this.scale;
                        final int n20 = (int)(n12 * 255.0f);
                        final int nextIcon3 = this.nextIcon;
                        int alpha = n20;
                        if (nextIcon3 != 3) {
                            alpha = n20;
                            if (nextIcon3 != 14) {
                                alpha = n20;
                                if (nextIcon3 != 2) {
                                    alpha = (int)(n20 * (1.0f - Math.min(1.0f, this.transitionProgress / 0.5f)));
                                }
                            }
                        }
                        if (n18 != 0.0f) {
                            canvas.save();
                            canvas.rotate(n18, n14, n3);
                        }
                        if (alpha != 0) {
                            this.paint.setAlpha(alpha);
                            if (this.nextIcon == 14) {
                                this.paint3.setAlpha(alpha);
                                this.rect.set((float)(centerX - AndroidUtilities.dp(3.5f)), (float)(centerY - AndroidUtilities.dp(3.5f)), (float)(centerX + AndroidUtilities.dp(3.5f)), (float)(centerY + AndroidUtilities.dp(3.5f)));
                                canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), this.paint3);
                                this.paint.setAlpha((int)(alpha * 0.15f));
                                float n21;
                                if (this.isMini) {
                                    n21 = 2.0f;
                                }
                                else {
                                    n21 = 4.0f;
                                }
                                final int dp = AndroidUtilities.dp(n21);
                                this.rect.set((float)(bounds.left + dp), (float)(bounds.top + dp), (float)(bounds.right - dp), (float)(bounds.bottom - dp));
                                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                                this.paint.setAlpha(alpha);
                            }
                            else {
                                final float n22 = n14 - n19;
                                final float n23 = n3 - n19;
                                final float n24 = n14 + n19;
                                final float n25 = n3 + n19;
                                canvas.drawLine(n22, n23, n24, n25, this.paint);
                                canvas.drawLine(n24, n23, n22, n25, this.paint);
                            }
                        }
                        if (n18 != 0.0f) {
                            canvas.restore();
                        }
                    }
                    n26 = n6;
                    n27 = (n28 = n14);
                    n29 = n17;
                }
                else {
                    float n30;
                    if (this.nextIcon == 2) {
                        n30 = 1.0f - n7;
                    }
                    else {
                        n30 = n7 / 0.5f;
                        n7 = 1.0f - n30;
                    }
                    final float n31 = (n8 - n4) * n30 + n4;
                    n6 = (n6 - n5) * n30 + n5;
                    final float n32 = (float)centerX;
                    n28 = n32 - AndroidUtilities.dp(8.0f) * n7 * this.scale;
                    n27 = n32 + AndroidUtilities.dp(8.0f) * n7 * this.scale;
                    final float n33 = n6 - AndroidUtilities.dp(8.0f) * n7 * this.scale;
                    n29 = n31;
                    n26 = n33;
                }
                final float n34 = n6;
                final float n35 = n26;
                n36 = n29;
                n37 = n34;
                n38 = n28;
                n39 = n35;
            }
            else {
                final float n40 = (float)centerX;
                final float n41 = (float)AndroidUtilities.dp(8.0f);
                final float scale = this.scale;
                final float n42 = (float)AndroidUtilities.dp(8.0f);
                final float scale2 = this.scale;
                final float n43 = (float)AndroidUtilities.dp(8.0f);
                final float scale3 = this.scale;
                n27 = n40 + n42 * scale2;
                n38 = n40 - n41 * scale;
                n39 = n5 - n43 * scale3;
                n36 = n4;
                n37 = n5;
            }
            if (n36 != n37) {
                final float n44 = (float)centerX;
                canvas.drawLine(n44, n36, n44, n37, this.paint);
            }
            final float n45 = (float)centerX;
            if (n38 != n45) {
                canvas.drawLine(n38, n39, n45, n37, this.paint);
                canvas.drawLine(n27, n39, n45, n37, this.paint);
            }
        }
        final int currentIcon2 = this.currentIcon;
        Label_2572: {
            Label_1547: {
                if (currentIcon2 != 3 && currentIcon2 != 14) {
                    if (currentIcon2 == 4) {
                        final int nextIcon4 = this.nextIcon;
                        if (nextIcon4 == 14) {
                            break Label_1547;
                        }
                        if (nextIcon4 == 3) {
                            break Label_1547;
                        }
                    }
                    final int currentIcon3 = this.currentIcon;
                    if (currentIcon3 == 10 || this.nextIcon == 10 || currentIcon3 == 13) {
                        final int nextIcon5 = this.nextIcon;
                        int n46;
                        if (nextIcon5 != 4 && nextIcon5 != 6) {
                            n46 = 255;
                        }
                        else {
                            n46 = (int)((1.0f - this.transitionProgress) * 255.0f);
                        }
                        if (n46 != 0) {
                            this.paint.setAlpha((int)(n46 * this.overrideAlpha));
                            final float max = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                            float n47;
                            if (this.isMini) {
                                n47 = 2.0f;
                            }
                            else {
                                n47 = 4.0f;
                            }
                            final int dp2 = AndroidUtilities.dp(n47);
                            this.rect.set((float)(bounds.left + dp2), (float)(bounds.top + dp2), (float)(bounds.right - dp2), (float)(bounds.bottom - dp2));
                            canvas.drawArc(this.rect, this.downloadRadOffset, max, false, this.paint);
                        }
                    }
                    break Label_2572;
                }
            }
            final int nextIcon6 = this.nextIcon;
            int alpha2 = 0;
            float n50 = 0.0f;
            float n55 = 0.0f;
            float n65 = 0.0f;
            float n66 = 0.0f;
            float n67 = 0.0f;
            Label_2062: {
                Label_1621: {
                    if (nextIcon6 == 2) {
                        final float transitionProgress2 = this.transitionProgress;
                        float n49;
                        if (transitionProgress2 <= 0.5f) {
                            final float n48 = 1.0f - transitionProgress2 / 0.5f;
                            n49 = AndroidUtilities.dp(7.0f) * n48 * this.scale;
                            alpha2 = (int)(n48 * 255.0f);
                        }
                        else {
                            n49 = 0.0f;
                            alpha2 = 0;
                        }
                        n50 = n49;
                    }
                    else {
                        if (nextIcon6 != 0 && nextIcon6 != 1 && nextIcon6 != 5 && nextIcon6 != 8 && nextIcon6 != 9 && nextIcon6 != 7 && nextIcon6 != 6) {
                            float n51;
                            float n54;
                            float n56;
                            if (nextIcon6 == 4) {
                                final float transitionProgress3 = this.transitionProgress;
                                n51 = 1.0f - transitionProgress3;
                                final float n52 = (float)AndroidUtilities.dp(7.0f);
                                final float scale4 = this.scale;
                                alpha2 = (int)(n51 * 255.0f);
                                float n53;
                                if (this.currentIcon == 14) {
                                    n53 = 0.0f;
                                }
                                else {
                                    n53 = transitionProgress3 * 45.0f;
                                    n51 = 1.0f;
                                }
                                n54 = n53;
                                n50 = n52 * scale4;
                                n55 = 0.0f;
                                n56 = 0.0f;
                            }
                            else {
                                if (nextIcon6 != 14 && nextIcon6 != 3) {
                                    n50 = this.scale * AndroidUtilities.dp(7.0f);
                                    alpha2 = 255;
                                    break Label_1621;
                                }
                                final float transitionProgress4 = this.transitionProgress;
                                float n57;
                                float n58;
                                if (this.currentIcon == 4) {
                                    n57 = transitionProgress4;
                                    n58 = 0.0f;
                                }
                                else {
                                    n58 = (1.0f - transitionProgress4) * 45.0f;
                                    n57 = 1.0f;
                                }
                                final float n59 = (float)AndroidUtilities.dp(7.0f);
                                final float scale5 = this.scale;
                                final int n60 = (int)(transitionProgress4 * 255.0f);
                                int n61;
                                if (this.nextIcon == 14) {
                                    n55 = (float)bounds.left;
                                    n61 = bounds.top;
                                }
                                else {
                                    n55 = (float)bounds.centerX();
                                    n61 = bounds.centerY();
                                }
                                final float n62 = (float)n61;
                                final float n63 = n59 * scale5;
                                n54 = n58;
                                n51 = n57;
                                n56 = n62;
                                alpha2 = n60;
                                n50 = n63;
                            }
                            final float n64 = n51;
                            n65 = n54;
                            n66 = n56;
                            n67 = n64;
                            break Label_2062;
                        }
                        float n68;
                        if (this.nextIcon == 6) {
                            n68 = Math.min(1.0f, this.transitionProgress / 0.5f);
                        }
                        else {
                            n68 = this.transitionProgress;
                        }
                        final float n69 = 1.0f - n68;
                        final float n70 = (float)AndroidUtilities.dp(7.0f);
                        final float scale6 = this.scale;
                        alpha2 = (int)(Math.min(1.0f, n69 * 2.0f) * 255.0f);
                        n50 = n70 * n69 * scale6;
                        n66 = 0.0f;
                        final float n71 = 1.0f;
                        n65 = n68 * 45.0f;
                        n55 = 0.0f;
                        n67 = n71;
                        break Label_2062;
                    }
                }
                n55 = 0.0f;
                n65 = 0.0f;
                n66 = 0.0f;
                n67 = 1.0f;
            }
            if (n67 != 1.0f) {
                canvas.save();
                canvas.scale(n67, n67, n55, n66);
            }
            if (n65 != 0.0f) {
                canvas.save();
                canvas.rotate(n65, (float)centerX, (float)centerY);
            }
            if (alpha2 != 0) {
                final Paint paint = this.paint;
                final float n72 = (float)alpha2;
                paint.setAlpha((int)(this.overrideAlpha * n72));
                if (this.currentIcon != 14 && this.nextIcon != 14) {
                    final float n73 = (float)centerX;
                    final float n74 = n73 - n50;
                    final float n75 = (float)centerY;
                    final float n76 = n75 - n50;
                    final float n77 = n73 + n50;
                    final float n78 = n75 + n50;
                    canvas.drawLine(n74, n76, n77, n78, this.paint);
                    canvas.drawLine(n77, n76, n74, n78, this.paint);
                }
                else {
                    this.paint3.setAlpha((int)(n72 * this.overrideAlpha));
                    this.rect.set((float)(centerX - AndroidUtilities.dp(3.5f)), (float)(centerY - AndroidUtilities.dp(3.5f)), (float)(AndroidUtilities.dp(3.5f) + centerX), (float)(AndroidUtilities.dp(3.5f) + centerY));
                    canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), this.paint3);
                }
            }
            if (n65 != 0.0f) {
                canvas.restore();
            }
            final int currentIcon4 = this.currentIcon;
            Label_2561: {
                if (currentIcon4 != 3 && currentIcon4 != 14) {
                    if (currentIcon4 != 4) {
                        break Label_2561;
                    }
                    final int nextIcon7 = this.nextIcon;
                    if (nextIcon7 != 14 && nextIcon7 != 3) {
                        break Label_2561;
                    }
                }
                if (alpha2 != 0) {
                    final float max2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                    float n79;
                    if (this.isMini) {
                        n79 = 2.0f;
                    }
                    else {
                        n79 = 4.0f;
                    }
                    final int dp3 = AndroidUtilities.dp(n79);
                    this.rect.set((float)(bounds.left + dp3), (float)(bounds.top + dp3), (float)(bounds.right - dp3), (float)(bounds.bottom - dp3));
                    final int currentIcon5 = this.currentIcon;
                    Label_2542: {
                        if (currentIcon5 != 14) {
                            if (currentIcon5 != 4) {
                                break Label_2542;
                            }
                            final int nextIcon8 = this.nextIcon;
                            if (nextIcon8 != 14 && nextIcon8 != 3) {
                                break Label_2542;
                            }
                        }
                        this.paint.setAlpha((int)(alpha2 * 0.15f * this.overrideAlpha));
                        canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                        this.paint.setAlpha(alpha2);
                    }
                    canvas.drawArc(this.rect, this.downloadRadOffset, max2, false, this.paint);
                }
            }
            if (n67 != 1.0f) {
                canvas.restore();
            }
        }
        final Drawable drawable = null;
        Drawable drawable2 = null;
        final int currentIcon6 = this.currentIcon;
        float n80;
        float n81;
        if (currentIcon6 == this.nextIcon) {
            n80 = 1.0f;
            n81 = 1.0f;
        }
        else {
            float n82;
            float max3;
            if (currentIcon6 == 4) {
                n82 = this.transitionProgress;
                max3 = 1.0f - n82;
            }
            else {
                n82 = Math.min(1.0f, this.transitionProgress / 0.5f);
                max3 = Math.max(0.0f, 1.0f - this.transitionProgress / 0.5f);
            }
            n81 = max3;
            n80 = n82;
        }
        Drawable chat_fileIcon;
        if (this.nextIcon == 5) {
            chat_fileIcon = Theme.chat_fileIcon;
        }
        else {
            chat_fileIcon = drawable;
            if (this.currentIcon == 5) {
                drawable2 = Theme.chat_fileIcon;
                chat_fileIcon = drawable;
            }
        }
        Drawable chat_flameIcon;
        if (this.nextIcon == 7) {
            chat_flameIcon = Theme.chat_flameIcon;
        }
        else {
            chat_flameIcon = chat_fileIcon;
            if (this.currentIcon == 7) {
                drawable2 = Theme.chat_flameIcon;
                chat_flameIcon = chat_fileIcon;
            }
        }
        Drawable chat_gifIcon;
        Drawable chat_gifIcon2;
        if (this.nextIcon == 8) {
            chat_gifIcon = Theme.chat_gifIcon;
            chat_gifIcon2 = drawable2;
        }
        else {
            chat_gifIcon = chat_flameIcon;
            chat_gifIcon2 = drawable2;
            if (this.currentIcon == 8) {
                chat_gifIcon2 = Theme.chat_gifIcon;
                chat_gifIcon = chat_flameIcon;
            }
        }
        if (this.currentIcon == 9 || this.nextIcon == 9) {
            final Paint paint2 = this.paint;
            int alpha3;
            if (this.currentIcon == this.nextIcon) {
                alpha3 = 255;
            }
            else {
                alpha3 = (int)(this.transitionProgress * 255.0f);
            }
            paint2.setAlpha(alpha3);
            final int n83 = centerY + AndroidUtilities.dp(7.0f);
            final int n84 = centerX - AndroidUtilities.dp(3.0f);
            if (this.currentIcon != this.nextIcon) {
                canvas.save();
                final float transitionProgress5 = this.transitionProgress;
                canvas.scale(transitionProgress5, transitionProgress5, (float)centerX, (float)centerY);
            }
            final float n85 = (float)(n84 - AndroidUtilities.dp(6.0f));
            final float n86 = (float)(n83 - AndroidUtilities.dp(6.0f));
            final float n87 = (float)n84;
            final float n88 = (float)n83;
            canvas.drawLine(n85, n86, n87, n88, this.paint);
            canvas.drawLine(n87, n88, (float)(n84 + AndroidUtilities.dp(12.0f)), (float)(n83 - AndroidUtilities.dp(12.0f)), this.paint);
            if (this.currentIcon != this.nextIcon) {
                canvas.restore();
            }
        }
        final Drawable drawable3 = chat_gifIcon;
        final Drawable drawable4 = chat_gifIcon2;
        if (this.currentIcon == 12 || this.nextIcon == 12) {
            final int currentIcon7 = this.currentIcon;
            final int nextIcon9 = this.nextIcon;
            float transitionProgress6;
            if (currentIcon7 == nextIcon9) {
                transitionProgress6 = 1.0f;
            }
            else if (nextIcon9 == 13) {
                transitionProgress6 = this.transitionProgress;
            }
            else {
                transitionProgress6 = 1.0f - this.transitionProgress;
            }
            final Paint paint3 = this.paint;
            int alpha4;
            if (this.currentIcon == this.nextIcon) {
                alpha4 = 255;
            }
            else {
                alpha4 = (int)(transitionProgress6 * 255.0f);
            }
            paint3.setAlpha(alpha4);
            AndroidUtilities.dp(7.0f);
            AndroidUtilities.dp(3.0f);
            if (this.currentIcon != this.nextIcon) {
                canvas.save();
                canvas.scale(transitionProgress6, transitionProgress6, (float)centerX, (float)centerY);
            }
            final float n89 = AndroidUtilities.dp(7.0f) * this.scale;
            final float n90 = (float)centerX;
            final float n91 = n90 - n89;
            final float n92 = (float)centerY;
            final float n93 = n92 - n89;
            final float n94 = n90 + n89;
            final float n95 = n92 + n89;
            canvas.drawLine(n91, n93, n94, n95, this.paint);
            canvas.drawLine(n94, n93, n91, n95, this.paint);
            if (this.currentIcon != this.nextIcon) {
                canvas.restore();
            }
        }
        if (this.currentIcon == 13 || this.nextIcon == 13) {
            final int currentIcon8 = this.currentIcon;
            final int nextIcon10 = this.nextIcon;
            float transitionProgress7;
            if (currentIcon8 == nextIcon10) {
                transitionProgress7 = 1.0f;
            }
            else if (nextIcon10 == 13) {
                transitionProgress7 = this.transitionProgress;
            }
            else {
                transitionProgress7 = 1.0f - this.transitionProgress;
            }
            this.textPaint.setAlpha((int)(transitionProgress7 * 255.0f));
            final int dp4 = AndroidUtilities.dp(5.0f);
            final int n96 = this.percentStringWidth / 2;
            if (this.currentIcon != this.nextIcon) {
                canvas.save();
                canvas.scale(transitionProgress7, transitionProgress7, (float)centerX, (float)centerY);
            }
            final int lastPercent = (int)(this.animatedDownloadProgress * 100.0f);
            if (this.percentString == null || lastPercent != this.lastPercent) {
                this.lastPercent = lastPercent;
                this.percentString = String.format("%d%%", this.lastPercent);
                this.percentStringWidth = (int)Math.ceil(this.textPaint.measureText(this.percentString));
            }
            canvas.drawText(this.percentString, (float)(centerX - n96), (float)(dp4 + centerY), (Paint)this.textPaint);
            if (this.currentIcon != this.nextIcon) {
                canvas.restore();
            }
        }
        final int currentIcon9 = this.currentIcon;
        int n97 = 0;
        float n98 = 0.0f;
        Label_4820: {
            if (currentIcon9 != 0 && currentIcon9 != 1) {
                final int nextIcon11 = this.nextIcon;
                if (nextIcon11 != 0) {
                    if (nextIcon11 != 1) {
                        n97 = centerX;
                        n98 = n80;
                        break Label_4820;
                    }
                }
            }
            float interpolation;
            if (((this.currentIcon == 0 && this.nextIcon == 1) || (this.currentIcon == 1 && this.nextIcon == 0)) && this.animatingTransition) {
                interpolation = this.interpolator.getInterpolation(this.transitionProgress);
            }
            else {
                interpolation = 0.0f;
            }
            this.path1.reset();
            this.path2.reset();
            float[] playFinalPath = null;
            final int currentIcon10 = this.currentIcon;
            float[] array;
            float[] array2;
            int n99;
            if (currentIcon10 != 0) {
                if (currentIcon10 != 1) {
                    array = null;
                    array2 = null;
                    n99 = 0;
                }
                else {
                    array = MediaActionDrawable.pausePath1;
                    array2 = MediaActionDrawable.pausePath2;
                    n99 = 90;
                }
            }
            else {
                array = MediaActionDrawable.playPath1;
                array2 = MediaActionDrawable.playPath2;
                playFinalPath = MediaActionDrawable.playFinalPath;
                n99 = 0;
            }
            final int nextIcon12 = this.nextIcon;
            float[] array3 = null;
            float[] array4 = null;
            int n100 = 0;
            Label_3748: {
                if (nextIcon12 != 0) {
                    if (nextIcon12 == 1) {
                        array3 = MediaActionDrawable.pausePath1;
                        array4 = MediaActionDrawable.pausePath2;
                        n100 = 90;
                        break Label_3748;
                    }
                    array3 = null;
                    array4 = null;
                }
                else {
                    array3 = MediaActionDrawable.playPath1;
                    array4 = MediaActionDrawable.playPath2;
                }
                n100 = 0;
            }
            float[] array5;
            if (array == null) {
                array5 = null;
                final float[] array6 = null;
                array2 = array4;
                array4 = array6;
            }
            else {
                final float[] array7 = array;
                array5 = array3;
                array3 = array7;
            }
            int n114 = 0;
            Label_4658: {
                if (!this.animatingTransition && playFinalPath != null) {
                    for (int i = 0; i < playFinalPath.length / 2; ++i) {
                        if (i == 0) {
                            final Path path1 = this.path1;
                            final int n101 = i * 2;
                            final float n102 = (float)AndroidUtilities.dp(playFinalPath[n101]);
                            final float scale7 = this.scale;
                            final int n103 = n101 + 1;
                            path1.moveTo(n102 * scale7, AndroidUtilities.dp(playFinalPath[n103]) * this.scale);
                            this.path2.moveTo(AndroidUtilities.dp(playFinalPath[n101]) * this.scale, AndroidUtilities.dp(playFinalPath[n103]) * this.scale);
                        }
                        else {
                            final Path path2 = this.path1;
                            final int n104 = i * 2;
                            final float n105 = (float)AndroidUtilities.dp(playFinalPath[n104]);
                            final float scale8 = this.scale;
                            final int n106 = n104 + 1;
                            path2.lineTo(n105 * scale8, AndroidUtilities.dp(playFinalPath[n106]) * this.scale);
                            this.path2.lineTo(AndroidUtilities.dp(playFinalPath[n104]) * this.scale, AndroidUtilities.dp(playFinalPath[n106]) * this.scale);
                        }
                    }
                }
                else {
                    if (array5 != null) {
                        for (int j = 0; j < 5; ++j) {
                            if (j == 0) {
                                final Path path3 = this.path1;
                                final int n107 = j * 2;
                                final float n108 = (float)AndroidUtilities.dp(array3[n107] + (array5[n107] - array3[n107]) * interpolation);
                                final float scale9 = this.scale;
                                final int n109 = n107 + 1;
                                path3.moveTo(n108 * scale9, AndroidUtilities.dp(array3[n109] + (array5[n109] - array3[n109]) * interpolation) * this.scale);
                                this.path2.moveTo(AndroidUtilities.dp(array2[n107] + (array4[n107] - array2[n107]) * interpolation) * this.scale, AndroidUtilities.dp(array2[n109] + (array4[n109] - array2[n109]) * interpolation) * this.scale);
                            }
                            else {
                                final Path path4 = this.path1;
                                final int n110 = j * 2;
                                final float n111 = (float)AndroidUtilities.dp(array3[n110] + (array5[n110] - array3[n110]) * interpolation);
                                final float scale10 = this.scale;
                                final int n112 = n110 + 1;
                                path4.lineTo(n111 * scale10, AndroidUtilities.dp(array3[n112] + (array5[n112] - array3[n112]) * interpolation) * this.scale);
                                this.path2.lineTo(AndroidUtilities.dp(array2[n110] + (array4[n110] - array2[n110]) * interpolation) * this.scale, AndroidUtilities.dp(array2[n112] + (array4[n112] - array2[n112]) * interpolation) * this.scale);
                            }
                        }
                        final int n113 = n100;
                        this.paint2.setAlpha(255);
                        n97 = centerX;
                        n114 = n113;
                        break Label_4658;
                    }
                    for (int k = 0; k < 5; ++k) {
                        if (k == 0) {
                            final Path path5 = this.path1;
                            final int n115 = k * 2;
                            final float n116 = (float)AndroidUtilities.dp(array3[n115]);
                            final float scale11 = this.scale;
                            final int n117 = n115 + 1;
                            path5.moveTo(n116 * scale11, AndroidUtilities.dp(array3[n117]) * this.scale);
                            this.path2.moveTo(AndroidUtilities.dp(array2[n115]) * this.scale, AndroidUtilities.dp(array2[n117]) * this.scale);
                        }
                        else {
                            final Path path6 = this.path1;
                            final int n118 = k * 2;
                            final float n119 = (float)AndroidUtilities.dp(array3[n118]);
                            final float scale12 = this.scale;
                            final int n120 = n118 + 1;
                            path6.lineTo(n119 * scale12, AndroidUtilities.dp(array3[n120]) * this.scale);
                            this.path2.lineTo(AndroidUtilities.dp(array2[n118]) * this.scale, AndroidUtilities.dp(array2[n120]) * this.scale);
                        }
                    }
                    final int nextIcon13 = this.nextIcon;
                    if (nextIcon13 == 4) {
                        this.paint2.setAlpha((int)((1.0f - this.transitionProgress) * 255.0f));
                    }
                    else {
                        final Paint paint4 = this.paint2;
                        int alpha5;
                        if (this.currentIcon == nextIcon13) {
                            alpha5 = 255;
                        }
                        else {
                            alpha5 = (int)(this.transitionProgress * 255.0f);
                        }
                        paint4.setAlpha(alpha5);
                    }
                }
                n114 = n100;
                n97 = n;
            }
            this.path1.close();
            this.path2.close();
            canvas.save();
            canvas.translate((float)bounds.left, (float)bounds.top);
            canvas.rotate(n99 + (n114 - n99) * interpolation, (float)(n97 - bounds.left), (float)(centerY - bounds.top));
            final int currentIcon11 = this.currentIcon;
            if ((currentIcon11 != 0 && currentIcon11 != 1) || this.currentIcon == 4) {
                final float n121 = (float)(n97 - bounds.left);
                final float n122 = (float)(centerY - bounds.top);
                final float n123 = n80;
                canvas.scale(n123, n123, n121, n122);
            }
            canvas.drawPath(this.path1, this.paint2);
            canvas.drawPath(this.path2, this.paint2);
            canvas.restore();
            n98 = n80;
        }
        if (this.currentIcon == 6 || this.nextIcon == 6) {
            float n125;
            float n126;
            if (this.currentIcon != 6) {
                final float transitionProgress8 = this.transitionProgress;
                if (transitionProgress8 > 0.5f) {
                    final float n124 = (transitionProgress8 - 0.5f) / 0.5f;
                    n125 = 1.0f - Math.min(1.0f, n124 / 0.5f);
                    if (n124 > 0.5f) {
                        n126 = (n124 - 0.5f) / 0.5f;
                    }
                    else {
                        n126 = 0.0f;
                    }
                }
                else {
                    n126 = 0.0f;
                    n125 = 1.0f;
                }
            }
            else {
                n126 = 1.0f;
                n125 = 0.0f;
            }
            final int n127 = centerY + AndroidUtilities.dp(7.0f);
            final int n128 = n97 - AndroidUtilities.dp(3.0f);
            this.paint.setAlpha(255);
            if (n125 < 1.0f) {
                canvas.drawLine((float)(n128 - AndroidUtilities.dp(6.0f)), (float)(n127 - AndroidUtilities.dp(6.0f)), n128 - AndroidUtilities.dp(6.0f) * n125, n127 - AndroidUtilities.dp(6.0f) * n125, this.paint);
            }
            if (n126 > 0.0f) {
                final float n129 = (float)n128;
                final float n130 = (float)n127;
                canvas.drawLine(n129, n130, n129 + AndroidUtilities.dp(12.0f) * n126, n130 - AndroidUtilities.dp(12.0f) * n126, this.paint);
            }
        }
        if (drawable4 != null && drawable4 != drawable3) {
            final int n131 = (int)(drawable4.getIntrinsicWidth() * n81);
            final int n132 = (int)(drawable4.getIntrinsicHeight() * n81);
            drawable4.setColorFilter(this.colorFilter);
            int alpha6;
            if (this.currentIcon == this.nextIcon) {
                alpha6 = 255;
            }
            else {
                alpha6 = (int)((1.0f - this.transitionProgress) * 255.0f);
            }
            drawable4.setAlpha(alpha6);
            final int n133 = n131 / 2;
            final int n134 = n132 / 2;
            drawable4.setBounds(n97 - n133, centerY - n134, n97 + n133, centerY + n134);
            drawable4.draw(canvas);
        }
        if (drawable3 != null) {
            final int n135 = (int)(drawable3.getIntrinsicWidth() * n98);
            final int n136 = (int)(drawable3.getIntrinsicHeight() * n98);
            drawable3.setColorFilter(this.colorFilter);
            int alpha7;
            if (this.currentIcon == this.nextIcon) {
                alpha7 = 255;
            }
            else {
                alpha7 = (int)(this.transitionProgress * 255.0f);
            }
            drawable3.setAlpha(alpha7);
            final int n137 = n135 / 2;
            final int n138 = n136 / 2;
            drawable3.setBounds(n97 - n137, centerY - n138, n97 + n137, centerY + n138);
            drawable3.draw(canvas);
        }
        final long currentTimeMillis = System.currentTimeMillis();
        long n139;
        if ((n139 = currentTimeMillis - this.lastAnimationTime) > 17L) {
            n139 = 17L;
        }
        this.lastAnimationTime = currentTimeMillis;
        final int currentIcon12 = this.currentIcon;
        Label_5560: {
            if (currentIcon12 != 3 && currentIcon12 != 14 && (currentIcon12 != 4 || this.nextIcon != 14)) {
                final int currentIcon13 = this.currentIcon;
                if (currentIcon13 != 10 && currentIcon13 != 13) {
                    break Label_5560;
                }
            }
            this.downloadRadOffset += 360L * n139 / 2500.0f;
            this.downloadRadOffset = this.getCircleValue(this.downloadRadOffset);
            if (this.nextIcon != 2) {
                final float downloadProgress = this.downloadProgress;
                final float downloadProgressAnimationStart = this.downloadProgressAnimationStart;
                final float n140 = downloadProgress - downloadProgressAnimationStart;
                if (n140 > 0.0f) {
                    this.downloadProgressTime += n139;
                    final float downloadProgressTime = this.downloadProgressTime;
                    if (downloadProgressTime >= 200.0f) {
                        this.animatedDownloadProgress = downloadProgress;
                        this.downloadProgressAnimationStart = downloadProgress;
                        this.downloadProgressTime = 0.0f;
                    }
                    else {
                        this.animatedDownloadProgress = downloadProgressAnimationStart + n140 * this.interpolator.getInterpolation(downloadProgressTime / 200.0f);
                    }
                }
            }
            this.invalidateSelf();
        }
        if (this.animatingTransition) {
            final float transitionProgress9 = this.transitionProgress;
            if (transitionProgress9 < 1.0f) {
                this.transitionProgress = transitionProgress9 + n139 / this.transitionAnimationTime;
                if (this.transitionProgress >= 1.0f) {
                    this.currentIcon = this.nextIcon;
                    this.transitionProgress = 1.0f;
                    this.animatingTransition = false;
                }
                this.invalidateSelf();
            }
        }
        final int nextIcon14 = this.nextIcon;
        if (nextIcon14 == 4 || ((nextIcon14 == 6 || nextIcon14 == 10) && this.currentIcon == 4)) {
            canvas.restore();
        }
    }
    
    public int getColor() {
        return this.paint.getColor();
    }
    
    public int getCurrentIcon() {
        return this.nextIcon;
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(48.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(48.0f);
    }
    
    public int getMinimumHeight() {
        return AndroidUtilities.dp(48.0f);
    }
    
    public int getMinimumWidth() {
        return AndroidUtilities.dp(48.0f);
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public int getPreviousIcon() {
        return this.currentIcon;
    }
    
    public float getProgressAlpha() {
        return 1.0f - this.transitionProgress;
    }
    
    public float getTransitionProgress() {
        float transitionProgress;
        if (this.animatingTransition) {
            transitionProgress = this.transitionProgress;
        }
        else {
            transitionProgress = 1.0f;
        }
        return transitionProgress;
    }
    
    public void invalidateSelf() {
        super.invalidateSelf();
        final MediaActionDrawableDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.invalidate();
        }
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setBounds(final int n, final int n2, final int n3, final int n4) {
        super.setBounds(n, n2, n3, n4);
        this.scale = (n3 - n) / (float)this.getIntrinsicWidth();
        if (this.scale < 0.7f) {
            this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        }
    }
    
    public void setColor(final int n) {
        final Paint paint = this.paint;
        final int n2 = 0xFF000000 | n;
        paint.setColor(n2);
        this.paint2.setColor(n2);
        this.paint3.setColor(n2);
        this.textPaint.setColor(n2);
        this.colorFilter = (ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.paint2.setColorFilter(colorFilter);
        this.paint3.setColorFilter(colorFilter);
        this.textPaint.setColorFilter(colorFilter);
    }
    
    public void setDelegate(final MediaActionDrawableDelegate delegate) {
        this.delegate = delegate;
    }
    
    public boolean setIcon(final int currentIcon, final boolean b) {
        if (this.currentIcon == currentIcon) {
            final int nextIcon = this.nextIcon;
            if (nextIcon != currentIcon) {
                this.currentIcon = nextIcon;
                this.transitionProgress = 1.0f;
            }
        }
        if (b) {
            final int currentIcon2 = this.currentIcon;
            if (currentIcon2 == currentIcon || this.nextIcon == currentIcon) {
                return false;
            }
            if (currentIcon2 == 2 && (currentIcon == 3 || currentIcon == 14)) {
                this.transitionAnimationTime = 400.0f;
            }
            else if (this.currentIcon != 4 && currentIcon == 6) {
                this.transitionAnimationTime = 360.0f;
            }
            else if ((this.currentIcon == 4 && currentIcon == 14) || (this.currentIcon == 14 && currentIcon == 4)) {
                this.transitionAnimationTime = 160.0f;
            }
            else {
                this.transitionAnimationTime = 220.0f;
            }
            this.animatingTransition = true;
            this.nextIcon = currentIcon;
            this.savedTransitionProgress = this.transitionProgress;
            this.transitionProgress = 0.0f;
        }
        else {
            if (this.currentIcon == currentIcon) {
                return false;
            }
            this.animatingTransition = false;
            this.nextIcon = currentIcon;
            this.currentIcon = currentIcon;
            this.savedTransitionProgress = this.transitionProgress;
            this.transitionProgress = 1.0f;
        }
        if (currentIcon == 3 || currentIcon == 14) {
            this.downloadRadOffset = 112.0f;
            this.animatedDownloadProgress = 0.0f;
            this.downloadProgressAnimationStart = 0.0f;
            this.downloadProgressTime = 0.0f;
        }
        this.invalidateSelf();
        return true;
    }
    
    public void setMini(final boolean isMini) {
        this.isMini = isMini;
        final Paint paint = this.paint;
        float n;
        if (this.isMini) {
            n = 2.0f;
        }
        else {
            n = 3.0f;
        }
        paint.setStrokeWidth((float)AndroidUtilities.dp(n));
    }
    
    public void setOverrideAlpha(final float overrideAlpha) {
        this.overrideAlpha = overrideAlpha;
    }
    
    public void setProgress(final float n, final boolean b) {
        if (!b) {
            this.animatedDownloadProgress = n;
            this.downloadProgressAnimationStart = n;
        }
        else {
            if (this.animatedDownloadProgress > n) {
                this.animatedDownloadProgress = n;
            }
            this.downloadProgressAnimationStart = this.animatedDownloadProgress;
        }
        this.downloadProgress = n;
        this.downloadProgressTime = 0.0f;
        this.invalidateSelf();
    }
    
    public interface MediaActionDrawableDelegate
    {
        void invalidate();
    }
}
