package com.leejongsul.todolist.security

import com.leejongsul.todolist.services.CustomUserDetailsService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val userDetailsService: CustomUserDetailsService
) {
    val key: SecretKey =
        Keys.hmacShaKeyFor("d8845362263c194a4ec58e4382690e7ae0d208ab6497fe55a0c3c2166a099abf".toByteArray())

    fun generate(nickname: String, expiredAt: Date): String {
        val now = Date()
        return Jwts.builder()
            .subject(nickname)
            .signWith(key)
            .issuedAt(now)
            .expiration(expiredAt)
            .compact()
    }

    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaimsFromToken(token)
        val userDetails = userDetailsService.loadUserByUsername(claims.subject)
        return UsernamePasswordAuthenticationToken(userDetails, null, AuthorityUtils.NO_AUTHORITIES)
    }

    fun isExpired(token: String): Boolean {
        val claims = getClaimsFromToken(token)
        return claims.expiration.before(Date())
    }
}
