package scheduler;

import java.util.ArrayList;

public class RMSScheduler
{
	private PeriodicServer server;
	private ArrayList<TaskInstance> instances;
	private ArrayList<TaskInstance> activePeriodicInstances;
	private ArrayList<TaskInstance> activeAperiodicInstances;
	private TaskInstance executing;
	private int totalTime;
	
	int currentTime;
	
	
	
	public RMSScheduler(PeriodicServer server, ArrayList<TaskInstance> instances, int totalTime)
	{
		this.server = server;
		this.instances = instances;
		activePeriodicInstances = new ArrayList<TaskInstance>();
		executing = null;
		currentTime = 0;
		this.totalTime = totalTime;
	}
	
	
	public void run()
	{
		while(currentTime < totalTime)
		{
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