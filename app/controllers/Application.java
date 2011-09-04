package controllers;

import play.*;
import play.mvc.*;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import controllers.oauth.*;
import models.oauth.*;
import play.mvc.*;

import java.util.*;

import java.net.URLEncoder;

import models.*;

public class Application extends Controller {
	
	private final static String TWITTER_CONSUMER_KEY = "3zxmknG5d1pNCZvNSfMQ";
	private final static String TWITTER_CONSUMER_SECRET = "4aDVCOYfqzTjBKvNncqQx3gzpwXZi3TLiQ8jofttjq0";

    public static void index() throws Exception {
		String url = "http://twitter.com/statuses/mentions.xml";
		String mentions = play.libs.WS.url(getConnector().sign(getUser().twitterCreds, url)).get().getString();
	
		List events = Event.find("order by happensAt desc").fetch();		
        render(events, mentions);
    }

    public static void show(String slug) {
		Event event = Event.find("slug = ?", slug).first();
		render(event);
	}
	
	public static void createEvent(String username, String title, String location, String description, String date) {
		User owner = User.find("username", username).first();
		Date happensAt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		if (owner != null && owner.isAdmin()) {
			Event event = new Event(owner, title, location, description, happensAt).save();
			renderJSON(event);
		}
	}
	
	public static void setStatus(String status) throws Exception {
		String url = "http://twitter.com/statuses/update.json?status=" + URLEncoder.encode(status, "utf-8");
		String response = getConnector().sign(getUser().twitterCreds, WS.url(url), "POST").post().getString();
		request.current().contentType = "application/json";
		renderText(response);
	}

	// Twitter authentication

	public static void authenticate(String callback) throws Exception {
		// 1: get the request token
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("callback", callback);
		String callbackURL = Router.getFullUrl(request.controller + ".oauthCallback", args);
		getConnector().authenticate(getUser().twitterCreds, callbackURL);
	}

	public static void oauthCallback(String callback, String oauth_token, String oauth_verifier) throws Exception {
		// 2: get the access token
		getConnector().retrieveAccessToken(getUser().twitterCreds, oauth_verifier);
		redirect(callback);
	}

	private static OAuthClient connector = null;
	private static OAuthClient getConnector() {
		if (connector == null) {
			connector = new OAuthClient(
					"http://twitter.com/oauth/request_token",
					"http://twitter.com/oauth/access_token",
					"http://twitter.com/oauth/authorize",
					TWITTER_CONSUMER_KEY,
					TWITTER_CONSUMER_SECRET);
		}
		return connector;
	}

	private static User getUser() {
		return User.findOrCreate("olecr");
	}

    
 
}