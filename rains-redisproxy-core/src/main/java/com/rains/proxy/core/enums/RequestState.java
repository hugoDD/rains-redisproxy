/**
 * 
 */
package com.rains.proxy.core.enums;

/**
 * @author liubing
 *
 */
public enum RequestState {
	READ_SKIP,//健壮性考虑，如果第一个字符不是*则skip直到遇到*
    READ_INIT,// 开始
    READ_ARG_COUNT,// 读取参数数量(新协议)
    READ_ARG_LENGTH, 	// 读取参数长度(新协议)
    READ_ARG, // 读取参数(新协议)
    READ_END// 结束
}
