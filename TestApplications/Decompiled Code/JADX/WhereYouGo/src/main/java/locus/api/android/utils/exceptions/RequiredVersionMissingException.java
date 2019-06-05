package locus.api.android.utils.exceptions;

import locus.api.android.utils.LocusUtils.VersionCode;

public class RequiredVersionMissingException extends Exception {
    private static final long serialVersionUID = 1;
    private String mistake;

    public RequiredVersionMissingException(int version) {
        this(version, version);
    }

    public RequiredVersionMissingException(int versionFree, int versionPro) {
        super("Required version: Free (" + versionFree + "), " + "or Pro (" + versionPro + "), not installed!");
    }

    public RequiredVersionMissingException(VersionCode vc) {
        super("Required version: Free (" + getVersionAsText(vc.vcFree) + "), or " + "Pro (" + getVersionAsText(vc.vcPro) + "), or " + "Gis (" + getVersionAsText(vc.vcGis) + "), not installed!");
    }

    private static String getVersionAsText(int code) {
        if (code == 0) {
            return "Not supported";
        }
        return Integer.toString(code);
    }

    public RequiredVersionMissingException(String packageName, int version) {
        super(String.format("Required application: '%s', version: '%s', not installed", new Object[]{packageName, Integer.valueOf(version)}));
    }

    public String getError() {
        return this.mistake;
    }
}
