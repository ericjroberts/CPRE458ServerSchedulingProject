package scheduler;

import java.util.ArrayList;


public class Polling extends PeriodicServer
{
	public Polling(int serverTime, int per)
	{
		super(serverTime, per);
	}
	
	public void doServerTask(RMSScheduler sched, ArrayList<TaskInstance> activeAPList, TaskInstance serverT, int currentTime)
	{
		//Does nothing here
	}
}
