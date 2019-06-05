package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.constraint.solver.widgets.Helper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import java.util.Arrays;

public abstract class ConstraintHelper extends View {
   protected int mCount = 0;
   protected Helper mHelperWidget = null;
   protected int[] mIds = new int[32];
   private String mReferenceIds;
   protected boolean mUseViewMeasure = false;
   protected Context myContext;

   public ConstraintHelper(Context var1) {
      super(var1);
      this.myContext = var1;
      this.init((AttributeSet)null);
   }

   private void addID(String var1) {
      if (var1 != null) {
         if (this.myContext != null) {
            var1 = var1.trim();

            int var2;
            try {
               var2 = R.id.class.getField(var1).getInt((Object)null);
            } catch (Exception var5) {
               var2 = 0;
            }

            int var4 = var2;
            if (var2 == 0) {
               var4 = this.myContext.getResources().getIdentifier(var1, "id", this.myContext.getPackageName());
            }

            var2 = var4;
            if (var4 == 0) {
               var2 = var4;
               if (this.isInEditMode()) {
                  var2 = var4;
                  if (this.getParent() instanceof ConstraintLayout) {
                     Object var3 = ((ConstraintLayout)this.getParent()).getDesignInformation(0, var1);
                     var2 = var4;
                     if (var3 != null) {
                        var2 = var4;
                        if (var3 instanceof Integer) {
                           var2 = (Integer)var3;
                        }
                     }
                  }
               }
            }

            if (var2 != 0) {
               this.setTag(var2, (Object)null);
            } else {
               StringBuilder var6 = new StringBuilder();
               var6.append("Could not find id of \"");
               var6.append(var1);
               var6.append("\"");
               Log.w("ConstraintHelper", var6.toString());
            }

         }
      }
   }

   private void setIds(String var1) {
      if (var1 != null) {
         int var2 = 0;

         while(true) {
            int var3 = var1.indexOf(44, var2);
            if (var3 == -1) {
               this.addID(var1.substring(var2));
               return;
            }

            this.addID(var1.substring(var2, var3));
            var2 = var3 + 1;
         }
      }
   }

   public int[] getReferencedIds() {
      return Arrays.copyOf(this.mIds, this.mCount);
   }

   protected void init(AttributeSet var1) {
      if (var1 != null) {
         TypedArray var5 = this.getContext().obtainStyledAttributes(var1, R.styleable.ConstraintLayout_Layout);
         int var2 = var5.getIndexCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = var5.getIndex(var3);
            if (var4 == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
               this.mReferenceIds = var5.getString(var4);
               this.setIds(this.mReferenceIds);
            }
         }
      }

   }

   public void onDraw(Canvas var1) {
   }

   protected void onMeasure(int var1, int var2) {
      if (this.mUseViewMeasure) {
         super.onMeasure(var1, var2);
      } else {
         this.setMeasuredDimension(0, 0);
      }

   }

   public void setReferencedIds(int[] var1) {
      int var2 = 0;

      for(this.mCount = 0; var2 < var1.length; ++var2) {
         this.setTag(var1[var2], (Object)null);
      }

   }

   public void setTag(int var1, Object var2) {
      if (this.mCount + 1 > this.mIds.length) {
         this.mIds = Arrays.copyOf(this.mIds, this.mIds.length * 2);
      }

      this.mIds[this.mCount] = var1;
      ++this.mCount;
   }

   public void updatePostLayout(ConstraintLayout var1) {
   }

   public void updatePostMeasure(ConstraintLayout var1) {
   }

   public void updatePreLayout(ConstraintLayout var1) {
      if (this.isInEditMode()) {
         this.setIds(this.mReferenceIds);
      }

      if (this.mHelperWidget != null) {
         this.mHelperWidget.removeAllIds();

         for(int var2 = 0; var2 < this.mCount; ++var2) {
            View var3 = var1.findViewById(this.mIds[var2]);
            if (var3 != null) {
               this.mHelperWidget.add(var1.getViewWidget(var3));
            }
         }

      }
   }

   public void validateParams() {
      if (this.mHelperWidget != null) {
         LayoutParams var1 = this.getLayoutParams();
         if (var1 instanceof ConstraintLayout.LayoutParams) {
            ((ConstraintLayout.LayoutParams)var1).widget = this.mHelperWidget;
         }

      }
   }
}
