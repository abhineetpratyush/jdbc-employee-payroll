package com.capgemini.jdbcemployeepayroll;

enum ExceptionType{
	SQL_EXCEPTION, RECORD_UPDATE_FAILURE, CLASS_NOT_FOUND
}

public class CustomJDBCException extends Exception{
	public CustomJDBCException(ExceptionType exceptionType) {
		super(exceptionType.toString());
	}
}






