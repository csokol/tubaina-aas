package models;

import java.util.List;

public class GithubUser {

	private String login;
	private List<String> emails;

	public GithubUser(String login, List<String> emails) {
		super();
		this.login = login;
		this.emails = emails;
	}
	
	public boolean hasEmail(){
		return !emails.isEmpty();
	}
	
	public String getFirstEmail(){
		if(emails.isEmpty()){
			throw new IllegalStateException("Don't have a github email?");
		}
		return emails.get(0);
	}

}
