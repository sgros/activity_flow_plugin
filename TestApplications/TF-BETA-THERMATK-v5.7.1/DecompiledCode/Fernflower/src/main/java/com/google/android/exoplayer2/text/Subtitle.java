package com.google.android.exoplayer2.text;

import java.util.List;

public interface Subtitle {
   List getCues(long var1);

   long getEventTime(int var1);

   int getEventTimeCount();

   int getNextEventTimeIndex(long var1);
}
