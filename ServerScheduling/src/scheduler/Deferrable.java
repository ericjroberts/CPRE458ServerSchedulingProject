package scheduler;

import java.util.ArrayList;

public class Deferrable extends PeriodicServer
{
	
	public Deferrable(int serverTime, int per)
	{
		super(serverTime, per);	
	}
	
	public void doServerTask(RMSScheduler sched, ArrayList<TaskInstance> activeAPList, TaskInstance serverT, int currentTime)
	{
		//Make a new server task
		sched.addPeriodicTask(new TaskInstance(serverT.getLabel() + " Deferred", serverT.getPeriod(), serverT.getCompTimeRemaining(), currentTime, true, serverT.getTaskInstance(), serverT.getDeadline()));
	}
	
}
