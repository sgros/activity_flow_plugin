package android.arch.lifecycle;

import android.app.Application;
import java.lang.reflect.InvocationTargetException;

public class ViewModelProvider {
    private final Factory mFactory;
    private final ViewModelStore mViewModelStore;

    public interface Factory {
        <T extends ViewModel> T create(Class<T> cls);
    }

    public static class NewInstanceFactory implements Factory {
        public <T extends ViewModel> T create(Class<T> cls) {
            StringBuilder stringBuilder;
            try {
                return (ViewModel) cls.newInstance();
            } catch (InstantiationException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot create an instance of ");
                stringBuilder.append(cls);
                throw new RuntimeException(stringBuilder.toString(), e);
            } catch (IllegalAccessException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot create an instance of ");
                stringBuilder.append(cls);
                throw new RuntimeException(stringBuilder.toString(), e2);
            }
        }
    }

    public static class AndroidViewModelFactory extends NewInstanceFactory {
        private static AndroidViewModelFactory sInstance;
        private Application mApplication;

        public static AndroidViewModelFactory getInstance(Application application) {
            if (sInstance == null) {
                sInstance = new AndroidViewModelFactory(application);
            }
            return sInstance;
        }

        public AndroidViewModelFactory(Application application) {
            this.mApplication = application;
        }

        public <T extends ViewModel> T create(Class<T> cls) {
            StringBuilder stringBuilder;
            if (!AndroidViewModel.class.isAssignableFrom(cls)) {
                return super.create(cls);
            }
            try {
                return (ViewModel) cls.getConstructor(new Class[]{Application.class}).newInstance(new Object[]{this.mApplication});
            } catch (NoSuchMethodException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot create an instance of ");
                stringBuilder.append(cls);
                throw new RuntimeException(stringBuilder.toString(), e);
            } catch (IllegalAccessException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot create an instance of ");
                stringBuilder.append(cls);
                throw new RuntimeException(stringBuilder.toString(), e2);
            } catch (InstantiationException e3) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot create an instance of ");
                stringBuilder.append(cls);
                throw new RuntimeException(stringBuilder.toString(), e3);
            } catch (InvocationTargetException e4) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot create an instance of ");
                stringBuilder.append(cls);
                throw new RuntimeException(stringBuilder.toString(), e4);
            }
        }
    }

    public ViewModelProvider(ViewModelStore viewModelStore, Factory factory) {
        this.mFactory = factory;
        this.mViewModelStore = viewModelStore;
    }

    public <T extends ViewModel> T get(Class<T> cls) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("android.arch.lifecycle.ViewModelProvider.DefaultKey:");
            stringBuilder.append(canonicalName);
            return get(stringBuilder.toString(), cls);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    public <T extends ViewModel> T get(String str, Class<T> cls) {
        ViewModel viewModel = this.mViewModelStore.get(str);
        if (cls.isInstance(viewModel)) {
            return viewModel;
        }
        ViewModel create = this.mFactory.create(cls);
        this.mViewModelStore.put(str, create);
        return create;
    }
}
