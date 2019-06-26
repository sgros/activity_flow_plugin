package com.journeyapps.barcodescanner.camera;

import com.journeyapps.barcodescanner.SourceData;

public interface PreviewCallback {
   void onPreview(SourceData var1);

   void onPreviewError(Exception var1);
}
