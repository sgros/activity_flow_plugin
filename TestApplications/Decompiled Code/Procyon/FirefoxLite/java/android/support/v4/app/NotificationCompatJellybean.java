// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.app.Notification$Builder;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import android.util.Log;
import android.app.Notification;
import android.os.Parcelable;
import android.util.SparseArray;
import android.os.Bundle;
import java.util.List;
import java.lang.reflect.Field;

class NotificationCompatJellybean
{
    private static final Object sActionsLock;
    private static Field sExtrasField;
    private static boolean sExtrasFieldAccessFailed;
    private static final Object sExtrasLock;
    
    static {
        sExtrasLock = new Object();
        sActionsLock = new Object();
    }
    
    public static SparseArray<Bundle> buildActionExtrasMap(final List<Bundle> list) {
        final int size = list.size();
        SparseArray sparseArray = null;
        SparseArray sparseArray2;
        for (int i = 0; i < size; ++i, sparseArray = sparseArray2) {
            final Bundle bundle = list.get(i);
            sparseArray2 = sparseArray;
            if (bundle != null) {
                if ((sparseArray2 = sparseArray) == null) {
                    sparseArray2 = new SparseArray();
                }
                sparseArray2.put(i, (Object)bundle);
            }
        }
        return (SparseArray<Bundle>)sparseArray;
    }
    
    static Bundle getBundleForAction(final NotificationCompat.Action action) {
        final Bundle bundle = new Bundle();
        bundle.putInt("icon", action.getIcon());
        bundle.putCharSequence("title", action.getTitle());
        bundle.putParcelable("actionIntent", (Parcelable)action.getActionIntent());
        Bundle bundle2;
        if (action.getExtras() != null) {
            bundle2 = new Bundle(action.getExtras());
        }
        else {
            bundle2 = new Bundle();
        }
        bundle2.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        bundle.putBundle("extras", bundle2);
        bundle.putParcelableArray("remoteInputs", (Parcelable[])toBundleArray(action.getRemoteInputs()));
        bundle.putBoolean("showsUserInterface", action.getShowsUserInterface());
        bundle.putInt("semanticAction", action.getSemanticAction());
        return bundle;
    }
    
    public static Bundle getExtras(final Notification notification) {
        synchronized (NotificationCompatJellybean.sExtrasLock) {
            if (NotificationCompatJellybean.sExtrasFieldAccessFailed) {
                return null;
            }
            try {
                if (NotificationCompatJellybean.sExtrasField == null) {
                    final Field declaredField = Notification.class.getDeclaredField("extras");
                    if (!Bundle.class.isAssignableFrom(declaredField.getType())) {
                        Log.e("NotificationCompat", "Notification.extras field is not of type Bundle");
                        NotificationCompatJellybean.sExtrasFieldAccessFailed = true;
                        return null;
                    }
                    declaredField.setAccessible(true);
                    NotificationCompatJellybean.sExtrasField = declaredField;
                }
                Bundle value;
                if ((value = (Bundle)NotificationCompatJellybean.sExtrasField.get(notification)) == null) {
                    value = new Bundle();
                    NotificationCompatJellybean.sExtrasField.set(notification, value);
                }
                return value;
            }
            catch (NoSuchFieldException ex) {
                Log.e("NotificationCompat", "Unable to access notification extras", (Throwable)ex);
            }
            catch (IllegalAccessException ex2) {
                Log.e("NotificationCompat", "Unable to access notification extras", (Throwable)ex2);
            }
            NotificationCompatJellybean.sExtrasFieldAccessFailed = true;
            return null;
        }
    }
    
    private static Bundle toBundle(final RemoteInput remoteInput) {
        final Bundle bundle = new Bundle();
        bundle.putString("resultKey", remoteInput.getResultKey());
        bundle.putCharSequence("label", remoteInput.getLabel());
        bundle.putCharSequenceArray("choices", remoteInput.getChoices());
        bundle.putBoolean("allowFreeFormInput", remoteInput.getAllowFreeFormInput());
        bundle.putBundle("extras", remoteInput.getExtras());
        final Set<String> allowedDataTypes = remoteInput.getAllowedDataTypes();
        if (allowedDataTypes != null && !allowedDataTypes.isEmpty()) {
            final ArrayList list = new ArrayList<String>(allowedDataTypes.size());
            final Iterator<String> iterator = allowedDataTypes.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
            bundle.putStringArrayList("allowedDataTypes", list);
        }
        return bundle;
    }
    
    private static Bundle[] toBundleArray(final RemoteInput[] array) {
        if (array == null) {
            return null;
        }
        final Bundle[] array2 = new Bundle[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = toBundle(array[i]);
        }
        return array2;
    }
    
    public static Bundle writeActionAndGetExtras(final Notification$Builder notification$Builder, final NotificationCompat.Action action) {
        notification$Builder.addAction(action.getIcon(), action.getTitle(), action.getActionIntent());
        final Bundle bundle = new Bundle(action.getExtras());
        if (action.getRemoteInputs() != null) {
            bundle.putParcelableArray("android.support.remoteInputs", (Parcelable[])toBundleArray(action.getRemoteInputs()));
        }
        if (action.getDataOnlyRemoteInputs() != null) {
            bundle.putParcelableArray("android.support.dataRemoteInputs", (Parcelable[])toBundleArray(action.getDataOnlyRemoteInputs()));
        }
        bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        return bundle;
    }
}
