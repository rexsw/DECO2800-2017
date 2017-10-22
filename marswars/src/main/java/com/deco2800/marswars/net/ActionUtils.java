package com.deco2800.marswars.net;

import com.deco2800.marswars.util.ServerGameInformation;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;
import com.esotericsoftware.kryo.Kryo;

import java.util.ArrayList;

/**
 * This is a class of static utility functions for network actions.
 */
public class ActionUtils {
    /**
     * Kryonet servers and clients need to register the actions they want,
     * and in the same order.
     * This function will register all action classes.
     */
    private ActionUtils(){}

    public static void registerActions(Kryo kryo) {
        kryo.register(JoinLobbyAction.class);
        kryo.register(MessageAction.class);
        kryo.register(LeaveLobbyAction.class);
        kryo.register(ReadyAction.class);
        kryo.register(LobbyAction.class);
        kryo.register(ArrayList.class);
        kryo.register(LobbyUser.class);
        kryo.register(LobbyMessageAction.class);
        kryo.register(LobbyRequestAction.class);
        kryo.register(StartRequestAction.class);
        kryo.register(StartGameAction.class);
        kryo.register(ServerGameInformation.class);
        kryo.register(MapTypes.class);
        kryo.register(MapSizeTypes.class);
        kryo.register(MapSizeAction.class);
        kryo.register(MapTypeAction.class);
        kryo.register(GameInfoAction.class);
        kryo.register(RequestGameInfoAction.class);
    }
}
