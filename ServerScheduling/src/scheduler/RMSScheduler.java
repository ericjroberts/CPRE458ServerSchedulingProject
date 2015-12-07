package scheduler;

import java.util.ArrayList;

public class RMSScheduler
{
	private PeriodicServer server;
	private ArrayList<TaskInstance> instances;
	private ArrayList<TaskInstance> activeInstances;
	private TaskInstance executing;
	
	
	public RMSScheduler(PeriodicServer server, ArrayList<TaskInstance> instances)
	{
		this.server = server;
		this.instances = instances;
		activeInstances = new ArrayList<TaskInstance>();
		executing = null;
	}
	
}
