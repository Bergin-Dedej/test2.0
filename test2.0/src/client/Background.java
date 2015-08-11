package client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Background extends JPanel implements Serializable {

	private BufferedImage background;
	private static final long serialVersionUID = 3;
	private int xCoordinate = 400;
	private int yCoordinate = 300;
	private int realXMove;
	private int realYMove;
	
	public Background(){

		try {
			background = ImageIO.read(new File("images/Background.png"));
			this.setOpaque(false);
			
			
			this.setVisible(true);

			this.setBounds(-400, -300, 2216, 2168);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int getXCoordinate() {
		return xCoordinate;
	}
	public int getYCoordinate() {
		return yCoordinate;
	}
	public void paintComponent(Graphics g){
		g.drawImage(background, 0,0, null);
		this.setOpaque(false);
		this.setBounds(-xCoordinate, -yCoordinate, 2216, 2168);
	}
	
	
	public void readMove(ServerObject servObj, int index){
		
		if(!servObj.getArrayList().isEmpty()){
			//Makes it so only a user with the specific username can move the background
			if(servObj.getUsername().equals(servObj.getArrayList().get(index))){  
				realXMove = servObj.getXMove();
				realYMove = servObj.getYMove();
			}
		}

	}
	
	public void move(){
		xCoordinate += realXMove;
		yCoordinate += realYMove;
		
	}
}

