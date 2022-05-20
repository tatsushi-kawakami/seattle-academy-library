package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍詳細情報格納DTO
 *
 */
@Configuration
@Data
public class HistoryBookInfo {
private Integer bookId;

private String title;

private String checkout_date;

private String return_date;

	public HistoryBookInfo() {

	}
}