package com.example.tssruntime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.taskana.classification.api.ClassificationService;
import pro.taskana.classification.api.models.Classification;
import pro.taskana.classification.api.models.ClassificationSummary;
import pro.taskana.task.api.TaskService;
import pro.taskana.task.api.models.ObjectReference;
import pro.taskana.task.api.models.Task;
import pro.taskana.task.api.models.TaskSummary;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.WorkbasketType;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.api.models.WorkbasketSummary;

import java.util.List;

@RestController
@RequestMapping("/api/taskana")
public class TaskanaController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkbasketService workbasketService;

    @Autowired
    private ClassificationService classificationService;

    /**
     * Create a new task
     */
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        try {
            System.out.println("Creating task with name: " + request.getName() + ", workbasketId: " + request.getWorkbasketKey());
            
            Task task = taskService.newTask(request.getWorkbasketKey());
            System.out.println("Task is "+task);
            System.out.println("Task Service is "+taskService);

            task.setName(request.getName());
            task.setDescription(request.getDescription());
            task.setClassificationKey("TASK");
            
            // Create and set the primary ObjectReference
            ObjectReference objRef = taskService.newObjectReference();
            objRef.setCompany("MyCompany");
            objRef.setSystem("MySystem");
            objRef.setSystemInstance("MyInstance");
            objRef.setType("MyType");
            objRef.setValue("MyValue");
            task.setPrimaryObjRef(objRef);
            
            System.out.println("About to create task: " + task);
            Task createdTask = taskService.createTask(task);
            System.out.println("Created task: " + createdTask);
            
            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            System.out.println("Error creating task: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieve all tasks
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskSummary>> getAllTasks() {
        try {
            List<TaskSummary> tasks = taskService.createTaskQuery().list();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            System.out.println("Error getting tasks: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieve a specific task by ID
     */
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<Task> retrieveTask(@PathVariable String taskId) {
        try {
            Task task = taskService.getTask(taskId);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all workbaskets
     */
    @GetMapping("/workbaskets")
    public ResponseEntity<List<WorkbasketSummary>> getWorkbaskets() {
        try {
            List<WorkbasketSummary> workbaskets = workbasketService.createWorkbasketQuery().list();
            return ResponseEntity.ok(workbaskets);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Create a new workbasket
     */
    @PostMapping("/workbaskets")
    public ResponseEntity<Workbasket> createWorkbasket(@RequestBody CreateWorkbasketRequest request) {
        try {
            System.out.println("Creating workbasket with key: " + request.getKey() + ", domain: " + request.getDomain());
            Workbasket workbasket = workbasketService.newWorkbasket(request.getKey(), request.getDomain());
            workbasket.setName(request.getName());
            workbasket.setDescription(request.getDescription());
            workbasket.setOwner("vijay");
            workbasket.setType(WorkbasketType.PERSONAL);
            System.out.println("About to create workbasket: " + workbasket);
            Workbasket createdWorkbasket = workbasketService.createWorkbasket(workbasket);
            System.out.println("Workbasket created successfully: " + createdWorkbasket);
            return ResponseEntity.ok(createdWorkbasket);
        } catch (Exception e) {
            System.err.println("Error creating workbasket: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Create a new classification
     */
    @PostMapping("/classifications")
    public ResponseEntity<Classification> createClassification(@RequestBody CreateClassificationRequest request) {
        try {
            System.out.println("Creating classification with key: " + request.getKey() + ", domain: " + request.getDomain());
            Classification classification = classificationService.newClassification(request.getKey(), request.getDomain(), "TASK");
            classification.setName(request.getName());
            classification.setDescription(request.getDescription());
            
            System.out.println("About to create classification: " + classification);
            Classification createdClassification = classificationService.createClassification(classification);
            System.out.println("Created classification: " + createdClassification);
            
            return ResponseEntity.ok(createdClassification);
        } catch (Exception e) {
            System.out.println("Error creating classification: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get all classifications
     */
    @GetMapping("/classifications")
    public ResponseEntity<List<ClassificationSummary>> getAllClassifications() {
        try {
            List<ClassificationSummary> classifications = classificationService.createClassificationQuery().list();
            return ResponseEntity.ok(classifications);
        } catch (Exception e) {
            System.out.println("Error getting classifications: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * DTO for creating tasks
     */
    public static class CreateTaskRequest {
        private String name;
        private String description;
        private Integer priority;
        private String state;
        private String workbasketKey;
        private String domain;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getWorkbasketKey() { return workbasketKey; }
        public void setWorkbasketKey(String workbasketKey) { this.workbasketKey = workbasketKey; }

        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }
    }

    /**
     * DTO for creating workbaskets
     */
    public static class CreateWorkbasketRequest {
        private String key;
        private String name;
        private String description;
        private String domain;
        private String type;

        // Getters and setters
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    public static class CreateClassificationRequest {
        private String key;
        private String name;
        private String description;
        private String domain;
        private String category;

        // Getters and setters
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }
} 