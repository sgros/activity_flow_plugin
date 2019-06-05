// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.utils.exceptions;

import locus.api.android.utils.LocusUtils;

public class RequiredVersionMissingException extends Exception
{
    private static final long serialVersionUID = 1L;
    private String mistake;
    
    public RequiredVersionMissingException(final int n) {
        this(n, n);
    }
    
    public RequiredVersionMissingException(final int i, final int j) {
        super("Required version: Free (" + i + "), " + "or Pro (" + j + "), not installed!");
    }
    
    public RequiredVersionMissingException(final String s, final int i) {
        super(String.format("Required application: '%s', version: '%s', not installed", s, i));
    }
    
    public RequiredVersionMissingException(final LocusUtils.VersionCode versionCode) {
        super("Required version: Free (" + getVersionAsText(versionCode.vcFree) + "), or " + "Pro (" + getVersionAsText(versionCode.vcPro) + "), or " + "Gis (" + getVersionAsText(versionCode.vcGis) + "), not installed!");
    }
    
    private static String getVersionAsText(final int i) {
        String string;
        if (i == 0) {
            string = "Not supported";
        }
        else {
            string = Integer.toString(i);
        }
        return string;
    }
    
    public String getError() {
        return this.mistake;
    }
}
