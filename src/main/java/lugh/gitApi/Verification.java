package lugh.gitApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Verification{

	@JsonProperty("reason")
	private String reason;

	@JsonProperty("signature")
	private Object signature;

	@JsonProperty("payload")
	private Object payload;

	@JsonProperty("verified")
	private boolean verified;

	public void setReason(String reason){
		this.reason = reason;
	}

	public String getReason(){
		return reason;
	}

	public void setSignature(Object signature){
		this.signature = signature;
	}

	public Object getSignature(){
		return signature;
	}

	public void setPayload(Object payload){
		this.payload = payload;
	}

	public Object getPayload(){
		return payload;
	}

	public void setVerified(boolean verified){
		this.verified = verified;
	}

	public boolean isVerified(){
		return verified;
	}

	@Override
 	public String toString(){
		return 
			"Verification{" + 
			"reason = '" + reason + '\'' + 
			",signature = '" + signature + '\'' + 
			",payload = '" + payload + '\'' + 
			",verified = '" + verified + '\'' + 
			"}";
		}
}