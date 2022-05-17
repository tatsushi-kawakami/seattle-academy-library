package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

/**
 * 書籍検索コントローラー
 */
@Controller
public class SearchBookController{
	final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);
	@Autowired
	private BooksService booksService;
	
	@RequestMapping(value = "/searchBook", method = RequestMethod.POST)
	public String SearchBook(Locale locale, @RequestParam("search") String search ,@RequestParam("radio_button") String radio_button, Model model) {
		
		if(radio_button.equals("1")) {
			model.addAttribute("bookList",booksService.getSearchBookInfo(search));
		}
		if(radio_button.equals("2")) {
			model.addAttribute("bookList", booksService.getSearchAllBookInfo(search));
		}
		return "home";
	}
}