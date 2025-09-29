package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorUpdateResponse;


import java.util.List;

public interface AuthorCustomRepository {
    List<AuthorUpdateResponse> findAllAuthorUpdateResponses();
}
