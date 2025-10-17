package Network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.assignment1task2.AndroidGameController;

import SequenceDice.GameEvent;
import SequenceDice.GameEventType;
import SequenceDice.GameModel;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int clientId;
    private int gameId;
    private GameModel model;
    private Context context;
    private AndroidGameController controller;


    public Client(Context context, String host, int port, int numberOfPlayers) throws IOException {
        ClientManager.setClient(this);
        this.context = context;

        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Receive assigned client ID from server
        String line = in.readLine();
        if (line != null && line.startsWith("ID|")) {
            clientId = Integer.parseInt(line.split("\\|")[1]);
            System.out.println("Connected with client ID: " + clientId);
        }

        out.println("NUMOFPLAYERS|1|" + numberOfPlayers);
        // Start listener thread for incoming messages
        new Thread(this::listen).start();
    }

    @Override
    public void run() {
        // Nothing fancy — main socket loop is handled in listen()
        listen();
    }

    public void joinGameQueue(int numberOfPlayers) {
        out.println("NUMOFPLAYERS|1|" + numberOfPlayers);
        System.out.println("Queued for " + numberOfPlayers + "-player game.");
    }

    public void subscribe(int topic) {
        out.println("SUBSCRIBE|" + topic + "|");
    }

    public void publish(int topic, GameEvent event) {
        try {
            String serialized = Serializer.serializeToString(event);
            out.println("PUBLISH|" + topic + "|" + serialized);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (!line.startsWith("MESSAGE|")) continue;

                String[] parts = line.split("\\|", 3);
                String message = parts[2];
                Object o = Serializer.deserializeFromString(message);

                if (o instanceof GameEvent) {
                    GameEvent event = (GameEvent) o;
                    Log.d("CLIENT", event.getType().toString());
                    handleGameEvent(parts, event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGameEvent(String[] parts, GameEvent event) {
        if (event.getType().equals(GameEventType.START_GAME)) {
            gameId = Integer.parseInt(parts[1]);
            int[] clientIds = (int[]) event.getCargo();
            int playerNumber = -1;

            for (int i = 0; i < clientIds.length; i++) {
                if (clientIds[i] == clientId) {
                    playerNumber = i + 1;
                    break;
                }
            }

            int numOfPlayers = clientIds.length;

            // Launch AndroidGameController safely with Intent
            Intent intent = new Intent(context, AndroidGameController.class);
            intent.putExtra("gameId", gameId);
            intent.putExtra("playerNumber", playerNumber);
            intent.putExtra("numOfPlayers", numOfPlayers);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }
        else {
            controller.onGameEvent(event);
        }
    }

    public int getClientId() {
        return clientId;
    }

    public void setController(AndroidGameController controller){this.controller = controller;}

}
