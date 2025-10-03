package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.publisher.PublisherCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.publisher.PublisherUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.publisher.PublisherResponse;
import dev.thangngo.lmssoftdreams.dtos.response.publisher.PublisherUpdateResponse;
import dev.thangngo.lmssoftdreams.entities.Book;
import dev.thangngo.lmssoftdreams.entities.Publisher;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.mappers.PublisherMapper;
import dev.thangngo.lmssoftdreams.repositories.BookRepository;
import dev.thangngo.lmssoftdreams.repositories.PublisherRepository;
import dev.thangngo.lmssoftdreams.services.PublisherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("publisherService")
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;
    private final BookRepository bookRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository, PublisherMapper publisherMapper, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
        this.bookRepository = bookRepository;
    }

    @Override
    public PublisherResponse getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toPublisherResponse)
                .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_FOUND));
    }

    @Override
    public List<PublisherResponse> getPublisherByName(String name) {
        return publisherRepository.findByNameContainingAndIsActive(name, true)
                .stream()
                .map(publisherMapper::toPublisherResponse)
                .toList();
    }

    @Override
    public PublisherResponse createPublisher(PublisherCreateRequest request) {
        boolean exists = publisherRepository.findAll().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(request.getName()));
        if (exists) {
            throw new AppException(ErrorCode.PUBLISHER_ALREADY_EXISTS);
        }
        Publisher publisher = publisherMapper.toPublisher(request);
        return publisherMapper.toPublisherResponse(publisherRepository.save(publisher));
    }

    @Override
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_FOUND));

        if (bookRepository.existsByPublisherId(id)) {
            throw new AppException(ErrorCode.PUBLISHER_IS_USING);
        }
        publisher.setIsActive(false);
        publisherRepository.save(publisher);
    }

    @Override
    public PublisherResponse updatePublisher(Long id, PublisherUpdateRequest request) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_FOUND));

        boolean exists = publisherRepository.findAll().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(request.getName()) && !p.getId().equals(id));
        if (exists) {
            throw new AppException(ErrorCode.PUBLISHER_ALREADY_EXISTS);
        }

        publisherMapper.updatePublisherFromDto(request, publisher);
        return publisherMapper.toPublisherResponse(publisherRepository.save(publisher));
    }

    @Override
    public List<PublisherResponse> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(publisherMapper::toPublisherResponse)
                .toList();
    }

    @Override
    public List<PublisherUpdateResponse> getPublisherUpdate() {
        return publisherRepository.findAllPublisherUpdateResponses();
    }
}
