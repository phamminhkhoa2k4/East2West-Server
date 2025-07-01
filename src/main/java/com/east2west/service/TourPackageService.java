package com.east2west.service;

import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.models.Entity.*;
import com.east2west.models.mapper.*;
import com.east2west.models.payload.request.ActivityOrderRequest;
import com.east2west.models.payload.request.ItineraryRequest;
import com.east2west.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TourPackageService {
    private final TourPackageRepository tourPackageRepository;

    private final ThemeRepository themeRepository;

    private final SuitableRepository suitableRepository;

    private final CategoryRepository categoryRepository;


    private final PlaceRepository placeRepository;


    private final AccommodationRepository accommodationRepository;

    private final TransferRepository transferRepository;

    private final MealRepository mealRepository;

    public TourPackageService(TourPackageRepository tourPackageRepository, ThemeRepository themeRepository, SuitableRepository suitableRepository, CategoryRepository categoryRepository, PlaceRepository placeRepository, AccommodationRepository accommodationRepository, TransferRepository transferRepository, MealRepository mealRepository) {
        this.tourPackageRepository = tourPackageRepository;
        this.themeRepository = themeRepository;
        this.suitableRepository = suitableRepository;
        this.categoryRepository = categoryRepository;
        this.placeRepository = placeRepository;
        this.accommodationRepository = accommodationRepository;
        this.transferRepository = transferRepository;
        this.mealRepository = mealRepository;
    }

//
//
//    @Autowired
//    private ItineraryRepository itineraryRepository;
//
//
//    @Autowired
//    private AccommodationRepository accommodationRepository;
//
//    @Autowired
//    private MealRepository mealRepository;
//
//    @Autowired
//    private PlaceRepository placeRepository;
//
//    @Autowired
//    private TransferRepository transferRepository;
//
//    public List<TourPackage> getAllTourpackages() {
//        return tourPackageRepository.findAll();
//    }
//
//    public List<Category> getAllTourPackagesCategory() {
//        return categoryTourRepository.findAll();
//    }
//
//    public List<TourPackage> getAllTourPackagesByCategory(String cat) {
//        return tourPackageRepository.findByCategoryTourName(cat);
//    }
//
//    public List<Theme> getAllTourPackagesTheme() {
//        return ThemeTourRepository.findAll();
//    }
//
//    public List<TourPackage> getToursByThemeTourName(String themeTourName) {
//        return tourPackageRepository.findByThemeTourName(themeTourName);
//    }
//
//
//    public List<Suitable> getAllTourPackagesSuitable() {
//        return suitableRepository.findAll();
//    }
//
//    public List<TourPackage> getToursBySuitableName(String suitableName) {
//        return tourPackageRepository.findBySuitableName(suitableName);
//    }
//
//    public List<DepartureDate> getAllDepartureDate() {
//        return departureDateRepository.findAll();
//    }



   public Optional<TourPackage> findByTitle(String title) {
        return tourPackageRepository.findByTitle(title);
    }

    public TourPackageDTO createTour(TourPackageDTO tourPackageDTO) {

        Set<Theme> themes = tourPackageDTO.getThemes().stream()
                .map(dto -> themeRepository.findById(dto.getThemeId()).orElseThrow())
                .collect(Collectors.toSet());

        Set<Suitable> suitable = tourPackageDTO.getSuitable().stream()
                .map(dto -> suitableRepository.findById(dto.getSuitableId()).orElseThrow())
                .collect(Collectors.toSet());


        Set<Category> categories = tourPackageDTO.getCategories().stream()
                .map(dto -> categoryRepository.findById(dto.getCategoryId()).orElseThrow())
                .collect(Collectors.toSet());

        TourPackage.TourPackageBuilder tourBuilder = TourPackage.builder()
                .price(tourPackageDTO.getPrice())
                .title(tourPackageDTO.getTitle())
                .thumbnail(tourPackageDTO.getThumbnail())
                .groupsize(tourPackageDTO.getGroupsize())
                .deposit(tourPackageDTO.getDeposit())
                .categories(categories)
                .themes(themes)
                .suitable(suitable)
                .departuredates(tourPackageDTO.getDeparturedates().stream().map(DepartureDateMapper.INSTANCE::toEntity).collect(Collectors.toList()));

        TourPackage tourPackage = tourBuilder.build();
        List<Itinerary> itineraries =  tourPackageDTO.getItineraries().stream().map(itineraryDTO -> {
            Itinerary itinerary = new Itinerary();
            itinerary.setDay(itineraryDTO.getDay());
            itinerary.setTourPackage(tourPackage);
            List<ActivityOrder> activityOrders = itineraryDTO.getActivityOrders().stream().map(orderDTO -> ActivityOrder.builder()
                      .activitytype(orderDTO.getActivitytype())
                      .refid(orderDTO.getRefid())
                      .sortorder(orderDTO.getSortorder())
                      .itinerary(itinerary)
                      .build()).toList();
            itinerary.setActivityOrders(activityOrders);
            return itinerary;
        }).toList();
        tourPackage.setItineraries(itineraries);


        return TourPackageMapper.INSTANCE.toDTO(tourPackageRepository.save(tourPackage));

    }


    public TourPackageDTO  updateTour(TourPackageDTO tourPackageDTO){

        TourPackage tourPackage = tourPackageRepository.findById(tourPackageDTO.getPackageid())
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with id: " + tourPackageDTO.getPackageid()));

        Set<Theme> themes = tourPackageDTO.getThemes().stream()
                .map(dto -> themeRepository.findById(dto.getThemeId()).orElseThrow())
                .collect(Collectors.toSet());

        Set<Suitable> suitable = tourPackageDTO.getSuitable().stream()
                .map(dto -> suitableRepository.findById(dto.getSuitableId()).orElseThrow())
                .collect(Collectors.toSet());


        Set<Category> categories = tourPackageDTO.getCategories().stream()
                .map(dto -> categoryRepository.findById(dto.getCategoryId()).orElseThrow())
                .collect(Collectors.toSet());


        List<Itinerary> itineraries =  tourPackageDTO.getItineraries().stream().map(itineraryDTO -> {
            Itinerary itinerary = new Itinerary();
            itinerary.setItineraryId(itineraryDTO.getItineraryId());
            itinerary.setDay(itineraryDTO.getDay());
            itinerary.setTourPackage(tourPackage);
            List<ActivityOrder> activityOrders = itineraryDTO.getActivityOrders().stream().map(orderDTO -> {
                ActivityOrder activityOrder = new ActivityOrder();
                activityOrder.setActivityorderid(orderDTO.getActivityorderid());
                activityOrder.setActivitytype(orderDTO.getActivitytype());
                activityOrder.setRefid(orderDTO.getRefid());
                activityOrder.setSortorder(orderDTO.getSortorder());
                activityOrder.setItinerary(itinerary);
                return activityOrder;
            }).toList();
            itinerary.setActivityOrders(activityOrders);
            return itinerary;
        }).toList();

        TourPackage tour = TourPackage.builder()
                .packageid(tourPackageDTO.getPackageid())
                .title(tourPackageDTO.getTitle())
                .price(tourPackageDTO.getPrice())
                .deposit(tourPackageDTO.getDeposit())
                .groupsize(tourPackageDTO.getGroupsize())
                .thumbnail(tourPackageDTO.getThumbnail())
                .themes(themes)
                .suitable(suitable)
                .categories(categories)
                .departuredates(tourPackageDTO.getDeparturedates().stream().map(DepartureDateMapper.INSTANCE::toEntity).collect(Collectors.toList()))
                .itineraries(itineraries)
                .build();


        return TourPackageMapper.INSTANCE.toDTO(tourPackageRepository.save(tour));
    }



    public Page<TourPackageDTO> getAllCars(Pageable pageable) {
        Page<TourPackage>  tourPackagePage = tourPackageRepository.findAll(pageable);
        return tourPackagePage.map(TourPackageMapper.INSTANCE::toDTO);
    }



    public List<TourPackageDTO> searchTour(String keyword) {
        List<TourPackage> tour = tourPackageRepository.findByTitleContainingIgnoreCase(keyword);
        return tour.stream().map(TourPackageMapper.INSTANCE::toDTO).toList();
    }

    public String deleteTour(int id){

        Optional<TourPackage> tour = tourPackageRepository.findById(id);
        if(tour.isPresent()){
            tourPackageRepository.deleteById(id);
            return "Deleted " + tour.get().getTitle() + " tour package successfully";
        }else{
            return "Not found tour";
        }
    }


    public Optional<TourPackageDTO> getTourById(int id) {
        Optional<TourPackage> tour = tourPackageRepository.findById(id);
        if (tour.isPresent()){
            TourPackageDTO tourDTO  = TourPackageMapper.INSTANCE.toDTO(tour.get());
            return Optional.ofNullable(tourDTO);
        }
        return Optional.empty();
    }



    public String saveTourFromCSV(@NotNull MultipartFile[] files) {
        try {
            List<TourPackage> toursList = new ArrayList<>();
            Optional<TourPackage> maxIdTour = tourPackageRepository.findAll().stream()
                    .max(Comparator .comparingInt(TourPackage::getPackageid));
            int idCounter = maxIdTour.map(tour -> tour.getPackageid() + 1)
                    .orElse(1);

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                String line;
                int lineNumber = 0;
                Map<String, Integer> headerMap = new HashMap<>();

                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    if (lineNumber == 1) {

                        String[] headers = line.split(",");
                        for (int i = 0; i < headers.length; i++) {
                            headerMap.put(headers[i].trim(), i);
                        }
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data.length < headerMap.size()) {
                        continue;
                    }



                    String Title = getValueByHeader(data, headerMap, "Title");
                    String Price = getValueByHeader(data, headerMap, "Price");
                    String Deposit = getValueByHeader(data, headerMap, "Deposit");
                    String GroupSize = getValueByHeader(data, headerMap, "GroupSize");
                    List<String> CategoryName = Arrays.asList(getValueByHeader(data, headerMap, "CategoryName").replace("|",",").split(","));
                    List<String>  ThemeName = Arrays.asList(getValueByHeader(data, headerMap, "ThemeName").replace("|",",").split(","));
                    List<String>  SuitableName = Arrays.asList(getValueByHeader(data, headerMap, "SuitableName").replace("|",",").split(","));
                    List<String> Thumbnails = Arrays.asList(getValueByHeader(data, headerMap, "Thumbnails").replace("|",",").split(","));
                    String DepartureDatesRaw = normalizeJsonFromCsv(getValueByHeader(data, headerMap, "DepartureDates")).replace("\"priceoverride\":\"Default\"", "\"priceoverride\":" + Price);;
                    String  ItineraryRaw = normalizeJsonFromCsv(getValueByHeader(data, headerMap,"Itinerary"));

                    ObjectMapper mapper = new ObjectMapper();

                    List<ItineraryRequest> ItineraryRequests  = mapper.readValue(
                            ItineraryRaw,
                            new TypeReference<>() {
                            }
                    );





                    List<DepartureDate> DepartureDates = mapper.readValue(
                            DepartureDatesRaw,
                            new TypeReference<>() {
                            }
                    );



                    Set<Category> Categories = categoryRepository.findByCategoryNameIn(CategoryName);
                    if(Categories.isEmpty()){
                        return "Line: " + lineNumber + " column: " +
                                findHeaderName(headerMap, "CategoryName") +
                                " - Categories are invalid !!!";
                    }
                    Set<Theme> Themes = themeRepository.findByThemeNameIn(ThemeName);
                    if(Themes.isEmpty()){
                        return "Line: " + lineNumber + " column: " +
                                findHeaderName(headerMap, "ThemeName") +
                                " - Themes are invalid !!!";
                    }
                    Set<Suitable> Suitable = suitableRepository.findBySuitableNameIn(SuitableName);
                    if(Themes.isEmpty()){
                        return "Line: " + lineNumber + " column: " +
                                findHeaderName(headerMap, "SuitableName") +
                                " - Suitable are invalid !!!";
                    }





                    TourPackage tour = TourPackage.builder()
                            .packageid(idCounter++)
                            .title(Title)
                            .price(new BigDecimal(Price))
                            .deposit(Deposit)
                            .groupsize(GroupSize)
                            .thumbnail(Thumbnails)
                            .categories(Categories)
                            .themes(Themes)
                            .suitable(Suitable)
                            .departuredates(DepartureDates)

                            .build();


                    List<Itinerary> Itineraries = new ArrayList<>();

                    for (ItineraryRequest itineraryRequest : ItineraryRequests) {
                        Itinerary itinerary = new Itinerary();
                        itinerary.setDay(itineraryRequest.getDay());
                        itinerary.setTourPackage(tour);

                        List<ActivityOrder> activityOrders = new ArrayList<>();
                        for (ActivityOrderRequest activityOrderRequest : itineraryRequest.getActivityOrders()) {
                            ActivityOrder activityOrder = new ActivityOrder();

                            switch (activityOrderRequest.getActivitytype()) {
                                case PLACE -> {
                                    Optional<Place> place = placeRepository.findByPlacename(activityOrderRequest.getActivityname());
                                    if (place.isEmpty()) {
                                        return "Line: " + lineNumber + " column: " +
                                                findHeaderName(headerMap, "Itinerary") +
                                                " - PlaceName at Day " + itineraryRequest.getDay() + " is invalid !!!";
                                    }
                                    activityOrder.setRefid((long) place.get().getPlaceid());
                                }
                                case ACCOMMODATION -> {
                                    Optional<Accommodation> acc = accommodationRepository.findByAccommodationname(activityOrderRequest.getActivityname());
                                    if (acc.isEmpty()) {
                                        return "Line: " + lineNumber + " column: " +
                                                findHeaderName(headerMap, "Itinerary") +
                                                " - AccommodationName at Day " + itineraryRequest.getDay() + " is invalid !!!";
                                    }
                                    activityOrder.setRefid((long) acc.get().getAccommodationid());
                                }
                                case MEAL -> {
                                    Optional<Meal> meal = mealRepository.findByMealname(activityOrderRequest.getActivityname());
                                    if (meal.isEmpty()) {
                                        return "Line: " + lineNumber + " column: " +
                                                findHeaderName(headerMap, "Itinerary") +
                                                " - MealName at Day " + itineraryRequest.getDay() + " is invalid !!!";
                                    }
                                    activityOrder.setRefid((long) meal.get().getMealid());
                                }
                                case TRANSFER -> {
                                    Optional<Transfer> transfer = transferRepository.findByTransfername(activityOrderRequest.getActivityname());
                                    if (transfer.isEmpty()) {
                                        return "Line: " + lineNumber + " column: " +
                                                findHeaderName(headerMap, "Itinerary") +
                                                " - TransferName at Day " + itineraryRequest.getDay() + " is invalid !!!";
                                    }
                                    activityOrder.setRefid((long) transfer.get().getTransferid());
                                }
                                default -> {
                                    return "Line: " + lineNumber + " column: " +
                                            findHeaderName(headerMap, "Itinerary") +
                                            " - ActivityType at Day " + itineraryRequest.getDay() + " is invalid !!!";
                                }
                            }
                            activityOrder.setItinerary(itinerary);
                            activityOrder.setActivitytype(activityOrderRequest.getActivitytype());
                            activityOrder.setSortorder(activityOrderRequest.getSortorder());

                            activityOrders.add(activityOrder);
                        }

                        itinerary.setActivityOrders(activityOrders);
                        Itineraries.add(itinerary);
                    }

                    tour.setItineraries(Itineraries);

                    toursList.add(tour);
                }
            }
            tourPackageRepository.saveAll(toursList);
            return "" + toursList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }

    public String normalizeJsonFromCsv(String input) {
        if (input.startsWith("\"") && input.endsWith("\"")) {
            input = input.substring(1, input.length() - 1);
        }
        input = input.replace("\"\"", "\"");

        input = input.replace("|", ",");

        return input;
    }
    private String getValueByHeader(String[] data, Map<String, Integer> headerMap, String headerName) {
        Integer index = headerMap.get(headerName);
        if (index != null && index < data.length) {
            return data[index].trim();
        }
        return "";
    }
    private String findHeaderName(Map<String, Integer> headerMap, String defaultName) {
        for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
            if (entry.getKey().trim().equalsIgnoreCase(defaultName.trim())) {
                return entry.getKey();
            }
        }
        return defaultName;
    }

//    public TourPackageDetailDTO getTourDetailByPackageid(int packageid) {
//        TourPackage tourPackage = tourPackageRepository.findByPackageid(packageid);
//        List<Itinerary> itineraries = tourPackage.getItineraries();

//        TourPackageDetailDTO dto = new TourPackageDetailDTO();
//        dto.setPackageid(tourPackage.getPackageid());
//        dto.setTitle(tourPackage.getTitle());
//        dto.setThumbnail(tourPackage.getThumbnail());
//        dto.setPrice(tourPackage.getPrice());
//        dto.setPricereduce(tourPackage.getPricereduce());
//        dto.setGroupsize(tourPackage.getGroupsize());
//        dto.setDeposit(tourPackage.getDeposit());
//        dto.setItineraries(itineraries);
//        dto.setCategoryTours(tourPackage.getCategoryTours());
//        dto.setThemeTours(tourPackage.getThemes());
//        dto.setDepartureDates(tourPackage.getDepartureDates());
//        dto.setSuitableTours(tourPackage.getSuitableTours());
//        return dto;
//    }

//    public TourPackage getTourPackageByPackageid(int id) {
//        return tourPackageRepository.findByPackageid(id);
//    }

//    public Optional<TourPackage> findById(int id) {
//        return tourPackageRepository.findById(id);
//    }

//    public void save(TourPackage tourPackage) {
//        tourPackageRepository.save(tourPackage);
//    }

//    public boolean existsByTitle(String title) {
//        return tourPackageRepository.existsByTitle(title);
//    }

//    public TourPackage updateTourPackageFields(TourPackage tourPackage, TourPackageDTO tourPackageDTO) {
//        tourPackage.setTitle(tourPackageDTO.getTitle());
//        tourPackage.setThumbnail(tourPackageDTO.getThumbnail());
//        tourPackage.setPrice(tourPackageDTO.getPrice());
//        tourPackage.setPricereduce(tourPackageDTO.getPricereduce());
//        tourPackage.setGroupsize(tourPackageDTO.getGroupsize());
//        tourPackage.setDeposit(tourPackageDTO.getDeposit());

    // Map CategoryTours
//        tourPackage.getCategoryTours().clear();
//        List<Category> categoryTours = categoryTourRepository.findAllById(tourPackageDTO.getCategoryTourId());
//        tourPackage.setCategoryTours(categoryTours);

    // Map ThemeTours
//        tourPackage.getThemeTours().clear();
//        List<Theme> themeTours = themeTourRepository.findAllById(tourPackageDTO.getThemeTourId());
//        tourPackage.setThemeTours(themeTours);

    // Map SuitableTours
//        tourPackage.getSuitableTours().clear();
//        List<Suitable> suitableTours = suitableRepository.findAllById(tourPackageDTO.getSuitableTourId());
//        tourPackage.setSuitableTours(suitableTours);

    // Map DepartureDates
//        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
//                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
//                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
//                .toFormatter();
//
//        tourPackage.getCategoryTours().clear();
//        List<DepartureDate> existingDepartureDates = new ArrayList<>();

//        for (TourPackageDTO.DepartureDateDTO departureDateDTO : tourPackageDTO.getDepartureDates()) {
//            String dt = departureDateDTO.getDateTime();
//            try {
//                LocalDateTime localDateTime = LocalDateTime.parse(dt, formatter);
//                Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
//                Timestamp timestamp = Timestamp.from(instant);
//                Optional<DepartureDate> departureDateOpt = departureDateRepository.findByDeparturedate(timestamp);
//                DepartureDate departureDate = departureDateOpt.orElseGet(() -> {
//                    DepartureDate newDepartureDate = new DepartureDate();
//                    newDepartureDate.setDeparturedate(timestamp);
//                    departureDateRepository.save(newDepartureDate);
//                    return newDepartureDate;
//                });
//                existingDepartureDates.add(departureDate);
//            } catch (DateTimeParseException dtpe) {
//                throw new RuntimeException("Invalid date format for departure date: " + dt, dtpe);
//            }
//        }
//
//        tourPackage.setDepartureDates(existingDepartureDates);
//
//        return tourPackage;
//    }
//
//    public TourPackage createTour(TourPackageDTO tourPackageDTO) {
//        if (tourPackageDTO.getTitle() == null || tourPackageDTO.getTitle().trim().isEmpty()) {
//            throw new IllegalArgumentException("Title cannot be empty");
//        }
//
//        if (tourPackageDTO.getGroupsize() == null || tourPackageDTO.getDeposit() == null ||
//                tourPackageDTO.getBookinghold() == null || tourPackageDTO.getBookingchange() == null) {
//            throw new IllegalArgumentException("Groupsize, deposit, booking hold, and booking change cannot be null");
//        }
//
//        if (tourPackageRepository.existsByTitle(tourPackageDTO.getTitle())) {
//            throw new IllegalArgumentException("A tour package with this title already exists");
//        }
//
//        TourPackage tourPackage = new TourPackage();
//        tourPackage.setTitle(tourPackageDTO.getTitle());
//        tourPackage.setThumbnail(tourPackageDTO.getThumbnail());
//        tourPackage.setPrice(tourPackageDTO.getPrice());
//        tourPackage.setGroupsize(tourPackageDTO.getGroupsize());
//        tourPackage.setDeposit(tourPackageDTO.getDeposit());
//
//        List<Category> categoryTours = categoryTourRepository.findAllById(tourPackageDTO.getCategoryTourId());
//        tourPackage.setCategoryTours(categoryTours);
//
//        List<Theme> themeTours = themeTourRepository.findAllById(tourPackageDTO.getThemeTourId());
////        tourPackage.setThemeTours(themeTours);
//
//        List<Suitable> suitableTours = suitableRepository.findAllById(tourPackageDTO.getSuitableTourId());
////        tourPackage.setSuitableTours(suitableTours);
//
//        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
//                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
//                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
//                .toFormatter();
//        List<DepartureDate> existingDepartureDates = new ArrayList<>();
//
//        for (DepartureDateDTO departureDateDTO : tourPackageDTO.getDepartureDates()) {
//            String dt = departureDateDTO.getDateTime();
//            try {
//                LocalDateTime localDateTime = LocalDateTime.parse(dt, formatter);
//                Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
//                Timestamp timestamp = Timestamp.from(instant);
//                Optional<DepartureDate> departureDateOpt = departureDateRepository.findByDeparturedate(timestamp);
//                DepartureDate departureDate = departureDateOpt.orElseGet(() -> {
//                    DepartureDate newDepartureDate = new DepartureDate();
//                    newDepartureDate.setDeparturedate(timestamp);
//                    departureDateRepository.save(newDepartureDate);
//                    return newDepartureDate;
//                });
//                existingDepartureDates.add(departureDate);
//            } catch (DateTimeParseException dtpe) {
//                throw new RuntimeException("Invalid date format for departure date: " + dt, dtpe);
//            }
//        }
//
//        tourPackage.setDepartureDates(existingDepartureDates);
//
//        TourPackage savedTourPackage = tourPackageRepository.save(tourPackage);
//
//        List<Itinerary> itineraries = new ArrayList<>();
//
//        for (ItineraryDTO itineraryDTO : tourPackageDTO.getItineraries()) {
//            Itinerary itinerary = new Itinerary();
////            itinerary.setAccommodations(new ArrayList<>());
////            itinerary.setMeals(new ArrayList<>());
////            itinerary.setPlaces(new ArrayList<>());
////            itinerary.setTransfers(new ArrayList<>());
//
//            itinerary.setTourPackage(savedTourPackage);
//
////            if (itineraryDTO.getAccommodationIds() != null && !itineraryDTO.getAccommodationIds().isEmpty()) {
////                List<Accommodation> accommodations = accommodationRepository.findAllById(itineraryDTO.getAccommodationIds());
////                itinerary.getAccommodations().addAll(accommodations);
////            }
//
////            if (itineraryDTO.getMealIds() != null && !itineraryDTO.getMealIds().isEmpty()) {
////                List<Meal> meals = mealRepository.findAllById(itineraryDTO.getMealIds());
////                itinerary.getMeals().addAll(meals);
////            }
//
////            if (itineraryDTO.getPlaceIds() != null && !itineraryDTO.getPlaceIds().isEmpty()) {
////                List<Place> places = placeRepository.findAllById(itineraryDTO.getPlaceIds());
////                itinerary.getPlaces().addAll(places);
////            }
//
////            if (itineraryDTO.getTransferIds() != null && !itineraryDTO.getTransferIds().isEmpty()) {
////                List<Transfer> transfers = transferRepository.findAllById(itineraryDTO.getTransferIds());
////                itinerary.getTransfers().addAll(transfers);
////            }
//
//            itinerary.setDay(itineraryDTO.getDay());
//
//            // Save each Itinerary
//            itineraries.add(itineraryRepository.save(itinerary));
//        }
//
//
//        return savedTourPackage;
//    }
//
//
//    public TourPackage updateTour(TourPackageDTO tourPackageDTO) {
//        Integer id = tourPackageDTO.getId();
//        if (id == null || !tourPackageRepository.existsById(id)) {
//            throw new ResourceNotFoundException("TourPackage not found with id " + id);
//        }
//
//        Optional<TourPackage> existingTourOpt = tourPackageRepository.findById(id);
//        if (existingTourOpt.isPresent()) {
//            TourPackage tourPackage = existingTourOpt.get();
//
//            if (!tourPackage.getTitle().equals(tourPackageDTO.getTitle())) {
//                 Check for duplicate title
//                boolean exists = tourPackageRepository.existsByTitle(tourPackageDTO.getTitle());
//                if (exists) {
//                    throw new IllegalArgumentException("Title already exists");
//                }
//            }
//
//            updateTourPackageFields(tourPackage, tourPackageDTO);
//            return tourPackageRepository.save(tourPackage);
//        } else {
//            throw new ResourceNotFoundException("TourPackage not found with id " + id);
//        }
//    }
//
//    public boolean deleteTour(int id) {
//        Optional<TourPackage> tourPackageOpt = tourPackageRepository.findById(id);
//        if (tourPackageOpt.isPresent()) {
//            TourPackage tourPackage = tourPackageOpt.get();
//
//            List<Itinerary> itineraries = itineraryRepository.findByTourPackage_Packageid(id);
//
//
//            for (Itinerary itinerary : itineraries) {
//                itinerary.getAccommodations().clear();
//                itinerary.getMeals().clear();
//                itinerary.getPlaces().clear();
//                itinerary.getTransfers().clear();
//
//                itineraryRepository.delete(itinerary);
//            }
//
//            tourPackageRepository.deleteById(id);
//
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    public BookingTour saveBookingTour(BookingTourDTO bookingTourDTO) {
//        BookingTour bookingTour = new BookingTour();
//
//         Retrieve TourPackage
//        TourPackage tourPackage = tourPackageRepository.findById(bookingTourDTO.getPackageId())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Tour Package not found with id " + bookingTourDTO.getPackageId()));
//
//         Retrieve Payment
//        Payment payment = paymentRepository.findById(bookingTourDTO.getPaymentId())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Payment not found with id " + bookingTourDTO.getPaymentId()));
//
//         Set booking details
//        bookingTour.setUserid(bookingTourDTO.getUserId());
//        bookingTour.setTourpackage(tourPackage);
//        bookingTour.setPayment(payment);
//        LocalDateTime now = LocalDateTime.now();
//        Timestamp timestamp = Timestamp.valueOf(now);
//        bookingTour.setBookingdate(timestamp);
//        bookingTour.setTourdate(convertToTimestamp(bookingTourDTO.getTourDate()));
//        bookingTour.setNumberofpeople(bookingTourDTO.getNumberOfPeople());
//        bookingTour.setTotalprice(bookingTourDTO.getTotalPrice());
//        bookingTour.setDepositamount(bookingTourDTO.getDepositAmount());
//        bookingTour.setStatus("Waiting");
//        bookingTour.setRefundamount(null);
//        bookingTour.setRefunddate(null);
//        bookingTour.setReason(null);
//        bookingTour.setDepositrefund(false);
//
//         Save and return booking
//        return bookingTourRepository.save(bookingTour);
//    }
//
//    public Timestamp convertToTimestamp(Date date) {
//        return new Timestamp(date.getTime());
//    }
//    public String cancelRefund(int id) {
//        BookingTour bookingTour = bookingTourRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "bookingTourId not found with id " + id));
//        bookingTour.setRefundamount(null);
//        bookingTour.setRefunddate(null);
//        bookingTour.setStatus("Waiting");
//        bookingTour.setReason("");
//        bookingTour.setDepositrefund(false);
//        bookingTourRepository.save(bookingTour);
//        return "Cancel Refund ";
//    }
//    public String cancelBooking(CancelDTO cancelDTO) {
//        BookingTour bookingTour = bookingTourRepository.findById(cancelDTO.getBookingTourId())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "bookingTourId not found with id " + cancelDTO.getBookingTourId()));
//
//        long daysBeforeTour = ChronoUnit.DAYS.between(LocalDate.now(),
//                bookingTour.getTourdate().toLocalDateTime().toLocalDate());
//
//        BigDecimal refundAmount = calculateRefund(bookingTour.getDepositamount(), daysBeforeTour);
//        bookingTour.setRefundamount(refundAmount);
//        bookingTour.setRefunddate(Timestamp.valueOf(LocalDateTime.now()));
//        bookingTour.setStatus("Waiting Refund");
//        bookingTour.setReason(cancelDTO.getReasson());
//        bookingTour.setDepositrefund(true);
//
//        bookingTourRepository.save(bookingTour);
//
//        return "Booking canceled successfully. Refund amount: " + refundAmount;
//    }
//    public String cancelBookingEmployee(CancelDTO cancelDTO) {
//        BookingTour bookingTour = bookingTourRepository.findById(cancelDTO.getBookingTourId())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "bookingTourId not found with id " + cancelDTO.getBookingTourId()));
//
//        long daysBeforeTour = ChronoUnit.DAYS.between(LocalDate.now(),
//                bookingTour.getTourdate().toLocalDateTime().toLocalDate());
//
//        BigDecimal refundAmount = calculateRefund(bookingTour.getDepositamount(), daysBeforeTour);
//        bookingTour.setRefundamount(refundAmount);
//        bookingTour.setRefunddate(Timestamp.valueOf(LocalDateTime.now()));
//        bookingTour.setStatus("Refunded");
//        bookingTour.setReason(cancelDTO.getReasson());
//        bookingTour.setDepositrefund(true);
//
//        bookingTourRepository.save(bookingTour);
//
//        return "Booking canceled successfully. Refund amount: " + refundAmount;
//    }
//    private BigDecimal calculateRefund(BigDecimal depositAmount, long daysBeforeTour) {
//
//        BigDecimal refundPercentage;
//
//        if (daysBeforeTour >= 5) {
//            refundPercentage = BigDecimal.valueOf(0.95);
//        } else if (daysBeforeTour == 4) {
//            refundPercentage = BigDecimal.valueOf(0.90);
//        } else if (daysBeforeTour == 3) {
//            refundPercentage = BigDecimal.valueOf(0.85);
//        } else if (daysBeforeTour == 2) {
//            refundPercentage = BigDecimal.valueOf(0.80);
//        } else {
//            refundPercentage = BigDecimal.valueOf(0.75);
//        }
//
//        return depositAmount.multiply(refundPercentage);
//    }
//
//
//    public List<TourPackage> findTop10ByOrderByTotalBookingsDesc() {
//        List<TourPackage> top10Tours = bookingTourRepository.findAll().stream()
//                .collect(Collectors.groupingBy(BookingTour::getTourpackage, Collectors.counting()))
//                .entrySet().stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .limit(10)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
//
//        return top10Tours;
//    }
//
//    public Category createCategoryTour(Category categoryTour) {
//        return categoryTourRepository.save(categoryTour);
//    }
//
//    public Theme createThemeTour(Theme themeTour) {
//        return themeTourRepository.save(themeTour);
//    }
//
//    public Suitable createSuitableTour(Suitable suitableTour) {
//        return suitableRepository.save(suitableTour);
//    }
//
//    public Optional<Suitable> findSuitableById(int id) {
//        return suitableRepository.findById(id);
//    }
//
//    public Optional<Category> findCategoryById(int id) {
//        return categoryTourRepository.findById(id);
//    }
//
//    public Optional<Theme> findThemeById(int id) {
//        return themeTourRepository.findById(id);
//    }
//
//    public Suitable saveSuitableTour(Suitable suitableTour) {
//        if (doesSuitableTourNameExist(suitableTour.getSuitableName(), suitableTour.getSuitableId())) {
//            throw new IllegalArgumentException("SuitableTour name already exists.");
//        }
//
//        return suitableRepository.save(suitableTour);
//    }
//
//    private boolean doesSuitableTourNameExist(String name, int excludeId) {
//        return suitableTourRepository.findBySuitableNameAndSuitableTourIdNot(name, excludeId).isPresent();
//    }
//
//    public Category saveCategory(Category categoryTour) {
//        Optional<Category> existingCategory = categoryTourRepository.findById(categoryTour.getCategoryId());
//        if (!existingCategory.isPresent()) {
//            throw new IllegalArgumentException("CategoryTour not found for update.");
//        }
//        if (doesCategoryTourNameExist(categoryTour.getCategoryName(), categoryTour.getCategoryTourId())) {
//            throw new IllegalArgumentException("CategoryTour name already exists.");
//        }
//        return categoryTourRepository.save(categoryTour);
//    }
//
//    private boolean doesCategoryTourNameExist(String name, int excludeId) {
//        return categoryTourRepository.findByCategoryTourNameAndCategoryTourIdNot(name, excludeId).isPresent();
//    }
//
//    public Theme saveTheme(Theme themeTour) {
//        Optional<Theme> existingTheme = themeTourRepository.findById(themeTour.getThemeId());
//        if (!existingTheme.isPresent()) {
//            throw new IllegalArgumentException("ThemeTour not found for update.");
//        }
//        if (doesThemeTourNameExist(themeTour.getThemeName(), themeTour.getThemeId())) {
//            throw new IllegalArgumentException("ThemeTour name already exists.");
//        }
//        return themeTourRepository.save(themeTour);
//    }
//
//    private boolean doesThemeTourNameExist(String name, int excludeId) {
//        return themeTourRepository.findByThemeTourNameAndThemeTourIdNot(name, excludeId).isPresent();
//    }
//
//    public List<TourPackage> filterTourPackages(TourPackageFilterDTO filterDTO) {
//        List<TourPackage> allTourPackages = tourPackageRepository.findAll();
//        BigDecimal budget = filterDTO.getBudget() != null && !filterDTO.getBudget().isEmpty()
//                ? new BigDecimal(filterDTO.getBudget())
//                : null;
//        return allTourPackages.stream()
//                .filter(pkg -> filterDTO.getCategoryTourId() == null || filterDTO.getCategoryTourId().isEmpty() ||
//                        pkg.getCategoryTours().stream()
//                                .anyMatch(c -> filterDTO.getCategoryTourId().contains(c.getCategoryTourId())))
//                .filter(pkg -> filterDTO.getThemeTourId() == null || filterDTO.getThemeTourId().isEmpty() ||
//                        pkg.getThemeTours().stream()
//                                .anyMatch(t -> filterDTO.getThemeTourId().contains(t.getThemeId())))
//                .filter(pkg -> filterDTO.getSuitableTourId() == null || filterDTO.getSuitableTourId().isEmpty() ||
//                        pkg.getSuitableTours().stream()
//                                .anyMatch(s -> filterDTO.getSuitableTourId().contains(s.getSuitableTourId())))
//                .filter(pkg -> budget == null || pkg.getPrice().compareTo(budget) <= 0)
//                .collect(Collectors.toList());
//    }
//
//    public List<BookingTour> getListBookingByUser(int userId) {
//        return bookingTourRepository.findByUserid(userId);
//    }
//
//     public List<BookingTour> getBookingTour() {
//     return bookingTourRepository.findAll();
//     }
//    public List<BookingTourFetch> getAllBookingTours() {
//        List<BookingTour> bookings = bookingTourRepository.findAll();
//        return bookings.stream()
//        .filter(booking -> !booking.isDepositrefund())
//        .map(booking -> {
//            User user = userRepository.findById(booking.getUserid()).orElse(null);
//            UserFetch userFetch = new UserFetch();
//
//            userFetch.setFirstname(user.getFirstname());
//            userFetch.setLastname(user.getLastname());
//            userFetch.setPhone(user.getPhone());
//
//            BookingTourFetch bookingTourFetch = new BookingTourFetch();
//            bookingTourFetch.setBookingTourId(booking.getBookingtourid());
//            bookingTourFetch.setTourTitle(booking.getTourpackage().getTitle()); // Tên tiêu đề tour
//            bookingTourFetch.setUser(userFetch);
//            bookingTourFetch.setStatus(booking.getStatus());
//            bookingTourFetch.setTotalAmount(booking.getTotalprice());
//            bookingTourFetch.setBookingDate(booking.getBookingdate());
//            return bookingTourFetch;
//        }).collect(Collectors.toList());
//    }
//
//    public List<RefundFetch> getAllRefunds() {
//        List<BookingTour> bookings = bookingTourRepository.findAll();
//
//        return bookings.stream()
//                .filter(BookingTour::isDepositrefund)
//                .map(bookingTour -> {
//                    User user = userRepository.findById(bookingTour.getUserid()).orElse(null);
//                    UserFetch userFetch = new UserFetch();
//                    if (user != null) {
//                        userFetch.setFirstname(user.getFirstname());
//                        userFetch.setLastname(user.getLastname());
//                        userFetch.setPhone(user.getPhone());
//                    }
//                    RefundFetch refundFetch = new RefundFetch();
//                    refundFetch.setBookingTourId(bookingTour.getBookingtourid());
//                    refundFetch.setTourTitle(bookingTour.getTourpackage().getTitle());
//                    refundFetch.setUser(userFetch);
//                    refundFetch.setStatus(bookingTour.getStatus());
//                    refundFetch.setReason(bookingTour.getReason());
//                    refundFetch.setRefundAmount(bookingTour.getRefundamount());
//                    refundFetch.setRefundDate(bookingTour.getRefunddate());
//
//                    return refundFetch; // Return RefundFetch object
//                })
//                .collect(Collectors.toList());
//    }
//
//
//    public boolean deleteThemeTour(int id) {
//        try {
//            themeTourRepository.deleteById(id);
//            return true;
//        } catch (DataIntegrityViolationException ex) {
//             Handle the exception and return false if there's a foreign key constraint
//             violation
//            return false;
//        }
//    }
//
//    public boolean deleteCategoryTour(int id) {
//        try {
//            categoryTourRepository.deleteById(id);
//            return true;
//        } catch (DataIntegrityViolationException ex) {
//             Handle the exception and return false if there's a foreign key constraint
//             violation
//            return false;
//        }
//    }
//
//    public boolean deleteSuitable(int id) {
//        try {
//            suitableRepository.deleteById(id);
//            return true;
//        } catch (DataIntegrityViolationException ex) {
//             Handle the exception and return false if there's a foreign key constraint
//             violation
//            return false;
//        }
//    }
//
//    public String eployeeSaveBookingTour(BookingTourDTO bookingTourDTO) {
//        BookingTour bookingTour = new BookingTour();
//
//         Retrieve TourPackage
//        TourPackage tourPackage = tourPackageRepository.findById(bookingTourDTO.getPackageId())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Tour Package not found with id " + bookingTourDTO.getPackageId()));
//
//         Retrieve Payment
//        Payment payment = paymentRepository.findById(bookingTourDTO.getPaymentId())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Payment not found with id " + bookingTourDTO.getPaymentId()));
//
//         Set booking details
//        bookingTour.setUserid(bookingTourDTO.getUserId());
//        bookingTour.setTourpackage(tourPackage);
//        bookingTour.setPayment(payment);
//        LocalDateTime now = LocalDateTime.now();
//        Timestamp timestamp = Timestamp.valueOf(now);
//        bookingTour.setBookingdate(timestamp);
//       bookingTour.setTourdate(convertToTimestamp(bookingTourDTO.getTourDate()));
//        bookingTour.setNumberofpeople(bookingTourDTO.getNumberOfPeople());
//        bookingTour.setTotalprice(bookingTourDTO.getTotalPrice());
//        bookingTour.setDepositamount(bookingTourDTO.getDepositAmount());
//        bookingTour.setStatus("Waiting");
//        bookingTour.setRefundamount(null);
//        bookingTour.setRefunddate(null);
//        bookingTour.setReason(null);
//        bookingTour.setDepositrefund(false);
//        return "Booking tour successfully";
//    }
//
//    public Optional<BookingTour> getBookingTourById(int bookingTourId) {
//        return bookingTourRepository.findById(bookingTourId);
//    }
//
//    public BookingTour saveBookingTour(BookingTour bookingTour) {
//        return bookingTourRepository.save(bookingTour);
//    }
//
//    public List<TourPackage> findByTitle(String title) {
//        return tourPackageRepository.findByTitleContainingIgnoreCase(title);
//    }
//
//
//
//
//    public List<TourPackage> searchTourPackages(String title, Integer minPrice, Integer maxPrice, Integer categoryId, Integer themeId, Integer suitableId) {
//        return tourPackageRepository.findByCriteria(title, minPrice, maxPrice, categoryId, themeId, suitableId);
//    }
}







