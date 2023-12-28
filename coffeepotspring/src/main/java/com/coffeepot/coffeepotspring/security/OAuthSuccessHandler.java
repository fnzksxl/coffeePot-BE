package com.coffeepot.coffeepotspring.security;

import static com.coffeepot.coffeepotspring.security.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.coffeepot.coffeepotspring.persistence.JwtRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final JwtRepository jwtRepository;
	
	private static final String LOCAL_REDIRECT_URL = "http://localhost:3000";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("auth succeeded");
		TokenProvider tokenProvider = new TokenProvider(jwtRepository);
		String accessToken = tokenProvider.createAccessToken(authentication);
		String refreshToken = tokenProvider.createRefreshToken(authentication);
		
		Optional<Cookie> oCookie = Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals(REDIRECT_URI_PARAM)).findFirst();
		Optional<String> redirectUri = oCookie.map(Cookie::getValue);
		
//		response.getWriter().write(token);
		log.info("accessToken {}, refreshToken {}", accessToken, refreshToken);
//		response.sendRedirect("http://localhost:3000/sociallogin?token=" + token);
		response.sendRedirect(redirectUri.orElseGet(() -> LOCAL_REDIRECT_URL)
				+ "/sociallogin?access_token=" + accessToken
				+ "&refresh_token=" + refreshToken);
	}

}
