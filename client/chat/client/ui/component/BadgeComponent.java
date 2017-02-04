package chat.client.ui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

public class BadgeComponent extends JPanel {

	private final String NICKNAME;
	private final int FONT_WIDTH, FONT_HEIGHT;
	private static Font nickFont = new Font("Courier New", Font.PLAIN, 14);
	private static final int MARGIN = 20;
	
	private BadgeStyle style = BadgeStyle.STANDARD;
	
	/**
	 * Create the panel.
	 */
	public BadgeComponent(String nickname) {
		this.NICKNAME = nickname;
		
		setFont(nickFont);
		FontMetrics fm = getFontMetrics(getFont());
		FONT_WIDTH = fm.stringWidth(NICKNAME);
		FONT_HEIGHT = fm.getHeight();
		System.out.println(FONT_WIDTH + ", " + FONT_HEIGHT);
		
		Dimension size = new Dimension(FONT_WIDTH+MARGIN, FONT_HEIGHT+MARGIN);
		setPreferredSize(size);
	}
	/*
	 *   
	 *     +-----
	 *     |   a
	 *     |  a  a
	 *     + a    a
	 *     (x,y)
	 *     
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	
	public void setHighlighted(boolean highlight){
		style = highlight ? BadgeStyle.HIGH_LIGHTED : BadgeStyle.STANDARD;
		repaint();
	}
	
	public String getNickname(){
		return NICKNAME;
	}
	
	private enum BadgeStyle{
		STANDARD(new Color(200,200,200,128), Color.BLACK, Color.GRAY),
		HIGH_LIGHTED(Color.lightGray, Color.BLACK, Color.DARK_GRAY);
		
		private final Color bg, fg;
		private final Color border;
		
		private BadgeStyle(Color bg, Color fg, Color border){
			this.bg = bg;
			this.fg = fg;
			this.border = border;
		}

		public Color getBgColor() {
			return bg;
		}

		public Color getFgColor() {
			return fg;
		}

		public Color getBorderColor() {
			return border;
		}
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		/*
		 * 레이아웃 매니저가 현재 컴포넌트한테 할당해준 영역
		 */
		System.out.println(FONT_WIDTH + ", " + FONT_HEIGHT);
		
		int W = getWidth();
		int H = getHeight();
		System.out.println(W + ", " + H);
		
		int x = (W-FONT_WIDTH)/2;
		int y = (H+MARGIN-FONT_HEIGHT)/2;
		
		g.setColor(style.getBgColor());
		g.fillRoundRect(0, 0, W, H, 3, 3);
		
		g.setColor(style.getBorderColor());
		g.drawRoundRect(0, 0, W-1, H-1, 3,3);
		
		g.setColor(style.getFgColor());
		g.drawString(NICKNAME, x, y);
	}
}
