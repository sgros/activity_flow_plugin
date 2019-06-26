package com.googlecode.mp4parser;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public interface DataSource extends Closeable {
   void close() throws IOException;

   ByteBuffer map(long var1, long var3) throws IOException;

   long position() throws IOException;

   void position(long var1) throws IOException;

   long transferTo(long var1, long var3, WritableByteChannel var5) throws IOException;
}
