package locus.api.android;

import android.content.Context;
import android.content.Intent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import locus.api.android.ActionDisplay.ExtraAction;
import locus.api.android.objects.PackWaypoints;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.Storable;
import locus.api.utils.Logger;
import locus.api.utils.Utils;

public class ActionDisplayPoints extends ActionDisplay {
    private static final String TAG = "ActionDisplayPoints";

    public static boolean sendPack(Context context, PackWaypoints data, ExtraAction extraAction) throws RequiredVersionMissingException {
        boolean z = true;
        String str = LocusConst.ACTION_DISPLAY_DATA;
        boolean z2 = extraAction == ExtraAction.IMPORT;
        if (extraAction != ExtraAction.CENTER) {
            z = false;
        }
        return sendPack(str, context, data, z2, z);
    }

    public static boolean sendPackSilent(Context context, PackWaypoints data, boolean centerOnData) throws RequiredVersionMissingException {
        return sendPack(LocusConst.ACTION_DISPLAY_DATA_SILENTLY, context, data, false, centerOnData);
    }

    private static boolean sendPack(String action, Context context, PackWaypoints data, boolean callImport, boolean centerOnData) throws RequiredVersionMissingException {
        if (data == null) {
            return false;
        }
        Intent intent = new Intent();
        intent.putExtra(LocusConst.INTENT_EXTRA_POINTS_DATA, data.getAsBytes());
        return ActionDisplay.sendData(action, context, intent, callImport, centerOnData);
    }

    public static boolean sendPacks(Context context, List<PackWaypoints> data, ExtraAction extraAction) throws RequiredVersionMissingException {
        boolean z = true;
        String str = LocusConst.ACTION_DISPLAY_DATA;
        boolean z2 = extraAction == ExtraAction.IMPORT;
        if (extraAction != ExtraAction.CENTER) {
            z = false;
        }
        return sendPacks(str, context, data, z2, z);
    }

    public static boolean sendPacksSilent(Context context, List<PackWaypoints> data, boolean centerOnData) throws RequiredVersionMissingException {
        return sendPacks(LocusConst.ACTION_DISPLAY_DATA_SILENTLY, context, data, false, centerOnData);
    }

    private static boolean sendPacks(String action, Context context, List<PackWaypoints> data, boolean callImport, boolean centerOnData) throws RequiredVersionMissingException {
        if (data == null) {
            return false;
        }
        Intent intent = new Intent();
        intent.putExtra(LocusConst.INTENT_EXTRA_POINTS_DATA_ARRAY, Storable.getAsBytes(data));
        return ActionDisplay.sendData(action, context, intent, callImport, centerOnData);
    }

    public static boolean sendPacksFile(Context context, ArrayList<PackWaypoints> data, String filepath, ExtraAction extraAction) throws RequiredVersionMissingException {
        return sendPacksFile(LocusConst.ACTION_DISPLAY_DATA, context, data, filepath, extraAction == ExtraAction.IMPORT, extraAction == ExtraAction.CENTER);
    }

    public static boolean sendPacksFileSilent(Context context, ArrayList<PackWaypoints> data, String filepath, boolean centerOnData) throws RequiredVersionMissingException {
        return sendPacksFile(LocusConst.ACTION_DISPLAY_DATA_SILENTLY, context, data, filepath, false, centerOnData);
    }

    private static boolean sendPacksFile(String action, Context context, List<PackWaypoints> data, String filepath, boolean callImport, boolean centerOnData) throws RequiredVersionMissingException {
        if (!sendDataWriteOnCard(data, filepath)) {
            return false;
        }
        Intent intent = new Intent();
        intent.putExtra(LocusConst.INTENT_EXTRA_POINTS_FILE_PATH, filepath);
        return ActionDisplay.sendData(action, context, intent, callImport, centerOnData);
    }

    private static boolean sendDataWriteOnCard(List<PackWaypoints> data, String filepath) {
        Exception e;
        Throwable th;
        if (data == null || data.size() == 0) {
            return false;
        }
        DataOutputStream dos = null;
        try {
            File file = new File(filepath);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                file.delete();
            }
            DataOutputStream dos2 = new DataOutputStream(new FileOutputStream(file, false));
            try {
                Storable.writeList(data, dos2);
                dos2.flush();
                Utils.closeStream(dos2);
                return true;
            } catch (Exception e2) {
                e = e2;
                dos = dos2;
                try {
                    Logger.logE(TAG, "sendDataWriteOnCard(" + filepath + ", " + data + ")", e);
                    Utils.closeStream(dos);
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    Utils.closeStream(dos);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                dos = dos2;
                Utils.closeStream(dos);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            Logger.logE(TAG, "sendDataWriteOnCard(" + filepath + ", " + data + ")", e);
            Utils.closeStream(dos);
            return false;
        }
    }

    public static List<PackWaypoints> readDataWriteOnCard(String filepath) {
        Exception e;
        Throwable th;
        File file = new File(filepath);
        if (!file.exists()) {
            return new ArrayList();
        }
        DataInputStream dis = null;
        try {
            DataInputStream dis2 = new DataInputStream(new FileInputStream(file));
            try {
                List<PackWaypoints> readList = Storable.readList(PackWaypoints.class, dis2);
                Utils.closeStream(dis2);
                return readList;
            } catch (Exception e2) {
                e = e2;
                dis = dis2;
                try {
                    Logger.logE(TAG, "getDataFile(" + filepath + ")", e);
                    Utils.closeStream(dis);
                    return new ArrayList();
                } catch (Throwable th2) {
                    th = th2;
                    Utils.closeStream(dis);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                dis = dis2;
                Utils.closeStream(dis);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            Logger.logE(TAG, "getDataFile(" + filepath + ")", e);
            Utils.closeStream(dis);
            return new ArrayList();
        }
    }

    public void removePackFromLocus(Context ctx, String packName) throws RequiredVersionMissingException {
        if (packName != null && packName.length() != 0) {
            PackWaypoints pw = new PackWaypoints(packName);
            new Intent().putExtra(LocusConst.INTENT_EXTRA_POINTS_DATA, pw.getAsBytes());
            sendPackSilent(ctx, pw, false);
        }
    }
}
