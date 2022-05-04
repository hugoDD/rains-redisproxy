package com.rains.proxy.core.reply;

import com.rains.proxy.core.enums.Type;
import io.netty.buffer.ByteBuf;

/**
 * redis 回答
 *
 * Redis 命令会返回多种不同类型的回复。
 *
 * 通过检查服务器发回数据的第一个字节， 可以确定这个回复是什么类型：
 *
 * 状态回复（status reply）的第一个字节是 "+"  </br>
 * 错误回复（error reply）的第一个字节是 "-"   </br>
 * 整数回复（integer reply）的第一个字节是 ":" </br>
 * 批量回复（bulk reply）的第一个字节是 "$"    </br>
 * 多条批量回复（multi bulk reply）的第一个字节是 "*"  </br>
 *
 * @author liubing
 *
 */
public interface IRedisReply {

	  /**
	   * 获取类型
	   * @return
	   */
	  Type getType();

	  /**
	   * 设置类型
	   * @param type
	   */
	  void setType(Type type);

	  /**
	   * 编码
	   * @param out
	   */
	  void encode(ByteBuf out);


}
