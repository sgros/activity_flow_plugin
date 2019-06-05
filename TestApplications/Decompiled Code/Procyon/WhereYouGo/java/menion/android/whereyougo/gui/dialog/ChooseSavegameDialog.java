// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.dialog;

import android.widget.ListView;
import android.app.AlertDialog$Builder;
import android.widget.AdapterView$OnItemLongClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.content.Context;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.UtilsFormat;
import menion.android.whereyougo.utils.A;
import android.app.Dialog;
import android.os.Bundle;
import android.app.Activity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import java.io.IOException;
import menion.android.whereyougo.utils.FileSystem;
import cz.matejcik.openwig.EventTable;
import menion.android.whereyougo.gui.activity.MainActivity;
import java.io.File;
import menion.android.whereyougo.gui.extension.DataInfo;
import java.util.ArrayList;
import android.widget.BaseAdapter;
import menion.android.whereyougo.gui.extension.dialog.CustomDialogFragment;

public class ChooseSavegameDialog extends CustomDialogFragment
{
    private static final String SAVE_FILE = "SAVE_FILE";
    private static final String TAG = "ChooseSavegameDialog";
    private BaseAdapter adapter;
    private ArrayList<DataInfo> data;
    private File saveFile;
    
    private void itemClicked(final int index) {
        if (index == 0) {
            MainActivity.wui.showScreen(11, null);
            this.dismiss();
        }
        else {
            try {
                FileSystem.copyFile((File)this.data.remove(index).addData01, this.saveFile);
                MainActivity.startSelectedCartridge(true);
                this.dismiss();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void itemLongClicked(final int n) {
        if (n != 0) {
            UtilsGUI.showDialogQuestion(this.getActivity(), 2131165331, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    ((File)ChooseSavegameDialog.this.data.remove(n).addData01).delete();
                    if (ChooseSavegameDialog.this.adapter != null) {
                        ChooseSavegameDialog.this.adapter.notifyDataSetChanged();
                    }
                }
            }, null);
        }
    }
    
    public static ChooseSavegameDialog newInstance(final File file) {
        final ChooseSavegameDialog chooseSavegameDialog = new ChooseSavegameDialog();
        final Bundle arguments = new Bundle();
        arguments.putString("SAVE_FILE", file.getAbsolutePath());
        chooseSavegameDialog.setArguments(arguments);
        return chooseSavegameDialog;
    }
    
    private boolean restoreInstance(final Bundle bundle) {
        boolean b = false;
        if (bundle != null) {
            final String string = bundle.getString("SAVE_FILE");
            if (string != null) {
                this.saveFile = new File(string);
                b = true;
            }
        }
        return b;
    }
    
    @Override
    public Dialog createDialog(final Bundle bundle) {
        Object create;
        if (A.getMain() == null) {
            create = null;
        }
        else if (!this.restoreInstance(bundle) && !this.restoreInstance(this.getArguments())) {
            create = null;
        }
        else {
            (this.data = new ArrayList<DataInfo>()).add(new DataInfo(this.getString(2131165398), ""));
            final File saveFile = this.saveFile;
            if (saveFile.exists()) {
                this.data.add(new DataInfo(this.getString(2131165397), UtilsFormat.formatDatetime(saveFile.lastModified()), saveFile));
            }
            final File file = new File(this.saveFile.getAbsolutePath() + ".bak");
            if (file.exists()) {
                this.data.add(new DataInfo(this.getString(2131165396), UtilsFormat.formatDatetime(file.lastModified()), file));
            }
            for (int i = 1; i <= Preferences.GLOBAL_SAVEGAME_SLOTS; ++i) {
                final File file2 = new File(this.saveFile.getAbsolutePath() + "." + i);
                if (file2.exists()) {
                    this.data.add(new DataInfo(String.format("%s %d", this.getString(2131165402), i), UtilsFormat.formatDatetime(file2.lastModified()), file2));
                }
            }
            final ListView listView = UtilsGUI.createListView((Context)this.getActivity(), false, this.data);
            listView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                    ChooseSavegameDialog.this.itemClicked(n);
                }
            });
            listView.setOnItemLongClickListener((AdapterView$OnItemLongClickListener)new AdapterView$OnItemLongClickListener() {
                public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                    ChooseSavegameDialog.this.itemLongClicked(n);
                    return true;
                }
            });
            this.adapter = (BaseAdapter)listView.getAdapter();
            create = new AlertDialog$Builder((Context)this.getActivity()).setTitle(2131165342).setIcon(2130837551).setView((View)listView).setNeutralButton(2131165192, (DialogInterface$OnClickListener)null).create();
        }
        return (Dialog)create;
    }
    
    public void setParams(final File saveFile) {
        this.saveFile = saveFile;
    }
}
