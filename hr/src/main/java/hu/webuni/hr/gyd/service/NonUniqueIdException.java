package hu.webuni.hr.gyd.service;

public class NonUniqueIdException extends RuntimeException {

	public NonUniqueIdException() {
		super("Existing Id");
	}
}
