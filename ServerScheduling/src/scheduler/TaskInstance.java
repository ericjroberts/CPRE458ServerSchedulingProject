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
	boolean active;
	
	public TaskInstance(int per, int compTime, int arrival, boolean server, int instance)
	{
		super(per, compTime, server);
		arrivalTime = arrival;
		taskInstance = instance;
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
	
	
}
