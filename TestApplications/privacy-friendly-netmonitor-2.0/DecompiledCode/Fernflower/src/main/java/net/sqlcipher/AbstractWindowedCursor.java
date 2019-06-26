package net.sqlcipher;

import android.database.CharArrayBuffer;
import java.util.HashMap;

public abstract class AbstractWindowedCursor extends AbstractCursor {
   protected CursorWindow mWindow;

   protected void checkPosition() {
      super.checkPosition();
      if (this.mWindow == null) {
         throw new StaleDataException("Access closed cursor");
      }
   }

   public void copyStringToBuffer(int var1, CharArrayBuffer var2) {
      this.checkPosition();
      HashMap var3 = this.mUpdatedRows;
      synchronized(var3){}

      label136: {
         Throwable var10000;
         boolean var10001;
         label131: {
            try {
               if (this.isFieldUpdated(var1)) {
                  super.copyStringToBuffer(var1, var2);
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label131;
            }

            label128:
            try {
               break label136;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label128;
            }
         }

         while(true) {
            Throwable var16 = var10000;

            try {
               throw var16;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               continue;
            }
         }
      }

      this.mWindow.copyStringToBuffer(this.mPos, var1, var2);
   }

   public byte[] getBlob(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.isFieldUpdated(var1)) {
               byte[] var16 = (byte[])this.getUpdatedField(var1);
               return var16;
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label137;
         }

         return this.mWindow.getBlob(this.mPos, var1);
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public double getDouble(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.isFieldUpdated(var1)) {
               double var3 = ((Number)this.getUpdatedField(var1)).doubleValue();
               return var3;
            }
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label137;
         }

         return this.mWindow.getDouble(this.mPos, var1);
      }

      while(true) {
         Throwable var5 = var10000;

         try {
            throw var5;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            continue;
         }
      }
   }

   public float getFloat(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.isFieldUpdated(var1)) {
               float var3 = ((Number)this.getUpdatedField(var1)).floatValue();
               return var3;
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label137;
         }

         return this.mWindow.getFloat(this.mPos, var1);
      }

      while(true) {
         Throwable var4 = var10000;

         try {
            throw var4;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            continue;
         }
      }
   }

   public int getInt(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.isFieldUpdated(var1)) {
               var1 = ((Number)this.getUpdatedField(var1)).intValue();
               return var1;
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label137;
         }

         return this.mWindow.getInt(this.mPos, var1);
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public long getLong(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.isFieldUpdated(var1)) {
               long var3 = ((Number)this.getUpdatedField(var1)).longValue();
               return var3;
            }
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label137;
         }

         return this.mWindow.getLong(this.mPos, var1);
      }

      while(true) {
         Throwable var5 = var10000;

         try {
            throw var5;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            continue;
         }
      }
   }

   public short getShort(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.isFieldUpdated(var1)) {
               short var3 = ((Number)this.getUpdatedField(var1)).shortValue();
               return var3;
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label137;
         }

         return this.mWindow.getShort(this.mPos, var1);
      }

      while(true) {
         Throwable var4 = var10000;

         try {
            throw var4;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            continue;
         }
      }
   }

   public String getString(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.isFieldUpdated(var1)) {
               String var16 = (String)this.getUpdatedField(var1);
               return var16;
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label137;
         }

         return this.mWindow.getString(this.mPos, var1);
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public int getType(int var1) {
      this.checkPosition();
      return this.mWindow.getType(this.mPos, var1);
   }

   public CursorWindow getWindow() {
      return this.mWindow;
   }

   public boolean hasWindow() {
      boolean var1;
      if (this.mWindow != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isBlob(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label329: {
         Object var3;
         label330: {
            try {
               if (this.isFieldUpdated(var1)) {
                  var3 = this.getUpdatedField(var1);
                  break label330;
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label329;
            }

            try {
               ;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label329;
            }

            return this.mWindow.isBlob(this.mPos, var1);
         }

         boolean var4;
         label313: {
            label312: {
               if (var3 != null) {
                  try {
                     if (!(var3 instanceof byte[])) {
                        break label312;
                     }
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label329;
                  }
               }

               var4 = true;
               break label313;
            }

            var4 = false;
         }

         label305:
         try {
            return var4;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label305;
         }
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isFloat(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label347: {
         label348: {
            Object var3;
            try {
               if (!this.isFieldUpdated(var1)) {
                  break label348;
               }

               var3 = this.getUpdatedField(var1);
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label347;
            }

            boolean var4;
            label332: {
               label331: {
                  if (var3 != null) {
                     try {
                        if (var3 instanceof Float || var3 instanceof Double) {
                           break label331;
                        }
                     } catch (Throwable var32) {
                        var10000 = var32;
                        var10001 = false;
                        break label347;
                     }
                  }

                  var4 = false;
                  break label332;
               }

               var4 = true;
            }

            try {
               return var4;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label347;
            }
         }

         try {
            ;
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label347;
         }

         return this.mWindow.isFloat(this.mPos, var1);
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isLong(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label347: {
         label348: {
            Object var3;
            try {
               if (!this.isFieldUpdated(var1)) {
                  break label348;
               }

               var3 = this.getUpdatedField(var1);
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label347;
            }

            boolean var4;
            label332: {
               label331: {
                  if (var3 != null) {
                     try {
                        if (var3 instanceof Integer || var3 instanceof Long) {
                           break label331;
                        }
                     } catch (Throwable var32) {
                        var10000 = var32;
                        var10001 = false;
                        break label347;
                     }
                  }

                  var4 = false;
                  break label332;
               }

               var4 = true;
            }

            try {
               return var4;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label347;
            }
         }

         try {
            ;
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label347;
         }

         return this.mWindow.isLong(this.mPos, var1);
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isNull(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label227: {
         boolean var3;
         label231: {
            label225: {
               label224: {
                  try {
                     if (!this.isFieldUpdated(var1)) {
                        break label225;
                     }

                     if (this.getUpdatedField(var1) == null) {
                        break label224;
                     }
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label227;
                  }

                  var3 = false;
                  break label231;
               }

               var3 = true;
               break label231;
            }

            try {
               ;
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label227;
            }

            return this.mWindow.isNull(this.mPos, var1);
         }

         label212:
         try {
            return var3;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label212;
         }
      }

      while(true) {
         Throwable var4 = var10000;

         try {
            throw var4;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isString(int var1) {
      this.checkPosition();
      HashMap var2 = this.mUpdatedRows;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label329: {
         Object var3;
         label330: {
            try {
               if (this.isFieldUpdated(var1)) {
                  var3 = this.getUpdatedField(var1);
                  break label330;
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label329;
            }

            try {
               ;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label329;
            }

            return this.mWindow.isString(this.mPos, var1);
         }

         boolean var4;
         label313: {
            label312: {
               if (var3 != null) {
                  try {
                     if (!(var3 instanceof String)) {
                        break label312;
                     }
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label329;
                  }
               }

               var4 = true;
               break label313;
            }

            var4 = false;
         }

         label305:
         try {
            return var4;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label305;
         }
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public void setWindow(CursorWindow var1) {
      if (this.mWindow != null) {
         this.mWindow.close();
      }

      this.mWindow = var1;
   }
}
