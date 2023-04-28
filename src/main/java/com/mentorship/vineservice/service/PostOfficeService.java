package com.mentorship.vineservice.service;

import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.PostOffice;
import com.mentorship.vineservice.dto.PostOfficeDto;
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

    public PostOffice findPostOfficeByCityAndOfficeNumber(String city, Integer officeNumber) {
        return postOfficeRepository.findPostOfficeByCityAndOfficeNumber(city,
                officeNumber)
            .orElseThrow(() -> new NoSuchElementException(
                String.format("Post office in %s with number %s not exist", city,
                    officeNumber)));
    }

}
