package com.adjust.sdk;

public interface ISdkClickHandler {
    void init(IActivityHandler iActivityHandler, boolean z);

    void pauseSending();

    void resumeSending();

    void sendSdkClick(ActivityPackage activityPackage);

    void teardown();
}
