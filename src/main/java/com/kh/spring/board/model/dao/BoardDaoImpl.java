package com.kh.spring.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;

@Repository
public class BoardDaoImpl implements BoardDao{

	@Autowired
	private SqlSessionTemplate session;

	/**
	 * RowBounds : myBatis가 제공하는 paging기능
	 * - offset : 건너뛸 행수
	 * - limit : 한페이지당 게시물수 - numPerPage
	 */
	@Override
	public List<Board> selectBoardList(Map<String, Object> param) {
		int cPage = (int)param.get("cPage");
		int limit = (int)param.get("numPerPage");
		int offset = (cPage - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		return session.selectList("board.selectBoardList", null, rowBounds);
	}

	@Override
	public int getTotalContents() {
		return session.selectOne("board.getTotalContents");
	}

	@Override
	public int boardEnroll(Board board) {
		return session.insert("board.boardEnroll", board);
	}

}
