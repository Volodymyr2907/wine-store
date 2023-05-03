package com.mentorship.vineservice.service;

import com.mentorship.vineservice.domain.PostOffice;
import com.mentorship.vineservice.repository.PostOfficeRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostOfficeService {

    private final PostOfficeRepository postOfficeRepository;

    public PostOffice findPostOfficeById(Long postOfficeId) {
        return postOfficeRepository.findById(postOfficeId)
            .orElseThrow(() -> new NoSuchElementException(
                String.format("Post office with id  %s not exist", postOfficeId)));
    }

}
