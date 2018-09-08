package com.seventeen.TCP;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @function
 * @author 陈绍康
 * @Time 2018-12-31 21:04:01
 */
@Component
public class ServerRun  implements CommandLineRunner {

	@Override
	public void run(String... strings) throws Exception {
		new SocketServer().run();
	}
}


