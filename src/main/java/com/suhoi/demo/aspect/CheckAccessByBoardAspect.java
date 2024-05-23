package com.suhoi.demo.aspect;

import com.suhoi.demo.annotation.CheckAccessByBoard;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.BoardRepository;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckAccessByBoardAspect {
    private final BoardRepository boardRepository;
    private final UserUtils userUtils;

    @Before("@annotation(checkAccessByBoard)")
    public void checkAccess(JoinPoint joinPoint, CheckAccessByBoard checkAccessByBoard) {
        Object[] args = joinPoint.getArgs();
        Long boardId = null;

        for (Object arg : args) {
            if (arg instanceof Long) {
                boardId = (Long) arg;
                break;
            }
        }

        if (boardId == null) {
            throw new IllegalArgumentException("Board ID not found in method arguments");
        }
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DataNotFoundException("Board not found"));
        User currentUser = userUtils.getCurrentUser();

        switch (checkAccessByBoard.value()) {
            case MODERATOR:
                if (!board.getModerators().contains(currentUser)) {
                    throw new AccessPermissionDeniedException("You are not moderator");
                }
                break;
            case MEMBER:
                if (!board.getMembers().contains(currentUser)) {
                    throw new AccessPermissionDeniedException("You are not member of board");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown access type");
        }
    }
}
