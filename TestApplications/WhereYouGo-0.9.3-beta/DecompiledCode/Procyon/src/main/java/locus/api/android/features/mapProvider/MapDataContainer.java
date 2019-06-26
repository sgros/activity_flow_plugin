// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.mapProvider;

import locus.api.objects.Storable;
import java.io.IOException;
import locus.api.utils.Logger;
import android.os.Parcel;
import locus.api.android.features.mapProvider.data.MapTileResponse;
import locus.api.android.features.mapProvider.data.MapTileRequest;
import locus.api.android.features.mapProvider.data.MapConfigLayer;
import java.util.List;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class MapDataContainer implements Parcelable
{
    public static final Parcelable$Creator<MapDataContainer> CREATOR;
    public static final int DATA_TYPE_CONFIGURATION = 1;
    public static final int DATA_TYPE_TILE_REQUEST = 2;
    public static final int DATA_TYPE_TILE_RESPONSE = 3;
    private static final int DATA_TYPE_UNDEFINED = 0;
    private static final String TAG;
    private int mDataType;
    private List<MapConfigLayer> mMapConfigs;
    private MapTileRequest mMapTileRequest;
    private MapTileResponse mMapTileResponse;
    
    static {
        TAG = MapDataContainer.class.getSimpleName();
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MapDataContainer>() {
            public MapDataContainer createFromParcel(final Parcel parcel) {
                return new MapDataContainer(parcel, null);
            }
            
            public MapDataContainer[] newArray(final int n) {
                return new MapDataContainer[n];
            }
        };
    }
    
    private MapDataContainer(final Parcel obj) {
        try {
            this.readFromParcel(obj);
        }
        catch (IOException ex) {
            this.mDataType = 0;
            Logger.logE(MapDataContainer.TAG, "DataTransporter(" + obj + ")", ex);
        }
    }
    
    public MapDataContainer(final List<MapConfigLayer> mMapConfigs) {
        this.mDataType = 1;
        this.mMapConfigs = mMapConfigs;
    }
    
    public MapDataContainer(final MapTileRequest mMapTileRequest) {
        this.mDataType = 2;
        this.mMapTileRequest = mMapTileRequest;
    }
    
    public MapDataContainer(final MapTileResponse mMapTileResponse) {
        this.mDataType = 3;
        this.mMapTileResponse = mMapTileResponse;
    }
    
    private void readFromParcel(final Parcel parcel) throws IOException {
        this.mDataType = parcel.readInt();
        if (this.mDataType == 1) {
            final byte[] array = new byte[parcel.readInt()];
            parcel.readByteArray(array);
            this.mMapConfigs = (List<MapConfigLayer>)Storable.readList(MapConfigLayer.class, array);
        }
        else if (this.mDataType == 2) {
            final byte[] array2 = new byte[parcel.readInt()];
            parcel.readByteArray(array2);
            this.mMapTileRequest = new MapTileRequest(array2);
        }
        else if (this.mDataType == 3) {
            final byte[] array3 = new byte[parcel.readInt()];
            parcel.readByteArray(array3);
            this.mMapTileResponse = new MapTileResponse(array3);
        }
    }
    
    private void writeObject(final Parcel parcel, final byte[] array) {
        parcel.writeInt(array.length);
        parcel.writeByteArray(array);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public List<MapConfigLayer> getMapConfigurations() {
        return this.mMapConfigs;
    }
    
    public MapTileRequest getTileRequest() {
        return this.mMapTileRequest;
    }
    
    public MapTileResponse getTileResponse() {
        return this.mMapTileResponse;
    }
    
    public boolean isValid(final int i) {
        boolean b = true;
        if (this.mDataType != i) {
            Logger.logW(MapDataContainer.TAG, "isValid(" + i + "), " + "invalid type:" + this.mDataType);
            b = false;
        }
        else if (this.mDataType == 1) {
            if (this.mMapConfigs == null || this.mMapConfigs.size() <= 0) {
                b = false;
            }
        }
        else if (this.mDataType == 2) {
            if (this.mMapTileRequest == null) {
                b = false;
            }
        }
        else if (this.mDataType == 3) {
            if (this.mMapTileResponse == null) {
                b = false;
            }
        }
        else {
            b = false;
        }
        return b;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mDataType);
        if (this.mDataType == 1) {
            this.writeObject(parcel, Storable.getAsBytes(this.mMapConfigs));
        }
        else if (this.mDataType == 2) {
            this.writeObject(parcel, this.mMapTileRequest.getAsBytes());
        }
        else if (this.mDataType == 3) {
            this.writeObject(parcel, this.mMapTileResponse.getAsBytes());
        }
    }
}
