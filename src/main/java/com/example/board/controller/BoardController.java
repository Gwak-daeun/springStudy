package com.example.board.controller;

import com.example.board.dto.Board;
import com.example.board.dto.LoginInfo;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

//브라우저(HTTP요청)의 요청을 받아서 응답하는 컴포넌트
//스프링 부트가 자동으로 Bean으로 생성한다.(@Controller의 부모 중 @Component가 있는 부모가 있어서)
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시물 목록을 보여준다.
    //컨트롤러의 메소드가 리턴하는 문자열은 템플릿 이름이다.
    //http://localhost:8080/ -> 브라우저에서 요청 -> "list"라는 이름의 템플릿을 사용(포워딩)하여 화면에 출력
    //list를 리턴한다는 것은
    //classpath:/templates/list.html을 사용한다는 것이다. <- Thymeleaf 의 기본 설정
    @GetMapping("/") // "/"라는 요청이 오면 아래의 메소드를 실행한다.
    public String list(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model){ // httpSession과 Model은 스프링이 자동으로 넣어준다.
                                                        //Model은 템플릿에다가 return으로 값을 넘기기 위한 객체
    //게시물 목록을 읽어온다. 페이징 처리한다.
      LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
      //getAttribute()는 Object타입을 리턴하기 때문에 LoginInfo객체로 형변환을 해줘야 함
        model.addAttribute("loginInfo", loginInfo); // 템플릿("list")에 "loginInfo"라는 키값으로 객체를 넘기게 된다.
                                                                // 현재 템플릿 엔진 : ThymeLeaf

        int totalCount = boardService.getTotalCount();

        List<Board> list = boardService.getBoards(page); // page는 1, 2, 3, 4 ...

        int pageCount = totalCount / 10;
        if (totalCount % 10 > 0) { // 나머지가 있을 경우 1page 추가
            pageCount++;
        }

        int currentPage = page;

//        System.out.println("토탈 카운드 : " + totalCount);
//        for (Board board : list) {
//            System.out.println(board);
//        }
        model.addAttribute("list", list);
        model.addAttribute("pageCount", pageCount); // 전체 페이지 수
        model.addAttribute("currentPage", currentPage); // 현재 페이지 수
    return "list";
    }

    @GetMapping("/writeForm")
    public String writeFrom(HttpSession session, Model model) {
        //로그인한 사용자만 글을 써야 한다. -> 세션의 정보를 읽어들어야 한다.
        //세션에서 로그인한 정보를 읽어들인다. 로그인을 하지 않았다면 리스트 보기로 이동시킨다.

        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if (loginInfo == null) { // 세션에 로그인 정보가 없으면 /loginform으로 리다이렉트
            return "redirect:/loginform";
        }

        model.addAttribute("loginInfo", loginInfo);
        return "writeForm";
    }

    // /board?id=3 // 파라미터 id, 파라미터 id의 값은 3
    //id에 해당하는 게시물을 읽어온다.
    //id에 해당하는 게시물의 조회수도 1증가한다.
    @GetMapping("/board")
    public String board(@RequestParam("boardId") int boardId, Model model){
                        //url의 id 뒤에 있는 값을 자동으로 넘겨받게 된다.

       Board board = boardService.getBoard(boardId);

        model.addAttribute("board", board);

        System.out.println("boardId: " + boardId);
        return "board";
    }

    //삭제한다. 관리자는 모든 글을 삭제할 수 있다.
    //수정한다.

    @PostMapping("/write")
    public String write(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session,
            Model model
    ) {
        //로그인한 사용자만 글을 써야 한다.
        //세션에서 로그인한 정보를 읽어들인다. 로그인을 하지 않았다면 리스트 보기로 이동시킨다.

        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if (loginInfo == null) { // 세션에 로그인 정보가 없으면 /loginform으로 리다이렉트
            return "redirect:/loginform";
        }

        boardService.addBoard(loginInfo.getUserId(), title, content);

        //로그인 한 회원 정보 + 제목, 내용을 저장한다.

        return "redirect:/"; //리스트 보기로 리다이렉트한다.
    }

    @GetMapping("/delete")
    public String delete(
            @RequestParam int boardId,
            HttpSession session
    ) {
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if (loginInfo == null) {// 세션에 로그인 정보가 없으면 /loginform으로 리다이렉트
            return "redirect:/loginform";
        }

        //loginInfo.getUserId() 사용자가 쓴 글일 경우에만 삭제한다.
        boardService.deleteBoard(loginInfo.getUserId(), boardId);

        return "redirect:/";
    }

}
