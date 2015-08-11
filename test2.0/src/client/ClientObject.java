package client;

import java.io.Serializable;

public class ClientObject implements Serializable {
	private static final long serialVersionUID = 3;

	private Boolean down = false;
	private Boolean up = false;
	private Boolean left = false;
	private Boolean right = false;
	private Boolean r = false;
	private String message;
	private String username = "undefined";


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Boolean getDown() {
		return down;
	}
	public void setDown(Boolean down) {
		this.down = down;
	}
	public Boolean getUp() {
		return up;
	}
	public void setUp(Boolean up) {
		this.up = up;
	}
	public Boolean getLeft() {
		return left;
	}
	public void setLeft(Boolean left) {
		this.left = left;
	}
	public Boolean getRight() {
		return right;
	}
	public void setRight(Boolean right) {
		this.right = right;
	}
	public Boolean getR() {
		return r;
	}
	public void setR(Boolean r) {
		this.r = r;
	}
	
}