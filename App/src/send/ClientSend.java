package send;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import message.Message;
public class ClientSend extends Thread {

	private ObjectOutputStream objectOutput = null;

	public static Message message;

	public ClientSend(Socket socket) {
		try {
			this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) { 
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				//
			}
			try {
				if(ClientSend.message != null){
					System.out.println("Enviando message");
					this.objectOutput.writeObject(ClientSend.message);
					System.out.println("enviado para o server");
					ClientSend.message = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
