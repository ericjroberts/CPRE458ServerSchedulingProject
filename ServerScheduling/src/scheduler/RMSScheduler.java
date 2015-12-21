package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
	private ArrayList<String> builtSchedule;
	
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
		builtSchedule = new ArrayList<String>();
		executing = null;
		currentTime = 0;
		this.totalTime = totalTime;
	}
	
	
	public void run()
	{
		while(currentTime < totalTime)
		{
			while(instances.peek() != null && instances.peek().getArrivalTime() == currentTime)
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
			if(executing != null && executing.getDeadline() <= currentTime)
			{
				addMissed(executing);
				executing = null;
			}
			
			if(activePeriodicInstances.size() > 0)
			{
				for(int i=0;i<activePeriodicInstances.size();i++)
				{
					//Missed the deadline
					if(activePeriodicInstances.get(i).getDeadline() <= currentTime)
					{
						addMissed(activePeriodicInstances.remove(i));
						i--;//ArrayList shifts everything
					}
				}
			}		
			
			//If there are still tasks
			schedule();	
			
			
			if(executing != null && executing.isServerTask)
			{
				//Maintain AP task list
				//Here we should do aperiodic stuff
				//Have to do some of the same stuff from here
				//Decrement aperiodic task in server. Switch priority or update server comp time remaining
				server.doServer(this, activeAperiodicInstances, executing, currentTime);
			}
			
			//record what task is executing at the current time
			record();
			
			update();
		}
	}
	
	private void schedule()
	{
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
				//Check of this is an actual pre-emption. Server has to decide what to do with its time if nothing to run still
				if(!(temp.isServerTask && activeAperiodicInstances.size() == 0 && !server.isExecuting()))
				{
					addPreemption(executing, temp, currentTime);
				}			
				activePeriodicInstances.add(executing);
				executing = temp;		
			}
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
			if(executing.isServerTask())
			{
				//Check if the current task is done
				if(server.getExecuting() != null && server.getExecuting().getCompTimeRemaining() <= 0)
				{
					addCompletedAtTime(server.getExecuting(), currentTime+1);
					server.setExecuting(null);
				}
			}
		}
				
		//Finally increment the time
		currentTime++;
	}
	
	/**
	 * Do this when the server has nothing to do. Pick something else to run
	 */
	public void reScheduleServer()
	{
		//Kill the server task
		executing.setwasDeferred(true);
		addCompleted(executing);
		executing = null;
		schedule();
	}
	
	public int exchangePriority(TaskInstance server)
	{
		int priority;
		
		if(activePeriodicInstances.size() > 0)
		{
			priority = activePeriodicInstances.get(0).getPeriod();
			activePeriodicInstances.get(0).setEffectivePriority(server.getPeriod());
			
		}
		//No priority exchange happened
		else
		{
			priority = server.getPeriod();
		}
		
		return priority;
	}
	
	public void addPreemption(TaskInstance preempted, TaskInstance preempting, int time)
	{
		preemptionList.add(new Preemption(preempted, preempting, time));
	}
	
	public void addCompleted(TaskInstance completed)
	{
		if(!completed.isServerTask)
		{
			completed.setEndTime(currentTime);
			completedInstances.add(completed);
		}
	}
	
	public void addCompletedAtTime(TaskInstance completed, int time)
	{
		if(!completed.isServerTask)
		{
			completed.setEndTime(time);
			completedInstances.add(completed);
		}
	}
	
	public void addMissed(TaskInstance missed)
	{
		if(!missed.isServerTask)
		{
			missed.setMissDeadiline(true);
			missedInstances.add(missed);
		}
	}
	
	public void addPeriodicTask(TaskInstance toAdd)
	{
		activePeriodicInstances.add(toAdd);
	}
	

	/**
	 * Records the currently executing task
	 */
	private void record()
	{
		String entry = "nothing ";
		TaskInstance current = executing;
		
		if(current != null)
		{
			if(executing.isServerTask())
			{
				current = server.getExecuting();
			}
			entry = current.getLabel() + " " + current.getTaskInstance() + " ";
		}
		
		builtSchedule.add(entry);
	}
	
	/**
	 * Returns a string that has the blocked out schedule
	 * @return
	 */
	public String getBuiltScheduleString()
	{
		String scheduleString = "";
		String current;
		String previous = null;
		
		for(int i = 0; i < builtSchedule.size(); i++)
		{
			current = builtSchedule.get(i);
			
			//if this current string is not the same task instance, only then should it put down a new time and string.
			if(!current.equals(previous))
			{
				scheduleString += "|" + i + "| " + current;
			}
			previous = current;
		}
		
		scheduleString += "|" + builtSchedule.size() + "|";
		
		return scheduleString;
	}
	
	/**
	 * Returns the average response time of a list of TaskInstances
	 * @param list A list of TaskInstances
	 * @return The average response time of the given list of TaskInstances
	 */
	public double getAverageResponseTime(ArrayList<TaskInstance> list)
	{
		double sum = 0;
		double size = list.size();
		TaskInstance current;
		
		for(int i = 0; i < size; i++)
		{
			current = list.get(i);
			sum += (current.getEndTime() - current.getArrivalTime());
		}
		
		return (sum/size);
	}
	
	/**
	 * Returns the average wait time of a list of TaskInstances
	 * @param list A list of TaskInstances
	 * @return The average wait time of the given list of TaskInstances.
	 */
	public double getAverageWaitTime(ArrayList<TaskInstance> list)
	{
		double sum = 0;
		double size = list.size();
		TaskInstance current;
		
		for(int i = 0; i < size; i++)
		{
			current = list.get(i);
			sum += (current.getStartTime() - current.getArrivalTime());
		}
		
		return (sum/size);
	}
	
	/**
	 * Returns a string containing statistics on the task instances.
	 * @return A string containing statistics on the task instances.
	 */
	public String getStats()
	{
		String statsMessage = "";
		int size = completedInstances.size();
		double avgCompletedResponseTime = getAverageResponseTime(completedInstances);
		double avgCompletedWaitTime = getAverageWaitTime(completedInstances);
		double avgCompletedPeriodicResponseTime;
		double avgCompletedPeriodicWaitTime;
		double avgCompletedAperiodicResponseTime;
		double avgCompletedAperiodicWaitTime;
		ArrayList<TaskInstance> completedPeriodicInstances = new ArrayList<TaskInstance>();
		ArrayList<TaskInstance> completedAperiodicInstances = new ArrayList<TaskInstance>();
		TaskInstance current;
		Preemption currentPreemption;
		
		//ignore server tasks.
		for(int i = 0; i < size; i++)
		{
			current = completedInstances.get(i);
			if(!current.isServerTask())
			{
				if(current.getPeriod() != 0)
				{
					completedPeriodicInstances.add(current);
				}
				else
				{
					completedAperiodicInstances.add(current);
				}
			}
		}
		
		avgCompletedResponseTime = getAverageResponseTime(completedInstances);
		avgCompletedWaitTime = getAverageWaitTime(completedInstances);
		avgCompletedPeriodicResponseTime = getAverageResponseTime(completedPeriodicInstances);
		avgCompletedPeriodicWaitTime = getAverageWaitTime(completedPeriodicInstances);
		avgCompletedAperiodicResponseTime = getAverageResponseTime(completedAperiodicInstances);
		avgCompletedAperiodicWaitTime = getAverageWaitTime(completedAperiodicInstances);
		
		
		statsMessage += "\nAll Tasks:\n"
					  + "Average Response Time: " + avgCompletedResponseTime + "\n"
					  + "Average Wait Time: " + avgCompletedWaitTime + "\n"
					  + "\nPeriodic Tasks:\n"
					  + "Average Response Time: " + avgCompletedPeriodicResponseTime + "\n"
					  + "Average Wait Time: " + avgCompletedPeriodicWaitTime + "\n"
					  + "\nAperiodic Tasks:\n"
					  + "Averiage ResponseTime: " + avgCompletedAperiodicResponseTime + "\n"
					  + "Average Wait Time: " + avgCompletedAperiodicWaitTime + "\n"
					  + "\nMissed Deadlines: " + missedInstances.size() + "\n";
		
		
		for(int i = 0; i < missedInstances.size(); i++)
		{
			current = missedInstances.get(i);
			
			statsMessage += "\n" + current.getLabel() + current.getTaskInstance() + ":\nArrivalTime: " + current.arrivalTime + "\nDeadline: " + current.getDeadline();
		}
		
		statsMessage += "\n\nPreemptions: " + preemptionList.size();
		
		for(int i = 0; i < preemptionList.size(); i++)
		{
			currentPreemption = preemptionList.get(i);
			TaskInstance preempted = currentPreemption.getPreempted();
			TaskInstance preempting = currentPreemption.getPreempting();
			
			statsMessage += "\n\nPreemption " + i
						 + "\nTime: " + currentPreemption.getTime()
						 + "\nPreempted: " + preempted.getLabel() + " " + preempted.getTaskInstance()
						 + "\nPreempting: " + preempting.getLabel() + " " + preempting.getTaskInstance();
		}
		
		
		return statsMessage;
	}
	
	/**
	 * This class holds information about a single preemption.
	 *
	 */
	public void printResults()
	{
		System.out.println("Completed Instances");
		for(int i=0;i<completedInstances.size();i++)
		{
			System.out.println(completedInstances.get(i).getLabel() + " Instance: " + completedInstances.get(i).getTaskInstance()
					+ " End Time: " + completedInstances.get(i).getEndTime() + " Arrival: " + completedInstances.get(i).getArrivalTime()
					+ " Start Time: " + completedInstances.get(i).getStartTime());
		}
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