package es.flaviojmend;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import reactor.Environment;
import reactor.spring.context.config.EnableReactor;

import java.io.DataInputStream;
import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 *
 * Created by flavio on 11/07/16.
 */
@EnableReactor
public class Main {


    static {
        Environment.initializeIfEmpty();

    }

    public static void main(String[] args) throws InterruptedException, IOException {
        ApplicationContext ctx = SpringApplication.run(AppConfiguration.class, args);

        DataInputStream reader = new DataInputStream(System.in);
        do{
            char key = reader.readChar();
            if(key == 'q'){ break;}
            sleep(1000);
        }while(true);
        SpringApplication.exit(ctx);
    }

}
