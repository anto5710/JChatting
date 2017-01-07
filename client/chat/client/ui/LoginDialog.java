package chat.client.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.management.InstanceAlreadyExistsException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import chat.client.ServerHandler;

import java.awt.Insets;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

public class LoginDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField ipField;
	private JTextField portField;
	private JTextField nicknameField;
	
	public static ServerHandler client;
	private JTextArea errorArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoginDialog dialog = new LoginDialog();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LoginDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights =   new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("IP");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			ipField = new JTextField("localhost");
			GridBagConstraints gbc_ipField = new GridBagConstraints();
			gbc_ipField.insets = new Insets(0, 0, 5, 0);
			gbc_ipField.fill = GridBagConstraints.HORIZONTAL;
			gbc_ipField.gridx = 1;
			gbc_ipField.gridy = 0;
			contentPanel.add(ipField, gbc_ipField);
			ipField.setColumns(10);
		}
		{
			JLabel lblPort = new JLabel("PORT");
			GridBagConstraints gbc_lblPort = new GridBagConstraints();
			gbc_lblPort.anchor = GridBagConstraints.EAST;
			gbc_lblPort.insets = new Insets(0, 0, 5, 5);
			gbc_lblPort.gridx = 0;
			gbc_lblPort.gridy = 1;
			contentPanel.add(lblPort, gbc_lblPort);
		}
		{
			portField = new JTextField("8999");
			GridBagConstraints gbc_portField = new GridBagConstraints();
			gbc_portField.insets = new Insets(0, 0, 5, 0);
			gbc_portField.fill = GridBagConstraints.HORIZONTAL;
			gbc_portField.gridx = 1;
			gbc_portField.gridy = 1;
			contentPanel.add(portField, gbc_portField);
			portField.setColumns(10);
		}
		{
			JLabel lblNickname = new JLabel("NICKNAME");
			GridBagConstraints gbc_lblNickname = new GridBagConstraints();
			gbc_lblNickname.anchor = GridBagConstraints.EAST;
			gbc_lblNickname.insets = new Insets(0, 0, 5, 5);
			gbc_lblNickname.gridx = 0;
			gbc_lblNickname.gridy = 2;
			contentPanel.add(lblNickname, gbc_lblNickname);
		}
		{
			nicknameField = new JTextField("nickName");
			GridBagConstraints gbc_nicknameField = new GridBagConstraints();
			gbc_nicknameField.insets = new Insets(0, 0, 5, 0);
			gbc_nicknameField.fill = GridBagConstraints.HORIZONTAL;
			gbc_nicknameField.gridx = 1;
			gbc_nicknameField.gridy = 2;
			contentPanel.add(nicknameField, gbc_nicknameField);
			nicknameField.setColumns(10);
		}
		{
			errorArea = new JTextArea();
			errorArea.setEnabled(false);
			errorArea.setEditable(false);
			GridBagConstraints gbc_errorArea = new GridBagConstraints();
			gbc_errorArea.insets = new Insets(0, 0, 0, 5);
			gbc_errorArea.fill = GridBagConstraints.BOTH;
			gbc_errorArea.gridx = 1;
			gbc_errorArea.gridy = 3;
			contentPanel.add(errorArea, gbc_errorArea);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton loginBtn = new JButton("Login");
				loginBtn.setActionCommand("OK");
				buttonPane.add(loginBtn);
				loginBtn.addActionListener((e)->{
					ChatFrame frame = openChatFrame();
					processLogin(frame);
					dispose();
				});
				getRootPane().setDefaultButton(loginBtn);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(e->{
					LoginDialog.this.dispose();
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * 채팅 화면을 보여줍니다.
	 */
	private ChatFrame openChatFrame() {
	
		try {
			ChatFrame.INSTANCE = new ChatFrame();
			ChatFrame.INSTANCE.showFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ChatFrame.INSTANCE;
	}

	/**
	 * 서버에 로그인을 시도합니다.
	 * @return 로그인 성공 여부
	 */
	private boolean processLogin(ChatFrame frame) {
		
		String IP = ipField.getText();
		String nickName = nicknameField.getText();

		int port;
		try {
			port = Integer.parseInt(portField.getText());
			client = new ServerHandler(IP, port, nickName);
			frame.sethandler ( client );
			client.sendLogin(nickName);
			return true;
		} catch (Exception e) {
			errorArea.append( String.format("[%s] %s\n",  e.getClass().getName(), e.getMessage() ));
			return false;
		}
	}
}
