package com.packt.footballresource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class AadJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Collection<String> WELL_KNOWN_SCOPE_CLAIM_NAMES = Arrays.asList("scope", "scp");
    private static final Collection<String> WELL_KNOWN_ROLE_CLAIM_NAMES = Arrays.asList("roles");

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Collection<String> tokenScopes = parseMultipleValuesClaim(source, WELL_KNOWN_SCOPE_CLAIM_NAMES);
        Collection<String> tokenRoles = parseMultipleValuesClaim(source, WELL_KNOWN_ROLE_CLAIM_NAMES);
        if (tokenScopes.isEmpty() && tokenRoles.isEmpty()) {
            return Collections.emptyList();
        }
        return Stream.concat(
                tokenScopes
                        .stream()
                        .map(s -> "SCOPE_" + s)
                        .map(SimpleGrantedAuthority::new),
                tokenRoles
                        .stream()
                        .map(s -> "ROLE_" + s)
                        .map(SimpleGrantedAuthority::new))
                .collect(Collectors.toCollection(HashSet::new));
    }

    private Collection<String> parseMultipleValuesClaim(Jwt jwt, Collection<String> claimNames) {
        String scopeClaim;

        scopeClaim = claimNames.stream()
                .filter(claim -> jwt.hasClaim(claim))
                .findFirst()
                .orElse(null);

        if (scopeClaim == null) {
            return Collections.emptyList();
        }

        Object v = jwt.getClaim(scopeClaim);
        if (v == null) {
            return Collections.emptyList();
        }

        if (v instanceof String) {
            return Arrays.asList(v.toString().split(" "));
        } else if (v instanceof Collection) {
            return ((Collection<?>) v).stream()
                    .map(s -> s.toString())
                    .collect(Collectors.toCollection(HashSet::new));
        }
        return Collections.emptyList();

    }

}
