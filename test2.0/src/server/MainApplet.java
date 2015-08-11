package server;
import java.awt.BorderLayout;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MainApplet extends JApplet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(){
		Server server = new Server();
		JPanel serverPane = new JPanel(new BorderLayout());
		JLabel label = new JLabel("testing");
		serverPane.add(BorderLayout.CENTER, label);
		serverPane.setOpaque(true);
		setSize(600,400);
		setContentPane(serverPane);
		server.go();

		
		
	}

}
