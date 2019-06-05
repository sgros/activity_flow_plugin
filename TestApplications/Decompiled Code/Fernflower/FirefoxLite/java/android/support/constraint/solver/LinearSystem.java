package android.support.constraint.solver;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
   private static int POOL_SIZE;
   public static Metrics sMetrics;
   private int TABLE_SIZE = 32;
   public boolean graphOptimizer;
   private boolean[] mAlreadyTestedCandidates;
   final Cache mCache;
   private LinearSystem.Row mGoal;
   private int mMaxColumns;
   private int mMaxRows;
   int mNumColumns;
   int mNumRows;
   private SolverVariable[] mPoolVariables;
   private int mPoolVariablesCount;
   ArrayRow[] mRows;
   private final LinearSystem.Row mTempGoal;
   private HashMap mVariables = null;
   int mVariablesID = 0;
   private ArrayRow[] tempClientsCopy;

   public LinearSystem() {
      this.mMaxColumns = this.TABLE_SIZE;
      this.mRows = null;
      this.graphOptimizer = false;
      this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
      this.mNumColumns = 1;
      this.mNumRows = 0;
      this.mMaxRows = this.TABLE_SIZE;
      this.mPoolVariables = new SolverVariable[POOL_SIZE];
      this.mPoolVariablesCount = 0;
      this.tempClientsCopy = new ArrayRow[this.TABLE_SIZE];
      this.mRows = new ArrayRow[this.TABLE_SIZE];
      this.releaseRows();
      this.mCache = new Cache();
      this.mGoal = new GoalRow(this.mCache);
      this.mTempGoal = new ArrayRow(this.mCache);
   }

   private SolverVariable acquireSolverVariable(SolverVariable.Type var1, String var2) {
      SolverVariable var3 = (SolverVariable)this.mCache.solverVariablePool.acquire();
      SolverVariable var5;
      if (var3 == null) {
         var3 = new SolverVariable(var1, var2);
         var3.setType(var1, var2);
         var5 = var3;
      } else {
         var3.reset();
         var3.setType(var1, var2);
         var5 = var3;
      }

      if (this.mPoolVariablesCount >= POOL_SIZE) {
         POOL_SIZE *= 2;
         this.mPoolVariables = (SolverVariable[])Arrays.copyOf(this.mPoolVariables, POOL_SIZE);
      }

      SolverVariable[] var6 = this.mPoolVariables;
      int var4 = this.mPoolVariablesCount++;
      var6[var4] = var5;
      return var5;
   }

   private void addError(ArrayRow var1) {
      var1.addError(this, 0);
   }

   private final void addRow(ArrayRow var1) {
      if (this.mRows[this.mNumRows] != null) {
         this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
      }

      this.mRows[this.mNumRows] = var1;
      var1.variable.definitionId = this.mNumRows++;
      var1.variable.updateReferencesWithNewDefinition(var1);
   }

   private void computeValues() {
      for(int var1 = 0; var1 < this.mNumRows; ++var1) {
         ArrayRow var2 = this.mRows[var1];
         var2.variable.computedValue = var2.constantValue;
      }

   }

   public static ArrayRow createRowDimensionPercent(LinearSystem var0, SolverVariable var1, SolverVariable var2, SolverVariable var3, float var4, boolean var5) {
      ArrayRow var6 = var0.createRow();
      if (var5) {
         var0.addError(var6);
      }

      return var6.createRowDimensionPercent(var1, var2, var3, var4);
   }

   private int enforceBFS(LinearSystem.Row var1) throws Exception {
      int var2 = 0;

      boolean var18;
      while(true) {
         if (var2 >= this.mNumRows) {
            var18 = false;
            break;
         }

         if (this.mRows[var2].variable.mType != SolverVariable.Type.UNRESTRICTED && this.mRows[var2].constantValue < 0.0F) {
            var18 = true;
            break;
         }

         ++var2;
      }

      if (var18) {
         boolean var3 = false;

         int var4;
         for(var2 = 0; !var3; var2 = var4) {
            Metrics var16;
            if (sMetrics != null) {
               var16 = sMetrics;
               ++var16.bfs;
            }

            var4 = var2 + 1;
            int var5 = 0;
            int var6 = -1;
            var2 = -1;
            float var7 = Float.MAX_VALUE;

            ArrayRow var9;
            int var13;
            for(int var8 = 0; var5 < this.mNumRows; var8 = var13) {
               var9 = this.mRows[var5];
               int var10;
               int var11;
               float var12;
               if (var9.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                  var10 = var6;
                  var11 = var2;
                  var12 = var7;
                  var13 = var8;
               } else if (var9.isSimpleDefinition) {
                  var10 = var6;
                  var11 = var2;
                  var12 = var7;
                  var13 = var8;
               } else {
                  var10 = var6;
                  var11 = var2;
                  var12 = var7;
                  var13 = var8;
                  if (var9.constantValue < 0.0F) {
                     int var14 = 1;

                     while(true) {
                        var10 = var6;
                        var11 = var2;
                        var12 = var7;
                        var13 = var8;
                        if (var14 >= this.mNumColumns) {
                           break;
                        }

                        SolverVariable var17 = this.mCache.mIndexedVariables[var14];
                        float var15 = var9.variables.get(var17);
                        if (var15 > 0.0F) {
                           var10 = var8;
                           byte var19 = 0;
                           var8 = var2;
                           var13 = var6;
                           var2 = var19;
                           var6 = var10;

                           while(true) {
                              if (var2 >= 7) {
                                 var2 = var8;
                                 var8 = var6;
                                 var6 = var13;
                                 break;
                              }

                              label119: {
                                 var12 = var17.strengthVector[var2] / var15;
                                 if (var12 >= var7 || var2 != var6) {
                                    var10 = var6;
                                    if (var2 <= var6) {
                                       break label119;
                                    }
                                 }

                                 var8 = var14;
                                 var13 = var5;
                                 var7 = var12;
                                 var10 = var2;
                              }

                              ++var2;
                              var6 = var10;
                           }
                        }

                        ++var14;
                     }
                  }
               }

               ++var5;
               var6 = var10;
               var2 = var11;
               var7 = var12;
            }

            if (var6 != -1) {
               var9 = this.mRows[var6];
               var9.variable.definitionId = -1;
               if (sMetrics != null) {
                  var16 = sMetrics;
                  ++var16.pivots;
               }

               var9.pivot(this.mCache.mIndexedVariables[var2]);
               var9.variable.definitionId = var6;
               var9.variable.updateReferencesWithNewDefinition(var9);
            } else {
               var3 = true;
            }

            if (var4 > this.mNumColumns / 2) {
               var3 = true;
            }
         }
      } else {
         var2 = 0;
      }

      return var2;
   }

   public static Metrics getMetrics() {
      return sMetrics;
   }

   private void increaseTableSize() {
      this.TABLE_SIZE *= 2;
      this.mRows = (ArrayRow[])Arrays.copyOf(this.mRows, this.TABLE_SIZE);
      this.mCache.mIndexedVariables = (SolverVariable[])Arrays.copyOf(this.mCache.mIndexedVariables, this.TABLE_SIZE);
      this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
      this.mMaxColumns = this.TABLE_SIZE;
      this.mMaxRows = this.TABLE_SIZE;
      if (sMetrics != null) {
         Metrics var1 = sMetrics;
         ++var1.tableSizeIncrease;
         sMetrics.maxTableSize = Math.max(sMetrics.maxTableSize, (long)this.TABLE_SIZE);
         sMetrics.lastTableSize = sMetrics.maxTableSize;
      }

   }

   private final int optimize(LinearSystem.Row var1, boolean var2) {
      Metrics var3;
      if (sMetrics != null) {
         var3 = sMetrics;
         ++var3.optimize;
      }

      int var4;
      for(var4 = 0; var4 < this.mNumColumns; ++var4) {
         this.mAlreadyTestedCandidates[var4] = false;
      }

      boolean var5 = false;
      var4 = 0;

      while(true) {
         while(!var5) {
            if (sMetrics != null) {
               var3 = sMetrics;
               ++var3.iterations;
            }

            int var6 = var4 + 1;
            if (var6 >= this.mNumColumns * 2) {
               return var6;
            }

            if (var1.getKey() != null) {
               this.mAlreadyTestedCandidates[var1.getKey().id] = true;
            }

            SolverVariable var14 = var1.getPivotCandidate(this, this.mAlreadyTestedCandidates);
            if (var14 != null) {
               if (this.mAlreadyTestedCandidates[var14.id]) {
                  return var6;
               }

               this.mAlreadyTestedCandidates[var14.id] = true;
            }

            if (var14 != null) {
               var4 = 0;
               int var7 = -1;

               float var11;
               for(float var8 = Float.MAX_VALUE; var4 < this.mNumRows; var8 = var11) {
                  ArrayRow var9 = this.mRows[var4];
                  int var10;
                  if (var9.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                     var10 = var7;
                     var11 = var8;
                  } else if (var9.isSimpleDefinition) {
                     var10 = var7;
                     var11 = var8;
                  } else {
                     var10 = var7;
                     var11 = var8;
                     if (var9.hasVariable(var14)) {
                        float var12 = var9.variables.get(var14);
                        var10 = var7;
                        var11 = var8;
                        if (var12 < 0.0F) {
                           var12 = -var9.constantValue / var12;
                           var10 = var7;
                           var11 = var8;
                           if (var12 < var8) {
                              var10 = var4;
                              var11 = var12;
                           }
                        }
                     }
                  }

                  ++var4;
                  var7 = var10;
               }

               if (var7 > -1) {
                  ArrayRow var13 = this.mRows[var7];
                  var13.variable.definitionId = -1;
                  if (sMetrics != null) {
                     Metrics var15 = sMetrics;
                     ++var15.pivots;
                  }

                  var13.pivot(var14);
                  var13.variable.definitionId = var7;
                  var13.variable.updateReferencesWithNewDefinition(var13);
                  var4 = var6;
                  continue;
               }
            }

            var5 = true;
            var4 = var6;
         }

         return var4;
      }
   }

   private void releaseRows() {
      for(int var1 = 0; var1 < this.mRows.length; ++var1) {
         ArrayRow var2 = this.mRows[var1];
         if (var2 != null) {
            this.mCache.arrayRowPool.release(var2);
         }

         this.mRows[var1] = null;
      }

   }

   private final void updateRowFromVariables(ArrayRow var1) {
      if (this.mNumRows > 0) {
         var1.variables.updateFromSystem(var1, this.mRows);
         if (var1.variables.currentSize == 0) {
            var1.isSimpleDefinition = true;
         }
      }

   }

   public void addCenterPoint(ConstraintWidget var1, ConstraintWidget var2, float var3, int var4) {
      SolverVariable var5 = this.createObjectVariable(var1.getAnchor(ConstraintAnchor.Type.LEFT));
      SolverVariable var6 = this.createObjectVariable(var1.getAnchor(ConstraintAnchor.Type.TOP));
      SolverVariable var7 = this.createObjectVariable(var1.getAnchor(ConstraintAnchor.Type.RIGHT));
      SolverVariable var8 = this.createObjectVariable(var1.getAnchor(ConstraintAnchor.Type.BOTTOM));
      SolverVariable var18 = this.createObjectVariable(var2.getAnchor(ConstraintAnchor.Type.LEFT));
      SolverVariable var9 = this.createObjectVariable(var2.getAnchor(ConstraintAnchor.Type.TOP));
      SolverVariable var10 = this.createObjectVariable(var2.getAnchor(ConstraintAnchor.Type.RIGHT));
      SolverVariable var11 = this.createObjectVariable(var2.getAnchor(ConstraintAnchor.Type.BOTTOM));
      ArrayRow var19 = this.createRow();
      double var12 = (double)var3;
      double var14 = Math.sin(var12);
      double var16 = (double)var4;
      var19.createRowWithAngle(var6, var8, var9, var11, (float)(var14 * var16));
      this.addConstraint(var19);
      var19 = this.createRow();
      var19.createRowWithAngle(var5, var7, var18, var10, (float)(Math.cos(var12) * var16));
      this.addConstraint(var19);
   }

   public void addCentering(SolverVariable var1, SolverVariable var2, int var3, float var4, SolverVariable var5, SolverVariable var6, int var7, int var8) {
      ArrayRow var9 = this.createRow();
      var9.createRowCentering(var1, var2, var3, var4, var5, var6, var7);
      if (var8 != 6) {
         var9.addError(this, var8);
      }

      this.addConstraint(var9);
   }

   public void addConstraint(ArrayRow var1) {
      if (var1 != null) {
         Metrics var2;
         if (sMetrics != null) {
            var2 = sMetrics;
            ++var2.constraints;
            if (var1.isSimpleDefinition) {
               var2 = sMetrics;
               ++var2.simpleconstraints;
            }
         }

         if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
         }

         boolean var3 = false;
         boolean var4 = false;
         if (!var1.isSimpleDefinition) {
            this.updateRowFromVariables(var1);
            if (var1.isEmpty()) {
               return;
            }

            var1.ensurePositiveConstant();
            var3 = var4;
            if (var1.chooseSubject(this)) {
               SolverVariable var6 = this.createExtraVariable();
               var1.variable = var6;
               this.addRow(var1);
               this.mTempGoal.initFromRow(var1);
               this.optimize(this.mTempGoal, true);
               if (var6.definitionId == -1) {
                  if (var1.variable == var6) {
                     SolverVariable var5 = var1.pickPivot(var6);
                     if (var5 != null) {
                        if (sMetrics != null) {
                           var2 = sMetrics;
                           ++var2.pivots;
                        }

                        var1.pivot(var5);
                     }
                  }

                  if (!var1.isSimpleDefinition) {
                     var1.variable.updateReferencesWithNewDefinition(var1);
                  }

                  --this.mNumRows;
               }

               var3 = true;
            }

            if (!var1.hasKeyVariable()) {
               return;
            }
         }

         if (!var3) {
            this.addRow(var1);
         }

      }
   }

   public ArrayRow addEquality(SolverVariable var1, SolverVariable var2, int var3, int var4) {
      ArrayRow var5 = this.createRow();
      var5.createRowEquals(var1, var2, var3);
      if (var4 != 6) {
         var5.addError(this, var4);
      }

      this.addConstraint(var5);
      return var5;
   }

   public void addEquality(SolverVariable var1, int var2) {
      int var3 = var1.definitionId;
      ArrayRow var4;
      if (var1.definitionId != -1) {
         var4 = this.mRows[var3];
         if (var4.isSimpleDefinition) {
            var4.constantValue = (float)var2;
         } else if (var4.variables.currentSize == 0) {
            var4.isSimpleDefinition = true;
            var4.constantValue = (float)var2;
         } else {
            var4 = this.createRow();
            var4.createRowEquals(var1, var2);
            this.addConstraint(var4);
         }
      } else {
         var4 = this.createRow();
         var4.createRowDefinition(var1, var2);
         this.addConstraint(var4);
      }

   }

   public void addGreaterBarrier(SolverVariable var1, SolverVariable var2, boolean var3) {
      ArrayRow var4 = this.createRow();
      SolverVariable var5 = this.createSlackVariable();
      var5.strength = 0;
      var4.createRowGreaterThan(var1, var2, var5, 0);
      if (var3) {
         this.addSingleError(var4, (int)(var4.variables.get(var5) * -1.0F), 1);
      }

      this.addConstraint(var4);
   }

   public void addGreaterThan(SolverVariable var1, SolverVariable var2, int var3, int var4) {
      ArrayRow var5 = this.createRow();
      SolverVariable var6 = this.createSlackVariable();
      var6.strength = 0;
      var5.createRowGreaterThan(var1, var2, var6, var3);
      if (var4 != 6) {
         this.addSingleError(var5, (int)(var5.variables.get(var6) * -1.0F), var4);
      }

      this.addConstraint(var5);
   }

   public void addLowerBarrier(SolverVariable var1, SolverVariable var2, boolean var3) {
      ArrayRow var4 = this.createRow();
      SolverVariable var5 = this.createSlackVariable();
      var5.strength = 0;
      var4.createRowLowerThan(var1, var2, var5, 0);
      if (var3) {
         this.addSingleError(var4, (int)(var4.variables.get(var5) * -1.0F), 1);
      }

      this.addConstraint(var4);
   }

   public void addLowerThan(SolverVariable var1, SolverVariable var2, int var3, int var4) {
      ArrayRow var5 = this.createRow();
      SolverVariable var6 = this.createSlackVariable();
      var6.strength = 0;
      var5.createRowLowerThan(var1, var2, var6, var3);
      if (var4 != 6) {
         this.addSingleError(var5, (int)(var5.variables.get(var6) * -1.0F), var4);
      }

      this.addConstraint(var5);
   }

   public void addRatio(SolverVariable var1, SolverVariable var2, SolverVariable var3, SolverVariable var4, float var5, int var6) {
      ArrayRow var7 = this.createRow();
      var7.createRowDimensionRatio(var1, var2, var3, var4, var5);
      if (var6 != 6) {
         var7.addError(this, var6);
      }

      this.addConstraint(var7);
   }

   void addSingleError(ArrayRow var1, int var2, int var3) {
      var1.addSingleError(this.createErrorVariable(var3, (String)null), var2);
   }

   public SolverVariable createErrorVariable(int var1, String var2) {
      if (sMetrics != null) {
         Metrics var3 = sMetrics;
         ++var3.errors;
      }

      if (this.mNumColumns + 1 >= this.mMaxColumns) {
         this.increaseTableSize();
      }

      SolverVariable var4 = this.acquireSolverVariable(SolverVariable.Type.ERROR, var2);
      ++this.mVariablesID;
      ++this.mNumColumns;
      var4.id = this.mVariablesID;
      var4.strength = var1;
      this.mCache.mIndexedVariables[this.mVariablesID] = var4;
      this.mGoal.addError(var4);
      return var4;
   }

   public SolverVariable createExtraVariable() {
      if (sMetrics != null) {
         Metrics var1 = sMetrics;
         ++var1.extravariables;
      }

      if (this.mNumColumns + 1 >= this.mMaxColumns) {
         this.increaseTableSize();
      }

      SolverVariable var2 = this.acquireSolverVariable(SolverVariable.Type.SLACK, (String)null);
      ++this.mVariablesID;
      ++this.mNumColumns;
      var2.id = this.mVariablesID;
      this.mCache.mIndexedVariables[this.mVariablesID] = var2;
      return var2;
   }

   public SolverVariable createObjectVariable(Object var1) {
      SolverVariable var2 = null;
      if (var1 == null) {
         return null;
      } else {
         if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
         }

         if (var1 instanceof ConstraintAnchor) {
            ConstraintAnchor var3 = (ConstraintAnchor)var1;
            var2 = var3.getSolverVariable();
            SolverVariable var4 = var2;
            if (var2 == null) {
               var3.resetSolverVariable(this.mCache);
               var4 = var3.getSolverVariable();
            }

            if (var4.id != -1 && var4.id <= this.mVariablesID) {
               var2 = var4;
               if (this.mCache.mIndexedVariables[var4.id] != null) {
                  return var2;
               }
            }

            if (var4.id != -1) {
               var4.reset();
            }

            ++this.mVariablesID;
            ++this.mNumColumns;
            var4.id = this.mVariablesID;
            var4.mType = SolverVariable.Type.UNRESTRICTED;
            this.mCache.mIndexedVariables[this.mVariablesID] = var4;
            var2 = var4;
         }

         return var2;
      }
   }

   public ArrayRow createRow() {
      ArrayRow var1 = (ArrayRow)this.mCache.arrayRowPool.acquire();
      if (var1 == null) {
         var1 = new ArrayRow(this.mCache);
      } else {
         var1.reset();
      }

      SolverVariable.increaseErrorId();
      return var1;
   }

   public SolverVariable createSlackVariable() {
      if (sMetrics != null) {
         Metrics var1 = sMetrics;
         ++var1.slackvariables;
      }

      if (this.mNumColumns + 1 >= this.mMaxColumns) {
         this.increaseTableSize();
      }

      SolverVariable var2 = this.acquireSolverVariable(SolverVariable.Type.SLACK, (String)null);
      ++this.mVariablesID;
      ++this.mNumColumns;
      var2.id = this.mVariablesID;
      this.mCache.mIndexedVariables[this.mVariablesID] = var2;
      return var2;
   }

   public Cache getCache() {
      return this.mCache;
   }

   public int getObjectVariableValue(Object var1) {
      SolverVariable var2 = ((ConstraintAnchor)var1).getSolverVariable();
      return var2 != null ? (int)(var2.computedValue + 0.5F) : 0;
   }

   public void minimize() throws Exception {
      Metrics var1;
      if (sMetrics != null) {
         var1 = sMetrics;
         ++var1.minimize;
      }

      if (this.graphOptimizer) {
         if (sMetrics != null) {
            var1 = sMetrics;
            ++var1.graphOptimizer;
         }

         boolean var2 = false;
         int var3 = 0;

         boolean var4;
         while(true) {
            if (var3 >= this.mNumRows) {
               var4 = true;
               break;
            }

            if (!this.mRows[var3].isSimpleDefinition) {
               var4 = var2;
               break;
            }

            ++var3;
         }

         if (!var4) {
            this.minimizeGoal(this.mGoal);
         } else {
            if (sMetrics != null) {
               var1 = sMetrics;
               ++var1.fullySolved;
            }

            this.computeValues();
         }
      } else {
         this.minimizeGoal(this.mGoal);
      }

   }

   void minimizeGoal(LinearSystem.Row var1) throws Exception {
      if (sMetrics != null) {
         Metrics var2 = sMetrics;
         ++var2.minimizeGoal;
         sMetrics.maxVariables = Math.max(sMetrics.maxVariables, (long)this.mNumColumns);
         sMetrics.maxRows = Math.max(sMetrics.maxRows, (long)this.mNumRows);
      }

      this.updateRowFromVariables((ArrayRow)var1);
      this.enforceBFS(var1);
      this.optimize(var1, false);
      this.computeValues();
   }

   public void reset() {
      int var1;
      for(var1 = 0; var1 < this.mCache.mIndexedVariables.length; ++var1) {
         SolverVariable var2 = this.mCache.mIndexedVariables[var1];
         if (var2 != null) {
            var2.reset();
         }
      }

      this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
      this.mPoolVariablesCount = 0;
      Arrays.fill(this.mCache.mIndexedVariables, (Object)null);
      if (this.mVariables != null) {
         this.mVariables.clear();
      }

      this.mVariablesID = 0;
      this.mGoal.clear();
      this.mNumColumns = 1;

      for(var1 = 0; var1 < this.mNumRows; ++var1) {
         this.mRows[var1].used = false;
      }

      this.releaseRows();
      this.mNumRows = 0;
   }

   interface Row {
      void addError(SolverVariable var1);

      void clear();

      SolverVariable getKey();

      SolverVariable getPivotCandidate(LinearSystem var1, boolean[] var2);

      void initFromRow(LinearSystem.Row var1);
   }
}
