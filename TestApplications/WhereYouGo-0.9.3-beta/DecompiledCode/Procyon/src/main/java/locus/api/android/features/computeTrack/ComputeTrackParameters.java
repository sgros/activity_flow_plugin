// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.computeTrack;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.DataReaderBigEndian;
import java.io.IOException;
import locus.api.objects.extra.Location;
import locus.api.objects.Storable;

public class ComputeTrackParameters extends Storable
{
    private boolean mComputeInstructions;
    private float mDirection;
    private boolean mHasDirection;
    private Location[] mLocs;
    private int mType;
    
    public ComputeTrackParameters() {
    }
    
    public ComputeTrackParameters(final int mType, final Location[] mLocs) {
        this.mType = mType;
        if (mLocs == null || mLocs.length < 2) {
            throw new IllegalArgumentException("'locs' parameter cannot be 'null' or smaller then 2");
        }
        this.mLocs = mLocs;
    }
    
    public ComputeTrackParameters(final byte[] array) throws IOException {
        super(array);
    }
    
    public float getCurrentDirection() {
        return this.mDirection;
    }
    
    public Location[] getLocations() {
        return this.mLocs;
    }
    
    public int getType() {
        return this.mType;
    }
    
    @Override
    protected int getVersion() {
        return 1;
    }
    
    public boolean hasDirection() {
        return this.mHasDirection;
    }
    
    public boolean isComputeInstructions() {
        return this.mComputeInstructions;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mType = dataReaderBigEndian.readInt();
        this.mComputeInstructions = dataReaderBigEndian.readBoolean();
        this.mDirection = dataReaderBigEndian.readFloat();
        this.mLocs = new Location[dataReaderBigEndian.readInt()];
        for (int i = 0; i < this.mLocs.length; ++i) {
            this.mLocs[i] = new Location(dataReaderBigEndian);
        }
        if (n >= 1) {
            this.mHasDirection = dataReaderBigEndian.readBoolean();
        }
    }
    
    @Override
    public void reset() {
        this.mType = 6;
        this.mComputeInstructions = true;
        this.mHasDirection = false;
        this.mDirection = 0.0f;
        this.mLocs = new Location[0];
    }
    
    public void setComputeInstructions(final boolean mComputeInstructions) {
        this.mComputeInstructions = mComputeInstructions;
    }
    
    public void setCurrentDirection(final float mDirection) {
        this.mDirection = mDirection;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeInt(this.mType);
        dataWriterBigEndian.writeBoolean(this.mComputeInstructions);
        dataWriterBigEndian.writeFloat(this.mDirection);
        dataWriterBigEndian.writeInt(this.mLocs.length);
        final Location[] mLocs = this.mLocs;
        for (int length = mLocs.length, i = 0; i < length; ++i) {
            dataWriterBigEndian.writeStorable(mLocs[i]);
        }
        dataWriterBigEndian.writeBoolean(this.mHasDirection);
    }
}
