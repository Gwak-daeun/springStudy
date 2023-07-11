package com.example.board.service;


import com.example.board.dao.UserDao;
import com.example.board.dto.User;
import org.springframework.stereotype.Service;

//트랜잭션 단위로 실행될 메소드를 선언하고 있는 클래스
@Service // @Service도 @Component를 상속받고 있어서 스프링이 관리하는 Bean이다.
public class UserService {

    private final UserDao userDao; // 이렇게 인젝션 되는 dao는 final로 선언하는게 좋다.
    //final로 선언된 것은 반드시 생성자 주입을 해야 한다.

    //Spring이 UserService를 Bean으로 생성할 때 생성자를 이용해 생성을 하는데,
    //이때 UserDao Bean이 있는지 보고 그 빈을 주입한다. 생성자 주입
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User addUser(String name, String email, String password) {



        User user = new User();



        return user;
    }
}
/*
        * insert into user(email, name, password, regdate) values(?, ?, ?, now());
        SELECT LAST_INSERT_ID();
        insert into user_role(user_id, role_id) values(? 1-일반 유저 role);
        * */
//위의 세 가지 쿼리를 실행해서 User 객체에 담은 후 리턴하자
//sql 이 실행되는 객체를 dao라고 한다.