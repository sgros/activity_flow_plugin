package locus.api.android.features.computeTrack;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import locus.api.android.features.computeTrack.IComputeTrackService.Stub;
import locus.api.android.objects.ParcelableContainer;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.LocusUtils.LocusVersion;
import locus.api.objects.extra.Track;
import locus.api.utils.Logger;

public abstract class ComputeTrackService extends Service {
    private static final String TAG = ComputeTrackService.class.getSimpleName();
    private final Stub mBinder = new C02401();

    /* renamed from: locus.api.android.features.computeTrack.ComputeTrackService$1 */
    class C02401 extends Stub {
        C02401() {
        }

        public String getAttribution() {
            return ComputeTrackService.this.getAttribution();
        }

        public int[] getTrackTypes() throws RemoteException {
            return ComputeTrackService.this.getTrackTypes();
        }

        public Intent getIntentForSettings() {
            return ComputeTrackService.this.getIntentForSettings();
        }

        public int getNumOfTransitPoints() {
            return ComputeTrackService.this.getNumOfTransitPoints();
        }

        public ParcelableContainer computeTrack(ParcelableContainer trackParams) throws RemoteException {
            try {
                ComputeTrackParameters params = new ComputeTrackParameters(trackParams.getData());
                LocusVersion lv = LocusUtils.getActiveVersion(ComputeTrackService.this);
                if (lv == null) {
                    Logger.logW(ComputeTrackService.TAG, "Problem with finding running Locus instance");
                    return null;
                }
                Track track = ComputeTrackService.this.computeTrack(lv, params);
                if (track != null) {
                    return new ParcelableContainer(track.getAsBytes());
                }
                return null;
            } catch (Exception e) {
                Logger.logE(ComputeTrackService.TAG, "computeTrack(" + trackParams + ")", e);
                return null;
            }
        }
    }

    public abstract Track computeTrack(LocusVersion locusVersion, ComputeTrackParameters computeTrackParameters);

    public abstract String getAttribution();

    public abstract Intent getIntentForSettings();

    public abstract int[] getTrackTypes();

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public int getNumOfTransitPoints() {
        return 0;
    }
}
