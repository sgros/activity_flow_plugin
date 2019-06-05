package locus.api.android.features.geocaching.fieldNotes;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class FieldNoteImage extends Storable {
    private String mCaption;
    private String mDescription;
    private long mFieldNoteId;
    private long mId;
    private byte[] mImage;

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public long getFieldNoteId() {
        return this.mFieldNoteId;
    }

    public void setFieldNoteId(long fieldNoteId) {
        this.mFieldNoteId = fieldNoteId;
    }

    public String getCaption() {
        return this.mCaption;
    }

    public void setCaption(String caption) {
        if (caption == null) {
            caption = "";
        }
        this.mCaption = caption;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        this.mDescription = description;
    }

    public byte[] getImage() {
        return this.mImage;
    }

    public void setImage(byte[] image) {
        this.mImage = image;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

    public void reset() {
        this.mId = -1;
        this.mFieldNoteId = -1;
        this.mCaption = "";
        this.mDescription = "";
        this.mImage = null;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mId = dr.readLong();
        this.mFieldNoteId = dr.readLong();
        this.mCaption = dr.readString();
        this.mDescription = dr.readString();
        int imgSize = dr.readInt();
        if (imgSize > 0) {
            this.mImage = new byte[imgSize];
            dr.readBytes(this.mImage);
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeLong(this.mId);
        dw.writeLong(this.mFieldNoteId);
        dw.writeString(this.mCaption);
        dw.writeString(this.mDescription);
        if (this.mImage == null || this.mImage.length <= 0) {
            dw.writeInt(0);
            return;
        }
        dw.writeInt(this.mImage.length);
        dw.write(this.mImage);
    }
}
