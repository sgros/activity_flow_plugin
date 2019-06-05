package com.airbnb.lottie.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyPath {
    private final List<String> keys;
    private KeyPathElement resolvedElement;

    public KeyPath(String... strArr) {
        this.keys = Arrays.asList(strArr);
    }

    private KeyPath(KeyPath keyPath) {
        this.keys = new ArrayList(keyPath.keys);
        this.resolvedElement = keyPath.resolvedElement;
    }

    public KeyPath addKey(String str) {
        KeyPath keyPath = new KeyPath(this);
        keyPath.keys.add(str);
        return keyPath;
    }

    public KeyPath resolve(KeyPathElement keyPathElement) {
        KeyPath keyPath = new KeyPath(this);
        keyPath.resolvedElement = keyPathElement;
        return keyPath;
    }

    public KeyPathElement getResolvedElement() {
        return this.resolvedElement;
    }

    public boolean matches(String str, int i) {
        if (isContainer(str)) {
            return true;
        }
        if (i >= this.keys.size()) {
            return false;
        }
        if (((String) this.keys.get(i)).equals(str) || ((String) this.keys.get(i)).equals("**") || ((String) this.keys.get(i)).equals("*")) {
            return true;
        }
        return false;
    }

    public int incrementDepthBy(String str, int i) {
        if (isContainer(str)) {
            return 0;
        }
        if (!((String) this.keys.get(i)).equals("**")) {
            return 1;
        }
        if (i != this.keys.size() - 1 && ((String) this.keys.get(i + 1)).equals(str)) {
            return 2;
        }
        return 0;
    }

    public boolean fullyResolvesTo(String str, int i) {
        boolean z = false;
        if (i >= this.keys.size()) {
            return false;
        }
        Object obj = i == this.keys.size() - 1 ? 1 : null;
        String str2 = (String) this.keys.get(i);
        if (str2.equals("**")) {
            Object obj2 = (obj == null && ((String) this.keys.get(i + 1)).equals(str)) ? 1 : null;
            if (obj2 != null) {
                if (i == this.keys.size() - 2 || (i == this.keys.size() - 3 && endsWithGlobstar())) {
                    z = true;
                }
                return z;
            } else if (obj != null) {
                return true;
            } else {
                i++;
                if (i < this.keys.size() - 1) {
                    return false;
                }
                return ((String) this.keys.get(i)).equals(str);
            }
        }
        Object obj3 = (str2.equals(str) || str2.equals("*")) ? 1 : null;
        if ((obj != null || (i == this.keys.size() - 2 && endsWithGlobstar())) && obj3 != null) {
            z = true;
        }
        return z;
    }

    public boolean propagateToChildren(String str, int i) {
        boolean z = true;
        if (str.equals("__container")) {
            return true;
        }
        if (i >= this.keys.size() - 1 && !((String) this.keys.get(i)).equals("**")) {
            z = false;
        }
        return z;
    }

    private boolean isContainer(String str) {
        return str.equals("__container");
    }

    private boolean endsWithGlobstar() {
        return ((String) this.keys.get(this.keys.size() - 1)).equals("**");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("KeyPath{keys=");
        stringBuilder.append(this.keys);
        stringBuilder.append(",resolved=");
        stringBuilder.append(this.resolvedElement != null);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
