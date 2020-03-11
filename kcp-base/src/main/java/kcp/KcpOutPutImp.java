package kcp;

import com.backblaze.erasure.fec.Snmp;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;


/**
 * 基于udp方式实现
 *
 * Created by JinMiao
 * 2018/9/21.
 */
public class KcpOutPutImp implements KcpOutput {

    @Override
    public void out(ByteBuf data, Kcp kcp) {
        // 更新统计信息
        Snmp.snmp.OutPkts.increment();
        Snmp.snmp.OutBytes.add(data.writerIndex());
        // 获取对应的连接对象
        User user = (User) kcp.getUser();
        // 组装为udp包
        DatagramPacket temp = new DatagramPacket(data, user.getRemoteAddress(), user.getLocalAddress());
        // 发送数据
        user.getChannel().writeAndFlush(temp);
    }
}
