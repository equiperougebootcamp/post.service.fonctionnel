package com.bootcamp.controllers;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.entities.Post;
import com.bootcamp.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
import org.junit.runner.RunWith;
*/
/*
 *
 * Created by darextossa on 12/5/17.
 */

//@TestExecutionListeners(MockitoTestExecutionListener.class)
//@ActiveProfiles({"controller-unit-test"})
//@SpringBootApplication(scanBasePackages={"com.bootcamp"})
//@ContextConfiguration(classes = ControllerUnitTestConfig.class)
//@WebMvcTest(value = PilierController.class, secure = false, excludeAutoConfiguration = {HealthIndicatorAutoConfiguration.class, HibernateJpaAutoConfiguration.class, FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class})

@RunWith(SpringRunner.class)
@WebMvcTest(value = PostController.class, secure = false)
@ContextConfiguration(classes={Application.class})
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService postService;


    @Test
    public void getAllPost() throws Exception{
        List<Post> posts =  loadDataPostFromJsonFile();
        //Mockito.mock(PostCRUD.class);
        when(postService.getAll()).thenReturn(posts);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/posts")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());
        System.out.println("*********************************Test for get all pilar  in secteur controller done *******************");


    }

    @Test
    public void getPostById() throws Exception{
        int id = 1;
        Post post = getPostById(id);
        when(postService.read(id)).thenReturn(post);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/posts/{id}",id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        System.out.println(response.getContentAsString());
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for get pilar by id in pilar controller done *******************");

    }


    @Test
    public void createPost() throws Exception{
        List<Post> posts =  loadDataPostFromJsonFile();
        Post post = getPostById( 1 );

        when(postService.exist(1)).thenReturn(false);
                when(postService.create(post)).thenReturn(post);

        RequestBuilder requestBuilder =
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(post));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        System.out.println(response.getContentAsString());

        System.out.println("*********************************Test for create post in post controller done *******************");
    }

/*
    @Test
    public void updatePost() throws Exception{
        List<Post> posts =  loadDataPostFromJsonFile();
        Post post = getPostById( 1 );
        when(postService.exist(post.getId())).thenReturn(true);
        when(postService.update(post)).thenReturn(true);

        RequestBuilder requestBuilder =
                put("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(post));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for update post in post controller done *******************");


    }*/

    @Test
    public void deletePost() throws Exception{
        int id = 5;
        when(postService.exist(id)).thenReturn(true);
              when(postService.delete(id)).thenReturn(true);

        RequestBuilder requestBuilder =
                delete("/posts/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for delete post in post controller done *******************");


    }

    public List<Post> loadDataPostFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile( "data-json" + File.separator + "posts.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Post>>() {
        }.getType();
        List<Post> posts = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return posts;
    }

    private Post getPostById(int id) throws Exception {
        List<Post> posts = loadDataPostFromJsonFile();
        Post post = posts.stream().filter(item -> item.getId() == id).findFirst().get();

        return post;
    }



    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if(!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    private static String objectToJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
