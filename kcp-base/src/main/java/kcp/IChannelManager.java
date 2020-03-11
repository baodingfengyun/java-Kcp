package kcp;

import io.netty.channel.socket.DatagramPacket;

import java.net.SocketAddress;
import java.util.Collection;


/**
 * Created by JinMiao
 * 2019/10/16.
 */
public interface IChannelManager {

    /**
     * 通过数据查找对应的kcp
     * @param msg
     * @return
     */
    Ukcp get(DatagramPacket msg);

    /**
     * 注册地址与kcp
     * @param socketAddress
     * @param ukcp
     */
    void New(SocketAddress socketAddress, Ukcp ukcp);

    /**
     * 删除kcp
     * @param ukcp
     */
    void del(Ukcp ukcp);

    /**
     * 获取所有的kcp
     * @return
     */
    Collection<Ukcp> getAll();
}
