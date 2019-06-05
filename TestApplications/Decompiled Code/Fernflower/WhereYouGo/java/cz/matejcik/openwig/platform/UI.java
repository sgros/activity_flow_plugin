package cz.matejcik.openwig.platform;

import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import se.krka.kahlua.vm.LuaClosure;

public interface UI {
   int DETAILSCREEN = 1;
   int INVENTORYSCREEN = 2;
   int ITEMSCREEN = 3;
   int LOCATIONSCREEN = 4;
   int MAINSCREEN = 0;
   int TASKSCREEN = 5;

   void blockForSaving();

   void command(String var1);

   void debugMsg(String var1);

   void end();

   void playSound(byte[] var1, String var2);

   void pushDialog(String[] var1, Media[] var2, String var3, String var4, LuaClosure var5);

   void pushInput(EventTable var1);

   void refresh();

   void setStatusText(String var1);

   void showError(String var1);

   void showScreen(int var1, EventTable var2);

   void start();

   void unblock();
}
