package jp.co.seattle.library.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class EditBookController {
	final static Logger logger = LoggerFactory.getLogger(EditBookController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private ThumbnailService thumbnailService;
	
	@Autowired
    private BooksService bookdService;

	@RequestMapping(value = "/editBook", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String detailsBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome detailsControler.java! The client locale is {}.", locale);

        model.addAttribute("bookDetailsInfo", bookdService.getBookInfo(bookId));

        return "editBook";
    }
	

	/**
	 * 書籍情報を更新する
	 * 
	 * @param locale    ロケール情報
	 * @param title     書籍名
	 * @param author    著者名
	 * @param publisher 出版社
	 * @param file      サムネイルファイル
	 * @param model     モデル
	 * @return 遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/editBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String editBook(Locale locale,@RequestParam("bookId") Integer bookId, @RequestParam("title") String title, @RequestParam("author") String author,
			@RequestParam("publisher") String publisher, @RequestParam("publishDate") String publishDate,
			@RequestParam("thumbnail") MultipartFile file, @RequestParam("isbn") String isbn,
			@RequestParam("explanation") String explanation, Model model) {
		logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

		// パラメータで受け取った書籍情報をDtoに格納する。
		BookDetailsInfo bookInfo = new BookDetailsInfo();
		bookInfo.setBookId(bookId);
		bookInfo.setTitle(title);
		bookInfo.setAuthor(author);
		bookInfo.setPublisher(publisher);
		bookInfo.setPublishDate(publishDate);
		bookInfo.setIsbn(isbn);
		bookInfo.setExplanation(explanation);

		// クライアントのファイルシステムにある元のファイル名を設定する
		String thumbnail = file.getOriginalFilename();

		if (!file.isEmpty()) {
			try {
				// サムネイル画像をアップロード
				String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
				// URLを取得
				String thumbnailUrl = thumbnailService.getURL(fileName);

				bookInfo.setThumbnailName(fileName);
				bookInfo.setThumbnailUrl(thumbnailUrl);

			} catch (Exception e) {

				// 異常終了時の処理
				logger.error("サムネイルアップロードでエラー発生", e);
				model.addAttribute("bookDetailsInfo", bookInfo);
				return "addBook";
			}
		}
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
		if (list.size() == 0) {
			// 書籍情報を更新する
			booksService.updateBook(bookInfo);
			model.addAttribute("resultMessage", "登録完了");
		} else {
			model.addAttribute("errorMessageDetails", list);
			model.addAttribute("bookInfo", bookInfo);
			return "editBook";
		}

		// TODO 編集した書籍の詳細情報を表示するように実装
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		// 詳細画面に遷移する
		return "details";
	}

}