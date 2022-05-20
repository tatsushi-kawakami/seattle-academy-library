package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.RentalsService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class HistoryListController{
	final static Logger logger = LoggerFactory.getLogger(EditBookController.class);
	
	@Autowired
	private RentalsService rentalsService;
	
	@RequestMapping(value = "/HistoryList", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String historyList(Locale locale, Model model) {
		// デバッグ用ログ
		logger.info("Welcome HistoryControler.java! The client locale is {}.", locale);
		
		model.addAttribute("historyList", rentalsService.getHistoryList());
		return "historyList";
	}
}