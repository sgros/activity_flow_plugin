package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.util.Pair;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ArticleViewer;

public class TableLayout extends View {
   public static final int ALIGN_BOUNDS = 0;
   public static final int ALIGN_MARGINS = 1;
   public static final TableLayout.Alignment BASELINE;
   public static final TableLayout.Alignment BOTTOM;
   private static final int CAN_STRETCH = 2;
   public static final TableLayout.Alignment CENTER;
   private static final int DEFAULT_ALIGNMENT_MODE = 1;
   private static final int DEFAULT_COUNT = Integer.MIN_VALUE;
   private static final boolean DEFAULT_ORDER_PRESERVED = true;
   private static final int DEFAULT_ORIENTATION = 0;
   private static final boolean DEFAULT_USE_DEFAULT_MARGINS = false;
   public static final TableLayout.Alignment END;
   public static final TableLayout.Alignment FILL;
   public static final int HORIZONTAL = 0;
   private static final int INFLEXIBLE = 0;
   private static final TableLayout.Alignment LEADING = new TableLayout.Alignment() {
      public int getAlignmentValue(TableLayout.Child var1, int var2) {
         return 0;
      }

      int getGravityOffset(TableLayout.Child var1, int var2) {
         return 0;
      }
   };
   public static final TableLayout.Alignment LEFT;
   static final int MAX_SIZE = 100000;
   public static final TableLayout.Alignment RIGHT;
   public static final TableLayout.Alignment START;
   public static final TableLayout.Alignment TOP;
   private static final TableLayout.Alignment TRAILING = new TableLayout.Alignment() {
      public int getAlignmentValue(TableLayout.Child var1, int var2) {
         return var2;
      }

      int getGravityOffset(TableLayout.Child var1, int var2) {
         return var2;
      }
   };
   public static final int UNDEFINED = Integer.MIN_VALUE;
   static final TableLayout.Alignment UNDEFINED_ALIGNMENT = new TableLayout.Alignment() {
      public int getAlignmentValue(TableLayout.Child var1, int var2) {
         return Integer.MIN_VALUE;
      }

      int getGravityOffset(TableLayout.Child var1, int var2) {
         return Integer.MIN_VALUE;
      }
   };
   static final int UNINITIALIZED_HASH = 0;
   public static final int VERTICAL = 1;
   private Path backgroundPath = new Path();
   private ArrayList cellsToFixHeight = new ArrayList();
   private ArrayList childrens = new ArrayList();
   private int colCount;
   private TableLayout.TableLayoutDelegate delegate;
   private boolean drawLines;
   private boolean isRtl;
   private boolean isStriped;
   private int itemPaddingLeft = AndroidUtilities.dp(8.0F);
   private int itemPaddingTop = AndroidUtilities.dp(7.0F);
   private Path linePath = new Path();
   private int mAlignmentMode = 1;
   private int mDefaultGap;
   private final TableLayout.Axis mHorizontalAxis = new TableLayout.Axis(true);
   private int mLastLayoutParamsHashCode = 0;
   private int mOrientation = 0;
   private boolean mUseDefaultMargins = false;
   private final TableLayout.Axis mVerticalAxis = new TableLayout.Axis(false);
   private float[] radii = new float[8];
   private RectF rect = new RectF();
   private ArrayList rowSpans = new ArrayList();

   static {
      TableLayout.Alignment var0 = LEADING;
      TOP = var0;
      TableLayout.Alignment var1 = TRAILING;
      BOTTOM = var1;
      START = var0;
      END = var1;
      LEFT = createSwitchingAlignment(START);
      RIGHT = createSwitchingAlignment(END);
      CENTER = new TableLayout.Alignment() {
         public int getAlignmentValue(TableLayout.Child var1, int var2) {
            return var2 >> 1;
         }

         int getGravityOffset(TableLayout.Child var1, int var2) {
            return var2 >> 1;
         }
      };
      BASELINE = new TableLayout.Alignment() {
         public int getAlignmentValue(TableLayout.Child var1, int var2) {
            return Integer.MIN_VALUE;
         }

         public TableLayout.Bounds getBounds() {
            return new TableLayout.Bounds() {
               private int size;

               protected int getOffset(TableLayout var1, TableLayout.Child var2, TableLayout.Alignment var3, int var4, boolean var5) {
                  return Math.max(0, super.getOffset(var1, var2, var3, var4, var5));
               }

               protected void include(int var1, int var2) {
                  super.include(var1, var2);
                  this.size = Math.max(this.size, var1 + var2);
               }

               protected void reset() {
                  super.reset();
                  this.size = Integer.MIN_VALUE;
               }

               protected int size(boolean var1) {
                  return Math.max(super.size(var1), this.size);
               }
            };
         }

         int getGravityOffset(TableLayout.Child var1, int var2) {
            return 0;
         }
      };
      FILL = new TableLayout.Alignment() {
         public int getAlignmentValue(TableLayout.Child var1, int var2) {
            return Integer.MIN_VALUE;
         }

         int getGravityOffset(TableLayout.Child var1, int var2) {
            return 0;
         }

         public int getSizeInCell(TableLayout.Child var1, int var2, int var3) {
            return var3;
         }
      };
   }

   public TableLayout(Context var1, TableLayout.TableLayoutDelegate var2) {
      super(var1);
      this.setRowCount(Integer.MIN_VALUE);
      this.setColumnCount(Integer.MIN_VALUE);
      this.setOrientation(0);
      this.setUseDefaultMargins(false);
      this.setAlignmentMode(1);
      this.setRowOrderPreserved(true);
      this.setColumnOrderPreserved(true);
      this.delegate = var2;
   }

   // $FF: synthetic method
   static void access$1700(String var0) {
      handleInvalidParams(var0);
      throw null;
   }

   static int adjust(int var0, int var1) {
      return MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1 + var0), MeasureSpec.getMode(var0));
   }

   static Object[] append(Object[] var0, Object[] var1) {
      Object[] var2 = (Object[])Array.newInstance(var0.getClass().getComponentType(), var0.length + var1.length);
      System.arraycopy(var0, 0, var2, 0, var0.length);
      System.arraycopy(var1, 0, var2, var0.length, var1.length);
      return var2;
   }

   static boolean canStretch(int var0) {
      boolean var1;
      if ((var0 & 2) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void checkLayoutParams(TableLayout.LayoutParams var1, boolean var2) {
      String var3;
      if (var2) {
         var3 = "column";
      } else {
         var3 = "row";
      }

      TableLayout.Spec var6;
      if (var2) {
         var6 = var1.columnSpec;
      } else {
         var6 = var1.rowSpec;
      }

      TableLayout.Interval var4 = var6.span;
      int var5 = var4.min;
      StringBuilder var8;
      if (var5 != Integer.MIN_VALUE && var5 < 0) {
         var8 = new StringBuilder();
         var8.append(var3);
         var8.append(" indices must be positive");
         handleInvalidParams(var8.toString());
         throw null;
      } else {
         TableLayout.Axis var7;
         if (var2) {
            var7 = this.mHorizontalAxis;
         } else {
            var7 = this.mVerticalAxis;
         }

         var5 = var7.definedCount;
         if (var5 != Integer.MIN_VALUE) {
            if (var4.max > var5) {
               var8 = new StringBuilder();
               var8.append(var3);
               var8.append(" indices (start + span) mustn't exceed the ");
               var8.append(var3);
               var8.append(" count");
               handleInvalidParams(var8.toString());
               throw null;
            }

            if (var4.size() > var5) {
               var8 = new StringBuilder();
               var8.append(var3);
               var8.append(" span mustn't exceed the ");
               var8.append(var3);
               var8.append(" count");
               handleInvalidParams(var8.toString());
               throw null;
            }
         }

      }
   }

   private static int clip(TableLayout.Interval var0, boolean var1, int var2) {
      int var3 = var0.size();
      if (var2 == 0) {
         return var3;
      } else {
         int var4;
         if (var1) {
            var4 = Math.min(var0.min, var2);
         } else {
            var4 = 0;
         }

         return Math.min(var3, var2 - var4);
      }
   }

   private int computeLayoutParamsHashCode() {
      int var1 = this.getChildCount();
      int var2 = 1;

      for(int var3 = 0; var3 < var1; ++var3) {
         var2 = var2 * 31 + this.getChildAt(var3).getLayoutParams().hashCode();
      }

      return var2;
   }

   private void consistencyCheck() {
      int var1 = this.mLastLayoutParamsHashCode;
      if (var1 == 0) {
         this.validateLayoutParams();
         this.mLastLayoutParamsHashCode = this.computeLayoutParamsHashCode();
      } else if (var1 != this.computeLayoutParamsHashCode()) {
         this.invalidateStructure();
         this.consistencyCheck();
      }

   }

   private static TableLayout.Alignment createSwitchingAlignment(final TableLayout.Alignment var0) {
      return new TableLayout.Alignment() {
         public int getAlignmentValue(TableLayout.Child var1, int var2) {
            return var0.getAlignmentValue(var1, var2);
         }

         int getGravityOffset(TableLayout.Child var1, int var2) {
            return var0.getGravityOffset(var1, var2);
         }
      };
   }

   private static boolean fits(int[] var0, int var1, int var2, int var3) {
      if (var3 > var0.length) {
         return false;
      } else {
         while(var2 < var3) {
            if (var0[var2] > var1) {
               return false;
            }

            ++var2;
         }

         return true;
      }
   }

   static TableLayout.Alignment getAlignment(int var0, boolean var1) {
      byte var2;
      if (var1) {
         var2 = 7;
      } else {
         var2 = 112;
      }

      byte var3;
      if (var1) {
         var3 = 0;
      } else {
         var3 = 4;
      }

      var0 = (var0 & var2) >> var3;
      if (var0 != 1) {
         TableLayout.Alignment var4;
         if (var0 != 3) {
            if (var0 != 5) {
               if (var0 != 7) {
                  if (var0 != 8388611) {
                     return var0 != 8388613 ? UNDEFINED_ALIGNMENT : END;
                  } else {
                     return START;
                  }
               } else {
                  return FILL;
               }
            } else {
               if (var1) {
                  var4 = RIGHT;
               } else {
                  var4 = BOTTOM;
               }

               return var4;
            }
         } else {
            if (var1) {
               var4 = LEFT;
            } else {
               var4 = TOP;
            }

            return var4;
         }
      } else {
         return CENTER;
      }
   }

   private int getDefaultMargin(TableLayout.Child var1, TableLayout.LayoutParams var2, boolean var3, boolean var4) {
      boolean var5 = this.mUseDefaultMargins;
      boolean var6 = false;
      if (!var5) {
         return 0;
      } else {
         TableLayout.Spec var9;
         if (var3) {
            var9 = var2.columnSpec;
         } else {
            var9 = var2.rowSpec;
         }

         TableLayout.Axis var7;
         if (var3) {
            var7 = this.mHorizontalAxis;
         } else {
            var7 = this.mVerticalAxis;
         }

         TableLayout.Interval var10 = var9.span;
         if (var3 && this.isRtl) {
            var5 = true;
         } else {
            var5 = false;
         }

         boolean var8;
         if (var5 != var4) {
            var8 = true;
         } else {
            var8 = false;
         }

         if (var8) {
            var5 = var6;
            if (var10.min != 0) {
               return this.getDefaultMargin(var1, var5, var3, var4);
            }
         } else {
            var5 = var6;
            if (var10.max != var7.getCount()) {
               return this.getDefaultMargin(var1, var5, var3, var4);
            }
         }

         var5 = true;
         return this.getDefaultMargin(var1, var5, var3, var4);
      }
   }

   private int getDefaultMargin(TableLayout.Child var1, boolean var2, boolean var3) {
      return this.mDefaultGap / 2;
   }

   private int getDefaultMargin(TableLayout.Child var1, boolean var2, boolean var3, boolean var4) {
      return this.getDefaultMargin(var1, var3, var4);
   }

   private int getMargin(TableLayout.Child var1, boolean var2, boolean var3) {
      if (this.mAlignmentMode == 1) {
         return this.getMargin1(var1, var2, var3);
      } else {
         TableLayout.Axis var4;
         if (var2) {
            var4 = this.mHorizontalAxis;
         } else {
            var4 = this.mVerticalAxis;
         }

         int[] var8;
         if (var3) {
            var8 = var4.getLeadingMargins();
         } else {
            var8 = var4.getTrailingMargins();
         }

         TableLayout.LayoutParams var6 = var1.getLayoutParams();
         TableLayout.Spec var7;
         if (var2) {
            var7 = var6.columnSpec;
         } else {
            var7 = var6.rowSpec;
         }

         int var5;
         if (var3) {
            var5 = var7.span.min;
         } else {
            var5 = var7.span.max;
         }

         return var8[var5];
      }
   }

   private int getMeasurement(TableLayout.Child var1, boolean var2) {
      int var3;
      if (var2) {
         var3 = var1.getMeasuredWidth();
      } else {
         var3 = var1.getMeasuredHeight();
      }

      return var3;
   }

   private int getTotalMargin(TableLayout.Child var1, boolean var2) {
      return this.getMargin(var1, var2, true) + this.getMargin(var1, var2, false);
   }

   private static void handleInvalidParams(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0);
      var1.append(". ");
      throw new IllegalArgumentException(var1.toString());
   }

   private void invalidateStructure() {
      this.mLastLayoutParamsHashCode = 0;
      this.mHorizontalAxis.invalidateStructure();
      this.mVerticalAxis.invalidateStructure();
      this.invalidateValues();
   }

   private void invalidateValues() {
      TableLayout.Axis var1 = this.mHorizontalAxis;
      if (var1 != null && this.mVerticalAxis != null) {
         var1.invalidateValues();
         this.mVerticalAxis.invalidateValues();
      }

   }

   static int max2(int[] var0, int var1) {
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1 = Math.max(var1, var0[var3]);
      }

      return var1;
   }

   private void measureChildWithMargins2(TableLayout.Child var1, int var2, int var3, int var4, int var5, boolean var6) {
      var1.measure(this.getTotalMargin(var1, true) + var4, this.getTotalMargin(var1, false) + var5, var6);
   }

   private void measureChildrenWithMargins(int var1, int var2, boolean var3) {
      int var4 = this.getChildCount();

      for(int var5 = 0; var5 < var4; ++var5) {
         TableLayout.Child var6 = this.getChildAt(var5);
         TableLayout.LayoutParams var7 = var6.getLayoutParams();
         int var8;
         if (var3) {
            var8 = MeasureSpec.getSize(var1);
            if (this.colCount == 2) {
               var8 = (int)((float)var8 / 2.0F) - this.itemPaddingLeft * 4;
            } else {
               var8 = (int)((float)var8 / 1.5F);
            }

            var6.setTextLayout(this.delegate.createTextLayout(var6.cell, var8));
            if (var6.textLayout != null) {
               var7.width = var6.textWidth + this.itemPaddingLeft * 2;
               var7.height = var6.textHeight + this.itemPaddingTop * 2;
            } else {
               var7.width = 0;
               var7.height = 0;
            }

            this.measureChildWithMargins2(var6, var1, var2, var7.width, var7.height, true);
         } else {
            boolean var9;
            if (this.mOrientation == 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            TableLayout.Spec var10;
            if (var9) {
               var10 = var7.columnSpec;
            } else {
               var10 = var7.rowSpec;
            }

            if (var10.getAbsoluteAlignment(var9) == FILL) {
               TableLayout.Interval var11 = var10.span;
               TableLayout.Axis var12;
               if (var9) {
                  var12 = this.mHorizontalAxis;
               } else {
                  var12 = this.mVerticalAxis;
               }

               int[] var13 = var12.getLocations();
               var8 = var13[var11.max] - var13[var11.min] - this.getTotalMargin(var6, var9);
               if (var9) {
                  this.measureChildWithMargins2(var6, var1, var2, var8, var7.height, false);
               } else {
                  this.measureChildWithMargins2(var6, var1, var2, var7.width, var8, false);
               }
            }
         }
      }

   }

   private static void procrusteanFill(int[] var0, int var1, int var2, int var3) {
      int var4 = var0.length;
      Arrays.fill(var0, Math.min(var1, var4), Math.min(var2, var4), var3);
   }

   private static void setCellGroup(TableLayout.LayoutParams var0, int var1, int var2, int var3, int var4) {
      var0.setRowSpecSpan(new TableLayout.Interval(var1, var2 + var1));
      var0.setColumnSpecSpan(new TableLayout.Interval(var3, var4 + var3));
   }

   public static TableLayout.Spec spec(int var0) {
      return spec(var0, 1);
   }

   public static TableLayout.Spec spec(int var0, float var1) {
      return spec(var0, 1, var1);
   }

   public static TableLayout.Spec spec(int var0, int var1) {
      return spec(var0, var1, UNDEFINED_ALIGNMENT);
   }

   public static TableLayout.Spec spec(int var0, int var1, float var2) {
      return spec(var0, var1, UNDEFINED_ALIGNMENT, var2);
   }

   public static TableLayout.Spec spec(int var0, int var1, TableLayout.Alignment var2) {
      return spec(var0, var1, var2, 0.0F);
   }

   public static TableLayout.Spec spec(int var0, int var1, TableLayout.Alignment var2, float var3) {
      boolean var4;
      if (var0 != Integer.MIN_VALUE) {
         var4 = true;
      } else {
         var4 = false;
      }

      return new TableLayout.Spec(var4, var0, var1, var2, var3);
   }

   public static TableLayout.Spec spec(int var0, TableLayout.Alignment var1) {
      return spec(var0, 1, var1);
   }

   public static TableLayout.Spec spec(int var0, TableLayout.Alignment var1, float var2) {
      return spec(var0, 1, var1, var2);
   }

   private void validateLayoutParams() {
      boolean var1;
      if (this.mOrientation == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      TableLayout.Axis var2;
      if (var1) {
         var2 = this.mHorizontalAxis;
      } else {
         var2 = this.mVerticalAxis;
      }

      int var3 = var2.definedCount;
      if (var3 == Integer.MIN_VALUE) {
         var3 = 0;
      }

      int[] var4 = new int[var3];
      int var5 = this.getChildCount();
      int var6 = 0;
      int var7 = 0;

      int var15;
      for(int var8 = 0; var6 < var5; var7 = var15) {
         TableLayout.LayoutParams var9 = this.getChildAt(var6).getLayoutParams();
         TableLayout.Spec var19;
         if (var1) {
            var19 = var9.rowSpec;
         } else {
            var19 = var9.columnSpec;
         }

         TableLayout.Interval var10 = var19.span;
         boolean var11 = var19.startDefined;
         int var12 = var10.size();
         if (var11) {
            var7 = var10.min;
         }

         if (var1) {
            var19 = var9.columnSpec;
         } else {
            var19 = var9.rowSpec;
         }

         var10 = var19.span;
         boolean var13 = var19.startDefined;
         int var14 = clip(var10, var13, var3);
         if (var13) {
            var8 = var10.min;
         }

         var15 = var7;
         int var16 = var8;
         if (var3 != 0) {
            label70: {
               int var17 = var7;
               int var18 = var8;
               if (var11) {
                  var15 = var7;
                  var16 = var8;
                  if (var13) {
                     break label70;
                  }

                  var18 = var8;
                  var17 = var7;
               }

               while(true) {
                  var7 = var18 + var14;
                  var15 = var17;
                  var16 = var18;
                  if (fits(var4, var17, var18, var7)) {
                     break;
                  }

                  if (var13) {
                     ++var17;
                  } else if (var7 <= var3) {
                     ++var18;
                  } else {
                     ++var17;
                     var18 = 0;
                  }
               }
            }

            procrusteanFill(var4, var16, var16 + var14, var15 + var12);
         }

         if (var1) {
            setCellGroup(var9, var15, var12, var16, var14);
         } else {
            setCellGroup(var9, var16, var14, var15, var12);
         }

         var8 = var16 + var14;
         ++var6;
      }

   }

   public void addChild(int var1, int var2, int var3, int var4) {
      TableLayout.Child var5 = new TableLayout.Child(this.childrens.size());
      TableLayout.LayoutParams var6 = new TableLayout.LayoutParams();
      var6.rowSpec = new TableLayout.Spec(false, new TableLayout.Interval(var2, var2 + var4), FILL, 0.0F);
      var6.columnSpec = new TableLayout.Spec(false, new TableLayout.Interval(var1, var1 + var3), FILL, 0.0F);
      var5.layoutParams = var6;
      this.childrens.add(var5);
      this.invalidateStructure();
   }

   public void addChild(TLRPC.TL_pageTableCell var1, int var2, int var3, int var4) {
      if (var4 == 0) {
         var4 = 1;
      }

      TableLayout.Child var5 = new TableLayout.Child(this.childrens.size());
      var5.cell = var1;
      TableLayout.LayoutParams var6 = new TableLayout.LayoutParams();
      int var7 = var1.rowspan;
      if (var7 == 0) {
         var7 = 1;
      }

      var6.rowSpec = new TableLayout.Spec(false, new TableLayout.Interval(var3, var7 + var3), FILL, 0.0F);
      var6.columnSpec = new TableLayout.Spec(false, new TableLayout.Interval(var2, var4 + var2), FILL, 1.0F);
      var5.layoutParams = var6;
      this.childrens.add(var5);
      var2 = var1.rowspan;
      if (var2 > 1) {
         this.rowSpans.add(new Point((float)var3, (float)(var2 + var3)));
      }

      this.invalidateStructure();
   }

   public int getAlignmentMode() {
      return this.mAlignmentMode;
   }

   public TableLayout.Child getChildAt(int var1) {
      return var1 >= 0 && var1 < this.childrens.size() ? (TableLayout.Child)this.childrens.get(var1) : null;
   }

   public int getChildCount() {
      return this.childrens.size();
   }

   public int getColumnCount() {
      return this.mHorizontalAxis.getCount();
   }

   int getMargin1(TableLayout.Child var1, boolean var2, boolean var3) {
      TableLayout.LayoutParams var4 = var1.getLayoutParams();
      int var5;
      if (var2) {
         if (var3) {
            var5 = var4.leftMargin;
         } else {
            var5 = var4.rightMargin;
         }
      } else if (var3) {
         var5 = var4.topMargin;
      } else {
         var5 = var4.bottomMargin;
      }

      int var6 = var5;
      if (var5 == Integer.MIN_VALUE) {
         var6 = this.getDefaultMargin(var1, var4, var2, var3);
      }

      return var6;
   }

   final int getMeasurementIncludingMargin(TableLayout.Child var1, boolean var2) {
      return this.getMeasurement(var1, var2) + this.getTotalMargin(var1, var2);
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public int getRowCount() {
      return this.mVerticalAxis.getCount();
   }

   public boolean getUseDefaultMargins() {
      return this.mUseDefaultMargins;
   }

   public boolean isColumnOrderPreserved() {
      return this.mHorizontalAxis.isOrderPreserved();
   }

   public boolean isRowOrderPreserved() {
      return this.mVerticalAxis.isOrderPreserved();
   }

   protected void onDraw(Canvas var1) {
      int var2 = this.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.getChildAt(var3).draw(var1);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.consistencyCheck();
   }

   protected void onMeasure(int var1, int var2) {
      this.consistencyCheck();
      this.invalidateValues();
      this.colCount = 0;
      int var3 = this.getChildCount();

      int var4;
      TableLayout.Child var5;
      for(var4 = 0; var4 < var3; ++var4) {
         var5 = this.getChildAt(var4);
         this.colCount = Math.max(this.colCount, var5.layoutParams.columnSpec.span.max);
      }

      this.measureChildrenWithMargins(var1, var2, true);
      if (this.mOrientation == 0) {
         var4 = this.mHorizontalAxis.getMeasure(var1);
         this.measureChildrenWithMargins(var1, var2, false);
         var2 = this.mVerticalAxis.getMeasure(var2);
      } else {
         var3 = this.mVerticalAxis.getMeasure(var2);
         this.measureChildrenWithMargins(var1, var2, false);
         var4 = this.mHorizontalAxis.getMeasure(var1);
         var2 = var3;
      }

      var4 = Math.max(var4, MeasureSpec.getSize(var1));
      var1 = Math.max(var2, this.getSuggestedMinimumHeight());
      this.setMeasuredDimension(var4, var1);
      this.mHorizontalAxis.layout(var4);
      this.mVerticalAxis.layout(var1);
      int[] var29 = this.mHorizontalAxis.getLocations();
      int[] var6 = this.mVerticalAxis.getLocations();
      this.cellsToFixHeight.clear();
      int var7 = var29[var29.length - 1];
      int var8 = this.getChildCount();

      int var14;
      int var15;
      int var19;
      boolean var28;
      for(var2 = 0; var2 < var8; ++var2) {
         TableLayout.Child var9 = this.getChildAt(var2);
         TableLayout.LayoutParams var10 = var9.getLayoutParams();
         TableLayout.Spec var11 = var10.columnSpec;
         TableLayout.Spec var31 = var10.rowSpec;
         TableLayout.Interval var12 = var11.span;
         TableLayout.Interval var13 = var31.span;
         var4 = var29[var12.min];
         var3 = var6[var13.min];
         var14 = var29[var12.max];
         var15 = var6[var13.max];
         var14 -= var4;
         var15 -= var3;
         int var16 = this.getMeasurement(var9, true);
         int var17 = this.getMeasurement(var9, false);
         TableLayout.Alignment var33 = var11.getAbsoluteAlignment(true);
         TableLayout.Alignment var36 = var31.getAbsoluteAlignment(false);
         TableLayout.Bounds var35 = (TableLayout.Bounds)this.mHorizontalAxis.getGroupBounds().getValue(var2);
         TableLayout.Bounds var32 = (TableLayout.Bounds)this.mVerticalAxis.getGroupBounds().getValue(var2);
         int var18 = var33.getGravityOffset(var9, var14 - var35.size(true));
         var19 = var36.getGravityOffset(var9, var15 - var32.size(true));
         int var20 = this.getMargin(var9, true, true);
         int var21 = this.getMargin(var9, false, true);
         int var22 = this.getMargin(var9, true, false);
         int var23 = this.getMargin(var9, false, false);
         int var24 = var20 + var22;
         int var25 = var21 + var23;
         int var26 = var35.getOffset(this, var9, var33, var16 + var24, true);
         var23 = var32.getOffset(this, var9, var36, var17 + var25, false);
         var14 = var33.getSizeInCell(var9, var16, var14 - var24);
         var15 = var36.getSizeInCell(var9, var17, var15 - var25);
         var4 = var4 + var18 + var26;
         if (!this.isRtl) {
            var4 += var20;
         } else {
            var4 = var7 - var14 - var22 - var4;
         }

         var19 = var3 + var19 + var23 + var21;
         if (var9.cell != null) {
            if (var14 != var9.getMeasuredWidth() || var15 != var9.getMeasuredHeight()) {
               var9.measure(var14, var15, false);
            }

            if (var9.fixedHeight != 0 && var9.fixedHeight != var15 && var9.layoutParams.rowSpec.span.max - var9.layoutParams.rowSpec.span.min <= 1) {
               var21 = this.rowSpans.size();
               var3 = 0;

               while(true) {
                  if (var3 >= var21) {
                     var28 = false;
                     break;
                  }

                  Point var34 = (Point)this.rowSpans.get(var3);
                  if (var34.x <= (float)var9.layoutParams.rowSpec.span.min && var34.y > (float)var9.layoutParams.rowSpec.span.min) {
                     var28 = true;
                     break;
                  }

                  ++var3;
               }

               if (!var28) {
                  this.cellsToFixHeight.add(var9);
               }
            }
         }

         var9.layout(var4, var19, var14 + var4, var15 + var19);
      }

      var4 = this.cellsToFixHeight.size();
      byte var27 = 0;
      var8 = var1;

      for(var1 = var27; var1 < var4; ++var1) {
         var5 = (TableLayout.Child)this.cellsToFixHeight.get(var1);
         var2 = var5.measuredHeight - var5.fixedHeight;
         var3 = var5.index + 1;

         TableLayout.Child var30;
         label99: {
            for(var15 = this.childrens.size(); var3 < var15; var2 = var14) {
               var30 = (TableLayout.Child)this.childrens.get(var3);
               if (var5.layoutParams.rowSpec.span.min != var30.layoutParams.rowSpec.span.min) {
                  break;
               }

               if (var5.fixedHeight < var30.fixedHeight) {
                  var28 = true;
                  break label99;
               }

               var19 = var30.measuredHeight - var30.fixedHeight;
               var14 = var2;
               if (var19 > 0) {
                  var14 = Math.min(var2, var19);
               }

               ++var3;
            }

            var28 = false;
         }

         var14 = var2;
         boolean var37 = var28;
         if (!var28) {
            var15 = var5.index - 1;

            while(true) {
               var14 = var2;
               var37 = var28;
               if (var15 < 0) {
                  break;
               }

               var30 = (TableLayout.Child)this.childrens.get(var15);
               var14 = var2;
               var37 = var28;
               if (var5.layoutParams.rowSpec.span.min != var30.layoutParams.rowSpec.span.min) {
                  break;
               }

               if (var5.fixedHeight < var30.fixedHeight) {
                  var37 = true;
                  var14 = var2;
                  break;
               }

               var19 = var30.measuredHeight - var30.fixedHeight;
               var14 = var2;
               if (var19 > 0) {
                  var14 = Math.min(var2, var19);
               }

               --var15;
               var2 = var14;
            }
         }

         if (!var37) {
            var5.setFixedHeight(var5.fixedHeight);
            var15 = var8 - var14;
            var19 = this.childrens.size();
            var2 = var4;

            for(var8 = 0; var8 < var19; var2 = var4) {
               var30 = (TableLayout.Child)this.childrens.get(var8);
               if (var5 == var30) {
                  var3 = var1;
                  var4 = var2;
               } else if (var5.layoutParams.rowSpec.span.min == var30.layoutParams.rowSpec.span.min) {
                  var3 = var1;
                  var4 = var2;
                  if (var30.fixedHeight != var30.measuredHeight) {
                     this.cellsToFixHeight.remove(var30);
                     var3 = var1;
                     if (var30.index < var5.index) {
                        var3 = var1 - 1;
                     }

                     var4 = var2 - 1;
                  }

                  var30.measuredHeight = var30.measuredHeight - var14;
                  var30.measure(var30.measuredWidth, var30.measuredHeight, true);
               } else {
                  var3 = var1;
                  var4 = var2;
                  if (var5.layoutParams.rowSpec.span.min < var30.layoutParams.rowSpec.span.min) {
                     var30.y -= var14;
                     var4 = var2;
                     var3 = var1;
                  }
               }

               ++var8;
               var1 = var3;
            }

            var4 = var2;
            var8 = var15;
         }
      }

      this.setMeasuredDimension(var7, var8);
   }

   public void removeAllChildrens() {
      this.childrens.clear();
      this.rowSpans.clear();
      this.invalidateStructure();
   }

   public void requestLayout() {
      super.requestLayout();
      this.invalidateValues();
   }

   public void setAlignmentMode(int var1) {
      this.mAlignmentMode = var1;
      this.requestLayout();
   }

   public void setColumnCount(int var1) {
      this.mHorizontalAxis.setCount(var1);
      this.invalidateStructure();
      this.requestLayout();
   }

   public void setColumnOrderPreserved(boolean var1) {
      this.mHorizontalAxis.setOrderPreserved(var1);
      this.invalidateStructure();
      this.requestLayout();
   }

   public void setDrawLines(boolean var1) {
      this.drawLines = var1;
   }

   public void setOrientation(int var1) {
      if (this.mOrientation != var1) {
         this.mOrientation = var1;
         this.invalidateStructure();
         this.requestLayout();
      }

   }

   public void setRowCount(int var1) {
      this.mVerticalAxis.setCount(var1);
      this.invalidateStructure();
      this.requestLayout();
   }

   public void setRowOrderPreserved(boolean var1) {
      this.mVerticalAxis.setOrderPreserved(var1);
      this.invalidateStructure();
      this.requestLayout();
   }

   public void setRtl(boolean var1) {
      this.isRtl = var1;
   }

   public void setStriped(boolean var1) {
      this.isStriped = var1;
   }

   public void setUseDefaultMargins(boolean var1) {
      this.mUseDefaultMargins = var1;
      this.requestLayout();
   }

   public abstract static class Alignment {
      Alignment() {
      }

      abstract int getAlignmentValue(TableLayout.Child var1, int var2);

      TableLayout.Bounds getBounds() {
         return new TableLayout.Bounds();
      }

      abstract int getGravityOffset(TableLayout.Child var1, int var2);

      int getSizeInCell(TableLayout.Child var1, int var2, int var3) {
         return var2;
      }
   }

   static final class Arc {
      public final TableLayout.Interval span;
      public boolean valid = true;
      public final TableLayout.MutableInt value;

      public Arc(TableLayout.Interval var1, TableLayout.MutableInt var2) {
         this.span = var1;
         this.value = var2;
      }
   }

   static final class Assoc extends ArrayList {
      private final Class keyType;
      private final Class valueType;

      private Assoc(Class var1, Class var2) {
         this.keyType = var1;
         this.valueType = var2;
      }

      public static TableLayout.Assoc of(Class var0, Class var1) {
         return new TableLayout.Assoc(var0, var1);
      }

      public TableLayout.PackedMap pack() {
         int var1 = this.size();
         Object[] var2 = (Object[])Array.newInstance(this.keyType, var1);
         Object[] var3 = (Object[])Array.newInstance(this.valueType, var1);

         for(int var4 = 0; var4 < var1; ++var4) {
            var2[var4] = ((Pair)this.get(var4)).first;
            var3[var4] = ((Pair)this.get(var4)).second;
         }

         return new TableLayout.PackedMap(var2, var3);
      }

      public void put(Object var1, Object var2) {
         this.add(Pair.create(var1, var2));
      }
   }

   final class Axis {
      private static final int COMPLETE = 2;
      private static final int NEW = 0;
      private static final int PENDING = 1;
      public TableLayout.Arc[] arcs;
      public boolean arcsValid;
      TableLayout.PackedMap backwardLinks;
      public boolean backwardLinksValid;
      public int definedCount;
      public int[] deltas;
      TableLayout.PackedMap forwardLinks;
      public boolean forwardLinksValid;
      TableLayout.PackedMap groupBounds;
      public boolean groupBoundsValid;
      public boolean hasWeights;
      public boolean hasWeightsValid;
      public final boolean horizontal;
      public int[] leadingMargins;
      public boolean leadingMarginsValid;
      public int[] locations;
      public boolean locationsValid;
      private int maxIndex;
      boolean orderPreserved;
      private TableLayout.MutableInt parentMax;
      private TableLayout.MutableInt parentMin;
      public int[] trailingMargins;
      public boolean trailingMarginsValid;

      private Axis(boolean var2) {
         this.definedCount = Integer.MIN_VALUE;
         this.maxIndex = Integer.MIN_VALUE;
         this.groupBoundsValid = false;
         this.forwardLinksValid = false;
         this.backwardLinksValid = false;
         this.leadingMarginsValid = false;
         this.trailingMarginsValid = false;
         this.arcsValid = false;
         this.locationsValid = false;
         this.hasWeightsValid = false;
         this.orderPreserved = true;
         this.parentMin = new TableLayout.MutableInt(0);
         this.parentMax = new TableLayout.MutableInt(-100000);
         this.horizontal = var2;
      }

      // $FF: synthetic method
      Axis(boolean var2, Object var3) {
         this(var2);
      }

      private void addComponentSizes(List var1, TableLayout.PackedMap var2) {
         int var3 = 0;

         while(true) {
            Object[] var4 = var2.keys;
            if (var3 >= ((TableLayout.Interval[])var4).length) {
               return;
            }

            this.include(var1, ((TableLayout.Interval[])var4)[var3], ((TableLayout.MutableInt[])var2.values)[var3], false);
            ++var3;
         }
      }

      private int calculateMaxIndex() {
         int var1 = TableLayout.this.getChildCount();
         int var2 = 0;

         int var3;
         for(var3 = -1; var2 < var1; ++var2) {
            TableLayout.LayoutParams var4 = TableLayout.this.getChildAt(var2).getLayoutParams();
            TableLayout.Spec var5;
            if (this.horizontal) {
               var5 = var4.columnSpec;
            } else {
               var5 = var4.rowSpec;
            }

            TableLayout.Interval var6 = var5.span;
            var3 = Math.max(Math.max(Math.max(var3, var6.min), var6.max), var6.size());
         }

         var2 = var3;
         if (var3 == -1) {
            var2 = Integer.MIN_VALUE;
         }

         return var2;
      }

      private float calculateTotalWeight() {
         int var1 = TableLayout.this.getChildCount();
         float var2 = 0.0F;

         for(int var3 = 0; var3 < var1; ++var3) {
            TableLayout.LayoutParams var4 = TableLayout.this.getChildAt(var3).getLayoutParams();
            TableLayout.Spec var5;
            if (this.horizontal) {
               var5 = var4.columnSpec;
            } else {
               var5 = var4.rowSpec;
            }

            var2 += var5.weight;
         }

         return var2;
      }

      private void computeArcs() {
         this.getForwardLinks();
         this.getBackwardLinks();
      }

      private void computeGroupBounds() {
         TableLayout.Bounds[] var1 = (TableLayout.Bounds[])this.groupBounds.values;

         int var2;
         for(var2 = 0; var2 < var1.length; ++var2) {
            var1[var2].reset();
         }

         int var3 = TableLayout.this.getChildCount();

         for(var2 = 0; var2 < var3; ++var2) {
            TableLayout.Child var4 = TableLayout.this.getChildAt(var2);
            TableLayout.LayoutParams var7 = var4.getLayoutParams();
            TableLayout.Spec var8;
            if (this.horizontal) {
               var8 = var7.columnSpec;
            } else {
               var8 = var7.rowSpec;
            }

            int var5 = TableLayout.this.getMeasurementIncludingMargin(var4, this.horizontal);
            int var6;
            if (var8.weight == 0.0F) {
               var6 = 0;
            } else {
               var6 = this.deltas[var2];
            }

            ((TableLayout.Bounds)this.groupBounds.getValue(var2)).include(TableLayout.this, var4, var8, this, var5 + var6);
         }

      }

      private boolean computeHasWeights() {
         int var1 = TableLayout.this.getChildCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            TableLayout.LayoutParams var3 = TableLayout.this.getChildAt(var2).getLayoutParams();
            TableLayout.Spec var4;
            if (this.horizontal) {
               var4 = var3.columnSpec;
            } else {
               var4 = var3.rowSpec;
            }

            if (var4.weight != 0.0F) {
               return true;
            }
         }

         return false;
      }

      private void computeLinks(TableLayout.PackedMap var1, boolean var2) {
         TableLayout.MutableInt[] var3 = (TableLayout.MutableInt[])var1.values;
         byte var4 = 0;

         int var5;
         for(var5 = 0; var5 < var3.length; ++var5) {
            var3[var5].reset();
         }

         TableLayout.Bounds[] var6 = (TableLayout.Bounds[])this.getGroupBounds().values;

         for(var5 = var4; var5 < var6.length; ++var5) {
            int var9 = var6[var5].size(var2);
            TableLayout.MutableInt var8 = (TableLayout.MutableInt)var1.getValue(var5);
            int var7 = var8.value;
            if (!var2) {
               var9 = -var9;
            }

            var8.value = Math.max(var7, var9);
         }

      }

      private void computeLocations(int[] var1) {
         if (!this.hasWeights()) {
            this.solve(var1);
         } else {
            this.solveAndDistributeSpace(var1);
         }

         if (!this.orderPreserved) {
            int var2 = 0;
            int var3 = var1[0];

            for(int var4 = var1.length; var2 < var4; ++var2) {
               var1[var2] -= var3;
            }
         }

      }

      private void computeMargins(boolean var1) {
         int[] var2;
         if (var1) {
            var2 = this.leadingMargins;
         } else {
            var2 = this.trailingMargins;
         }

         int var3 = 0;

         for(int var4 = TableLayout.this.getChildCount(); var3 < var4; ++var3) {
            TableLayout.Child var5 = TableLayout.this.getChildAt(var3);
            TableLayout.LayoutParams var6 = var5.getLayoutParams();
            TableLayout.Spec var8;
            if (this.horizontal) {
               var8 = var6.columnSpec;
            } else {
               var8 = var6.rowSpec;
            }

            TableLayout.Interval var9 = var8.span;
            int var7;
            if (var1) {
               var7 = var9.min;
            } else {
               var7 = var9.max;
            }

            var2[var7] = Math.max(var2[var7], TableLayout.this.getMargin1(var5, this.horizontal, var1));
         }

      }

      private TableLayout.Arc[] createArcs() {
         ArrayList var1 = new ArrayList();
         ArrayList var2 = new ArrayList();
         this.addComponentSizes(var1, this.getForwardLinks());
         this.addComponentSizes(var2, this.getBackwardLinks());
         int var3;
         int var4;
         if (this.orderPreserved) {
            for(var3 = 0; var3 < this.getCount(); var3 = var4) {
               var4 = var3 + 1;
               this.include(var1, new TableLayout.Interval(var3, var4), new TableLayout.MutableInt(0));
            }
         }

         var3 = this.getCount();
         this.include(var1, new TableLayout.Interval(0, var3), this.parentMin, false);
         this.include(var2, new TableLayout.Interval(var3, 0), this.parentMax, false);
         return (TableLayout.Arc[])TableLayout.append(this.topologicalSort((List)var1), this.topologicalSort((List)var2));
      }

      private TableLayout.PackedMap createGroupBounds() {
         TableLayout.Assoc var1 = TableLayout.Assoc.of(TableLayout.Spec.class, TableLayout.Bounds.class);
         int var2 = TableLayout.this.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            TableLayout.LayoutParams var4 = TableLayout.this.getChildAt(var3).getLayoutParams();
            TableLayout.Spec var5;
            if (this.horizontal) {
               var5 = var4.columnSpec;
            } else {
               var5 = var4.rowSpec;
            }

            var1.put(var5, var5.getAbsoluteAlignment(this.horizontal).getBounds());
         }

         return var1.pack();
      }

      private TableLayout.PackedMap createLinks(boolean var1) {
         TableLayout.Assoc var2 = TableLayout.Assoc.of(TableLayout.Interval.class, TableLayout.MutableInt.class);
         TableLayout.Spec[] var3 = (TableLayout.Spec[])this.getGroupBounds().keys;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            TableLayout.Interval var6;
            if (var1) {
               var6 = var3[var5].span;
            } else {
               var6 = var3[var5].span.inverse();
            }

            var2.put(var6, new TableLayout.MutableInt());
         }

         return var2.pack();
      }

      private TableLayout.PackedMap getBackwardLinks() {
         if (this.backwardLinks == null) {
            this.backwardLinks = this.createLinks(false);
         }

         if (!this.backwardLinksValid) {
            this.computeLinks(this.backwardLinks, false);
            this.backwardLinksValid = true;
         }

         return this.backwardLinks;
      }

      private TableLayout.PackedMap getForwardLinks() {
         if (this.forwardLinks == null) {
            this.forwardLinks = this.createLinks(true);
         }

         if (!this.forwardLinksValid) {
            this.computeLinks(this.forwardLinks, true);
            this.forwardLinksValid = true;
         }

         return this.forwardLinks;
      }

      private int getMaxIndex() {
         if (this.maxIndex == Integer.MIN_VALUE) {
            this.maxIndex = Math.max(0, this.calculateMaxIndex());
         }

         return this.maxIndex;
      }

      private int getMeasure(int var1, int var2) {
         this.setParentConstraints(var1, var2);
         return this.size(this.getLocations());
      }

      private boolean hasWeights() {
         if (!this.hasWeightsValid) {
            this.hasWeights = this.computeHasWeights();
            this.hasWeightsValid = true;
         }

         return this.hasWeights;
      }

      private void include(List var1, TableLayout.Interval var2, TableLayout.MutableInt var3) {
         this.include(var1, var2, var3, true);
      }

      private void include(List var1, TableLayout.Interval var2, TableLayout.MutableInt var3, boolean var4) {
         if (var2.size() != 0) {
            if (var4) {
               Iterator var5 = var1.iterator();

               while(var5.hasNext()) {
                  if (((TableLayout.Arc)var5.next()).span.equals(var2)) {
                     return;
                  }
               }
            }

            var1.add(new TableLayout.Arc(var2, var3));
         }
      }

      private void init(int[] var1) {
         Arrays.fill(var1, 0);
      }

      private boolean relax(int[] var1, TableLayout.Arc var2) {
         if (!var2.valid) {
            return false;
         } else {
            TableLayout.Interval var3 = var2.span;
            int var4 = var3.min;
            int var5 = var3.max;
            int var6 = var2.value.value;
            var6 += var1[var4];
            if (var6 > var1[var5]) {
               var1[var5] = var6;
               return true;
            } else {
               return false;
            }
         }
      }

      private void setParentConstraints(int var1, int var2) {
         this.parentMin.value = var1;
         this.parentMax.value = -var2;
         this.locationsValid = false;
      }

      private void shareOutDelta(int var1, float var2) {
         int[] var3 = this.deltas;
         byte var4 = 0;
         Arrays.fill(var3, 0);
         int var5 = TableLayout.this.getChildCount();
         int var6 = var1;

         float var8;
         for(var1 = var4; var1 < var5; var2 = var8) {
            TableLayout.LayoutParams var9 = TableLayout.this.getChildAt(var1).getLayoutParams();
            TableLayout.Spec var10;
            if (this.horizontal) {
               var10 = var9.columnSpec;
            } else {
               var10 = var9.rowSpec;
            }

            float var7 = var10.weight;
            int var11 = var6;
            var8 = var2;
            if (var7 != 0.0F) {
               var11 = Math.round((float)var6 * var7 / var2);
               this.deltas[var1] = var11;
               var11 = var6 - var11;
               var8 = var2 - var7;
            }

            ++var1;
            var6 = var11;
         }

      }

      private int size(int[] var1) {
         return var1[this.getCount()];
      }

      private boolean solve(int[] var1) {
         return this.solve(this.getArcs(), var1);
      }

      private boolean solve(TableLayout.Arc[] var1, int[] var2) {
         return this.solve(var1, var2, true);
      }

      private boolean solve(TableLayout.Arc[] var1, int[] var2, boolean var3) {
         int var4 = this.getCount() + 1;

         for(int var5 = 0; var5 < var1.length; ++var5) {
            this.init(var2);

            int var6;
            int var8;
            for(var6 = 0; var6 < var4; ++var6) {
               int var7 = var1.length;
               var8 = 0;

               boolean var9;
               for(var9 = false; var8 < var7; ++var8) {
                  var9 |= this.relax(var2, var1[var8]);
               }

               if (!var9) {
                  return true;
               }
            }

            if (!var3) {
               return false;
            }

            boolean[] var10 = new boolean[var1.length];

            for(var6 = 0; var6 < var4; ++var6) {
               int var13 = var1.length;

               for(var8 = 0; var8 < var13; ++var8) {
                  var10[var8] |= this.relax(var2, var1[var8]);
               }
            }

            for(var6 = 0; var6 < var1.length; ++var6) {
               if (var10[var6]) {
                  TableLayout.Arc var11 = var1[var6];
                  TableLayout.Interval var12 = var11.span;
                  if (var12.min >= var12.max) {
                     var11.valid = false;
                     break;
                  }
               }
            }
         }

         return true;
      }

      private void solveAndDistributeSpace(int[] var1) {
         Arrays.fill(this.getDeltas(), 0);
         this.solve(var1);
         int var2 = this.parentMin.value * TableLayout.this.getChildCount() + 1;
         if (var2 >= 2) {
            float var3 = this.calculateTotalWeight();
            int var4 = -1;
            int var5 = 0;
            boolean var6 = true;

            while(var5 < var2) {
               int var7 = (int)(((long)var5 + (long)var2) / 2L);
               this.invalidateValues();
               this.shareOutDelta(var7, var3);
               var6 = this.solve(this.getArcs(), var1, false);
               if (var6) {
                  var5 = var7 + 1;
                  var4 = var7;
               } else {
                  var2 = var7;
               }
            }

            if (var4 > 0 && !var6) {
               this.invalidateValues();
               this.shareOutDelta(var4, var3);
               this.solve(var1);
            }

         }
      }

      private TableLayout.Arc[] topologicalSort(List var1) {
         return this.topologicalSort((TableLayout.Arc[])var1.toArray(new TableLayout.Arc[var1.size()]));
      }

      private TableLayout.Arc[] topologicalSort(final TableLayout.Arc[] var1) {
         return ((<undefinedtype>)(new Object() {
            TableLayout.Arc[][] arcsByVertex;
            int cursor;
            TableLayout.Arc[] result;
            int[] visited;

            {
               TableLayout.Arc[] var3 = var1;
               this.result = new TableLayout.Arc[var3.length];
               this.cursor = this.result.length - 1;
               this.arcsByVertex = Axis.this.groupArcsByFirstVertex(var3);
               this.visited = new int[Axis.this.getCount() + 1];
            }

            TableLayout.Arc[] sort() {
               int var1x = this.arcsByVertex.length;

               for(int var2 = 0; var2 < var1x; ++var2) {
                  this.walk(var2);
               }

               return this.result;
            }

            void walk(int var1x) {
               int[] var2 = this.visited;
               if (var2[var1x] == 0) {
                  var2[var1x] = 1;
                  TableLayout.Arc[] var3 = this.arcsByVertex[var1x];
                  int var4 = var3.length;

                  for(int var5 = 0; var5 < var4; ++var5) {
                     TableLayout.Arc var8 = var3[var5];
                     this.walk(var8.span.max);
                     TableLayout.Arc[] var6 = this.result;
                     int var7 = this.cursor--;
                     var6[var7] = var8;
                  }

                  this.visited[var1x] = 2;
               }

            }
         })).sort();
      }

      public TableLayout.Arc[] getArcs() {
         if (this.arcs == null) {
            this.arcs = this.createArcs();
         }

         if (!this.arcsValid) {
            this.computeArcs();
            this.arcsValid = true;
         }

         return this.arcs;
      }

      public int getCount() {
         return Math.max(this.definedCount, this.getMaxIndex());
      }

      public int[] getDeltas() {
         if (this.deltas == null) {
            this.deltas = new int[TableLayout.this.getChildCount()];
         }

         return this.deltas;
      }

      public TableLayout.PackedMap getGroupBounds() {
         if (this.groupBounds == null) {
            this.groupBounds = this.createGroupBounds();
         }

         if (!this.groupBoundsValid) {
            this.computeGroupBounds();
            this.groupBoundsValid = true;
         }

         return this.groupBounds;
      }

      public int[] getLeadingMargins() {
         if (this.leadingMargins == null) {
            this.leadingMargins = new int[this.getCount() + 1];
         }

         if (!this.leadingMarginsValid) {
            this.computeMargins(true);
            this.leadingMarginsValid = true;
         }

         return this.leadingMargins;
      }

      public int[] getLocations() {
         if (this.locations == null) {
            this.locations = new int[this.getCount() + 1];
         }

         if (!this.locationsValid) {
            this.computeLocations(this.locations);
            this.locationsValid = true;
         }

         return this.locations;
      }

      public int getMeasure(int var1) {
         int var2 = MeasureSpec.getMode(var1);
         var1 = MeasureSpec.getSize(var1);
         if (var2 != Integer.MIN_VALUE) {
            if (var2 != 0) {
               return var2 != 1073741824 ? 0 : this.getMeasure(var1, var1);
            } else {
               return this.getMeasure(0, 100000);
            }
         } else {
            return this.getMeasure(0, var1);
         }
      }

      public int[] getTrailingMargins() {
         if (this.trailingMargins == null) {
            this.trailingMargins = new int[this.getCount() + 1];
         }

         if (!this.trailingMarginsValid) {
            this.computeMargins(false);
            this.trailingMarginsValid = true;
         }

         return this.trailingMargins;
      }

      TableLayout.Arc[][] groupArcsByFirstVertex(TableLayout.Arc[] var1) {
         int var2 = this.getCount() + 1;
         TableLayout.Arc[][] var3 = new TableLayout.Arc[var2][];
         int[] var4 = new int[var2];
         int var5 = var1.length;
         byte var6 = 0;

         int var7;
         for(var2 = 0; var2 < var5; ++var2) {
            var7 = var1[var2].span.min;
            int var10002 = var4[var7]++;
         }

         for(var2 = 0; var2 < var4.length; ++var2) {
            var3[var2] = new TableLayout.Arc[var4[var2]];
         }

         Arrays.fill(var4, 0);
         var5 = var1.length;

         for(var2 = var6; var2 < var5; ++var2) {
            TableLayout.Arc var8 = var1[var2];
            var7 = var8.span.min;
            TableLayout.Arc[] var9 = var3[var7];
            int var10 = var4[var7]++;
            var9[var10] = var8;
         }

         return var3;
      }

      public void invalidateStructure() {
         this.maxIndex = Integer.MIN_VALUE;
         this.groupBounds = null;
         this.forwardLinks = null;
         this.backwardLinks = null;
         this.leadingMargins = null;
         this.trailingMargins = null;
         this.arcs = null;
         this.locations = null;
         this.deltas = null;
         this.hasWeightsValid = false;
         this.invalidateValues();
      }

      public void invalidateValues() {
         this.groupBoundsValid = false;
         this.forwardLinksValid = false;
         this.backwardLinksValid = false;
         this.leadingMarginsValid = false;
         this.trailingMarginsValid = false;
         this.arcsValid = false;
         this.locationsValid = false;
      }

      public boolean isOrderPreserved() {
         return this.orderPreserved;
      }

      public void layout(int var1) {
         this.setParentConstraints(var1, var1);
         this.getLocations();
      }

      public void setCount(int var1) {
         if (var1 != Integer.MIN_VALUE && var1 < this.getMaxIndex()) {
            StringBuilder var2 = new StringBuilder();
            String var3;
            if (this.horizontal) {
               var3 = "column";
            } else {
               var3 = "row";
            }

            var2.append(var3);
            var2.append("Count must be greater than or equal to the maximum of all grid indices (and spans) defined in the LayoutParams of each child");
            TableLayout.access$1700(var2.toString());
            throw null;
         } else {
            this.definedCount = var1;
         }
      }

      public void setOrderPreserved(boolean var1) {
         this.orderPreserved = var1;
         this.invalidateStructure();
      }
   }

   static class Bounds {
      public int after;
      public int before;
      public int flexibility;

      private Bounds() {
         this.reset();
      }

      // $FF: synthetic method
      Bounds(Object var1) {
         this();
      }

      protected int getOffset(TableLayout var1, TableLayout.Child var2, TableLayout.Alignment var3, int var4, boolean var5) {
         return this.before - var3.getAlignmentValue(var2, var4);
      }

      protected void include(int var1, int var2) {
         this.before = Math.max(this.before, var1);
         this.after = Math.max(this.after, var2);
      }

      protected final void include(TableLayout var1, TableLayout.Child var2, TableLayout.Spec var3, TableLayout.Axis var4, int var5) {
         this.flexibility &= var3.getFlexibility();
         int var6 = var3.getAbsoluteAlignment(var4.horizontal).getAlignmentValue(var2, var5);
         this.include(var6, var5 - var6);
      }

      protected void reset() {
         this.before = Integer.MIN_VALUE;
         this.after = Integer.MIN_VALUE;
         this.flexibility = 2;
      }

      protected int size(boolean var1) {
         return !var1 && TableLayout.canStretch(this.flexibility) ? 100000 : this.before + this.after;
      }
   }

   public class Child {
      private TLRPC.TL_pageTableCell cell;
      private int fixedHeight;
      private int index;
      private TableLayout.LayoutParams layoutParams;
      private int measuredHeight;
      private int measuredWidth;
      public int textHeight;
      public ArticleViewer.DrawingText textLayout;
      public int textLeft;
      public int textWidth;
      public int textX;
      public int textY;
      public int x;
      public int y;

      public Child(int var2) {
         this.index = var2;
      }

      public void draw(Canvas var1) {
         if (this.cell != null) {
            int var2 = this.x;
            int var3 = this.measuredWidth;
            int var4 = TableLayout.this.getMeasuredWidth();
            boolean var5 = false;
            boolean var16;
            if (var2 + var3 == var4) {
               var16 = true;
            } else {
               var16 = false;
            }

            boolean var15;
            if (this.y + this.measuredHeight == TableLayout.this.getMeasuredHeight()) {
               var15 = true;
            } else {
               var15 = false;
            }

            var2 = AndroidUtilities.dp(3.0F);
            float var8;
            int var9;
            int var17;
            if (this.cell.header || TableLayout.this.isStriped && this.layoutParams.rowSpec.span.min % 2 == 0) {
               float[] var6;
               float[] var7;
               if (this.x == 0 && this.y == 0) {
                  var6 = TableLayout.this.radii;
                  var7 = TableLayout.this.radii;
                  var8 = (float)var2;
                  var7[1] = var8;
                  var6[0] = var8;
                  var5 = true;
               } else {
                  var6 = TableLayout.this.radii;
                  TableLayout.this.radii[1] = 0.0F;
                  var6[0] = 0.0F;
               }

               if (var16 && this.y == 0) {
                  var7 = TableLayout.this.radii;
                  var6 = TableLayout.this.radii;
                  var8 = (float)var2;
                  var6[3] = var8;
                  var7[2] = var8;
                  var5 = true;
               } else {
                  var6 = TableLayout.this.radii;
                  TableLayout.this.radii[3] = 0.0F;
                  var6[2] = 0.0F;
               }

               if (var16 && var15) {
                  var7 = TableLayout.this.radii;
                  var6 = TableLayout.this.radii;
                  var8 = (float)var2;
                  var6[5] = var8;
                  var7[4] = var8;
                  var5 = true;
               } else {
                  var6 = TableLayout.this.radii;
                  TableLayout.this.radii[5] = 0.0F;
                  var6[4] = 0.0F;
               }

               if (this.x == 0 && var15) {
                  var6 = TableLayout.this.radii;
                  var7 = TableLayout.this.radii;
                  var8 = (float)var2;
                  var7[7] = var8;
                  var6[6] = var8;
                  var5 = true;
               } else {
                  var6 = TableLayout.this.radii;
                  TableLayout.this.radii[7] = 0.0F;
                  var6[6] = 0.0F;
               }

               if (var5) {
                  RectF var19 = TableLayout.this.rect;
                  var17 = this.x;
                  var8 = (float)var17;
                  var9 = this.y;
                  var19.set(var8, (float)var9, (float)(var17 + this.measuredWidth), (float)(var9 + this.measuredHeight));
                  TableLayout.this.backgroundPath.reset();
                  TableLayout.this.backgroundPath.addRoundRect(TableLayout.this.rect, TableLayout.this.radii, Direction.CW);
                  if (this.cell.header) {
                     var1.drawPath(TableLayout.this.backgroundPath, TableLayout.this.delegate.getHeaderPaint());
                  } else {
                     var1.drawPath(TableLayout.this.backgroundPath, TableLayout.this.delegate.getStripPaint());
                  }
               } else if (this.cell.header) {
                  var17 = this.x;
                  var8 = (float)var17;
                  var9 = this.y;
                  var1.drawRect(var8, (float)var9, (float)(var17 + this.measuredWidth), (float)(var9 + this.measuredHeight), TableLayout.this.delegate.getHeaderPaint());
               } else {
                  var9 = this.x;
                  var8 = (float)var9;
                  var17 = this.y;
                  var1.drawRect(var8, (float)var17, (float)(var9 + this.measuredWidth), (float)(var17 + this.measuredHeight), TableLayout.this.delegate.getStripPaint());
               }
            }

            if (this.textLayout != null) {
               var1.save();
               var1.translate((float)this.getTextX(), (float)this.getTextY());
               this.textLayout.draw(var1);
               var1.restore();
            }

            if (TableLayout.this.drawLines) {
               Paint var21 = TableLayout.this.delegate.getLinePaint();
               Paint var18 = TableLayout.this.delegate.getLinePaint();
               float var10 = var21.getStrokeWidth() / 2.0F;
               float var11 = var18.getStrokeWidth() / 2.0F;
               var9 = this.x;
               float var12;
               float var13;
               if (var9 == 0) {
                  var17 = this.y;
                  var12 = (float)var17;
                  var13 = (float)(this.measuredHeight + var17);
                  var8 = var12;
                  if (var17 == 0) {
                     var8 = var12 + (float)var2;
                  }

                  var12 = var13;
                  if (var13 == (float)TableLayout.this.getMeasuredHeight()) {
                     var12 = var13 - (float)var2;
                  }

                  var17 = this.x;
                  var1.drawLine((float)var17 + var10, var8, (float)var17 + var10, var12, var21);
               } else {
                  var8 = (float)var9;
                  var17 = this.y;
                  var1.drawLine(var8 - var11, (float)var17, (float)var9 - var11, (float)(var17 + this.measuredHeight), var18);
               }

               var17 = this.y;
               if (var17 == 0) {
                  var17 = this.x;
                  var12 = (float)var17;
                  var13 = (float)(this.measuredWidth + var17);
                  var8 = var12;
                  if (var17 == 0) {
                     var8 = var12 + (float)var2;
                  }

                  var12 = var13;
                  if (var13 == (float)TableLayout.this.getMeasuredWidth()) {
                     var12 = var13 - (float)var2;
                  }

                  var17 = this.y;
                  var1.drawLine(var8, (float)var17 + var10, var12, (float)var17 + var10, var21);
               } else {
                  var9 = this.x;
                  var1.drawLine((float)var9, (float)var17 - var11, (float)(var9 + this.measuredWidth), (float)var17 - var11, var18);
               }

               label131: {
                  if (var16) {
                     var17 = this.y;
                     if (var17 == 0) {
                        var8 = (float)(var17 + var2);
                        break label131;
                     }
                  }

                  var8 = (float)this.y - var10;
               }

               if (var16 && var15) {
                  var12 = (float)(this.y + this.measuredHeight - var2);
               } else {
                  var12 = (float)(this.y + this.measuredHeight) - var10;
               }

               var17 = this.x;
               var9 = this.measuredWidth;
               var1.drawLine((float)(var17 + var9) - var10, var8, (float)(var17 + var9) - var10, var12, var21);
               var17 = this.x;
               if (var17 == 0 && var15) {
                  var8 = (float)(var17 + var2);
               } else {
                  var8 = (float)this.x - var10;
               }

               if (var16 && var15) {
                  var12 = (float)(this.x + this.measuredWidth - var2);
               } else {
                  var12 = (float)(this.x + this.measuredWidth) - var10;
               }

               var9 = this.y;
               var17 = this.measuredHeight;
               var1.drawLine(var8, (float)(var9 + var17) - var10, var12, (float)(var9 + var17) - var10, var21);
               RectF var20;
               if (this.x == 0 && this.y == 0) {
                  var20 = TableLayout.this.rect;
                  var17 = this.x;
                  var12 = (float)var17;
                  var9 = this.y;
                  var13 = (float)var9;
                  var8 = (float)var17;
                  var11 = (float)(var2 * 2);
                  var20.set(var12 + var10, var13 + var10, var8 + var10 + var11, (float)var9 + var10 + var11);
                  var1.drawArc(TableLayout.this.rect, -180.0F, 90.0F, false, var21);
               }

               int var14;
               if (var16 && this.y == 0) {
                  var20 = TableLayout.this.rect;
                  var14 = this.x;
                  var17 = this.measuredWidth;
                  var12 = (float)(var14 + var17);
                  var8 = (float)(var2 * 2);
                  var9 = this.y;
                  var20.set(var12 - var10 - var8, (float)var9 + var10, (float)(var14 + var17) - var10, (float)var9 + var10 + var8);
                  var1.drawArc(TableLayout.this.rect, 0.0F, -90.0F, false, var21);
               }

               if (this.x == 0 && var15) {
                  var20 = TableLayout.this.rect;
                  var9 = this.x;
                  var8 = (float)var9;
                  var17 = this.y;
                  var14 = this.measuredHeight;
                  var13 = (float)(var17 + var14);
                  var12 = (float)(var2 * 2);
                  var20.set(var8 + var10, var13 - var10 - var12, (float)var9 + var10 + var12, (float)(var17 + var14) - var10);
                  var1.drawArc(TableLayout.this.rect, 180.0F, -90.0F, false, var21);
               }

               if (var16 && var15) {
                  var20 = TableLayout.this.rect;
                  var17 = this.x;
                  var4 = this.measuredWidth;
                  var12 = (float)(var17 + var4);
                  var8 = (float)(var2 * 2);
                  var3 = this.y;
                  var2 = this.measuredHeight;
                  var20.set(var12 - var10 - var8, (float)(var3 + var2) - var10 - var8, (float)(var17 + var4) - var10, (float)(var3 + var2) - var10);
                  var1.drawArc(TableLayout.this.rect, 0.0F, 90.0F, false, var21);
               }
            }

         }
      }

      public TableLayout.LayoutParams getLayoutParams() {
         return this.layoutParams;
      }

      public int getMeasuredHeight() {
         return this.measuredHeight;
      }

      public int getMeasuredWidth() {
         return this.measuredWidth;
      }

      public int getTextX() {
         return this.x + this.textX;
      }

      public int getTextY() {
         return this.y + this.textY;
      }

      public void layout(int var1, int var2, int var3, int var4) {
         this.x = var1;
         this.y = var2;
      }

      public void measure(int var1, int var2, boolean var3) {
         this.measuredWidth = var1;
         this.measuredHeight = var2;
         if (var3) {
            this.fixedHeight = this.measuredHeight;
         }

         TLRPC.TL_pageTableCell var4 = this.cell;
         if (var4 != null) {
            if (var4.valign_middle) {
               this.textY = (this.measuredHeight - this.textHeight) / 2;
            } else if (var4.valign_bottom) {
               this.textY = this.measuredHeight - this.textHeight - TableLayout.this.itemPaddingTop;
            } else {
               this.textY = TableLayout.this.itemPaddingTop;
            }

            ArticleViewer.DrawingText var5 = this.textLayout;
            if (var5 != null) {
               var1 = var5.getLineCount();
               if (!var3) {
                  label50: {
                     if (var1 <= 1) {
                        if (var1 <= 0) {
                           break label50;
                        }

                        var4 = this.cell;
                        if (!var4.align_center && !var4.align_right) {
                           break label50;
                        }
                     }

                     this.setTextLayout(TableLayout.this.delegate.createTextLayout(this.cell, this.measuredWidth - TableLayout.this.itemPaddingLeft * 2));
                     this.fixedHeight = this.textHeight + TableLayout.this.itemPaddingTop * 2;
                  }
               }

               var1 = this.textLeft;
               if (var1 != 0) {
                  this.textX = -var1;
                  var4 = this.cell;
                  if (var4.align_right) {
                     this.textX += this.measuredWidth - this.textWidth - TableLayout.this.itemPaddingLeft;
                  } else if (var4.align_center) {
                     this.textX += Math.round((float)((this.measuredWidth - this.textWidth) / 2));
                  } else {
                     this.textX += TableLayout.this.itemPaddingLeft;
                  }
               } else {
                  this.textX = TableLayout.this.itemPaddingLeft;
               }
            }
         }

      }

      public void setFixedHeight(int var1) {
         this.measuredHeight = this.fixedHeight;
         TLRPC.TL_pageTableCell var2 = this.cell;
         if (var2.valign_middle) {
            this.textY = (this.measuredHeight - this.textHeight) / 2;
         } else if (var2.valign_bottom) {
            this.textY = this.measuredHeight - this.textHeight - TableLayout.this.itemPaddingTop;
         }

      }

      public void setTextLayout(ArticleViewer.DrawingText var1) {
         this.textLayout = var1;
         int var2 = 0;
         if (var1 != null) {
            this.textWidth = 0;
            this.textLeft = 0;

            for(int var3 = var1.getLineCount(); var2 < var3; ++var2) {
               float var4 = var1.getLineLeft(var2);
               int var5;
               if (var2 == 0) {
                  var5 = (int)Math.ceil((double)var4);
               } else {
                  var5 = Math.min(this.textLeft, (int)Math.ceil((double)var4));
               }

               this.textLeft = var5;
               this.textWidth = (int)Math.ceil((double)Math.max(var1.getLineWidth(var2), (float)this.textWidth));
            }

            this.textHeight = var1.getHeight();
         } else {
            this.textLeft = 0;
            this.textWidth = 0;
            this.textHeight = 0;
         }

      }
   }

   static final class Interval {
      public final int max;
      public final int min;

      public Interval(int var1, int var2) {
         this.min = var1;
         this.max = var2;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && TableLayout.Interval.class == var1.getClass()) {
            TableLayout.Interval var2 = (TableLayout.Interval)var1;
            if (this.max != var2.max) {
               return false;
            } else {
               return this.min == var2.min;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.min * 31 + this.max;
      }

      TableLayout.Interval inverse() {
         return new TableLayout.Interval(this.max, this.min);
      }

      int size() {
         return this.max - this.min;
      }
   }

   public static class LayoutParams extends MarginLayoutParams {
      private static final int DEFAULT_HEIGHT = -2;
      private static final int DEFAULT_MARGIN = Integer.MIN_VALUE;
      private static final TableLayout.Interval DEFAULT_SPAN = new TableLayout.Interval(Integer.MIN_VALUE, -2147483647);
      private static final int DEFAULT_SPAN_SIZE;
      private static final int DEFAULT_WIDTH = -2;
      public TableLayout.Spec columnSpec;
      public TableLayout.Spec rowSpec;

      static {
         DEFAULT_SPAN_SIZE = DEFAULT_SPAN.size();
      }

      public LayoutParams() {
         TableLayout.Spec var1 = TableLayout.Spec.UNDEFINED;
         this(var1, var1);
      }

      private LayoutParams(int var1, int var2, int var3, int var4, int var5, int var6, TableLayout.Spec var7, TableLayout.Spec var8) {
         super(var1, var2);
         TableLayout.Spec var9 = TableLayout.Spec.UNDEFINED;
         this.rowSpec = var9;
         this.columnSpec = var9;
         this.setMargins(var3, var4, var5, var6);
         this.rowSpec = var7;
         this.columnSpec = var8;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
         TableLayout.Spec var2 = TableLayout.Spec.UNDEFINED;
         this.rowSpec = var2;
         this.columnSpec = var2;
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
         TableLayout.Spec var2 = TableLayout.Spec.UNDEFINED;
         this.rowSpec = var2;
         this.columnSpec = var2;
      }

      public LayoutParams(TableLayout.LayoutParams var1) {
         super(var1);
         TableLayout.Spec var2 = TableLayout.Spec.UNDEFINED;
         this.rowSpec = var2;
         this.columnSpec = var2;
         this.rowSpec = var1.rowSpec;
         this.columnSpec = var1.columnSpec;
      }

      public LayoutParams(TableLayout.Spec var1, TableLayout.Spec var2) {
         this(-2, -2, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, var1, var2);
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && TableLayout.LayoutParams.class == var1.getClass()) {
            TableLayout.LayoutParams var2 = (TableLayout.LayoutParams)var1;
            if (!this.columnSpec.equals(var2.columnSpec)) {
               return false;
            } else {
               return this.rowSpec.equals(var2.rowSpec);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.rowSpec.hashCode() * 31 + this.columnSpec.hashCode();
      }

      final void setColumnSpecSpan(TableLayout.Interval var1) {
         this.columnSpec = this.columnSpec.copyWriteSpan(var1);
      }

      public void setGravity(int var1) {
         this.rowSpec = this.rowSpec.copyWriteAlignment(TableLayout.getAlignment(var1, false));
         this.columnSpec = this.columnSpec.copyWriteAlignment(TableLayout.getAlignment(var1, true));
      }

      final void setRowSpecSpan(TableLayout.Interval var1) {
         this.rowSpec = this.rowSpec.copyWriteSpan(var1);
      }
   }

   static final class MutableInt {
      public int value;

      public MutableInt() {
         this.reset();
      }

      public MutableInt(int var1) {
         this.value = var1;
      }

      public void reset() {
         this.value = Integer.MIN_VALUE;
      }
   }

   static final class PackedMap {
      public final int[] index;
      public final Object[] keys;
      public final Object[] values;

      private PackedMap(Object[] var1, Object[] var2) {
         this.index = createIndex(var1);
         this.keys = compact(var1, this.index);
         this.values = compact(var2, this.index);
      }

      // $FF: synthetic method
      PackedMap(Object[] var1, Object[] var2, Object var3) {
         this(var1, var2);
      }

      private static Object[] compact(Object[] var0, int[] var1) {
         int var2 = var0.length;
         Object[] var3 = (Object[])Array.newInstance(var0.getClass().getComponentType(), TableLayout.max2(var1, -1) + 1);

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var1[var4]] = var0[var4];
         }

         return var3;
      }

      private static int[] createIndex(Object[] var0) {
         int var1 = var0.length;
         int[] var2 = new int[var1];
         HashMap var3 = new HashMap();

         for(int var4 = 0; var4 < var1; ++var4) {
            Object var5 = var0[var4];
            Integer var6 = (Integer)var3.get(var5);
            Integer var7 = var6;
            if (var6 == null) {
               var7 = var3.size();
               var3.put(var5, var7);
            }

            var2[var4] = var7;
         }

         return var2;
      }

      public Object getValue(int var1) {
         return this.values[this.index[var1]];
      }
   }

   public static class Spec {
      static final float DEFAULT_WEIGHT = 0.0F;
      static final TableLayout.Spec UNDEFINED = TableLayout.spec(Integer.MIN_VALUE);
      final TableLayout.Alignment alignment;
      final TableLayout.Interval span;
      final boolean startDefined;
      float weight;

      private Spec(boolean var1, int var2, int var3, TableLayout.Alignment var4, float var5) {
         this(var1, new TableLayout.Interval(var2, var3 + var2), var4, var5);
      }

      // $FF: synthetic method
      Spec(boolean var1, int var2, int var3, TableLayout.Alignment var4, float var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }

      private Spec(boolean var1, TableLayout.Interval var2, TableLayout.Alignment var3, float var4) {
         this.startDefined = var1;
         this.span = var2;
         this.alignment = var3;
         this.weight = var4;
      }

      // $FF: synthetic method
      Spec(boolean var1, TableLayout.Interval var2, TableLayout.Alignment var3, float var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      private TableLayout.Alignment getAbsoluteAlignment(boolean var1) {
         TableLayout.Alignment var2 = this.alignment;
         if (var2 != TableLayout.UNDEFINED_ALIGNMENT) {
            return var2;
         } else if (this.weight == 0.0F) {
            if (var1) {
               var2 = TableLayout.START;
            } else {
               var2 = TableLayout.BASELINE;
            }

            return var2;
         } else {
            return TableLayout.FILL;
         }
      }

      final TableLayout.Spec copyWriteAlignment(TableLayout.Alignment var1) {
         return new TableLayout.Spec(this.startDefined, this.span, var1, this.weight);
      }

      final TableLayout.Spec copyWriteSpan(TableLayout.Interval var1) {
         return new TableLayout.Spec(this.startDefined, var1, this.alignment, this.weight);
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && TableLayout.Spec.class == var1.getClass()) {
            TableLayout.Spec var2 = (TableLayout.Spec)var1;
            if (!this.alignment.equals(var2.alignment)) {
               return false;
            } else {
               return this.span.equals(var2.span);
            }
         } else {
            return false;
         }
      }

      final int getFlexibility() {
         byte var1;
         if (this.alignment == TableLayout.UNDEFINED_ALIGNMENT && this.weight == 0.0F) {
            var1 = 0;
         } else {
            var1 = 2;
         }

         return var1;
      }

      public int hashCode() {
         return this.span.hashCode() * 31 + this.alignment.hashCode();
      }
   }

   public interface TableLayoutDelegate {
      ArticleViewer.DrawingText createTextLayout(TLRPC.TL_pageTableCell var1, int var2);

      Paint getHalfLinePaint();

      Paint getHeaderPaint();

      Paint getLinePaint();

      Paint getStripPaint();
   }
}
