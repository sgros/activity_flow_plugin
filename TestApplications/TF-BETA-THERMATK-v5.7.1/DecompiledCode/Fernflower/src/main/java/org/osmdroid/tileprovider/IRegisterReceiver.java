package org.osmdroid.tileprovider;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

public interface IRegisterReceiver {
   void destroy();

   Intent registerReceiver(BroadcastReceiver var1, IntentFilter var2);

   void unregisterReceiver(BroadcastReceiver var1);
}
