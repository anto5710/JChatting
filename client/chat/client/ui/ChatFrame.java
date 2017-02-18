package chat.client.ui;

import static util.Util.*;

import java.awt.Dimension;
import java.awt.EventQueue;

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
import util.Util;
import chat.client.ServerDataListener;
import chat.client.ServerHandler;
import chat.client.ui.component.BadgePanel;
import chat.protocol.UnknownCommandException;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JCheckBox;

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
	private JPanel prvMsgPanel;
	private JPanel msgModePanel;
	private BadgePanel badgePanel;
	private JTextArea badgeArea;
	private JCheckBox prvCheckBox;
	private JButton sendBtn;

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
		inputArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableSendBtn();
			}
		});
		
		sendBtn = new JButton("SEND");
		enableSendBtn();
		sendBtn.addActionListener((e)->{
			if(!inputArea.getText().isEmpty()) sendText();
		});
		inputpanel.add(sendBtn, BorderLayout.EAST);
		
		msgModePanel = new JPanel();
		inputpanel.add(msgModePanel, BorderLayout.SOUTH);
		msgModePanel.setLayout(new BorderLayout(0, 0));
		
		
		prvMsgPanel = new JPanel();
		msgModePanel.add(prvMsgPanel, BorderLayout.CENTER);
		
		prvCheckBox = new JCheckBox("비밀전송");
		msgModePanel.add(prvCheckBox, BorderLayout.SOUTH);
		prvCheckBox.addActionListener(new ActionListener() {
			{ hidePrivPanel(); }
			
			public void actionPerformed(ActionEvent e) {
				if ( prvCheckBox.isSelected()) {
					showPrivePanel();
				} else {
					hidePrivPanel();
				}
			}
		});
		badgePanel = new BadgePanel();
		badgeArea = new JTextArea();
		badgeArea.setPreferredSize(new Dimension(200, 20));
		badgeArea.addKeyListener(new KeyTypeListener());	
		prvMsgPanel.add(badgePanel);
		prvMsgPanel.add(badgeArea);
		
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
		msgModePanel.add(prvMsgPanel, BorderLayout.CENTER);
		msgModePanel.revalidate();
	}

	private void sendText(){
		String text = inputArea.getText();
		if(prvCheckBox.isSelected()){ // 비밀메시지일 경우
			String [] names = Util.toArray(String.class, badgePanel.getSelectedChatters());
			Util.tryDoing(()->connector.sendPrivateMSG(text, names));
		}else{
			Util.tryDoing(()->connector.sendPublicMSG(text));
		}
	}	
	
	private void processLogout(){
		tryDoing(()->LoginDialog.client.sendLogout());
		INSTANCE.close();
	}
	
	public void printMessage(String msg){
		chatArea.append(msg+"\n");
	}
	
	public void setHandler(ServerHandler handler) {
		this.connector = handler;
		this.connector.addListener(new DataRenderer());
	}

	public void enableSendBtn() {
		boolean hasMSG =  !inputArea.getText().isEmpty();
		sendBtn.setEnabled(hasMSG);
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
	
	public List<String> getNicknames (){
		return Arrays.stream(chatterModel.toArray())
		      .map(e-> (String) e).
		      collect(Collectors.toList());
	}
	
	private void handlePopup(){
		String text = badgeArea.getText();
		
		if(text.isEmpty()){
			badgePanel.hidePopup();
		}else{
			badgePanel.updatePopup(badgeArea.getText(), getNicknames());
			badgePanel.showPopup(badgeArea);
		}
	}
	
	private class KeyTypeListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			if( e.getID() == KeyEvent.KEY_RELEASED ){
				handlePopup();
			}
		}
	}
	
	public static class DataRenderer implements ServerDataListener {

		@SuppressWarnings("unchecked")
		@Override
		public void onDataReceived(String cmd, Object data) {
			Logger.log(" handler : " + cmd + " and " + data);
			switch (cmd) {
			case "PRV_MSG":
				Map<String, Object> map = (Map<String, Object>)data;
				String msg = (String) map.get("msg");
				String sender = (String) map.get("sender");
				INSTANCE.printMessage("[PRV_MSG] "+sender+": "+msg);
				
				break;
			case "MSG":
				map = (Map<String, Object>)data;
				msg = (String) map.get("msg");
				sender = (String) map.get("sender");
				INSTANCE.printMessage(sender +": "+ msg);

				break;
			case "CHATTER_LIST":
				String [] names = (String[]) data;
				System.out.println("대화 참여자들 : " + names.toString());
				INSTANCE.updateChatterList(names);
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
				new UnknownCommandException("unknown command: "+cmd);
			}
		}
	}
}
