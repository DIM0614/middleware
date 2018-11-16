import java.util.NavigableSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class QoSObserver implements Observer {
	
	private static final int maxSizeSet = 1000;
	private ConcurrentMap<Invocation, LogInvocation> map;
	private NavigableSet<LogInvocation> set;
	
	public QoSObserver(){
		this.map = new ConcurrentHashMap<Invocation, LogInvocation>();
		this.set = new ConcurrentSkipListSet<LogInvocation>();
	}
	
	@Override
	public synchronized void started(Invocation invocation, BasicRemotingPatterns pattern) {
		long size = ObjectSizeFetcher.getObjectSize(invocation);
		LogInvocation log = new LogInvocation(invocation, pattern, size);
		map.put(invocation, log);	
	}

	@Override
	public synchronized void done(Invocation invocation){
		LogInvocation log = map.get(invocation);
		if(log.isAlive()){
			log.done();
			set.add(log);
			map.remove(invocation);
		}
	
		if(set.size() == maxSizeSet){
			writeLogs();
			set.clear();
		}
	}

	private void writeLogs(){
		WritterLog wl = new WritterLog(set);
        wl.start();
	}

}