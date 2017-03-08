# Android-Intermediate

## Aplicación de componentes de Android

<img src="https://i.ytimg.com/vi/PXn_-Jd1Zv8/hqdefault.jpg" />

Retrofit es una librería que nos facilitará las conexiones a servicios rest. En la siguiente imagen podemos ver a grandes rasgos como funciona:

<img src="https://i.ytimg.com/vi/3cN6aJmwMAg/maxresdefault.jpg" />

Siguiendo el demo de la página de Retrofit, vamos a detallar algunos conceptos claves a tener en cuenta:

1. Para utilizar el API lo hacemos a través de una interfaz:

```	
public interface GitHubService {
  @GET("users/{user}/repos")
  Call<List<Repo>> listRepos(@Path("user") String user);
}
```	

2. Para poder utilizar esta interfaz, debemos implementar el builder de Retrofit:

```	
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .build();

GitHubService service = retrofit.create(GitHubService.class);
```	

3. Cada llamada creada por lo definido anteriormente (a diferencia de Retrofit 1.x) puede realizarse de forma síncrona y asíncrona:

```
Call<List<Repo>> repos = service.listRepos("octocat");
```

4. Para incluir Retrofit como dependencia en tu proyecto:

```	
compile 'com.squareup.retrofit2:retrofit:2.2.0'
```	

5. Además hay algunas dependencias extras:

```
Gson: com.squareup.retrofit2:converter-gson
Jackson: com.squareup.retrofit2:converter-jackson
Moshi: com.squareup.retrofit2:converter-moshi
Protobuf: com.squareup.retrofit2:converter-protobuf
Wire: com.squareup.retrofit2:converter-wire
Simple XML: com.squareup.retrofit2:converter-simplexml
Scalars (primitives, boxed, and String): com.squareup.retrofit2:converter-scalars
```

Hay muchas otras recomendaciones, como la parte del proguard que hay que tener en cuenta al momento de ofuscar la aplicación para ser subida al play store. 

## Referencias

  - http://square.github.io/retrofit/
  - https://github.com/square/retrofit
  - http://www.vogella.com/tutorials/Retrofit/article.html
  - https://github.com/cpinan/CapstoneProject
  - https://inthecheesefactory.com/blog/retrofit-2.0/en
