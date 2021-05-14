package hu.webuni.hr.gyd.web;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<MyError> handleNoSuchElementException(NoSuchElementException e, WebRequest req) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new MyError(e.getMessage()));
	}
}
