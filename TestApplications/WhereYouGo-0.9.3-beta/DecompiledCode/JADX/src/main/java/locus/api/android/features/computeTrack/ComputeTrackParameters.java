package locus.api.android.features.computeTrack;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.objects.extra.Location;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class ComputeTrackParameters extends Storable {
    private boolean mComputeInstructions;
    private float mDirection;
    private boolean mHasDirection;
    private Location[] mLocs;
    private int mType;

    public ComputeTrackParameters(int type, Location[] locs) {
        this.mType = type;
        if (locs == null || locs.length < 2) {
            throw new IllegalArgumentException("'locs' parameter cannot be 'null' or smaller then 2");
        }
        this.mLocs = locs;
    }

    public ComputeTrackParameters(byte[] data) throws IOException {
        super(data);
    }

    public int getType() {
        return this.mType;
    }

    public boolean isComputeInstructions() {
        return this.mComputeInstructions;
    }

    public void setComputeInstructions(boolean computeInstructions) {
        this.mComputeInstructions = computeInstructions;
    }

    public boolean hasDirection() {
        return this.mHasDirection;
    }

    public float getCurrentDirection() {
        return this.mDirection;
    }

    public void setCurrentDirection(float direction) {
        this.mDirection = direction;
    }

    public Location[] getLocations() {
        return this.mLocs;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 1;
    }

    public void reset() {
        this.mType = 6;
        this.mComputeInstructions = true;
        this.mHasDirection = false;
        this.mDirection = 0.0f;
        this.mLocs = new Location[0];
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mType = dr.readInt();
        this.mComputeInstructions = dr.readBoolean();
        this.mDirection = dr.readFloat();
        this.mLocs = new Location[dr.readInt()];
        int m = this.mLocs.length;
        for (int i = 0; i < m; i++) {
            this.mLocs[i] = new Location(dr);
        }
        if (version >= 1) {
            this.mHasDirection = dr.readBoolean();
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeInt(this.mType);
        dw.writeBoolean(this.mComputeInstructions);
        dw.writeFloat(this.mDirection);
        dw.writeInt(this.mLocs.length);
        for (Location mLoc : this.mLocs) {
            dw.writeStorable(mLoc);
        }
        dw.writeBoolean(this.mHasDirection);
    }
}
