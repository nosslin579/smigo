package org.smigo.comment;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentHandler {
    private static final Logger log = LoggerFactory.getLogger(CommentHandler.class);
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CommentDao commentDao;


    public List<Comment> getComments(String receiver) {
        return commentDao.getComments(receiver);
    }

    public int addComment(Comment comment, AuthenticatedUser user) {
        return commentDao.addComment(comment, user.getId());
    }

    public HttpStatus removeComment(int id, AuthenticatedUser user) {
        List<Comment> comments = commentDao.getComments(user.getUsername());
        boolean isReceiver = comments.stream().anyMatch(comment -> comment.getId() == id);
        if (user.isModerator() || isReceiver) {
            commentDao.deleteComment(id);
            return HttpStatus.OK;
        }
        return HttpStatus.FORBIDDEN;
    }

    public void updateComment(Comment comment, AuthenticatedUser user) {
        List<Comment> comments = commentDao.getComments(user.getUsername());
        boolean isReceiver = comments.stream().anyMatch(c -> c.getId() == comment.getId());
        if (isReceiver) {
            commentDao.update(comment);
        } else {
            throw new IllegalArgumentException("Can not update other ppl comment");
        }
    }
}