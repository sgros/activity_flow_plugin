// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.filepicker;

import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import java.io.File;
import android.content.Context;
import android.widget.BaseAdapter;

class FilePickerIconAdapter extends BaseAdapter
{
    private final Context context;
    private File currentFile;
    private File[] files;
    private boolean hasParentFolder;
    private TextView textView;
    
    FilePickerIconAdapter(final Context context) {
        this.context = context;
    }
    
    public int getCount() {
        int length;
        if (this.files == null) {
            length = 0;
        }
        else {
            length = this.files.length;
        }
        return length;
    }
    
    public Object getItem(final int n) {
        return this.files[n];
    }
    
    public long getItemId(final int n) {
        return n;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        if (view instanceof TextView) {
            this.textView = (TextView)view;
        }
        else {
            (this.textView = new TextView(this.context)).setLines(2);
            this.textView.setGravity(1);
            this.textView.setPadding(5, 10, 5, 10);
        }
        if (n == 0 && this.hasParentFolder) {
            this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 2130837516, 0, 0);
            this.textView.setText((CharSequence)"..");
        }
        else {
            this.currentFile = this.files[n];
            if (this.currentFile.isDirectory()) {
                this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 2130837518, 0, 0);
            }
            else {
                this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 2130837517, 0, 0);
            }
            this.textView.setText((CharSequence)this.currentFile.getName());
        }
        return (View)this.textView;
    }
    
    void setFiles(final File[] array, final boolean hasParentFolder) {
        this.files = array.clone();
        this.hasParentFolder = hasParentFolder;
    }
}
