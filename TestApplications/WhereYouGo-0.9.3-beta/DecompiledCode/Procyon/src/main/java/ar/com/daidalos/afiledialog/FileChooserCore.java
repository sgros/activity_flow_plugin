// 
// Decompiled by Procyon v0.5.34
// 

package ar.com.daidalos.afiledialog;

import java.util.Arrays;
import java.util.Comparator;
import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.Button;
import java.util.LinkedList;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.widget.EditText;
import android.app.AlertDialog$Builder;
import android.view.View;
import ar.com.daidalos.afiledialog.view.FileItem;
import java.util.List;
import android.view.View$OnClickListener;
import java.io.File;

class FileChooserCore
{
    private static File defaultFolder;
    private View$OnClickListener addButtonClickListener;
    private boolean canCreateFiles;
    private View$OnClickListener cancelButtonClickListener;
    private List<OnCancelListener> cancelListeners;
    private FileChooser chooser;
    private File currentFolder;
    private FileItem.OnFileClickListener fileItemClickListener;
    private List<OnFileSelectedListener> fileSelectedListeners;
    private String filter;
    private boolean folderMode;
    private FileChooserLabels labels;
    private View$OnClickListener okButtonClickListener;
    private boolean showCancelButton;
    private boolean showConfirmationOnCreate;
    private boolean showConfirmationOnSelect;
    private boolean showFullPathInTitle;
    private boolean showOnlySelectable;
    
    static {
        FileChooserCore.defaultFolder = null;
    }
    
    public FileChooserCore(final FileChooser chooser) {
        this.addButtonClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final Context context = view.getContext();
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(context);
                int n;
                if (FileChooserCore.this.folderMode) {
                    n = R.string.daidalos_create_folder;
                }
                else {
                    n = R.string.daidalos_create_file;
                }
                String title = context.getString(n);
                if (FileChooserCore.this.labels != null) {
                    title = title;
                    if (FileChooserCore.this.labels.createFileDialogTitle != null) {
                        title = FileChooserCore.this.labels.createFileDialogTitle;
                    }
                }
                int n2;
                if (FileChooserCore.this.folderMode) {
                    n2 = R.string.daidalos_enter_folder_name;
                }
                else {
                    n2 = R.string.daidalos_enter_file_name;
                }
                String message = context.getString(n2);
                if (FileChooserCore.this.labels != null) {
                    message = message;
                    if (FileChooserCore.this.labels.createFileDialogMessage != null) {
                        message = FileChooserCore.this.labels.createFileDialogMessage;
                    }
                }
                String s;
                if (FileChooserCore.this.labels != null && FileChooserCore.this.labels.createFileDialogAcceptButton != null) {
                    s = FileChooserCore.this.labels.createFileDialogAcceptButton;
                }
                else {
                    s = context.getString(R.string.daidalos_accept);
                }
                String s2;
                if (FileChooserCore.this.labels != null && FileChooserCore.this.labels.createFileDialogCancelButton != null) {
                    s2 = FileChooserCore.this.labels.createFileDialogCancelButton;
                }
                else {
                    s2 = context.getString(R.string.daidalos_cancel);
                }
                alertDialog$Builder.setTitle((CharSequence)title);
                alertDialog$Builder.setMessage((CharSequence)message);
                final EditText view2 = new EditText(context);
                view2.setSingleLine();
                alertDialog$Builder.setView((View)view2);
                alertDialog$Builder.setPositiveButton((CharSequence)s, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        final String string = view2.getText().toString();
                        if (string != null && string.length() > 0) {
                            FileChooserCore.this.notifyFileListeners(FileChooserCore.this.currentFolder, string);
                        }
                    }
                });
                alertDialog$Builder.setNegativeButton((CharSequence)s2, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                    }
                });
                alertDialog$Builder.show();
            }
        };
        this.okButtonClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                FileChooserCore.this.notifyFileListeners(FileChooserCore.this.currentFolder, null);
            }
        };
        this.cancelButtonClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                FileChooserCore.this.notifyCancelListeners();
            }
        };
        this.fileItemClickListener = new FileItem.OnFileClickListener() {
            @Override
            public void onClick(final FileItem fileItem) {
                final File file = fileItem.getFile();
                if (file.isDirectory()) {
                    FileChooserCore.this.loadFolder(file);
                }
                else {
                    FileChooserCore.this.notifyFileListeners(file, null);
                }
            }
        };
        this.chooser = chooser;
        this.fileSelectedListeners = new LinkedList<OnFileSelectedListener>();
        this.cancelListeners = new LinkedList<OnCancelListener>();
        this.filter = null;
        this.setCanCreateFiles(this.showOnlySelectable = false);
        this.setFolderMode(false);
        this.currentFolder = null;
        this.labels = null;
        this.showConfirmationOnCreate = false;
        this.showConfirmationOnSelect = false;
        this.showFullPathInTitle = false;
        this.showCancelButton = false;
        final LinearLayout rootLayout = this.chooser.getRootLayout();
        ((Button)rootLayout.findViewById(R.id.buttonAdd)).setOnClickListener(this.addButtonClickListener);
        ((Button)rootLayout.findViewById(R.id.buttonOk)).setOnClickListener(this.okButtonClickListener);
        ((Button)rootLayout.findViewById(R.id.buttonCancel)).setOnClickListener(this.cancelButtonClickListener);
    }
    
    private void notifyCancelListeners() {
        for (int i = 0; i < this.cancelListeners.size(); ++i) {
            this.cancelListeners.get(i).onCancel();
        }
    }
    
    private void notifyFileListeners(final File file, final String s) {
        final boolean b = (s != null && s.length() > 0) || false;
        if ((b && this.showConfirmationOnCreate) || (!b && this.showConfirmationOnSelect)) {
            final Context context = this.chooser.getContext();
            final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(context);
            String s2;
            if (this.labels != null && ((b && this.labels.messageConfirmCreation != null) || (!b && this.labels.messageConfirmSelection != null))) {
                if (b) {
                    s2 = this.labels.messageConfirmCreation;
                }
                else {
                    s2 = this.labels.messageConfirmSelection;
                }
            }
            else if (this.folderMode) {
                int n;
                if (b) {
                    n = R.string.daidalos_confirm_create_folder;
                }
                else {
                    n = R.string.daidalos_confirm_select_folder;
                }
                s2 = context.getString(n);
            }
            else {
                int n2;
                if (b) {
                    n2 = R.string.daidalos_confirm_create_file;
                }
                else {
                    n2 = R.string.daidalos_confirm_select_file;
                }
                s2 = context.getString(n2);
            }
            String replace = s2;
            if (s2 != null) {
                String name;
                if (s != null) {
                    name = s;
                }
                else {
                    name = file.getName();
                }
                replace = s2.replace("$file_name", name);
            }
            String s3;
            if (this.labels != null && this.labels.labelConfirmYesButton != null) {
                s3 = this.labels.labelConfirmYesButton;
            }
            else {
                s3 = context.getString(R.string.daidalos_yes);
            }
            String s4;
            if (this.labels != null && this.labels.labelConfirmNoButton != null) {
                s4 = this.labels.labelConfirmNoButton;
            }
            else {
                s4 = context.getString(R.string.daidalos_no);
            }
            alertDialog$Builder.setMessage((CharSequence)replace);
            alertDialog$Builder.setPositiveButton((CharSequence)s3, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, int i) {
                    for (int i = 0; i < FileChooserCore.this.fileSelectedListeners.size(); ++i) {
                        if (b) {
                            ((OnFileSelectedListener)FileChooserCore.this.fileSelectedListeners.get(i)).onFileSelected(file, s);
                        }
                        else {
                            ((OnFileSelectedListener)FileChooserCore.this.fileSelectedListeners.get(i)).onFileSelected(file);
                        }
                    }
                }
            });
            alertDialog$Builder.setNegativeButton((CharSequence)s4, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                }
            });
            alertDialog$Builder.show();
        }
        else {
            for (int i = 0; i < this.fileSelectedListeners.size(); ++i) {
                if (b) {
                    this.fileSelectedListeners.get(i).onFileSelected(file, s);
                }
                else {
                    this.fileSelectedListeners.get(i).onFileSelected(file);
                }
            }
        }
    }
    
    private void updateButtonsLayout() {
        final int n = 0;
        final LinearLayout rootLayout = this.chooser.getRootLayout();
        final View viewById = rootLayout.findViewById(R.id.buttonAdd);
        int visibility;
        if (this.canCreateFiles) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        viewById.setVisibility(visibility);
        final View viewById2 = rootLayout.findViewById(R.id.buttonOk);
        int visibility2;
        if (this.folderMode) {
            visibility2 = 0;
        }
        else {
            visibility2 = 8;
        }
        viewById2.setVisibility(visibility2);
        final View viewById3 = rootLayout.findViewById(R.id.buttonCancel);
        int visibility3;
        if (this.showCancelButton) {
            visibility3 = n;
        }
        else {
            visibility3 = 8;
        }
        viewById3.setVisibility(visibility3);
    }
    
    public void addListener(final OnCancelListener onCancelListener) {
        this.cancelListeners.add(onCancelListener);
    }
    
    public void addListener(final OnFileSelectedListener onFileSelectedListener) {
        this.fileSelectedListeners.add(onFileSelectedListener);
    }
    
    public File getCurrentFolder() {
        return this.currentFolder;
    }
    
    public void loadFolder() {
        this.loadFolder(FileChooserCore.defaultFolder);
    }
    
    public void loadFolder(File currentFolder) {
        final LinearLayout linearLayout = (LinearLayout)this.chooser.getRootLayout().findViewById(R.id.linearLayoutFiles);
        linearLayout.removeAllViews();
        if (currentFolder == null || !currentFolder.exists()) {
            if (FileChooserCore.defaultFolder != null) {
                this.currentFolder = FileChooserCore.defaultFolder;
            }
            else {
                this.currentFolder = Environment.getExternalStorageDirectory();
            }
        }
        else {
            this.currentFolder = currentFolder;
        }
        if (this.currentFolder.exists() && linearLayout != null) {
            final LinkedList<View> list = new LinkedList<View>();
            if (this.currentFolder.getParent() != null) {
                currentFolder = new File(this.currentFolder.getParent());
                if (currentFolder.exists()) {
                    list.add(new FileItem(this.chooser.getContext(), currentFolder, ".."));
                }
            }
            if (this.currentFolder.isDirectory()) {
                final File[] listFiles = this.currentFolder.listFiles();
                if (listFiles != null) {
                    Arrays.sort(listFiles, new Comparator<File>() {
                        @Override
                        public int compare(final File file, final File file2) {
                            int compareTo;
                            if (file != null && file2 != null) {
                                if (file.isDirectory() && !file2.isDirectory()) {
                                    compareTo = -1;
                                }
                                else if (file2.isDirectory() && !file.isDirectory()) {
                                    compareTo = 1;
                                }
                                else {
                                    compareTo = file.getName().compareTo(file2.getName());
                                }
                            }
                            else {
                                compareTo = 0;
                            }
                            return compareTo;
                        }
                    });
                    for (int i = 0; i < listFiles.length; ++i) {
                        boolean selectable = true;
                        if (!listFiles[i].isDirectory()) {
                            selectable = (!this.folderMode && (this.filter == null || listFiles[i].getName().matches(this.filter)));
                        }
                        if (selectable || !this.showOnlySelectable) {
                            final FileItem fileItem = new FileItem(this.chooser.getContext(), listFiles[i]);
                            fileItem.setSelectable(selectable);
                            list.add((View)fileItem);
                        }
                    }
                }
                String currentFolderName;
                if (this.showFullPathInTitle) {
                    currentFolderName = this.currentFolder.getPath();
                }
                else {
                    currentFolderName = this.currentFolder.getName();
                }
                this.chooser.setCurrentFolderName(currentFolderName);
            }
            else {
                list.add((View)new FileItem(this.chooser.getContext(), this.currentFolder));
            }
            for (int j = 0; j < list.size(); ++j) {
                list.get(j).addListener(this.fileItemClickListener);
                linearLayout.addView((View)list.get(j));
            }
            FileChooserCore.defaultFolder = this.currentFolder;
        }
    }
    
    public void loadFolder(final String pathname) {
        File file = null;
        if (pathname != null) {
            file = file;
            if (pathname.length() > 0) {
                file = new File(pathname);
            }
        }
        this.loadFolder(file);
    }
    
    public void removeAllListeners() {
        this.fileSelectedListeners.clear();
        this.cancelListeners.clear();
    }
    
    public void removeListener(final OnCancelListener onCancelListener) {
        this.cancelListeners.remove(onCancelListener);
    }
    
    public void removeListener(final OnFileSelectedListener onFileSelectedListener) {
        this.fileSelectedListeners.remove(onFileSelectedListener);
    }
    
    public void setCanCreateFiles(final boolean canCreateFiles) {
        this.canCreateFiles = canCreateFiles;
        this.updateButtonsLayout();
    }
    
    public void setFilter(final String filter) {
        if (filter == null || filter.length() == 0) {
            this.filter = null;
        }
        else {
            this.filter = filter;
        }
        this.loadFolder(this.currentFolder);
    }
    
    public void setFolderMode(final boolean folderMode) {
        this.folderMode = folderMode;
        this.updateButtonsLayout();
        this.loadFolder(this.currentFolder);
    }
    
    public void setLabels(final FileChooserLabels labels) {
        this.labels = labels;
        if (labels != null) {
            final LinearLayout rootLayout = this.chooser.getRootLayout();
            if (labels.labelAddButton != null) {
                ((Button)rootLayout.findViewById(R.id.buttonAdd)).setText((CharSequence)labels.labelAddButton);
            }
            if (labels.labelSelectButton != null) {
                ((Button)rootLayout.findViewById(R.id.buttonOk)).setText((CharSequence)labels.labelSelectButton);
            }
            if (labels.labelCancelButton != null) {
                ((Button)rootLayout.findViewById(R.id.buttonCancel)).setText((CharSequence)labels.labelCancelButton);
            }
        }
    }
    
    public void setShowCancelButton(final boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
        this.updateButtonsLayout();
    }
    
    public void setShowConfirmationOnCreate(final boolean showConfirmationOnCreate) {
        this.showConfirmationOnCreate = showConfirmationOnCreate;
    }
    
    public void setShowConfirmationOnSelect(final boolean showConfirmationOnSelect) {
        this.showConfirmationOnSelect = showConfirmationOnSelect;
    }
    
    public void setShowFullPathInTitle(final boolean showFullPathInTitle) {
        this.showFullPathInTitle = showFullPathInTitle;
    }
    
    public void setShowOnlySelectable(final boolean showOnlySelectable) {
        this.showOnlySelectable = showOnlySelectable;
        this.loadFolder(this.currentFolder);
    }
    
    public interface OnCancelListener
    {
        void onCancel();
    }
    
    public interface OnFileSelectedListener
    {
        void onFileSelected(final File p0);
        
        void onFileSelected(final File p0, final String p1);
    }
}
