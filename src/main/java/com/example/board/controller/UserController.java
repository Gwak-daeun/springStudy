package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

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
            @RequestParam("password") String password
    ) {

        System.out.println("email: " + email);
        System.out.println("password: " + password);
        //email에 해당하는 회원정보를 읽어온 후
        //아이디 암호가 맞다면 세션에 회원정보를 저장한다.
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        //세션에서 회원정보를 삭제

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
