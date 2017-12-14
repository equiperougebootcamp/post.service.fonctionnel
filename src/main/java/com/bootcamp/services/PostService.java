package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.TypePost;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.crud.PostCRUD;
import com.bootcamp.entities.Post;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@Component
public class PostService implements DatabaseConstants {

     PostCRUD postCRUD;


    @PostConstruct
    public void init(){
        postCRUD = new PostCRUD();
    }

    public Post create(Post post) throws SQLException {
        post.setDateCreation(System.currentTimeMillis());
        post.setDateMiseAJour(System.currentTimeMillis());
        postCRUD.create(post);
        return post;
    }

    public boolean update(Post post) throws SQLException {
        post.setDateMiseAJour(System.currentTimeMillis());
        postCRUD.update(post);
        return true;

    }

    public boolean delete(int id) throws SQLException {
        Post post = read(id);
        postCRUD.delete(post);
        return true;
    }

    public Post read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Post> posts = postCRUD.read(criterias);

        return posts.get(0);
    }

    public List<Post> readByTypePost(TypePost type) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("typePost", "=", type));
        List<Post> posts = postCRUD.read(criterias);
        return posts;
    }



    public List<Post> read(HttpServletRequest request) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Post> posts = null;
        if(criterias == null && fields == null)
            posts =  postCRUD.read();
        else if(criterias!= null && fields==null)
            posts = postCRUD.read(criterias);
        else if(criterias== null && fields!=null)
            posts = postCRUD.read(fields);
        else
            posts = postCRUD.read(criterias, fields);

        return posts;
    }


    public boolean exist(int id) throws Exception{
        if(read(id)!=null)
            return true;
        return false;
    }

    public List<Post> getAll() throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        return PostCRUD.read();
    }
}
