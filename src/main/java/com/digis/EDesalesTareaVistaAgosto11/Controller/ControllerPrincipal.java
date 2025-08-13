package com.digis.EDesalesTareaVistaAgosto11.Controller;

import com.digis.EDesalesTareaVistaAgosto11.ML.Result;
import com.digis.EDesalesTareaVistaAgosto11.ML.Tarea;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/tarea")
public class ControllerPrincipal {

    @GetMapping
    public String index(Model model) {
        Result result = new Result();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Tarea>> response = restTemplate.exchange("http://localhost:8081/tarea",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Tarea>>() {
        });
        List<Tarea> tareas = response.getBody().objects;
        model.addAttribute("tarea", tareas);

        return "index";
    }

    @GetMapping("/barraDeHerramientas")
    public String barraHerramientas(Model model) {

        return "Layout";
    }

}
