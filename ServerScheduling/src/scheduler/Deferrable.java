package scheduler;

import java.util.ArrayList;

public class Deferrable extends PeriodicServer
{
	public Deferrable(int serverTime)
	{
		super(serverTime);	
	}

	@Override
	public ArrayList<TaskInstance> doServer(ArrayList<TaskInstance> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<TaskInstance> update(ArrayList<TaskInstance> list) {
		// TODO Auto-generated method stub
		return null;
	}
}
