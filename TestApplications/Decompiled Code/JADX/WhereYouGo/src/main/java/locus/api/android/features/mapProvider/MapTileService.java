package locus.api.android.features.mapProvider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;
import locus.api.android.features.mapProvider.IMapTileService.Stub;
import locus.api.android.features.mapProvider.data.MapConfigLayer;
import locus.api.android.features.mapProvider.data.MapTileRequest;
import locus.api.android.features.mapProvider.data.MapTileResponse;
import locus.api.utils.Logger;

public abstract class MapTileService extends Service {
    private static final String TAG = MapTileService.class.getSimpleName();
    private final Stub mBinder = new C02421();

    /* renamed from: locus.api.android.features.mapProvider.MapTileService$1 */
    class C02421 extends Stub {
        C02421() {
        }

        public MapDataContainer getMapConfigs() throws RemoteException {
            List configs = MapTileService.this.getMapConfigs();
            if (configs != null && configs.size() != 0) {
                return new MapDataContainer(configs);
            }
            Logger.logW(MapTileService.TAG, "getMapConfigs(), invalid configs");
            return new MapDataContainer(new ArrayList());
        }

        public MapDataContainer getMapTile(MapDataContainer request) throws RemoteException {
            MapTileResponse resp;
            if (request == null || !request.isValid(2)) {
                Logger.logW(MapTileService.TAG, "getMapTile(" + request + "), invalid request");
                resp = new MapTileResponse();
                resp.setResultCode(2);
                return new MapDataContainer(resp);
            }
            MapTileResponse response = MapTileService.this.getMapTile(request.getTileRequest());
            if (response != null) {
                return new MapDataContainer(response);
            }
            Logger.logW(MapTileService.TAG, "getMapTile(" + request + "), invalid response");
            resp = new MapTileResponse();
            resp.setResultCode(4);
            return new MapDataContainer(resp);
        }
    }

    public abstract List<MapConfigLayer> getMapConfigs();

    public abstract MapTileResponse getMapTile(MapTileRequest mapTileRequest);

    public void onCreate() {
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}
