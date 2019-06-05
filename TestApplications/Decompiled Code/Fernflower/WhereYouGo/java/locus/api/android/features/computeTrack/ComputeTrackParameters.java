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

   public ComputeTrackParameters() {
   }

   public ComputeTrackParameters(int var1, Location[] var2) {
      this.mType = var1;
      if (var2 != null && var2.length >= 2) {
         this.mLocs = var2;
      } else {
         throw new IllegalArgumentException("'locs' parameter cannot be 'null' or smaller then 2");
      }
   }

   public ComputeTrackParameters(byte[] var1) throws IOException {
      super(var1);
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

   protected int getVersion() {
      return 1;
   }

   public boolean hasDirection() {
      return this.mHasDirection;
   }

   public boolean isComputeInstructions() {
      return this.mComputeInstructions;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mType = var2.readInt();
      this.mComputeInstructions = var2.readBoolean();
      this.mDirection = var2.readFloat();
      this.mLocs = new Location[var2.readInt()];
      int var3 = 0;

      for(int var4 = this.mLocs.length; var3 < var4; ++var3) {
         this.mLocs[var3] = new Location(var2);
      }

      if (var1 >= 1) {
         this.mHasDirection = var2.readBoolean();
      }

   }

   public void reset() {
      this.mType = 6;
      this.mComputeInstructions = true;
      this.mHasDirection = false;
      this.mDirection = 0.0F;
      this.mLocs = new Location[0];
   }

   public void setComputeInstructions(boolean var1) {
      this.mComputeInstructions = var1;
   }

   public void setCurrentDirection(float var1) {
      this.mDirection = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeInt(this.mType);
      var1.writeBoolean(this.mComputeInstructions);
      var1.writeFloat(this.mDirection);
      var1.writeInt(this.mLocs.length);
      Location[] var2 = this.mLocs;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.writeStorable(var2[var4]);
      }

      var1.writeBoolean(this.mHasDirection);
   }
}
