// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Locale;

public final class UrlTemplate
{
    private final int identifierCount;
    private final String[] identifierFormatTags;
    private final int[] identifiers;
    private final String[] urlPieces;
    
    private UrlTemplate(final String[] urlPieces, final int[] identifiers, final String[] identifierFormatTags, final int identifierCount) {
        this.urlPieces = urlPieces;
        this.identifiers = identifiers;
        this.identifierFormatTags = identifierFormatTags;
        this.identifierCount = identifierCount;
    }
    
    public static UrlTemplate compile(final String s) {
        final String[] array = new String[5];
        final int[] array2 = new int[4];
        final String[] array3 = new String[4];
        return new UrlTemplate(array, array2, array3, parseTemplate(s, array, array2, array3));
    }
    
    private static int parseTemplate(final String str, final String[] array, final int[] array2, final String[] array3) {
        array[0] = "";
        int i = 0;
        int n = 0;
        while (i < str.length()) {
            final int index = str.indexOf("$", i);
            final int n2 = -1;
            if (index == -1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(array[n]);
                sb.append(str.substring(i));
                array[n] = sb.toString();
                i = str.length();
            }
            else if (index != i) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(array[n]);
                sb2.append(str.substring(i, index));
                array[n] = sb2.toString();
                i = index;
            }
            else if (str.startsWith("$$", i)) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(array[n]);
                sb3.append("$");
                array[n] = sb3.toString();
                i += 2;
            }
            else {
                ++i;
                final int index2 = str.indexOf("$", i);
                String s = str.substring(i, index2);
                if (s.equals("RepresentationID")) {
                    array2[n] = 1;
                }
                else {
                    final int index3 = s.indexOf("%0");
                    String s2;
                    if (index3 != -1) {
                        final String str2 = s2 = s.substring(index3);
                        if (!str2.endsWith("d")) {
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append(str2);
                            sb4.append("d");
                            s2 = sb4.toString();
                        }
                        s = s.substring(0, index3);
                    }
                    else {
                        s2 = "%01d";
                    }
                    final int hashCode = s.hashCode();
                    int n3;
                    if (hashCode != -1950496919) {
                        if (hashCode != 2606829) {
                            if (hashCode != 38199441) {
                                n3 = n2;
                            }
                            else {
                                n3 = n2;
                                if (s.equals("Bandwidth")) {
                                    n3 = 1;
                                }
                            }
                        }
                        else {
                            n3 = n2;
                            if (s.equals("Time")) {
                                n3 = 2;
                            }
                        }
                    }
                    else {
                        n3 = n2;
                        if (s.equals("Number")) {
                            n3 = 0;
                        }
                    }
                    if (n3 != 0) {
                        if (n3 != 1) {
                            if (n3 != 2) {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("Invalid template: ");
                                sb5.append(str);
                                throw new IllegalArgumentException(sb5.toString());
                            }
                            array2[n] = 4;
                        }
                        else {
                            array2[n] = 3;
                        }
                    }
                    else {
                        array2[n] = 2;
                    }
                    array3[n] = s2;
                }
                ++n;
                array[n] = "";
                i = index2 + 1;
            }
        }
        return n;
    }
    
    public String buildUri(final String str, final long l, final int i, final long j) {
        final StringBuilder sb = new StringBuilder();
        int n = 0;
        int identifierCount;
        while (true) {
            identifierCount = this.identifierCount;
            if (n >= identifierCount) {
                break;
            }
            sb.append(this.urlPieces[n]);
            final int[] identifiers = this.identifiers;
            if (identifiers[n] == 1) {
                sb.append(str);
            }
            else if (identifiers[n] == 2) {
                sb.append(String.format(Locale.US, this.identifierFormatTags[n], l));
            }
            else if (identifiers[n] == 3) {
                sb.append(String.format(Locale.US, this.identifierFormatTags[n], i));
            }
            else if (identifiers[n] == 4) {
                sb.append(String.format(Locale.US, this.identifierFormatTags[n], j));
            }
            ++n;
        }
        sb.append(this.urlPieces[identifierCount]);
        return sb.toString();
    }
}
