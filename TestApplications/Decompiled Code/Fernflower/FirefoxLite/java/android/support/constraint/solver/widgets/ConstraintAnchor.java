package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;

public class ConstraintAnchor {
   private int mConnectionCreator;
   private ConstraintAnchor.ConnectionType mConnectionType;
   int mGoneMargin = -1;
   public int mMargin = 0;
   final ConstraintWidget mOwner;
   private ResolutionAnchor mResolutionAnchor = new ResolutionAnchor(this);
   SolverVariable mSolverVariable;
   private ConstraintAnchor.Strength mStrength;
   ConstraintAnchor mTarget;
   final ConstraintAnchor.Type mType;

   public ConstraintAnchor(ConstraintWidget var1, ConstraintAnchor.Type var2) {
      this.mStrength = ConstraintAnchor.Strength.NONE;
      this.mConnectionType = ConstraintAnchor.ConnectionType.RELAXED;
      this.mConnectionCreator = 0;
      this.mOwner = var1;
      this.mType = var2;
   }

   public boolean connect(ConstraintAnchor var1, int var2, int var3, ConstraintAnchor.Strength var4, int var5, boolean var6) {
      if (var1 == null) {
         this.mTarget = null;
         this.mMargin = 0;
         this.mGoneMargin = -1;
         this.mStrength = ConstraintAnchor.Strength.NONE;
         this.mConnectionCreator = 2;
         return true;
      } else if (!var6 && !this.isValidConnection(var1)) {
         return false;
      } else {
         this.mTarget = var1;
         if (var2 > 0) {
            this.mMargin = var2;
         } else {
            this.mMargin = 0;
         }

         this.mGoneMargin = var3;
         this.mStrength = var4;
         this.mConnectionCreator = var5;
         return true;
      }
   }

   public boolean connect(ConstraintAnchor var1, int var2, ConstraintAnchor.Strength var3, int var4) {
      return this.connect(var1, var2, -1, var3, var4, false);
   }

   public int getConnectionCreator() {
      return this.mConnectionCreator;
   }

   public int getMargin() {
      if (this.mOwner.getVisibility() == 8) {
         return 0;
      } else {
         return this.mGoneMargin > -1 && this.mTarget != null && this.mTarget.mOwner.getVisibility() == 8 ? this.mGoneMargin : this.mMargin;
      }
   }

   public ConstraintWidget getOwner() {
      return this.mOwner;
   }

   public ResolutionAnchor getResolutionNode() {
      return this.mResolutionAnchor;
   }

   public SolverVariable getSolverVariable() {
      return this.mSolverVariable;
   }

   public ConstraintAnchor.Strength getStrength() {
      return this.mStrength;
   }

   public ConstraintAnchor getTarget() {
      return this.mTarget;
   }

   public ConstraintAnchor.Type getType() {
      return this.mType;
   }

   public boolean isConnected() {
      boolean var1;
      if (this.mTarget != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isValidConnection(ConstraintAnchor var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         ConstraintAnchor.Type var3 = var1.getType();
         if (var3 == this.mType) {
            return this.mType != ConstraintAnchor.Type.BASELINE || var1.getOwner().hasBaseline() && this.getOwner().hasBaseline();
         } else {
            boolean var4;
            switch(this.mType) {
            case CENTER:
               var4 = var2;
               if (var3 != ConstraintAnchor.Type.BASELINE) {
                  var4 = var2;
                  if (var3 != ConstraintAnchor.Type.CENTER_X) {
                     var4 = var2;
                     if (var3 != ConstraintAnchor.Type.CENTER_Y) {
                        var4 = true;
                     }
                  }
               }

               return var4;
            case LEFT:
            case RIGHT:
               if (var3 != ConstraintAnchor.Type.LEFT && var3 != ConstraintAnchor.Type.RIGHT) {
                  var2 = false;
               } else {
                  var2 = true;
               }

               var4 = var2;
               if (var1.getOwner() instanceof Guideline) {
                  if (!var2 && var3 != ConstraintAnchor.Type.CENTER_X) {
                     var4 = false;
                  } else {
                     var4 = true;
                  }
               }

               return var4;
            case TOP:
            case BOTTOM:
               if (var3 != ConstraintAnchor.Type.TOP && var3 != ConstraintAnchor.Type.BOTTOM) {
                  var4 = false;
               } else {
                  var4 = true;
               }

               var2 = var4;
               if (var1.getOwner() instanceof Guideline) {
                  if (!var4 && var3 != ConstraintAnchor.Type.CENTER_Y) {
                     var2 = false;
                  } else {
                     var2 = true;
                  }
               }

               return var2;
            case BASELINE:
            case CENTER_X:
            case CENTER_Y:
            case NONE:
               return false;
            default:
               throw new AssertionError(this.mType.name());
            }
         }
      }
   }

   public void reset() {
      this.mTarget = null;
      this.mMargin = 0;
      this.mGoneMargin = -1;
      this.mStrength = ConstraintAnchor.Strength.STRONG;
      this.mConnectionCreator = 0;
      this.mConnectionType = ConstraintAnchor.ConnectionType.RELAXED;
      this.mResolutionAnchor.reset();
   }

   public void resetSolverVariable(Cache var1) {
      if (this.mSolverVariable == null) {
         this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, (String)null);
      } else {
         this.mSolverVariable.reset();
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.mOwner.getDebugName());
      var1.append(":");
      var1.append(this.mType.toString());
      return var1.toString();
   }

   public static enum ConnectionType {
      RELAXED,
      STRICT;
   }

   public static enum Strength {
      NONE,
      STRONG,
      WEAK;
   }

   public static enum Type {
      BASELINE,
      BOTTOM,
      CENTER,
      CENTER_X,
      CENTER_Y,
      LEFT,
      NONE,
      RIGHT,
      TOP;
   }
}
