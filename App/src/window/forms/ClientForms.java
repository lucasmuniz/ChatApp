package window.forms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import message.Message;
import message.format.MessageUtils;
import receive.ClientReceive;
import send.ClientSend;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.JSeparator;
import java.awt.Font;
import javax.swing.JTextArea;

public class ClientForms {

	private JFrame principalFrame;
	private JTextField txtMessage;
	public static JTextField txtUserName;
	private Socket socket;
	private Thread processClientSend;
	private Thread processClientReceive;
	private JButton btnDisconnect = new JButton("Desconectar");
	private File selectedFile = null;
	public static JTextArea txtMessages;
	@SuppressWarnings("rawtypes")
	public static DefaultListModel connecteds = new DefaultListModel();
	@SuppressWarnings("rawtypes")
	public JList listUsersConnected;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientForms window = new ClientForms();
					window.principalFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientForms() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		principalFrame = new JFrame();
		principalFrame.setTitle("CHAT APP TCP IP");
		principalFrame.setBounds(100, 100, 716, 525);
		principalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		principalFrame.getContentPane().setLayout(null);

		txtMessage = new JTextField();
		txtMessage.setEnabled(false);
		txtMessage.setBounds(10, 372, 320, 20);
		principalFrame.getContentPane().add(txtMessage);
		txtMessage.setColumns(10);

		ClientForms.txtMessages = new JTextArea();
		ClientForms.txtMessages.setBounds(10, 112, 430, 235);
		principalFrame.getContentPane().add(txtMessages);
		txtMessages.setEditable(false);

		JButton btnSend = new JButton("Enviar");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtMessage.getText() != null && !txtMessage.getText().isEmpty() || selectedFile != null) {
					if (txtMessage.getText() != null && !txtMessage.getText().isEmpty()) {
						
					} else if (selectedFile != null) {
						ClientForms.txtMessages.append("\nArquivo selecionado para envio: " + selectedFile);
					}
					Message message;
					if(listUsersConnected.getSelectedValue() != null && !listUsersConnected.getSelectedValue().toString().equals(txtUserName)){
						message = new Message(txtUserName.getText(), selectedFile, txtMessage.getText(), false, listUsersConnected.getSelectedValue().toString());
					}else{
						message = new Message(txtUserName.getText(), selectedFile, txtMessage.getText(), false, null);
					}

					ClientSend.message = message;
					selectedFile = null;
					txtMessage.setText(null);
				} else {
					JOptionPane.showMessageDialog(principalFrame,
							"Digite uma mensagem a enviar, ou selecione um arquivo.");
				}

			}
		});
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnSend.setEnabled(false);
		btnSend.setBounds(340, 371, 89, 23);
		principalFrame.getContentPane().add(btnSend);

		JLabel lblMensagens = new JLabel("Mensagens");
		lblMensagens.setBounds(10, 87, 78, 14);
		principalFrame.getContentPane().add(lblMensagens);

		JLabel lblUsuariosConectados = new JLabel("Usuarios Conectados");
		lblUsuariosConectados.setBounds(451, 87, 143, 14);
		principalFrame.getContentPane().add(lblUsuariosConectados);

		JButton btnSelectFile = new JButton("Selecionar Arquivo");
		btnSelectFile.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnSelectFile.setEnabled(false);
		btnSelectFile.setBounds(451, 371, 168, 23);
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(new JFrame());
				if (result == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
				}
			}
		});
		principalFrame.getContentPane().add(btnSelectFile);

		listUsersConnected = new JList();
		List<String> values = new ArrayList<>();
		listUsersConnected.setBounds(449, 112, 228, 235);
		listUsersConnected.setModel(connecteds);
		principalFrame.getContentPane().add(listUsersConnected);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 64, 710, 12);
		principalFrame.getContentPane().add(separator);

		JLabel lblDigiteSuaMensagem_1 = new JLabel("Digite sua mensagem");
		lblDigiteSuaMensagem_1.setBounds(10, 358, 207, 14);
		principalFrame.getContentPane().add(lblDigiteSuaMensagem_1);

		txtUserName = new JTextField();
		txtUserName.setColumns(10);
		txtUserName.setBounds(10, 31, 161, 20);
		principalFrame.getContentPane().add(txtUserName);

		JLabel lblDigiteSeuNome = new JLabel("Digite seu nome");
		lblDigiteSeuNome.setBounds(10, 11, 207, 14);
		principalFrame.getContentPane().add(lblDigiteSeuNome);

		JButton btnConect = new JButton("Conectar");
		btnConect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtUserName.getText() != null && !txtUserName.getText().isEmpty()) {
					try {
						socket = new Socket("127.0.0.1", 8080);
						processClientSend = new Thread(new ClientSend(socket));
						processClientSend.start();

						processClientReceive = new Thread(new ClientReceive(socket));
						processClientReceive.start();

						setButtonsConnected(btnSend, btnSelectFile, btnDisconnect, btnConect);
						connecteds.addElement(txtUserName.getText());
						JOptionPane.showMessageDialog(principalFrame, "Cliente conectado com sucesso !");

					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(principalFrame,
								"Ocorreu um erro ao conectar ao server, tente novamente");
					}
				} else {
					JOptionPane.showMessageDialog(principalFrame, "Informe o nome de usuário para conectar");
				}

			}

			private void setButtonsConnected(JButton btnSend, JButton btnSelectFile, JButton btnDisconnect,
					JButton btnConect) {
				btnConect.setEnabled(false);
				btnSelectFile.setEnabled(true);
				btnSend.setEnabled(true);
				txtMessage.setEnabled(true);
				txtUserName.setEnabled(false);
				btnDisconnect.setEnabled(true);
			}
		});
		btnConect.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnConect.setBounds(181, 30, 89, 23);
		principalFrame.getContentPane().add(btnConect);

		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Message message = new Message(txtUserName.getText(), null, null, true, null);
				ClientSend.message = message;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				if (processClientReceive != null) {
					processClientReceive.interrupt();
					processClientReceive.stop();
					System.out.println("Stopped");
				}
				if (processClientSend != null) {
					processClientSend.interrupt();
					processClientReceive.stop();
					System.out.println("Stopped");
				}
				try {
					socket.shutdownOutput();
					socket.shutdownInput();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				JOptionPane.showMessageDialog(principalFrame, "Cliente desconectado com sucesso!");
				setButtonsDisconnected(btnSend, btnSelectFile, btnConect);
				connecteds.clear();
			}

			private void setButtonsDisconnected(JButton btnSend, JButton btnSelectFile, JButton btnConect) {
				btnConect.setEnabled(true);
				btnSelectFile.setEnabled(false);
				btnSend.setEnabled(false);
				txtMessage.setEnabled(false);
				txtUserName.setEnabled(true);
				btnDisconnect.setEnabled(false);
			}
		});
		btnDisconnect.setEnabled(false);
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnDisconnect.setBounds(280, 30, 89, 23);
		principalFrame.getContentPane().add(btnDisconnect);
	}
}
