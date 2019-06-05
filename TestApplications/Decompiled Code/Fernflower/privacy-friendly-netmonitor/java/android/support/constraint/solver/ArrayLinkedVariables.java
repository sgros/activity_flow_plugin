package android.support.constraint.solver;

import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables {
   private static final boolean DEBUG = false;
   private static final int NONE = -1;
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

   public final void add(SolverVariable var1, float var2) {
      if (var2 != 0.0F) {
         if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[this.mHead] = var2;
            this.mArrayIndices[this.mHead] = var1.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++this.currentSize;
            if (!this.mDidFillOnce) {
               ++this.mLast;
            }

         } else {
            int var3 = this.mHead;
            int var4 = 0;

            int var5;
            int var6;
            for(var5 = -1; var3 != -1 && var4 < this.currentSize; ++var4) {
               var6 = this.mArrayIndices[var3];
               if (var6 == var1.id) {
                  float[] var7 = this.mArrayValues;
                  var7[var3] += var2;
                  if (this.mArrayValues[var3] == 0.0F) {
                     if (var3 == this.mHead) {
                        this.mHead = this.mArrayNextIndices[var3];
                     } else {
                        this.mArrayNextIndices[var5] = this.mArrayNextIndices[var3];
                     }

                     this.mCache.mIndexedVariables[var6].removeClientEquation(this.mRow);
                     if (this.mDidFillOnce) {
                        this.mLast = var3;
                     }

                     --this.currentSize;
                  }

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
                  var6 = 0;

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

   public final void clear() {
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

   public void display() {
      int var1 = this.currentSize;
      System.out.print("{ ");

      for(int var2 = 0; var2 < var1; ++var2) {
         SolverVariable var3 = this.getVariable(var2);
         if (var3 != null) {
            PrintStream var4 = System.out;
            StringBuilder var5 = new StringBuilder();
            var5.append(var3);
            var5.append(" = ");
            var5.append(this.getVariableValue(var2));
            var5.append(" ");
            var4.print(var5.toString());
         }
      }

      System.out.println(" }");
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

   SolverVariable getPivotCandidate() {
      if (this.candidate != null) {
         return this.candidate;
      } else {
         int var1 = this.mHead;
         int var2 = 0;

         SolverVariable var3;
         SolverVariable var4;
         for(var3 = null; var1 != -1 && var2 < this.currentSize; var3 = var4) {
            var4 = var3;
            if (this.mArrayValues[var1] < 0.0F) {
               label24: {
                  SolverVariable var5 = this.mCache.mIndexedVariables[this.mArrayIndices[var1]];
                  if (var3 != null) {
                     var4 = var3;
                     if (var3.strength >= var5.strength) {
                        break label24;
                     }
                  }

                  var4 = var5;
               }
            }

            var1 = this.mArrayNextIndices[var1];
            ++var2;
         }

         return var3;
      }
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

   boolean hasAtLeastOnePositiveVariable() {
      int var1 = this.mHead;

      for(int var2 = 0; var1 != -1 && var2 < this.currentSize; ++var2) {
         if (this.mArrayValues[var1] > 0.0F) {
            return true;
         }

         var1 = this.mArrayNextIndices[var1];
      }

      return false;
   }

   void invert() {
      int var1 = this.mHead;

      for(int var2 = 0; var1 != -1 && var2 < this.currentSize; ++var2) {
         float[] var3 = this.mArrayValues;
         var3[var1] *= -1.0F;
         var1 = this.mArrayNextIndices[var1];
      }

   }

   SolverVariable pickPivotCandidate() {
      int var1 = this.mHead;
      SolverVariable var2 = null;
      int var3 = 0;

      SolverVariable var4;
      SolverVariable var8;
      for(var4 = null; var1 != -1 && var3 < this.currentSize; var4 = var8) {
         float var6;
         label50: {
            float var5 = this.mArrayValues[var1];
            if (var5 < 0.0F) {
               var6 = var5;
               if (var5 <= -0.001F) {
                  break label50;
               }

               this.mArrayValues[var1] = 0.0F;
            } else {
               var6 = var5;
               if (var5 >= 0.001F) {
                  break label50;
               }

               this.mArrayValues[var1] = 0.0F;
            }

            var6 = 0.0F;
         }

         SolverVariable var7 = var2;
         var8 = var4;
         if (var6 != 0.0F) {
            SolverVariable var9 = this.mCache.mIndexedVariables[this.mArrayIndices[var1]];
            if (var9.mType == SolverVariable.Type.UNRESTRICTED) {
               if (var6 < 0.0F) {
                  return var9;
               }

               var7 = var2;
               var8 = var4;
               if (var2 == null) {
                  var7 = var9;
                  var8 = var4;
               }
            } else {
               var7 = var2;
               var8 = var4;
               if (var6 < 0.0F) {
                  label39: {
                     if (var4 != null) {
                        var7 = var2;
                        var8 = var4;
                        if (var9.strength >= var4.strength) {
                           break label39;
                        }
                     }

                     var8 = var9;
                     var7 = var2;
                  }
               }
            }
         }

         var1 = this.mArrayNextIndices[var1];
         ++var3;
         var2 = var7;
      }

      return var2 != null ? var2 : var4;
   }

   public final void put(SolverVariable var1, float var2) {
      if (var2 == 0.0F) {
         this.remove(var1);
      } else if (this.mHead == -1) {
         this.mHead = 0;
         this.mArrayValues[this.mHead] = var2;
         this.mArrayIndices[this.mHead] = var1.id;
         this.mArrayNextIndices[this.mHead] = -1;
         ++this.currentSize;
         if (!this.mDidFillOnce) {
            ++this.mLast;
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

         ++this.currentSize;
         if (!this.mDidFillOnce) {
            ++this.mLast;
         }

         if (this.currentSize >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
         }

      }
   }

   public final float remove(SolverVariable var1) {
      if (this.candidate == var1) {
         this.candidate = null;
      }

      if (this.mHead == -1) {
         return 0.0F;
      } else {
         int var2 = this.mHead;
         int var3 = 0;

         int var5;
         for(int var4 = -1; var2 != -1 && var3 < this.currentSize; var2 = var5) {
            var5 = this.mArrayIndices[var2];
            if (var5 == var1.id) {
               if (var2 == this.mHead) {
                  this.mHead = this.mArrayNextIndices[var2];
               } else {
                  this.mArrayNextIndices[var4] = this.mArrayNextIndices[var2];
               }

               this.mCache.mIndexedVariables[var5].removeClientEquation(this.mRow);
               --this.currentSize;
               this.mArrayIndices[var2] = -1;
               if (this.mDidFillOnce) {
                  this.mLast = var2;
               }

               return this.mArrayValues[var2];
            }

            var5 = this.mArrayNextIndices[var2];
            ++var3;
            var4 = var2;
         }

         return 0.0F;
      }
   }

   int sizeInBytes() {
      return 0 + 3 * this.mArrayIndices.length * 4 + 36;
   }

   public String toString() {
      String var1 = "";
      int var2 = this.mHead;

      for(int var3 = 0; var2 != -1 && var3 < this.currentSize; ++var3) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var1);
         var4.append(" -> ");
         String var6 = var4.toString();
         StringBuilder var5 = new StringBuilder();
         var5.append(var6);
         var5.append(this.mArrayValues[var2]);
         var5.append(" : ");
         var6 = var5.toString();
         var5 = new StringBuilder();
         var5.append(var6);
         var5.append(this.mCache.mIndexedVariables[this.mArrayIndices[var2]]);
         var1 = var5.toString();
         var2 = this.mArrayNextIndices[var2];
      }

      return var1;
   }

   void updateClientEquations(ArrayRow var1) {
      int var2 = this.mHead;

      for(int var3 = 0; var2 != -1 && var3 < this.currentSize; ++var3) {
         this.mCache.mIndexedVariables[this.mArrayIndices[var2]].addClientEquation(var1);
         var2 = this.mArrayNextIndices[var2];
      }

   }

   void updateFromRow(ArrayRow var1, ArrayRow var2) {
      int var3 = this.mHead;

      label33:
      while(true) {
         for(int var4 = 0; var3 != -1 && var4 < this.currentSize; ++var4) {
            if (this.mArrayIndices[var3] == var2.variable.id) {
               float var5 = this.mArrayValues[var3];
               this.remove(var2.variable);
               ArrayLinkedVariables var6 = var2.variables;
               var4 = var6.mHead;

               for(var3 = 0; var4 != -1 && var3 < var6.currentSize; ++var3) {
                  this.add(this.mCache.mIndexedVariables[var6.mArrayIndices[var4]], var6.mArrayValues[var4] * var5);
                  var4 = var6.mArrayNextIndices[var4];
               }

               var1.constantValue += var2.constantValue * var5;
               var2.variable.removeClientEquation(var1);
               var3 = this.mHead;
               continue label33;
            }

            var3 = this.mArrayNextIndices[var3];
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
               this.remove(var5);
               ArrayRow var7 = var2[var5.definitionId];
               if (!var7.isSimpleDefinition) {
                  ArrayLinkedVariables var8 = var7.variables;
                  var4 = var8.mHead;

                  for(var3 = 0; var4 != -1 && var3 < var8.currentSize; ++var3) {
                     this.add(this.mCache.mIndexedVariables[var8.mArrayIndices[var4]], var8.mArrayValues[var4] * var6);
                     var4 = var8.mArrayNextIndices[var4];
                  }
               }

               var1.constantValue += var7.constantValue * var6;
               var7.variable.removeClientEquation(var1);
               var3 = this.mHead;
               continue label35;
            }

            var3 = this.mArrayNextIndices[var3];
         }

         return;
      }
   }
}
