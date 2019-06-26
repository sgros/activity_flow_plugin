package p005cz.matejcik.openwig.platform;

import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Media;
import p009se.krka.kahlua.p010vm.LuaClosure;

/* renamed from: cz.matejcik.openwig.platform.UI */
public interface C0237UI {
    public static final int DETAILSCREEN = 1;
    public static final int INVENTORYSCREEN = 2;
    public static final int ITEMSCREEN = 3;
    public static final int LOCATIONSCREEN = 4;
    public static final int MAINSCREEN = 0;
    public static final int TASKSCREEN = 5;

    void blockForSaving();

    void command(String str);

    void debugMsg(String str);

    void end();

    void playSound(byte[] bArr, String str);

    void pushDialog(String[] strArr, Media[] mediaArr, String str, String str2, LuaClosure luaClosure);

    void pushInput(EventTable eventTable);

    void refresh();

    void setStatusText(String str);

    void showError(String str);

    void showScreen(int i, EventTable eventTable);

    void start();

    void unblock();
}
