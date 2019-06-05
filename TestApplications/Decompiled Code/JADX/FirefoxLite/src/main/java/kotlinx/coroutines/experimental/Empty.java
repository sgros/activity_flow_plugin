package kotlinx.coroutines.experimental;

/* compiled from: JobSupport.kt */
final class Empty implements Incomplete {
    private final boolean isActive;

    public NodeList getList() {
        return null;
    }

    public Empty(boolean z) {
        this.isActive = z;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Empty{");
        stringBuilder.append(isActive() ? "Active" : "New");
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
