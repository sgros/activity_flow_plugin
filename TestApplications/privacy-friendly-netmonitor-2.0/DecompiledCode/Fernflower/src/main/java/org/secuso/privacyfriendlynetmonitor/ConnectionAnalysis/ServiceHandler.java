package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;
import java.util.Iterator;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;

public class ServiceHandler {
   private boolean mIsBound;
   private PassiveService mPassiveService;
   private ServiceConnection mPassiveServiceConnection = new ServiceConnection() {
      public void onServiceConnected(ComponentName var1, IBinder var2) {
         ServiceHandler.this.mPassiveService = ((PassiveService.AnalyzerBinder)var2).getService();
         Toast.makeText(RunStore.getContext(), 2131624036, 0).show();
      }

      public void onServiceDisconnected(ComponentName var1) {
         ServiceHandler.this.mPassiveService = null;
         Toast.makeText(RunStore.getContext(), 2131624037, 0).show();
      }
   };

   public void bindPassiveService(Context var1) {
      var1.bindService(new Intent(RunStore.getAppContext(), PassiveService.class), this.mPassiveServiceConnection, 1);
      this.mIsBound = true;
   }

   public boolean isServiceRunning(Class var1) {
      Iterator var2 = ((ActivityManager)RunStore.getAppContext().getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();

      RunningServiceInfo var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (RunningServiceInfo)var2.next();
      } while(!var1.getName().equals(var3.service.getClassName()));

      return true;
   }

   public void startPassiveService() {
      Intent var1 = new Intent(RunStore.getAppContext(), PassiveService.class);
      RunStore.getContext().startService(var1);
   }

   public void stopPassiveService() {
      if (this.isServiceRunning(PassiveService.class)) {
         RunStore.getContext().stopService(new Intent(RunStore.getAppContext(), PassiveService.class));
      }

   }

   public void unbindPassiveService(Context var1) {
      if (this.mIsBound) {
         var1.unbindService(this.mPassiveServiceConnection);
         this.mIsBound = false;
      }

   }
}
