package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewGroup.LayoutParams;
import java.lang.ref.WeakReference;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class ViewStubCompat extends View {
   private ViewStubCompat.OnInflateListener mInflateListener;
   private int mInflatedId;
   private WeakReference mInflatedViewRef;
   private LayoutInflater mInflater;
   private int mLayoutResource;

   public ViewStubCompat(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ViewStubCompat(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mLayoutResource = 0;
      TypedArray var4 = var1.obtainStyledAttributes(var2, R.styleable.ViewStubCompat, var3, 0);
      this.mInflatedId = var4.getResourceId(R.styleable.ViewStubCompat_android_inflatedId, -1);
      this.mLayoutResource = var4.getResourceId(R.styleable.ViewStubCompat_android_layout, 0);
      this.setId(var4.getResourceId(R.styleable.ViewStubCompat_android_id, -1));
      var4.recycle();
      this.setVisibility(8);
      this.setWillNotDraw(true);
   }

   protected void dispatchDraw(Canvas var1) {
   }

   public void draw(Canvas var1) {
   }

   public int getInflatedId() {
      return this.mInflatedId;
   }

   public LayoutInflater getLayoutInflater() {
      return this.mInflater;
   }

   public int getLayoutResource() {
      return this.mLayoutResource;
   }

   public View inflate() {
      ViewParent var1 = this.getParent();
      if (var1 != null && var1 instanceof ViewGroup) {
         if (this.mLayoutResource != 0) {
            ViewGroup var2 = (ViewGroup)var1;
            LayoutInflater var5;
            if (this.mInflater != null) {
               var5 = this.mInflater;
            } else {
               var5 = LayoutInflater.from(this.getContext());
            }

            View var3 = var5.inflate(this.mLayoutResource, var2, false);
            if (this.mInflatedId != -1) {
               var3.setId(this.mInflatedId);
            }

            int var4 = var2.indexOfChild(this);
            var2.removeViewInLayout(this);
            LayoutParams var6 = this.getLayoutParams();
            if (var6 != null) {
               var2.addView(var3, var4, var6);
            } else {
               var2.addView(var3, var4);
            }

            this.mInflatedViewRef = new WeakReference(var3);
            if (this.mInflateListener != null) {
               this.mInflateListener.onInflate(this, var3);
            }

            return var3;
         } else {
            throw new IllegalArgumentException("ViewStub must have a valid layoutResource");
         }
      } else {
         throw new IllegalStateException("ViewStub must have a non-null ViewGroup viewParent");
      }
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(0, 0);
   }

   public void setInflatedId(int var1) {
      this.mInflatedId = var1;
   }

   public void setLayoutInflater(LayoutInflater var1) {
      this.mInflater = var1;
   }

   public void setLayoutResource(int var1) {
      this.mLayoutResource = var1;
   }

   public void setOnInflateListener(ViewStubCompat.OnInflateListener var1) {
      this.mInflateListener = var1;
   }

   public void setVisibility(int var1) {
      if (this.mInflatedViewRef != null) {
         View var2 = (View)this.mInflatedViewRef.get();
         if (var2 == null) {
            throw new IllegalStateException("setVisibility called on un-referenced view");
         }

         var2.setVisibility(var1);
      } else {
         super.setVisibility(var1);
         if (var1 == 0 || var1 == 4) {
            this.inflate();
         }
      }

   }

   public interface OnInflateListener {
      void onInflate(ViewStubCompat var1, View var2);
   }
}
