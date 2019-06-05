package android.support.constraint.solver;

import java.util.Arrays;

public class SolverVariable {
   private static int uniqueConstantId;
   private static int uniqueErrorId;
   private static int uniqueId;
   private static int uniqueSlackId;
   private static int uniqueUnrestrictedId;
   public float computedValue;
   int definitionId = -1;
   public int id = -1;
   ArrayRow[] mClientEquations = new ArrayRow[8];
   int mClientEquationsCount = 0;
   private String mName;
   SolverVariable.Type mType;
   public int strength = 0;
   float[] strengthVector = new float[7];
   public int usageInRowCount = 0;

   public SolverVariable(SolverVariable.Type var1, String var2) {
      this.mType = var1;
   }

   static void increaseErrorId() {
      ++uniqueErrorId;
   }

   public final void addToRow(ArrayRow var1) {
      for(int var2 = 0; var2 < this.mClientEquationsCount; ++var2) {
         if (this.mClientEquations[var2] == var1) {
            return;
         }
      }

      if (this.mClientEquationsCount >= this.mClientEquations.length) {
         this.mClientEquations = (ArrayRow[])Arrays.copyOf(this.mClientEquations, this.mClientEquations.length * 2);
      }

      this.mClientEquations[this.mClientEquationsCount] = var1;
      ++this.mClientEquationsCount;
   }

   public final void removeFromRow(ArrayRow var1) {
      int var2 = this.mClientEquationsCount;
      int var3 = 0;

      for(int var4 = 0; var4 < var2; ++var4) {
         if (this.mClientEquations[var4] == var1) {
            while(var3 < var2 - var4 - 1) {
               ArrayRow[] var6 = this.mClientEquations;
               int var5 = var4 + var3;
               var6[var5] = this.mClientEquations[var5 + 1];
               ++var3;
            }

            --this.mClientEquationsCount;
            return;
         }
      }

   }

   public void reset() {
      this.mName = null;
      this.mType = SolverVariable.Type.UNKNOWN;
      this.strength = 0;
      this.id = -1;
      this.definitionId = -1;
      this.computedValue = 0.0F;
      this.mClientEquationsCount = 0;
      this.usageInRowCount = 0;
   }

   public void setType(SolverVariable.Type var1, String var2) {
      this.mType = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("");
      var1.append(this.mName);
      return var1.toString();
   }

   public final void updateReferencesWithNewDefinition(ArrayRow var1) {
      int var2 = this.mClientEquationsCount;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.mClientEquations[var3].variables.updateFromRow(this.mClientEquations[var3], var1, false);
      }

      this.mClientEquationsCount = 0;
   }

   public static enum Type {
      CONSTANT,
      ERROR,
      SLACK,
      UNKNOWN,
      UNRESTRICTED;
   }
}
