package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 작성한 글 저장
    public void write(Board board, MultipartFile file) throws Exception {

        // 파일을 저장할 경로
        String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        // 랜덤으로 이름(식별자) 만들기
        UUID uuid = UUID.randomUUID();

        // 랜덤으로 식별자_원래파일이름
        String fileName = uuid + "_" + file.getOriginalFilename();

        // File 클래스를 이용해 파일경로와 파일이름을 담을 saveFile 생성
        File saveFile = new File(filePath, fileName);

        // 업로드한 파일을 saveFile에 담기
        file.transferTo(saveFile);

        board.setFilepath("/files/" + fileName);
        board.setFilename(fileName);

        boardRepository.save(board);
    }

    // 게시판 리스트 가져오기
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 특정 게시글 불러오기
    public Board boardDetail(Integer id) {
        return boardRepository.findById(id).get();
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }

    // 검색 기능
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
}
