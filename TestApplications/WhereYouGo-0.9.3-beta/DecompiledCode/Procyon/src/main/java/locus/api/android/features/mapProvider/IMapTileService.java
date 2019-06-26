// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.mapProvider;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface IMapTileService extends IInterface
{
    MapDataContainer getMapConfigs() throws RemoteException;
    
    MapDataContainer getMapTile(final MapDataContainer p0) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IMapTileService
    {
        private static final String DESCRIPTOR = "locus.api.android.features.mapProvider.IMapTileService";
        static final int TRANSACTION_getMapConfigs = 1;
        static final int TRANSACTION_getMapTile = 2;
        
        public Stub() {
            this.attachInterface((IInterface)this, "locus.api.android.features.mapProvider.IMapTileService");
        }
        
        public static IMapTileService asInterface(final IBinder binder) {
            IMapTileService mapTileService;
            if (binder == null) {
                mapTileService = null;
            }
            else {
                final IInterface queryLocalInterface = binder.queryLocalInterface("locus.api.android.features.mapProvider.IMapTileService");
                if (queryLocalInterface != null && queryLocalInterface instanceof IMapTileService) {
                    mapTileService = (IMapTileService)queryLocalInterface;
                }
                else {
                    mapTileService = new Proxy(binder);
                }
            }
            return mapTileService;
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            boolean onTransact = true;
            switch (n) {
                default: {
                    onTransact = super.onTransact(n, parcel, parcel2, n2);
                    break;
                }
                case 1598968902: {
                    parcel2.writeString("locus.api.android.features.mapProvider.IMapTileService");
                    break;
                }
                case 1: {
                    parcel.enforceInterface("locus.api.android.features.mapProvider.IMapTileService");
                    final MapDataContainer mapConfigs = this.getMapConfigs();
                    parcel2.writeNoException();
                    if (mapConfigs != null) {
                        parcel2.writeInt(1);
                        mapConfigs.writeToParcel(parcel2, 1);
                        break;
                    }
                    parcel2.writeInt(0);
                    break;
                }
                case 2: {
                    parcel.enforceInterface("locus.api.android.features.mapProvider.IMapTileService");
                    MapDataContainer mapDataContainer;
                    if (parcel.readInt() != 0) {
                        mapDataContainer = (MapDataContainer)MapDataContainer.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        mapDataContainer = null;
                    }
                    final MapDataContainer mapTile = this.getMapTile(mapDataContainer);
                    parcel2.writeNoException();
                    if (mapTile != null) {
                        parcel2.writeInt(1);
                        mapTile.writeToParcel(parcel2, 1);
                        break;
                    }
                    parcel2.writeInt(0);
                    break;
                }
            }
            return onTransact;
        }
        
        private static class Proxy implements IMapTileService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "locus.api.android.features.mapProvider.IMapTileService";
            }
            
            @Override
            public MapDataContainer getMapConfigs() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("locus.api.android.features.mapProvider.IMapTileService");
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    MapDataContainer mapDataContainer;
                    if (obtain2.readInt() != 0) {
                        mapDataContainer = (MapDataContainer)MapDataContainer.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        mapDataContainer = null;
                    }
                    return mapDataContainer;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public MapDataContainer getMapTile(MapDataContainer mapDataContainer) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken("locus.api.android.features.mapProvider.IMapTileService");
                        if (mapDataContainer != null) {
                            obtain.writeInt(1);
                            mapDataContainer.writeToParcel(obtain, 0);
                        }
                        else {
                            obtain.writeInt(0);
                        }
                        this.mRemote.transact(2, obtain, obtain2, 0);
                        obtain2.readException();
                        if (obtain2.readInt() != 0) {
                            mapDataContainer = (MapDataContainer)MapDataContainer.CREATOR.createFromParcel(obtain2);
                            return mapDataContainer;
                        }
                    }
                    finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                    mapDataContainer = null;
                    return mapDataContainer;
                }
            }
        }
    }
}
