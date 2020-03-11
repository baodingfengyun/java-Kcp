package test;

import com.backblaze.erasure.fec.Snmp;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import kcp.*;

import java.net.InetSocketAddress;

/**
 * 测试单连接吞吐量
 * Created by JinMiao
 * 2019-06-27.
 */
public class KcpPingPongExampleClient implements KcpListener {

    public static void main(String[] args) {
        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.nodelay(true, 40, 2, true);
        channelConfig.setSndwnd(1024);
        channelConfig.setRcvwnd(1024);
        channelConfig.setMtu(1400);
        //channelConfig.setFecDataShardCount(10);
        //channelConfig.setFecParityShardCount(3);
        channelConfig.setAckNoDelay(true);
        channelConfig.setCrc32Check(true);
        //channelConfig.setTimeoutMillis(10000);

        KcpClient kcpClient = new KcpClient();
        int workers = Runtime.getRuntime().availableProcessors();
        kcpClient.init(workers, channelConfig);

        KcpPingPongExampleClient kcpPingPongClient = new KcpPingPongExampleClient();
        String ip = "127.0.0.1";
        int port = 10001;
        kcpClient.connect(new InetSocketAddress(ip, port), channelConfig, kcpPingPongClient);
    }

    int i = 0;

    @Override
    public void onConnected(Ukcp ukcp) {
        for (int i = 0; i < 100; i++) {
            ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer(1024);
            byteBuf.writeInt(i++);
            byte[] bytes = new byte[1020];
            byteBuf.writeBytes(bytes);
            ukcp.writeOrderedReliableMessage(byteBuf);
            byteBuf.release();
        }
    }

    int j = 0;

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp, int protocolType) {
        ukcp.writeOrderedReliableMessage(byteBuf);
        int id = byteBuf.getInt(0);
        //if(j-id%10!=0){
        //    System.out.println("id"+id +"  j" +j);
        //}

        j++;
        if (j % 100000 == 0) {
            System.out.println(Snmp.snmp.toString());
            System.out.println("收到了 返回回去" + j);
        }
    }

    @Override
    public void handleException(Throwable ex, Ukcp kcp) {
        ex.printStackTrace();
    }

    @Override
    public void handleClose(Ukcp kcp) {
        System.out.println(kcp + " 连接断开了");
    }


}
