package client;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;


public class AddKeyListener extends JPanel {

	private static final long serialVersionUID = 1L;
	public PlayerMob player;

	public AddKeyListener() {
		
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_LEFT)	player.keyReleasedLeft();
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) player.keyReleasedRight();
				if(e.getKeyCode() == KeyEvent.VK_UP)	player.keyReleasedUp();
				if(e.getKeyCode() == KeyEvent.VK_DOWN)	player.keyReleasedDown();
				if(e.getKeyCode() == KeyEvent.VK_R)		player.keyReleasedR();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP)	player.keyPressedUp();
				if(e.getKeyCode() == KeyEvent.VK_DOWN)	player.keyPressedDown();
				if(e.getKeyCode() == KeyEvent.VK_LEFT)	player.keyPressedLeft();
				if(e.getKeyCode() == KeyEvent.VK_RIGHT)	player.keyPressedRight();
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)player.keyPressedEscape();
				if(e.getKeyCode() == KeyEvent.VK_R)		player.keyPressedR();
			}
		});
		setFocusable(true);
		
	}
	
	public void setPlayer(PlayerMob player){
		this.player = player;
	}
		
}
