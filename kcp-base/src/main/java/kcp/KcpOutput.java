package kcp;

import io.netty.buffer.ByteBuf;

/**
 * @author <a href="mailto:szhnet@gmail.com">szh</a>
 */
public interface KcpOutput {

    /**
     * 写数据
     * @param data
     * @param kcp
     */
    void out(ByteBuf data, Kcp kcp);

}
