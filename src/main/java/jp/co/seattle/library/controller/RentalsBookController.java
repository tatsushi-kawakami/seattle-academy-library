package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalsService;

/**
 * 詳細表示コントローラー
 */
@Controller
public class RentalsBookController {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private RentalsService rentalsService;

	/**
	 * 借りるボタンの実装
	 * 
	 * @param locale
	 * @param bookId
	 * @param model
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/rentalsBook", method = RequestMethod.POST)
	public String RentalsBook(Locale locale, @RequestParam("bookId") int bookId, RedirectAttributes redirectAttributes) {
		logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

		boolean bookIdExists = rentalsService.selectBookInfo(bookId);
		if (bookIdExists) {
			redirectAttributes.addFlashAttribute("errorRentals", "貸し出し済みです。");
		} else {
			rentalsService.registRentals(bookId);
		}
		return "redirect:/details?bookId=" + bookId;
	}
}