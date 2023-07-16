package com.example.board.controller;

import com.example.board.dto.LoginInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

//브라우저(HTTP요청)의 요청을 받아서 응답하는 컴포넌트
//스프링 부트가 자동으로 Bean으로 생성한다.(@Controller의 부모 중 @Component가 있는 부모가 있어서)
@Controller
public class BoardController {
    //게시물 목록을 보여준다.
    //컨트롤러의 메소드가 리턴하는 문자열은 템플릿 이름이다.
    //http://localhost:8080/ -> 브라우저에서 요청 -> "list"라는 이름의 템플릿을 사용(포워딩)하여 화면에 출력
    //list를 리턴한다는 것은
    //classpath:/templates/list.html을 사용한다는 것이다. <- Thymeleaf 의 기본 설정
    @GetMapping("/") // "/"라는 요청이 오면 아래의 메소드를 실행한다.
    public String list(HttpSession session, Model model){ // httpSession과 Model은 스프링이 자동으로 넣어준다.
                                                        //Model은 템플릿에다가 return으로 값을 넘기기 위한 객체
    //게시물 목록을 읽어온다. 페이징 처리한다.
      LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
      //getAttribute()는 Object타입을 리턴하기 때문에 LoginInfo객체로 형변환을 해줘야 함
        model.addAttribute("loginInfo", loginInfo); // 템플릿("list")에 "loginInfo"라는 키값으로 객체를 넘기게 된다.
                                                                // 현재 템플릿 엔진 : ThymeLeaf
    return "list";
    }

    // /board?id=3 // 파라미터 id, 파라미터 id의 값은 3
    //id에 해당하는 게시물을 읽어온다.
    //id에 해당하는 게시물의 조회수도 1증가한다.
    @GetMapping("/board")
    public String board(@RequestParam("id") int id){
                        //url의 id 뒤에 있는 값을 자동으로 넘겨받게 된다.

        System.out.println("id: " + id);
        return "board";
    }

    //삭제한다. 관리자는 모든 글을 삭제할 수 있다.
    //수정한다.


}
