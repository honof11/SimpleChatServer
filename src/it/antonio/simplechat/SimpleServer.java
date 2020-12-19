package it.antonio.simplechat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SimpleServer extends Thread {

	private boolean run=true;
	private int port;
	private List<SimpleClient> clientsList;

	public List<SimpleClient> getClientsList() {
		return clientsList;
	}

	public void setClientsList(List<SimpleClient> clientsList) {
		this.clientsList = clientsList;
	}

	public SimpleServer(int port) {
		this.port = port;
		this.clientsList = new ArrayList<>();
	}

	public void run() {
		 
		try (ServerSocket serverSocket = new ServerSocket(port)) 
		{

			System.out.println("Server is up and listening on port " + port);

			while (run) {
				Socket socket = serverSocket.accept();
				System.out.println("New user connected " + socket);

				SimpleClient client = new SimpleClient(socket, this);
				clientsList.add(client);
				client.start();
			}
			
			System.out.println("Server is stopping on port " + port);
			
		} catch (IOException ex) {
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	
	public void stopping()
	{
		this.setRun(false);
	}
	
	public boolean isRun() {
		return run;
	}

	private void setRun(boolean run) {
		this.run = run;
	}

	public static void main(String[] args) {

		int port = 10000;

		SimpleServer server = new SimpleServer(port);
		server.start();
	}

	public void sendMessageToBroadcast(String msg, SimpleClient clientSender) {
		for (SimpleClient client : this.clientsList) {
			if (client != clientSender) {
				client.sendMessage(msg);
			}
		}
	}

	public void disconnectClient(SimpleClient clientDisconnect) {
				clientsList.remove(clientDisconnect);
		}
	}

