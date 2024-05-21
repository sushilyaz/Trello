package com.suhoi.demo.service.impl;

import com.suhoi.demo.dto.CardListCreateDto;
import com.suhoi.demo.dto.CardListUpdateDto;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.CardListMapper;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.BoardRepository;
import com.suhoi.demo.repository.CardListRepository;
import com.suhoi.demo.repository.UserRepository;
import com.suhoi.demo.service.CardListService;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CardListServiceImpl implements CardListService {

    private final UserUtils userUtils;
    private final CardListRepository cardListRepository;
    private final BoardRepository boardRepository;
    private final CardListMapper cardListMapper;
    private final UserRepository userRepository;

    @Override
    public CardList createCardList(CardListCreateDto dto, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DataNotFoundException("Board not found"));
        Set<User> moderators = board.getModerators();
        if (!moderators.contains(userUtils.getCurrentUser())) {
            throw new AccessPermissionDeniedException("You do not have permission to create card list, you not moderator");
        }
        CardList cardList = cardListMapper.map(dto);
        cardList.setBoard(board);
        Long id = userUtils.getCurrentUser().getId();
        User byId = userRepository.findById(id).get();
        Object unproxy = Hibernate.unproxy(byId);
        Hibernate.initialize(cardList.getBoard().getCreator());
        cardList.setCreator((User) unproxy);
        return cardListRepository.save(cardList);
    }

    @Override
    public List<CardList> findCardListByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new DataNotFoundException("Board not found"));
        Set<User> members = board.getMembers();
        if (!members.contains(userUtils.getCurrentUser())) {
            throw new AccessPermissionDeniedException("You do not have permission to get all card lists");
        }
        return cardListRepository.findByBoardId(boardId);
    }

    @Override
    public CardList findCardListById(Long boardId, Long id) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new DataNotFoundException("Board not found"));
        Set<User> members = board.getMembers();
        if (!members.contains(userUtils.getCurrentUser())) {
            throw new AccessPermissionDeniedException("You do not have permission to get all card lists");
        }
        return cardListRepository.findByBoardIdAndId(boardId, id)
                .orElseThrow(() -> new DataNotFoundException("CardList not found"));
    }

    @Override
    public void delete(Long boardId, Long id) {

        CardList cardList = cardListRepository.findByBoardIdAndId(boardId, id)
                .orElseThrow(() -> new DataNotFoundException("CardList not found"));
        if (cardList.getCreator().getId().equals(userUtils.getCurrentUser().getId())) {
            cardListRepository.delete(cardList);
        } else {
            throw new AccessPermissionDeniedException("You do not have permission to delete this card list");
        }
    }

    @Override
    public void update(CardListUpdateDto dto, Long boardId, Long id) {
        CardList cardList = cardListRepository.findByBoardIdAndId(boardId, id)
                .orElseThrow(() -> new DataNotFoundException("CardList not found"));
        if (cardList.getCreator().getId().equals(userUtils.getCurrentUser().getId())) {
            cardListMapper.update(dto, cardList);
            cardListRepository.save(cardList);
        } else {
            throw new AccessPermissionDeniedException("Permission denied");
        }
    }
}
