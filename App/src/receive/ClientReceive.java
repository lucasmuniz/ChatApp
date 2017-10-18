package receive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import message.Message;
import message.format.MessageUtils;
import window.forms.ClientForms;

public class ClientReceive extends Thread {

	private ObjectInputStream input = null;

	public ClientReceive(Socket socket) {
		try {
			this.input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void run() {
		while (true) {
			try {
				Message message = (Message) this.input.readObject();
				if(message.getUsersConnecteds() != null){
					for (String userName : message.getUsersConnecteds()) {
						if(!ClientForms.connecteds.contains(userName)){
							ClientForms.connecteds.addElement(userName);
						}
					}
				}
				if(message.isUserDisconnected()){
					ClientForms.txtMessages.append("\nO usuário: " + message.getUserName() + " desconectou");
					ClientForms.connecteds.removeElement(message.getUserName());
				}
				if (message.getFileContent() != null && message.getUserName() != ClientForms.txtUserName.getText()) {
					File file = new File(System.getProperty("user.dir")+System.getProperty("file.separator") + message.getFileName());
					FileOutputStream in = new FileOutputStream(file);
					in.write(message.getFileContent());
					in.close();
					ClientForms.txtMessages.append("\nO usuário: " + message.getUserName() + " enviou um arquivo");
					ClientForms.txtMessages.append("\n"+message.getFileName() +" salvo com sucesso em: "+ System.getProperty("user.dir")+System.getProperty("file.separator"));
				}
				if (message.getMessage() != null && !message.getMessage().isEmpty()) {
					ClientForms.txtMessages
							.append(MessageUtils.formatMessage(message.getUserName(), message.getMessage()));
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
