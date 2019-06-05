package menion.android.whereyougo.gui.dialog;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.dialog.CustomDialogFragment;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.UtilsFormat;

public class ChooseSavegameDialog extends CustomDialogFragment {
    private static final String SAVE_FILE = "SAVE_FILE";
    private static final String TAG = "ChooseSavegameDialog";
    private BaseAdapter adapter;
    private ArrayList<DataInfo> data;
    private File saveFile;

    /* renamed from: menion.android.whereyougo.gui.dialog.ChooseSavegameDialog$1 */
    class C02891 implements OnItemClickListener {
        C02891() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ChooseSavegameDialog.this.itemClicked(position);
        }
    }

    /* renamed from: menion.android.whereyougo.gui.dialog.ChooseSavegameDialog$2 */
    class C02902 implements OnItemLongClickListener {
        C02902() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            ChooseSavegameDialog.this.itemLongClicked(position);
            return true;
        }
    }

    public static ChooseSavegameDialog newInstance(File saveFile) {
        ChooseSavegameDialog savegameDialog = new ChooseSavegameDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SAVE_FILE, saveFile.getAbsolutePath());
        savegameDialog.setArguments(bundle);
        return savegameDialog;
    }

    private boolean restoreInstance(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        String saveFileName = bundle.getString(SAVE_FILE);
        if (saveFileName == null) {
            return false;
        }
        this.saveFile = new File(saveFileName);
        return true;
    }

    public Dialog createDialog(Bundle savedInstanceState) {
        if (C0322A.getMain() == null) {
            return null;
        }
        if (!restoreInstance(savedInstanceState) && !restoreInstance(getArguments())) {
            return null;
        }
        this.data = new ArrayList();
        this.data.add(new DataInfo(getString(C0254R.string.save_file_new), ""));
        Object file = this.saveFile;
        if (file.exists()) {
            this.data.add(new DataInfo(getString(C0254R.string.save_file_main), UtilsFormat.formatDatetime(file.lastModified()), file));
        }
        file = new File(this.saveFile.getAbsolutePath() + ".bak");
        if (file.exists()) {
            this.data.add(new DataInfo(getString(C0254R.string.save_file_backup), UtilsFormat.formatDatetime(file.lastModified()), file));
        }
        for (int slot = 1; slot <= Preferences.GLOBAL_SAVEGAME_SLOTS; slot++) {
            file = new File(this.saveFile.getAbsolutePath() + "." + slot);
            if (file.exists()) {
                this.data.add(new DataInfo(String.format("%s %d", new Object[]{getString(C0254R.string.save_game_slot), Integer.valueOf(slot)}), UtilsFormat.formatDatetime(file.lastModified()), file));
            }
        }
        ListView listView = UtilsGUI.createListView(getActivity(), false, this.data);
        listView.setOnItemClickListener(new C02891());
        listView.setOnItemLongClickListener(new C02902());
        this.adapter = (BaseAdapter) listView.getAdapter();
        return new Builder(getActivity()).setTitle(C0254R.string.load_game).setIcon(C0254R.C0252drawable.ic_title_logo).setView(listView).setNeutralButton(C0254R.string.close, null).create();
    }

    private void itemClicked(int position) {
        if (position == 0) {
            MainActivity.wui.showScreen(11, null);
            dismiss();
            return;
        }
        try {
            FileSystem.copyFile((File) ((DataInfo) this.data.remove(position)).addData01, this.saveFile);
            MainActivity.startSelectedCartridge(true);
            dismiss();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void itemLongClicked(final int position) {
        if (position != 0) {
            UtilsGUI.showDialogQuestion(getActivity(), (int) C0254R.string.delete_save_game, new OnClickListener() {
                public void onClick(DialogInterface dialog, int btn) {
                    ((File) ((DataInfo) ChooseSavegameDialog.this.data.remove(position)).addData01).delete();
                    if (ChooseSavegameDialog.this.adapter != null) {
                        ChooseSavegameDialog.this.adapter.notifyDataSetChanged();
                    }
                }
            }, null);
        }
    }

    public void setParams(File saveFile) {
        this.saveFile = saveFile;
    }
}
