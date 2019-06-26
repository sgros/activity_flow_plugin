package com.coremedia.iso.boxes;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public interface Box {
   void getBox(WritableByteChannel var1) throws IOException;

   long getSize();

   void setParent(Container var1);
}
