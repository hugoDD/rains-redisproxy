/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rains.proxy.core.reply.impl;

import com.rains.proxy.core.enums.Type;
import com.rains.proxy.core.utils.ProtoUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author dourx
 * @version V1.0
 * @Description:
 * 批量回复
 * 服务器使用批量回复来返回二进制安全的字符串，字符串的最大长度为 512 MB 。
 *
 * 客户端：GET mykey
 * 服务器：foobar
 * 服务器发送的内容中：
 *
 * 第一字节为 "$" 符号
 * 接下来跟着的是表示实际回复长度的数字值
 * 之后跟着一个 CRLF
 * 再后面跟着的是实际回复数据
 * 最末尾是另一个 CRLF
 * 对于前面的 GET 命令，服务器实际发送的内容为：
 *
 * "$6\r\nfoobar\r\n"
 * 如果被请求的值不存在， 那么批量回复会将特殊值 -1 用作回复的长度值， 就像这样：
 *
 * 客户端：GET non-existing-key
 * 服务器：$-1
 * 这种回复称为空批量回复（NULL Bulk Reply）。
 *
 * 当请求对象不存在时，客户端应该返回空对象，而不是空字符串： 比如 Ruby 库应该返回 nil ， 而 C 库应该返回 NULL （或者在回复对象中设置一个特殊标志）， 诸如此类。
 * @date 2018/5/24  0:35
 */
public class BulkRedisReply extends CommonRedisReply {

    private int length=-1;

    public BulkRedisReply(byte[] value) {
        this();
        this.value = value;
    }

    public BulkRedisReply() {
        super(Type.BULK);
    }

    public void setLength(int length) {
        this.length = length;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.wanda.ffan.redis.proxy.core.reply.impl.AbstractRedisReply#doEncode
     * (io.netty.buffer.ByteBuf)
     */
    @Override
    public void doEncode(ByteBuf out) {
        out.writeBytes(ProtoUtils.convertIntToByteArray(length));
        writeCRLF(out);
        if (length > -1 && value != null) {
            out.writeBytes(value);
            writeCRLF(out);
        }
    }

}
