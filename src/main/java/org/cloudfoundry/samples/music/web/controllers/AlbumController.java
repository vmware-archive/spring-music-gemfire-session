package org.cloudfoundry.samples.music.web.controllers;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.SessionInfo;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/albums")
@Scope("session")
public class AlbumController {
    private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);
    private AlbumRepository repository;

    private SessionInfo sessionInfo = new SessionInfo();

    @Autowired
    public AlbumController(AlbumRepository repository) {
        this.repository = repository;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Album> albums() {
        return repository.findAll();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public Album add(@RequestBody @Valid Album album) {
        logger.info("Adding album " + album.getId());

        sessionInfo.incModifyCount();

        logger.info("Total modifications for this session now "
                + sessionInfo.getModifyCount());

        return repository.save(album);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Album update(@RequestBody @Valid Album album) {
        logger.info("Updating album " + album.getId());

        sessionInfo.incModifyCount();

        logger.info("Total modifications for this session now "
                + sessionInfo.getModifyCount());

        return repository.save(album);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Album getById(@PathVariable String id) {
        logger.info("Getting album " + id);
        return repository.findOne(id);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable String id) {
        logger.info("Deleting album " + id);

        repository.delete(id);

        sessionInfo.incModifyCount();

        logger.info("Total modifications for this session now "
                + sessionInfo.getModifyCount());
    }

    @ResponseBody
    @RequestMapping(value="/sessionContent")
    public SessionInfo sessionContent(){
        return sessionInfo;
    }

}