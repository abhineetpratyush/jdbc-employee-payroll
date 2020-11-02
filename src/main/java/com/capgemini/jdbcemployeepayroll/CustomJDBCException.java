package com.capgemini.jdbcemployeepayroll;

enum ExceptionType{
	SQL_EXCEPTION, RECORD_UPDATE_FAILURE, CLASS_NOT_FOUND, UNABLE_TO_USE_PREPARED_STATEMENT, UNABLE_TO_USE_STATEMENT
}

public class CustomJDBCException extends Exception{
	public CustomJDBCException(ExceptionType exceptionType) {
		super(exceptionType.toString());
	}
}






