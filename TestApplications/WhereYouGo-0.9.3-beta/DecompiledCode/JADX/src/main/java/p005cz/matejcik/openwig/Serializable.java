package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* renamed from: cz.matejcik.openwig.Serializable */
public interface Serializable {
    void deserialize(DataInputStream dataInputStream) throws IOException;

    void serialize(DataOutputStream dataOutputStream) throws IOException;
}
