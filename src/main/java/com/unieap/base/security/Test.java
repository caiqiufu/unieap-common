package com.unieap.base.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String password = "1";  
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
        String hashedPassword1 = passwordEncoder.encode(password);  
        String hashedPassword2 = passwordEncoder.encode(password);  
        String hashedPassword3 = passwordEncoder.encode(password);  
        String hashedPassword4 = passwordEncoder.encode(password);  
        System.out.println(hashedPassword1);  
        System.out.println(hashedPassword2);  
        System.out.println(hashedPassword3);  
        System.out.println(hashedPassword4); 
        boolean match1 = passwordEncoder.matches(password, hashedPassword1);  
        System.out.println(match1);  
        boolean match2 = passwordEncoder.matches(password, hashedPassword2);  
        System.out.println(match2);  
        boolean match3 = passwordEncoder.matches(password, hashedPassword3);  
        System.out.println(match3);  
        boolean match4 = passwordEncoder.matches(password, hashedPassword4);  
        System.out.println(match4); 
	}

}
