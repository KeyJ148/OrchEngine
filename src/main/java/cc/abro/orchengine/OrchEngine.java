package cc.abro.orchengine;

import cc.abro.orchengine.implementation.GameInterface;
import cc.abro.orchengine.implementation.NetGameReadInterface;
import cc.abro.orchengine.implementation.NetServerReadInterface;
import cc.abro.orchengine.implementation.ServerInterface;
import cc.abro.orchengine.profiles.Profile;
import cc.abro.orchengine.profiles.Profiles;

public class OrchEngine {

	public static void start(GameInterface game, NetGameReadInterface netGameRead,
							 ServerInterface server, NetServerReadInterface netServerRead){
		Manager.addService(game);
		Manager.addService(netGameRead);
		Manager.addService(server);
		Manager.addService(netServerRead);
		new Loader().tryInit();
	}

	public static void start(Class<? extends GameInterface> gameClass, Class<? extends NetGameReadInterface> netGameReadClass,
							 Class<? extends ServerInterface> serverClass, Class<? extends NetServerReadInterface> netServerReadClass) {
		Manager.addService(netGameReadClass);
		Manager.addService(gameClass);
		Manager.addService(serverClass);
		Manager.addService(netServerReadClass);
		new Loader().tryInit();
	}

	public static void start(GameInterface game, NetGameReadInterface netGameRead,
							 ServerInterface server, NetServerReadInterface netServerRead, Profile profile){
		Profiles.initProfile(profile);
		start(game, netGameRead, server, netServerRead);
	}

	public static void start(Class<? extends GameInterface> gameClass, Class<? extends NetGameReadInterface> netGameReadClass,
							 Class<? extends ServerInterface> serverClass, Class<? extends NetServerReadInterface> netServerReadClass,
							 Profile profile) {
		Profiles.initProfile(profile);
		start(gameClass, netGameReadClass, serverClass, netServerReadClass);
	}
}
