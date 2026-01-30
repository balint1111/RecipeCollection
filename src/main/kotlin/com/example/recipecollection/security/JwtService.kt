package com.example.recipecollection.security

import com.example.recipecollection.domain.UserRole
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.issuer}") private val issuer: String,
    @Value("\${jwt.audience}") private val audience: String,
    @Value("\${jwt.expiration-ms}") private val expirationMs: Long,
) {
    fun generateToken(username: String, roles: Set<UserRole>): String {
        val now = Date()
        val expiration = Date(now.time + expirationMs)
        return Jwts.builder()
            .subject(username)
            .issuer(issuer)
            .audience().add(audience).and()
            .claim("roles", roles.map { it.name })
            .issuedAt(now)
            .expiration(expiration)
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .compact()
    }

    fun extractUsername(token: String): String =
        parseToken(token).payload.subject

    fun extractRoles(token: String): List<String> {
        val roles = parseToken(token).payload["roles"]
        return when (roles) {
            is Collection<*> -> roles.filterIsInstance<String>()
            else -> emptyList()
        }
    }

    fun isTokenValid(token: String, username: String): Boolean =
        extractUsername(token) == username

    private fun parseToken(token: String) =
        Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .build()
            .parseSignedClaims(token)
}
