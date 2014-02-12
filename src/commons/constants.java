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
public interface constants {
    public String uname=System.getProperty("user.name");
    public int server_port=12345;
    public Color COLOR_ERROR=Color.RED;
    public Color COLOR_INFO=Color.GRAY;
    public Color COLOR_MESSAGE=Color.BLUE;
    public Color COLOR_LIST=Color.ORANGE;
    public String MSG_JUST_JOINED="-just joined the chat.";
    public String MSG_JUST_LEFT="-just left the chat.";
}
