package com.journeyapps.barcodescanner.camera;

public class CameraSettings {
   private boolean autoFocusEnabled = true;
   private boolean autoTorchEnabled = false;
   private boolean barcodeSceneModeEnabled = false;
   private boolean continuousFocusEnabled = false;
   private boolean exposureEnabled = false;
   private CameraSettings.FocusMode focusMode;
   private boolean meteringEnabled = false;
   private int requestedCameraId = -1;
   private boolean scanInverted = false;

   public CameraSettings() {
      this.focusMode = CameraSettings.FocusMode.AUTO;
   }

   public CameraSettings.FocusMode getFocusMode() {
      return this.focusMode;
   }

   public int getRequestedCameraId() {
      return this.requestedCameraId;
   }

   public boolean isAutoFocusEnabled() {
      return this.autoFocusEnabled;
   }

   public boolean isAutoTorchEnabled() {
      return this.autoTorchEnabled;
   }

   public boolean isBarcodeSceneModeEnabled() {
      return this.barcodeSceneModeEnabled;
   }

   public boolean isContinuousFocusEnabled() {
      return this.continuousFocusEnabled;
   }

   public boolean isExposureEnabled() {
      return this.exposureEnabled;
   }

   public boolean isMeteringEnabled() {
      return this.meteringEnabled;
   }

   public boolean isScanInverted() {
      return this.scanInverted;
   }

   public void setAutoFocusEnabled(boolean var1) {
      this.autoFocusEnabled = var1;
      if (var1 && this.continuousFocusEnabled) {
         this.focusMode = CameraSettings.FocusMode.CONTINUOUS;
      } else if (var1) {
         this.focusMode = CameraSettings.FocusMode.AUTO;
      } else {
         this.focusMode = null;
      }

   }

   public void setAutoTorchEnabled(boolean var1) {
      this.autoTorchEnabled = var1;
   }

   public void setBarcodeSceneModeEnabled(boolean var1) {
      this.barcodeSceneModeEnabled = var1;
   }

   public void setContinuousFocusEnabled(boolean var1) {
      this.continuousFocusEnabled = var1;
      if (var1) {
         this.focusMode = CameraSettings.FocusMode.CONTINUOUS;
      } else if (this.autoFocusEnabled) {
         this.focusMode = CameraSettings.FocusMode.AUTO;
      } else {
         this.focusMode = null;
      }

   }

   public void setExposureEnabled(boolean var1) {
      this.exposureEnabled = var1;
   }

   public void setFocusMode(CameraSettings.FocusMode var1) {
      this.focusMode = var1;
   }

   public void setMeteringEnabled(boolean var1) {
      this.meteringEnabled = var1;
   }

   public void setRequestedCameraId(int var1) {
      this.requestedCameraId = var1;
   }

   public void setScanInverted(boolean var1) {
      this.scanInverted = var1;
   }

   public static enum FocusMode {
      AUTO,
      CONTINUOUS,
      INFINITY,
      MACRO;
   }
}
