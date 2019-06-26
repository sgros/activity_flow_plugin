// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso;

import java.io.IOException;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.DataSource;

public interface BoxParser
{
    Box parseBox(final DataSource p0, final Container p1) throws IOException;
}
