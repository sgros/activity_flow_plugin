package menion.android.whereyougo.maps.mapsforge.filepicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import menion.android.whereyougo.maps.mapsforge.filefilter.ValidFileFilter;

public class FilePicker extends Activity implements OnItemClickListener {
   private static final String CURRENT_DIRECTORY = "currentDirectory";
   private static final String DEFAULT_DIRECTORY = "/";
   private static final int DIALOG_FILE_INVALID = 0;
   private static final int DIALOG_FILE_SELECT = 1;
   private static final String PREFERENCES_FILE = "FilePicker";
   public static final String SELECTED_FILE = "selectedFile";
   private static Comparator fileComparator = getDefaultFileComparator();
   private static FileFilter fileDisplayFilter;
   private static ValidFileFilter fileSelectFilter;
   private File currentDirectory;
   private FilePickerIconAdapter filePickerIconAdapter;
   private File[] files;
   private File[] filesWithParentFolder;

   private void browseToCurrentDirectory() {
      this.setTitle(this.currentDirectory.getAbsolutePath());
      if (fileDisplayFilter == null) {
         this.files = this.currentDirectory.listFiles();
      } else {
         this.files = this.currentDirectory.listFiles(fileDisplayFilter);
      }

      if (this.files == null) {
         this.files = new File[0];
      } else {
         Arrays.sort(this.files, fileComparator);
      }

      if (this.currentDirectory.getParentFile() != null) {
         this.filesWithParentFolder = new File[this.files.length + 1];
         this.filesWithParentFolder[0] = this.currentDirectory.getParentFile();
         System.arraycopy(this.files, 0, this.filesWithParentFolder, 1, this.files.length);
         this.files = this.filesWithParentFolder;
         this.filePickerIconAdapter.setFiles(this.files, true);
      } else {
         this.filePickerIconAdapter.setFiles(this.files, false);
      }

      this.filePickerIconAdapter.notifyDataSetChanged();
   }

   private static Comparator getDefaultFileComparator() {
      return new Comparator() {
         public int compare(File var1, File var2) {
            int var3;
            if (var1.isDirectory() && !var2.isDirectory()) {
               var3 = -1;
            } else if (!var1.isDirectory() && var2.isDirectory()) {
               var3 = 1;
            } else {
               var3 = var1.getName().compareToIgnoreCase(var2.getName());
            }

            return var3;
         }
      };
   }

   public static void setFileComparator(Comparator var0) {
      fileComparator = var0;
   }

   public static void setFileDisplayFilter(FileFilter var0) {
      fileDisplayFilter = var0;
   }

   public static void setFileSelectFilter(ValidFileFilter var0) {
      fileSelectFilter = var0;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2130903040);
      this.filePickerIconAdapter = new FilePickerIconAdapter(this);
      GridView var2 = (GridView)this.findViewById(2131492879);
      var2.setOnItemClickListener(this);
      var2.setAdapter(this.filePickerIconAdapter);
      if (var1 == null) {
         this.showDialog(1);
      }

   }

   protected Dialog onCreateDialog(int var1) {
      AlertDialog var2 = null;
      Builder var3 = new Builder(this);
      switch(var1) {
      case 0:
         var3.setIcon(17301569);
         var3.setTitle(2131165200);
         StringBuilder var4 = new StringBuilder();
         var4.append(this.getString(2131165415));
         var4.append("\n\n");
         var4.append(fileSelectFilter.getFileOpenResult().getErrorMessage());
         var3.setMessage(var4.toString());
         var3.setPositiveButton(2131165230, (OnClickListener)null);
         var2 = var3.create();
         break;
      case 1:
         var3.setMessage(2131165416);
         var3.setPositiveButton(2131165230, (OnClickListener)null);
         var2 = var3.create();
      }

      return var2;
   }

   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      File var6 = this.files[(int)var4];
      if (var6.isDirectory()) {
         this.currentDirectory = var6;
         this.browseToCurrentDirectory();
      } else if (fileSelectFilter != null && !fileSelectFilter.accept(var6)) {
         this.showDialog(0);
      } else {
         this.setResult(-1, (new Intent()).putExtra("selectedFile", var6.getAbsolutePath()));
         this.finish();
      }

   }

   protected void onPause() {
      super.onPause();
      Editor var1 = this.getSharedPreferences("FilePicker", 0).edit();
      var1.clear();
      if (this.currentDirectory != null) {
         var1.putString("currentDirectory", this.currentDirectory.getAbsolutePath());
      }

      var1.commit();
   }

   protected void onResume() {
      super.onResume();
      if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fullscreen", false)) {
         this.getWindow().addFlags(1024);
         this.getWindow().clearFlags(2048);
      } else {
         this.getWindow().clearFlags(1024);
         this.getWindow().addFlags(2048);
      }

      this.currentDirectory = new File(this.getSharedPreferences("FilePicker", 0).getString("currentDirectory", "/"));
      if (!this.currentDirectory.exists() || !this.currentDirectory.canRead()) {
         this.currentDirectory = new File("/");
      }

      if (!this.currentDirectory.exists() || !this.currentDirectory.canRead()) {
         this.currentDirectory = Environment.getExternalStorageDirectory();
      }

      this.browseToCurrentDirectory();
   }
}
