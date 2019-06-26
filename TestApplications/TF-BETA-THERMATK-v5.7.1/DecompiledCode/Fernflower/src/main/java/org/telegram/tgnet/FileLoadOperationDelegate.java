package org.telegram.tgnet;

public interface FileLoadOperationDelegate {
   void onFailed(int var1);

   void onFinished(String var1);

   void onProgressChanged(float var1);
}
