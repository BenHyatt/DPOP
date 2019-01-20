import javax.swing.*;
import java.io.File;

public class runner {
	public static void main(String[] args) {
		final String title = "DPOP Crafter Extreme";
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int closeTime = 17;
		int numVolunteers;
		do {
			numVolunteers = Integer.parseInt(JOptionPane.showInputDialog(frame,
					"How many volunteers are scheduled today?", title, JOptionPane.PLAIN_MESSAGE));
		} while (!(numVolunteers >= 1));
		String volName;
		String[] startEndTimes = new String[numVolunteers];
		int volStart;
		int volEnd;
		final String[] startChoices = { "9", "10", "11", "12", "13" };
		final String[] endChoices = { "17", "13", "14", "15", "16", "17" };
		int[][] baseDPOP = new int[(closeTime - 9) * 2][numVolunteers];
		String[] volNames = new String[numVolunteers];
		for (int i = 0; i < numVolunteers; i++) {
			volName = JOptionPane.showInputDialog(frame,
					"What is this volunteer's name?\nOn person " + (i + 1) + "/" + numVolunteers, title,
					JOptionPane.PLAIN_MESSAGE);
			volNames[i] = volName;
			volStart = Integer.parseInt((String) JOptionPane.showInputDialog(frame,
					"When does " + volName + " begin work today?\nOn person " + (i + 1) + "/" + numVolunteers, title,
					JOptionPane.PLAIN_MESSAGE, null, startChoices, 0));
			volEnd = Integer.parseInt((String) JOptionPane.showInputDialog(frame,
					"When does " + volName + " leave today?\nOn person " + (i + 1) + "/" + numVolunteers, title,
					JOptionPane.PLAIN_MESSAGE, null, endChoices, 4));
			// System.out.println(volName + " starts at " + volStart + " and ends at " +
			// volEnd);
			for (int time = ((volStart - 9) * 2); time < ((volEnd - 9) * 2); time++) {
				baseDPOP[time][i] = 1;
			}
			startEndTimes[i] = clockTime((volStart - 9) * 2) + "-" + clockTime((volEnd - 9) * 2);
		}
		String fileName = "dpop.xls";
		File file = new File(fileName);
		if (file.exists()) {
			String choice = "a";
			while (!choice.equals("yes") && !choice.equals("no")) {
				choice = JOptionPane.showInputDialog(frame,
						"dpop.xls already exists, would you like to overwrite? \"yes\" or \"no\"", title,
						JOptionPane.PLAIN_MESSAGE);
			}
			if (choice.equals("yes")) {
				file = new File(fileName);
			} else {
				fileName = JOptionPane.showInputDialog(frame,
						"What would you like to name it? File extension must be \".xls\"", title,
						JOptionPane.PLAIN_MESSAGE);
			}
		}
		dpop ben = new dpop(baseDPOP, closeTime, volNames);
		ben.lunch();
		ben.meeting();
		ben.movie();
		ben.planet();
		ben.dinos();
		ben.movieSecond();
		ben.planetSecond();
		ben.STARS();
		ben.greet();
		ben.atrium();
		ben.ROVE();
		ben.helpLoadPlanet();
		ben.helpLoadMovie();
		if (!ben.fairCheck().equals("")) {
			JOptionPane.showMessageDialog(frame, ben.fairCheck(), title, JOptionPane.PLAIN_MESSAGE);
		}
		if (!ben.emptyCheck().equals("")) {
			JOptionPane.showMessageDialog(frame, ben.emptyCheck(), title, JOptionPane.PLAIN_MESSAGE);
		}
		export dpop = new export(ben.output(), volNames, fileName, startEndTimes);
		dpop.process();
		dpop.toExcel();
		JOptionPane.showMessageDialog(frame, "DPOP has been generated!", title, JOptionPane.PLAIN_MESSAGE);
		JOptionPane.showMessageDialog(frame, "Need help for:\n" + ben.returnHelp(), title, JOptionPane.PLAIN_MESSAGE);
		System.exit(0);
	}

	public static String clockTime(int i) {
		String time = "";
		double k;
		if (i * .5 + 9 > 12.5) {
			k = (i * .5 + 9) - 12;
		} else {
			k = (i * .5 + 9);
		}
		if (k % 1 != 0) {
			time = Math.round(Math.floor(k)) + ":30";
		} else {
			time = Math.round(Math.floor(k)) + ":00";
		}
		return time;
	}
}
