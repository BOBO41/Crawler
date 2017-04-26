package com.tim.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tim.crawlertask.Crawltask;
import com.tim.helper.DbAction;

public class CrawltaskDAO {
	
	/**
	 * 读取未处理的抓取任务
	 * 
	 */
	public static List<Crawltask> getUnDoCrawTask() {
		List<Crawltask> crawltasks = new ArrayList<Crawltask>();

		String strSql = "select * from bll_crawltask where state=0";
		ResultSet rs = DbAction.getQuery(strSql);

		try {
			while (rs.next()) {
				Crawltask task = new Crawltask();
				task.setId(rs.getString("ID"));
				task.setCreateBy(rs.getString("CreateBy"));
				task.setCrawlURL(rs.getString("CrawlURL"));
				task.setKeyWords(rs.getString("KeyWords"));
				task.setCreateTime(rs.getDate("CreateTime"));
				task.setState(rs.getInt("State"));

				crawltasks.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return crawltasks;
	}

	/**
	 * 任务抓取完成后，将状态改为完成
	 * 
	 */
	public static void updateTaskState(String taskId, String state) throws SQLException {
		// 执行中，无完成时间
		if (state == "1") {
			String strSql = "update bll_crawltask set state=? where id=?";
			String[] parameters = { state, taskId };

			DbAction.executeUpdate(strSql, parameters);
		} else {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String strSql = "update bll_crawltask set state=?,finishtime=? where id=?";
			String[] parameters = { state, df.format(new Date()).toString(), taskId };

			DbAction.executeUpdate(strSql, parameters);
		}
	}
}
