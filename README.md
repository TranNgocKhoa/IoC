# IoC

This is a simple implementation of an IoC library.
I use Java reflection in project.
Maybe in the future, I will research about implementing an IoC library that provides dependency injection with [no runtime overhead](https://rskupnik.github.io/dependency-injection-in-pet-project-dagger2).

## Usage

It's simple.

```java
// Main class
@ComponentScan("com.example.demoioc")
public class DemoIoCApplication {
    public static void main(String[] args) {
        IoC ioC = IoC.initBeans(DemoIoCApplication.class);
        ioC.getBean(Start.class).start();
    }
}

// Component classes
@Component
public class ToolService {
    public String getTool() {
        return "Tool!";
    }
}

@Component
public class PersonService {
    private final ToolService toolService;

    public PersonService(ToolService toolService) {
        this.toolService = toolService;
    }

    public void work() {
        System.out.println(toolService.getTool());

        System.out.println("Work!");
    }
}

@Component
public class Start {
    private final PersonService personService;

    public Start(PersonService personService) {
        this.personService = personService;
    }

    public void start() {
        System.out.println("======= Start =======");
        personService.work();
        System.out.println("=======  End  =======");
    }
}
```
