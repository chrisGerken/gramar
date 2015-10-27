package org.gramar.exception;

import org.gramar.ast.SourceRegion;

public class IllFormedTemplateException extends GramarException {

//	public IllFormedTemplateException() {
//		// TODO Auto-generated constructor stub
//	}
//
//	public IllFormedTemplateException(String message) {
//		super(message);
//		// TODO Auto-generated constructor stub
//	}
//
//	public IllFormedTemplateException(Throwable cause) {
//		super(cause);
//		// TODO Auto-generated constructor stub
//	}
//
//	public IllFormedTemplateException(String message, Throwable cause) {
//		super(message, cause);
//		// TODO Auto-generated constructor stub
//	}
//
//	public IllFormedTemplateException(String message, Throwable cause,
//			boolean enableSuppression, boolean writableStackTrace) {
//		super(message, cause, enableSuppression, writableStackTrace);
//		// TODO Auto-generated constructor stub
//	}

	public IllFormedTemplateException(SourceRegion nextRegion, String string) {
		super(string);
	}

}
