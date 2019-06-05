package com.airbnb.lottie.model;

import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public interface KeyPathElement {
   void addValueCallback(Object var1, LottieValueCallback var2);

   void resolveKeyPath(KeyPath var1, int var2, List var3, KeyPath var4);
}
