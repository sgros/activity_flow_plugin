package menion.android.whereyougo.gui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.dialog.CustomDialogFragment;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;

public class ChooseCartridgeDialog extends CustomDialogFragment {
   private static final String TAG = "ChooseCartridgeDialog";
   private BaseAdapter adapter;
   private Vector cartridgeFiles;
   private ArrayList data;

   private void itemClicked(int var1) {
      try {
         MainActivity.openCartridge((CartridgeFile)this.cartridgeFiles.get(var1));
      } catch (Exception var3) {
         Logger.e("ChooseCartridgeDialog", "onCreate()", var3);
      }

      this.dismiss();
   }

   private void itemLongClicked(final int var1) {
      try {
         final CartridgeFile var2 = (CartridgeFile)this.cartridgeFiles.get(var1);
         final String var3 = var2.filename.substring(0, var2.filename.length() - 3);
         FragmentActivity var4 = this.getActivity();
         OnClickListener var5 = new OnClickListener() {
            public void onClick(DialogInterface var1x, int var2x) {
               File[] var4 = FileSystem.getFiles2((new File(var2.filename)).getParent(), new FileFilter() {
                  public boolean accept(File var1x) {
                     return var1x.getAbsolutePath().startsWith(var3);
                  }
               });
               int var3x = var4.length;

               for(var2x = 0; var2x < var3x; ++var2x) {
                  var4[var2x].delete();
               }

               MainActivity.refreshCartridges();
               ChooseCartridgeDialog.this.cartridgeFiles.remove(var1);
               ChooseCartridgeDialog.this.data.remove(var1);
               if (ChooseCartridgeDialog.this.adapter != null) {
                  ChooseCartridgeDialog.this.adapter.notifyDataSetChanged();
               }

            }
         };
         UtilsGUI.showDialogQuestion(var4, 2131165330, var5, (OnClickListener)null);
      } catch (Exception var6) {
         Logger.e("ChooseCartridgeDialog", "onCreate()", var6);
      }

   }

   public Dialog createDialog(Bundle var1) {
      AlertDialog var14;
      if (A.getMain() != null && this.cartridgeFiles != null) {
         Exception var10000;
         label61: {
            final Location var2;
            boolean var10001;
            try {
               var2 = LocationState.getLocation();
               final Location var3 = new Location("ChooseCartridgeDialog");
               final Location var4 = new Location("ChooseCartridgeDialog");
               Vector var15 = this.cartridgeFiles;
               Comparator var5 = new Comparator() {
                  public int compare(CartridgeFile var1, CartridgeFile var2x) {
                     var3.setLatitude(var1.latitude);
                     var3.setLongitude(var1.longitude);
                     var4.setLatitude(var2x.latitude);
                     var4.setLongitude(var2x.longitude);
                     return (int)(var2.distanceTo(var3) - var2.distanceTo(var4));
                  }
               };
               Collections.sort(var15, var5);
               ArrayList var16 = new ArrayList();
               this.data = var16;
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label61;
            }

            int var6 = 0;

            while(true) {
               byte[] var17;
               CartridgeFile var24;
               try {
                  if (var6 >= this.cartridgeFiles.size()) {
                     break;
                  }

                  var24 = (CartridgeFile)this.cartridgeFiles.get(var6);
                  var17 = var24.getFile(var24.iconId);
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label61;
               }

               Bitmap var20;
               try {
                  var20 = BitmapFactory.decodeByteArray(var17, 0, var17.length);
               } catch (Exception var11) {
                  try {
                     var20 = Images.getImageB(2130837554);
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label61;
                  }
               }

               try {
                  String var26 = var24.name;
                  StringBuilder var22 = new StringBuilder();
                  DataInfo var7 = new DataInfo(var26, var22.append(var24.type).append(", ").append(var24.author).append(", ").append(var24.version).toString(), var20);
                  var7.value01 = var24.latitude;
                  var7.value02 = var24.longitude;
                  var7.setDistAzi(var2);
                  this.data.add(var7);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label61;
               }

               ++var6;
            }

            try {
               ListView var25 = UtilsGUI.createListView(this.getActivity(), false, this.data);
               OnItemClickListener var18 = new OnItemClickListener() {
                  public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
                     ChooseCartridgeDialog.this.itemClicked(var3);
                  }
               };
               var25.setOnItemClickListener(var18);
               OnItemLongClickListener var19 = new OnItemLongClickListener() {
                  public boolean onItemLongClick(AdapterView var1, View var2, int var3, long var4) {
                     ChooseCartridgeDialog.this.itemLongClicked(var3);
                     return true;
                  }
               };
               var25.setOnItemLongClickListener(var19);
               this.adapter = (BaseAdapter)var25.getAdapter();
               Builder var21 = new Builder(this.getActivity());
               var14 = var21.setTitle(2131165191).setIcon(2130837551).setView(var25).setNeutralButton(2131165192, (OnClickListener)null).create();
               return var14;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         Exception var23 = var10000;
         Logger.e("ChooseCartridgeDialog", "createDialog()", var23);
         var14 = null;
      } else {
         var14 = null;
      }

      return var14;
   }

   public void setParams(Vector var1) {
      this.cartridgeFiles = var1;
   }
}
