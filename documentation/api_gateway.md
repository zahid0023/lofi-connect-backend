When making gateway for a specific api endpoint you first have to look for if it has the necessary classes previously
created!

Necessary classes are:

For POST/PUT

- Request DTO class
- GoHighLevel Request DTO class
- Controller class (*Controller)
    - annotation: @RestController
    - annotation: @RequestMapping
- Service interface (*Service)
- Service Implementation class (*ServiceImpl)
    - annotation: @Service
    - annotation @Slf4j
- Feign Client interface (*Client)
    - annotation: @FeignClient

For GET/DELETE

- Controller class (*Controller)
    - annotation: @RestController
    - annotation: @RequestMapping
- Service interface (*Service)
- Service Implementation class (*ServiceImpl)
    - annotation: @Service
    - annotation @Slf4j
- Feign Client interface (*Client)
    - annotation: @FeignClient

*Component
The name of the class is same as the subsection name in the stoplight gohighlevel API documentation.

Then you have to provide the curl command to chatgpt. Before giving the curl command to chatgpt you have to first give
it the prompt. After giving the prompt please validate the methods.