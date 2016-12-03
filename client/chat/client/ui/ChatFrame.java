package chat.client.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ChatFrame {

	private JFrame frame;
	private JList chatterList;
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(1.0);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane_1.setLeftComponent(scrollPane_1);
		
		JTextArea chatArea = new JTextArea();
		scrollPane_1.setViewportView(chatArea);
		
		JPanel panel = new JPanel();
		splitPane_1.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel.add(scrollPane_2, BorderLayout.CENTER);
		
		JTextArea inputArea = new JTextArea();
		scrollPane_2.setViewportView(inputArea);
		
		JButton btnNewButton = new JButton("SEND");
		panel.add(btnNewButton, BorderLayout.EAST);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		chatterList = new JList( chatterModel );
		scrollPane.setViewportView(chatterList);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblStatus = new JLabel("STATUS");
		panel_1.add(lblStatus, BorderLayout.NORTH);
	}
	
	public void updateChatterList( List<String> chatters ){
//		DefaultListModel<String> model = (DefaultListModel<String>) chatterList.getModel() ;
		chatterModel.clear();
		for ( String chatter : chatters ) {
			chatterModel.addElement(chatter);
		}
		
	}

}
