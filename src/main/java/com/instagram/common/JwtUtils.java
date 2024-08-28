//JwtUtils
package com.instagram.common;

import com.instagram.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

	private final Key hmacKey;
	private final Long expirationTime;

	public JwtUtils(Environment env) {
		String secret = env.getProperty("token.secret");
		String expiration = env.getProperty("token.expiration-time");

		// 디버깅 로그 추가
		log.info("Loaded token.secret: {}", secret);
		log.info("Loaded token.expiration-time: {}", expiration);

		if (secret == null || expiration == null) {
			throw new IllegalArgumentException("Token secret or expiration time is not set in the environment properties");
		}

		this.hmacKey = Keys.hmacShaKeyFor(secret.getBytes());
		this.expirationTime = Long.parseLong(expiration);
	}

	public String generateToken(String userId) {
		Instant now = Instant.now();
		String jwtToken = Jwts.builder()
				.claim("name", userId)
				.claim("email", "userEmail")
				.setSubject(userId)
				//.setId(String.valueOf(user.getId()))
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plusMillis(expirationTime)))
				.signWith(hmacKey) // SignatureAlgorithm을 제거하고 Key만 사용
				.compact();

		log.debug(jwtToken);
		return jwtToken;
	}

	//   private Claims getAllClaimsFromToken(String token) {
//      return Jwts.parser()
//            .setSigningKey(hmacKey)
//            .build()
//            .parseClaimsJws(token)
//            .getBody();
//   }
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder() // parserBuilder 메서드를 사용
				.setSigningKey(hmacKey) // 여전히 사용할 수 있음
				.build()
				.parseClaimsJws(token)
				.getBody(); // getBody()는 여전히 사용 가능
	}

	//   public String getSubjectFromToken(String token) {
//      final Claims claims = getAllClaimsFromToken(token);
//      return claims.getSubject();
//   }
	public String getSubjectFromToken(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(hmacKey)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
		} catch (JwtException e) {
			throw new RuntimeException("Failed to extract subject from token", e);
		}
	}

	private Date getExpirationDateFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.getExpiration();
	}

	private boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	//   public boolean validateToken(String token, User user) {
//      // 토큰 유효기간 체크
//      if (isTokenExpired(token)) {
//         return false;
//      }
//
//      // 토큰 내용을 검증
//      String subject = getSubjectFromToken(token);
//      String username = user.getUserName();
//
//      return subject != null && username != null && subject.equals(username);
//   }
	public boolean validateToken(String token, User user) {
		try {
			String userId = getSubjectFromToken(token);
			return (userId.equals(user.getUserId()) && !isTokenExpired(token));
		} catch (JwtException e) {
			return false;
		}
	}
}