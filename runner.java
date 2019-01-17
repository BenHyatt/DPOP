import javax.swing.*;
public class runner {
	public static void main(String[] args) {
		JFrame frame=new JFrame("DPOP Pro");
		final String title="DPOP Crafter Pro";
		int closeTime=Integer.parseInt(JOptionPane.showInputDialog(
			frame,
			"Welcome to DPOP Pro.\nAt which hour does COSI close today?\nUse military time (i.e.), 5:00 is \"17\".",
			title,
			JOptionPane.PLAIN_MESSAGE
		));
		int numVolunteers=Integer.parseInt(JOptionPane.showInputDialog(
			frame,
			"How many volunteers are scheduled today?",
			title,
			JOptionPane.PLAIN_MESSAGE	
		));
		int[][] baseDPOP=new int[(closeTime-9)*2][numVolunteers];
		dpop ben=new dpop(baseDPOP, closeTime);
		ben.meeting();
		ben.lunch();
		ben.movie();
		ben.planet();
		ben.dinos();
		ben.movieSecond();
		ben.planetSecond();
		ben.STARS();
		ben.greet();
		ben.atrium();
		ben.ROVE();
		ben.export();
		//ben.movie();
		//ben.planet();
		//ben.atrium();
		JOptionPane.showMessageDialog(
			frame,
			"Finished exporting",
			title,
			JOptionPane.PLAIN_MESSAGE
		);
		System.exit(0);
	}
}
