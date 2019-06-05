package cz.matejcik.openwig.platform;

import java.io.IOException;

public interface SeekableFile {
   long position() throws IOException;

   int read() throws IOException;

   double readDouble() throws IOException;

   void readFully(byte[] var1) throws IOException;

   int readInt() throws IOException;

   long readLong() throws IOException;

   short readShort() throws IOException;

   String readString() throws IOException;

   void seek(long var1) throws IOException;

   long skip(long var1) throws IOException;
}
