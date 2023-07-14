package com.example.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter // lombok 라이브러리가 getter, setter를 어노테이션으로 만들어준다.
@NoArgsConstructor // 기본 생성자가 자동으로 만들어진다.
public class User {
    private int userId;
    private String email;
    private String name;
    private String password;
    private LocalDateTime regDate; // 원래는 날짜 타입으로 읽어온 후 문자열로 변환해야 한다.


}

/*
* user_id, email, name, password, regdate => 유저가 가지는 db 필드. 이 정보들을 DTO에서 받아와서
* 저장해야 한다.
* */