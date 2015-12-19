package scheduler;

import java.util.Comparator;

public class TaskInstance extends Task
{
	
	int arrivalTime;
	int startTime;
	int endTime;
	int deadline;
	int compTimeRemaining;
	int taskInstance;
	int effectivePriority;
	boolean active;
	boolean missedDeadline;
	boolean wasDeferred;
	
	public TaskInstance(String label, int per, int compTime, int arrival, boolean server, int instance, int deadlineTime)
	{
		super(label, per, compTime, server);
		arrivalTime = arrival;
		taskInstance = instance;
		startTime = 0;
		endTime = 0;
		deadline = deadlineTime;
		compTimeRemaining = compTime;
		effectivePriority = per;
		missedDeadline = false;
		wasDeferred = false;
	}

	public int getEffectivePriority()
	{
		return effectivePriority;
	}
	
	public void setEffectivePriority(int period)
	{
		effectivePriority = period;
	}
	
	
	public boolean didMissDeadiline()
	{
		return missedDeadline;
	}
	
	public void setMissDeadiline(boolean missed)
	{
		missedDeadline = missed;
	}
	
	public boolean wasDeferred()
	{
		return wasDeferred;
	}
	
	public void setwasDeferred(boolean deferred)
	{
		wasDeferred = deferred;
	}
	
	public int getArrivalTime()
	{
		return arrivalTime;
	}
	
	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
	public int getDeadline()
	{
		return deadline;
	}
	
	public void setDeadline(int deadline)
	{
		this.deadline = deadline;
	}

	public int getCompTimeRemaining() {
		return compTimeRemaining;
	}

	public void setCompTimeRemaining(int compTimeRemaining) {
		this.compTimeRemaining = compTimeRemaining;
	}

	public int getTaskInstance() {
		return taskInstance;
	}
	
	/**
	 * A comparator to be used in sorting active tasks by period,
	 * as RMS prioritizes task instances by smallest period.
	 *
	 */
	public static class TaskInstancePeriodComparator implements Comparator<TaskInstance>
	{
		
		public TaskInstancePeriodComparator()
		{
			
		}
		
		
		@Override
		public int compare(TaskInstance instance1, TaskInstance instance2)
		{
			int comparison = 0;
			int firstPeriod = instance1.getEffectivePriority();
			int secondPeriod = instance2.getEffectivePriority();
			
			if(firstPeriod > secondPeriod)
			{
				comparison = 1;
			}
			if(firstPeriod == secondPeriod)
			{
				comparison = 0;
			}
			if(firstPeriod < secondPeriod)
			{
				comparison = -1;
			}
			
			return comparison;
		}
		
	}
	
	public static class TaskInstanceArrivalTimeComparator implements Comparator<TaskInstance>
	{
		
		public TaskInstanceArrivalTimeComparator()
		{
			
		}
		
		
		@Override
		public int compare(TaskInstance instance1, TaskInstance instance2)
		{
			int comparison = 0;
			int firstTime = instance1.getArrivalTime();
			int secondTime = instance2.getArrivalTime();
			
			if(firstTime > secondTime)
			{
				comparison = 1;
			}
			if(firstTime == secondTime)
			{
				comparison = 0;
			}
			if(firstTime < secondTime)
			{
				comparison = -1;
			}
			
			return comparison;
		}
		
	}
	
	public static TaskInstancePeriodComparator getPeriodComparator()
	{
		return new TaskInstancePeriodComparator();
	}
	
	public static TaskInstanceArrivalTimeComparator getArrivalTimeComparator()
	{
		return new TaskInstanceArrivalTimeComparator();
	}
}
