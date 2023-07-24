package com.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
//@AllArgsConstructor //lombok. 모든 필드에 대한 생성자가 자동으로 만들어진다.
public class LoginInfo {
    private int userId;
    private String email;
    private String name;
    private List<String> roles = new ArrayList<>(); // 선언과 동시에 비어있는 ArrayList로 초기화

    public LoginInfo(int userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public void addRole(String roleName) {
        roles.add(roleName);
    }


//    public LoginInfo(int userId, String email, String name) {
//        this.userId = userId;
//        this.email = email;
//        this.name = name;
//    }
//위의 생성자가 @AllArgsConstructor가 하는 기능과 같다
}
