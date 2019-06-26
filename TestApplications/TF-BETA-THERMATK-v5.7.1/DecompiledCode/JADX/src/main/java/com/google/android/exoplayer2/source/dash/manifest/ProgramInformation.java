package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.util.Util;

public class ProgramInformation {
    public final String copyright;
    public final String lang;
    public final String moreInformationURL;
    public final String source;
    public final String title;

    public ProgramInformation(String str, String str2, String str3, String str4, String str5) {
        this.title = str;
        this.source = str2;
        this.copyright = str3;
        this.moreInformationURL = str4;
        this.lang = str5;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || ProgramInformation.class != obj.getClass()) {
            return false;
        }
        ProgramInformation programInformation = (ProgramInformation) obj;
        if (!(Util.areEqual(this.title, programInformation.title) && Util.areEqual(this.source, programInformation.source) && Util.areEqual(this.copyright, programInformation.copyright) && Util.areEqual(this.moreInformationURL, programInformation.moreInformationURL) && Util.areEqual(this.lang, programInformation.lang))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.title;
        int i = 0;
        int hashCode = (527 + (str != null ? str.hashCode() : 0)) * 31;
        str = this.source;
        hashCode = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        str = this.copyright;
        hashCode = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        str = this.moreInformationURL;
        hashCode = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        str = this.lang;
        if (str != null) {
            i = str.hashCode();
        }
        return hashCode + i;
    }
}
