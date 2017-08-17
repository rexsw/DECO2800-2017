package com.deco2800.marswars.net;

import com.esotericsoftware.kryo.Kryo;

public class ActionUtils {
    public static void registerActions(Kryo kryo) {
        kryo.register(JoinLobbyAction.class);
        kryo.register(MessageAction.class);
    }
}
