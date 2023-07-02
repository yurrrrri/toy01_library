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

import static app.syr.lib.Member.controller.MemberController.MemberCreateForm;
import static app.syr.lib.Member.controller.MemberController.MemberModifyForm;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public RsData<Member> create(MemberCreateForm form) {
        RsData rs = canCreate(form.getUsername(), form.getPassword1(), form.getPassword2());

        if (rs.isFail()) return rs;

        Member member = Member
                .builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword1()))
                .email(form.getEmail())
                .phoneNumber(form.getPhoneNumber())
                .build();

        memberRepository.save(member);
        return RsData.of("S-1", "회원가입 되었습니다.", member);
    }

    private RsData canCreate(String username, String password1, String password2) {
        if (findByUsername(username) != null) {
            return RsData.of("F-1", "이미 존재하는 아이디입니다.");
        }

        if (!password1.equals(password2)) {
            return RsData.of("F-2", "2개의 비밀번호가 일치하지 않습니다.");
        }

        return RsData.of("S-1", "회원가입이 가능합니다.");
    }

    public RsData<Member> modify(Member member, MemberModifyForm form) {
        Member modifiedMember = member
                .toBuilder()
                .password(passwordEncoder.encode(form.getPassword1()))
                .email(form.getEmail())
                .phoneNumber(form.getPhoneNumber())
                .build();

        String username = modifiedMember.getUsername();
        memberRepository.save(modifiedMember);
        return RsData.of("S-1", "%s 회원의 정보가 수정되었습니다.".formatted(username), modifiedMember);
    }

    // soft-delete
    public RsData delete(Member member) {
        String username = member.getUsername();
        Member deletedMember = member
                .toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        memberRepository.save(deletedMember);
        return RsData.of("S-1", "%s 회원의 탈퇴가 완료되었습니다.".formatted(username));
    }

    // hard-delete
    public RsData deleteHard(Member member) {
        String username = member.getUsername();
        memberRepository.delete(member);
        return RsData.of("S-1", "%s 회원이 삭제되었습니다.".formatted(username));
    }

    public void whenBeforeLoan(Member member) {
        LocalDateTime now = LocalDateTime.now();
        if (member.getTimeout() == null)
            member.setCannotUse(false);
        else if (now.isAfter(member.getTimeout())) {
            member.setCannotUse(false);
        }
    }

    public void whenAfterReturn(Loan loan) {
        if (loan.isOverdue()) {
            loan.getMember().setTimeout();
            loan.getMember().setCannotUse(true);
        }
    }
}
