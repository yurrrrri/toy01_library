package app.syr.lib.Member.service;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String password, String email) {
        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        memberRepository.save(member);
        return member;
    }

    public Member findById(Long id) {
        Optional<Member> member = memberRepository.findById(id);

        if(member.isEmpty()) return null;

        return member.get();
    }

    public Member modify(Member member, String password, String email) {
        Member modifiedMember = member
                .toBuilder()
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        memberRepository.save(modifiedMember);
        return modifiedMember;
    }

    // soft-delete
    public void delete(Member member) {
        member.toBuilder().deleteDate(LocalDateTime.now()).build();
    }

    // hard-delete
    public void hardDelete(Member member) {
        memberRepository.delete(member);
    }

}
