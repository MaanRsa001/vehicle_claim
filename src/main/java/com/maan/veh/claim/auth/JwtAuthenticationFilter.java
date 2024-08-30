package com.maan.veh.claim.auth;

import static com.maan.veh.claim.auth.Constants.HEADER_STRING;
import static com.maan.veh.claim.auth.Constants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.maan.veh.claim.entity.SessionMaster;
import com.maan.veh.claim.repository.SessionMasterRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Lazy
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private SessionMasterRepository sessionRep;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	private String userType ;
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)	throws IOException, ServletException {

		String header = req.getHeader(HEADER_STRING);
		String username = null;
		String authToken = null;
		String loginId = null;
		SessionMaster table = new SessionMaster();
		HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
		if (header != null && header.startsWith(TOKEN_PREFIX)) {
			authToken = header.replace(TOKEN_PREFIX, "");
			table = sessionRep.findByTempTokenid(authToken);
			
			loginId = table.getLoginId();
			authToken = table.getTokenId();
			userType = table.getUserType() ;
	        requestWrapper.addHeader(HEADER_STRING, authToken);
			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);
				Date today = new Date();
				if(today.before(table.getEndTime())  ) {
					table.setStartTime(today);
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MINUTE, 50);
					Date endTime = cal.getTime();
					table.setEndTime(endTime );
					String token = jwtTokenUtil.doGenerateToken(table.getLoginId());
					table.setTokenId(token);
					sessionRep.save(table);
				}
			} catch (IllegalArgumentException e) {
				logger.error("an error occured during getting username from token", e);
			} catch (Exception e) {
				Date today = new Date();
				if(today.before(table.getEndTime())  ) {
					table.setStartTime(today);
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MINUTE, 50);
					Date endTime = cal.getTime();
					table.setEndTime(endTime );
					String token = jwtTokenUtil.doGenerateToken(table.getLoginId());
					table.setTokenId(token);
					sessionRep.save(table);
					username = jwtTokenUtil.getUsernameFromToken(authToken);
				} else {
					logger.warn("the token is expired and not valid anymore");
					username = table.getTokenId();
				}
			} 
		} else {
			logger.warn("couldn't find bearer string, will ignore the header");
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			//Added for getting companyid
			String combinedUsername = username + ":" + loginId;
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(combinedUsername);
			boolean validtoken = jwtTokenUtil.validateToken(username, userDetails,req);
			
			if(validtoken && table.getStatus().equals("DE-ACTIVE")) {
				validtoken = false;
				res.setHeader(username, "This session has been terminated. User logged in to a new session");
			}
			
			if (validtoken) {
							
				if(userType.equalsIgnoreCase("ADMIN")) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(requestWrapper));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
				} else if(userType.equalsIgnoreCase("SURVEYOR")) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_SURVEYOR")));
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(requestWrapper));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
				} else if (userType.equalsIgnoreCase("DEALER")) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_DEALER")));
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(requestWrapper));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else if (userType.equalsIgnoreCase("GARAGE")) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, Arrays.asList(new SimpleGrantedAuthority("GARAGE")));
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(requestWrapper));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
				
				
				
			}
		}
		chain.doFilter(requestWrapper, res);
	}
}