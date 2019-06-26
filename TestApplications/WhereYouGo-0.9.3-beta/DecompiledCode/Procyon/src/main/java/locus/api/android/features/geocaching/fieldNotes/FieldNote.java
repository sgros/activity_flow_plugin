// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.geocaching.fieldNotes;

import locus.api.utils.DataWriterBigEndian;
import java.util.ArrayList;
import locus.api.utils.DataReaderBigEndian;
import java.util.Iterator;
import java.io.IOException;
import java.util.List;
import locus.api.objects.Storable;

public class FieldNote extends Storable
{
    private String mCacheCode;
    private String mCacheName;
    private boolean mFavorite;
    private long mId;
    private List<FieldNoteImage> mImages;
    private boolean mLogged;
    private String mNote;
    private long mTime;
    private int mType;
    
    public FieldNote() {
    }
    
    public FieldNote(final byte[] array) throws IOException {
        super(array);
    }
    
    public void addImage(final FieldNoteImage fieldNoteImage) {
        if (fieldNoteImage == null) {
            throw new IllegalArgumentException("Image not valid");
        }
        this.mImages.add(fieldNoteImage);
    }
    
    public String getCacheCode() {
        return this.mCacheCode;
    }
    
    public String getCacheName() {
        return this.mCacheName;
    }
    
    public long getId() {
        return this.mId;
    }
    
    public Iterator<FieldNoteImage> getImages() {
        return this.mImages.iterator();
    }
    
    public int getImagesCount() {
        return this.mImages.size();
    }
    
    public String getNote() {
        return this.mNote;
    }
    
    public long getTime() {
        return this.mTime;
    }
    
    public int getType() {
        return this.mType;
    }
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    public boolean isFavorite() {
        return this.mFavorite;
    }
    
    public boolean isLogged() {
        return this.mLogged;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mId = dataReaderBigEndian.readLong();
        this.mCacheCode = dataReaderBigEndian.readString();
        this.mCacheName = dataReaderBigEndian.readString();
        this.mType = dataReaderBigEndian.readInt();
        this.mTime = dataReaderBigEndian.readLong();
        this.mNote = dataReaderBigEndian.readString();
        this.mFavorite = dataReaderBigEndian.readBoolean();
        this.mLogged = dataReaderBigEndian.readBoolean();
        this.mImages = (List<FieldNoteImage>)dataReaderBigEndian.readListStorable(FieldNoteImage.class);
    }
    
    @Override
    public void reset() {
        this.mId = -1L;
        this.mCacheCode = "";
        this.mCacheName = "";
        this.mType = 0;
        this.mTime = 0L;
        this.mNote = "";
        this.mFavorite = false;
        this.mLogged = false;
        this.mImages = new ArrayList<FieldNoteImage>();
    }
    
    public void setCacheCode(final String s) {
        String mCacheCode = s;
        if (s == null) {
            mCacheCode = "";
        }
        this.mCacheCode = mCacheCode;
    }
    
    public void setCacheName(final String s) {
        String mCacheName = s;
        if (s == null) {
            mCacheName = "";
        }
        this.mCacheName = mCacheName;
    }
    
    public void setFavorite(final boolean mFavorite) {
        this.mFavorite = mFavorite;
    }
    
    public void setId(final long mId) {
        this.mId = mId;
    }
    
    public void setLogged(final boolean mLogged) {
        this.mLogged = mLogged;
    }
    
    public void setNote(final String s) {
        String mNote = s;
        if (s == null) {
            mNote = "";
        }
        this.mNote = mNote;
    }
    
    public void setTime(final long mTime) {
        this.mTime = mTime;
    }
    
    public void setType(final int mType) {
        this.mType = mType;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeLong(this.mId);
        dataWriterBigEndian.writeString(this.mCacheCode);
        dataWriterBigEndian.writeString(this.mCacheName);
        dataWriterBigEndian.writeInt(this.mType);
        dataWriterBigEndian.writeLong(this.mTime);
        dataWriterBigEndian.writeString(this.mNote);
        dataWriterBigEndian.writeBoolean(this.mFavorite);
        dataWriterBigEndian.writeBoolean(this.mLogged);
        dataWriterBigEndian.writeListStorable(this.mImages);
    }
}
