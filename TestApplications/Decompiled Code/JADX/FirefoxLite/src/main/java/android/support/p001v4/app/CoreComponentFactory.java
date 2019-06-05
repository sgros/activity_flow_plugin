package android.support.p001v4.app;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;

/* renamed from: android.support.v4.app.CoreComponentFactory */
public class CoreComponentFactory extends AppComponentFactory {

    /* renamed from: android.support.v4.app.CoreComponentFactory$CompatWrapped */
    public interface CompatWrapped {
        Object getWrapper();
    }

    public Activity instantiateActivity(ClassLoader classLoader, String str, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Activity) CoreComponentFactory.checkCompatWrapper(super.instantiateActivity(classLoader, str, intent));
    }

    public Application instantiateApplication(ClassLoader classLoader, String str) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Application) CoreComponentFactory.checkCompatWrapper(super.instantiateApplication(classLoader, str));
    }

    public BroadcastReceiver instantiateReceiver(ClassLoader classLoader, String str, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (BroadcastReceiver) CoreComponentFactory.checkCompatWrapper(super.instantiateReceiver(classLoader, str, intent));
    }

    public ContentProvider instantiateProvider(ClassLoader classLoader, String str) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ContentProvider) CoreComponentFactory.checkCompatWrapper(super.instantiateProvider(classLoader, str));
    }

    public Service instantiateService(ClassLoader classLoader, String str, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Service) CoreComponentFactory.checkCompatWrapper(super.instantiateService(classLoader, str, intent));
    }

    static <T> T checkCompatWrapper(T t) {
        if (t instanceof CompatWrapped) {
            Object wrapper = ((CompatWrapped) t).getWrapper();
            if (wrapper != null) {
                return wrapper;
            }
        }
        return t;
    }
}
