package com.wowmate.server;

import com.wowmate.server.post.domain.Category;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.repository.CategoryRepository;
import com.wowmate.server.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
