package io.butty.network.connect;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class UserHandle extends ChannelInboundHandlerAdapter {
    UserConnect connect;

    public UserHandle(UserConnect connect){
        this.connect = connect;
    }

    //Server read
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf result = (ByteBuf) msg;
        this.connect.context.write(result);
        this.connect.context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        UserConnect.LOGGER.warn("User connect has exception.",cause);
        ctx.close();
    }


    //Server send
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }
}
