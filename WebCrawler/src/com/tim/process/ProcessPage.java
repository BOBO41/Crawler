package com.tim.process;

import com.tim.page.PageUtils;

/**
 * 多线程处理抓取任务
 * 
 * @author tim
 *
 */
public class ProcessPage extends Thread {
	
	PageUtils pageUtils;

	public ProcessPage(PageUtils page) {
		this.pageUtils = page;
	}

	@Override
	public void run() {
		pageUtils.starGetPage();
	}

}
