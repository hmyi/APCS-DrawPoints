/* Tim Yi
 * AP Computer Science
 * 01/31/2018
 * Project Draw Points - Panel Wrapper
 */

package apcsjava;

import java.awt.EventQueue;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class DrawPointsWrapper extends JFrame {

	public final int FRAMESIZE = 1000;
	public final int BTNSPACE = 100;
	public final int HRZSPACE = 8;
	
	public DrawPointsWrapper() {
        setSize(5*FRAMESIZE/4+HRZSPACE, FRAMESIZE+BTNSPACE);
		add(new DrawPoints(FRAMESIZE, FRAMESIZE));
        setResizable(false);
        setTitle("Draw Points");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DrawPointsWrapper go = new DrawPointsWrapper();
                go.setVisible(true);
            }
        });
	}

}
