package it.antonio.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ListenSocket extends Thread {

	private BufferedReader reader;
	private SimpleClient myClient;

	public ListenSocket(Socket socket, SimpleClient myClient) {

		this.myClient = myClient;

		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
		} catch (IOException ex) {

			System.out.println("Error getting input stream: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean running = true;
		while (running) {
			try {
				String response = reader.readLine();

				System.out.println("\n message:" + response + " from: " + this.myClient);

				if (response != null && !(response.trim().equals("exit"))) {
					this.getMyClient().notifyMessageForBroadcast(response);
				} else {
					running = false;
				}

			} catch (IOException ex) {
				System.out.println("Error reading from server: " + ex.getMessage());
				ex.printStackTrace();
				break;
			}
		}
		try {
			this.myClient.notifyExit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SimpleClient getMyClient() {
		return myClient;
	}
}
