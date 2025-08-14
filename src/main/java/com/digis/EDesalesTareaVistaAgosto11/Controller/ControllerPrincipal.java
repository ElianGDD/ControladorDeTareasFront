package com.digis.EDesalesTareaVistaAgosto11.Controller;

import com.digis.EDesalesTareaVistaAgosto11.ML.EstadoTarea;
import com.digis.EDesalesTareaVistaAgosto11.ML.Result;
import com.digis.EDesalesTareaVistaAgosto11.ML.Tarea;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

    @GetMapping("/formEditable")
    public String disenioTarea(
            @RequestParam int idTarea,
            Model model) {

        if (idTarea >= 1) {
            RestTemplate restTemplateEstado = new RestTemplate();
            ResponseEntity<Result<EstadoTarea>> responseEstado = restTemplateEstado.exchange("http://localhost:8081/tarea/DarEstadoTarea",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<EstadoTarea>>() {
            });
            List<EstadoTarea> estadoTareas = responseEstado.getBody().objects;
            model.addAttribute("estado", estadoTareas);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Result<Tarea>> response = restTemplate.exchange(
                    "http://localhost:8081/tarea/DarTareaXId?idTarea=" + idTarea,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Tarea>>() {
            }
            );
            Tarea tarea = response.getBody().object;

            model.addAttribute("tarea", tarea);

            return "DisenioDeTareas";
        } else {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Result<EstadoTarea>> response = restTemplate.exchange("http://localhost:8081/tarea/DarEstadoTarea",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<EstadoTarea>>() {
            });
            List<EstadoTarea> estadoTareas = response.getBody().objects;
            model.addAttribute("estado", estadoTareas);
            Tarea tarea = new Tarea();
            tarea.setEstadoTarea(new EstadoTarea());
            model.addAttribute("tarea", tarea);
            return "DisenioDeTareas";
        }

    }

    @GetMapping("/barraDeHerramientas")
    public String barraHerramientas(Model model) {

        return "Layout";
    }

    @PostMapping("/EditarTarea")
    public String formEditableTarea(
            @ModelAttribute Tarea tarea,
            Model model) {
        Result result = new Result();
        RestTemplate restTemplateForm = new RestTemplate();
        RestTemplate restTemplateDatosTarea = new RestTemplate();

        if (tarea.getIdTarea() == 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Tarea> request = new HttpEntity<>(tarea, headers);

            try {
                ResponseEntity<Tarea> responseForm = restTemplateForm.exchange(
                        "http://localhost:8081/tarea/AgregarNuevaTarea",
                        HttpMethod.POST,
                        request,
                        Tarea.class
                );
                tarea = responseForm.getBody();
            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                System.out.println("HTTP Status: " + ex.getStatusCode());
                System.out.println("Error body: " + ex.getResponseBodyAsString());
                ex.printStackTrace();
            }

            ResponseEntity<Result<Tarea>> responseTarea = restTemplateDatosTarea.exchange("http://localhost:8081/tarea",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Tarea>>() {
            });
            List<Tarea> tareas = responseTarea.getBody().objects;
            model.addAttribute("tarea", tareas);
            return "index";
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Tarea> requestEntity = new HttpEntity<>(tarea, headers);
            try {
                ResponseEntity<Tarea> responseForm = restTemplateForm.exchange(
                        "http://localhost:8081/tarea/ActualizarTarea",
                        HttpMethod.PUT,
                        requestEntity,
                        Tarea.class
                );
                tarea = responseForm.getBody();
            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                System.out.println("HTTP Status: " + ex.getStatusCode());
                System.out.println("Error body: " + ex.getResponseBodyAsString());
                ex.printStackTrace();
            }

            ResponseEntity<Result<Tarea>> responseTarea = restTemplateDatosTarea.exchange("http://localhost:8081/tarea",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Tarea>>() {
            });
            List<Tarea> tareas = responseTarea.getBody().objects;
            model.addAttribute("tarea", tareas);
            return "index";
        }
    }

    @PostMapping("/EliminarTarea")
    public String EliminarTareaXId(@ModelAttribute Tarea tarea,
            Model model) {
        RestTemplate restTemplateEliminar = new RestTemplate();
        RestTemplate restTemplateDatosTarea = new RestTemplate();
        ResponseEntity<Result> result = restTemplateEliminar.exchange(
                "http://localhost:8081/tarea/BorrarTarea?idTarea=" + tarea.getIdTarea(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Result.class
        );
        ResponseEntity<Result<Tarea>> responseTarea = restTemplateDatosTarea.exchange("http://localhost:8081/tarea",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Tarea>>() {
        });
        List<Tarea> tareas = responseTarea.getBody().objects;
        model.addAttribute("tarea", tareas);

        return "Index";
    }

}
