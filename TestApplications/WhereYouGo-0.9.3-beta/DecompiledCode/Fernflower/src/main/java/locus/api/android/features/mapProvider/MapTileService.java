package locus.api.android.features.mapProvider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;
import locus.api.android.features.mapProvider.data.MapTileRequest;
import locus.api.android.features.mapProvider.data.MapTileResponse;
import locus.api.utils.Logger;

public abstract class MapTileService extends Service {
   private static final String TAG = MapTileService.class.getSimpleName();
   private final IMapTileService.Stub mBinder = new IMapTileService.Stub() {
      public MapDataContainer getMapConfigs() throws RemoteException {
         List var1 = MapTileService.this.getMapConfigs();
         MapDataContainer var2;
         if (var1 != null && var1.size() != 0) {
            var2 = new MapDataContainer(var1);
         } else {
            Logger.logW(MapTileService.TAG, "getMapConfigs(), invalid configs");
            var2 = new MapDataContainer(new ArrayList());
         }

         return var2;
      }

      public MapDataContainer getMapTile(MapDataContainer var1) throws RemoteException {
         MapTileResponse var3;
         if (var1 != null && var1.isValid(2)) {
            MapTileResponse var2 = MapTileService.this.getMapTile(var1.getTileRequest());
            if (var2 == null) {
               Logger.logW(MapTileService.TAG, "getMapTile(" + var1 + "), invalid response");
               var3 = new MapTileResponse();
               var3.setResultCode(4);
               var1 = new MapDataContainer(var3);
            } else {
               var1 = new MapDataContainer(var2);
            }
         } else {
            Logger.logW(MapTileService.TAG, "getMapTile(" + var1 + "), invalid request");
            var3 = new MapTileResponse();
            var3.setResultCode(2);
            var1 = new MapDataContainer(var3);
         }

         return var1;
      }
   };

   public abstract List getMapConfigs();

   public abstract MapTileResponse getMapTile(MapTileRequest var1);

   public IBinder onBind(Intent var1) {
      return this.mBinder;
   }

   public void onCreate() {
      super.onCreate();
   }
}
