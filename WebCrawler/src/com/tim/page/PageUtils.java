package com.tim.page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tim.dao.CrawltaskDAO;
import com.tim.dao.PageInfoDAO;
import com.tim.helper.SaveFile;

/**
 * 针对页面进行处理 先针对博客园进行定制处理http://www.cnblogs.com/
 * 
 * @author tim
 *
 */
public class PageUtils {
	
	private static Logger logger = Logger.getLogger(PageUtils.class);

	private String homeUrl; // 开始抓取的网页
	private String[] keyWords;// 需要在网页上匹配的关键词
	private String createBy;// 创建人
	private String taskId;// 抓取任务id

	private String pagePath = "/media/tim/学习/我的项目/博客系统/Ubuntu/springmvc/爬虫/pages";// 网页存放路径

	public PageUtils(String taskid, String homeUrl, String[] keyWords, String createBy) {
		this.taskId = taskid;
		this.homeUrl = homeUrl;
		this.keyWords = keyWords;
		this.createBy = createBy;
	}

	public PageUtils(String homeUrl, String[] keyWords, String createBy) {
		this.homeUrl = homeUrl;
		this.keyWords = keyWords;
		this.createBy = createBy;
	}

	public PageUtils(String homeUrl, String[] keyWords) {
		this.homeUrl = homeUrl;
		this.keyWords = keyWords;
		this.createBy = "";
	}

	public PageUtils(String homeUrl) {
		this.homeUrl = homeUrl;
		keyWords = null;
		this.createBy = "";
	}

	public PageUtils(String homeUrl, String createBy) {
		this.homeUrl = homeUrl;
		this.createBy = createBy;
	}

	/**
	 * 调用入口
	 */
	public void starGetPage() {
		logger.info("开始抓取信息……");
		// 开始执行
		updateTaskInfoToDB(taskId, "1");

		ArrayList<String> childUrls = getHomePageUrls();
		getChildPageInfo(childUrls);
	}

	/**
	 * 通过url获取页面上符合要求的url
	 * 
	 * @return
	 */
	private ArrayList<String> getHomePageUrls() {
		ArrayList<String> href_list = new ArrayList<String>();

		if (homeUrl.endsWith("/")) {
			homeUrl = homeUrl.substring(0, homeUrl.length() - 1);
		}
		try {
			// 读取页面
			Document mainDoc = Jsoup.connect(homeUrl).timeout(30000).get();

			// 1、先读取首页。首页只需要获取上面的链接，这些链接即是第二级页面
			Elements listLinks = mainDoc.select("a.titlelnk");// 针对博客园

			for (Element link : listLinks) {
				String linkHref = link.attr("abs:href");
				if (linkHref.endsWith("/")) {
					linkHref = linkHref.substring(0, linkHref.length() - 1);
				}
				// 如果是有效的链接，且数组中不存在这个链接
				if (linkIsAvailable(linkHref) && !href_list.contains(linkHref)) {
					href_list.add(linkHref);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}

		return href_list;
	}

	/**
	 * 判断链接是否有效，排除掉.exe\.rar等
	 * 
	 * @param url
	 * @return
	 */
	private boolean linkIsAvailable(String url) {
		if (url.startsWith("http://")) {
			String regex = ".*.exe|.*.apk|.*.zip|.*.rar|.*.pdf|.*.doc";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(url);
			return !matcher.find();
		}
		return false;
	}

	/**
	 * 判断输入的内容中是否包含关键词
	 * 
	 * @param content
	 *            输入的内容
	 * @return 包含指定关键词，则返回true，否则返回false
	 */
	private boolean hasKeyword(String content) {
		boolean result = false;

		if (keyWords != null && keyWords.length > 0) {
			String regEx = ""; // String regEx = "a|f";表示a或f，关键词
			StringBuilder strRegEx = new StringBuilder();
			for (String word : keyWords) {
				strRegEx.append(word);
				strRegEx.append("|");
			}
			regEx = strRegEx.substring(0, strRegEx.length() - 1).toString();

			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(content);
			result = m.find();
		} else {
			// 无关键词，则不进行匹配，所有内容都符合要求
			result = true;
		}

		return result;
	}

	/**
	 * 分析第二级页面
	 * 
	 * @param infoList
	 */
	private void getChildPageInfo(ArrayList<String> urlList) {
		for (String childUrl : urlList) {
			if (childUrl.endsWith("/")) {
				childUrl = childUrl.substring(0, childUrl.length() - 1);
			}
			try {
				// 读取页面
				Document childDoc = Jsoup.connect(childUrl).timeout(30000).get();

				// 2、再分析第二级页面内容

				// 首先分析文章内容，如果文章内容包含关键词，则是需要的文章
				// TODO，这里先进行简单分析，后续使用其他技术
				String body = childDoc.select("#cnblogs_post_body").html();
				if (hasKeyword(body)) {
					// 符合要求
					PageInfo pageInfo = new PageInfo();

					String id = UUID.randomUUID().toString();

					pageInfo.setId(id);
					pageInfo.setUrl(childUrl);

					String title = childDoc.select("#cb_post_title_url").text();
					pageInfo.setTitle(title);

					String postDate = childDoc.select("#post-date").text();
					pageInfo.setPostTime(postDate);

					Element postDesc = null;
					String author = "未知";

					try {
						// 有些文档格式不一样，可能有些信息获取不到，会抛异常，如果出现异常，则catch进行处理
						postDesc = childDoc.select(".postDesc>a").get(0);
						author = postDesc.text();
						pageInfo.setAuthor(author);
						pageInfo.setAuthorPage(postDesc.attr("href"));
					} catch (Exception e) {
						System.out.println("发布信息未获取到，可能文档格式不一样！");
						pageInfo.setAuthor("未知");
						pageInfo.setAuthorPage("未知");
					}

					pageInfo.setCreateTime(new Date());
					pageInfo.setCreateBy(createBy);

					// 将文章内容保存为网页存放，这里只存放网页路径
					// 只取文章正文内容，这里为了在网页上显示，加上html头和尾
					String pageName = pagePath + "/" + id + ".htm";
					StringBuilder strContent = new StringBuilder();
					strContent.append("<!DOCTYPE html>");
					strContent.append("<html lang=\"zh-cn\">");
					strContent.append("<head>");
					strContent.append("    <meta charset=\"utf-8\" /> ");
					strContent.append("<title>" + title + "</title>");
					strContent.append("</head>");
					strContent.append("<body>");
					strContent.append("<h2>" + title + "</h2>");
					strContent.append("<p>作者：" + author + "	发布时间：" + postDate + "</p>");

					strContent.append(body);

					strContent.append("</body>");
					strContent.append("</html>");

					// 存放网页
					SaveFile.saveTxtFile(pageName, strContent.toString());
					pageInfo.setContent(pageName);

					// 将符合要求的网页信息存放到数据库中
					addPageInfoToDB(pageInfo);
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				ex.printStackTrace();
				updateTaskInfoToDB(taskId, "3");// 执行失败
			}
		}
		logger.info("任务完成，任务id " + taskId);
		updateTaskInfoToDB(taskId, "2");// 执行成功
	}

	/**
	 * 将符合要求的网页信息存放到数据库中
	 * 
	 * @param pageInfo
	 */
	private void addPageInfoToDB(PageInfo pageInfo) {
		// 调用DAL层
		try {
			PageInfoDAO.addPageInfo(pageInfo);
			logger.info("get a page,title=" + pageInfo.getTitle());
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * 更新抓取任务的状态
	 * 
	 * @param taskId
	 * @param state
	 *            0：创建；1：执行中；2：执行成功；3：执行失败。
	 */
	private void updateTaskInfoToDB(String taskId, String state) {
		// 调用DAL层
		try {
			CrawltaskDAO.updateTaskState(taskId, state);
			logger.info("task finished " + taskId);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
