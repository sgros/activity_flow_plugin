// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.mapProvider.data;

import android.graphics.Bitmap$CompressFormat;
import locus.api.utils.DataWriterBigEndian;
import locus.api.android.utils.UtilsBitmap;
import locus.api.utils.DataReaderBigEndian;
import java.io.IOException;
import android.graphics.Bitmap;
import locus.api.objects.Storable;

public class MapTileResponse extends Storable
{
    public static final int CODE_INTERNAL_ERROR = 4;
    public static final int CODE_INVALID_REQUEST = 2;
    public static final int CODE_NOT_EXISTS = 3;
    public static final int CODE_UNKNOWN = 0;
    public static final int CODE_VALID = 1;
    private Bitmap mImage;
    private int mResultCode;
    
    public MapTileResponse() {
    }
    
    public MapTileResponse(final byte[] array) throws IOException {
        super(array);
    }
    
    public Bitmap getImage() {
        return this.mImage;
    }
    
    public int getResultCode() {
        return this.mResultCode;
    }
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    @Override
    protected void readObject(int int1, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mResultCode = dataReaderBigEndian.readInt();
        int1 = dataReaderBigEndian.readInt();
        if (int1 > 0) {
            this.mImage = UtilsBitmap.getBitmap(dataReaderBigEndian.readBytes(int1));
        }
        else {
            this.mImage = null;
        }
    }
    
    @Override
    public void reset() {
        this.mResultCode = 0;
        this.mImage = null;
    }
    
    public void setImage(final Bitmap mImage) {
        this.mImage = mImage;
    }
    
    public void setResultCode(final int mResultCode) {
        this.mResultCode = mResultCode;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeInt(this.mResultCode);
        if (this.mImage == null) {
            dataWriterBigEndian.writeInt(0);
        }
        else {
            final byte[] bitmap = UtilsBitmap.getBitmap(this.mImage, Bitmap$CompressFormat.PNG);
            if (bitmap == null || bitmap.length == 0) {
                dataWriterBigEndian.writeInt(0);
            }
            else {
                dataWriterBigEndian.writeInt(bitmap.length);
                dataWriterBigEndian.write(bitmap);
            }
        }
    }
}
