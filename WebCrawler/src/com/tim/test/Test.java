package com.tim.test;

import java.util.List;

import org.apache.log4j.Logger;

import com.tim.crawlertask.Crawltask;
import com.tim.dao.CrawltaskDAO;
import com.tim.page.PageUtils;
import com.tim.process.ProcessPage;

public class Test {
	
	private static Logger logger = Logger.getLogger(Test.class);

	public static void main(String args[]) {
		while (true) {
			List<Crawltask> crawltasks = CrawltaskDAO.getUnDoCrawTask();

			if (crawltasks.size() > 0) {
				for (Crawltask crawltask : crawltasks) {
					String[] keywords = null;
					if (crawltask.getKeyWords() != null && crawltask.getKeyWords().length() > 0) {
						keywords = crawltask.getKeyWords().split(",");
					}

					PageUtils pageUtile = new PageUtils(crawltask.getId(), crawltask.getCrawlURL(), keywords,
							crawltask.getCreateBy());
					ProcessPage page = new ProcessPage(pageUtile);
					page.start();
				}
			} else {
				logger.info("没有取到任务。");
			}
			// 每完成一次任务，休息10秒
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
