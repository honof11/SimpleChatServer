package it.antonio.simplechat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SimpleClient extends Thread  {

	private ListenSocket listenSocket;
	private Socket socket;
	private SimpleServer server;
	public List<String> MessageHistoryReceived;
	public List<String> MessageHistorySend;

	public SimpleClient(Socket socket, SimpleServer server) {
		this.socket = socket;
		this.server = server;

		this.listenSocket = new ListenSocket(this.socket, this);
		this.MessageHistorySend = new ArrayList<String>();
		this.MessageHistoryReceived = new ArrayList<String>();
	}

	@Override
	public void run() {

		System.out.println("Connected to the chat server the client "+this);
		listenSocket.start();
	}

	public void sendMessage(String msg) {
		try {
			OutputStream output = this.socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(msg);
			MessageHistoryReceived.add(0,msg);

		} catch (IOException ex) {
			System.out.println("Error getting output stream: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void notifyMessageForBroadcast(String msg) {
		this.getServer().sendMessageToBroadcast(msg, this);
		this.MessageHistorySend.add(0,msg);
	}

	public SimpleServer getServer() {
		return server;
	}

	public void notifyExit() throws IOException {
		System.out.println("Exit from the chat server the client "+this);
		this.socket.close();
		this.server.disconnectClient(this);
				
	}

	
}
