package scheduler;

import java.util.ArrayList;

public class PriorityExchange extends PeriodicServer
{
	TaskInstance switchedTask;
	TaskInstance deferredInstance;
	int switchTimeLeft;
	int tempPeriod;
	
	public PriorityExchange(int serverTime, int per)
	{
		super(serverTime, per);
		switchedTask = null;
		tempPeriod = per;
		deferredInstance = null;
		switchTimeLeft = 0;
	}

	@Override
	public void doServerTask(RMSScheduler sched, ArrayList<TaskInstance> activeAPList, TaskInstance serverT, int currentTime)
	{
		//wasn't deferred yet exchange priority
		if(!serverT.getLabel().contains("Deferred"))
		{
			int newPriority;
			newPriority = sched.exchangePriority(serverT);
			sched.addPeriodicTask(new TaskInstance(serverT.getLabel() + " Deferred - Priority exchange", newPriority, serverT.getCompTimeRemaining(), currentTime, true, serverT.getTaskInstance(), serverT.getDeadline()));
		}
		//Just make a new task new priority exchange since this was already deferred
		else
		{
			sched.addPeriodicTask(new TaskInstance(serverT.getLabel() + " Deferred - Priority exchange", serverT.getPeriod(), serverT.getCompTimeRemaining(), currentTime, true, serverT.getTaskInstance(), serverT.getDeadline()));
		}
	}
	
}
