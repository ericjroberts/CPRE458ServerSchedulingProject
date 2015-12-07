package scheduler;

public class Task 
{
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
	public Task(int per, int compTime, boolean server)
	{
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
			return new TaskInstance(period, exeTime, arrivalTime, isServerTask, numInstances);
		}
		else
		{
			return new TaskInstance(period, exeTime, newArrival, isServerTask, numInstances);
		}	
		

	}
}
