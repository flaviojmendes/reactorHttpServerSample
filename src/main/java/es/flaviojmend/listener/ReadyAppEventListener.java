package es.flaviojmend.listener;

import es.flaviojmend.data.entity.Task;
import es.flaviojmend.data.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;
import reactor.io.buffer.Buffer;
import reactor.io.codec.StandardCodecs;
import reactor.io.codec.json.JsonCodec;
import reactor.io.net.NetStreams;
import reactor.io.net.ReactorChannelHandler;
import reactor.io.net.http.HttpChannel;
import reactor.rx.Streams;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flavio on 11/07/16.
 */
@Component
public class ReadyAppEventListener  {

    @Autowired
    TaskRepository taskRepository;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws InterruptedException {

        final EventBus serverReactor = EventBus.create(Environment.get());

        Selector readingsMessageSelector = Selectors.object("readingsMessage");
        Selector queryRuleSelector = Selectors.object("queryRule");

        NetStreams.<String, String>httpServer(spec -> spec.codec(StandardCodecs.STRING_CODEC)
                .listen(3000))
                .get("/tasks", tasksHandler())
                .post("/task", createTaskHandler())
                .start()
                .await();



    }


    private ReactorChannelHandler<String, String, HttpChannel<String, String>> tasksHandler() {
        return channel -> {
            JsonCodec<List, List> tasksCodec = new JsonCodec<>(List.class);

            List<Task> tasks = (List<Task>) taskRepository.findAll();



                return channel.writeWith(Streams.just(tasksCodec.encoder().apply(tasks).asString()));

        };
    }

    private ReactorChannelHandler<String, String, HttpChannel<String, String>> createTaskHandler() {
        return channel -> {

            return channel.writeWith(Streams
                    .wrap(channel)
                    .take(1)
                    .log("received")
                    .flatMap(data -> {
                        JsonCodec<Task, Task> taskCodec = new JsonCodec<>(Task.class);
                        Task task = taskCodec.decoder().apply(Buffer.wrap(data));
                        task.setDateCreated(new Date());
                        task.setDone(false);

                        taskRepository.save(task);



                        return Streams.just("Created!");
                    }));
        };
    }

}
