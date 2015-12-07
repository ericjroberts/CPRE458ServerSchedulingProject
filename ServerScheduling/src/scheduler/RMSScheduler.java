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
			
			
			Collections.sort(activePeriodicInstances, TaskInstance.getPeriodComparator());
			
			update();
		}
	}
	
	/**
	 * Updates the scheduler.
	 */
	private void update()
	{
		if(activePeriodicInstances.get(0).getPeriod() < executing.getPeriod())
		{
			TaskInstance temp = activePeriodicInstances.remove(0);
			preemptionList.add(new Preemption(temp, executing, currentTime));
			activePeriodicInstances.add(executing);
			executing = temp;
			
			
			
		}
		//decrease currently running's remaining execution time by 1
		executing.setCompTimeRemaining(executing.getCompTimeRemaining() - 1);
		
		//Finally increment the time
		currentTime++;
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