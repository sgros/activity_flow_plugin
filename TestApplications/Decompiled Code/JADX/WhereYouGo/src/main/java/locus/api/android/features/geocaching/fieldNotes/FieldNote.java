package locus.api.android.features.geocaching.fieldNotes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class FieldNote extends Storable {
    private String mCacheCode;
    private String mCacheName;
    private boolean mFavorite;
    private long mId;
    private List<FieldNoteImage> mImages;
    private boolean mLogged;
    private String mNote;
    private long mTime;
    private int mType;

    public FieldNote(byte[] data) throws IOException {
        super(data);
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getCacheCode() {
        return this.mCacheCode;
    }

    public void setCacheCode(String cacheCode) {
        if (cacheCode == null) {
            cacheCode = "";
        }
        this.mCacheCode = cacheCode;
    }

    public String getCacheName() {
        return this.mCacheName;
    }

    public void setCacheName(String cacheName) {
        if (cacheName == null) {
            cacheName = "";
        }
        this.mCacheName = cacheName;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public long getTime() {
        return this.mTime;
    }

    public void setTime(long dateTime) {
        this.mTime = dateTime;
    }

    public String getNote() {
        return this.mNote;
    }

    public void setNote(String note) {
        if (note == null) {
            note = "";
        }
        this.mNote = note;
    }

    public boolean isFavorite() {
        return this.mFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.mFavorite = favorite;
    }

    public boolean isLogged() {
        return this.mLogged;
    }

    public void setLogged(boolean logged) {
        this.mLogged = logged;
    }

    public void addImage(FieldNoteImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image not valid");
        }
        this.mImages.add(image);
    }

    public Iterator<FieldNoteImage> getImages() {
        return this.mImages.iterator();
    }

    public int getImagesCount() {
        return this.mImages.size();
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

    public void reset() {
        this.mId = -1;
        this.mCacheCode = "";
        this.mCacheName = "";
        this.mType = 0;
        this.mTime = 0;
        this.mNote = "";
        this.mFavorite = false;
        this.mLogged = false;
        this.mImages = new ArrayList();
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mId = dr.readLong();
        this.mCacheCode = dr.readString();
        this.mCacheName = dr.readString();
        this.mType = dr.readInt();
        this.mTime = dr.readLong();
        this.mNote = dr.readString();
        this.mFavorite = dr.readBoolean();
        this.mLogged = dr.readBoolean();
        this.mImages = dr.readListStorable(FieldNoteImage.class);
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeLong(this.mId);
        dw.writeString(this.mCacheCode);
        dw.writeString(this.mCacheName);
        dw.writeInt(this.mType);
        dw.writeLong(this.mTime);
        dw.writeString(this.mNote);
        dw.writeBoolean(this.mFavorite);
        dw.writeBoolean(this.mLogged);
        dw.writeListStorable(this.mImages);
    }
}
