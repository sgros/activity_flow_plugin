package menion.android.whereyougo.gui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import cz.matejcik.openwig.EventTable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.dialog.CustomDialogFragment;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.UtilsFormat;

public class ChooseSavegameDialog extends CustomDialogFragment {
   private static final String SAVE_FILE = "SAVE_FILE";
   private static final String TAG = "ChooseSavegameDialog";
   private BaseAdapter adapter;
   private ArrayList data;
   private File saveFile;

   private void itemClicked(int var1) {
      if (var1 == 0) {
         MainActivity.wui.showScreen(11, (EventTable)null);
         this.dismiss();
      } else {
         try {
            FileSystem.copyFile((File)((DataInfo)this.data.remove(var1)).addData01, this.saveFile);
            MainActivity.startSelectedCartridge(true);
            this.dismiss();
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

   }

   private void itemLongClicked(final int var1) {
      if (var1 != 0) {
         UtilsGUI.showDialogQuestion(this.getActivity(), 2131165331, new OnClickListener() {
            public void onClick(DialogInterface var1x, int var2) {
               ((File)((DataInfo)ChooseSavegameDialog.this.data.remove(var1)).addData01).delete();
               if (ChooseSavegameDialog.this.adapter != null) {
                  ChooseSavegameDialog.this.adapter.notifyDataSetChanged();
               }

            }
         }, (OnClickListener)null);
      }

   }

   public static ChooseSavegameDialog newInstance(File var0) {
      ChooseSavegameDialog var1 = new ChooseSavegameDialog();
      Bundle var2 = new Bundle();
      var2.putString("SAVE_FILE", var0.getAbsolutePath());
      var1.setArguments(var2);
      return var1;
   }

   private boolean restoreInstance(Bundle var1) {
      boolean var2 = false;
      if (var1 != null) {
         String var3 = var1.getString("SAVE_FILE");
         if (var3 != null) {
            this.saveFile = new File(var3);
            var2 = true;
         }
      }

      return var2;
   }

   public Dialog createDialog(Bundle var1) {
      AlertDialog var3;
      if (A.getMain() == null) {
         var3 = null;
      } else if (!this.restoreInstance(var1) && !this.restoreInstance(this.getArguments())) {
         var3 = null;
      } else {
         this.data = new ArrayList();
         this.data.add(new DataInfo(this.getString(2131165398), ""));
         File var4 = this.saveFile;
         if (var4.exists()) {
            this.data.add(new DataInfo(this.getString(2131165397), UtilsFormat.formatDatetime(var4.lastModified()), var4));
         }

         var4 = new File(this.saveFile.getAbsolutePath() + ".bak");
         if (var4.exists()) {
            this.data.add(new DataInfo(this.getString(2131165396), UtilsFormat.formatDatetime(var4.lastModified()), var4));
         }

         for(int var2 = 1; var2 <= Preferences.GLOBAL_SAVEGAME_SLOTS; ++var2) {
            var4 = new File(this.saveFile.getAbsolutePath() + "." + var2);
            if (var4.exists()) {
               this.data.add(new DataInfo(String.format("%s %d", this.getString(2131165402), var2), UtilsFormat.formatDatetime(var4.lastModified()), var4));
            }
         }

         ListView var5 = UtilsGUI.createListView(this.getActivity(), false, this.data);
         var5.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
               ChooseSavegameDialog.this.itemClicked(var3);
            }
         });
         var5.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView var1, View var2, int var3, long var4) {
               ChooseSavegameDialog.this.itemLongClicked(var3);
               return true;
            }
         });
         this.adapter = (BaseAdapter)var5.getAdapter();
         var3 = (new Builder(this.getActivity())).setTitle(2131165342).setIcon(2130837551).setView(var5).setNeutralButton(2131165192, (OnClickListener)null).create();
      }

      return var3;
   }

   public void setParams(File var1) {
      this.saveFile = var1;
   }
}
