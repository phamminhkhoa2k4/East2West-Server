package com.east2west.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.ZoneId;

import com.east2west.models.DTO.*;
import com.east2west.models.Entity.*;
import com.east2west.models.enums.EHomestayStatus;
import com.east2west.models.enums.EStatusVerify;
import com.east2west.models.mapper.HomestayMapper;

import com.east2west.repository.*;
import com.east2west.util.DateUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Collectors;
import com.east2west.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.locationtech.jts.io.WKBWriter;

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


    public List<HomestayDTO> getHomestaysByStructureId(int structureId) {

        List<Homestay> homestays  =  homestayRepository.findByStructure_Structureid(structureId);
        return homestays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deletePhotos(String url, int id) {
        Optional<Homestay> homestay = homestayRepository.findById(id);
        if (homestay.isPresent()) {
            Homestay homestayy = homestay.get();
            List<String> photos = new ArrayList<>(homestayy.getPhotos()); // Tạo một bản sao có thể sửa đổi

            System.out.println("Before removing: " + photos);

            if (photos.contains(url)) {
                boolean removed = photos.remove(url);
                if (removed) {
                    System.out.println("Successfully removed: " + url);
                    homestayy.setPhotos(photos);
                    homestayRepository.save(homestayy);
                } else {
                    System.out.println("URL not found in photos.");
                }
            }
        }
    }


    public void approveHomestay(int homestayId) {
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(() -> new RuntimeException("Homestay not found with id: " + homestayId));

        homestay.setIsapproved(true);

        homestayRepository.save(homestay);
    }


    public HomestayDTO createHomestay(HomestayDTO homestayDTO) {

        homestayDTO.setIsApproved(false);
        Homestay homestay = new Homestay();
        populateHomestayFields(homestay, homestayDTO);
        if(homestayDTO.getPricePerNight() != null){
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(365);
            List<HomestayAvailability> homestayAvailabilityList = new ArrayList<>();
            while (!today.isAfter(endDate)) {
                HomestayAvailability availability = new HomestayAvailability();
                availability.setHomestay(homestayRepository.findById(homestayDTO.getHomestayid()).get());
                availability.setDate(Timestamp.valueOf(today.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime()));
                availability.setStatus("Available");
                availability.setPricepernight(homestayDTO.getPricePerNight());

                homestayAvailabilityList.add(availability);
                today = today.plusDays(1);

            }
            homestay.setHomestayAvailabilityList(homestayAvailabilityList);
        }
            Homestay response = homestayRepository.save(homestay);
        return HomestayMapper.INSTANCE.toDTO(response);
    }

    public List<HomestayDTO> search(String keyword,int userId) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return homestayRepository.findByUserid(userId).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        List<Homestay> homestays = homestayRepository.searchByUserIdAndKeyword(userId, keyword);
        return homestays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public void confirmBooking(int bookingId) {
        BookingHomestay booking = bookingHomestayRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStatus("Booked");


        bookingHomestayRepository.save(booking);
    }


    public void cancelBooking(int bookingId) {
        BookingHomestay booking = bookingHomestayRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));



        LocalDate checkinDate = booking.getCheckin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate checkoutDate = booking.getCheckout().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


        List<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDate = checkinDate;
        while (!currentDate.isAfter(checkoutDate)) {
            dateList.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        List<HomestayAvailability> availabilities = homestayAvailabilityRepository.findAll();


        for (HomestayAvailability availabiliti : availabilities) {
            LocalDate availabilityDate = availabiliti.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            if (dateList.contains(availabilityDate) && availabiliti.getHomestay().getHomestayid() == booking.getHomestayavailability().getHomestay().getHomestayid()) {
                availabiliti.setStatus("Available");

                homestayAvailabilityRepository.save(availabiliti);
            }
        }






            booking.setStatus("Available");


        bookingHomestayRepository.save(booking);
    }

    public void createBooking(BookingHomestayDTO bookingDTO) {
        HomestayAvailability availability = homestayAvailabilityRepository.findById(bookingDTO.getHomestayavailabilityId())
                .orElseThrow(() -> new ResourceNotFoundException("HomestayAvailability not found"));

        LocalDate checkinDate = bookingDTO.getCheckin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate checkoutDate = bookingDTO.getCheckout().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


        List<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDate = checkinDate;
        while (!currentDate.isAfter(checkoutDate)) {
            dateList.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        List<HomestayAvailability> availabilities = homestayAvailabilityRepository.findAll();


        for (HomestayAvailability availabiliti : availabilities) {
            LocalDate availabilityDate = availabiliti.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
          var s =   availability.getHomestay().getHomestayid();
            var a =  availabiliti.getHomestay().getHomestayid();
            System.out.println(s +  " " + a);
            if (dateList.contains(availabilityDate) && availabiliti.getHomestay().getHomestayid() == availability.getHomestay().getHomestayid()) {
                availabiliti.setStatus("Booked");

                homestayAvailabilityRepository.save(availabiliti);
            }
        }
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
        if(availability.getHomestay().getInstant()){
            booking.setStatus("Booked");

        }else{
            booking.setStatus("Pending");
        }
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
        if(homestayDTO.getHomestayid() != null){
            homestay.setHomestayid(homestayDTO.getHomestayid());
        }
        homestay.setUserid(homestayDTO.getUserId());
        if(homestayDTO.getLongitude() != null && homestayDTO.getLatitude() != null){
            homestay.setLongitude(homestayDTO.getLongitude());
            homestay.setLatitude(homestayDTO.getLatitude());
            GeometryFactory geometryFactory = new GeometryFactory();
            Point point = geometryFactory.createPoint(new Coordinate(homestayDTO.getLongitude(), homestayDTO.getLatitude()));
            homestay.setGeom(point);
        }
        if(homestayDTO.getStatus() != null){
            homestay.setStatus(homestayDTO.getStatus());
        }
        if(homestayDTO.getTitle() != null){
            homestay.setTitle(homestayDTO.getTitle());
        }
        if(homestayDTO.getAddress() != null){
            homestay.setAddress(homestayDTO.getAddress());
        }
        if(homestayDTO.getPhotos() != null){
            homestay.setPhotos(homestayDTO.getPhotos());
        }
        if(homestayDTO.getDescription() != null){
            homestay.setDescription(homestayDTO.getDescription());
        }
        if(homestayDTO.getExtraInfo() != null) {
            homestay.setExtrainfo(homestayDTO.getExtraInfo());
        }

        if(homestayDTO.getCleaningFee() != null){
            homestay.setCleaningfee(homestayDTO.getCleaningFee());
        }

        if(homestayDTO.getIsApproved() != null){
            homestay.setIsapproved(false);
        }


        if(homestayDTO.getAmenityIds() != null && !homestayDTO.getAmenityIds().isEmpty()){
            List<Amenities> amenities = amenitiesRepository.findByAmenitiesidIn(homestayDTO.getAmenityIds());
            homestay.setAmenities(amenities);
        }

        if(homestayDTO.getMaxGuest() != null){
            homestay.setMaxguest(homestayDTO.getMaxGuest());
        }
        homestay.setType(homestayDTO.getType());
        if(homestayDTO.getBathroom() != null){
            homestay.setBathroom(homestayDTO.getBathroom());
        }
        if(homestayDTO.getBeds() != null){
            homestay.setBeds(homestayDTO.getBeds());
        }

            homestay.setInstant(homestayDTO.getInstant());

        if(homestayDTO.getRoom() == null){
            homestay.setRoom(0);
        }else{
            homestay.setRoom(homestayDTO.getRoom());
        }


        if(homestayDTO.getWardName() != null && homestayDTO.getCityProvinceName() != null && homestayDTO.getDistrictName() != null){
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
        }

        if(homestayDTO.getStructureId() != null){
            Structure structure = structureRepository.findById(homestayDTO.getStructureId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "structure not found with id " + homestayDTO.getStructureId()));
            homestay.setStructure(structure);

        }
        if (homestayDTO.getAmenityIds() != null && !homestayDTO.getAmenityIds().isEmpty()) {
            List<Amenities> amenities = amenitiesRepository.findAllById(homestayDTO.getAmenityIds());
            homestay.setAmenities(amenities);
        }
    }


    public List<BookingHomestayDTO> listPendingBookingHomestays() {
        List<BookingHomestay> bookinghomestay = bookingHomestayRepository.findByStatus("Pending");
        return bookinghomestay.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


public HomestayDTO getHomestayHomestayAvailabilityById(int id){
    Optional<HomestayAvailability> ava = homestayAvailabilityRepository.findById(id);
    HomestayDTO homestay = convertToDTO(ava.get().getHomestay());
    return homestay;
}

    public HomestayAvailabilityDTO getHomestayAvailabilityById(int id){
        Optional<HomestayAvailability> ava = homestayAvailabilityRepository.findById(id);
        HomestayAvailabilityDTO homestay = convertToDTO(ava.get());
        return homestay;
    }



    public List<BookingHomestayDTO> listBookedBookingHomestays() {
        List<BookingHomestay> bookinghomestay = bookingHomestayRepository.findByStatus("Booked");
        return bookinghomestay.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public List<BookingHomestayDTO> listBookedBookingHomestaysId(int userId) {
        List<BookingHomestay> bookingHomestays = bookingHomestayRepository.findByStatusAndUserUserId("Booked", userId);
        return bookingHomestays.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    private HomestayAvailabilityDTO convertToDTO(HomestayAvailability homestay) {
        HomestayAvailabilityDTO dto = new HomestayAvailabilityDTO();
        dto.setDate(homestay.getDate());
        dto.setStatus(homestay.getStatus());
        dto.setHomestayid(homestay.getHomestay().getHomestayid());
        dto.setHomestayavailabilityid(homestay.getHomestayavailabilityid());
        dto.setPricepernight(homestay.getPricepernight());
        return dto;
    }

    private BookingHomestayDTO convertToDTO(BookingHomestay homestay) {
        BookingHomestayDTO dto = new BookingHomestayDTO();
        dto.setHomestayavailabilityId(homestay.getHomestayavailability().getHomestayavailabilityid());
        dto.setUserId(homestay.getUser().getUserId());
        dto.setCheckin(homestay.getCheckin());
        dto.setCheckout(homestay.getCheckout());
        dto.setBookingdate(homestay.getBookingdate());
        dto.setFeeamount(homestay.getFeeamount());
        dto.setStatus(homestay.getStatus());
        dto.setNumberofguest(homestay.getNumberofguest());
        dto.setTotalPrice(homestay.getTotalprice());
        dto.setBookinghomestayid(homestay.getBookinghomestayid());
        return dto;
    }



    public String deleteHomestay(int id){

        Optional<Homestay> homestay = homestayRepository.findById(id);
        if(homestay.isPresent()){
            List<HomestayAvailability>  ava =  homestayAvailabilityRepository.findByHomestay_Homestayid(id);
            homestayAvailabilityRepository.deleteAll(ava);
            homestayRepository.deleteById(id);
            return "Deleted " + homestay.get().getTitle() + " homestay successfully";
        }else{
            return "Not found homestay";
        }
    }

    public List<HomestayDTO> getAllHomestayStatus(int userId , EHomestayStatus status) {
        List<Homestay> homestays = homestayRepository.findByUseridAndStatus(userId, status);
        return homestays.stream().map(HomestayMapper.INSTANCE::toDTO).toList();
    }



    public List<HomestayDTO> getAllApproved() {
        List<Homestay> homestays = homestayRepository.findAll();
        return homestays.stream()
                .filter(Homestay::isIsapproved)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HomestayDTO> getAll() {
        List<Homestay> homestays = homestayRepository.findAll();
        return homestays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HomestayDTO> filterHomestays(HomestayFilterDTO filterDTO) {
        List<Homestay>  homestays =  homestayRepository.findByFilter(
                filterDTO.getMinBeds(),
                filterDTO.getMaxBeds(),
                filterDTO.getMinMaxGuest(),
                filterDTO.getMaxMaxGuest(),
                filterDTO.getType(),
                filterDTO.getAmenityIds()
        );

        return homestays.stream()
                .filter(Homestay::isIsapproved)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HomestayDTO> getAllByIdUser(int id) {
        List<Homestay> homestays = homestayRepository.findByUserid(id);
        return homestays.stream()
                .map(HomestayMapper.INSTANCE::toDTO)
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
        dto.setRoom(homestay.getRoom());
        dto.setType(homestay.getType());
        dto.setBeds(homestay.getBeds());
        dto.setBathroom(homestay.getBathroom());
        dto.setInstant(homestay.getInstant());
        dto.setAmenityIds(homestay.getAmenities().stream()
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

        dto.setWardName(homestay.getWard().getWardname());
        dto.setDistrictName(homestay.getWard().getDistrict().getDistrictname());
        dto.setCityProvinceName(homestay.getWard().getDistrict().getCityprovince().getCityname());
        // TODO: Check data type here
//        if (homestay.getGeom() != null) {
//            WKBWriter wkbWriter = new WKBWriter();
//            byte[] wkb = wkbWriter.write(homestay.getGeom());
//            String wkbHex = bytesToHex(wkb);
//            dto.setGeom(wkbHex);
//        }

//                if (homestay.getGeom() != null) {
//            try {
//                WKTReader wktReader = new WKTReader();
//                Geometry geometry = wktReader.read(homestay.getGeom());
//
//                WKBWriter wkbWriter = new WKBWriter();
//                byte[] wkb = wkbWriter.write(geometry);
//                String wkbHex = bytesToHex(wkb);
//                dto.setGeom(wkbHex);
//            } catch (ParseException e) {
//
//                System.err.println("Error parsing WKT for geometry: " + e.getMessage());
//
//            }
//        }
        return dto;
    }
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    public HomestayDTO getById(int id) {
        Homestay homestay = homestayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homestay not found with id " + id));
        return convertToDTO(homestay);
    }



    public Optional<HomestayDTO> getHomestayById(int id){
        Optional<Homestay> homestay = homestayRepository.findById(id);
        if(homestay.isEmpty()) return Optional.empty();
        HomestayDTO response = HomestayMapper.INSTANCE.toDTO(homestay.get());
        return Optional.ofNullable(response);
    }


    public void updateBasePrice(int homestayId, BigDecimal newPrice) {
        homestayAvailabilityRepository.updateBasePrice(homestayId, newPrice);
    }


    public void updateWeekendPrice(int homestayId, BigDecimal newPrice) {
        homestayAvailabilityRepository.updateWeekendPrice(homestayId, newPrice);
    }


    public BigDecimal getMinPriceForToday() {
        LocalDate today = LocalDate.now();
        return homestayAvailabilityRepository.findMinPriceByDate(today);
    }

    public BigDecimal getMaxPriceForToday() {
        LocalDate today = LocalDate.now();
        return homestayAvailabilityRepository.findMaxPriceByDate(today);
    }

    public List<HomestayDTO> searchHomestays(HomestaySearchDTO request) {


        var checkinDate = request.getCheckinDate();
        var checkoutDate = request.getCheckoutDate();



        int nights = (int) DateUtil.getDiffInDays(checkinDate, checkoutDate);
        checkoutDate = checkoutDate.minusDays(1);

        List<Homestay> homestays = homestayRepository.searchHomestay(
                request.getLongitude(),
                request.getLatitude(),
                request.getRadius(),
                checkinDate,
                checkoutDate,
                nights,
                request.getGuests(),
                request.getStatus()

        );
        return homestays.stream()
                .filter(Homestay::isIsapproved)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Homestay> findByHomestaysIdUserId(int homestayid,int userid) {
        return homestayRepository.findByHomestayidAndUserid(homestayid,userid);
    }

    public HomestayDTO activateHomestays(Homestay homestay, EStatusVerify status) {
        if(status == EStatusVerify.VERIFYED){
            homestay.setStatus(EHomestayStatus.APPROVED);
            return HomestayMapper.INSTANCE.toDTO(homestayRepository.save(homestay));
        }

        return  null;
    }
}