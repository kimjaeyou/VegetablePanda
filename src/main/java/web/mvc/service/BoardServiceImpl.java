package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Board;
import web.mvc.dto.BoardReq;
import web.mvc.dto.BoardRes;
import web.mvc.exception.BoardSearchNotException;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.BoardRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{
	private final BoardRepository boardRepository;

	@Transactional
	public Board addBoard(BoardReq boardReq) {
		System.out.println("boardReq = " + boardReq);

		Board  board = boardReq.toBoard(boardReq);
		System.out.println("board = " + board.toString());
		return boardRepository.save(board);
	}
	

	@Transactional(readOnly = true)
	public Board findBoard(Long id) throws BoardSearchNotException {
		return boardRepository.findById(id)
				.orElseThrow(()->new BoardSearchNotException(ErrorCode.NOTFOUND_NO));

	}
	
	@Transactional(readOnly = true)
	//public List<Board> findAllBoard() throws BoardSearchNotException {
	public List<BoardRes> findAllBoard() throws BoardSearchNotException {
		//List<Board> list = boardRepository.findAll();
		List<Board> list = boardRepository.join();

		//Lis<Board>
		System.out.println("--------------------------------------------");
		if(list ==null || list.isEmpty())
			throw new BoardSearchNotException(ErrorCode.NOTFOUND_NO);

		return list.stream().map(BoardRes::new).collect(Collectors.toList());
	}
	
	@Transactional
	public Board updateBoard(Long id,BoardReq board)  throws DMLException {

		Board boardEntity = boardRepository.findById(id)
				      .orElseThrow(()->new DMLException(ErrorCode.UPDATE_FAILED));
		
		boardEntity.setTitle(board.getTitle());
		boardEntity.setContent(board.getContent());
		return boardEntity;
	}
	
	@Transactional
	public String deleteBoard(Long id) {
		System.out.println("id = " + id);
		boardRepository.findById(id).orElseThrow(()->
				new DMLException(ErrorCode.DELETE_FAILED));

		boardRepository.deleteById(id);

		return "ok";
	}
}
