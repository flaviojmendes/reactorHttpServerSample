package es.flaviojmend.data.repo;

import es.flaviojmend.data.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by flavio on 19/07/16.
 */
public interface TaskRepository extends CrudRepository<Task, Long> {


    List<Task> findByDescription(String description);

}

