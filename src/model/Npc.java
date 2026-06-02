package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import observer.Observer;
import observer.Subject;

public abstract class Npc implements Subject, Runnable {

	private final Lock pauseLock = new ReentrantLock();
	private final Condition unpaused = pauseLock.newCondition();
	private volatile boolean isPaused = false;
	private final List<Observer> observers = new ArrayList<>();

	public final void pause() {
		isPaused = true;
	}

	public final void resume() {
		pauseLock.lock();
		try {
			isPaused = false;
			unpaused.signalAll();
		} finally {
			pauseLock.unlock();
		}
	}

	@Override
	public final void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public final void notifyObserver(Object event) {
		for (Observer observer : observers) {
			observer.update(this, event);
		}
	}

	@Override
	public final void run() {
		while(true) {
			pauseLock.lock();
			try {
				while(isPaused) {
					unpaused.await();
				}
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			} finally {
				pauseLock.unlock();
			}
			handleCurrentState();
		}
	}

	protected abstract void handleCurrentState();
}