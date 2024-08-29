
package com.maan.veh.claim.auth;



import java.io.Serializable;
import java.security.Key;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.maan.veh.claim.entity.LoginMaster;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	String key_value ="3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";
	
    
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key_value);
        return Keys.hmacShaKeyFor(keyBytes);
    }

	public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        JwtParser parser = Jwts.parserBuilder()
                               .setSigningKey(getSignInKey())
                               .build();
        return parser.parseClaimsJws(token).getBody();
    }


    private Boolean isTokenExpired(String token,HttpServletRequest req) {
    	Date expiration = new Date();
    	boolean time = false;
    	String value = req.getSession().getAttribute(token)==null?"":req.getSession().getAttribute(token).toString();
    	if(StringUtils.isNotBlank(value)) {
    		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
    		try {
    		expiration = formatter.parse(value);
    		long addMinuteTime = expiration.getTime();  
    		expiration = new Date(addMinuteTime + (50*60*1000));
    		time = expiration.before(new Date());
    		if(!time) {
    		req.getSession().setAttribute(token,new Date());
    		}
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}else {
    		req.getSession().setAttribute(token,new Date());
    	}
        return time;
    }

    public String generateToken(LoginMaster user) {
        return doGenerateToken(user.getLoginId());
    }
      
   public String doGenerateToken(String loginid) {
        Claims claims = Jwts.claims().setSubject(loginid);
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://devglan.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3000000 )) 
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public Boolean validateToken(String token, UserDetails userDetails,HttpServletRequest req) {
        final String username = token;//getUsernameFromToken(token);
        return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token,req));
    }
}


