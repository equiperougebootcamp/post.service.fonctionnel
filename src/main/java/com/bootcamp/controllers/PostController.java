package com.bootcamp.controllers;

import com.bootcamp.commons.enums.TypePost;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.entities.Post;
import com.bootcamp.services.PostService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@RestController("PostContoller")
@RequestMapping("/posts")
@Api(value = "Post API", description = "Post API")
@CrossOrigin(origins = "*")
public class PostController {
//

    @Autowired
    PostService postService;

    @Autowired
    HttpServletRequest request;

    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Create a post", notes = "Create a post")
    public ResponseEntity<Post> create(@RequestBody  Post post) throws SQLException {
        post = postService.create(post);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read All Posts", notes = "Read aall the Posts")
    public ResponseEntity<List<Post>> read() throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        List<Post> posts = postService.read(request);
        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }

    @RequestMapping(value="/{typePoost}",method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read All Posts by Type of post", notes = "Read aall the Posts by type of post")
    public ResponseEntity<List<Post>> read(@PathVariable String typePost) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        TypePost type = TypePost.valueOf( typePost.toUpperCase() );
        List<Post> posts = postService.readByTypePost(type);
        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }

   /* @RequestMapping(value="/{idProdui} ",method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read All Posts", notes = "Read aall the Posts")
    public ResponseEntity<List<Post>> readByProduit() throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        List<Post> posts = postService.read(request);
        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get one Posts", notes = "Read a particular Posts")
    public ResponseEntity<Post> getById(@PathVariable int id) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Post post = postService.read(id);
        return new ResponseEntity<Post>(post, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Delete one Posts", notes = "delete a particular Posts")
    public ResponseEntity<Boolean> delete(@PathVariable int id) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
       boolean done = postService.delete(id);
        return new ResponseEntity<>(done, HttpStatus.OK);
    }

}
