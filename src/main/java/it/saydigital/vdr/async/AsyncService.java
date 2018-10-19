package it.saydigital.vdr.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.saydigital.vdr.async.task.BackgroundTask;
import it.saydigital.vdr.async.task.FullDownloadTask;
import it.saydigital.vdr.async.task.TaskStatus;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;

@Service
public class AsyncService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	private @Autowired 
	AutowireCapableBeanFactory beanFactory;

	
	private List<BackgroundTask> tasks = new ArrayList<>();
	
	
	@Async
	@Transactional //to allow hibernate session in new thread
	public void fullDowload(MarketingEntity entity, User user, String baseUrl) {
		String id = entity.getName().replaceAll(" ", "_")+"_"+user.getId()+"_"+System.currentTimeMillis();
		FullDownloadTask task = new FullDownloadTask(id, user, entity);
		beanFactory.autowireBean(task);
		this.tasks.add(task);
		Map<String, Object> params = new HashMap<>();
		params.put("baseUrl", baseUrl);
		task.executeTask(params);
	}
	
	
	
	
	public List<BackgroundTask> getTasks() {
		return tasks;
	}
	
	public boolean hasRunningFullDownloadTasks(long userId, long entityId) {
		for (BackgroundTask task : this.getTasks()) {
			if (task.getName().equalsIgnoreCase("Full Download")) {
				FullDownloadTask fdTask = (FullDownloadTask) task;
				if (fdTask.getRequestor().getId() == userId && fdTask.getMktEntity().getId() == entityId && task.getStatus() == TaskStatus.RUNNING)
					return true;
			}
		}
		return false;
	}
	
	
	public List<BackgroundTask> getTasksForUser(long id) {
		List<BackgroundTask> result = new ArrayList<>();
		for (BackgroundTask task : this.tasks) {
			if (task.getRequestor().getId()==id)
				result.add(task);
		}
		return result;
	}

	
	

}
