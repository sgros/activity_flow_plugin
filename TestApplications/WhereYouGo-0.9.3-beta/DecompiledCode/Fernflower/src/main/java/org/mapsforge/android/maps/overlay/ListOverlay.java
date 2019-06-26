package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mapsforge.core.model.BoundingBox;

public class ListOverlay implements Overlay {
   private final List overlayItems = Collections.synchronizedList(new ArrayList());

   public int compareTo(Overlay var1) {
      return 0;
   }

   public void draw(BoundingBox param1, byte param2, Canvas param3) {
      // $FF: Couldn't be decompiled
   }

   public List getOverlayItems() {
      // $FF: Couldn't be decompiled
   }
}
