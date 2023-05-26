package app.syr.lib.Member.repository;

import app.syr.lib.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
