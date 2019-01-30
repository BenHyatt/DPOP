import java.util.ArrayList;
import java.util.List;

public class dpop {
	int dpop[][];
	String help[][];
	int[] lunchTimes = { 11, 12, 13 };
	int[] movieTimes = { 12, 13, 14, 15 };
	int[] planetTimes = { 11, 12, 13, 14, 15, 16 };
	int[] dinoTimes = { 10, 11, 12, 13, 14, 15, 16 };
	int[] amnhTimes = { 10, 11, 12, 13, 14, 15, 16 };
	double[] greetTimes = { 10, 10.5 };
	double[] atriumTimes = { 10, 10.5, 11, 11.5, 12, 12.5, 13, 13.5, 14, 14.5, 15, 15.5, 16, 16.5 };
	double[] STARTimes = { 10, 11, 12, 13, 14, 15, 16 };
	double[] roveTimes = { 10, 10.5, 14, 14.5, 15, 15.5, 16, 16.5 };
	double[] helpLoadMovie = { 11.5, 12.5, 13.5, 14.5 };
	double[] helpLoadPlanet = { 11, 12, 13, 14, 15, 16 };
	int numVolunteers;
	String[] volNames;
	int minTime;

	public dpop() {

	}

	public dpop(int[][] x, int closeTime, String[] names) {
		dpop = x;
		numVolunteers = dpop[0].length;
		help = new String[(closeTime - 9) * 2][3];
		volNames = names;
	}

	public void lunch() {
		int[] x = { -1, 0, 1 };
		int[] y = { -2, 0, 2 };
		// Add a lunch for everyone on the DPOP
		int shiftLength;
		int middleTime;
		for (int i = 0; i < dpop[0].length; i++) {
			shiftLength = findAmount(i, 1);
			if (shiftLength == 16) {
				dpop[time(lunchTimes[i % lunchTimes.length])][i] = 2;
				dpop[time(lunchTimes[i % lunchTimes.length] + .5)][i] = 2;
			} else if (shiftLength >= 12) {
				middleTime = middleTime(i);
				if (middleTime % 2 == 1) {
					middleTime--;
				}
				dpop[y[i % y.length] + middleTime][i] = 2;
				dpop[(y[i % y.length] + middleTime) + 1][i] = 2;
			} else if (shiftLength >= 8) {
				dpop[x[i % x.length] + middleTime(i)][i] = 2;
			}
		}
	}

	public int middleTime(int vol) {
		int start = 0;
		while (dpop[start][vol] == 0) {
			start++;
		}
		int end = start;
		while (end < dpop.length && dpop[end][vol] != 0) {
			end++;
		}
		// System.out.println("MIddle time of " + vol + " is " + (start + end) / 2);
		return (start + end) / 2;
	}

	public void movie() {
		// Assign all the movie times to a volunteer who doesn't have lunch or doors
		// before.
		// 3 represents what the movie task is
		int[] restrictions = { 5, 2, 0 };
		int[] post_restrictions = {};
		assignNecessary(movieTimes, 3, restrictions, post_restrictions);
	}

	public void movieSecond() {
		int[] prerestrictions = { 5, 2 };
		int maxPerSlot = 1;
		int maxPerVol = 8;
		int duration = 2;
		double[] movieDoubleTimes = new double[movieTimes.length];
		for (int i = 0; i < movieTimes.length; i++) {
			movieDoubleTimes[i] = movieTimes[i];
		}
		assignOptional(movieDoubleTimes, maxPerSlot, 3, prerestrictions, maxPerVol, duration);
	}

	public void planet() {
		int[] restrictions = {};
		int[] post_restrictions = {};
		assignNecessary(planetTimes, 4, restrictions, post_restrictions);
	}

	public void planetSecond() {
		int[] prerestrictions = {};
		int maxPerSlot = 1;
		int maxPerVol = 8;
		int duration = 2;
		double[] planetDoubleTimes = new double[planetTimes.length];
		for (int i = 0; i < planetTimes.length; i++) {
			planetDoubleTimes[i] = planetTimes[i];
		}
		int assignmentNumber = 4;
		assignOptional(planetDoubleTimes, maxPerSlot, assignmentNumber, prerestrictions, maxPerVol, duration);
	}

	public void dinos() {
		int[] pre_restrictions = { 5 };
		int[] post_restrictions = { 3 };
		assignNecessary(dinoTimes, 5, pre_restrictions, post_restrictions);
	}

	public void amnh() {
		int[] pre_restrictions = { 5, 14 };
		int[] post_restrictions = { 3 };
		assignNecessary(amnhTimes, 14, pre_restrictions, post_restrictions);
	}

	public void greet() {
		int[] prerestrictions = { 9 };
		int maxPerSlot = 3;
		int maxPerVol = 2;
		int duration = 1;// one slot, so 30 minutes
		assignOptional(greetTimes, maxPerSlot, 9, prerestrictions, maxPerVol, duration);
	}

	public void atrium() {
		int[] preRestrictions = {};
		int maxPerSlot = 3;
		int maxPerVol = 4;
		int duration = 1;// one slot so 30 minutes
		assignOptional(atriumTimes, maxPerSlot, 6, preRestrictions, maxPerVol, duration);
	}

	public void STARS() {
		int[] preRestrictions = { 10 };
		int maxPerSlot = 2;
		int maxPerVol = 2;
		int duration = 2;// two slots so 60 minutes
		assignOptional(STARTimes, maxPerSlot, 10, preRestrictions, maxPerVol, duration);
	}

	public void ROVE() {
		int[] preRestrictions = { 11 };
		int maxPerSlot = 3;
		int maxPerVol = 2;
		int duration = 1;
		int assignmentNumber = 11;
		assignOptional(roveTimes, maxPerSlot, assignmentNumber, preRestrictions, maxPerVol, duration);
	}

	public void helpLoadMovie() {
		int[] preRestrictions = {};
		int maxPerSlot = 1;
		int maxPerVol = 2;
		int duration = 1;
		int assignmentNumber = 12;
		assignOptional(helpLoadMovie, maxPerSlot, assignmentNumber, preRestrictions, maxPerVol, duration);
	}

	public void helpLoadPlanet() {
		int[] preRestrictions = {};
		int maxPerSlot = 1;
		int maxPerVol = 2;
		int duration = 1;
		int assignmentNumber = 13;
		assignOptional(helpLoadPlanet, maxPerSlot, assignmentNumber, preRestrictions, maxPerVol, duration);
	}

	// Assign activity to the array of times. Volunteer must not have any activity
	// from restrictions right before the activity
	// nor any activity from post_restrictions
	public void assignNecessary(int[] times, int activity, int[] restrictions, int[] post_restrictions) {
		for (int i = 0; i < times.length; i++) {
			int volunteerCounter = -1;
			int randomStart = (int) (Math.random() * numVolunteers);
			int currentVolunteer = (randomStart + volunteerCounter) % numVolunteers;
			List<Integer> possibleVolunteers = new ArrayList<Integer>();
			int ActivityTime = time(times[i]);
			boolean foundVol = false;
			// Go through all volunteers until you find a good one
			while (volunteerCounter < numVolunteers - 1) {
				// If they have this time clear
				volunteerCounter++;
				currentVolunteer = (randomStart + volunteerCounter) % numVolunteers;
				if (isClear(ActivityTime, currentVolunteer) && isClear(ActivityTime + 1, currentVolunteer)
				// && (!(volNames[currentVolunteer].equals("Ben") && activity == 5))
				) {
					// Assume you've found a volunteer, until you prove false
					foundVol = true;
					// Go through restrictions, and if they meet any of them, then realize you
					// haven't found what you're looking for
					for (int x = 0; x < restrictions.length; x++) {
						if (haveBefore(currentVolunteer, restrictions[x], ActivityTime - 1)) {
							foundVol = false;
						}
					}
					for (int x = 0; x < post_restrictions.length; x++) {
						if (ActivityTime < dpop.length - 2
								&& dpop[ActivityTime + 2][currentVolunteer] == post_restrictions[x]) {
							foundVol = false;
						}
					}
					if (foundVol == true) {
						possibleVolunteers.add(currentVolunteer);
					}
				}
			}
			// What if there are multiple volunteers who have the spot clear for the
			// activity?
			// Then, give it to the volunteer who has it the least
			// If there are multiple are tied for least, give it to who has had it the
			// longest time ago
			// If it's the same amount (would happen if 2 people have never had it yet),
			// give it to the randomly generated seed
			if (possibleVolunteers.size() > 0) {
				int min_amount = 99999;
				// Find volunteer who has minimum amount
				for (int vol : possibleVolunteers) {
					if (findAmount(vol, activity) < min_amount) {
						min_amount = findAmount(vol, activity);
						currentVolunteer = vol;
					}
				}
				// Find volunteers who have this min amount
				List<Integer> possibleVolunteers2 = new ArrayList<Integer>();
				for (int vol : possibleVolunteers) {
					if (findAmount(vol, activity) == min_amount) {
						possibleVolunteers2.add(vol);
					}
				}
				// Find best volunteer from all who have min amount
				// Finds the volunteer who hasn't had the activity for the longest time
				// System.out.println("\nAssigning the " + i + " activity " + activity + "
				// someone has this activity "+ min_amount + " times");
				System.out.println("Eligible volunteers are: " + possibleVolunteers);
				System.out.println("Eligible volunteers2 are: " + possibleVolunteers2);
				if (possibleVolunteers2.size() > 1) {
					minTime = 99999;
					for (int vol : possibleVolunteers2) {
						// System.out.println("Volunteer " + vol + " has last had " + activity + " at "
						// + getLatest(vol, activity) + " minTime is " + minTime);
						if (getLatest(vol, activity) < minTime) {
							currentVolunteer = vol;
							minTime = getLatest(vol, activity);
						}
					}
				}
				dpop[time(times[i])][currentVolunteer] = activity;
				dpop[time(times[i] + .5)][currentVolunteer] = activity;
			} else {
				// System.out.println("HELP: " + times[i] + " for " + activity);
				addHelp(time(times[i]), activity);
			}

		}
	}

	public void addHelp(int spot, int activity) {
		if (activity == 3) {
			help[spot][2] = "Movie-";
			help[spot + 1][2] = "Movie-";
		}
		if (activity == 4) {
			help[spot][1] = "Planet";
			help[spot + 1][1] = "Planet";
		}
		if (activity == 5) {
			help[spot][0] = "Dinos-";
			help[spot + 1][0] = "Dinos";
		}
		if (activity == 14) {
			help[spot][3] = "AMNH-";
			help[spot + 1][3] = "AMNH-";
		}
	}

	public String returnHelp() {
		String result = "";
		for (String[] hour : help) {
			for (String assignment : hour) {
				if (assignment == null) {
					result += "------";
				} else {
					result += assignment;
				}
			}
			result += "\n";
		}
		return result;
	}

	public void assignOptional(double[] possibleTimes, int MaxPerSlot, int activity, int[] pre_restrictions, int maxDay,
			int duration) {
		int volunteerCounter;
		int randomStart;
		int currentVolunteer;
		int volPerSlot;
		int ActivityTime = time(possibleTimes[0]);
		for (int i = 0; i < possibleTimes.length; i++) {
			ActivityTime = time(possibleTimes[i]);
			volunteerCounter = 0;
			randomStart = (int) (Math.random() * numVolunteers);
			volPerSlot = 0;
			while (volunteerCounter < numVolunteers && volPerSlot < MaxPerSlot) {
				currentVolunteer = (randomStart + volunteerCounter) % numVolunteers;
				if (((isClear(ActivityTime, currentVolunteer) && duration == 1)
						|| (isClear(ActivityTime, currentVolunteer) && isClear(ActivityTime + 1, currentVolunteer)
								&& duration == 2))
						&& findAmount(currentVolunteer, activity) < maxDay) {
					boolean foundVol = true;
					// Go through restrictions, and if they meet any of them, then realize you
					// haven't found what you're looking for
					for (int x = 0; x < pre_restrictions.length; x++) {
						if (haveBefore(currentVolunteer, pre_restrictions[x], ActivityTime)) {
							foundVol = false;
						}
					}
					if (foundVol == true) {
						dpop[ActivityTime][currentVolunteer] = activity;
						if (duration == 2) {
							dpop[ActivityTime + 1][currentVolunteer] = activity;
						}
						volPerSlot++;
					}
				}
				volunteerCounter++;
			}
		}
	}

	public void meeting() {
		for (int i = 0; i < dpop[0].length; i++) {
			if (dpop[0][i] == 1) {
				dpop[0][i] = 8;
			}
		}
		for (int i = 0; i < dpop[0].length; i++) {
			if (dpop[1][i] == 1) {
				dpop[1][i] = 8;
			}
		}
	}

	// Assumes volunteer has activity
	public int getLatest(int vol, int activity) {
		int latest = 0;
		for (int i = 0; i < dpop.length; i++) {
			if (dpop[i][vol] == activity)
				latest = i;
		}
		return latest;
	}

	public int findAmount(int volunteer, int activity) {
		int cnt = 0;
		for (int i = 0; i < dpop.length; i++)
			if (dpop[i][volunteer] == activity)
				cnt++;

		return cnt;
	}

	// Return true if a volunteer has an activity right before hour (in time format)
	public boolean haveBefore(int volunteer, int activity, int hour) {
		return dpop[hour - 1][volunteer] == activity;
	}

	public int time(double time) {
		return (int) ((time - 9) * 2);
	}

	public boolean isClear(int hour, int volunteer) {
		return dpop[hour][volunteer] == 1;
	}

	public int[][] output() {
		return dpop;
	}

	public String fairCheck() {
		int max = 0;
		String maxVol = "";
		int min = 9001;
		String minVol = "";
		int currentAmount;
		for (int i = 0; i < numVolunteers; i++) {
			// if (volNames[i].equals("Ben"))
			// continue;
			currentAmount = findAmount(i, 5) + findAmount(i, 14);
			if (currentAmount > max) {
				max = currentAmount;
				maxVol = volNames[i];
			}
			if (currentAmount < min) {
				min = currentAmount;
				minVol = volNames[i];
			}
		}
		if (max - min > 2) {
			return ("Oops... an injustice has been detected!\n" + maxVol + " has " + (max / 2)
					+ " hours of doors\nWhile " + minVol + " has " + (min / 2)
					+ " hours of doors\nYou must investigate manually (autofix in development)\nPress \"OK\" to acknowledge the problem and generate the DPOP");
		} else {
			return "";
		}
	}

	public String emptyCheck() {
		int amount;
		String statement = "";
		for (int i = 0; i < numVolunteers; i++) {
			amount = findAmount(i, 1);
			if (amount > 0) {
				statement += volNames[i] + " has nothing to do in " + amount + " slot(s)\n";
			}
		}
		if (!statement.equals("")) {
			statement += "\nYou will have to fix this manually";
		}
		return statement;
	}
}
