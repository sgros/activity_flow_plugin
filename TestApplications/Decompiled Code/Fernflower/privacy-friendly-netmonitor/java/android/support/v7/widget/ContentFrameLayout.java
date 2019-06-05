package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

public class ContentFrameLayout extends FrameLayout {
   private ContentFrameLayout.OnAttachListener mAttachListener;
   private final Rect mDecorPadding;
   private TypedValue mFixedHeightMajor;
   private TypedValue mFixedHeightMinor;
   private TypedValue mFixedWidthMajor;
   private TypedValue mFixedWidthMinor;
   private TypedValue mMinWidthMajor;
   private TypedValue mMinWidthMinor;

   public ContentFrameLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ContentFrameLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ContentFrameLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mDecorPadding = new Rect();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void dispatchFitSystemWindows(Rect var1) {
      this.fitSystemWindows(var1);
   }

   public TypedValue getFixedHeightMajor() {
      if (this.mFixedHeightMajor == null) {
         this.mFixedHeightMajor = new TypedValue();
      }

      return this.mFixedHeightMajor;
   }

   public TypedValue getFixedHeightMinor() {
      if (this.mFixedHeightMinor == null) {
         this.mFixedHeightMinor = new TypedValue();
      }

      return this.mFixedHeightMinor;
   }

   public TypedValue getFixedWidthMajor() {
      if (this.mFixedWidthMajor == null) {
         this.mFixedWidthMajor = new TypedValue();
      }

      return this.mFixedWidthMajor;
   }

   public TypedValue getFixedWidthMinor() {
      if (this.mFixedWidthMinor == null) {
         this.mFixedWidthMinor = new TypedValue();
      }

      return this.mFixedWidthMinor;
   }

   public TypedValue getMinWidthMajor() {
      if (this.mMinWidthMajor == null) {
         this.mMinWidthMajor = new TypedValue();
      }

      return this.mMinWidthMajor;
   }

   public TypedValue getMinWidthMinor() {
      if (this.mMinWidthMinor == null) {
         this.mMinWidthMinor = new TypedValue();
      }

      return this.mMinWidthMinor;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.mAttachListener != null) {
         this.mAttachListener.onAttachedFromWindow();
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.mAttachListener != null) {
         this.mAttachListener.onDetachedFromWindow();
      }

   }

   protected void onMeasure(int var1, int var2) {
      DisplayMetrics var3 = this.getContext().getResources().getDisplayMetrics();
      int var4 = var3.widthPixels;
      int var5 = var3.heightPixels;
      boolean var6 = true;
      boolean var13;
      if (var4 < var5) {
         var13 = true;
      } else {
         var13 = false;
      }

      int var7;
      int var8;
      TypedValue var9;
      int var10;
      boolean var11;
      label92: {
         var7 = MeasureSpec.getMode(var1);
         var8 = MeasureSpec.getMode(var2);
         if (var7 == Integer.MIN_VALUE) {
            if (var13) {
               var9 = this.mFixedWidthMinor;
            } else {
               var9 = this.mFixedWidthMajor;
            }

            if (var9 != null && var9.type != 0) {
               if (var9.type == 5) {
                  var5 = (int)var9.getDimension(var3);
               } else if (var9.type == 6) {
                  var5 = (int)var9.getFraction((float)var3.widthPixels, (float)var3.widthPixels);
               } else {
                  var5 = 0;
               }

               if (var5 > 0) {
                  var10 = MeasureSpec.makeMeasureSpec(Math.min(var5 - (this.mDecorPadding.left + this.mDecorPadding.right), MeasureSpec.getSize(var1)), 1073741824);
                  var11 = true;
                  break label92;
               }
            }
         }

         var11 = false;
         var10 = var1;
      }

      var5 = var2;
      if (var8 == Integer.MIN_VALUE) {
         if (var13) {
            var9 = this.mFixedHeightMajor;
         } else {
            var9 = this.mFixedHeightMinor;
         }

         var5 = var2;
         if (var9 != null) {
            var5 = var2;
            if (var9.type != 0) {
               if (var9.type == 5) {
                  var1 = (int)var9.getDimension(var3);
               } else if (var9.type == 6) {
                  var1 = (int)var9.getFraction((float)var3.heightPixels, (float)var3.heightPixels);
               } else {
                  var1 = 0;
               }

               var5 = var2;
               if (var1 > 0) {
                  var5 = MeasureSpec.makeMeasureSpec(Math.min(var1 - (this.mDecorPadding.top + this.mDecorPadding.bottom), MeasureSpec.getSize(var2)), 1073741824);
               }
            }
         }
      }

      boolean var12;
      label82: {
         super.onMeasure(var10, var5);
         var8 = this.getMeasuredWidth();
         var10 = MeasureSpec.makeMeasureSpec(var8, 1073741824);
         if (!var11 && var7 == Integer.MIN_VALUE) {
            if (var13) {
               var9 = this.mMinWidthMinor;
            } else {
               var9 = this.mMinWidthMajor;
            }

            if (var9 != null && var9.type != 0) {
               if (var9.type == 5) {
                  var1 = (int)var9.getDimension(var3);
               } else if (var9.type == 6) {
                  var1 = (int)var9.getFraction((float)var3.widthPixels, (float)var3.widthPixels);
               } else {
                  var1 = 0;
               }

               var2 = var1;
               if (var1 > 0) {
                  var2 = var1 - (this.mDecorPadding.left + this.mDecorPadding.right);
               }

               if (var8 < var2) {
                  var1 = MeasureSpec.makeMeasureSpec(var2, 1073741824);
                  var12 = var6;
                  break label82;
               }
            }
         }

         var12 = false;
         var1 = var10;
      }

      if (var12) {
         super.onMeasure(var1, var5);
      }

   }

   public void setAttachListener(ContentFrameLayout.OnAttachListener var1) {
      this.mAttachListener = var1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setDecorPadding(int var1, int var2, int var3, int var4) {
      this.mDecorPadding.set(var1, var2, var3, var4);
      if (ViewCompat.isLaidOut(this)) {
         this.requestLayout();
      }

   }

   public interface OnAttachListener {
      void onAttachedFromWindow();

      void onDetachedFromWindow();
   }
}
