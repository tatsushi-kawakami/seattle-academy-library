package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 貸し出しサービス
 * 
 * rentalsテーブルに関する処理を実装する
 */
@Service
public class RentalsService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍番号を取得する
	 *
	 * @param bookId 書籍番号
	 * @return　selectedBookId
	 */
	public int selectBookInfo(int bookId) {
		// TODO SQL生成
		String sql = "select book_id from rentals where book_id = " + bookId;
		try {
			int selectedBookId = jdbcTemplate.queryForObject(sql, int.class);
			return selectedBookId;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * rentalsテーブルに書籍を登録する
	 *
	 * @param bookId 書籍番号
	 */
	public void registRentals(int bookId) {
		String sql = "INSERT INTO rentals (book_id) VALUES (" + bookId + ")";

		jdbcTemplate.update(sql);
	}

}