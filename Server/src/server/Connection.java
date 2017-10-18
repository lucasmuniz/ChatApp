package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import message.Message;

public class Connection extends Thread {

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Socket client;
	private ArrayList<Connection> connections = null;
	private String userName;

	public Connection(Socket cliente, ArrayList<Connection> connections) {
		try {
			this.connections = connections;
			this.client = cliente;
			this.inputStream = new ObjectInputStream(this.client.getInputStream());
			this.outputStream = new ObjectOutputStream(this.client.getOutputStream());
			this.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				System.out.println("esperando mensagem do client");
				Message message = (Message) this.inputStream.readObject();
				System.out.println("Servidor recebeu mensagem de: " + message.getUserName());
				this.userName = message.getUserName();
				boolean sendOnlyUser = message.getSendOnlyUser() != null;
				if(sendOnlyUser){
					sendMessageOnlyUser(message);
				}else{
					sendMessageAllConnections(message);
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				this.interrupt();
				this.stop();
			}
		}
	}

	private void sendMessageOnlyUser(Message message) throws IOException {
		for (Connection connection : this.connections) {
			if (!connection.client.isClosed()) {
				if(message.getSendOnlyUser().toString().equals(connection.userName)){
					connection.outputStream.writeObject(message);
					System.out.println("Enviado ao client: " + connection.getName());
				}
			} else {
				System.out.println("Client está desconectado");
				this.connections.remove(connection);
			}
			if (message.isUserDisconnected()) {
				System.out.println("Client está desconectado");
				this.connections.remove(connection);
				this.interrupt();
				this.stop();
			}
		}
		
	}

	private void sendMessageAllConnections(Message message) throws IOException {
		for (Connection connection : this.connections) {
			if (!connection.client.isClosed()) {
				sleep();
				List<String> usersName = getUsersConnecteds(message);
				message.setUsersConnecteds(usersName);
				

				connection.outputStream.writeObject(message);

				System.out.println("Enviado ao client: " + connection.getName());

			} else {
				System.out.println("Client está desconectado");
				this.connections.remove(connection);
			}
			if (message.isUserDisconnected()) {
				System.out.println("Client está desconectado");
				this.connections.remove(connection);
				this.interrupt();
				this.stop();
			}
		}
	}
	
	private void sleep() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			//
		}
	}

	private List<String> getUsersConnecteds(Message message) {
		ArrayList<String> usersName = new ArrayList<>();
		for (Connection connection : connections) {
			usersName.add(connection.userName);
		}
		message.setUsersConnecteds(usersName);

		return usersName;
	}
}
