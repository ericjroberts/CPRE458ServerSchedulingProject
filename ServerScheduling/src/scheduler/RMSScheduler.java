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
		//Finally increment the time
		currentTime++;
	}
	
	
	
}