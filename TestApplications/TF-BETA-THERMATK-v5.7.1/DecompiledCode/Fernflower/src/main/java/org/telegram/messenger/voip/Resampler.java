package org.telegram.messenger.voip;

import java.nio.ByteBuffer;

public class Resampler {
   public static native int convert44to48(ByteBuffer var0, ByteBuffer var1);

   public static native int convert48to44(ByteBuffer var0, ByteBuffer var1);
}