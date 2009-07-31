package org.nideasystems.webtools.zwitrng.server;

public class Configuration {

	public static final int MAX_FOLLOW_JOB = 50;
	public static final long MAX_JOB_RUN_TIME = 1000*25;//25 seconds
	public static final int FOLLOW_UNFOLLOW_JOB_INTERVAL = 10;
	public static final int CREATE_AUTO_FOLLOW_QUEUE_INTERVAL = 30;
	public static final int CREATE_AUTO_UNFOLLOW_QUEUE_INTERVAL = 60*24;
	public static final int SYNCH_INTERVAL = 10;
}

