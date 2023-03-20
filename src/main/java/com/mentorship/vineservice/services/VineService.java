package com.mentorship.vineservice.services;

import com.mentorship.vineservice.domain.Vine;

public interface VineService {

    Long saveVineAndReturnId(Vine vine);

}
