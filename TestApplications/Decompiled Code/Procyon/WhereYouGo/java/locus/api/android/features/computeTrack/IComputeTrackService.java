// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.computeTrack;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.content.Intent;
import android.os.RemoteException;
import locus.api.android.objects.ParcelableContainer;
import android.os.IInterface;

public interface IComputeTrackService extends IInterface
{
    ParcelableContainer computeTrack(final ParcelableContainer p0) throws RemoteException;
    
    String getAttribution() throws RemoteException;
    
    Intent getIntentForSettings() throws RemoteException;
    
    int getNumOfTransitPoints() throws RemoteException;
    
    int[] getTrackTypes() throws RemoteException;
    
    public abstract static class Stub extends Binder implements IComputeTrackService
    {
        private static final String DESCRIPTOR = "locus.api.android.features.computeTrack.IComputeTrackService";
        static final int TRANSACTION_computeTrack = 4;
        static final int TRANSACTION_getAttribution = 1;
        static final int TRANSACTION_getIntentForSettings = 3;
        static final int TRANSACTION_getNumOfTransitPoints = 5;
        static final int TRANSACTION_getTrackTypes = 2;
        
        public Stub() {
            this.attachInterface((IInterface)this, "locus.api.android.features.computeTrack.IComputeTrackService");
        }
        
        public static IComputeTrackService asInterface(final IBinder binder) {
            IComputeTrackService computeTrackService;
            if (binder == null) {
                computeTrackService = null;
            }
            else {
                final IInterface queryLocalInterface = binder.queryLocalInterface("locus.api.android.features.computeTrack.IComputeTrackService");
                if (queryLocalInterface != null && queryLocalInterface instanceof IComputeTrackService) {
                    computeTrackService = (IComputeTrackService)queryLocalInterface;
                }
                else {
                    computeTrackService = new Proxy(binder);
                }
            }
            return computeTrackService;
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(int numOfTransitPoints, final Parcel parcel, final Parcel parcel2, final int n) throws RemoteException {
            boolean onTransact = true;
            switch (numOfTransitPoints) {
                default: {
                    onTransact = super.onTransact(numOfTransitPoints, parcel, parcel2, n);
                    break;
                }
                case 1598968902: {
                    parcel2.writeString("locus.api.android.features.computeTrack.IComputeTrackService");
                    break;
                }
                case 1: {
                    parcel.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
                    final String attribution = this.getAttribution();
                    parcel2.writeNoException();
                    parcel2.writeString(attribution);
                    break;
                }
                case 2: {
                    parcel.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
                    final int[] trackTypes = this.getTrackTypes();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(trackTypes);
                    break;
                }
                case 3: {
                    parcel.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
                    final Intent intentForSettings = this.getIntentForSettings();
                    parcel2.writeNoException();
                    if (intentForSettings != null) {
                        parcel2.writeInt(1);
                        intentForSettings.writeToParcel(parcel2, 1);
                        break;
                    }
                    parcel2.writeInt(0);
                    break;
                }
                case 4: {
                    parcel.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
                    ParcelableContainer parcelableContainer;
                    if (parcel.readInt() != 0) {
                        parcelableContainer = (ParcelableContainer)ParcelableContainer.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        parcelableContainer = null;
                    }
                    final ParcelableContainer computeTrack = this.computeTrack(parcelableContainer);
                    parcel2.writeNoException();
                    if (computeTrack != null) {
                        parcel2.writeInt(1);
                        computeTrack.writeToParcel(parcel2, 1);
                        break;
                    }
                    parcel2.writeInt(0);
                    break;
                }
                case 5: {
                    parcel.enforceInterface("locus.api.android.features.computeTrack.IComputeTrackService");
                    numOfTransitPoints = this.getNumOfTransitPoints();
                    parcel2.writeNoException();
                    parcel2.writeInt(numOfTransitPoints);
                    break;
                }
            }
            return onTransact;
        }
        
        private static class Proxy implements IComputeTrackService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public ParcelableContainer computeTrack(ParcelableContainer parcelableContainer) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
                        if (parcelableContainer != null) {
                            obtain.writeInt(1);
                            parcelableContainer.writeToParcel(obtain, 0);
                        }
                        else {
                            obtain.writeInt(0);
                        }
                        this.mRemote.transact(4, obtain, obtain2, 0);
                        obtain2.readException();
                        if (obtain2.readInt() != 0) {
                            parcelableContainer = (ParcelableContainer)ParcelableContainer.CREATOR.createFromParcel(obtain2);
                            return parcelableContainer;
                        }
                    }
                    finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                    parcelableContainer = null;
                    return parcelableContainer;
                }
            }
            
            @Override
            public String getAttribution() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public Intent getIntentForSettings() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    Intent intent;
                    if (obtain2.readInt() != 0) {
                        intent = (Intent)Intent.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        intent = null;
                    }
                    return intent;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public String getInterfaceDescriptor() {
                return "locus.api.android.features.computeTrack.IComputeTrackService";
            }
            
            @Override
            public int getNumOfTransitPoints() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public int[] getTrackTypes() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("locus.api.android.features.computeTrack.IComputeTrackService");
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createIntArray();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
