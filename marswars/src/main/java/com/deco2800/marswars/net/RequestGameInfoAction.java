package com.deco2800.marswars.net;

/**
 * An action that requests updated game information from the server.
 * 
 * @author James McCall
 *
 */
public class RequestGameInfoAction implements Action {

    public RequestGameInfoAction() {
        // Blank constructor for kryonet
    }
    
    @Override
    public String toString() {
        return "Requested game info";
    }
}
