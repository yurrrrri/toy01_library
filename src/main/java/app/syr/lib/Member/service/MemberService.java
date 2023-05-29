package app.syr.lib.Member.service;

import app.syr.lib.Loan.entity.Loan;
import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.repository.MemberRepository;
import app.syr.lib.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public RsData<Member> create(String username, String password1, String password2, String email, String phoneNumber) {
        RsData rs = canBorrow(username, password1, password2);

        if (rs.isFail()) return rs;

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

    private RsData canBorrow(String username, String password1, String password2) {
        if (findByUsername(username) != null) {
            return RsData.of("F-1", "이미 존재하는 아이디입니다.");
        }

        if (!password1.equals(password2)) {
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

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public RsData<Member> modify(Member member, String password, String email, String phoneNumber) {
        Member modifiedMember = member
                .toBuilder()
                .password(passwordEncoder.encode(password))
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        String username = modifiedMember.getUsername();
        memberRepository.save(modifiedMember);
        return RsData.of("S-1", "%s 회원의 정보가 수정되었습니다.".formatted(username), modifiedMember);
    }

    // soft-delete
    public RsData delete(Member member) {
        String username = member.getUsername();
        Member member1 = member
                .toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        memberRepository.save(member1);
        return RsData.of("S-1", "%s 회원의 탈퇴가 완료되었습니다.".formatted(username));
    }

    // hard-delete
    public RsData deleteHard(Member member) {
        String username = member.getUsername();
        memberRepository.delete(member);
        return RsData.of("S-1", "%s 회원이 삭제되었습니다.".formatted(username));
    }

    public void whenAfterLoan(Loan loan) {
        Member member = loan.getMember();
        List<Loan> listAfterAdd = member.getLoanList();
        listAfterAdd.add(loan);

        Member member1 = member
                .toBuilder()
                .loanList(listAfterAdd)
                .build();
        memberRepository.save(member1);
    }

    public void whenAfterReturn(Loan loan) {
        Member member = loan.getMember();
        List<Loan> listAfterReturn = member.getLoanList();
        listAfterReturn.remove(loan);

        Member member1 = member
                .toBuilder()
                .loanList(listAfterReturn)
                .build();
        memberRepository.save(member1);
    }
}
