package telran.cowbull.dto;

import java.io.Serializable;

import telran.cowbull.service.Game;

public class User implements Serializable, Comparable<User> {

	private static final long serialVersionUID = 1L;
	public User(long userId, String name){
		this.userId = userId;
		this.name = name;
	}
	  
	public long userId;
	public String name;
	public Game currentGame;
	public int number= 0;
	
	public Game getCurrentGame() {
		return currentGame;
	}
	public void setCurrentGame(Game currentGame) {
		number++;
		this.currentGame = currentGame;
	} 
	public Game removeGame() {
		Game res = currentGame;
		currentGame = null;
		return res;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", currentGame=" + currentGame + " number=" + number + "]";
	}
	
	public void setNumber() {
		number++;
	}
	

	
	@Override
	public int compareTo(User o) {
		
		return (int) (userId - o.userId);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userId != other.userId)
			return false;
		return true;
	}
}
