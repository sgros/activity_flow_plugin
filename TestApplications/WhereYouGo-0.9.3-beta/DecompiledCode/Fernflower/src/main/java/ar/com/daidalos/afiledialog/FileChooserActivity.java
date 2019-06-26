package ar.com.daidalos.afiledialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import java.io.File;

public class FileChooserActivity extends Activity implements FileChooser {
   public static final String INPUT_CAN_CREATE_FILES = "input_can_create_files";
   public static final String INPUT_FOLDER_MODE = "input_folder_mode";
   public static final String INPUT_LABELS = "input_labels";
   public static final String INPUT_REGEX_FILTER = "input_regex_filter";
   public static final String INPUT_SHOW_CANCEL_BUTTON = "input_show_cancel_button";
   public static final String INPUT_SHOW_CONFIRMATION_ON_CREATE = "input_show_confirmation_on_create";
   public static final String INPUT_SHOW_CONFIRMATION_ON_SELECT = "input_show_confirmation_on_select";
   public static final String INPUT_SHOW_FULL_PATH_IN_TITLE = "input_show_full_path_in_title";
   public static final String INPUT_SHOW_ONLY_SELECTABLE = "input_show_only_selectable";
   public static final String INPUT_START_FOLDER = "input_start_folder";
   public static final String INPUT_USE_BACK_BUTTON_TO_NAVIGATE = "input_use_back_button_to_navigate";
   public static final String OUTPUT_FILE_OBJECT = "output_file_object";
   public static final String OUTPUT_NEW_FILE_NAME = "output_new_file_name";
   private FileChooserCore core;
   private File startFolder;
   private boolean useBackButton;

   public Context getContext() {
      return this;
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

   public void onBackPressed() {
      File var1 = this.core.getCurrentFolder();
      if (this.useBackButton && var1 != null && var1.getParent() != null && var1.getPath().compareTo(this.startFolder.getPath()) != 0) {
         this.core.loadFolder(var1.getParent());
      } else {
         super.onBackPressed();
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.daidalos_file_chooser);
      ((LinearLayout)this.findViewById(R.id.rootLayout)).setBackgroundColor(this.getResources().getColor(R.color.daidalos_backgroud));
      this.useBackButton = false;
      this.core = new FileChooserCore(this);
      String var2 = null;
      String var4 = null;
      Bundle var3 = this.getIntent().getExtras();
      if (var3 != null) {
         if (var3.containsKey("input_start_folder")) {
            var4 = var3.getString("input_start_folder");
         }

         if (var3.containsKey("input_regex_filter")) {
            this.core.setFilter(var3.getString("input_regex_filter"));
         }

         if (var3.containsKey("input_show_only_selectable")) {
            this.core.setShowOnlySelectable(var3.getBoolean("input_show_only_selectable"));
         }

         if (var3.containsKey("input_folder_mode")) {
            this.core.setFolderMode(var3.getBoolean("input_folder_mode"));
         }

         if (var3.containsKey("input_can_create_files")) {
            this.core.setCanCreateFiles(var3.getBoolean("input_can_create_files"));
         }

         if (var3.containsKey("input_labels")) {
            this.core.setLabels((FileChooserLabels)var3.get("input_labels"));
         }

         if (var3.containsKey("input_show_confirmation_on_create")) {
            this.core.setShowConfirmationOnCreate(var3.getBoolean("input_show_confirmation_on_create"));
         }

         if (var3.containsKey("input_show_cancel_button")) {
            this.core.setShowCancelButton(var3.getBoolean("input_show_cancel_button"));
         }

         if (var3.containsKey("input_show_confirmation_on_select")) {
            this.core.setShowConfirmationOnSelect(var3.getBoolean("input_show_confirmation_on_select"));
         }

         if (var3.containsKey("input_show_full_path_in_title")) {
            this.core.setShowFullPathInTitle(var3.getBoolean("input_show_full_path_in_title"));
         }

         var2 = var4;
         if (var3.containsKey("input_use_back_button_to_navigate")) {
            this.useBackButton = var3.getBoolean("input_use_back_button_to_navigate");
            var2 = var4;
         }
      }

      this.core.loadFolder(var2);
      this.startFolder = this.core.getCurrentFolder();
      this.core.addListener(new FileChooserCore.OnFileSelectedListener() {
         public void onFileSelected(File var1) {
            Intent var2 = new Intent();
            Bundle var3 = new Bundle();
            var3.putSerializable("output_file_object", var1);
            var2.putExtras(var3);
            FileChooserActivity.this.setResult(-1, var2);
            FileChooserActivity.this.finish();
         }

         public void onFileSelected(File var1, String var2) {
            Intent var3 = new Intent();
            Bundle var4 = new Bundle();
            var4.putSerializable("output_file_object", var1);
            var4.putString("output_new_file_name", var2);
            var3.putExtras(var4);
            FileChooserActivity.this.setResult(-1, var3);
            FileChooserActivity.this.finish();
         }
      });
      this.core.addListener(new FileChooserCore.OnCancelListener() {
         public void onCancel() {
            FileChooserActivity.super.onBackPressed();
         }
      });
   }

   public void setCurrentFolderName(String var1) {
      this.setTitle(var1);
   }
}
