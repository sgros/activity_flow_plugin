package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;

interface EngineJobListener {
   void onEngineJobCancelled(EngineJob var1, Key var2);

   void onEngineJobComplete(Key var1, EngineResource var2);
}
