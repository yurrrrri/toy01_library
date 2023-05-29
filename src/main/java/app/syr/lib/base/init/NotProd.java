package app.syr.lib.base.init;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(MemberService memberService) {
        return args -> {
            Member memberAdmin = memberService.create("ADMIN", "123456", "123456", "admin@test.com", "01011112222").getData();
            Member member1 = memberService.create("user1", "123456", "123456", "test1@test.com", "01011112222").getData();
            Member member2 = memberService.create("user2", "123456", "123456", "test2@test.com", "01011112222").getData();
            Member member3 = memberService.create("user3", "123456", "123456", "test3@test.com", "01011112222").getData();
        };
    }
}
