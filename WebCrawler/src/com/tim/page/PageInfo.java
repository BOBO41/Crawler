package com.tim.page;

import java.util.Date;

/**
 * 实体类，存放页面信息
 * 
 * @author tim
 *
 */
public class PageInfo {
	
	private String id;
	private String url;// 页面的url
	private String title;// 页面的title
	private String postTime;// 文章发表时间
	private String content;// 文章内容
	private String author;// 文章作者
	private String authorPage;// 文章作者主页
	private Date createTime;// 抓取时间
	private String createBy;// 拥有者，指发起抓取任务的人

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setTitle(String title) {
		// 数据库设计该字段长度为100，大于100，则后面用省略号
		if (title.length() > 100) {
			this.title = title.substring(0, 90) + "……";
		} else {
			this.title = title;
		}
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		// 超出长度截断
		if (content.length() > 65535) {
			this.content = content.substring(0, 6000) + "……";
		} else {
			this.content = content;
		}
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorPage() {
		return authorPage;
	}

	public void setAuthorPage(String authorPage) {
		this.authorPage = authorPage;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
}
