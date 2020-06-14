package org.NauhWuun.jio.protocols;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

public class NtpProtocol
{
	public static final String DEFAULT_DURATION_FORMAT = "%02d:%02d:%02d";
	public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final List<String> NTP_POOL = Arrays.asList(
			"pool.ntp.org”, “ntp.ntsc.ac.cn”, “asia.pool.ntp.org”, “ntp1.aliyun.com”, “cn.pool.ntp.org",
			"hk.pool.ntp.org", "tw.pool.ntp.org", "time.windows.com", "time.apple.com", "time.android.com",
			"kr.ntp.org.cn", "us.ntp.org.cn");

	private static final ScheduledExecutorService ntpUpdateTimer = Executors.newSingleThreadScheduledExecutor();

	private static volatile int NTPTIMEOUT = 20000;
	private static long timeOffsetFromNtp = 0;
	private static int MAXREPEATCOUNTS = 12;

	private NtpProtocol() {
	}

	public static void startNtpProcessing(long currentTimeMillis, TimeUnit unit, int ntpTimeout) {
		NTPTIMEOUT = ntpTimeout;

		updateNetworkTimeOffset();
		ntpUpdateTimer.scheduleAtFixedRate(NtpProtocol::updateNetworkTimeOffset, 0, currentTimeMillis, unit);
	}

	public static String formatDuration(Duration duration) {
		long seconds = duration.getSeconds();
		return String.format(DEFAULT_DURATION_FORMAT, seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
	}

	public static String formatTimestamp(Long timestamp) {
		return new SimpleDateFormat(DEFAULT_TIMESTAMP_FORMAT).format(new Date(timestamp));
	}

	public static long currentTimeMillis() {
		return System.currentTimeMillis() + timeOffsetFromNtp;
	}

	private static void updateNetworkTimeOffset() {
		NTPUDPClient client = new NTPUDPClient();
		client.setDefaultTimeout(NTPTIMEOUT);

		int repeatCount = 0;
		do {
			if (repeatCount == MAXREPEATCOUNTS) {
				client.close();
				shutdownNtpUpdater();
			}

			try {
				client.open();
				InetAddress hostAddr = InetAddress.getByName(NTP_POOL.get(repeatCount));

				TimeInfo info = client.getTime(hostAddr);
				info.computeDetails();

				timeOffsetFromNtp = info.getOffset();
				return;
			} catch (IOException e) {
				++repeatCount;
				continue;
			}
		} while(repeatCount < MAXREPEATCOUNTS);
	}

	public static void shutdownNtpUpdater() {
		System.out.println("ShutDown NTP Client... \r\n");
		ntpUpdateTimer.shutdownNow();

		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.notify();
		}
	}
}
