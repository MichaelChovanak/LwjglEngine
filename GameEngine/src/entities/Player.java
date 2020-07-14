package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class Player extends Entity{

	private static final float GRAVITY = -9.8f;
	private static final float TERMINAL_VELOCITY = 50f;
	private static final float JUMP_POWER = 1;

	private static float RUN_SPEED = 20;

	private long lastTimeGrounded;
	private float timeInAir;
	private float currentForwardSpeed = 0;
	private float currentSideSpeed = 0;
	private float currentTurnSpeed = 0;
	private float verticalSpeed = 0;
	private boolean readyToJump = true;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentForwardSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		distance = currentSideSpeed * DisplayManager.getFrameTimeSeconds();
		dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY() + 90f)));
		dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY() + 90f)));
		super.increasePosition(dx, 0, dz);
		timeInAir = (lastTimeGrounded - DisplayManager.getCurrentTime()) / 1000f;
		if(verticalSpeed < -TERMINAL_VELOCITY)
			verticalSpeed = -TERMINAL_VELOCITY;
		else if (verticalSpeed > TERMINAL_VELOCITY)
			verticalSpeed = TERMINAL_VELOCITY;
		else
			verticalSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds() * timeInAir;
		float terrainHeight = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < terrainHeight) {
			verticalSpeed = 0;
			super.getPosition().y = terrainHeight;
			readyToJump = true;
			lastTimeGrounded = DisplayManager.getCurrentTime();
		}
		super.increasePosition(0, 0.5f * verticalSpeed * timeInAir, 0);
		
	}
	
	private void jump() {
		verticalSpeed = -JUMP_POWER;
	}
	
	private void checkInputs() {
		
		if(readyToJump) {
			currentForwardSpeed = 0;
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				currentForwardSpeed += RUN_SPEED;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				currentForwardSpeed -= RUN_SPEED;
			}
			currentSideSpeed = 0;
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				currentSideSpeed += RUN_SPEED;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				currentSideSpeed -= RUN_SPEED;
			}
			
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && readyToJump) {
				jump();
				readyToJump = false;
				lastTimeGrounded = DisplayManager.getCurrentTime();
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				RUN_SPEED = 40;
			} else {
				RUN_SPEED = 20;
			}
			
		}
		
		
		
		
		
	}

}
