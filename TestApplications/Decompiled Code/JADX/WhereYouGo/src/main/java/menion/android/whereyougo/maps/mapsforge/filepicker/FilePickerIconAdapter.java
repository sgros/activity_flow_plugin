package menion.android.whereyougo.maps.mapsforge.filepicker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.io.File;
import menion.android.whereyougo.C0254R;

class FilePickerIconAdapter extends BaseAdapter {
    private final Context context;
    private File currentFile;
    private File[] files;
    private boolean hasParentFolder;
    private TextView textView;

    FilePickerIconAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        if (this.files == null) {
            return 0;
        }
        return this.files.length;
    }

    public Object getItem(int index) {
        return this.files[index];
    }

    public long getItemId(int index) {
        return (long) index;
    }

    public View getView(int index, View convertView, ViewGroup parent) {
        if (convertView instanceof TextView) {
            this.textView = (TextView) convertView;
        } else {
            this.textView = new TextView(this.context);
            this.textView.setLines(2);
            this.textView.setGravity(1);
            this.textView.setPadding(5, 10, 5, 10);
        }
        if (index == 0 && this.hasParentFolder) {
            this.textView.setCompoundDrawablesWithIntrinsicBounds(0, C0254R.C0252drawable.file_picker_back, 0, 0);
            this.textView.setText("..");
        } else {
            this.currentFile = this.files[index];
            if (this.currentFile.isDirectory()) {
                this.textView.setCompoundDrawablesWithIntrinsicBounds(0, C0254R.C0252drawable.file_picker_folder, 0, 0);
            } else {
                this.textView.setCompoundDrawablesWithIntrinsicBounds(0, C0254R.C0252drawable.file_picker_file, 0, 0);
            }
            this.textView.setText(this.currentFile.getName());
        }
        return this.textView;
    }

    /* Access modifiers changed, original: 0000 */
    public void setFiles(File[] files, boolean newHasParentFolder) {
        this.files = (File[]) files.clone();
        this.hasParentFolder = newHasParentFolder;
    }
}
