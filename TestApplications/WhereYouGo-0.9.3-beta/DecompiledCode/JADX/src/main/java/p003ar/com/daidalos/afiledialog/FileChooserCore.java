package p003ar.com.daidalos.afiledialog;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import p003ar.com.daidalos.afiledialog.view.FileItem;
import p003ar.com.daidalos.afiledialog.view.FileItem.OnFileClickListener;

/* renamed from: ar.com.daidalos.afiledialog.FileChooserCore */
class FileChooserCore {
    private static File defaultFolder = null;
    private OnClickListener addButtonClickListener = new C01661();
    private boolean canCreateFiles;
    private OnClickListener cancelButtonClickListener = new C01683();
    private List<OnCancelListener> cancelListeners;
    private FileChooser chooser;
    private File currentFolder;
    private OnFileClickListener fileItemClickListener = new C01724();
    private List<OnFileSelectedListener> fileSelectedListeners;
    private String filter;
    private boolean folderMode;
    private FileChooserLabels labels;
    private OnClickListener okButtonClickListener = new C01672();
    private boolean showCancelButton;
    private boolean showConfirmationOnCreate;
    private boolean showConfirmationOnSelect;
    private boolean showFullPathInTitle;
    private boolean showOnlySelectable;

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserCore$1 */
    class C01661 implements OnClickListener {

        /* renamed from: ar.com.daidalos.afiledialog.FileChooserCore$1$2 */
        class C01652 implements DialogInterface.OnClickListener {
            C01652() {
            }

            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }

        C01661() {
        }

        public void onClick(View v) {
            Context context = v.getContext();
            Builder alert = new Builder(context);
            String title = context.getString(FileChooserCore.this.folderMode ? C0175R.string.daidalos_create_folder : C0175R.string.daidalos_create_file);
            if (!(FileChooserCore.this.labels == null || FileChooserCore.this.labels.createFileDialogTitle == null)) {
                title = FileChooserCore.this.labels.createFileDialogTitle;
            }
            String message = context.getString(FileChooserCore.this.folderMode ? C0175R.string.daidalos_enter_folder_name : C0175R.string.daidalos_enter_file_name);
            if (!(FileChooserCore.this.labels == null || FileChooserCore.this.labels.createFileDialogMessage == null)) {
                message = FileChooserCore.this.labels.createFileDialogMessage;
            }
            String posButton = (FileChooserCore.this.labels == null || FileChooserCore.this.labels.createFileDialogAcceptButton == null) ? context.getString(C0175R.string.daidalos_accept) : FileChooserCore.this.labels.createFileDialogAcceptButton;
            String negButton = (FileChooserCore.this.labels == null || FileChooserCore.this.labels.createFileDialogCancelButton == null) ? context.getString(C0175R.string.daidalos_cancel) : FileChooserCore.this.labels.createFileDialogCancelButton;
            alert.setTitle(title);
            alert.setMessage(message);
            final EditText input = new EditText(context);
            input.setSingleLine();
            alert.setView(input);
            alert.setPositiveButton(posButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String fileName = input.getText().toString();
                    if (fileName != null && fileName.length() > 0) {
                        FileChooserCore.this.notifyFileListeners(FileChooserCore.this.currentFolder, fileName);
                    }
                }
            });
            alert.setNegativeButton(negButton, new C01652());
            alert.show();
        }
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserCore$2 */
    class C01672 implements OnClickListener {
        C01672() {
        }

        public void onClick(View v) {
            FileChooserCore.this.notifyFileListeners(FileChooserCore.this.currentFolder, null);
        }
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserCore$3 */
    class C01683 implements OnClickListener {
        C01683() {
        }

        public void onClick(View v) {
            FileChooserCore.this.notifyCancelListeners();
        }
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserCore$6 */
    class C01706 implements DialogInterface.OnClickListener {
        C01706() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
        }
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserCore$7 */
    class C01717 implements Comparator<File> {
        C01717() {
        }

        public int compare(File file1, File file2) {
            if (file1 == null || file2 == null) {
                return 0;
            }
            if (file1.isDirectory() && !file2.isDirectory()) {
                return -1;
            }
            if (!file2.isDirectory() || file1.isDirectory()) {
                return file1.getName().compareTo(file2.getName());
            }
            return 1;
        }
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserCore$OnCancelListener */
    public interface OnCancelListener {
        void onCancel();
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserCore$OnFileSelectedListener */
    public interface OnFileSelectedListener {
        void onFileSelected(File file);

        void onFileSelected(File file, String str);
    }

    /* renamed from: ar.com.daidalos.afiledialog.FileChooserCore$4 */
    class C01724 implements OnFileClickListener {
        C01724() {
        }

        public void onClick(FileItem source) {
            File file = source.getFile();
            if (file.isDirectory()) {
                FileChooserCore.this.loadFolder(file);
            } else {
                FileChooserCore.this.notifyFileListeners(file, null);
            }
        }
    }

    public FileChooserCore(FileChooser fileChooser) {
        this.chooser = fileChooser;
        this.fileSelectedListeners = new LinkedList();
        this.cancelListeners = new LinkedList();
        this.filter = null;
        this.showOnlySelectable = false;
        setCanCreateFiles(false);
        setFolderMode(false);
        this.currentFolder = null;
        this.labels = null;
        this.showConfirmationOnCreate = false;
        this.showConfirmationOnSelect = false;
        this.showFullPathInTitle = false;
        this.showCancelButton = false;
        LinearLayout root = this.chooser.getRootLayout();
        ((Button) root.findViewById(C0175R.C0174id.buttonAdd)).setOnClickListener(this.addButtonClickListener);
        ((Button) root.findViewById(C0175R.C0174id.buttonOk)).setOnClickListener(this.okButtonClickListener);
        ((Button) root.findViewById(C0175R.C0174id.buttonCancel)).setOnClickListener(this.cancelButtonClickListener);
    }

    public void addListener(OnFileSelectedListener listener) {
        this.fileSelectedListeners.add(listener);
    }

    public void removeListener(OnFileSelectedListener listener) {
        this.fileSelectedListeners.remove(listener);
    }

    public void addListener(OnCancelListener listener) {
        this.cancelListeners.add(listener);
    }

    public void removeListener(OnCancelListener listener) {
        this.cancelListeners.remove(listener);
    }

    public void removeAllListeners() {
        this.fileSelectedListeners.clear();
        this.cancelListeners.clear();
    }

    private void notifyCancelListeners() {
        for (int i = 0; i < this.cancelListeners.size(); i++) {
            ((OnCancelListener) this.cancelListeners.get(i)).onCancel();
        }
    }

    private void notifyFileListeners(final File file, final String name) {
        final boolean creation = name != null && name.length() > 0;
        if (!(creation && this.showConfirmationOnCreate) && (creation || !this.showConfirmationOnSelect)) {
            for (int i = 0; i < this.fileSelectedListeners.size(); i++) {
                if (creation) {
                    ((OnFileSelectedListener) this.fileSelectedListeners.get(i)).onFileSelected(file, name);
                } else {
                    ((OnFileSelectedListener) this.fileSelectedListeners.get(i)).onFileSelected(file);
                }
            }
            return;
        }
        String message;
        String negButton;
        Context context = this.chooser.getContext();
        Builder alert = new Builder(context);
        if (this.labels != null && ((creation && this.labels.messageConfirmCreation != null) || (!creation && this.labels.messageConfirmSelection != null))) {
            message = creation ? this.labels.messageConfirmCreation : this.labels.messageConfirmSelection;
        } else if (this.folderMode) {
            message = context.getString(creation ? C0175R.string.daidalos_confirm_create_folder : C0175R.string.daidalos_confirm_select_folder);
        } else {
            message = context.getString(creation ? C0175R.string.daidalos_confirm_create_file : C0175R.string.daidalos_confirm_select_file);
        }
        if (message != null) {
            message = message.replace("$file_name", name != null ? name : file.getName());
        }
        String posButton = (this.labels == null || this.labels.labelConfirmYesButton == null) ? context.getString(C0175R.string.daidalos_yes) : this.labels.labelConfirmYesButton;
        if (this.labels == null || this.labels.labelConfirmNoButton == null) {
            negButton = context.getString(C0175R.string.daidalos_no);
        } else {
            negButton = this.labels.labelConfirmNoButton;
        }
        alert.setMessage(message);
        alert.setPositiveButton(posButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                for (int i = 0; i < FileChooserCore.this.fileSelectedListeners.size(); i++) {
                    if (creation) {
                        ((OnFileSelectedListener) FileChooserCore.this.fileSelectedListeners.get(i)).onFileSelected(file, name);
                    } else {
                        ((OnFileSelectedListener) FileChooserCore.this.fileSelectedListeners.get(i)).onFileSelected(file);
                    }
                }
            }
        });
        alert.setNegativeButton(negButton, new C01706());
        alert.show();
    }

    public void setShowConfirmationOnSelect(boolean show) {
        this.showConfirmationOnSelect = show;
    }

    public void setShowConfirmationOnCreate(boolean show) {
        this.showConfirmationOnCreate = show;
    }

    public void setShowFullPathInTitle(boolean show) {
        this.showFullPathInTitle = show;
    }

    public void setLabels(FileChooserLabels labels) {
        this.labels = labels;
        if (labels != null) {
            LinearLayout root = this.chooser.getRootLayout();
            if (labels.labelAddButton != null) {
                ((Button) root.findViewById(C0175R.C0174id.buttonAdd)).setText(labels.labelAddButton);
            }
            if (labels.labelSelectButton != null) {
                ((Button) root.findViewById(C0175R.C0174id.buttonOk)).setText(labels.labelSelectButton);
            }
            if (labels.labelCancelButton != null) {
                ((Button) root.findViewById(C0175R.C0174id.buttonCancel)).setText(labels.labelCancelButton);
            }
        }
    }

    public void setFilter(String filter) {
        if (filter == null || filter.length() == 0) {
            this.filter = null;
        } else {
            this.filter = filter;
        }
        loadFolder(this.currentFolder);
    }

    public void setFolderMode(boolean folderMode) {
        this.folderMode = folderMode;
        updateButtonsLayout();
        loadFolder(this.currentFolder);
    }

    public void setShowCancelButton(boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
        updateButtonsLayout();
    }

    public void setCanCreateFiles(boolean canCreate) {
        this.canCreateFiles = canCreate;
        updateButtonsLayout();
    }

    public void setShowOnlySelectable(boolean show) {
        this.showOnlySelectable = show;
        loadFolder(this.currentFolder);
    }

    public File getCurrentFolder() {
        return this.currentFolder;
    }

    private void updateButtonsLayout() {
        int i;
        int i2 = 0;
        LinearLayout root = this.chooser.getRootLayout();
        View addButton = root.findViewById(C0175R.C0174id.buttonAdd);
        if (this.canCreateFiles) {
            i = 0;
        } else {
            i = 8;
        }
        addButton.setVisibility(i);
        View okButton = root.findViewById(C0175R.C0174id.buttonOk);
        if (this.folderMode) {
            i = 0;
        } else {
            i = 8;
        }
        okButton.setVisibility(i);
        View cancelButton = root.findViewById(C0175R.C0174id.buttonCancel);
        if (!this.showCancelButton) {
            i2 = 8;
        }
        cancelButton.setVisibility(i2);
    }

    public void loadFolder() {
        loadFolder(defaultFolder);
    }

    public void loadFolder(String folderPath) {
        File path = null;
        if (folderPath != null && folderPath.length() > 0) {
            path = new File(folderPath);
        }
        loadFolder(path);
    }

    public void loadFolder(File folder) {
        LinearLayout layout = (LinearLayout) this.chooser.getRootLayout().findViewById(C0175R.C0174id.linearLayoutFiles);
        layout.removeAllViews();
        if (folder != null && folder.exists()) {
            this.currentFolder = folder;
        } else if (defaultFolder != null) {
            this.currentFolder = defaultFolder;
        } else {
            this.currentFolder = Environment.getExternalStorageDirectory();
        }
        if (this.currentFolder.exists() && layout != null) {
            int i;
            List<FileItem> fileItems = new LinkedList();
            if (this.currentFolder.getParent() != null) {
                File parent = new File(this.currentFolder.getParent());
                if (parent.exists()) {
                    fileItems.add(new FileItem(this.chooser.getContext(), parent, ".."));
                }
            }
            if (this.currentFolder.isDirectory()) {
                File[] fileList = this.currentFolder.listFiles();
                if (fileList != null) {
                    Arrays.sort(fileList, new C01717());
                    for (i = 0; i < fileList.length; i++) {
                        boolean selectable = true;
                        if (!fileList[i].isDirectory()) {
                            selectable = !this.folderMode && (this.filter == null || fileList[i].getName().matches(this.filter));
                        }
                        if (selectable || !this.showOnlySelectable) {
                            FileItem fileItem = new FileItem(this.chooser.getContext(), fileList[i]);
                            fileItem.setSelectable(selectable);
                            fileItems.add(fileItem);
                        }
                    }
                }
                this.chooser.setCurrentFolderName(this.showFullPathInTitle ? this.currentFolder.getPath() : this.currentFolder.getName());
            } else {
                fileItems.add(new FileItem(this.chooser.getContext(), this.currentFolder));
            }
            for (i = 0; i < fileItems.size(); i++) {
                ((FileItem) fileItems.get(i)).addListener(this.fileItemClickListener);
                layout.addView((View) fileItems.get(i));
            }
            defaultFolder = this.currentFolder;
        }
    }
}
