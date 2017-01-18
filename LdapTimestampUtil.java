package pl.ppl.utils;

import java.time.*;

class LdapTimestampUtil {

	/**
	 * Microsoft world exist since since Jan 1, 1601 UTC
	 */
	public static final ZonedDateTime LDAP_MIN_DATE_TIME = ZonedDateTime.of(1601, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
	/**
	 * ZonedDateTime representing Epoch start time at UTC
	 */
	public static final ZonedDateTime UNIX_MIN_DATE_TIME = ZonedDateTime.of(1970, 1, 1, 0, 0 ,0, 0, ZoneId.of("UTC"));
	/**
	 * The LDAP timestamp is the number of 100-nanoseconds intervals since since Jan 1, 1601 UTC
	 * LDAP_MAX_TIMESTAMP_VALUE is maximum number value for ldap timestamp (useful e.g. for setting account that never expires)
	 */
	public static final long LDAP_MAX_TIMESTAMP_VALUE = 9223372036854775807L;

	public static long instantToLdapTimestamp(Instant instant) {
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
		return zonedDateToLdapTimestamp(zonedDateTime);
	}

	public static long localDateToLdapTimestamp(LocalDateTime dateTime) {
		ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
		return zonedDateToLdapTimestamp(zonedDateTime);
	}

	public static long zonedDateToLdapTimestamp(ZonedDateTime zonedDatetime) {
		Duration duration = Duration.between(LDAP_MIN_DATE_TIME, zonedDatetime);
		return millisecondsToLdapTimestamp(duration.toMillis());
	}

	public static LocalDateTime ldapTimestampToLocalDate(long ldapTimestamp) {
		long ldapTimestampForBeginOfUnixTimestamp = zonedDateToLdapTimestamp(UNIX_MIN_DATE_TIME);
		long epochSeconds =  ldapTimestampToSeconds (ldapTimestamp - ldapTimestampForBeginOfUnixTimestamp );
		Instant instant = Instant.ofEpochSecond(epochSeconds);
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * The LDAP timestamp is the number of 100-nanoseconds intervals since since Jan 1, 1601 UTC
	 */
	private static long millisecondsToLdapTimestamp(long millis) {
		return millis * 1000 * 10;
	}

	private static long ldapTimestampToSeconds(long ldapTimestamp) {
		return ldapTimestamp / 10000000;
	}
}
