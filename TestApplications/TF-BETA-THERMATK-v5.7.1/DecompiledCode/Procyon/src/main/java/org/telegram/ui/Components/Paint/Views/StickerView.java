// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint.Views;

import android.graphics.Paint$Style;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.RectF;
import android.graphics.Paint;
import android.widget.FrameLayout;
import android.graphics.Canvas;
import android.view.View$MeasureSpec;
import android.view.ViewGroup;
import org.telegram.ui.Components.Rect;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Point;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Components.Size;

public class StickerView extends EntityView
{
    private int anchor;
    private Size baseSize;
    private ImageReceiver centerImage;
    private FrameLayoutDrawer containerView;
    private boolean mirrored;
    private Object parentObject;
    private TLRPC.Document sticker;
    
    public StickerView(final Context context, final StickerView stickerView, final Point point) {
        this(context, point, stickerView.getRotation(), stickerView.getScale(), stickerView.baseSize, stickerView.sticker, stickerView.parentObject);
        if (stickerView.mirrored) {
            this.mirror();
        }
    }
    
    public StickerView(final Context context, final Point point, final float rotation, final float scale, final Size baseSize, final TLRPC.Document sticker, final Object parentObject) {
        super(context, point);
        this.anchor = -1;
        int i = 0;
        this.mirrored = false;
        this.centerImage = new ImageReceiver();
        this.setRotation(rotation);
        this.setScale(scale);
        this.sticker = sticker;
        this.baseSize = baseSize;
        this.parentObject = parentObject;
        while (i < sticker.attributes.size()) {
            final TLRPC.DocumentAttribute documentAttribute = sticker.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                final TLRPC.TL_maskCoords mask_coords = documentAttribute.mask_coords;
                if (mask_coords != null) {
                    this.anchor = mask_coords.n;
                    break;
                }
                break;
            }
            else {
                ++i;
            }
        }
        this.addView((View)(this.containerView = new FrameLayoutDrawer(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.centerImage.setAspectFit(true);
        this.centerImage.setInvalidateAll(true);
        this.centerImage.setParentView((View)this.containerView);
        this.centerImage.setImage(ImageLocation.getForDocument(sticker), null, ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(sticker.thumbs, 90), sticker), null, "webp", parentObject, 1);
        this.updatePosition();
    }
    
    public StickerView(final Context context, final Point point, final Size size, final TLRPC.Document document, final Object o) {
        this(context, point, 0.0f, 1.0f, size, document, o);
    }
    
    @Override
    protected SelectionView createSelectionView() {
        return new StickerViewSelectionView(this.getContext());
    }
    
    public int getAnchor() {
        return this.anchor;
    }
    
    @Override
    protected Rect getSelectionBounds() {
        final float scaleX = ((ViewGroup)this.getParent()).getScaleX();
        final float n = this.getWidth() * (this.getScale() + 0.4f);
        final Point position = super.position;
        final float x = position.x;
        final float n2 = n / 2.0f;
        final float y = position.y;
        final float n3 = n * scaleX;
        return new Rect((x - n2) * scaleX, (y - n2) * scaleX, n3, n3);
    }
    
    public TLRPC.Document getSticker() {
        return this.sticker;
    }
    
    public void mirror() {
        this.mirrored ^= true;
        this.containerView.invalidate();
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec((int)this.baseSize.width, 1073741824), View$MeasureSpec.makeMeasureSpec((int)this.baseSize.height, 1073741824));
    }
    
    protected void stickerDraw(final Canvas canvas) {
        if (this.containerView == null) {
            return;
        }
        canvas.save();
        if (this.centerImage.getBitmap() != null) {
            if (this.mirrored) {
                canvas.scale(-1.0f, 1.0f);
                canvas.translate(-this.baseSize.width, 0.0f);
            }
            final ImageReceiver centerImage = this.centerImage;
            final Size baseSize = this.baseSize;
            centerImage.setImageCoords(0, 0, (int)baseSize.width, (int)baseSize.height);
            this.centerImage.draw(canvas);
        }
        canvas.restore();
    }
    
    @Override
    protected void updatePosition() {
        final Size baseSize = this.baseSize;
        final float n = baseSize.width / 2.0f;
        final float n2 = baseSize.height / 2.0f;
        this.setX(super.position.x - n);
        this.setY(super.position.y - n2);
        this.updateSelectionView();
    }
    
    private class FrameLayoutDrawer extends FrameLayout
    {
        public FrameLayoutDrawer(final Context context) {
            super(context);
            this.setWillNotDraw(false);
        }
        
        protected void onDraw(final Canvas canvas) {
            StickerView.this.stickerDraw(canvas);
        }
    }
    
    public class StickerViewSelectionView extends SelectionView
    {
        private Paint arcPaint;
        private RectF arcRect;
        
        public StickerViewSelectionView(final Context context) {
            super(context);
            this.arcPaint = new Paint(1);
            this.arcRect = new RectF();
            this.arcPaint.setColor(-1);
            this.arcPaint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
            this.arcPaint.setStyle(Paint$Style.STROKE);
        }
        
        protected void onDraw(final Canvas canvas) {
            super.onDraw(canvas);
            final float n = (float)AndroidUtilities.dp(1.0f);
            final float n2 = (float)AndroidUtilities.dp(4.5f);
            final float n3 = n + n2 + AndroidUtilities.dp(15.0f);
            final float n4 = this.getWidth() / 2 - n3;
            final RectF arcRect = this.arcRect;
            final float n5 = 2.0f * n4 + n3;
            arcRect.set(n3, n3, n5, n5);
            for (int i = 0; i < 48; ++i) {
                canvas.drawArc(this.arcRect, i * 8.0f, 4.0f, false, this.arcPaint);
            }
            final float n6 = n4 + n3;
            canvas.drawCircle(n3, n6, n2, super.dotPaint);
            canvas.drawCircle(n3, n6, n2, super.dotStrokePaint);
            canvas.drawCircle(n5, n6, n2, super.dotPaint);
            canvas.drawCircle(n5, n6, n2, super.dotStrokePaint);
        }
        
        @Override
        protected int pointInsideHandle(final float n, final float n2) {
            final float n3 = (float)AndroidUtilities.dp(1.0f);
            final float n4 = (float)AndroidUtilities.dp(19.5f);
            final float n5 = n3 + n4;
            final float n6 = (float)this.getHeight();
            final float n7 = n5 * 2.0f;
            final float n8 = (n6 - n7) / 2.0f + n5;
            if (n > n5 - n4 && n2 > n8 - n4 && n < n5 + n4 && n2 < n8 + n4) {
                return 1;
            }
            if (n > this.getWidth() - n7 + n5 - n4 && n2 > n8 - n4 && n < n5 + (this.getWidth() - n7) + n4 && n2 < n8 + n4) {
                return 2;
            }
            final float n9 = this.getWidth() / 2.0f;
            if (Math.pow(n - n9, 2.0) + Math.pow(n2 - n9, 2.0) < Math.pow(n9, 2.0)) {
                return 3;
            }
            return 0;
        }
    }
}
