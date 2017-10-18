package message.format;

public class MessageUtils {

	public static String formatMessage(String userName, String message){
		return "\n"+ userName + " escreveu: " + message;
	}
}
