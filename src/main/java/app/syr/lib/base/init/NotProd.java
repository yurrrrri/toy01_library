package app.syr.lib.base.init;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.service.MemberService;
import app.syr.lib.category.entity.Category;
import app.syr.lib.category.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(MemberService memberService, CategoryService categoryService) {
        return args -> {
            Member memberAdmin = memberService.create("ADMIN", "123456", "123456", "admin@test.com", "01011112222").getData();
            Member member1 = memberService.create("user1", "123456", "123456", "test1@test.com", "01011112222").getData();
            Member member2 = memberService.create("user2", "123456", "123456", "test2@test.com", "01011112222").getData();
            Member member3 = memberService.create("user3", "123456", "123456", "test3@test.com", "01011112222").getData();

            Category category000 = categoryService.create("000 총류").getData();
            Category category100 = categoryService.create("100 철학").getData();
            Category category200 = categoryService.create("200 종교").getData();
            Category category300 = categoryService.create("300 사회과학").getData();
            Category category400 = categoryService.create("400 자연과학").getData();
            Category category500 = categoryService.create("500 기술과학").getData();
            Category category600 = categoryService.create("600 예술").getData();
            Category category700 = categoryService.create("700 언어").getData();
            Category category800 = categoryService.create("800 문학").getData();
            Category category900 = categoryService.create("900 역사").getData();
        };
    }
}
