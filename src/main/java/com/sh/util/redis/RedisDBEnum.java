package com.sh.util.redis;

public enum RedisDBEnum {

	DB0(0, "DB0"), DB1(1, "DB1"), DB2(2, "DB2"), DB3(3, "DB3");

	/** 简码 */
	private final int code;

	/** 描述 */
	private final String description;

	/**
	 * 私有构造方法
	 *
	 * @param code
	 *            简码
	 * @param description
	 *            描述
	 */
	private RedisDBEnum(int code, String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 通过枚举<code>code</code>获得枚举。
	 *
	 * @param code
	 *            简码
	 * @return 枚举
	 */
	public static RedisDBEnum getByCode(int code) {
		for (RedisDBEnum status : values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return null;
	}

}
