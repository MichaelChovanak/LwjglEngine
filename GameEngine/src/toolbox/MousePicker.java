package toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;

public class MousePicker {
	
	private Vector3f currentRay;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	
	public MousePicker(Camera camera, Matrix4f projectionMatrix) {
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = Maths.createViewMatrix(camera);
		
	}
	
	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public void update() {
		this.viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
	}
	
	private Vector3f calculateMouseRay() {
		//gets Viewport Space coords
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		
		//converts to Normalized Device Space coords
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		
		//converts to Homogenous Clip Space coords
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		
		//converts to Eye Space coords
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		
		//converts to World Space coords
		Vector3f worldRay = toWorldCoords(eyeCoords);
		
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedViewMatrix = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedViewMatrix, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		return mouseRay;
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjectionMatrix = Matrix4f.invert(projectionMatrix, null); //second input is for storing inversion in new matrix
		Vector4f eyeCoords = Matrix4f.transform(invertedProjectionMatrix, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f); //set z component to point into the screen, then w component to be 0
	}
	
	/**
	 * getNormalizedDeviceCoords converts Viewport Space mouse coordinates to Normalized Device Space coordinates.
	 * @param mouseX
	 * @param mouseY
	 * @return normalizedCoords
	 */
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f*mouseX) / Display.getWidth() - 1f;
		float y = (2f*mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x,y);
	}
}
