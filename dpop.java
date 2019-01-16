import org.apache.poi.ss.usermodel.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class dpop {
	int dpop[][];
	int help[][];
	int[] lunchTimes={11,12,13};
	int[] movieTimes= {12,13,14,15};
	int[] planetTimes= {11,12,13,14,15,16};
	int[] dinoTimes= {10,11,12,13,14,15,16};
	double[] greetTimes= {10,10.5};
	double[] atriumTimes= {10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5};
	double[] STARTimes= {10,11,12,13,14,15,16};
	double[] roveTimes= {15,15.5,16,16.5};
	int numVolunteers;
	public dpop(int[][] x, int closeTime) {
		dpop=x;
		numVolunteers=dpop[0].length;
		help=new int[(closeTime-9)*2][2];
	}
	public void lunch(){
		//Add a lunch for everyone on the DPOP
		for(int i=0;i<dpop[0].length;i++) {
			dpop[time(lunchTimes[i%lunchTimes.length])][i]=2;
			dpop[time(lunchTimes[i%lunchTimes.length]+.5)][i]=2;
		}
	}
	public void movie() {
		//Assign all the movie times to a volunteer who doesn't have lunch or doors before.
		//3 represents what the movie task is
		int[] restrictions= {5,2};
		int[] post_restrictions= {};
		assignNecessary(movieTimes,3,restrictions,post_restrictions);
	}
	public void movieSecond() {
		int[] prerestrictions= {5,2};
		int maxPerSlot=1;
		int maxPerVol=8;
		int duration=2;
		double[] movieDoubleTimes=new double[movieTimes.length];
		for(int i=0; i<movieTimes.length; i++) {
		    movieDoubleTimes[i] = movieTimes[i];
		}
		assignOptional(movieDoubleTimes,maxPerSlot,3,prerestrictions,maxPerVol,duration);
	}
 	public void planet() {
		int[] restrictions= {};
		int[] post_restrictions= {};
		assignNecessary(planetTimes,4,restrictions,post_restrictions);
	}
 	public void planetSecond() {
 		int[] prerestrictions= {};
 		int maxPerSlot=1;
 		int maxPerVol=8;
 		int duration=2;
 		double[] planetDoubleTimes=new double[planetTimes.length];
 		for(int i=0;i<planetTimes.length;i++) {
 			planetDoubleTimes[i]=planetTimes[i];
 		}
 		int assignmentNumber=4;
 		assignOptional(planetDoubleTimes,maxPerSlot,assignmentNumber,prerestrictions,maxPerVol,duration);
 	}
	public void dinos() {
		int[] pre_restrictions= {5};
		int[] post_restrictions= {3};
		assignNecessary(dinoTimes,5,pre_restrictions,post_restrictions);
	}
	public void greet() {
		int[] prerestrictions= {9};
		int maxPerSlot=3;
		int maxPerVol=2;
		int duration=1;//one slot, so 30 minutes
		assignOptional(greetTimes,maxPerSlot, 9,prerestrictions,maxPerVol, duration);
	}
	public void atrium() {
		int[] preRestrictions= {};
		int maxPerSlot=3;
		int maxPerVol=4;
		int duration=1;//one slot so 30 minutes
		assignOptional(atriumTimes,maxPerSlot,6,preRestrictions,maxPerVol,duration);
	}
	public void STARS() {
		int[] preRestrictions= {10};
		int maxPerSlot=2;
		int maxPerVol=4;
		int duration=2;//two slots so 60 minutes
		assignOptional(STARTimes,maxPerSlot,10,preRestrictions,maxPerVol,duration);
	}
	public void ROVE() {
		int[] preRestrictions= {};
		int maxPerSlot=3;
		int maxPerVol=3;
		int duration=1;
		int assignmentNumber=11;
		assignOptional(roveTimes,maxPerSlot,assignmentNumber,preRestrictions,maxPerVol,duration);
	}
	//Assign activity to the array of times. Volunteer must not have any activity from restrictions right before the activity
	//nor any activity from post_restrictions
	public void assignNecessary(int[] times, int activity, int[] restrictions, int[] post_restrictions) {
		for(int i=0;i<times.length;i++) {
			int volunteerCounter=-1;
			int randomStart=(int)(Math.random()*numVolunteers);
			int currentVolunteer=(randomStart+volunteerCounter)%numVolunteers;
			List<Integer> possibleVolunteers = new ArrayList<Integer>();
			int ActivityTime=time(times[i]);
			boolean foundVol=false;
			//Go through all volunteers until you find a good one
			while(volunteerCounter<numVolunteers-1) {
				//If they have this time clear
				volunteerCounter++;
				currentVolunteer=(randomStart+volunteerCounter)%numVolunteers;
				if(isClear(ActivityTime,currentVolunteer)) {
					//Assume you've found a volunteer, until you prove false
					foundVol=true;
					//Go through restrictions, and if they meet any of them, then realize you haven't found what you're looking for
					for(int x=0;x<restrictions.length;x++) {
						if(haveBefore(currentVolunteer,restrictions[x],ActivityTime-1)) {
							foundVol=false;
						}
					}
					for(int x=0;x<post_restrictions.length;x++) {
						if(ActivityTime<dpop.length-2&&dpop[ActivityTime+2][currentVolunteer]==post_restrictions[x]) {
							foundVol=false;
						}
					}
					if(foundVol==true) {
						possibleVolunteers.add(currentVolunteer);
					}
				}
			}
			if(possibleVolunteers.size()>0) {
				int min_amount=99999;
				//Find best volunteer
				for(int a=0;a<possibleVolunteers.size();a++) {
					if(findAmount(possibleVolunteers.get(a),activity)<min_amount) {
						min_amount=findAmount(possibleVolunteers.get(a),activity);
						currentVolunteer=possibleVolunteers.get(a);
					}
				}
				dpop[time(times[i])][currentVolunteer]=activity;
				dpop[time(times[i]+.5)][currentVolunteer]=activity;
			}else {
				System.out.println("HELP: " + times[i] + " for " + activity);
				//addHelp(time(times[i]),activity);
				//addhelp(time(times[i]+.5),activity);
			}
			
			
			
		}
	}
	public void assignOptional(double[] possibleTimes, int MaxPerSlot, int activity, int[] pre_restrictions, int maxDay,int duration) {
		int volunteerCounter;
		int randomStart;
		int currentVolunteer;
		int volPerSlot;
		int ActivityTime=time(possibleTimes[0]);
		for(int i=0;i<possibleTimes.length;i++) {
			ActivityTime=time(possibleTimes[i]);
			volunteerCounter=0;
			randomStart=(int)(Math.random()*numVolunteers);
			volPerSlot=0;
			while(volunteerCounter<numVolunteers&&volPerSlot<MaxPerSlot) {
				currentVolunteer=(randomStart+volunteerCounter)%numVolunteers;
				if(
						(
							(isClear(ActivityTime,currentVolunteer)&&duration==1)
							||
							(isClear(ActivityTime,currentVolunteer)&&isClear(ActivityTime+1,currentVolunteer)&&duration==2)
						)
						&&
						findAmount(currentVolunteer,activity)<maxDay
				) {
					boolean foundVol=true;
					//Go through restrictions, and if they meet any of them, then realize you haven't found what you're looking for
					for(int x=0;x<pre_restrictions.length;x++) {
						if(haveBefore(currentVolunteer,pre_restrictions[x],ActivityTime)) {
							foundVol=false;
						}
					}
					if(foundVol==true) {
						dpop[ActivityTime][currentVolunteer]=activity;
						if(duration==2) {
							dpop[ActivityTime+1][currentVolunteer]=activity;
						}
						volPerSlot++;
					}
				}
				volunteerCounter++;
			}
		}
	}
	public void meeting() {
		for(int i=0;i<dpop[0].length;i++) {
			dpop[0][i]=8;
		}
		for(int i=0;i<dpop[0].length;i++) {
			dpop[1][i]=8;
		}
	}
	//Return true if a volunteer has an activity right before hour (in time format)
	public int findAmount(int volunteer, int activity) {
		int cnt=0;
		for(int i=0;i<dpop.length;i++)
			if(dpop[i][volunteer]==activity)
				cnt++;

		return cnt;
	}
	public boolean haveBefore(int volunteer, int activity, int hour) {
		return dpop[hour-1][volunteer]==activity;
	}
	public int time(double time) {
		return (int)((time-9)*2);
	}
	public boolean isClear(int hour, int volunteer) {
		return dpop[hour][volunteer]==0;
	}
	@Override
	public String toString() {
		String result="";
		for(int[] hour: dpop) {
			for(int volunteerAssignment : hour) {
				//result+=""+volunteerAssignment;
				result+=getName(volunteerAssignment);
			}
			result+="\n";
		}
		return result;
	}
	public void export(){
		String[][] export=new String[dpop.length][dpop[0].length];
		for(int i=0;i<dpop.length;i++) {
			for(int a=0;a<dpop[0].length;a++) {
				export[i][a]=getName(dpop[i][a]);
			}
		}
		Workbook wb;
		wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("DPOP");
		Row row;
		Cell cell;
		int rownum = 1;
		for (int i = 0; i < export.length; i++, rownum++) {
			row = sheet.createRow(rownum);
			for (int j = 0; j < export[i].length; j++) {
				cell = row.createCell(j);
				cell.setCellValue(export[i][j]);
			}
		}
		File file = new File("dpop.xls");
		if (!file.exists()) {
		     try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("ERROR trying to create file");
			}
		  }
		try (FileOutputStream out = new FileOutputStream(file)) {
            System.out.println("OUTPUTING FILE");
			wb.write(out);
        } catch (IOException e) {
        	System.out.println("ERROR Trying to write");
        }
		try {
			wb.close();
		} catch (IOException e){
			System.out.println("ERROR TRING TO CLOSE");
		}
       
	}
	public String getName(int volunteerAssignment) {
		String result="";
		if(volunteerAssignment==0) {
			result+="------";
		}
		if(volunteerAssignment==1) {
			result+="------";
		}
		if(volunteerAssignment==2) {
			result+="Lunch-";
		}
		if(volunteerAssignment==3) {
			result+="Movie-";
		}
		if(volunteerAssignment==4) {
			result+="Planet";
		}
		if(volunteerAssignment==5) {
			result+="Dinos-";
		}
		if(volunteerAssignment==6) {
			result+="Atrium";
		}
		if(volunteerAssignment==7) {
			result+="STARS-";
		}
		if(volunteerAssignment==8) {
			result+="MEETIN";
		}
		if(volunteerAssignment==9) {
			result+="GREET-";
		}
		if(volunteerAssignment==10) {
			result+="STARS-";
		}
		if(volunteerAssignment==11) {
			result+="ROVE--";
		}
		return result;
	}
}
