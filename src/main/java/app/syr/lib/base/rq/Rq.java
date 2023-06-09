package app.syr.lib.base.rq;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequestScope
public class Rq {
    private final MemberService memberService;
    private final MessageSource messageSource;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final User user;
    private final Member member = null;

    public Rq(MemberService memberService, MessageSource messageSource, HttpServletRequest req, HttpServletResponse resp) {
        this.memberService = memberService;
        this.messageSource = messageSource;
        this.req = req;
        this.resp = resp;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        this.user = authentication.getPrincipal() instanceof User ? (User) authentication.getPrincipal() : null;
    }

    public boolean isLogin() {
        return user != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public boolean isAdmin() {
        return getMember() != null && getMember().getUsername().equals("ADMIN");
    }

    public Member getMember() {
        if (isLogout()) return null;

        return member != null ? member : memberService.findByUsername(user.getUsername());
    }

    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg__" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "common/js";
    }

    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }

    private String urlWithMsg(String url, String msg) {
        return Url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }

    private String msgWithTtl(String msg) {
        return Url.encode(msg) + ";ttl=" + new Date().getTime();
    }

    public static class Url {

        public static String encode(String str) {
            return URLEncoder.encode(str, StandardCharsets.UTF_8);
        }

        public static String modifyQueryParam(String url, String paramName, String paramValue) {
            url = deleteQueryParam(url, paramName);
            url = addQueryParam(url, paramName, paramValue);

            return url;
        }

        public static String addQueryParam(String url, String paramName, String paramValue) {
            if (!url.contains("?")) url += "?";

            if (!url.endsWith("?") && !url.endsWith("&")) url += "&";

            url += paramName + "=" + paramValue;

            return url;
        }

        private static String deleteQueryParam(String url, String paramName) {
            int startPoint = url.indexOf(paramName + "=");

            if (startPoint == -1) return url;

            int endPoint = url.substring(startPoint).indexOf("&");

            if (endPoint == -1) {
                return url.substring(0, startPoint - 1);
            }

            String urlAfter = url.substring(startPoint + endPoint + 1);

            return url.substring(0, startPoint) + urlAfter;
        }
    }

}
