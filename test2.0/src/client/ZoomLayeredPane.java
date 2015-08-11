package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JLayeredPane;

public class ZoomLayeredPane extends JLayeredPane {
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		double width = screenSize.getWidth();
	
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(width/1024, height/726);
	}
}
