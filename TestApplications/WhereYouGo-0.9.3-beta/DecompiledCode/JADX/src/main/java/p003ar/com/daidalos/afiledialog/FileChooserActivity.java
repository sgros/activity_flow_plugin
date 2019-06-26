package p003ar.com.daidalos.afiledialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import java.io.File;
import p003ar.com.daidalos.afiledialog.FileChooserCore.OnCancelListener;
import p003ar.com.daidalos.afiledialog.FileChooserCore.OnFileSelectedListener;

/* renamed from: ar.com.daidalos.afiledialog.FileChooserActivity */
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

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserActivity$1 */
    class C03961 implements OnFileSelectedListener {
        C03961() {
        }

        public void onFileSelected(File folder, String name) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(FileChooserActivity.OUTPUT_FILE_OBJECT, folder);
            bundle.putString(FileChooserActivity.OUTPUT_NEW_FILE_NAME, name);
            intent.putExtras(bundle);
            FileChooserActivity.this.setResult(-1, intent);
            FileChooserActivity.this.finish();
        }

        public void onFileSelected(File file) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(FileChooserActivity.OUTPUT_FILE_OBJECT, file);
            intent.putExtras(bundle);
            FileChooserActivity.this.setResult(-1, intent);
            FileChooserActivity.this.finish();
        }
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserActivity$2 */
    class C03972 implements OnCancelListener {
        C03972() {
        }

        public void onCancel() {
            super.onBackPressed();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0175R.layout.daidalos_file_chooser);
        ((LinearLayout) findViewById(C0175R.C0174id.rootLayout)).setBackgroundColor(getResources().getColor(C0175R.color.daidalos_backgroud));
        this.useBackButton = false;
        this.core = new FileChooserCore(this);
        String folderPath = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(INPUT_START_FOLDER)) {
                folderPath = extras.getString(INPUT_START_FOLDER);
            }
            if (extras.containsKey(INPUT_REGEX_FILTER)) {
                this.core.setFilter(extras.getString(INPUT_REGEX_FILTER));
            }
            if (extras.containsKey(INPUT_SHOW_ONLY_SELECTABLE)) {
                this.core.setShowOnlySelectable(extras.getBoolean(INPUT_SHOW_ONLY_SELECTABLE));
            }
            if (extras.containsKey(INPUT_FOLDER_MODE)) {
                this.core.setFolderMode(extras.getBoolean(INPUT_FOLDER_MODE));
            }
            if (extras.containsKey(INPUT_CAN_CREATE_FILES)) {
                this.core.setCanCreateFiles(extras.getBoolean(INPUT_CAN_CREATE_FILES));
            }
            if (extras.containsKey(INPUT_LABELS)) {
                this.core.setLabels((FileChooserLabels) extras.get(INPUT_LABELS));
            }
            if (extras.containsKey(INPUT_SHOW_CONFIRMATION_ON_CREATE)) {
                this.core.setShowConfirmationOnCreate(extras.getBoolean(INPUT_SHOW_CONFIRMATION_ON_CREATE));
            }
            if (extras.containsKey(INPUT_SHOW_CANCEL_BUTTON)) {
                this.core.setShowCancelButton(extras.getBoolean(INPUT_SHOW_CANCEL_BUTTON));
            }
            if (extras.containsKey(INPUT_SHOW_CONFIRMATION_ON_SELECT)) {
                this.core.setShowConfirmationOnSelect(extras.getBoolean(INPUT_SHOW_CONFIRMATION_ON_SELECT));
            }
            if (extras.containsKey(INPUT_SHOW_FULL_PATH_IN_TITLE)) {
                this.core.setShowFullPathInTitle(extras.getBoolean(INPUT_SHOW_FULL_PATH_IN_TITLE));
            }
            if (extras.containsKey(INPUT_USE_BACK_BUTTON_TO_NAVIGATE)) {
                this.useBackButton = extras.getBoolean(INPUT_USE_BACK_BUTTON_TO_NAVIGATE);
            }
        }
        this.core.loadFolder(folderPath);
        this.startFolder = this.core.getCurrentFolder();
        this.core.addListener(new C03961());
        this.core.addListener(new C03972());
    }

    public void onBackPressed() {
        File current = this.core.getCurrentFolder();
        if (!this.useBackButton || current == null || current.getParent() == null || current.getPath().compareTo(this.startFolder.getPath()) == 0) {
            super.onBackPressed();
        } else {
            this.core.loadFolder(current.getParent());
        }
    }

    public LinearLayout getRootLayout() {
        View root = findViewById(C0175R.C0174id.rootLayout);
        return root instanceof LinearLayout ? (LinearLayout) root : null;
    }

    public Context getContext() {
        return this;
    }

    public void setCurrentFolderName(String name) {
        setTitle(name);
    }
}
