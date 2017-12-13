package com.bootcamp.servcice;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.crud.PostCRUD;
import com.bootcamp.entities.Post;
import com.bootcamp.services.PostService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by Ibrahim on 12/9/17.
 */

@RunWith(PowerMockRunner.class)
@WebMvcTest(value = PostService.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(PostCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class PostServiceTest {


    @InjectMocks
    private PostService postService;

    @Test
    public void getAllPost() throws Exception {
        List<Post> posts = loadDataPostFromJsonFile();
        PowerMockito.mockStatic(PostCRUD.class);
        Mockito.
                when(PostCRUD.read()).thenReturn(posts);
        List<Post> resultPosts = postService.getAll();
        Assert.assertEquals(posts.size(), resultPosts.size());

    }


    @Test
    public void create() throws Exception{
        List<Post> posts = loadDataPostFromJsonFile();
        Post post = posts.get(1);

        PowerMockito.mockStatic(PostCRUD.class);
        Mockito.
                when(PostCRUD.create(post)).thenReturn(true);
    }

    @Test
    public void delete() throws Exception{
        List<Post> posts = loadDataPostFromJsonFile();
        Post post = posts.get(1);

        PowerMockito.mockStatic(PostCRUD.class);
        Mockito.
                when(PostCRUD.delete(post)).thenReturn(true);
    }

    @Test
    public void update() throws Exception{
        List<Post> posts = loadDataPostFromJsonFile();
        Post post = posts.get(1);

        PowerMockito.mockStatic(PostCRUD.class);
        Mockito.
                when(PostCRUD.update(post)).thenReturn(true);
    }


    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
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



    private Post getDebatById(int id) throws Exception {
        List<Post> posts = loadDataPostFromJsonFile();
        Post post = posts.stream().filter(item -> item.getId() == id).findFirst().get();

        return post;
    }

}