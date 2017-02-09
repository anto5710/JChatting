package chat.client.ui.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.DirectoryStream.Filter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat.client.ui.ChatFrame;

public class BadgePanel extends JPanel{
	private JPopupMenu menu;
	private Set<String> selected = new HashSet<>();
	
	public BadgePanel(){
		setLayout(new FlowLayout());
		
		menu = new JPopupMenu();
		menu = new JPopupMenu("Chatters");
		menu.setFocusable(false);
		
		setComponentPopupMenu(menu);
		add(menu);
	}
	
	public Set<String> getSelectedChatters(){
		return new HashSet<>(selected);
	}
	
	public void addBadge(String nickName) {
		BadgeComponent badge = new BadgeComponent(nickName);
		BadgeListener listener = new BadgeListener();
		badge.addMouseListener(listener);
		badge.addMouseMotionListener(listener);
		
		add(badge);
		selected.add(nickName);
		revalidate();
	}
	
	private void removeBadge(BadgeComponent badge){
		remove(badge);
		selected.remove(badge);
		
		revalidate();
	}
	
	private List<String> getNicknames() {
		return Arrays.stream(getComponents())
		      .map(c -> (BadgeComponent ) c)
		      .map(b -> b.getNickname())
		      .collect(Collectors.toList());
	}
	
	public void setMenuItems ( List<String> values) {
		values.stream().forEach(name->{
			JMenuItem item = new JMenuItem(name);
			item.addActionListener(new MenuListener());
			menu.add(item);
		});
		menu.revalidate();
	}
	
	public void updatePopup(String text, List<String> values) {
		menu.removeAll();
		List<String> toPut = values.stream().
			filter(name->!selected.contains(name)).
			filter(name->
				   name.toLowerCase().startsWith(text.toLowerCase())
			).collect(Collectors.toList());
		
		setMenuItems(toPut);
		menu.revalidate();
	}
	
	public void showPopup(Component comp) {
		int x= comp.getX();
		int y = comp.getY();
		int height = comp.getHeight();
		
		menu.show(
				comp.getParent(), 
				x ,
				y + height + 5);
		comp.requestFocus();
		System.out.println("Show PopupMenu");
	}
	
	public void hidePopup(){
		menu.setVisible(false);
	}

	private class BadgeListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			BadgeComponent badge = (BadgeComponent) e.getSource();
			removeBadge(badge);
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			BadgeComponent badge = (BadgeComponent) e.getSource();
			badge.setHighlighted(true);
		}
	
		@Override
		public void mouseExited(MouseEvent e) {
			BadgeComponent badge = (BadgeComponent) e.getSource();
			badge.setHighlighted(false);
		}
	}
	
	private class MenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			addBadge(item.getText());
		}
	}
}
