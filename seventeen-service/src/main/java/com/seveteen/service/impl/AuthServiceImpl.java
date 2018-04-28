package com.seveteen.service.impl;

import com.seveteen.bean.core.SysUser;
import com.seveteen.config.JwtTokenConfig;
import com.seveteen.filter.JwtAuthenticationTokenFilter;
import com.seveteen.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtTokenConfig jwtTokenConfig;

	@Override
	public String login(String username, String password) {
		UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
		// Perform the security
		final Authentication authentication = authenticationManager.authenticate(upToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// Reload password post-security so we can generate token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		final String token = jwtTokenConfig.generateToken(userDetails);
		return token;
	}

	@Override
	public String refresh(String oldToken) {
		final String token = oldToken.substring(JwtAuthenticationTokenFilter.TOKEN_HEAD.length());
		String username = jwtTokenConfig.getUsernameFromToken(token);
		SysUser user = (SysUser) userDetailsService.loadUserByUsername(username);
		if (jwtTokenConfig.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			return jwtTokenConfig.refreshToken(token);
		}
		return null;
	}
}