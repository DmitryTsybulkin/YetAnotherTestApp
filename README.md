# App

Change `application.properties`:

If you want to use in-memory storage, specify the corresponding profile: `use_db_storage` OR `use_mem_storage`
If you use in-memory storage, specify it size: `storage.memory.size`. It is a number of objects (multiply 2), 
that will save in the storage. The database is H2.

Run:
```
$ mvn clean install
$ mvn spring-boot:run
```

Swagger: http://localhost:8080/swagger-ui.html

In-memory storage contains `eden` and `longterm` maps. The first is structure, which keeps "new" objects. 
When it becomes full, the whole data move to a long-term data structure.