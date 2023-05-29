package app.syr.lib.Member.service;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.repository.MemberRepository;
import app.syr.lib.base.rsData.RsData;
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

    public RsData<Member> create(String username, String password1, String password2, String email, String phoneNumber) {
        RsData canCreateRs = canCreate(username, password1, password2, email, phoneNumber);

        if(canCreateRs.isFail()) return canCreateRs;

        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password1))
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        memberRepository.save(member);
        return RsData.of("S-1", "회원가입 되었습니다.", member);
    }

    private RsData canCreate(String username, String password1, String password2, String email, String phoneNumber) {
        if(findByUsername(username) != null) {
            return RsData.of("F-1", "이미 존재하는 아이디입니다.");
        }

        if(!password1.equals(password2)) {
            return RsData.of("F-2", "2개의 비밀번호가 일치하지 않습니다.");
        }

        return RsData.of("S-1", "회원가입이 가능합니다.");
    }

    public Member findById(Long id) {
        Optional<Member> member = memberRepository.findById(id);

        if (member.isEmpty()) return null;

        return member.get();
    }

    public Member findByUsername(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()) return null;

        return member.get();
    }

    public Member modify(Member member, String password, String email, String phoneNumber) {
        Member modifiedMember = member
                .toBuilder()
                .password(passwordEncoder.encode(password))
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        memberRepository.save(modifiedMember);
        return modifiedMember;
    }

    // soft-delete
    public String delete(Member member) {
        String username = member.getUsername();
        Member member1 = member
                .toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        memberRepository.save(member1);
        return username;
    }

    // hard-delete
    public String hardDelete(Member member) {
        String username = member.getUsername();
        memberRepository.delete(member);
        return username;
    }

}
