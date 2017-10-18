package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerApp {
	private final static int PORT = 8080;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {

		ArrayList<Connection> connections = new ArrayList<>();
		try {
			ServerSocket listening = new ServerSocket(PORT);
			System.out.println("==> Servidor escutando (listen) na porta: " + PORT + "<==");
			while (true) {
				Socket client = listening.accept();
				System.out.println("Conexao aceita de (remoto): " + client.getRemoteSocketAddress());
				Connection conexao = new Connection(client, connections);	
				connections.add(conexao);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
