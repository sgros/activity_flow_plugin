package android.support.v7.widget;

class RtlSpacingHelper {
   private int mEnd = Integer.MIN_VALUE;
   private int mExplicitLeft = 0;
   private int mExplicitRight = 0;
   private boolean mIsRelative = false;
   private boolean mIsRtl = false;
   private int mLeft = 0;
   private int mRight = 0;
   private int mStart = Integer.MIN_VALUE;

   public int getEnd() {
      int var1;
      if (this.mIsRtl) {
         var1 = this.mLeft;
      } else {
         var1 = this.mRight;
      }

      return var1;
   }

   public int getLeft() {
      return this.mLeft;
   }

   public int getRight() {
      return this.mRight;
   }

   public int getStart() {
      int var1;
      if (this.mIsRtl) {
         var1 = this.mRight;
      } else {
         var1 = this.mLeft;
      }

      return var1;
   }

   public void setAbsolute(int var1, int var2) {
      this.mIsRelative = false;
      if (var1 != Integer.MIN_VALUE) {
         this.mExplicitLeft = var1;
         this.mLeft = var1;
      }

      if (var2 != Integer.MIN_VALUE) {
         this.mExplicitRight = var2;
         this.mRight = var2;
      }

   }

   public void setDirection(boolean var1) {
      if (var1 != this.mIsRtl) {
         this.mIsRtl = var1;
         if (this.mIsRelative) {
            int var2;
            if (var1) {
               if (this.mEnd != Integer.MIN_VALUE) {
                  var2 = this.mEnd;
               } else {
                  var2 = this.mExplicitLeft;
               }

               this.mLeft = var2;
               if (this.mStart != Integer.MIN_VALUE) {
                  var2 = this.mStart;
               } else {
                  var2 = this.mExplicitRight;
               }

               this.mRight = var2;
            } else {
               if (this.mStart != Integer.MIN_VALUE) {
                  var2 = this.mStart;
               } else {
                  var2 = this.mExplicitLeft;
               }

               this.mLeft = var2;
               if (this.mEnd != Integer.MIN_VALUE) {
                  var2 = this.mEnd;
               } else {
                  var2 = this.mExplicitRight;
               }

               this.mRight = var2;
            }
         } else {
            this.mLeft = this.mExplicitLeft;
            this.mRight = this.mExplicitRight;
         }

      }
   }

   public void setRelative(int var1, int var2) {
      this.mStart = var1;
      this.mEnd = var2;
      this.mIsRelative = true;
      if (this.mIsRtl) {
         if (var2 != Integer.MIN_VALUE) {
            this.mLeft = var2;
         }

         if (var1 != Integer.MIN_VALUE) {
            this.mRight = var1;
         }
      } else {
         if (var1 != Integer.MIN_VALUE) {
            this.mLeft = var1;
         }

         if (var2 != Integer.MIN_VALUE) {
            this.mRight = var2;
         }
      }

   }
}
