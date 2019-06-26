// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.geocaching.fieldNotes;

import locus.api.utils.DataWriterBigEndian;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.objects.Storable;

public class FieldNoteImage extends Storable
{
    private String mCaption;
    private String mDescription;
    private long mFieldNoteId;
    private long mId;
    private byte[] mImage;
    
    public String getCaption() {
        return this.mCaption;
    }
    
    public String getDescription() {
        return this.mDescription;
    }
    
    public long getFieldNoteId() {
        return this.mFieldNoteId;
    }
    
    public long getId() {
        return this.mId;
    }
    
    public byte[] getImage() {
        return this.mImage;
    }
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    @Override
    protected void readObject(int int1, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mId = dataReaderBigEndian.readLong();
        this.mFieldNoteId = dataReaderBigEndian.readLong();
        this.mCaption = dataReaderBigEndian.readString();
        this.mDescription = dataReaderBigEndian.readString();
        int1 = dataReaderBigEndian.readInt();
        if (int1 > 0) {
            dataReaderBigEndian.readBytes(this.mImage = new byte[int1]);
        }
    }
    
    @Override
    public void reset() {
        this.mId = -1L;
        this.mFieldNoteId = -1L;
        this.mCaption = "";
        this.mDescription = "";
        this.mImage = null;
    }
    
    public void setCaption(final String s) {
        String mCaption = s;
        if (s == null) {
            mCaption = "";
        }
        this.mCaption = mCaption;
    }
    
    public void setDescription(final String s) {
        String mDescription = s;
        if (s == null) {
            mDescription = "";
        }
        this.mDescription = mDescription;
    }
    
    public void setFieldNoteId(final long mFieldNoteId) {
        this.mFieldNoteId = mFieldNoteId;
    }
    
    public void setId(final long mId) {
        this.mId = mId;
    }
    
    public void setImage(final byte[] mImage) {
        this.mImage = mImage;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeLong(this.mId);
        dataWriterBigEndian.writeLong(this.mFieldNoteId);
        dataWriterBigEndian.writeString(this.mCaption);
        dataWriterBigEndian.writeString(this.mDescription);
        if (this.mImage != null && this.mImage.length > 0) {
            dataWriterBigEndian.writeInt(this.mImage.length);
            dataWriterBigEndian.write(this.mImage);
        }
        else {
            dataWriterBigEndian.writeInt(0);
        }
    }
}
