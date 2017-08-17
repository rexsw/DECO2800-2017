package com.deco2800.marswars.net;

import com.esotericsoftware.kryo.Kryo;

/**
 * This is a class of static utility functions for network actions.
 */
public class ActionUtils {
    /**
     * Kryonet servers and clients need to register the actions they want,
     * and in the same order.
     * This function will register all action classes.
     */
    public static void registerActions(Kryo kryo) {
        kryo.register(JoinLobbyAction.class);
        kryo.register(MessageAction.class);
    }
}
