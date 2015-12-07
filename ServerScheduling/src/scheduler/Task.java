package scheduler;

public class Task 
{
	String label;
	int period;
	int exeTime;
	boolean isServerTask;
	int numInstances = 0;

	/**
	 * 
	 * @param per
	 * The period of a task. Set to 0 if aperiodic
	 * @param compTime
	 * Time to complete task
	 * @param arrival
	 * Arrival time. Set if aperiodic, 0 otherwise
	 * @param server
	 */
	public Task(String label, int per, int compTime, boolean server)
	{
		this.label = label;
		period = per;
		exeTime = compTime;
		isServerTask = server;
	}
	
	public boolean isAperiodic()
	{
		//Should have no period
		return (period == 0);
	}
	
	public boolean isServerTask()
	{
		return isServerTask;
	}
	
	public int getPeriod()
	{
		return period;
	}
	
	public int getCompTime()
	{
		return exeTime;
	}
	
	public String toString()
	{
		return "Period: " + period + " Execution time: " + exeTime + " Arrival time: " + " isServerTask: " + isServerTask;
	}
	
	public TaskInstance generateInstance()
	{
		int newArrival = numInstances * period; 
		numInstances++;
		if(isAperiodic())
		{
			return new TaskInstance(label, period, exeTime, newArrival, isServerTask, numInstances);
		}
		else
		{
			return new TaskInstance(label, period, exeTime, newArrival, isServerTask, numInstances);
		}	
		

	}
}
