package jp.co.seattle.library.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"select id, title, thumbnail_url, author, publisher, publish_date from books order by title",
				new BookInfoRowMapper());

		return getedBookList;
	}

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT books.id, books.title, books.author, books.publisher, books.publish_date,books.thumbnail_name,books.thumbnail_url, books.isbn,books.explanation,rentals.book_id "
				+ "FROM books LEFT OUTER JOIN  rentals ON (books.id = rentals.book_id) "
				+ "where books.id ="+ bookId;

		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}

	/**
	 * 書籍を登録する
	 *
	 * @param bookInfo 書籍情報
	 */
	public void registBook(BookDetailsInfo bookInfo) {
		String sql = "INSERT INTO books (title,author,publisher,publish_date,thumbnail_name,thumbnail_url,reg_date,upd_date,isbn,explanation) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getPublishDate() + "','" + bookInfo.getThumbnailName() + "','" + bookInfo.getThumbnailUrl()
				+ "','" + "now()" + "','" + "now()" + "','" + bookInfo.getIsbn() + "','" + bookInfo.getExplanation()
				+ "');";

		jdbcTemplate.update(sql);
	}

	public int max_id() {
		String sql = "select max(id) from books;";
		return jdbcTemplate.queryForObject(sql, int.class);
	}

	/**
	 * 書籍を更新する
	 *
	 * @param bookInfo 書籍情報
	 */
	public void updateBook(BookDetailsInfo bookInfo) {
		if (bookInfo.getThumbnailUrl() == "null") {
			String sql = "update books set title= '" + bookInfo.getTitle() + "', author= '" + bookInfo.getAuthor()
					+ "', publisher='" + bookInfo.getPublisher() + "', publish_date='" + bookInfo.getPublishDate()
					+ "',isbn='" + bookInfo.getIsbn() + "',upd_date= now(),explanation ='" + bookInfo.getExplanation()
					+ "' where id=" + bookInfo.getBookId();

			jdbcTemplate.update(sql);
		} else {
			String sql = "update books set title= '" + bookInfo.getTitle() + "', author= '" + bookInfo.getAuthor()
					+ "', publisher='" + bookInfo.getPublisher() + "', publish_date='" + bookInfo.getPublishDate()
					+ "', thumbnail_name='" + bookInfo.getThumbnailName() + "',thumbnail_url='"
					+ bookInfo.getThumbnailUrl() + "',isbn='" + bookInfo.getIsbn() + "',upd_date= now(),explanation ='"
					+ bookInfo.getExplanation() + "' where id=" + bookInfo.getBookId();

			jdbcTemplate.update(sql);
		}

	}

	/**
	 * 書籍を削除する
	 *
	 * @param bookId 書籍番号
	 */
	public void deleteBook(Integer bookId) {
		String sql = "DELETE FROM books WHERE id=" + bookId + ";";
		jdbcTemplate.update(sql);
	}

	public List<String> errorList(String title, String author, String publisher, String publishDate, String isbn) {
		List<String> list = new ArrayList<String>();

		if (title.equals("") || author.equals("") || publisher.equals("") || publishDate.equals("")) {
			list.add("<p>必須項目に入力してください</p>");
		}
		if (!(publishDate.matches("^[0-9]{8}"))) {
			list.add("<p>出版日は半角数字のYYYYMMDD形式で入力してください</p>");
		}
		if (!(isbn.equals("")) && !(isbn.matches("^[0-9]{10}|[0-9]{13}$"))) {
			list.add("<p>ISBNの桁数または半角数字が正しくありません</p>");
		}
		return list;
	}
}
