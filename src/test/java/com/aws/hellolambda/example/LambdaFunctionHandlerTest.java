package com.aws.hellolambda.example;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.aws.hellolambda.example.LambdaFunctionHandler;
import com.aws.hellolambda.example.LambdaRequest;
import com.aws.hellolambda.example.LambdaResponse;

import junit.framework.Assert;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {

	private static LambdaRequest input;

	@BeforeClass
	public static void createInput() throws IOException {
		input = new LambdaRequest();
		input.setName("MyName");

	}

	private Context createContext() {
		TestContext ctx = new TestContext();

		// TODO: customize your context here if needed.
		ctx.setFunctionName("MyLambda");

		return ctx;
	}

	@Test
	public void testLambdaFunctionHandler() {
		LambdaFunctionHandler handler = new LambdaFunctionHandler();
		Context ctx = createContext();

		LambdaResponse output = handler.handleRequest(input, ctx);

		// TODO: validate output here if needed.
		if (output != null) {
			System.out.println(output.toString());
		}

		Assert.assertEquals(" Not equal", output.getResponseMessage(), "Hello-" + input.getName());

	}
}
