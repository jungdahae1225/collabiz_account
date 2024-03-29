package com.example.collabiz_account.errors;

import com.example.collabiz_account.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ErrorResource extends EntityModel<Errors> {
    public static EntityModel<Errors> modelOf(Errors errors) {
        EntityModel<Errors> errorResource = EntityModel.of(errors);
        errorResource.add(linkTo(IndexController.class).slash("search").withRel("search"));
        return errorResource;
    }
}
