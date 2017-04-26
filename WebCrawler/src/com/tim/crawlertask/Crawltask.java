package com.tim.crawlertask;

import java.util.Date;

public class Crawltask {
	
	private String id;
	private String CreateBy;
	private String CrawlURL;
	private String KeyWords;
	private Date CreateTime;
	private int State;// 0：创建；1：执行中；2：执行成功；3：执行失败。
	private Date FinishTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateBy() {
		return CreateBy;
	}

	public void setCreateBy(String createBy) {
		CreateBy = createBy;
	}

	public String getCrawlURL() {
		return CrawlURL;
	}

	public void setCrawlURL(String crawlURL) {
		CrawlURL = crawlURL;
	}

	public String getKeyWords() {
		return KeyWords;
	}

	public void setKeyWords(String keyWords) {
		KeyWords = keyWords;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public Date getFinishTime() {
		return FinishTime;
	}

	public void setFinishTime(Date finishTime) {
		FinishTime = finishTime;
	}
}
