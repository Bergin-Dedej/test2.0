package client;
import java.awt.BorderLayout;
import javax.swing.JApplet;
import javax.swing.JPanel;


public class MainApplet extends JApplet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(){
		Client networkStartup = new Client(); 		//Make new ChatClient Object called networkStartup
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(true);
		setContentPane(panel);
		setSize(600,400);
		networkStartup.startUp(networkStartup,panel);  		//call the startUp method and send the newly made object to it and this JApplet as an object to fill in with game contents
	}
}