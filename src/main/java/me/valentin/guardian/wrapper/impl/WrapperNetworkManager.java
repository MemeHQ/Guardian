package me.valentin.guardian.wrapper.impl;

import me.valentin.guardian.wrapper.ClassWrapper;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.util.io.netty.channel.Channel;

public class WrapperNetworkManager extends ClassWrapper<NetworkManager> {

	public WrapperNetworkManager(NetworkManager instance) {
		super(instance, NetworkManager.class);
	}

	public Channel getChannel() {
		return (Channel) this.getField("m");
	}
}
