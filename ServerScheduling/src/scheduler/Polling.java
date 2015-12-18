package scheduler;

import java.util.ArrayList;


public class Polling extends PeriodicServer
{
	public Polling(int serverTime, int per)
	{
		super(serverTime, per);
	}
	
	protected void doServerTask(RMSScheduler sched, ArrayList<TaskInstance> activeAPList, TaskInstance serverT, int currentTime)
	{
		//That's all polling does here
		serverT.setCompTimeRemaining(0);
	}
}
