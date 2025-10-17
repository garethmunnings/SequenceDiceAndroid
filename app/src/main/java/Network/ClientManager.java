/**
 * Author: Gareth Munnings
 * Created on 2025/10/17
 */

package Network;

public class ClientManager {
    private static Client client;
    public static void setClient(Client c) { client = c; }
    public static Client getClient() { return client; }
}
