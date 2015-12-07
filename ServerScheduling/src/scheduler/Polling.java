package scheduler;

import java.util.ArrayList;

public class Polling extends PeriodicServer
{
	public Polling(int serverTime)
	{
		super(serverTime);
	}

	@Override
	public ArrayList<TaskInstance> doServer(ArrayList<TaskInstance> list) {
		// TODO Auto-generated method stub
		if(list.size() == 0)
		{
			return null;
		}
		
		
		return list;
	}

	@Override
	public ArrayList<TaskInstance> update(ArrayList<TaskInstance> list) {
		return null;
	}
	
	
}
