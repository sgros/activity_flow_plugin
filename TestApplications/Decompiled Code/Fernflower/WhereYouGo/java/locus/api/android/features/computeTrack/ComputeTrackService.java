package locus.api.android.features.computeTrack;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import locus.api.android.objects.ParcelableContainer;
import locus.api.android.utils.LocusUtils;
import locus.api.objects.extra.Track;
import locus.api.utils.Logger;

public abstract class ComputeTrackService extends Service {
   private static final String TAG = ComputeTrackService.class.getSimpleName();
   private final IComputeTrackService.Stub mBinder = new IComputeTrackService.Stub() {
      public ParcelableContainer computeTrack(ParcelableContainer var1) throws RemoteException {
         Object var2 = null;

         ParcelableContainer var11;
         Exception var10000;
         label38: {
            ComputeTrackParameters var4;
            LocusUtils.LocusVersion var9;
            boolean var10001;
            try {
               byte[] var3 = var1.getData();
               var4 = new ComputeTrackParameters(var3);
               var9 = LocusUtils.getActiveVersion(ComputeTrackService.this);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label38;
            }

            if (var9 == null) {
               label53: {
                  try {
                     Logger.logW(ComputeTrackService.TAG, "Problem with finding running Locus instance");
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label53;
                  }

                  var11 = (ParcelableContainer)var2;
                  return var11;
               }
            } else {
               label43: {
                  Track var10;
                  try {
                     var10 = ComputeTrackService.this.computeTrack(var9, var4);
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label43;
                  }

                  var11 = (ParcelableContainer)var2;
                  if (var10 == null) {
                     return var11;
                  }

                  try {
                     var11 = new ParcelableContainer(var10.getAsBytes());
                     return var11;
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                  }
               }
            }
         }

         Exception var12 = var10000;
         Logger.logE(ComputeTrackService.TAG, "computeTrack(" + var1 + ")", var12);
         var11 = (ParcelableContainer)var2;
         return var11;
      }

      public String getAttribution() {
         return ComputeTrackService.this.getAttribution();
      }

      public Intent getIntentForSettings() {
         return ComputeTrackService.this.getIntentForSettings();
      }

      public int getNumOfTransitPoints() {
         return ComputeTrackService.this.getNumOfTransitPoints();
      }

      public int[] getTrackTypes() throws RemoteException {
         return ComputeTrackService.this.getTrackTypes();
      }
   };

   public abstract Track computeTrack(LocusUtils.LocusVersion var1, ComputeTrackParameters var2);

   public abstract String getAttribution();

   public abstract Intent getIntentForSettings();

   public int getNumOfTransitPoints() {
      return 0;
   }

   public abstract int[] getTrackTypes();

   public IBinder onBind(Intent var1) {
      return this.mBinder;
   }
}
