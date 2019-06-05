package menion.android.whereyougo.maps.mapsforge.filepicker;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.maps.mapsforge.filefilter.ValidFileFilter;

public class FilePicker extends Activity implements OnItemClickListener {
    private static final String CURRENT_DIRECTORY = "currentDirectory";
    private static final String DEFAULT_DIRECTORY = "/";
    private static final int DIALOG_FILE_INVALID = 0;
    private static final int DIALOG_FILE_SELECT = 1;
    private static final String PREFERENCES_FILE = "FilePicker";
    public static final String SELECTED_FILE = "selectedFile";
    private static Comparator<File> fileComparator = getDefaultFileComparator();
    private static FileFilter fileDisplayFilter;
    private static ValidFileFilter fileSelectFilter;
    private File currentDirectory;
    private FilePickerIconAdapter filePickerIconAdapter;
    private File[] files;
    private File[] filesWithParentFolder;

    /* renamed from: menion.android.whereyougo.maps.mapsforge.filepicker.FilePicker$1 */
    static class C03171 implements Comparator<File> {
        C03171() {
        }

        public int compare(File file1, File file2) {
            if (file1.isDirectory() && !file2.isDirectory()) {
                return -1;
            }
            if (file1.isDirectory() || !file2.isDirectory()) {
                return file1.getName().compareToIgnoreCase(file2.getName());
            }
            return 1;
        }
    }

    private static Comparator<File> getDefaultFileComparator() {
        return new C03171();
    }

    public static void setFileComparator(Comparator<File> fileComparator) {
        fileComparator = fileComparator;
    }

    public static void setFileDisplayFilter(FileFilter fileDisplayFilter) {
        fileDisplayFilter = fileDisplayFilter;
    }

    public static void setFileSelectFilter(ValidFileFilter fileSelectFilter) {
        fileSelectFilter = fileSelectFilter;
    }

    private void browseToCurrentDirectory() {
        setTitle(this.currentDirectory.getAbsolutePath());
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
            this.filesWithParentFolder = new File[(this.files.length + 1)];
            this.filesWithParentFolder[0] = this.currentDirectory.getParentFile();
            System.arraycopy(this.files, 0, this.filesWithParentFolder, 1, this.files.length);
            this.files = this.filesWithParentFolder;
            this.filePickerIconAdapter.setFiles(this.files, true);
        } else {
            this.filePickerIconAdapter.setFiles(this.files, false);
        }
        this.filePickerIconAdapter.notifyDataSetChanged();
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0254R.layout.activity_file_picker);
        this.filePickerIconAdapter = new FilePickerIconAdapter(this);
        GridView gridView = (GridView) findViewById(C0254R.C0253id.filePickerView);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(this.filePickerIconAdapter);
        if (savedInstanceState == null) {
            showDialog(1);
        }
    }

    /* Access modifiers changed, original: protected */
    public Dialog onCreateDialog(int id) {
        Builder builder = new Builder(this);
        switch (id) {
            case 0:
                builder.setIcon(17301569);
                builder.setTitle(C0254R.string.error);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(getString(C0254R.string.file_invalid));
                stringBuilder.append("\n\n");
                stringBuilder.append(fileSelectFilter.getFileOpenResult().getErrorMessage());
                builder.setMessage(stringBuilder.toString());
                builder.setPositiveButton(C0254R.string.f48ok, null);
                return builder.create();
            case 1:
                builder.setMessage(C0254R.string.file_select);
                builder.setPositiveButton(C0254R.string.f48ok, null);
                return builder.create();
            default:
                return null;
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        File selectedFile = this.files[(int) id];
        if (selectedFile.isDirectory()) {
            this.currentDirectory = selectedFile;
            browseToCurrentDirectory();
        } else if (fileSelectFilter == null || fileSelectFilter.accept(selectedFile)) {
            setResult(-1, new Intent().putExtra(SELECTED_FILE, selectedFile.getAbsolutePath()));
            finish();
        } else {
            showDialog(0);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        Editor editor = getSharedPreferences(PREFERENCES_FILE, 0).edit();
        editor.clear();
        if (this.currentDirectory != null) {
            editor.putString(CURRENT_DIRECTORY, this.currentDirectory.getAbsolutePath());
        }
        editor.commit();
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fullscreen", false)) {
            getWindow().addFlags(1024);
            getWindow().clearFlags(2048);
        } else {
            getWindow().clearFlags(1024);
            getWindow().addFlags(2048);
        }
        this.currentDirectory = new File(getSharedPreferences(PREFERENCES_FILE, 0).getString(CURRENT_DIRECTORY, DEFAULT_DIRECTORY));
        if (!(this.currentDirectory.exists() && this.currentDirectory.canRead())) {
            this.currentDirectory = new File(DEFAULT_DIRECTORY);
        }
        if (!(this.currentDirectory.exists() && this.currentDirectory.canRead())) {
            this.currentDirectory = Environment.getExternalStorageDirectory();
        }
        browseToCurrentDirectory();
    }
}
