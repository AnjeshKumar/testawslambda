package com.aws.hellolambda.example;

public class LambdaResponse {
	String responseMessage;
	String transactionID;

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	@Override
	public String toString() {
		return "LambdaResponse [responseMessage=" + responseMessage + ", transactionID=" + transactionID + "]";
	}
}
