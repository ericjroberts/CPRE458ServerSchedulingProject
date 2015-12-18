package scheduler;

import java.util.ArrayList;

public class Deferrable extends PeriodicServer
{
	
	public Deferrable(int serverTime, int per)
	{
		super(serverTime, per);	
	}
	
	protected void doServerTask(RMSScheduler sched, ArrayList<TaskInstance> activeAPList, TaskInstance serverT, int currentTime)
	{
		//Kill this instance and make a new one
		serverT.setCompTimeRemaining(0);
		serverT.setwasDeferred(true);
		sched.addPeriodicTask(new TaskInstance(serverT.getLabel(), serverT.getPeriod(), serverT.getCompTimeRemaining(), currentTime, true, serverT.getTaskInstance()));
	}
	
}
