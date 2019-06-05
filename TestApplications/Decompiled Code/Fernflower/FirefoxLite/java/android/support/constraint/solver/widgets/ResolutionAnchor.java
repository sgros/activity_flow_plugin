package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;

public class ResolutionAnchor extends ResolutionNode {
   float computedValue;
   private ResolutionDimension dimension = null;
   private int dimensionMultiplier = 1;
   ConstraintAnchor myAnchor;
   float offset;
   private ResolutionAnchor opposite;
   private ResolutionDimension oppositeDimension = null;
   private int oppositeDimensionMultiplier = 1;
   private float oppositeOffset;
   float resolvedOffset;
   ResolutionAnchor resolvedTarget;
   ResolutionAnchor target;
   int type = 0;

   public ResolutionAnchor(ConstraintAnchor var1) {
      this.myAnchor = var1;
   }

   void addResolvedValue(LinearSystem var1) {
      SolverVariable var2 = this.myAnchor.getSolverVariable();
      if (this.resolvedTarget == null) {
         var1.addEquality(var2, (int)this.resolvedOffset);
      } else {
         var1.addEquality(var2, var1.createObjectVariable(this.resolvedTarget.myAnchor), (int)this.resolvedOffset, 6);
      }

   }

   public void dependsOn(int var1, ResolutionAnchor var2, int var3) {
      this.type = var1;
      this.target = var2;
      this.offset = (float)var3;
      this.target.addDependent(this);
   }

   public void dependsOn(ResolutionAnchor var1, int var2) {
      this.target = var1;
      this.offset = (float)var2;
      this.target.addDependent(this);
   }

   public void dependsOn(ResolutionAnchor var1, int var2, ResolutionDimension var3) {
      this.target = var1;
      this.target.addDependent(this);
      this.dimension = var3;
      this.dimensionMultiplier = var2;
      this.dimension.addDependent(this);
   }

   public float getResolvedValue() {
      return this.resolvedOffset;
   }

   public void reset() {
      super.reset();
      this.target = null;
      this.offset = 0.0F;
      this.dimension = null;
      this.dimensionMultiplier = 1;
      this.oppositeDimension = null;
      this.oppositeDimensionMultiplier = 1;
      this.resolvedTarget = null;
      this.resolvedOffset = 0.0F;
      this.computedValue = 0.0F;
      this.opposite = null;
      this.oppositeOffset = 0.0F;
      this.type = 0;
   }

   public void resolve() {
      int var1 = this.state;
      boolean var2 = true;
      if (var1 != 1) {
         if (this.type != 4) {
            if (this.dimension != null) {
               if (this.dimension.state != 1) {
                  return;
               }

               this.offset = (float)this.dimensionMultiplier * this.dimension.value;
            }

            if (this.oppositeDimension != null) {
               if (this.oppositeDimension.state != 1) {
                  return;
               }

               this.oppositeOffset = (float)this.oppositeDimensionMultiplier * this.oppositeDimension.value;
            }

            if (this.type == 1 && (this.target == null || this.target.state == 1)) {
               if (this.target == null) {
                  this.resolvedTarget = this;
                  this.resolvedOffset = this.offset;
               } else {
                  this.resolvedTarget = this.target.resolvedTarget;
                  this.resolvedOffset = this.target.resolvedOffset + this.offset;
               }

               this.didResolve();
            } else {
               Metrics var3;
               if (this.type == 2 && this.target != null && this.target.state == 1 && this.opposite != null && this.opposite.target != null && this.opposite.target.state == 1) {
                  if (LinearSystem.getMetrics() != null) {
                     var3 = LinearSystem.getMetrics();
                     ++var3.centerConnectionResolved;
                  }

                  this.resolvedTarget = this.target.resolvedTarget;
                  this.opposite.resolvedTarget = this.opposite.target.resolvedTarget;
                  ConstraintAnchor.Type var13 = this.myAnchor.mType;
                  ConstraintAnchor.Type var4 = ConstraintAnchor.Type.RIGHT;
                  int var5 = 0;
                  boolean var11 = var2;
                  if (var13 != var4) {
                     if (this.myAnchor.mType == ConstraintAnchor.Type.BOTTOM) {
                        var11 = var2;
                     } else {
                        var11 = false;
                     }
                  }

                  float var6;
                  if (var11) {
                     var6 = this.target.resolvedOffset - this.opposite.target.resolvedOffset;
                  } else {
                     var6 = this.opposite.target.resolvedOffset - this.target.resolvedOffset;
                  }

                  float var7;
                  if (this.myAnchor.mType != ConstraintAnchor.Type.LEFT && this.myAnchor.mType != ConstraintAnchor.Type.RIGHT) {
                     var7 = var6 - (float)this.myAnchor.mOwner.getHeight();
                     var6 = this.myAnchor.mOwner.mVerticalBiasPercent;
                  } else {
                     var7 = var6 - (float)this.myAnchor.mOwner.getWidth();
                     var6 = this.myAnchor.mOwner.mHorizontalBiasPercent;
                  }

                  int var8 = this.myAnchor.getMargin();
                  int var12 = this.opposite.myAnchor.getMargin();
                  if (this.myAnchor.getTarget() == this.opposite.myAnchor.getTarget()) {
                     var6 = 0.5F;
                     var12 = 0;
                  } else {
                     var5 = var8;
                  }

                  float var9 = (float)var5;
                  float var10 = (float)var12;
                  var7 = var7 - var9 - var10;
                  if (var11) {
                     this.opposite.resolvedOffset = this.opposite.target.resolvedOffset + var10 + var7 * var6;
                     this.resolvedOffset = this.target.resolvedOffset - var9 - var7 * (1.0F - var6);
                  } else {
                     this.resolvedOffset = this.target.resolvedOffset + var9 + var7 * var6;
                     this.opposite.resolvedOffset = this.opposite.target.resolvedOffset - var10 - var7 * (1.0F - var6);
                  }

                  this.didResolve();
                  this.opposite.didResolve();
               } else if (this.type == 3 && this.target != null && this.target.state == 1 && this.opposite != null && this.opposite.target != null && this.opposite.target.state == 1) {
                  if (LinearSystem.getMetrics() != null) {
                     var3 = LinearSystem.getMetrics();
                     ++var3.matchConnectionResolved;
                  }

                  this.resolvedTarget = this.target.resolvedTarget;
                  this.opposite.resolvedTarget = this.opposite.target.resolvedTarget;
                  this.resolvedOffset = this.target.resolvedOffset + this.offset;
                  this.opposite.resolvedOffset = this.opposite.target.resolvedOffset + this.opposite.offset;
                  this.didResolve();
                  this.opposite.didResolve();
               } else if (this.type == 5) {
                  this.myAnchor.mOwner.resolve();
               }
            }

         }
      }
   }

   public void resolve(ResolutionAnchor var1, float var2) {
      if (this.state == 0 || this.resolvedTarget != var1 && this.resolvedOffset != var2) {
         this.resolvedTarget = var1;
         this.resolvedOffset = var2;
         if (this.state == 1) {
            this.invalidate();
         }

         this.didResolve();
      }

   }

   String sType(int var1) {
      if (var1 == 1) {
         return "DIRECT";
      } else if (var1 == 2) {
         return "CENTER";
      } else if (var1 == 3) {
         return "MATCH";
      } else if (var1 == 4) {
         return "CHAIN";
      } else {
         return var1 == 5 ? "BARRIER" : "UNCONNECTED";
      }
   }

   public void setOpposite(ResolutionAnchor var1, float var2) {
      this.opposite = var1;
      this.oppositeOffset = var2;
   }

   public void setOpposite(ResolutionAnchor var1, int var2, ResolutionDimension var3) {
      this.opposite = var1;
      this.oppositeDimension = var3;
      this.oppositeDimensionMultiplier = var2;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public String toString() {
      StringBuilder var1;
      if (this.state == 1) {
         if (this.resolvedTarget == this) {
            var1 = new StringBuilder();
            var1.append("[");
            var1.append(this.myAnchor);
            var1.append(", RESOLVED: ");
            var1.append(this.resolvedOffset);
            var1.append("]  type: ");
            var1.append(this.sType(this.type));
            return var1.toString();
         } else {
            var1 = new StringBuilder();
            var1.append("[");
            var1.append(this.myAnchor);
            var1.append(", RESOLVED: ");
            var1.append(this.resolvedTarget);
            var1.append(":");
            var1.append(this.resolvedOffset);
            var1.append("] type: ");
            var1.append(this.sType(this.type));
            return var1.toString();
         }
      } else {
         var1 = new StringBuilder();
         var1.append("{ ");
         var1.append(this.myAnchor);
         var1.append(" UNRESOLVED} type: ");
         var1.append(this.sType(this.type));
         return var1.toString();
      }
   }

   public void update() {
      ConstraintAnchor var1 = this.myAnchor.getTarget();
      if (var1 != null) {
         if (var1.getTarget() == this.myAnchor) {
            this.type = 4;
            var1.getResolutionNode().type = 4;
         }

         int var3;
         label16: {
            int var2 = this.myAnchor.getMargin();
            if (this.myAnchor.mType != ConstraintAnchor.Type.RIGHT) {
               var3 = var2;
               if (this.myAnchor.mType != ConstraintAnchor.Type.BOTTOM) {
                  break label16;
               }
            }

            var3 = -var2;
         }

         this.dependsOn(var1.getResolutionNode(), var3);
      }
   }
}
