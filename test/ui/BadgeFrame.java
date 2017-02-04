//package ui;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.EventQueue;
//import java.awt.Point;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.FocusAdapter;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import javax.swing.JFrame;
//import javax.swing.JMenu;
//import javax.swing.JMenuItem;
//import javax.swing.JPanel;
//import javax.swing.JPopupMenu;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.SwingUtilities;
//import javax.swing.border.EmptyBorder;
//
//import chat.client.ui.component.BadgeComponent;
//
//public class BadgeFrame extends JFrame {
//
//	private JPanel contentPane;
//	private JPopupMenu mnu;
//	private JTextField inputField;
//	private Set<String> selected = new HashSet<>();
//	private static Set<String> CHATTERS = new HashSet<>(Arrays.asList("AA","BB","CC","DD","EDSF"));
//	private JPanel panel;
//	
//	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					BadgeFrame frame = new BadgeFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the frame.
//	 */
//	public BadgeFrame() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 450, 300);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
//
//		panel = new JPanel();
//		contentPane.add(panel, BorderLayout.CENTER);
//
//		inputField = new JTextField();
//		inputField.setPreferredSize(new Dimension(200, 20));
//		menu = new JPopupMenu();
//		inputField.addKeyListener(new KeyTypeListener());		
//		menu.add(new JMenuItem("dsds")); 
//		
//		
//		panel.add(menu);
//		panel.setComponentPopupMenu(menu);
//		panel.add(inputField);
//		
//		menu.setFocusable(false);
//	}
//	
//	public void addBadge(String text) {
//		BadgeComponent badge = new BadgeComponent(text);
//		
//		BadgeListener listener = new BadgeListener();
//		badge.addMouseListener(listener);
//		badge.addMouseMotionListener(listener);
//		selected.add(text);
//		panel.add(badge);
//		panel.revalidate();
//	}
//	
//	public void removeBadge(BadgeComponent badge) {
//		selected.remove(badge.getNickname());
//		panel.remove(badge);
//		panel.revalidate();
//		handlePopup();
//	}
//	
//	private void updatePopup() {
//		String text = inputField.getText();
//		menu.removeAll();
//		CHATTERS.stream().
//			filter(name->!selected.contains(name)).
//			filter(name->
//				name.toLowerCase().startsWith(text.toLowerCase()) 
//			).
//			forEach(name->{
//				JMenuItem item = new JMenuItem(name);
//				item.addActionListener(new MenuListener());
//				menu.add(item);
//			});
//		menu.revalidate();
//	}
//	
//	void showPopup() {
//		int x= inputField.getX();
//		int y = inputField.getY();
//		int height = inputField.getHeight();
//		
//		menu.show(
//				inputField.getParent(), 
//				x ,
//				y + height + 5);
//		inputField.requestFocus();
//		System.out.println("OK????");
//	}
//	
//	public void handlePopup(){
//		String text = inputField.getText();
//		
//		if(text.isEmpty()){
//			hidePopup();
//		}else{
//			updatePopup();
//			showPopup();
//		}
//	}
//	
//	private void hidePopup(){
//		menu.setVisible(false);
//		System.out.println("hide");
//	}
//
//	private class BadgeListener extends MouseAdapter {
//		@Override
//		public void mouseClicked(MouseEvent e) {
//			BadgeComponent badge = (BadgeComponent) e.getSource();
//			removeBadge(badge);
//		}
//		
//		@Override
//		public void mouseEntered(MouseEvent e) {
//			BadgeComponent badge = (BadgeComponent) e.getSource();
//			badge.setHighlighted(true);
//		}
//	
//		@Override
//		public void mouseExited(MouseEvent e) {
//			BadgeComponent badge = (BadgeComponent) e.getSource();
//			badge.setHighlighted(false);
//		}
//	}
//	
//	private class MenuListener implements ActionListener {
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			System.out.println("왔음???");
//			JMenuItem item = (JMenuItem) e.getSource();
//			addBadge(item.getText());
//		}
//	}
//	
//	private class KeyTypeListener extends KeyAdapter {
//		
//		@Override
//		public void keyReleased(KeyEvent e) {
//			if( e.getID() != KeyEvent.KEY_RELEASED ) return;
//			char typed = e.getKeyChar();
//			handlePopup();
//		}
//	}
//
//}
