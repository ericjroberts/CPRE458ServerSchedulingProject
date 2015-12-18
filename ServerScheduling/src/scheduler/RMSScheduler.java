package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;

public class RMSScheduler
{
	private PeriodicServer server;
	private Queue<TaskInstance> instances;
	private ArrayList<TaskInstance> activePeriodicInstances;
	private ArrayList<TaskInstance> activeAperiodicInstances;
	private ArrayList<TaskInstance> completedInstances;
	private ArrayList<TaskInstance> missedInstances;
	private ArrayList<Preemption> preemptionList;
	private TaskInstance executing;
	private int totalTime;
	
	int currentTime;
	
	
	
	public RMSScheduler(PeriodicServer server, Queue<TaskInstance> instances, int totalTime)
	{
		this.server = server;
		this.instances = instances;
		activePeriodicInstances = new ArrayList<TaskInstance>();
		activeAperiodicInstances = new ArrayList<TaskInstance>();
		completedInstances = new ArrayList<TaskInstance>();
		missedInstances = new ArrayList<TaskInstance>();
		preemptionList = new ArrayList<Preemption>();
		executing = null;
		currentTime = 0;
		this.totalTime = totalTime;
	}
	
	
	public void run()
	{
		while(currentTime < totalTime)
		{
			while(instances.peek().getArrivalTime() == currentTime)
			{
				TaskInstance newlyActive = instances.poll();
				
				if(newlyActive.isAperiodic())
				{
					activeAperiodicInstances.add(newlyActive);
				}
			
				else if(!newlyActive.isAperiodic() || newlyActive.isServerTask())
				{
					activePeriodicInstances.add(newlyActive);
				}
				
			}
			
			//Check if the current task is done
			if(executing != null && executing.getCompTimeRemaining() <= 0)
			{
				executing.setEndTime(currentTime);
				addCompleted(executing);
				executing = null;
			}
			
			//Check for missed deadlines
			if(executing != null && executing.getDeadline() < currentTime)
			{
				addMissed(executing);
				executing = null;
			}
			
			if(activePeriodicInstances.size() > 0)
			{
				for(int i=0;i<activePeriodicInstances.size();i++)
				{
					//Missed the deadline
					if(activePeriodicInstances.get(i).getDeadline() < currentTime)
					{
						addMissed(activePeriodicInstances.remove(i));
						i--;//ArrayList shifts everything
					}
				}
			}		
			
			//If there are still tasks
			if(activePeriodicInstances.size() > 0)
			{
				Collections.sort(activePeriodicInstances, TaskInstance.getPeriodComparator());
				//If nothing is running
				//Should be highest priority since activePeriodicInstances is sorted 
				if(executing == null)
				{
					executing = activePeriodicInstances.remove(0);	
					
				}	
				//Preemption
				else if(activePeriodicInstances.get(0).getPeriod() < executing.getPeriod())
				{
					TaskInstance temp = activePeriodicInstances.remove(0);
					addPreemption(executing, temp, currentTime);
					activePeriodicInstances.add(executing);
					executing = temp;		
				}
				//Continue executing task otherwise
			}
			
			if(executing.isServerTask)
			{
				//Maintain AP task list
				//Here we should do aperiodic stuff
				//Have to do some of the same stuff from here
				//Decrement aperiodic task in server. Switch priority or update server comp time remaining
				server.doServer(this, activeAperiodicInstances, executing, currentTime);
			}
			
			update();
		}
	}
	
	/**
	 * Updates the scheduler.
	 */
	private void update()
	{
		if(executing != null)
		{
			//decrease currently running's remaining execution time by 1
			executing.setCompTimeRemaining(executing.getCompTimeRemaining() - 1);
			
			if(executing.getStartTime() == 0)
			{
				executing.setStartTime(currentTime);
			}
		}
				
		//Finally increment the time
		currentTime++;
	}
	
	public void addPreemption(TaskInstance preempted, TaskInstance preempting, int time)
	{
		preemptionList.add(new Preemption(preempted, preempting, time));
	}
	
	public void addCompleted(TaskInstance completed)
	{
		completed.setEndTime(currentTime);
		completedInstances.add(completed);
	}
	
	public void addMissed(TaskInstance missed)
	{
		missed.setMissDeadiline(true);
		missedInstances.add(missed);
	}
	
	public void addPeriodicTask(TaskInstance toAdd)
	{
		activePeriodicInstances.add(toAdd);
	}
	
	private class Preemption
	{
		TaskInstance preempted;
		TaskInstance preempting;
		int time;
		
		public Preemption(TaskInstance preempted, TaskInstance preempting, int time)
		{
			this.preempted = preempted;
			this.preempting = preempting;
			this.time = time;
		}
		
		public TaskInstance getPreempted()
		{
			return preempted;
		}
		
		public TaskInstance getPreempting()
		{
			return preempting;
		}
		
		public int getTime()
		{
			return time;
		}
	}
}