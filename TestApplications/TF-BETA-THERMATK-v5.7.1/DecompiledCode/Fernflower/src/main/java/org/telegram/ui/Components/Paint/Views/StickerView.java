package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Rect;
import org.telegram.ui.Components.Size;

public class StickerView extends EntityView {
   private int anchor;
   private Size baseSize;
   private ImageReceiver centerImage;
   private StickerView.FrameLayoutDrawer containerView;
   private boolean mirrored;
   private Object parentObject;
   private TLRPC.Document sticker;

   public StickerView(Context var1, StickerView var2, Point var3) {
      this(var1, var3, var2.getRotation(), var2.getScale(), var2.baseSize, var2.sticker, var2.parentObject);
      if (var2.mirrored) {
         this.mirror();
      }

   }

   public StickerView(Context var1, Point var2, float var3, float var4, Size var5, TLRPC.Document var6, Object var7) {
      super(var1, var2);
      this.anchor = -1;
      int var8 = 0;
      this.mirrored = false;
      this.centerImage = new ImageReceiver();
      this.setRotation(var3);
      this.setScale(var4);
      this.sticker = var6;
      this.baseSize = var5;

      for(this.parentObject = var7; var8 < var6.attributes.size(); ++var8) {
         TLRPC.DocumentAttribute var10 = (TLRPC.DocumentAttribute)var6.attributes.get(var8);
         if (var10 instanceof TLRPC.TL_documentAttributeSticker) {
            TLRPC.TL_maskCoords var11 = var10.mask_coords;
            if (var11 != null) {
               this.anchor = var11.n;
            }
            break;
         }
      }

      this.containerView = new StickerView.FrameLayoutDrawer(var1);
      this.addView(this.containerView, LayoutHelper.createFrame(-1, -1.0F));
      this.centerImage.setAspectFit(true);
      this.centerImage.setInvalidateAll(true);
      this.centerImage.setParentView(this.containerView);
      TLRPC.PhotoSize var9 = FileLoader.getClosestPhotoSizeWithSize(var6.thumbs, 90);
      this.centerImage.setImage(ImageLocation.getForDocument(var6), (String)null, ImageLocation.getForDocument(var9, var6), (String)null, "webp", var7, 1);
      this.updatePosition();
   }

   public StickerView(Context var1, Point var2, Size var3, TLRPC.Document var4, Object var5) {
      this(var1, var2, 0.0F, 1.0F, var3, var4, var5);
   }

   protected EntityView.SelectionView createSelectionView() {
      return new StickerView.StickerViewSelectionView(this.getContext());
   }

   public int getAnchor() {
      return this.anchor;
   }

   protected Rect getSelectionBounds() {
      float var1 = ((ViewGroup)this.getParent()).getScaleX();
      float var2 = (float)this.getWidth() * (this.getScale() + 0.4F);
      Point var3 = super.position;
      float var4 = var3.x;
      float var5 = var2 / 2.0F;
      float var6 = var3.y;
      var2 *= var1;
      return new Rect((var4 - var5) * var1, (var6 - var5) * var1, var2, var2);
   }

   public TLRPC.Document getSticker() {
      return this.sticker;
   }

   public void mirror() {
      this.mirrored ^= true;
      this.containerView.invalidate();
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec((int)this.baseSize.width, 1073741824), MeasureSpec.makeMeasureSpec((int)this.baseSize.height, 1073741824));
   }

   protected void stickerDraw(Canvas var1) {
      if (this.containerView != null) {
         var1.save();
         if (this.centerImage.getBitmap() != null) {
            if (this.mirrored) {
               var1.scale(-1.0F, 1.0F);
               var1.translate(-this.baseSize.width, 0.0F);
            }

            ImageReceiver var2 = this.centerImage;
            Size var3 = this.baseSize;
            var2.setImageCoords(0, 0, (int)var3.width, (int)var3.height);
            this.centerImage.draw(var1);
         }

         var1.restore();
      }
   }

   protected void updatePosition() {
      Size var1 = this.baseSize;
      float var2 = var1.width / 2.0F;
      float var3 = var1.height / 2.0F;
      this.setX(super.position.x - var2);
      this.setY(super.position.y - var3);
      this.updateSelectionView();
   }

   private class FrameLayoutDrawer extends FrameLayout {
      public FrameLayoutDrawer(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
      }

      protected void onDraw(Canvas var1) {
         StickerView.this.stickerDraw(var1);
      }
   }

   public class StickerViewSelectionView extends EntityView.SelectionView {
      private Paint arcPaint = new Paint(1);
      private RectF arcRect = new RectF();

      public StickerViewSelectionView(Context var2) {
         super(var2);
         this.arcPaint.setColor(-1);
         this.arcPaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
         this.arcPaint.setStyle(Style.STROKE);
      }

      protected void onDraw(Canvas var1) {
         super.onDraw(var1);
         float var2 = (float)AndroidUtilities.dp(1.0F);
         float var3 = (float)AndroidUtilities.dp(4.5F);
         float var4 = var2 + var3 + (float)AndroidUtilities.dp(15.0F);
         float var5 = (float)(this.getWidth() / 2) - var4;
         RectF var6 = this.arcRect;
         var2 = 2.0F * var5 + var4;
         var6.set(var4, var4, var2, var2);

         for(int var7 = 0; var7 < 48; ++var7) {
            var1.drawArc(this.arcRect, (float)var7 * 8.0F, 4.0F, false, this.arcPaint);
         }

         var5 += var4;
         var1.drawCircle(var4, var5, var3, super.dotPaint);
         var1.drawCircle(var4, var5, var3, super.dotStrokePaint);
         var1.drawCircle(var2, var5, var3, super.dotPaint);
         var1.drawCircle(var2, var5, var3, super.dotStrokePaint);
      }

      protected int pointInsideHandle(float var1, float var2) {
         float var3 = (float)AndroidUtilities.dp(1.0F);
         float var4 = (float)AndroidUtilities.dp(19.5F);
         var3 += var4;
         float var5 = (float)this.getHeight();
         float var6 = var3 * 2.0F;
         var5 = (var5 - var6) / 2.0F + var3;
         if (var1 > var3 - var4 && var2 > var5 - var4 && var1 < var3 + var4 && var2 < var5 + var4) {
            return 1;
         } else if (var1 > (float)this.getWidth() - var6 + var3 - var4 && var2 > var5 - var4 && var1 < var3 + ((float)this.getWidth() - var6) + var4 && var2 < var5 + var4) {
            return 2;
         } else {
            var4 = (float)this.getWidth() / 2.0F;
            return Math.pow((double)(var1 - var4), 2.0D) + Math.pow((double)(var2 - var4), 2.0D) < Math.pow((double)var4, 2.0D) ? 3 : 0;
         }
      }
   }
}
