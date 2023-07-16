package com.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor //lombok
public class LoginInfo {
    private int userId;
    private String email;
    private String name;


//    public LoginInfo(int userId, String email, String name) {
//        this.userId = userId;
//        this.email = email;
//        this.name = name;
//    }
//위의 생성자가 @AllArgsConstructor가 하는 기능과 같다
}
