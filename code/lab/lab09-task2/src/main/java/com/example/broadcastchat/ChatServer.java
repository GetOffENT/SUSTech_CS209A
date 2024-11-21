package com.example.broadcastchat;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ChatServer {
    public static void main(String[] args) {
        Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server started ......");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected.");

                ClientHandler clientHandler = new ClientHandler(socket, clients);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Set<ClientHandler> clients;
    private String clientName;

    public ClientHandler(Socket socket, Set<ClientHandler> clients) throws IOException {
        this.socket = socket;
        this.clients = clients;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    @Override
    public void run() {
        try {
            clientName = in.readLine();
            broadcast(clientName + " joined the chat!", this);

            String message;
            while ((message = in.readLine()) != null) {
                broadcast(clientName + ": " + message, this);

                    HttpClient client = HttpClient.newHttpClient();

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + message))
                            .GET()
                            .build();

                    // 发送请求并处理响应
                    try {
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                        // 检查响应状态码是否为200 (OK)
                        if (response.statusCode() == 200) {
                            // 输出响应体内容
                            JSONObject jsonObject = JSONObject.parseObject(response.body());
                            this.sendMessage(jsonObject.get("height").toString());
                            this.sendMessage(jsonObject.get("weight").toString());
                            this.sendMessage(jsonObject.get("abilities").toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


            }
        } catch (IOException e) {
            System.out.println(clientName + " disconnected");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clients.remove(this);
            broadcast(clientName + " left the chat!", this);
        }
    }
}
