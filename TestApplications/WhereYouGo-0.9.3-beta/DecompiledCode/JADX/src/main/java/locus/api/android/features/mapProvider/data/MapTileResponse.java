package locus.api.android.features.mapProvider.data;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import java.io.IOException;
import locus.api.android.utils.UtilsBitmap;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class MapTileResponse extends Storable {
    public static final int CODE_INTERNAL_ERROR = 4;
    public static final int CODE_INVALID_REQUEST = 2;
    public static final int CODE_NOT_EXISTS = 3;
    public static final int CODE_UNKNOWN = 0;
    public static final int CODE_VALID = 1;
    private Bitmap mImage;
    private int mResultCode;

    public MapTileResponse(byte[] data) throws IOException {
        super(data);
    }

    public int getResultCode() {
        return this.mResultCode;
    }

    public void setResultCode(int resultCode) {
        this.mResultCode = resultCode;
    }

    public Bitmap getImage() {
        return this.mImage;
    }

    public void setImage(Bitmap image) {
        this.mImage = image;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

    public void reset() {
        this.mResultCode = 0;
        this.mImage = null;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mResultCode = dr.readInt();
        int size = dr.readInt();
        if (size > 0) {
            this.mImage = UtilsBitmap.getBitmap(dr.readBytes(size));
        } else {
            this.mImage = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeInt(this.mResultCode);
        if (this.mImage == null) {
            dw.writeInt(0);
            return;
        }
        byte[] data = UtilsBitmap.getBitmap(this.mImage, CompressFormat.PNG);
        if (data == null || data.length == 0) {
            dw.writeInt(0);
            return;
        }
        dw.writeInt(data.length);
        dw.write(data);
    }
}
