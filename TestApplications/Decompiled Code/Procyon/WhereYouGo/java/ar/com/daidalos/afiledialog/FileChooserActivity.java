// 
// Decompiled by Procyon v0.5.34
// 

package ar.com.daidalos.afiledialog;

import java.io.Serializable;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.content.Context;
import java.io.File;
import android.app.Activity;

public class FileChooserActivity extends Activity implements FileChooser
{
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
    
    static /* synthetic */ void access$001(final FileChooserActivity fileChooserActivity) {
        fileChooserActivity.onBackPressed();
    }
    
    public Context getContext() {
        return (Context)this;
    }
    
    public LinearLayout getRootLayout() {
        final View viewById = this.findViewById(R.id.rootLayout);
        LinearLayout linearLayout;
        if (viewById instanceof LinearLayout) {
            linearLayout = (LinearLayout)viewById;
        }
        else {
            linearLayout = null;
        }
        return linearLayout;
    }
    
    public void onBackPressed() {
        final File currentFolder = this.core.getCurrentFolder();
        if (!this.useBackButton || currentFolder == null || currentFolder.getParent() == null || currentFolder.getPath().compareTo(this.startFolder.getPath()) == 0) {
            super.onBackPressed();
        }
        else {
            this.core.loadFolder(currentFolder.getParent());
        }
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.daidalos_file_chooser);
        ((LinearLayout)this.findViewById(R.id.rootLayout)).setBackgroundColor(this.getResources().getColor(R.color.daidalos_backgroud));
        this.useBackButton = false;
        this.core = new FileChooserCore(this);
        String s = null;
        String string = null;
        final Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("input_start_folder")) {
                string = extras.getString("input_start_folder");
            }
            if (extras.containsKey("input_regex_filter")) {
                this.core.setFilter(extras.getString("input_regex_filter"));
            }
            if (extras.containsKey("input_show_only_selectable")) {
                this.core.setShowOnlySelectable(extras.getBoolean("input_show_only_selectable"));
            }
            if (extras.containsKey("input_folder_mode")) {
                this.core.setFolderMode(extras.getBoolean("input_folder_mode"));
            }
            if (extras.containsKey("input_can_create_files")) {
                this.core.setCanCreateFiles(extras.getBoolean("input_can_create_files"));
            }
            if (extras.containsKey("input_labels")) {
                this.core.setLabels((FileChooserLabels)extras.get("input_labels"));
            }
            if (extras.containsKey("input_show_confirmation_on_create")) {
                this.core.setShowConfirmationOnCreate(extras.getBoolean("input_show_confirmation_on_create"));
            }
            if (extras.containsKey("input_show_cancel_button")) {
                this.core.setShowCancelButton(extras.getBoolean("input_show_cancel_button"));
            }
            if (extras.containsKey("input_show_confirmation_on_select")) {
                this.core.setShowConfirmationOnSelect(extras.getBoolean("input_show_confirmation_on_select"));
            }
            if (extras.containsKey("input_show_full_path_in_title")) {
                this.core.setShowFullPathInTitle(extras.getBoolean("input_show_full_path_in_title"));
            }
            s = string;
            if (extras.containsKey("input_use_back_button_to_navigate")) {
                this.useBackButton = extras.getBoolean("input_use_back_button_to_navigate");
                s = string;
            }
        }
        this.core.loadFolder(s);
        this.startFolder = this.core.getCurrentFolder();
        this.core.addListener((FileChooserCore.OnFileSelectedListener)new FileChooserCore.OnFileSelectedListener() {
            @Override
            public void onFileSelected(final File file) {
                final Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                bundle.putSerializable("output_file_object", (Serializable)file);
                intent.putExtras(bundle);
                FileChooserActivity.this.setResult(-1, intent);
                FileChooserActivity.this.finish();
            }
            
            @Override
            public void onFileSelected(final File file, final String s) {
                final Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                bundle.putSerializable("output_file_object", (Serializable)file);
                bundle.putString("output_new_file_name", s);
                intent.putExtras(bundle);
                FileChooserActivity.this.setResult(-1, intent);
                FileChooserActivity.this.finish();
            }
        });
        this.core.addListener((FileChooserCore.OnCancelListener)new FileChooserCore.OnCancelListener() {
            @Override
            public void onCancel() {
                FileChooserActivity.access$001(FileChooserActivity.this);
            }
        });
    }
    
    public void setCurrentFolderName(final String title) {
        this.setTitle((CharSequence)title);
    }
}
