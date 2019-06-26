// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.util.Util;

public class ProgramInformation
{
    public final String copyright;
    public final String lang;
    public final String moreInformationURL;
    public final String source;
    public final String title;
    
    public ProgramInformation(final String title, final String source, final String copyright, final String moreInformationURL, final String lang) {
        this.title = title;
        this.source = source;
        this.copyright = copyright;
        this.moreInformationURL = moreInformationURL;
        this.lang = lang;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && ProgramInformation.class == o.getClass()) {
            final ProgramInformation programInformation = (ProgramInformation)o;
            if (!Util.areEqual(this.title, programInformation.title) || !Util.areEqual(this.source, programInformation.source) || !Util.areEqual(this.copyright, programInformation.copyright) || !Util.areEqual(this.moreInformationURL, programInformation.moreInformationURL) || !Util.areEqual(this.lang, programInformation.lang)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final String title = this.title;
        int hashCode = 0;
        int hashCode2;
        if (title != null) {
            hashCode2 = title.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String source = this.source;
        int hashCode3;
        if (source != null) {
            hashCode3 = source.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String copyright = this.copyright;
        int hashCode4;
        if (copyright != null) {
            hashCode4 = copyright.hashCode();
        }
        else {
            hashCode4 = 0;
        }
        final String moreInformationURL = this.moreInformationURL;
        int hashCode5;
        if (moreInformationURL != null) {
            hashCode5 = moreInformationURL.hashCode();
        }
        else {
            hashCode5 = 0;
        }
        final String lang = this.lang;
        if (lang != null) {
            hashCode = lang.hashCode();
        }
        return ((((527 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode;
    }
}
