package TableSimulator1;
import javax.swing.JFrame;
// Double buffering ÀÌ¿כ
public class TableSimulator extends JFrame {
	public TableSimulator(){
		setTitle("Table Simulator");
		setSize(Main.SCREEN_SIZE_X,Main.SCREEN_SIZE_Y);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}