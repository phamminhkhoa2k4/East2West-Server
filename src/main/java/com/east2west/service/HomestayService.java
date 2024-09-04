package com.east2west.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.ZoneId;

import com.east2west.constans.AvailabilityStatus;
import com.east2west.models.DTO.BookingHomestayDTO;
import com.east2west.models.DTO.HomestayAvailabilityDTO;
import com.east2west.models.DTO.HomestaySearchDTO;
import com.east2west.models.Entity.*;
import com.east2west.repository.*;
import com.east2west.util.DateUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Collectors;
import com.east2west.exception.ResourceNotFoundException;
import com.east2west.models.DTO.HomestayDTO;
import org.springframework.stereotype.Service;

@Service
public class HomestayService {
    @Autowired
    private HomestayRepository homestayRepository;

    @Autowired
    private AmenitiesRepository amenitiesRepository;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private CityProvinceRepository cityProvinceRepository;

    @Autowired
    private DistrictRepository districtRepository;


    @Autowired
    private StructureRepository structureRepository;


    @Autowired
    private HomestayAvailabilityRepository homestayAvailabilityRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private BookingHomestayRepository bookingHomestayRepository;





    public Amenities createAmenities(Amenities amenities) {
        return amenitiesRepository.save(amenities);
    }

    public Optional<Amenities>  getByIdAmenities(int id){
        return amenitiesRepository.findById(id);
    }

    public List<Amenities> getAmenitiesAll(){return  amenitiesRepository.findAll();}

    public List<Amenities> getByIdsAmenities(List<Integer> ids) {
        return amenitiesRepository.findAllById(ids);
    }
    public void deleteAmenities(int id){ amenitiesRepository.deleteById(id);}
    public Structure createStructure(Structure structure) {
        return structureRepository.save(structure);
    }

    public Optional<Structure>  getByIdStructure(int id){
        return structureRepository.findById(id);
    }

    public List<Structure> getStructureAll(){
        return structureRepository.findAll();
    }

    public void deleteStructure(int id){
        structureRepository.deleteById(id);
    }



    public Homestay createHomestay(HomestayDTO homestayDTO) {
        Homestay homestay = new Homestay();
        populateHomestayFields(homestay, homestayDTO);



            Homestay savedHomestay = homestayRepository.save(homestay);
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(30);

            while (!today.isAfter(endDate)) {
                HomestayAvailability availability = new HomestayAvailability();
                availability.setHomestay(savedHomestay);
                availability.setDate(Timestamp.valueOf(today.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime()));
                availability.setStatus("available");
                availability.setPricepernight(homestayDTO.getPricePerNight());

                homestayAvailabilityRepository.save(availability);
                today = today.plusDays(1);

            }


        return savedHomestay;




    }

    public void createBooking(BookingHomestayDTO bookingDTO) {
        HomestayAvailability availability = homestayAvailabilityRepository.findById(bookingDTO.getHomestayavailabilityId())
                .orElseThrow(() -> new ResourceNotFoundException("HomestayAvailability not found"));

        User user = userRepository.findById(bookingDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        long daysBetween = java.time.Duration.between(bookingDTO.getCheckin().toLocalDateTime(), bookingDTO.getCheckout().toLocalDateTime()).toDays();
        BigDecimal totalPrice = availability.getPricepernight().multiply(BigDecimal.valueOf(daysBetween));

        BookingHomestay booking = new BookingHomestay();
        booking.setHomestayavailability(availability);
        booking.setUser(user);
        booking.setCheckin(bookingDTO.getCheckin());
        booking.setCheckout(bookingDTO.getCheckout());
        booking.setFeeamount(bookingDTO.getFeeamount());
        booking.setStatus("Booked");
        booking.setNumberofguest(bookingDTO.getNumberofguest());
        booking.setTotalprice(totalPrice);
        booking.setBookingdate(Timestamp.valueOf(LocalDateTime.now()));

        bookingHomestayRepository.save(booking);
    }

    public Homestay updateHomestay(int id, HomestayDTO homestayDTO) {
        Homestay homestay = homestayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Homestay not found with id " + id));

        populateHomestayFields(homestay, homestayDTO);
        return homestayRepository.save(homestay);
    }


    public Optional<HomestayAvailability> getHomeAvailabilityById(int id){
        return homestayAvailabilityRepository.findById(id);
    }


    private void populateHomestayFields(Homestay homestay, HomestayDTO homestayDTO) {
        homestay.setUserid(homestayDTO.getUserId());
        homestay.setLongitude(homestayDTO.getLongitude());
        homestay.setLatitude(homestayDTO.getLatitude());
        homestay.setTitle(homestayDTO.getTitle());
        homestay.setAddress(homestayDTO.getAddress());
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(homestayDTO.getLongitude(), homestayDTO.getLatitude()));
        homestay.setGeom(point);
        homestay.setPhotos(homestayDTO.getPhotos());
        homestay.setDescription(homestayDTO.getDescription());
        homestay.setExtrainfo(homestayDTO.getExtraInfo());
        homestay.setCleaningfee(homestayDTO.getCleaningFee());
        homestay.setIsapproved(homestayDTO.getIsApproved());
        homestay.setMaxguest(homestayDTO.getMaxGuest());
        homestay.setType(homestayDTO.getType());


        CityProvince cityProvince = new CityProvince();
        cityProvince.setCityname(homestayDTO.getCityProvinceName());
        cityProvince = cityProvinceRepository.save(cityProvince);


        District district = new District();
        district.setDistrictname(homestayDTO.getDistrictName());
        district.setCityprovince(cityProvince);
        district = districtRepository.save(district);


        Ward ward = new Ward();
        ward.setWardname(homestayDTO.getWardName());
        ward.setDistrict(district);
        ward = wardRepository.save(ward);


        homestay.setWard(ward);


        Structure structure = structureRepository.findById(homestayDTO.getStructureId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "structure not found with id " + homestayDTO.getStructureId()));
        homestay.setStructure(structure);


        if (homestayDTO.getPerkIds() != null && !homestayDTO.getPerkIds().isEmpty()) {
            List<Amenities> amenities = amenitiesRepository.findAllById(homestayDTO.getPerkIds());
            homestay.setAmenities(amenities);
        } else {
            homestay.setAmenities(new ArrayList<>());
        }
    }




    public void  deleteHomestay(int id) {
        homestayRepository.deleteById(id);
    }


    public List<HomestayDTO> getAll() {
        List<Homestay> homestays = homestayRepository.findAll();
        return homestays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HomestayDTO convertToDTO(Homestay homestay) {
        HomestayDTO dto = new HomestayDTO();
        dto.setHomestayid(homestay.getHomestayid());
        dto.setStructureId(homestay.getStructure().getStructureid());
        dto.setUserId(homestay.getUserid());
        dto.setLongitude(homestay.getLongitude());
        dto.setLatitude(homestay.getLatitude());
        dto.setTitle(homestay.getTitle());
        dto.setAddress(homestay.getAddress());
        dto.setPhotos(homestay.getPhotos());
        dto.setDescription(homestay.getDescription());
        dto.setExtraInfo(homestay.getExtrainfo());
        dto.setCleaningFee(homestay.getCleaningfee());
        dto.setIsApproved(homestay.isIsapproved());
        dto.setMaxGuest(homestay.getMaxguest());

        dto.setPerkIds(homestay.getAmenities().stream()
                .map(Amenities::getAmenitiesid)
                .collect(Collectors.toList()));

        List<HomestayAvailabilityDTO> availabilityDTOs = homestay.getHomestayAvailabilityList().stream()
                .map(availability -> {
                    HomestayAvailabilityDTO availabilityDTO = new HomestayAvailabilityDTO();
                    availabilityDTO.setHomestayavailabilityid(availability.getHomestayavailabilityid());
                    availabilityDTO.setDate(availability.getDate());
                    availabilityDTO.setPricepernight(availability.getPricepernight());
                    availabilityDTO.setStatus(availability.getStatus());
                    return availabilityDTO;
                })
                .collect(Collectors.toList());

        dto.setAvailability(availabilityDTOs);

        return dto;
    }
    public HomestayDTO getById(int id) {
        Homestay homestay = homestayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homestay not found with id " + id));
        return convertToDTO(homestay);
    }

    public Homestay getHomestayById(int id){
        return homestayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homestay not found with id " + id));
    }

    public List<Homestay> searchHomestays(HomestaySearchDTO request) {
        request.setStatus(AvailabilityStatus.AVAILABLE);

        var checkinDate = request.getCheckinDate();
        var checkoutDate = request.getCheckoutDate();

//        if (request.getCheckinDate().isAfter(request.getCheckoutDate())) {
//            throw new BusinessException(ResponseCode.CHECKIN_DATE_INVALID);
//        }
//
//        if (request.getCheckinDate().isBefore(LocalDate.now())) {
//            throw new BusinessException(ResponseCode.CHECKIN_DATE_INVALID);
//        }


        int nights = (int) DateUtil.getDiffInDays(checkinDate, checkoutDate);
        checkoutDate = checkoutDate.minusDays(1);

        return homestayRepository.searchHomestay(
                request.getLongitude(),
                request.getLatitude(),
                request.getRadius(),
                checkinDate,
                checkoutDate,
                nights,
                request.getGuests(),
                request.getStatus().getValue()
        );
    }
}
