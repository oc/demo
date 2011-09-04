package models;

import play.*;
import play.db.jpa.*;
import play.data.validation.*;

import javax.persistence.*;
import javax.util.*;

import java.util.*;
import java.lang.*;

@Entity
public class Post extends Model {
	
	@Required
	public String title;
	
	public String slug;
	
	@Required
	@MaxSize(5000)
	@Lob
	public String excerpt;
	
	@Required
	@MaxSize(100000)
	@Lob
	public String body;
	
	public Date createdAt;
	
	public Post(String title, String excerpt, String body) {
		this.title = title;
		this.excerpt = excerpt;
		this.body = body;
		this.slug = title.toLowerCase().replaceAll("\\s+", "-");
		this.createdAt = new Date();
	}
	
	public String toString() {
		return title;
	}
	
}