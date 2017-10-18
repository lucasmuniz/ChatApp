package message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] fileContent;

	private String fileName;

	private String message;

	private String userName;

	private boolean userDisconnected;
	
	private List<String> usersConnecteds;
	
	private String sendOnlyUser;
	
	public Message(String userName, File file, String message, boolean userDisconnected, String sendOnlyUser) {
		if (!userDisconnected) {
			this.message = message;
			this.sendOnlyUser = sendOnlyUser;
			setFileContent(file);
		} else {
			this.userDisconnected = userDisconnected;
		}
		this.userName = userName;
	}
	
	public Message(){
		
	}

	public List<String> getUsersConnecteds() {
		return usersConnecteds;
	}

	public void setUsersConnecteds(List<String> usersConnecteds) {
		this.usersConnecteds = usersConnecteds;
	}
	
	public boolean isUserDisconnected() {
		return userDisconnected;
	}

	public void setUserDisconnected(boolean userDisconnected) {
		this.userDisconnected = userDisconnected;
	}

	private void setFileContent(File file) {
		if (file != null) {
			this.fileName = file.getName();
			byte[] fileBytes = new byte[(int) file.length()];
			FileInputStream fis;
			BufferedInputStream bis;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				bis.read(fileBytes, 0, fileBytes.length);
				setFileContent(fileBytes);
				bis.close();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isContentFile() {
		return this.fileContent != null;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getSendOnlyUser() {
		return sendOnlyUser;
	}

	public void setSendOnlyUser(String sendOnlyUser) {
		this.sendOnlyUser = sendOnlyUser;
	}
}
