package android.support.constraint.solver;

import java.util.Arrays;

public class ArrayLinkedVariables {
   private int ROW_SIZE = 8;
   private SolverVariable candidate = null;
   int currentSize = 0;
   private int[] mArrayIndices;
   private int[] mArrayNextIndices;
   private float[] mArrayValues;
   private final Cache mCache;
   private boolean mDidFillOnce;
   private int mHead;
   private int mLast;
   private final ArrayRow mRow;

   ArrayLinkedVariables(ArrayRow var1, Cache var2) {
      this.mArrayIndices = new int[this.ROW_SIZE];
      this.mArrayNextIndices = new int[this.ROW_SIZE];
      this.mArrayValues = new float[this.ROW_SIZE];
      this.mHead = -1;
      this.mLast = -1;
      this.mDidFillOnce = false;
      this.mRow = var1;
      this.mCache = var2;
   }

   private boolean isNew(SolverVariable var1, LinearSystem var2) {
      int var3 = var1.usageInRowCount;
      boolean var4 = true;
      if (var3 > 1) {
         var4 = false;
      }

      return var4;
   }

   final void add(SolverVariable var1, float var2, boolean var3) {
      if (var2 != 0.0F) {
         if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[this.mHead] = var2;
            this.mArrayIndices[this.mHead] = var1.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++var1.usageInRowCount;
            var1.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
               ++this.mLast;
               if (this.mLast >= this.mArrayIndices.length) {
                  this.mDidFillOnce = true;
                  this.mLast = this.mArrayIndices.length - 1;
               }
            }

         } else {
            int var4 = this.mHead;
            int var5 = 0;

            int var6;
            for(var6 = -1; var4 != -1 && var5 < this.currentSize; ++var5) {
               if (this.mArrayIndices[var4] == var1.id) {
                  float[] var7 = this.mArrayValues;
                  var7[var4] += var2;
                  if (this.mArrayValues[var4] == 0.0F) {
                     if (var4 == this.mHead) {
                        this.mHead = this.mArrayNextIndices[var4];
                     } else {
                        this.mArrayNextIndices[var6] = this.mArrayNextIndices[var4];
                     }

                     if (var3) {
                        var1.removeFromRow(this.mRow);
                     }

                     if (this.mDidFillOnce) {
                        this.mLast = var4;
                     }

                     --var1.usageInRowCount;
                     --this.currentSize;
                  }

                  return;
               }

               if (this.mArrayIndices[var4] < var1.id) {
                  var6 = var4;
               }

               var4 = this.mArrayNextIndices[var4];
            }

            var4 = this.mLast + 1;
            if (this.mDidFillOnce) {
               if (this.mArrayIndices[this.mLast] == -1) {
                  var4 = this.mLast;
               } else {
                  var4 = this.mArrayIndices.length;
               }
            }

            var5 = var4;
            if (var4 >= this.mArrayIndices.length) {
               var5 = var4;
               if (this.currentSize < this.mArrayIndices.length) {
                  int var8 = 0;

                  while(true) {
                     var5 = var4;
                     if (var8 >= this.mArrayIndices.length) {
                        break;
                     }

                     if (this.mArrayIndices[var8] == -1) {
                        var5 = var8;
                        break;
                     }

                     ++var8;
                  }
               }
            }

            var4 = var5;
            if (var5 >= this.mArrayIndices.length) {
               var4 = this.mArrayIndices.length;
               this.ROW_SIZE *= 2;
               this.mDidFillOnce = false;
               this.mLast = var4 - 1;
               this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
               this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
               this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
            }

            this.mArrayIndices[var4] = var1.id;
            this.mArrayValues[var4] = var2;
            if (var6 != -1) {
               this.mArrayNextIndices[var4] = this.mArrayNextIndices[var6];
               this.mArrayNextIndices[var6] = var4;
            } else {
               this.mArrayNextIndices[var4] = this.mHead;
               this.mHead = var4;
            }

            ++var1.usageInRowCount;
            var1.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
               ++this.mLast;
            }

            if (this.mLast >= this.mArrayIndices.length) {
               this.mDidFillOnce = true;
               this.mLast = this.mArrayIndices.length - 1;
            }

         }
      }
   }

   SolverVariable chooseSubject(LinearSystem var1) {
      int var2 = this.mHead;
      SolverVariable var3 = null;
      int var4 = 0;
      SolverVariable var5 = null;
      float var6 = 0.0F;
      boolean var7 = false;
      float var8 = 0.0F;

      boolean var17;
      for(boolean var9 = false; var2 != -1 && var4 < this.currentSize; var9 = var17) {
         float var10;
         SolverVariable var11;
         float var12;
         label68: {
            var10 = this.mArrayValues[var2];
            var11 = this.mCache.mIndexedVariables[this.mArrayIndices[var2]];
            if (var10 < 0.0F) {
               var12 = var10;
               if (var10 <= -0.001F) {
                  break label68;
               }

               this.mArrayValues[var2] = 0.0F;
               var11.removeFromRow(this.mRow);
            } else {
               var12 = var10;
               if (var10 >= 0.001F) {
                  break label68;
               }

               this.mArrayValues[var2] = 0.0F;
               var11.removeFromRow(this.mRow);
            }

            var12 = 0.0F;
         }

         SolverVariable var13 = var3;
         SolverVariable var14 = var5;
         var10 = var6;
         boolean var15 = var7;
         float var16 = var8;
         var17 = var9;
         if (var12 != 0.0F) {
            if (var11.mType == SolverVariable.Type.UNRESTRICTED) {
               label58: {
                  if (var3 == null) {
                     var15 = this.isNew(var11, var1);
                  } else {
                     if (var6 <= var12) {
                        var13 = var3;
                        var14 = var5;
                        var10 = var6;
                        var15 = var7;
                        var16 = var8;
                        var17 = var9;
                        if (!var7) {
                           var13 = var3;
                           var14 = var5;
                           var10 = var6;
                           var15 = var7;
                           var16 = var8;
                           var17 = var9;
                           if (this.isNew(var11, var1)) {
                              var15 = true;
                              var13 = var11;
                              var14 = var5;
                              var10 = var12;
                              var16 = var8;
                              var17 = var9;
                           }
                        }
                        break label58;
                     }

                     var15 = this.isNew(var11, var1);
                  }

                  var13 = var11;
                  var14 = var5;
                  var10 = var12;
                  var16 = var8;
                  var17 = var9;
               }
            } else {
               var13 = var3;
               var14 = var5;
               var10 = var6;
               var15 = var7;
               var16 = var8;
               var17 = var9;
               if (var3 == null) {
                  var13 = var3;
                  var14 = var5;
                  var10 = var6;
                  var15 = var7;
                  var16 = var8;
                  var17 = var9;
                  if (var12 < 0.0F) {
                     label51: {
                        if (var5 == null) {
                           var15 = this.isNew(var11, var1);
                        } else {
                           if (var8 <= var12) {
                              var13 = var3;
                              var14 = var5;
                              var10 = var6;
                              var15 = var7;
                              var16 = var8;
                              var17 = var9;
                              if (!var9) {
                                 var13 = var3;
                                 var14 = var5;
                                 var10 = var6;
                                 var15 = var7;
                                 var16 = var8;
                                 var17 = var9;
                                 if (this.isNew(var11, var1)) {
                                    var17 = true;
                                    var16 = var12;
                                    var15 = var7;
                                    var10 = var6;
                                    var14 = var11;
                                    var13 = var3;
                                 }
                              }
                              break label51;
                           }

                           var15 = this.isNew(var11, var1);
                        }

                        var17 = var15;
                        var13 = var3;
                        var14 = var11;
                        var10 = var6;
                        var15 = var7;
                        var16 = var12;
                     }
                  }
               }
            }
         }

         var2 = this.mArrayNextIndices[var2];
         ++var4;
         var3 = var13;
         var5 = var14;
         var6 = var10;
         var7 = var15;
         var8 = var16;
      }

      return var3 != null ? var3 : var5;
   }

   public final void clear() {
      int var1 = this.mHead;

      for(int var2 = 0; var1 != -1 && var2 < this.currentSize; ++var2) {
         SolverVariable var3 = this.mCache.mIndexedVariables[this.mArrayIndices[var1]];
         if (var3 != null) {
            var3.removeFromRow(this.mRow);
         }

         var1 = this.mArrayNextIndices[var1];
      }

      this.mHead = -1;
      this.mLast = -1;
      this.mDidFillOnce = false;
      this.currentSize = 0;
   }

   final boolean containsKey(SolverVariable var1) {
      if (this.mHead == -1) {
         return false;
      } else {
         int var2 = this.mHead;

         for(int var3 = 0; var2 != -1 && var3 < this.currentSize; ++var3) {
            if (this.mArrayIndices[var2] == var1.id) {
               return true;
            }

            var2 = this.mArrayNextIndices[var2];
         }

         return false;
      }
   }

   void divideByAmount(float var1) {
      int var2 = this.mHead;

      for(int var3 = 0; var2 != -1 && var3 < this.currentSize; ++var3) {
         float[] var4 = this.mArrayValues;
         var4[var2] /= var1;
         var2 = this.mArrayNextIndices[var2];
      }

   }

   public final float get(SolverVariable var1) {
      int var2 = this.mHead;

      for(int var3 = 0; var2 != -1 && var3 < this.currentSize; ++var3) {
         if (this.mArrayIndices[var2] == var1.id) {
            return this.mArrayValues[var2];
         }

         var2 = this.mArrayNextIndices[var2];
      }

      return 0.0F;
   }

   SolverVariable getPivotCandidate(boolean[] var1, SolverVariable var2) {
      int var3 = this.mHead;
      int var4 = 0;
      SolverVariable var5 = null;

      float var8;
      for(float var6 = 0.0F; var3 != -1 && var4 < this.currentSize; var6 = var8) {
         SolverVariable var7 = var5;
         var8 = var6;
         if (this.mArrayValues[var3] < 0.0F) {
            label30: {
               SolverVariable var9 = this.mCache.mIndexedVariables[this.mArrayIndices[var3]];
               if (var1 != null) {
                  var7 = var5;
                  var8 = var6;
                  if (var1[var9.id]) {
                     break label30;
                  }
               }

               var7 = var5;
               var8 = var6;
               if (var9 != var2) {
                  label25: {
                     if (var9.mType != SolverVariable.Type.SLACK) {
                        var7 = var5;
                        var8 = var6;
                        if (var9.mType != SolverVariable.Type.ERROR) {
                           break label25;
                        }
                     }

                     float var10 = this.mArrayValues[var3];
                     var7 = var5;
                     var8 = var6;
                     if (var10 < var6) {
                        var7 = var9;
                        var8 = var10;
                     }
                  }
               }
            }
         }

         var3 = this.mArrayNextIndices[var3];
         ++var4;
         var5 = var7;
      }

      return var5;
   }

   final SolverVariable getVariable(int var1) {
      int var2 = this.mHead;

      for(int var3 = 0; var2 != -1 && var3 < this.currentSize; ++var3) {
         if (var3 == var1) {
            return this.mCache.mIndexedVariables[this.mArrayIndices[var2]];
         }

         var2 = this.mArrayNextIndices[var2];
      }

      return null;
   }

   final float getVariableValue(int var1) {
      int var2 = this.mHead;

      for(int var3 = 0; var2 != -1 && var3 < this.currentSize; ++var3) {
         if (var3 == var1) {
            return this.mArrayValues[var2];
         }

         var2 = this.mArrayNextIndices[var2];
      }

      return 0.0F;
   }

   void invert() {
      int var1 = this.mHead;

      for(int var2 = 0; var1 != -1 && var2 < this.currentSize; ++var2) {
         float[] var3 = this.mArrayValues;
         var3[var1] *= -1.0F;
         var1 = this.mArrayNextIndices[var1];
      }

   }

   public final void put(SolverVariable var1, float var2) {
      if (var2 == 0.0F) {
         this.remove(var1, true);
      } else if (this.mHead == -1) {
         this.mHead = 0;
         this.mArrayValues[this.mHead] = var2;
         this.mArrayIndices[this.mHead] = var1.id;
         this.mArrayNextIndices[this.mHead] = -1;
         ++var1.usageInRowCount;
         var1.addToRow(this.mRow);
         ++this.currentSize;
         if (!this.mDidFillOnce) {
            ++this.mLast;
            if (this.mLast >= this.mArrayIndices.length) {
               this.mDidFillOnce = true;
               this.mLast = this.mArrayIndices.length - 1;
            }
         }

      } else {
         int var3 = this.mHead;
         int var4 = 0;

         int var5;
         for(var5 = -1; var3 != -1 && var4 < this.currentSize; ++var4) {
            if (this.mArrayIndices[var3] == var1.id) {
               this.mArrayValues[var3] = var2;
               return;
            }

            if (this.mArrayIndices[var3] < var1.id) {
               var5 = var3;
            }

            var3 = this.mArrayNextIndices[var3];
         }

         var3 = this.mLast + 1;
         if (this.mDidFillOnce) {
            if (this.mArrayIndices[this.mLast] == -1) {
               var3 = this.mLast;
            } else {
               var3 = this.mArrayIndices.length;
            }
         }

         var4 = var3;
         if (var3 >= this.mArrayIndices.length) {
            var4 = var3;
            if (this.currentSize < this.mArrayIndices.length) {
               int var6 = 0;

               while(true) {
                  var4 = var3;
                  if (var6 >= this.mArrayIndices.length) {
                     break;
                  }

                  if (this.mArrayIndices[var6] == -1) {
                     var4 = var6;
                     break;
                  }

                  ++var6;
               }
            }
         }

         var3 = var4;
         if (var4 >= this.mArrayIndices.length) {
            var3 = this.mArrayIndices.length;
            this.ROW_SIZE *= 2;
            this.mDidFillOnce = false;
            this.mLast = var3 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
         }

         this.mArrayIndices[var3] = var1.id;
         this.mArrayValues[var3] = var2;
         if (var5 != -1) {
            this.mArrayNextIndices[var3] = this.mArrayNextIndices[var5];
            this.mArrayNextIndices[var5] = var3;
         } else {
            this.mArrayNextIndices[var3] = this.mHead;
            this.mHead = var3;
         }

         ++var1.usageInRowCount;
         var1.addToRow(this.mRow);
         ++this.currentSize;
         if (!this.mDidFillOnce) {
            ++this.mLast;
         }

         if (this.currentSize >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
         }

         if (this.mLast >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
            this.mLast = this.mArrayIndices.length - 1;
         }

      }
   }

   public final float remove(SolverVariable var1, boolean var2) {
      if (this.candidate == var1) {
         this.candidate = null;
      }

      if (this.mHead == -1) {
         return 0.0F;
      } else {
         int var3 = this.mHead;
         int var4 = 0;

         int var6;
         for(int var5 = -1; var3 != -1 && var4 < this.currentSize; var3 = var6) {
            if (this.mArrayIndices[var3] == var1.id) {
               if (var3 == this.mHead) {
                  this.mHead = this.mArrayNextIndices[var3];
               } else {
                  this.mArrayNextIndices[var5] = this.mArrayNextIndices[var3];
               }

               if (var2) {
                  var1.removeFromRow(this.mRow);
               }

               --var1.usageInRowCount;
               --this.currentSize;
               this.mArrayIndices[var3] = -1;
               if (this.mDidFillOnce) {
                  this.mLast = var3;
               }

               return this.mArrayValues[var3];
            }

            var6 = this.mArrayNextIndices[var3];
            ++var4;
            var5 = var3;
         }

         return 0.0F;
      }
   }

   public String toString() {
      String var1 = "";
      int var2 = this.mHead;

      for(int var3 = 0; var2 != -1 && var3 < this.currentSize; ++var3) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var1);
         var4.append(" -> ");
         var1 = var4.toString();
         var4 = new StringBuilder();
         var4.append(var1);
         var4.append(this.mArrayValues[var2]);
         var4.append(" : ");
         String var6 = var4.toString();
         StringBuilder var5 = new StringBuilder();
         var5.append(var6);
         var5.append(this.mCache.mIndexedVariables[this.mArrayIndices[var2]]);
         var1 = var5.toString();
         var2 = this.mArrayNextIndices[var2];
      }

      return var1;
   }

   final void updateFromRow(ArrayRow var1, ArrayRow var2, boolean var3) {
      int var4 = this.mHead;

      label37:
      while(true) {
         for(int var5 = 0; var4 != -1 && var5 < this.currentSize; ++var5) {
            if (this.mArrayIndices[var4] == var2.variable.id) {
               float var6 = this.mArrayValues[var4];
               this.remove(var2.variable, var3);
               ArrayLinkedVariables var7 = (ArrayLinkedVariables)var2.variables;
               var5 = var7.mHead;

               for(var4 = 0; var5 != -1 && var4 < var7.currentSize; ++var4) {
                  this.add(this.mCache.mIndexedVariables[var7.mArrayIndices[var5]], var7.mArrayValues[var5] * var6, var3);
                  var5 = var7.mArrayNextIndices[var5];
               }

               var1.constantValue += var2.constantValue * var6;
               if (var3) {
                  var2.variable.removeFromRow(var1);
               }

               var4 = this.mHead;
               continue label37;
            }

            var4 = this.mArrayNextIndices[var4];
         }

         return;
      }
   }

   void updateFromSystem(ArrayRow var1, ArrayRow[] var2) {
      int var3 = this.mHead;

      label35:
      while(true) {
         for(int var4 = 0; var3 != -1 && var4 < this.currentSize; ++var4) {
            SolverVariable var5 = this.mCache.mIndexedVariables[this.mArrayIndices[var3]];
            if (var5.definitionId != -1) {
               float var6 = this.mArrayValues[var3];
               this.remove(var5, true);
               ArrayRow var8 = var2[var5.definitionId];
               if (!var8.isSimpleDefinition) {
                  ArrayLinkedVariables var7 = (ArrayLinkedVariables)var8.variables;
                  var4 = var7.mHead;

                  for(var3 = 0; var4 != -1 && var3 < var7.currentSize; ++var3) {
                     this.add(this.mCache.mIndexedVariables[var7.mArrayIndices[var4]], var7.mArrayValues[var4] * var6, true);
                     var4 = var7.mArrayNextIndices[var4];
                  }
               }

               var1.constantValue += var8.constantValue * var6;
               var8.variable.removeFromRow(var1);
               var3 = this.mHead;
               continue label35;
            }

            var3 = this.mArrayNextIndices[var3];
         }

         return;
      }
   }
}
