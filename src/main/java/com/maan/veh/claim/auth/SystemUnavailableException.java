package com.maan.veh.claim.auth;

public class SystemUnavailableException extends Exception 
{
	public static String error = "";

    public SystemUnavailableException() {}
    public SystemUnavailableException(String exception) { error = exception;}
    
    public static void main(String[] args) 
	{
		System.out.println("System Exception!");
        System.out.println(error);
	}
}