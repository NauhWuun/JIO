package org.NauhWuun.jio.protocols.DNS;

import io.vertx.core.Vertx;
import io.vertx.core.dns.*;

import java.util.List;

public class Client
{
	private DnsClient client;

	public Client(final String host) {
		client = Vertx.vertx().createDnsClient(new DnsClientOptions()
				.setPort(53)
				.setHost(host)
				.setQueryTimeout(20000));
	}

	/**
	 * To lookup the A / AAAA record for "vertx.io"
	 */
	public void lookup() {
		client.lookup("vertx.io", ar -> {
			if (ar.succeeded()) {
				System.out.println(ar.result());
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to lookup" + ar.cause());
				}
			}
		});
	}

	/**
	 * To lookup the A record for "vertx.io"
	 */
	public void loopV4() {
		client.lookup4("vertx.io", ar -> {
			if (ar.succeeded()) {
				System.out.println(ar.result());
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to loopV4" + ar.cause());
				}
			}
		});
	}

	/**
	 * To lookup the A record for "vertx.io"
	 */
	public void loopV6() {
		client.lookup6("vertx.io", ar -> {
			if (ar.succeeded()) {
				System.out.println(ar.result());
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to loopV6" + ar.cause());
				}
			}
		});
	}

	/**
	 * To lookup all the A records for "vertx.io"
	 */
	public void resolveA() {
		client.resolveA("vertx.io", ar -> {
			if (ar.succeeded()) {
				List<String> records = ar.result();
				for (String record : records) {
					System.out.println(record);
				}
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to resolveA" + ar.cause());
				}
			}
		});
	}

	/**
	 * To lookup all the AAAAA records for "vertx.io"
	 */
	public void resolveAAAA() {
		client.resolveAAAA("vertx.io", ar -> {
			if (ar.succeeded()) {
				List<String> records = ar.result();
				for (String record : records) {
					System.out.println(record);
				}
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to resolveAAAA" + ar.cause());
				}
			}
		});
	}

	/**
	 * To lookup all the CNAME records for "vertx.io"
	 */
	public void resolveCNAME() {
		client.resolveCNAME("vertx.io", ar -> {
			if (ar.succeeded()) {
				List<String> records = ar.result();
				for (String record : records) {
					System.out.println(record);
				}
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to resolveCNAME" + ar.cause());
				}
			}
		});
	}

	/**
	 * To lookup all the MX records for "vertx.io"
	 */
	public void resolveMX() {
		client.resolveMX("vertx.io", ar -> {
			if (ar.succeeded()) {
				List<MxRecord> records = ar.result();
				for (MxRecord record: records) {
					System.out.println(record);
				}
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to resolveMX" + ar.cause());
				}
			}
		});
	}

	/**
	 * To resolve all the TXT records for "vertx.io"
	 */
	public void resolveTXT() {
		client.resolveTXT("vertx.io", ar -> {
			if (ar.succeeded()) {
				List<String> records = ar.result();
				for (String record: records) {
					System.out.println(record);
				}
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to resolveTXT" + ar.cause());
				}
			}
		});
	}

	/**
	 * To resolve all the NS records for "vertx.io"
	 */
	public void resolveNS() {
		client.resolveNS("vertx.io", ar -> {
			if (ar.succeeded()) {
				List<String> records = ar.result();
				for (String record: records) {
					System.out.println(record);
				}
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to resolveNS" + ar.cause());
				}
			}
		});
	}

	/**
	 * Try to resolve the PTR record for a given name. The PTR record maps an ipaddress to a name.
	 * To resolve the PTR record for the ipaddress 0.0.0.0 you would use the PTR notion of "0.0.0.0.in-addr.arpa"
	 * @param ptr
	 */
	public void resolvePTR(final String ptr) {
		client.resolvePTR(ptr, ar -> {
			if (ar.succeeded()) {
				String record = ar.result();
				System.out.println(record);
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to resolvePTR" + ar.cause());
				}
			}
		});
	}

	/**
	 * To do a reverse lookup for the ipaddress
	 */
	public void reverseLookup(final String ipAddr) {
		client.reverseLookup(ipAddr, ar -> {
			if (ar.succeeded()) {
				String record = ar.result();
				System.out.println(record);
			} else {
				Throwable cause = ar.cause();

				if (cause instanceof DnsException) {
					DnsException exception = (DnsException) cause;
					DnsResponseCode code = exception.code();
				} else {
					System.out.println("Failed to reverseLookup" + ar.cause());
				}
			}
		});
	}
}
