package p003ar.com.daidalos.afiledialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import p003ar.com.daidalos.afiledialog.FileChooserCore.OnCancelListener;
import p003ar.com.daidalos.afiledialog.FileChooserCore.OnFileSelectedListener;

/* renamed from: ar.com.daidalos.afiledialog.FileChooserDialog */
public class FileChooserDialog extends Dialog implements FileChooser {
    private FileChooserCore core;
    private List<OnFileSelectedListener> listeners;

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserDialog$OnFileSelectedListener */
    public interface OnFileSelectedListener {
        void onFileSelected(Dialog dialog, File file);

        void onFileSelected(Dialog dialog, File file, String str);
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserDialog$1 */
    class C03981 implements OnFileSelectedListener {
        C03981() {
        }

        public void onFileSelected(File folder, String name) {
            for (int i = 0; i < FileChooserDialog.this.listeners.size(); i++) {
                ((OnFileSelectedListener) FileChooserDialog.this.listeners.get(i)).onFileSelected(FileChooserDialog.this, folder, name);
            }
        }

        public void onFileSelected(File file) {
            for (int i = 0; i < FileChooserDialog.this.listeners.size(); i++) {
                ((OnFileSelectedListener) FileChooserDialog.this.listeners.get(i)).onFileSelected(FileChooserDialog.this, file);
            }
        }
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserDialog$2 */
    class C03992 implements OnCancelListener {
        C03992() {
        }

        public void onCancel() {
            super.onBackPressed();
        }
    }

    public FileChooserDialog(Context context) {
        this(context, null);
    }

    public FileChooserDialog(Context context, String folderPath) {
        super(context);
        setContentView(C0175R.layout.daidalos_file_chooser);
        LayoutParams lp = new LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = -1;
        lp.height = -1;
        getWindow().setAttributes(lp);
        this.core = new FileChooserCore(this);
        this.core.loadFolder(folderPath);
        this.listeners = new LinkedList();
        ((LinearLayout) findViewById(C0175R.C0174id.rootLayout)).setBackgroundColor(context.getResources().getColor(C0175R.color.daidalos_backgroud));
        this.core.addListener(new C03981());
        this.core.addListener(new C03992());
    }

    public void addListener(OnFileSelectedListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(OnFileSelectedListener listener) {
        this.listeners.remove(listener);
    }

    public void removeAllListeners() {
        this.listeners.clear();
    }

    public void setFilter(String filter) {
        this.core.setFilter(filter);
    }

    public void setShowOnlySelectable(boolean show) {
        this.core.setShowOnlySelectable(show);
    }

    public void loadFolder() {
        this.core.loadFolder();
    }

    public void loadFolder(String folderPath) {
        this.core.loadFolder(folderPath);
    }

    public void setFolderMode(boolean folderMode) {
        this.core.setFolderMode(folderMode);
    }

    public void setCanCreateFiles(boolean canCreate) {
        this.core.setCanCreateFiles(canCreate);
    }

    public void setShowCancelButton(boolean canShow) {
        this.core.setShowCancelButton(canShow);
    }

    public void setLabels(FileChooserLabels labels) {
        this.core.setLabels(labels);
    }

    public void setShowConfirmation(boolean onSelect, boolean onCreate) {
        this.core.setShowConfirmationOnCreate(onCreate);
        this.core.setShowConfirmationOnSelect(onSelect);
    }

    public void setShowFullPath(boolean show) {
        this.core.setShowFullPathInTitle(show);
    }

    public LinearLayout getRootLayout() {
        View root = findViewById(C0175R.C0174id.rootLayout);
        return root instanceof LinearLayout ? (LinearLayout) root : null;
    }

    public void setCurrentFolderName(String name) {
        setTitle(name);
    }
}
