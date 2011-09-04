package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
		List posts = Post.find("order by createdAt desc").fetch();
        render(posts);
    }

    public static void show(String slug) {
		Post post = Post.find("slug = ?", slug).first();
		render(post);
	}
	
	public static void createPost(String title, String excerpt, String body) {
		Post post = new Post(title, excerpt, body).save();
		renderJSON(post);
	}
    
 
}