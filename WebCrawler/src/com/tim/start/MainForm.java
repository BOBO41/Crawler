package com.tim.start;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.tim.crawlertask.Crawltask;
import com.tim.dao.CrawltaskDAO;
import com.tim.page.PageUtils;
import com.tim.process.ProcessPage;

/**
 * 博客抓取控制台
 * 
 * @author tim
 *
 */
public class MainForm {
	
	private processMain processthread = null;

	@SuppressWarnings("rawtypes")
	private static DefaultListModel listModel = new DefaultListModel();// 列表项，用于输出内容到JList控件上

	public MainForm() {
		// 创建 JFrame 实例
		JFrame frame = new JFrame("Blog Crawler");

		frame.setSize(700, 600);// Setting the width and height of frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);// 中间显示

		/*
		 * 创建面板，这个类似于 HTML 的 div 标签 我们可以创建多个面板并在 JFrame 中指定位置
		 * 面板中我们可以添加文本字段，按钮及其他组件。
		 */
		JPanel panel = new JPanel();
		// 添加面板
		frame.add(panel);

		placeComponents(panel);

		// 设置界面可见
		frame.setVisible(true);

		printLog("博客抓取程序初始化完成。");
	}

	public static void main(String[] args) {
		new MainForm();
	}

	/**
	 * 调用用户定义的方法并添加组件到面板
	 */
	private void placeComponents(JPanel panel) {
		// 这边设置布局为 null
		panel.setLayout(null);

		// 创建开始按钮
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(10, 10, 80, 25);
		panel.add(btnStart);

		// 创建停止按钮
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(100, 10, 80, 25);
		btnStop.setEnabled(false);// 默认停止按钮不可用
		panel.add(btnStop);

		// 创建 JLabel
		JLabel lblLog = new JLabel("Information:");
		lblLog.setBounds(10, 40, 100, 25);
		panel.add(lblLog);

		// 创建日志输出list
		// 滚动pane
		JScrollPane panScroll = new JScrollPane();
		panScroll.setBounds(10, 70, 680, 520);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		JList logLst = new JList(listModel);
		logLst.setBounds(10, 70, 680, 520);

		panScroll.setViewportView(logLst);
		panel.add(panScroll);

		// 这里是匿名类，类中方法是actionPerformed
		// 给开始按钮 增加 监听
		btnStart.addActionListener(new ActionListener() {
			// 当按钮被点击时，就会触发 ActionEvent事件
			// actionPerformed 方法就会被执行
			public void actionPerformed(ActionEvent e) {
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);

				printLog("抓取任务开始……");
				if (processthread == null) {
					// 先初始化线程类，再将开始线程标志设置为true，再开始线程
					processthread = new processMain();
					processthread.startTask();
					processthread.start();
				}
			}
		});

		// 给停止按钮 增加 监听
		btnStop.addActionListener(new ActionListener() {
			// 当按钮被点击时，就会触发 ActionEvent事件
			// actionPerformed 方法就会被执行
			public void actionPerformed(ActionEvent e) {
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);

				printLog("停止任务。");
				if (processthread != null) {
					// 停止线程，设置开始标志为false，线程内部不满足条件则终止
					// 再将对象置为空
					processthread.stopTask();
					processthread = null;
				}
			}
		});
	}

	/**
	 * 用于调用抓取方法的多线程类，数据展示到界面上也是一个线程，异步 一个用于抓取，一个用于显示信息
	 * 
	 * @author tim
	 *
	 */
	class processMain extends Thread {
		// 关键字volatile，这个关键字的目的是使exit同步，也就是说在同一时刻只能由一个线程来修改exit的值。
		// 线程的停止，不使用stop方法，这是强制停止线程，会导致数据不一致
		// 使用标志来停止线程
		private volatile boolean flag = true;

		/**
		 * 停止线程使用标志
		 */
		public void stopTask() {
			flag = false;
		}

		/**
		 * 开始线程
		 */
		public void startTask() {
			flag = true;
		}

		public void run() {
			while (flag) {
				beginCrawl();
			}
		}
	}

	/**
	 * 开始抓取信息
	 */
	private void beginCrawl() {
		// 读取任务
		List<Crawltask> crawltasks = CrawltaskDAO.getUnDoCrawTask();

		if (crawltasks.size() > 0) {
			printLog("获取到任务：" + crawltasks.size() + "个");
			for (Crawltask crawltask : crawltasks) {
				printLog("开始任务，任务id=" + crawltask.getId());
				String[] keywords = null;
				if (crawltask.getKeyWords() != null && crawltask.getKeyWords().length() > 0) {
					keywords = crawltask.getKeyWords().split(",");
				}

				PageUtils pageUtile = new PageUtils(crawltask.getId(), crawltask.getCrawlURL(), keywords,
						crawltask.getCreateBy());
				// pageUtile.starGetPage();// 单线程

				// 多线程进行任务抓取
				ProcessPage page = new ProcessPage(pageUtile);
				page.start();
			}
		} else {
			printLog("没有任务……");
		}
		// 每完成一次任务，休息10秒
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将信息输出到界面控件上
	 * 
	 * @param info
	 */
	@SuppressWarnings("unchecked")
	private void printLog(String info) {
		// 在使用SimpleDateFormat时格式化时间的 yyyy.MM.dd为年月日
		// 如果希望格式化时间为12小时制的，则使用hh:mm:ss
		// 如果希望格式化时间为24小时制的，则使用HH:mm:ss

		// 动态增加列表显示项
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dtTime = df.format(new Date());

		// 显示到控件项上
		listModel.addElement(dtTime + " " + info);
	}
}
