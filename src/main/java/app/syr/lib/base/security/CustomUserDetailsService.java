package app.syr.lib.base.security;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsernameAndDeleteDateIsNull(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        return new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
    }
}
