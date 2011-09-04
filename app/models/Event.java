package models;

import play.*;
import play.db.jpa.*;
import play.data.validation.*;

import models.oauth.*;

import javax.persistence.*;
import javax.util.*;

import java.util.*;
import java.lang.*;

@Entity
public class Event extends Model {

	@Required
	public String title;

	@Required
	@ManyToOne	
	public User owner;
	
	@Required
	public Date happensAt;
			
	@Lob
	public String description;
	
	public String location;
	
	public String slug;
	public Date createdAt;
	
	public Event(User owner, String title, String location, String description, Date happensAt) {
		this.owner = owner;
		this.title = title;
		this.location = location;
		this.description = description;
		this.happensAt = happensAt;
		
		this.slug = title.toLowerCase().replaceAll("\\s+", "-");
		this.createdAt = new Date();
	}
	
	public String toString() {
		return title;
	}
	
}