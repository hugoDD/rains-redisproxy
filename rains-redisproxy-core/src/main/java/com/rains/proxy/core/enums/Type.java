/**
 * 
 */
package com.rains.proxy.core.enums;

/**
 * 类型枚举类
 * @author liubing
 *
 */
public enum Type {
	EMPTY((byte)' '),//空处理，不返回结果
	ERROR((byte) '-'),
    STATUS((byte) '+'),
    BULK((byte) '$'),
    INTEGER((byte) ':'),
    MULTYBULK((byte) '*'),;

    private byte code;

    Type(byte code) {
      this.code = code;
    }

    public byte getCode() {
      return this.code;
    }
}
