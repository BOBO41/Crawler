package com.tim.dao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.tim.helper.DbAction;
import com.tim.page.PageInfo;

public class PageInfoDAO {
	
	/**
	 * 将抓取的网页信息存入数据库
	 */
	public static void addPageInfo(PageInfo pageInfo) throws SQLException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String strSql = "insert into bll_pageinfo values (?,?,?,?,?,?,?,?,?)";
		String[] parameters = { pageInfo.getId(), pageInfo.getUrl(), pageInfo.getTitle(), pageInfo.getPostTime(),
				pageInfo.getContent(), pageInfo.getAuthor(), pageInfo.getAuthorPage(),
				df.format(pageInfo.getCreateTime()).toString(), pageInfo.getCreateBy() };

		DbAction.executeUpdate(strSql, parameters);
	}

}
