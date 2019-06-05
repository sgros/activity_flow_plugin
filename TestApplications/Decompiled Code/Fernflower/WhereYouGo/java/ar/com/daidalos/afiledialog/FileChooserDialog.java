package ar.com.daidalos.afiledialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileChooserDialog extends Dialog implements FileChooser {
   private FileChooserCore core;
   private List listeners;

   public FileChooserDialog(Context var1) {
      this(var1, (String)null);
   }

   public FileChooserDialog(Context var1, String var2) {
      super(var1);
      this.setContentView(R.layout.daidalos_file_chooser);
      LayoutParams var3 = new LayoutParams();
      var3.copyFrom(this.getWindow().getAttributes());
      var3.width = -1;
      var3.height = -1;
      this.getWindow().setAttributes(var3);
      this.core = new FileChooserCore(this);
      this.core.loadFolder(var2);
      this.listeners = new LinkedList();
      ((LinearLayout)this.findViewById(R.id.rootLayout)).setBackgroundColor(var1.getResources().getColor(R.color.daidalos_backgroud));
      this.core.addListener(new FileChooserCore.OnFileSelectedListener() {
         public void onFileSelected(File var1) {
            for(int var2 = 0; var2 < FileChooserDialog.this.listeners.size(); ++var2) {
               ((FileChooserDialog.OnFileSelectedListener)FileChooserDialog.this.listeners.get(var2)).onFileSelected(FileChooserDialog.this, var1);
            }

         }

         public void onFileSelected(File var1, String var2) {
            for(int var3 = 0; var3 < FileChooserDialog.this.listeners.size(); ++var3) {
               ((FileChooserDialog.OnFileSelectedListener)FileChooserDialog.this.listeners.get(var3)).onFileSelected(FileChooserDialog.this, var1, var2);
            }

         }
      });
      this.core.addListener(new FileChooserCore.OnCancelListener() {
         public void onCancel() {
            FileChooserDialog.super.onBackPressed();
         }
      });
   }

   public void addListener(FileChooserDialog.OnFileSelectedListener var1) {
      this.listeners.add(var1);
   }

   public LinearLayout getRootLayout() {
      View var1 = this.findViewById(R.id.rootLayout);
      LinearLayout var2;
      if (var1 instanceof LinearLayout) {
         var2 = (LinearLayout)var1;
      } else {
         var2 = null;
      }

      return var2;
   }

   public void loadFolder() {
      this.core.loadFolder();
   }

   public void loadFolder(String var1) {
      this.core.loadFolder(var1);
   }

   public void removeAllListeners() {
      this.listeners.clear();
   }

   public void removeListener(FileChooserDialog.OnFileSelectedListener var1) {
      this.listeners.remove(var1);
   }

   public void setCanCreateFiles(boolean var1) {
      this.core.setCanCreateFiles(var1);
   }

   public void setCurrentFolderName(String var1) {
      this.setTitle(var1);
   }

   public void setFilter(String var1) {
      this.core.setFilter(var1);
   }

   public void setFolderMode(boolean var1) {
      this.core.setFolderMode(var1);
   }

   public void setLabels(FileChooserLabels var1) {
      this.core.setLabels(var1);
   }

   public void setShowCancelButton(boolean var1) {
      this.core.setShowCancelButton(var1);
   }

   public void setShowConfirmation(boolean var1, boolean var2) {
      this.core.setShowConfirmationOnCreate(var2);
      this.core.setShowConfirmationOnSelect(var1);
   }

   public void setShowFullPath(boolean var1) {
      this.core.setShowFullPathInTitle(var1);
   }

   public void setShowOnlySelectable(boolean var1) {
      this.core.setShowOnlySelectable(var1);
   }

   public interface OnFileSelectedListener {
      void onFileSelected(Dialog var1, File var2);

      void onFileSelected(Dialog var1, File var2, String var3);
   }
}
