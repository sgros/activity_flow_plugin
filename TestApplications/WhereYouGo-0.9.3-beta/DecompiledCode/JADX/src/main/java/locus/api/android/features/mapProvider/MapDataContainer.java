package locus.api.android.features.mapProvider;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.IOException;
import java.util.List;
import locus.api.android.features.mapProvider.data.MapConfigLayer;
import locus.api.android.features.mapProvider.data.MapTileRequest;
import locus.api.android.features.mapProvider.data.MapTileResponse;
import locus.api.objects.Storable;
import locus.api.utils.Logger;

public class MapDataContainer implements Parcelable {
    public static final Creator<MapDataContainer> CREATOR = new C02411();
    public static final int DATA_TYPE_CONFIGURATION = 1;
    public static final int DATA_TYPE_TILE_REQUEST = 2;
    public static final int DATA_TYPE_TILE_RESPONSE = 3;
    private static final int DATA_TYPE_UNDEFINED = 0;
    private static final String TAG = MapDataContainer.class.getSimpleName();
    private int mDataType;
    private List<MapConfigLayer> mMapConfigs;
    private MapTileRequest mMapTileRequest;
    private MapTileResponse mMapTileResponse;

    /* renamed from: locus.api.android.features.mapProvider.MapDataContainer$1 */
    static class C02411 implements Creator<MapDataContainer> {
        C02411() {
        }

        public MapDataContainer createFromParcel(Parcel in) {
            return new MapDataContainer(in, null);
        }

        public MapDataContainer[] newArray(int size) {
            return new MapDataContainer[size];
        }
    }

    /* synthetic */ MapDataContainer(Parcel x0, C02411 x1) {
        this(x0);
    }

    public MapDataContainer(List<MapConfigLayer> mapConfigs) {
        this.mDataType = 1;
        this.mMapConfigs = mapConfigs;
    }

    public MapDataContainer(MapTileRequest tileRequest) {
        this.mDataType = 2;
        this.mMapTileRequest = tileRequest;
    }

    public MapDataContainer(MapTileResponse tileResponse) {
        this.mDataType = 3;
        this.mMapTileResponse = tileResponse;
    }

    public boolean isValid(int requestedType) {
        if (this.mDataType != requestedType) {
            Logger.logW(TAG, "isValid(" + requestedType + "), " + "invalid type:" + this.mDataType);
            return false;
        } else if (this.mDataType == 1) {
            if (this.mMapConfigs == null || this.mMapConfigs.size() <= 0) {
                return false;
            }
            return true;
        } else if (this.mDataType == 2) {
            if (this.mMapTileRequest == null) {
                return false;
            }
            return true;
        } else if (this.mDataType != 3) {
            return false;
        } else {
            if (this.mMapTileResponse == null) {
                return false;
            }
            return true;
        }
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

    private MapDataContainer(Parcel in) {
        try {
            readFromParcel(in);
        } catch (IOException e) {
            this.mDataType = 0;
            Logger.logE(TAG, "DataTransporter(" + in + ")", e);
        }
    }

    private void readFromParcel(Parcel in) throws IOException {
        this.mDataType = in.readInt();
        byte[] data;
        if (this.mDataType == 1) {
            data = new byte[in.readInt()];
            in.readByteArray(data);
            this.mMapConfigs = Storable.readList(MapConfigLayer.class, data);
        } else if (this.mDataType == 2) {
            data = new byte[in.readInt()];
            in.readByteArray(data);
            this.mMapTileRequest = new MapTileRequest(data);
        } else if (this.mDataType == 3) {
            data = new byte[in.readInt()];
            in.readByteArray(data);
            this.mMapTileResponse = new MapTileResponse(data);
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mDataType);
        if (this.mDataType == 1) {
            writeObject(dest, Storable.getAsBytes(this.mMapConfigs));
        } else if (this.mDataType == 2) {
            writeObject(dest, this.mMapTileRequest.getAsBytes());
        } else if (this.mDataType == 3) {
            writeObject(dest, this.mMapTileResponse.getAsBytes());
        }
    }

    private void writeObject(Parcel dest, byte[] data) {
        dest.writeInt(data.length);
        dest.writeByteArray(data);
    }

    public int describeContents() {
        return 0;
    }
}
