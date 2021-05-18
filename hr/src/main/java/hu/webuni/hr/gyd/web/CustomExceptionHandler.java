package hu.webuni.hr.gyd.web;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import hu.webuni.hr.gyd.service.ClaimAlreadyApprovedException;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<MyError> handleNoSuchElementException(NoSuchElementException e, WebRequest req) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new MyError(e.getMessage()));
	}
	
	@ExceptionHandler
	public ResponseEntity<MyError> handleClaimAlreadyApprovedException(ClaimAlreadyApprovedException e, WebRequest req) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new MyError(e.getMessage()));
	}
}
