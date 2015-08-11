package client;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame {

	public static void main(String[] args){
		Client networkStartup = new Client(); 			//Make new ChatClient Object called networkStartup
		JFrame frame = new JFrame("Rebirth of Martial Arts");
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(true);
		frame.setContentPane(panel);

		//frame.setUndecorated(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		networkStartup.startUp(networkStartup,panel);  			//call the startUp method and send the newly made object to it
		frame.setVisible(true);
	}
}
