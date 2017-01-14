package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import chat.client.ui.component.BadgeComponent;

public class BadgeFrame extends JFrame {

	private JPanel contentPane;
	private JPopupMenu menu;
	private JTextField inputField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BadgeFrame frame = new BadgeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BadgeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);

		inputField = new JTextField();
		inputField.setPreferredSize(new Dimension(200, 20));
		menu = new JPopupMenu();
		inputField.addKeyListener(new KeyTypeListener());		
		menu.add(new JMenuItem("dsds")); 
		
		
		panel.add(menu);
		panel.setComponentPopupMenu(menu);

		BadgeComponent b1 = new BadgeComponent("Winter XXXX");
		BadgeComponent b2 = new BadgeComponent("YES");
		panel.add(inputField);
		panel.add(b1);
		panel.add(b2);
//		panel.requestFocusInWindow();
//		contentPane.requestFocus();
		
		menu.setFocusable(false);
		
		
	}
	private static Set<String> list = new HashSet<>(Arrays.asList("AA","BB","CC","DD","EDSF"));
	private void updatePopup(String value) {
		menu.removeAll();
		list.stream().
			filter(e->e.toLowerCase().startsWith(value.toLowerCase())).
			forEach(str->menu.add(new JMenuItem(str)));
		menu.revalidate();
	}
	
	void showPopup() {
//		if ( menu.isVisible() ) {
//			return ;
//		}
		int x= inputField.getX();
		int y = inputField.getY();
		int height = inputField.getHeight();
//		if(mouseLoc==null) mouseLoc = new Point(0, 0);
		
		menu.show(
				inputField.getParent(), 
				x ,
				y + height + 5);
		int len = inputField.getText().length();
		inputField.requestFocus();
		inputField.setSelectionStart(len);
		inputField.setSelectionEnd(len);
//		inputField.setCaret(c);
//		inputField.getCaret().moveDot();
		System.out.println("OK????");
	}
	
	private void hidePopup(){
		menu.setVisible(false);
		System.out.println("hide");
	}
	

	private class KeyTypeListener extends KeyAdapter {
		private JPopupMenu menu = new JPopupMenu();
		
		@Override
		public void keyReleased(KeyEvent e) {
			if( e.getID() == KeyEvent.KEY_RELEASED ){
				String text = inputField.getText();
				if(text.isEmpty()){
					hidePopup();
				}else{
					updatePopup(inputField.getText());
					showPopup();
				}
			}
		}
		
		
	}

}
