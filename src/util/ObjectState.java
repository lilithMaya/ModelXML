package util;

public class ObjectState {
	
	private Object object;
	private int currentIndex;
	private Object targetObject;
	private String id;
	
	public ObjectState(Object object, int currentIndex, Object targetObject, String id) {
		super();
		this.object = object;
		this.currentIndex = currentIndex;
		this.targetObject = targetObject;
		this.id = id;
	}
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
