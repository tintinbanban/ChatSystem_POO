package com.chat_system.controller;

import java.util.TimerTask;

import com.chat_system.controller.Controller.method_name;
import communication.User;

public class TimeoutController extends TimerTask {
	private Controller controller;
	
	public TimeoutController(Controller controller) {
		this.controller = controller;
	}
	
	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		long delay, currentTime = System.currentTimeMillis();
		
		for (java.util.Map.Entry<User, Long> entry : getController().getUserList().getTimeoutUserList().entrySet()) {
			User user = entry.getKey();
			long timeUser = entry.getValue();
			delay = currentTime - timeUser;
			if (delay > 6000) {
				getController().getUserList().removeUser(user);
				getController().notifierObservateurs(method_name.processUser.name());
			}
		}
	}

}
