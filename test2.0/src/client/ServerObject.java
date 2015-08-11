package client;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerObject implements Serializable {
	private static final long serialVersionUID = 3;

	private int xCoordinate = 400;
	private int yCoordinate = 200;
	private int xMove;
	private int yMove;

	
	private Boolean faceDown = false;
	private Boolean faceUp = false;
	private Boolean faceLeft = false;
	private Boolean faceRight = true;
	private Boolean cross = false;

	private ArrayList<String> usernames;
	private String message;
	private String username = "undefined";

	
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
	
	public void setArrayList(ArrayList<String> usernames){
		this.usernames = usernames;
		
	}
	
	public void addArrayList(){
		if(username != "undefined" && usernames.indexOf(username) < 0){
			usernames.add(username);
		}
	}
	
	public ArrayList<String> getArrayList(){
		return usernames;
	}
	
	public void setXMove(int xMove){
		this.xMove = xMove;
	}
	
	public int getXMove(){
		return xMove;
	}
	
	public void setYMove(int yMove){
		this.yMove = yMove;
	}
	
	public int getYMove(){
		return yMove;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	public String getMessage(){
		return message;
	}
	
	public void setCross(Boolean cross){
		this.cross = cross;
	}
	public Boolean getCross(){
		return cross;
	}
	
	public void setFaceRight(Boolean faceRight){
		this.faceRight = faceRight;
	}
	public Boolean getFaceRight(){
		return faceRight;
	}
	
	public void setFaceLeft(Boolean faceLeft){
		this.faceLeft = faceLeft;
	}
	public Boolean getFaceLeft(){
		return faceLeft;
	}
	
	public void setFaceUp(Boolean faceUp){
		this.faceUp = faceUp;
	}
	public Boolean getFaceUp(){
		return faceUp;
	}
	
	public void setFaceDown(Boolean faceDown){
		this.faceDown = faceDown;
	}
	public Boolean getFaceDown(){
		return faceDown;
	}
}