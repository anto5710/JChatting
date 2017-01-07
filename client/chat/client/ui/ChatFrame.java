package chat.client.ui;

import static util.Util.*;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import util.Logger;
import chat.client.ServerDataListener;
import chat.client.ServerHandler;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ChatFrame {

	private JFrame frame;
	private JList<String> chatterList;
	private DefaultListModel<String> chatterModel = new DefaultListModel<>();
	/**
	 * 여기저기서 참조하는 인스턴스를 매번 생성자나 메소드로 전달하기 귀찮으면
	 * 이와 같이 전역 변수를 만들어서 아무데서나 참조하게 해주면 좋습니다.
	 * 
	 * 하지만!!!
	 * 
	 * 애플리케이션에 이런 요소들이 자꾸 많아지면 나중에 관리하기가 힘듭니다.
	 * Observer Pattern을 이용해야 하지만 아직은 배우지 않았으므로 일단은 전역변수(Singleton)으로 복잡도를 낮춰줍니다.
	 */
	public static ChatFrame INSTANCE;
	private JTextArea inputArea;
	private JTextArea chatArea;
	private ServerHandler connector;
	private DataRenderer dataHandler;
	private JTextArea senderList;
	private JPanel prvMsgPanel;
	private JPanel msgModePanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatFrame window = new ChatFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void showFrame() {
		frame.setVisible(true);
	}
	
	public void close(){
		frame.dispose();
	}
	
	
	/**
	 * Create the application.
	 */
	public ChatFrame() {
		initialize();
//		this.connector = client;
		this.dataHandler = new DataRenderer();
//		this.connector.addListener(dataHandler);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 593, 422);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(1.0);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		
		JScrollPane chatScroll = new JScrollPane();
		splitPane_1.setLeftComponent(chatScroll);
		
		chatArea = new JTextArea();
		chatScroll.setViewportView(chatArea);
		
		JPanel inputpanel = new JPanel();
		splitPane_1.setRightComponent(inputpanel);
		inputpanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane inputScroll = new JScrollPane();
		inputpanel.add(inputScroll, BorderLayout.CENTER);
		
		inputArea = new JTextArea();
		inputScroll.setViewportView(inputArea);
		
		senderList = new JTextArea("전체");
		
		
		
		JButton sendBtn = new JButton("SEND");
		sendBtn.addActionListener((e)->{
			if(!inputArea.getText().isEmpty()) sendText();
		});
		inputpanel.add(sendBtn, BorderLayout.EAST);
		
		msgModePanel = new JPanel();
		inputpanel.add(msgModePanel, BorderLayout.SOUTH);
		msgModePanel.setLayout(new BorderLayout(0, 0));
		
		JCheckBox checkBox = new JCheckBox("비밀전송");
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( checkBox.isSelected()) {
					showPrivePanel();
				} else {
					hidePrivPanel();
				}
			}
		});
		msgModePanel.add(checkBox, BorderLayout.SOUTH);
		
		prvMsgPanel = new JPanel();
		msgModePanel.add(prvMsgPanel, BorderLayout.CENTER);
		
		JToggleButton tglbtnAa = new JToggleButton("AA");
		prvMsgPanel.add(tglbtnAa);
		
		JToggleButton tglbtnBb = new JToggleButton("BB");
		prvMsgPanel.add(tglbtnBb);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		chatterList = new JList<>( chatterModel );
		scrollPane.setViewportView(chatterList);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblStatus = new JLabel("STATUS");
		panel_1.add(lblStatus, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel_2, BorderLayout.NORTH);
		
		JButton btnLogout = new JButton("LOGOUT");
		btnLogout.addActionListener(e->processLogout());
		panel_2.add(btnLogout);
	}
	
	protected void hidePrivPanel() {
		msgModePanel.remove(prvMsgPanel);
		msgModePanel.revalidate();
		
	}

	protected void showPrivePanel() {
//		frame.getContentPane().set
		msgModePanel.add(prvMsgPanel, BorderLayout.CENTER);
		msgModePanel.revalidate();
	}

	private void sendText(){
		
		String text = inputArea.getText();
		tryDoing(()->{LoginDialog.client.sendPublicMSG(text);});
		inputArea.setText("");
	}
	
	private void processLogout(){
		tryDoing(()->{LoginDialog.client.sendLogout();});
		ChatFrame.INSTANCE.close();
	}
	
	public void printMessage(String msg){
		chatArea.append(msg+"\n");
	}
	
	public void addNickName(String nicknam){
		chatterModel.addElement(nicknam);
	}
	
	public void removeNickName(String nicknam){
		chatterModel.removeElement(nicknam);
	}
	
	public void updateChatterList( String[] nickNames ){
		chatterModel.clear();
		Arrays.stream(nickNames).forEach(chatterModel::addElement);
	}
	
	static class DataRenderer implements ServerDataListener {

		private String convertPublicMSG(String input){
			int l = input.indexOf(":");
			if(l==-1) return ""; // can't find regex
			
			int length = input.length();
			String sender = input.substring(0, l);
			String msg = input.substring(l+1, length);
			return String.format("%s: %s", sender, msg); 
		}
		
		@Override
		public void onDataReceived(String cmd, Object data) {
			Logger.log(" handler : " + cmd + " and " + data);
			switch (cmd) {
			case "MSG":
				String msg = convertPublicMSG((String)data);
				INSTANCE.printMessage(msg);
				break;
				
			case "CHATTER_LIST":
				System.out.println("대화 참여자들 : " + Arrays.asList((String[])data));
				INSTANCE.updateChatterList((String[]) data);
				break;
				
			case "LOGIN":
				
				String nickNam = (String)data;
				INSTANCE.addNickName(nickNam);
				INSTANCE.printMessage(nickNam+" login");
				break;
				
			case "LOGOUT":
				
				nickNam = (String)data;
				INSTANCE.removeNickName(nickNam);
				INSTANCE.printMessage(nickNam+" logout");
				break;
			default : 
				Logger.log("what is this? " + cmd);
			}
		}
	}

	public void sethandler(ServerHandler handler) {
		this.connector = handler;
		this.connector.addListener(this.dataHandler);
	}
}
