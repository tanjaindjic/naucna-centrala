package master.naucnacentrala.security;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import master.naucnacentrala.model.korisnici.Korisnik;


public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Korisnik user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getIme(),
                user.getPrezime(),
                user.getEmail(),
                user.getPass(),
                null,
                true,
                null
        );
    }

	/*
	 * private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority>
	 * authorities) { return authorities.stream() .map(authority -> new
	 * SimpleGrantedAuthority(authority.getName().name()))
	 * .collect(Collectors.toList()); }
	 */
}
