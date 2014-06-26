package org.mock.mock;

public class NoCardInserted extends Exception {

	public NoCardInserted() {
		System.out.println("No valid cart inserted.");
	}

}
