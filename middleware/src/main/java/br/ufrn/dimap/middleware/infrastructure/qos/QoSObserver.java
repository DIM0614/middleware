package br.ufrn.dimap.middleware.infrastructure.qos;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import br.ufrn.dimap.middleware.remotting.impl.Invocation;

public class QoSObserver implements Observer {
	
	private static final int maxSizeSet = 5;
	private final ConcurrentMap<Invocation, LogInvocation> map;
	private final Set<LogInvocation> set;
	private final BasicRemotingPatterns pattern;
	
	public QoSObserver(BasicRemotingPatterns pattern){
		this.map = new ConcurrentHashMap<Invocation, LogInvocation>();
		this.set = ConcurrentHashMap.<LogInvocation>newKeySet();
		this.pattern = pattern;
	}
	
	@Override
	public synchronized void started(Invocation invocation, long sizeInvocation) {
		long size = sizeInvocation;
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
