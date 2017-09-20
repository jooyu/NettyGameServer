package cpgame.demo.core.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

/**
 * spring调度封装 schedule.xml
 * @author 0x737263
 *
 */
public class Schedule {
	private static final Logger LOGGER = LoggerFactory.getLogger(Schedule.class);

	@Autowired
	TaskScheduler taskScheduler;
	
	@PostConstruct
	void init() {
		LOGGER.info("Schedule init complete!");
	}
	
	/**
	 * 每x毫秒钟执行（如果时间已过立即执行一次）
	 * @param task 任务
	 * @param startSeconds 执行周期（毫秒）
	 */
	public void addEveryMillisecond(Runnable task, long startSeconds){
		taskScheduler.scheduleAtFixedRate(task, startSeconds);
	}
	
	/**
	 * 每x秒钟执行（如果时间已过立即执行一次）
	 * @param task 任务
	 * @param startSeconds 执行周期（秒）
	 */
	public void addEverySecond(Runnable task, int startSeconds){
		int millSeconds = startSeconds * DateUtils.SECOND_MILLISECOND;
		Date startDate = DateUtils.getDelayDate(millSeconds, TimeUnit.MILLISECONDS);
		taskScheduler.scheduleAtFixedRate(task, startDate, millSeconds);
	}
	
	/**
	 * 每x分钟执行 （如果时间已过立即执行一次）
	 * @param task			runnable对象
	 * @param startMinute	执行周期时间(分钟)
	 */
	public void addEveryMinute(Runnable task, int startMinute) {
		int millSeconds = startMinute * 60 * DateUtils.SECOND_MILLISECOND;
		Date startDate = DateUtils.getDelayDate(millSeconds, TimeUnit.MILLISECONDS);
		taskScheduler.scheduleAtFixedRate(task, startDate, millSeconds);
	}
	
	/**
	 * 每x分钟执行（整数倍时间）
	 * @param task			runnable对象
	 * @param startMinute	执行周期时间(分钟) 
	 */
	public void addEveryMinuteZeroStart(Runnable task, int startMinute) {
		int millSeconds = startMinute * 60 * DateUtils.SECOND_MILLISECOND;
		Date startDate = DateUtils.getDelayMinuteDate(startMinute);
		taskScheduler.scheduleAtFixedRate(task, startDate, millSeconds);
	}
		
	/**
	 * 每小时整点触发(每天24次） 重复执行
	 * @param task	任务
	 */
	public void addEveryHour(Runnable task) {
		long oneHourMillisecond = DateUtils.HOUR_MILLISECOND;
		long nextHourTime = DateUtils.getNextHourTime();
		// 调度首次触发时间为下个小时整点+1000ms(防止在前一个小时的59秒就开始执行任务)
		Date startDate = new Date(nextHourTime + 1000);
		
		taskScheduler.scheduleAtFixedRate(task, startDate, oneHourMillisecond);
	}
	
	/**
	 * 
	 * 每天x点执行.(每天一次) （如果时间已过立即执行一次），然后延迟一天， 重复执行
	 * @param task
	 * @param hour  1-24 小时定时执行
	 */
	public void addFixedTime(Runnable task, int hour) {
		if (hour == 0) {
			hour = 24;
		}
		addFixedTime(task, hour, 0, 0);
	}
	/**
	 * 每天x点执行.(每天一次) （如果时间已过立即执行一次），然后延迟一天， 重复执行
	 * @param task
	 * @param hour
	 * @param minutes
	 * @param seconds
	 */
	public void addFixedTime(Runnable task, int hour, int minutes, int seconds) {
		if (hour == 0) {
			hour = 24;
		}
		long oneDayMillisecond = DateUtils.DAY_MILLISECOND;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minutes);
		c.set(Calendar.SECOND, seconds);
		c.set(Calendar.MILLISECOND, 0);
		// 如果调度时间小于当前时间,则调度时间往后延时一天
		if (c.getTimeInMillis() < System.currentTimeMillis()) {
			c.setTimeInMillis(c.getTimeInMillis() + oneDayMillisecond);
		}
		Date startDate; 
		if (hour == 24 && minutes == 0 && seconds == 0) {
			// 对于跨天的任务延时2秒执行，防止在前一天的59秒就开始执行任务
			startDate = new Date(DateUtils.setFixTime(hour) + 2000); 
		} else {
			// 普通定时任务延时1秒执行
			startDate = new Date(c.getTimeInMillis() + 1000);
		}
		taskScheduler.scheduleAtFixedRate(task, startDate, oneDayMillisecond);
	}
	
	/**
	 * 延迟执行 
	 * @param task 任务
	 * @param seconds 延迟时间(秒)
	 */
	public void addDelaySeconds(Runnable task, int seconds){
		long millSeconds = TimeUnit.SECONDS.toMillis(seconds);
		taskScheduler.schedule(task, new Date(System.currentTimeMillis() + millSeconds));
	}
	
}
