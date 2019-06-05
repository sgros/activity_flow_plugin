// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.computeTrack;

import android.os.IBinder;
import android.content.Intent;
import android.os.RemoteException;
import locus.api.objects.extra.Track;
import locus.api.utils.Logger;
import android.content.Context;
import locus.api.android.utils.LocusUtils;
import locus.api.android.objects.ParcelableContainer;
import android.app.Service;

public abstract class ComputeTrackService extends Service
{
    private static final String TAG;
    private final IComputeTrackService.Stub mBinder;
    
    static {
        TAG = ComputeTrackService.class.getSimpleName();
    }
    
    public ComputeTrackService() {
        this.mBinder = new IComputeTrackService.Stub() {
            public ParcelableContainer computeTrack(final ParcelableContainer obj) throws RemoteException {
                final ParcelableContainer parcelableContainer = null;
                ParcelableContainer parcelableContainer2;
                try {
                    final ComputeTrackParameters computeTrackParameters = new ComputeTrackParameters(obj.getData());
                    final LocusUtils.LocusVersion activeVersion = LocusUtils.getActiveVersion((Context)ComputeTrackService.this);
                    if (activeVersion == null) {
                        Logger.logW(ComputeTrackService.TAG, "Problem with finding running Locus instance");
                        parcelableContainer2 = parcelableContainer;
                    }
                    else {
                        final Track computeTrack = ComputeTrackService.this.computeTrack(activeVersion, computeTrackParameters);
                        parcelableContainer2 = parcelableContainer;
                        if (computeTrack != null) {
                            parcelableContainer2 = new ParcelableContainer(computeTrack.getAsBytes());
                        }
                    }
                    return parcelableContainer2;
                }
                catch (Exception ex) {
                    Logger.logE(ComputeTrackService.TAG, "computeTrack(" + obj + ")", ex);
                    parcelableContainer2 = parcelableContainer;
                    return parcelableContainer2;
                }
                return parcelableContainer2;
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
    }
    
    public abstract Track computeTrack(final LocusUtils.LocusVersion p0, final ComputeTrackParameters p1);
    
    public abstract String getAttribution();
    
    public abstract Intent getIntentForSettings();
    
    public int getNumOfTransitPoints() {
        return 0;
    }
    
    public abstract int[] getTrackTypes();
    
    public IBinder onBind(final Intent intent) {
        return (IBinder)this.mBinder;
    }
}
