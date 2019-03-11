package lugh.gitApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ParentsItem{

	@JsonProperty("html_url")
	private String htmlUrl;

	@JsonProperty("sha")
	private String sha;

	@JsonProperty("url")
	private String url;

	public void setHtmlUrl(String htmlUrl){
		this.htmlUrl = htmlUrl;
	}

	public String getHtmlUrl(){
		return htmlUrl;
	}

	public void setSha(String sha){
		this.sha = sha;
	}

	public String getSha(){
		return sha;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"ParentsItem{" + 
			"html_url = '" + htmlUrl + '\'' + 
			",sha = '" + sha + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}