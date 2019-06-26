// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.content.res.XmlResourceParser;
import android.util.Xml;
import android.content.res.Resources$NotFoundException;
import android.content.res.TypedArray;
import android.support.v4.content.res.TypedArrayUtils;
import android.view.ViewGroup;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.view.InflateException;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.content.Context;
import java.lang.reflect.Constructor;
import android.support.v4.util.ArrayMap;

public class TransitionInflater
{
    private static final ArrayMap<String, Constructor> CONSTRUCTORS;
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE;
    private final Context mContext;
    
    static {
        CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class };
        CONSTRUCTORS = new ArrayMap<String, Constructor>();
    }
    
    private TransitionInflater(@NonNull final Context mContext) {
        this.mContext = mContext;
    }
    
    private Object createCustom(final AttributeSet set, final Class clazz, final String str) {
        final String attributeValue = set.getAttributeValue((String)null, "class");
        if (attributeValue == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" tag must have a 'class' attribute");
            throw new InflateException(sb.toString());
        }
        try {
            synchronized (TransitionInflater.CONSTRUCTORS) {
                Constructor<?> constructor2;
                final Constructor<Object> constructor = (Constructor<Object>)(constructor2 = TransitionInflater.CONSTRUCTORS.get(attributeValue));
                if (constructor == null) {
                    final Class<?> subclass = this.mContext.getClassLoader().loadClass(attributeValue).asSubclass((Class<Object>)clazz);
                    constructor2 = constructor;
                    if (subclass != null) {
                        constructor2 = subclass.getConstructor(TransitionInflater.CONSTRUCTOR_SIGNATURE);
                        constructor2.setAccessible(true);
                        TransitionInflater.CONSTRUCTORS.put(attributeValue, constructor2);
                    }
                }
                return constructor2.newInstance(this.mContext, set);
            }
        }
        catch (Exception ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not instantiate ");
            sb2.append(clazz);
            sb2.append(" class ");
            sb2.append(attributeValue);
            throw new InflateException(sb2.toString(), (Throwable)ex);
        }
    }
    
    private Transition createTransitionFromXml(final XmlPullParser xmlPullParser, final AttributeSet set, final Transition transition) throws XmlPullParserException, IOException {
        final int depth = xmlPullParser.getDepth();
        TransitionSet set2;
        if (transition instanceof TransitionSet) {
            set2 = (TransitionSet)transition;
        }
        else {
            set2 = null;
        }
        while (true) {
            Transition transition2 = null;
            while (true) {
                final int next = xmlPullParser.next();
                if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                    return transition2;
                }
                if (next != 2) {
                    continue;
                }
                final String name = xmlPullParser.getName();
                Transition transition3;
                if ("fade".equals(name)) {
                    transition3 = new Fade(this.mContext, set);
                }
                else if ("changeBounds".equals(name)) {
                    transition3 = new ChangeBounds(this.mContext, set);
                }
                else if ("slide".equals(name)) {
                    transition3 = new Slide(this.mContext, set);
                }
                else if ("explode".equals(name)) {
                    transition3 = new Explode(this.mContext, set);
                }
                else if ("changeImageTransform".equals(name)) {
                    transition3 = new ChangeImageTransform(this.mContext, set);
                }
                else if ("changeTransform".equals(name)) {
                    transition3 = new ChangeTransform(this.mContext, set);
                }
                else if ("changeClipBounds".equals(name)) {
                    transition3 = new ChangeClipBounds(this.mContext, set);
                }
                else if ("autoTransition".equals(name)) {
                    transition3 = new AutoTransition(this.mContext, set);
                }
                else if ("changeScroll".equals(name)) {
                    transition3 = new ChangeScroll(this.mContext, set);
                }
                else if ("transitionSet".equals(name)) {
                    transition3 = new TransitionSet(this.mContext, set);
                }
                else if ("transition".equals(name)) {
                    transition3 = (Transition)this.createCustom(set, Transition.class, "transition");
                }
                else if ("targets".equals(name)) {
                    this.getTargetIds(xmlPullParser, set, transition);
                    transition3 = transition2;
                }
                else if ("arcMotion".equals(name)) {
                    if (transition == null) {
                        throw new RuntimeException("Invalid use of arcMotion element");
                    }
                    transition.setPathMotion(new ArcMotion(this.mContext, set));
                    transition3 = transition2;
                }
                else if ("pathMotion".equals(name)) {
                    if (transition == null) {
                        throw new RuntimeException("Invalid use of pathMotion element");
                    }
                    transition.setPathMotion((PathMotion)this.createCustom(set, PathMotion.class, "pathMotion"));
                    transition3 = transition2;
                }
                else {
                    if (!"patternPathMotion".equals(name)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown scene name: ");
                        sb.append(xmlPullParser.getName());
                        throw new RuntimeException(sb.toString());
                    }
                    if (transition == null) {
                        throw new RuntimeException("Invalid use of patternPathMotion element");
                    }
                    transition.setPathMotion(new PatternPathMotion(this.mContext, set));
                    transition3 = transition2;
                }
                transition2 = transition3;
                if (transition3 == null) {
                    continue;
                }
                if (!xmlPullParser.isEmptyElementTag()) {
                    this.createTransitionFromXml(xmlPullParser, set, transition3);
                }
                if (set2 != null) {
                    set2.addTransition(transition3);
                    break;
                }
                transition2 = transition3;
                if (transition != null) {
                    throw new InflateException("Could not add transition to another transition.");
                }
            }
        }
    }
    
    private TransitionManager createTransitionManagerFromXml(final XmlPullParser xmlPullParser, final AttributeSet set, final ViewGroup viewGroup) throws XmlPullParserException, IOException {
        final int depth = xmlPullParser.getDepth();
        TransitionManager transitionManager = null;
        while (true) {
            final int next = xmlPullParser.next();
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                return transitionManager;
            }
            if (next != 2) {
                continue;
            }
            final String name = xmlPullParser.getName();
            if (name.equals("transitionManager")) {
                transitionManager = new TransitionManager();
            }
            else {
                if (!name.equals("transition") || transitionManager == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown scene name: ");
                    sb.append(xmlPullParser.getName());
                    throw new RuntimeException(sb.toString());
                }
                this.loadTransition(set, xmlPullParser, viewGroup, transitionManager);
            }
        }
    }
    
    public static TransitionInflater from(final Context context) {
        return new TransitionInflater(context);
    }
    
    private void getTargetIds(final XmlPullParser xmlPullParser, final AttributeSet set, final Transition transition) throws XmlPullParserException, IOException {
        final int depth = xmlPullParser.getDepth();
        Label_0311: {
            TypedArray obtainStyledAttributes;
            String str;
            while (true) {
                final int next = xmlPullParser.next();
                if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                    return;
                }
                if (next != 2) {
                    continue;
                }
                if (!xmlPullParser.getName().equals("target")) {
                    break Label_0311;
                }
                obtainStyledAttributes = this.mContext.obtainStyledAttributes(set, Styleable.TRANSITION_TARGET);
                final int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, xmlPullParser, "targetId", 1, 0);
                Label_0263: {
                    if (namedResourceId != 0) {
                        transition.addTarget(namedResourceId);
                    }
                    else {
                        final int namedResourceId2 = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, xmlPullParser, "excludeId", 2, 0);
                        if (namedResourceId2 != 0) {
                            transition.excludeTarget(namedResourceId2, true);
                        }
                        else {
                            final String namedString = TypedArrayUtils.getNamedString(obtainStyledAttributes, xmlPullParser, "targetName", 4);
                            if (namedString != null) {
                                transition.addTarget(namedString);
                            }
                            else {
                                final String namedString2 = TypedArrayUtils.getNamedString(obtainStyledAttributes, xmlPullParser, "excludeName", 5);
                                if (namedString2 != null) {
                                    transition.excludeTarget(namedString2, true);
                                }
                                else {
                                    final String namedString3 = TypedArrayUtils.getNamedString(obtainStyledAttributes, xmlPullParser, "excludeClass", 3);
                                    if (namedString3 != null) {
                                        try {
                                            transition.excludeTarget(Class.forName(namedString3), true);
                                            break Label_0263;
                                        }
                                        catch (ClassNotFoundException cause) {
                                            str = namedString3;
                                            break;
                                        }
                                    }
                                    final String namedString4 = TypedArrayUtils.getNamedString(obtainStyledAttributes, xmlPullParser, "targetClass", 0);
                                    if (namedString4 != null) {
                                        try {
                                            transition.addTarget(Class.forName(namedString4));
                                        }
                                        catch (ClassNotFoundException cause) {
                                            str = namedString4;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                obtainStyledAttributes.recycle();
            }
            obtainStyledAttributes.recycle();
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not create ");
            sb.append(str);
            final ClassNotFoundException cause;
            throw new RuntimeException(sb.toString(), cause);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unknown scene name: ");
        sb2.append(xmlPullParser.getName());
        throw new RuntimeException(sb2.toString());
    }
    
    private void loadTransition(final AttributeSet set, final XmlPullParser xmlPullParser, final ViewGroup viewGroup, final TransitionManager transitionManager) throws Resources$NotFoundException {
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(set, Styleable.TRANSITION_MANAGER);
        final int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, xmlPullParser, "transition", 2, -1);
        final int namedResourceId2 = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, xmlPullParser, "fromScene", 0, -1);
        final Scene scene = null;
        Scene sceneForLayout;
        if (namedResourceId2 < 0) {
            sceneForLayout = null;
        }
        else {
            sceneForLayout = Scene.getSceneForLayout(viewGroup, namedResourceId2, this.mContext);
        }
        final int namedResourceId3 = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, xmlPullParser, "toScene", 1, -1);
        Scene sceneForLayout2;
        if (namedResourceId3 < 0) {
            sceneForLayout2 = scene;
        }
        else {
            sceneForLayout2 = Scene.getSceneForLayout(viewGroup, namedResourceId3, this.mContext);
        }
        if (namedResourceId >= 0) {
            final Transition inflateTransition = this.inflateTransition(namedResourceId);
            if (inflateTransition != null) {
                if (sceneForLayout2 == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("No toScene for transition ID ");
                    sb.append(namedResourceId);
                    throw new RuntimeException(sb.toString());
                }
                if (sceneForLayout == null) {
                    transitionManager.setTransition(sceneForLayout2, inflateTransition);
                }
                else {
                    transitionManager.setTransition(sceneForLayout, sceneForLayout2, inflateTransition);
                }
            }
        }
        obtainStyledAttributes.recycle();
    }
    
    public Transition inflateTransition(final int n) {
        final XmlResourceParser xml = this.mContext.getResources().getXml(n);
        try {
            try {
                final Transition transitionFromXml = this.createTransitionFromXml((XmlPullParser)xml, Xml.asAttributeSet((XmlPullParser)xml), null);
                xml.close();
                return transitionFromXml;
            }
            finally {}
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append(xml.getPositionDescription());
            sb.append(": ");
            sb.append(ex.getMessage());
            throw new InflateException(sb.toString(), (Throwable)ex);
        }
        catch (XmlPullParserException ex2) {
            throw new InflateException(ex2.getMessage(), (Throwable)ex2);
        }
        xml.close();
    }
    
    public TransitionManager inflateTransitionManager(final int n, final ViewGroup viewGroup) {
        final XmlResourceParser xml = this.mContext.getResources().getXml(n);
        try {
            try {
                final TransitionManager transitionManagerFromXml = this.createTransitionManagerFromXml((XmlPullParser)xml, Xml.asAttributeSet((XmlPullParser)xml), viewGroup);
                xml.close();
                return transitionManagerFromXml;
            }
            finally {}
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append(xml.getPositionDescription());
            sb.append(": ");
            sb.append(ex.getMessage());
            final InflateException ex2 = new InflateException(sb.toString());
            ex2.initCause((Throwable)ex);
            throw ex2;
        }
        catch (XmlPullParserException ex4) {
            final InflateException ex3 = new InflateException(ex4.getMessage());
            ex3.initCause((Throwable)ex4);
            throw ex3;
        }
        xml.close();
    }
}
