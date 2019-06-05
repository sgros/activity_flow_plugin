package org.mozilla.rocket.content;

import android.arch.lifecycle.LifecycleOwner;
import java.util.List;

public interface NewsViewContract {
   LifecycleOwner getViewLifecycleOwner();

   void updateNews(List var1);
}
