package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

public class Camera 
{

	private static final float SPEED = 0.1f;
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,50,0);
	private Vector3f direction = new Vector3f(0,0,0);
	private Vector3f perpendicular = new Vector3f(0,0,0);
	private float pitch; //how high or low the camera is aiming
	private float yaw; //how left or right the camera is aiming
	private float roll; //how tilted the camera is to one side or the other
	
	private Player player;
	
	public Camera(Player player)
	{
		this.player = player;
	}
	public void move()
	{
		/*
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			position.x += direction.x * SPEED;
			position.y -= direction.y * SPEED;
			position.z += direction.z * SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			position.x -= perpendicular.x * SPEED;
			position.z -= perpendicular.z * SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			position.x -= direction.x * SPEED;
			position.y += direction.y * SPEED;
			position.z -= direction.z * SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x += perpendicular.x * SPEED;
			position.z += perpendicular.z * SPEED;
		} 
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			yaw -= 1f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			yaw += 1f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			pitch -= 0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			pitch += 0.5f;
		}

		updateDirection();
		updatePerpendicular();
		clampDirection();*/
		
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		clampPitch();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();

		updateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	private void updateDirection() {
		direction.x = (float) Math.sin(Math.toRadians(yaw));
		direction.z = (float) - Math.cos(Math.toRadians(yaw));
		direction.y = (float) Math.sin(Math.toRadians(pitch));
		//System.out.println("x:" + direction.x + " y:" + direction.y + " z:" + direction.z);
	}
	
	private void clampPitch() {
		if(pitch >= 90) {
			pitch = 90;
		}
		else if(pitch <= -90){
			pitch = -90;
		}
		
	}
	
	private void updatePerpendicular()
	{
		perpendicular.x = (float) Math.sin(Math.toRadians(yaw + 90f));
		perpendicular.z = (float) - Math.cos(Math.toRadians(yaw + 90f));
	}
	public Vector3f getPosition() 
	{
		return position;
	}
	
	public float getPitch() 
	{
		return pitch;
	}
	
	public float getYaw() 
	{
		return yaw;
	}
	
	public float getRoll() 
	{
		return roll;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void updateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance;
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch() {
		//if(Mouse.isButtonDown(1)) { //1 = right mouse button, 0 = left mouse button
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		//}
	}
	
	private void calculateAngleAroundPlayer() {
		float angleChange = Mouse.getDX() * 0.3f;
		if(Mouse.isButtonDown(1)) { //1 = right mouse button, 0 = left mouse button
			angleAroundPlayer -= angleChange;
			//player.increaseRotation(0, -angleChange, 0);
		} else {
			//angleAroundPlayer -= angleChange;
			player.increaseRotation(0, -angleChange, 0);
		}
	}
	
}
