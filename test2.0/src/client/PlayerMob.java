package client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PlayerMob extends JPanel implements Serializable {
	private static final long serialVersionUID = 2;
	private int xCoordinate = 400;
	private int yCoordinate = 200;

	private Boolean faceDown = false;
	private Boolean faceUp = false;
	private Boolean faceLeft = true;
	private Boolean faceRight = false;
	private Boolean cross = false;
	private Boolean knockedOut = false;

	private int xMove = 0;
	private int yMove = 0;
	private int realXMove = 0;
	private int realYMove = 0;
	private Boolean pressedLeft = false;
	private Boolean pressedRight = false;
	private Boolean pressedUp = false;
	private Boolean pressedDown = false;
	private Client networkStartup;
	private Boolean horVert;

	private BufferedImage standRight;
	private BufferedImage standLeft;
	private BufferedImage standUp;
	private BufferedImage standDown;
	private BufferedImage rCross;
	private BufferedImage lCross;
	private BufferedImage uCross;
	private BufferedImage dCross;
	private String username = "NEW";
	public String clientUsername;
	private int index;
	private ServerObject info;
	private int speed = 1;
	@SuppressWarnings("unused")
	private ClientObject clientObject;
	private int readXMove;
	private int readYMove;
	private int worldXMove;
	private int worldYMove;

	public PlayerMob(Client netStartup) {
		networkStartup = netStartup;
		try {
			standRight = ImageIO.read(new File("images/character/standRight.png"));
			standLeft = ImageIO.read(new File("images/character/standLeft.png"));
			standUp = ImageIO.read(new File("images/character/standUp.png"));
			standDown = ImageIO.read(new File("images/character/standDown.png"));
			rCross = ImageIO.read(new File("images/character/rCross.png"));
			lCross = ImageIO.read(new File("images/character/lCross.png"));
			uCross = ImageIO.read(new File("images/character/uCross.png"));
			dCross = ImageIO.read(new File("images/character/dCross.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setOpaque(false);
        this.setBounds(400, 200, 150, 150);
	}

	public void setXCoordinate(int xCoordinate){
		this.xCoordinate = xCoordinate;
	}

	public int getXCoordinate(){
		return xCoordinate;
	}

	public void setYCoordinate(int yCoordinate){
		this.yCoordinate = yCoordinate;
	}

	public int getYCoordinate(){
		return yCoordinate;
	}
	
	public void keyPressedR(){
		networkStartup.keyPressedR(true);
	}
	public void keyReleasedR(){
		networkStartup.keyPressedR(false);
	}
	
	public Boolean getCross(){
		return cross;
	}


	public Boolean getKnockedOut(){
		return knockedOut;
	}
	public void setKnockedOut(Boolean knockedOut){
		this.knockedOut = knockedOut;
	}

	public void keyPressedUp(){
		if (!pressedUp){
			yMove = -speed;
			pressedUp = true;
			horVert = true;
			networkStartup.keyPressed(horVert, yMove,yCoordinate);
		}
	}

	public void keyPressedDown(){
		if (!pressedDown){
			yMove = speed;
			pressedDown = true;
			horVert = false;
			networkStartup.keyPressed(horVert, yMove,yCoordinate);
		}
	}

	public void keyPressedRight(){
		if (!pressedRight){
			xMove = speed;
			pressedRight = true;
			horVert = true;
			networkStartup.keyPressed(xMove, horVert,xCoordinate);
		}
	}

	public void keyPressedLeft(){
		if (!pressedLeft){
			xMove = -speed;
			pressedLeft = true;
			horVert = false;
			networkStartup.keyPressed(xMove, horVert,xCoordinate);
		}
	}
	
	public void keyPressedEscape(){
		System.exit(0);
	}

	public void keyReleasedUp(){
		if (pressedUp  && pressedDown){
			pressedUp = false;
		}
		else if(pressedUp){
			yMove = 0;
			pressedUp = false;
			networkStartup.keyReleased(horVert, yMove,yCoordinate);
		}
		if(pressedDown){
			yMove = speed;
			networkStartup.keyReleased(horVert, yMove,yCoordinate);
		}
	}

	public void keyReleasedDown(){
		if (pressedDown && pressedUp){
			pressedDown = false;
		}
		else if(pressedDown){
			yMove = 0;
			pressedDown = false;
			networkStartup.keyReleased(horVert,yMove,yCoordinate);
		}
		if(pressedUp){
			yMove = -speed;
			networkStartup.keyReleased(horVert,yMove,yCoordinate);
		}
	}

	public void keyReleasedRight(){
		if(pressedRight && pressedLeft){
			pressedRight = false;
		}
		else if(pressedRight){
			xMove = 0;
			pressedRight = false;
			networkStartup.keyReleased(xMove, horVert,xCoordinate);
		}
		if(pressedLeft){
			xMove = -speed;
			networkStartup.keyReleased(xMove, horVert,xCoordinate);
		}
	}

	public void keyReleasedLeft(){

		if (pressedLeft && pressedRight){
			pressedLeft = false;
		}
		else if(pressedLeft){
			xMove = 0;
			pressedLeft = false;
			networkStartup.keyReleased(xMove, horVert,xCoordinate);
		}
		if(pressedRight){
			xMove = speed;
			networkStartup.keyReleased(xMove, horVert,xCoordinate);
		}
	}
	
	public void readMove(ServerObject servObj, int index){
		info = servObj;
		this.index = index;
		if(!servObj.getArrayList().isEmpty()){
			this.username = servObj.getUsername();
			//Makes it so only a user with the specific username can move the circle
			if(servObj.getUsername().equals(servObj.getArrayList().get(index))){  
				readXMove = servObj.getXMove();
				readYMove = servObj.getYMove();
			}
		}

	}
	
	public void worldMove(ServerObject servObj, int index){
		if(!servObj.getArrayList().isEmpty()){
			
			//Makes it so only a user with the specific username can move the circle
			if(servObj.getUsername().equals(servObj.getArrayList().get(index))){ 
				worldXMove = -servObj.getXMove();
				worldYMove = -servObj.getYMove();
			}
		}
	}

	
	public void updateCoordinates(ServerObject servObj){
		if(realXMove == 0 && realYMove == 0){
				xCoordinate = servObj.getXCoordinate();
				yCoordinate = servObj.getYCoordinate();
		}
		
	}
	
	public void updateOthersCoordinates(int xCoordinate, int yCoordinate){
		if(realXMove == 0 && realYMove == 0){
				this.xCoordinate = xCoordinate;
				this.yCoordinate = yCoordinate;
		}
		
	}
		
	public void updateFace(int index){
		faceDown = networkStartup.faceDowns.get(index);
		faceUp = networkStartup.faceUps.get(index);
		faceLeft = networkStartup.faceLefts.get(index);
		faceRight = networkStartup.faceRights.get(index);
		cross = networkStartup.crosses.get(index);

	}
	
	
	public void setClientServUsername(ClientObject clientObject, int index){
		if(!networkStartup.usernames.isEmpty()){
			this.clientObject = clientObject;
			clientUsername = clientObject.getUsername();
			username = networkStartup.usernames.get(index);
		}
	}

	public void move(){
		realXMove = readXMove + worldXMove;
		realYMove = readYMove + worldYMove;
		xCoordinate += realXMove;
		yCoordinate += realYMove;
		
	}
	


	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(knockedOut){
			this.setVisible(false);
			knockedOut = false;
		}
		else{
			if(faceRight){
				if(cross){
					g.drawImage(rCross, 0,16, null);
				}else{
					g.drawImage(standRight,0,16, null);
				}
			}else if(faceLeft) {
				if(cross){
					g.drawImage(lCross, 0,16, null);
				}else{
					g.drawImage(standLeft,0,16, null);
				}
			}else if(faceUp) {
				if(cross){
					g.drawImage(uCross, 0,16, null);
				}else{
					g.drawImage(standUp,0,16, null);
				}
			}else if(faceDown) {
				if(cross){
					g.drawImage(dCross, 0,16, null);
				}else{
					g.drawImage(standDown,0,16, null);
				}
			}else{
				g.drawImage(standRight,0,16, null);
			}
			
			
			if(username.equals(clientUsername)){
				this.setBounds(xCoordinate, yCoordinate, 150, 150);
				this.setBounds(400, 200, 150, 150);
				
				networkStartup.moveBackground(info, index);
			}else{
				if(yCoordinate >= 590 && xCoordinate >= 1015){
					this.setBounds(1023, 593, 150, 150);
				}else if(yCoordinate >= 590){
					this.setBounds(xCoordinate, 593, 150, 150);
				}else if(xCoordinate >= 1015){
					this.setBounds(1023, yCoordinate, 150, 150);
				}else if(yCoordinate <= -145 && xCoordinate <= -125){
					this.setBounds(-125, -150, 150, 151);
				}else if(yCoordinate <= -145){
					this.setBounds(xCoordinate, -150, 150, 151);
				}else if(xCoordinate <= -125){
					this.setBounds(-125, yCoordinate, 150, 150);
				}else{
					this.setVisible(true);
					this.setBounds(xCoordinate, yCoordinate, 150, 150);
				}
				
				
				System.out.println(xCoordinate + "---" + yCoordinate);
			}
			

		}
		
		g.drawString(username,134/3 - 2*username.length(), 10);
		
	}
	
	public Rectangle getBounds() {
	
		if(username.equals(clientUsername)){
			xCoordinate = 400;
			yCoordinate = 200;
		}
		
		if(faceRight){
			if(cross){
				return new Rectangle(xCoordinate+120,yCoordinate+40,10,10);
			}else{
				return new Rectangle(xCoordinate,yCoordinate,115,150);
			}
		}else if(faceLeft) {
			if(cross){
				return new Rectangle(xCoordinate,yCoordinate+40,10,10);
			}else{
				return new Rectangle(xCoordinate,yCoordinate,115,150);
			}
		}else if(faceUp) {
			if(cross){
				return new Rectangle(xCoordinate+25,yCoordinate+15,10,10);
			}else{
				return new Rectangle(xCoordinate,yCoordinate,115,150);
			}
		}else if(faceDown) {
			if(cross){
				return new Rectangle(xCoordinate+25,yCoordinate+135,10,10);
			}else{
				return new Rectangle(xCoordinate,yCoordinate,115,150);
			}
		}else{
			if(cross){
				return new Rectangle(xCoordinate+120,yCoordinate+40,10,10);
			}else{
				return new Rectangle(xCoordinate,yCoordinate,115,150);
			}
		}
	}
	
	
}
