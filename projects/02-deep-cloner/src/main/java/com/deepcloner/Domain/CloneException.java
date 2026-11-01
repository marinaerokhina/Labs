package com.deepcloner.Domain;

public class CloneException extends RuntimeException {
    public CloneException(String msg, Throwable cause) { super(msg, cause); }
    public CloneException(String msg) { super(msg); }
}