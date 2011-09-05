package models;

import java.util.*;

import play.*;
import play.db.jpa.*;
import javax.persistence.*;

@Entity
@Table(name="`User`") // User is a reserved kw in postgresql
public class User extends Model {

	public String username;

	//public Credentials twitterCreds;

    public boolean admin;

	public User(String username) {
		this.username = username;
		//this.twitterCreds = new Credentials();
		//this.twitterCreds.save();
		this.admin = "olecr".equals(username); //XXX
	}

	public static User findOrCreate(String username) {
		User user = User.find("username", username).first();
		if (user == null) {
			user = new User(username).save();
		}
		return user;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public String toString() {
		return username;
	}

}