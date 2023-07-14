package com.example.board.service;


import com.example.board.dao.UserDao;
import com.example.board.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//트랜잭션 단위로 실행될 메소드를 선언하고 있는 클래스
@Service // @Service도 @Component를 상속받고 있어서 스프링이 관리하는 Bean이다.
@RequiredArgsConstructor//lombok이 final한 변수를 초기화해주는 생성자를 쉽게 자동으로 만들어준다.
public class UserService {

    private final UserDao userDao; // 이렇게 인젝션 되는 dao는 final로 선언하는게 좋다.
    //final로 선언된 것은 반드시 생성자를 통해 초기화가 돼야 한다.

    //만약, final 변수가 여러개라면 생성자에서 final 변수를 인자로 변수 수 만큼 넣어서 초기화 해줘야함

    //Spring이 UserService를 Bean으로 생성할 때 생성자를 이용해 생성을 하는데,
    //이때 UserDao Bean이 있는지 보고 그 빈을 주입한다. 생성자 주입
//    public UserService(UserDao userDao) {
//        this.userDao = userDao;
//        //userDao가 들어와서 final 변수가 초기화 된다.
//    }

    //이러한 매소드가 하나의 트랜잭션으로 동작한다.
    //보통 서비스에서는 @Transactional을 붙여서 하나의 트랜잭션으로 처리하게 한다.
    //Spring boot는 트랜잭션을 처리해주는 트랜잭션 관리자를 가지고 있다.
    @Transactional // 트랜잭션으로 동작하기 위한 어노테이션
    public User addUser(String name, String email, String password) { // 트랜잭션이 시작된다.
        User user = userDao.addUser(email, name, password);
        userDao.mappingUserRole(user.getUserId()); // 권한 부여

        return user;
        //리턴 후 트랜잭션이 끝난다.
    }

    @Transactional
    public User getUser(String email) {
        return userDao.getUser(email);
    }

}
/*
        * insert into user(email, name, password, regdate) values(?, ?, ?, now());
        SELECT LAST_INSERT_ID();
        insert into user_role(user_id, role_id) values(? 1-일반 유저 role);
        * */
//위의 세 가지 쿼리를 실행해서 User 객체에 담은 후 리턴하자
//sql 이 실행되는 객체를 dao라고 한다.