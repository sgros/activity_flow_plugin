package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.View;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParserException;

public class ConstraintSet {
   private static final int[] VISIBILITY_FLAGS = new int[]{0, 4, 8};
   private static SparseIntArray mapToConstant = new SparseIntArray();
   private HashMap mConstraints = new HashMap();

   static {
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_toLeftOf, 25);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_toRightOf, 26);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_toLeftOf, 29);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_toRightOf, 30);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_toTopOf, 36);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_toBottomOf, 35);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_toTopOf, 4);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_toBottomOf, 3);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBaseline_toBaselineOf, 1);
      mapToConstant.append(R.styleable.ConstraintSet_layout_editor_absoluteX, 6);
      mapToConstant.append(R.styleable.ConstraintSet_layout_editor_absoluteY, 7);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_begin, 17);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_end, 18);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_percent, 19);
      mapToConstant.append(R.styleable.ConstraintSet_android_orientation, 27);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintStart_toEndOf, 32);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintStart_toStartOf, 33);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintEnd_toStartOf, 10);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintEnd_toEndOf, 9);
      mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginLeft, 13);
      mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginTop, 16);
      mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginRight, 14);
      mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginBottom, 11);
      mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginStart, 15);
      mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginEnd, 12);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_weight, 40);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_weight, 39);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_chainStyle, 41);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_chainStyle, 42);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_bias, 20);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_bias, 37);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintDimensionRatio, 5);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_creator, 64);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_creator, 64);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_creator, 64);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_creator, 64);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBaseline_creator, 64);
      mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginLeft, 24);
      mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginRight, 28);
      mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginStart, 31);
      mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginEnd, 8);
      mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginTop, 34);
      mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginBottom, 2);
      mapToConstant.append(R.styleable.ConstraintSet_android_layout_width, 23);
      mapToConstant.append(R.styleable.ConstraintSet_android_layout_height, 21);
      mapToConstant.append(R.styleable.ConstraintSet_android_visibility, 22);
      mapToConstant.append(R.styleable.ConstraintSet_android_alpha, 43);
      mapToConstant.append(R.styleable.ConstraintSet_android_elevation, 44);
      mapToConstant.append(R.styleable.ConstraintSet_android_rotationX, 45);
      mapToConstant.append(R.styleable.ConstraintSet_android_rotationY, 46);
      mapToConstant.append(R.styleable.ConstraintSet_android_rotation, 60);
      mapToConstant.append(R.styleable.ConstraintSet_android_scaleX, 47);
      mapToConstant.append(R.styleable.ConstraintSet_android_scaleY, 48);
      mapToConstant.append(R.styleable.ConstraintSet_android_transformPivotX, 49);
      mapToConstant.append(R.styleable.ConstraintSet_android_transformPivotY, 50);
      mapToConstant.append(R.styleable.ConstraintSet_android_translationX, 51);
      mapToConstant.append(R.styleable.ConstraintSet_android_translationY, 52);
      mapToConstant.append(R.styleable.ConstraintSet_android_translationZ, 53);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_default, 54);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_default, 55);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_max, 56);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_max, 57);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_min, 58);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_min, 59);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintCircle, 61);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintCircleRadius, 62);
      mapToConstant.append(R.styleable.ConstraintSet_layout_constraintCircleAngle, 63);
      mapToConstant.append(R.styleable.ConstraintSet_android_id, 38);
   }

   private ConstraintSet.Constraint fillFromAttributeList(Context var1, AttributeSet var2) {
      ConstraintSet.Constraint var3 = new ConstraintSet.Constraint();
      TypedArray var4 = var1.obtainStyledAttributes(var2, R.styleable.ConstraintSet);
      this.populateConstraint(var3, var4);
      var4.recycle();
      return var3;
   }

   private static int lookupID(TypedArray var0, int var1, int var2) {
      int var3 = var0.getResourceId(var1, var2);
      var2 = var3;
      if (var3 == -1) {
         var2 = var0.getInt(var1, -1);
      }

      return var2;
   }

   private void populateConstraint(ConstraintSet.Constraint var1, TypedArray var2) {
      int var3 = var2.getIndexCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2.getIndex(var4);
         int var6 = mapToConstant.get(var5);
         switch(var6) {
         case 1:
            var1.baselineToBaseline = lookupID(var2, var5, var1.baselineToBaseline);
            break;
         case 2:
            var1.bottomMargin = var2.getDimensionPixelSize(var5, var1.bottomMargin);
            break;
         case 3:
            var1.bottomToBottom = lookupID(var2, var5, var1.bottomToBottom);
            break;
         case 4:
            var1.bottomToTop = lookupID(var2, var5, var1.bottomToTop);
            break;
         case 5:
            var1.dimensionRatio = var2.getString(var5);
            break;
         case 6:
            var1.editorAbsoluteX = var2.getDimensionPixelOffset(var5, var1.editorAbsoluteX);
            break;
         case 7:
            var1.editorAbsoluteY = var2.getDimensionPixelOffset(var5, var1.editorAbsoluteY);
            break;
         case 8:
            var1.endMargin = var2.getDimensionPixelSize(var5, var1.endMargin);
            break;
         case 9:
            var1.endToEnd = lookupID(var2, var5, var1.endToEnd);
            break;
         case 10:
            var1.endToStart = lookupID(var2, var5, var1.endToStart);
            break;
         case 11:
            var1.goneBottomMargin = var2.getDimensionPixelSize(var5, var1.goneBottomMargin);
            break;
         case 12:
            var1.goneEndMargin = var2.getDimensionPixelSize(var5, var1.goneEndMargin);
            break;
         case 13:
            var1.goneLeftMargin = var2.getDimensionPixelSize(var5, var1.goneLeftMargin);
            break;
         case 14:
            var1.goneRightMargin = var2.getDimensionPixelSize(var5, var1.goneRightMargin);
            break;
         case 15:
            var1.goneStartMargin = var2.getDimensionPixelSize(var5, var1.goneStartMargin);
            break;
         case 16:
            var1.goneTopMargin = var2.getDimensionPixelSize(var5, var1.goneTopMargin);
            break;
         case 17:
            var1.guideBegin = var2.getDimensionPixelOffset(var5, var1.guideBegin);
            break;
         case 18:
            var1.guideEnd = var2.getDimensionPixelOffset(var5, var1.guideEnd);
            break;
         case 19:
            var1.guidePercent = var2.getFloat(var5, var1.guidePercent);
            break;
         case 20:
            var1.horizontalBias = var2.getFloat(var5, var1.horizontalBias);
            break;
         case 21:
            var1.mHeight = var2.getLayoutDimension(var5, var1.mHeight);
            break;
         case 22:
            var1.visibility = var2.getInt(var5, var1.visibility);
            var1.visibility = VISIBILITY_FLAGS[var1.visibility];
            break;
         case 23:
            var1.mWidth = var2.getLayoutDimension(var5, var1.mWidth);
            break;
         case 24:
            var1.leftMargin = var2.getDimensionPixelSize(var5, var1.leftMargin);
            break;
         case 25:
            var1.leftToLeft = lookupID(var2, var5, var1.leftToLeft);
            break;
         case 26:
            var1.leftToRight = lookupID(var2, var5, var1.leftToRight);
            break;
         case 27:
            var1.orientation = var2.getInt(var5, var1.orientation);
            break;
         case 28:
            var1.rightMargin = var2.getDimensionPixelSize(var5, var1.rightMargin);
            break;
         case 29:
            var1.rightToLeft = lookupID(var2, var5, var1.rightToLeft);
            break;
         case 30:
            var1.rightToRight = lookupID(var2, var5, var1.rightToRight);
            break;
         case 31:
            var1.startMargin = var2.getDimensionPixelSize(var5, var1.startMargin);
            break;
         case 32:
            var1.startToEnd = lookupID(var2, var5, var1.startToEnd);
            break;
         case 33:
            var1.startToStart = lookupID(var2, var5, var1.startToStart);
            break;
         case 34:
            var1.topMargin = var2.getDimensionPixelSize(var5, var1.topMargin);
            break;
         case 35:
            var1.topToBottom = lookupID(var2, var5, var1.topToBottom);
            break;
         case 36:
            var1.topToTop = lookupID(var2, var5, var1.topToTop);
            break;
         case 37:
            var1.verticalBias = var2.getFloat(var5, var1.verticalBias);
            break;
         case 38:
            var1.mViewId = var2.getResourceId(var5, var1.mViewId);
            break;
         case 39:
            var1.horizontalWeight = var2.getFloat(var5, var1.horizontalWeight);
            break;
         case 40:
            var1.verticalWeight = var2.getFloat(var5, var1.verticalWeight);
            break;
         case 41:
            var1.horizontalChainStyle = var2.getInt(var5, var1.horizontalChainStyle);
            break;
         case 42:
            var1.verticalChainStyle = var2.getInt(var5, var1.verticalChainStyle);
            break;
         case 43:
            var1.alpha = var2.getFloat(var5, var1.alpha);
            break;
         case 44:
            var1.applyElevation = true;
            var1.elevation = var2.getDimension(var5, var1.elevation);
            break;
         case 45:
            var1.rotationX = var2.getFloat(var5, var1.rotationX);
            break;
         case 46:
            var1.rotationY = var2.getFloat(var5, var1.rotationY);
            break;
         case 47:
            var1.scaleX = var2.getFloat(var5, var1.scaleX);
            break;
         case 48:
            var1.scaleY = var2.getFloat(var5, var1.scaleY);
            break;
         case 49:
            var1.transformPivotX = var2.getFloat(var5, var1.transformPivotX);
            break;
         case 50:
            var1.transformPivotY = var2.getFloat(var5, var1.transformPivotY);
            break;
         case 51:
            var1.translationX = var2.getDimension(var5, var1.translationX);
            break;
         case 52:
            var1.translationY = var2.getDimension(var5, var1.translationY);
            break;
         case 53:
            var1.translationZ = var2.getDimension(var5, var1.translationZ);
            break;
         default:
            StringBuilder var7;
            switch(var6) {
            case 60:
               var1.rotation = var2.getFloat(var5, var1.rotation);
               break;
            case 61:
               var1.circleConstraint = lookupID(var2, var5, var1.circleConstraint);
               break;
            case 62:
               var1.circleRadius = var2.getDimensionPixelSize(var5, var1.circleRadius);
               break;
            case 63:
               var1.circleAngle = var2.getFloat(var5, var1.circleAngle);
               break;
            case 64:
               var7 = new StringBuilder();
               var7.append("unused attribute 0x");
               var7.append(Integer.toHexString(var5));
               var7.append("   ");
               var7.append(mapToConstant.get(var5));
               Log.w("ConstraintSet", var7.toString());
               break;
            default:
               var7 = new StringBuilder();
               var7.append("Unknown attribute 0x");
               var7.append(Integer.toHexString(var5));
               var7.append("   ");
               var7.append(mapToConstant.get(var5));
               Log.w("ConstraintSet", var7.toString());
            }
         }
      }

   }

   void applyToInternal(ConstraintLayout var1) {
      int var2 = var1.getChildCount();
      HashSet var3 = new HashSet(this.mConstraints.keySet());

      Barrier var8;
      for(int var4 = 0; var4 < var2; ++var4) {
         View var5 = var1.getChildAt(var4);
         int var6 = var5.getId();
         if (var6 == -1) {
            throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
         }

         if (this.mConstraints.containsKey(var6)) {
            var3.remove(var6);
            ConstraintSet.Constraint var7 = (ConstraintSet.Constraint)this.mConstraints.get(var6);
            if (var7.mHelperType != -1 && var7.mHelperType == 1) {
               var8 = (Barrier)var5;
               var8.setId(var6);
               var8.setReferencedIds(var7.mReferenceIds);
               var8.setType(var7.mBarrierDirection);
               var7.applyTo(var1.generateDefaultLayoutParams());
            }

            ConstraintLayout.LayoutParams var14 = (ConstraintLayout.LayoutParams)var5.getLayoutParams();
            var7.applyTo(var14);
            var5.setLayoutParams(var14);
            var5.setVisibility(var7.visibility);
            if (VERSION.SDK_INT >= 17) {
               var5.setAlpha(var7.alpha);
               var5.setRotation(var7.rotation);
               var5.setRotationX(var7.rotationX);
               var5.setRotationY(var7.rotationY);
               var5.setScaleX(var7.scaleX);
               var5.setScaleY(var7.scaleY);
               if (!Float.isNaN(var7.transformPivotX)) {
                  var5.setPivotX(var7.transformPivotX);
               }

               if (!Float.isNaN(var7.transformPivotY)) {
                  var5.setPivotY(var7.transformPivotY);
               }

               var5.setTranslationX(var7.translationX);
               var5.setTranslationY(var7.translationY);
               if (VERSION.SDK_INT >= 21) {
                  var5.setTranslationZ(var7.translationZ);
                  if (var7.applyElevation) {
                     var5.setElevation(var7.elevation);
                  }
               }
            }
         }
      }

      Iterator var10 = var3.iterator();

      while(var10.hasNext()) {
         Integer var12 = (Integer)var10.next();
         ConstraintSet.Constraint var11 = (ConstraintSet.Constraint)this.mConstraints.get(var12);
         if (var11.mHelperType != -1 && var11.mHelperType == 1) {
            var8 = new Barrier(var1.getContext());
            var8.setId(var12);
            var8.setReferencedIds(var11.mReferenceIds);
            var8.setType(var11.mBarrierDirection);
            ConstraintLayout.LayoutParams var9 = var1.generateDefaultLayoutParams();
            var11.applyTo(var9);
            var1.addView(var8, var9);
         }

         if (var11.mIsGuideline) {
            Guideline var15 = new Guideline(var1.getContext());
            var15.setId(var12);
            ConstraintLayout.LayoutParams var13 = var1.generateDefaultLayoutParams();
            var11.applyTo(var13);
            var1.addView(var15, var13);
         }
      }

   }

   public void clone(Constraints var1) {
      int var2 = var1.getChildCount();
      this.mConstraints.clear();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = var1.getChildAt(var3);
         Constraints.LayoutParams var5 = (Constraints.LayoutParams)var4.getLayoutParams();
         int var6 = var4.getId();
         if (var6 == -1) {
            throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
         }

         if (!this.mConstraints.containsKey(var6)) {
            this.mConstraints.put(var6, new ConstraintSet.Constraint());
         }

         ConstraintSet.Constraint var7 = (ConstraintSet.Constraint)this.mConstraints.get(var6);
         if (var4 instanceof ConstraintHelper) {
            var7.fillFromConstraints((ConstraintHelper)var4, var6, var5);
         }

         var7.fillFromConstraints(var6, var5);
      }

   }

   public void load(Context var1, int var2) {
      XmlResourceParser var3 = var1.getResources().getXml(var2);

      XmlPullParserException var18;
      label77: {
         IOException var10000;
         label63: {
            boolean var10001;
            try {
               var2 = var3.getEventType();
            } catch (XmlPullParserException var14) {
               var18 = var14;
               var10001 = false;
               break label77;
            } catch (IOException var15) {
               var10000 = var15;
               var10001 = false;
               break label63;
            }

            label60:
            while(true) {
               if (var2 == 1) {
                  return;
               }

               if (var2 != 0) {
                  switch(var2) {
                  case 2:
                     ConstraintSet.Constraint var5;
                     try {
                        String var4 = var3.getName();
                        var5 = this.fillFromAttributeList(var1, Xml.asAttributeSet(var3));
                        if (var4.equalsIgnoreCase("Guideline")) {
                           var5.mIsGuideline = true;
                        }
                     } catch (XmlPullParserException var12) {
                        var18 = var12;
                        var10001 = false;
                        break label77;
                     } catch (IOException var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label60;
                     }

                     try {
                        this.mConstraints.put(var5.mViewId, var5);
                     } catch (XmlPullParserException var10) {
                        var18 = var10;
                        var10001 = false;
                        break label77;
                     } catch (IOException var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label60;
                     }
                  case 3:
                  }
               } else {
                  try {
                     var3.getName();
                  } catch (XmlPullParserException var8) {
                     var18 = var8;
                     var10001 = false;
                     break label77;
                  } catch (IOException var9) {
                     var10000 = var9;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var2 = var3.next();
               } catch (XmlPullParserException var6) {
                  var18 = var6;
                  var10001 = false;
                  break label77;
               } catch (IOException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }
            }
         }

         IOException var16 = var10000;
         var16.printStackTrace();
         return;
      }

      XmlPullParserException var17 = var18;
      var17.printStackTrace();
   }

   private static class Constraint {
      public float alpha;
      public boolean applyElevation;
      public int baselineToBaseline;
      public int bottomMargin;
      public int bottomToBottom;
      public int bottomToTop;
      public float circleAngle;
      public int circleConstraint;
      public int circleRadius;
      public boolean constrainedHeight;
      public boolean constrainedWidth;
      public String dimensionRatio;
      public int editorAbsoluteX;
      public int editorAbsoluteY;
      public float elevation;
      public int endMargin;
      public int endToEnd;
      public int endToStart;
      public int goneBottomMargin;
      public int goneEndMargin;
      public int goneLeftMargin;
      public int goneRightMargin;
      public int goneStartMargin;
      public int goneTopMargin;
      public int guideBegin;
      public int guideEnd;
      public float guidePercent;
      public int heightDefault;
      public int heightMax;
      public int heightMin;
      public float heightPercent;
      public float horizontalBias;
      public int horizontalChainStyle;
      public float horizontalWeight;
      public int leftMargin;
      public int leftToLeft;
      public int leftToRight;
      public int mBarrierDirection;
      public int mHeight;
      public int mHelperType;
      boolean mIsGuideline;
      public int[] mReferenceIds;
      int mViewId;
      public int mWidth;
      public int orientation;
      public int rightMargin;
      public int rightToLeft;
      public int rightToRight;
      public float rotation;
      public float rotationX;
      public float rotationY;
      public float scaleX;
      public float scaleY;
      public int startMargin;
      public int startToEnd;
      public int startToStart;
      public int topMargin;
      public int topToBottom;
      public int topToTop;
      public float transformPivotX;
      public float transformPivotY;
      public float translationX;
      public float translationY;
      public float translationZ;
      public float verticalBias;
      public int verticalChainStyle;
      public float verticalWeight;
      public int visibility;
      public int widthDefault;
      public int widthMax;
      public int widthMin;
      public float widthPercent;

      private Constraint() {
         this.mIsGuideline = false;
         this.guideBegin = -1;
         this.guideEnd = -1;
         this.guidePercent = -1.0F;
         this.leftToLeft = -1;
         this.leftToRight = -1;
         this.rightToLeft = -1;
         this.rightToRight = -1;
         this.topToTop = -1;
         this.topToBottom = -1;
         this.bottomToTop = -1;
         this.bottomToBottom = -1;
         this.baselineToBaseline = -1;
         this.startToEnd = -1;
         this.startToStart = -1;
         this.endToStart = -1;
         this.endToEnd = -1;
         this.horizontalBias = 0.5F;
         this.verticalBias = 0.5F;
         this.dimensionRatio = null;
         this.circleConstraint = -1;
         this.circleRadius = 0;
         this.circleAngle = 0.0F;
         this.editorAbsoluteX = -1;
         this.editorAbsoluteY = -1;
         this.orientation = -1;
         this.leftMargin = -1;
         this.rightMargin = -1;
         this.topMargin = -1;
         this.bottomMargin = -1;
         this.endMargin = -1;
         this.startMargin = -1;
         this.visibility = 0;
         this.goneLeftMargin = -1;
         this.goneTopMargin = -1;
         this.goneRightMargin = -1;
         this.goneBottomMargin = -1;
         this.goneEndMargin = -1;
         this.goneStartMargin = -1;
         this.verticalWeight = 0.0F;
         this.horizontalWeight = 0.0F;
         this.horizontalChainStyle = 0;
         this.verticalChainStyle = 0;
         this.alpha = 1.0F;
         this.applyElevation = false;
         this.elevation = 0.0F;
         this.rotation = 0.0F;
         this.rotationX = 0.0F;
         this.rotationY = 0.0F;
         this.scaleX = 1.0F;
         this.scaleY = 1.0F;
         this.transformPivotX = Float.NaN;
         this.transformPivotY = Float.NaN;
         this.translationX = 0.0F;
         this.translationY = 0.0F;
         this.translationZ = 0.0F;
         this.constrainedWidth = false;
         this.constrainedHeight = false;
         this.widthDefault = 0;
         this.heightDefault = 0;
         this.widthMax = -1;
         this.heightMax = -1;
         this.widthMin = -1;
         this.heightMin = -1;
         this.widthPercent = 1.0F;
         this.heightPercent = 1.0F;
         this.mBarrierDirection = -1;
         this.mHelperType = -1;
      }

      // $FF: synthetic method
      Constraint(Object var1) {
         this();
      }

      private void fillFrom(int var1, ConstraintLayout.LayoutParams var2) {
         this.mViewId = var1;
         this.leftToLeft = var2.leftToLeft;
         this.leftToRight = var2.leftToRight;
         this.rightToLeft = var2.rightToLeft;
         this.rightToRight = var2.rightToRight;
         this.topToTop = var2.topToTop;
         this.topToBottom = var2.topToBottom;
         this.bottomToTop = var2.bottomToTop;
         this.bottomToBottom = var2.bottomToBottom;
         this.baselineToBaseline = var2.baselineToBaseline;
         this.startToEnd = var2.startToEnd;
         this.startToStart = var2.startToStart;
         this.endToStart = var2.endToStart;
         this.endToEnd = var2.endToEnd;
         this.horizontalBias = var2.horizontalBias;
         this.verticalBias = var2.verticalBias;
         this.dimensionRatio = var2.dimensionRatio;
         this.circleConstraint = var2.circleConstraint;
         this.circleRadius = var2.circleRadius;
         this.circleAngle = var2.circleAngle;
         this.editorAbsoluteX = var2.editorAbsoluteX;
         this.editorAbsoluteY = var2.editorAbsoluteY;
         this.orientation = var2.orientation;
         this.guidePercent = var2.guidePercent;
         this.guideBegin = var2.guideBegin;
         this.guideEnd = var2.guideEnd;
         this.mWidth = var2.width;
         this.mHeight = var2.height;
         this.leftMargin = var2.leftMargin;
         this.rightMargin = var2.rightMargin;
         this.topMargin = var2.topMargin;
         this.bottomMargin = var2.bottomMargin;
         this.verticalWeight = var2.verticalWeight;
         this.horizontalWeight = var2.horizontalWeight;
         this.verticalChainStyle = var2.verticalChainStyle;
         this.horizontalChainStyle = var2.horizontalChainStyle;
         this.constrainedWidth = var2.constrainedWidth;
         this.constrainedHeight = var2.constrainedHeight;
         this.widthDefault = var2.matchConstraintDefaultWidth;
         this.heightDefault = var2.matchConstraintDefaultHeight;
         this.constrainedWidth = var2.constrainedWidth;
         this.widthMax = var2.matchConstraintMaxWidth;
         this.heightMax = var2.matchConstraintMaxHeight;
         this.widthMin = var2.matchConstraintMinWidth;
         this.heightMin = var2.matchConstraintMinHeight;
         this.widthPercent = var2.matchConstraintPercentWidth;
         this.heightPercent = var2.matchConstraintPercentHeight;
         if (VERSION.SDK_INT >= 17) {
            this.endMargin = var2.getMarginEnd();
            this.startMargin = var2.getMarginStart();
         }

      }

      private void fillFromConstraints(int var1, Constraints.LayoutParams var2) {
         this.fillFrom(var1, var2);
         this.alpha = var2.alpha;
         this.rotation = var2.rotation;
         this.rotationX = var2.rotationX;
         this.rotationY = var2.rotationY;
         this.scaleX = var2.scaleX;
         this.scaleY = var2.scaleY;
         this.transformPivotX = var2.transformPivotX;
         this.transformPivotY = var2.transformPivotY;
         this.translationX = var2.translationX;
         this.translationY = var2.translationY;
         this.translationZ = var2.translationZ;
         this.elevation = var2.elevation;
         this.applyElevation = var2.applyElevation;
      }

      private void fillFromConstraints(ConstraintHelper var1, int var2, Constraints.LayoutParams var3) {
         this.fillFromConstraints(var2, var3);
         if (var1 instanceof Barrier) {
            this.mHelperType = 1;
            Barrier var4 = (Barrier)var1;
            this.mBarrierDirection = var4.getType();
            this.mReferenceIds = var4.getReferencedIds();
         }

      }

      public void applyTo(ConstraintLayout.LayoutParams var1) {
         var1.leftToLeft = this.leftToLeft;
         var1.leftToRight = this.leftToRight;
         var1.rightToLeft = this.rightToLeft;
         var1.rightToRight = this.rightToRight;
         var1.topToTop = this.topToTop;
         var1.topToBottom = this.topToBottom;
         var1.bottomToTop = this.bottomToTop;
         var1.bottomToBottom = this.bottomToBottom;
         var1.baselineToBaseline = this.baselineToBaseline;
         var1.startToEnd = this.startToEnd;
         var1.startToStart = this.startToStart;
         var1.endToStart = this.endToStart;
         var1.endToEnd = this.endToEnd;
         var1.leftMargin = this.leftMargin;
         var1.rightMargin = this.rightMargin;
         var1.topMargin = this.topMargin;
         var1.bottomMargin = this.bottomMargin;
         var1.goneStartMargin = this.goneStartMargin;
         var1.goneEndMargin = this.goneEndMargin;
         var1.horizontalBias = this.horizontalBias;
         var1.verticalBias = this.verticalBias;
         var1.circleConstraint = this.circleConstraint;
         var1.circleRadius = this.circleRadius;
         var1.circleAngle = this.circleAngle;
         var1.dimensionRatio = this.dimensionRatio;
         var1.editorAbsoluteX = this.editorAbsoluteX;
         var1.editorAbsoluteY = this.editorAbsoluteY;
         var1.verticalWeight = this.verticalWeight;
         var1.horizontalWeight = this.horizontalWeight;
         var1.verticalChainStyle = this.verticalChainStyle;
         var1.horizontalChainStyle = this.horizontalChainStyle;
         var1.constrainedWidth = this.constrainedWidth;
         var1.constrainedHeight = this.constrainedHeight;
         var1.matchConstraintDefaultWidth = this.widthDefault;
         var1.matchConstraintDefaultHeight = this.heightDefault;
         var1.matchConstraintMaxWidth = this.widthMax;
         var1.matchConstraintMaxHeight = this.heightMax;
         var1.matchConstraintMinWidth = this.widthMin;
         var1.matchConstraintMinHeight = this.heightMin;
         var1.matchConstraintPercentWidth = this.widthPercent;
         var1.matchConstraintPercentHeight = this.heightPercent;
         var1.orientation = this.orientation;
         var1.guidePercent = this.guidePercent;
         var1.guideBegin = this.guideBegin;
         var1.guideEnd = this.guideEnd;
         var1.width = this.mWidth;
         var1.height = this.mHeight;
         if (VERSION.SDK_INT >= 17) {
            var1.setMarginStart(this.startMargin);
            var1.setMarginEnd(this.endMargin);
         }

         var1.validate();
      }

      public ConstraintSet.Constraint clone() {
         ConstraintSet.Constraint var1 = new ConstraintSet.Constraint();
         var1.mIsGuideline = this.mIsGuideline;
         var1.mWidth = this.mWidth;
         var1.mHeight = this.mHeight;
         var1.guideBegin = this.guideBegin;
         var1.guideEnd = this.guideEnd;
         var1.guidePercent = this.guidePercent;
         var1.leftToLeft = this.leftToLeft;
         var1.leftToRight = this.leftToRight;
         var1.rightToLeft = this.rightToLeft;
         var1.rightToRight = this.rightToRight;
         var1.topToTop = this.topToTop;
         var1.topToBottom = this.topToBottom;
         var1.bottomToTop = this.bottomToTop;
         var1.bottomToBottom = this.bottomToBottom;
         var1.baselineToBaseline = this.baselineToBaseline;
         var1.startToEnd = this.startToEnd;
         var1.startToStart = this.startToStart;
         var1.endToStart = this.endToStart;
         var1.endToEnd = this.endToEnd;
         var1.horizontalBias = this.horizontalBias;
         var1.verticalBias = this.verticalBias;
         var1.dimensionRatio = this.dimensionRatio;
         var1.editorAbsoluteX = this.editorAbsoluteX;
         var1.editorAbsoluteY = this.editorAbsoluteY;
         var1.horizontalBias = this.horizontalBias;
         var1.horizontalBias = this.horizontalBias;
         var1.horizontalBias = this.horizontalBias;
         var1.horizontalBias = this.horizontalBias;
         var1.horizontalBias = this.horizontalBias;
         var1.orientation = this.orientation;
         var1.leftMargin = this.leftMargin;
         var1.rightMargin = this.rightMargin;
         var1.topMargin = this.topMargin;
         var1.bottomMargin = this.bottomMargin;
         var1.endMargin = this.endMargin;
         var1.startMargin = this.startMargin;
         var1.visibility = this.visibility;
         var1.goneLeftMargin = this.goneLeftMargin;
         var1.goneTopMargin = this.goneTopMargin;
         var1.goneRightMargin = this.goneRightMargin;
         var1.goneBottomMargin = this.goneBottomMargin;
         var1.goneEndMargin = this.goneEndMargin;
         var1.goneStartMargin = this.goneStartMargin;
         var1.verticalWeight = this.verticalWeight;
         var1.horizontalWeight = this.horizontalWeight;
         var1.horizontalChainStyle = this.horizontalChainStyle;
         var1.verticalChainStyle = this.verticalChainStyle;
         var1.alpha = this.alpha;
         var1.applyElevation = this.applyElevation;
         var1.elevation = this.elevation;
         var1.rotation = this.rotation;
         var1.rotationX = this.rotationX;
         var1.rotationY = this.rotationY;
         var1.scaleX = this.scaleX;
         var1.scaleY = this.scaleY;
         var1.transformPivotX = this.transformPivotX;
         var1.transformPivotY = this.transformPivotY;
         var1.translationX = this.translationX;
         var1.translationY = this.translationY;
         var1.translationZ = this.translationZ;
         var1.constrainedWidth = this.constrainedWidth;
         var1.constrainedHeight = this.constrainedHeight;
         var1.widthDefault = this.widthDefault;
         var1.heightDefault = this.heightDefault;
         var1.widthMax = this.widthMax;
         var1.heightMax = this.heightMax;
         var1.widthMin = this.widthMin;
         var1.heightMin = this.heightMin;
         var1.widthPercent = this.widthPercent;
         var1.heightPercent = this.heightPercent;
         var1.mBarrierDirection = this.mBarrierDirection;
         var1.mHelperType = this.mHelperType;
         if (this.mReferenceIds != null) {
            var1.mReferenceIds = Arrays.copyOf(this.mReferenceIds, this.mReferenceIds.length);
         }

         var1.circleConstraint = this.circleConstraint;
         var1.circleRadius = this.circleRadius;
         var1.circleAngle = this.circleAngle;
         return var1;
      }
   }
}
