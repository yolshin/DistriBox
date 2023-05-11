package capstone.distribox.project.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class ListService {

    public List<String> getServerList() {

        return List.of("8081", "8082", "8083");
    }

}
