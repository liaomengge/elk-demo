package com.sh.util.serialization;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SerializationException;
import org.nustaq.serialization.FSTConfiguration;

public class JRedisSerializationUtils {
	private JRedisSerializationUtils() throws Exception {
		throw new Exception("inital JRedisSerializationUtils instance failed...");
	}

	private final static FSTConfiguration configuration = FSTConfiguration.createDefaultConfiguration();

	public static byte[] fastSerialize(Object obj) {
		byte[] result = null;
		if (null == obj) {
			return result;
		}
		try {
			result = configuration.asByteArray(obj);
		} catch (Exception e) {
			throw new SerializationException(e);
		}
		return result;
	}

	public static Object fastDeserialize(byte[] objectData) {
		Object obj = null;
		if (ArrayUtils.isEmpty(objectData)) {
			return obj;
		}
		try {
			obj = configuration.asObject(objectData);
		} catch (Exception e) {
			throw new RuntimeException("deseriale failed", e);
		}
		return obj;
	}

}
