package com.example.board.service;

import com.example.board.dao.BoardDao;
import com.example.board.dto.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardDao boardDao;

    @Transactional // Service가 갖고있는 메소드는 하나의 트랜잭션 단위로 동작해야 한다.
    public void addBoard(int userId, String title, String content) {
        boardDao.addBoard(userId, title, content);
    }

    @Transactional(readOnly = true) // 조회(select)만 할 땐 트랜잭션 어노테이션 옆에 readOnly=true를 붙이는게 성능상 좋다.
    public int getTotalCount() {
        return boardDao.getTotalCount();
    }

    @Transactional(readOnly = true)
    public List<Board> getBoards(int page) {
        return boardDao.getBoards(page);
    }

    @Transactional // 이 메소드에서는 readOnly를 붙이지 않는다. 게시물의 조회수를 수정해야 하기 때문
    public Board getBoard(int boardId) {
        //id에 해당하는 게시물을 읽어온다.
      Board board =  boardDao.getBoard(boardId);

        //id에 해당하는 게시물의 조회수도 1증가한다.
        boardDao.updateViewCount(boardId);
        return board;

    }

    @Transactional
    public void deleteBoard(int userId, int boardId) {
        Board board =  boardDao.getBoard(boardId);
        if (board.getBoardId() == userId) {
            boardDao.deleteBoard(boardId);
        }
    }
}
