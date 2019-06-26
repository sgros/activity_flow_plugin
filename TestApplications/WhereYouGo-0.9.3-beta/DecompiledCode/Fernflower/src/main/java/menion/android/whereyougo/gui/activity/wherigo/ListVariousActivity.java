package menion.android.whereyougo.gui.activity.wherigo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.Thing;
import java.util.ArrayList;
import java.util.Vector;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.UtilsFormat;

public abstract class ListVariousActivity extends CustomActivity implements IRefreshable {
   private static final String TAG = "ListVarious";
   private static final Paint paintArrow;
   private static final Paint paintArrowBorder;
   private static final Paint paintText = new Paint();
   private ListView lv;
   private final Vector stuff = new Vector();
   private String title;

   static {
      paintText.setColor(-65536);
      paintText.setTextSize(Utils.getDpPixels(12.0F));
      paintText.setTypeface(Typeface.DEFAULT_BOLD);
      paintText.setAntiAlias(true);
      paintArrow = new Paint();
      paintArrow.setColor(-256);
      paintArrow.setAntiAlias(true);
      paintArrow.setStyle(Style.FILL);
      paintArrowBorder = new Paint();
      paintArrowBorder.setColor(-16777216);
      paintArrowBorder.setAntiAlias(true);
      paintArrowBorder.setStyle(Style.STROKE);
   }

   private Bitmap getLocatedIcon(EventTable var1) {
      Bitmap var23;
      if (!var1.isLocated()) {
         var23 = Images.IMAGE_EMPTY_B;
      } else {
         Bitmap var2;
         label57: {
            Exception var10000;
            label53: {
               Canvas var3;
               float var5;
               float var6;
               int var7;
               int var8;
               boolean var10001;
               try {
                  var2 = Bitmap.createBitmap((int)Utils.getDpPixels(80.0F), (int)Utils.getDpPixels(40.0F), Config.ARGB_8888);
                  var3 = new Canvas(var2);
                  var3.drawColor(0);
                  Location var4 = UtilsWherigo.extractLocation(var1);
                  var5 = LocationState.getLocation().bearingTo(var4);
                  var6 = LocationState.getLocation().distanceTo(var4);
                  var7 = var2.getHeight() / 2;
                  var8 = var2.getHeight() / 2;
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label53;
               }

               double var9 = (double)(var5 / 57.29578F);

               float var11;
               float var12;
               try {
                  var11 = (float)(Math.sin(var9) * (double)var7 * 0.9D);
                  var12 = (float)(Math.cos(var9) * (double)var7 * 0.9D);
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label53;
               }

               var9 = (double)((180.0F + var5) / 57.29578F);

               float var13;
               float var14;
               try {
                  var13 = (float)(Math.sin(var9) * (double)var7 * 0.2D);
                  var14 = (float)(Math.cos(var9) * (double)var7 * 0.2D);
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label53;
               }

               var9 = (double)((140.0F + var5) / 57.29578F);

               float var15;
               float var16;
               try {
                  var15 = (float)(Math.sin(var9) * (double)var7 * 0.6D);
                  var16 = (float)(Math.cos(var9) * (double)var7 * 0.6D);
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label53;
               }

               var9 = (double)((220.0F + var5) / 57.29578F);

               try {
                  float var17 = (float)(Math.sin(var9) * (double)var7 * 0.6D);
                  var5 = (float)(Math.cos(var9) * (double)var7 * 0.6D);
                  Path var25 = new Path();
                  var25.moveTo((float)var7 + var11, (float)var8 - var12);
                  var25.lineTo((float)var7 + var13, (float)var8 - var14);
                  var25.lineTo((float)var7 + var15, (float)var8 - var16);
                  var3.drawPath(var25, paintArrow);
                  var25 = new Path();
                  var25.moveTo((float)var7 + var11, (float)var8 - var12);
                  var25.lineTo((float)var7 + var13, (float)var8 - var14);
                  var25.lineTo((float)var7 + var17, (float)var8 - var5);
                  var3.drawPath(var25, paintArrow);
                  var3.drawLine((float)var7 + var11, (float)var8 - var12, (float)var7 + var15, (float)var8 - var16, paintArrowBorder);
                  var3.drawLine((float)var7 + var11, (float)var8 - var12, (float)var7 + var17, (float)var8 - var5, paintArrowBorder);
                  var3.drawLine((float)var7 + var13, (float)var8 - var14, (float)var7 + var15, (float)var8 - var16, paintArrowBorder);
                  var3.drawLine((float)var7 + var13, (float)var8 - var14, (float)var7 + var17, (float)var8 - var5, paintArrowBorder);
                  var3.drawText(UtilsFormat.formatDistance((double)var6, false), (float)(var7 * 2 + 2), (float)var8 + paintText.getTextSize() / 2.0F, paintText);
                  break label57;
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
               }
            }

            Exception var24 = var10000;
            Logger.e("ListVarious", "getLocatedIcon(" + var1 + ")", var24);
            var23 = Images.IMAGE_EMPTY_B;
            return var23;
         }

         var23 = var2;
      }

      return var23;
   }

   protected abstract void callStuff(Object var1);

   Bitmap getStuffIcon(Object var1) {
      Bitmap var4;
      if (((EventTable)var1).isLocated()) {
         var4 = this.getLocatedIcon((EventTable)var1);
      } else {
         Media var5 = (Media)((EventTable)var1).table.rawget("Icon");
         if (var5 != null) {
            label28: {
               Exception var10000;
               label33: {
                  boolean var10001;
                  byte[] var6;
                  try {
                     var6 = Engine.mediaFile(var5);
                  } catch (Exception var3) {
                     var10000 = var3;
                     var10001 = false;
                     break label33;
                  }

                  if (var6 == null) {
                     break label28;
                  }

                  try {
                     var4 = BitmapFactory.decodeByteArray(var6, 0, var6.length);
                     return var4;
                  } catch (Exception var2) {
                     var10000 = var2;
                     var10001 = false;
                  }
               }

               Exception var7 = var10000;
               Logger.e("ListVarious", "getStuffIcon()", var7);
            }
         }

         var4 = Images.IMAGE_EMPTY_B;
      }

      return var4;
   }

   protected abstract String getStuffName(Object var1);

   protected abstract Vector getValidStuff();

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (A.getMain() != null && Engine.instance != null) {
         this.setContentView(2130903042);
         if (this.getIntent().getStringExtra("title") != null) {
            this.title = this.getIntent().getStringExtra("title");
         }

         CustomDialog.setTitle(this, this.title, (Bitmap)null, 2130837528, new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               ListVariousActivity.this.finish();
               return true;
            }
         });
         this.lv = new ListView(this);
         CustomDialog.setContent(this, this.lv, 0, false, true);
         CustomDialog.setBottom(this, (String)null, (CustomDialog.OnClickListener)null, (String)null, (CustomDialog.OnClickListener)null, (String)null, (CustomDialog.OnClickListener)null);
      } else {
         this.finish();
      }

   }

   public void onResume() {
      super.onResume();
      this.refresh();
   }

   public void refresh() {
      this.runOnUiThread(new Runnable() {
         public void run() {
            if (!ListVariousActivity.this.stillValid()) {
               ListVariousActivity.this.finish();
            } else {
               Vector var1 = ListVariousActivity.this.getValidStuff();
               int var2 = ListVariousActivity.this.lv.getFirstVisiblePosition();
               ListVariousActivity.this.stuff.clear();

               int var3;
               Object var4;
               for(var3 = 0; var3 < var1.size(); ++var3) {
                  var4 = var1.get(var3);
                  if (var4 != null) {
                     ListVariousActivity.this.stuff.add(var4);
                  }
               }

               ArrayList var5 = new ArrayList();

               for(var3 = 0; var3 < ListVariousActivity.this.stuff.size(); ++var3) {
                  var4 = ListVariousActivity.this.stuff.get(var3);
                  DataInfo var6;
                  if (var4 instanceof Thing) {
                     var6 = new DataInfo(((Thing)var4).name, (String)null, ListVariousActivity.this.getStuffIcon(var4));
                  } else if (var4 instanceof Action) {
                     var6 = new DataInfo(((Action)var4).text, (String)null, ListVariousActivity.this.getStuffIcon(var4));
                  } else {
                     var6 = new DataInfo(var4.toString(), (String)null, ListVariousActivity.this.getStuffIcon(var4));
                  }

                  var5.add(var6);
               }

               IconedListAdapter var7 = new IconedListAdapter(ListVariousActivity.this, var5, ListVariousActivity.this.lv);
               var7.setMultiplyImageSize(1.5F);
               var7.setTextView02Visible(0, true);
               ListVariousActivity.this.lv.setAdapter(var7);
               ListVariousActivity.this.lv.setOnItemClickListener(new OnItemClickListener() {
                  public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
                     Object var18;
                     label152: {
                        Throwable var10000;
                        boolean var10001;
                        label148: {
                           Logger.d("ListVarious", "onItemClick:" + var3);
                           var2 = null;
                           synchronized(this){}
                           var18 = var2;
                           if (var3 >= 0) {
                              var18 = var2;

                              try {
                                 if (var3 < ListVariousActivity.this.stuff.size()) {
                                    var18 = ListVariousActivity.this.stuff.get(var3);
                                 }
                              } catch (Throwable var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label148;
                              }
                           }

                           label143:
                           try {
                              break label152;
                           } catch (Throwable var16) {
                              var10000 = var16;
                              var10001 = false;
                              break label143;
                           }
                        }

                        while(true) {
                           Throwable var19 = var10000;

                           try {
                              throw var19;
                           } catch (Throwable var15) {
                              var10000 = var15;
                              var10001 = false;
                              continue;
                           }
                        }
                     }

                     if (var18 != null) {
                        ListVariousActivity.this.callStuff(var18);
                     }

                  }
               });
               ListVariousActivity.this.lv.setSelectionFromTop(var2, 5);
            }

         }
      });
   }

   protected abstract boolean stillValid();
}
