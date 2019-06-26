// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.filepicker;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.content.SharedPreferences$Editor;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.app.Dialog;
import android.widget.ListAdapter;
import android.widget.GridView;
import android.content.Context;
import android.os.Bundle;
import java.util.Arrays;
import menion.android.whereyougo.maps.mapsforge.filefilter.ValidFileFilter;
import java.io.FileFilter;
import java.io.File;
import java.util.Comparator;
import android.widget.AdapterView$OnItemClickListener;
import android.app.Activity;

public class FilePicker extends Activity implements AdapterView$OnItemClickListener
{
    private static final String CURRENT_DIRECTORY = "currentDirectory";
    private static final String DEFAULT_DIRECTORY = "/";
    private static final int DIALOG_FILE_INVALID = 0;
    private static final int DIALOG_FILE_SELECT = 1;
    private static final String PREFERENCES_FILE = "FilePicker";
    public static final String SELECTED_FILE = "selectedFile";
    private static Comparator<File> fileComparator;
    private static FileFilter fileDisplayFilter;
    private static ValidFileFilter fileSelectFilter;
    private File currentDirectory;
    private FilePickerIconAdapter filePickerIconAdapter;
    private File[] files;
    private File[] filesWithParentFolder;
    
    static {
        FilePicker.fileComparator = getDefaultFileComparator();
    }
    
    private void browseToCurrentDirectory() {
        this.setTitle((CharSequence)this.currentDirectory.getAbsolutePath());
        if (FilePicker.fileDisplayFilter == null) {
            this.files = this.currentDirectory.listFiles();
        }
        else {
            this.files = this.currentDirectory.listFiles(FilePicker.fileDisplayFilter);
        }
        if (this.files == null) {
            this.files = new File[0];
        }
        else {
            Arrays.sort(this.files, FilePicker.fileComparator);
        }
        if (this.currentDirectory.getParentFile() != null) {
            (this.filesWithParentFolder = new File[this.files.length + 1])[0] = this.currentDirectory.getParentFile();
            System.arraycopy(this.files, 0, this.filesWithParentFolder, 1, this.files.length);
            this.files = this.filesWithParentFolder;
            this.filePickerIconAdapter.setFiles(this.files, true);
        }
        else {
            this.filePickerIconAdapter.setFiles(this.files, false);
        }
        this.filePickerIconAdapter.notifyDataSetChanged();
    }
    
    private static Comparator<File> getDefaultFileComparator() {
        return new Comparator<File>() {
            @Override
            public int compare(final File file, final File file2) {
                int compareToIgnoreCase;
                if (file.isDirectory() && !file2.isDirectory()) {
                    compareToIgnoreCase = -1;
                }
                else if (!file.isDirectory() && file2.isDirectory()) {
                    compareToIgnoreCase = 1;
                }
                else {
                    compareToIgnoreCase = file.getName().compareToIgnoreCase(file2.getName());
                }
                return compareToIgnoreCase;
            }
        };
    }
    
    public static void setFileComparator(final Comparator<File> fileComparator) {
        FilePicker.fileComparator = fileComparator;
    }
    
    public static void setFileDisplayFilter(final FileFilter fileDisplayFilter) {
        FilePicker.fileDisplayFilter = fileDisplayFilter;
    }
    
    public static void setFileSelectFilter(final ValidFileFilter fileSelectFilter) {
        FilePicker.fileSelectFilter = fileSelectFilter;
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903040);
        this.filePickerIconAdapter = new FilePickerIconAdapter((Context)this);
        final GridView gridView = (GridView)this.findViewById(2131492879);
        gridView.setOnItemClickListener((AdapterView$OnItemClickListener)this);
        gridView.setAdapter((ListAdapter)this.filePickerIconAdapter);
        if (bundle == null) {
            this.showDialog(1);
        }
    }
    
    protected Dialog onCreateDialog(final int n) {
        Object o = null;
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        switch (n) {
            case 0: {
                alertDialog$Builder.setIcon(17301569);
                alertDialog$Builder.setTitle(2131165200);
                final StringBuilder sb = new StringBuilder();
                sb.append(this.getString(2131165415));
                sb.append("\n\n");
                sb.append(FilePicker.fileSelectFilter.getFileOpenResult().getErrorMessage());
                alertDialog$Builder.setMessage((CharSequence)sb.toString());
                alertDialog$Builder.setPositiveButton(2131165230, (DialogInterface$OnClickListener)null);
                o = alertDialog$Builder.create();
                break;
            }
            case 1: {
                alertDialog$Builder.setMessage(2131165416);
                alertDialog$Builder.setPositiveButton(2131165230, (DialogInterface$OnClickListener)null);
                o = alertDialog$Builder.create();
                break;
            }
        }
        return (Dialog)o;
    }
    
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        final File currentDirectory = this.files[(int)n2];
        if (currentDirectory.isDirectory()) {
            this.currentDirectory = currentDirectory;
            this.browseToCurrentDirectory();
        }
        else if (FilePicker.fileSelectFilter == null || FilePicker.fileSelectFilter.accept(currentDirectory)) {
            this.setResult(-1, new Intent().putExtra("selectedFile", currentDirectory.getAbsolutePath()));
            this.finish();
        }
        else {
            this.showDialog(0);
        }
    }
    
    protected void onPause() {
        super.onPause();
        final SharedPreferences$Editor edit = this.getSharedPreferences("FilePicker", 0).edit();
        edit.clear();
        if (this.currentDirectory != null) {
            edit.putString("currentDirectory", this.currentDirectory.getAbsolutePath());
        }
        edit.commit();
    }
    
    protected void onResume() {
        super.onResume();
        if (PreferenceManager.getDefaultSharedPreferences((Context)this).getBoolean("fullscreen", false)) {
            this.getWindow().addFlags(1024);
            this.getWindow().clearFlags(2048);
        }
        else {
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
