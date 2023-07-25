# Caffeine Event Listener on Removal
This is a simple Spring Boot application that have this mechanism of changing status of an object at the time of eviction from the cache.

The following snippet provides the main functionality of this application.

```
Caffeine.newBuilder()
        .expireAfterWrite(60, TimeUnit.SECONDS)
        .scheduler(Scheduler.systemScheduler())
        .removalListener((key, value, cause) -> {
            ResponseEntity<Optional<Employee>> response = (ResponseEntity) value;
            Employee evictedEmployee = response.getBody().get();
            System.out.println("Evicted Employee: " + evictedEmployee.toString());
            evictedEmployee.setStatus("Inactive");
            Employee savedEmployee = employeeRepository.save(evictedEmployee);
            System.out.println("Saved After Eviction: " + savedEmployee.toString());
        });
```
