// 
// Decompiled by Procyon v0.5.34
// 

package ar.com.daidalos.afiledialog;

import android.view.View;
import java.io.File;
import android.widget.LinearLayout;
import java.util.LinkedList;
import android.view.WindowManager$LayoutParams;
import android.content.Context;
import java.util.List;
import android.app.Dialog;

public class FileChooserDialog extends Dialog implements FileChooser
{
    private FileChooserCore core;
    private List<OnFileSelectedListener> listeners;
    
    public FileChooserDialog(final Context context) {
        this(context, null);
    }
    
    public FileChooserDialog(final Context context, final String s) {
        super(context);
        this.setContentView(R.layout.daidalos_file_chooser);
        final WindowManager$LayoutParams attributes = new WindowManager$LayoutParams();
        attributes.copyFrom(this.getWindow().getAttributes());
        attributes.width = -1;
        attributes.height = -1;
        this.getWindow().setAttributes(attributes);
        (this.core = new FileChooserCore(this)).loadFolder(s);
        this.listeners = new LinkedList<OnFileSelectedListener>();
        ((LinearLayout)this.findViewById(R.id.rootLayout)).setBackgroundColor(context.getResources().getColor(R.color.daidalos_backgroud));
        this.core.addListener((FileChooserCore.OnFileSelectedListener)new FileChooserCore.OnFileSelectedListener() {
            @Override
            public void onFileSelected(final File file) {
                for (int i = 0; i < FileChooserDialog.this.listeners.size(); ++i) {
                    ((FileChooserDialog.OnFileSelectedListener)FileChooserDialog.this.listeners.get(i)).onFileSelected(FileChooserDialog.this, file);
                }
            }
            
            @Override
            public void onFileSelected(final File file, final String s) {
                for (int i = 0; i < FileChooserDialog.this.listeners.size(); ++i) {
                    ((FileChooserDialog.OnFileSelectedListener)FileChooserDialog.this.listeners.get(i)).onFileSelected(FileChooserDialog.this, file, s);
                }
            }
        });
        this.core.addListener((FileChooserCore.OnCancelListener)new FileChooserCore.OnCancelListener() {
            @Override
            public void onCancel() {
                FileChooserDialog.access$101(FileChooserDialog.this);
            }
        });
    }
    
    static /* synthetic */ void access$101(final FileChooserDialog fileChooserDialog) {
        fileChooserDialog.onBackPressed();
    }
    
    public void addListener(final OnFileSelectedListener onFileSelectedListener) {
        this.listeners.add(onFileSelectedListener);
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
    
    public void loadFolder() {
        this.core.loadFolder();
    }
    
    public void loadFolder(final String s) {
        this.core.loadFolder(s);
    }
    
    public void removeAllListeners() {
        this.listeners.clear();
    }
    
    public void removeListener(final OnFileSelectedListener onFileSelectedListener) {
        this.listeners.remove(onFileSelectedListener);
    }
    
    public void setCanCreateFiles(final boolean canCreateFiles) {
        this.core.setCanCreateFiles(canCreateFiles);
    }
    
    public void setCurrentFolderName(final String title) {
        this.setTitle((CharSequence)title);
    }
    
    public void setFilter(final String filter) {
        this.core.setFilter(filter);
    }
    
    public void setFolderMode(final boolean folderMode) {
        this.core.setFolderMode(folderMode);
    }
    
    public void setLabels(final FileChooserLabels labels) {
        this.core.setLabels(labels);
    }
    
    public void setShowCancelButton(final boolean showCancelButton) {
        this.core.setShowCancelButton(showCancelButton);
    }
    
    public void setShowConfirmation(final boolean showConfirmationOnSelect, final boolean showConfirmationOnCreate) {
        this.core.setShowConfirmationOnCreate(showConfirmationOnCreate);
        this.core.setShowConfirmationOnSelect(showConfirmationOnSelect);
    }
    
    public void setShowFullPath(final boolean showFullPathInTitle) {
        this.core.setShowFullPathInTitle(showFullPathInTitle);
    }
    
    public void setShowOnlySelectable(final boolean showOnlySelectable) {
        this.core.setShowOnlySelectable(showOnlySelectable);
    }
    
    public interface OnFileSelectedListener
    {
        void onFileSelected(final Dialog p0, final File p1);
        
        void onFileSelected(final Dialog p0, final File p1, final String p2);
    }
}
