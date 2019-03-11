package lugh.gitApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Commit{

	@JsonProperty("comment_count")
	private int commentCount;

	@JsonProperty("committer")
	private Committer committer;

	@JsonProperty("author")
	private Author author;

	@JsonProperty("tree")
	private Tree tree;

	@JsonProperty("message")
	private String message;

	@JsonProperty("url")
	private String url;

	@JsonProperty("verification")
	private Verification verification;

	public void setCommentCount(int commentCount){
		this.commentCount = commentCount;
	}

	public int getCommentCount(){
		return commentCount;
	}

	public void setCommitter(Committer committer){
		this.committer = committer;
	}

	public Committer getCommitter(){
		return committer;
	}

	public void setAuthor(Author author){
		this.author = author;
	}

	public Author getAuthor(){
		return author;
	}

	public void setTree(Tree tree){
		this.tree = tree;
	}

	public Tree getTree(){
		return tree;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setVerification(Verification verification){
		this.verification = verification;
	}

	public Verification getVerification(){
		return verification;
	}

	@Override
 	public String toString(){
		return 
			"Commit{" + 
			"comment_count = '" + commentCount + '\'' + 
			",committer = '" + committer + '\'' + 
			",author = '" + author + '\'' + 
			",tree = '" + tree + '\'' + 
			",message = '" + message + '\'' + 
			",url = '" + url + '\'' + 
			",verification = '" + verification + '\'' + 
			"}";
		}
}