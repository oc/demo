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
public class RSVP extends Model {
	@ManyToOne
	public User owner;
	
	@ManyToOne
    public Event event;

	@Required
	public boolean status;
	
	public Date createdAt;
				
	public RSVP(Event event, User owner, boolean status) {
		this.event = event;
		this.owner = owner;
		this.status = status;
		
		this.createdAt = new Date();
	}
	
	public String toString() {
		return status ? "yes" : "no";
	}
	
}