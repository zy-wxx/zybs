package com.example.aaaaaac.net

import android.util.Log
import com.example.aaaaaac.net.handler.Callback
import com.example.aaaaaac.net.handler.ClientHandlerInitializer
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

private const val TAG = "ClientBootStrap"
class ClientBootStrap(private var callback: Callback) {
    private var bootstrap: Bootstrap? = null
    private var channel: Channel? = null
    private var workerGroup: NioEventLoopGroup? = null

    //constructor
    init {
        val socketAddress = InetSocketAddress(ClientConfig.REMOTE_IP_ADDR, ClientConfig.REMOTE_TCP_PORT)
        workerGroup = NioEventLoopGroup(ClientConfig.WORKER_GROUP_COUNT)
        bootstrap = Bootstrap()
        bootstrap!!.group(workerGroup)
            .channel(NioSocketChannel::class.java)
            .remoteAddress(socketAddress)
            .handler(ClientHandlerInitializer(callback))
    }

    @Throws(InterruptedException::class)
    fun connect(): Channel? {
        val future = bootstrap!!.connect().sync()
        future.addListener(ChannelFutureListener { channelFuture ->
            if (channelFuture.isSuccess) {
                Log.e(TAG, "connect succeed")
            } else {
                Log.e(TAG, "connect failed")
            }
        })
        channel = future.channel()
        return channel
    }

    fun close() {
        channel?.close()
        workerGroup!!.shutdownGracefully()
    }
}