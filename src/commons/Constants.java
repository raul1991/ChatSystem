/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import java.awt.Color;

/**
 *
 * @author user
 */
public interface Constants {
    public String uname=System.getProperty("user.name");
    public int server_port=12345;
    public Color COLOR_ERROR=Color.RED;
    public Color COLOR_INFO=Color.PINK;
    public Color COLOR_MESSAGE=Color.BLUE;
    public Color COLOR_LIST=Color.ORANGE;
    public String MSG_JUST_JOINED="-just joined the chat.";
    public String MSG_JUST_LEFT="-just left the chat.";
    public String ACTION_SEPARATOR="#";
    public String ACTION_SEPARATOR_USER_LIST="/";
    public String ACTION_SEPARATOR_USER_JOINED_TIME=":";
    public String ACTION_SEPARATOR_USER_PROPERTIES=";";
    public String ACTION_SUFFIX_IS_AVAILABLE="IS_AVAILABLE";
    public String ACTION_SUFFIX_JUST_JOINED="JUST_JOINED";
    public String ACTION_SUFFIX_USER_EXITED="USER_EXITED";
    public String ACTION_SUFFIX_MESSAGE="MESSAGE";
    public String ACTION_SUFFIX_STATUS_CHANGE="STATUS_CHANGE";
    public String ACTION_SUFFIX_GIVE_ME_LISTS="LIST";
    public String ACTION_SUFFIX_GIVE_ME_INFO="INFO";
    public String MSG_USER_ALREADY_EXISTS="Username already exists.";
    public String MSG_NO_SERVER_FOUND="No such server found in the network.";

}
