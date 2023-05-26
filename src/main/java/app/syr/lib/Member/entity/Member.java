package app.syr.lib.Member.entity;

import app.syr.lib.base.entity.BaseEntity;
import app.syr.lib.borrow.entity.Borrow;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username;

    private String password;

    @Email
    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Borrow> borrowList = new ArrayList<>();

    private LocalDateTime timeout; // 연체했을 때 대출 가능해지는 시간 저장

    private boolean cannotUse;

    public void setCannotUse() {
        this.cannotUse = true;
        setTimeout();
    }

    public void setTimeout() {
        LocalDateTime now = LocalDateTime.now();
        timeout = now.plusDays(14L); // 2주 동안 대출 불가
    }

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("MEMBER"));

        if ("ADMIN".equals(username)) grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

        return grantedAuthorities;
    }
}
