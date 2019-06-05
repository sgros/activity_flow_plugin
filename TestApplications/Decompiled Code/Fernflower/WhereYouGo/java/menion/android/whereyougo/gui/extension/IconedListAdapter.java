package menion.android.whereyougo.gui.extension;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

public class IconedListAdapter extends BaseAdapter {
   private static final int PADDING = (int)Utils.getDpPixels(4.0F);
   private static final String TAG = "IconedListAdapter";
   private static final int TYPE_LIST_VIEW = 0;
   private static final int TYPE_OTHER = 2;
   private static final int TYPE_SPINNER_VIEW = 1;
   private final Context context;
   private final ArrayList mData;
   private int minHeight = Integer.MIN_VALUE;
   private float multiplyImageSize = 1.0F;
   private boolean textView02HideIfEmpty = false;
   private int textView02Visibility = 0;
   private int type = 0;

   public IconedListAdapter(Context var1, ArrayList var2, View var3) {
      this.mData = var2;
      if (var3 instanceof ListView) {
         ((ListView)var3).setBackgroundColor(-1);
         this.type = 0;
      } else if (var3 instanceof Spinner) {
         this.type = 1;
      } else {
         this.setTextView02Visible(8, true);
         this.type = 2;
      }

      this.context = var1;
   }

   private static LinearLayout createEmptyView(Context var0) {
      return (LinearLayout)LinearLayout.inflate(var0, 2130903049, (ViewGroup)null);
   }

   private View getViewItem(int var1, View var2, boolean var3) {
      label223: {
         Exception var10000;
         label225: {
            DataInfo var4;
            LinearLayout var5;
            boolean var10001;
            try {
               var4 = (DataInfo)this.mData.get(var1);
               var5 = (LinearLayout)var2.findViewById(2131492886);
               var5.setPadding(PADDING, PADDING, PADDING, PADDING);
               if (this.minHeight != Integer.MIN_VALUE) {
                  var5.setMinimumHeight(this.minHeight);
               }
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label225;
            }

            TextView var6;
            TextView var7;
            ImageView var8;
            ImageView var9;
            String var10;
            try {
               var6 = (TextView)var2.findViewById(2131492927);
               var7 = (TextView)var2.findViewById(2131492928);
               var8 = (ImageView)var2.findViewById(2131492926);
               var9 = (ImageView)var2.findViewById(2131492929);
               var6.setBackgroundColor(0);
               var6.setTextColor(-16777216);
               var10 = var4.getName();
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label225;
            }

            if (var10 == null) {
               try {
                  var6.setVisibility(8);
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label225;
               }
            } else {
               try {
                  var6.setVisibility(0);
                  var6.setText(Html.fromHtml(var10));
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label225;
               }
            }

            label226: {
               String var11;
               label219: {
                  try {
                     var7.setTextColor(-12303292);
                     if (this.textView02Visibility != 8) {
                        var7.setVisibility(0);
                        var11 = var4.getDescription();
                        break label219;
                     }
                  } catch (Exception var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label225;
                  }

                  try {
                     var7.setVisibility(8);
                     break label226;
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label225;
                  }
               }

               var10 = var11;
               if (var11 == null) {
                  var10 = "";
               }

               try {
                  if (var10.length() > 0) {
                     var7.setText(Html.fromHtml(var10));
                     break label226;
                  }
               } catch (Exception var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label225;
               }

               try {
                  if (this.textView02HideIfEmpty) {
                     var7.setVisibility(8);
                     break label226;
                  }
               } catch (Exception var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label225;
               }

               try {
                  var7.setText(2131165225);
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label225;
               }
            }

            float var12 = 1.0F;

            label227: {
               label196: {
                  try {
                     if (this.type != 1) {
                        break label196;
                     }
                  } catch (Exception var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label225;
                  }

                  if (!var3) {
                     var12 = 0.75F;
                     break label227;
                  }
               }

               label189: {
                  try {
                     if (this.type != 1) {
                        break label189;
                     }
                  } catch (Exception var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label225;
                  }

                  if (var3) {
                     var12 = 1.25F;

                     try {
                        var6.setHeight((int)((float)Images.SIZE_BIG * 1.25F));
                        break label227;
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label225;
                     }
                  }
               }

               label182: {
                  try {
                     if (this.type != 0) {
                        break label182;
                     }
                  } catch (Exception var30) {
                     var10000 = var30;
                     var10001 = false;
                     break label225;
                  }

                  var12 = 1.0F;
                  break label227;
               }

               try {
                  if (this.type != 2) {
                     break label227;
                  }
               } catch (Exception var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label225;
               }

               var12 = 1.0F;
            }

            int var13;
            label233: {
               try {
                  var12 *= this.multiplyImageSize;
                  var13 = (int)((float)Images.SIZE_BIG * var12);
                  if (var4.getImage() != -1) {
                     var8.setImageResource(var4.getImage());
                     break label233;
                  }
               } catch (Exception var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label225;
               }

               try {
                  if (var4.getImageD() != null) {
                     var8.setImageDrawable(var4.getImageD());
                     break label233;
                  }
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label225;
               }

               Bitmap var37;
               label157: {
                  label231: {
                     Bitmap var36;
                     try {
                        if (var4.getImageB() == null) {
                           break label231;
                        }

                        var36 = var4.getImageB();
                        if (var36.getWidth() > Const.SCREEN_WIDTH / 2 && var4.getName() != null && var4.getName().length() > 0) {
                           var37 = Images.resizeBitmap(var36, Const.SCREEN_WIDTH / 2);
                           break label157;
                        }
                     } catch (Exception var26) {
                        var10000 = var26;
                        var10001 = false;
                        break label225;
                     }

                     var37 = var36;

                     try {
                        if (var36.getWidth() > Const.SCREEN_WIDTH) {
                           var37 = Images.resizeBitmap(var36, Const.SCREEN_WIDTH);
                        }
                        break label157;
                     } catch (Exception var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label225;
                     }
                  }

                  var13 = 0;
                  break label233;
               }

               try {
                  var8.setImageBitmap(var37);
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label225;
               }
            }

            try {
               LayoutParams var38 = var8.getLayoutParams();
               var38.width = var13;
               var38.height = (int)((float)Images.SIZE_BIG * var12);
               var8.setLayoutParams(var38);
               var8.setVisibility(0);
               var9.setVisibility(8);
               if (var4.getImageRight() != null) {
                  var9.setVisibility(0);
                  var9.setImageBitmap(var4.getImageRight());
               }
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label225;
            }

            try {
               if (var4.enabled) {
                  var5.setBackgroundColor(0);
                  break label223;
               }
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label225;
            }

            try {
               var5.setBackgroundColor(-3355444);
               break label223;
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
            }
         }

         Exception var39 = var10000;
         Logger.e("IconedListAdapter", "getView(" + var1 + ", " + var2 + ")", var39);
      }

      var2.forceLayout();
      return var2;
   }

   public boolean areAllItemsEnabled() {
      return false;
   }

   public int getCount() {
      return this.mData.size();
   }

   public DataInfo getDataInfo(int var1) {
      return (DataInfo)this.mData.get(var1);
   }

   public View getDropDownView(int var1, View var2, ViewGroup var3) {
      Object var4 = var2;
      if (var2 == null) {
         var4 = createEmptyView(this.context);
      }

      return this.getViewItem(var1, (View)var4, true);
   }

   public Object getItem(int var1) {
      return this.mData.get(var1);
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      Object var4 = var2;
      if (var2 == null) {
         var4 = createEmptyView(this.context);
      }

      return this.getViewItem(var1, (View)var4, false);
   }

   public boolean isEnabled(int var1) {
      boolean var2;
      try {
         var2 = ((DataInfo)this.mData.get(var1)).enabled;
      } catch (Exception var4) {
         Logger.e("IconedListAdapter", "isEnabled(" + var1 + ")", var4);
         var2 = false;
      }

      return var2;
   }

   public void setMinHeight(int var1) {
      this.minHeight = var1;
   }

   public void setMultiplyImageSize(float var1) {
      this.multiplyImageSize = var1;
   }

   public void setTextView02Visible(int var1, boolean var2) {
      this.textView02Visibility = var1;
      this.textView02HideIfEmpty = var2;
   }
}
