package no.nith.predikament;

import javax.swing.JFrame;

public class DerpTheFlag 
{
	public static void main(String[] args)
	{
		Game game = new Game();
		
		JFrame frame = new JFrame("HERPDERPZERPFLERPSHNERPKERP THE FLAG");
		
		frame.add(game);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		game.start();
	}
}
