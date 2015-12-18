package scheduler;

import java.util.ArrayList;
import java.util.Collections;

public abstract class PeriodicServer 
{
	
	int totalCompTime;
	int period;
	protected TaskInstance apExecuting;
	
	protected PeriodicServer(int serverTime, int per)
	{
		totalCompTime = serverTime;
		period = per;
		apExecuting = null;
	}
	
	public boolean isExecuting()
	{
		if(apExecuting == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	//Given the list of arrived aperiodic tasks
	//Should return the list of tasks to be run for the server time
	public void doServer(RMSScheduler sched, ArrayList<TaskInstance> activeAPList, TaskInstance serverT, int currentTime)
	{
		//Check if the current task is done
		if(apExecuting != null && apExecuting.getCompTimeRemaining() <= 0)
		{
			sched.addCompleted(apExecuting);
			apExecuting = null;
		}
		
		//Check for missed deadlines
		if(apExecuting != null && apExecuting.getDeadline() <= currentTime)
		{
			sched.addMissed(apExecuting);
			apExecuting = null;
		}
		
		if(activeAPList.size() > 0)
		{
			for(int i=0;i<activeAPList.size();i++)
			{
				//Missed the deadline
				if(activeAPList.get(i).getDeadline() <= currentTime)
				{
					sched.addMissed(activeAPList.remove(i));
					i--;//ArrayList shifts everything
				}
			}
		}
		
		//If there are still tasks
		if(activeAPList.size() > 0)
		{
			Collections.sort(activeAPList, TaskInstance.getArrivalTimeComparator());
			//If nothing is running
			//Should be highest priority since activePeriodicInstances is sorted 
			if(apExecuting == null)
			{
				apExecuting = activeAPList.remove(0);	
				
			}	
			//Preemption
			else if(activeAPList.get(0).getArrivalTime() < apExecuting.getArrivalTime())
			{
				TaskInstance temp = activeAPList.remove(0);
				sched.addPreemption(apExecuting, temp, currentTime);
				activeAPList.add(apExecuting);
				apExecuting = temp;		
			}
			//Continue executing task otherwise
		}
		//Do something if there are no tasks. Determined by subclasses
		else if(activeAPList.size() == 0 && apExecuting == null)
		{
			//Current server task is going to get removed, we just need its data
			TaskInstance temp = serverT;
			sched.reScheduleServer();
			doServerTask(sched, activeAPList, temp, currentTime);
		}
		
		//Update at the end
		update(activeAPList, currentTime);
	}
	
	private void update(ArrayList<TaskInstance> activeAPList, int currentTime)
	{
		if(apExecuting != null)
		{
			//decrease currently running's remaining execution time by 1
			apExecuting.setCompTimeRemaining(apExecuting.getCompTimeRemaining() - 1);
			
			if(apExecuting.getStartTime() == 0)
			{
				apExecuting.setStartTime(currentTime);
			}
		}
	}
	
	public abstract void doServerTask(RMSScheduler sched, ArrayList<TaskInstance> activeAPList, TaskInstance serverT, int currentTime);
}
