package com.example.nagoyameshi.controller;

public class ResourceNotFoundExceptionController extends Exception {
    public ResourceNotFoundExceptionController(String message) {
    	class Test {
    	    public static void main(String[] args) {
    	        try {
    	            throw new ResourceNotFoundExceptionController("Resource not found");
    	        } catch (ResourceNotFoundExceptionController e) {
    	            e.printStackTrace();
    	        }
    	    }
    	}
    }
}
    
