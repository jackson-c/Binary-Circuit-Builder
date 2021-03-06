package com.github.jacksonc.circuitbuilder;

import java.util.HashSet;
import java.util.Set;
import com.github.jacksonc.circuitbuilder.LogicGate.GateTypes;
import com.github.jacksonc.circuitbuilder.InputHandler;
import com.github.jacksonc.circuitbuilder.InputHandler.Actions;

// Controls the updating of the gates. Contains methods for connecting gates and changing
// how much time occurs between tick/tock calls.

public class Manager {
	
	private float timePerFrame = 0.2f; //Seconds between each tick/tock (timePerFrame * 2 is time between a single tick)
	private float timeSinceLastFrame = 0f;
	
	//for getting gate data at a grid location
	public static Gate gateAtPoint = null;
	public static int gateAtPointType = 0; //1=gate 2=input 3=output
	public static int gateAtPointInput = 0; //input index
	
	private Set<Gate> gates = new HashSet<Gate>();
			
	// Updates the game world to reflect change in time (use parameter deltaTime)
	public void update(float deltaTime) {
		timeSinceLastFrame += deltaTime;
		int max = 0;
		while (timeSinceLastFrame >= timePerFrame && max<10) {
			max++;
			if(max>=10){
				timeSinceLastFrame = 0;
			} else {
				timeSinceLastFrame = timeSinceLastFrame - timePerFrame;
			}
			tick();
			tock();
		}
	}

	// Adds a source gate to a destination gate's list of inputs
	public void connectGates(Gate source, Gate destination) {
		if (destination.getMaxInputs() == -1 || destination.getInputs().size() + 1 <= destination.getMaxInputs()) {
			destination.getInputs().add(source);
		}
	}
	
	// Overloaded if you want to set an input at a certain point on the list.
	public void connectGates(Gate source, Gate destination, int index) {
		destination.getInputs().set(index, source);
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
	
	// Change how long each frame (which is a tick or tock) lasts.
	public void setTimePerFrame(float timePerFrame) {
		this.timePerFrame = timePerFrame;
	}
	
	// Get how long each frame (which is a tick or tock) lasts.
	public float getTimePerFrame() {
		return timePerFrame;
	}
	
	//Returns reference to list of all gates
	public Set<Gate> getGates() {
		return gates;
	}
	
	public Gate addGate(Gate gate) {
		gates.add(gate);
		return gate;
	}
	
	public Gate makeGate(GateTypes gateType,int x,int y){
		switch(gateType){
		case AND:
			return addGate(new AndGate(x,y));
		case OR:
			return addGate(new OrGate(x,y));
		case NOT:
			return addGate(new NotGate(x,y));
		case LIGHT:
			return addGate(new LightGate(x,y));
		case NAND:
			return addGate(new NandGate(x,y));
		case NOR:
			return addGate(new NorGate(x,y));
		case XNOR:
			return addGate(new XnorGate(x,y));
		case XOR:
			return addGate(new XorGate(x,y));
		case TOGGLE:
			return addGate(new ToggleGate(x,y));
		case MEMORY:
			return addGate(new MemoryCell(x,y));
		default:
			return null;
		}
	}
	
	public boolean findGate(int x,int y) {
		for (Gate g : gates) {
			DrawInfo info = g.getDrawInfo();
			if(x>=info.x && x<info.x+info.width && y>=info.y && y<info.y+info.height) {
				gateAtPoint = g;
				gateAtPointType = 1;
				return true;
			}
			if(x==info.x && y==info.y+info.height && info.hasOutput) {
				gateAtPoint = g;
				gateAtPointType = 3;
				return true;
			}
			if(x>=info.x && x<info.x+g.getInputs().size() && y==info.y-1) {
				gateAtPoint = g;
				gateAtPointType = 2;
				gateAtPointInput = x-info.x;
				return true;
			}
		}
		gateAtPoint = null;
		gateAtPointType = 0;
		gateAtPointInput = 0;
		return false;
	}
}


