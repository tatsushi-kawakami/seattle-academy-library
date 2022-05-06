package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class BulkRegistController {
	final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

	@Autowired
	private BooksService booksService;

//	@Autowired
//	private ThumbnailService thumbnailService;

	@RequestMapping(value = "/bulkRegist", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String login(Model model) {
		return "bulkRegist";
	}

	@RequestMapping(value = "/bulkRegist", method = RequestMethod.POST)
	public String bulkRegist(Locale locale, @RequestParam("file") MultipartFile file, Model model) {

		String line;
		List<BookDetailsInfo> BookList = new ArrayList<BookDetailsInfo>();
		List<String> ErrorList = new ArrayList<String>();
		int count = 0;

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

			if (!br.ready()) {
				model.addAttribute("errorBulk", "csvに書籍情報がありません。");
				return "bulkRegist";
			}

			while ((line = br.readLine()) != null) {
				count++;
				final String[] data = line.split(",", -1);
				List<String> error = booksService.errorList(data[0], data[1], data[2], data[3], data[4]);
				if (error.size() == 0) {
					BookDetailsInfo bookInfo = new BookDetailsInfo();
					bookInfo.setTitle(data[0]);
					bookInfo.setAuthor(data[1]);
					bookInfo.setPublisher(data[2]);
					bookInfo.setPublishDate(data[3]);
					bookInfo.setIsbn(data[4]);
					bookInfo.setExplanation(data[5]);

					BookList.add(bookInfo);
				} else {
					ErrorList.add("<p>"+count + "行目でバリデーションエラーが起きました"+"</p>");
					
				}

			}

			if (!(ErrorList.isEmpty())) {
				model.addAttribute("errorBulk", ErrorList);
				return "bulkRegist";
			} else {
				BookList.forEach(bookInfo -> booksService.registBook(bookInfo));
				model.addAttribute("bookList", booksService.getBookList());
				return "home";
			}
		} catch (IOException e) {
			// ファイルがない場合
			throw new RuntimeException("ファイルが読み込めません", e);
		}
	}
}