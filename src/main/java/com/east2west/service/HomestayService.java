package com.east2west.service;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import com.east2west.exception.ResourceNotFoundException;
import com.east2west.models.DTO.HomestayDTO;
import com.east2west.models.Entity.HomeType;
import com.east2west.models.Entity.Homestay;
import com.east2west.models.Entity.Perk;
import com.east2west.models.Entity.Ward;
import com.east2west.repository.HomeTypeRepository;
import com.east2west.repository.HomestayRepository;
import com.east2west.repository.PerkRepository;
import com.east2west.repository.WardRepository;

public class HomestayService {
    @Autowired
    private HomestayRepository homestayRepository;

    @Autowired
    private PerkRepository perkRepository;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private HomeTypeRepository homeTypeRepository;

    public Homestay createOrUpdateHomestay(HomestayDTO homestayDTO) {
        Homestay homestay;
        if (homestayDTO.getHomestayid() != null && homestayDTO.getHomestayid() != 0) {
            homestay = homestayRepository.findById(homestayDTO.getHomestayid())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Homestay not found with id " + homestayDTO.getHomestayid()));
        } else {
            homestay = new Homestay();
        }
        homestay.setUserid(homestayDTO.getUserid());
        homestay.setLongitude(homestayDTO.getLongitude());
        homestay.setLatitude(homestayDTO.getLatitude());
        homestay.setTitle(homestayDTO.getTitle());
        homestay.setAddress(homestayDTO.getAddress());
        homestay.setGeom(homestayDTO.getGeom());
        homestay.setPhotos(homestayDTO.getPhotos());
        homestay.setDescription(homestayDTO.getDescription());
        homestay.setExactinfo(homestayDTO.getExactinfo());
        homestay.setCleaningfee(homestayDTO.getCleaningfee());
        homestay.setIsapproved(homestayDTO.getIsapproved());
        homestay.setMaxguest(homestayDTO.getMaxguest());
        Ward ward = wardRepository.findById(homestayDTO.getWardId())
                .orElseThrow(() -> new ResourceNotFoundException("Ward not found with id " + homestayDTO.getWardId()));
        homestay.setWard(ward);

        HomeType homeType = homeTypeRepository.findById(homestayDTO.getHometypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "HomeType not found with id " + homestayDTO.getHometypeId()));
        homestay.setHometype(homeType);

        // Set Perks from DTO
        if (homestayDTO.getPerkIds() != null && !homestayDTO.getPerkIds().isEmpty()) {
            List<Perk> perks = perkRepository.findAllById(homestayDTO.getPerkIds());
            homestay.setPerks(perks);
        } else {
            homestay.setPerks(new ArrayList<>()); 
        }

        return homestayRepository.save(homestay);
    }

    public void deleteHomestay(int id) {
        homestayRepository.deleteById(id);
    }
}
