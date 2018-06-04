package com.rains.proxy.core.utils;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

public class ProtoUtils {

	public static final byte[] OK =   "OK".getBytes();
	public static final byte[] PONG =  "PONG".getBytes();
	public static final byte[] ERR_NO_AUTH = "NOAUTH Authentication required.".getBytes();
	public static final byte[] ERR_NO_AUTH_PASSWORD = "ERR invalid password.".getBytes();
	public static final byte[] ERR_NO_AUTH_NO_PASSWORD = "ERR Client sent AUTH, but no password is set".getBytes();
	public static final byte[] ERR_PIPELINE_BACKEND = "ERR pipeline error".getBytes();
	public static final byte[] ERR_INVALID_COMMAND = "ERR invalid command exist.".getBytes();

	public static final byte[] ERROR_ERR_BULK = "ERR".getBytes();
	public static final byte[] WRONGTYPE_ERR_BULK = "WRONGTYPE".getBytes();
	public static final byte[] NOIMPLEMENT_ERR_BULK = "NOIMPLEMENT".getBytes();
	private final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999,
			9999999, 99999999, 999999999, Integer.MAX_VALUE };

	private final static byte[] DigitTens = { '0', '0', '0', '0', '0', '0',
			'0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1',
			'1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3',
			'3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4',
			'4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5',
			'5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7',
			'7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8',
			'8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9',
			'9', '9', '9', };

	private final static byte[] DigitOnes = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', };

	private final static byte[] digits = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };

	public static byte[] convertIntToByteArray(int value) {
		int originValue = value;
		int size = 0;
		if (value < 0) {
			value = -value;
		}

		while (value > sizeTable[size])
			size++;
		size++;
		if (originValue < 0)
			size++;

		int q, r;
		byte[] buf = new byte[size];
		if (originValue < 0) {
			buf[0] = '-';
		}
		int charPos = size;

		while (value >= 65536) {
			q = value / 100;
			r = value - ((q << 6) + (q << 5) + (q << 2));
			value = q;
			buf[--charPos] = DigitOnes[r];
			buf[--charPos] = DigitTens[r];
		}

		for (;;) {
			q = (value * 52429) >>> (16 + 3);
			r = value - ((q << 3) + (q << 1));
			buf[--charPos] = digits[r];
			value = q;
			if (value == 0)
				break;
		}
		return buf;
	}

	public static byte[] buildErrorReplyBytes(String message) {

		return ("ERR " + message).getBytes();
	}

	public static int readInt(ByteBuf buffer) throws IndexOutOfBoundsException {

		long size = 0;
		boolean isNeg = false;

		byte b =buffer.readByte();
		while (b != '\r') {
			if (b == '-') {
				isNeg = true;
			} else {
				size = size * 10 + b - '0';
			}
			b = buffer.readByte();
		}

		// skip \r\n
		buffer.readByte();

		size = (isNeg ? -size : size);
		if (size > Integer.MAX_VALUE) {
			throw new RuntimeException("Cannot allocate more than " + Integer.MAX_VALUE + " bytes");
		}
		if (size < Integer.MIN_VALUE) {
			throw new RuntimeException("Cannot allocate less than " + Integer.MIN_VALUE + " bytes");
		}
		return (int) size;
	}


}
