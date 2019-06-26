package android.support.constraint.solver;

import java.util.ArrayList;

public class Goal {
   ArrayList variables = new ArrayList();

   private void initFromSystemErrors(LinearSystem var1) {
      this.variables.clear();

      for(int var2 = 1; var2 < var1.mNumColumns; ++var2) {
         SolverVariable var3 = var1.mCache.mIndexedVariables[var2];

         for(int var4 = 0; var4 < 6; ++var4) {
            var3.strengthVector[var4] = 0.0F;
         }

         var3.strengthVector[var3.strength] = 1.0F;
         if (var3.mType == SolverVariable.Type.ERROR) {
            this.variables.add(var3);
         }
      }

   }

   SolverVariable getPivotCandidate() {
      int var1 = this.variables.size();
      int var2 = 0;
      int var3 = 0;

      SolverVariable var4;
      for(var4 = null; var2 < var1; ++var2) {
         SolverVariable var5 = (SolverVariable)this.variables.get(var2);

         for(int var6 = 5; var6 >= 0; --var6) {
            float var7 = var5.strengthVector[var6];
            SolverVariable var8 = var4;
            int var9 = var3;
            if (var4 == null) {
               var8 = var4;
               var9 = var3;
               if (var7 < 0.0F) {
                  var8 = var4;
                  var9 = var3;
                  if (var6 >= var3) {
                     var8 = var5;
                     var9 = var6;
                  }
               }
            }

            var4 = var8;
            var3 = var9;
            if (var7 > 0.0F) {
               var4 = var8;
               var3 = var9;
               if (var6 > var9) {
                  var4 = null;
                  var3 = var6;
               }
            }
         }
      }

      return var4;
   }

   public String toString() {
      String var1 = "Goal: ";
      int var2 = this.variables.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         SolverVariable var4 = (SolverVariable)this.variables.get(var3);
         StringBuilder var5 = new StringBuilder();
         var5.append(var1);
         var5.append(var4.strengthsToString());
         var1 = var5.toString();
      }

      return var1;
   }

   void updateFromSystem(LinearSystem var1) {
      this.initFromSystemErrors(var1);
      int var2 = this.variables.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         SolverVariable var4 = (SolverVariable)this.variables.get(var3);
         if (var4.definitionId != -1) {
            ArrayLinkedVariables var5 = var1.getRow(var4.definitionId).variables;
            int var6 = var5.currentSize;

            for(int var7 = 0; var7 < var6; ++var7) {
               SolverVariable var8 = var5.getVariable(var7);
               if (var8 != null) {
                  float var9 = var5.getVariableValue(var7);

                  for(int var10 = 0; var10 < 6; ++var10) {
                     float[] var11 = var8.strengthVector;
                     var11[var10] += var4.strengthVector[var10] * var9;
                  }

                  if (!this.variables.contains(var8)) {
                     this.variables.add(var8);
                  }
               }
            }

            var4.clearStrengths();
         }
      }

   }
}
