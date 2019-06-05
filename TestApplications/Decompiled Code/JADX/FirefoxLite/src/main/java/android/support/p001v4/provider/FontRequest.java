package android.support.p001v4.provider;

import android.support.p001v4.util.Preconditions;
import android.util.Base64;
import java.util.List;

/* renamed from: android.support.v4.provider.FontRequest */
public final class FontRequest {
    private final List<List<byte[]>> mCertificates;
    private final int mCertificatesArray = 0;
    private final String mIdentifier;
    private final String mProviderAuthority;
    private final String mProviderPackage;
    private final String mQuery;

    public FontRequest(String str, String str2, String str3, List<List<byte[]>> list) {
        this.mProviderAuthority = (String) Preconditions.checkNotNull(str);
        this.mProviderPackage = (String) Preconditions.checkNotNull(str2);
        this.mQuery = (String) Preconditions.checkNotNull(str3);
        this.mCertificates = (List) Preconditions.checkNotNull(list);
        StringBuilder stringBuilder = new StringBuilder(this.mProviderAuthority);
        stringBuilder.append("-");
        stringBuilder.append(this.mProviderPackage);
        stringBuilder.append("-");
        stringBuilder.append(this.mQuery);
        this.mIdentifier = stringBuilder.toString();
    }

    public String getProviderAuthority() {
        return this.mProviderAuthority;
    }

    public String getProviderPackage() {
        return this.mProviderPackage;
    }

    public String getQuery() {
        return this.mQuery;
    }

    public List<List<byte[]>> getCertificates() {
        return this.mCertificates;
    }

    public int getCertificatesArrayResId() {
        return this.mCertificatesArray;
    }

    public String getIdentifier() {
        return this.mIdentifier;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("FontRequest {mProviderAuthority: ");
        stringBuilder2.append(this.mProviderAuthority);
        stringBuilder2.append(", mProviderPackage: ");
        stringBuilder2.append(this.mProviderPackage);
        stringBuilder2.append(", mQuery: ");
        stringBuilder2.append(this.mQuery);
        stringBuilder2.append(", mCertificates:");
        stringBuilder.append(stringBuilder2.toString());
        for (int i = 0; i < this.mCertificates.size(); i++) {
            stringBuilder.append(" [");
            List list = (List) this.mCertificates.get(i);
            for (int i2 = 0; i2 < list.size(); i2++) {
                stringBuilder.append(" \"");
                stringBuilder.append(Base64.encodeToString((byte[]) list.get(i2), 0));
                stringBuilder.append("\"");
            }
            stringBuilder.append(" ]");
        }
        stringBuilder.append("}");
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("mCertificatesArray: ");
        stringBuilder2.append(this.mCertificatesArray);
        stringBuilder.append(stringBuilder2.toString());
        return stringBuilder.toString();
    }
}
