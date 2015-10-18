package com.github.jacksonc.circuitbuilder;

import java.util.ArrayList;
import java.util.List;

//Controls the updating of the gates. Contains methods for adding

public class Manager {
	
	private float timePerFrame = 1f;
	private float timeSinceLastFrame = 0f;
	private boolean tick = false;
	
	private List<Gate> gates = new ArrayList<Gate>();
	
			
	// Updates the game world to reflect change in time (use parameter deltaTime)
	public void update(float deltaTime) {
		timeSinceLastFrame += deltaTime;
		if (timeSinceLastFrame >= timePerFrame) {
			timeSinceLastFrame = 0f;
			tick = !tick;
			if (tick) {
				tick();
			} else {
				tock();
			}
		}
	}

	// Adds a source gate to a destination gate's list of inputs
	public void connectGates(Gate source, Gate destination) {
		destination.getInputs().add(source);
	}
	
	// Overloaded if you want to insert an input at a certain point on the list.
	public void connectGates(Gate source, Gate destination, int index) {
		destination.getInputs().add(index, source);
	}
	
	// Tells all gates to think.
	private void tick() {
		for (Gate g : gates) {
			g.think();
		}	
	}
	
	// Tells all game to talk.
	private void tock() {
		for (Gate g : gates) {
			g.talk();
		}
	}
	
	// Change how long each frame (a tick or tock) lasts.
	public void setTimePerFrame(float timePerFrame) {
		this.timePerFrame = timePerFrame;
	}
}


