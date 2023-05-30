package app.syr.lib.Member.repository;

import app.syr.lib.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndDeleteDateIsNull(Long id);

    Optional<Member> findByUsername(String username);

    Optional<Member> findByUsernameAndDeleteDateIsNull(String username);
}
