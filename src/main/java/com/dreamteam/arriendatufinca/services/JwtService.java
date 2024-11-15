package com.dreamteam.arriendatufinca.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.entities.Cuenta;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.CuentaRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final CuentaRepository cuentaRepository;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long expiration;

    public String generateToken(Cuenta user, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getIdCuenta());
        claims.put("nombre", user.getNombreCuenta());
        claims.put("email", user.getEmail());
        claims.put("rol", rol);
        return createToken(claims, user.getEmail());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        Date expirationDate = extractExpiration(token);
        if (expirationDate.before(new Date())) {
            return false;
        }
        String username = extractEmail(token);
        return userDetails.getUsername().equals(username) && !expirationDate.before(new Date());
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public CuentaDTO extractCuenta(String token){
        Map<String, Object> claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        CuentaDTO cuenta = new CuentaDTO();
        cuenta.setIdCuenta((Integer) claims.get("id"));
        cuenta.setNombreCuenta((String) claims.get("nombre"));
        cuenta.setEmail((String) claims.get("email"));
        return cuenta;
    }

    public void verifyLoggedUser(CuentaDTO cuentaPeticion, String emailAutenticado){
        boolean isSameUser = false;
        Optional<Cuenta> cuentaAutenticada = cuentaRepository.findByEmail(emailAutenticado);
        if(!cuentaAutenticada.isEmpty() && cuentaAutenticada.get().getIdCuenta() != null){
            if( cuentaAutenticada.get().getIdCuenta().equals(cuentaPeticion.getIdCuenta())){
                isSameUser = true;
            }
        }
        if(!isSameUser){
            UtilityService.devolverUnuthorized(ManejadorErrores.ERROR_CUENTA_INCORRECTA);
        }
    }
}
