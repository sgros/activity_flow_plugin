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
   public static final Creator CREATOR = new Creator() {
      public MapDataContainer createFromParcel(Parcel var1) {
         return new MapDataContainer(var1);
      }

      public MapDataContainer[] newArray(int var1) {
         return new MapDataContainer[var1];
      }
   };
   public static final int DATA_TYPE_CONFIGURATION = 1;
   public static final int DATA_TYPE_TILE_REQUEST = 2;
   public static final int DATA_TYPE_TILE_RESPONSE = 3;
   private static final int DATA_TYPE_UNDEFINED = 0;
   private static final String TAG = MapDataContainer.class.getSimpleName();
   private int mDataType;
   private List mMapConfigs;
   private MapTileRequest mMapTileRequest;
   private MapTileResponse mMapTileResponse;

   private MapDataContainer(Parcel var1) {
      try {
         this.readFromParcel(var1);
      } catch (IOException var3) {
         this.mDataType = 0;
         Logger.logE(TAG, "DataTransporter(" + var1 + ")", var3);
      }

   }

   // $FF: synthetic method
   MapDataContainer(Parcel var1, Object var2) {
      this(var1);
   }

   public MapDataContainer(List var1) {
      this.mDataType = 1;
      this.mMapConfigs = var1;
   }

   public MapDataContainer(MapTileRequest var1) {
      this.mDataType = 2;
      this.mMapTileRequest = var1;
   }

   public MapDataContainer(MapTileResponse var1) {
      this.mDataType = 3;
      this.mMapTileResponse = var1;
   }

   private void readFromParcel(Parcel var1) throws IOException {
      this.mDataType = var1.readInt();
      byte[] var2;
      if (this.mDataType == 1) {
         var2 = new byte[var1.readInt()];
         var1.readByteArray(var2);
         this.mMapConfigs = Storable.readList(MapConfigLayer.class, var2);
      } else if (this.mDataType == 2) {
         var2 = new byte[var1.readInt()];
         var1.readByteArray(var2);
         this.mMapTileRequest = new MapTileRequest(var2);
      } else if (this.mDataType == 3) {
         var2 = new byte[var1.readInt()];
         var1.readByteArray(var2);
         this.mMapTileResponse = new MapTileResponse(var2);
      }

   }

   private void writeObject(Parcel var1, byte[] var2) {
      var1.writeInt(var2.length);
      var1.writeByteArray(var2);
   }

   public int describeContents() {
      return 0;
   }

   public List getMapConfigurations() {
      return this.mMapConfigs;
   }

   public MapTileRequest getTileRequest() {
      return this.mMapTileRequest;
   }

   public MapTileResponse getTileResponse() {
      return this.mMapTileResponse;
   }

   public boolean isValid(int var1) {
      boolean var2 = true;
      if (this.mDataType != var1) {
         Logger.logW(TAG, "isValid(" + var1 + "), " + "invalid type:" + this.mDataType);
         var2 = false;
      } else if (this.mDataType == 1) {
         if (this.mMapConfigs == null || this.mMapConfigs.size() <= 0) {
            var2 = false;
         }
      } else if (this.mDataType == 2) {
         if (this.mMapTileRequest == null) {
            var2 = false;
         }
      } else if (this.mDataType == 3) {
         if (this.mMapTileResponse == null) {
            var2 = false;
         }
      } else {
         var2 = false;
      }

      return var2;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mDataType);
      if (this.mDataType == 1) {
         this.writeObject(var1, Storable.getAsBytes(this.mMapConfigs));
      } else if (this.mDataType == 2) {
         this.writeObject(var1, this.mMapTileRequest.getAsBytes());
      } else if (this.mDataType == 3) {
         this.writeObject(var1, this.mMapTileResponse.getAsBytes());
      }

   }
}
