package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.mindrot.jbcrypt.BCrypt;

import play.db.ebean.Model;

@Entity
public class User extends Model {
	@Id
	private String email;
	private String password;
	
	public User(String email, String password){
		String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
		this.email = email;
		this.password = passwordHash;
	}
	
	public static Model.Finder<Integer, User> find = 
			new Model.Finder<>(Integer.class, User.class);
	
	public static User authenticate(String email, String password) {
		User user = find.where().eq("email", email).findUnique();
		if (user == null){
			return user;
		} else if (BCrypt.checkpw(password, user.password)) {
			return user;
		} else {
			return null;
		}
	}
}
