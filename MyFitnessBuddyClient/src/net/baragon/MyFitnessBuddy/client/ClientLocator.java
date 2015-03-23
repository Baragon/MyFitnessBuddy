package net.baragon.MyFitnessBuddy.client;


public class ClientLocator {
    private static Client instance;

    public static Client getClient() {
        return instance;
    }

    public static void setClient(Client instance) {
        ClientLocator.instance = instance;
    }
}
