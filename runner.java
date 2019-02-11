import javax.swing.*;
import java.io.File;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class runner {
	public static void main(String[] args) {

		String choice = "";
		final String title = "DPOP Crafter Extreme";
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int closeTime = 17;
		String input;
		int numVolunteers = -1;
		ImageIcon icon = new ImageIcon("logo.png");
		Scanner scanner;
		do {
			input = (String) JOptionPane.showInputDialog(frame,
					"Welcome to DPOP Crafter Extreme!\nHow many volunteers are scheduled today?", title,
					JOptionPane.PLAIN_MESSAGE, icon, null, null);
			scanner = new Scanner(input);
			while (!scanner.hasNextInt() && scanner.hasNext()) {
				scanner.next();
			}
			if (scanner.hasNextInt())
				numVolunteers = scanner.nextInt();
		} while (!(numVolunteers >= 1 && numVolunteers <= 25));
		scanner.close();

		String volName;
		String[] startEndTimes = new String[numVolunteers];
		int volStart;
		int volEnd;
		final String[] startChoices = { "9", "10", "11", "12", "13" };
		final String[] endChoices = { "13", "14", "15", "16", "17" };
		int[][] baseDPOP = new int[(closeTime - 9) * 2][numVolunteers];
		String[] volNames = new String[numVolunteers];
		for (int i = 0; i < numVolunteers; i++) {
			volName = (String) JOptionPane.showInputDialog(frame,
					"What is this volunteer's name?\nOn person " + (i + 1) + "/" + numVolunteers, title,
					JOptionPane.PLAIN_MESSAGE, icon, null, null);
			volNames[i] = volName;

			volEnd = 17;
			volStart = 9;

			for (int time = ((volStart - 9) * 2); time < ((volEnd - 9) * 2); time++) {
				baseDPOP[time][i] = 1;
			}
			startEndTimes[i] = clockTime((volStart - 9) * 2) + "-" + clockTime((volEnd - 9) * 2);
		}
		String message = "";
		for (int i = 0; i < volNames.length; i++)
			message += volNames[i] + " here " + startEndTimes[i] + "\n";
		while (!choice.equals("yes") && !choice.equals("no")) {
			choice = (String) JOptionPane.showInputDialog(frame,
					"Your data is:\n\n" + message + "\nWould you like to edit any? \"yes\" or \"no\"", title,
					JOptionPane.PLAIN_MESSAGE, icon, null, null);
			if (choice == null) {
				System.exit(0);
			}
		}
		String userChoice;
		// Begin editing the data
		while (choice.equals("yes")) {
			do {
				userChoice = (String) JOptionPane.showInputDialog(frame,
						"What is the name of the volunteer you wish to edit?\n\n" + message, title,
						JOptionPane.PLAIN_MESSAGE, icon, null, null);
			} while (find(volNames, userChoice) < 0);
			int i = find(volNames, userChoice);
			do {
				userChoice = (String) JOptionPane.showInputDialog(
						frame, "Would you like to edit name or time for\n" + volNames[i] + " who is here "
								+ startEndTimes[i] + "?\nType \"name\" or \"time\"",
						title, JOptionPane.PLAIN_MESSAGE, icon, null, null);
			} while (!(userChoice.equals("name") || userChoice.equals("time")));
			if (userChoice.equals("name")) {
				volNames[i] = (String) JOptionPane.showInputDialog(frame,
						"What should\n" + volNames[i] + " who is here " + startEndTimes[i] + " be called?", title,
						JOptionPane.PLAIN_MESSAGE, icon, null, volNames[i]);
			} else {
				volStart = Integer.parseInt(
						(String) JOptionPane.showInputDialog(frame, "When should " + volNames[i] + " begin work today?",
								title, JOptionPane.PLAIN_MESSAGE, icon, startChoices, 0));
				volEnd = Integer.parseInt(
						(String) JOptionPane.showInputDialog(frame, "When should " + volNames[i] + " leave today?",
								title, JOptionPane.PLAIN_MESSAGE, icon, endChoices, "17"));
				for (int x = 0; x < baseDPOP.length; x++)
					baseDPOP[x][i] = 0;
				for (int time = ((volStart - 9) * 2); time < ((volEnd - 9) * 2); time++)
					baseDPOP[time][i] = 1;
				startEndTimes[i] = clockTime((volStart - 9) * 2) + "-" + clockTime((volEnd - 9) * 2);
			}
			message = "";
			// Regenerate message
			for (int a = 0; a < volNames.length; a++)
				message += volNames[a] + " here " + startEndTimes[a] + "\n";
			do {
				choice = (String) JOptionPane.showInputDialog(frame,
						"Your data is:\n\n" + message + "\nWould you like to edit anthing else? \"yes\" or \"no\"",
						title, JOptionPane.PLAIN_MESSAGE, icon, null, null);
			} while (!(choice.equals("no") || choice.equals("yes")));
		}
		choice = "";
		String fileName;
		File file;
		do {
			fileName = (String) JOptionPane.showInputDialog(frame,
					"What would you like to name your file? File extension must be \".xls\"", title,
					JOptionPane.PLAIN_MESSAGE, icon, null,
					"GSVol-" + new SimpleDateFormat("MMM-d").format(new Date()) + ".xls");
			file = new File(fileName);
			if (file.exists()) {
				do {
					choice = (String) JOptionPane.showInputDialog(frame,
							fileName + " already exists, would you like to overwrite? \"yes\" or \"no\"", title,
							JOptionPane.PLAIN_MESSAGE, icon, null, null);
				} while (!choice.equals("yes") && !choice.equals("no"));
			}

		} while (file.exists() && choice.equals("no"));
		dpop ben = new dpop(baseDPOP, closeTime, volNames);
		generate(ben);
		int cnt = 0;
		while (!ben.fairCheck().equals("") && cnt < 10) {
			System.out.println("Regenerating becase " + ben.fairCheck());
			ben.reset();
			generate(ben);
			cnt++;
		}
		message = "";
		if (!ben.fairCheck().equals("")) {
			message += ben.fairCheck();
		} else {
			message += "\n\nAdditionally, our AI fairness checker has determined that dino doors have been evenly distributed";
		}
		if (!ben.emptyCheck().equals("")) {
			message += ben.emptyCheck();
		} else {
			message += "\n\nAlso, our AI algorithm has confirmed that everyone has something to do throughout their time volunteering";
		}
		if (!ben.empathy().equals("")) {
			message += ben.empathy();
		} else {
			message += "\n\nFinally, our AI algorithm has found that no one has more than 2 hours of doors";
		}

		export dpop = new export(ben.output(), volNames, fileName, startEndTimes);
		dpop.process();
		dpop.toExcel();
		JOptionPane.showMessageDialog(frame, "Your DPOP has been generated at " + fileName + message, title,
				JOptionPane.PLAIN_MESSAGE, icon);
		JOptionPane.showMessageDialog(frame, "You need help for:\n" + ben.returnHelp(), title,
				JOptionPane.PLAIN_MESSAGE, icon);
		System.exit(0);
	}

	public static void generate(dpop ben) {
		ben.lunch();
		ben.meeting();
		ben.movie();
		ben.planet();
		ben.dinos();
		// ben.amnh();
		ben.movieSecond();
		ben.planetSecond();
		ben.STARS();
		ben.greet();
		ben.atrium();
		ben.ROVE();
		ben.helpLoadPlanet();
		ben.helpLoadMovie();
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

	public static int find(String[] a, String target) {
		for (int i = 0; i < a.length; i++)
			if (a[i].equals(target))
				return i;

		return -1;
	}
}
