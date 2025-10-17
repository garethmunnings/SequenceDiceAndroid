package Network;

import java.io.*;
import java.util.Base64;

public class Serializer {

    public static String serializeToString(Serializable obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
        }
        // Convert binary bytes → Base64 string
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static Object deserializeFromString(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ois.readObject();
        }
    }
}
