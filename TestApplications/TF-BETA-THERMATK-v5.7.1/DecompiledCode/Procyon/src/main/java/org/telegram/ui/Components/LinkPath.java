// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import android.graphics.Path$Direction;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.graphics.Path;

public class LinkPath extends Path
{
    private boolean allowReset;
    private int baselineShift;
    private StaticLayout currentLayout;
    private int currentLine;
    private float heightOffset;
    private float lastTop;
    private int lineHeight;
    private RectF rect;
    private boolean useRoundRect;
    
    public LinkPath() {
        this.lastTop = -1.0f;
        this.allowReset = true;
    }
    
    public LinkPath(final boolean useRoundRect) {
        this.lastTop = -1.0f;
        this.allowReset = true;
        this.useRoundRect = useRoundRect;
    }
    
    public void addRect(float spacingAdd, float n, float n2, float n3, final Path$Direction path$Direction) {
        final float heightOffset = this.heightOffset;
        final float n4 = n + heightOffset;
        n3 += heightOffset;
        n = this.lastTop;
        if (n == -1.0f) {
            this.lastTop = n4;
        }
        else if (n != n4) {
            this.lastTop = n4;
            ++this.currentLine;
        }
        n = this.currentLayout.getLineRight(this.currentLine);
        final float lineLeft = this.currentLayout.getLineLeft(this.currentLine);
        if (spacingAdd < n) {
            if (spacingAdd > lineLeft || n2 > lineLeft) {
                if (n2 <= n) {
                    n = n2;
                }
                if (spacingAdd < lineLeft) {
                    n2 = lineLeft;
                }
                else {
                    n2 = spacingAdd;
                }
                final int sdk_INT = Build$VERSION.SDK_INT;
                spacingAdd = 0.0f;
                final float n5 = 0.0f;
                if (sdk_INT >= 28) {
                    spacingAdd = n3;
                    if (n3 - n4 > this.lineHeight) {
                        final float heightOffset2 = this.heightOffset;
                        spacingAdd = n5;
                        if (n3 != this.currentLayout.getHeight()) {
                            spacingAdd = this.currentLayout.getLineBottom(this.currentLine) - this.currentLayout.getSpacingAdd();
                        }
                        spacingAdd += heightOffset2;
                    }
                }
                else {
                    if (n3 != this.currentLayout.getHeight()) {
                        spacingAdd = this.currentLayout.getSpacingAdd();
                    }
                    spacingAdd = n3 - spacingAdd;
                }
                final int baselineShift = this.baselineShift;
                float n6;
                if (baselineShift < 0) {
                    n6 = spacingAdd + baselineShift;
                    n3 = n4;
                }
                else {
                    n3 = n4;
                    n6 = spacingAdd;
                    if (baselineShift > 0) {
                        n3 = n4 + baselineShift;
                        n6 = spacingAdd;
                    }
                }
                if (this.useRoundRect) {
                    if (this.rect == null) {
                        this.rect = new RectF();
                    }
                    this.rect.set(n2 - AndroidUtilities.dp(4.0f), n3, n + AndroidUtilities.dp(4.0f), n6);
                    super.addRoundRect(this.rect, (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(4.0f), path$Direction);
                }
                else {
                    super.addRect(n2, n3, n, n6, path$Direction);
                }
            }
        }
    }
    
    public boolean isUsingRoundRect() {
        return this.useRoundRect;
    }
    
    public void reset() {
        if (!this.allowReset) {
            return;
        }
        super.reset();
    }
    
    public void setAllowReset(final boolean allowReset) {
        this.allowReset = allowReset;
    }
    
    public void setBaselineShift(final int baselineShift) {
        this.baselineShift = baselineShift;
    }
    
    public void setCurrentLayout(final StaticLayout currentLayout, int lineCount, final float heightOffset) {
        this.currentLayout = currentLayout;
        this.currentLine = currentLayout.getLineForOffset(lineCount);
        this.lastTop = -1.0f;
        this.heightOffset = heightOffset;
        if (Build$VERSION.SDK_INT >= 28) {
            lineCount = currentLayout.getLineCount();
            if (lineCount > 0) {
                --lineCount;
                this.lineHeight = currentLayout.getLineBottom(lineCount) - currentLayout.getLineTop(lineCount);
            }
        }
    }
    
    public void setUseRoundRect(final boolean useRoundRect) {
        this.useRoundRect = useRoundRect;
    }
}
