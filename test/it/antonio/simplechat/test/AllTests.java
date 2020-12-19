package it.antonio.simplechat.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import it.antonio.simplechat.SimpleServer;

@RunWith(Suite.class)
@SuiteClasses({})
public class AllTests {

	private final static int SERVER_PORT = 10000;
	private final static String SERVER_NAME = "localhost";

	@Test
	public void testSendAndReceptionMsg() throws InterruptedException, IOException {

		String msgToSendC1 = "ABCD";
		String msgToSendC2 = "DCBA";
		String msgToReceived = "ABCD";

		SimpleServer server = new SimpleServer(SERVER_PORT);
		server.start();

		// C1 Client
		Socket socketC1 = new Socket(SERVER_NAME, SERVER_PORT);
		OutputStream outputStreamC1 = socketC1.getOutputStream();
		DataOutputStream dataOutputStreamC1 = new DataOutputStream(outputStreamC1);
		dataOutputStreamC1.writeUTF(msgToSendC1);
		dataOutputStreamC1.flush();

		TimeUnit.SECONDS.sleep(1);

		// C2 Client
		Socket socketC2 = new Socket(SERVER_NAME, SERVER_PORT);
		OutputStream outputStreamC2 = socketC2.getOutputStream();
		DataOutputStream dataOutputStreamC2 = new DataOutputStream(outputStreamC2);
		dataOutputStreamC2.writeUTF(msgToSendC2);
		dataOutputStreamC2.flush();

		TimeUnit.SECONDS.sleep(1);

		assertTrue(!server.getClientsList().isEmpty());
		assertTrue(server.getClientsList().size() == 2);

		// C1 exit
		dataOutputStreamC1.close();

		// get the string received from C2
		String a = server.getClientsList().get(0).MessageHistoryReceived.get(0);
		assertTrue(a.equals(msgToSendC1));

		// C2 exit
		dataOutputStreamC2.close();


		server.stopping();
	}
	
	
	
	@Test
	public void testDisconnectClient() throws InterruptedException, IOException {

		String msgToSendC1 = "ABCD";
		String msgToSendC2 = "DCBA";
		String msgToReceived = "ABCD";

		SimpleServer server = new SimpleServer(SERVER_PORT);
		server.start();

		// C1 Client
		Socket socketC1 = new Socket(SERVER_NAME, SERVER_PORT);
		OutputStream outputStreamC1 = socketC1.getOutputStream();
		DataOutputStream dataOutputStreamC1 = new DataOutputStream(outputStreamC1);
		dataOutputStreamC1.writeUTF(msgToSendC1);
		dataOutputStreamC1.flush();

		TimeUnit.SECONDS.sleep(1);

		// C2 Client
		Socket socketC2 = new Socket(SERVER_NAME, SERVER_PORT);
		OutputStream outputStreamC2 = socketC2.getOutputStream();
		DataOutputStream dataOutputStreamC2 = new DataOutputStream(outputStreamC2);
		dataOutputStreamC2.writeUTF(msgToSendC2);
		dataOutputStreamC2.flush();

		TimeUnit.SECONDS.sleep(1);

		assertTrue(!server.getClientsList().isEmpty());
		assertTrue(server.getClientsList().size() == 2);

		// C1 exit
		dataOutputStreamC1.close();
		// C2 exit
		dataOutputStreamC2.close();

		TimeUnit.SECONDS.sleep(2);
		assertTrue(server.getClientsList().isEmpty());
		
		server.stopping();
	}
}
