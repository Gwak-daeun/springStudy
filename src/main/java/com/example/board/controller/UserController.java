package com.example.board.controller;

import com.example.board.dto.LoginInfo;
import com.example.board.dto.User;
import com.example.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor // lombok이 자동으로 final변수를 초기화해주는 생성자를 만들게 된다.
public class UserController {

    private final UserService userService;

    //localhost:8080/userRegForm
    //classpath:/templates/userRegForm.html
    @GetMapping("/userRegForm")
    public String userRegForm() {
        return "userRegForm";
    }

    //회원정보를 저장한다.
    @PostMapping("/userReg")
    public String userReg(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        System.out.println("name: " + name);
        System.out.println("email: " + email);
        System.out.println("password: " + password);

        userService.addUser(name, email, password);

        return "redirect:/welcome";
        //리다이렉트: 이동시키라는 뜻
        //브라우저에게 자동으로 http://localhost:8080/welcome으로 이동하라는 뜻
        // /welcome을 서버에서 get 방식으로 처리할 수 있어야
    }

    //http://localhost:8080/welcome경로로 요청이 왔을 때
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/loginform")
    public String loginform() {
        return "loginform";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password, // 원래는 암호를 받을 때에도 암호화를 해야 하는데,
                                                    //spring security를 배우면서 그 때 적용해 볼 것 이다.
            HttpSession httpSession // spring이 자동으로 sessiong을 처리하는 httpSession 객체를 넣어줌
    ) {

        System.out.println("email: " + email);
        System.out.println("password: " + password);
        //email에 해당하는 회원정보를 읽어온 후 -> userService에 회원정보를 가지고 오는 기능이 필요하다.
        //아이디 암호가 맞다면 세션에 회원정보를 저장한다.

        try {
            User user = userService.getUser(email);
            // System.out.println(user); // lombok 어노테이션인 @toString이 호출됨
            if (user.getPassword().equals(password)) {
                System.out.println("암호가 같아요.");
                LoginInfo loginInfo = new LoginInfo(user.getUserId(), user.getEmail(), user.getName());
                httpSession.setAttribute("loginInfo", loginInfo); //첫번째 파라피터는 키, 두번째 파라미터는 값
                //세션에는 키, 값으로 여러개의 값을 저장할 수 있다.
                //키가 같으면 여러번 setAttribute()를 해도 나중의 값이 기존 값을 덮어쓰게 됨 => 여러번 로그인 시도를 하면 마지막 값만 저장됨
                //session은 현재 브라우저 사용자만 접근할 수 있다.
                //각각의 브라우저마다 세션이 내부적으로 각각 다르게 만들어진다고 보면 된다.
                //서버에 접속한 클라이언트가 10개면 세션도 10개인 것
                System.out.println("세션에 로그인 정보가 저장됨");
            } else {
                throw new RuntimeException("암호가 같지 않아요");
            }
        } catch (Exception ex) {
            return "redirect:/loginform?error=true"; // email에 해당하는 정보가 없으면 다시 로그인 폼으로 리다이렉트 하는데,
            // 파라미터로 에러는 true값을 담아서 보낸다.
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        //세션에서 회원정보를 삭제
        httpSession.removeAttribute("loginInfo"); // key에 해당하는 값을 지운다. 세션에서 값을 지워서
                                                        //로그아웃 처리가 되는 것
        return "redirect:/";
    }

    @GetMapping("/writeForm")
    public String writeFrom() {
        //로그인한 사용자만 글을 써야 한다.
        //세션에서 로그인한 정보를 읽어들인다. 로그인을 하지 않았다면 리스트 보기로 이동시킨다.

        return "writeFrom";
    }

    @PostMapping("/write")
    public String write(
            @RequestParam("title") String title,
            @RequestParam("content") String content
    ) {
        //로그인한 사용자만 글을 써야 한다.
        //세션에서 로그인한 정보를 읽어들인다. 로그인을 하지 않았다면 리스트 보기로 이동시킨다.

        //로그인 한 회원 정보 + 제목, 내용을 저장한다.

        return "redirect:/"; //리스트 보기로 리다이렉트한다.
    }
}
