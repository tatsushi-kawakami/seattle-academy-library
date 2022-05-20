package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.HistoryBookInfo;
import jp.co.seattle.library.rowMapper.HistoryBookInfoRowMapper;

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
	 * @return selectedBookId
	 */
	public boolean selectBookInfo(int bookId) {
		// TODO SQL生成
		String sql = "select case WHEN checkout_date notnull THEN true ELSE false end from rentals where book_id =" + bookId;
		boolean BookIdExists = jdbcTemplate.queryForObject(sql, boolean.class);
		return BookIdExists;
	}

	/**
	 * rentalsテーブルに書籍を登録する
	 *
	 * @param bookId 書籍番号
	 */
	public void registRentals(int bookId) {
		//try-catchでくくる（データがない場合にエラーが起きるため）
		String sql = "insert into  rentals (book_id,checkout_date) values ("+ bookId +",now())"
				+ "on conflict (book_id)"
				+ "do update set checkout_date = now(),return_date = null";
		jdbcTemplate.update(sql);
	}

	/**
	 * rentalsテーブルの書籍を削除する
	 *
	 * @param bookId 書籍番号
	 */
	public void deleteRentals(int bookId) {
		//返すと,もともとあるレコードの日にちだけ切り替える
		String sql = "update rentals set return_date = now(),checkout_date  = null where book_id = "+ bookId;
		jdbcTemplate.update(sql);
	}
	
	/**
	 * booksテーブルとrentalsテーブルを結合した書籍情報を取得する
	 *
	 * @param　貸し出し履歴リスト
	 */
	public List<HistoryBookInfo> getHistoryList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<HistoryBookInfo> historyBookList = jdbcTemplate.query(
				"select title, book_id, checkout_date, return_date from rentals LEFT JOIN books ON books.id = rentals.book_id ",
				new HistoryBookInfoRowMapper());

		return historyBookList;
	}
}